package com.freshdirect.routing.model;

import java.util.List;
import java.util.Map;

public interface IServiceTimeScenarioModel {
	
	String getCode();
	void setCode(String code);
	double getDefaultCartonCount();
	void setDefaultCartonCount(double defaultCartonCount);
	double getDefaultCaseCount();
	void setDefaultCaseCount(double defaultCaseCount);
	double getDefaultFreezerCount();
	void setDefaultFreezerCount(double defaultFreezerCount);
	
	String getDescription();
	void setDescription(String description);
	String getIsDefault();
	void setIsDefault(String isDefault);
	String getOrderSizeFormula();
	void setOrderSizeFormula(String orderSizeFormula);
	String getServiceTimeFactorFormula();
	void setServiceTimeFactorFormula(String serviceTimeFactorFormula);
	String getServiceTimeFormula();
	void setServiceTimeFormula(String serviceTimeFormula);
	
	String getBalanceBy();
	void setBalanceBy(String balanceBy);
	double getLoadBalanceFactor();
	void setLoadBalanceFactor(double loadBalanceFactor);
	boolean getNeedsLoadBalance();
	void setNeedsLoadBalance(boolean needsLoadBalance);
	
	double getLateDeliveryFactor();
	void setLateDeliveryFactor(double lateDeliveryTimeWindowFactor);
	 
	Map<String, IZoneScenarioModel> getZoneConfiguration();
	void setZoneConfiguration(Map<String, IZoneScenarioModel> zoneConfiguration);
}
