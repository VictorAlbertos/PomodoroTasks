package services;

import android.support.annotation.Nullable;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import models.Board;
import models.Card;
import models.DoingCard;
import models.List;
import models.User;
import utilities.Persistence;

@EBean(scope = EBean.Scope.Singleton)
public class UserService {
    @Bean protected Persistence mPersistence;

    public String getToken() {
        if (getUser().getToken() == null) throw new RuntimeException("Token has not been granted");
        return getUser().getToken();
    }

    public boolean createSession(String url) {
        String token;
        try {
            token = url.substring(url.lastIndexOf("=") + 1);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        getUser().setToken(token);
        return persistsChanges();
    }

    public void destroySession() {
        mUser = null;
        persistsChanges();
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
        persistsChanges();
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

    public boolean showRateThisAppIfApplicable() {
        return mUser.showRateThisAppIfApplicable();
    }

    public void showRateThisAppIfApplicable(boolean show) {
        mUser.showRateThisAppIfApplicable(show);
        persistsChanges();
    }

    private User mUser;
    private User getUser() {
        if (mUser != null) return mUser;

        mUser = (User) mPersistence.JSONFromDisk(User.class, "mUser");
        if (mUser != null) return mUser;

        return mUser = new User();
    }

    public void updateForNotMatchingDoingCards(java.util.List<Card> doingCards) {
        java.util.List<DoingCard> validDoingCards = new ArrayList<>(doingCards.size());
        for (Card card : doingCards) {
            validDoingCards.add((DoingCard) card);
        }
        getUser().setDoingCards(validDoingCards);
        persistsChanges();
    }

    public void addDoingCard(DoingCard doingCard) {
        getUser().getDoingCards().add(doingCard);
        persistsChanges();
    }

    @Nullable public DoingCard isThisDoingCardStillValid(final String idDoingCard) {
        for (DoingCard doingCard : getUser().getDoingCards()) {
            if (doingCard.getId().equals(idDoingCard)) return doingCard;
        }
        return null;
    }

    @Nullable public DoingCard isThisCardSetAsDoingCard(Card candidate) {
        for (DoingCard doingCard : getUser().getDoingCards()) {
            if (candidate.getId().equals(doingCard.getId())) return doingCard;
        }
        return null;
    }

    public boolean persistsChanges() {
        return mPersistence.JSONToDisk(mUser, "mUser");
    }
}

