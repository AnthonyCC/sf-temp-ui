package com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.service.TextMessageAlertService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.AlertValidationService;

public class TextMessageAlertSubmitDataServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 2212391641766279478L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			boolean isRequestStoredSuccessFully = false;

			final FormDataRequest alertRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			final ValidationResult validationResult = new ValidationResult();
			validationResult.setFdform(alertRequest.getFormId());
			validationResult.getErrors().addAll(AlertValidationService.defaultService().prepareAndValidate(alertRequest));

			if (isValidationOk(validationResult)) {
				isRequestStoredSuccessFully = TextMessageAlertService.defaultService().storeSmsAlertPreferences(alertRequest, user);
			}

			final FormDataResponse alertResponse = createFormDataResponse(alertRequest.getFormId(), validationResult, isRequestStoredSuccessFully);
			writeResponseData(response, alertResponse);
		} catch (final Exception e) {
			BaseJsonServlet.returnHttpError(500, "Error while store text message alert preferences for user " + user.getUserId());
		}
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.SIGNED_IN;
	}

	private boolean isValidationOk(final ValidationResult validationResult) {
		return validationResult.getErrors().isEmpty();
	}

	private FormDataResponse createFormDataResponse(String formId, ValidationResult validationResult, boolean success) {
		final FormDataResponse formDataResponse = new FormDataResponse();
		final SubmitForm submitForm = new SubmitForm();
		submitForm.setSuccess(success);
		submitForm.setFormId(formId);
		formDataResponse.setFormSubmit(submitForm);
		formDataResponse.setValidationResult(validationResult);
		return formDataResponse;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

}
