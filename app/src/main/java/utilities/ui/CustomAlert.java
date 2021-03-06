package utilities.ui;

import android.app.Activity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

@EBean
public class CustomAlert {
    @StringRes protected String agree, disagree;

    public MaterialDialog showOneChoice(Activity activity, String title, String content) {
        return new MaterialDialog.Builder(activity)
                .title(title)
                .content(content)
                .positiveText(agree)
                .show();
    }

    public MaterialDialog showTwoChoices(Activity activity, String title, String content, MaterialDialog.ButtonCallback callback) {
        return new MaterialDialog.Builder(activity)
                .title(title)
                .callback(callback)
                .content(content)
                .positiveText(agree)
                .negativeText(disagree)
                .show();
    }

    public MaterialDialog showTwoChoicesCustomView(Activity activity, String title, View view, MaterialDialog.ButtonCallback callback) {
        return new MaterialDialog.Builder(activity)
                .title(title)
                .autoDismiss(false)
                .customView(view, true)
                .callback(callback)
                .positiveText(agree)
                .negativeText(disagree)
                .show();
    }
}
