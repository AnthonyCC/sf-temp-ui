package com.freshdirect.fdstore.ecoupon.service;

import java.util.List;
import java.util.Set;

import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponCartResponse;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponMetaDataResponse;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponResponse;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCustomerCouponResponse;

public interface CouponService {
	
	void close();
	YTCouponMetaDataResponse getCoupons() throws CouponServiceException;	
	YTCustomerCouponResponse getCouponsForUser(FDCouponCustomer couponCustomer) throws CouponServiceException;
	YTCouponResponse doClipCoupon(String couponId, FDCouponCustomer couponCustomer) throws CouponServiceException;
	YTCouponCartResponse evaluateCartAndCoupons(CouponCart couponCart) throws CouponServiceException;
	YTCouponCartResponse submitOrder(CouponCart couponCart) throws CouponServiceException;
	YTCouponResponse cancelOrder(String orderId) throws CouponServiceException;
	YTCouponResponse confirmOrder(String orderId,Set<String> coupons) throws CouponServiceException;
}
