package com.freshdirect.fdstore.ecoupon.model.yt;

import java.util.List;


public class YTCouponCartResponse extends YTCouponResponse {
	
	private List<YTCouponEligibleInfo> applied_coupons;

	/**
	 * @return the applied_coupons
	 */
	public List<YTCouponEligibleInfo> getApplied_coupons() {
		return applied_coupons;
	}

	/**
	 * @param applied_coupons the applied_coupons to set
	 */
	public void setApplied_coupons(List<YTCouponEligibleInfo> applied_coupons) {
		this.applied_coupons = applied_coupons;
	}
	
	
	

}
