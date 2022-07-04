package com.example.strollsafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurePWD();
        configureCaregiver();
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
        Log.d("TEST", "SHOULD PRINT THIS MESSAGE TO THE LOGCAT");
    }

}