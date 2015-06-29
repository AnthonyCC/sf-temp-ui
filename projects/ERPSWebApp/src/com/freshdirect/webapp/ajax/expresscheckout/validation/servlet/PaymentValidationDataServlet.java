package com.freshdirect.webapp.ajax.expresscheckout.validation.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.PaymentValidationDataService;

public class PaymentValidationDataServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -7582639712245761241L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			final FormDataRequest paymentValidationRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			final ValidationResult validationResult = new ValidationResult();
			validationResult.setFdform(paymentValidationRequest.getFormId());
			validationResult.getErrors().addAll(PaymentValidationDataService.defaultService().prepareAndValidate(paymentValidationRequest));
			final FormDataResponse deliveryAddressResponse = createPaymentValidationResponse(paymentValidationRequest, validationResult);
			writeResponseData(response, deliveryAddressResponse);
		} catch (final Exception e) {
			BaseJsonServlet.returnHttpError(500, "Error while validate payment for user " + user.getUserId(), e);
		}
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.SIGNED_IN;
	}

	private FormDataResponse createPaymentValidationResponse(FormDataRequest paymentValidationRequest, ValidationResult formValidation) {
		final FormDataResponse deliveryAddressResponse = new FormDataResponse();
		deliveryAddressResponse.setValidationResult(formValidation);
		return deliveryAddressResponse;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

}
