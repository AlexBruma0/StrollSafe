package com.example.strollsafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.bson.types.ObjectId;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class MainActivity extends AppCompatActivity {

    RealmConfiguration config;
    Realm backgroundThreadRealm;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        configurePWD();
        configureCaregiver();

        Realm.init(this);
        String appId = "strollsafe-pjbnn";
        app = new App(new AppConfiguration.Builder(appId)
                .build());
        config = new SyncConfiguration.Builder(app.currentUser(), app.currentUser().getId())
                .name(appId)
                .schemaVersion(2)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();

        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                Log.v(
                        "EXAMPLE",
                        "Successfully opened a realm with reads and writes allowed on the UI thread."
                );
                backgroundThreadRealm = realm;
            }
        });

    }
    public void configurePWD(){
        Button PWD = (Button) findViewById(R.id.PWD_button);
        PWD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PWD_activity.class));
            }
        });
    }
    public void configureCaregiver(){
        Button PWD = (Button) findViewById(R.id.Caregiver);
        PWD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CaregiverActivity.class));
            }
        });
    }



    public void testButtonOnClick(View v) {
//        Credentials anonymousCredentials = Credentials.anonymous();
//        AtomicReference<User> user = new AtomicReference<User>();
//        app.loginAsync(anonymousCredentials, it -> {
//            if (it.isSuccess()) {
//                Log.v("AUTH", "Successfully authenticated anonymously.");
//                user.set(app.currentUser());
//            } else {
//                Log.e("AUTH", it.getError().toString());
//            }
//        });
        String email = "test123";
        String password = "123456";

        app.getEmailPassword().registerUserAsync(email, password, it -> {
            if (it.isSuccess()) {
                Log.i("EXAMPLE", "Successfully registered user.");
            } else {
                Log.e("EXAMPLE", "Failed to register user: " + it.getError().getErrorMessage());
            }
        });

//
//        app.getEmailPassword().registerUserAsync("test", "123", result -> {
//            if(result.isSuccess()) {
//                Log.v("AUTH", "Successfully regsitered.");
//
//            } else {
//                Log.v("AUTH", "FAILED. " + result.toString());
//            }
//        });

    }

    public void login(View v) {
        AtomicReference<User> user = new AtomicReference<User>();
        Credentials emailPasswordCredentials = Credentials.emailPassword("test123", "123456");
        app.loginAsync(emailPasswordCredentials, it -> {
                    if (it.isSuccess()) {
                        Log.v("AUTH", "Successfully authenticated using an email and password.");
                        user.set(app.currentUser());
                    } else {
                        Log.e("AUTH", it.getError().toString());
                    }
                });
    }

    public void addTestCaregiver(View v) {
        backgroundThreadRealm.executeTransaction(t -> {
//            Caregiver testCaregiver = backgroundThreadRealm.createObject(Caregiver.class, new ObjectId());
//            testCaregiver.setFirstName("Brittany");
//            testCaregiver.setLastName("Spears");
            PWD testPwd = backgroundThreadRealm.createObject(PWD.class, new ObjectId());
            testPwd.setFirstName("Terry");
            testPwd.setPWDCode("123ABC");
        });


//        backgroundThreadRealm.executeTransactionAsync (transactionRealm -> {
//            transactionRealm.insert(testCaregiver);
//        });

        RealmResults<PWD> caregiverRealmResults = backgroundThreadRealm.where(PWD.class).findAll();
        Log.d("",caregiverRealmResults.asJSON());
    }

}