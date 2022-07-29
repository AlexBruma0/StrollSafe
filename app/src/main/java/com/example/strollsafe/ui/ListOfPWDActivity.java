package com.example.strollsafe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.strollsafe.GeofencingMapsActivity;
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
    private static String t;
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
                startActivity(new Intent(ListOfPWDActivity.this, GeofencingMapsActivity.class));
            }
        });
    }
    public void configureMap2(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map2);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, GeofencingMapsActivity.class));
            }
        });
    }
    public void configureMap3(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map3);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, GeofencingMapsActivity.class));
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
                B.setText("CLICK HERE TO ACTIVATE");
                PWD.setVisibility(View.GONE);
                A.setVisibility(View.GONE);
                B.setVisibility(View.GONE);
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
                B.setText("CLICK HERE TO ACTIVATE");
                PWD.setVisibility(View.GONE);
                A.setVisibility(View.GONE);
                B.setVisibility(View.GONE);

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
                B.setText("CLICK HERE TO ACTIVATE");
                PWD.setVisibility(View.GONE);
                A.setVisibility(View.GONE);
                B.setVisibility(View.GONE);

            }
        });
    }


    public void add(View view){
        Button name1 = (Button) findViewById(R.id.NAME1);
        ImageButton delete1 = (ImageButton) findViewById(R.id.delete1);
        ImageButton map1 = (ImageButton) findViewById(R.id.Map1);

        Button name2 = (Button) findViewById(R.id.NAME2);
        ImageButton delete2 = (ImageButton) findViewById(R.id.delete2);
        ImageButton map2 = (ImageButton) findViewById(R.id.Map2);

        Button name3 = (Button) findViewById(R.id.NAME3);
        ImageButton delete3 = (ImageButton) findViewById(R.id.delete3);
        ImageButton map3 = (ImageButton) findViewById(R.id.Map3);

        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);
        Credentials emailPasswordCredentials = Credentials.emailPassword("1234", "password123");
        AtomicReference<User> user = new AtomicReference<User>();
        app.loginAsync(emailPasswordCredentials, it -> {
            user.set(app.currentUser());
            RealmConfiguration config = new SyncConfiguration.Builder(Objects.requireNonNull(app.currentUser()), Objects.requireNonNull(app.currentUser()).getId())
                    .name(APP_ID)
                    .schemaVersion(2)
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .build();
            Realm.getInstanceAsync(config, new Realm.Callback() {
                @Override
                public void onSuccess(@NonNull Realm realm) {
                    realmDatabase = realm;
                    if (name1.getText().equals("CLICK HERE TO ACTIVATE")) {
                        PWD acc = realmDatabase.where(PWD.class).equalTo("pwdCode", editText.getText().toString()).findFirst();
                        name1.setText(acc.getFirstName() + " " + acc.getLastName());
                        name1.setVisibility(View.VISIBLE);
                        delete1.setVisibility(View.VISIBLE);
                        map1.setVisibility(View.VISIBLE);
                    }
                    else if (name2.getText().equals("CLICK HERE TO ACTIVATE")) {
                        PWD acc = realmDatabase.where(PWD.class).equalTo("pwdCode", editText.getText().toString()).findFirst();
                        name2.setText(acc.getFirstName() + " " + acc.getLastName());
                        delete2.setVisibility(View.VISIBLE);
                        name2.setVisibility(View.VISIBLE);
                        map2.setVisibility(View.VISIBLE);
                    }
                    else if (name3.getText().equals("CLICK HERE TO ACTIVATE")) {
                        PWD acc = realmDatabase.where(PWD.class).equalTo("pwdCode", editText.getText().toString()).findFirst();
                        name3.setText(acc.getFirstName() + " " + acc.getLastName());
                        delete3.setVisibility(View.VISIBLE);
                        name3.setVisibility(View.VISIBLE);

                        map3.setVisibility(View.VISIBLE);
                    }


                }
            });
        });
    }


}
