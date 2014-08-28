package com.freshdirect.delivery.sms;

import java.util.Date;

public class SmsAlertETAInfo {
	private String customerId;
	private String mobileNumber;
	private Date etaStartTime;
	private Date etaEndTime;
	private boolean etaEnabled;
	private String orderId;
	
	
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public boolean isEtaEnabled() {
		return etaEnabled;
	}
	public void setEtaEnabled(boolean etaEnabled) {
		this.etaEnabled = etaEnabled;
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
	public Date getEtaStartTime() {
		return etaStartTime;
	}
	public void setEtaStartTime(Date etaStartTime) {
		this.etaStartTime = etaStartTime;
	}
	public Date getEtaEndTime() {
		return etaEndTime;
	}
	public void setEtaEndTime(Date etaEndTime) {
		this.etaEndTime = etaEndTime;
	}
	
	
	

}
