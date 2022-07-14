package com.example.strollsafe.pwd;

import com.example.strollsafe.utils.Address;
import com.example.strollsafe.utils.EmergencyContact;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PWD extends RealmObject {

    //@PrimaryKey private ObjectId _id = new ObjectId();
    @PrimaryKey private String _id;
    @Required
    private String PWDCode;
    private String  lastName;
    private String firstName;
    private String phoneNumber;
    private String email;
    //private Address homeAddress;


    //private EmergencyContact[] emergencyContacts;
    public PWD(){

    }
    public PWD(String fName, String lName, String PN, String code){
        this._id = code;
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

//    public ObjectId get_id() {
//        return _id;
//    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
