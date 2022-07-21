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

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;

import androidx.appcompat.app.AppCompatActivity;


import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.strollsafe.R;


import com.example.strollsafe.pwd.location.PWDLocations;

import java.util.ArrayList;
import java.util.List;


public class ShowSavedLocationsList extends AppCompatActivity {

    ListView lv_wayPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_locations_list);

        lv_wayPoints = findViewById(R.id.lv_wayPoints);

        // get list of saved locations
        PWDLocations myApplication = (PWDLocations) getApplicationContext();
        ArrayList<Location> savedLocations = myApplication.getMyLocations();

        // convert locations to a street address if possible
        ArrayList<String> savedAddresses = new ArrayList<>();
        for (Location location: savedLocations) {
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
        lv_wayPoints.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, savedAddresses));

    } // end of onCreate()
} // end of ShowSavedLocationsList.java
