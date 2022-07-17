package com.example.strollsafe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

import com.example.strollsafe.caregiver.Caregiver;
import com.example.strollsafe.pwd.PWD_login;
import com.example.strollsafe.R;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


public class MainActivity extends AppCompatActivity {
    // Global variables for the database access
    RealmConfiguration config;
    Realm database;
    App app;
    private final String appId = "strollsafe-pjbnn";

    // Shared preferences for storing caregiver information locally
    SharedPreferences caregiverPreferences;
    SharedPreferences.Editor caregiverPreferencesEditor;

    // Shared preferences for storing PWD information locally
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        caregiverPreferences = getSharedPreferences("CAREGIVER", MODE_PRIVATE);
        caregiverPreferencesEditor = caregiverPreferences.edit();

        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        setupRealm();
        configureNewPWD();
        configureCaregiver();
        configureLoginButton();



    }

    public void configureNewPWD(){
        Button PWD = (Button) findViewById(R.id.button_new_PWD);
        PWD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this, PWD_login.class));
                startActivity(new Intent(MainActivity.this, PWD_login.class));
            }
        });
    }
    public void configureCaregiver(){
        Button PWD = (Button) findViewById(R.id.button_new_caregiver);
        PWD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewCaregiverActivity.class));
            }
        });
    }
    public void configureLoginButton(){
        Button PWD = (Button) findViewById(R.id.button_toLogin);
        PWD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListOfPWDActivity.class));
            }
        });
    }


    // Setup the database
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
                        database = realm;
                    }
                });
            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });
    }

    public void addObject() {
        database.executeTransaction(t -> {
            // Adding objects to the database
//            Caregiver caregiver = new Caregiver();
//            t.insert(caregiver);
//
//            Caregiver testCaregiver = database.createObject(Caregiver.class);
//            testCaregiver.setFirstName("Brittany");
//            testCaregiver.setLastName("Spears");
        });

    }

    public void retrieveFromDatabase() {
//        RealmResults<PWD> caregiverRealmResults = database.where(PWD.class).findAll();
//        Log.d("",caregiverRealmResults.asJSON());

    }

}
