package com.freshdirect.routing.model;

import java.util.Map;

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
		
	private double loadBalanceFactor;
	
	private double lateDeliveryFactor;
	
	private String balanceBy;
	
	private boolean needsLoadBalance;
	
	private Map<String, IZoneScenarioModel> zoneConfiguration;
	private int defaultTrailerContainerCount;
	private int defaultContainerCartonCount;
	private double bulkThreshold;
	
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
	public Map<String, IZoneScenarioModel> getZoneConfiguration() {
		return zoneConfiguration;
	}
	public void setZoneConfiguration(
			Map<String, IZoneScenarioModel> zoneConfiguration) {
		this.zoneConfiguration = zoneConfiguration;
	}
	public int getDefaultTrailerContainerCount() {
		return defaultTrailerContainerCount;
	}
	public void setDefaultTrailerContainerCount(int defaultTrailerContainerCount) {
		this.defaultTrailerContainerCount = defaultTrailerContainerCount;
	}
	public int getDefaultContainerCartonCount() {
		return defaultContainerCartonCount;
	}
	public void setDefaultContainerCartonCount(int defaultContainerCartonCount) {
		this.defaultContainerCartonCount = defaultContainerCartonCount;
	}
	@Override
	public double getBulkThreshold() {
		return bulkThreshold;
	}
	@Override
	public void setBulkThreshold(double bulkThreshold) {
		this.bulkThreshold = bulkThreshold;
	}
	
	
}
