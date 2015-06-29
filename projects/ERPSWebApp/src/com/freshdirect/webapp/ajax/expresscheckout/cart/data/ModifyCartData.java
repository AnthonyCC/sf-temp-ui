package com.freshdirect.webapp.ajax.expresscheckout.cart.data;

public class ModifyCartData {

	private boolean modifyOrderEnabled;
	private String orderId;
	private String cutoffTime;
	private String oneWeekLater;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCutoffTime() {
		return cutoffTime;
	}

	public void setCutoffTime(String cutoffTime) {
		this.cutoffTime = cutoffTime;
	}

	public String getOneWeekLater() {
		return oneWeekLater;
	}

	public void setOneWeekLater(String oneWeekLater) {
		this.oneWeekLater = oneWeekLater;
	}

	public boolean isModifyOrderEnabled() {
		return modifyOrderEnabled;
	}

	public void setModifyOrderEnabled(boolean modifyOrderEnabled) {
		this.modifyOrderEnabled = modifyOrderEnabled;
	}
}
