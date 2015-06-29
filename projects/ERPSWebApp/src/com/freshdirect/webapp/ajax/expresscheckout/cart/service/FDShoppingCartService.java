package com.freshdirect.webapp.ajax.expresscheckout.cart.service;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag;


public class FDShoppingCartService {

	private static final FDShoppingCartService INSTANCE = new FDShoppingCartService();

	public static final String CART_CHANGED_BY_CLEAN_UP_SESSION_ATTRIBUTE_ID = "cartChangedByCleanUp";

	private FDShoppingCartService() {
	}

	public static FDShoppingCartService defaultService() {
		return INSTANCE;
	}
	
	public void updateShoppingCart(FDUserI user, HttpSession session) throws JspException {
		FDCartModel cart = user.getShoppingCart();
		FDShoppingCartControllerTag.handleDeliveryPass(user, cart);
		boolean cartChangedByCleanUp = FDShoppingCartControllerTag.cartCleanUp(true, cart, session, user);
		session.setAttribute(CART_CHANGED_BY_CLEAN_UP_SESSION_ATTRIBUTE_ID, cartChangedByCleanUp);
		FDShoppingCartControllerTag.requiredCouponEvaluation(session, user, false);
		user.updateUserState();
	}
}
