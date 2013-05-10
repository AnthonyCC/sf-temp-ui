package com.freshdirect.fdstore.ecoupon.model.yt;

import java.io.Serializable;
import java.util.List;

public class YTCouponEligibleInfo implements Serializable {
	
	private String coupon_id;
	private List<String> discounted_upcs;
	private String discount;
	
	
	/**
	 * @return the coupon_id
	 */
	public String getCoupon_id() {
		return coupon_id;
	}
	/**
	 * @param coupon_id the coupon_id to set
	 */
	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}
	/**
	 * @return the discounted_upcs
	 */
	public List<String> getDiscounted_upcs() {
		return discounted_upcs;
	}
	/**
	 * @param discounted_upcs the discounted_upcs to set
	 */
	public void setDiscounted_upcs(List<String> discounted_upcs) {
		this.discounted_upcs = discounted_upcs;
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
