package com.example.strollsafe;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PWD extends RealmObject {
    @PrimaryKey
    private ObjectId _id = new ObjectId();

    @Required
    private String  lastName;
    private String firstName;
    private String phoneNumber;
//    private Address homeAddress;
    private String PWDCode;
//    private EmergencyContact emergencyContacts;

    public PWD() {

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

//    public Address getHomeAddress() {
//        return homeAddress;
//    }
//
//    public void setHomeAddress(Address homeAddress) {
//        this.homeAddress = homeAddress;
//    }

    public String getPWDCode() {
        return PWDCode;
    }

    public void setPWDCode(String PWDCode) {
        this.PWDCode = PWDCode;
    }

//    public EmergencyContact getEmergencyContacts() {
//        return emergencyContacts;
//    }
//
//    public void setEmergencyContacts(EmergencyContact emergencyContacts) {
//        this.emergencyContacts = emergencyContacts;
//    }
}