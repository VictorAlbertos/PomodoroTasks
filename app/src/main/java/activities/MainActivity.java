package activities;

import android.view.Menu;
import android.view.MenuItem;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringRes;

import utilities.ui.Animations;

@EActivity(R.layout.main_activity)
public class MainActivity extends BaseAppCompatActivity {
    @Bean protected Animations mAnimations;

    @AfterViews protected void initViews() {
        super.init();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.config) ConfigActivity_.intent(this).start();
        return super.onOptionsItemSelected(item);
    }

    @StringRes protected String app_name;
    @Override protected String titleToolbar() {
        return app_name;
    }
}
