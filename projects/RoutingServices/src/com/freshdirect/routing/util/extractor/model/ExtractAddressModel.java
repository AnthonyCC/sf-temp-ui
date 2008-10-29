package com.freshdirect.routing.util.extractor.model;

import java.io.Serializable;

public class ExtractAddressModel implements Serializable {
	
	private String id;
	private String refId;
	private String zone;
	private String area;
	private String address1;
	private String address2;
	private String apartment;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String scrubbedAddress;
	private String refAddressId;
	private String deliveryType;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefAddressId() {
		return refAddressId;
	}
	public void setRefAddressId(String refAddressId) {
		this.refAddressId = refAddressId;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	
	
}
