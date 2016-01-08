package com.freshdirect.common.address;

import java.io.Serializable;

public class AddressInfo implements Serializable {

	public AddressInfo() {
		super();
	}

	public AddressInfo(String zoneCode, double longitude,
			double latitude, String scrubbedStreet,
			EnumAddressType addressType, String county, String buildingId,
			String locationId) {
		super();
		this.zoneCode = zoneCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.scrubbedStreet = scrubbedStreet;
		this.addressType = addressType;
		this.county = county;
		this.buildingId = buildingId;
		this.locationId = locationId;
	}

	private String zoneId;
	private String zoneCode;
	private double longitude;
	private double latitude;
	private String scrubbedStreet;
	private EnumAddressType addressType;
	private String county;
	private boolean isGeocodeException;
	private String buildingId;
	private String locationId;
	private String ssScrubbedAddress;
	
	public boolean isGeocodeException() {
		return isGeocodeException;
	}

	public void setGeocodeException(boolean isGeocodeException) {
		this.isGeocodeException = isGeocodeException;
	}

	public String getZoneId(){
		return this.zoneId;
	}
	
	public void setZoneId(String zoneId){
		this.zoneId = zoneId;
	}
	
	public String getZoneCode(){
		return this.zoneCode;
	}
	
	public void setZoneCode(String zoneCode){
		this.zoneCode = zoneCode;
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

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the ssScrubbedAddress
	 */
	public String getSsScrubbedAddress() {
		return ssScrubbedAddress;
	}

	/**
	 * @param ssScrubbedAddress the ssScrubbedAddress to set
	 */
	public void setSsScrubbedAddress(String ssScrubbedAddress) {
		this.ssScrubbedAddress = ssScrubbedAddress;
	}

	
}
