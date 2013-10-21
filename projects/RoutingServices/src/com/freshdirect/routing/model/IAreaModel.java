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
	
	boolean isDepot();
	
	boolean isActive();
	void setActive(boolean isActive);	
	
	double getDeliveryRate();
	void setDeliveryRate(double deliveryRate);
	
	public int getPreTripTime();
	public void setPreTripTime(int preTripTime);

	public int getPostTripTime();
	public void setPostTripTime(int postTripTime); 
	
	String getPrefix();
	void setPrefix(String prefix);
	
	String getDeliveryModel();
	void setDeliveryModel(String deliveryModel);
	
	IRegionModel getRegion();
	void setRegion(IRegionModel region);
}
