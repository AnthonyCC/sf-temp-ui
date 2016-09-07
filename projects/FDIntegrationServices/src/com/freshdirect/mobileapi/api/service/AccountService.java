package com.freshdirect.mobileapi.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.springframework.stereotype.Component;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountRegisterRequest;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.model.Checkout;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.OrderHistory;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.RegistrationControllerTagWrapper;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.LocatorUtil;
import com.freshdirect.webapp.util.StoreContextUtil;

@Component
public class AccountService {

    private static Category LOGGER = LoggerFactory.getInstance(AccountService.class);

    public SessionUser getSessionUser(HttpServletRequest request, HttpServletResponse response, String source) {
        HttpSession session = request.getSession();
        FDSessionUser user = getUserFromSession(session);

        if (user == null) {
            try {
                ensureTransactionSourceInSession(session, source);
                user = CookieMonster.loadCookie(request);
            } catch (FDResourceException ex) {
                LOGGER.warn(ex);
            }
            if (user != null) {
                FDCustomerCouponUtil.initCustomerCoupons(session);
            } else {
                user = LocatorUtil.useIpLocator(session, request, response, null);
            }
            user.resetUserContext();
            ContentFactory.getInstance().setCurrentUserContext(user.getUserContext());
            session.setAttribute(SessionName.USER, user);
        }

        return SessionUser.wrap(user);
    }

    public LoggedIn createLoginResponseMessage(SessionUser user) throws FDException {
        LoggedIn responseMessage = new LoggedIn();

        if (user.isLoggedIn()) {
            OrderHistory history = user.getOrderHistory();
            String cutoffMessage = user.getCutoffInfo();

            OrderInfo closestPendingOrder = history.getClosestPendingOrderInfo(new Date());
            List<OrderInfo> orderHistoryInfo = new ArrayList<OrderInfo>();
            if (closestPendingOrder != null) {
                orderHistoryInfo.add(closestPendingOrder);
            }

            responseMessage.setChefTable(user.isChefsTable());
            responseMessage.setCustomerServicePhoneNumber(user.getCustomerServiceContact());
            if (user.getReservationTimeslot() != null) {
                responseMessage.setReservationTimeslot(new Timeslot(user.getReservationTimeslot()));
            }
            responseMessage.setFirstName(user.getFirstName());
            responseMessage.setLastName(user.getLastName());
            responseMessage.setUsername(user.getUsername());
            responseMessage.setOrders(com.freshdirect.mobileapi.controller.data.response.OrderHistory.Order.createOrderList(orderHistoryInfo, user));
            responseMessage.setSuccessMessage("User has been logged in successfully.");
            responseMessage.setItemsInCartCount(user.getItemsInCartCount());
            responseMessage.setOrderCount(user.getOrderHistory().getValidOrderCount());
            responseMessage.setFdUserId(user.getPrimaryKey());

            // DOOR3 FD-iPad FDIP-474
            // Note: The field is inappropriately named. It is not referring to whether or not the user is on the FD mailing
            // list, but rather intended to represent whether or not the user is to be notified
            // in the event of service being introduced to their area.
            responseMessage.setOnMailingList(user.isFutureZoneNotificationEmailSentForCurrentAddress());

            if (cutoffMessage != null) {
                responseMessage.addNoticeMessage(MessageCodes.NOTICE_DELIVERY_CUTOFF, cutoffMessage);
            }

            if (!user.getFDSessionUser().isCouponsSystemAvailable() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
                responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE, SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
            }

            responseMessage.setBrowseEnabled(MobileApiProperties.isBrowseEnabled());

            // Added during Mobile Coremetrics Implementation
            responseMessage.setSelectedServiceType(user.getSelectedServiceType() != null ? user.getSelectedServiceType().toString() : "");
            responseMessage.setCohort(user.getCohort());
            responseMessage.setTotalOrderCount(user.getTotalOrderCount());

            responseMessage.setTcAcknowledge(user.getTcAcknowledge());

            // FDX-1873 - Show timeslots for anonymous address
            boolean deliveryAddr = setDeliveryAddress(user);
            responseMessage.setAnonymousAddressSetFromAcc(deliveryAddr);
            responseMessage.setMobileNumber(getMobileNumber(user));
        } else {
            responseMessage.setFailureMessage("User is not logged in.");
        }
        responseMessage.setPlantId(BrowseUtil.getPlantId(user));
        return responseMessage;
    }

    private String getMobileNumber(SessionUser user) throws FDResourceException {
        String mobileNumber = null;
        FDSessionUser fduser = (FDSessionUser) user.getFDSessionUser();
        FDCustomerModel fdCustomerModel = FDCustomerFactory.getFDCustomer(fduser.getIdentity());
        FDCustomerEStoreModel customerSmsPreferenceModel = fdCustomerModel.getCustomerSmsPreferenceModel();

        if (EnumEStoreId.FDX.getContentId().equals(fduser.getUserContext().getStoreContext().getEStoreId().getContentId())) {
            mobileNumber = customerSmsPreferenceModel.getFdxMobileNumber() != null ? customerSmsPreferenceModel.getFdxMobileNumber().getPhone() : "";
        } else {
            mobileNumber = customerSmsPreferenceModel.getMobileNumber() != null ? customerSmsPreferenceModel.getMobileNumber().getPhone() : "";
        }
        return mobileNumber;
    }
    
    // FDX-1873 - Show timeslots for anonymous address
    // FDX-2036 API - at login, if anon address exists in Address Book of user, select the Address Book address
    private boolean setDeliveryAddress(SessionUser user) throws FDException {
        if (user.getAddress() != null && user.getAddress().isCustomerAnonymousAddress() && user.getAddress().getAddress1() != null
                && user.getAddress().getAddress1().trim().length() > 0) {
            List<ErpAddressModel> addresses = FDCustomerFactory.getErpCustomer(user.getFDSessionUser().getIdentity()).getShipToAddresses();
            for (ErpAddressModel acctAddr : addresses) {
                boolean isAddressMatching = matchAddress(acctAddr, user.getAddress());
                if (isAddressMatching) {
                    Checkout checkout = new Checkout(user);
                    ResultBundle resultBundle = checkout.setCheckoutDeliveryAddressEx(acctAddr.getId(), DeliveryAddressType.RESIDENTIAL);
                    ActionResult result = resultBundle.getActionResult();
                    if (result.isSuccess()) {
                        user.getAddress().setCustomerAnonymousAddress(false);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    // FDX-1873 - Show timeslots for anonymous address
    private static boolean matchAddress(ErpAddressModel addr1, AddressModel addr2) {
        if (addr1 == null || addr2 == null)
            return false;
        if (addr1.getAddress1() != null && addr1.getAddress1().equalsIgnoreCase(addr2.getAddress1())
                && ((addr1.getAddress2() == null && addr2.getAddress2() == null) || (addr1.getAddress2() != null && addr1.getAddress2().equalsIgnoreCase(addr2.getAddress2()))
                        || (addr1.getAddress2().trim().length() == 0 && addr2.getAddress2().trim().length() == 0))
                && addr1.getCity() != null && addr1.getCity().equalsIgnoreCase(addr2.getCity())) {
            return true;
        }
        return false;
    }

    private void ensureTransactionSourceInSession(HttpSession session, String source) {
        EnumTransactionSource transactionSource = EnumTransactionSource.getTransactionSource(source);
        if (transactionSource == null) {
            transactionSource = EnumTransactionSource.IPHONE_WEBSITE;
        }
        session.setAttribute(SessionName.APPLICATION, transactionSource.getCode());
    }

    private FDSessionUser getUserFromSession(HttpSession session) {
        return (FDSessionUser) session.getAttribute(SessionName.USER);
    }

    public FDSessionUser login(HttpServletRequest request, HttpServletResponse response, String email, String password) throws FDAuthenticationException, FDResourceException {
        FDIdentity identity = FDCustomerManager.login(email, password);
        FDUser user = FDCustomerManager.recognize(identity);
        UserUtil.createSessionUser(request, response, user);
        return (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
    }

    public void logout(HttpSession session, HttpServletResponse response) {
        if (session != null) {
            session.invalidate();
        }
        CookieMonster.clearCookie(response);
    }

    public FDSessionUser createUser(AddressModel address, EnumServiceType serviceType, Set<EnumServiceType> availableServices, HttpSession session, HttpServletResponse response)
            throws FDResourceException {
        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

        if ((user == null) || ((user.getZipCode() == null) && (user.getDepotCode() == null))) {
            //
            // if there is no user object or a dummy user object created in
            // CallCenter, make a new using this zipcode
            // make sure to hang on to the cart that might be in progress in
            // CallCenter
            //
            FDCartModel oldCart = null;
            if (user != null) {
                oldCart = user.getShoppingCart();
            }
            StoreContext storeContext = StoreContextUtil.getStoreContext(session);
            user = new FDSessionUser(FDCustomerManager.createNewUser(address, serviceType, storeContext.getEStoreId()), session);
            user.setUserCreatedInThisSession(true);

            // user.setAddress(this.address);
            user.setAddress(address);
            user.setSelectedServiceType(serviceType);
            // Added the following line for zone pricing to keep user service type up-to-date.
            user.setZPServiceType(serviceType);
            user.setAvailableServices(availableServices);

            if (oldCart != null) {
                user.setShoppingCart(oldCart);
            }

            CookieMonster.storeCookie(user, response);
            session.setAttribute(SessionName.USER, user);
        } else {
            //
            // otherwise, just update the zipcode in their existing object if
            // they haven't yet registered
            //
            if (user.getLevel() < FDUser.RECOGNIZED) {
                user.setAddress(address);
                user.setSelectedServiceType(serviceType);
                // Added the following line for zone pricing to keep user service type up-to-date.
                user.setZPServiceType(serviceType);
                user.setAvailableServices(availableServices);
                // Need to reset the pricing context so the pricing context can be recalculated.
                CookieMonster.storeCookie(user, response);
                FDCustomerManager.storeUser(user.getUser());
                session.setAttribute(SessionName.USER, user);
            }
        }

        // To fetch and set customer's coupons.
        if (user != null) {
            FDCustomerCouponUtil.initCustomerCoupons(session);
            user.setNewUserWelcomePageShown(true); // do not redirect to welcome.jsp
        }
        return user;
    }

    public ResultBundle registerUser(Map<String, String> socialUserProfile, FDSessionUser user, int regType) throws Exception {

        // registration and login goes here
        String email = (String) socialUserProfile.get("email");
        String displayName = (String) socialUserProfile.get("displayName");
        String names[] = displayName.split(" ");
        String firstName = (names.length == 0) ? "" : names[0];
        String lastName = (names.length <= 1) ? "" : names[names.length - 1];

        ExternalAccountRegisterRequest requestMessageRegister = new ExternalAccountRegisterRequest();
        requestMessageRegister.setEmail(email);
        requestMessageRegister.setFirstName(firstName);
        requestMessageRegister.setLastName(lastName);
        requestMessageRegister.setMobile_number("");
        requestMessageRegister.setPassword("");
        requestMessageRegister.setWorkPhone("");
        requestMessageRegister.setServiceType("");
        requestMessageRegister.setSecurityQuestion("");

        RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user);
        ResultBundle result = tagWrapper.registerSocial(requestMessageRegister);

        return result;
    }

}
