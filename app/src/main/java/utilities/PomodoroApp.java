package utilities;

import android.app.Activity;
import android.app.Application;

import org.androidannotations.annotations.EApplication;

@EApplication
public class PomodoroApp extends Application {
    //Tracking current activity to manage notifications
    private Activity mCurrentActivity = null;

    public boolean isAnyActivityRunning(){
        return mCurrentActivity != null;
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }
}
