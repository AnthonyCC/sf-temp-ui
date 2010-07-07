package com.freshdirect.routing.model;

public interface IServiceTimeTypeModel {
	
	String getCode();
	void setCode(String code);
	String getName();
	void setName(String name);
	String getDescription();
	void setDescription(String description);
	double getFixedServiceTime();
	void setFixedServiceTime(double fixedServiceTime);
	double getVariableServiceTime();
	void setVariableServiceTime(double variableServiceTime);
}
