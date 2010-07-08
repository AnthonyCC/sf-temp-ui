package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.EnumArithmeticOperator;

public class LocationModel extends BaseModel implements ILocationModel {
	
	private String locationId;
	
	private String apartmentNumber;	
		
	private IServiceTimeTypeModel serviceTimeType;
	private IBuildingModel building;
	
	private double serviceTimeOverride;
	private double serviceTimeAdjustment;
	private EnumArithmeticOperator adjustmentOperator;
			
	
	public LocationModel(IBuildingModel building) {
		super();
		this.building = building;
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
	public IBuildingModel getBuilding() {
		return building;
	}
	
	public void setBuilding(IBuildingModel building) {
		this.building = building;
	}
	public IServiceTimeTypeModel getServiceTimeType() {
		return serviceTimeType;
	}
	public void setServiceTimeType(IServiceTimeTypeModel serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}
	public String getApartmentNumber() {
		return apartmentNumber;
	}
	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}
	
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
		
}
