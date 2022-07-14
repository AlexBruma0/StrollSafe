package com.example.strollsafe.pwd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.strollsafe.ui.MainActivity;
import com.example.strollsafe.R;
import com.example.strollsafe.ui.PWDSignupActivity;

public class PWD_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_login);
        configureCreateAccount();
        configurePWDlogin();
        SharedPreferences pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        SharedPreferences.Editor pwdPreferenceEditor = pwdPreferences.edit();
    }
    public void configureCreateAccount() {
        Button PWD = (Button) findViewById(R.id.SignUp);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PWD_login.this, PWDSignupActivity.class));;
            }
        });
    }
    public void configurePWDlogin(){
        Button PWD = (Button) findViewById(R.id.PWDLogin);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ;
            }
        });
    }
}