package com.example.admin.finalproject.helpers;

import com.example.admin.finalproject.entities.User;

import java.util.List;

import retrofit.http.EncodedPath;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by admin on 11/20/2016.
 */
public interface UserService {

    @GET("collections/user")
    Observable<List<User>> getUser(@Query("q") String q, @Query("apiKey") String apiKey);

    @POST("collections/user")
    Observable<User> insertUser(@Query("apiKey") String apiKey, @Body User user);

    @PUT("collections/user/{id}")
    Observable<User> updateUser(@Path("id") String id, @Query("apiKey") String apiKey, @Body User user);

    @GET("collections/user/{id}")
    Observable<User> getUserbyId(@Path("id")String id, @Query("apiKey") String apiKey);

}
