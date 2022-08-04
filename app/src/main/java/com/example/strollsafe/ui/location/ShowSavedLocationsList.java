package com.example.strollsafe.ui.location;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.example.strollsafe.R;
import com.example.strollsafe.pwd.PWDLocation;
import com.example.strollsafe.utils.DatabaseManager;
import com.example.strollsafe.utils.SafeZone;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.bson.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoCollection;

/**
 * ShowSavedLocationsList.java
 *
 * Description: Show the list of saved locations as addresses
 *
 * @since July 18, 2022
 * @author Alvin Tsang
 *
 * Last modified on; July 21, 2022
 * Last modified by: Alvin Tsang
 * */

@RequiresApi(api = Build.VERSION_CODES.O)
public class ShowSavedLocationsList extends AppCompatActivity {

    private static final String SHARED_PREFS = "StrollSafe: LocationList";
    private DatabaseManager databaseManager;
    private App app;
    private MongoCollection userCollection;
    private ArrayList<PWDLocation> PWDLocationList = new ArrayList<>();
    private LocationListViewAdapter adapter;
    private RecyclerView rv_locationList;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_locations_list);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        databaseManager = new DatabaseManager(this);
        app = databaseManager.getApp();
        userCollection = databaseManager.getUsersCollection();

        rv_locationList = findViewById(R.id.rv_locationList);

        // get list of saved locations from shared preferences
        loadData();
    } // end of onCreate()

    /**
     * Description: Initialize adapter and the recycler view
     * */
    private void buildRecyclerView() {
        // initializing adapter class
        adapter = new LocationListViewAdapter(PWDLocationList, ShowSavedLocationsList.this);

        // adding layout manager to our recycler view
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_locationList.setHasFixedSize(true);

        // setting layout manager to our recycler view
        rv_locationList.setLayoutManager(manager);

        // setting adapter to our recycler view
        rv_locationList.setAdapter(adapter);
    } // end of buildRecyclerView()


    /**
     * Description: Read the shared preference folder for the list of saved locations and
     *              store them in an arraylist
     * */
    private void loadData() {
        userCollection.findOne(new Document("userId", userId)).getAsync(callback -> {
            if (callback.isSuccess()) {
                Document userInfo = (Document) callback.get();
                ArrayList<Document> locations = (ArrayList<Document>) userInfo.get("locations");
                for(Document location : locations) {
                    PWDLocationList.add(new PWDLocation(location));
                }
                buildRecyclerView();
            }
        });
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//
//        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
//                new TypeAdapter<LocalDateTime>() {
//                    @Override
//                    public void write(JsonWriter jsonWriter, LocalDateTime date) throws IOException {
//                        jsonWriter.value(date.toString());
//                    }
//                    @Override
//                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
//                        return LocalDateTime.parse(jsonReader.nextString());
//                    }
//                }).setPrettyPrinting().create();
//
//
//        Type type = new TypeToken<ArrayList<PWDLocation>>() {}.getType();
//        String json = sharedPreferences.getString("Locations", null);
//        PWDLocationList = gson.fromJson(json, type);
//
//        // checking below if the array list is empty or not
//        if (PWDLocationList == null) {
//            PWDLocationList = new ArrayList<>();
//        }
    } // end of loadData()
} // end of ShowSavedLocationsList.java
