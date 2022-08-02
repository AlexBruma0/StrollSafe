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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.strollsafe.R;
import com.example.strollsafe.pwd.PWDLocation;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.example.strollsafe.databinding.ActivityMapsBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String SHARED_PREFS = "StrollSafe: LocationList";
    private ArrayList<PWDLocation> PWDLocationList;
    private LatLng lastLocationPlaced;
    private final float ZOOM_FACTOR = 12.0F;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    } // end of onCreate()

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (PWDLocationList.size() < 1) { // if no saved locations, map will not display
            Toast.makeText(MapsActivity.this, "No locations saved",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // convert all locations to LatLng
            for (PWDLocation location : PWDLocationList) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                // show address and last access details when the marker is touched
                markerOptions.title(location.getAddress());
                markerOptions.snippet("Last here on " + location.getLastHereDateTime());
                // place location as a pin on the map
                mMap.addMarker(markerOptions);
                lastLocationPlaced = latLng;
            }
            // when maps is opened, zoom in to the last saved location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocationPlaced, ZOOM_FACTOR));
        }
    } // end of onMapReady()

    /**
     * Description: Read the shared preference folder for the list of saved locations and
     *              store them in an arraylist
     * */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter, LocalDateTime date) throws IOException {
                        jsonWriter.value(date.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).setPrettyPrinting().create();

        Type type = new TypeToken<ArrayList<PWDLocation>>() {}.getType();
        String json = sharedPreferences.getString("Locations", null);
        PWDLocationList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (PWDLocationList == null) {
            PWDLocationList = new ArrayList<>();
        }
    } // end of loadData()

}// end of MapsActivity.java

