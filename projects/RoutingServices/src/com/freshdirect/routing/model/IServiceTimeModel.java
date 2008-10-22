package com.freshdirect.routing.model;

public interface IServiceTimeModel {
	
	double getFixedServiceTime();
	void setFixedServiceTime(double fixedServiceTime);
	double getVariableServiceTime();
	void setVariableServiceTime(double variableServiceTime);
	boolean isNew();
	void setNew(boolean isNew);
}
