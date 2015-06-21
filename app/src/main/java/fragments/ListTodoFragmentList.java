package fragments;

import android.view.View;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;

import adapters.CardsRecyclerViewAdapter;
import models.Card;

@EFragment(R.layout.list_fragment)
public class ListTodoFragmentList extends ListBaseFragment {

    @Override protected String getIdList() {
        return mUserService.getToDoList().getId();
    }

    @StringRes protected String to_do_list;
    @Override protected String getNameList() {
        return to_do_list;
    }

    @Override public int getIdResource() {
        return R.layout.card_todo_item;
    }

    @Override public void onInflate(CardsRecyclerViewAdapter.ViewHolder viewHolder, final Card card) {
        View bt_start = viewHolder.root.findViewById(R.id.bt_start);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomToast.showToast(card.getName());
            }
        });
    }
}
