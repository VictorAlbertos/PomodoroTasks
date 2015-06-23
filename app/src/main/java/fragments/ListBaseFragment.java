package fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

import adapters.CardsRecyclerViewAdapter;
import de.greenrobot.event.EventBus;
import models.Card;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.TrelloApiDataService;
import services.UserService;
import utilities.EventTask;
import utilities.ui.CustomToast;

@EFragment
public abstract class ListBaseFragment extends Fragment implements CardsRecyclerViewAdapter.ItemCardView {
    @ViewById protected RecyclerView rv_cards;
    @ViewById protected PullRefreshLayout prl_cards;
    @StringRes protected String empty_list;
    @Bean protected TrelloApiDataService mApiDataService;
    @Bean protected CustomToast mCustomToast;
    @Bean protected UserService mUserService;
    @Bean protected CardsRecyclerViewAdapter adapter;

    @AfterViews protected void initViews() {
        EventBus.getDefault().register(this);
        setUpRecyclerView();
        setUpRefreshLayout();
        populateCards(false);
    }

    private void setUpRecyclerView() {
        rv_cards.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_cards.setHasFixedSize(true);
        rv_cards.setItemViewCacheSize(10);
    }

    private void setUpRefreshLayout() {
        prl_cards.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                populateCards(true);
            }
        });
    }

    private void populateCards(final boolean refreshedByUser) {
        prl_cards.setRefreshing(true);
        mApiDataService.getCards(getIdList(), new Callback<List<Card>>() {
            @Override public void success(List<Card> cards, Response response) {
                rv_cards.setAdapter(adapter.init(validateDataSource(cards), ListBaseFragment.this));
                prl_cards.setRefreshing(false);

                if (cards.isEmpty() && refreshedByUser)
                    mCustomToast.showToast(empty_list.replace("__", getNameList()));
            }

            @Override public void failure(RetrofitError error) {
                prl_cards.setRefreshing(false);
                mCustomToast.showErrorConnection();
            }
        });
    }

    @Override public void onInflate(CardsRecyclerViewAdapter.ViewHolder viewHolder, Card card) {
        ((TextView) viewHolder.root.findViewById(R.id.tv_title)).setText(card.getName());
    }

    protected abstract String getIdList();
    protected abstract String getNameList();

    protected List<Card> validateDataSource(List<Card> candidates) {
        return candidates;
    }

    public void onEvent(EventTask eventTask) {
        if (eventTask != EventTask.TABS_LISTS_UPDATE_DATA_SOURCE) return;
        populateCards(false);
    }
}
