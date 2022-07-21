package com.example.strollsafe.pwd;

import com.example.strollsafe.utils.Address;
import com.example.strollsafe.utils.EmergencyContact;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PWD extends RealmObject {

    //@PrimaryKey private ObjectId _id = new ObjectId();
    @PrimaryKey private ObjectId _id = new ObjectId();
    @Required
    private String PWDCode;
    private String  lastName;
    private String firstName;
    private String phoneNumber;
    private String email;
    private String password;
    //private Address homeAddress;


    //private EmergencyContact[] emergencyContacts;
    public PWD(){

    }
    public PWD(String fName, String lName, String PN, String code, String mail, String password){
        this.firstName = fName;
        this.lastName = lName;
        this.phoneNumber = PN;
        this.PWDCode = code;
        this.email = mail;
        this.email = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

   public ObjectId get_id() {
       return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPWDCode(String PWDCode) {
        this.PWDCode = PWDCode;
    }
}
