package com.example.admin.finalproject.helpers;

import com.example.admin.finalproject.entities.notification.SendNotificationRequest;
import com.example.admin.finalproject.entities.notification.SendNotificationResponse;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 12/1/2016.
 */

public class NotificationRetrofitHelper {

    public static class Factory {

        private static final String TAG = "NotifRetrofitHelperTAG_";

        public static final String NOTIFICATION_BASE_URL = "https://fcm.googleapis.com/";

        public static Retrofit createNotification() {
            return new Retrofit.Builder()
                    .baseUrl(NOTIFICATION_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build())
                    .build();
        }

        public static Call<SendNotificationResponse> sendFriendRequest (SendNotificationRequest sendNotificationRequest) {
            Retrofit retrofit = createNotification();
            NotificationService notificationService = retrofit.create(NotificationService.class);
            return notificationService.sendNotification(sendNotificationRequest);
        }
    }

    }
