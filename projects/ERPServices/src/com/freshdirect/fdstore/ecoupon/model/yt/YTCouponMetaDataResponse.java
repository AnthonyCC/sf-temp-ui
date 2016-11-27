package com.freshdirect.fdstore.ecoupon.model.yt;


import java.util.List;


public class YTCouponMetaDataResponse extends YTCouponResponse {

	private List<YTCouponMetaInfo> coupons;

	/**
	 * @return the coupons
	 */
	public List<YTCouponMetaInfo> getCoupons() {
		return coupons;
	}

	/**
	 * @param coupons the coupons to set
	 */
	public void setCoupons(List<YTCouponMetaInfo> coupons) {
		this.coupons = coupons;
	}
}
