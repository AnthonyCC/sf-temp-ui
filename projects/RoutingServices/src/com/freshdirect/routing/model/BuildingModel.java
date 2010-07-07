package com.freshdirect.routing.model;

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
		
	
}
