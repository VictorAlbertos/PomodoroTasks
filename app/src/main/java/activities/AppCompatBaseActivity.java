package activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public class AppCompatBaseActivity extends AppCompatActivity {
    @ViewById protected Toolbar toolbar;
    protected void initViews() {
        if (toolbar != null) setSupportActionBar(toolbar);
    }
}
