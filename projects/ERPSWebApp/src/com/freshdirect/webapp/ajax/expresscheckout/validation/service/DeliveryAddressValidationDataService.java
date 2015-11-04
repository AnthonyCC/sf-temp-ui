package com.freshdirect.webapp.ajax.expresscheckout.validation.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.taglib.fdstore.UnattendedDeliveryTag;

public class DeliveryAddressValidationDataService implements FormValidationService {

    public static final String ADD_ADDRESS_HOME = "addaddress_home";
    public static final String ADD_ADDRESS_OFFICE = "addaddress_office";
    private static final String NO_AVAILABLE_VALIDATOR_IS_FOUND = "No available validator is found.";
    private static final String STREET_ADDRESS_KEY = "street_address";
    private static final String CITY_KEY = "city";
    private static final String APARTMENT_KEY = "apartment";
    private static final String STATE_KEY = "state";
    private static final String ZIPCODE_KEY = "zip";

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

    public boolean prepareAndValidateForUnattendedCheck(final FormDataRequest addressRequestData) {
        addressRequestData.setEdited(null);
        final Map<String, String> validatedData = assemblerService.assembleValidatedData(addressRequestData);
        boolean result = false;

        if ((validationService.validateUnattendedDelivery(validatedData)).isEmpty()) {
            ErpAddressModel address = new ErpAddressModel();
            address.setAddress1(validatedData.get(STREET_ADDRESS_KEY));
            address.setCity(validatedData.get(CITY_KEY));
            address.setZipCode(validatedData.get(ZIPCODE_KEY));
            address.setState(validatedData.get(STATE_KEY));
            address.setApartment(validatedData.get(APARTMENT_KEY));
            result = UnattendedDeliveryTag.checkUnattendedDelivery(address);
        }

        return result;

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
