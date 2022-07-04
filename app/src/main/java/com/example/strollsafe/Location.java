package com.example.strollsafe;

public class Location{
    private double Longitude;
    private double Latitude;

    public void makeLocation() {
        this.Latitude = 0;
        this.Longitude = 0;
    }
    public void setLongitude(double a){
        this.Longitude = a;
    }
    public void setLatitude(double a){
        this.Latitude = a;
    }
    public double getLongitude(){
        return this.Longitude;
    }
    public double getLatitude(){
        return this.getLatitude();
    }
}
