package com.freshdirect.routing.model;

public interface IBuildingModel {
	
	String getSrubbedStreet();
	void setSrubbedStreet(String street);	
	String getZipCode();
	void setZipCode(String zipCode);
	String getCountry();
	void setCountry(String country);
	
	String getCity();
	void setCity(String city);	
	String getState();
	void setState(String state);
	
	String getServiceTimeType();
	void setServiceTimeType(String serviceTimeType);	
	String getBuildingId();
	void setBuildingId(String buildingId);
	
	IGeographicLocation getGeographicLocation();
	void setGeographicLocation(IGeographicLocation geographicLocation);
}
