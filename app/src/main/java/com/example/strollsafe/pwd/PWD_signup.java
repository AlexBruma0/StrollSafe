package com.example.strollsafe.pwd;

import android.os.Bundle;

import com.example.strollsafe.R;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class PWD_signup extends AppCompatActivity {


    private Button btnMain;
    private TextView txtMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_signup);
        configureBack();

        btnMain = findViewById(R.id.btn_createUser);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uniqueCode = getRandomString(4);
                Log.d("newPWDInfo", uniqueCode);
            }
        });
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
        Button PWD = (Button) findViewById(R.id.pwdBackButton);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}