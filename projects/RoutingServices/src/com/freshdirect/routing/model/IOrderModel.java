package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;

public interface IOrderModel {
	
	String getOrderNumber();
	void setOrderNumber(String orderNumber);
	
	String getErpOrderNumber();
	void setErpOrderNumber(String erpOrderNumber);
	
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
	
	String getUnassignedAction();
	void setUnassignedAction(String unassignedAction);	
	
	String getUpdateStatus();
	void setUpdateStatus(String updateStatus);
	
	double getUnassignedServiceTime();
	void setUnassignedServiceTime(double unassignedServiceTime);
	
	double getUnassignedOrderSize();
	void setUnassignedOrderSize(double unassignedOrderSize);
	
	EnumSaleStatus getStatus();
	void setStatus(EnumSaleStatus status);
}
