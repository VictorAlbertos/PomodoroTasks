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
    @StringRes protected String time_remaining_pomodoro, time_over_pomodoro, time_remaining_long_break,
                            time_over_long_break, time_remaining_short_break, time_over_short_break, paused;
    public ActionCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private DoingCard mDoingCard;
    private CountDownTimer mCountDownTimer;
    private CountDownListener mCountDownListener;

    public ActionCountDownView bind(DoingCard doingCar) {
        mDoingCard = doingCar;
        restartCountDown();
        return this;
    }

    public void setCountDownListener(CountDownListener countDownListener) {
        mCountDownListener = countDownListener;
    }

    private void restartCountDown() {
        mCountDownTimer = new CountDownTimer(mDoingCard.getRemainingTimeInMilliseconds(), 1000) {
            @Override public void onTick(long millisUntilFinished) {

                DoingCard.Action.Type type = DoingCard.Action.Type.Pomodoro;

                String typeString = time_remaining_pomodoro;
                if (type == DoingCard.Action.Type.LongBreak) typeString = time_remaining_long_break;
                else if (type == DoingCard.Action.Type.ShortBreak) typeString = time_remaining_short_break;

                if (!mDoingCard.isPause()) {
                    ActionCountDownView.this.setText(typeString + " " + getFormattedTime(millisUntilFinished));
                } else {
                    millisUntilFinished = mDoingCard.getTimeRemainingWhenThisWasPause();
                    if (mCountDownTimer != null) mCountDownTimer.cancel();
                    ActionCountDownView.this.setText(typeString + " " + getFormattedTime(millisUntilFinished) + paused);
                }
            }

            @Override public void onFinish() {
                DoingCard.Action.Type type = DoingCard.Action.Type.Pomodoro;

                String typeString = time_over_pomodoro;
                if (type == DoingCard.Action.Type.LongBreak) typeString = time_over_long_break;
                else if (type == DoingCard.Action.Type.ShortBreak) typeString = time_over_short_break;

                ActionCountDownView.this.setText(typeString);
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
