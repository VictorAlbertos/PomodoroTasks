package services;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import models.User;
import utilities.Persistence;

@EBean(scope = EBean.Scope.Singleton)
public class UserService {
    @Bean protected Persistence mPersistence;

    public String getToken() {
        if (getSafetyUser().getToken() == null)
            throw new RuntimeException("Token has not been granted");

        return getSafetyUser().getToken();
    }

    public boolean setToken(String token) {
        mUser.setToken(token);
        return mPersistence.JSONToDisk(mUser, mUser.getClass().getName());
    }

    public boolean isAuthenticate() {
        return getSafetyUser().getToken() != null;
    }

    private User mUser;
    private User getSafetyUser() {
        if (mUser != null) return mUser;

        mUser = (User) mPersistence.JSONFromDisk(User.class, mUser.getClass().getName());
        if (mUser != null)
            return mUser;

        return mUser = new User();
    }
}
