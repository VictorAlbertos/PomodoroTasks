package activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public class AppCompatBaseActivity extends AppCompatActivity {
    @ViewById protected Toolbar toolbar;
    @ViewById protected TextView tv_title;

    protected void initViews(String titleActivity) {
        setSupportActionBar(toolbar);
        tv_title.setText(titleActivity);
    }
}
