package com.freshdirect.transadmin.web.model;

import com.freshdirect.transadmin.model.DlvScenarioZones;

public class ScenarioZoneCommand implements java.io.Serializable{
	
	private String scenarioZonesId;
	private String serviceTimeType;
	private String serviceTimeOverride;
	private String serviceTimeOperator;
	private String serviceTimeAdjustment;
	
	public ScenarioZoneCommand(DlvScenarioZones src) {
		
		this.scenarioZonesId  = src.getScenarioZonesId().getZoneCode()!=null ? src.getScenarioZonesId().getZoneCode(): "";
		this.serviceTimeType = src.getServiceTimeType()!=null
								    ? src.getServiceTimeType():"";
		this.serviceTimeOverride = src.getServiceTimeOverride() != null 
									? src.getServiceTimeOverride().toString() : "";
		this.serviceTimeOperator =  src.getServiceTimeOperator() != null 
										? src.getServiceTimeOperator().toString() : "";
		this.serviceTimeAdjustment = src.getServiceTimeAdjustment() != null 
										? src.getServiceTimeAdjustment().toString() : "";
		
	}
	
	public String getScenarioZonesId() {
		return scenarioZonesId;
	}
	public void setScenarioZonesId(String scenarioZonesId) {
		this.scenarioZonesId = scenarioZonesId;
	}
	public String getServiceTimeType() {
		return serviceTimeType;
	}
	public void setServiceTimeType(String serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}
	public String getServiceTimeOverride() {
		return serviceTimeOverride;
	}
	public void setServiceTimeOverride(String serviceTimeOverride) {
		this.serviceTimeOverride = serviceTimeOverride;
	}
	public String getServiceTimeOperator() {
		return serviceTimeOperator;
	}
	public void setServiceTimeOperator(String serviceTimeOperator) {
		this.serviceTimeOperator = serviceTimeOperator;
	}
	public String getServiceTimeAdjustment() {
		return serviceTimeAdjustment;
	}
	public void setServiceTimeAdjustment(String serviceTimeAdjustment) {
		this.serviceTimeAdjustment = serviceTimeAdjustment;
	}
	
	
	
}
