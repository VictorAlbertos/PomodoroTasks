package utilities.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.afollestad.materialdialogs.MaterialDialog;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

import services.UserService;

@EBean
public class RateThisApp extends MaterialDialog.ButtonCallback {
    @Bean protected UserService mUser;
    @StringRes protected String rate_this_app_title, rate_this_app_content, rate_now,
            remind_me_later, no_thanks;
    private Activity mActivity;

    public void askIfNeedIt(Activity activity) {
        if (!mUser.showRateThisAppIfApplicable()) return;

        mActivity = activity;

        new MaterialDialog.Builder(activity)
                .title(rate_this_app_title)
                .callback(this)
                .content(rate_this_app_content)
                .positiveText(rate_now)
                .neutralText(remind_me_later)
                .negativeText(no_thanks)
                .show();
    }

    @Override public void onPositive(MaterialDialog dialog) {
        String packageName = mActivity.getPackageName();
        Uri uri = Uri.parse("market://details?id=" + packageName);

        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            mActivity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }

        mUser.showRateThisAppIfApplicable(false);
    }

    @Override public void onNegative(MaterialDialog dialog) {
        mUser.showRateThisAppIfApplicable(false);
    }
}
