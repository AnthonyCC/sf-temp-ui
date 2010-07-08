package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.EnumArithmeticOperator;

public interface ILocationModel {
	
	String getApartmentNumber();
	void setApartmentNumber(String apartmentNumber);
	
	String getLocationId();
	void setLocationId(String locationId);
	IServiceTimeTypeModel getServiceTimeType();
	void setServiceTimeType(IServiceTimeTypeModel serviceTimeType);
	IBuildingModel getBuilding();
	void setBuilding(IBuildingModel building);
	
	double getServiceTimeOverride();
	void setServiceTimeOverride(double serviceTimeOverride);
	double getServiceTimeAdjustment();
	void setServiceTimeAdjustment(double serviceTimeAdjustment);
	EnumArithmeticOperator getAdjustmentOperator();
	void setAdjustmentOperator(EnumArithmeticOperator adjustmentOperator);
}
