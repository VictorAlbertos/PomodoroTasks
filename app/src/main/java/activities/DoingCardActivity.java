package activities;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import adapters.TypeActionSpinnerAdapter;
import custom_views.ActionCountDownView;
import de.greenrobot.event.EventBus;
import fr.ganfra.materialspinner.MaterialSpinner;
import info.hoang8f.widget.FButton;
import mehdi.sakout.fancybuttons.FancyButton;
import models.Card;
import models.DoingCard;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.TrelloApiDataService;
import services.UserService;
import utilities.EventTask;
import utilities.notifications.ScheduleNotifications;
import utilities.ui.Animations;
import utilities.ui.CustomAlert;
import utilities.ui.CustomToast;
import utilities.ui.Sounds;

import static models.DoingCard.Action.Type;


@EActivity(R.layout.doing_card_activity)
public class DoingCardActivity extends BaseAppCompatActivity {
    @Bean protected CustomAlert mCustomAlert;
    @Bean protected CustomToast mCustomToast;
    @Bean protected ScheduleNotifications mScheduleNotifications;
    @Bean protected TypeActionSpinnerAdapter mAdapter;
    @Bean protected TrelloApiDataService mApiDataService;
    @Bean protected UserService mUserService;
    @Bean protected Animations mAnimations;
    @Bean protected Sounds mSounds;
    @ViewById protected ViewGroup vg_no_active_action, vg_active_action;
    @ViewById protected ActionCountDownView countdown_view;
    @ViewById protected FancyButton bt_play_pause;
    @ViewById protected MaterialSpinner sp_actions;
    @ViewById protected TextView tv_n_spent_time, tv_n_pomodoros, tv_n_long_breaks, tv_n_short_breaks;
    @ViewById protected FButton bt_spend_action;
    @StringRes protected String task_moved_to_done_list, task_moved_to_todo_list,
                                    error_connection, validation_empty_field, spent_time_added;
    private DoingCard mDoingCard;
    private Type mActionType;

    @Override protected void initViews() {
        super.initViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpSpinner();
        setUpCountDownListener();

        String idDoingCard = getIntent().getExtras().getString(ScheduleNotifications.ID_CARD_DOING);
        if (idDoingCard == null) return;

        mDoingCard = mUserService.isThisDoingCardStillValid(idDoingCard);
        if(mDoingCard != null) update();
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
        countdown_view.setCountDownListener(() -> {
            update();
            mSounds.play(R.raw.alarm_sound);
        });
    }

    private void update() {
        countdown_view.bind(mDoingCard);

        mUserService.persistsChanges();
        super.tv_title.setText(mDoingCard.getName());

        int resImage = mDoingCard.isPaused() ? R.drawable.ic_play_arrow_white_24dp : R.drawable.ic_pause_white_24dp;
        bt_play_pause.setIconResource(resImage);

        tv_n_spent_time.setText(mDoingCard.getSpentTime());
        tv_n_pomodoros.setText(mDoingCard.getNPomodoros());
        tv_n_long_breaks.setText(mDoingCard.getNLongBreaks());
        tv_n_short_breaks.setText(mDoingCard.getNShortBreaks());


        if (mDoingCard.isCurrentActionEnd()) {
            vg_active_action.setVisibility(View.GONE);
            vg_no_active_action.setVisibility(View.VISIBLE);
        } else {
            vg_active_action.setVisibility(View.VISIBLE);
            vg_no_active_action.setVisibility(View.GONE);
        }
    }

    @Click protected void bt_play_pause() {
        mDoingCard.setPause(!mDoingCard.isPaused());
        update();
    }

    @Click protected void bt_reset() {
        String addedTime = mDoingCard.resetCurrentAction();
        String message = spent_time_added.replaceFirst("__", addedTime);
        mCustomToast.showToast(message);

        update();
    }

    @StringRes protected String performance, spent_time, pomodoros, long_breaks, short_breaks;
    @Click protected void bt_move_to_done_list() {
        String performanceTask = mDoingCard.getPerformance(performance,
                spent_time, pomodoros, long_breaks, short_breaks);

        mApiDataService.moveCardToDoneList(performanceTask, mDoingCard, new Callback<Card>() {
            @Override
            public void success(Card card, Response response) {
                EventBus.getDefault().post(EventTask.TABS_LISTS_FRAGMENT_MOVE_TO_DONE_LIST);
                EventBus.getDefault().post(EventTask.TABS_LISTS_UPDATE_DATA_SOURCE);

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
                EventBus.getDefault().post(EventTask.TABS_LISTS_FRAGMENT_MOVE_TO_TODO_LIST);
                EventBus.getDefault().post(EventTask.TABS_LISTS_UPDATE_DATA_SOURCE);

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
        update();
    }

    @Override public void onBackPressed() {
        if (!isTaskRoot()) {
            EventBus.getDefault().post(EventTask.LIST_DOING_FRAGMENT_UPDATE_COUNTDOWN);
            super.onBackPressed();
            return;
        }

        MainActivity_.intent(this)
                .flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
        finish();
    }

    @Override protected void onPause() {
        mScheduleNotifications.setFor(mDoingCard);
        countdown_view.cancelCountDown();
        super.onPause();
    }

    @Override protected void onResume() {
        countdown_view.restartCountDown();
        super.onResume();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override protected String titleToolbar() {
        return "";
    }

    public boolean isAlreadyPresent(DoingCard doingCard) {
        return mDoingCard.getId().equals(doingCard.getId());
    }
}
