/**
 * ShowSavedLocationsList.java
 *
 * Description: Show the list of saved locations as addresses
 *
 * Created on: July 18, 2022
 * Created by: Alvin Tsang
 *
 * Last modified on; July 21, 2022
 * Last modified by: Alvin Tsang
 *
 * */

package com.example.strollsafe.ui.location;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.strollsafe.R;
import com.example.strollsafe.pwd.PWDLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowSavedLocationsList extends AppCompatActivity {

    private static final String SHARED_PREFS = "StrollSafe: LocationList";
    private ArrayList<PWDLocation> PWDLocationList;
    private ListView lv_wayPoints;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_locations_list);

        lv_wayPoints = findViewById(R.id.lv_wayPoints);

        // get list of saved locations
        loadData();

        // convert locations to a street address if possible
        ArrayList<String> savedAddresses = new ArrayList<>();
        for (PWDLocation location: PWDLocationList) {
            Geocoder geocoder = new Geocoder(ShowSavedLocationsList.this);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                savedAddresses.add(addresses.get(0).getAddressLine(0));
            } catch (Exception e) {
                String address = "Lat: " + location.getLatitude() + "Lon: " + location.getLongitude() +
                        "\n" + "Unable to get street address";
                savedAddresses.add(address);
            }
        }
        // save all locations as a list and display for the user
        lv_wayPoints.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, savedAddresses));

    } // end of onCreate()


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter, LocalDateTime date) throws IOException {
                        jsonWriter.value(date.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).setPrettyPrinting().create();


        Type type = new TypeToken<ArrayList<PWDLocation>>() {}.getType();
        String json = sharedPreferences.getString("Locations", null);
        PWDLocationList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (PWDLocationList == null) {
            PWDLocationList = new ArrayList<>();
        }
    } // end of loadData()



} // end of ShowSavedLocationsList.java
