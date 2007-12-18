package com.freshdirect.webapp.taglib.fdstore;

import java.io.Serializable;
import java.util.HashMap;

public class EnumUserInfoName implements Serializable {

	private final static HashMap INFO_NAMES = new HashMap();

	public final static EnumUserInfoName DLV_FIRST_NAME = new EnumUserInfoName(0, "dlvfirstname", "First Name");
	public final static EnumUserInfoName DLV_LAST_NAME = new EnumUserInfoName(1, "dlvlastname", "Last Name");
	public final static EnumUserInfoName DLV_HOME_PHONE = new EnumUserInfoName(2, "dlvhomephone", "Home Phone");
	public final static EnumUserInfoName DLV_HOME_PHONE_EXT = new EnumUserInfoName(3, "dlvhomephoneext", "Home Phone Ext.");
	public final static EnumUserInfoName DLV_ADDRESS_1 = new EnumUserInfoName(4, "address1", "Street Address");
	public final static EnumUserInfoName DLV_ADDRESS_2 = new EnumUserInfoName(5, "address2", "Address Line 2");
	public final static EnumUserInfoName DLV_APARTMENT = new EnumUserInfoName(6, "apartment", "Apt.");
	public final static EnumUserInfoName DLV_CITY = new EnumUserInfoName(7, "city", "City");
	public final static EnumUserInfoName DLV_STATE = new EnumUserInfoName(8, "state", "State");
	public final static EnumUserInfoName DLV_ZIPCODE = new EnumUserInfoName(9, "zipcode", "Zip Code");
	public final static EnumUserInfoName DLV_COUNTRY = new EnumUserInfoName(10, "country", "Country");
	public final static EnumUserInfoName DLV_DELIVERY_INSTRUCTIONS = new EnumUserInfoName(11, "deliveryInstructions", "Delivery Instructions");
	public final static EnumUserInfoName DLV_ADDRESS_SUGGEST = new EnumUserInfoName(12, "addressSuggest", "Street Address");
	public final static EnumUserInfoName DLV_NOT_IN_ZONE = new EnumUserInfoName(13, "undeliverableAddress", "Undeliverable Address");
	public final static EnumUserInfoName DLV_CANT_GEOCODE = new EnumUserInfoName(14, "cantGeocode", "Can't Geocode");

	// Alternate delivery info stuff
	// > used to determine promo eligibility at some point.. public final static DLV_ALTERNATE_THRESHHOLD 	= 2;
	public final static EnumUserInfoName DLV_ALTERNATE_DELIVERY = new EnumUserInfoName(15, "alternateDelivery", "Alternate Delivery");
	public final static EnumUserInfoName DLV_ALT_FIRSTNAME = new EnumUserInfoName(16, "alternateFirstName", "First Name");
	public final static EnumUserInfoName DLV_ALT_LASTNAME = new EnumUserInfoName(17, "alternateLastName", "Last Name");
	public final static EnumUserInfoName DLV_ALT_APARTMENT = new EnumUserInfoName(18, "alternateApartment", "Apartment");
	public final static EnumUserInfoName DLV_ALT_PHONE = new EnumUserInfoName(19, "alternatePhone", "Alt Phone");
	public final static EnumUserInfoName DLV_ALT_EXT = new EnumUserInfoName(20, "alternatePhoneExt", "Alt Ext.");

	// Alternate delivery settings constants  (DLV_ATERNATE_DELIVERY)
	public final static EnumUserInfoName NO_ALT_DELIVERY = new EnumUserInfoName(21, "none", "None");
	public final static EnumUserInfoName LEAVE_WITH_DOORMAN = new EnumUserInfoName(22, "doorman", "Doorman");
	public final static EnumUserInfoName LEAVE_WITH_NEIGHBOR = new EnumUserInfoName(23, "neighbor", "Neighbor");

	// SIGN IN INFORMATION	
	public final static EnumUserInfoName EMAIL = new EnumUserInfoName(24, "email", "E-mail Address");
	public final static EnumUserInfoName REPEAT_EMAIL = new EnumUserInfoName(25, "repeat_email", "Repeat E-mail Address");
	public final static EnumUserInfoName EMAIL_FORMAT = new EnumUserInfoName(26, "email_format", "E-mail Address");
	public final static EnumUserInfoName ALT_EMAIL = new EnumUserInfoName(27, "alt_email", "Alternate E-mail Address");

	public final static EnumUserInfoName PASSWORD = new EnumUserInfoName(28, "password", "Password");
	public final static EnumUserInfoName REPEAT_PASSWORD = new EnumUserInfoName(29, "repeat_password", "Repeat Password");
	public final static EnumUserInfoName PASSWORD_HINT =
		new EnumUserInfoName(30, "password_hint", "Town of Birth or Mother's Maiden Name");
	public final static EnumUserInfoName CUSTOMER_AGREEMENT = new EnumUserInfoName(31, "terms", "Customer Agreement");

	// CREDIT CARD INFO
	public final static EnumUserInfoName CARD_HOLDER = new EnumUserInfoName(32, "cardHolderName", "Name on Card");
	public final static EnumUserInfoName CARD_BRAND = new EnumUserInfoName(33, "cardBrand", "Card Type");
	public final static EnumUserInfoName CARD_NUMBER = new EnumUserInfoName(34, "cardNum", "Account Number");
	public final static EnumUserInfoName CARD_EXPIRATION = new EnumUserInfoName(35, "expiration", "Expires");
	
	//	BILLING ADDRESS
	public final static EnumUserInfoName BIL_ADDRESS_1 = new EnumUserInfoName(36, "bil_address1", "Street Address");
	public final static EnumUserInfoName BIL_ADDRESS_2 = new EnumUserInfoName(37, "bil_address2", "Street Address");
	public final static EnumUserInfoName BIL_APARTMENT = new EnumUserInfoName(38, "bil_apartment", "apt.");
	public final static EnumUserInfoName BIL_CITY = new EnumUserInfoName(39, "bil_city", "City");
	public final static EnumUserInfoName BIL_STATE = new EnumUserInfoName(40, "bil_state", "State");
	public final static EnumUserInfoName BIL_ZIPCODE = new EnumUserInfoName(41, "bil_zipcode", "Zip Code");
	public final static EnumUserInfoName BIL_COUNTRY = new EnumUserInfoName(42, "bil_country", "Country");

	// LOGIN
	public final static EnumUserInfoName USER_ID = new EnumUserInfoName(43, "userid", "E-mail Address");

	// CONTACT FD
	public final static EnumUserInfoName CONTACT_FD_SUBJECT = new EnumUserInfoName(44, "subject", "Subject");
	public final static EnumUserInfoName CONTACT_FD_MESSAGE = new EnumUserInfoName(45, "message", "Your Message");
	public final static EnumUserInfoName CONTACT_FD_EMAIL = new EnumUserInfoName(46, "email", "Email Address");
	public final static EnumUserInfoName CONTACT_FD_FIRST_NAME = new EnumUserInfoName(47, "first_name", "First Name");
	public final static EnumUserInfoName CONTACT_FD_LAST_NAME = new EnumUserInfoName(48, "last_name", "Last Name");
	public final static EnumUserInfoName CONTACT_FD_HOME_PHONE = new EnumUserInfoName(49, "home_phone", "Home Phone");

	// DEPOT
	public final static EnumUserInfoName DLV_WORK_DEPARTMENT = new EnumUserInfoName(50, "workDepartment", "Dept/Division");
	public final static EnumUserInfoName DLV_DEPOT_LOCATION_ID = new EnumUserInfoName(51, "locationId", "Primary Depot Address");
	public final static EnumUserInfoName DLV_DEPOT_CODE = new EnumUserInfoName(52, "depotCode", "Depot");
	public final static EnumUserInfoName DLV_DEPOT_REG_CODE = new EnumUserInfoName(53, "depotAccessCode", "Registration Code");
	public final static EnumUserInfoName DLV_WORK_PHONE = new EnumUserInfoName(54, "busphone", "Work Phone Number");
	public final static EnumUserInfoName DLV_EMPLOYEE_ID = new EnumUserInfoName(55, "employeeId", "Employee ID");

	public final static EnumUserInfoName TECHNICAL_DIFFICULTY = new EnumUserInfoName(56, "technical_difficulty", "Technical Difficulty");
	
	//These two declaration belong at top with dlv fields but to avoid the confusion in id# continuity, declaring them down here
	public final static EnumUserInfoName DLV_SERVICE_TYPE = new EnumUserInfoName(57, "dlvservicetype", "Service Type");
	public final static EnumUserInfoName DLV_COMPANY_NAME = new EnumUserInfoName(58, "dlvcompanyname", "Company name");

	// ECHECKS
	public final static EnumUserInfoName CARD_NUMBER_VERIFY = new EnumUserInfoName(59, "cardNumVerify", "Verify Account Number");
	
	public final static EnumUserInfoName DLV_ALT_CONTACT_PHONE = new EnumUserInfoName(60, "altContactPhone", "Alt Contact");
	public final static EnumUserInfoName DLV_ALT_CONTACT_EXT = new EnumUserInfoName(61, "altContactPhoneExt", "Alt Ext");
	
	// Unattended delivery
	public final static EnumUserInfoName DLV_UNATTENDED_DELIVERY_OPT = new EnumUserInfoName(62,"unattendedDeliveryOpt","Unattended Delivery Flag");
	public final static EnumUserInfoName DLV_UNATTENDED_DELIVERY_INSTRUCTIONS = new EnumUserInfoName(63,"unattendedDeliveryInstr","Unattended Delivery Instructions");
	public final static EnumUserInfoName DLV_UNATTENDED_CONSENT_SEEN = new EnumUserInfoName(64,"unattendedDeliveryNoticeSeen","Unattended Delivery Notice Seen");
	
	private int id;
	private String code;
	private String description;

	private EnumUserInfoName(int id, String code, String desc) {
		this.id = id;
		this.code = code;
		this.description = desc;
		INFO_NAMES.put(code, this);
	}

	public int getId() {
		return this.id;
	}
	public String getCode() {
		return this.code;
	}
	public String getDescription() {
		return this.description;
	}

	public boolean equals(Object o) {
		if (o instanceof EnumUserInfoName) {
			return this.id == ((EnumUserInfoName) o).id;
		}
		return false;
	}

	public static EnumUserInfoName getUserInfoName(String code) {
		if (code == null){
			return null;
		}
		return (EnumUserInfoName) (INFO_NAMES.get(code));
	}
}
