package activities;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringRes;

@EActivity(R.layout.main_activity)
public class MainActivity extends AppCompatBaseActivity {
    @StringRes protected String app_name;

    @AfterViews protected void initViews() {
        super.initViews(app_name);
    }
}
