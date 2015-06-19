package activities;

import android.support.v4.app.FragmentActivity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import services.UserService;

@EActivity
public class LaunchActivity extends FragmentActivity {
    @Bean protected UserService mUserService;

    @AfterInject protected void init() {
        if (!mUserService.isAuthenticate()) LoginActivity_.intent(this).start();
        else if (!mUserService.isAccountConfigured()) ConfigActivity_.intent(this).start();
        else MainActivity_.intent(this).start();
    }

}
