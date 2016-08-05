package com.freshdirect.mobileapi.api.service;

import org.springframework.stereotype.Component;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.model.SessionUser;

@Component
public class CartService {

    public CartDetail getCartDetail(SessionUser user) throws FDException {
        return user.getShoppingCart().getCartDetail(user, EnumCouponContext.VIEWCART);
    }

}
