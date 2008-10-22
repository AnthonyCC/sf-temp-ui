package com.freshdirect.routing.model;

public interface ILocationModel {
	
	String getApartmentNumber();
	void setApartmentNumber(String apartmentNumber);
	String getCity();
	void setCity(String city);	
	String getState();
	void setState(String state);
	String getStreetAddress1();
	void setStreetAddress1(String streetAddress1);
	String getStreetAddress2();
	void setStreetAddress2(String streetAddress2);
	String getZipCode();
	void setZipCode(String zipCode);
	IGeographicLocation getGeographicLocation();
	void setGeographicLocation(IGeographicLocation geographicLocation);
	String getCountry();
	void setCountry(String country);
	String getLocationId();
	void setLocationId(String locationId);
	String getServiceTimeType();
	void setServiceTimeType(String serviceTimeType);	
	String getBuildingId();
	void setBuildingId(String buildingId);
	
}
