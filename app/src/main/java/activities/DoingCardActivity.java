package activities;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import adapters.TypeActionSpinnerAdapter;
import custom_views.ActionCountDownView;
import fr.ganfra.materialspinner.MaterialSpinner;
import info.hoang8f.widget.FButton;
import models.Card;
import models.DoingCard;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.TrelloApiDataService;
import services.UserService;
import utilities.PomodoroApp;
import utilities.notifications.ScheduleNotifications;
import utilities.ui.Animations;
import utilities.ui.CustomAlert;
import utilities.ui.CustomToast;
import utilities.ui.Sounds;

import static models.DoingCard.Action.Type;


@EActivity(R.layout.doing_card_activity)
public class DoingCardActivity extends BaseAppCompatActivity {
    @App protected PomodoroApp mApp;
    @Bean protected CustomAlert mCustomAlert;
    @Bean protected CustomToast mCustomToast;
    @Bean protected ScheduleNotifications mScheduleNotifications;
    @Bean protected TypeActionSpinnerAdapter mAdapter;
    @Bean protected TrelloApiDataService mApiDataService;
    @Bean protected UserService mUserService;
    @Bean protected Animations mAnimations;
    @Bean protected Sounds mSounds;
    @ViewById protected ViewGroup ll_active_action, ll_no_active_action;
    @ViewById protected ActionCountDownView tv_countdown;
    @ViewById protected TextView tv_title;
    @ViewById protected Button bt_play_pause;
    @ViewById protected MaterialSpinner sp_actions;
    @ViewById protected ImageView iv_icon;
    @ViewById protected TextView tv_n_spent_time, tv_n_pomodoros, tv_n_long_breaks, tv_n_short_breaks;
    @ViewById protected FButton bt_spend_action;
    @StringRes protected String play, pause, task_moved_to_done_list, task_moved_to_todo_list,
                                    error_connection, validation_empty_field;
    private DoingCard mDoingCard;
    private Type mActionType;

    @AfterViews protected void initViews() {
        super.init();

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setUpSpinner();
        setUpCountDownListener();

        String idDoingCard = getIntent().getExtras().getString(ScheduleNotifications.ID_CARD_DOING);
        if (idDoingCard == null) return;

        mDoingCard = mUserService.isThisDoingCardStillValid(idDoingCard);
        if(mDoingCard != null) update(false);
    }

    private void setUpSpinner() {
        sp_actions.setAdapter(mAdapter);
        sp_actions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == -1) mActionType = null;
                else mActionType = mAdapter.getItem(position);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setUpCountDownListener() {
        tv_countdown.setCountDownListener(new ActionCountDownView.CountDownListener() {
            @Override public void onFinish() {
                update(true);
                mSounds.play(R.raw.alarm_sound);
            }
        });
    }

    private void update(boolean isCalledFromCountDownOnFinish) {
        if (!isCalledFromCountDownOnFinish && !mDoingCard.isCurrentActionEnd())
            tv_countdown.bind(mDoingCard);

        mUserService.persistsChanges();
        tv_title.setText(mDoingCard.getName());

        String textButton = mDoingCard.isPause() ? play : pause;
        bt_play_pause.setText(textButton);

        tv_n_spent_time.setText(mDoingCard.getSpentTime());
        tv_n_pomodoros.setText(mDoingCard.getNPomodoros());
        tv_n_long_breaks.setText(mDoingCard.getNLongBreaks());
        tv_n_short_breaks.setText(mDoingCard.getNShortBreaks());

        iv_icon.setImageResource(mDoingCard.getResourceIcon());

        if (mDoingCard.isCurrentActionEnd()) {
            ll_active_action.setVisibility(View.GONE);
            ll_no_active_action.setVisibility(View.VISIBLE);

        } else {
            ll_active_action.setVisibility(View.VISIBLE);
            ll_no_active_action.setVisibility(View.GONE);
        }
    }

    public void fromNotificationAskToSwitchTo(final DoingCard doingCard, String title, String text) {
        if (doingCard.getId().equals(mDoingCard.getId())) return;

        mCustomAlert.showTwoChoices(this, title, text, new MaterialDialog.ButtonCallback() {
            @Override public void onPositive(MaterialDialog dialog) {
                mScheduleNotifications.setFor(mDoingCard);
                mDoingCard = doingCard;
            }
        });
    }

    @Click protected void bt_play_pause() {
        mDoingCard.setPause(!mDoingCard.isPause());
        update(false);
    }

    @Click protected void bt_stop() {
        mDoingCard.resetCurrentAction();
        update(false);
    }

    @StringRes protected String performance, spent_time, pomodoros, long_breaks, short_breaks;
    @Click protected void bt_move_to_done_list() {
        String feedbackTask = performance + "\\";
        feedbackTask += spent_time + mDoingCard.getSpentTime();
        feedbackTask += pomodoros + mDoingCard.getNPomodoros();
        feedbackTask += long_breaks + mDoingCard.getNLongBreaks();
        feedbackTask += short_breaks + mDoingCard.getNShortBreaks();

        mApiDataService.moveCardToDoneList(feedbackTask, mDoingCard, new Callback<Card>() {
            @Override
            public void success(Card card, Response response) {
                String message = task_moved_to_done_list;
                mCustomToast.showToast(message.replaceFirst("__", mDoingCard.getName()));
                onBackPressed();
            }

            @Override
            public void failure(RetrofitError error) {
                mCustomToast.showToast(error_connection);
            }
        });
    }

    @Click protected void bt_back_to_todo_list() {
        mApiDataService.moveCardToTodoList(mDoingCard, new Callback<Card>() {
            @Override public void success(Card card, Response response) {
                String message = task_moved_to_todo_list;
                mCustomToast.showToast(message.replaceFirst("__", mDoingCard.getName()));
                onBackPressed();
            }

            @Override public void failure(RetrofitError error) {
                mCustomToast.showToast(error_connection);
            }
        });
    }

    @Click protected void bt_spend_action() {
        if (mActionType == null) {
            sp_actions.setError(validation_empty_field);
            mAnimations.shakeValidation(bt_spend_action);
            return;
        }

        mDoingCard.addNewAction(mActionType);
        update(false);
    }

    @Override public void onBackPressed() {
        mScheduleNotifications.setFor(mDoingCard);
        MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
        finish();
    }

    @Override protected void onResume() {
        mApp.setCardDoingActivity(this);
        super.onResume();
    }

    @Override protected void onPause() {
        mScheduleNotifications.setFor(mDoingCard);
        mApp.setCardDoingActivity(null);
        super.onPause();
    }

    @Override protected void onDestroy() {
        mApp.setCardDoingActivity(null);
        super.onDestroy();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
