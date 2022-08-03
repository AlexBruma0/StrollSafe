package com.example.strollsafe.ui.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strollsafe.R;
import com.example.strollsafe.pwd.PWDLocation;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * LocationListViewAdapter.java
 *
 * Description: Display all of the PWD's past locations in an list
 *
 * @since July 31, 2022
 * @author Alvin Tsang
 *
 * Last updated on August 2, 2022
 * Last updated by Alvin Tsang
 * */

@RequiresApi(api = Build.VERSION_CODES.O)
public class LocationListViewAdapter extends RecyclerView.Adapter<LocationListViewAdapter.ViewHolder> {

    private final ArrayList<PWDLocation> PWDLocationList;
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Context context;

    /**
     * Description: Parameterized constructor
     *
     * @param PWDLocationList arraylist of PWD locations
     * @param context context of which this classed is called
     * */
    public LocationListViewAdapter(ArrayList<PWDLocation> PWDLocationList, Context context) {
        this.PWDLocationList = PWDLocationList;
        this.context = context;
    } // end of constructor

    /**
     * Description: Implementation of interface constructor. Inflate the layout
     * */
    @NonNull
    @Override
    public LocationListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.location_view_item, parent, false);
        return new ViewHolder(view);
    } // end of onCreateViewHolder()

    /**
     * Description: Implementation of interface method. Set data to the views of recycler view
     * */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LocationListViewAdapter.ViewHolder holder, int position) {
        PWDLocation location = PWDLocationList.get(position);
        holder.tv_streetAddress.setText(location.getAddress());
        holder.tv_lastHereDateTime.setText("Last here on " +
                location.getLastHereDateTime().format(DATE_FORMAT));
    } // end of onBindViewHolder()

    /**
     * Description: Implementation of interface method. Get the size of PWDLocationList
     *
     * @return size of the list
     * */
    @Override
    public int getItemCount() {
        return PWDLocationList.size();
    } // end of getItemCount()

    /**
     * Description: Class that describes each item in the GUI PWD list
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_streetAddress;
        private TextView tv_lastHereDateTime;

        /**
         * Description: Constructor to set the text of the item field
         * */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_streetAddress = itemView.findViewById(R.id.tv_streetAddress);
            tv_lastHereDateTime = itemView.findViewById(R.id.tv_lastHereDateTime);
        } // end of constructor

    } // end of ViewHolder

} // end of LocationListViewAdapter.java