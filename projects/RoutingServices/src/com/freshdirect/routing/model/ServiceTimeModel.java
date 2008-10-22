package com.freshdirect.routing.model;

public class ServiceTimeModel extends BaseModel implements IServiceTimeModel {
	
	private double fixedServiceTime;
	private double variableServiceTime;
	private boolean isNew;
	
	public double getFixedServiceTime() {
		return fixedServiceTime;
	}
	public void setFixedServiceTime(double fixedServiceTime) {
		this.fixedServiceTime = fixedServiceTime;
	}
	public double getVariableServiceTime() {
		return variableServiceTime;
	}
	public void setVariableServiceTime(double variableServiceTime) {
		this.variableServiceTime = variableServiceTime;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public String toString() {
		return fixedServiceTime+" -> "+variableServiceTime+"\n";
	}

}
