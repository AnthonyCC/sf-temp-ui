package com.freshdirect.transadmin.model;

import java.math.BigDecimal;

public class DlvServiceTimeScenario implements java.io.Serializable, TrnBaseEntityI {

	private String code;
	private String description;
	private String isDefault;
	private String serviceTimeFactorFormula;
	private String serviceTimeFormula;
	private BigDecimal defaultCartonCount;
	private BigDecimal defaultCaseCount;
	private BigDecimal defaultFreezerCount;
	private String orderSizeFormula;
	
	private DlvServiceTimeType defaultServiceTimeType;
	private TrnZoneType defaultZoneType;
	
	private String isNew;

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public DlvServiceTimeScenario() {
	}

	public DlvServiceTimeScenario(String code) {
		this.code = code;		
	}
		
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getDefaultCartonCount() {
		return defaultCartonCount;
	}

	public void setDefaultCartonCount(BigDecimal defaultCartonCount) {
		this.defaultCartonCount = defaultCartonCount;
	}

	public BigDecimal getDefaultCaseCount() {
		return defaultCaseCount;
	}

	public void setDefaultCaseCount(BigDecimal defaultCaseCount) {
		this.defaultCaseCount = defaultCaseCount;
	}

	public BigDecimal getDefaultFreezerCount() {
		return defaultFreezerCount;
	}

	public void setDefaultFreezerCount(BigDecimal defaultFreezerCount) {
		this.defaultFreezerCount = defaultFreezerCount;
	}

	public DlvServiceTimeType getDefaultServiceTimeType() {
		return defaultServiceTimeType;
	}

	public void setDefaultServiceTimeType(DlvServiceTimeType defaultServiceTimeType) {
		this.defaultServiceTimeType = defaultServiceTimeType;
	}
	
	public String getServiceTimeType() {
		if(getDefaultServiceTimeType() == null) {
			return null;
		}
		return getDefaultServiceTimeType().getCode();
	}

	public void setServiceTimeType(String trnServiceTimeTypeId) {
		if("null".equals(trnServiceTimeTypeId)) {
			setDefaultServiceTimeType(null);
		} else {
			DlvServiceTimeType trnServiceTimeType = new DlvServiceTimeType();
			trnServiceTimeType.setCode(trnServiceTimeTypeId);
			setDefaultServiceTimeType(trnServiceTimeType);
		}
	}

	public TrnZoneType getDefaultZoneType() {
		return defaultZoneType;
	}

	public void setDefaultZoneType(TrnZoneType defaultZoneType) {
		this.defaultZoneType = defaultZoneType;
	}
	
	public String getZoneType() {
		if(getDefaultZoneType() == null) {
			return null;
		}
		return getDefaultZoneType().getZoneTypeId();
	}

	public void setZoneType(String trnZoneTypeId) {
		if("null".equals(trnZoneTypeId)) {
			setDefaultZoneType(null);
		} else {
			TrnZoneType trnZoneType = new TrnZoneType();
			trnZoneType.setZoneTypeId(trnZoneTypeId);
			setDefaultZoneType(trnZoneType);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getOrderSizeFormula() {
		return orderSizeFormula;
	}

	public void setOrderSizeFormula(String orderSizeFormula) {
		this.orderSizeFormula = orderSizeFormula;
	}

	public String getServiceTimeFactorFormula() {
		return serviceTimeFactorFormula;
	}

	public void setServiceTimeFactorFormula(String serviceTimeFactorFormula) {
		this.serviceTimeFactorFormula = serviceTimeFactorFormula;
	}

	public String getServiceTimeFormula() {
		return serviceTimeFormula;
	}

	public void setServiceTimeFormula(String serviceTimeFormula) {
		this.serviceTimeFormula = serviceTimeFormula;
	}

	public boolean isObsoleteEntity() {
		return false;
	}

}
