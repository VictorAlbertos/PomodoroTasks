package custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;

import adapters.BaseModelSpinnerAdapter;
import fr.ganfra.materialspinner.MaterialSpinner;
import models.BaseModel;

@EViewGroup(R.layout.config_input_view)
public class ConfigInputView extends FrameLayout implements AdapterView.OnItemSelectedListener {
    public final static int DEFAULT_POSITION = -1;
    @ViewById public MaterialSpinner sp_base_model;
    @ViewById protected View pw_loading;
    private BaseModel mBaseModel;

    @Bean protected BaseModelSpinnerAdapter mBoardAdapter;
    private Listener mListener;

    public ConfigInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews protected void initViews() {
        setEnable(false);
    }

    public void setUp(String floatingLabelText, String hint) {
        sp_base_model.setFloatingLabelText(floatingLabelText);
        sp_base_model.setHint(hint);
    }

    public void clearDataSource() {
        setDataSource(new ArrayList<BaseModel>(), null);
        setEnable(false);
        sp_base_model.setError(null);
    }

    public void setDataSource(List<? extends BaseModel> dataSource, BaseModel persistedModel) {
        mBaseModel = persistedModel;
        sp_base_model.setAdapter(mBoardAdapter.init(dataSource));
        sp_base_model.setOnItemSelectedListener(this);
        setStatusLoading(false);

        final int indexPersistedModel = getIndexForBaseModel(dataSource, persistedModel);
        if (indexPersistedModel != DEFAULT_POSITION) {
            sp_base_model.setSelection(indexPersistedModel, true);
        }
    }

    private int getIndexForBaseModel(List<? extends BaseModel> dataSource, BaseModel persisted) {
        int position = 1;
        if (persisted == null) return DEFAULT_POSITION;

        for (BaseModel model : dataSource) {
            if (model.getId().equals(persisted.getId())) return position;
            position++;
        }
        return DEFAULT_POSITION;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void showLoading() {
        setStatusLoading(true);
    }

    public BaseModel getSelected() {
        return mBaseModel;
    }

    @StringRes protected String validation_empty_field;
    public boolean validationIsModelEmpty() {
        if (getSelected() == null) {
            sp_base_model.setError(validation_empty_field);
            return true;
        }

        return false;
    }

    @StringRes protected String validation_repeated_list;
    public boolean validationIsIdRepeated(String... ids) {
        for (String id : ids) {
            if (mBaseModel.getId().equals(id)) {
                sp_base_model.setError(validation_repeated_list);
                return true;
            }
        }

        return false;
    }

    private void setStatusLoading(boolean loading) {
        if (loading) {
            pw_loading.setVisibility(VISIBLE);
            setEnable(false);
        } else {
            pw_loading.setVisibility(GONE);
            setEnable(true);
        }
    }

    private void setEnable(boolean enable) {
        sp_base_model.setEnabled(enable);
        sp_base_model.setClickable(enable);
        if (enable) sp_base_model.setAlpha(1);
        else sp_base_model.setAlpha(0.5f);
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == -1) mBaseModel = null;
        else  mBaseModel = mBoardAdapter.getItem(position);

        if( mListener!= null) mListener.onValueSelected(this, mBaseModel);
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
        sp_base_model.setEnabled(true);
    }

    public interface Listener {
        void onValueSelected(ConfigInputView configInputView, BaseModel baseModel);
    }
}
