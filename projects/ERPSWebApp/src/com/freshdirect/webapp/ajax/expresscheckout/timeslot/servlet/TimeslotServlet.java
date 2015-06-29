package com.freshdirect.webapp.ajax.expresscheckout.timeslot.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.soy.SoyTemplateEngine;

public class TimeslotServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 8229972384125974371L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			FormDataRequest timeslotRequestData = parseRequestData(request, FormDataRequest.class);
			FormDataResponse responseData = new FormDataResponse();
			SubmitForm submitForm = new SubmitForm();
			submitForm.setFormId(timeslotRequestData.getFormId());
			responseData.setFormSubmit(submitForm);
			ValidationResult validationResult = new ValidationResult();
			validationResult.setFdform(timeslotRequestData.getFormId());
			responseData.setValidationResult(validationResult);
			try {
				List<ValidationError> timeslotReservationErrors = TimeslotService.defaultService().reserveDeliveryTimeSlot(timeslotRequestData, request.getSession());
				validationResult.getErrors().addAll(timeslotReservationErrors);
			} catch (FDResourceException e) {
				validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not reserve timeslot due to technical difficulty."));
			}
			if (validationResult.getErrors().isEmpty()) {
				try {
					SinglePageCheckoutData checkoutData = SinglePageCheckoutFacade.defaultFacade().load(user, request);
					submitForm.setResult(SoyTemplateEngine.convertToMap(checkoutData));
				} catch (FDResourceException e) {
					validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not load checkout data due to technical difficulty."));
				} catch (JspException e) {
					validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not load checkout data due to technical difficulty."));
				} catch (RedirectToPage e) {
					validationResult.getErrors().add(new ValidationError("technical_difficulty", "Could not load checkout data due to technical difficulty."));
				}
			}
			submitForm.setSuccess(validationResult.getErrors().isEmpty());
			writeResponseData(response, responseData);
		} catch (IOException e) {
			returnHttpError(500, "Failed to load Learn More media for restriction message.", e);
		} catch (TemplateException e) {
			returnHttpError(500, "Failed to render Learn More HTML media for restricion message.", e);
		}
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

}
