package com.freshdirect.fdstore.customer;

import java.io.Serializable;

public class PendingOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6206865101358715497L;

	private String orderCount;
	
	private String deliveryDate;
	
	public String getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
}
