package com.freshdirect.webapp.ajax.expresscheckout.servlet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentEditData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.service.PaymentService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;

public class PaymentMethodServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -7582639712245761241L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			final FormDataRequest paymentRequestData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			PageAction pageAction = FormDataService.defaultService().getPageAction(paymentRequestData);
			ValidationResult validationResult = new ValidationResult();
			boolean changed = false;
			final FormDataResponse paymentSubmitResponse = FormDataService.defaultService().prepareFormDataResponse(paymentRequestData, validationResult);
			if (pageAction != null) {
				switch (pageAction) {
				case ADD_PAYMENT_METHOD: {
					List<ValidationError> validationErrors = PaymentService.defaultService().addPaymentMethod(paymentRequestData, request, user);
					validationResult.getErrors().addAll(validationErrors);
					if (validationErrors.isEmpty()) {
						List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(user.getIdentity()).getPaymentMethods();
						if (!paymentMethods.isEmpty()) {
							String paymentId = paymentMethods.get(0).getPK().getId();
							validationErrors = PaymentService.defaultService().selectPaymentMethod(paymentId, pageAction.actionName, request);
							validationResult.getErrors().addAll(validationErrors);
						}
					}
					changed = true;
					break;
				}
				case EDIT_PAYMENT_METHOD: {
					List<ValidationError> validationErrors = PaymentService.defaultService().editPaymentMethod(paymentRequestData, request, user);
					validationResult.getErrors().addAll(validationErrors);
					changed= true;
					break;
				}
				case DELETE_PAYMENT_METHOD: {
					PaymentService.defaultService().deletePaymentMethod(paymentRequestData, request);
					changed= true;
					break;
				}
				case SELECT_PAYMENT_METHOD: {
					String paymentId = FormDataService.defaultService().get(paymentRequestData, "id");
					List<ValidationError> validationErrors = PaymentService.defaultService().selectPaymentMethod(paymentId, pageAction.actionName, request);
					validationResult.getErrors().addAll(validationErrors);
					changed= true;
					break;
				}
				case LOAD_PAYMENT_METHOD: {
					String paymentId = FormDataService.defaultService().get(paymentRequestData, "id");
					PaymentEditData userPaymentMethod = PaymentService.defaultService().loadUserPaymentMethod(user, paymentId);
					paymentSubmitResponse.getSubmitForm().getResult().put("paymentEditValue", userPaymentMethod);
					break;
				}
				default:
					break;
				}
				paymentSubmitResponse.getSubmitForm().setSuccess(paymentSubmitResponse.getValidationResult().getErrors().isEmpty());
				if (changed && paymentSubmitResponse.getSubmitForm().isSuccess()) {
					Map<String, Object> singlePageCheckoutData = SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult);
					paymentSubmitResponse.getSubmitForm().setResult(singlePageCheckoutData);
				}
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

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
