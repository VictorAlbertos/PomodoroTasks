package activities;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringRes;

import utilities.ui.CustomAlert;

@EActivity(R.layout.config_activity)
public class InitialConfigActivity extends BaseAppCompatActivity {
    @StringRes protected String welcome, info_config;
    @Bean protected CustomAlert customAlert;

    @Override protected void initViews() {
        super.initViews();
        customAlert.showOneChoice(this, welcome, info_config);
    }

    @StringRes protected String config_account;
    @Override protected String titleToolbar() {
        return config_account;
    }
}
