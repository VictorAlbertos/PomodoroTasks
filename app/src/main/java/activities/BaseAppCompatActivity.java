package activities;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.lang.reflect.Field;

import utilities.PomodoroApp;

@EActivity
public class BaseAppCompatActivity extends AppCompatActivity {
    @ViewById protected Toolbar toolbar;
    @App protected PomodoroApp mApp;

    protected void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @ColorRes(R.color.white) protected int color;
    private void hackDrawerTintColor() {
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Field field = Toolbar.class.getDeclaredField("mNavButtonView");
                    field.setAccessible(true);
                    ImageButton mNavButtonView = (ImageButton) field.get(toolbar);
                    mNavButtonView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
