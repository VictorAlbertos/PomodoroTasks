package models;

import com.hacerapp.pomodorotasks.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DoingCard extends Card {
    private List<Action> actions = new ArrayList<>();
    private Action currentAction;
    private long timeRemainingWhenPaused;
    private long aggregatedSpentTime;
    private boolean paused;

    public DoingCard(Card card){
        this.id = card.getId();
        this.name = card.getName();
        this.idList = card.getIdList();
        addNewAction(Action.Type.Pomodoro);
    }

    public String resetCurrentAction() {
        long remainingTime = isPaused() ? getTimeRemainingAtPause() : getRemainingTimeInMilliseconds();
        long spentTime = currentAction.getDuration() - remainingTime;

        aggregatedSpentTime += spentTime;
        currentAction.setStartTimeStamp(System.currentTimeMillis());
        paused = false;

        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(spentTime) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(spentTime) % TimeUnit.MINUTES.toSeconds(1));
    }

    //used for schedule notifications id
    public int getIntId() {
        return Integer.parseInt(id.substring(0, 8), 16);
    }

    public boolean isCurrentActionEnd() {
        if (isPaused()) return false;
        return getRemainingTimeInMilliseconds() <= 0;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPause(boolean pause) {
        this.paused = pause;
        if (this.paused) {
            timeRemainingWhenPaused = getRemainingTimeInMilliseconds();
        } else {
            long delta = currentAction.getDuration() - timeRemainingWhenPaused;
            currentAction.setStartTimeStamp(System.currentTimeMillis() - delta);
        }
    }

    public long getTimeRemainingAtPause() {
        return timeRemainingWhenPaused;
    }

    public String getPerformance(String performance, String spent_time, String pomodoros,
                                  String long_breaks, String short_breaks) {
        return  performance + "\n    " +
                    spent_time + " " + getSpentTime() + "\n    " +
                    pomodoros + " " + getNPomodoros() + "\n    " +
                    long_breaks + " " + getNLongBreaks() + "\n    " +
                    short_breaks + " " + getNShortBreaks();
    }

    public String getSpentTime() {
        long spentTime = 0;

        for (Action action : actions) {
            spentTime += action.getDuration();
        }

        spentTime+= aggregatedSpentTime;
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(spentTime),
                TimeUnit.MILLISECONDS.toMinutes(spentTime) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(spentTime) % TimeUnit.MINUTES.toSeconds(1));
    }

    public String getNPomodoros() {
        return String.valueOf(getNTimes(Action.Type.Pomodoro));
    }

    public String getNLongBreaks() {
        return String.valueOf(getNTimes(Action.Type.LongBreak));
    }

    public String getNShortBreaks() {
        return String.valueOf(getNTimes(Action.Type.ShortBreak));
    }

    public void addNewAction(Action.Type type) {
        currentAction = new Action(type);
        actions.add(currentAction);
        paused = false;
    }

    private int getNTimes(Action.Type type) {
        int n = 0;
        for (Action action : actions) {
            if (action.type == type) n++;
        }
        return n;
    }

    public long getRemainingTimeInMilliseconds() {
        long endTime = currentAction.getStartTimeStamp() + currentAction.getDuration();
        return endTime - System.currentTimeMillis();
    }

    public Action.Type getType() {
        return currentAction.getType();
    }

    public int getResourceIcon() {
        if (getType() == DoingCard.Action.Type.Pomodoro && !isPaused()) return R.drawable.ic_pomodoro;
        if (getType() == DoingCard.Action.Type.Pomodoro) return R.drawable.ic_pomodoro_pause;
        if (getType() == DoingCard.Action.Type.LongBreak && !isPaused()) return R.drawable.ic_break;
        if (getType() == DoingCard.Action.Type.LongBreak) return R.drawable.ic_break_pause;
        if (!isPaused()) return R.drawable.ic_break;
        return R.drawable.ic_break_pause;
    }

    public float getCurrentProgress() {
        long remainingTime = isPaused() ? timeRemainingWhenPaused : getRemainingTimeInMilliseconds();
        float delta = ((currentAction.getDuration() - remainingTime) * 100) / currentAction.getDuration();
        float validDelta = delta / 100 <= 1 ? delta / 100 : 1;
        return validDelta;
    }

    public static class Action {
        private final static long DURATION_POMODORO = TimeUnit.MINUTES.toMillis(25),
                DURATION_LONG_BREAK = TimeUnit.MINUTES.toMillis(15),
                DURATION_SHORT_BREAK = TimeUnit.MINUTES.toMillis(5);

        private Type type;
        private long startTimeStamp, duration;

        Action(Type type) {
            this.type = type;
            this.startTimeStamp = System.currentTimeMillis();

            switch (this.type) {
                case Pomodoro:
                    this.duration = DURATION_POMODORO;
                    break;
                case LongBreak:
                    this.duration = DURATION_LONG_BREAK;
                    break;
                case ShortBreak:
                    this.duration = DURATION_SHORT_BREAK;
                    break;
            }
        }

        public Type getType() {
            return type;
        }

        public void setStartTimeStamp(long startTimeStamp) {
            this.startTimeStamp = startTimeStamp;
        }

        private long getStartTimeStamp() {
            return startTimeStamp;
        }

        private long getDuration() {
            return duration;
        }

        public enum Type {
            Pomodoro, ShortBreak, LongBreak;
        }
    }
}
