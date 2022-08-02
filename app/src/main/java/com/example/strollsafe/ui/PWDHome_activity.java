package com.example.strollsafe.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.example.strollsafe.R;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.strollsafe.R;
import com.example.strollsafe.utils.DatabaseManager;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoCollection;

public class PWDHome_activity extends AppCompatActivity{
    DatabaseManager databaseManager;
    App app;
    int battery;
    private final String APP_ID = "strollsafe-pjbnn";
    Realm realmDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_home);
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();
//        Toolbar topBar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(topBar);
        //callNumber();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                updatePWDBattery();
                handler.postDelayed(this, 120000); //now is every 2 minutes
            }
        }, 120000);
    }

    public void updatePWDBattery(){
        int battery;
        BroadcastReceiver mBatInfoReciever = new BroadcastReceiver() {
            @Override
            int battery;
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                battery = level;
            }
        };
        app.currentUser().refreshCustomData(refreshResult -> {
            MongoCollection userCollection = databaseManager.getUsersCollection();
            userCollection.findOne(new Document("batteryLife", 100)).getAsync(new App.Callback() {
                @Override
                public void onResult(App.Result result) {
                    Document pwdInfo = (Document) result.get();
                    if(pwdInfo == null) {
                        Toast.makeText(PWDHome_activity.this, "PWD can not be found.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Document careGiverData = new Document("userId", app.currentUser().getId());
                    BroadcastReceiver mBatInfoReciever = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                            Document update =  new Document("batteryLife", level);
                            userCollection.updateOne(careGiverData,update).getAsync(new App.Callback() {
                                @Override
                                public void onResult(App.Result result) {
                                    if(result.isSuccess()) {
                                        Toast.makeText(PWDHome_activity.this, "PWD has been linked to your account.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PWDHome_activity.this, "Error linked PWD to your account.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    };
                }
            });
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

}
