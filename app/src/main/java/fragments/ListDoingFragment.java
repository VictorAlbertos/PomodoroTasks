package fragments;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;

import activities.DoingCardActivity_;
import adapters.CardsRecyclerViewAdapter;
import custom_views.ActionCountDownView;
import models.Card;
import models.DoingCard;
import utilities.EventTask;
import utilities.notifications.ScheduleNotifications;

@EFragment(R.layout.list_fragment)
public class ListDoingFragment extends ListBaseFragment {

    @AfterViews protected void initViews() {
        super.initViews();
    }

    @Override protected String getIdList() {
        return mUserService.getDoingList().getId();
    }

    @StringRes protected String doing_list;
    @Override protected String getNameList() {
        return doing_list;
    }

    @Override public int getIdResource() {
        return R.layout.doing_card_item;
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

        final DoingCard doingCard = (DoingCard) candidate;

        ActionCountDownView tv_time_running = (ActionCountDownView) viewHolder.root.findViewById(R.id.countdown_view);
        tv_time_running.bind(doingCard);

        viewHolder.root.setOnClickListener(v ->
                DoingCardActivity_.intent(getActivity())
                .extra(ScheduleNotifications.ID_CARD_DOING, doingCard.getId())
                .start()
        );
    }

    public void onEvent(EventTask eventTask) {
        super.onEvent(eventTask);

        if (eventTask != EventTask.LIST_DOING_FRAGMENT_UPDATE_COUNTDOWN) return;
        rv_cards.getAdapter().notifyDataSetChanged();
    }
}
