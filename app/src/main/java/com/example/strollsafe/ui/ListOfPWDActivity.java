package com.example.strollsafe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.strollsafe.R;
import com.example.strollsafe.ui.Location.PWDLocationInformationActivity;
import com.example.strollsafe.utils.DatabaseManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;

public class ListOfPWDActivity extends AppCompatActivity {
    public static final String TEXT = "text";
    public static final String SHARED_PREFS = "sharedPrefs";

    private Button button;
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
        setContentView(R.layout.activity_listofpwd);

        button = (Button) findViewById(R.id.NAME1);
        databaseManager = new DatabaseManager(this);

        configureBack();
        configureMap1();
        configureMap2();
        configureMap3();
        configureDelete1();
        configureDelete2();
        configureDelete3();
        configureNew();
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
                startActivity(new Intent(ListOfPWDActivity.this, PWDLocationInformationActivity.class));
            }
        });
    }
    public void configureMap2(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map2);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, PWDLocationInformationActivity.class));
            }
        });
    }
    public void configureMap3(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map3);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, PWDLocationInformationActivity.class));
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
    public void configureNew(){
        ImageButton newPWD = (ImageButton) findViewById(R.id.NewPWD);
//        ImageButton delete = (ImageButton) findViewById(R.id.delete1);
//        ImageButton map = (ImageButton) findViewById(R.id.Map1);
        Button name = (Button) findViewById(R.id.NAME1);
        newPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ListOfPWDActivity.this, PWDListActivity.class));
            }
        });
    }

    public void configureName1(){
        Button name = (Button) findViewById(R.id.NAME1);
        ImageButton delete = (ImageButton) findViewById(R.id.delete1);
        ImageButton map = (ImageButton) findViewById(R.id.Map1);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                name.setText(i.getStringExtra("cool"));
                delete.setVisibility(View.VISIBLE);
                map.setVisibility(View.VISIBLE);
            }
        });
    }
    public void configureName2(){
        Button name = (Button) findViewById(R.id.NAME2);
        ImageButton delete = (ImageButton) findViewById(R.id.delete2);
        ImageButton map = (ImageButton) findViewById(R.id.Map2);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                name.setText(i.getStringExtra("cool"));
                delete.setVisibility(View.VISIBLE);
                map.setVisibility(View.VISIBLE);
            }
        });
    }
    public void configureName3(){
        Button name = (Button) findViewById(R.id.NAME3);
        ImageButton delete = (ImageButton) findViewById(R.id.delete3);
        ImageButton map = (ImageButton) findViewById(R.id.Map3);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                name.setText(i.getStringExtra("cool"));
                delete.setVisibility(View.VISIBLE);
                map.setVisibility(View.VISIBLE);
            }
        });
    }

    public void saveData(){
        SharedPreferences pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        SharedPreferences.Editor PreferenceEditor = pwdPreferences.edit();

        PreferenceEditor.putString(TEXT,button.getText().toString());
        PreferenceEditor.apply();
        Toast.makeText(this, "DATA SAVED", Toast.LENGTH_SHORT).show();
    }
    public void loadData(){
        SharedPreferences pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        text = pwdPreferences.getString(TEXT, "");

    }
    public void updateViews(){
        button.setText(text);
    }
}
