package com.freshdirect.deliverypass;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.customer.EnumSaleStatus;

public class DlvPassUsageLine implements Serializable {
	private String dlvPassIdUsed;
	private String orderIdUsedFor;
	private Date deliveryDate;
	private EnumSaleStatus orderStatusUsedFor;
	
	public EnumSaleStatus getOrderStatusUsedFor() {
		return orderStatusUsedFor;
	}
	public void setOrderStatusUsedFor(EnumSaleStatus status) {
		this.orderStatusUsedFor = status;
	}
	public DlvPassUsageLine(String dlvPassId, String orderId, Date dlvDate, EnumSaleStatus status) {
		super();
		this.dlvPassIdUsed = dlvPassId;
		this.orderIdUsedFor = orderId;
		this.deliveryDate = dlvDate;
		this.orderStatusUsedFor = status;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDlvPassIdUsed() {
		return dlvPassIdUsed;
	}

	public void setDlvPassIdUsed(String dlvPassIdUsed) {
		this.dlvPassIdUsed = dlvPassIdUsed;
	}

	public String getOrderIdUsedFor() {
		return orderIdUsedFor;
	}

	public void setOrderIdUsedFor(String orderIdUsedFor) {
		this.orderIdUsedFor = orderIdUsedFor;
	}

}
