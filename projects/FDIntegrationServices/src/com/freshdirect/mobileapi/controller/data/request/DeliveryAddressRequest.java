package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class DeliveryAddressRequest extends Message{
	private  String dlvfirstname;
	private  String dlvlastname;
	private  String dlvcompanyname;
	private  String dlvhomephone;
	private  String dlvhomephoneext;
	private  String address1;
	private  String address2;
	private  String apartment;
	private  String city;
	private  String state;
	private  String zipcode;
	private  String country;
	private  String deliveryInstructions;
	private  String alternatePhone;
	private  String alternatePhoneExt;
	private  boolean doorman;
	private  boolean neighbor;
	
	private  String alternateFirstName;
	private  String alternateLastName;
	private  String alternateApartment;
	private  String altContactPhone;
	private  String altContactPhoneExt;
	private  String unattendedDeliveryOpt;
	private  String unattendedDeliveryInstr;
	private  String unattendedDeliveryNoticeSeen;
	private  String shipToAddressId;
	private  String dlvServiceType; 
	
	public boolean isDoorman() {
		return doorman;
	}
	public void setDoorman(boolean doorman) {
		this.doorman = doorman;
	}
	public boolean isNeighbor() {
		return neighbor;
	}
	public void setNeighbor(boolean neighbor) {
		this.neighbor = neighbor;
	}
		
	public String getDlvServiceType() {
		return dlvServiceType;
	}
	public void setDlvServiceType(String dlvServiceType) {
		this.dlvServiceType = dlvServiceType;
	}
	public String getDlvcompanyname() {
		return dlvcompanyname;
	}
	public void setDlvcompanyname(String dlvcompanyname) {
		this.dlvcompanyname = dlvcompanyname;
	}
	public String getDlvfirstname() {
		return dlvfirstname;
	}
	public void setDlvfirstname(String dlvfirstname) {
		this.dlvfirstname = dlvfirstname;
	}
	public String getDlvlastname() {
		return dlvlastname;
	}
	public void setDlvlastname(String dlvlastname) {
		this.dlvlastname = dlvlastname;
	}
	public String getDlvhomephone() {
		return dlvhomephone;
	}
	public void setDlvhomephone(String dlvhomephone) {
		this.dlvhomephone = dlvhomephone;
	}
	public String getDlvhomephoneext() {
		return dlvhomephoneext;
	}
	public void setDlvhomephoneext(String dlvhomephoneext) {
		this.dlvhomephoneext = dlvhomephoneext;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDeliveryInstructions() {
		return deliveryInstructions;
	}
	public void setDeliveryInstructions(String deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}
	public String getAlternatePhone() {
		return alternatePhone;
	}
	public void setAlternatePhone(String alternatePhone) {
		this.alternatePhone = alternatePhone;
	}
	public String getAlternatePhoneExt() {
		return alternatePhoneExt;
	}
	public void setAlternatePhoneExt(String alternatePhoneExt) {
		this.alternatePhoneExt = alternatePhoneExt;
	}
/*	public String getAlternateDelivery() {
		return alternateDelivery;
	}
	public void setAlternateDelivery(String alternateDelivery) {
		this.alternateDelivery = alternateDelivery;
	}*/
	public String getAlternateFirstName() {
		return alternateFirstName;
	}
	public void setAlternateFirstName(String alternateFirstName) {
		this.alternateFirstName = alternateFirstName;
	}
	public String getAlternateLastName() {
		return alternateLastName;
	}
	public void setAlternateLastName(String alternateLastName) {
		this.alternateLastName = alternateLastName;
	}
	public String getAlternateApartment() {
		return alternateApartment;
	}
	public void setAlternateApartment(String alternateApartment) {
		this.alternateApartment = alternateApartment;
	}
	public String getAltContactPhone() {
		return altContactPhone;
	}
	public void setAltContactPhone(String altContactPhone) {
		this.altContactPhone = altContactPhone;
	}
	public String getAltContactPhoneExt() {
		return altContactPhoneExt;
	}
	public void setAltContactPhoneExt(String altContactPhoneExt) {
		this.altContactPhoneExt = altContactPhoneExt;
	}
	public String getUnattendedDeliveryOpt() {
		return unattendedDeliveryOpt;
	}
	public void setUnattendedDeliveryOpt(String unattendedDeliveryOpt) {
		this.unattendedDeliveryOpt = unattendedDeliveryOpt;
	}
	public String getUnattendedDeliveryInstr() {
		return unattendedDeliveryInstr;
	}
	public void setUnattendedDeliveryInstr(String unattendedDeliveryInstr) {
		this.unattendedDeliveryInstr = unattendedDeliveryInstr;
	}
	public String getUnattendedDeliveryNoticeSeen() {
		return unattendedDeliveryNoticeSeen;
	}
	public void setUnattendedDeliveryNoticeSeen(String unattendedDeliveryNoticeSeen) {
		this.unattendedDeliveryNoticeSeen = unattendedDeliveryNoticeSeen;
	}
	public String getShipToAddressId() {
		return shipToAddressId;
	}
	public void setShipToAddressId(String deleteShipToAddressId) {
		this.shipToAddressId = deleteShipToAddressId;
	}
}
