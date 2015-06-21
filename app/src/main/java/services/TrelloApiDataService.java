package services;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

import models.Board;
import models.Card;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


@EBean()
public class TrelloApiDataService {
    @StringRes protected String trello_key;
    @Bean protected UserService userService;

    private TrelloRestApi restApi;

    @AfterInject protected void init() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.trello.com/1")
                .build();
        restApi = restAdapter.create(TrelloRestApi.class);
    }

    public void getBoards(Callback<List<Board>> response) {
        restApi.getBoards(trello_key, userService.getToken(), "name", response);
    }

    public void getLists(String id_board, Callback<List<models.List>> response) {
        restApi.getLists(id_board, trello_key, userService.getToken(), "name", "idBoard", response);
    }

    public void getCards(String id_list, Callback<List<Card>> response) {
        restApi.getCards(id_list, trello_key, userService.getToken(), "name", "idList", response);
    }

    private interface TrelloRestApi {
        @GET("/members/me/boards")
        void getBoards(
                @Query("key") String key,
                @Query("token") String token,
                @Query("fields") String name,
                Callback<List<Board>> response
        );

        @GET("/boards/{id_board}/lists")
        void getLists(
                @Path("id_board") String id_board,
                @Query("key") String key,
                @Query("token") String token,
                @Query("fields") String name,
                @Query("fields") String idBoard,
                Callback<List<models.List>> response
        );

        @GET("/lists/{id_list}/cards")
        void getCards(
                @Path("id_list") String id_list,
                @Query("key") String key,
                @Query("token") String token,
                @Query("fields") String name,
                @Query("fields") String idList,
                Callback<List<Card>> response
        );
    }
}

