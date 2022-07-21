package com.example.strollsafe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.strollsafe.caregiver.Caregiver;
import com.example.strollsafe.ui.MainActivity;
import com.example.strollsafe.R;
import com.example.strollsafe.ui.CaregiverActivity;
import com.example.strollsafe.pwd.PWD;
import com.example.strollsafe.utils.DatabaseManager;
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
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.strollsafe.databinding.ActivityPwdSignupBinding;

import org.bson.types.ObjectId;

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

public class PWDSignupActivity extends AppCompatActivity {

    DatabaseManager databaseManager;
    App app;
    private User user;
    private final String APP_ID = "strollsafe-pjbnn";
    private RealmConfiguration config;
    Realm realmDatabase;
    String TAG = "";
    private Button btnMain;
    private TextView txtMain;
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editPhoneNumber;
    private EditText editPassword;
    private EditText editEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();
        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        setContentView(R.layout.activity_pwd_signup);
        configureBack();
        configureSignUp();
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
                editFirstName = (EditText) findViewById(R.id.editFirstNamePWD);
                editLastName = (EditText) findViewById(R.id.editLastNamePWD);
                editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
                editEmail = (EditText) findViewById(R.id.editEmailAddress);
                editPassword = (EditText) findViewById(R.id.editPassword);
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String firstName = editFirstName.getText().toString();
                String lastName = editLastName.getText().toString();
                String phoneNumber = editPhoneNumber.getText().toString();

                pwdPreferenceEditor.putString("code",code);
                pwdPreferenceEditor.putString("F_name",firstName);
                pwdPreferenceEditor.putString("L_name",lastName);
                pwdPreferenceEditor.putString("Phone",phoneNumber);
                pwdPreferenceEditor.putString("email",email);
                pwdPreferenceEditor.putString("password", password);

                PWD account = new PWD(firstName,lastName,phoneNumber,code,email,password);
                pwdPreferenceEditor.putString("id",account.get_id().toString());
                pwdPreferenceEditor.apply();
                app.getEmailPassword().registerUserAsync(email, password, createResult -> {
                    if (createResult.isSuccess()) {
                        Log.i(TAG, "Successfully registered user: " + email);
                        Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);
                        AtomicReference<User> user = new AtomicReference<User>();

                        app.loginAsync(emailPasswordCredentials, loginResult -> {
                            if (loginResult.isSuccess()) {
                                Log.i(TAG + "asyncLoginToRealm", "Successfully authenticated using an email and password: " + email);
                                user.set(app.currentUser());
                                config = new SyncConfiguration.Builder(Objects.requireNonNull(app.currentUser()), Objects.requireNonNull(app.currentUser()).getId())
                                        .name(APP_ID)
                                        .schemaVersion(2)
                                        .allowQueriesOnUiThread(true)
                                        .allowWritesOnUiThread(true)
                                        .build();

                                Realm.getInstanceAsync(config, new Realm.Callback() {
                                    @Override
                                    public void onSuccess(@NonNull Realm realm) {
                                        Log.v(TAG, "Successfully opened a realm with reads and writes allowed on the UI thread.");
                                        realmDatabase = realm;
                                        realmDatabase.executeTransaction(transaction -> {
                                            PWD account = transaction.createObject(PWD.class, new ObjectId());
                                            account.setEmail(email);
                                            account.setFirstName(firstName);
                                            account.setLastName(lastName);
                                            account.setPhoneNumber(phoneNumber);
                                            account.setPWDCode(code);
                                        });
                                        startActivity(new Intent(PWDSignupActivity.this, PWDActivity.class));
                                    }
                                });
                            } else {
                                Log.e(TAG + "asyncLoginToRealm", "email: " + loginResult.getError().toString());
                                //Toast.makeText(this, "email: " + loginResult.getError().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.e(TAG, "Failed to register user: " + email + "\t" + createResult.getError().getErrorMessage());
                        //Toast.makeText(this, "Failed to register user: " + email + "\t" + createResult.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
    /*public void configureSignUp(){
        Button PWD = (Button) findViewById(R.id.btnMain);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = getRandomString(4);
                editFirstName = (EditText) findViewById(R.id.editFirstNamePWD);
                editLastName = (EditText) findViewById(R.id.editLastNamePWD);
                editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
                editEmail = (EditText) findViewById(R.id.editEmailAddress);
                editPassword = (EditText) findViewById(R.id.editPassword);
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String firstName = editFirstName.getText().toString();
                String lastName = editLastName.getText().toString();
                String phoneNumber = editPhoneNumber.getText().toString();

                pwdPreferenceEditor.putString("code",code);
                pwdPreferenceEditor.putString("F_name",firstName);
                pwdPreferenceEditor.putString("L_name",lastName);
                pwdPreferenceEditor.putString("Phone",phoneNumber);
                pwdPreferenceEditor.putString("email",email);
                pwdPreferenceEditor.putString("password", password);

                PWD account = new PWD(firstName,lastName,phoneNumber,code,email,password);
                pwdPreferenceEditor.putString("id",account.get_id().toString());
                pwdPreferenceEditor.apply();

                createUserLogin(email,password);
                login(email,password);
                realmDatabase.executeTransaction(transaction -> {
                    transaction.insert(account);
                });
                startActivity(new Intent(PWDSignupActivity.this, PWDActivity.class));
            }
        });
    }
    public void setupRealm() {
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(APP_ID).build());
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
                        .name(APP_ID)
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
                        realmDatabase = realm;
                    }
                });
            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });
    }
}*/
    /*public void createUserLoginAndUploadObjectFromFields(View view) {
        EditText emailEditText = findViewById(R.id.caregiverEmail);
        EditText passwordEditText = findViewById(R.id.caregiverPassword);
        EditText firstNameEditText = findViewById(R.id.caregiverFirstName);
        EditText lastNameEditText = findViewById(R.id.caregiverLastName);
        EditText phoneNumberEditText = findViewById(R.id.caregiverPhoneNumber);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();

        app.getEmailPassword().registerUserAsync(email, password, createResult -> {
            if (createResult.isSuccess()) {
                Log.i(TAG, "Successfully registered user: " + email);
                Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);
                AtomicReference<User> user = new AtomicReference<User>();

                app.loginAsync(emailPasswordCredentials, loginResult -> {
                    if (loginResult.isSuccess()) {
                        Log.i(TAG + "asyncLoginToRealm", "Successfully authenticated using an email and password: " + email);
                        user.set(app.currentUser());
                        config = new SyncConfiguration.Builder(Objects.requireNonNull(app.currentUser()), Objects.requireNonNull(app.currentUser()).getId())
                                .name(APP_ID)
                                .schemaVersion(2)
                                .allowQueriesOnUiThread(true)
                                .allowWritesOnUiThread(true)
                                .build();

                        Realm.getInstanceAsync(config, new Realm.Callback() {
                            @Override
                            public void onSuccess(@NonNull Realm realm) {
                                Log.v(TAG, "Successfully opened a realm with reads and writes allowed on the UI thread.");
                                realmDatabase = realm;
                                realmDatabase.executeTransaction(transaction -> {
                                    Caregiver caregiver = transaction.createObject(Caregiver.class, new ObjectId());
                                    caregiver.setEmail(email);
                                    caregiver.setFirstName(firstName);
                                    caregiver.setLastName(lastName);
                                    caregiver.setPhoneNumber(phoneNumber);
                                });
                                startActivity(new Intent(NewCaregiverActivity.this, CaregiverActivity.class));
                            }
                        });
                    } else {
                        Log.e(TAG + "asyncLoginToRealm", "email: " + loginResult.getError().toString());
                        Toast.makeText(this, "email: " + loginResult.getError().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e(TAG, "Failed to register user: " + email + "\t" + createResult.getError().getErrorMessage());
                Toast.makeText(this, "Failed to register user: " + email + "\t" + createResult.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/;