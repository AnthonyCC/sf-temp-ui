package com.freshdirect.webapp.ajax.expresscheckout.validation.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public class DeliveryAddressValidationDataService implements FormValidationService {

    public static final String ADD_ADDRESS_HOME = "addaddress_home";
    public static final String ADD_ADDRESS_OFFICE = "addaddress_office";
    private static final String NO_AVAILABLE_VALIDATOR_IS_FOUND = "No available validator is found.";

    private static final DeliveryAddressValidationDataService INSTANCE = new DeliveryAddressValidationDataService();

    private ValidationProviderService validationService;
    private ValidationDataAssemblerService assemblerService;

    private DeliveryAddressValidationDataService() {
        validationService = ValidationProviderService.defaultService();
        assemblerService = ValidationDataAssemblerService.defaultService();
    }

    public static DeliveryAddressValidationDataService defaultService() {
        return INSTANCE;
    }

    @Override
    public List<ValidationError> prepareAndValidate(final FormDataRequest addressRequestData) {
        final String formId = addressRequestData.getFormId();
        final Map<String, String> validatedData = assemblerService.assembleValidatedData(addressRequestData);
        List<ValidationError> validationErrors = null;

        if (checkHomeValidator(formId)) {
            validationErrors = validationService.validateHomeDeliveryAddress(validatedData);
        } else if (checkOfficeValidator(formId)) {
            validationErrors = validationService.validateOfficeDeliveryAddress(validatedData);
        } else {
            noValidatiorAvailable();
        }
        return validationErrors;
    }

    public List<ValidationError> validateUnattendedDelivery(final Map<String, String> addressData) {
        return validationService.validateUnattendedDelivery(addressData);
    }

    private boolean checkHomeValidator(String formId) {
        return ADD_ADDRESS_HOME.equalsIgnoreCase(formId);
    }

    private boolean checkOfficeValidator(String formId) {
        return ADD_ADDRESS_OFFICE.equalsIgnoreCase(formId);
    }

    private void noValidatiorAvailable() {
        throw new IllegalArgumentException(NO_AVAILABLE_VALIDATOR_IS_FOUND);
    }

}
