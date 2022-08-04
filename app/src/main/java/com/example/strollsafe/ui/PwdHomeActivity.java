package com.example.strollsafe.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.strollsafe.R;
import com.example.strollsafe.utils.DatabaseManager;
import com.example.strollsafe.utils.GeofenceBroadcastReceiver;
import com.example.strollsafe.utils.SafeZone;
import com.example.strollsafe.utils.SafeZoneManager;
import com.example.strollsafe.utils.location.BackgroundLocationWork;
import com.example.strollsafe.utils.location.LocationManager;
import com.example.strollsafe.utils.location.LocationPermissionManager;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.bson.Document;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoCollection;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PwdHomeActivity extends AppCompatActivity{
    DatabaseManager databaseManager;
    App app;
    BatteryManager batteryManager;
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;

    // location permissions
    private final int PERMISSION_REQUEST_CODE = 200;
    private final String[] locationPermissions = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };
    private final String[] backgroundPermission = {Manifest.permission.ACCESS_BACKGROUND_LOCATION};
    private LocationPermissionManager locationPermissionManager;
    private LocationManager locationManager;
    private WorkRequest backgroundWorkRequest;
    private SafeZoneManager safeZoneManager;
    private PendingIntent geofencePendingIntent;
    private GeofencingClient geofencingClient;
    private ArrayList<SafeZone> safeZones = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();

        locationPermissionManager = LocationPermissionManager.getInstance(PwdHomeActivity.this);
        locationManager = LocationManager.getInstance(PwdHomeActivity.this);
        if (!locationPermissionManager.checkPermissions(locationPermissions)) {
            locationPermissionManager.askPermissions(PwdHomeActivity.this,
                    locationPermissions, PERMISSION_REQUEST_CODE);
            if (!locationPermissionManager.checkPermissions(backgroundPermission)) {
                locationPermissionManager.askPermissions(PwdHomeActivity.this,
                        locationPermissions, PERMISSION_REQUEST_CODE);
            }
        } else {
            startLocationWork();
        }

        setContentView(R.layout.activity_pwd_home);
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();
        safeZoneManager = new SafeZoneManager(this);
        geofencingClient = safeZoneManager.getGeofencingClient();
        batteryManager = (BatteryManager) this.getSystemService(BATTERY_SERVICE);
        configureSignout();
//        Toolbar topBar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(topBar);
        //callNumber();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 2 * 60 * 1000); // every 2 minutes
                updatePWDBattery();
            }
        }, 0);
        registerSafeZones();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(this, 60 * 1000); // every minute
//                //registerSafeZones();
//            }
//        }, 0);
    }

    public void updatePWDBattery() {
        if(app.currentUser() != null) {
            app.currentUser().refreshCustomData(refreshResult -> {
                if(refreshResult.isSuccess()) {
                    MongoCollection userCollection = databaseManager.getUsersCollection();
                    Document pwdData = new Document("userId", app.currentUser().getId());
                    Document update =  new Document("batteryLife", batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
                    userCollection.updateOne(pwdData, new Document("$set", update)).getAsync(new App.Callback() {
                        @Override
                        public void onResult(App.Result result) {
                            if(result.isSuccess()) {
                                Toast.makeText(PwdHomeActivity.this, "Battery % has been updated.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PwdHomeActivity.this, "Battery % could not be updated.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }
    }

    public void SOS(View view){
        Uri number = Uri.parse("tel:9111");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    public void nonEmergency(View view){
        Uri number = Uri.parse("tel:5551234");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    public void configureSignout() {
        Button PWD = (Button) findViewById(R.id.Signout);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwdPreferenceEditor.remove("email");
                pwdPreferenceEditor.remove("Phone");
                pwdPreferenceEditor.remove("L_name");
                pwdPreferenceEditor.remove("F_name");
                pwdPreferenceEditor.remove("password");
                pwdPreferenceEditor.remove("id");
                databaseManager.logoutOfRealm();
                locationManager.stopLocationUpdates();
                startActivity(new Intent(PwdHomeActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, locationPermissions, grantResults);
        if (!locationPermissionManager.handlePermissionResult(PwdHomeActivity.this, requestCode,
                locationPermissions, grantResults)) {
            startLocationWork();
        }
    }

    private void startLocationWork() {
        backgroundWorkRequest = new OneTimeWorkRequest.Builder(BackgroundLocationWork.class)
                .addTag("LocationWork")
                .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MAX_BACKOFF_MILLIS, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(PwdHomeActivity.this).enqueue(backgroundWorkRequest);
    }

    @SuppressLint("MissingPermission")
    private void registerSafeZones() {
        app.currentUser().refreshCustomData(refreshResult -> {
           if(refreshResult.isSuccess()) {
               Document userInfo = refreshResult.get();
               ArrayList<Document> safeZoneDocuments = (ArrayList<Document>) userInfo.get("safezones");
               safeZones = new ArrayList<>();
               for (Document document : safeZoneDocuments) {
                   safeZones.add(new SafeZone(document));
               }
               for(SafeZone safeZone : safeZones) {
                   safeZoneManager.addNewGeofence(safeZone.getSafeZoneName(), safeZone.getLat(), safeZone.getLng(), (float) safeZone.getRadius());
               }

               geofencingClient.addGeofences(getGeofencingRequest(), getGeofencingPendingIntent())
                       .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Log.e("", "SUCCESSFUL GEOFENCE ADD");
                           }
                       }).addOnFailureListener(this, new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Log.e("", "FAILURE GEOFENCE ADD");
                           }
                       });
           }
        });
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(safeZoneManager.getGeofenceList());
        return builder.build();
    }

    private PendingIntent getGeofencingPendingIntent() {
        // Reuse the PendingIntent if we already have it.
//        if (geofencePendingIntent != null) {
//            return geofencePendingIntent;
//        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

}
