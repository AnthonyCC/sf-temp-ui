package com.freshdirect.common.address;

import java.io.Serializable;

public class AddressInfo implements Serializable {

	private String zoneId;
	private double longitude;
	private double latitude;
	private String scrubbedStreet;
	private EnumAddressType addressType;
	private String county;
	
	public String getZoneId(){
		return this.zoneId;
	}
	
	public void setZoneId(String zoneId){
		this.zoneId = zoneId;
	}
	
	public double getLongitude(){
		return this.longitude; 
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public String getScrubbedStreet(){
		return this.scrubbedStreet;
	}
	
	public void setScrubbedStreet(String scrubbedStreet){
		this.scrubbedStreet = scrubbedStreet;
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
	
	public String toString() {
		return "AddressInfo[zoneId " + zoneId
			+ ", scrubbedStreet "
			+ scrubbedStreet
			+ ", longitude/latitude "
			+ longitude
			+ "/"
			+ latitude
			+ ", addressType "
			+ addressType
			+ ", county "
			+ county
			+ "]";
	}

}
