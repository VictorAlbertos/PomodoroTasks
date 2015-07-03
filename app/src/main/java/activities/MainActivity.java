package activities;

import android.view.Menu;
import android.view.MenuItem;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringRes;

@EActivity(R.layout.main_activity)
public class MainActivity extends BaseAppCompatActivity {
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
