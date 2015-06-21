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
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
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

    public void moveCardFromTodoToDoing(final Card card, final Callback<Card> createCallback) {
        final Callback<Response> deleteCallback = new Callback<Response>() {
            @Override public void success(Response response, Response response2) {
                String idList = userService.getDoingList().getId();
                createCard(idList, card.getName(), createCallback);
            }

            @Override public void failure(RetrofitError error) {
                createCallback.failure(error);
            }
        };

        deleteCard(card.getId(), deleteCallback);
    }
    public void deleteCard(String idCard, Callback<Response> callback) {
        restApi.deleteCard(idCard, trello_key, userService.getToken(), callback);
    }

    public void createCard(String idList, String name, Callback<Card> callback) {
        restApi.createCard(idList, name, trello_key, userService.getToken(), "name", "idList", callback);
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

        @POST("/cards")
        void createCard(
                @Query("idList") String idList,
                @Query("name") String name,
                @Query("key") String key,
                @Query("token") String token,
                @Query("fields") String fieldName,
                @Query("fields") String fieldIdList,
                Callback<Card> cb
        );

        @DELETE("/cards/{id}")
        void deleteCard(
                @Path("id") String id,
                @Query("key") String key,
                @Query("token") String token,
                Callback<Response> cb
        );
    }

    public interface CustomCallback  {
        void onResponse(boolean success);
    }
}

