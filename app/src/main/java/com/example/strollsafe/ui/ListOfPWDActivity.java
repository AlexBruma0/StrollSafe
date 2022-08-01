package com.example.strollsafe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.strollsafe.GeofencingMapsActivity;
import com.example.strollsafe.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.strollsafe.utils.DatabaseManager;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoCollection;


public class ListOfPWDActivity extends AppCompatActivity {
    App app;
    DatabaseManager databaseManager;
    String TAG = "ListOfPWDActivity";
    SharedPreferences pwdPreferences;
    SharedPreferences.Editor pwdPreferenceEditor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pwdPreferences = getSharedPreferences("PWD", MODE_PRIVATE);
        pwdPreferenceEditor = pwdPreferences.edit();
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();
        setContentView(R.layout.activity_listofpwd);

        configureBack();
        configureMap1();
        configureMap2();
        configureMap3();
        configureDelete1();
        configureDelete2();
        configureDelete3();
        stylePage();
    }

    public void configureBack(){
        ImageButton PWD = (ImageButton) findViewById(R.id.quit);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfPWDActivity.this, MainActivity.class));
            }
        });
    }

    public void configureMap1(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map1);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog("Opening Safe Zone Manager...", false);
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(0);
                        Intent intent = new Intent(ListOfPWDActivity.this, GeofencingMapsActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                    dismissProgressDialog();
                });
            }
        });
    }

    public void configureMap2(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map2);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog("Opening Safe Zone Manager...", false);
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(1);
                        Intent intent = new Intent(ListOfPWDActivity.this, GeofencingMapsActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                    dismissProgressDialog();
                });
            }
        });
    }

    public void configureMap3(){
        ImageButton PWD = (ImageButton) findViewById(R.id.Map3);
        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgressDialog("Opening Safe Zone Manager...", false);
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(2);
                        Intent intent = new Intent(ListOfPWDActivity.this, GeofencingMapsActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                    dismissProgressDialog();
                });
            }
        });
    }

    public void configureDelete1(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete1);
        ImageButton A = (ImageButton) findViewById(R.id.Map1);
        Button B = (Button) findViewById(R.id.NAME1);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(0);
                        remove(userId);

                        B.setText("CLICK HERE TO ACTIVATE");
                        PWD.setVisibility(View.GONE);
                        A.setVisibility(View.GONE);
                        B.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void configureDelete2(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete2);
        ImageButton A = (ImageButton) findViewById(R.id.Map2);
        Button B = (Button) findViewById(R.id.NAME2);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(1);
                        remove(userId);

                        B.setText("CLICK HERE TO ACTIVATE");
                        PWD.setVisibility(View.GONE);
                        A.setVisibility(View.GONE);
                        B.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void configureDelete3(){
        ImageButton PWD = (ImageButton) findViewById(R.id.delete3);
        ImageButton A = (ImageButton) findViewById(R.id.Map3);
        Button B = (Button) findViewById(R.id.NAME3);

        PWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.currentUser().refreshCustomData(refreshResult -> {
                    if(refreshResult.isSuccess()) {
                        ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
                        String userId = patientsList.get(2);
                        remove(userId);

                        B.setText("CLICK HERE TO ACTIVATE");
                        PWD.setVisibility(View.GONE);
                        A.setVisibility(View.GONE);
                        B.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void stylePage() {
        startProgressDialog("Loading patients...", false);
        app.currentUser().refreshCustomData(refreshResult -> {
            ArrayList<String> patientsList = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
            if(patientsList != null) {
                if(patientsList.size() >= 3) {
                    findViewById(R.id.pwdListTitle).setVisibility(View.INVISIBLE);
                    findViewById(R.id.pwdListEmailEntry).setVisibility(View.INVISIBLE);
                    findViewById(R.id.pwdListAddButton).setVisibility(View.INVISIBLE);

                } else {
                    findViewById(R.id.pwdListTitle).setVisibility(View.VISIBLE);
                    findViewById(R.id.pwdListEmailEntry).setVisibility(View.VISIBLE);
                    findViewById(R.id.pwdListAddButton).setVisibility(View.VISIBLE);
                }
                for(int x = 0; x < patientsList.size(); x++) {
                    MongoCollection userCollection = databaseManager.getUsersCollection();
                    int finalX = x;
                    userCollection.findOne(new Document("userId", patientsList.get(x))).getAsync(new App.Callback() {
                        @Override
                        public void onResult(App.Result result) {
                            if(result.isSuccess()) {
                                Document pwdInfo = (Document) result.get();

                                switch (finalX) {
                                    case 0:
                                        Log.i(TAG, "STYLING THE FIRST BUTTON");
                                        Button name1 = (Button) findViewById(R.id.NAME1);
                                        ImageButton delete1 = (ImageButton) findViewById(R.id.delete1);
                                        ImageButton map1 = (ImageButton) findViewById(R.id.Map1);

                                        name1.setText(pwdInfo.get("firstName") + " " + pwdInfo.get("lastName"));
                                        name1.setVisibility(View.VISIBLE);
                                        delete1.setVisibility(View.VISIBLE);
                                        map1.setVisibility(View.VISIBLE);

                                        if(finalX == patientsList.size() - 1) {
                                            dismissProgressDialog();
                                        }

                                        break;

                                    case 1:
                                        Log.i(TAG, "STYLING THE SECOND BUTTON");
                                        Button name2 = (Button) findViewById(R.id.NAME2);
                                        ImageButton delete2 = (ImageButton) findViewById(R.id.delete2);
                                        ImageButton map2 = (ImageButton) findViewById(R.id.Map2);

                                        name2.setText(pwdInfo.get("firstName") + " " + pwdInfo.get("lastName"));
                                        delete2.setVisibility(View.VISIBLE);
                                        name2.setVisibility(View.VISIBLE);
                                        map2.setVisibility(View.VISIBLE);

                                        if(finalX == patientsList.size() - 1) {
                                            dismissProgressDialog();
                                        }
                                        break;

                                    case 2:
                                        Log.i(TAG, "STYLING THE THIRD BUTTON");
                                        Button name3 = (Button) findViewById(R.id.NAME3);
                                        ImageButton delete3 = (ImageButton) findViewById(R.id.delete3);
                                        ImageButton map3 = (ImageButton) findViewById(R.id.Map3);

                                        name3.setText(pwdInfo.get("firstName") + " " + pwdInfo.get("lastName"));
                                        delete3.setVisibility(View.VISIBLE);
                                        name3.setVisibility(View.VISIBLE);
                                        map3.setVisibility(View.VISIBLE);

                                        if(finalX == patientsList.size() - 1) {
                                            dismissProgressDialog();
                                        }
                                        break;
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public void add(View view){
        startProgressDialog("Adding patient...", false);
        app.currentUser().refreshCustomData(refreshResult -> {
            ArrayList<String> patientsArray = (ArrayList<String>) app.currentUser().getCustomData().get("patients");
            if(patientsArray == null || patientsArray.size() >= 3) {
                dismissProgressDialog();
                Toast.makeText(ListOfPWDActivity.this, "PWD limit max already reached.", Toast.LENGTH_SHORT).show();
                return;
            }

            EditText editText = (EditText) findViewById(R.id.pwdListEmailEntry);
            String code = editText.getText().toString();

            MongoCollection userCollection = databaseManager.getUsersCollection();
            userCollection.findOne(new Document("email", code)).getAsync(new App.Callback() {
                @Override
                public void onResult(App.Result result) {
                    Document pwdInfo = (Document) result.get();
                    if(pwdInfo == null) {
                        dismissProgressDialog();
                        Toast.makeText(ListOfPWDActivity.this, "PWD can not be found.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(patientsArray.contains(pwdInfo.get("userId"))) {
                        dismissProgressDialog();
                        Toast.makeText(ListOfPWDActivity.this, "This PWD is already linked to your account.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Document careGiverData = new Document("userId", app.currentUser().getId());
                    Document update =  new Document("patients", pwdInfo.get("userId"));
                    userCollection.updateOne(careGiverData, new Document("$push", update)).getAsync(new App.Callback() {
                        @Override
                        public void onResult(App.Result result) {
                            if(result.isSuccess()) {
                                dismissProgressDialog();
                                Toast.makeText(ListOfPWDActivity.this, "PWD has been linked to your account.", Toast.LENGTH_SHORT).show();
                                stylePage();
                            } else {
                                dismissProgressDialog();
                                Toast.makeText(ListOfPWDActivity.this, "Error linked PWD to your account.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        });
    }

    public void remove(String userId) {
        startProgressDialog("Removing patient...", false);
        app.currentUser().refreshCustomData(refreshResult -> {
            MongoCollection userCollection = databaseManager.getUsersCollection();
            Document careGiverData = new Document("userId", app.currentUser().getId());
            Document update =  new Document("patients", userId);
            userCollection.updateOne(careGiverData, new Document("$pull", update)).getAsync(new App.Callback() {
                @Override
                public void onResult(App.Result result) {
                    if(result.isSuccess()) {
                        dismissProgressDialog();
                        Toast.makeText(ListOfPWDActivity.this, "PWD has been removed to your account.", Toast.LENGTH_SHORT).show();
                        stylePage();
                    } else {
                        dismissProgressDialog();
                        Toast.makeText(ListOfPWDActivity.this, "Error removing PWD to your account.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }

    private void startProgressDialog(String message, boolean cancelable) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

}
