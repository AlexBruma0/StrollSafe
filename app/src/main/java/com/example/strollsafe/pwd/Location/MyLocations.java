/*
* MyLocations.java
*
* Description: Store the location of the PWD as list
*
* Created on: July 18, 2022
* Created by: Alvin Tsang
*
* */

package com.example.strollsafe.pwd.Location;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;


public class MyLocations extends Application {

    private static MyLocations singleton;
    private ArrayList<Location> myLocations;



    /**
     * Description: Initialize the activity
     * */
    public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
    } // end of onCreate()

    /**
     * Description: Get the instance of the activity
     * */
    public MyLocations getInstance() {
        return singleton;
    } // end of getInstance()

    /**
     * Description get the list of saved locations
     *
     * @return list of locations
     * */
    public ArrayList<Location> getMyLocations() {
        return myLocations;
    } // end of getMyLocations()


} // end of MyLocations.java
