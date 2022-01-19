package com.redpepper.taxiapp.Fcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    SharedPreferences prefs;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        SharedPreferences prefs = getApplication().getSharedPreferences("app",MODE_PRIVATE);

        prefs.edit().putString("fcm_token",token).apply();

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {

            Log.d("blepo", "messg data: " + remoteMessage.getData());

            //Handle "Search For Taxi" Messages
            if(remoteMessage.getData().get("ntf_type").equalsIgnoreCase("sft")){

                if(remoteMessage.getData().get("action").equalsIgnoreCase("driver_accepted_ride")){

                    int ride_id = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("rideId")));

                    String comingDriverId = remoteMessage.getData().get("driverId");

                    Intent newMessageFromFCMIntent = new Intent();

                    newMessageFromFCMIntent.setAction("FCM MainActivity");
                    newMessageFromFCMIntent.putExtra("message_action", "sft");
                    newMessageFromFCMIntent.putExtra("ride_id",ride_id);
                    newMessageFromFCMIntent.putExtra("driver_id",comingDriverId);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(newMessageFromFCMIntent);
                }
            }

            //Handle "Handle The Ride" Messages
            if(remoteMessage.getData().get("ntf_type").equalsIgnoreCase("htr")){

                if(remoteMessage.getData().get("action").equalsIgnoreCase("driver_arrived")){

                    Intent newMessageFromFCMIntent = new Intent();

                    newMessageFromFCMIntent.setAction("FCM MainActivity");
                    newMessageFromFCMIntent.putExtra("message_action", "htr");
                    newMessageFromFCMIntent.putExtra("action_type","driver_arrived");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(newMessageFromFCMIntent);

                }

                if(remoteMessage.getData().get("action").equalsIgnoreCase("passenger_onboard")){

                    Intent newMessageFromFCMIntent = new Intent();

                    newMessageFromFCMIntent.setAction("FCM MainActivity");
                    newMessageFromFCMIntent.putExtra("message_action", "htr");
                    newMessageFromFCMIntent.putExtra("action_type","passenger_onboard");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(newMessageFromFCMIntent);

                }

                if(remoteMessage.getData().get("action").equalsIgnoreCase("ride_completed")){

                    Intent newMessageFromFCMIntent = new Intent();

                    newMessageFromFCMIntent.setAction("FCM MainActivity");
                    newMessageFromFCMIntent.putExtra("message_action", "htr");
                    newMessageFromFCMIntent.putExtra("action_type","ride_completed");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(newMessageFromFCMIntent);

                }

            }

            //if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }
    }
}