package com.example.strollsafe.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.strollsafe.GeofencingMapsActivity;
import com.example.strollsafe.R;
import com.example.strollsafe.pwd.PWD;
import com.example.strollsafe.ui.location.MapsActivity;
import com.example.strollsafe.utils.DatabaseManager;
import com.example.strollsafe.utils.location.BackgroundLocationWork;
import com.example.strollsafe.utils.location.LocationManager;
import com.example.strollsafe.utils.location.LocationPermissionManager;

import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class PWDActivity extends AppCompatActivity {
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;
    RealmConfiguration config;
    App app;
    DatabaseManager databaseManager;
    private final String APP_ID = "strollsafe-pjbnn";
    Realm realmDatabase;
    private EditText editEmail;
    private EditText editPassword;
    boolean isUserLoggedIn;
    PWD account;

    // location permissions
    private final int PERMISSION_REQUEST_CODE = 200;
    private final String[] locationPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private final String[] backgroundPermission = {Manifest.permission.ACCESS_BACKGROUND_LOCATION};
    private LocationPermissionManager locationPermissionManager;
    private LocationManager locationManager;
    private WorkRequest backgroundWorkRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();

        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();

        locationPermissionManager = LocationPermissionManager.getInstance(PWDActivity.this);
        locationManager = LocationManager.getInstance(PWDActivity.this);

        if (!locationPermissionManager.checkPermissions(locationPermissions)) {
            locationPermissionManager.askPermissions(PWDActivity.this,
                    locationPermissions, PERMISSION_REQUEST_CODE);
            if (!locationPermissionManager.checkPermissions(backgroundPermission)) {
                locationPermissionManager.askPermissions(PWDActivity.this,
                        locationPermissions, PERMISSION_REQUEST_CODE);
            }
        } else {
            startLocationWork();
        }


        setContentView(R.layout.activity_pwd);
        TextView code = findViewById(R.id.viewPWDCODE);
        code.setText(pwdPreferences.getString("code","error"));
        configureSignout();
        configureViewMap();
        //was testing an update timer
//        Timer timer = new Timer();
//        TimerTask update = new TimerTask() {
//            @Override
//            public void run() {
//                login(pwdPreferences.getString("email","error"),pwdPreferences.getString("password","error"));
//                database.executeTransaction(transaction -> {
//                    transaction.insertOrUpdate(account);
//                });
//            }
//        };
//        timer.scheduleAtFixedRate(update,01,5*(60*1000));
    }

    public void configureSignout() {
        Button PWD = (Button) findViewById(R.id.pwdSignOut);
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
                finish();
            }
        });
    }

    public void configureViewMap() {
        Button toMap = (Button) findViewById(R.id.btn_showMap);
        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PWDActivity.this, MapsActivity.class));
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, locationPermissions, grantResults);
        if (!locationPermissionManager.handlePermissionResult(PWDActivity.this, requestCode,
                locationPermissions, grantResults)) {
            startLocationWork();
        }
    }

    private void startLocationWork() {
        backgroundWorkRequest = new OneTimeWorkRequest.Builder(BackgroundLocationWork.class)
                .addTag("LocationWork")
                .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MAX_BACKOFF_MILLIS, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(PWDActivity.this).enqueue(backgroundWorkRequest);
    }

}
