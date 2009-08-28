package com.freshdirect.routing.model;

import java.util.Date;

public interface IOrderModel {
	
	String getOrderNumber();
	void setOrderNumber(String orderNumber);
	
	String getCustomerName();
	void setCustomerName(String customerName);
	String getCustomerNumber();
	void setCustomerNumber(String customerNumber);
	
	IDeliveryModel getDeliveryInfo();
	void setDeliveryInfo(IDeliveryModel deliveryInfo);
	
	Date getCreateModifyTime();
	void setCreateModifyTime(Date createModifyTime);
	
	Date getUnassignedTime();
	void setUnassignedTime(Date unassignedTime);
}
