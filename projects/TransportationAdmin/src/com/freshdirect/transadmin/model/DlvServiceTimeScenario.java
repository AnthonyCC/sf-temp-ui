package com.freshdirect.transadmin.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	
	private BigDecimal loadBalanceFactor;
	
	private BigDecimal lateDeliveryFactor;
	
	private String balanceBy;
	
	private String needsLoadBalance;
	
	private String isNew;
	
	private Set scenarioDays = new HashSet(0);
	
	private Set scenarioZones = new HashSet(0);
	
	public DlvServiceTimeScenario(String code, String description,
			String isDefault, String serviceTimeFactorFormula,
			String serviceTimeFormula, String isNew, Set scenarioDays,
			Set scenarioZones) {
		super();
		this.code = code;
		this.description = description;
		this.isDefault = isDefault;
		this.serviceTimeFactorFormula = serviceTimeFactorFormula;
		this.serviceTimeFormula = serviceTimeFormula;
		this.isNew = isNew;
		this.scenarioDays = scenarioDays;
		this.scenarioZones = scenarioZones;
	}

	public Set getScenarioZones() {
		return scenarioZones;
	}

	public void setScenarioZones(Set scenarioZones) {
		this.scenarioZones = scenarioZones;
	}

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

	public String getBalanceBy() {
		return balanceBy;
	}

	public void setBalanceBy(String balanceBy) {
		this.balanceBy = balanceBy;
	}

	public BigDecimal getLoadBalanceFactor() {
		return loadBalanceFactor;
	}

	public void setLoadBalanceFactor(BigDecimal loadBalanceFactor) {
		this.loadBalanceFactor = loadBalanceFactor;
	}

	public String getNeedsLoadBalance() {
		return needsLoadBalance;
	}

	public void setNeedsLoadBalance(String needsLoadBalance) {
		this.needsLoadBalance = needsLoadBalance;
	}

	public BigDecimal getLateDeliveryFactor() {
		return lateDeliveryFactor;
	}

	public void setLateDeliveryFactor(BigDecimal lateDeliveryFactor) {
		this.lateDeliveryFactor = lateDeliveryFactor;
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("DlvServiceTimeScenario: ");
		result.append("code="+code);
		result.append(" description= "+ description);
	
		result.append(" \nDays: ");
		
		Iterator iterator = scenarioDays.iterator();
		while(iterator.hasNext()) {
			DlvScenarioDay var= (DlvScenarioDay)iterator.next();
			result.append(" \nscenarioCode="+ var.getScenario().getCode());
			result.append(" dayOfWeek="+ var.getDayOfWeek());
			result.append(" date="+ var.getNormalDate());
		}
		
		
		result.append(" \nZones: ");	
		
		Iterator itr = scenarioZones.iterator();
		while(itr.hasNext()) {
			DlvScenarioZones var= (DlvScenarioZones)itr.next();
			result.append(" \nZoneCode="+ var.getScenarioZonesId().getZoneCode());
			result.append(" serviceTimeType="+ var.getServiceTimeType());
			result.append(" serviceTimeOverride="+ var.getServiceTimeOverride());
			result.append(" serviceTimeOperator="+ var.getServiceTimeOperator());
			result.append(" serviceTimeAdjustment="+ var.getServiceTimeAdjustment());
		}
		return result.toString();
		
	}

	public Set getScenarioDays() {
		return scenarioDays;
	}

	public void setScenarioDays(Set scenarioDays) {
		this.scenarioDays = scenarioDays;
	}
}
