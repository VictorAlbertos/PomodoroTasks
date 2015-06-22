package utilities.ui;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import utilities.PomodoroApp;

@EBean
public class Sounds {
    @App protected PomodoroApp app;

    public MediaPlayer play(int resID) {
        try {
            AssetFileDescriptor afd = app.getResources().openRawResourceFd(resID);

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.prepare();
            mediaPlayer.start();

            afd.close();
            return mediaPlayer;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
