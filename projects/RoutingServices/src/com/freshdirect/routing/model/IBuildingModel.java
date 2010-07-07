package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.EnumArithmeticOperator;

public interface IBuildingModel {
	
	String getSrubbedStreet();
	void setSrubbedStreet(String street);	
	
	String getStreetAddress1();
	void setStreetAddress1(String streetAddress1);
	String getStreetAddress2();
	void setStreetAddress2(String streetAddress2);
	
	String getZipCode();
	void setZipCode(String zipCode);
	String getCountry();
	void setCountry(String country);
	
	String getCity();
	void setCity(String city);	
	String getState();
	void setState(String state);
	
	
	String getBuildingId();
	void setBuildingId(String buildingId);
	
	IGeographicLocation getGeographicLocation();
	void setGeographicLocation(IGeographicLocation geographicLocation);
	

	IServiceTimeTypeModel getServiceTimeType();
	void setServiceTimeType(IServiceTimeTypeModel serviceTimeType);
	
	double getServiceTimeOverride();
	void setServiceTimeOverride(double serviceTimeOverride);
	double getServiceTimeAdjustment();
	void setServiceTimeAdjustment(double serviceTimeAdjustment);
	EnumArithmeticOperator getAdjustmentOperator();
	void setAdjustmentOperator(EnumArithmeticOperator adjustmentOperator);
}
