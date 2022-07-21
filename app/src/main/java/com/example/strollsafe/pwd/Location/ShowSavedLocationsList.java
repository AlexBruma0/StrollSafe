package com.example.strollsafe.pwd.Location;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.strollsafe.R;
import com.example.strollsafe.pwd.Location.MyLocations;

import java.util.ArrayList;

public class ShowSavedLocationsList extends AppCompatActivity {

    ListView lv_wayPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_locations_list);

        lv_wayPoints = findViewById(R.id.lv_wayPoints);

        MyLocations myApplication = (MyLocations) getApplicationContext();
        ArrayList<Location> savedLocations = myApplication.getMyLocations();
        lv_wayPoints.setAdapter(new ArrayAdapter<Location>(this,
                android.R.layout.simple_expandable_list_item_1, savedLocations));

    }
}