package com.freshdirect.routing.model;

import java.util.Set;

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
	
	// Start Additional
	String getAddrType();
	String getCompanyName();
	String getSvcScrubbedStreet();
	String getSvcCrossStreet();
	String getSvcCity();
	String getSvcState();
	String getSvcZip();
	boolean isDoorman();
	boolean isWalkup();
	
	boolean isElevator();
	boolean isSvcEnt();
	boolean isHouse();
	boolean isFreightElevator();
	boolean isHandTruckAllowed();
	int getWalkUpFloors();
	String getOther();
	String getDifficultReason();
	boolean isDifficultToDeliver();
	String getCrossStreet();
	
	void setAddrType(String addrType);
	void setCompanyName(String companyName);
	void setSvcScrubbedStreet(String svcScrubbedStreet);
	void setSvcCrossStreet(String svcCrossStreet);
	void setSvcCity(String svcCity);
	void setSvcState(String svcState);
	void setSvcZip(String svcZip);
	void setDoorman(boolean doorman);
	void setWalkup(boolean walkup);
	void setElevator(boolean elevator);
	void setSvcEnt(boolean svcEnt);
	void setHouse(boolean house);
	void setFreightElevator(boolean freightElevator);
	void setHandTruckAllowed(boolean handTruckAllowed);
	void setWalkUpFloors(int walkUpFloors);
	void setOther(String other);
	void setDifficultReason(String difficultReason);
	void setDifficultToDeliver(boolean difficultToDeliver);
	void setCrossStreet(String crossStreet);
	
	Set<IBuildingOperationDetails> getOperationDetails();
	void setOperationDetails(Set<IBuildingOperationDetails> operationDetails);
}
