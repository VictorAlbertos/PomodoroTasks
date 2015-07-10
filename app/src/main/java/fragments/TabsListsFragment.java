package fragments;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import adapters.ListsFragmentAdapter;
import de.greenrobot.event.EventBus;
import utilities.EventTask;
import utilities.ui.RateThisApp;

@EFragment(R.layout.tabs_list_fragment)
public class TabsListsFragment  extends Fragment {
    @ViewById protected PagerSlidingTabStrip pst_lists;
    @ViewById protected ViewPager vp_lists;
    @Bean protected RateThisApp mRateThisApp;

    @AfterViews protected void initViews() {
        EventBus.getDefault().register(this);

        vp_lists.setOffscreenPageLimit(ListsFragmentAdapter.NUM_ITEMS);
        vp_lists.setAdapter(new ListsFragmentAdapter(getActivity(), getChildFragmentManager()));

        pst_lists.setViewPager(vp_lists);
    }

    public void onEvent(EventTask eventTask) {
        if (eventTask == EventTask.TABS_LISTS_FRAGMENT_MOVE_TO_TODO_LIST) {
            vp_lists.setCurrentItem(ListsFragmentAdapter.POSITION_TODO, true);
        } else if (eventTask == EventTask.TABS_LISTS_FRAGMENT_MOVE_TO_DOING_LIST) {
            vp_lists.setCurrentItem(ListsFragmentAdapter.POSITION_DOING, true);
        } else if (eventTask == EventTask.TABS_LISTS_FRAGMENT_MOVE_TO_DONE_LIST) {
            vp_lists.setCurrentItem(ListsFragmentAdapter.POSITION_DONE, true);
            mRateThisApp.askIfNeedIt(getActivity());
        }
    }
}
