package com.example.admin.finalproject.helpers;

import com.example.admin.finalproject.entities.Invitation;
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

public interface InvitationService {
    @GET("collections/invitation")
    Observable<List<Invitation>> getInvitations(@Query("q") String q, @Query("apiKey") String apiKey);


    @PUT("collections/invitation/{id}")
    Observable<Invitation> updateInvitation(@Path("id") String id, @Query("apiKey") String apiKey, @Body Invitation invitation);


    @POST("collections/invitation")
    Observable<Invitation> insertInvitation(@Query("apiKey") String apiKey, @Body Invitation invitation);
}
