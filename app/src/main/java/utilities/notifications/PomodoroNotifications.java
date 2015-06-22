package utilities.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

import activities.DoingCardActivity_;
import models.Card;
import models.DoingCard;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.TrelloApiDataService;
import services.UserService;
import utilities.PomodoroApp;

@EBean
class PomodoroNotifications {
    @App protected PomodoroApp mApp;
    @Bean protected UserService mUserService;
    @Bean protected TrelloApiDataService mApiDataService;
    private NotificationManager mNotificationManager;

    @AfterInject protected void init() {
        mNotificationManager = (NotificationManager) mApp.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    void sendIfNeeded(Intent intent) {
        String idDoingCard = intent.getExtras().getString(ScheduleNotifications.ID_CARD_DOING);
        if (idDoingCard == null) return;

        final DoingCard doingCard = mUserService.isThisDoingCardStillValid(idDoingCard);
        if (doingCard == null) return;

        Runnable callback = new Runnable() {
            @Override public void run() {
                if (mApp.isCardDoingActivityRunning()) {
                    String title = doingCard.getName();
                    String text = getTextAlert(doingCard, true);
                    mApp.getCardDoingActivity().fromNotificationAskToSwitchTo(doingCard, title, text);
                } else  showNotification(doingCard);
            }
        };

        callbackOnlyIfCardStillExitsInTrello(doingCard.getId(), callback);
    }

    void cancelIfNeeded(DoingCard doingCard) {
        mNotificationManager.cancel(doingCard.getIdNotification());
    }

    private void callbackOnlyIfCardStillExitsInTrello(final String idDoingCard, final Runnable callback) {
        String idDoingList = mUserService.getDoingList().getId();
        mApiDataService.getCards(idDoingList, new Callback<List<Card>>() {
            @Override  public void success(List<Card> cards, Response response) {
                for (Card card : cards) {
                    if (card.getId().equals(idDoingCard)) callback.run();
                }
            }

            @Override public void failure(RetrofitError error) {}
        });
    }

    private void showNotification(DoingCard doingCard) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mApp)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(doingCard.getName())
                        .setContentText(getTextAlert(doingCard, false));
        builder.setContentIntent(getContentIntentNotification(doingCard));

        mNotificationManager.notify(doingCard.getIdNotification(), builder.build());
    }

    private PendingIntent getContentIntentNotification(DoingCard doingCard) {
        Intent resultIntent = new Intent(mApp, DoingCardActivity_.class);

        Bundle bundle = new Bundle();
        bundle.putString(ScheduleNotifications.ID_CARD_DOING, doingCard.getId());
        resultIntent.putExtras(bundle);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mApp);
        stackBuilder.addParentStack(DoingCardActivity_.class);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @StringRes protected String notification_inside_activity, notification, pomodoro, long_break, short_break;
    private String getTextAlert(DoingCard doingCard, boolean insideActivity) {
        DoingCard.Action.Type type = doingCard.getType();

        String text = insideActivity ? notification_inside_activity : notification;
        String typeString = pomodoro;
        if (type == DoingCard.Action.Type.LongBreak) typeString = long_break;
        else if (type == DoingCard.Action.Type.ShortBreak) typeString = short_break;

        return text.replaceFirst("__", String.valueOf(typeString));
    }

}
