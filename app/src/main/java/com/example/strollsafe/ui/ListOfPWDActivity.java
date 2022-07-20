package com.example.strollsafe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.strollsafe.R;

public class ListOfPWDActivity extends AppCompatActivity {
    public static final String TEXT = "text";
    private Button button;
    private String text;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofpwd);
        configureBack();
        configureMap();
        configureDelete();
        configureNew();
    }

    public void configureBack(){
        ImageButton PWD = (ImageButton) findViewById(R.id.quit);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void configureMap(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map1);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, PWDLocationInformationActivity.class));
            }
        });
    }


    public void configureDelete(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete1);
        ImageButton A = (ImageButton) findViewById(R.id.Map1);
        Button B = (Button) findViewById(R.id.NAME1);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                B.setVisibility(View.GONE);
                PWD.setVisibility(View.GONE);
                A.setVisibility(View.GONE);
            }
        });
    }
    public void configureNew(){
        ImageButton newPWD = (ImageButton) findViewById(R.id.NewPWD);
//        ImageButton delete = (ImageButton) findViewById(R.id.delete1);
//        ImageButton map = (ImageButton) findViewById(R.id.Map1);
//        Button name = (Button) findViewById(R.id.NAME1);
        newPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startActivity(new Intent(ListOfPWDActivity.this, PWDListActivity.class));
//                name.setVisibility(View.VISIBLE);
//                name.setText("name");
//                delete.setVisibility(View.VISIBLE);
//                map.setVisibility(View.VISIBLE);
            }
        });
    }
    public void saveData(){
        SharedPreferences pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        SharedPreferences.Editor PreferenceEditor = pwdPreferences.edit();

        PreferenceEditor.putString(TEXT,button.getText().toString());
        PreferenceEditor.apply();
        Toast.makeText(this, "DATA SAVED", Toast.LENGTH_SHORT).show();
    }
    public void loadData(){
        SharedPreferences pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        text = pwdPreferences.getString(TEXT, "");

    }
    public void updateViews(){
        button.setText(text);
    }
}
