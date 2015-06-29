package com.freshdirect.webapp.ajax.expresscheckout.fdcoupon.service;

import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.webapp.taglib.fdstore.display.FDCouponTag;


public class FDCouponService {

	private static final FDCouponService INSTANCE = new FDCouponService();

	private FDCouponService() {
	}

	public static FDCouponService defaultService() {
		return INSTANCE;
	}

	public String getContent(FDCustomerCoupon coupon, String contClass) {
		FDCouponTag tag = new FDCouponTag();
		tag.setCoupon(coupon);
		tag.setContClass(contClass);
		tag.initContent(null);
		return tag.getContent();
	}

}
