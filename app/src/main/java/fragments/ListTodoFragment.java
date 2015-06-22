package fragments;

import android.view.View;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
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

@EFragment(R.layout.list_fragment)
public class ListTodoFragment extends ListBaseFragment {
    @Bean protected ScheduleNotifications mScheduleNotifications;
    @StringRes protected String to_do_list, card_moved_to_doing_list, error_connection;

    @Override protected String getIdList() {
        return mUserService.getToDoList().getId();
    }

    @Override protected String getNameList() {
        return to_do_list;
    }

    @Override public int getIdResource() {
        return R.layout.card_todo_item;
    }

    @Override public void onInflate(CardsRecyclerViewAdapter.ViewHolder viewHolder, final Card card) {
        super.onInflate(viewHolder, card);

        View bt_start = viewHolder.root.findViewById(R.id.bt_start);

        final Callback<Card> moveCallback = new Callback<Card>() {
            @Override public void success(Card card, Response response) {
                mCustomToast.showToast(card_moved_to_doing_list);

                DoingCard doingCard = new DoingCard(card);
                mScheduleNotifications.setAlarm(doingCard);
                mUserService.addDoingCard(doingCard);

                EventBus.getDefault().post(EventTask.TABS_LISTS_FRAGMENT_MOVE_FROM_TODO_TO_DOING_LIST);
                EventBus.getDefault().post(EventTask.TABS_LISTS_UPDATE_DATA_SOURCE);
            }

            @Override public void failure(RetrofitError error) {
                mCustomToast.showToast(error_connection);
            }
        };

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mApiDataService.moveCardFromTodoToDoingList(card, moveCallback);
            }
        });
    }
}
