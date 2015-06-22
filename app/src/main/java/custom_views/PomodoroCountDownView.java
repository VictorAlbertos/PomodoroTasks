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
public class PomodoroCountDownView extends TextView {
    @StringRes protected String time_remaining_pomodoro, time_over_pomodoro, time_remaining_long_break,
                            time_over_long_break, time_remaining_short_break, time_over_short_break ;
    public PomodoroCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCountDownValueInMilliseconds(final DoingCard doingCard) {
        new CountDownTimer(doingCard.getRemainingTimeInMilliseconds(), 1000) {

            @Override public void onTick(long millisUntilFinished) {
                String minutesString = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                String secondsString = seconds < 10 ? "0" + String.valueOf(seconds) : String.valueOf(seconds);

                DoingCard.Action.Type type = DoingCard.Action.Type.Pomodoro;

                String typeString = time_remaining_pomodoro;
                if (type == DoingCard.Action.Type.LongBreak) typeString = time_remaining_long_break;
                else if (type == DoingCard.Action.Type.ShortBreak) typeString = time_remaining_short_break;

                PomodoroCountDownView.this.setText(typeString + " " + minutesString+":"+secondsString);
            }

            @Override public void onFinish() {
                DoingCard.Action.Type type = DoingCard.Action.Type.Pomodoro;

                String typeString = time_over_pomodoro;
                if (type == DoingCard.Action.Type.LongBreak) typeString = time_over_long_break;
                else if (type == DoingCard.Action.Type.ShortBreak) typeString = time_over_short_break;

                PomodoroCountDownView.this.setText(typeString);
            }
        }.start();
    }
}
