package com.freshdirect.routing.util.extractor.model;

import java.io.Serializable;

public class ExtractAddressModel implements Serializable {
	
	private String address1;
	private String address2;
	private String apartment;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String scrubbedAddress;
	
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getScrubbedAddress() {
		return scrubbedAddress;
	}
	public void setScrubbedAddress(String scrubbedAddress) {
		this.scrubbedAddress = scrubbedAddress;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	
}
