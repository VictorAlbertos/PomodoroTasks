package models;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DoingCard extends Card {
    private List<Action> actions = new ArrayList<>();
    private Action currentAction;
    private long timeRemainingWhenThisWasPause;
    private long aggregatedStopTime;
    private boolean pause;

    public DoingCard(Card card){
        this.id = card.getId();
        this.name = card.getName();
        this.idList = card.getIdList();
        addNewAction(Action.Type.Pomodoro);
    }

    public void resetCurrentAction() {
        aggregatedStopTime += getRemainingTimeInMilliseconds();
        currentAction.setStartTimeStamp(System.currentTimeMillis());
    }

    public int getIdNotification() {
        String temp = id.replaceAll("[^0-9]+", " ");
        return Integer.valueOf(temp);
    }

    public boolean isCurrentActionEnd() {
        return getRemainingTimeInMilliseconds() == 0;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
        if (this.pause) {
            timeRemainingWhenThisWasPause = getRemainingTimeInMilliseconds();
        } else {
            long delta = currentAction.getDuration() - timeRemainingWhenThisWasPause;
            currentAction.setStartTimeStamp(System.currentTimeMillis() + delta);
        }
    }

    public long getTimeRemainingWhenThisWasPause() {
        return timeRemainingWhenThisWasPause;
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

    public String getSpentTime() {
        long spentTime = 0;

        for (Action action : actions) {
            spentTime += action.getDuration();
        }

        spentTime=+aggregatedStopTime;
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(spentTime),
                TimeUnit.MILLISECONDS.toMinutes(spentTime) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(spentTime) % TimeUnit.MINUTES.toSeconds(1));
    }

    public void addNewAction(Action.Type type) {
        if (currentAction != null) actions.add(currentAction);
        currentAction = new Action(type);
        pause = false;
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

    public static class Action {
        private final static long DURATION_POMODORO = TimeUnit.MINUTES.toMillis(1),
                DURATION_LONG_BREAK = TimeUnit.MINUTES.toMillis(10),
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
