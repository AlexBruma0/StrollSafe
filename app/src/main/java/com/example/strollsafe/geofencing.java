package com.example.strollsafe;

public class geofencing {
    private double size;

    public void makeGeofencing(){
        this.size = 0;
    }
    public double getSize() {
        return size;
    }
    public void setSize(double size) {
        this.size = size;
    }
    public double getArea(){
        double a = Math.PI*size*size;
        return a;
    }
}
