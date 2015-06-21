package utilities.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.androidannotations.annotations.EBean;

import info.hoang8f.widget.FButton;

@EBean
public class Animations {
    private final static int DURATION = 500;
    public final static float DEFAULT_VALUE_INTERPOLATOR = 2.0f;


    public void shakeValidation(FButton button) {
        YoYo.with(Techniques.Shake)
                .delay(100)
                .duration(DURATION)
                .playOn(button);
    }
    public void alpha(View view, float to) {
        animatePropertyTo(view, "alpha", to, 0, DURATION, DEFAULT_VALUE_INTERPOLATOR);
    }

    public void animatePropertyTo(View view, String property, float to, long delay,
                                  int duration, float interpolator) {
        ValueAnimator animation = ObjectAnimator.ofFloat(view, property, to);
        animation.setStartDelay(delay);
        animation.setInterpolator(new DecelerateInterpolator(interpolator));
        animation.setDuration(duration).start();
    }
}
