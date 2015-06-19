package utilities.ui;

import android.app.Activity;

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
}
