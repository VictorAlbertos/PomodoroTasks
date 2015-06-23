package adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hacerapp.pomodorotasks.R;

import fragments.ListBaseFragment;
import fragments.ListDoingFragment_;
import fragments.ListDoneFragment_;
import fragments.ListTodoFragment_;

public class ListsFragmentAdapter extends FragmentPagerAdapter {
    public static final int NUM_ITEMS = 3, POSITION_TODO = 0, POSITION_DOING = 1, POSITION_DONE = 3;
    private Resources mResources;

    public ListsFragmentAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mResources = context.getResources();
    }

    @Override public ListBaseFragment getItem(int position) {
        if (position == POSITION_TODO) return new ListTodoFragment_();
        else if (position == POSITION_DOING) return new ListDoingFragment_();
        else return new ListDoneFragment_();
    }

    @Override public int getCount() {
        return NUM_ITEMS;
    }

    @Override public CharSequence getPageTitle(int position) {
        if (position == POSITION_TODO) return mResources.getString(R.string.to_do_list);
        else if (position == POSITION_DOING) return mResources.getString(R.string.doing_list);
        else return mResources.getString(R.string.done_list);
    }
}
