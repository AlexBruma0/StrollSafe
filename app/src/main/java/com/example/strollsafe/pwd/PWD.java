package com.example.strollsafe.pwd;

import com.example.strollsafe.pwd.Address;
import com.example.strollsafe.pwd.EmergencyContact;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PWD extends RealmObject {
    @PrimaryKey
    private ObjectId _id = new ObjectId();
    private String PWDCode;
    @Required
    private String  lastName;
    private String firstName;
    private String phoneNumber;
    //private Address homeAddress;


    //private EmergencyContact[] emergencyContacts;
    public PWD(){

    }
    public PWD(String fName, String lName, String PN, String code){
        this.firstName = fName;
        this.lastName = lName;
        this.phoneNumber = PN;
        this.PWDCode = code;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPWDCode() {
        return PWDCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPWDCode(String PWDCode) {
        this.PWDCode = PWDCode;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
