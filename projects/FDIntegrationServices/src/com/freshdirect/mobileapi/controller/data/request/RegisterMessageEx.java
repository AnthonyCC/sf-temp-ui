package com.freshdirect.mobileapi.controller.data.request;

public class RegisterMessageEx extends RegisterMessage {

	private String companyName;
	private String mobile_number;
	private String currentPartnerMessages;
	private boolean recieveSMSAlerts;
	
	public boolean isRecieveSMSAlerts() {
		return recieveSMSAlerts;
	}
	public void setRecieveSMSAlerts(boolean recieveSMSAlerts) {
		this.recieveSMSAlerts = recieveSMSAlerts;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCurrentPartnerMessages() {
		return currentPartnerMessages;
	}
	public void setCurrentPartnerMessages(String currentPartnerMessages) {
		this.currentPartnerMessages = currentPartnerMessages;
	}
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	
}
