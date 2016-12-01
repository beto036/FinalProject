package com.example.admin.finalproject.helpers;

import android.util.Log;

import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.entities.User;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.List;

import okhttp3.OkHttpClient;
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
                    .client(new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build())
                    .build();
        }

        public static Observable<List<User>> create(String user, String pass) {
            Retrofit retrofit = create();
            UserService userService = retrofit.create(UserService.class);
            String query = "{\"username\":\"" + user + "\", \"password\":\"" + pass + "\"}";
            return userService.getUser(query, API_KEY);
        }

        public static Observable<List<User>> getUserByEmail(String email) {
            Retrofit retrofit = create();
            UserService userService = retrofit.create(UserService.class);
            String query = "{\"email\":\"" + email + "\"}";
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

        public static Observable<Event> insertEvent(Event event) {
            Retrofit retrofit = create();
            EventService userService = retrofit.create(EventService.class);
            return userService.insertEvent(API_KEY, event);
        }

        public static Observable<User> update(User user, String oid) {
            Retrofit retrofit = create();
            Log.d(TAG, "update: " + user);
            UserService userService = retrofit.create(UserService.class);
            return userService.updateUser(oid, API_KEY, user);
        }

        public static Observable<List<Friendship>> getFriends(User userApp, User userFriend) {
            Retrofit retrofit = create();
            String q = "{\"$or\":[{\"senderId\":\"" + userApp.getId().get$oid()
                    + "\",\"receiverId\":\"" + userFriend.getId().get$oid() + "\"}," +
                    "{\"senderId\":\"" + userFriend.getId().get$oid()
                    + "\",\"receiverId\":\"" + userApp.getId().get$oid() + "\"}"+
                    "]}";
            Log.d(TAG, "getFriends: " + q);
            FriendshipService friendshipService = retrofit.create(FriendshipService.class);
            return friendshipService.getFriends(q, API_KEY);
        }

        public static Observable<Friendship> saveFriend(Friendship friendship) {
            Retrofit retrofit = create();
            FriendshipService friendshipService = retrofit.create(FriendshipService.class);
            return friendshipService.saveFriend(API_KEY,friendship);
        }

        public static Observable<Friendship> updateFriendship(Friendship friendship) {
            Retrofit retrofit = create();
            FriendshipService friendshipService = retrofit.create(FriendshipService.class);
            return friendshipService.updateFriend(friendship.getId().get$oid(), API_KEY, friendship);
        }
    }

}
