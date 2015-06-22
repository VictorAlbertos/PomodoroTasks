package adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

import java.util.Arrays;
import java.util.List;

import models.DoingCard;
import utilities.PomodoroApp;

import static models.DoingCard.Action.Type;

@EBean
public class TypeActionSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    @App protected PomodoroApp app;
    private List<Type> typesAction = Arrays.asList(
            Type.Pomodoro, Type.LongBreak, Type.ShortBreak
    );

    @Override public int getCount() {
        return typesAction.size();
    }

    @Override public Type getItem(int position) {
        return typesAction.get(position);
    }

    @Override public long getItemId(int position) {
        return 0;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Type type = getItem(position);

        TextView textView = (TextView) View.inflate(app, R.layout.item_spinner, null);
        textView.setText(getNameBasedOnType(type));

        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Type type = getItem(position);

        LinearLayout root = (LinearLayout) View.inflate(app, R.layout.dropdown_item_spinner, null);
        ((TextView) root.findViewById(R.id.tv_text)).setText(getNameBasedOnType(type));

        return root;
    }

    @StringRes protected String pomodoro, long_break, short_break;
    private String getNameBasedOnType(Type type) {
        String typeString = pomodoro;
        if (type == DoingCard.Action.Type.LongBreak) typeString = long_break;
        else if (type == DoingCard.Action.Type.ShortBreak) typeString = short_break;
        return typeString;
    }
}
