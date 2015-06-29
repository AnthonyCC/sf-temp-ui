package com.freshdirect.webapp.ajax.expresscheckout.cart.servlet;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.soy.SoyTemplateEngine;

public class CartDataServlet extends BaseJsonServlet {

	private static final String ORDER_ID = "orderId";

	private static final long serialVersionUID = -3650318272577031376L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			Map<String, Object> responseData;
			String actionName = request.getParameter("action");
			if ("startCheckout".equals(actionName)) {
				final FormDataRequest startCheckoutData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
				responseData = SoyTemplateEngine.convertToMap(CartDataService.defaultService().validateOrderMinimumOnStartCheckout(user, startCheckoutData));
			} else {
				String orderId = request.getParameter(ORDER_ID);
				if (orderId == null) {
					CartData cartData = CartDataService.defaultService().loadCartData(request, user);
					responseData = SoyTemplateEngine.convertToMap(cartData);
				} else {
					CartData cartData = CartDataService.defaultService().loadCartSuccessData(request, user, orderId);
					responseData = SoyTemplateEngine.convertToMap(cartData);
				}
			}
			writeResponseData(response, responseData);
		} catch (FDResourceException e) {
			BaseJsonServlet.returnHttpError(500, "Failed to load cart for user.");
		} catch (JspException e) {
			BaseJsonServlet.returnHttpError(500, "Failed to load cart for user.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			CartData cartData = CartDataService.defaultService().updateCartData(request, user);
			writeResponseData(response, cartData);
		} catch (FDResourceException e) {
			BaseJsonServlet.returnHttpError(500, "Failed to update cart for user.");
		} catch (JspException e) {
			BaseJsonServlet.returnHttpError(500, "Failed to update cart for user.");
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
