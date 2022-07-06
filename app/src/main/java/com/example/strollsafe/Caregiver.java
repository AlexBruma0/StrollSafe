package com.example.strollsafe;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Caregiver extends RealmObject {
    @PrimaryKey
    private ObjectId _id = new ObjectId();
    private String name = "Caregiver";

    @Required
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String patients;

    public Caregiver() {

    }

    public Caregiver(String firstName, String lastName, String phoneNumber, String patients) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.patients = patients;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }
}