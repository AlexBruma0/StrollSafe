package com.example.strollsafe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.strollsafe.R;
public class PWDActivity extends AppCompatActivity {
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        setContentView(R.layout.activity_pwd);
        TextView code = findViewById(R.id.viewPWDCODE);
        code.setText(pwdPreferences.getString("code","error"));
        configureBack();
        configurePWDLocationInformation();
    }

    public void configureBack() {
        Button PWD = (Button) findViewById(R.id.pwdBackButton);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void configurePWDLocationInformation() {
        Button PWD = (Button) findViewById(R.id.GPSInfoButton);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PWDActivity.this, PWDLocationInformationActivity.class));
            }
        });
    }
}