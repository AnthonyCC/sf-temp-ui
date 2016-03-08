package com.freshdirect.webapp.ajax.expresscheckout.cart.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class CartDataServlet extends BaseJsonServlet {

	private static final String ORDER_ID = "orderId";

	private static final long serialVersionUID = -3650318272577031376L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			Map<String, Object> responseData;
			String actionName = request.getParameter("action");
			if ("startCheckout".equals(actionName)) {
				StandingOrderHelper.clearSO3Context(user, request, null);
				final FormDataRequest startCheckoutData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
				// com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag.handleMakeGood(String[], String[], HttpSession, List<FDCartLineI>, ActionResult)
				final FormDataResponse result = CartDataService.defaultService().validateOrderMinimumOnStartCheckout(user, startCheckoutData);
				
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
							final String cartLineId = line.getCartlineId();
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
