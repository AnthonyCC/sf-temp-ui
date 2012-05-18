package com.freshdirect.transadmin.model;

import java.math.BigDecimal;


public class CapacitySnapshotModel implements java.io.Serializable{
	
	private String id;
	private String buildingId;
	private String servicetype;
	private String srubbedStreet;
	private String zip;
	private String country;
	private String city;
	private String state;
	

	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
		
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	
	
	public String getSrubbedStreet() {
		return srubbedStreet;
	}
	public void setSrubbedStreet(String srubbedStreet) {
		this.srubbedStreet = srubbedStreet;
	}
	
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

}
