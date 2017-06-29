package com.freshdirect.webapp.ajax.expresscheckout.servlet;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.checkout.service.CheckoutService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.checkout.RedirectToPage;

public class SinglePageCheckoutServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 3291565289153034570L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
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

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.SIGNED_IN;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
