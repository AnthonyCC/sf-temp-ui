package com.freshdirect.webapp.ajax.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
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
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class LocationHandlerServlet extends BaseJsonServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -78178069898737893L;

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
            FormDataRequest zipCodeRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
            ValidationResult validationResult = new ValidationResult();
            FormDataResponse zipCodeResponse = FormDataService.defaultService().prepareFormDataResponse(zipCodeRequest, validationResult);
            String zipCode = (String) zipCodeRequest.getFormData().get("zipCode");
            EnumServiceType dlvType = EnumServiceType.getEnum((String) zipCodeRequest.getFormData().get("addressType"));
            List<ValidationError> validationErrors = new ArrayList<ValidationError>();
            Map<String, Object> redirectMap = new HashMap<String, Object>();

            FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FD);
            Set<EnumServiceType> availableServices = serviceResult.getAvailableServices();

            if (availableServices.contains(dlvType)) {
                LocationHandlerService.getDefaultService().doSetZipCodeAction((FDSessionUser) user, result, EnumUserInfoName.DLV_ZIPCODE.getCode(), zipCode, true);
                if (EnumServiceType.CORPORATE.equals(dlvType)) {
                    redirectMap.put("redirectUrl", "/cos.jsp");
                }
                if (EnumServiceType.HOME.equals(dlvType)) {
                    redirectMap.put("redirectUrl", "currentUrl");
                }

                zipCodeResponse.getSubmitForm().setResult(redirectMap);
            } else {
                validationErrors.add(new ValidationError(new ActionError("FreshDirect does not deliver to the selected zip code")));
            }
            validationResult.setErrors(validationErrors);
            zipCodeResponse.getSubmitForm().setSuccess(validationErrors.isEmpty());

            writeResponseData(response, zipCodeResponse);
        } catch (FDResourceException e) {
            returnHttpError(500, "Failed to submit zip form action.", e);
        }
    }
}
