/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateAddressException;
import com.freshdirect.customer.ErpDuplicateDisplayNameException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.delivery.sms.SMSAlertManager;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.fdstore.RegistrationAction;
import com.freshdirect.webapp.checkout.DeliveryAddressManipulator;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag;
import com.freshdirect.webapp.util.AccountUtil;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class RegistrationControllerTag extends AbstractControllerTag implements SessionName { // AddressName,

    private static Category LOGGER = LoggerFactory.getInstance(RegistrationControllerTag.class);

    private String fraudPage;
    private String statusChangePage;
    private boolean signupFromCheckout;
    private int registrationType;
    private String source;

    private ErpAddressModel lastSavedAddressModel;

    public void setFraudPage(String s) {
        this.fraudPage = s;
    }

    public void setStatusChangePage(String s) {
        this.statusChangePage = s;
    }

    public void setSignupFromCheckout(boolean b) {
        this.signupFromCheckout = b;
    }

    public void setRegistrationType(int registrationType) {
        this.registrationType = registrationType;
    }

    public ErpAddressModel getLastSavedAddressModel() {
        return lastSavedAddressModel;
    }

    @Override
    protected boolean performAction(HttpServletRequest request, ActionResult actionResult) {
        final HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        String actionName = this.getActionName();
        try {
            String source = (request.getParameter("source") == null) ? "" : (String) request.getParameter("source");
            HttpSession session = pageContext.getSession();
            FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
            FDCartModel cart = null;
            if (user != null)
                cart = user.getShoppingCart();

            TimeslotEvent event = null;
            if (user != null) {
                event = new TimeslotEvent((user.getApplication() != null) ? user.getApplication().getCode() : "", (cart != null) ? cart.isDlvPassApplied() : false,
                        (cart != null) ? cart.getDeliverySurcharge() : 0.00, (cart != null) ? cart.isDeliveryChargeWaived() : false, (cart.getZoneInfo() != null) ? cart
                                .getZoneInfo().isCtActive() : false, user.getPrimaryKey(), EnumCompanyCode.fd.name());
            }

            if ("register".equalsIgnoreCase(actionName)) {
                RegistrationAction ra = new RegistrationAction(this.registrationType);

                HttpContext ctx = new HttpContext(this.pageContext.getSession(), (HttpServletRequest) this.pageContext.getRequest(),
                        (HttpServletResponse) this.pageContext.getResponse());

                ra.setHttpContext(ctx);
                ra.setResult(actionResult);
                ra.setFraudPage(this.fraudPage);
                ra.setSignupFromCheckout(this.signupFromCheckout);
                ra.setStatusChangePage(this.statusChangePage);
                ra.setSuccessPage(this.getSuccessPage());

                ra.execute();
                CmRegistrationTag.setPendingRegistrationEvent(session);
                this.setSuccessPage(ra.getSuccessPage()); // reset if changed.

            } else if ("registerEx".equalsIgnoreCase(actionName)) {
                /*
                 * this.pageContext.getSession().removeAttribute("LITESIGNUP_COMPLETE"); // if(session.getAttribute("REFERRALNAME") != null ){ if(session.getAttribute("CLICKID") !=
                 * null ) { if(!"done".equals(request.getParameter("submission"))) { actionResult.addError(new ActionError("Dummy","Dummy")); return true; }
                 */

                String successPage = AccountServiceFactory.getService(source).register(user, this.pageContext, actionResult, this.registrationType);
                if (successPage != null) {
                    this.setSuccessPage(successPage);
                    this.setAjax(true);
                    /* } */
                }
            } else if ("addDeliveryAddressEx".equalsIgnoreCase(actionName)) {
                DeliveryAddressManipulator m = new DeliveryAddressManipulator(request, response, actionResult, actionName);
                lastSavedAddressModel = m.performAddDeliveryAddress();

            } else if ("addDeliveryAddress".equalsIgnoreCase(actionName)) {
                DeliveryAddressManipulator m = new DeliveryAddressManipulator(request, response, actionResult, actionName);
                lastSavedAddressModel = m.performAddDeliveryAddress();
                FDIdentity identity = getIdentity();
                FDCustomerInfo customerInfo = FDCustomerManager.getCustomerInfo(identity);
                // Made changed for address checking
                if (actionResult.isSuccess()) {
                    ActionResult result = new ActionResult();
                    ErpAddressModel erpAddress = checkDeliveryAddressInForm(request, result, session);
                    LOGGER.debug("RegistrationControllerTag :: addDeliveryAddress ===> If no address error send email");
                    EnumEStoreId estoreId = EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId()));
                    FDCustomerManager.sendEmail(FDEmailFactory.getInstance().createShippingAddressAdditionEmail(customerInfo, erpAddress, estoreId));
                }

            } else if ("editDeliveryAddress".equalsIgnoreCase(actionName)) {
                // this.performEditDeliveryAddress(request, actionResult, event);
                DeliveryAddressManipulator m = new DeliveryAddressManipulator(request, response, actionResult, actionName);
                m.performEditDeliveryAddress(event);
                // Security Enhancements
                FDIdentity identity = getIdentity();
                FDCustomerInfo customerInfo = FDCustomerManager.getCustomerInfo(identity);
                // Made changed for address checking
                if (actionResult.isSuccess()) {
                    ActionResult result = new ActionResult();
                    ErpAddressModel erpAddress = checkDeliveryAddressInForm(request, result, session);
                    LOGGER.debug("RegistrationControllerTag :: editDeliveryAddress ===> If no address error send email");
                    EnumEStoreId estoreId = EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId()));
                    FDCustomerManager.sendEmail(FDEmailFactory.getInstance().createShippingAddressChangeEmail(customerInfo, erpAddress, estoreId));
                }

                // APPDEV-4177 - Code changes to avoid sending multiple mails : Start
            } else if ("editDeliveryAddressForUnattendZone".equalsIgnoreCase(actionName)) {
                // this.performEditDeliveryAddress(request, actionResult, event);
                DeliveryAddressManipulator m = new DeliveryAddressManipulator(request, response, actionResult, actionName);
                m.performEditDeliveryAddress(event);
                // APPDEV-4177 - Code changes to avoid sending multiple mails : End

            } else if ("deleteDeliveryAddress".equalsIgnoreCase(actionName)) {
                // this.performDeleteDeliveryAddress(request, actionResult, event);
                DeliveryAddressManipulator m = new DeliveryAddressManipulator(request, response, actionResult, actionName);
                m.performDeleteDeliveryAddress(event);

            } else if ("changeUserID".equalsIgnoreCase(actionName)) {
                this.performChangeUserID(request, actionResult);
                CmRegistrationTag.setPendingRegistrationEvent(session);

            } else if ("changePassword".equalsIgnoreCase(actionName)) {
                this.performChangePassword(request, actionResult);

            } else if ("changeContactInfo".equalsIgnoreCase(actionName)) {
                this.performChangeContactInfo(request, actionResult);

            } else if ("changeContactNames".equals(actionName)) {
                this.changeContactInfoName(request, actionResult);
            } else if ("changeEmailPreference".equalsIgnoreCase(actionName)) {
                this.performChangeEmailPreference(request, actionResult);

            } else if ("changeMailPhonePreference".equalsIgnoreCase(actionName)) {
                this.changeMailPhonePreference(request, actionResult);

            } else if ("changeEmailPreferenceLevel".equalsIgnoreCase(actionName)) {
                this.changeEmailPreferenceLevel(request, actionResult);
            } else if ("mobilepreferences".equals(actionName)) {
                // Save mobile preferences
                this.changeMobilePreferences(request, actionResult);
            } else if ("otherpreferences".equals(actionName)) {
                // Save mobile preferences
                this.changeOtherPreferences(request, actionResult);
            } else if ("ordermobilepref".equals(actionName)) {
                // coming from order receipt screen. store all of them together.
                this.storeMobilePreferences(request, actionResult);
            } else if ("changeDisplayName".equals(actionName)) {
                // coming from order receipt screen. store all of them together.
                this.performChangeDisplayName(request, actionResult);
            } else if ("ordersmsalerts".equals(actionName)) {
                // coming from order receipt screen. store all of them together.
                this.addAllSmsAlerts(request, actionResult);
            } else if ("disconnectSocialAccount".equals(actionName)) {
                // disconnect social account from user's profile.
                this.performDisconnectSocialAccount(request, actionResult);
            }

        } catch (Exception ex) {
            LOGGER.error("Error performing action " + actionName, ex);
            actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
        }
        return true;
    }

    private void storeMobilePreferences(HttpServletRequest request, ActionResult actionResult) {
        HttpSession session = pageContext.getSession();
        String orderNumber = (String) session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
        session.removeAttribute("SMSSubmission" + orderNumber);
        String submitbutton = request.getParameter("submitbutton");
        if ("update".equals(submitbutton)) {
            String text_offers = request.getParameter("text_offers");
            String text_delivery = request.getParameter("text_delivery");
            String mobile_number = request.getParameter("mobile_number");
            String go_green = request.getParameter("go_green");

            if ("Y".equals(text_offers) || "Y".equals(text_delivery)) {
                if (mobile_number == null || mobile_number.length() == 0) {
                    actionResult.addError(true, "mobile_number", SystemMessageList.MSG_REQUIRED);
                    return;
                }
                PhoneNumber phone = new PhoneNumber(mobile_number);
                if (!phone.isValid()) {
                    actionResult.addError(true, "mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
                    return;
                }
            } else if (mobile_number != null && mobile_number.length() != 0) {
                if (!"Y".equals(text_offers) && !"Y".equals(text_delivery)) {
                    actionResult.addError(true, "text_option", "Please select your text messaging preferences below.");
                    return;
                }
            }

            // check for the other phone
            String busphone = request.getParameter("busphone");
            String ext = request.getParameter("busphoneext");
            if (busphone == null || busphone.length() == 0) {
                actionResult.addError(true, "busphone", SystemMessageList.MSG_REQUIRED);
                return;
            }
            PhoneNumber bphone = new PhoneNumber(busphone, ext);
            if (!bphone.isValid()) {
                actionResult.addError(true, "busphone", SystemMessageList.MSG_PHONE_FORMAT);
                return;
            }

            // save it to DB
            FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
            try {
                FDCustomerManager.storeAllMobilePreferences(user.getIdentity().getErpCustomerPK(), user.getIdentity().getFDCustomerPK(), mobile_number, text_offers, text_delivery,
                        go_green, busphone, ext, user.isCorporateUser(), user.getUserContext().getStoreContext().getEStoreId());
            } catch (FDResourceException e) {
                LOGGER.error("Error from mobile preferences", e);
            }
            session.setAttribute("SMSSubmission" + orderNumber, "done");
        } else if ("remind".equals(submitbutton)) {
            // ignore
        } else {
            // no thanks
            FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
            try {
                FDCustomerManager.storeMobilePreferencesNoThanks(user.getIdentity().getErpCustomerPK());
            } catch (FDResourceException e) {
                LOGGER.error("Error from mobile preferences", e);
            }
            session.setAttribute("SMSSubmission" + orderNumber, "done");
        }
    }

    private void changeMobilePreferences(HttpServletRequest request, ActionResult actionResult) {
        String text_offers = request.getParameter("text_offers");
        String text_delivery = request.getParameter("text_delivery");
        String mobile_number = request.getParameter("mobile_number");
        String order_notices = request.getParameter("order_notices");
        String order_exceptions = request.getParameter("order_exceptions");
        String offers = request.getParameter("offers");
        String partner_messages = request.getParameter("partner_messages");
        boolean order_notice_existing = Boolean.parseBoolean(request.getParameter("order_notice_existing"));
        boolean order_exception_existing = Boolean.parseBoolean(request.getParameter("order_exception_existing"));
        boolean offer_existing = Boolean.parseBoolean(request.getParameter("offer_existing"));
        boolean partner_existing = Boolean.parseBoolean(request.getParameter("partner_existing"));
        String existingMobileNumber = request.getParameter("mobile_existing");
        boolean orderNoticeOptin = false;
        boolean orderExceptionOptin = false;
        boolean offersOptin = false;
        boolean partnerMessagesOptin = false;
        HttpSession session = pageContext.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        FDIdentity identity = user.getIdentity();
        boolean optOut = false;

        LOGGER.debug("Boolean value got for order Notices : " + order_notice_existing);

        LOGGER.debug("Value of order Notice existing in reqyest : " + request.getParameter("order_notice_existing"));

        orderNoticeOptin = "Y".equalsIgnoreCase(order_notices);
        orderExceptionOptin = "Y".equalsIgnoreCase(order_exceptions);
        offersOptin = "Y".equalsIgnoreCase(offers);
        partnerMessagesOptin = "Y".equalsIgnoreCase(partner_messages);

        boolean subscribedBefore = order_notice_existing || order_exception_existing || offer_existing || partner_existing;
        boolean subscribedBeforeNonMarketingSmsFdx = order_notice_existing || order_exception_existing;
        boolean subscribedBeforeMarketingSmsFdx = offer_existing;
        boolean subscribedNow = orderNoticeOptin || orderExceptionOptin || offersOptin || partnerMessagesOptin;
        boolean subscribedNowNonMarketingSmsFdx = orderNoticeOptin || orderExceptionOptin;
        boolean subscribedNowMarketingSmsFdx = offersOptin;

        PhoneNumber phone = new PhoneNumber(mobile_number);

        if ("Y".equalsIgnoreCase(order_notices) || "Y".equalsIgnoreCase(order_exceptions) || "Y".equalsIgnoreCase(offers) || "Y".equalsIgnoreCase(partner_messages)) {
            if (mobile_number == null || mobile_number.length() == 0) {
                actionResult.addError(true, "mobile_number", SystemMessageList.MSG_REQUIRED);
                return;
            }

            if (!phone.isValid()) {
                actionResult.addError(true, "mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
                return;
            }
        } else if (mobile_number != null && mobile_number.length() != 0) {
            if ("Y".equalsIgnoreCase(order_notices) || "Y".equalsIgnoreCase(order_exceptions) || "Y".equalsIgnoreCase(offers) || "Y".equalsIgnoreCase(partner_messages)) {

                if (!phone.isValid()) {
                    actionResult.addError(true, "mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
                    return;
                }
            }
            // commenting this logic as per ponnu based on the FDX mobile save requirements without checking notification preferences
            /*
             * else{ actionResult.addError(true, "mobile_number", SystemMessageList.MSG_OPTIN_REQ); return; }
             */
        } else if (!"Y".equalsIgnoreCase(order_notices) && !"Y".equalsIgnoreCase(order_exceptions) && !"Y".equalsIgnoreCase(offers) && !"Y".equalsIgnoreCase(partner_messages)
                && (mobile_number == null || mobile_number.length() == 0)) {
            optOut = true;
        } else {
            actionResult.addError(true, "mobile_number", SystemMessageList.MSG_REQUIRED);
            return;
        }

        boolean isSent = false;
        PhoneNumber _tmpNo = new PhoneNumber(mobile_number);
        PhoneNumber _tmpExistingNo = new PhoneNumber(existingMobileNumber);

        if (user.getUserContext().getStoreContext().getEStoreId().getContentId().equals(EnumEStoreId.FDX.name())) {
            try {
                if (subscribedNowNonMarketingSmsFdx || !_tmpNo.getPhone().equals(_tmpExistingNo.getPhone())) {
                    if (subscribedBeforeNonMarketingSmsFdx && _tmpNo.getPhone().equals(_tmpExistingNo.getPhone()))
                        isSent = true;
                    if (subscribedNowNonMarketingSmsFdx)
                        isSent = SMSAlertManager.getInstance().smsOptInNonMarketing(identity.getErpCustomerPK(), mobile_number,
                                user.getUserContext().getStoreContext().getEStoreId().getContentId());
                    else
                        optOut = true;
                } else {
                    optOut = true;
                }
                if (subscribedNowMarketingSmsFdx || !_tmpNo.getPhone().equals(_tmpExistingNo.getPhone())) {
                    if (subscribedBeforeMarketingSmsFdx && _tmpNo.getPhone().equals(_tmpExistingNo.getPhone()))
                        isSent = true;
                    if (subscribedNowMarketingSmsFdx)
                        isSent = SMSAlertManager.getInstance().smsOptInMarketing(identity.getErpCustomerPK(), mobile_number,
                                user.getUserContext().getStoreContext().getEStoreId().getContentId());

                } else {
                    optOut = true;
                }

            } catch (FDResourceException e) {
                LOGGER.error("Error from mobile preferences", e);
                actionResult.addError(true, "mobile_number", SystemMessageList.MSG_TIMEOUT_ERROR);
                return;
            }
        } else {
            try {
                if (subscribedNow || !_tmpNo.getPhone().equals(_tmpExistingNo.getPhone())) {
                    if (subscribedBefore && _tmpNo.getPhone().equals(_tmpExistingNo.getPhone())) {
                        isSent = true;
                    } else {
                        if (subscribedNow)
                            isSent = SMSAlertManager.getInstance().smsOptIn(identity.getErpCustomerPK(), mobile_number,
                                    user.getUserContext().getStoreContext().getEStoreId().getContentId());
                        else
                            optOut = true;
                    }
                } else {
                    optOut = true;
                }
            } catch (FDResourceException e) {
                LOGGER.error("Error from mobile preferences", e);
                actionResult.addError(true, "mobile_number", SystemMessageList.MSG_TIMEOUT_ERROR);
                return;
            }
        }
        if (isSent || optOut) {
            try {

                ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
                FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(user.getIdentity());

                FDCustomerManager.storeMobilePreferences(identity.getErpCustomerPK(), identity.getFDCustomerPK(), mobile_number, text_offers, text_delivery, order_notices,
                        order_exceptions, offers, partner_messages, user.getUserContext().getStoreContext().getEStoreId());

                if (user.getUserContext().getStoreContext().getEStoreId().getContentId().equals(EnumEStoreId.FD.name())) {
                    if (subscribedNow) {
                        FDCustomerManager.storeSmsPreferenceFlag(identity.getFDCustomerPK(), "Y", user.getUserContext().getStoreContext().getEStoreId());
                    } else {
                        FDCustomerManager.storeSmsPreferenceFlag(identity.getFDCustomerPK(), null, user.getUserContext().getStoreContext().getEStoreId());
                    }
                } else {
                    if (subscribedBeforeNonMarketingSmsFdx || subscribedNowMarketingSmsFdx) {
                        FDCustomerManager.storeSmsPreferenceFlag(identity.getFDCustomerPK(), "Y", user.getUserContext().getStoreContext().getEStoreId());
                    } else {
                        FDCustomerManager.storeSmsPreferenceFlag(identity.getFDCustomerPK(), null, user.getUserContext().getStoreContext().getEStoreId());
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error from mobile preferences", e);
            }
        } else {
            actionResult.addError(new ActionError("send_sms_failure", SystemMessageList.MSG_SMS_ERROR));
        }
    }

    private void changeOtherPreferences(HttpServletRequest request, ActionResult actionResult) {
        String go_green = request.getParameter("go_green");
        HttpSession session = pageContext.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        try {
            FDCustomerManager.storeGoGreenPreferences(user.getIdentity().getErpCustomerPK(), go_green);
        } catch (FDResourceException e) {
            LOGGER.error("Error from mobile preferences", e);
        }
    }

    protected void performDeleteDeliveryAddress(HttpServletRequest request, ActionResult actionResult, TimeslotEvent event) throws FDResourceException {
        String shipToAddressId = request.getParameter("deleteShipToAddressId");
        AddressUtil.deleteShipToAddress(getIdentity(), shipToAddressId, actionResult, request);
        // check that if this address had any outstanding reservations.
        HttpSession session = pageContext.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        FDReservation reservation = user.getReservation();
        if (reservation != null) {
            reservation = FDCustomerManager.validateReservation(user, reservation, event);
            user.setReservation(reservation);
            session.setAttribute(USER, user);
            if (reservation == null) {
                session.setAttribute(REMOVED_RESERVATION, Boolean.TRUE);
            }
        }
    }

    // [segabor] refactored from performEditDeliveryAddress and performAddDeliveryAddess
    public static ErpAddressModel checkDeliveryAddressInForm(HttpServletRequest request, ActionResult actionResult, HttpSession session) throws FDResourceException {

        AddressForm addressForm = new AddressForm();
        addressForm.populateForm(request);
        addressForm.validateForm(actionResult);
        if (!actionResult.isSuccess())
            return null;

        AddressModel deliveryAddress = addressForm.getDeliveryAddress();
        deliveryAddress.setServiceType(addressForm.getDeliveryAddress().getServiceType());
        DeliveryAddressValidator validator = new DeliveryAddressValidator(deliveryAddress);

        if (!validator.validateAddress(actionResult)) {
            return null;
        }

        AddressModel scrubbedAddress = validator.getScrubbedAddress(); // get 'normalized' address
        FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(scrubbedAddress.getZipCode());
        boolean isEBTAccepted = null != serviceResult ? serviceResult.isEbtAccepted() : false;
        if (validator.isAddressDeliverable()) {
            FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
            isEBTAccepted = isEBTAccepted && (user.getOrderHistory().getUnSettledEBTOrderCount() <= 0) && !user.hasEBTAlert();
            if (user.isPickupOnly() && user.getOrderHistory().getValidOrderCount() == 0) {
                //
                // now eligible for home/corporate delivery and still not placed an order.
                //
                user.setSelectedServiceType(scrubbedAddress.getServiceType());
                // Added the following line for zone pricing to keep user service type up-to-date.
                user.setZPServiceType(scrubbedAddress.getServiceType());
                user.setZipCode(scrubbedAddress.getZipCode());
                user.setEbtAccepted(isEBTAccepted);
                FDCustomerManager.storeUser(user.getUser());
                session.setAttribute(USER, user);
            } else {
                // Already is a home or a corporate customer.
                if (user.getOrderHistory().getValidOrderCount() == 0) {
                    // check if customer has no order history.
                    user.setSelectedServiceType(scrubbedAddress.getServiceType());
                    // Added the following line for zone pricing to keep user service type up-to-date.
                    user.setZPServiceType(scrubbedAddress.getServiceType());
                    user.setZipCode(scrubbedAddress.getZipCode());
                    user.setEbtAccepted(isEBTAccepted);
                    FDCustomerManager.storeUser(user.getUser());
                    session.setAttribute(USER, user);
                }
            }
        }

        ErpAddressModel erpAddress = addressForm.getErpAddress();
        erpAddress.setFrom(scrubbedAddress);
        erpAddress.setCity(scrubbedAddress.getCity());
        erpAddress.setAddressInfo(scrubbedAddress.getAddressInfo());

        LOGGER.debug("ErpAddressModel:" + scrubbedAddress);

        /*
         * Remove Alt Contact as required for Hamptons as well as COS (for Unattended Delivery process) batchley 20110208
         * if("SUFFOLK".equals(FDDeliveryManager.getInstance().getCounty(scrubbedAddress)) && erpAddress.getAltContactPhone() == null){ actionResult.addError(true,
         * EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED); return null; }
         */

        return erpAddress;
    }

    protected void performAddDeliveryAddress(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
        HttpSession session = pageContext.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);

        // call common delivery address check
        ErpAddressModel erpAddress = checkDeliveryAddressInForm(request, actionResult, session);
        if (erpAddress == null) {
            return;
        }

        try {
            boolean foundFraud = FDCustomerManager.addShipToAddress(AccountActivityUtil.getActionInfo(session), !user.isDepotUser(), erpAddress);
            if (foundFraud) {
                // session.setAttribute(SessionName.SIGNUP_WARNING, MessageFormat.format(
                // SystemMessageList.MSG_NOT_UNIQUE_INFO,
                // new Object[] {user.getCustomerServiceContact()}));
                this.applyFraudChange(user);
            }
            /*
             * if(user.getOrderHistory().getValidOrderCount()==0) { user.setZipCode(erpAddress.getZipCode()); user.setSelectedServiceType(erpAddress.getServiceType()); }
             */
        } catch (ErpDuplicateAddressException ex) {
            LOGGER.warn("AddressUtil:addShipToAddress(): ErpDuplicateAddressException caught while trying to add a shipping address to the customer info:", ex);
            actionResult.addError(new ActionError("duplicate_user_address", "The information entered for this address matches an existing address in your account."));
        }
    }

    protected void performChangeUserID(HttpServletRequest request, ActionResult result) throws FDResourceException {
        ErpCustomerInfoModel cim = null;
        FDIdentity identity = getIdentity();
        String userId = request.getParameter(EnumUserInfoName.EMAIL.getCode());
        String repeatUserId = request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode());

        result.addError((userId == null || userId.trim().length() < 1), EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_REQUIRED);

        if (!result.hasError(EnumUserInfoName.EMAIL.getCode()) && !com.freshdirect.mail.EmailUtil.isValidEmailAddress(userId)) {
            result.addError(new ActionError(EnumUserInfoName.EMAIL_FORMAT.getCode(), SystemMessageList.MSG_EMAIL_FORMAT));
        }

        if (repeatUserId == null || repeatUserId.length() < 1) {
            result.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_REQUIRED));
        } else if (!userId.equals(repeatUserId)) {
            result.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_REPEAT));
        }

        if (result.isSuccess()) {
            try {

                FDCustomerInfo customerInfo = FDCustomerManager.getCustomerInfo(identity);
                String oldUserId = customerInfo.getEmailAddress();
                //
                // Update UserId first since if new user id is a duplicate it will not perform the second change (of user email address)
                //
                FDCustomerManager.updateUserId(AccountActivityUtil.getActionInfo(pageContext.getSession()), userId);

                //
                // No errors updating UserId, so update the email address too
                //
                cim = FDCustomerFactory.getErpCustomerInfo(identity);
                cim.setEmail(userId);

                if (!StringUtils.equals(oldUserId, userId)) {
                    FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);
                    EnumEStoreId estoreId = EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId()));
                    FDCustomerManager.sendEmail(FDEmailFactory.getInstance().createUserIdChangeEmail(customerInfo, oldUserId, userId, estoreId));
                }
            } catch (ErpDuplicateUserIdException ex) {
                LOGGER.warn("New userId already exists in system", ex);
                result.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_UNIQUE_USERNAME));
            }
        }
    }

    protected void performChangeDisplayName(HttpServletRequest request, ActionResult result) throws FDResourceException {
        ErpCustomerInfoModel cim = null;
        String displayName = request.getParameter("displayName");

        if (displayName != null && !"".equals(displayName)) {

            FDIdentity identity = getIdentity();

            cim = FDCustomerFactory.getErpCustomerInfo(identity);
            cim.setDisplayName(displayName);
            try {
                FDCustomerManager.isDisplayNameUsed(displayName, identity.getErpCustomerPK());
            } catch (ErpDuplicateDisplayNameException fde) {
                result.addError(true, "displayName", fde.getMessage());
            }
            if (!result.isSuccess()) {
                return;
            }

            FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);

        }
    }

    private void changeContactInfoName(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");

        FDIdentity identity = getIdentity();
        ErpCustomerInfoModel cim = null;
        cim = FDCustomerFactory.getErpCustomerInfo(identity);
        cim.setFirstName(firstName);
        cim.setLastName(lastName);

        boolean fraudFound = FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);

    }

    protected void performChangeContactInfo(HttpServletRequest request, ActionResult result) throws FDResourceException {
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");
        String title = request.getParameter("title");
        String altEmail = request.getParameter(EnumUserInfoName.ALT_EMAIL.getCode());
        String otherPhone = request.getParameter("other_phone");
        String homePhone = request.getParameter("homephone");
        String homePhoneExt = request.getParameter("ext");
        String otherPhoneExt = request.getParameter("other_ext");

        String busPhone = request.getParameter("busphone");
        String busPhoneExt = request.getParameter("busphoneext");
        String cellPhone = request.getParameter("cellphone");
        String cellPhoneExt = request.getParameter("cellphoneext");
        homePhone = homePhone != null && homePhone.trim().length() == 0 ? null : homePhone;
        busPhone = busPhone != null && busPhone.trim().length() == 0 ? null : busPhone;
        cellPhone = cellPhone != null && cellPhone.trim().length() == 0 ? null : cellPhone;

        String workDept = request.getParameter(EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode());
        String employeeId = request.getParameter("employeeId");

        result.addError(lastName == null || lastName.trim().length() < 1, EnumUserInfoName.DLV_LAST_NAME.getCode(), SystemMessageList.MSG_REQUIRED);

        result.addError(altEmail != null && !"".equals(altEmail) && !EmailUtil.isValidEmailAddress(altEmail), EnumUserInfoName.ALT_EMAIL.getCode(),
                SystemMessageList.MSG_EMAIL_FORMAT);

        result.addError(firstName == null || firstName.trim().length() < 1, EnumUserInfoName.DLV_FIRST_NAME.getCode(), SystemMessageList.MSG_REQUIRED);

        FDUserI user = getUser();

        if (!user.isCorporateUser()) {
            result.addError(homePhone == null, EnumUserInfoName.DLV_HOME_PHONE.getCode(), SystemMessageList.MSG_REQUIRED);
        }

        if (user.isDepotUser()) {

            result.addError(busPhone == null, EnumUserInfoName.DLV_WORK_PHONE.getCode(), SystemMessageList.MSG_REQUIRED);

            result.addError(workDept == null || workDept.trim().length() < 1, EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode(), SystemMessageList.MSG_REQUIRED);

            // TODO LOGISTICS REINTEGRATION TASK

            /*
             * com.freshdirect.delivery.depot.DlvDepotModel depot = FDDepotManager.getInstance().getDepot(user.getDepotCode()); if (depot.getRequireEmployeeId()) {
             * 
             * result.addError( employeeId == null || employeeId.trim().length() < 1, EnumUserInfoName.DLV_EMPLOYEE_ID.getCode(), SystemMessageList.MSG_REQUIRED);
             * 
             * }
             */

        }

        result.addError(homePhone != null && PhoneNumber.normalize(homePhone).length() != 10, EnumUserInfoName.DLV_HOME_PHONE.getCode(), SystemMessageList.MSG_PHONE_FORMAT);
        result.addError(busPhone != null && PhoneNumber.normalize(busPhone).length() != 10, EnumUserInfoName.DLV_WORK_PHONE.getCode(), SystemMessageList.MSG_PHONE_FORMAT);
        result.addError(cellPhone != null && PhoneNumber.normalize(cellPhone).length() != 10, EnumUserInfoName.DLV_CELL_PHONE.getCode(), SystemMessageList.MSG_PHONE_FORMAT);

        result.addError(homePhone != null && PhoneNumber.normalize(homePhone).startsWith("0"), EnumUserInfoName.DLV_HOME_PHONE.getCode(), SystemMessageList.MSG_PHONE_FORMAT);
        result.addError(busPhone != null && PhoneNumber.normalize(busPhone).startsWith("0"), EnumUserInfoName.DLV_WORK_PHONE.getCode(), SystemMessageList.MSG_PHONE_FORMAT);
        result.addError(cellPhone != null && PhoneNumber.normalize(cellPhone).startsWith("0"), EnumUserInfoName.DLV_CELL_PHONE.getCode(), SystemMessageList.MSG_PHONE_FORMAT);

        if (!result.isSuccess()) {
            return;
        }

        FDIdentity identity = getIdentity();
        ErpCustomerInfoModel cim = null;
        cim = FDCustomerFactory.getErpCustomerInfo(identity);
        cim.setFirstName(firstName);
        cim.setLastName(lastName);
        cim.setTitle(title);
        cim.setHomePhone(new PhoneNumber(homePhone, homePhoneExt));
        cim.setOtherPhone(new PhoneNumber(otherPhone, otherPhoneExt));
        cim.setAlternateEmail(altEmail);
        cim.setBusinessPhone(new PhoneNumber(busPhone, busPhoneExt));
        cim.setCellPhone(new PhoneNumber(cellPhone, cellPhoneExt));

        cim.setWorkDepartment(workDept);
        cim.setEmployeeId(employeeId);

        LOGGER.debug("Updating customer info");
        boolean foundFraud = FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);
        LOGGER.debug("Customer info updated");

        ((FDSessionUser) user).getUser().resetCustomerInfoModel();

        /*
         * if(foundFraud){ pageContext.getSession().setAttribute(SessionName.SIGNUP_WARNING, MessageFormat.format( SystemMessageList.MSG_NOT_UNIQUE_INFO, new Object[]
         * {user.getCustomerServiceContact()})); this.applyFraudChange(user); }
         */
    }

    private void applyFraudChange(FDUserI user) throws FDResourceException {
        user.invalidateCache();
        if (user.isFraudulent()) {
            user.updateUserState();
            PromotionI promo = user.getRedeemedPromotion();
            if (promo != null && !user.getPromotionEligibility().isEligible(promo.getPromotionCode())) {
                user.setRedeemedPromotion(null);
            }
        }
        pageContext.getSession().setAttribute(SessionName.USER, user);
    }

    protected void performChangeEmailPreference(HttpServletRequest request, ActionResult result) throws FDResourceException {

        HttpSession session = pageContext.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        boolean receiveNews = "yes".equalsIgnoreCase(request.getParameter("receive_mail"));
        boolean plainTextEmail = request.getParameter("isSendPlainTextEmail") != null;
        boolean sendOptinNewsletter = request.getParameter("isSendOptinNewsletter") != null;

        if (!result.isSuccess()) {
            return;
        }

        FDIdentity identity = getIdentity();
        ErpCustomerInfoModel cim = null;
        cim = FDCustomerFactory.getErpCustomerInfo(identity);

        cim.setReceiveNewsletter(receiveNews);
        if (receiveNews) {
            cim.setUnsubscribeDate(null);
        } else if (cim.getUnsubscribeDate() == null) {
            cim.setUnsubscribeDate(new java.util.Date());
        }

        cim.setEmailPlaintext(plainTextEmail);
        cim.setReceiveOptinNewsletter(sendOptinNewsletter);

        LOGGER.debug("Updating customer email preference");
        FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);
        // To persist store specific 'email optin'.
        FDCustomerManager.storeEmailPreferenceFlag(identity.getFDCustomerPK(), receiveNews ? "X" : "", user.getUserContext().getStoreContext().getEStoreId());
        LOGGER.debug("Customer email preference updated");

    }

    protected void performChangePassword(HttpServletRequest request, ActionResult result) throws FDResourceException {
        FDIdentity identity = getIdentity();
        String password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "").trim();
        String confirmPassword = NVL.apply(request.getParameter("confirmPassword"), "").trim();

        AccountUtil.validatePassword(result, password, confirmPassword);

        if (!result.isSuccess()) {
            return;
        }

        try {
            ErpCustomerModel cm = FDCustomerFactory.getErpCustomer(identity);

            FDCustomerManager.changePassword(AccountActivityUtil.getActionInfo(request.getSession()), cm.getCustomerInfo().getEmail(), password);

        } catch (ErpInvalidPasswordException ex) {
            LOGGER.info("new password too short", ex);
            result.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_PASSWORD_LENGTH));
        }

    }

    protected void performDisconnectSocialAccount(HttpServletRequest request, ActionResult result) throws FDResourceException {

        HttpSession session = pageContext.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        String customerId = user.getUser().getIdentity().getErpCustomerPK();

        String socialEmail = request.getParameter("socialEmail");
        String userToken = request.getParameter("userToken");
        String socialNetworkProvider = request.getParameter("socialNetworkProvider");

        if ((socialEmail == null || "".equals(socialEmail)) || (userToken == null || "".equals(userToken))) {

            return;

        } else {

            try {
                // FDSocialManager.unlinkSocialAccountWithUser( socialEmail, userToken);
                // ExternalAccountManager.unlinkExternalAccountWithUser(socialEmail, userToken, socialNetworkProvider);
                ExternalAccountManager.unlinkExternalAccountWithUser(customerId, socialNetworkProvider);
                request.setAttribute("NewlyDisconnectedSocialNetworkProvider", socialNetworkProvider);
            } catch (FDResourceException e1) {
                LOGGER.error("Error in disconnecting social account:" + e1.getMessage());
                result.addError(new ActionError("Error in disconnecting social account:" + socialEmail));
            }
        }
    }

    protected void changeEmailPreferenceLevel(HttpServletRequest request, ActionResult result) throws FDResourceException {

        // get value
    	HttpSession session = pageContext.getSession();
    	FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        String receive_emailLevel = NVL.apply(request.getParameter("receive_emailLevel"), " ");
        

        if (!result.isSuccess()) {
            return;
        }

        FDIdentity identity = getIdentity();
        ErpCustomerInfoModel cim = null;
        cim = FDCustomerFactory.getErpCustomerInfo(identity);

        cim.setEmailPreferenceLevel(receive_emailLevel);

        LOGGER.debug("Updating customer email level preference");
        FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);
        FDCustomerManager.storeEmailPreferenceFlag(identity.getFDCustomerPK(), receive_emailLevel.equals("2") ? "X":"", user.getUserContext().getStoreContext().getEStoreId());
        LOGGER.debug("Customer email preference level updated");
    }

    protected void changeMailPhonePreference(HttpServletRequest request, ActionResult result) throws FDResourceException {

        // get values

        boolean noContactMail = "yes".equalsIgnoreCase(request.getParameter("noContactMail"));
        boolean noContactPhone = "yes".equalsIgnoreCase(request.getParameter("noContactPhone"));

        if (!result.isSuccess()) {
            return;
        }

        FDIdentity identity = getIdentity();
        ErpCustomerInfoModel cim = null;
        cim = FDCustomerFactory.getErpCustomerInfo(identity);

        cim.setNoContactMail(noContactMail);
        cim.setNoContactPhone(noContactPhone);

        LOGGER.debug("Updating customer mail/phone preferences");
        FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);
        LOGGER.debug("Customer mail/phone preferences updated");
    }

    protected void addAllSmsAlerts(HttpServletRequest request, ActionResult actionResult) {
        String mobile_number = request.getParameter("mobile_number");

        HttpSession session = pageContext.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        FDIdentity identity = user.getIdentity();
        String orderNumber = (String) session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
        session.removeAttribute("SMSAlert" + orderNumber);
        String submitbutton = request.getParameter("submitbutton");
        if ("update".equals(submitbutton)) {
            if (mobile_number == null || mobile_number.length() == 0) {
                actionResult.addError(true, "mobile_number", SystemMessageList.MSG_REQUIRED);
                return;
            } else if (mobile_number != null && mobile_number.length() != 0) {
                PhoneNumber phone = new PhoneNumber(mobile_number);
                if (!phone.isValid()) {
                    actionResult.addError(true, "mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
                    return;
                }
            }

            boolean isSent = false;
            try {
                isSent = SMSAlertManager.getInstance().smsOptIn(identity.getErpCustomerPK(), mobile_number, user.getUserContext().getStoreContext().getEStoreId().getContentId());
            } catch (FDResourceException e) {
                LOGGER.error("Error from mobile preferences", e);
                actionResult.addError(true, "mobile_number", SystemMessageList.MSG_TIMEOUT_ERROR);
                return;
            }
            if (isSent) {

                try {
                    ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
                    FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(user.getIdentity());
                    FDCustomerManager.storeMobilePreferences(identity.getErpCustomerPK(), identity.getFDCustomerPK(), mobile_number, "N", "N", "Y", "Y", "Y", "Y",
                            user.getUserContext().getStoreContext().getEStoreId());
                    FDCustomerManager.storeSmsPreferenceFlag(identity.getFDCustomerPK(), "Y", user.getUserContext().getStoreContext().getEStoreId());
                    session.setAttribute("SMSAlert" + orderNumber, "done");
                } catch (FDResourceException e) {
                    LOGGER.error("Error from mobile preferences", e);
                }
            } else {
                actionResult.addError(new ActionError("send_sms_failure", SystemMessageList.MSG_SMS_ERROR));
            }
        } else if ("remind".equals(submitbutton)) {

        } else {
            // no thanks
            try {
                FDCustomerManager.storeSmsPreferenceFlag(user.getIdentity().getFDCustomerPK(), "N", user.getUserContext().getStoreContext().getEStoreId());
            } catch (FDResourceException e) {
                LOGGER.error("Error from mobile preferences", e);
            }
            session.setAttribute("SMSAlert" + orderNumber, "done");
            session.setAttribute("noThanksFlag", "noThanks");
        }
    }

    protected FDIdentity getIdentity() {
        HttpSession session = pageContext.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
        return (user == null) ? null : user.getIdentity();
    }

    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }

    private FDUserI getUser() {
        HttpSession session = pageContext.getSession();
        FDUserI user = (FDUserI) session.getAttribute(USER);
        return user;
    }

    private static String retainDigits(String string) {
        StringBuffer clean = new StringBuffer();
        if (string == null)
            return "";
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                clean.append(string.charAt(i));
            }
        }
        return clean.toString();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
