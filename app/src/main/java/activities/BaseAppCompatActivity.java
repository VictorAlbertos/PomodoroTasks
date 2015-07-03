package activities;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.lang.reflect.Field;

import utilities.PomodoroApp;

@EActivity
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    @ViewById protected Toolbar toolbar;
    @App protected PomodoroApp mApp;
    @ViewById protected TextView tv_title;

    @AfterViews protected void initViews() {
        setSupportActionBar(toolbar);
        tv_title.setText(titleToolbar());
        hackDrawerTintColor();
    }

    @Override protected void onResume() {
        mApp.setCurrentActivity(this);
        super.onResume();
    }

    @Override protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = mApp.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) mApp.setCurrentActivity(null);
    }

    protected abstract String titleToolbar();

    @ColorRes(R.color.white) protected int color;
    private void hackDrawerTintColor() {
        toolbar.post(() -> {
            try {
                Field field = Toolbar.class.getDeclaredField("mNavButtonView");
                field.setAccessible(true);
                ImageButton mNavButtonView = (ImageButton) field.get(toolbar);
                mNavButtonView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
