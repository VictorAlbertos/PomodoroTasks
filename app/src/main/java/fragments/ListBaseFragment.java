package fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

import adapters.CardsRecyclerViewAdapter;
import models.Card;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.TrelloApiDataService;
import services.UserService;
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
        mApiDataService.getCards(getIdList(), new Callback<List<Card>>() {

            @Override public void success(List<Card> cards, Response response) {
                rv_cards.setAdapter(adapter.init(cards, ListBaseFragment.this));
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

    @Override public int getIdResource() {
        return R.layout.card_item;
    }

    @Override public void onInflate(CardsRecyclerViewAdapter.ViewHolder viewHolder, Card card) {}
    protected abstract String getIdList();
    protected abstract String getNameList();
}
