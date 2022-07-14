/*
* Address.java
*
* Last modified on July 14, 2022
* Last modified by: Alvin Tsang
* */

package com.example.strollsafe.pwd;

public class Address{

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String region;
    private String postalCode;
    private String country;

    /**
     * Description: Parameterized constructor
     *
     * @param addressLine1 describe the house number and street
     * @param addressLine2 describe the unit number if applicable
     * @param city         the city of the address
     * @param region       province/state/territory
     * @param postalCode   postal code of address
     * @param country      country of address
     * */
    public Address(String addressLine1, String addressLine2, String city,
                   String region, String postalCode, String country) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;

    } // end of parameterized constructor

    /**
     * Description: Get address line 1
     *
     * @return return address line 1
     * */
    public String getAddressLine1() {
        return addressLine1;
    } // end of getAddressLine1()

    /**
     * Description: Get address line 2
     *
     * @return return address line 2
     * */
    public String getAddressLine2() {
        return addressLine2;
    } // end of getAddressLine2()


    /**
     * Description: Get city
     *
     * @return return city name
     * */
    public String getCity() {
        return city;
    } // end of getCity()

    /**
     * Description: Get region
     *
     * @return return province/state/territory of address
     * */
    public String getRegion() {
        return region;
    } // end of getRegion()

    /**
     * Description: Get postal code
     *
     * @return return postal code of address
     * */
    public String getPostalCode() {
        return postalCode;
    } // end of getPostalCode()

    /**
     * Description: Get country
     *
     * @return return country name
     * */
    public String getCountry() {
        return country;
    }

    /**
     * Description: Set address line 1
     *
     * @param addressLine1 house number and street
     * */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    } // end of setAddressLine1()

    /**
     * Description: Set address line 2
     *
     * @param addressLine2 apartment unit number or suite
     * */
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    } // end of setAddressLine2()

    /**
     * Description: Set city name
     *
     * @param city city of address
     * */
    public void setCity(String city) {
        this.city = city;
    } // end of setCity()

    /**
     * Description: Set province/state/territory
     *
     * @param region province/state/territory of address
     * */
    public void setRegion(String region) {
        this.region = region;
    } // end of setRegion()

    /**
     * Description: Set postal code
     *
     * @param postalCode postal code of address
     * */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    } // end of setPostalCode()

    /**
     * Description: Set country name
     *
     * @param country country of address
     * */
    public void setCountry(String country) {
        this.country = country;
    } // end of setCountry()
    
} // end of Address.java
