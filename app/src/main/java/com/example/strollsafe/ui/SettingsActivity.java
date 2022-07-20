package com.example.strollsafe.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.realm.mongodb.User;

import com.example.strollsafe.R;
import com.example.strollsafe.utils.DatabaseManager;

public class SettingsActivity extends AppCompatActivity {
    DatabaseManager databaseManager;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar topBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topBar);

        databaseManager = new DatabaseManager(this);
        currentUser = databaseManager.isUserLoggedIn() ? databaseManager.getLoggedInUser() : null;
        currentUser.getDeviceId();
        currentUser.getId();
        currentUser.getProfile().getEmail();
        currentUser.getProfile().getFirstName();
        currentUser.getProfile().getLastName();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionsMenuItem:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
