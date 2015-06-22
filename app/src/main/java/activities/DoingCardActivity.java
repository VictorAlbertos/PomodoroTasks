package activities;

import android.support.v7.app.AppCompatActivity;
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
public class DoingCardActivity extends AppCompatActivity {
    @App protected PomodoroApp mApp;
    @Bean protected UserService mUserService;
    @Bean protected CustomAlert mCustomAlert;
    @ViewById protected PomodoroCountDownView tv_countdown;
    @ViewById protected TextView tv_title;

    @AfterViews protected void initViews() {
        String idDoingCard = getIntent().getExtras().getString(ScheduleNotifications.ID_CARD_DOING);
        if (idDoingCard == null) return;

        DoingCard doingCard = mUserService.isThisDoingCardStillValid(idDoingCard);
        if(doingCard!= null) populateFields(doingCard);
    }

    private void populateFields(DoingCard doingCard) {
        tv_countdown.setCountDownValueInMilliseconds(doingCard);

    }

    public void fromNotificationAskToSwitchTo(final DoingCard doingCard, String title, String text) {
        mCustomAlert.showTwoChoices(this, title, text, new MaterialDialog.ButtonCallback() {
            @Override public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                populateFields(doingCard);
            }
        });
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
