package com.freshdirect.webapp.ajax.expresscheckout.gogreen.servlet;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.gogreen.service.GoGreenService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;

public class GoGreenServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 7603225763950961920L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		FormDataRequest goGreenFormData = parseRequestData(request, FormDataRequest.class);
		try {
			GoGreenService.defaultService().saveGoGreenOption(goGreenFormData, user);
			FormDataResponse responseData = new FormDataResponse();
			SubmitForm submitForm = new SubmitForm();
			submitForm.setFormId(goGreenFormData.getFormId());
			submitForm.setSuccess(true);
			responseData.setFormSubmit(submitForm);
			ValidationResult validationResult = new ValidationResult();
			validationResult.setFdform(goGreenFormData.getFormId());
			responseData.setValidationResult(validationResult);
			writeResponseData(response, responseData);
		} catch (FDResourceException e) {
			returnHttpError(500, MessageFormat.format("Cannot save go green option [{0}] for user: {1}", goGreenFormData.getFormData().get(GoGreenService.GO_GREEN_FIELD_ID), user.getIdentity().getErpCustomerPK()), e);
		}
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
