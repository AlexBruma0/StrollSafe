package com.example.strollsafe.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.realm.mongodb.User;

import com.example.strollsafe.R;
import com.example.strollsafe.utils.DatabaseManager;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    DatabaseManager databaseManager;
    User currentUser;
    TextView loginStatusTextView, accountTypeTextView, emailTextView, userIdTextView, deviceIdTextView, firstNameTextView, lastNameTextView;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar topBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topBar);

        logoutButton = findViewById(R.id.logoutButton);
        loginStatusTextView = findViewById(R.id.loginStatus);
        accountTypeTextView = findViewById(R.id.userAccountType);
        emailTextView = findViewById(R.id.userLoggedInEmail);
        userIdTextView = findViewById(R.id.userId);
        deviceIdTextView = findViewById(R.id.userDeviceId);
        firstNameTextView = findViewById(R.id.userFirstName);
        lastNameTextView = findViewById(R.id.userLastName);

        databaseManager = new DatabaseManager(this);
        currentUser = databaseManager.isUserLoggedIn() ? databaseManager.getLoggedInUser() : null;
        if(currentUser == null) {
            styleSettingsPageLoggedOut();
        } else {
            styleSettingsPageLoggedIn();

        }
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

    public void logoutUserFromRealm(View view) {
        Objects.requireNonNull(databaseManager.getApp().currentUser()).logOutAsync(logoutStatus -> {
            if(logoutStatus.isSuccess()) {
                styleSettingsPageLoggedOut();
                Toast.makeText(this, "Logged out.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "" + logoutStatus.getError().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void styleSettingsPageLoggedOut() {
        final String NULL_STRING = "null";
        loginStatusTextView.setText(String.format(getString(R.string.status_prefix), "LOGGED OUT"));

        emailTextView.setText(String.format(getString(R.string.email_prefix), NULL_STRING));
        emailTextView.setAlpha(0.5f);

        userIdTextView.setText(String.format(getString(R.string.user_id_prefix), NULL_STRING));
        userIdTextView.setAlpha(0.5f);

        deviceIdTextView.setText(String.format(getString(R.string.device_id_prefix), NULL_STRING));
        deviceIdTextView.setAlpha(0.5f);

        firstNameTextView.setText(String.format(getString(R.string.first_name_prefix), NULL_STRING));
        firstNameTextView.setAlpha(0.5f);

        lastNameTextView.setText(String.format(getString(R.string.last_name_prefix), NULL_STRING));
        lastNameTextView.setAlpha(0.5f);

        logoutButton.setEnabled(false);
        logoutButton.setAlpha(0.5f);
    }

    private void styleSettingsPageLoggedIn() {
        loginStatusTextView.setText(String.format(getString(R.string.status_prefix), "LOGGED IN"));

        emailTextView.setText(String.format(getString(R.string.email_prefix), currentUser.getProfile().getEmail()));
        emailTextView.setAlpha(1f);

        userIdTextView.setText(String.format(getString(R.string.user_id_prefix), currentUser.getId()));
        userIdTextView.setAlpha(1.0f);

        deviceIdTextView.setText(String.format(getString(R.string.device_id_prefix), currentUser.getDeviceId()));
        deviceIdTextView.setAlpha(1f);

        firstNameTextView.setText(String.format(getString(R.string.first_name_prefix), currentUser.getProfile().getFirstName()));
        firstNameTextView.setAlpha(1f);

        lastNameTextView.setText(String.format(getString(R.string.last_name_prefix), currentUser.getProfile().getLastName()));
        lastNameTextView.setAlpha(1f);

        logoutButton.setEnabled(true);
        logoutButton.setAlpha(1f);
    }
}
