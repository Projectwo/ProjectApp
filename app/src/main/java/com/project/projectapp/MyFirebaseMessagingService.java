package com.project.projectapp;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

// MyFirebaseMessagingService.class
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String token;

    @Override
    public void onNewToken(String token){

        Log.d("FCM Log", "Refreshed token: "+token);

    }

    public String getToken(){

        return this.token;
    }

}