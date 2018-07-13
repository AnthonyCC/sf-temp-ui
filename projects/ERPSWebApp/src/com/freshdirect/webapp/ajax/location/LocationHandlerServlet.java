package com.freshdirect.webapp.ajax.location;

import java.text.MessageFormat;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.location.service.DeliveryAddressService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.FDURLUtil;

public class LocationHandlerServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -78178069898737893L;

    private static final Logger LOGGER = LoggerFactory.getInstance(LocationHandlerServlet.class);

    private static final String ERROR_MESSAGE = "Failed to execute action:{0} with user:{1}";

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
        FormDataRequest locationRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
        PageAction pageAction = FormDataService.defaultService().getPageAction(locationRequest);
        ValidationResult validationResult = new ValidationResult();

        try {
            if (pageAction != null) {
                switch (pageAction) {
                    case SET_ZIP_CODE:
                        validationResult = setZipCode(user, locationRequest);
                        break;
                    case CHECK_ZIP_CODE:
                        validationResult = checkZipCode(user, locationRequest);
                        break;
                    case FUTURE_ZONE_NOTIFICATION:
                        validationResult = futureZoneNotification(user, locationRequest);
                        break;
                    case SELECT_ADDRESS:
                        validationResult = setDeliveryAddress(user, locationRequest, request.getSession());
                        break;
                    default:
                        break;
                }
            }
            FormDataResponse locationResponse = FormDataService.defaultService().prepareFormDataResponse(locationRequest, validationResult);
            writeResponseData(response, locationResponse);
        } catch (FDResourceException e) {
            String errorMessage = MessageFormat.format(ERROR_MESSAGE, pageAction, user.getUserId());
            LOGGER.error(errorMessage, e);
            returnHttpError(500, errorMessage, e);
        } catch (JspException e) {
            String errorMessage = MessageFormat.format(ERROR_MESSAGE, pageAction, user.getUserId());
            LOGGER.error(errorMessage, e);
            returnHttpError(500, errorMessage, e);
        } catch (RedirectToPage e) {
            String errorMessage = MessageFormat.format(ERROR_MESSAGE, pageAction, user.getUserId());
            LOGGER.error(errorMessage, e);
            returnHttpError(500, errorMessage, e);
        }
    }

    private ValidationResult setZipCode(FDUserI user, FormDataRequest locationRequest) throws FDResourceException {
        ValidationResult validationResult = new ValidationResult();
        ActionResult result = new ActionResult();
        String zipCode = FormDataService.defaultService().get(locationRequest, "zipCode");
        EnumServiceType dlvType = EnumServiceType.getEnum(FormDataService.defaultService().get(locationRequest, "addressType"));

        FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FD);
        Set<EnumServiceType> availableServices = serviceResult.getAvailableServices();

        if (availableServices.contains(dlvType)) {
            LocationHandlerService.getDefaultService().doSetZipCodeAction((FDSessionUser) user, result, EnumUserInfoName.DLV_ZIPCODE.getCode(), zipCode, true);
        } else {
            result.addError(new ActionError("FreshDirect does not deliver to the selected zip code:" + zipCode));
        }

        if (result.isSuccess()) {
            validationResult.putResult("redirectUrl", FDURLUtil.getLandingPageUrl(EnumServiceType.CORPORATE.equals(dlvType)));
        }

        if (result.isFailure()) {
            for (ActionError error : result.getErrors()) {
                validationResult.addError(new ValidationError(error));
            }
        }

        return validationResult;
    }

    private ValidationResult checkZipCode(FDUserI user, FormDataRequest locationRequest) throws FDResourceException {
        ValidationResult validationResult = new ValidationResult();
        ActionResult result = new ActionResult();
        String zipCode = FormDataService.defaultService().get(locationRequest, "zipCode");
        EnumServiceType dlvType = EnumServiceType.getEnum(FormDataService.defaultService().get(locationRequest, "addressType"));

        FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FD);
        Set<EnumServiceType> availableServices = serviceResult.getAvailableServices();

        if (!availableServices.contains(dlvType)) {
        	result.addError(new ActionError("FreshDirect does not deliver to the selected zip code:" + zipCode));
        }
        
        if (result.isFailure()) {
            for (ActionError error : result.getErrors()) {
                validationResult.addError(new ValidationError(error));
            }
        }

        return validationResult;
    }
    
    private ValidationResult futureZoneNotification(FDUserI user, FormDataRequest locationRequest) throws FDResourceException {
        ValidationResult validationResult = new ValidationResult();
        ActionResult result = new ActionResult();
        String zipCode = FormDataService.defaultService().get(locationRequest, "zipCode");
        EnumServiceType dlvType = EnumServiceType.getEnum(FormDataService.defaultService().get(locationRequest, "addressType"));
        String email = FormDataService.defaultService().get(locationRequest, "email");

        LocationHandlerService.getDefaultService().doFutureZoneNotificationAction((FDSessionUser) user, email, dlvType, zipCode, result, true);

        if (result.isFailure()) {
            for (ActionError error : result.getErrors()) {
                validationResult.addError(new ValidationError(error));
            }
        }

        return validationResult;
    }

    private ValidationResult setDeliveryAddress(FDUserI user, FormDataRequest locationRequest, HttpSession session) throws FDResourceException, JspException, RedirectToPage {
        ValidationResult validationResult = new ValidationResult();

        String deliveryAddressId = FormDataService.defaultService().get(locationRequest, "selectAddressList");
        String contactNumber = NVL.apply(FormDataService.defaultService().get(locationRequest, "contactNumber"), "");

        boolean isOriginCorporateUser = user.isCorporateUser();

        validationResult.addErrors(DeliveryAddressService.defaultService().selectDeliveryAddressMethod(deliveryAddressId, contactNumber,
                PageAction.SELECT_DELIVERY_ADDRESS_METHOD.actionName, session, user));

        boolean isUpdatedCorporateUser = user.isCorporateUser();

            if (validationResult.getErrors().isEmpty()) {
            if (isOriginCorporateUser ^ isUpdatedCorporateUser) {
                validationResult.putResult("redirectUrl", FDURLUtil.getLandingPageUrl(isUpdatedCorporateUser));
            }
        }

        return validationResult;
    }

}
