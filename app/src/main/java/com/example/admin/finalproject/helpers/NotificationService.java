package com.example.admin.finalproject.helpers;

import com.example.admin.finalproject.entities.notification.SendNotificationRequest;
import com.example.admin.finalproject.entities.notification.SendNotificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by admin on 11/27/2016.
 */

public interface NotificationService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AIzaSyA7HyCaG9Uyujv7GdXdZZT2u5kTAmZ5poo"
    })
    @POST("fcm/send")
    Call<SendNotificationResponse> sendNotification(@Body SendNotificationRequest request);
}
