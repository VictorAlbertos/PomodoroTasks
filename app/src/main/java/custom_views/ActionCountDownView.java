package custom_views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

import models.DoingCard;
import utilities.CountDownTimer;

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

    public void restartCountDown() {
        if (mDoingCard.isPause()) showPauseMessage();
        else if (mDoingCard.isCurrentActionEnd()) setText(time_over);
        else setUpCountDown();
    }

    private void setUpCountDown() {
        if (mCountDownTimer != null) mCountDownTimer.cancel();

        mCountDownTimer = new CountDownTimer(mDoingCard.getRemainingTimeInMilliseconds(), 1000) {
            @Override public void onTick(long millisUntilFinished) {
                if (mDoingCard.isPause()) {
                    showPauseMessage();
                    cancelCountDown();
                } else ActionCountDownView.this.setText(getFormattedTime(millisUntilFinished));
            }

            @Override public void onFinish() {
                ActionCountDownView.this.setText(time_over);
                if (mCountDownListener != null) mCountDownListener.onFinish();
            }
        }.start();
    }

    public void cancelCountDown() {
        if (mCountDownTimer != null) mCountDownTimer.cancel();
    }

    private void showPauseMessage() {
        setText(getFormattedTime(mDoingCard.getTimeRemainingAtPause()) + " " + paused);
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
