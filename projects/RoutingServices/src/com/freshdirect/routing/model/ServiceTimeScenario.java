package com.freshdirect.routing.model;



public class ServiceTimeScenario extends BaseModel implements IServiceTimeScenarioModel {
	
	private String code;
	private String description;
	private String isDefault;
	private String serviceTimeFactorFormula;
	private String serviceTimeFormula;
	private double defaultCartonCount;
	private double defaultCaseCount;
	private double defaultFreezerCount;
	private String orderSizeFormula;
	
	private String defaultServiceTimeType;
	private String defaultZoneType;
	
	private double loadBalanceFactor;
	
	private double lateDeliveryFactor;
	
	private String balanceBy;
	
	private boolean needsLoadBalance;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getDefaultCartonCount() {
		return defaultCartonCount;
	}
	public void setDefaultCartonCount(double defaultCartonCount) {
		this.defaultCartonCount = defaultCartonCount;
	}
	public double getDefaultCaseCount() {
		return defaultCaseCount;
	}
	public void setDefaultCaseCount(double defaultCaseCount) {
		this.defaultCaseCount = defaultCaseCount;
	}
	public double getDefaultFreezerCount() {
		return defaultFreezerCount;
	}
	public void setDefaultFreezerCount(double defaultFreezerCount) {
		this.defaultFreezerCount = defaultFreezerCount;
	}
	public String getDefaultServiceTimeType() {
		return defaultServiceTimeType;
	}
	public void setDefaultServiceTimeType(String defaultServiceTimeType) {
		this.defaultServiceTimeType = defaultServiceTimeType;
	}
	public String getDefaultZoneType() {
		return defaultZoneType;
	}
	public void setDefaultZoneType(String defaultZoneType) {
		this.defaultZoneType = defaultZoneType;
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
	public String getBalanceBy() {
		return balanceBy;
	}
	public void setBalanceBy(String balanceBy) {
		this.balanceBy = balanceBy;
	}
	public double getLoadBalanceFactor() {
		return loadBalanceFactor;
	}
	public void setLoadBalanceFactor(double loadBalanceFactor) {
		this.loadBalanceFactor = loadBalanceFactor;
	}
	public boolean getNeedsLoadBalance() {
		return needsLoadBalance;
	}
	public void setNeedsLoadBalance(boolean needsLoadBalance) {
		this.needsLoadBalance = needsLoadBalance;
	}
	public double getLateDeliveryFactor() {
		return lateDeliveryFactor;
	}
	public void setLateDeliveryFactor(double lateDeliveryFactor) {
		this.lateDeliveryFactor = lateDeliveryFactor;
	}
	
}
