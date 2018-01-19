package com.freshdirect.webapp.ajax.expresscheckout.cart.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class CartDataServlet extends BaseJsonServlet {

	private static final String ORDER_ID = "orderId";

	private static final long serialVersionUID = -3650318272577031376L;
	private static final Logger LOG = LoggerFactory.getInstance(CartDataServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			Map<String, Object> responseData;
			String actionName = request.getParameter("action");
			if ("startCheckout".equals(actionName)) {
                StandingOrderHelper.clearSO3Context(user, request.getParameter("isSO"), null);
				final FormDataRequest startCheckoutData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
				ValidationResult validationResult = new ValidationResult();
	            FormDataResponse result = FormDataService.defaultService().prepareFormDataResponse(startCheckoutData, validationResult);
	            CartDataService.defaultService().validateOrderMinimumOnStartCheckout(user, result);
	            CartDataService.defaultService().validateCarouselData(request, user, result);
				
				// make-good validation
				if (user != null && user.getMasqueradeContext() != null && user.getMasqueradeContext().getMakeGoodFromOrderId() != null) {
					ErpComplaintModel complaintModel = (ErpComplaintModel) request.getSession().getAttribute(SessionName.MAKEGOOD_COMPLAINT);
					
					boolean success = true;
					if (complaintModel == null) {
						result.getSubmitForm().getResult().put("global", "Technical Error! Missing Complaints!");
						success = false;
					} else {
                        List<String> badCartLineIds = new ArrayList<String>();

						for (FDCartLineI line : user.getShoppingCart().getOrderLines()) {
							final String orderLineId = line.getOrderLineId();
							ErpComplaintLineModel cline = complaintModel.getComplaintLine( orderLineId );
							if (cline == null) {
                                badCartLineIds.add(Integer.toString(line.getRandomId()));
								success = false;
							} else if (cline.getReason() == null) {
                                badCartLineIds.add(Integer.toString(line.getRandomId()));
								success = false;
							}
						}
						
						if (!success && result.getSubmitForm().isSuccess()) {
                            result.getSubmitForm().getResult().put("reasonFailures", badCartLineIds);
							result.getSubmitForm().setSuccess( false );
				            result.getSubmitForm().getResult().put("redirectUrl", null);
						}
					}
				}
				// make-good validation
				
				responseData = SoyTemplateEngine.convertToMap(result);
			} else {
				String orderId = request.getParameter(ORDER_ID);
				boolean isOAuth = isOAuthTokenInHeader(request);
				boolean shouldFetch = request.getParameter("fetch") != null && request.getParameter("fetch").equals("true");
				if ((isOAuth ||shouldFetch) && user!= null && user.getIdentity() != null) {
					try {
						FDUser recognizedUser = FDCustomerManager.recognize(user.getIdentity(), false);
						user.setShoppingCart(recognizedUser.getShoppingCart());
					} catch (FDAuthenticationException e) {
						LOG.error("Failed to recognize user", e);
						BaseJsonServlet.returnHttpError(500, "Failed to get shopping cart for user.");
					}
				}
				if (orderId == null || orderId.isEmpty()) {
					// if isOAuth is true, do not get/set session related data
					CartData cartData = CartDataService.defaultService().loadCartData(request, user, !isOAuth);
					responseData = SoyTemplateEngine.convertToMap(cartData);
				} else {
					CartData cartData = CartDataService.defaultService().loadCartSuccessData(request, user, orderId);
					responseData = SoyTemplateEngine.convertToMap(cartData);
				}
			}
			writeResponseData(response, responseData);
		} catch (FDResourceException e) {
			LOG.error("Failed to load cart for user", e);
			BaseJsonServlet.returnHttpError(500, "Failed to load cart for user.");
		} catch (JspException e) {
			LOG.error("Failed to load cart for user", e);
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
	
	@Override
	protected boolean isOAuthEnabled() {
		return true;
	}

}
