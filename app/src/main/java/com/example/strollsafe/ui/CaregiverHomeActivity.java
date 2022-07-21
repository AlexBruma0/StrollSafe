package com.example.strollsafe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.strollsafe.R;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class CaregiverHomeActivity extends AppCompatActivity {
    RealmConfiguration config;
    App app;
    private final String APP_ID = "strollsafe-pjbnn";
    Realm realmDatabase;
    DatabaseManager databaseManager;
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

        setContentView(R.layout.activity_caregiver_home);
        Toolbar topBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topBar);

        SharedPreferences pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        SharedPreferences.Editor pwdPreferenceEditor = pwdPreferences.edit();
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
                startActivity(new Intent(CaregiverHomeActivity.this, SettingsActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}