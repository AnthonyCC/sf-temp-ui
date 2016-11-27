package com.freshdirect.fdstore.ecoupon.model;

import java.io.Serializable;

public class FDCouponCustomer implements Serializable {
	
	private String couponUserId; //FDUserId
	
	private String couponCustomerId; //FDCustomerId
	
	private String zipCode;

	public String getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(String couponUserId) {
		this.couponUserId = couponUserId;
	}

	public String getCouponCustomerId() {
		return couponCustomerId;
	}

	public void setCouponCustomerId(String couponCustomerId) {
		this.couponCustomerId = couponCustomerId;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
}
