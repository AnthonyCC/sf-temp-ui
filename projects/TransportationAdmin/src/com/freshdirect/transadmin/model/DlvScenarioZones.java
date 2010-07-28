package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

import com.freshdirect.routing.constants.EnumArithmeticOperator;

public class DlvScenarioZones{
	

	private ScenarioZonesId scenarioZonesId;
	private String serviceTimeType;
	private BigDecimal serviceTimeOverride;
	private EnumArithmeticOperator serviceTimeOperator;
	private BigDecimal serviceTimeAdjustment;
	
	public DlvScenarioZones() {
	
	}
	public DlvScenarioZones(ScenarioZonesId scenarioZonesId) {
		this.scenarioZonesId = scenarioZonesId;
	}

	public DlvScenarioZones(ScenarioZonesId scenarioZonesId,
			String serviceTimeType, BigDecimal serviceTimeOverride,
			EnumArithmeticOperator serviceTimeOperator,BigDecimal serviceTimeAdjustment) {
		
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
	
	public EnumArithmeticOperator getServiceTimeOperator() {
		return serviceTimeOperator;
	}
	public void setServiceTimeOperator(EnumArithmeticOperator serviceTimeOperator) {
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
