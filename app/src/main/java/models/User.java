package models;

public class User {
    private String token;
    private Board board;
    private List toDoList, doingList, doneList;

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
}
