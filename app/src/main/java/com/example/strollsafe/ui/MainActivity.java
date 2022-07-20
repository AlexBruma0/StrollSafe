package com.example.strollsafe.ui;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

import com.example.strollsafe.R;
import com.example.strollsafe.caregiver.Caregiver;
import com.example.strollsafe.utils.DatabaseManager;

import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


public class MainActivity extends AppCompatActivity {
    // Global variables for the database access
    Realm database;
    App app;
    private final String appId = "strollsafe-pjbnn";
    RealmConfiguration config;
    DatabaseManager databaseManager;

    // Shared preferences for storing caregiver information locally
    SharedPreferences caregiverPreferences;
    SharedPreferences.Editor caregiverPreferencesEditor;

    // Shared preferences for storing PWD information locally
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseManager = new DatabaseManager(this);
//        config = new RealmConfiguration.Builder().name(appId).allowQueriesOnUiThread(true).allowWritesOnUiThread(true).build();
//        database = Realm.getInstance(config);


        caregiverPreferences = getSharedPreferences("CAREGIVER", MODE_PRIVATE);
        caregiverPreferencesEditor = caregiverPreferences.edit();

        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();

        setContentView(R.layout.activity_main);
        Toolbar topBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topBar);


        configureNewPWD();
        configureCaregiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionsMenuItem:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void configureNewPWD(){
        Button PWD = (Button) findViewById(R.id.button_new_PWD);
        PWD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PWDSignupActivity.class));
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

    public void loginOnClick(View v) {
        if(databaseManager.isUserLoggedIn()) {
            if(databaseManager.logoutOfRealm()) {
            }
        } else {
            String email = "gsstatia@sfu.ca";
            String password = "Strollsafe1";
            if(!databaseManager.isUserLoggedIn()) {
                databaseManager.asyncLoginToRealm(email, password);
                Log.i("AUTH", "Logged into realm");
//                if(databaseManager.createRealmUserAccount("gsstatia@sfu.ca", "Strollsafe1")) {
//
//
//                }
            } else {
                Log.i("AUTH", "User is already logged in.");
            }
        }
    }

    // Logs into the databased given a user name and password
    public void login(String email, String password) {
        try {
            Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);
            AtomicReference<User> user = new AtomicReference<User>();
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
        } catch (Exception e) {
            Log.e("", "" + e.getLocalizedMessage());
        }
    }

    public void addObject() {
        database.executeTransaction(t -> {
//             Adding objects to the database
//            Caregiver caregiver = new Caregiver();
//            t.insert(caregiver);
//
//            Caregiver testCaregiver = database.createObject(Caregiver.class, new ObjectId());
//            testCaregiver.setFirstName("Brittany");
//            testCaregiver.setLastName("Spears");
//            database.insert(testCaregiver);
//            Log.e("Object added", testCaregiver.toString());
        });

    }

    public void retrieveFromDatabase() {
//        RealmResults<PWD> caregiverRealmResults = database.where(PWD.class).findAll();
//        Log.d("",caregiverRealmResults.asJSON());

    }

}
