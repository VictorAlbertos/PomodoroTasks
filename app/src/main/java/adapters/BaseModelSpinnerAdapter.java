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

import java.util.List;

import models.BaseModel;
import utilities.PomodoroApp;

@EBean
public class BaseModelSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    @App protected PomodoroApp app;
    private List<? extends BaseModel> baseModels;

    public BaseModelSpinnerAdapter init(List<? extends BaseModel> baseModels) {
        this.baseModels = baseModels;
        return this;
    }

    @Override public int getCount() {
        return baseModels.size();
    }

    @Override public BaseModel getItem(int position) {
        return baseModels.get(position);
    }

    @Override public long getItemId(int position) {
        return 0;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        BaseModel baseModel = getItem(position);

        TextView textView = (TextView) View.inflate(app, R.layout.item_spinner, null);
        textView.setText(baseModel.getName());

        return textView;
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        BaseModel baseModel = getItem(position);

        LinearLayout root = (LinearLayout) View.inflate(app, R.layout.dropdown_item_spinner, null);
        ((TextView) root.findViewById(R.id.tv_text)).setText(baseModel.getName());

        return root;
    }
}
