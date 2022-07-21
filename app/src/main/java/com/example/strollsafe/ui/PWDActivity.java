package com.example.strollsafe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.strollsafe.R;
import com.example.strollsafe.pwd.PWD;
import com.example.strollsafe.ui.location.PWDLocationInformationActivity;
import com.example.strollsafe.utils.DatabaseManager;

import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();
        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        setContentView(R.layout.activity_pwd);
        TextView code = findViewById(R.id.viewPWDCODE);
        code.setText(pwdPreferences.getString("code","error"));
        configureSignout();
        configurePWDLocationInformation();
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
//                pwdPreferenceEditor.remove("email");
//                pwdPreferenceEditor.remove("Phone");
//                pwdPreferenceEditor.remove("L_name");
//                pwdPreferenceEditor.remove("F_name");
//                pwdPreferenceEditor.remove("password");
//                pwdPreferenceEditor.remove("id");
//                databaseManager.logoutOfRealm();
                finish();
            }
        });
    }

    public void configurePWDLocationInformation() {
        Button PWD = (Button) findViewById(R.id.GPSInfoButton);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PWDActivity.this, PWDLocationInformationActivity.class));
            }
        });
    }


//    public void back() {
//        Button PWD = (Button) findViewById(R.id.back1);
//        PWD.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//    }
}