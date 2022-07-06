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

public class PWDLocation {

    private final String[] locationArray;

    private double latitude;
    private double longitude;
    private float accuracy;
    private String address;

    /**
     * Description: Default constructor
     * */
    public PWDLocation() {
        this.locationArray = new String[4];
    }

    /**
     * Description: Parameterized constructor
     * */
    public PWDLocation(double latitude, double longitude, float accuracy, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.address = address;
        this.locationArray = new String[4];

        updateLocationValues(latitude, longitude, accuracy, address);
    } // end of constructor

    /**
     * Description: Returns the PWD GPS information as an ArrayList
     * */
    public String[] getPWDLocationArray() {
        return locationArray;
    }

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
     * Description: Update the location array
     * */
    public void updateLocationValues(double latitude, double longitude,
                                        float accuracy, String address) {
        locationArray[0] = String.valueOf(latitude);
        locationArray[1] = String.valueOf(longitude);
        locationArray[2] = String.valueOf(accuracy);
        locationArray[3] = String.valueOf(address);

    } // end of updateLocationValues()

} // end of PWDLocation.java


