package utilities.ui;

import android.graphics.PorterDuff;
import android.widget.LinearLayout;

import com.github.johnpersano.supertoasts.SuperToast;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;

import java.lang.reflect.Field;

import utilities.PomodoroApp;

@EBean
public class CustomToast {
    @App protected PomodoroApp app;
    @ColorRes protected int background_toast;
    @ColorRes protected int texts_toast;

    @StringRes protected String error_connection;
    public void showErrorConnection() {
        showToast(error_connection);
    }

    public void showToast(String message) {
        if (message.isEmpty()) return;

        SuperToast superToast = new SuperToast(app);
        superToast.setTextColor(texts_toast);
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
            mRootLayout.getBackground().setColorFilter(background_toast, PorterDuff.Mode.MULTIPLY);
        } catch (Exception exception) {exception.printStackTrace();}
    }
}
