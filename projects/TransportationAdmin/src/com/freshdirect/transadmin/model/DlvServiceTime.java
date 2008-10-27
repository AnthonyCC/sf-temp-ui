package com.freshdirect.transadmin.model;

// Generated May 20, 2008 2:33:06 PM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;

/**
 * Servicetime generated by hbm2java
 */
public class DlvServiceTime implements java.io.Serializable, TrnBaseEntityI {

	private DlvServiceTimeId serviceTimeId;
	
	private BigDecimal fixedServiceTime;
	
	private BigDecimal variableServiceTime;
	
	private String isNew;

	public DlvServiceTime() {
	}

	public DlvServiceTime(DlvServiceTimeId id) {
		this.serviceTimeId = id;		
	}

	public DlvServiceTime(DlvServiceTimeId id, BigDecimal fixedServiceTime, BigDecimal variableServiceTime) {
		this.serviceTimeId = id;		
		this.fixedServiceTime = fixedServiceTime;
		this.variableServiceTime = variableServiceTime;
	}

	public DlvServiceTimeId getServiceTimeId() {
		return this.serviceTimeId;
	}

	public void setServiceTimeId(DlvServiceTimeId id) {
		this.serviceTimeId = id;
	}
	
	public BigDecimal getFixedServiceTime() {
		return fixedServiceTime;
	}

	public void setFixedServiceTime(BigDecimal fixedServiceTime) {
		this.fixedServiceTime = fixedServiceTime;
	}

	public BigDecimal getVariableServiceTime() {
		return variableServiceTime;
	}

	public void setVariableServiceTime(BigDecimal variableServiceTime) {
		this.variableServiceTime = variableServiceTime;
	}

	public String getServiceTimeType() {
		if(this.getServiceTimeId() == null) {
			return null;
		}
		return this.getServiceTimeId().getServiceTimeType();
	}

	public void setServiceTimeType(String serviceTimeType) {
		if(this.getServiceTimeId() == null) {
			this.setServiceTimeId(new DlvServiceTimeId());
		}
		this.getServiceTimeId().setServiceTimeType(serviceTimeType);
	}

	public String getZoneType() {
		if(this.getServiceTimeId() == null) {
			return null;
		}
		return this.getServiceTimeId().getZoneType();
	}

	public void setZoneType(String serviceType) {
		if(this.getServiceTimeId() == null) {
			this.setServiceTimeId(new DlvServiceTimeId());
		}
		this.getServiceTimeId().setZoneType(serviceType);
	}
	
	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	
	public boolean isObsoleteEntity() {
		return false;
	}

}
