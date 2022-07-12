package com.example.strollsafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.strollsafe.R;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        configureNewPWD();
        configureCaregiver();
    }
    public void configureNewPWD(){
        Button PWD = (Button) findViewById(R.id.button_new_PWD);
        PWD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PWDSignupActivity.class));
            }
        });
    }
    public void configureCaregiver(){
        Button PWD = (Button) findViewById(R.id.button_new_caregiver);
        PWD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewCaregiverActivity.class));
            }
        });
    }



    public void testButtonOnClick(View v) {
        Log.d("TEST", "SHOULD PRINT THIS MESSAGE TO THE LOGCAT");
    }

}
