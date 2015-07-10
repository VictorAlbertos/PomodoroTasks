package utilities.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import models.DoingCard;
import utilities.PomodoroApp;

@EBean
public class ScheduleNotifications {
    public static final String ID_CARD_DOING = "id_card_doing";
    @App protected PomodoroApp mApp;

    public void setFor(DoingCard doingCard) {
        if (doingCard.isPaused() || doingCard.isCurrentActionEnd()) return;

        Intent alertIntent = new Intent(mApp, PomodoroBroadcastReceiver_.class);

        Bundle bundle = new Bundle();
        bundle.putString(ID_CARD_DOING, doingCard.getId());
        alertIntent.putExtras(bundle);

        long triggerAtMillis = System.currentTimeMillis() + doingCard.getRemainingTimeInMilliseconds();
        AlarmManager alarmManager = (AlarmManager) mApp.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis,
                PendingIntent.getBroadcast(mApp, doingCard.getIntId(), alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
