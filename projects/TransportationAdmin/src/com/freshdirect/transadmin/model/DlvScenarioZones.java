package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

import com.freshdirect.transadmin.util.EnumDayOfWeek;
import com.freshdirect.transadmin.util.EnumLogicalOperator;


public class DlvScenarioZones implements java.io.Serializable{
	

	private ScenarioZonesId scenarioZonesId;
	private String serviceTimeType;
	private BigDecimal serviceTimeOverride;
	private BigDecimal serviceTimeAdjustment;
	private EnumLogicalOperator serviceTimeOperator;
		
	public DlvScenarioZones() {
	
	}
	public DlvScenarioZones(ScenarioZonesId scenarioZonesId) {
		this.scenarioZonesId = scenarioZonesId;
	}

	public DlvScenarioZones(ScenarioZonesId scenarioZonesId,
			String serviceTimeType, BigDecimal serviceTimeOverride,
			EnumLogicalOperator serviceTimeOperator,BigDecimal serviceTimeAdjustment) {
		
		this.scenarioZonesId = scenarioZonesId;
		this.serviceTimeType = serviceTimeType;
		this.serviceTimeOverride = serviceTimeOverride;
		this.serviceTimeOperator = serviceTimeOperator;
		this.serviceTimeAdjustment = serviceTimeAdjustment;
	}

	public ScenarioZonesId getScenarioZonesId() {
		return scenarioZonesId;
	}
	public void setScenarioZonesId(ScenarioZonesId scenarioZonesId) {
		this.scenarioZonesId = scenarioZonesId;
	}
	
	public EnumLogicalOperator getServiceTimeOperator() {
		return serviceTimeOperator;
	}
	public void setServiceTimeOperator(EnumLogicalOperator serviceTimeOperator) {
		this.serviceTimeOperator = serviceTimeOperator;	
	}	
	
	public String getServiceTimeType() {
		return serviceTimeType;
	}
	public void setServiceTimeType(String serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}
	public BigDecimal getServiceTimeOverride() {
		return serviceTimeOverride;
	}
	public void setServiceTimeOverride(BigDecimal serviceTimeOverride) {
		this.serviceTimeOverride = serviceTimeOverride;
	}
	public BigDecimal getServiceTimeAdjustment() {
		return serviceTimeAdjustment;
	}
	public void setServiceTimeAdjustment(BigDecimal serviceTimeAdjustment) {
		this.serviceTimeAdjustment = serviceTimeAdjustment;
	}
		
	
}
