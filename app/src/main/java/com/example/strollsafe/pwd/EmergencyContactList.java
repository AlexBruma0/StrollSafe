/*
* EmergencyContactList.java
*
* Description: List of all PWD emergency contacts.
*              Manage all methods related to the list
* */

package com.example.strollsafe.pwd;


import java.util.ArrayList;

public class EmergencyContactList {
    private final ArrayList<EmergencyContact> emergencyContactList;
    private static final int MAX_SIZE = 5;

    /**
     * Description: Default constructor
     * */
    public EmergencyContactList() {
        this.emergencyContactList = new ArrayList<>();
    } // end of default constructor

    /**
     * Description: Get the list of emergency contacts
     *
     * @return list of emergency contacts
     * */
    public ArrayList<EmergencyContact> getEmergencyContactList() {
        return emergencyContactList;
    } // end of getEmergencyContactList()

    /**
     * Description: Add emergency contact to the list
     *              List size can not exceed MAX_SIZE
     *
     * @param emergencyContact EmergencyContact object to be added
     * @return If list size is less than MAX_SIZE, insert the emergency contact and return true.
     *         Otherwise, return false
     * */
    public boolean addContact(EmergencyContact emergencyContact) {
        boolean added;
        if (emergencyContactList.size() < MAX_SIZE) {
            emergencyContactList.add(emergencyContact);
            added = true;
        } else {
            added = false;
        }
        return added;
    } // end of addContact()

    /**
     * Description: Remove an emergency contact from the list
     *
     * @param emergencyContact EmergencyContact object to be removed
     * @return If list contains the emergency contact, remove and return true.
     *         Otherwise, return false.
     * */
    public boolean removeContact(EmergencyContact emergencyContact) {
        int listSize = emergencyContactList.size();
        boolean removed = false;
        int index = 0;
        EmergencyContact currentContact;
        while (!removed && index < listSize) {
            currentContact = emergencyContactList.get(index);
            if (currentContact.getLastName().equals(emergencyContact.getLastName()) &&
                    currentContact.getFirstName().equals(emergencyContact.getFirstName()) &&
                    currentContact.getPhoneNumber().equals(emergencyContact.getPhoneNumber()) &&
                    currentContact.getEmail().equals(emergencyContact.getEmail())) {

                emergencyContactList.remove(index);
                removed = true;
            } else {
                index++;
            }
        }
        return removed;
    } // end of removeContact()
    


} // end of EmergencyContactList.java
