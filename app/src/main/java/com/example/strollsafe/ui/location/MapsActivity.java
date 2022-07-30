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
import android.location.Address;
import android.location.Geocoder;
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
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private static final String SHARED_PREFS = "StrollSafe: LocationList";
    private ArrayList<PWDLocation> PWDLocationList;
    private LatLng lastLocationPlaced;
    private final float ZOOM_FACTOR = 12.0F;



    @RequiresApi(api = Build.VERSION_CODES.O)
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

//        PWDLocationList myApplication = (PWDLocationList) getApplicationContext();
//        savedLocations = myApplication.getPWDLocationList();
    } // end of onCreate()

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (PWDLocationList.size() < 1) { // if no saved locations, map will not display
            Toast.makeText(MapsActivity.this, "No waypoints saved",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // convert all locations to LatLng
            for (PWDLocation location : PWDLocationList) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                Geocoder geocoder = new Geocoder(MapsActivity.this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                    markerOptions.title(addresses.get(0).getAddressLine(0));
                } catch (Exception e) {
                    markerOptions.title("Lat: " + location.getLatitude() + "\n" +
                            "Lon: " + location.getLongitude());
                }
                // place location as a pin on the map
                mMap.addMarker(markerOptions);
                lastLocationPlaced = latLng;
            }
            // when maps is opened, zoom in to the last saved location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocationPlaced, ZOOM_FACTOR));
            mMap.setOnMarkerClickListener(marker -> {
                Integer clicks = (Integer) marker.getTag();
                if (clicks == null) {
                    clicks = 0;

                }
                clicks++;
                marker.setTag(clicks);
                Toast.makeText(MapsActivity.this, "Marker " + marker.getTitle() +
                        " was clicked " + clicks + " times", Toast.LENGTH_SHORT).show();

                return false;
            });
        }
    } // end of onMapReady()

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        // creating a variable for gson.
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

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<PWDLocation>>() {}.getType();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("Locations", null);

        // in below line we are getting data from gson
        // and saving it to our array list
        PWDLocationList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (PWDLocationList == null) {
            PWDLocationList = new ArrayList<>();
        }
    } // end of loadData()

}// end of MapsActivity.java

