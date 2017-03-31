package com.freshdirect.webapp.ajax.location;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class LocationAvailabilityNotificationServlet extends BaseJsonServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 7398312005454467711L;

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUser.GUEST;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        try {
            ActionResult result = new ActionResult();
            FormDataRequest emailNotificationRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
            ValidationResult validationResult = new ValidationResult();
            FormDataResponse emailNotificationResponse = FormDataService.defaultService().prepareFormDataResponse(emailNotificationRequest, validationResult);
            String zipCode = (String) emailNotificationRequest.getFormData().get("zipCode");
            String email = (String) emailNotificationRequest.getFormData().get("email");
            EnumServiceType dlvType = EnumServiceType.getEnum((String) emailNotificationRequest.getFormData().get("addressType"));
            List<ValidationError> validationErrors = new ArrayList<ValidationError>();

            LocationHandlerService.getDefaultService().doFutureZoneNotificationAction((FDSessionUser) user, email, dlvType, zipCode, result, true);

            if (result.isFailure()) {
                for (ActionError actionError : result.getErrors()) {
                    validationErrors.add(new ValidationError(actionError));
                }
            }

            validationResult.setErrors(validationErrors);
            emailNotificationResponse.getSubmitForm().setSuccess(validationErrors.isEmpty());

            writeResponseData(response, emailNotificationResponse);
        } catch (FDResourceException e) {
            returnHttpError(500, "Failed to submit zip form action.", e);
        }
    }

}
