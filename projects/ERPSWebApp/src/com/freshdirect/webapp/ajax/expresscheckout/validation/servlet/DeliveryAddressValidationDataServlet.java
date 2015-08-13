package com.freshdirect.webapp.ajax.expresscheckout.validation.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.DeliveryAddressValidationDataService;

public class DeliveryAddressValidationDataServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -7582639712245761241L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        try {
            FormDataRequest deliveryAddressRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
            ValidationResult validationResult = new ValidationResult();
            validationResult.setFdform(deliveryAddressRequest.getFormId());
            validationResult.getErrors().addAll(DeliveryAddressValidationDataService.defaultService().prepareAndValidate(deliveryAddressRequest));
            FormDataResponse deliveryAddressResponse = createDeliveryAddressResponse(validationResult);
            writeResponseData(response, deliveryAddressResponse);
        } catch (Exception e) {
            BaseJsonServlet.returnHttpError(500, "Error while validate delivery address for user " + user.getUserId());
        }
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.SIGNED_IN;
    }

    private FormDataResponse createDeliveryAddressResponse(ValidationResult validationResult) {
        final FormDataResponse deliveryAddressResponse = new FormDataResponse();
        deliveryAddressResponse.setValidationResult(validationResult);
        return deliveryAddressResponse;
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

}
