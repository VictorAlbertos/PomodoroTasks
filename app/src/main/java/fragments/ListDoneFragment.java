package fragments;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.list_fragment)
public class ListDoneFragment extends ListBaseFragment {

    @Override protected String getIdList() {
        return mUserService.getDoneList().getId();
    }

    @StringRes protected String done_list;
    @Override protected String getNameList() {
        return done_list;
    }

}
