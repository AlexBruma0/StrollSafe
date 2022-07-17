package com.example.strollsafe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.strollsafe.R;

public class ListOfPWDActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofpwd);
        configureBack();
        configureNewPWD();
        configureMap();
        configureMap0();
    }

    public void configureBack(){
        ImageButton PWD = (ImageButton) findViewById(R.id.imageButton);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void configureNewPWD(){
        ImageButton PWD = (ImageButton) findViewById(R.id.imageButton2);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, PWDListActivity.class));
            }
        });
    }

    public void configureMap(){
        ImageButton PWD = (ImageButton) findViewById(R.id.imageButton4);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, PWDLocationInformationActivity.class));
            }
        });
    }

    public void configureMap0(){
        ImageButton PWD = (ImageButton) findViewById(R.id.imageButton3);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, PWDLocationInformationActivity.class));
            }
        });
    }
}