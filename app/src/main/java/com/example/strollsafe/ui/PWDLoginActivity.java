package com.example.strollsafe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.strollsafe.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.strollsafe.pwd.PWD;
import com.example.strollsafe.utils.DatabaseManager;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class PWDLoginActivity extends AppCompatActivity {
    RealmConfiguration config;
    App app;
    private final String APP_ID = "strollsafe-pjbnn";
    Realm realmDatabase;
    DatabaseManager databaseManager;
    private EditText editEmail;
    private EditText editPassword;
    boolean isUserLoggedIn;
    String TAG = "";
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;
    private final String appId = "strollsafe-pjbnn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();
        setContentView(R.layout.activity_pwd_login);
        configureCreateAccount();
        configurePWDlogin();
        SharedPreferences pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        SharedPreferences.Editor pwdPreferenceEditor = pwdPreferences.edit();
    }
    public void configureCreateAccount() {
        Button PWD = (Button) findViewById(R.id.SignUp);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PWDLoginActivity.this, PWDSignupActivity.class));;
            }
        });
    }
    public void configurePWDlogin(){
        Button PWD = (Button) findViewById(R.id.PWDLogin1);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPassword = (EditText) findViewById(R.id.editPWDPassword);
                editEmail = (EditText) findViewById(R.id.editPWDEmail);
                String email = "11";
                String password = "password123";
                try {
                    Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);
                    AtomicReference<User> user = new AtomicReference<User>();
                    app.loginAsync(emailPasswordCredentials, it -> {
                        if (it.isSuccess()) {
                            Log.i(TAG + "asyncLoginToRealm", "Successfully authenticated using an email and password: " + email);
                            user.set(app.currentUser());
                            isUserLoggedIn = true;
                            RealmConfiguration config = new SyncConfiguration.Builder(Objects.requireNonNull(app.currentUser()), Objects.requireNonNull(app.currentUser()).getId())
                                    .name(APP_ID)
                                    .schemaVersion(2)
                                    .allowQueriesOnUiThread(true)
                                    .allowWritesOnUiThread(true)
                                    .build();
                            Realm.getInstanceAsync(config, new Realm.Callback() {
                                @Override
                                public void onSuccess(@NonNull Realm realm) {
                                    Log.v(TAG, "Successfully opened a realm with the given config.");
                                    realmDatabase = realm;
                                    // CODE TO EXECUTE AFTER LOGIN
                                    PWD account = realmDatabase.where(PWD.class).equalTo("email",email).findFirst();
                                    pwdPreferenceEditor.putString("code", account.getPWDCode());
                                    pwdPreferenceEditor.putString("F_name",account.getFirstName());
                                    pwdPreferenceEditor.putString("L_name",account.getLastName());
                                    pwdPreferenceEditor.putString("Phone", account.getPhoneNumber());
                                    pwdPreferenceEditor.putString("email", account.getEmail());
                                    pwdPreferenceEditor.putString("password", account.getPassword());
                                    //startActivity(new Intent(PWDLoginActivity.this,PWDActivity.class));
                                }
                            });
                        } else {
                            Log.e(TAG + "asyncLoginToRealm", "email: " + it.getError().toString());
                            isUserLoggedIn = false;
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG + "asyncLoginToRealm", "" + e.getLocalizedMessage());
                }

            }
        });
    }
}