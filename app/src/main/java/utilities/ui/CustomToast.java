package utilities.ui;

import android.graphics.PorterDuff;
import android.widget.LinearLayout;

import com.github.johnpersano.supertoasts.SuperToast;
import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.ColorRes;

import java.lang.reflect.Field;

import utilities.PomodoroApp;

@EBean
public class CustomToast {
    @App protected PomodoroApp app;
    @ColorRes(R.color.light_gray) protected int colorBackground;
    @ColorRes(R.color.red) protected int colorText;

    public void showToast(String message) {
        if (message.isEmpty()) return;

        SuperToast superToast = new SuperToast(app);
        superToast.setTextColor(colorText);
        setBackgroundColor(superToast);
        superToast.setText(message);
        superToast.setDuration(SuperToast.Duration.MEDIUM);
        superToast.setAnimations(SuperToast.Animations.FLYIN);
        superToast.show();
    }

    private void setBackgroundColor(SuperToast superToast) {
        try {
            superToast.setBackground(SuperToast.Background.WHITE);
            Field field = SuperToast.class.getDeclaredField("mRootLayout");
            field.setAccessible(true);
            LinearLayout mRootLayout = (LinearLayout) field.get(superToast);
            mRootLayout.getBackground().setColorFilter(colorBackground, PorterDuff.Mode.MULTIPLY);
        } catch (Exception exception) {exception.printStackTrace();}
    }
}
