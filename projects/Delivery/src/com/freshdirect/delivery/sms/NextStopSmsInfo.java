package com.freshdirect.delivery.sms;

public class NextStopSmsInfo {
	
	private String customerId;
	private String mobileNumber;
	private String dlvManager;
	private String orderId;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getDlvManager() {
		return dlvManager;
	}
	public void setDlvManager(String dlvManager) {
		this.dlvManager = dlvManager;
	}
	
	

}
