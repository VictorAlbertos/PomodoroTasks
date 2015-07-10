package models;

import java.util.ArrayList;

public class User {
    private String token;
    private Board board;
    private List toDoList, doingList, doneList;
    private java.util.List<DoingCard> doingCards = new ArrayList<>();
    private boolean showRateThisAppIfApplicable = true;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List getToDoList() {
        return toDoList;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setToDoList(List toDoList) {
        this.toDoList = toDoList;
    }

    public List getDoingList() {
        return doingList;
    }

    public void setDoingList(List doingList) {
        this.doingList = doingList;
    }

    public List getDoneList() {
        return doneList;
    }

    public void setDoneList(List doneList) {
        this.doneList = doneList;
    }

    public java.util.List<DoingCard> getDoingCards() {
        return doingCards;
    }

    public void setDoingCards(java.util.List<DoingCard> doingCards) {
        this.doingCards = doingCards;
    }

    public boolean showRateThisAppIfApplicable() {
        return showRateThisAppIfApplicable;
    }

    public void showRateThisAppIfApplicable(boolean show) {
        this.showRateThisAppIfApplicable = show;
    }
}
