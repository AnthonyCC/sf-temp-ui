package com.freshdirect.webapp.ajax.expresscheckout.validation.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.expresscheckout.validation.ConstraintValidator;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public class ValidationProviderService {

	private static final ValidationProviderService INSTANCE = new ValidationProviderService();

	private final ConstraintValidator validator;
	private final ConstraintProviderService constraintService;

	private ValidationProviderService() {
		validator = ConstraintValidator.defaultValidator();
		constraintService = ConstraintProviderService.defaultService();
	}

	public static ValidationProviderService defaultService() {
		return INSTANCE;
	}

	public List<ValidationError> validateHomeDeliveryAddress(final Map<String, String> data) {
		return validator.validateByDatas(data, constraintService.getHomeDeliveryAddressConstraints(data));
	}

	public List<ValidationError> validateOfficeDeliveryAddress(final Map<String, String> data) {
		return validator.validateByDatas(data, constraintService.getOfficeDeliveryAddressConstraints());
	}

	public List<ValidationError> validateCreditCardPayment(final Map<String, String> data) {
		return validator.validateByDatas(data, constraintService.getCreditCardPaymentConstraints());
	}

	public List<ValidationError> validateBankPayment(final Map<String, String> data) {
		return validator.validateByDatas(data, constraintService.getBankPaymentConstraints());
	}

	public List<ValidationError> validateEbtPayment(final Map<String, String> data) {
		return validator.validateByDatas(data, constraintService.getElectronicBenefitTransferPaymentConstraints());
	}

	public List<ValidationError> validateSmsAlert(final Map<String, String> data) {
		return validator.validateByDatas(data, constraintService.getSmsAlertConstraints(data));
	}
	
	public List<ValidationError> validateUnattendedDelivery(final Map<String, String> data) {
		return validator.validateByConstraints(data, constraintService.getUnattendedDeliveryAddressConstraints());
	}

}
