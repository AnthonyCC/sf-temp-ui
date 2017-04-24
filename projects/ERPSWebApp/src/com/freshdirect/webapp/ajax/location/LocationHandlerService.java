package com.freshdirect.webapp.ajax.location;

import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.ServiceTypeUtil;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.ajax.modulehandling.service.ModuleHandlingService;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.location.LocationHandlerTag;

public class LocationHandlerService {

    private static final LocationHandlerService INSTANCE = new LocationHandlerService();
    private static final Logger LOGGER = LoggerFactory.getInstance(ModuleHandlingService.class);

    public static LocationHandlerService getDefaultService() {
        return INSTANCE;
    }

    private LocationHandlerService() {
    }

    public void doSetZipCodeAction(FDSessionUser user, ActionResult result, String zipCodeName, String zipCodeValue, boolean isZipPopup) throws FDResourceException {
        String zipCode = processZipCodeField(zipCodeName, zipCodeValue, result);

        if (result.isSuccess()) {
            AddressModel address = new AddressModel();
            address.setZipCode(zipCode);

            StateCounty stateCounty = FDDeliveryManager.getInstance().lookupStateCountyByZip(zipCode);
            if (stateCounty == null) {
                LOGGER.info("stateCounty is null for zip: " + zipCode);
            } else {
                address.setState(WordUtils.capitalizeFully(stateCounty.getState()));
                address.setCity(WordUtils.capitalizeFully(stateCounty.getCity()));
            }
            // no error check needed here, front end will display no delivery error if needed
            handleNewServiceResult(
                    FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(
                            zipCode,
                            (user.getUserContext() != null && user.getUserContext().getStoreContext() != null) ? user.getUserContext().getStoreContext().getEStoreId()
                                    : EnumEStoreId.FD), user);
            user.setAddress(address);
            user.setZPServiceType(address.getServiceType());
            handleNewAddressSet(user);

            if (isZipPopup) {
                user.setZipCheckPopupUsed(true);
            }

            user.updateUserState(); // based on DeliveryAddressManipulator.performSetDeliveryAddress()
            FDCustomerManager.storeUser(user.getUser());
        }
    }

    public void doFutureZoneNotificationAction(FDSessionUser user, String email, EnumServiceType deliveryType, String zipCode, ActionResult result, boolean isZipPopup)
            throws FDResourceException {
        if (deliveryType == null) {
            deliveryType = user.getSelectedServiceType();
        }
        if (zipCode == null) {
            zipCode = user.getZipCode();
        }

        if (!zipCode.matches(LocationHandlerTag.ZIP_CODE_PATTERN) || zipCode.equals("00000")) {
            result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_ZIP_CODE);
        }

        if (!result.isFailure() && EmailUtil.isValidEmailAddress(email)) {
            FDDeliveryManager.getInstance().saveFutureZoneNotification(email, zipCode, deliveryType);
            user.setFutureZoneNotificationEmailSentForCurrentAddress(true);
            if (isZipPopup) {
                user.setZipCheckPopupUsed(true);
                FDCustomerManager.storeUser(user.getUser());
            }
        } else {
            result.addError(true, LocationHandlerTag.FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER, SystemMessageList.MSG_EMAIL_FORMAT);
        }
    }

    private String processZipCodeField(String zipCodeFieldName, String zipCodeFieldValue, ActionResult result) {
        // String zipCode = processRequiredField(EnumUserInfoName.DLV_ZIPCODE, result);
        String zipCode = processRequiredField(zipCodeFieldName, zipCodeFieldValue, result);
        if (!zipCode.matches(LocationHandlerTag.ZIP_CODE_PATTERN) || zipCode.equals("00000")) {
            result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_ZIP_CODE);
        }
        return zipCode;
    }

    private String processRequiredField(String fieldName, String fieldValue, ActionResult result) {
        processFieldValue(fieldValue);
        if ("".equals(fieldValue)) {
            result.addError(true, fieldName, SystemMessageList.MSG_REQUIRED);
        }
        return fieldValue;
    }

    private String processFieldValue(String value) {
        return NVL.apply(value, "").trim();
    }

    private void handleNewServiceResult(FDDeliveryServiceSelectionResult serviceResult, FDSessionUser user) throws FDResourceException {

        Set<EnumServiceType> availableServices = serviceResult.getAvailableServices();
        user.setAvailableServices(availableServices);

        if (user.getSelectedServiceType() == EnumServiceType.PICKUP) {
            handleNewServiceType(ServiceTypeUtil.getPreferedServiceType(availableServices), user);
        }
    }

    private void handleNewAddressSet(FDSessionUser user) {
        user.setMoreInfoPopupShownForCurrentAddress(false); // reset for new address
        user.setFutureZoneNotificationEmailSentForCurrentAddress(false);
    }

    private boolean handleNewServiceType(EnumServiceType serviceType, FDUserI user) {
        boolean needToUpdate = (serviceType != user.getSelectedServiceType());
        if (needToUpdate) {
            user.setSelectedServiceType(serviceType);
            user.setZPServiceType(serviceType); // added as part of APPDEV-6036. We are updating the zone pricing service type to be in sync with user selected service type
        }
        return needToUpdate;
    }
}
