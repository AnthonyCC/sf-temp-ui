package com.freshdirect.routing.model;

import java.util.Date;

public class OrderModel extends BaseModel implements IOrderModel {
	
	private String orderNumber;	
	private String customerNumber;
	private String customerName;
	
	private Date createModifyTime;
	
	private Date unassignedTime;
	
	private IDeliveryModel deliveryInfo;
	
	private String unassignedAction;

	public String getUnassignedAction() {
		return unassignedAction;
	}

	public void setUnassignedAction(String unassignedAction) {
		this.unassignedAction = unassignedAction;
	}

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

	public Date getCreateModifyTime() {
		return createModifyTime;
	}

	public void setCreateModifyTime(Date createModifyTime) {
		this.createModifyTime = createModifyTime;
	}

	public Date getUnassignedTime() {
		return unassignedTime;
	}

	public void setUnassignedTime(Date unassignedTime) {
		this.unassignedTime = unassignedTime;
	}

	

}
