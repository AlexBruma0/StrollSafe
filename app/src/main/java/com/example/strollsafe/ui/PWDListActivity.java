package com.example.strollsafe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.strollsafe.R;
import com.example.strollsafe.utils.DatabaseManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;

public class PWDListActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button back;
    private Button name;


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    private String text;

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

        setContentView(R.layout.activity_pwdlist);
        databaseManager = new DatabaseManager(this);



        backB();

    }
//    public void disable(View v){
//        v.setEnabled(false);
//        Button b = (Button) v;
//        b.setText("hi");
//    }

    public void backB() {
        back = (Button) findViewById(R.id.backbutton);
        editText = (EditText) findViewById(R.id.editTextTextPassword);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                app = databaseManager.getApp();
                pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
                pwdPreferenceEditor = pwdPreferences.edit();
//                setContentView(R.layout.activity_pwd);
//                TextView code = findViewById(R.id.viewPWDCODE);
//                code.setText(pwdPreferences.getString("code","error"));

                back = (Button) findViewById(R.id.backbutton);
                Intent i =new Intent(PWDListActivity.this, ListOfPWDActivity.class);
                i.putExtra("cool",editText.getText().toString());
                startActivity(i);
            }
        });
    }
}
