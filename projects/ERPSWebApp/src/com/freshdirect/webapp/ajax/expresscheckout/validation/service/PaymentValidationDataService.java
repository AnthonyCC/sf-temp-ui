package com.freshdirect.webapp.ajax.expresscheckout.validation.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;

public class PaymentValidationDataService implements FormValidationService {

	private static final String NO_AVAILABLE_VALIDATOR_IS_FOUND = "No available validator is found.";
	public static final String ADD_PAYMENT_CREDIT_CARD = "CC";
	private static final String ADD_PAYMENT_BANK = "EC";
	private static final String ADD_PAYMENT_ELECTRONIC_BENEFIT_TRANSFER = "ET";
	private static final String ADD_PAYMENT_MASTER_PASS = "MP";

	private static final PaymentValidationDataService INSTANCE = new PaymentValidationDataService();

	private ValidationProviderService validationService;
	private ValidationDataAssemblerService assemblerService;

	private PaymentValidationDataService() {
		validationService = ValidationProviderService.defaultService();
		assemblerService = ValidationDataAssemblerService.defaultService();
	}

	public static PaymentValidationDataService defaultService() {
		return INSTANCE;
	}

	@Override
	public List<ValidationError> prepareAndValidate(final FormDataRequest paymentRequestData) {
		List<ValidationError> validationErrors = null;
		final String formId = paymentRequestData.getFormId();
		final Map<String, String> validatedData = assemblerService.assembleValidatedData(paymentRequestData);

		if (checkCreditCardValidator(formId)) {
			validationErrors = validationService.validateCreditCardPayment(validatedData);
		} else if (checkBankValidator(formId)) {
			validationErrors = validationService.validateBankPayment(validatedData);
		} else if (checkEbtValidator(formId)) {
			validationErrors = validationService.validateEbtPayment(validatedData);
		}else if (checkMPValidator(formId)) {
			validationErrors = validationService.validateCreditCardPayment(validatedData);
		} else {
			noValidatiorAvailable();
		}

		removeUnnecessaryValidationErrors(paymentRequestData, validationErrors);

		return validationErrors;
	}

	private void removeUnnecessaryValidationErrors(final FormDataRequest paymentRequestData, List<ValidationError> validationErrors) {
		if (validationErrors != null && !validationErrors.isEmpty() 
			&& ( "editPaymentMethod".equals(paymentRequestData.getFormData().get("action")) || "addPaymentMethod".equals(paymentRequestData.getFormData().get("action")) )
		) {
			Iterator<ValidationError> validationErrorIterator = validationErrors.iterator();
			while (validationErrorIterator.hasNext()) {
				ValidationError error = validationErrorIterator.next();
				if ("editPaymentMethod".equals(paymentRequestData.getFormData().get("action"))) {
					if (PaymentMethodName.ACCOUNT_NUMBER.equals(error.getName())) {
						validationErrorIterator.remove();
					}
				}
				//if international, ignore state error
				if ( (EnumUserInfoName.BIL_STATE.getCode()).equals(error.getName()) && !(paymentRequestData.getFormData().get(EnumUserInfoName.BIL_COUNTRY.getCode())).equals("US")) {
					validationErrorIterator.remove();
				}
				//if international, ignore zip error
				if ( (EnumUserInfoName.BIL_ZIPCODE.getCode()).equals(error.getName()) && !(paymentRequestData.getFormData().get(EnumUserInfoName.BIL_COUNTRY.getCode())).equals("US")) {
					validationErrorIterator.remove();
				}
			}
		}
	}

	private boolean checkCreditCardValidator(String formId) {
		return ADD_PAYMENT_CREDIT_CARD.equalsIgnoreCase(formId);
	}

	private boolean checkBankValidator(String formId) {
		return ADD_PAYMENT_BANK.equalsIgnoreCase(formId);
	}

	private boolean checkEbtValidator(String formId) {
		return ADD_PAYMENT_ELECTRONIC_BENEFIT_TRANSFER.equalsIgnoreCase(formId);
	}

	private boolean checkMPValidator(String formId) {
		return ADD_PAYMENT_MASTER_PASS.equalsIgnoreCase(formId);
	}
	
	private void noValidatiorAvailable() {
		throw new IllegalArgumentException(NO_AVAILABLE_VALIDATOR_IS_FOUND);
	}
}
