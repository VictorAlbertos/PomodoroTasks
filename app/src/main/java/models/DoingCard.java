package models;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DoingCard extends Card {
    private Card card;
    private List<Action> actions = new ArrayList<>();
    private Action currentAction;

    public DoingCard(){}

    public DoingCard(Card card) {
        this.card = card;
    }

    public int getNPomodoros() {
        return getNTimes(Action.Type.Pomodoro);
    }

    public int getNLongBreaks() {
        return getNTimes(Action.Type.LongBreak);
    }

    public int getNShortBreaks() {
        return getNTimes(Action.Type.LongBreak);
    }

    public void addNewAction(Action action) {
        if (currentAction != null) actions.add(currentAction);
        currentAction = action;
    }

    private int getNTimes(Action.Type type) {
        int n = 0;
        for (Action action : actions) {
            if (action.type == type) n++;
        }
        return n;
    }

    public long getSpentTime() {
        long spentTime = 0;
        for (Action action : actions) {
            spentTime += action.getDuration();
        }
        return spentTime;
    }

    public long getRunningTimeInMilliseconds() {
        long endTime = currentAction.getStartTimeStamp() + currentAction.getDuration();
        return endTime - System.currentTimeMillis();
    }

    static class Action {
        private final static long DURATION_POMODORO = TimeUnit.MINUTES.toMillis(25),
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

        private Type getType() {
            return type;
        }

        private long getStartTimeStamp() {
            return startTimeStamp;
        }

        private long getDuration() {
            return duration;
        }

        enum Type {
            Pomodoro, ShortBreak, LongBreak;
        }
    }

}
