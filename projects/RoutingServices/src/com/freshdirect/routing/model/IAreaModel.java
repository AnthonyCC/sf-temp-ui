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
	void setDepot(boolean isDepot);
	
	boolean isActive();
	void setActive(boolean isActive);	
	
	double getDeliveryRate();
	void setDeliveryRate(double deliveryRate);
	
	int getStemFromTime();
	void setStemFromTime(int stemFromTime);

	int getStemToTime();
	void setStemToTime(int stemToTime);
	
	String getPrefix();
	void setPrefix(String prefix);
	
	String getDeliveryModel();
	void setDeliveryModel(String deliveryModel);
	
	/*int getMaxStemTime();
	void setMaxStemTime(int maxStemTime);*/
}
