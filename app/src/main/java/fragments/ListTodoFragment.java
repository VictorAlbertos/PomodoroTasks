package fragments;

import com.hacerapp.pomodorotasks.R;
import com.melnykov.fab.FloatingActionButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import adapters.CardsRecyclerViewAdapter;
import de.greenrobot.event.EventBus;
import models.Card;
import models.DoingCard;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import utilities.EventTask;
import utilities.notifications.ScheduleNotifications;

@EFragment(R.layout.list_todo_fragment)
public class ListTodoFragment extends ListBaseFragment {
    @ViewById protected FloatingActionButton fab;
    @Bean protected ScheduleNotifications mScheduleNotifications;
    @StringRes protected String to_do_list, task_moved_to_doing_list, error_connection;

    @AfterViews protected void initViews() {
        super.initViews();
        fab.attachToRecyclerView(rv_cards);
    }

    @Click protected void fab() {
        mCustomToast.showErrorConnection();
    }

    @Override protected String getIdList() {
        return mUserService.getToDoList().getId();
    }

    @Override protected String getNameList() {
        return to_do_list;
    }

    @Override public int getIdResource() {
        return R.layout.todo_card_item;
    }

    @Override public void onInflate(CardsRecyclerViewAdapter.ViewHolder viewHolder, final Card card) {
        super.onInflate(viewHolder, card);

        final Callback<Card> moveCallback = new Callback<Card>() {
            @Override public void success(Card card, Response response) {
                String message = task_moved_to_doing_list.replaceFirst("__", card.getName());
                mCustomToast.showToast(message);

                DoingCard doingCard = new DoingCard(card);
                mScheduleNotifications.setFor(doingCard);
                mUserService.addDoingCard(doingCard);

                EventBus.getDefault().post(EventTask.TABS_LISTS_FRAGMENT_MOVE_TO_DOING_LIST);
                EventBus.getDefault().post(EventTask.TABS_LISTS_UPDATE_DATA_SOURCE);
            }

            @Override public void failure(RetrofitError error) {
                mCustomToast.showToast(error_connection);
            }
        };

        viewHolder.root.setOnClickListener(v -> mApiDataService.moveCardToDoingList(card, moveCallback));
    }
}
