package com.freshdirect.routing.model;

import java.util.Set;

import com.freshdirect.routing.constants.EnumArithmeticOperator;

public class BuildingModel extends BaseModel implements IBuildingModel  {
	
	private String buildingId;
	
	private String srubbedStreet;
	private String streetAddress1;
	private String streetAddress2;
	private String zipCode;
	private String city;
	private String state;
	private String country;
		
	private IGeographicLocation geographicLocation;
	
	private IServiceTimeTypeModel serviceTimeType;
	private double serviceTimeOverride;
	private double serviceTimeAdjustment;
	private EnumArithmeticOperator adjustmentOperator;
	
	private String addrType;
	private String companyName;
	
	private String svcScrubbedStreet;
	private String svcCrossStreet;
	private String svcCity;
	private String svcState;
	private String svcZip;
	
	private boolean doorman;
	private boolean walkup;
	private boolean elevator;
	private boolean svcEnt;
	private boolean house;
	private boolean freightElevator;
	
	private boolean handTruckAllowed;
	
	private int walkUpFloors;
	
	private String other;
	
	private String difficultReason;
	private boolean difficultToDeliver;
	
	private String crossStreet;
	
	private Set<IBuildingOperationDetails> operationDetails;
	
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
	public IServiceTimeTypeModel getServiceTimeType() {
		return serviceTimeType;
	}
	public void setServiceTimeType(IServiceTimeTypeModel serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}
	public double getServiceTimeOverride() {
		return serviceTimeOverride;
	}
	public void setServiceTimeOverride(double serviceTimeOverride) {
		this.serviceTimeOverride = serviceTimeOverride;
	}
	public double getServiceTimeAdjustment() {
		return serviceTimeAdjustment;
	}
	public void setServiceTimeAdjustment(double serviceTimeAdjustment) {
		this.serviceTimeAdjustment = serviceTimeAdjustment;
	}
	public EnumArithmeticOperator getAdjustmentOperator() {
		return adjustmentOperator;
	}
	public void setAdjustmentOperator(EnumArithmeticOperator adjustmentOperator) {
		this.adjustmentOperator = adjustmentOperator;
	}
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
	//Start Additional
	public String getAddrType() {
		return addrType;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getSvcScrubbedStreet() {
		return svcScrubbedStreet;
	}
	public String getSvcCrossStreet() {
		return svcCrossStreet;
	}
	public String getSvcCity() {
		return svcCity;
	}
	public String getSvcState() {
		return svcState;
	}
	public String getSvcZip() {
		return svcZip;
	}
	public boolean isDoorman() {
		return doorman;
	}
	public boolean isWalkup() {
		return walkup;
	}
	public boolean isElevator() {
		return elevator;
	}
	public boolean isSvcEnt() {
		return svcEnt;
	}
	public boolean isHouse() {
		return house;
	}
	public boolean isFreightElevator() {
		return freightElevator;
	}
	public boolean isHandTruckAllowed() {
		return handTruckAllowed;
	}
	public int getWalkUpFloors() {
		return walkUpFloors;
	}
	public String getOther() {
		return other;
	}
	public String getDifficultReason() {
		return difficultReason;
	}
	public boolean isDifficultToDeliver() {
		return difficultToDeliver;
	}
	public String getCrossStreet() {
		return crossStreet;
	}
	public void setAddrType(String addrType) {
		this.addrType = addrType;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setSvcScrubbedStreet(String svcScrubbedStreet) {
		this.svcScrubbedStreet = svcScrubbedStreet;
	}
	public void setSvcCrossStreet(String svcCrossStreet) {
		this.svcCrossStreet = svcCrossStreet;
	}
	public void setSvcCity(String svcCity) {
		this.svcCity = svcCity;
	}
	public void setSvcState(String svcState) {
		this.svcState = svcState;
	}
	public void setSvcZip(String svcZip) {
		this.svcZip = svcZip;
	}
	public void setDoorman(boolean doorman) {
		this.doorman = doorman;
	}
	public void setWalkup(boolean walkup) {
		this.walkup = walkup;
	}
	public void setElevator(boolean elevator) {
		this.elevator = elevator;
	}
	public void setSvcEnt(boolean svcEnt) {
		this.svcEnt = svcEnt;
	}
	public void setHouse(boolean house) {
		this.house = house;
	}
	public void setFreightElevator(boolean freightElevator) {
		this.freightElevator = freightElevator;
	}
	public void setHandTruckAllowed(boolean handTruckAllowed) {
		this.handTruckAllowed = handTruckAllowed;
	}
	public void setWalkUpFloors(int walkUpFloors) {
		this.walkUpFloors = walkUpFloors;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public void setDifficultReason(String difficultReason) {
		this.difficultReason = difficultReason;
	}
	public void setDifficultToDeliver(boolean difficultToDeliver) {
		this.difficultToDeliver = difficultToDeliver;
	}
	public void setCrossStreet(String crossStreet) {
		this.crossStreet = crossStreet;
	}
	public Set<IBuildingOperationDetails> getOperationDetails() {
		return operationDetails;
	}
	public void setOperationDetails(Set<IBuildingOperationDetails> operationDetails) {
		this.operationDetails = operationDetails;
	}
		
	
}
