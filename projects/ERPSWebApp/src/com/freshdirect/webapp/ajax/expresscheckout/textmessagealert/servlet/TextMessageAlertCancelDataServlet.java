package com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.service.TextMessageAlertService;

public class TextMessageAlertCancelDataServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 9054313460890172514L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			boolean isRequestStoredSuccessFully = false;

			final FormDataRequest alertRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);

			isRequestStoredSuccessFully = TextMessageAlertService.defaultService().cancelSmsAlertPreferences(alertRequest, user);

			final FormDataResponse alertResponse = createFormDataResponse(alertRequest.getFormId(), isRequestStoredSuccessFully);
			writeResponseData(response, alertResponse);
		} catch (final Exception e) {
			BaseJsonServlet.returnHttpError(500, "Error while cancel text message alert preferences for user " + user.getUserId());
		}
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.SIGNED_IN;
	}

	private FormDataResponse createFormDataResponse(String formId, boolean success) {
		final FormDataResponse formDataResponse = new FormDataResponse();
		final SubmitForm submitForm = new SubmitForm();
		submitForm.setSuccess(success);
		submitForm.setFormId(formId);
		formDataResponse.setFormSubmit(submitForm);
		return formDataResponse;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

}
