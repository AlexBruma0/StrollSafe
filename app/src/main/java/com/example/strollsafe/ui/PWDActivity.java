package com.example.strollsafe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.strollsafe.R;
import com.example.strollsafe.pwd.PWD;

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
    Realm database;
    App app;
    PWD account;
    RealmConfiguration config;
    private final String appId = "strollsafe-pjbnn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        setContentView(R.layout.activity_pwd);
        TextView code = findViewById(R.id.viewPWDCODE);
        code.setText(pwdPreferences.getString("code","error"));
        setupRealm();
        configureSignout();
        configurePWDLocationInformation();
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
    public void setupRealm() {
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(appId).build());
    }

    // Before we can login into an account we must register it first
    public void createUserLogin(String email, String password) {
        app.getEmailPassword().registerUserAsync(email, password, it -> {
            if (it.isSuccess()) {
                Log.i("EXAMPLE", "Successfully registered user.");
            } else {
                Log.e("EXAMPLE", "Failed to register user: " + it.getError().getErrorMessage());
            }
        });

    }

    // Logs into the databased given a user name and password
    public void login(String email, String password) {
        AtomicReference<User> user = new AtomicReference<User>();
        Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);
        app.loginAsync(emailPasswordCredentials, it -> {
            if (it.isSuccess()) {
                Log.v("AUTH", "Successfully authenticated using an email and password.");
                user.set(app.currentUser());
                config = new SyncConfiguration.Builder(Objects.requireNonNull(app.currentUser()), Objects.requireNonNull(app.currentUser()).getId())
                        .name(appId)
                        .schemaVersion(2)
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();

                Realm.getInstanceAsync(config, new Realm.Callback() {
                    @Override
                    public void onSuccess(@NonNull Realm realm) {
                        Log.v(
                                "EXAMPLE",
                                "Successfully opened a realm with reads and writes allowed on the UI thread."
                        );
                        realm.executeTransaction(transactionRealm -> {
                            RealmQuery<PWD> tasksQuery = realm.where(PWD.class);
                            PWD account = transactionRealm.where(PWD.class).equalTo("email", email).findFirst();


                        });
                        database = realm;
                    }
                });
            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });
    }
}