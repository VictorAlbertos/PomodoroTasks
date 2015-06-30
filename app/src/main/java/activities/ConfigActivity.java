package activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringRes;

import services.UserService;
import utilities.ui.CustomAlert;

@EActivity(R.layout.config_activity)
public class ConfigActivity extends BaseAppCompatActivity {
    @Bean protected UserService mUserService;
    @Bean protected CustomAlert mCustomAlert;
    @StringRes protected String config_account, logout, ask_to_logout;

    @AfterViews protected void initViews() {
        super.init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override protected String titleToolbar() {
        return config_account;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        else if (item.getItemId() == R.id.logout) askToLogout();
        return super.onOptionsItemSelected(item);
    }

    private void askToLogout() {
        mCustomAlert.showTwoChoices(this, logout, ask_to_logout, new MaterialDialog.ButtonCallback() {
            @Override public void onPositive(MaterialDialog dialog) {
                mUserService.destroySession();
                LaunchActivity_.intent(ConfigActivity.this)
                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .start();
            }
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_config, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
