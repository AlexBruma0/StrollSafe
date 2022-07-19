package com.example.strollsafe.pwd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.strollsafe.ui.MainActivity;
import com.example.strollsafe.R;
import com.example.strollsafe.ui.PWDActivity;
import com.example.strollsafe.ui.PWDSignupActivity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class PWD_login extends AppCompatActivity {
    RealmConfiguration config;
    Realm database;
    private EditText password;
    private EditText email;
    App app;
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;
    private final String appId = "strollsafe-pjbnn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        setupRealm();
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
                startActivity(new Intent(PWD_login.this, PWDSignupActivity.class));;
            }
        });
    }
    public void configurePWDlogin(){
        Button PWD = (Button) findViewById(R.id.PWDLogin);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = (EditText) findViewById(R.id.editPWDPassword);
                email = (EditText) findViewById(R.id.editPWDEmail);
                login(email.getText().toString(),password.getText().toString());
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
                            PWD account = transactionRealm.where(PWD.class).equalTo("email", email).findFirst();
                            pwdPreferenceEditor.putString("code",account.getPWDCode());
                            pwdPreferenceEditor.putString("F_name",account.getFirstName());
                            pwdPreferenceEditor.putString("L_name",account.getLastName());
                            pwdPreferenceEditor.putString("Phone",account.getPhoneNumber());
                            pwdPreferenceEditor.putString("id",account.get_id().toString());
                            pwdPreferenceEditor.putString("email",email);
                            pwdPreferenceEditor.putString("password",password);
                            pwdPreferenceEditor.apply();
                        });
                        database = realm;
                        startActivity(new Intent(PWD_login.this, PWDActivity.class));;
                    }
                });
            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });
    }
}