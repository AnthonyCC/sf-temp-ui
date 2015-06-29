package com.freshdirect.webapp.ajax.expresscheckout.validation.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public class AlertValidationService implements FormValidationService {

	private static final AlertValidationService INSTANCE = new AlertValidationService();

	private ValidationProviderService validationService;
	private ValidationDataAssemblerService assemblerService;

	private AlertValidationService() {
		validationService = ValidationProviderService.defaultService();
		assemblerService = ValidationDataAssemblerService.defaultService();
	}

	public static AlertValidationService defaultService() {
		return INSTANCE;
	}

	@Override
	public List<ValidationError> prepareAndValidate(FormDataRequest alertRequestData) {
		final Map<String, String> validatedData = assemblerService.assembleValidatedData(alertRequestData);
		return validationService.validateSmsAlert(validatedData);
	}
}
