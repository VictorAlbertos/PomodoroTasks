package utilities;

import android.app.Application;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import services.UserService;

@EApplication
public class PomodoroApp extends Application {
    @Bean protected UserService mUserService;

    @Override public void onCreate() {
        super.onCreate();
        //mUserService.destroySession();
    }
}
