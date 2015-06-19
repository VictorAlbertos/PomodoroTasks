package services;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

@EBean
public class TrelloApiAuthService {
    @StringRes protected String trello_key, app_name, login_success, login_error;
    @Bean UserService userService;

    private static class Urls {
        private static final String TRELLO_BASE = "https://trello.com/1";
    }

    private static class Params {
        private static final String CALLBACK_URL = "http://www.android.com";
        private static final String EXPIRATION = "30";
        private static final String PERMISSIONS = "read,write";
    }

    public String getAuthEndPoint() {
        return Urls.TRELLO_BASE + "/authorize?key="+trello_key+"&name="+app_name+"&EXPIRATION="+ Params.EXPIRATION
                +"days&return_url="+Params.CALLBACK_URL+"&response_type=token&scope="+ Params.PERMISSIONS;
    }

    public boolean authProcessHasReachedTokenEndPoint(String url) {
        return url.startsWith(Params.CALLBACK_URL);
    }

    public void authUser(String url, Callback callback) {
        if(userService.createSession(url))
            callback.onResponse(login_success, true);
        else
            callback.onResponse(login_error, false);
    }

    public interface Callback {
        void onResponse(String feedback, boolean success);
    }
}
