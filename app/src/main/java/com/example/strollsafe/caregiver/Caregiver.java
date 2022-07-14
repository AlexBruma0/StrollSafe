package com.example.strollsafe.caregiver;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Caregiver extends RealmObject {
    @PrimaryKey
    private ObjectId _id = new ObjectId();
    private String email;

    @Required
    private String lastName;
    private String firstName;
    private String phoneNumber;
    //private String[] PWDCodes;

    public Caregiver() {

    }

    public Caregiver(String email, String lastName, String firstName, String phoneNumber) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

//Updated upstream
//    public String[] getPWDCodes() {
//        return PWDCodes;
//    }
//
//    public void setPWDCodes(String[] PWDCodes) {
//        this.PWDCodes = PWDCodes;
//    }

    /*public String[] getPWDCodes() {
        return PWDCodes;
    }

    public void setPWDCodes(String[] PWDCodes) {
        this.PWDCodes = PWDCodes;
    }

    */
//>>>>>>> Stashed changes
}
