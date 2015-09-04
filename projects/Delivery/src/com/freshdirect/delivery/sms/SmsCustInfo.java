package com.freshdirect.delivery.sms;

public class SmsCustInfo {
	private String customerId;
	private String smsOrderNotice;
	private String smsOrderException;
	private String smsOffers;
	private String smsPartnerMessage;
	private String fdCustomerId;
	
	public String getFdCustomerId() {
		return fdCustomerId;
	}
	public void setFdCustomerId(String fdCustomerId) {
		this.fdCustomerId = fdCustomerId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getSmsOrderNotice() {
		return smsOrderNotice;
	}
	public void setSmsOrderNotice(String smsOrderNotice) {
		this.smsOrderNotice = smsOrderNotice;
	}
	public String getSmsOrderException() {
		return smsOrderException;
	}
	public void setSmsOrderException(String smsOrderException) {
		this.smsOrderException = smsOrderException;
	}
	public String getSmsOffers() {
		return smsOffers;
	}
	public void setSmsOffers(String smsOffers) {
		this.smsOffers = smsOffers;
	}
	public String getSmsPartnerMessage() {
		return smsPartnerMessage;
	}
	public void setSmsPartnerMessage(String smsPartnerMessage) {
		this.smsPartnerMessage = smsPartnerMessage;
	}
	
	

}
