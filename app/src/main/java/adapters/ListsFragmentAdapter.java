package adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hacerapp.pomodorotasks.R;

import fragments.ListBaseFragment;
import fragments.ListDoingFragmentList_;
import fragments.ListDoneFragmentList_;
import fragments.ListTodoFragmentList_;

public class ListsFragmentAdapter extends FragmentPagerAdapter {
    public static final int NUM_ITEMS = 3, POSITION_DOING = 1;
    private static final int POSITION_TODO = 0;
    private Resources mResources;

    public ListsFragmentAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mResources = context.getResources();
    }

    @Override public ListBaseFragment getItem(int position) {
        if (position == POSITION_TODO) return new ListTodoFragmentList_();
        else if (position == POSITION_DOING) return new ListDoingFragmentList_();
        else return new ListDoneFragmentList_();
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
