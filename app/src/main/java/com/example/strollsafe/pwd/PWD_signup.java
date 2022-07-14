package com.example.strollsafe.pwd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.strollsafe.MainActivity;
import com.example.strollsafe.R;
import com.example.strollsafe.caregiver.CaregiverActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.strollsafe.databinding.ActivityPwdSignupBinding;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class PWD_signup extends AppCompatActivity {

    RealmConfiguration config;
    Realm database;
    App app;
    private final String appId = "strollsafe-pjbnn";
    private Button btnMain;
    private TextView txtMain;
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;
    private EditText FirstName;
    private EditText LastName;
    private EditText PhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        setupRealm();
        setContentView(R.layout.activity_pwd_signup);
        configureBack();
        configureSignUp();
        login("1234@gmail.com","123456");
    } // end of onCreate()

    private static String getRandomString(int i){
        final String characters = "abcdefghiklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        while( i > 0 ){
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            i--;
        }
        return result.toString().toUpperCase(Locale.ROOT);
    }

    public void configureBack() {
        Button PWD = (Button) findViewById(R.id.pwdBackButton);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void configureSignUp(){
        Button PWD = (Button) findViewById(R.id.btnMain);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = getRandomString(4);
                FirstName = (EditText) findViewById(R.id.editFirstNamePWD);
                LastName = (EditText) findViewById(R.id.editLastNamePWD);
                PhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
                pwdPreferenceEditor.putString("code",code);
                pwdPreferenceEditor.putString("F_name",FirstName.getText().toString());
                pwdPreferenceEditor.putString("L_name",LastName.getText().toString());
                pwdPreferenceEditor.putString("Phone",PhoneNumber.getText().toString());
                pwdPreferenceEditor.apply();
                PWD account = new PWD(FirstName.getText().toString(),LastName.getText().toString(),PhoneNumber.getText().toString(),code);
                //createUserLogin("1234@gmail.com","123456");


                database.executeTransaction(transaction -> {
                    transaction.insert(account);
                });
                startActivity(new Intent(PWD_signup.this, PWD_activity.class));
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
                        database = realm;
                    }
                });
            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });
    }
}