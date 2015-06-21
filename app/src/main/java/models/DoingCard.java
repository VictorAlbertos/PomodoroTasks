package models;

import java.util.concurrent.TimeUnit;

public class DoingCard extends Card {
    private final static long DURATION_POMODORO = TimeUnit.MINUTES.toMillis(25);
    private long runningTime, spentTime;
    private int spentPomodoros, spentShortBreaks, spentLongBreaks;

    public long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public long getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(long spentTime) {
        this.spentTime = spentTime;
    }

    public int getSpentPomodoros() {
        return spentPomodoros;
    }

    public void setSpentPomodoros(int spentPomodoros) {
        this.spentPomodoros = spentPomodoros;
    }

    public int getSpentShortBreaks() {
        return spentShortBreaks;
    }

    public void setSpentShortBreaks(int spentShortBreaks) {
        this.spentShortBreaks = spentShortBreaks;
    }

    public int getSpentLongBreaks() {
        return spentLongBreaks;
    }

    public void setSpentLongBreaks(int spentLongBreaks) {
        this.spentLongBreaks = spentLongBreaks;
    }
}
