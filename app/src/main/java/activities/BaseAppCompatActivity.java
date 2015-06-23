package activities;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.lang.reflect.Field;

@EActivity
public class BaseAppCompatActivity extends AppCompatActivity {
    @ViewById protected Toolbar toolbar;

    protected void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        hackDrawerTintColor();
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
