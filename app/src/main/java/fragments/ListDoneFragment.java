package fragments;

import android.widget.TextView;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;

import java.util.Collections;
import java.util.List;

import adapters.CardsRecyclerViewAdapter;
import models.Card;

@EFragment(R.layout.list_fragment)
public class ListDoneFragment extends ListBaseFragment {

    @Override public int getIdResource() {
        return R.layout.done_card_item;
    }
    @Override protected String getIdList() {
        return mUserService.getDoneList().getId();
    }

    @StringRes protected String done_list;
    @Override protected String getNameList() {
        return done_list;
    }

    @Override public void onInflate(CardsRecyclerViewAdapter.ViewHolder viewHolder, Card card) {
        super.onInflate(viewHolder, card);
        ((TextView) viewHolder.root.findViewById(R.id.tv_desc)).setText(card.getDesc());
    }

    @Override protected List<Card> validateDataSource(List<Card> candidates) {
        Collections.reverse(candidates);
        return candidates;
    }
}
