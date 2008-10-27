package com.freshdirect.transadmin.web.model;

import java.math.BigDecimal;

import com.freshdirect.transadmin.model.DlvServiceTime;

public class ServiceTimeCommand extends BaseCommand {
	
	private String serviceTimeType;

	private String zoneType;
	
	private String zoneTypeName;
	
	private BigDecimal fixedServiceTime;
	
	private BigDecimal variableServiceTime;
	
	public ServiceTimeCommand() {
		
	}
	
	public ServiceTimeCommand(DlvServiceTime refObj) {
		if(refObj.getServiceTimeId() != null) {
			serviceTimeType = refObj.getServiceTimeId().getServiceTimeType();
			zoneType = refObj.getServiceTimeId().getZoneType();
		}
		fixedServiceTime = refObj.getFixedServiceTime();
		variableServiceTime = refObj.getVariableServiceTime();
	}
	
	public String getCompositeId() {
		return serviceTimeType+"$p$g"+zoneType;
	}
	
	public BigDecimal getFixedServiceTime() {
		return fixedServiceTime;
	}

	public void setFixedServiceTime(BigDecimal fixedServiceTime) {
		this.fixedServiceTime = fixedServiceTime;
	}

	public String getServiceTimeType() {
		return serviceTimeType;
	}

	public void setServiceTimeType(String serviceTimeType) {
		this.serviceTimeType = serviceTimeType;
	}

	public BigDecimal getVariableServiceTime() {
		return variableServiceTime;
	}

	public void setVariableServiceTime(BigDecimal variableServiceTime) {
		this.variableServiceTime = variableServiceTime;
	}

	public String getZoneType() {
		return zoneType;
	}

	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}

	public String getZoneTypeName() {
		return zoneTypeName;
	}

	public void setZoneTypeName(String zoneTypeName) {
		this.zoneTypeName = zoneTypeName;
	}

}
