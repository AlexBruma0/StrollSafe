package com.example.strollsafe.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.example.strollsafe.pwd.PWDLocation;
import com.example.strollsafe.utils.GeofencingMapsActivity;
import com.example.strollsafe.R;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.strollsafe.utils.DatabaseManager;
import com.example.strollsafe.utils.GeofencingNotification;
import com.google.android.gms.location.Geofence;

import org.bson.Document;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoCollection;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CaregiverPwdListActivity extends AppCompatActivity {

    App app;
    DatabaseManager databaseManager;
    String TAG = "ListOfPWDActivity";
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;
    ProgressDialog progressDialog;
    private final String BATTERY_NOTIFICATION_CHANNEL = "Battery Notifications";
    private final String IDLE_NOTIFICATION_CHANNEL = "Idle Device Notifications";
    private final String SAFEZONE_NOTIFICATION_CHANNEL = "Safe Zone Notifications";
    private final int BATTERY_CHECK_TIMER = 2 * 60 * 1000; // The first number is the amount of minutes
    private final int LOCATION_CHECK_TIMER = 10 * 1000; // Will check for updated patient locations every 45 seconds
    private static final long IDLE_MINUTES = 60 * 12; // 12 hours
    private GeofencingNotification recentNotification;
    int id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();
        setContentView(R.layout.activity_listofpwd);
        Toolbar topBar = findViewById(R.id.toolbar);
        setSupportActionBar(topBar);

        final Handler handler = new Handler();

        // check for pwd battery percentage
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, BATTERY_CHECK_TIMER);
                checkPatientsBatteryLevel();
            }
        }, 0);

        // Check for idle pwds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, LOCATION_CHECK_TIMER);
                idleNotification();
                safeZoneNotification();
            }
        }, 0);

        checkPatientsBatteryLevel();
        configureMap1();
        configureMap2();
        configureMap3();
        configureDelete1();
        configureDelete2();
        configureDelete3();
        stylePage();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_options, menu);
        return true;
    }

    public void checkPatientsBatteryLevel() {
        app.currentUser().refreshCustomData(refreshResult -> {
            if(refreshResult.isSuccess()) {
                ArrayList<String> linkedPwds = (ArrayList<String>) refreshResult.get().get("patients");
                MongoCollection userCollection = databaseManager.getUsersCollection();

                for(String userId : linkedPwds) {
                    userCollection.findOne(new Document("userId", userId)).getAsync(new App.Callback() {
                        @Override
                        public void onResult(App.Result result) {
                            if(result.isSuccess()) {
                                Document pwdInfo = (Document) result.get();
                                int batLevel = (int) pwdInfo.get("batteryLife");
                                String notificationTitle = pwdInfo.get("firstName") + "'s device has a low battery!";
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                    NotificationChannel Channel = new NotificationChannel(BATTERY_NOTIFICATION_CHANNEL,"Battery Notifications",NotificationManager.IMPORTANCE_HIGH);

                                    NotificationManager manager = getSystemService(NotificationManager.class);
                                    manager.createNotificationChannel(Channel);
                                }

                                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                if(batLevel < 25 && batLevel > 20) {
                                    notificationManager.notify(0, buildNotification(CaregiverPwdListActivity.this, BATTERY_NOTIFICATION_CHANNEL, notificationTitle, "Battery is getting low!"));
                                } else if(batLevel <= 20 && batLevel >= 10) {
                                    notificationManager.notify(1, buildNotification(CaregiverPwdListActivity.this, BATTERY_NOTIFICATION_CHANNEL, notificationTitle, "Battery is LOW!"));
                                } else if(batLevel <= 10 && batLevel >= 5) {
                                    notificationManager.notify(2, buildNotification(CaregiverPwdListActivity.this, BATTERY_NOTIFICATION_CHANNEL, notificationTitle, "Battery is getting critically low!"));
                                } else if(batLevel <= 5) {
                                    notificationManager.notify(3, buildNotification(CaregiverPwdListActivity.this, BATTERY_NOTIFICATION_CHANNEL, notificationTitle, "Battery is critically low!"));
                                }
                                id++;
                            }
                        }
                    });
                }
            }
        });
    }

    private Notification buildNotification(Context context, String channelId, String title, String content) {
        Notification builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up
                .setAutoCancel(false)
                .build();
        return builder;
    }

    public void idleNotification() {
        Objects.requireNonNull(app.currentUser()).refreshCustomData(refreshResult -> {
            if(refreshResult.isSuccess()) {
                ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                for(String userId : patientsList) {
                    MongoCollection userCollection = databaseManager.getUsersCollection();
                    userCollection.findOne(new Document("userId", userId)).getAsync(result -> {
                        if(result.isSuccess()) {
                            Document pwdData = (Document) result.get();
                            ArrayList<Document> pwdLocations = (ArrayList<Document>) pwdData.get("locations");

                            if(pwdLocations != null) {
                                PWDLocation lastLocation = new PWDLocation(pwdLocations.get(pwdLocations.size() - 1));

                                Duration duration = Duration.between(lastLocation.getInitialDateTime(), lastLocation.getLastHereDateTime());
                                if (duration.toMinutes() >= IDLE_MINUTES) {
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                        NotificationChannel Channel = new NotificationChannel(IDLE_NOTIFICATION_CHANNEL,"Idle Device Notifications",NotificationManager.IMPORTANCE_HIGH);

                                        NotificationManager manager = getSystemService(NotificationManager.class);
                                        manager.createNotificationChannel(Channel);

                                        manager.notify(0, buildNotification(CaregiverPwdListActivity.this, IDLE_NOTIFICATION_CHANNEL, pwdData.get("firstName") + " " + pwdData.get("lastName") + "'s device is idle", "Please check on your patient"));

                                    }

//                                    NotificationChannel channel = new NotificationChannel("idle_alert",
//                                            "PWD Idle", NotificationManager.IMPORTANCE_DEFAULT);
//                                    NotificationManager manager = context.getSystemService(NotificationManager.class);
//                                    manager.createNotificationChannel(channel);
//
//                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                                            context, "idle_alert");
//                                    builder.setSmallIcon(R.drawable.ic_launcher_background);
//                                    builder.setContentTitle("PWD Idle Alert");
//                                    builder.setContentText("PWD has been idle for " + IDLE_MINUTES + " minutes!");
//
//                                    notification = builder.build();
//                                    notificationManagerCompat = NotificationManagerCompat.from(context);
//                                    notificationManagerCompat.notify("idle_alert", 1, notification);
                                }
                            }

                        }
                    });
                }
//                String userId = patientsList.get(0);
//
//
//                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CaregiverPwdListActivity.this);
//                managerCompat.notify(1, builder.build());
//
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                    NotificationChannel Channel = new NotificationChannel("Battery Notification","Battery notification",NotificationManager.IMPORTANCE_DEFAULT);
//                    NotificationManager manager = getSystemService(NotificationManager.class);
//                    manager.createNotificationChannel(Channel);
//                }

                 //after IDLE_MINUTES, if the location has not changed, notify user

            }
        });
    }

    public void safeZoneNotification() {
        Objects.requireNonNull(app.currentUser()).refreshCustomData(refreshResult -> {
            if(refreshResult.isSuccess()) {
                ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                for(String userId : patientsList) {
                    MongoCollection userCollection = databaseManager.getUsersCollection();
                    userCollection.findOne(new Document("userId", userId)).getAsync(result -> {
                        if(result.isSuccess()) {
                            Document pwdData = (Document) result.get();
                            ArrayList<Document> pwdSafeZoneNotifications = (ArrayList<Document>) pwdData.get("safezoneNotifications");

                            if(pwdSafeZoneNotifications != null) {
                                GeofencingNotification geofencingNotification = new GeofencingNotification(pwdSafeZoneNotifications.get(pwdSafeZoneNotifications.size() - 1));

                                if(recentNotification != null) {
                                    if(geofencingNotification.getTimestamp() == recentNotification.getTimestamp()) {
                                        return;
                                    }
                                }
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                    NotificationChannel Channel = new NotificationChannel(SAFEZONE_NOTIFICATION_CHANNEL,"Safe Zone Notifications",NotificationManager.IMPORTANCE_HIGH);

                                    NotificationManager manager = getSystemService(NotificationManager.class);
                                    manager.createNotificationChannel(Channel);

                                    String transitionType = "";
                                    switch(geofencingNotification.getTransition()) {
                                        case Geofence.GEOFENCE_TRANSITION_ENTER:
                                            transitionType = "entered";
                                            break;

                                        case Geofence.GEOFENCE_TRANSITION_EXIT:
                                            transitionType = "exited";
                                            break;

                                        case Geofence.GEOFENCE_TRANSITION_DWELL:
                                            transitionType = "dwelled";
                                            break;
                                    }
                                    String title = pwdData.get("firstName") + " " + pwdData.get("lastName") + " has " + transitionType + " " + geofencingNotification.getSafeZoneName() + "!";
                                    manager.notify(0, buildNotification(CaregiverPwdListActivity.this, SAFEZONE_NOTIFICATION_CHANNEL, title, ""));

                                }
                                recentNotification = geofencingNotification;
                            }

                        }
                    });
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionsMenuItem:
                Log.i(TAG, "Starting SettingsActivity form CaregiverPwdListActivity");
                startActivity(new Intent(CaregiverPwdListActivity.this, SettingsActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void configureMap1(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map1);
        PWD.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                startProgressDialog("Opening Safe Zone Manager...", false);
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(0);
                        Intent intent = new Intent(CaregiverPwdListActivity.this, GeofencingMapsActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                    dismissProgressDialog();
                });
            }
        });
    }

    public void configureMap2(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map2);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog("Opening Safe Zone Manager...", false);
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(1);
                        Intent intent = new Intent(CaregiverPwdListActivity.this, GeofencingMapsActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                    dismissProgressDialog();
                });
            }
        });
    }

    public void configureMap3(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map3);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog("Opening Safe Zone Manager...", false);
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(2);
                        Intent intent = new Intent(CaregiverPwdListActivity.this, GeofencingMapsActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                    dismissProgressDialog();
                });
            }
        });
    }

    public void configureDelete1(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete1);
        ImageButton A = (ImageButton) findViewById(R.id.Map1);
        Button B = (Button) findViewById(R.id.NAME1);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(0);
                        remove(userId);

                        B.setText("CLICK HERE TO ACTIVATE");
                        PWD.setVisibility(View.GONE);
                        A.setVisibility(View.GONE);
                        B.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void configureDelete2(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete2);
        ImageButton A = (ImageButton) findViewById(R.id.Map2);
        Button B = (Button) findViewById(R.id.NAME2);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(1);
                        remove(userId);

                        B.setText("CLICK HERE TO ACTIVATE");
                        PWD.setVisibility(View.GONE);
                        A.setVisibility(View.GONE);
                        B.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void configureDelete3(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete3);
        ImageButton A = (ImageButton) findViewById(R.id.Map3);
        Button B = (Button) findViewById(R.id.NAME3);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(2);
                        remove(userId);

                        B.setText("CLICK HERE TO ACTIVATE");
                        PWD.setVisibility(View.GONE);
                        A.setVisibility(View.GONE);
                        B.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void stylePage() {
        startProgressDialog("Loading patients...", false);
        app.currentUser().refreshCustomData(refreshResult -> {
            ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
            if(patientsList != null) {
                if(patientsList.size() >= 3) {
                    findViewById(R.id.pwdListTitle).setVisibility(View.INVISIBLE);
                    findViewById(R.id.pwdListEmailEntry).setVisibility(View.INVISIBLE);
                    findViewById(R.id.pwdListAddButton).setVisibility(View.INVISIBLE);
                    dismissProgressDialog();

                } else {
                    findViewById(R.id.pwdListTitle).setVisibility(View.VISIBLE);
                    findViewById(R.id.pwdListEmailEntry).setVisibility(View.VISIBLE);
                    findViewById(R.id.pwdListAddButton).setVisibility(View.VISIBLE);
                    dismissProgressDialog();
                }
                for(int x = 0; x < patientsList.size(); x++) {
                    MongoCollection userCollection = databaseManager.getUsersCollection();
                    int finalX = x;
                    userCollection.findOne(new Document("userId", patientsList.get(x))).getAsync(new App.Callback() {
                        @Override
                        public void onResult(App.Result result) {
                            if(result.isSuccess()) {
                                Document pwdInfo = (Document) result.get();
                                Button name1 = (Button) findViewById(R.id.NAME1);
                                ImageButton delete1 = (ImageButton) findViewById(R.id.delete1);

                                Button name2 = (Button) findViewById(R.id.NAME2);
                                ImageButton delete2 = (ImageButton) findViewById(R.id.delete2);
                                ImageButton map2 = (ImageButton) findViewById(R.id.Map2);

                                ImageButton map1 = (ImageButton) findViewById(R.id.Map1);
                                Button name3 = (Button) findViewById(R.id.NAME3);
                                ImageButton delete3 = (ImageButton) findViewById(R.id.delete3);
                                ImageButton map3 = (ImageButton) findViewById(R.id.Map3);

                                switch (finalX) {
                                    case 0:
                                        Log.i(TAG, "STYLING THE FIRST BUTTON");
                                        name1.setText(pwdInfo.get("firstName") + " " + pwdInfo.get("lastName"));
                                        name1.setVisibility(View.VISIBLE);
                                        delete1.setVisibility(View.VISIBLE);
                                        map1.setVisibility(View.VISIBLE);

                                        if(finalX == patientsList.size() - 1) {
                                            dismissProgressDialog();
                                            delete2.setVisibility(View.INVISIBLE);
                                            name2.setVisibility(View.INVISIBLE);
                                            map2.setVisibility(View.INVISIBLE);

                                            delete3.setVisibility(View.INVISIBLE);
                                            name3.setVisibility(View.INVISIBLE);
                                            map3.setVisibility(View.INVISIBLE);
                                        }

                                        break;

                                    case 1:
                                        Log.i(TAG, "STYLING THE SECOND BUTTON");
                                        name2.setText(pwdInfo.get("firstName") + " " + pwdInfo.get("lastName"));
                                        delete2.setVisibility(View.VISIBLE);
                                        name2.setVisibility(View.VISIBLE);
                                        map2.setVisibility(View.VISIBLE);

                                        if(finalX == patientsList.size() - 1) {
                                            dismissProgressDialog();
                                            delete3.setVisibility(View.INVISIBLE);
                                            name3.setVisibility(View.INVISIBLE);
                                            map3.setVisibility(View.INVISIBLE);
                                        }
                                        break;

                                    case 2:
                                        Log.i(TAG, "STYLING THE THIRD BUTTON");
                                        name3.setText(pwdInfo.get("firstName") + " " + pwdInfo.get("lastName"));
                                        delete3.setVisibility(View.VISIBLE);
                                        name3.setVisibility(View.VISIBLE);
                                        map3.setVisibility(View.VISIBLE);

                                        if(finalX == patientsList.size() - 1) {
                                            dismissProgressDialog();
                                        }
                                        break;
                                }
                            }
                        }
                    });
                }
            }
            dismissProgressDialog();
        });
    }

    public void add(View view){
        startProgressDialog("Adding patient...", false);
        app.currentUser().refreshCustomData(refreshResult -> {
            ArrayList<String> patientsArray = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
            if(patientsArray == null || patientsArray.size() >= 3) {
                dismissProgressDialog();
                Toast.makeText(CaregiverPwdListActivity.this, "PWD limit max already reached.", Toast.LENGTH_SHORT).show();
                return;
            }

            EditText editText = (EditText) findViewById(R.id.pwdListEmailEntry);
            String code = editText.getText().toString();

            MongoCollection userCollection = databaseManager.getUsersCollection();
            userCollection.findOne(new Document("email", code)).getAsync(new App.Callback() {
                @Override
                public void onResult(App.Result result) {
                    Document pwdInfo = (Document) result.get();
                    if(pwdInfo == null) {
                        dismissProgressDialog();
                        Toast.makeText(CaregiverPwdListActivity.this, "PWD can not be found.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(patientsArray.contains(pwdInfo.get("userId"))) {
                        dismissProgressDialog();
                        Toast.makeText(CaregiverPwdListActivity.this, "This PWD is already linked to your account.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Document careGiverData = new Document("userId", app.currentUser().getId());
                    Document update =  new Document("patients", pwdInfo.get("userId"));
                    userCollection.updateOne(careGiverData, new Document("$push", update)).getAsync(new App.Callback() {
                        @Override
                        public void onResult(App.Result result) {
                            if(result.isSuccess()) {
                                dismissProgressDialog();
                                Toast.makeText(CaregiverPwdListActivity.this, "PWD has been linked to your account.", Toast.LENGTH_SHORT).show();
                                EditText enterPatientEmail = findViewById(R.id.pwdListEmailEntry);
                                enterPatientEmail.getText().clear();
                                stylePage();
                            } else {
                                dismissProgressDialog();
                                Toast.makeText(CaregiverPwdListActivity.this, "Error linked PWD to your account.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        });
    }

    public void remove(String userId) {
        startProgressDialog("Removing patient...", false);
        app.currentUser().refreshCustomData(refreshResult -> {
            MongoCollection userCollection = databaseManager.getUsersCollection();
            Document careGiverData = new Document("userId", app.currentUser().getId());
            Document update =  new Document("patients", userId);
            userCollection.updateOne(careGiverData, new Document("$pull", update)).getAsync(new App.Callback() {
                @Override
                public void onResult(App.Result result) {
                    if(result.isSuccess()) {
                        dismissProgressDialog();
                        Toast.makeText(CaregiverPwdListActivity.this, "PWD has been removed to your account.", Toast.LENGTH_SHORT).show();
                        stylePage();
                    } else {
                        dismissProgressDialog();
                        Toast.makeText(CaregiverPwdListActivity.this, "Error removing PWD to your account.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }

    private void startProgressDialog(String message, boolean cancelable) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

}
