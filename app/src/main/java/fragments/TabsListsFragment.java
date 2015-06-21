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
import de.greenrobot.event.EventBus;
import utilities.EventTask;

@EFragment(R.layout.tabs_list_fragment)
public class TabsListsFragment  extends Fragment {
    @ViewById protected TextView tv_title;
    @ViewById protected PagerSlidingTabStrip pst_lists;
    @ViewById protected ViewPager vp_lists;
    @StringRes protected String app_name;

    @AfterViews protected void initViews() {
        EventBus.getDefault().register(this);

        tv_title.setText(app_name);

        vp_lists.setOffscreenPageLimit(ListsFragmentAdapter.NUM_ITEMS);
        vp_lists.setAdapter(new ListsFragmentAdapter(getActivity(), getChildFragmentManager()));

        pst_lists.setViewPager(vp_lists);
    }

    public void onEvent(EventTask eventTask) {
        if (eventTask != EventTask.TABS_LISTS_FRAGMENT_MOVE_FROM_TODO_TO_DOING_LIST) return;
        vp_lists.setCurrentItem(ListsFragmentAdapter.POSITION_DOING, true);
    }
}
