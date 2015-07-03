package activities;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import services.TrelloApiAuthService;
import utilities.ui.CustomToast;

@EActivity(R.layout.login_activity)
public class LoginActivity extends BaseAppCompatActivity {
    @ViewById protected WebView web_view;
    @ViewById protected View pw_loading;
    @Bean protected CustomToast customToast;

    @Override protected void initViews() {
        super.initViews();
        setUpWebview();
    }

    @Bean protected TrelloApiAuthService mApi;
    private void setUpWebview() {
        web_view.getSettings().setJavaScriptEnabled(true);

        web_view.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mApi.authProcessHasReachedTokenEndPoint(url)) {
                    web_view.setVisibility(View.INVISIBLE);
                    attemptToPerformLogin(url);
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override public void onPageFinished(WebView view, String url) {
                pw_loading.setVisibility(View.GONE);
            }
        });

        web_view.loadUrl(mApi.getAuthEndPoint());
    }

    private void attemptToPerformLogin(String url) {
        mApi.authUser(url, (message, success) -> {
            if (success) InitialConfigActivity_.intent(LoginActivity.this).start();
            else customToast.showToast(message);
        });
    }

    @StringRes protected String connect_with_trello;
    @Override protected String titleToolbar() {
        return connect_with_trello;
    }
}
