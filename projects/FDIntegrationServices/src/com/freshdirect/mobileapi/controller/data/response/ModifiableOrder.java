package com.freshdirect.mobileapi.controller.data.response;

public class ModifiableOrder {
	
	public ModifiableOrder(String orderId, boolean isModifiable) {
		super();
		this.orderId = orderId;
		this.isModifiable = isModifiable;
	}
	private String orderId;
	private boolean isModifiable;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public boolean isModifiable() {
		return isModifiable;
	}
	public void setModifiable(boolean isModifiable) {
		this.isModifiable = isModifiable;
	}
}
