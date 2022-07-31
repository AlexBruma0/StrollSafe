/*
* Description: Class that store the most recent location of the PWD's device
*
* Created on: June 6, 2022
* Created by: Alvin Tsang
*
* Last modified date: June 6, 2022
* Last modified by: Alvin Tsang
* */

package com.example.strollsafe.pwd;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class PWDLocation {

//    private final String[] locationArray;

    private double latitude;
    private double longitude;
    private float accuracy;
    private String address;
    private LocalDateTime dateTime;

    /**
     * Description: Default constructor
     * */
//    public PWDLocation() {
//        this.locationArray = new String[5];
//    }

    /**
     * Description: Parameterized constructor
     * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public PWDLocation(double latitude, double longitude, float accuracy, String address) {
//        this.locationArray = new String[5];
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.address = address;
        this.dateTime = LocalDateTime.now();
    } // end of constructor

    /**
     * Description: Returns the PWD GPS information as an ArrayList
     * */
//    public String[] getPWDLocationArray() {
//        return locationArray;
//    }

    /**
     * Description: Returns the latitude of the PWD's location in degrees
     * */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Description: Returns the longitude of teh PWD's location in degrees
     * */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Description: Returns the estimated horizontal accuracy radius in meters of
     *              this location at the 68th percentile confidence level
     * */
    public float getAccuracy() {
        return accuracy;
    }

    /**
     * Description: Returns the first address line of the location
     * */
    public String getAddress() {
        return address;
    }

    /**
     * Description: Returns the first address line of the location
     * */
    public LocalDateTime getDate() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @NonNull
    @Override
    public String toString() {
        return(
                "Latitude: " + this.latitude + "\n" +
                "Longitude: " + this.longitude + "\n" +
                "Accuracy: " + this.accuracy + "\n" +
                "Address: " + this.address + "\n" +
                "Date: " + this.dateTime.toString() + "\n"
                );
    }


} // end of PWDLocation.java


