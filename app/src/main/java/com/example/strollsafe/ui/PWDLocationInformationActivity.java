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
package com.example.strollsafe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strollsafe.pwd.PWDLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import com.example.strollsafe.R;

public class PWDLocationInformationActivity extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30; // seconds
    public static final int FAST_UPDATE_INTERVAL = 10; // seconds
    public static final int LOCATION_REQUEST_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private static final int PERMISSIONS_CODE_ALL = 99; // any permission code

    public final PWDLocation locationArray = new PWDLocation();

    // references to all UI elements
    TextView tv_lat;
    TextView tv_lon;
    TextView tv_altitude;
    TextView tv_accuracy;
    TextView tv_speed;
    TextView tv_sensor;
    TextView tv_updates;
    TextView tv_address;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_locationsupdates;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_gps;

    // Google's API for location services
    FusedLocationProviderClient fusedLocationProviderClient;

    // location request is a config file for all settings related to the FusedLocationProviderClient
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    static String[] PERMISSIONS;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_location_information);

        // give each UI variable a value
        tv_lat = this.findViewById(R.id.tv_lat);
        tv_lon = this.findViewById(R.id.tv_lon);
        tv_altitude = this.findViewById(R.id.tv_altitude);
        tv_accuracy = this.findViewById(R.id.tv_accuracy);
        tv_speed = this.findViewById(R.id.tv_speed);
        tv_sensor = this.findViewById(R.id.tv_sensor);
        tv_updates = this.findViewById(R.id.tv_updates);
        tv_address = this.findViewById(R.id.tv_address);
        sw_locationsupdates = this.findViewById(R.id.sw_locationsupdates);
        sw_gps = this.findViewById(R.id.sw_gps);

        // load all required permissions into the String array
        PERMISSIONS = new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
        };


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
//                    updateUIValues(location);
                    updateLocationArray(location);
                }
            }
        };

        // GPS switch
        sw_gps.setOnClickListener(view -> {
            if (sw_gps.isChecked()) { // if switch is turned on, use GPS sensors
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                tv_sensor.setText("Using GPS sensor");
            } else { // if switch is turned off, use towers and WIFI
                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                tv_sensor.setText("Using towers + WIFI");
            }

        });

        // get constant location updates
        sw_locationsupdates.setOnClickListener(view -> {
            if (sw_locationsupdates.isChecked()) { // tracking is turned on
                startLocationUpdates();
            } else { // tracking is turned off
                stopLocationUpdates();
            }
        });

        updateGPS();
    } // end of onCreate()


    /**
     * Description: If permission is granted, track location. Otherwise, get permission from
     *              the user and then track location
     * */
    @SuppressLint("SetTextI18n")
    private void startLocationUpdates() {
        tv_updates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(PERMISSIONS, PERMISSIONS_CODE_ALL);
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());
        updateGPS();
    } // end of startLocationUpdates()


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
                    Log.d("permissionGranted", "Coarse location permission granted");
                    updateGPS();
                } else { // permission is not granted, exit program
                    Log.d("permissionDenied", "Coarse location permission denied");
                    Toast.makeText(this, "This app requires permission " +
                                    "to be granted to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }

                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permissionGranted", "Fine location permission granted");
                    updateGPS();
                } else { // permission is not granted
                    Log.d("permissionDenied", "fine location permission denied");
                }

                if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permissionGranted", "Internet permission granted");
                    updateGPS();
                } else { // permission is not granted
                    Log.d("permissionDenied", "Internet location permission denied");
                }
        }
    } // end of onRequestPermissionResult()


    /**
     * Description: Get permissions from the user to track GPS, get the current location
     * from the fused client and update the UI
     */
    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                PWDLocationInformationActivity.this);

        // dealing with permissions
        if (ActivityCompat.checkSelfPermission(PWDLocationInformationActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                    location -> {
                        if (location != null) {
                            Log.d("GPSStatus", "GPS is on");
//                                updateUIValues(location);
                            updateLocationArray(location);
                        } else {
                            if (ActivityCompat.checkSelfPermission(
                                    PWDLocationInformationActivity.this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                    PackageManager.PERMISSION_GRANTED) {

                                requestPermissions(PERMISSIONS, PERMISSIONS_CODE_ALL);
                            }
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                    locationCallback, Looper.getMainLooper());
                        }
                    });
        } else { // no permissions, must be requested
            requestPermissions(PERMISSIONS, PERMISSIONS_CODE_ALL);
        }
    } // end of updateGPS()

    /**
     * Description: Update the location array with the most recent location update
     * */
    private void updateLocationArray(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float accuracy = location.getAccuracy();
        String address;
        Geocoder geocoder = new Geocoder(PWDLocationInformationActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            address = ("Unable to get street address");
        }

        locationArray.updateLocationValues(latitude, longitude, accuracy, address);
        updateUIValues(location);
    } // end of updateLocationArray()

    /**
     * Description: Update all TextView objects with a new location
     */
    @SuppressLint("SetTextI18n")
    private void updateUIValues(Location location) {
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));

        // accuracy is the estimated horizontal accuracy radius in meters of
        // this location at the 68th percentile confidence level
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        // if the os is able to detect altitude
        // altitude of this location in meters above the WGS84 reference ellipsoid.
        if (location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tv_altitude.setText("Not available");
        }
        // if the os is able to detect speed
        // speed at the time of this location in meters per second
        if (location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.getSpeed()));
        } else {
            tv_speed.setText("Not available");
        }

        Geocoder geocoder = new Geocoder(PWDLocationInformationActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            tv_address.setText("Unable to get street address");
        }

    } // end of updateUIValues()


} // end of PWDLocationInformation.java
