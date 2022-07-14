/*
* EmergencyContactList.java
*
* Description: List of all PWD emergency contacts.
*              Manage all methods related to the list
* */

package com.example.strollsafe.pwd;


import java.util.ArrayList;

public class EmergencyContactList {
    private final ArrayList<EmergencyContact> EmergencyContactList;

    /**
     * Description: Default constructor
     * */
    public EmergencyContactList() {
        this.EmergencyContactList = new ArrayList<>();
    } // end of default constructor


    public ArrayList<EmergencyContact> getEmergencyContactList() {
        return EmergencyContactList;
    } // end of getEmergencyContactList()



} // end of EmergencyContactList.java
