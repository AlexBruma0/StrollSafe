package com.example.strollsafe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.strollsafe.R;

public class PWDListActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button back;
    private Button name;


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwdlist);




        backB();



    }
//    public void disable(View v){
//        v.setEnabled(false);
//        Button b = (Button) v;
//        b.setText("hi");
//    }

    public void backB() {
        back = (Button) findViewById(R.id.backbutton);
        editText = (EditText) findViewById(R.id.editTextTextPassword);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back = (Button) findViewById(R.id.backbutton);
                Intent i =new Intent(PWDListActivity.this, ListOfPWDActivity.class);
                i.putExtra("cool",editText.getText().toString());
                startActivity(i);
            }
        });
    }
}