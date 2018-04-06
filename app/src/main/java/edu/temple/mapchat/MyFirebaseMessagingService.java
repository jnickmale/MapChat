package edu.temple.mapchat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        // ...

//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

        JSONObject jsonMessage = null;
        try {
            jsonMessage = new JSONObject(remoteMessage.getData().get("payload"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setAction("com.example.broadcast.AMessage");
        try {
            intent.putExtra("message",jsonMessage.get("message").toString());
            intent.putExtra("from",jsonMessage.get("user").toString());
            intent.putExtra("to",jsonMessage.get("to").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendBroadcast(intent);

    }
}
