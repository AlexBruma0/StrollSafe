/*
* PWD.java
*
* Description: Class representing the PWD
*
* Last modified on July 14, 2022
* Last modified by Alvin Tsang
* */


package com.example.strollsafe.pwd;

import android.os.Build;

import com.example.strollsafe.pwd.Address;
import com.example.strollsafe.pwd.EmergencyContact;

public class PWD{

    private String lastName;
    private String firstName;
    private String dateOfBirth;
    private Address homeAddress;
    private String PWDCode;
    private EmergencyContactList emergencyContactList;


    public PWD(String lastName, String firstName, String dateOfBirth,
               Address homeAddress, String PWDCode, EmergencyContactList emergencyContactList) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
        this.homeAddress = homeAddress;
        this.PWDCode = PWDCode;
        this.emergencyContactList = emergencyContactList;
    } // end of parameterized constructor

} // end of PWD.java
