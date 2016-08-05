package com.freshdirect.mobileapi.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SocialGateway;
import com.freshdirect.webapp.taglib.fdstore.SocialProvider;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.AccountUtil;

@Component
public class SocialAccountService {

    @Autowired
    private AccountService loginService;

    private static Category LOGGER = LoggerFactory.getInstance(SocialAccountService.class);

    public Map<String, String> getSocialUserProfile(String connectionToken) {
        Map<String, String> socialUserProfile = null;

        SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");

        if (socialProvider != null && connectionToken.length() > 0) {
            // Retrieve social user's profile
            socialUserProfile = socialProvider.getSocialUserProfile(connectionToken);
        }
        return socialUserProfile;
    }

    public SessionUser signInSocialUser(HttpServletRequest request, HttpServletResponse response, Map<String, String> socialUserProfile)
            throws FDAuthenticationException, FDResourceException {
        FDSessionUser user = null;
        String socialUserId = socialUserProfile.get("email");

        // check if user token exist in FD external account database.
        if (isUserIdExistForUserToken(socialUserProfile)) {
            // the just authenticated social email has been linked with one FD account
            user = userLogin(socialUserId, request, response);
        } else {
            // the just authenticated social email is not linked with FD account
            if (isUserEmailAlreadyExist(socialUserId)) {
                // Social user is existing in FD domain
                user = userLogin(socialUserId, request, response);
                linkUserTokenToUserId(socialUserProfile);
            }
        }
        return (user == null) ? null : SessionUser.wrap(user);
    }

    public SessionUser registerSocialUser(HttpServletRequest request, HttpServletResponse response, Map<String, String> socialUserProfile) throws Exception {
        // Social user is not existing in FD domain

        // For express registration, default "user type" to 'Home_USER' default "delivery status" to 'DONOT_DELIVER' default "available services" to 'PICKUP'
        Set<EnumServiceType> availableServices = new HashSet<EnumServiceType>();
        availableServices.add(EnumServiceType.PICKUP);

        // set default address for sociallogin-only user
        AddressModel address = new AddressModel();
        address.setAddress1("23-30 borden ave");
        address.setCity("Long Island City");
        address.setState("NY");
        address.setCountry("US");
        address.setZipCode("11101");

        FDSessionUser user = loginService.createUser(address, EnumServiceType.PICKUP, availableServices, request.getSession(), response);
        ResultBundle result = loginService.registerUser(socialUserProfile, user, AccountUtil.HOME_USER);

        if (result.getActionResult().isSuccess()) {
            String socialUserId = socialUserProfile.get("email");
            user = userLogin(socialUserId, request, response);
            linkUserTokenToUserId(socialUserProfile);
        } else {
            throw new FDResourceException(result.getActionResult().getFirstError().toString());
        }
        return SessionUser.wrap(user);
    }

    private boolean isUserIdExistForUserToken(Map<String, String> socialUserProfile) {
        boolean exists = false;
        try {
            String userId = ExternalAccountManager.getUserIdForUserToken(socialUserProfile.get("userToken"));
            exists = userId != null && userId.length() > 0;
        } catch (FDResourceException e) {
        }
        return exists;
    }

    private boolean isUserEmailAlreadyExist(String email) {
        try {
            return ExternalAccountManager.isUserEmailAlreadyExist(email);
        } catch (FDResourceException e) {
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    public FDSessionUser userLogin(String userId, HttpServletRequest request, HttpServletResponse response) throws FDAuthenticationException, FDResourceException {
        HttpSession session = request.getSession();
        FDIdentity identity = FDCustomerManager.login(userId);
        LOGGER.info("Identity : erpId = " + identity.getErpCustomerPK() + " : fdId = " + identity.getFDCustomerPK());
        FDUser loginUser = FDCustomerManager.recognize(identity);
        LOGGER.info("FDUser : erpId = " + loginUser.getIdentity().getErpCustomerPK() + " : " + loginUser.getIdentity().getFDCustomerPK());
        FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
        LOGGER.info("loginUser is " + loginUser.getFirstName() + " Level = " + loginUser.getLevel());
        LOGGER.info("currentUser is " + (currentUser == null ? "null" : currentUser.getFirstName() + currentUser.getLevel()));
        String currentUserId = null;
        if (currentUser == null) {
            // this is the case right after signout
            UserUtil.createSessionUser(request, response, loginUser);
        } else if (!loginUser.getCookie().equals(currentUser.getCookie())) {
            // current user is different from user who just logged in
            int currentLines = currentUser.getShoppingCart().numberOfOrderLines();
            int loginLines = loginUser.getShoppingCart().numberOfOrderLines();
            // address needs to be set using logged in user's information -
            // in case existing cart is used or cart merge
            currentUser.getShoppingCart().setDeliveryAddress(loginUser.getShoppingCart().getDeliveryAddress());

            if ((currentLines > 0) && (loginLines > 0)) {
                // keep the current cart in the session and send them to the merge cart page
                session.setAttribute(SessionName.CURRENT_CART, currentUser.getShoppingCart());
            } else if ((currentLines > 0) && (loginLines == 0)) {
                // keep current cart
                loginUser.setShoppingCart(currentUser.getShoppingCart());
                loginUser.getShoppingCart().setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(loginUser.getUserContext()));
                loginUser.getShoppingCart().setUserContextToOrderLines(loginUser.getUserContext());
            }

            // merge coupons
            currentUserId = currentUser.getPrimaryKey();

            // current user has gift card recipients that need to be added
            // to the login user's recipients list
            if (currentUser.getLevel() == FDUserI.GUEST && currentUser.getRecipientList().getRecipients().size() > 0) {
                List<RecipientModel> tempList = currentUser.getRecipientList().getRecipients();
                ListIterator<RecipientModel> iterator = tempList.listIterator();
                // add currentUser's list to login user
                while (iterator.hasNext()) {
                    SavedRecipientModel srm = (SavedRecipientModel) iterator.next();
                    // reset the FDUserId to the login user
                    srm.setFdUserId(loginUser.getUserId());
                    loginUser.getRecipientList().removeRecipients(EnumGiftCardType.DONATION_GIFTCARD);
                    loginUser.getRecipientList().addRecipient(srm);
                }
            }

            loginUser.setGiftCardType(currentUser.getGiftCardType());

            if (currentUser.getDonationTotalQuantity() > 0) {
                loginUser.setDonationTotalQuantity(currentUser.getDonationTotalQuantity());
            }
            UserUtil.createSessionUser(request, response, loginUser);
            // The previous recommendations of the current session need to
            // be removed.
            session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
            session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
            session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);

        } else {
            // the logged in user was the same as the current user,
            // that means that they were previously recognized by their
            // cookie before log in
            // just set their login status and move on
            currentUser.isLoggedIn(true);
            session.setAttribute(SessionName.USER, currentUser);
        }
        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
        if (user != null) {
            user.setEbtAccepted(user.isEbtAccepted() && (user.getOrderHistory().getUnSettledEBTOrderCount() < 1) && !user.hasEBTAlert());
            FDCustomerCouponUtil.initCustomerCoupons(session, currentUserId);
        }

        if (user != null) {
            user.setJustLoggedIn(true);
            user.setTcAcknowledge(loginUser.getTcAcknowledge());
        }

        return user;
    }

    public void linkUserTokenToUserId(Map<String, String> socialUserProfile) throws FDResourceException {
        String customerId = FDCustomerManager.getCustomerId(socialUserProfile.get("email")).getId();
        ExternalAccountManager.linkUserTokenToUserId(customerId, socialUserProfile.get("email"), socialUserProfile.get("userToken"), socialUserProfile.get("identityToken"),
                socialUserProfile.get("provider"), socialUserProfile.get("displayName"), socialUserProfile.get("preferredUsername"), socialUserProfile.get("email"),
                socialUserProfile.get("emailVerified"));
        LOGGER.info("Account: " + socialUserProfile.get("email") + " provider: " + socialUserProfile.get("provider") + " linked");
    }

}
