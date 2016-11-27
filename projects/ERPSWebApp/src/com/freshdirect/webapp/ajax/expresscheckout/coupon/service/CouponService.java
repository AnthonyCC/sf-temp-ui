package com.freshdirect.webapp.ajax.expresscheckout.coupon.service;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;

public class CouponService {

    private static final CouponService INSTANCE = new CouponService();

    private CouponService() {
    }

    public static CouponService defaultService() {
        return INSTANCE;
    }

    public Boolean clipCoupon(String couponId, HttpSession session, FDUserI user) {
        Boolean result = false;
        if (user != null && couponId != null && couponId.trim().length() > 0) {
            result = FDCustomerCouponUtil.clipCoupon(session, couponId);
            if (user.isCouponEvaluationRequired()) {
                FDCustomerCouponUtil.evaluateCartAndCoupons(session);
            }
        }
        return result;
    }
}
