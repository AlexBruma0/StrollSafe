package com.example.strollsafe.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.AppException;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * The DatabaseManager class is responsible for handling the login, account creation and login, and
 * various database configuration functions.
 *
 * @author Steve Statia
 * @since 2022-07-19
 */
public class DatabaseManager {
    private Realm realmDatabase;
    private App app;
    private final String APP_ID = "strollsafe-pjbnn";
    private final String TAG = "DatabaseManager/";
    private boolean isUserLoggedIn = false;

    /**
     * Initializes a new DatabaseManager
     * @param context The activity context this is being called in.
     */
    public DatabaseManager(Context context) {
        Realm.init(context);
        app = new App(new AppConfiguration.Builder(APP_ID).build());
    }


    /**
     * Creates an account for the database, and leaves it in a pending state. To activate the
     * account and remove it from the pending state, the account must be used to login.
     * App users can be viewed here:
     * https://realm.mongodb.com/groups/62ab69d80c54cf25ba32b567/apps/62c53c26ebb532e9b5847441/auth/users
     * @param email email for the user account being created.
     * @param password password for the user account being created.
     */
    public void createRealmUserAccount(String email, String password) {
        app.getEmailPassword().registerUserAsync(email, password, it -> {
            if (it.isSuccess()) {
                Log.i(TAG, "Successfully registered user: " + email);
            } else {
                Log.e(TAG, "Failed to register user: " + email + "\t" + it.getError().getErrorMessage());
            }
        });
    }

    /**
     * Creates a user and logs them into the app
     * @param email
     * @param password
     */
    public void createRealmUserAndLoginAsync(String email, String password) {
        app.getEmailPassword().registerUserAsync(email, password, it -> {
            if (it.isSuccess()) {
                Log.i(TAG, "Successfully registered user: " + email);
                Log.i(TAG, "Logging in...");
                asyncLoginToRealm(email, password);
            } else {
                Log.e(TAG, "Failed to register user: " + email + "\t" + it.getError().getErrorMessage());
            }
        });
    }

    /**
     * Login to the realm database.
     * @param email to login with
     * @param password to login with (at least 6 characters)
     */
    public void asyncLoginToRealm(String email, String password) {
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
                    getRealmInstance(config);
                } else {
                    Log.e(TAG + "asyncLoginToRealm", "email: " + it.getError().toString());
                    isUserLoggedIn = false;
                }
            });
        } catch (Exception e) {
            Log.e(TAG + "asyncLoginToRealm", "" + e.getLocalizedMessage());
        }
    }

    /**
     * Have not tested it yet
     * @param email
     * @param password
     */
    public void loginToRealm(String email, String password) {
        new Thread() {
            public void run() {
                Credentials emailPasswordCredentials = Credentials.emailPassword(email, password);
                try {
                    AtomicReference<User> user = new AtomicReference<User>();
                    user.set(app.login(emailPasswordCredentials));
                    RealmConfiguration config = new SyncConfiguration.Builder(Objects.requireNonNull(app.currentUser()), Objects.requireNonNull(app.currentUser()).getId())
                            .name(APP_ID)
                            .schemaVersion(2)
                            .allowQueriesOnUiThread(true)
                            .allowWritesOnUiThread(true)
                            .build();
                    getRealmInstance(config);

                } catch (AppException e) {
                    Log.e("AUTH", "ERROR:" + e.getErrorMessage());
                }
            }
        }.start();
    }

    /**
     * Checks the app to see if the user is logged in.
     * @return if the user is currently logged into realm.
     */
    public boolean isUserLoggedIn() {
        if(app.currentUser() == null) {
            isUserLoggedIn = false;
            return false;
        } else {
            isUserLoggedIn = true;
            return true;
        }
    }

    /**
     * Logs the current user our of the Realm instance.
     * @return if logout was successful.
     */
    public boolean logoutOfRealm() {
        AtomicBoolean loggedOut = new AtomicBoolean(false);
        app.currentUser().logOutAsync(callback -> {
            if(callback.isSuccess()) {
                // User logged out
                loggedOut.set(true);
            } else {
                // User did not log out
                loggedOut.set(false);
            }
        });
        return loggedOut.get();
    }

    /**
     * @return User that is currently logged in
     */
    public User getLoggedInUser() {
        return app.currentUser();
    }

    /**
     * @return Realm object for database transactions
     */
    public Realm getRealmDatabase() {
        return realmDatabase;
    }

    /**
     * @return App that is used by the database.
     */
    public App getApp() {
        return app;
    }

    private void getRealmInstance(RealmConfiguration configuration) {
        Realm.getInstanceAsync(configuration, new Realm.Callback() {
            @Override
            public void onSuccess(@NonNull Realm realm) {
                Log.v(TAG, "Successfully opened a realm with the given config.");
                realmDatabase = realm;
            }
        });
    }
}
