package com.example.strollsafe;

import android.Manifest.permission;
import android.annotation.SuppressLint;

import com.example.strollsafe.databinding.ActivityMapsBinding;
import com.example.strollsafe.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeofencingMapsActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap map;
    private LocationManager locationManager;
    LatLng sfu = new LatLng(49.278965, -122.916582);
    LatLng bp = new LatLng(49.186469, -122.847344);
    private List<MarkerOptions> geoFences = Arrays.asList(new MarkerOptions().position(sfu).title("Marker in SFU"), new MarkerOptions().position(bp).title("BP"));
    LatLngBounds.Builder allMarkers;

    /**
     * Flag indicating whether a requested permission has been denied after returning in {@link
     * #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing_map);
        Toolbar topBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topBar);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(49.278965, -122.916582);

        allMarkers = new LatLngBounds.Builder();
        for (MarkerOptions markerOptions : geoFences) {
            allMarkers.include(markerOptions.getPosition());
            map.addMarker(markerOptions);
            map.addCircle(new CircleOptions().radius(10).center(markerOptions.getPosition()).clickable(true).fillColor(0x750000FF));
        }

        map.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(@NonNull Circle circle) {
                Toast.makeText(GeofencingMapsActivity.this, "Moving map to your location.", Toast.LENGTH_SHORT).show();
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18));
                return true;
            }
        });
        LatLngBounds bounds = allMarkers.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.moveCamera(cu);
        enableMyLocation();
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(map.mylo, 16));
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, 4444, true);
        // [END maps_check_location_permission]
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Moving map to your location.", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, String.format("Current location:\n%f\n%f", location.getLatitude(), location.getLongitude()), Toast.LENGTH_LONG).show();
    }

    // [START maps_check_location_permission_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }
    // [END maps_check_location_permission_result]

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private boolean requestBackgroundLocation() {
        //locationPermissionRequest.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED ? false : true;
    }


    public void showAllMarkersOnclick(View view) {
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(allMarkers.build(), 150);
        map.animateCamera(cu);
    }
}