package com.freshdirect.routing.model;

public class ServiceTimeTypeModel extends BaseModel implements IServiceTimeTypeModel {
	
	private String code;
	private String name;
	private String description;
	private double fixedServiceTime;
	private double variableServiceTime;
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
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
		
	public String toString() {
		return fixedServiceTime+" -> "+variableServiceTime+"\n";
	}

}
