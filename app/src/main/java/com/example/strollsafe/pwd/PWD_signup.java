/*
 * PWD_signup.java
 *
 * Last modified on July 14, 2022
 * Last modified by: Alvin Tsang
 * */


package com.example.strollsafe.pwd;

import android.os.Bundle;

import com.example.strollsafe.R;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class PWD_signup extends AppCompatActivity {



    // back button
    private Button btn_backToMain;

    // create user
    private Button btn_createUser;
    // PWD basic information
    private EditText et_PWD_lastName;
    private EditText et_PWD_firstName;
    private EditText et_dateOfBirth;

    // PWD address
    private EditText et_addressLine1;
    private EditText et_addressLine2;
    private EditText et_addressCity;
    private EditText et_addressRegion;
    private EditText et_addressPostalCode;
    private EditText et_addressCountry;

    // Primary Caregiver emergency contact
    private EditText et_emergencyContact_lastName;
    private EditText et_emergencyContact_firstName;
    private EditText et_emergencyContact_phoneNumber;
    private EditText et_emergencyContact_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_signup);
        configureBack();

        btn_createUser = findViewById(R.id.btn_createUser);

        btn_createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {










                String uniqueCode = getRandomString(4);
                Log.d("newPWDInfo", uniqueCode);
            }
        });
        configureBack();
    } // end of onCreate()

    private static String getRandomString(int i){
        final String characters = "abcdefghiklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        while( i > 0 ){
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            i--;
        }
        return result.toString().toUpperCase(Locale.ROOT);
    }

    public void configureBack() {
        Button PWD = (Button) findViewById(R.id.btn_backToMain);
        PWD.setOnClickListener(view -> finish());
    }
}