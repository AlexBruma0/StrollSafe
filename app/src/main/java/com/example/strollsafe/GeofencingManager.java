package com.example.strollsafe;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class GeofencingManager {

    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofenceList = new ArrayList<>();
    Location sfuAvacadoArea = new Location(49.278965, -122.916582);


    public GeofencingManager(Context context) {
        geofencingClient = LocationServices.getGeofencingClient(context);

    }

    public void createGeofence() {
        geofenceList.add(new Geofence.Builder()
                .setRequestId("NAME")
                .setCircularRegion(sfuAvacadoArea.getLatitude(), sfuAvacadoArea.getLongitude(), 10)
                .setExpirationDuration(1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

}
