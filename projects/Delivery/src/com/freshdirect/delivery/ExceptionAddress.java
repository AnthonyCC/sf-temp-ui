/**
 * @author ekracoff
 * Created on Apr 13, 2005*/

package com.freshdirect.delivery;

import com.freshdirect.common.address.EnumAddressType;

public class ExceptionAddress {
	
	private String id;
	private String streetAddress;
	private String aptNumLow;
	private String aptNumHigh;
	private String zip;
	private EnumAddressType addressType;
	private EnumAddressExceptionReason reason;
	private String county;
	private String state;
	private String userId;
	private String city;
	private String scrubbedAddress;
	private double latitude;
	private double longitude;
	
	public String getId(){
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getAptNumHigh() {
		return aptNumHigh;
	}

	public void setAptNumHigh(String aptNumHigh) {
		this.aptNumHigh = aptNumHigh;
	}

	public String getAptNumLow() {
		return aptNumLow;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getScrubbedAddress() {
		return scrubbedAddress;
	}

	public void setScrubbedAddress(String scrubbedAddress) {
		this.scrubbedAddress = scrubbedAddress;
	}

	public void setAptNumLow(String aptNumLow) {
		this.aptNumLow = aptNumLow;
	}

	public EnumAddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(EnumAddressType addressType) {
		this.addressType = addressType;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public EnumAddressExceptionReason getReason() {
		return reason;
	}

	public void setReason(EnumAddressExceptionReason reason) {
		this.reason = reason;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
}