package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;

public class OrderModel extends BaseModel implements IOrderModel {
	
	private String orderNumber;	
	private String erpOrderNumber;	
	private String customerNumber;
	private String customerName;
	
	private Date createModifyTime;
	
	private Date unassignedTime;
	
	private IDeliveryModel deliveryInfo;
	
	private String unassignedAction;
	
	private String updateStatus;
	
	private EnumSaleStatus status;
	
	private double unassignedServiceTime;
	private double unassignedOrderSize;
	
	
	public String getErpOrderNumber() {
		return erpOrderNumber;
	}

	public void setErpOrderNumber(String erpOrderNumber) {
		this.erpOrderNumber = erpOrderNumber;
	}

	public double getUnassignedServiceTime() {
		return unassignedServiceTime;
	}

	public void setUnassignedServiceTime(double unassignedServiceTime) {
		this.unassignedServiceTime = unassignedServiceTime;
	}

	public double getUnassignedOrderSize() {
		return unassignedOrderSize;
	}

	public void setUnassignedOrderSize(double unassignedOrderSize) {
		this.unassignedOrderSize = unassignedOrderSize;
	}

	public String getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}

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

	public EnumSaleStatus getStatus() {
		return status;
	}

	public void setStatus(EnumSaleStatus status) {
		this.status = status;
	}
}
