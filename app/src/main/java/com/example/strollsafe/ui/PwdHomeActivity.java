package com.example.strollsafe.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;

import com.example.strollsafe.R;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strollsafe.utils.DatabaseManager;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoCollection;

public class PwdHomeActivity extends AppCompatActivity{
    DatabaseManager databaseManager;
    App app;
    BatteryManager batteryManager;
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        setContentView(R.layout.activity_pwd_home);
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();
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
                /* your longer code here */
            }
        }, 0);
    }

    public void updatePWDBattery() {
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
                finish();
            }
        });
    }

}
