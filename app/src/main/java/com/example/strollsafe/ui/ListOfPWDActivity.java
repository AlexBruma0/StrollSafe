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
import android.widget.ImageButton;
import android.widget.EditText;

import com.example.strollsafe.pwd.PWD;
import com.example.strollsafe.utils.DatabaseManager;
import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class ListOfPWDActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_listofpwd);



        databaseManager = new DatabaseManager(this);

        configureBack();
        configureMap1();
        configureMap2();
        configureMap3();
        configureDelete1();
        configureDelete2();
        configureDelete3();
        configureName1();
        configureName2();
        configureName3();

    }


    public void configureBack(){
        ImageButton PWD = (ImageButton) findViewById(R.id.quit);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, MainActivity.class));
            }
        });
    }


    public void configureMap1(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map1);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this,
                        com.example.strollsafe.ui.location.PWDLocationInformationActivity.class));
            }
        });
    }
    public void configureMap2(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map2);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this,
                        com.example.strollsafe.ui.location.PWDLocationInformationActivity.class));
            }
        });
    }
    public void configureMap3(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map3);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this,
                        com.example.strollsafe.ui.location.PWDLocationInformationActivity.class));
            }
        });
    }


    public void configureDelete1(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete1);
        ImageButton A = (ImageButton) findViewById(R.id.Map1);
        Button B = (Button) findViewById(R.id.NAME1);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                B.setText("CLICK TO ACTIVATE");
                PWD.setVisibility(View.GONE);
                A.setVisibility(View.GONE);
            }
        });
    }
    public void configureDelete2(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete2);
        ImageButton A = (ImageButton) findViewById(R.id.Map2);
        Button B = (Button) findViewById(R.id.NAME2);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                B.setText("CLICK TO ACTIVATE");
                PWD.setVisibility(View.GONE);
                A.setVisibility(View.GONE);
            }
        });
    }
    public void configureDelete3(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete3);
        ImageButton A = (ImageButton) findViewById(R.id.Map3);
        Button B = (Button) findViewById(R.id.NAME3);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                B.setText("CLICK TO ACTIVATE");
                PWD.setVisibility(View.GONE);
                A.setVisibility(View.GONE);
            }
        });
    }


    public void configureName1(){
        Button name = (Button) findViewById(R.id.NAME1);
        ImageButton delete = (ImageButton) findViewById(R.id.delete1);
        ImageButton map = (ImageButton) findViewById(R.id.Map1);
        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = "22";
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
                                    realmDatabase.executeTransaction(transaction -> {
                                        PWD account = transaction.createObject(PWD.class, new ObjectId());
                                        account.setEmail("emaila");
                                        account.setFirstName("Herbert");
                                        account.setLastName("Tsang");
                                        account.setPhoneNumber("phoneNumb11er");
                                        account.setPWDCode("1234");
                                    });
                                    realmDatabase.executeTransaction(transaction -> {
                                        PWD account = transaction.createObject(PWD.class, new ObjectId());
                                        account.setEmail("haha");
                                        account.setFirstName("Alex");
                                        account.setLastName("Bruma");
                                        account.setPhoneNumber("phoneNumb11er1");
                                        account.setPWDCode("6666");
                                    });
                                    PWD acc = realmDatabase.where(PWD.class).equalTo("PWDCode",editText.getText().toString()).findFirst();
                                    name.setText(acc.getFirstName() + " " + acc.getLastName());

//                                    Intent i = getIntent();
//                                    name.setText(i.getStringExtra("cool"));
                                    delete.setVisibility(View.VISIBLE);
                                    map.setVisibility(View.VISIBLE);
                                    // CODE TO EXECUTE AFTER LOGIN



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

    public void configureName2(){
        Button name = (Button) findViewById(R.id.NAME2);
        ImageButton delete = (ImageButton) findViewById(R.id.delete2);
        ImageButton map = (ImageButton) findViewById(R.id.Map2);
        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = "22";
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
                                    realmDatabase.executeTransaction(transaction -> {
                                        PWD account = transaction.createObject(PWD.class, new ObjectId());
                                        account.setEmail("emaila");
                                        account.setFirstName("Herbert");
                                        account.setLastName("Tsang");
                                        account.setPhoneNumber("phoneNumb11er");
                                        account.setPWDCode("1234");
                                    });
                                    realmDatabase.executeTransaction(transaction -> {
                                        PWD account = transaction.createObject(PWD.class, new ObjectId());
                                        account.setEmail("haha");
                                        account.setFirstName("Alex");
                                        account.setLastName("Bruma");
                                        account.setPhoneNumber("phoneNumb11er1");
                                        account.setPWDCode("6666");
                                    });
                                    PWD acc = realmDatabase.where(PWD.class).equalTo("PWDCode",editText.getText().toString()).findFirst();
                                    name.setText(acc.getFirstName() + " " + acc.getLastName());

//                                    Intent i = getIntent();
//                                    name.setText(i.getStringExtra("cool"));
                                    delete.setVisibility(View.VISIBLE);
                                    map.setVisibility(View.VISIBLE);
                                    // CODE TO EXECUTE AFTER LOGIN



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
    public void configureName3(){
        Button name = (Button) findViewById(R.id.NAME3);
        ImageButton delete = (ImageButton) findViewById(R.id.delete3);
        ImageButton map = (ImageButton) findViewById(R.id.Map3);
        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = "22";
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
                                    realmDatabase.executeTransaction(transaction -> {
                                        PWD account = transaction.createObject(PWD.class, new ObjectId());
                                        account.setEmail("emaila");
                                        account.setFirstName("Herbert");
                                        account.setLastName("Tsang");
                                        account.setPhoneNumber("phoneNumb11er");
                                        account.setPWDCode("1234");
                                    });
                                    realmDatabase.executeTransaction(transaction -> {
                                        PWD account = transaction.createObject(PWD.class, new ObjectId());
                                        account.setEmail("haha");
                                        account.setFirstName("Alex");
                                        account.setLastName("Bruma");
                                        account.setPhoneNumber("phoneNumb11er1");
                                        account.setPWDCode("6666");
                                    });
                                    PWD acc = realmDatabase.where(PWD.class).equalTo("PWDCode",editText.getText().toString()).findFirst();
                                    name.setText(acc.getFirstName() + " " + acc.getLastName());

//                                    Intent i = getIntent();
//                                    name.setText(i.getStringExtra("cool"));
                                    delete.setVisibility(View.VISIBLE);
                                    map.setVisibility(View.VISIBLE);
                                    // CODE TO EXECUTE AFTER LOGIN



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


//    public void saveData(){
//        SharedPreferences pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
//        SharedPreferences.Editor PreferenceEditor = pwdPreferences.edit();
//
//        PreferenceEditor.putString(TEXT,button.getText().toString());
//        PreferenceEditor.apply();
//        Toast.makeText(this, "DATA SAVED", Toast.LENGTH_SHORT).show();
//    }
//    public void loadData(){
//        SharedPreferences pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
//        text = pwdPreferences.getString(TEXT, "");
//
//    }
//    public void updateViews(){
//        button.setText(text);
//    }
}
