package activities;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import services.TrelloApiAuthService;
import utilities.ui.CustomToast;

@EActivity(R.layout.login_activity)
public class LoginActivity extends FragmentActivity {
    @ViewById protected WebView web_view;
    @ViewById protected View pw_loading;
    @ViewById protected Toolbar toolbar;
    @ViewById protected TextView tv_title;
    @Bean protected CustomToast customToast;
    @StringRes protected String connect_with_trello;

    @AfterViews protected void initViews() {
        tv_title.setText(connect_with_trello);
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
        mApi.authUser(url, new TrelloApiAuthService.Callback() {
            @Override public void onResponse(String message, boolean success) {
                if (success) ConfigActivity_.intent(LoginActivity.this).start();
                else customToast.showToast(message);
            }
        });
    }
}
