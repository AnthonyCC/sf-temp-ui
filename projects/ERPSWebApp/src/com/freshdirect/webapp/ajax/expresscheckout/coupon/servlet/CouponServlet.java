package com.freshdirect.webapp.ajax.expresscheckout.coupon.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.coupon.service.CouponService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;

public class CouponServlet extends BaseJsonServlet {

    private static final String CART_DATA_KEY = "cartData";
    private static final String SUCCESS_FLAG_KEY = "success";
    private static final String PROMOTION_CLIP_ACTION_NAME = "clip";

    private static final long serialVersionUID = -7248138306304783880L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        try {
            Map<String, Object> responseData = new HashMap<String, Object>();
            String action = request.getParameter("action");
            String couponId = request.getParameter("cpid");
            if (PROMOTION_CLIP_ACTION_NAME.equals(action)) {
                boolean result = CouponService.defaultService().clipCoupon(couponId, request.getSession(), user);
                responseData.put(SUCCESS_FLAG_KEY, result);
                if (result) {
                    CartData cartData = CartDataService.defaultService().loadCartData(request, user);
                    responseData.put(CART_DATA_KEY, SoyTemplateEngine.convertToMap(cartData));
                }
            }
            writeResponseData(response, responseData);
        } catch (FDResourceException e) {
            BaseJsonServlet.returnHttpError(500, "Failed to clip coupon for user.");
        } catch (JspException e) {
            BaseJsonServlet.returnHttpError(500, "Failed to clip coupon for user.");
        }
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

}
