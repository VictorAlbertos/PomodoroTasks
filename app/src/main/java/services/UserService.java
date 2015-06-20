package services;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import models.Board;
import models.List;
import models.User;
import utilities.Persistence;

@EBean(scope = EBean.Scope.Singleton)
public class UserService {
    @Bean protected Persistence mPersistence;

    public String getToken() {
        if (getUser().getToken() == null)
            throw new RuntimeException("Token has not been granted");

        return getUser().getToken();
    }

    public boolean createSession(String url) {
        String token = null;
        try {
            token = url.substring(url.lastIndexOf("=") + 1);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        getUser().setToken(token);
        return mPersistence.JSONToDisk(getUser(), "user");
    }

    public void destroySession() {
        mUser = null;
        mPersistence.JSONToDisk(mUser, "user");
    }

    public boolean isAuthenticate() {
        return getUser().getToken() != null;
    }

    public boolean isAccountConfigured() {
        return getUser().getToDoList() != null || getUser().getDoingList() != null
                    || getUser().getDoneList() != null;
    }

    public void configAccount(Board board, List toDoList, List doingList, List doneList) {
        getUser().setBoard(board);
        getUser().setToDoList(toDoList);
        getUser().setDoingList(doingList);
        getUser().setDoneList(doneList);
        mPersistence.JSONToDisk(mUser, "user");
    }

    public Board getBoard() {
        return getUser().getBoard();
    }

    public List getToDoList() {
        return getUser().getToDoList();
    }

    public List getDoingList() {
        return getUser().getDoingList();
    }

    public List getDoneList() {
        return getUser().getDoneList();
    }

    private User mUser;
    private User getUser() {
        if (mUser != null) return mUser;

        mUser = (User) mPersistence.JSONFromDisk(User.class, "user");
        if (mUser != null)
            return mUser;

        return mUser = new User();
    }
}
