package fragments;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;

@EFragment(R.layout.list_fragment)
public class ListDoingFragmentList extends ListBaseFragment {

    @Override protected String getIdList() {
        return mUserService.getDoingList().getId();
    }

    @StringRes protected String doing_list;
    @Override protected String getNameList() {
        return doing_list;
    }
}
