package com.freshdirect.routing.model;

public class OrderModel extends BaseModel implements IOrderModel {
	
	private String orderNumber;	
	private String customerNumber;
	private String customerName;
	
	private IDeliveryModel deliveryInfo;

	public IDeliveryModel getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(IDeliveryModel deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	
	public String toString() {
		return orderNumber+"="+deliveryInfo.toString();
	}

}
