package utilities.ui;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.androidannotations.annotations.EBean;

import info.hoang8f.widget.FButton;

@EBean
public class Animations {
    private final static int DURATION = 500;

    public void shakeValidation(FButton button) {
        YoYo.with(Techniques.Shake)
                .delay(100)
                .duration(DURATION)
                .playOn(button);
    }
}
