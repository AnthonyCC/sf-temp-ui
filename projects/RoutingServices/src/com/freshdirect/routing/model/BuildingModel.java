package com.freshdirect.routing.model;

public class BuildingModel extends BaseModel implements IBuildingModel  {
	
	private String srubbedStreet;
	private String zipCode;
	private String city;
	private String state;
	private String country;
	private String serviceTimeType;	
	private String buildingId;
	private IGeographicLocation geographicLocation;
	
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public IGeographicLocation getGeographicLocation() {
		return geographicLocation;
	}
	public void setGeographicLocation(IGeographicLocation geographicLocation) {
		this.geographicLocation = geographicLocation;
	}
	public String getServiceTimeType() {
		return serviceTimeType;
	}
	public void setServiceTimeType(String serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}
	public String getSrubbedStreet() {
		return srubbedStreet;
	}
	public void setSrubbedStreet(String srubbedStreet) {
		this.srubbedStreet = srubbedStreet;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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
		
	
}
