package com.freshdirect.mobileapi.controller.data;

public class MobilePreferencesResult extends Message {
	
	private String mobile_number;
	private boolean order_notices;
	private boolean order_exceptions;
	private boolean offers;
	
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	public boolean isOrder_notices() {
		return order_notices;
	}
	public void setOrder_notices(boolean order_notices) {
		this.order_notices = order_notices;
	}
	public boolean isOrder_exceptions() {
		return order_exceptions;
	}
	public void setOrder_exceptions(boolean order_exceptions) {
		this.order_exceptions = order_exceptions;
	}
	public boolean isOffers() {
		return offers;
	}
	public void setOffers(boolean offers) {
		this.offers = offers;
	}
	
	
	
}