package com.example.admin.finalproject.helpers;

import android.util.Log;

import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.User;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 11/6/16.
 */

public class RetrofitHelper {

    public static final String BASE_URL = "https://api.mongolab.com/api/1/databases/mobileapp/";
    public static final String API_KEY = "djZ5Olq7x94SzNkxDFI8rx-ZCV4ZZmU5";

    public static class Factory {
        private static final String TAG = "RetrofitHelperTAG_";

        public static Retrofit create() {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build();
        }

        public static Observable<List<User>> create(String user, String pass) {
            Retrofit retrofit = create();
            UserService userService = retrofit.create(UserService.class);
            String query = "{\"username\":\"" + user + "\", \"password\":\"" + pass + "\"}";
            return userService.getUser(query, API_KEY);
        }

        public static Observable<User> insert(User user) {
            Retrofit retrofit = create();
            UserService userService = retrofit.create(UserService.class);
            return userService.insertUser(API_KEY, user);
        }

        public static Observable<List<Event>> getEvents(String userId, boolean isAdmin) {
            Retrofit retrofit = create();
            EventService eventService = retrofit.create(EventService.class);
            String query = "{\"userId\":\"" + userId + "\", \"isAdmin\":" + isAdmin + "}";
            return eventService.getEvents(query, API_KEY);
        }
    }

}
