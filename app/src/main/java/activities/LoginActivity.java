package activities;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import services.TrelloApiService;

@EActivity(R.layout.login_activity)
public class LoginActivity extends AppCompatBaseActivity {
    @ViewById protected WebView web_view;
    @ViewById protected View pw_loading;

    @AfterViews protected void initViews() {
        super.initViews();
        setUpWebview();
    }

    @Bean protected TrelloApiService mApi;
    private void setUpWebview() {
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mApi.authProcessHasReachedTokenEndPoint(url)) {
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
        mApi.authUser(url, new TrelloApiService.Callback() {
            @Override public void onResponse(String message, boolean success) {

            }
        });
    }
}
