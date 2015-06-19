package activities;

import android.support.v4.app.FragmentActivity;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringRes;

import utilities.ui.CustomAlert;

@EActivity(R.layout.config_activity)
public class ConfigActivity extends FragmentActivity {
    @StringRes protected String welcome, info_config;
    @Bean protected CustomAlert customAlert;

    @AfterViews protected void initViews() {
        customAlert.showOneChoice(this, welcome, info_config);
    }

}
