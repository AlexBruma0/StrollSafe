/*
* Description: Get's the PWD device's location
*
* Created on: June 4, 2022
* Created by: Alvin Tsang
*
* Last modified date: June 5, 2022
* Last modified by: Alvin Tsang
*
* */
package com.example.strollsafe.pwd.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strollsafe.MapsActivity;
import com.example.strollsafe.ShowSavedLocationsList;
import com.example.strollsafe.pwd.PWDLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import com.example.strollsafe.R;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

public class PWDLocationInformation extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30; // seconds
    public static final int FAST_UPDATE_INTERVAL = 10; // seconds
    public static final int LOCATION_REQUEST_PRIORITY = Priority.PRIORITY_HIGH_ACCURACY;
    private static final int PERMISSIONS_CODE_ALL = 99; // any permission code

    public final PWDLocation locationArray = new PWDLocation();

    // references to all UI elements
    private TextView tv_lat;
    private TextView tv_lon;
    private TextView tv_altitude;
    private TextView tv_accuracy;
    private TextView tv_speed;
    private TextView tv_sensor;
    private TextView tv_updates;
    private TextView tv_address;
    private TextView tv_breadCrumbCount;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch sw_locationsupdates;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch sw_gps;

    private Button btn_newWayPoint;
    private Button btn_showWayPoints;
    private Button btn_showMap;

    // current location
    Location currentLocation;
    // list of saved locations
    ArrayList<Location> savedLocations;


    // Google's API for location services
    FusedLocationProviderClient fusedLocationProviderClient;

    // location request is a config file for all settings related to the FusedLocationProviderClient
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    static String[] PERMISSIONS;






    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_location_information);

        PERMISSIONS = new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        // give each UI variable a value
        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);

        btn_newWayPoint = findViewById(R.id.btn_newWayPoint);
        btn_showWayPoints = findViewById(R.id.btn_showWayPoints);
        tv_breadCrumbCount = findViewById(R.id.tv_breadCrumbCount);
        btn_showMap = findViewById(R.id.btn_showMap);

        // set all properties of LocationRequest
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LOCATION_REQUEST_PRIORITY);
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        // update frequency with max power & accuracy
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);

        // event that is triggered whenever the update interval is met
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    updateUIValues(location);
                }
            }
        };

        // add a waypoint to list
        MyLocations myApplication = (MyLocations) getApplicationContext();
        savedLocations = myApplication.getMyLocations();
        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add location to list
                savedLocations = myApplication.getMyLocations();
                savedLocations.add(currentLocation);
                updateUIValues(currentLocation);
            }
        });




        // GPS switch
        sw_gps.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (sw_gps.isChecked()) { // if switch is turned on, use GPS sensors
                    locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensor");
                } else { // if swtich is turned off, use towers and WIFI
                    locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using towers + WIFI");
                }

            }
        });


        // get constant location updates
        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (sw_locationsupdates.isChecked()) { // tracking is turned on
                    startLocationUpdates();
                } else { // tracking is turned off
                    stopLocationUpdates();
                }
            }
        });


        btn_showWayPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PWDLocationInformation.this, ShowSavedLocationsList.class);
                startActivity(intent);
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PWDLocationInformation.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        updateGPS();
    } // end of onCreate()

    /**
     * Description: If permission is granted, track location. Otherwise, get permission from
     *              the user and then track location
     * */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private void startLocationUpdates() {
        tv_updates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS, PERMISSIONS_CODE_ALL);
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        updateGPS();
    }

    /**
     * Description: Disable tracking of user device.
     * */
    @SuppressLint("SetTextI18n")
    private void stopLocationUpdates() {
        tv_updates.setText("Location is NOT being tracked");
        tv_lat.setText("Not tracking location");
        tv_lon.setText("Not tracking location");
        tv_speed.setText("Not tracking location");
        tv_address.setText("Not tracking location");
        tv_accuracy.setText("Not tracking location");
        tv_altitude.setText("Not tracking location");
        tv_sensor.setText("Not tracking location");

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    } // end of stopLocationUpdates()


    /**
     * Description: Verify that permission has been granted to access GPS
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_CODE_ALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permission", "Fine permission granted");
                    updateGPS();
                } else { // permission is not granted, exit program
                    Toast.makeText(this, "This app requires permission to be granted to work properly",
                            Toast.LENGTH_SHORT).show();
                    Log.d("permission", "Fine permission denied");
                    finish();
                }

                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permission", "Coarse permission granted");
                    updateGPS();
                } else { // permission is not granted, exit program
                    Toast.makeText(this, "This app requires permission to be granted to work properly",
                            Toast.LENGTH_SHORT).show();
                    Log.d("permission", "Coarse permission denied");
                    finish();
                }
        }
    } // end of onRequestPermissionResult()


    /**
     * Description: Get permissions from the user to track GPS, get the current location
     *              from the fused client and update the UI
     */
    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(PWDLocationInformation.this);

        // dealing with permissions
        if (ActivityCompat.checkSelfPermission(PWDLocationInformation.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                    new OnSuccessListener<Location>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Log.d("GPSStatus", "GPS is on");
                                updateUIValues(location);
                                currentLocation = location;
                            } else {
                                if (ActivityCompat.checkSelfPermission(PWDLocationInformation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(PERMISSIONS, PERMISSIONS_CODE_ALL);
                                }
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }

                        }
                    });
        } else { // no permissions, must be requested
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // os version greater than marshmallow
                requestPermissions(PERMISSIONS, PERMISSIONS_CODE_ALL);

            }
        }
    } // end of updateGPS()


    /**
     * Description: Update all TextView objects with a new location
     * */
    @SuppressLint("SetTextI18n")
    private void updateUIValues(Location location) {
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if (location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tv_altitude.setText("Not available");
        }
        if (location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.getSpeed()));
        } else {
            tv_speed.setText("Not available");
        }

        Geocoder geocoder = new Geocoder(PWDLocationInformation.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            tv_address.setText("Unable to get street address");
        }

        // show the number of waypoints saved
        tv_breadCrumbCount.setText(Integer.toString(savedLocations.size()));


    } // end of updateUIValues()


} // end of PWDLocationInformation.java
