package activities;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringRes;

import utilities.ui.CustomAlert;

@EActivity(R.layout.config_activity)
public class ConfigActivity extends AppCompatBaseActivity {
    @StringRes protected String config_account, welcome, info_config;
    @Bean protected CustomAlert customAlert;

    @AfterViews protected void initViews() {
        super.initViews(config_account);
        customAlert.showOneChoice(this, welcome, info_config);
    }

}
