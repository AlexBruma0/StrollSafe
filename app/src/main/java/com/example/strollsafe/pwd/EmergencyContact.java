/*
* EmergencyContact.java
*
* Description: Class Describing an emergency contact person for the PWD
*
* Last modified on July 14, 2022
* Last modified by: Alvin Tsang
* */

package com.example.strollsafe.pwd;

public class EmergencyContact {

    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String email;
    private String relationToPatient;


    public EmergencyContact(String lastName, String firstName,
                            String phoneNumber, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public EmergencyContact(String lastName, String firstName, String phoneNumber,
                            String email, String relationToPatient) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.relationToPatient = relationToPatient;
    }


} // end of EmergencyContact.java
