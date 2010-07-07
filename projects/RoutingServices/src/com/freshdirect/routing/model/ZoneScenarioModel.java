package com.freshdirect.routing.model;

import com.freshdirect.routing.constants.EnumArithmeticOperator;

public class ZoneScenarioModel extends BaseModel implements IZoneScenarioModel  {
	
	
	private String zone;
		
	private double serviceTimeOverride;
	private double serviceTimeAdjustment;
	private EnumArithmeticOperator adjustmentOperator;
	
	private IServiceTimeTypeModel serviceTimeType;
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public double getServiceTimeOverride() {
		return serviceTimeOverride;
	}
	public void setServiceTimeOverride(double serviceTimeOverride) {
		this.serviceTimeOverride = serviceTimeOverride;
	}
	public double getServiceTimeAdjustment() {
		return serviceTimeAdjustment;
	}
	public void setServiceTimeAdjustment(double serviceTimeAdjustment) {
		this.serviceTimeAdjustment = serviceTimeAdjustment;
	}
	public EnumArithmeticOperator getAdjustmentOperator() {
		return adjustmentOperator;
	}
	public void setAdjustmentOperator(EnumArithmeticOperator adjustmentOperator) {
		this.adjustmentOperator = adjustmentOperator;
	}
	public IServiceTimeTypeModel getServiceTimeType() {
		return serviceTimeType;
	}
	public void setServiceTimeType(IServiceTimeTypeModel serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}
		
	
}
