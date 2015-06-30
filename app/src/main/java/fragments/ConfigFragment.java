package fragments;

import android.support.v4.app.Fragment;

import com.hacerapp.pomodorotasks.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

import activities.ConfigActivity;
import activities.InitialConfigActivity;
import activities.MainActivity_;
import custom_views.ConfigInputView;
import de.greenrobot.event.EventBus;
import info.hoang8f.widget.FButton;
import models.BaseModel;
import models.Board;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.TrelloApiDataService;
import services.UserService;
import utilities.EventTask;
import utilities.ui.Animations;
import utilities.ui.CustomToast;

@EFragment(R.layout.config_fragment)
public class ConfigFragment extends Fragment {
    @Bean protected TrelloApiDataService mApiDataService;
    @Bean protected CustomToast mCustomToast;
    @Bean protected Animations mAnimations;
    @Bean protected UserService mUserService;
    @ViewById protected ConfigInputView civ_boards, civ_to_do, civ_doing, civ_done;
    @ViewById protected FButton bt_save;
    @StringRes protected String board, select_board, to_do_list, select_to_do_list,
            doing_list, select_doing_list, done_list, select_done_list, settings_saved;

    @AfterViews protected void initViews() {
        setUpCivInputs();
        populateInputs();
    }

    private void setUpCivInputs() {
        civ_boards.setUp(board, select_board);
        civ_boards.setListener((configInputView, baseModel) -> populateLists(baseModel));

        civ_to_do.setUp(to_do_list, select_to_do_list);
        civ_doing.setUp(doing_list, select_doing_list);
        civ_done.setUp(done_list, select_done_list);
    }

    public void populateInputs() {
        civ_boards.showLoading();
        mApiDataService.getBoards(new Callback<List<Board>>() {
            @Override public void success(final List<Board> boards, Response response) {
                civ_boards.setDataSource(boards, mUserService.getBoard());
            }

            @Override public void failure(RetrofitError error) {
                mCustomToast.showErrorConnection();
            }
        });
    }

    private void populateLists(BaseModel board) {
        if (board == null) {
            civ_to_do.clearDataSource();
            civ_doing.clearDataSource();
            civ_done.clearDataSource();
            return;
        }

        civ_to_do.showLoading();
        civ_doing.showLoading();
        civ_done.showLoading();

        mApiDataService.getLists(board.getId(), new Callback<List<models.List>>() {
            @Override public void success(List<models.List> lists, Response response) {
                civ_to_do.setDataSource(lists, mUserService.getToDoList());
                civ_doing.setDataSource(lists, mUserService.getDoingList());
                civ_done.setDataSource(lists, mUserService.getDoneList());
            }

            @Override public void failure(RetrofitError error) {
                mCustomToast.showErrorConnection();
            }
        });
    }

    @Click protected void bt_save() {
        if (civ_boards.validationIsModelEmpty() || civ_to_do.validationIsModelEmpty()
                || civ_doing.validationIsModelEmpty() || civ_done.validationIsModelEmpty()) {
            mAnimations.shakeValidation(bt_save);
            return;
        }

        Board board = (Board) civ_boards.getSelected();
        models.List toDoList = (models.List) civ_to_do.getSelected();
        models.List doingList = (models.List) civ_doing.getSelected();
        models.List doneList = (models.List) civ_done.getSelected();

        if (civ_doing.validationIsIdRepeated(toDoList.getId()) ||
                civ_done.validationIsIdRepeated(toDoList.getId(), doingList.getId())) {
            mAnimations.shakeValidation(bt_save);
            return;
        }

        mUserService.configAccount(board, toDoList, doingList, doneList);
        mCustomToast.showToast(settings_saved);

        if (getActivity() instanceof InitialConfigActivity)
            MainActivity_.intent(getActivity()).start();
        else if (getActivity() instanceof ConfigActivity)
            EventBus.getDefault().post(EventTask.TABS_LISTS_UPDATE_DATA_SOURCE);
    }
}
