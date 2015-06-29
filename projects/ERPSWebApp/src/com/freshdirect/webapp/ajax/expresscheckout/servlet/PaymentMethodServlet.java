package com.freshdirect.webapp.ajax.expresscheckout.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentEditData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.service.PaymentService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.soy.SoyTemplateEngine;

public class PaymentMethodServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -7582639712245761241L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			final FormDataRequest paymentRequestData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			String action = FormDataService.defaultService().get(paymentRequestData, "action");
			ValidationResult validationResult = new ValidationResult();
			validationResult.setFdform(paymentRequestData.getFormId());
			boolean changed = false;
			final FormDataResponse paymentSubmitResponse = createPaymentResponse(paymentRequestData, validationResult);
			if ("addPaymentMethod".equals(action)) {
				List<ValidationError> validationErrors = PaymentService.defaultService().addPaymentMethod(paymentRequestData, request, user);
				validationResult.getErrors().addAll(validationErrors);
				if (validationErrors.isEmpty()) {
					List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(user.getIdentity()).getPaymentMethods();
					if (!paymentMethods.isEmpty()) {
						String paymentId = paymentMethods.get(0).getPK().getId();
						validationErrors = PaymentService.defaultService().selectPaymentMethod(paymentId, action, request);
						validationResult.getErrors().addAll(validationErrors);
					}
				}
				changed = true;
			} else if ("editPaymentMethod".equals(action)) {
				List<ValidationError> validationErrors = PaymentService.defaultService().editPaymentMethod(paymentRequestData, request, user);
				validationResult.getErrors().addAll(validationErrors);
				changed= true;
			} else if ("deletePaymentMethod".equals(action)) {
				PaymentService.defaultService().deletePaymentMethod(paymentRequestData, request);
				changed= true;
			} else if ("selectPaymentMethod".equals(action)) {
				String paymentId = FormDataService.defaultService().get(paymentRequestData, "id");
				List<ValidationError> validationErrors = PaymentService.defaultService().selectPaymentMethod(paymentId, action, request);
				validationResult.getErrors().addAll(validationErrors);
				changed= true;
			} else if ("loadPaymentMethod".equals(action)) {
				String paymentId = FormDataService.defaultService().get(paymentRequestData, "id");
				PaymentEditData userPaymentMethod = PaymentService.defaultService().loadUserPaymentMethod(user, paymentId);
				paymentSubmitResponse.getSubmitForm().getResult().put("paymentEditValue", userPaymentMethod);
			}
			paymentSubmitResponse.getSubmitForm().setSuccess(paymentSubmitResponse.getValidationResult().getErrors().isEmpty());
			if (changed && paymentSubmitResponse.getSubmitForm().isSuccess()) {
				SinglePageCheckoutData singlePageCheckoutData = SinglePageCheckoutFacade.defaultFacade().load(user, request);
				paymentSubmitResponse.getSubmitForm().setResult(SoyTemplateEngine.convertToMap(singlePageCheckoutData));
			}
			writeResponseData(response, paymentSubmitResponse);
		} catch (final Exception e) {
			BaseJsonServlet.returnHttpError(500, "Error while submit payment for user " + user.getUserId(), e);
		}
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.SIGNED_IN;
	}

	private FormDataResponse createPaymentResponse(final FormDataRequest paymentRequest, final ValidationResult formValidation) {
		final FormDataResponse paymentResponse = new FormDataResponse();
		final SubmitForm submitForm = new SubmitForm();
		submitForm.setFormId(paymentRequest.getFormId());
		submitForm.setSuccess(formValidation.getErrors().isEmpty());
		paymentResponse.setFormSubmit(submitForm);
		paymentResponse.setValidationResult(formValidation);
		return paymentResponse;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
