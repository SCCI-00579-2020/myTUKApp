package com.example.tuktradecircle.activities;

// MyFirebaseMessagingService.java
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Handle incoming messages here
        // You can display notifications, update UI, etc.
    }
}
