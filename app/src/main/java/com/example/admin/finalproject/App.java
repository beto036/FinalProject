package com.example.admin.finalproject;

import android.app.Application;

import com.example.admin.finalproject.entities.User;

/**
 * Created by admin on 11/14/2016.
 */

public class App extends Application{

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}