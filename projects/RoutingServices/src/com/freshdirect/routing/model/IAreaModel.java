package com.freshdirect.routing.model;

public interface IAreaModel {
	
	String getAreaCode();

	void setAreaCode(String areaCode);
	
	String getBalanceBy();
	void setBalanceBy(String balanceBy);
	double getLoadBalanceFactor();
	void setLoadBalanceFactor(double loadBalanceFactor);
	boolean getNeedsLoadBalance();
	void setNeedsLoadBalance(boolean needsLoadBalance);
	
}
