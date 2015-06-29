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
    @Bean protected UserService mUserService;
    private TrelloRestApi mRestApi;

    @AfterInject protected void init() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.trello.com/1")
                .build();
        mRestApi = restAdapter.create(TrelloRestApi.class);
    }

    public void getBoards(Callback<List<Board>> response) {
        mRestApi.getBoards(trello_key, mUserService.getToken(), "name", response);
    }

    public void getLists(String id_board, Callback<List<models.List>> response) {
        mRestApi.getLists(id_board, trello_key, mUserService.getToken(), "name", "idBoard", response);
    }

    public void getCards(String id_list, Callback<List<Card>> response) {
        mRestApi.getCards(id_list, trello_key, mUserService.getToken(), "name", "idList", "desc", response);
    }

    public void moveCardToTodoList(final Card card, final Callback<Card> createCallback) {
        String idList = mUserService.getToDoList().getId();
        moveCardTo(idList, "", card, createCallback);
    }

    public void moveCardToDoingList(final Card card, final Callback<Card> createCallback) {
        String idList = mUserService.getDoingList().getId();
        moveCardTo(idList, "", card, createCallback);
    }

    public void moveCardToDoneList(String desc, final Card card, final Callback<Card> createCallback) {
        String idList = mUserService.getDoneList().getId();
        moveCardTo(idList, desc, card, createCallback);
    }

    private void moveCardTo(final String idList, final String desc, final Card card, final Callback<Card> createCallback) {
        final Callback<Response> deleteCallback = new Callback<Response>() {
            @Override public void success(Response response, Response response2) {
                createCard(idList, card.getName(), desc, createCallback);
            }

            @Override public void failure(RetrofitError error) {
                createCallback.failure(error);
            }
        };

        deleteCard(card.getId(), deleteCallback);
    }


    public void deleteCard(String idCard, Callback<Response> callback) {
        if (callback == null) callback = placeHolderCallback;
        mRestApi.deleteCard(idCard, trello_key, mUserService.getToken(), callback);
    }

    public void createCard(String idList, String name, Callback<Card> callback) {
        createCard(idList, name, "", callback);
    }

    public void createCard(String idList, String name, String desc, Callback<Card> callback) {
        mRestApi.createCard(idList, name, desc, trello_key, mUserService.getToken(), "name", "idList", "desc", callback);
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
                @Query("fields") String fieldDesc,
                Callback<List<Card>> response
        );

        @POST("/cards")
        void createCard(
                @Query("idList") String idList,
                @Query("name") String name,
                @Query("desc") String desc,
                @Query("key") String key,
                @Query("token") String token,
                @Query("fields") String fieldName,
                @Query("fields") String fieldIdList,
                @Query("fields") String fieldDesc,
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

    private Callback<Response> placeHolderCallback = new Callback<Response>() {
        @Override public void success(Response response, Response response2) {
        }
        @Override public void failure(RetrofitError error) {}
    };
}

