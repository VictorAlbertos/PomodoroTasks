package fragments;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;

import adapters.CardsRecyclerViewAdapter;
import custom_views.PomodoroCountDownView;
import models.Card;
import models.DoingCard;

@EFragment(R.layout.list_fragment)
public class ListDoingFragment extends ListBaseFragment {

    @Override protected String getIdList() {
        return mUserService.getDoingList().getId();
    }

    @StringRes protected String doing_list;
    @Override protected String getNameList() {
        return doing_list;
    }

    @Override public int getIdResource() {
        return R.layout.card_doing_item;
    }

    /**
     * Returns a curated list of cards preserving the integrity of the data
     * @param  candidates cards from Trello
     * @return cards from trello marked as doing cards from the app
     */
    @Override protected List<Card> validateDataSource(List<Card> candidates) {
        List<Card> cards = new ArrayList<>(candidates.size());

        for (Card candidate : candidates) {
            DoingCard doingCard = mUserService.isThisCardSetAsDoingCard(candidate);
            if (doingCard != null) cards.add(doingCard);
            else mApiDataService.deleteCard(candidate.getId(), null);
        }

        mUserService.updateForNotMatchingDoingCards(cards);
        return cards;
    }

    @Override public void onInflate(CardsRecyclerViewAdapter.ViewHolder viewHolder, Card candidate) {
        super.onInflate(viewHolder, candidate);

        DoingCard doingCard = (DoingCard) candidate;

        PomodoroCountDownView tv_time_running = (PomodoroCountDownView) viewHolder.root.findViewById(R.id.tv_time_running);
        tv_time_running.setCountDownValueInMilliseconds(doingCard);
    }
}
