package com.example.strollsafe;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class SafezoneManager {

    public GeofencingClient getGeofencingClient() {
        return geofencingClient;
    }

    public void setGeofencingClient(GeofencingClient geofencingClient) {
        this.geofencingClient = geofencingClient;
    }

    public List<Geofence> getGeofenceList() {
        return geofenceList;
    }

    public void setGeofenceList(List<Geofence> geofenceList) {
        this.geofenceList = geofenceList;
    }

    public Location getSfuAvacadoArea() {
        return sfuAvacadoArea;
    }

    public void setSfuAvacadoArea(Location sfuAvacadoArea) {
        this.sfuAvacadoArea = sfuAvacadoArea;
    }

    private GeofencingClient geofencingClient;
    private List<Geofence> geofenceList = new ArrayList<>();
    Location sfuAvacadoArea = new Location(49.278965, -122.916582);


    public SafezoneManager(Context context) {
        geofencingClient = LocationServices.getGeofencingClient(context);

    }

    public void createGeofence() {
        geofenceList.add(new Geofence.Builder()
                .setRequestId("avocado")
                .setCircularRegion(sfuAvacadoArea.getLatitude(), sfuAvacadoArea.getLongitude(), 10)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

}
