package com.example.admin.finalproject.helpers;

import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.User;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by admin on 11/20/2016.
 */

public interface EventService {
    @GET("collections/event")
    Observable<List<Event>> getEvents(@Query("q") String q, @Query("apiKey") String apiKey);

    @POST("collections/event")
    Observable<Event> insertEvent(@Query("apiKey") String apiKey, @Body Event event);
}
