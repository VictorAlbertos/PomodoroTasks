package fragments;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import adapters.ListsFragmentAdapter;

@EFragment(R.layout.tabs_list_fragment)
public class TabsListsFragment extends Fragment {
    @ViewById protected TextView tv_title;
    @ViewById protected PagerSlidingTabStrip pst_lists;
    @ViewById protected ViewPager vp_lists;
    @StringRes protected String app_name;

    @AfterViews protected void initViews() {
        tv_title.setText(app_name);
        vp_lists.setAdapter(new ListsFragmentAdapter(getActivity(), getChildFragmentManager()));
        pst_lists.setViewPager(vp_lists);
    }

}
