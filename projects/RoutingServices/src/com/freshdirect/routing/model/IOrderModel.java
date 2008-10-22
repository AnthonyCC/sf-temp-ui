package com.freshdirect.routing.model;

public interface IOrderModel {
	
	String getOrderNumber();
	void setOrderNumber(String orderNumber);
	
	String getCustomerName();
	void setCustomerName(String customerName);
	String getCustomerNumber();
	void setCustomerNumber(String customerNumber);
	
	IDeliveryModel getDeliveryInfo();
	void setDeliveryInfo(IDeliveryModel deliveryInfo);

}
