package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.EnumArithmeticOperator;


 public interface IZoneScenarioModel {
	
	String getZone();
	void setZone(String zone);
	double getServiceTimeOverride();
	void setServiceTimeOverride(double serviceTimeOverride);
	double getServiceTimeAdjustment();
	void setServiceTimeAdjustment(double serviceTimeAdjustment);
	EnumArithmeticOperator getAdjustmentOperator();
	void setAdjustmentOperator(EnumArithmeticOperator adjustmentOperator);
	
	IServiceTimeTypeModel getServiceTimeType();
	void setServiceTimeType(IServiceTimeTypeModel serviceTimeType);
}
