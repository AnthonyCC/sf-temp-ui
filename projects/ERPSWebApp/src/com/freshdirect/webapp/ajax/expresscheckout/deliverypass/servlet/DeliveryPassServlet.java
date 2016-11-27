package com.freshdirect.webapp.ajax.expresscheckout.deliverypass.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.data.DeliveryPassData;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.service.DeliveryPassService;

public class DeliveryPassServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -6503107227290115125L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			DeliveryPassData responseData = DeliveryPassService.defaultService().loadDeliveryPasses(user);
			writeResponseData(response, responseData);
		} catch (FDResourceException e) {
			returnHttpError(500, "Failed to load delivery pass products", e);
		} catch (FDSkuNotFoundException e) {
			returnHttpError(500, "Delivery pass product SKU code not found.", e);
		} catch (IOException e) {
			returnHttpError(500, "Failed to load Terms & Conditions media for Delivery Pass.", e);
		} catch (TemplateException e) {
			returnHttpError(500, "Failed to render Terms & Conditions HTML media for Delivery Pass.", e);
		}
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

}
