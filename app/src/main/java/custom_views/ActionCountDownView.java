package custom_views;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

import models.DoingCard;

@EView
public class ActionCountDownView extends TextView {
    @StringRes protected String time_over, paused;
    public ActionCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private DoingCard mDoingCard;
    private CountDownTimer mCountDownTimer;
    private CountDownListener mCountDownListener;

    public void bind(DoingCard doingCar) {
        mDoingCard = doingCar;
        restartCountDown();
    }

    public void setCountDownListener(CountDownListener countDownListener) {
        mCountDownListener = countDownListener;
    }

    private void restartCountDown() {
        if (mCountDownTimer !=null) mCountDownTimer.cancel();
        mCountDownTimer = new CountDownTimer(mDoingCard.getRemainingTimeInMilliseconds(), 1000) {
            @Override public void onTick(long millisUntilFinished) {
                if (!mDoingCard.isPause()) {
                    ActionCountDownView.this.setText(getFormattedTime(millisUntilFinished));
                } else {
                    millisUntilFinished = mDoingCard.getTimeRemainingWhenThisWasPause();
                    if (mCountDownTimer != null) mCountDownTimer.cancel();
                    ActionCountDownView.this.setText(getFormattedTime(millisUntilFinished) + " " + paused);
                }
            }

            @Override public void onFinish() {
                ActionCountDownView.this.setText(time_over);
                if (mCountDownListener != null) mCountDownListener.onFinish();
            }
        }.start();
    }

    private String getFormattedTime(long millisUntilFinished) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));
    }

    public interface CountDownListener {
        void onFinish();
    }
}
