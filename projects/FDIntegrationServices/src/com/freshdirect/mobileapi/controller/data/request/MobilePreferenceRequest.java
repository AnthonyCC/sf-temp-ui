package com.freshdirect.mobileapi.controller.data.request;

public class MobilePreferenceRequest extends RequestMessage {

	private String mobile_number;
	private String order_notices;
	private String order_exceptions;
	private String offers;
	
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	public String getOrder_notices() {
		return order_notices;
	}
	public void setOrder_notices(String order_notices) {
		this.order_notices = order_notices;
	}
	public String getOrder_exceptions() {
		return order_exceptions;
	}
	public void setOrder_exceptions(String order_exceptions) {
		this.order_exceptions = order_exceptions;
	}
	public String getOffers() {
		return offers;
	}
	public void setOffers(String offers) {
		this.offers = offers;
	}
	
	
	
	
}
