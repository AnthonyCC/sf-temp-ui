package com.freshdirect.routing.model;

public class LocationModel extends BaseModel implements ILocationModel {
	
	private String locationId;
	private String streetAddress1;
	private String apartmentNumber;
	private String streetAddress2;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private IGeographicLocation geographicLocation;
	private String serviceTimeType;
	private String buildingId;
			
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getServiceTimeType() {
		return serviceTimeType;
	}
	public void setServiceTimeType(String serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}
	public String getApartmentNumber() {
		return apartmentNumber;
	}
	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
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
	public String getStreetAddress1() {
		return streetAddress1;
	}
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}
	public String getStreetAddress2() {
		return streetAddress2;
	}
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public IGeographicLocation getGeographicLocation() {
		// TODO Auto-generated method stub
		return this.geographicLocation;
	}
	public void setGeographicLocation(IGeographicLocation geographicLocation) {
		this.geographicLocation = geographicLocation;
		
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	public String toString() {
		return streetAddress1+","+apartmentNumber+","+zipCode+","
					+(geographicLocation != null ? geographicLocation.getLatitude() : "")+","+(geographicLocation != null ? geographicLocation.getLongitude() : "");
	}
}
