package com.freshdirect.routing.model;

public interface IServiceTimeScenarioModel {
	
	String getCode();
	void setCode(String code);
	double getDefaultCartonCount();
	void setDefaultCartonCount(double defaultCartonCount);
	double getDefaultCaseCount();
	void setDefaultCaseCount(double defaultCaseCount);
	double getDefaultFreezerCount();
	void setDefaultFreezerCount(double defaultFreezerCount);
	String getDefaultServiceTimeType();
	void setDefaultServiceTimeType(String defaultServiceTimeType);
	String getDefaultZoneType();
	void setDefaultZoneType(String defaultZoneType);
	String getDescription();
	void setDescription(String description);
	String getIsDefault();
	void setIsDefault(String isDefault);
	String getOrderSizeFormula();
	void setOrderSizeFormula(String orderSizeFormula);
	String getServiceTimeFactorFormula();
	void setServiceTimeFactorFormula(String serviceTimeFactorFormula);
	String getServiceTimeFormula();
	void setServiceTimeFormula(String serviceTimeFormula);
}
