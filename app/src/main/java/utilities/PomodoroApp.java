package utilities;

import android.app.Application;

import org.androidannotations.annotations.EApplication;

import activities.DoingCardActivity;

@EApplication
public class PomodoroApp extends Application {
    //Track if CardDoingActivity is the current screen
    private DoingCardActivity mDoingCardActivity = null;

    public boolean isCardDoingActivityRunning(){
        return mDoingCardActivity != null;
    }

    public DoingCardActivity getCardDoingActivity() {
        return mDoingCardActivity;
    }

    public void setCardDoingActivity(DoingCardActivity mDoingCardActivity) {
        this.mDoingCardActivity = mDoingCardActivity;
    }
}
