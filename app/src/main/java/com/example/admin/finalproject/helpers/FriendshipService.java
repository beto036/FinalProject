package com.example.admin.finalproject.helpers;

import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.entities.User;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by admin on 12/1/2016.
 */

public interface FriendshipService {
    @GET("collections/friends")
    Observable<List<Friendship>> getFriends(@Query("q") String q, @Query("apiKey") String apiKey);

    @POST("collections/friends")
    Observable<Friendship> saveFriend(@Query("apiKey") String apiKey, @Body Friendship friendship);

    @PUT("collections/friends/{id}")
    Observable<Friendship> updateFriend(@Path("id") String id, @Query("apiKey") String apiKey, @Body Friendship friendship);
}
