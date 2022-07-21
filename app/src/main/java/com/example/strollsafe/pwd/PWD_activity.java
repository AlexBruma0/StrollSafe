package com.example.strollsafe.pwd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.strollsafe.R;
import com.example.strollsafe.ui.Location.PWDLocationInformationActivity;

public class PWD_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);
        configureBack();
        configurePWDLocationInformation();
    }

    public void configureBack() {
        Button PWD = (Button) findViewById(R.id.pwdBackButton);
        PWD.setOnClickListener(view -> finish());
    }

    public void configurePWDLocationInformation() {
        Button PWD = (Button) findViewById(R.id.GPSInfoButton);
        PWD.setOnClickListener(view -> startActivity(new Intent(
                PWD_activity.this, PWDLocationInformationActivity.class))
        );
    }
}