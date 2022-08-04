package com.example.strollsafe.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.strollsafe.ui.CaregiverPwdListActivity;
import com.example.strollsafe.ui.PwdHomeActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import androidx.core.app.NotificationCompat;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoCollection;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "GBR";

    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "TEST");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            for(Geofence gf : triggeringGeofences) {
                sendNotification(context, gf, geofencingEvent.getGeofenceTransition());

            }

            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofencingEvent);
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, "ERROR");
        }
    }

    private static String getGeofenceTransitionDetails(GeofencingEvent event) {
        String transitionString;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("mm-ss");
        String formattedDate = df.format(c.getTime());

        int geofenceTransition = event.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            transitionString = "IN-" + formattedDate;
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            transitionString = "OUT-" + formattedDate;
        } else {
            transitionString = "OTHER-" + formattedDate;
        }
        List<String> triggeringIDs;
        triggeringIDs = new ArrayList<>();
        for (Geofence geofence : event.getTriggeringGeofences()) {
            triggeringIDs.add(geofence.getRequestId());
        }
        return String.format("%s: %s", transitionString, TextUtils.join(", ", triggeringIDs));
    }

    private void sendNotification(Context context, Geofence geofence, int transitionType) {
        DatabaseManager databaseManager = new DatabaseManager(context);
        App app = databaseManager.getApp();

        app.currentUser().refreshCustomData(result -> {
            if (result.isSuccess()) {
                Document userData = result.get();
                ArrayList<Document> safeZoneNotification = (ArrayList<Document>) userData.get("safezoneNotifications");

                Document pwdData = new Document("userId", app.currentUser().getId());
                Document newSafeZoneNotification = new Document().append("name", geofence.getRequestId()).append("transition", transitionType).append("timestamp", System.currentTimeMillis());
                Document update = new Document("safezoneNotifications", newSafeZoneNotification);

                // Add the new safezone to the pwds safezone array
                MongoCollection userCollection = databaseManager.getUsersCollection();
                userCollection.updateOne(pwdData, new Document("$push", update)).getAsync(new App.Callback() {

                    @Override
                    public void onResult(App.Result result) {
                        if (result.isSuccess()) {
                            Log.e(TAG, "upladed safezone notificaito");
                        }
                    }
                });
            }
        });
    }
}
