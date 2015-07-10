package custom_views;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

import models.DoingCard;
import utilities.CountDownTimer;

@EViewGroup()
public abstract class ActionCountDownView extends FrameLayout {
    @StringRes protected String time_over;
    @ViewById protected TextView tv_time_running;
    @ViewById protected ImageView iv_icon;
    @ViewById protected ProgressWheel pw_countdown, pw_countdown_base;
    private DoingCard mDoingCard;
    private CountDownTimer mCountDownTimer;
    private CountDownListener mCountDownListener;

    @AfterViews protected void initViews() {
        pw_countdown_base.setInstantProgress(1);
    }

    public ActionCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(DoingCard doingCar) {
        mDoingCard = doingCar;
        restartCountDown();
    }

    public void setCountDownListener(CountDownListener countDownListener) {
        mCountDownListener = countDownListener;
    }

    public void restartCountDown() {
        if (mDoingCard.isPaused()) showPauseMessage();
        else if (mDoingCard.isCurrentActionEnd()) {
            tv_time_running.setText(time_over);
            pw_countdown.setInstantProgress(mDoingCard.getCurrentProgress());
        }
        else setUpCountDown();

        iv_icon.setImageResource(mDoingCard.getResourceIcon());
    }

    private void setUpCountDown() {
        if (mCountDownTimer != null) mCountDownTimer.cancel();

        mCountDownTimer = new CountDownTimer(mDoingCard.getRemainingTimeInMilliseconds(), 1000) {
            @Override public void onTick(long millisUntilFinished) {
                if (mDoingCard.isPaused()) {
                    showPauseMessage();
                    cancelCountDown();
                } else {
                    pw_countdown.setInstantProgress(mDoingCard.getCurrentProgress());
                    tv_time_running.setText(getFormattedTime(millisUntilFinished));
                }
            }

            @Override public void onFinish() {
                pw_countdown.setInstantProgress(mDoingCard.getCurrentProgress());
                tv_time_running.setText(time_over);
                if (mCountDownListener != null) mCountDownListener.onFinish();
            }
        }.start();
    }

    public void cancelCountDown() {
        if (mCountDownTimer != null) mCountDownTimer.cancel();
    }

    private void showPauseMessage() {
        pw_countdown.setInstantProgress(mDoingCard.getCurrentProgress());
        tv_time_running.setText(getFormattedTime(mDoingCard.getTimeRemainingAtPause()));
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
