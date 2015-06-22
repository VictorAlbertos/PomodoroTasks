package utilities.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;

@EReceiver
public class PomodoroBroadcastReceiver extends BroadcastReceiver {
    @Bean protected PomodoroNotifications notifications;

    @Override public void onReceive(Context context, Intent intent) {
        notifications.sendIfNeeded(intent);
    }
}
