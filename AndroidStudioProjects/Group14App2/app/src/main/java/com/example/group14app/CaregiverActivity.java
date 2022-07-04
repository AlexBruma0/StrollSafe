package com.example.group14app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CaregiverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);
        configureBack();
        configureLocation();
        configureList();
    }
    public void configureBack(){
        Button PWD = (Button) findViewById(R.id.Backbutton2);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void configureLocation(){
        Button PWD = (Button) findViewById(R.id.map);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaregiverActivity.this, locationActivity.class));
            }
        });
    }
    public void configureList(){
        Button PWD = (Button) findViewById(R.id.PWDlist);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaregiverActivity.this, PWDlist.class));
            }
        });
    }
}