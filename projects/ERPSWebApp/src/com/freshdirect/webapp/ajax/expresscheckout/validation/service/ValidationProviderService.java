package com.freshdirect.webapp.ajax.expresscheckout.validation.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.ewallet.PaymentMethodName;
import com.freshdirect.webapp.ajax.expresscheckout.validation.ConstraintValidator;
import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.TwoFieldValidator;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

/**
 * IF YOU ARE IN THIS CLASS MOST LIKELY YOU WILL HAVE TO DOUBLE CHECK 
 * CrmPaymentMethodControllerTag
 *  com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil
 *  com.freshdirect.webapp.ajax.expresscheckout.validation.service.ValidationProviderService
 *  com.freshdirect.fdstore.payments.util.PaymentMethodUtil
 *  Especially if you are validating  BANKING account INFORMATION.
 */
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
		List<ValidationError> errors = validator.validateByDatas(data, constraintService.getBankPaymentConstraints());
		ValidationError verifyAccountNumberError = validateBankPaymentAccountNumberVerify(data);
		if (null != verifyAccountNumberError) {
			errors.add(verifyAccountNumberError);
		}
		return errors;
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

	/*THIS IS AN ODD DUCK THAT iM ADDING IN; appdev 6822 and 6789,
	 *  account number verify and only digits in account number.
	 *  Im checking for the presence of both fields in the map before trying to verify it.
	 */

	public ValidationError validateBankPaymentAccountNumberVerify(final Map<String, String> data) {
		TwoFieldValidator validator = new TwoFieldValidator();
		ValidationError error = null;
		if (data.containsKey(PaymentMethodName.ACCOUNT_NUMBER)
				&& data.containsKey(PaymentMethodName.ACCOUNT_NUMBER_VERIFY)) {

			if (!validator.isValid(data.get(PaymentMethodName.ACCOUNT_NUMBER),
					data.get(PaymentMethodName.ACCOUNT_NUMBER_VERIFY))) {
				error = new ValidationError(PaymentMethodName.ACCOUNT_NUMBER_VERIFY, validator.getErrorMessage());
			}
		}

		return error;
	}

}
