package com.freshdirect.webapp.ajax.expresscheckout.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.checkout.service.CheckoutService;
import com.freshdirect.webapp.ajax.expresscheckout.data.DrawerData;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormMetaData;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.drawer.service.DrawerService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormMetaDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotData;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class SinglePageCheckoutServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 3291565289153034570L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {
		try {
			if (request.getParameter("action") != null) {
				doGetAction(request, response, user, request.getParameter("action"));
				return;
			}
			
			final SinglePageCheckoutData data = SinglePageCheckoutFacade.defaultFacade().load(user, request);
			writeResponseData(response, data);
		} catch (FDResourceException e) {
			returnHttpError(500, "Failed to load single page checkout info.", e);
		} catch (IOException e) {
			returnHttpError(500, "Failed to load Learn More media for restriction message.", e);
		} catch (TemplateException e) {
			returnHttpError(500, "Failed to render Learn More HTML media for restricion message.", e);
		} catch (JspException e) {
			returnHttpError(500, "Failed to load single page checkout info.", e);
		} catch (RedirectToPage e) {
			returnHttpError(500, "Failed to load single page checkout info.", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			final FormDataRequest placeOrderData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			FormDataResponse responseData = CheckoutService.defaultService().submitOrder(user, placeOrderData, request.getSession(), request, response);
			writeResponseData(response, responseData);
        } catch (FDResourceException exception) {
            returnHttpErrorWithMessage(500, exception.getMessage(), exception);
		} catch (FDInvalidConfigurationException e) {
			returnHttpError(500, "Failed to post single page checkout info.", e);
		} catch (JspException e) {
			returnHttpError(500, MessageFormat.format("Failed to update cart for user[{0}] after adjust ATP restrictions.", user.getIdentity().getErpCustomerPK()), e);
		} catch (Exception e) {
			returnHttpError(500, MessageFormat.format("Failed to submit order for user[{0}].", user.getIdentity().getErpCustomerPK()), e);
		}
	}

	private void doGetAction(HttpServletRequest request, HttpServletResponse response, FDUserI user, String action)
			throws HttpErrorResponse, FDResourceException, JspException, RedirectToPage, IOException,
			TemplateException {
		if (action.equals("resetContext")) {
			StandingOrderHelper.clearSO3Context(user, request.getParameter("isSO"),
					request.getParameter("standingOrder"));

			SinglePageCheckoutFacade.defaultFacade().handleModifyCartPreSelections(user, request);
			if (FDStoreProperties.getAvalaraTaxEnabled() && null != user.getShoppingCart()
					&& null != user.getShoppingCart().getDeliveryAddress()) {
				CheckoutService.defaultService().getAvalaraTax(user.getShoppingCart());
			}
			writeResponseData(response, null);
		} else if (action.equals("getDrawer")) {
			Map<String, List<DrawerData>> data = DrawerService.defaultService().loadDrawer(user);
			writeResponseData(response, data);
		} else if (action.equals("getTimeSlot")) {
			FormTimeslotData data;
			if (StandingOrderHelper.isSO3StandingOrder(user)) {
				data = TimeslotService.defaultService().loadCartTimeslot(user, user.getSoTemplateCart());
			} else {
				data = TimeslotService.defaultService().loadCartTimeslot(user, user.getShoppingCart());
			}
			writeResponseData(response, data);
		} else if (action.equals("getRedirectUrl")) {
			if (!StandingOrderHelper.isSO3StandingOrder(user)) {
				String redirectUri = SinglePageCheckoutFacade.defaultFacade().populateRedirectUrl(user);
				writeResponseData(response, redirectUri);
			}
		} else if (action.equals("getFormMetaData")) {
			FormMetaData data = FormMetaDataService.defaultService().populateFormMetaData(user);
			writeResponseData(response, data);

		}
	}
	
	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.SIGNED_IN;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
