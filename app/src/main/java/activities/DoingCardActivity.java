package activities;

import android.app.Activity;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import custom_views.PomodoroCountDownView;
import models.DoingCard;
import services.UserService;
import utilities.PomodoroApp;
import utilities.notifications.ScheduleNotifications;
import utilities.ui.CustomAlert;

@EActivity(R.layout.doing_card_activity)
public class DoingCardActivity extends Activity {
    @App protected PomodoroApp mApp;
    @Bean protected UserService mUserService;
    @Bean protected CustomAlert mCustomAlert;
    @Bean protected ScheduleNotifications mScheduleNotifications;
    @ViewById protected PomodoroCountDownView tv_countdown;
    @ViewById protected TextView tv_title;
    private DoingCard mDoingCard;

    @AfterViews protected void initViews() {
        String idDoingCard = getIntent().getExtras().getString(ScheduleNotifications.ID_CARD_DOING);
        if (idDoingCard == null) return;

        mDoingCard = mUserService.isThisDoingCardStillValid(idDoingCard);
        if(mDoingCard != null) populateFields();
    }

    private void populateFields() {
        tv_countdown.setCountDownValueInMilliseconds(mDoingCard);
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

    @Override public void onBackPressed() {
        mScheduleNotifications.setFor(mDoingCard);
        super.onBackPressed();
    }

    @Override protected void onResume() {
        super.onResume();
        mApp.setCardDoingActivity(this);
    }

    @Override protected void onPause() {
        mApp.setCardDoingActivity(null);
        super.onPause();
    }

    @Override protected void onDestroy() {
        mApp.setCardDoingActivity(null);
        super.onDestroy();
    }

}
