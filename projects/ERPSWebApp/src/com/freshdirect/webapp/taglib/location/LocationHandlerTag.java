package com.freshdirect.webapp.taglib.location;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.ServiceTypeUtil;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotModel;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.ajax.location.LocationHandlerService;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class LocationHandlerTag extends SimpleTagSupport {

    public static final String SELECTED_ADDRESS_ATTR = "locationHandlerSelectedAddress";
    public static final String ALL_PICKUP_DEPOTS_ATTR = "locationHandlerAllPickupDepots";
    public static final String SELECTED_PICKUP_DEPOT_ID_ATTR = "locationHandlerSelectedPickDepotId";
    public static final String ACTION_RESULT_ATTR = "locationHandlerActionResult";
    public static final String SERVER_ERROR_ATTR = "locationHandlerServerErrorResult";
    public static final String DISABLED_ATTR = "locationHandlerDisabled";
    public static final String SERVICE_TYPE_MODIFICATION_ENABLED = "locationHandlerServiceTypeModificationEnabled";
    public static final String FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER = "futureZoneNotificationEmail";
    public static final String FUTURE_ZONE_NOTIFICATION_DLVTYPE_PARAMETER = "futureZoneNotificationDlvType";
    public static final String FUTURE_ZONE_NOTIFICATION_ZIPCODE_PARAMETER = "futureZoneNotificationZipCode";
    public static final String ZIP_CODE_PARAMETER = "zipCode";
    public static final String ZIP_CODE_PATTERN = "\\d{5}";

    public static boolean isDeliveryZone = false;

    private static final Logger LOGGER = LoggerFactory.getInstance(LocationHandlerTag.class);

    private String action;

    @Override
    public void doTag() {
        ActionResult result = new ActionResult();
        PageContext ctx = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        HttpServletResponse response = (HttpServletResponse) ctx.getResponse();
        FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);

        String zipCodeName = EnumUserInfoName.DLV_ZIPCODE.getCode();
        String zipCodeValue = request.getParameter(zipCodeName);

        try {

            if (user == null && !"ifDeliveryZone".equalsIgnoreCase(action)) {
                ctx.setAttribute(SERVER_ERROR_ATTR, "Server error occured. Sorry for your inconvinience, please refresh this page.");

            } else {
                LOGGER.debug("action: " + action);

                String url = request.getRequestURI().toLowerCase();

                if ("selectAddress".equalsIgnoreCase(action)) {
                    LocationHandlerService.getDefaultService().doSelectAddressAction(user, result, request, response);
                } else if ("setZipCode".equalsIgnoreCase(action)) {
                    LocationHandlerService.getDefaultService().doSetZipCodeAction(user, result, zipCodeName, zipCodeValue, false);
                } else if ("setMoreInfo".equalsIgnoreCase(action)) {
                    doSetMoreInfoAction(user, result, request, ctx);
                } else if ("futureZoneNotification".equalsIgnoreCase(action)) {
                    String email = request.getParameter(FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER);
                    EnumServiceType dlvType = EnumServiceType.getEnum(request.getParameter(FUTURE_ZONE_NOTIFICATION_DLVTYPE_PARAMETER));
                    String zipCode = request.getParameter(FUTURE_ZONE_NOTIFICATION_ZIPCODE_PARAMETER);
                    LocationHandlerService.getDefaultService().doFutureZoneNotificationAction(user, email, dlvType, zipCode, result, false);
                } else if ("ifDeliveryZone".equalsIgnoreCase(action)) {

                    // find out if Fresh Direct delivers to this zip code
                    isDeliveryZone = hasFdxService(zipCodeName, zipCodeValue, result);

                } else if ("futureZoneNotificationFdx".equalsIgnoreCase(action)) {
                    String email = request.getParameter("email");
                    doFutureZoneNotificationActionFdx(email, zipCodeName, zipCodeValue, user, result);
                }
                doExportAttributes(url, user, ctx);
            }

        } catch (Exception e) {
            LOGGER.error(e);
            ctx.setAttribute(SERVER_ERROR_ATTR, e.getMessage());
        }

        ctx.setAttribute(ACTION_RESULT_ATTR, result);
    }

    public static boolean hasFdService(String zipCode) {
        try {
            FDDeliveryServiceSelectionResult result = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FD);
            Set<EnumServiceType> availServices = result.getAvailableServices();

            // remove pickup for this check - APPDEV-5901
            availServices.remove(EnumServiceType.PICKUP);

            if (!availServices.isEmpty()) {
                return true;
            }
        } catch (FDResourceException e) {
            // LOGGER.debug(e);
        }

        return false;
    }

    public static boolean hasFdxService(String zipCode) {
        try {
            FDDeliveryServiceSelectionResult result = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FDX);
            Set<EnumServiceType> availServices = result.getAvailableServices();

            // remove pickup
            availServices.remove(EnumServiceType.PICKUP);

            if (!availServices.isEmpty()) {
                return true;
            }
        } catch (FDResourceException e) {
            LOGGER.debug(e);
        }

        return false;
    }

    /**
     * based on SiteAccessControllerTag.populate(), validate() and checkByAddress()
     * 
     * @param user
     * @param request
     * @param ctx
     */
    private void doSetMoreInfoAction(FDSessionUser user, ActionResult result, HttpServletRequest request, PageContext ctx) throws FDResourceException {

        AddressModel address = new AddressModel();
        address.setZipCode(processZipCodeField(EnumUserInfoName.DLV_ZIPCODE.getCode(), request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), result));
        address.setAddress1(processRequiredField(EnumUserInfoName.DLV_ADDRESS_1.getCode(), request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), result));
        address.setApartment(processFieldValue(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode())));
        address.setCity(processRequiredField(EnumUserInfoName.DLV_CITY.getCode(), request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), result));
        address.setState(processRequiredField(EnumUserInfoName.DLV_STATE.getCode(), request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), result));

        if (result.isSuccess()) {
            address = AddressUtil.scrubAddress(address, true, result);
        }
        if (result.isSuccess()) {
            try {
                handleNewServiceResult(FDDeliveryManager.getInstance().getDeliveryServicesByAddress(address), user);
                user.setAddress(address);
                handleNewAddressSet(user);
            } catch (FDInvalidAddressException e) {
                result.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(),
                        MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE_ZIP_CHECK, new Object[] { UserUtil.getCustomerServiceContact(request) }));
            }
        }
    }

    private String processFieldValue(String value) {
        return NVL.apply(value, "").trim();
    }

    private String processRequiredField(String fieldName, String fieldValue, ActionResult result) {
        processFieldValue(fieldValue);
        if ("".equals(fieldValue)) {
            result.addError(true, fieldName, SystemMessageList.MSG_REQUIRED);
        }
        return fieldValue;
    }

    private String processZipCodeField(String zipCodeFieldName, String zipCodeFieldValue, ActionResult result) {
        String zipCode = processRequiredField(zipCodeFieldName, zipCodeFieldValue, result);
        if (!zipCode.matches(ZIP_CODE_PATTERN) || zipCode.equals("00000")) {
            result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_ZIP_CODE);
        }
        return zipCode;
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

    /**
     * based on DeliveryAddressManipulator logic
     * 
     * @param user
     */
    private boolean handleNewServiceType(EnumServiceType serviceType, FDUserI user) {
        boolean needToUpdate = (serviceType != user.getSelectedServiceType());
        if (needToUpdate) {
            user.setSelectedServiceType(serviceType);
            user.setZPServiceType(serviceType); // added as part of APPDEV-6036. We are updating the zone pricing service type to be in sync with user selected service type
        }
        return needToUpdate;
    }

    /**
     * based on SiteAccessControllerTag.saveEmail()
     * 
     * @param result
     * @param user
     */
    private void doFutureZoneNotificationActionFdx(String email, String zipCodeName, String zipCodeFieldValue, FDSessionUser user, ActionResult result) throws FDResourceException {
        // String email = request.getParameter("email");

        String zipCode = processZipCodeField(zipCodeName, zipCodeFieldValue, result);

        if (EmailUtil.isValidEmailAddress(email)) {
            FDDeliveryManager.getInstance().saveFutureZoneNotification(email, zipCode, user.getSelectedServiceType());

            user.setFutureZoneNotificationEmailSentForCurrentAddress(true);
        } else {
            result.addError(true, FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER, SystemMessageList.MSG_EMAIL_FORMAT);
        }
    }

    private boolean hasFdxService(String zipCodeName, String zipCodeFieldValue, ActionResult result) {
        String zipCode = processZipCodeField(zipCodeName, zipCodeFieldValue, result);

        try {
            FDDeliveryServiceSelectionResult deliveryResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FDX);
            Set<EnumServiceType> availServices = deliveryResult.getAvailableServices();
            if (!availServices.isEmpty()) {
                return true;
            }
        } catch (FDResourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    private void doExportAttributes(String uri, FDSessionUser user, PageContext ctx) throws FDResourceException {

        AddressModel selectedAddress = user.getSelectedAddress();
        ctx.setAttribute(SELECTED_ADDRESS_ATTR, selectedAddress);

        if (selectedAddress instanceof ErpDepotAddressModel) {
            ctx.setAttribute(SELECTED_PICKUP_DEPOT_ID_ATTR, ((ErpDepotAddressModel) selectedAddress).getLocationId());
        }

        List<FDDeliveryDepotLocationModel> allPickupDepots = new ArrayList<FDDeliveryDepotLocationModel>();
        for (FDDeliveryDepotModel pickupDepot : FDDeliveryManager.getInstance().getPickupDepots()) {
            if (!"HAM".equalsIgnoreCase(pickupDepot.getDepotCode()) && !pickupDepot.isDeactivated()) { // based on i_pickup_depot_locations.jspf
                for (FDDeliveryDepotLocationModel location : pickupDepot.getLocations()) {
                    allPickupDepots.add(location);
                }
            }
        }
        ctx.setAttribute(ALL_PICKUP_DEPOTS_ATTR, allPickupDepots);

        if (uri.contains("/checkout/") || uri.contains("/login/")) {
            ctx.setAttribute(DISABLED_ATTR, true);
        }

        ctx.setAttribute(SERVICE_TYPE_MODIFICATION_ENABLED, isServiceTypeModificationEnabled(user));
    }

    private boolean isServiceTypeModificationEnabled(FDSessionUser user) {
        return user.isUserCreatedInThisSession() && (user.getLevel() < FDUserI.RECOGNIZED);
    }

    public static String formatAddressText(AddressModel address) {
        return StringUtil.concatWithDelimiters(new String[] { address.getAddress1(), address.getAddress2(), address.getCity(), address.getState() },
                new String[] { " ", ", ", ", " });
    }

    public static String formatAddressTextWithZip(AddressModel address) {
        return StringUtil.concatWithDelimiters(new String[] { address.getAddress1(), address.getAddress2(), address.getCity(), address.getState(), address.getZipCode() },
                new String[] { " ", ", ", ", ", " " });
    }

    public static String formatAddressShortText(AddressModel address) {
        return StringUtil.concatWithDelimiters(new String[] { address.getCity(), address.getState() }, new String[] { ", " });
    }

    public void setAction(String action) {
        this.action = action;
    }

}
