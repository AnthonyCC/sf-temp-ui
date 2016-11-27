package com.freshdirect.fdstore.ecoupon.model;

import java.io.Serializable;
import java.util.List;

public class FDCouponEligibleInfo implements Serializable{

	private String couponId;
	private List<String> discountedUpcs;
	private String discount;
	/**
	 * @return the couponId
	 */
	public String getCouponId() {
		return couponId;
	}
	/**
	 * @param couponId the couponId to set
	 */
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	/**
	 * @return the discountedUpcs
	 */
	public List<String> getDiscountedUpcs() {
		return discountedUpcs;
	}
	/**
	 * @param discountedUpcs the discountedUpcs to set
	 */
	public void setDiscountedUpcs(List<String> discountedUpcs) {
		this.discountedUpcs = discountedUpcs;
	}
	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	
	
}
