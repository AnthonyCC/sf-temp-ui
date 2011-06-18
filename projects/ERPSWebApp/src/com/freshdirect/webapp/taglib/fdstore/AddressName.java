/*
 * AddressName.java
 *
 * Created on September 27, 2001, 8:00 PM
 */

package com.freshdirect.webapp.taglib.fdstore;

/**
 *
 * @author  jmccarter
 * @version
 */
public interface AddressName {
    //
    //	Delivery address constants
    //
    public final static String DLV_LAST_NAME        = "dlvlastname";
    public final static String DLV_FIRST_NAME       = "dlvfirstname";
    public final static String DLV_HOME_PHONE       = "dlvhomephone";
    public final static String DLV_HOME_PHONE_EXT   = "dlvhomephoneext";    
    public final static String DLV_ADDRESS_1     	= "address1";
    public final static String DLV_ADDRESS_SUGGEST 	= "addressSuggest";
    public final static String DLV_NOT_IN_ZONE      = "undeliverableAddress";
    public final static String DLV_CANT_GEOCODE     = "cantGeocode";
    
    public final static String DLV_ADDRESS_2     	= "address2";
    public final static String DLV_APARTMENT    	= "apartment";
    public final static String DLV_CITY 			= "city";
    public final static String DLV_STATE			= "state";
    public final static String DLV_ZIPCODE			= "zipcode";
    public final static String DLV_COUNTRY			= "country";
    public final static String DLV_DELIVERY_INSTRUCTIONS = "deliveryInstructions";
    public final static String DLV_WORK_DEPARTMENT = "workDepartment"; 
    
	public final static String DLV_COMPANY_NAME     = "dlvcompanyname";
	public final static String DLV_SERVICE_TYPE     = "dlvservicetype";

    // Alternate delivery info stuff
    public final static int DLV_ALTERNATE_THRESHHOLD 	= 2;
    public final static String DLV_ALTERNATE_DELIVERY 	= "alternateDelivery";
    public final static String DLV_ALT_LASTNAME		= "alternateLastName";
    public final static String DLV_ALT_FIRSTNAME	= "alternateFirstName";
    public final static String DLV_ALT_APARTMENT    = "alternateApartment";
    public final static String DLV_ALT_PHONE      	= "alternatePhone";
    public final static String DLV_ALT_EXT      	= "alternatePhoneExt";
    
    public final static String DLV_ALT_CONTACT_PHONE = "altContactPhone";
	public final static String DLV_ALT_CONTACT_EXT = "altContactPhoneExt";

    // Alternate delivery settings constants  (DLV_ATERNATE_DELIVERY)
    public final static String NO_ALT_DELIVERY 		= "none";
    public final static String LEAVE_WITH_DOORMAN 	= "doorman";
    public final static String LEAVE_WITH_NEIGHBOR  = "neighbor";
    //
    //	Billing address constants
    //
    public final static String BIL_ADDRESS_1       	= "bil_address1";
    public final static String BIL_ADDRESS_2     	= "bil_address2";
    public final static String BIL_APARTMENT    	= "bil_apartment";
    public final static String BIL_CITY 			= "bil_city";
    public final static String BIL_STATE			= "bil_state";
    public final static String BIL_ZIPCODE			= "bil_zipcode";
    public final static String BIL_COUNTRY			= "bil_country";
    
}

