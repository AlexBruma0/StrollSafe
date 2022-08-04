package com.example.strollsafe.utils;

import org.bson.Document;

public class GeofencingNotification {
    private String safeZoneName;
    private long timestamp = System.currentTimeMillis();
    private int transition;

    public GeofencingNotification(String safeZoneName, long timestamp, int transition) {
        this.safeZoneName = safeZoneName;
        this.timestamp = timestamp;
        this.transition = transition;
    }

    public String getSafeZoneName() {
        return safeZoneName;
    }

    public void setSafeZoneName(String safeZoneName) {
        this.safeZoneName = safeZoneName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTransition() {
        return transition;
    }

    public void setTransition(int transition) {
        this.transition = transition;
    }

    public GeofencingNotification(Document document) {
        this.safeZoneName = (String) document.get("name");
        this.timestamp = (long) document.get("timestamp");
        this.transition = (int) document.get("transition");
    }

    public GeofencingNotification() {
    }
}
