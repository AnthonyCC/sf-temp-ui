package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.deliverypass.EnumDPAutoRenewalType;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.EnumWinePrice;
import com.freshdirect.fdstore.customer.DCPDPromoProductCache;
import com.freshdirect.fdstore.customer.ExternalCampaign;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.SilverPopupDetails;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.SignupDiscountRule;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.util.IgnoreCaseString;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.logistics.analytics.model.SessionEvent;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.smartstore.SessionImpressionLogEntry;
import com.freshdirect.smartstore.fdstore.SessionImpressionLog;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;

public class FDSessionUser implements FDUserI, HttpSessionBindingListener {

    private static final long serialVersionUID = 587469031501334715L;

    private static Logger LOGGER = LoggerFactory.getInstance(FDSessionUser.class);

    private final FDUser user;

    private long lastCartSaveTime;

    private final static long SAVE_PERIOD = FDStoreProperties.getUserCartSaveInterval(); // 0 minutes

    private int failedAuthorizations = 0;
    private boolean healthWarningAcknowledged = false;

    private Date startDate;
    private long lastRequestDate;
    private Map<String, SessionImpressionLogEntry> impressions = new ConcurrentHashMap<String, SessionImpressionLogEntry>();

    private String sessionId = null;

    private String tabSiteFeature = null;

    // No. of Gift Card apply attempts
    private int gcRetryCount = 0;

    private String lastSenderName = null;
    private String lastSenderEmail = null;

    private boolean lastRecipAdded;

    // Invalid Payment Method on Signup.

    private ErpPaymentMethodI invalidPaymentMethod;
    private boolean gcSignupError;
    private String gcFraudReason;

    private boolean isAddressVerificationError;
    private String addressVerficationMsg;

    private List<String> oneTimeGCPaymentError = null;

    // Vending changes
    private boolean lastCOSSurveySuccess; // holds if survey was success
    private String lastCOSSurvey = null; // holds name of survey

    private boolean seenDpNewTc = false;

    // APPDEV-2448 IPSniff fields
    private boolean userCreatedInThisSession;
    private boolean futureZoneNotificationEmailSentForCurrentAddress;
    private boolean moreInfoPopupShownForCurrentAddress;

    // APPDEV-2812 IP Detect Phase I Welcome Experience
    private boolean newUserWelcomePageShown;

    /* this is set in LoginServlet see getter for notes */
    private boolean justLoggedIn = false;
    /* set in RegistrationControllerTag see getter for notes */
    private boolean justSignedUp = false;
    private boolean rafFriendSignedUp = false;
    private boolean isAvalaraTaxed;
    private boolean isMobilePlatForm;
    private String 	platForm;
    private String 	lat;
    private String 	pdUserId;

    private Set<ContentKey> checkoutUnavailableProductKeys; // set of items which failed the ATP test

	public void setLastCOSSurveySuccess(boolean lastCOSSurveySuccess) {
        this.lastCOSSurveySuccess = lastCOSSurveySuccess;
    }

    private FDStandingOrder currentStandingOrder;

    private EnumCheckoutMode checkoutMode = EnumCheckoutMode.NORMAL;
    private boolean couponWarningAcknowledged = false;

    private Map<String, List<AddToCartItem>> pendingExternalAtcItems;

    private boolean isSoContainerOpen = false;

    private boolean zipPopupSeenInSession = false;

	private Map<String,String> soCartLineMessagesMap=new HashMap<String,String>();

	private boolean soCartOverlayFirstTime = false;

	private boolean isRefreshSoCartOverlay=true;

	private boolean soFeatureOverlay = false;

	private boolean isRefreshNewSoFeature = true;
	
	private boolean showInformOrderModify = false;
	
	@Override
    public boolean isSoContainerOpen() {
        return isSoContainerOpen;
    }

    public void setSoContainerOpen(boolean isSoContainerOpen) {
        this.isSoContainerOpen = isSoContainerOpen;
    }

    public boolean getLastCOSSurveySuccess() {
        return this.lastCOSSurveySuccess;
    }

    public void setLastCOSSurvey(String lastCOSSurvey) {
        this.lastCOSSurvey = lastCOSSurvey;
    }

    public String getLastCOSSurvey() {
        return this.lastCOSSurvey;
    }

    public List<String> getOneTimeGCPaymentError() {
        return oneTimeGCPaymentError;
    }

    public void setOneTimeGCPaymentError(List<String> oneTimeGCPaymentError) {
        this.oneTimeGCPaymentError = oneTimeGCPaymentError;
    }

    public String getAddressVerficationMsg() {
        return addressVerficationMsg;
    }

    public void setAddressVerficationMsg(String addressVerficationMsg) {
        this.addressVerficationMsg = addressVerficationMsg;
    }

    public boolean isAddressVerificationError() {
        return isAddressVerificationError;
    }

    public void setAddressVerificationError(boolean isAddressVerificationError) {
        this.isAddressVerificationError = isAddressVerificationError;
    }

    public FDSessionUser(FDUser user, HttpSession session) {
        super();
        this.user = user;

        String app = (String) session.getAttribute(SessionName.APPLICATION);
        EnumTransactionSource src = EnumTransactionSource.WEBSITE;

        if (app != null) {
            if ("callcenter".equalsIgnoreCase(app)) {
                src = EnumTransactionSource.CUSTOMER_REP;
            } else if (EnumTransactionSource.IPHONE_WEBSITE.getCode().equals(app)) {
                src = EnumTransactionSource.IPHONE_WEBSITE;
            } else if (EnumTransactionSource.ANDROID_WEBSITE.getCode().equals(app)) {
                src = EnumTransactionSource.ANDROID_WEBSITE;
            } else if (EnumTransactionSource.FDX_IPHONE.getCode().equals(app)) {
                src = EnumTransactionSource.FDX_IPHONE;
            } else if (EnumTransactionSource.ANDROID_PHONE.getCode().equals(app)) {
                src = EnumTransactionSource.ANDROID_PHONE;
            } else if (EnumTransactionSource.FOODKICK_WEBSITE.getCode().equals(app)) {
                src = EnumTransactionSource.FOODKICK_WEBSITE;
            } // else get defaulted to website
        }
        this.user.setApplication(src);

        lastCartSaveTime = System.currentTimeMillis();
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        LOGGER.debug("FDUser bound to session " + event.getSession().getId() + " user cookie " + this.getCookie() + " username " + this.getUserId());
        this.impressions.clear();
        this.startDate = new Date();
        this.lastRequestDate = startDate.getTime();
        sessionId = event.getSession().getId();
        if (!this.isRobot()) {
            this.saveCart();

            if (FDStoreProperties.isSessionLoggingEnabled()) {
            	if(user.getUserContext()!=null){
					EnumEStoreId  eStoreId = user.getUserContext().getStoreContext() != null ? user.getUserContext().getStoreContext().getEStoreId(): EnumEStoreId.FD;
					if(eStoreId!=null)
					user.setSessionEvent(new SessionEvent(eStoreId.toString().toLowerCase()));
				}
            	else
            		user.setSessionEvent(new SessionEvent());
            }

            // store cohort ID
            if (user.getCohortName() == null) {
                try {
                    user.createCohortName();
                    LOGGER.debug("Generated new cohort ID " + user.getCohortName() + " to user");
                } catch (FDResourceException e) {
                    LOGGER.error("Failed to generate cohort ID for user");
                }
            } else {
                LOGGER.debug("Assigned cohort ID " + user.getCohortName() + " to user");
            }
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        LOGGER.debug("FDUser unbound from session " + event.getSession().getId() + " user cookie " + this.getCookie() + " username " + this.getUserId());
        if (!this.isRobot()) {
            this.saveCart(true);
            this.saveImpressions();
            this.releaseModificationLock();
            
            /* store on session time out */
            try {
            	if (user != null && user.getIdentity() != null && user.getIdentity().getErpCustomerPK() != null) {
                	LOGGER.debug("Updating FDCustomerEStore (custId:"+user.getIdentity().getErpCustomerPK()+")");
    				FDCustomerManager.updateFDCustomerEStoreInfo(this.getFDCustomer().getCustomerEStoreModel(), this.getFDCustomer().getId());	
            	}
			} catch (FDResourceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            if (FDStoreProperties.isSessionLoggingEnabled()) {
                try {
                    if (user != null && user.getIdentity() != null && user.getIdentity().getErpCustomerPK() != null) {
                        Date loginTime = null;
                        if (event.getSession() != null)
                            loginTime = new Date(event.getSession().getCreationTime());

                        if (user.getSessionEvent() != null) {
                            SessionEvent sessionEvent = user.getSessionEvent();
                            sessionEvent.setCustomerId(user.getIdentity().getErpCustomerPK());
                            sessionEvent.setLoginTime(loginTime);
                            sessionEvent.setLogoutTime(new Date());
                            sessionEvent.setClientIp(user.getClientIp());
                            sessionEvent.setServerName(user.getServerName());
                            FDDeliveryManager.getInstance().logSessionEvent(sessionEvent);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.info("Exception while logging the session event");
                    e.printStackTrace();
                }
            }

            // clear masquerade agent, and log this event
            MasqueradeContext masqueradeContext = getMasqueradeContext();
            if (masqueradeContext != null) {

                String masqueradeAgent = masqueradeContext.getAgentId();
                if (masqueradeAgent != null) {
                    logMasqueradeLogout(masqueradeAgent);
                }
            }
        }
        sessionId = null;
    }

    private void releaseModificationLock() {
        try {
            if (this.user.getShoppingCart() instanceof FDModifyCartModel) {
                String orderId = ((FDModifyCartModel) this.user.getShoppingCart()).getOriginalOrder().getSale().getId();
                FDCustomerManager.releaseModificationLock(orderId);
            }

        } catch (FDResourceException ex) {
            LOGGER.warn("Unable to release modification lock", ex);
        }
    }

    private void logMasqueradeLogout(String masqueradeAgent) {
        LOGGER.debug("logMasqueradeLogout()");
        try {
            EnumEStoreId eStore = this.getUserContext().getStoreContext() != null ? this.getUserContext().getStoreContext().getEStoreId() : EnumEStoreId.FD;
            FDActionInfo.setMasqueradeAgentTL(this.getMasqueradeContext() == null ? null : this.getMasqueradeContext().getAgentId());
            FDActionInfo ai = new FDActionInfo(eStore, EnumTransactionSource.WEBSITE, this.getIdentity(), "Masquerade logout", masqueradeAgent + " logged out as "
                    + this.getUserId(), null, EnumAccountActivityType.MASQUERADE_LOGOUT, this.getPrimaryKey());
            ActivityLog.getInstance().logActivity(ai.createActivity());
        } catch (FDResourceException ex) {
            LOGGER.error("Activity logging failed due to FDResourceException.", ex);
        }
    }

	/**
     * update the last request date.
     */
    public void touch() {
        lastRequestDate = System.currentTimeMillis();
    }

    private void saveImpressions() {
        if (!impressions.isEmpty()) {
            List<SessionImpressionLogEntry> logEntries = new ArrayList<SessionImpressionLogEntry>();
            logEntries.addAll(impressions.values());
            Date endDate = new Date(lastRequestDate);
            for (int i = 0; i < logEntries.size(); i++) {
                logEntries.get(i).setStartEndTime(startDate, endDate);
                if (user != null && user.getUserContext() != null && user.getUserContext().getPricingContext() != null
                        && user.getUserContext().getPricingContext().getZoneInfo() != null) {
                    logEntries.get(i).setZoneId(user.getUserContext().getPricingContext().getZoneInfo().getPricingZoneId());
                }
            }

            SessionImpressionLog.getInstance().saveLogEntries(logEntries);
        }
    }

    public void logImpression(String variant, int productCount) {
        if (sessionId == null) {
            LOGGER.error("current FD user not bound to session");
            return;
        }

        SessionImpressionLogEntry entry = impressions.get(variant);
        if (entry == null) {
            entry = new SessionImpressionLogEntry(user.getPrimaryKey(), sessionId, variant);
            impressions.put(variant, entry);
        }

        entry.incrementImpressions(productCount);
    }

    public void logTabImpression(String variant, int tabCount) {
        if (sessionId == null) {
            LOGGER.error("current FD user not bound to session");
            return;
        }

        SessionImpressionLogEntry entry = impressions.get(variant);
        if (entry == null) {
            entry = new SessionImpressionLogEntry(user.getPrimaryKey(), sessionId, variant);
            impressions.put(variant, entry);
        }

        entry.incrementTabImpressions(tabCount);
    }

    public void saveCart() {
        this.saveCart(false);
    }

    public void saveCart(boolean forceSave) {
        //
        // don't save carts too often
        //
        if (!forceSave && (System.currentTimeMillis() - lastCartSaveTime) < SAVE_PERIOD)
            return;
        if (this.isCartPersistent()) {
            LOGGER.info("Saving FDUser cart");
            try {
                FDCustomerManager.storeUser(this.user);
                lastCartSaveTime = System.currentTimeMillis();
            } catch (FDResourceException ex) {
                LOGGER.warn("Unable to save FDUser", ex);
            }
        }
    }

    public void insertOrUpdateSilverPopup(SilverPopupDetails details) {

        if (null != details && null != details.getCustomerId() && !"".equals(details.getCustomerId())) {
            LOGGER.info("Saving Silverpopup details");
            try {
                FDCustomerManager.insertOrUpdateSilverPopup(details);
            } catch (FDResourceException ex) {
                LOGGER.warn("Unable to save Silver popup", ex);
            }
        }
    }

    private boolean isCartPersistent() {
        //
        // don't save users that don't have a primary key
        //
        // if (this.user.isAnonymous()) return false;
        //
        // don't save not persistent carts or in modify mode
        //
        FDCartModel cart = this.getShoppingCart();
        if (cart instanceof FDModifyCartModel || !cart.isPersistent())
            return false;
        //
        // save carts automatically for users who are:
        // already registered
        // anonymous and in our delivery area,
        // anonymous and will be in our delivery area within a year
        //
        if (this.getLevel() >= GUEST || this.isInZone()) {
            return true;
        }
        return false;
    }

    @Override
    public EnumTransactionSource getApplication() {
        return this.user.getApplication();
    }

    public int getFailedAuthorizations() {
        return this.failedAuthorizations;
    }

    public void incrementFailedAuthorizations() {
        this.failedAuthorizations++;
    }

    public boolean isHealthWarningAcknowledged() {
        return healthWarningAcknowledged;
    }

    public void setHealthWarningAcknowledged(boolean healthWarningAcknowledged) {
        this.healthWarningAcknowledged = healthWarningAcknowledged;
    }

    public FDUser getUser() {
        return this.user;
    }

    @Override
    public String getCookie() {
        return this.user.getCookie();
    }

    @Override
    public void setCookie(String cookie) {
        this.user.setCookie(cookie);
    }

    @Override
    public String getZipCode() {
        return this.user.getZipCode();
    }

    @Override
    public void setZipCode(String zipCode) {
        this.user.setZipCode(zipCode);
    }

    @Override
    public void setAddress(AddressModel a) {
        this.user.setAddress(a);
    }

    public void setAddress(AddressModel a, boolean populateUserContext) {
        this.user.setAddress(a, populateUserContext);
    }

    @Override
    public AddressModel getAddress() {
        return this.user.getAddress();
    }

    @Override
    public String getPrimaryKey() {
        return this.user.getPrimaryKey();
    }

    @Override
    public FDIdentity getIdentity() {
        return this.user.getIdentity();
    }

    @Override
    public void setIdentity(FDIdentity identity) {
        this.user.setIdentity(identity);
    }

    @Override
    public int getLevel() {
        return this.user.getLevel();
    }

    @Override
    public boolean isInZone() {
        return this.user.isInZone();
    }

    @Override
    public void isLoggedIn(boolean loggedIn) {
        this.user.isLoggedIn(loggedIn);
    }

    @Override
    public FDCartModel getShoppingCart() {
        return this.user.getShoppingCart();
    }

    @Override
    public void setShoppingCart(FDCartModel cart) {
        this.user.setShoppingCart(cart);
    }

    @Override
    public boolean isSurveySkipped() {
        return this.user.isSurveySkipped();
    }

    @Override
    public void setSurveySkipped(boolean skipped) {
        this.user.setSurveySkipped(skipped);
    }

    @Override
    public boolean isFraudulent() throws FDResourceException {
        return this.user.isFraudulent();
    }

    @Override
    public FDPromotionEligibility getPromotionEligibility() {
        return this.user.getPromotionEligibility();
    }

    @Override
    public double getMaxSignupPromotion() {
        return this.user.getMaxSignupPromotion();
    }

    @Override
    public SignupDiscountRule getSignupDiscountRule() {
        return this.user.getSignupDiscountRule();
    }

    @Override
    public boolean isPromotionAddressMismatch() {
        return this.user.isPromotionAddressMismatch();
    }

    @Override
    public void setRedeemedPromotion(PromotionI promotion) {
        this.user.setRedeemedPromotion(promotion);
    }

    @Override
    public PromotionI getRedeemedPromotion() {
        return this.user.getRedeemedPromotion();
    }

    @Override
    public void updateUserState() {
        this.user.updateUserState();
    }

    @Override
    public void updateUserState(boolean syncServiceType) {
        this.user.updateUserState(syncServiceType);
    }

    public void resetCachedCustomerInfo() throws FDResourceException {
        this.user.resetCustomerInfoModel();
    }

    @Override
    public String getFirstName() throws FDResourceException {
        return this.user.getFirstName();
    }

    @Override
    public String getDepotCode() {
        return this.user.getDepotCode();
    }

    @Override
    public void setDepotCode(String depotCode) {
        this.user.setDepotCode(depotCode);
    }

    @Override
    public boolean isDepotUser() {
        return this.user.isDepotUser();
    }

    @Override
    public boolean isCorporateUser() {
        return this.user.isCorporateUser();
    }

    @Override
    public void invalidateCache() {
        this.user.invalidateCache();
    }

    @Override
    public OrderHistoryI getOrderHistory() throws FDResourceException {
        return this.user.getOrderHistory();
    }

    @Override
    public CustomerAvgOrderSize getHistoricOrderSize() throws FDResourceException {
        return this.user.getHistoricOrderSize();
    }

    @Override
    public int getAdjustedValidOrderCount() throws FDResourceException {
        return this.user.getAdjustedValidOrderCount();
    }

    @Override
    public int getAdjustedValidOrderCount(EnumEStoreId storeId) throws FDResourceException {
        return this.user.getAdjustedValidOrderCount(storeId);
    }

    @Override
    public int getAdjustedValidOrderCount(EnumDeliveryType deliveryType) throws FDResourceException {
        return this.user.getAdjustedValidOrderCount(deliveryType);
    }

    @Override
    public int getValidOrderCount(EnumDeliveryType deliveryType) throws FDResourceException {
        return this.user.getValidOrderCount(deliveryType);
    }

    public int getDeliveredOrderCount() throws FDResourceException {
        return this.user.getDeliveredOrderCount();
    }

    @Override
    public int getValidPhoneOrderCount() throws FDResourceException {
        return this.user.getValidPhoneOrderCount();
    }

    @Override
    public boolean isEligibleForSignupPromotion() {
        return this.user.isEligibleForSignupPromotion();
    }

    @Override
    public PromotionI getEligibleSignupPromotion() {
        return this.user.getEligibleSignupPromotion();
    }

    /**
     * overrides minimum order limit in case Standing Order checkout
     *
     * @return
     */
    private Double getOverrideMinimumAmount() {
        if (getCheckoutMode().isStandingOrderMode()) {
            return ErpServicesProperties.getStandingOrderSoftLimit();
        }
        return null;
    }

    @Override
    public boolean isOrderMinimumMet() throws FDResourceException {
        return this.user.isOrderMinimumMet(getOverrideMinimumAmount());
    }

    @Override
    public boolean isOrderMinimumMet(boolean alcohol) throws FDResourceException {
        return this.user.isOrderMinimumMet(alcohol, getOverrideMinimumAmount());
    }

    @Override
    public boolean isOrderMinimumMetWithoutWine() throws FDResourceException {
        return this.user.isOrderMinimumMetWithoutWine();
    }

    @Override
    public double getMinimumOrderAmount() {
        Double overrideMinimumAmount = getOverrideMinimumAmount();
        return (overrideMinimumAmount == null ? this.user.getMinimumOrderAmount() : overrideMinimumAmount);
    }

    @Override
    public float getQuantityMaximum(ProductModel product) {
        return this.user.getQuantityMaximum(product);
    }

    @Override
    public int getOrderCountForChefsTableEligibility() throws FDResourceException {
        return this.user.getOrderCountForChefsTableEligibility();
    }

    @Override
    public String getOrderTotalForChefsTableEligibility() throws FDResourceException {
        return this.user.getOrderTotalForChefsTableEligibility();
    }

    @Override
    public String getOrderCountRemainingForChefsTableEligibility() throws FDResourceException {
        return this.user.getOrderCountRemainingForChefsTableEligibility();
    }

    @Override
    public String getOrderTotalRemainingForChefsTableEligibility() throws FDResourceException {
        return this.user.getOrderTotalRemainingForChefsTableEligibility();
    }

    @Override
    public boolean isCloseToCTEligibilityByOrderCount() throws FDResourceException {
        return this.user.isCloseToCTEligibilityByOrderCount();
    }

    @Override
    public boolean isCloseToCTEligibilityByOrderTotal() throws FDResourceException {
        return this.user.isCloseToCTEligibilityByOrderTotal();
    }

    @Override
    public boolean isOkayToDisplayCTEligibility() throws FDResourceException {
        return this.user.isOkayToDisplayCTEligibility();
    }

    @Override
    public boolean hasQualifiedForCT() throws FDResourceException {
        return this.user.hasQualifiedForCT();
    }

    @Override
    public String getEndChefsTableQualifyingDate() throws FDResourceException {
        return this.user.getEndChefsTableQualifyingDate();
    }

    @Override
    public boolean isPickupOnly() {
        return this.user.isPickupOnly();
    }

    @Override
    public boolean isPickupUser() {
        return this.user.isPickupUser();
    }

    @Override
    public boolean isNotServiceable() {
        return this.user.isNotServiceable();
    }

    @Override
    public boolean isDeliverableUser() {
        return this.user.isDeliverableUser();
    }

    @Override
    public boolean isHomeUser() {
        return this.user.isHomeUser();
    }

    @Override
    public FDCustomerModel getFDCustomer() throws FDResourceException {
        return this.user.getFDCustomer();
    }

    @Override
    public FDReservation getReservation() {
        return this.user.getReservation();
    }

    @Override
    public void setReservation(FDReservation reservation) {
        this.user.setReservation(reservation);
    }

    @Override
    public boolean isChefsTable() throws FDResourceException {
        return this.user.isChefsTable();
    }
    
    @Override
    public boolean isFDLabsCustomer() throws FDResourceException {
        return this.user.isFDLabsCustomer();
    }

    @Override
    public String getChefsTableInduction() throws FDResourceException {
        return this.user.getChefsTableInduction();
    }

    @Override
    public String getWinback() throws FDResourceException {
        return this.user.getWinback();
    }

    @Override
    public String getWinbackPath() throws FDResourceException {
        return this.user.getWinbackPath();
    }

    @Override
    public String getMarketingPromo() throws FDResourceException {
        return this.user.getMarketingPromo();
    }

    @Override
    public String getMarketingPromoPath() throws FDResourceException {
        return this.user.getMarketingPromoPath();
    }

    @Override
    public boolean isEligibleForPreReservation() throws FDResourceException {
        return this.user.isEligibleForPreReservation();
    }

    @Override
    public EnumServiceType getSelectedServiceType() {
        return this.user.getSelectedServiceType();
    }

    @Override
    public EnumServiceType getUserServiceType() {
        return user.getUserServiceType();
    }

    @Override
    public EnumServiceType getZPServiceType() {
        return this.user.getZPServiceType();
    }

    @Override
    public void setZPServiceType(EnumServiceType serviceType) {
        this.user.setZPServiceType(serviceType);
    }

    @Override
    public void setSelectedServiceType(EnumServiceType serviceType) {
        this.user.setSelectedServiceType(serviceType);
    }

    public void setAvailableServices(Set availableServices) {
        user.setAvailableServices(availableServices);
    }

    @Override
    public String getCustomerServiceContact() {
        return this.user.getCustomerServiceContact();
    }

    @Override
    public String getCustomerServiceContactMediaPath() {
        return this.user.getCustomerServiceContactMediaPath();
    }

    @Override
    public String getCustomerServiceEmail() throws FDResourceException {
        return this.user.getCustomerServiceEmail();
    }

    @Override
    public boolean isCheckEligible() {
        return (user != null) ? user.isCheckEligible() : false;
    }

    @Override
    public Collection<ErpPaymentMethodI> getPaymentMethods() {
        return (user != null) ? user.getPaymentMethods() : Collections.<ErpPaymentMethodI> emptyList();
    }

    @Override
    public String getUserId() {
        return (user != null) ? user.getUserId() : "";
    }

    @Override
    public String getLastRefTrackingCode() {
        return this.user.getLastRefTrackingCode();
    }

    @Override
    public void setLastRefTrackingCode(String lastRefTrackingCode) {
        this.user.setLastRefTrackingCode(lastRefTrackingCode);
    }

    @Override
    public boolean isReferrerRestricted() throws FDResourceException {
        return (user != null) ? user.isReferrerRestricted() : false;
    }

    @Override
    public boolean isReferrerEligible() throws FDResourceException {
        return (user != null) ? user.isReferrerEligible() : false;
    }

    @Override
    public boolean isECheckRestricted() throws FDResourceException {
        return (user != null) ? user.isECheckRestricted() : false;
    }

    public String getRegRefTrackingCode() {
        return (user != null) ? user.getRegRefTrackingCode() : "";
    }

    @Override
    public String getDefaultCounty() throws FDResourceException {
        return (user != null) ? user.getDefaultCounty() : null;
    }

    @Override
    public String getDefaultState() throws FDResourceException {
        return (user != null) ? user.getDefaultState() : null;
    }

    @Override
    public boolean isActive() {
        return user.isActive();
    }

    @Override
    public boolean isReceiveFDEmails() {
        return user.isReceiveFDEmails();
    }

    @Override
    public void performDlvPassStatusCheck() throws FDResourceException {
        user.performDlvPassStatusCheck();
    }

    @Override
    public FDUserDlvPassInfo getDlvPassInfo() {
        return this.user.getDlvPassInfo();
    }

    /**
     * @return Returns the deliveryPassStatus.
     */
    @Override
    public EnumDlvPassStatus getDeliveryPassStatus() {
        return this.user.getDeliveryPassStatus();
    }

    @Override
    public boolean isEligibleForDeliveryPass() throws FDResourceException {
        return this.user.isEligibleForDeliveryPass();
    }

    @Override
    public EnumDlvPassProfileType getEligibleDeliveryPass() throws FDResourceException {
        return this.user.getEligibleDeliveryPass();
    }

    @Override
    public String getDlvPassProfileValue() throws FDResourceException {
        return this.user.getDlvPassProfileValue();
    }

    @Override
    public boolean isDlvPassNone() {
        return this.user.isDlvPassNone();
    }

    @Override
    public boolean isDlvPassActive() {
        return this.user.isDlvPassActive();
    }

    @Override
    public boolean isDlvPassPending() {
        return this.user.isDlvPassPending();
    }

    @Override
    public boolean isDlvPassExpiredPending() {
        return this.user.isDlvPassExpiredPending();
    }

    @Override
    public boolean isDlvPassExpired() {
        return this.user.isDlvPassExpired();
    }

    @Override
    public boolean isDlvPassCancelled() {
        return this.user.isDlvPassCancelled();
    }

    @Override
    public boolean isDlvPassShortShipped() {
        return this.user.isDlvPassShortShipped();
    }

    @Override
    public boolean isDlvPassSettlementFailed() {
        return this.user.isDlvPassSettlementFailed();
    }

    @Override
    public boolean isDlvPassReturned() {
        return this.user.isDlvPassReturned();
    }

    @Override
    public void updateDlvPassInfo() throws FDResourceException {
        user.updateDlvPassInfo();
    }

    @Override
    public void setLastRefProgramId(String progId) {
        this.user.setLastRefProgramId(progId);
    }

    @Override
    public String getLastRefProgId() {
        return this.user.getLastRefProgId();
    }

    @Override
    public void setLastRefTrkDtls(String trkDtls) {
        this.user.setLastRefTrkDtls(trkDtls);
    }

    @Override
    public String getLastRefTrkDtls() {
        return this.user.getLastRefTrkDtls();
    }

    @Override
    public void setLastRefProgInvtId(String progInvtId) {
        this.user.setLastRefProgInvtId(progInvtId);
    }

    @Override
    public String getLastRefProgInvtId() {
        return this.user.getLastRefProgInvtId();
    }

    @Override
    public String getLastName() throws FDResourceException {
        return this.user.getLastName();
    }

    @Override
    public double getBaseDeliveryFee() {
        return this.user.getBaseDeliveryFee();
    }

    @Override
    public double getCorpDeliveryFee() {
        return this.user.getCorpDeliveryFee();
    }

    @Override
    public double getCorpDeliveryFeeMonday() {
        return this.user.getCorpDeliveryFeeMonday();
    }

    @Override
    public double getMinCorpOrderAmount() {
        return this.user.getMinCorpOrderAmount();
    }

    @Override
    public int getUsableDeliveryPassCount() {
        return user.getUsableDeliveryPassCount();
    }

    @Override
    public EnumDPAutoRenewalType hasAutoRenewDP() throws FDResourceException {
        return user.hasAutoRenewDP();
    }

    @Override
    public AssignedCustomerParam getAssignedCustomerParam(String promoId) {
        return user.getAssignedCustomerParam(promoId);
    }

    @Override
    public boolean isGiftCardsEnabled() {
        return this.user.isGiftCardsEnabled();
    }

    @Override
    public boolean isCCLEnabled() {
        return this.user.isCCLEnabled();
    }

    @Override
    public boolean isCCLInExperienced() {
        return this.user.isCCLInExperienced();
    }

    @Override
    public List<FDCustomerListInfo> getCustomerCreatedListInfos() {
        return this.user.getCustomerCreatedListInfos();
    }

    @Override
    public List<FDCustomerListInfo> getStandingOrderListInfos() {
        return this.user.getStandingOrderListInfos();
    }

    @Override
    public DCPDPromoProductCache getDCPDPromoProductCache() {
        return this.user.getDCPDPromoProductCache();
    }

    @Override
    public ErpPromotionHistory getPromotionHistory() throws FDResourceException {
        return this.user.getPromotionHistory();
    }

    /**
     * This method was introduced as part of PERF-22 task. Separate invalidation of Order History Cache from other caches.
     */
    @Override
    public void invalidateOrderHistoryCache() {
        this.user.invalidateOrderHistoryCache();
    }

    @Override
    public int getAdjustedValidECheckOrderCount() throws FDResourceException {
        return this.user.getAdjustedValidECheckOrderCount();
    }

    @Override
    public boolean isDYFEnabled() {
        return user.isDYFEnabled();
    }

    @Override
    public boolean isProduceRatingEnabled() {
        return user.isProduceRatingEnabled();
    }

    @Override
    public boolean isHomePageLetterVisited() {
        return user.isHomePageLetterVisited();
    }

    @Override
    public void setHomePageLetterVisited(boolean isHomePageLetterVisited) {
        user.setHomePageLetterVisited(isHomePageLetterVisited);
    }

    @Override
    public boolean isCampaignMsgLimitViewed() {
        return user.isCampaignMsgLimitViewed();
    }

    @Override
    public int getCampaignMsgViewed() {
        return user.getCampaignMsgViewed();
    }

    @Override
    public void setCampaignMsgViewed(int campaignMsgViewed) {
        user.setCampaignMsgViewed(campaignMsgViewed);
    }

    @Override
    public boolean hasServiceBasedOnUserAddress(EnumServiceType type) {
        return user.hasServiceBasedOnUserAddress(type);
    }

    public static FDUserI getFDSessionUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (FDUserI) session.getAttribute(SessionName.USER);
    }

    @Override
    public String getCohortName() {
        return this.user.getCohortName();
    }

    @Override
    public void setCohortName(String cohortName) {
        this.user.setCohortName(cohortName);
    }

    @Override
    public int getTotalCartSkuQuantity(String[] args) {
        return user.getTotalCartSkuQuantity(args);
    }

    @Override
    public String getFavoriteTabFeature() {
        return tabSiteFeature;
    }

    @Override
    public void setFavoriteTabFeature(String feature) {
        this.tabSiteFeature = feature;
    }

    @Override
    public Map getPromoVariantMap() {
        return this.user.getPromoVariantMap();
    }

    @Override
    public void setPromoVariantMap(Map pvMap) {
        user.setPromoVariantMap(pvMap);
    }

    @Override
    public String getSavingsVariantId() {
        return this.user.getSavingsVariantId();
    }

    @Override
    public void setSavingsVariantId(String savingsVariantId) {
        this.user.setSavingsVariantId(savingsVariantId);
    }

    @Override
    public boolean isSavingsVariantFound() {
        return this.user.isSavingsVariantFound();
    }

    @Override
    public void setSavingsVariantFound(boolean savingsVariantFound) {
        this.user.setSavingsVariantFound(savingsVariantFound);
    }

    @Override
    public PromoVariantModel getPromoVariant(String variantId) {
        return user.getPromoVariant(variantId);
    }

    @Override
    public boolean isPostPromoConflictEnabled() {
        return user.isPostPromoConflictEnabled();
    }

    @Override
    public void setPostPromoConflictEnabled(boolean isPostPromoConflictEnabled) {
        user.setPostPromoConflictEnabled(isPostPromoConflictEnabled);
    }

    @Override
    public void setPromotionAddressMismatch(boolean b) {
        user.setPromotionAddressMismatch(b);
    }

    @Override
    public void setSignupDiscountRule(SignupDiscountRule discountRule) {
        user.setSignupDiscountRule(discountRule);
    }

    @Override
    public boolean isPromoConflictResolutionApplied() {
        return user.isPromoConflictResolutionApplied();
    }

    @Override
    public void setPromoConflictResolutionApplied(boolean isPromoConflictResolutionApplied) {
        user.setPromoConflictResolutionApplied(isPromoConflictResolutionApplied);
    }

    @Override
    public FDGiftCardInfoList getGiftCardList() {
        return user.getGiftCardList();
    }

    @Override
    public FDCartModel getGiftCart() {
        return this.user.getGiftCart();
    }

    @Override
    public FDRecipientList getRecipientList() {
        return this.user.getRecipientList();
    }

    @Override
    public void setRecipientList(FDRecipientList r) {
        this.user.setRecipientList(r);
    }

    @Override
    public void setGiftCart(FDCartModel dcart) {
        this.user.setGiftCart(dcart);
    }

    @Override
    public double getGiftcardBalance() {
        return this.user.getGiftcardBalance();
    }

    @Override
    public boolean isGCOrderMinimumMet() {
        return this.user.isGCOrderMinimumMet();
    }

    @Override
    public double getGCMinimumOrderAmount() {
        return this.user.getGCMinimumOrderAmount();
    }

    @Override
    public void invalidateGiftCards() {
        this.user.invalidateGiftCards();
    }

    @Override
    public FDBulkRecipientList getBulkRecipentList() {
        return user.getBulkRecipentList();
    }

    @Override
    public void setBulkRecipientList(FDBulkRecipientList r) {
        user.setBulkRecipientList(r);
    }

    public int getGCRetryCount() {
        return this.gcRetryCount;
    }

    public void incrementGCRetryCount() {
        this.gcRetryCount++;
    }

    public void resetGCRetryCount() {
        this.gcRetryCount = 0;
    }

    public ErpPaymentMethodI getInvalidPaymentMethod() {
        return invalidPaymentMethod;
    }

    public void setInvalidPaymentMethod(ErpPaymentMethodI invalidPaymentMethod) {
        this.invalidPaymentMethod = invalidPaymentMethod;
    }

    public boolean isGCSignupError() {
        return gcSignupError;
    }

    public void setGCSignupError(boolean gcSignupError) {
        this.gcSignupError = gcSignupError;
    }

    public String getGcFraudReason() {
        return gcFraudReason;
    }

    public void setGcFraudReason(String gcFraudReason) {
        this.gcFraudReason = gcFraudReason;
    }

    public String getLastSenderName() {
        return lastSenderName;
    }

    public void setLastSenderName(String lastSenderName) {
        this.lastSenderName = lastSenderName;
    }

    public String getLastSenderEmail() {
        return lastSenderEmail;
    }

    public void setLastSenderEmail(String lastSenderEmail) {
        this.lastSenderEmail = lastSenderEmail;
    }

    public boolean isLastRecipAdded() {
        return lastRecipAdded;
    }

    public void setLastRecipAdded(boolean lastRecipAdded) {
        this.lastRecipAdded = lastRecipAdded;
    }

    @Override
    public Integer getDonationTotalQuantity() {
        return this.user.getDonationTotalQuantity();
    }

    @Override
    public void setDonationTotalQuantity(Integer donationTotalQuantity) {
        this.user.setDonationTotalQuantity(donationTotalQuantity);
    }

    @Override
    public FDCartModel getDonationCart() {
        return this.user.getDonationCart();
    }

    @Override
    public void setDonationCart(FDCartModel dcart) {
        this.user.setDonationCart(dcart);
    }

    @Override
    public double getGiftcardsTotalBalance() {
        return this.user.getGiftcardsTotalBalance();
    }

    @Override
    public String getGCSenderName(String certNum, String custId) {
        return this.user.getGCSenderName(certNum, custId);
    }

    public int getTotalRegularOrderCount() throws FDResourceException {
        return this.user.getTotalRegularOrderCount();
    }

    /** Is customer eligible for Standing Orders service? */
    @Override
    public boolean isEligibleForStandingOrders() {
        return user.isEligibleForStandingOrders();
    }

    @Override
    public boolean isEligibleForClientCodes() {
        return user.isEligibleForClientCodes();
    }

    @Override
    public FDStandingOrder getCurrentStandingOrder() {
        return currentStandingOrder;
    }

    @Override
    public void setCurrentStandingOrder(FDStandingOrder currentStandingOrder) {
        this.currentStandingOrder = currentStandingOrder;
    }

    @Override
    public EnumCheckoutMode getCheckoutMode() {
        return checkoutMode;
    }

    public void setCheckoutMode(EnumCheckoutMode mode) {
        this.checkoutMode = mode;
    }

    /** use getUserContext().getPricingContext().getZoneId() instead */
    @Override
    @Deprecated
    public String getPricingZoneId() {
        return "";
    }

    /** use getUserContext().getPricingContext() instead */
    @Override
    @Deprecated
    public PricingContext getPricingContext() {
        return this.user.getPricingContext();
    }

    /** use getUserContext().resetPricingContext() instead */
    @Override
    @Deprecated
    public void resetPricingContext() {
        this.user.resetPricingContext();
    }

    @Override
    public UserContext getUserContext() {
        return user.getUserContext();
    }

    public UserContext getUserContext(boolean override) {
        return user.getUserContext(override);
    }

    @Override
    public SortedSet<IgnoreCaseString> getClientCodesHistory() {
        return user.getClientCodesHistory();
    }

    @Override
    public Set<String> getAllAppliedPromos() {
        return this.getUser().getAllAppliedPromos();
    }

    @Override
    public void clearAllAppliedPromos() {
        this.user.clearAllAppliedPromos();
    }

    @Override
    public void addPromoErrorCode(String promoCode, int errorCode) {
        this.user.addPromoErrorCode(promoCode, errorCode);
    }

    @Override
    public int getPromoErrorCode(String promoCode) {
        return this.user.getPromoErrorCode(promoCode);
    }

    @Override
    public void clearPromoErrorCodes() {
        this.user.clearPromoErrorCodes();
    }

    @Override
    public MasqueradeContext getMasqueradeContext() {
        return user.getMasqueradeContext();
    }

    @Override
    public void setMasqueradeContext(MasqueradeContext ctx) {
        user.setMasqueradeContext(ctx);
    }

    @Override
    @Deprecated
    public EnumWinePrice getPreferredWinePrice() {
        return user.getPreferredWinePrice();
    }

    @Override
    public int getTotalCTSlots() {
        return this.user.getTotalCTSlots();
    }

    @Override
    public void setTotalCTSlots(int slots) {
        this.user.setTotalCTSlots(slots);
    }

    @Override
    public Date getRegistrationDate() {
        return user.getRegistrationDate();
    }

    @Override
    public double getPercSlotsSold() {
        // TODO Auto-generated method stub
        return user.getPercSlotsSold();
    }

    @Override
    public void setPercSlotsSold(double percSlotsSold) {
        // TODO Auto-generated method stub
        this.user.setPercSlotsSold(percSlotsSold);
    }

    @Override
    public SessionEvent getSessionEvent() {
        // TODO Auto-generated method stub
        return user.getSessionEvent();
    }

    @Override
    public void setSessionEvent(SessionEvent event) {
        // TODO Auto-generated method stub
        this.user.setSessionEvent(event);
    }

    // mergePendingOrder (APPDEV-2031)

    @Override
    public boolean getShowPendingOrderOverlay() {
        return this.user.getShowPendingOrderOverlay();
    }

    @Override
    public void setShowPendingOrderOverlay(boolean showPendingOrderOverlay) {
        this.user.setShowPendingOrderOverlay(showPendingOrderOverlay);
    }

    @Override
    public boolean isSupendShowPendingOrderOverlay() {
        return user.isSupendShowPendingOrderOverlay();
    }

    @Override
    public void setSuspendShowPendingOrderOverlay(boolean suspendShowPendingOrderOverlay) {
        user.setSuspendShowPendingOrderOverlay(suspendShowPendingOrderOverlay);
    }

    @Override
    public boolean hasPendingOrder() throws FDResourceException {
        return this.user.hasPendingOrder();
    }

    @Override
    public boolean hasPendingOrder(boolean incGiftCardOrds, boolean incDonationOrds) throws FDResourceException {
        return this.user.hasPendingOrder(incGiftCardOrds, incDonationOrds);
    }

    @Override
    public List<FDOrderInfoI> getPendingOrders() throws FDResourceException {
        return this.user.getPendingOrders();
    }

    @Override
    public List<FDOrderInfoI> getPendingOrders(boolean incGiftCardOrds, boolean incDonationOrds, boolean sorted) throws FDResourceException {
        return this.user.getPendingOrders(incGiftCardOrds, incDonationOrds, sorted);
    }

    @Override
    public FDCartModel getMergePendCart() {
        return this.user.getMergePendCart();
    }

    @Override
    public void setMergePendCart(FDCartModel mergePendCart) {
        this.user.setMergePendCart(mergePendCart);
    }

    @Override
    public void setReferralLink() {
        this.user.setReferralLink();
    }

    @Override
    public String getReferralLink() {
        return this.user.getReferralLink();
    }

    @Override
    public void setReferralPrgmId(String referralPrgmId) {
        this.user.setReferralPrgmId(referralPrgmId);
    }

    @Override
    public String getReferralPrgmId() {
        return this.user.getReferralPrgmId();
    }

    @Override
    public void setReferralCustomerId(String referralCustomerId) {
        this.user.setReferralCustomerId(referralCustomerId);
    }

    @Override
    public String getReferralCustomerId() {
        return this.user.getReferralCustomerId();
    }

    @Override
    public List<PromotionI> getReferralPromoList() {
        return this.user.getReferralPromoList();
    }

    @Override
    public void setReferralPromoList() {
        this.user.setReferralPromoList();
    }

    @Override
    public double getAvailableCredit() {
        return this.user.getAvailableCredit();
    }

    @Override
    public boolean isReferralProgramAvailable() {
        return this.user.isReferralProgramAvailable();
    }

    @Override
    public void setReferralPromotionFraud(boolean fraud) {
        this.user.setReferralPromotionFraud(fraud);
    }

    @Override
    public boolean isReferralPromotionFraud() {
        return this.user.isReferralPromotionFraud();
    }

    public boolean isEligibleForDDPP() {
        try {
            return this.user.isEligibleForDDPP();
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e);
        }
    }

    @Override
    public String getTsaPromoCode() {
        return this.user.getTsaPromoCode();
    }

    @Override
    public void setTsaPromoCode(String tsaPromoCode) {
        this.user.setTsaPromoCode(tsaPromoCode);
    }

    @Override
    public boolean isPopUpPendingOrderOverlay() {
        return user.isPopUpPendingOrderOverlay();
    }

    @Override
    public EnumGiftCardType getGiftCardType() {
        return this.user.getGiftCardType();
    }

    @Override
    public void setGiftCardType(EnumGiftCardType giftCardType) {
        this.user.setGiftCardType(giftCardType);
    }

    @Override
    public boolean isEbtAccepted() {
        return this.user.isEbtAccepted();
    }

    @Override
    public void setEbtAccepted(boolean ebtAccepted) {
        this.user.setEbtAccepted(ebtAccepted);
    }

    @Override
    public boolean isDpNewTcBlocking() {
        return this.user.isDpNewTcBlocking(true);
    }

    @Override
    public boolean isDpNewTcBlocking(boolean includeViewCount) {
        return this.user.isDpNewTcBlocking(includeViewCount);
    }

    @Override
    public boolean isWaiveDPFuelSurCharge(boolean includeViewCount) {
        return this.user.isWaiveDPFuelSurCharge(includeViewCount);
    }

    public boolean hasSeenDpNewTc() {
        return seenDpNewTc;
    }

    public void setSeenDpNewTc(boolean seenDpNewTc) {
        this.seenDpNewTc = seenDpNewTc;
    }

    @Override
    public boolean hasEBTAlert() {
        return this.user.hasEBTAlert();
    }

    @Override
    public Set<String> getSteeringSlotIds() {
        return this.user.getSteeringSlotIds();
    }

    @Override
    public void setSteeringSlotIds(Set<String> steeringSlotIds) {
        this.user.setSteeringSlotIds(steeringSlotIds);
    }

    @Override
    public List<ErpAddressModel> getAllHomeAddresses() throws FDResourceException {
        return user.getAllHomeAddresses();
    }

    @Override
    public List<ErpAddressModel> getAllCorporateAddresses() throws FDResourceException {
        return user.getAllCorporateAddresses();
    }

    @Override
    public void invalidateAllAddressesCaches() {
        user.invalidateAllAddressesCaches();
    }

    public boolean isUserCreatedInThisSession() {
        return userCreatedInThisSession;
    }

    public void setUserCreatedInThisSession(boolean userCreatedInThisSession) {
        this.userCreatedInThisSession = userCreatedInThisSession;
    }

    @Override
    public AddressModel getSelectedAddress() {
        return user.getSelectedAddress();
    }

    @Override
    public EnumDeliveryStatus getDeliveryStatus() throws FDResourceException {
        return user.getDeliveryStatus();
    }

    public boolean isFutureZoneNotificationEmailSentForCurrentAddress() {
        return futureZoneNotificationEmailSentForCurrentAddress;
    }

    public void setFutureZoneNotificationEmailSentForCurrentAddress(boolean futureZoneNotificationEmailSentForCurrentAddress) {
        this.futureZoneNotificationEmailSentForCurrentAddress = futureZoneNotificationEmailSentForCurrentAddress;
    }

    public boolean isMoreInfoPopupShownForCurrentAddress() {
        return moreInfoPopupShownForCurrentAddress;
    }

    public void setMoreInfoPopupShownForCurrentAddress(boolean moreInfoPopupShownForCurrentAddress) {
        this.moreInfoPopupShownForCurrentAddress = moreInfoPopupShownForCurrentAddress;
    }

    public boolean isNewUserWelcomePageShown() {
        return newUserWelcomePageShown;
    }

    public void setNewUserWelcomePageShown(boolean newUserWelcomePageShown) {
        this.newUserWelcomePageShown = newUserWelcomePageShown;
    }

    @Override
    public Set<ExternalCampaign> getExternalPromoCampaigns() {
        return this.user.getExternalPromoCampaigns();
    }

    @Override
    public void setExternalCampaign(ExternalCampaign campaign) {
        this.user.setExternalCampaign(campaign);
    }

    @Override
    public ExternalCampaign getExternalCampaign() {
        return this.user.getExternalCampaign();
    }

    @Override
    public void setExternalPromoCampaigns(Set<ExternalCampaign> externalCampaigns) {
        this.user.setExternalPromoCampaigns(externalCampaigns);
    }

    @Override
    public String getDefaultListId() {
        return user.getDefaultListId();
    }

    @Override
    public void setDefaultListId(String listId) {
        user.setDefaultListId(listId);
    }

    @Override
    public boolean isRobot() {
        return user.isRobot();
    }

    @Override
    public void setRobot(boolean robot) {
        user.setRobot(robot);
    }

    @Override
    public FDCustomerCouponWallet getCouponWallet() {
        return this.user.getCouponWallet();
    }

    @Override
    public void setCouponWallet(FDCustomerCouponWallet couponWallet) {
        this.user.setCouponWallet(couponWallet);
    }

    @Override
    public FDCustomerCoupon getCustomerCoupon(String upc, EnumCouponContext ctx) {
        return this.user.getCustomerCoupon(upc, ctx);
    }

    @Override
    public FDCustomerCoupon getCustomerCoupon(FDCartLineI cartLine, EnumCouponContext ctx) {
        return this.user.getCustomerCoupon(cartLine, ctx);
    }

    @Override
    public FDCustomerCoupon getCustomerCoupon(FDProductInfo prodInfo, EnumCouponContext ctx, String catId, String prodId) {
        return this.user.getCustomerCoupon(prodInfo, ctx, catId, prodId);
    }

    @Override
    public FDCustomerCoupon getCustomerCoupon(FDCartLineI cartLine, EnumCouponContext ctx, String catId, String prodId) {
        return this.user.getCustomerCoupon(cartLine, ctx, catId, prodId);
    }

    @Override
    public void updateClippedCoupon(String couponId) {
        this.user.updateClippedCoupon(couponId);
    }

    @Override
    public boolean isEligibleForCoupons() {
        try {
            return !user.isRobot() && this.user.isEligibleForCoupons();
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e);
        }
    }

    @Override
    public boolean isCouponsSystemAvailable() {
        try {
            return this.user.isCouponsSystemAvailable();
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e);
        }
    }

    public boolean isCouponWarningAcknowledged() {
        return couponWarningAcknowledged;
    }

    public void setCouponWarningAcknowledged(boolean couponWarningAcknowledged) {
        this.couponWarningAcknowledged = couponWarningAcknowledged;
    }

    @Override
    public boolean isCouponEvaluationRequired() {
        return this.user.isCouponEvaluationRequired();
    }

    @Override
    public void setCouponEvaluationRequired(boolean couponEvaluationRequired) {
        this.user.setCouponEvaluationRequired(couponEvaluationRequired);
    }

    @Override
    public boolean isRefreshCouponWalletRequired() {
        return this.user.isRefreshCouponWalletRequired();
    }

    @Override
    public void setRefreshCouponWalletRequired(boolean refreshCouponWalletRequired) {
        this.user.setRefreshCouponWalletRequired(refreshCouponWalletRequired);
    }

    @Override
    public EnumRegionServiceType getRegionSvcType(String addressId) {
        return this.user.getRegionSvcType(addressId);
    }

    @Override
    public boolean isPaymentechEnabled() {
        return this.user.isPaymentechEnabled();
    }

    @Override
    public double getMinHomeOrderAmount() {
        return this.user.getMinHomeOrderAmount();
    }

    @Override
    public void applyOrderMinimum() {
        this.user.applyOrderMinimum();
    }

    @Override
    public boolean isAnyNewOrder() {
        // TODO Auto-generated method stub
        return this.user.isAnyNewOrder();
    }

    @Override
    public void setAnyNewOrder(boolean anyNewOrder) {
        this.user.setAnyNewOrder(anyNewOrder);

    }

    @Override
    public String getClientIp() {
        return this.user.getClientIp();
    }

    public void setClientIp(String clientIp) {
        this.user.setClientIp(clientIp);
    }

    @Override
    public String getServerName() {
        return this.user.getServerName();
    }

    public void setServerName(String serverName) {
        this.user.setServerName(serverName);
    }

    public Set<ContentKey> getCheckoutUnavailableProductKeys() {
        return checkoutUnavailableProductKeys;
    }

    public void setCheckoutUnavailableProductKeys(Set<ContentKey> checkoutUnavailableProductKeys) {
        this.checkoutUnavailableProductKeys = checkoutUnavailableProductKeys;
    }

    /* cleared on first fetch if false isn't passed in to method */
    /* fetch with no clearing */
    @Override
    public boolean hasJustLoggedIn() {
        return hasJustLoggedIn(true);
    }

    /* clear after fetching value */
    @Override
    public boolean hasJustLoggedIn(boolean clear) {
        boolean preClear = justLoggedIn;

        if (clear) {
            this.setJustLoggedIn(false);
        }

        return preClear;
    }

    @Override
    public void setJustLoggedIn(boolean val) {
        this.justLoggedIn = val;
    }

    /* cleared on first fetch if false isn't passed in to method */
    /* fetch with no clearing */
    @Override
    public boolean hasJustSignedUp() {
        return hasJustSignedUp(true);
    }

    /* clear after fetching value */
    @Override
    public boolean hasJustSignedUp(boolean clear) {
        boolean preClear = justSignedUp;

        if (clear) {
            this.setJustSignedUp(false);
        }

        return preClear;
    }

    @Override
    public void setJustSignedUp(boolean val) {
        this.justSignedUp = val;
    }

    public Map<String, List<AddToCartItem>> getPendingExternalAtcItems() {
        return pendingExternalAtcItems;
    }

    public void setPendingExternalAtcItems(Map<String, List<AddToCartItem>> pendingExternalAtcItems) {
        this.pendingExternalAtcItems = pendingExternalAtcItems;
    }

    @Override
    public boolean isGlobalNavTutorialSeen() {
        return user.isGlobalNavTutorialSeen();
    }

    @Override
    public void setGlobalNavTutorialSeen(boolean isGlobalNavTutorialSeen) {
        user.setGlobalNavTutorialSeen(isGlobalNavTutorialSeen);
    }

    @Override
    public List<FDOrderInfoI> getScheduledOrdersForDelivery(boolean sorted) throws FDResourceException {
        return this.user.getScheduledOrdersForDelivery(sorted);
    }

    @Override
    public void resetUserContext() {
        this.user.resetUserContext();

    }

    @Override
    public void setRafPromoCode(String rafPromoCode) {
        this.user.setRafPromoCode(rafPromoCode);

    }

    @Override
    public String getRafPromoCode() {

        return this.user.getRafPromoCode();
    }

    @Override
    public void setRafClickId(String rafClickId) {

        this.user.setRafClickId(rafClickId);
    }

    @Override
    public String getRafClickId() {
        // TODO Auto-generated method stub
        return this.user.getRafClickId();
    }

    @Override
    public boolean isCrmMode() {
        return user.isCrmMode();
    }

    @Override
    public void setCrmMode(boolean flag) {
        user.setCrmMode(flag);
    }

    @Override
    public List<ProductReference> getProductSamples() {
        return user.getProductSamples();
    }

    @Override
    public void setProductSample(List<ProductReference> productSamples) {
        user.setProductSample(productSamples);
    }

    @Override
    public boolean isProductSample(ProductReference prodRef) {
        return user.isProductSample(prodRef);
    }

    @Override
    public boolean getTcAcknowledge() {

        return this.user.getTcAcknowledge();
    }

    public void setTcAcknowledge(boolean acknowledge) {

        this.user.setTcAcknowledge(acknowledge);
    }

    @Override
    public Date getFirstOrderDate() throws FDResourceException {
        return this.user.getFirstOrderDate();
    }

    @Override
    public boolean isVHInDelivery() throws FDResourceException {
        return this.user.isVHInDelivery();

    }

    @Override
    public boolean isVHOutOfDelivery() throws FDResourceException {
        return this.user.isVHOutOfDelivery();
    }

    @Override
    public boolean isVoucherHolder() throws FDResourceException {
        return this.isVHInDelivery() || this.isVHOutOfDelivery();
    }

    @Override
    public boolean isVHPopupDisplay() {
        // TODO Auto-generated method stub
        return user.isVHPopupDisplay();
    }

    @Override
    public void setVHPopupDisplay(boolean flag) {
        user.setVHPopupDisplay(flag);
    }

    @Override
    public Date getFirstOrderDateByStore(EnumEStoreId eStoreId) throws FDResourceException {
        return user.getFirstOrderDateByStore(eStoreId);
    }

    public boolean isRafFriendSignedUp() {
        return isRafFriendSignedUp(true);// clears the flag on the first fetch.
    }

    public boolean isRafFriendSignedUp(boolean clear) {
        boolean preClear = rafFriendSignedUp;

        if (clear) {
            this.setRafFriendSignedUp(false);
        }
        return preClear;
    }

    public void setRafFriendSignedUp(boolean rafFriendSignedUp) {
        this.rafFriendSignedUp = rafFriendSignedUp;
    }

    public boolean isAvalaraTaxed() {
        return this.isAvalaraTaxed;
    }

    public void setIsAvalaraTaxed(boolean isAvalaraTaxed) {
        this.isAvalaraTaxed = isAvalaraTaxed;
    }

    @Override
    public FDCartModel getSoTemplateCart() {
        return this.user.getSoTemplateCart();
    }

    @Override
    public void setSoTemplateCart(FDCartModel soTemplateCart) {
        this.user.setSoTemplateCart(soTemplateCart);
    }

    @Override
    public boolean isCustomerHasStandingOrders() {
        return user.isCustomerHasStandingOrders();
    }

    @Override
    public boolean isNewSO3Enabled() {
        // TODO Auto-generated method stub
        return user.isNewSO3Enabled();
    }

    @Override
    public Collection<FDStandingOrder> getValidSO3() {

        return user.getValidSO3();
    }

    @Override
    public void setValidSO3(Collection<FDStandingOrder> validSO3s) {

        this.user.setValidSO3(validSO3s);
    }

    @Override
    public Collection<FDStandingOrder> getAllSO3() {
        return this.user.getAllSO3();
    }

    @Override
    public void setAllSO3(Collection<FDStandingOrder> allSO3s) {
        this.user.setAllSO3(allSO3s);
    }

    @Override
    public boolean isRefreshSO3() {
        // TODO Auto-generated method stub
        return user.isRefreshSO3();
    }

    @Override
    public void setRefreshSO3(boolean isRefreshSO3) {
        this.user.setRefreshSO3(isRefreshSO3);

    }

    @Override
    public Collection<FDStandingOrder> getActiveSO3s() {
        return user.getActiveSO3s();
    }

    @Override
    public void setActiveSO3s(Collection<FDStandingOrder> validSO3s) {
        this.user.setActiveSO3s(validSO3s);
    }

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public boolean isMobilePlatForm() {
        return isMobilePlatForm;
    }

    public void setMobilePlatForm(boolean isMobilePlatForm) {
        this.isMobilePlatForm = isMobilePlatForm;
    }

    @Override
    public boolean isZipCheckPopupUsed() {
        return user.isZipCheckPopupUsed();
    }

    public void setZipCheckPopupUsed(boolean isZipCheckPopupUsed) {
        user.setZipCheckPopupUsed(isZipCheckPopupUsed);
    }

    public boolean isZipPopupSeenInSession() {
        return zipPopupSeenInSession;
    }

    public void setZipPopupSeenInSession(boolean ZipPopupSeenInSession) {
        this.zipPopupSeenInSession = ZipPopupSeenInSession;
    }

    @Override
    public Map<String, String> getSoCartLineMessagesMap() {
		return soCartLineMessagesMap;
	}

    @Override
    public void setSoCartLineMessagesMap(Map<String, String> soCartLineMessagesMap) {
		this.soCartLineMessagesMap = soCartLineMessagesMap;
	}

    @Override
    public boolean isSoCartOverlayFirstTime() {
		return soCartOverlayFirstTime;
	}

    @Override
    public void setSoCartOverlayFirstTime(boolean soCartOverlayFirstTime) {
		this.soCartOverlayFirstTime = soCartOverlayFirstTime;
	}

    @Override
    public boolean isRefreshSoCartOverlay() {
		return isRefreshSoCartOverlay;
	}

    @Override
    public void setRefreshSoCartOverlay(boolean isRefreshSoCartOverlay) {
		this.isRefreshSoCartOverlay = isRefreshSoCartOverlay;
	}

    @Override
    public boolean isSoFeatureOverlay() {
		return soFeatureOverlay;
	}

    @Override
    public void setSoFeatureOverlay(boolean soFeatureOverlay) {
		this.soFeatureOverlay = soFeatureOverlay;
	}

    @Override
    public boolean isRefreshNewSoFeature() {
		return isRefreshNewSoFeature;
	}

    @Override
    public void setRefreshNewSoFeature(boolean isRefreshNewSoFeature) {
		this.isRefreshNewSoFeature = isRefreshNewSoFeature;
	}

	public String isFromLogin() {
		return user.isFromLogin();
	}


	public void setFromLogin(String fromLogin) {
		this.user.setFromLogin(fromLogin);
	}

	@Override
    public void setValidSO3Data(Map<String, Object> validSO3Data){
		this.user.setValidSO3Data(validSO3Data);
	}

    @Override
    public Map<String, Object> getValidSO3Data(){
    	return this.user.getValidSO3Data();
    }

    @Override
    public boolean isRefreshSO3Settings(){
    	return this.user.isRefreshSO3Settings();
    }

    @Override
    public void setRefreshSO3Settings(boolean isRefreshSO3Settings){
    	this.user.setRefreshSO3Settings(isRefreshSO3Settings);
    }

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getPdUserId() {
		return pdUserId;
	}

	public void setPdUserId(String pdUserId) {
		this.pdUserId = pdUserId;
	}


	@Override
	public ErpCustomerInfoModel getCustomerInfoModel() throws FDResourceException {
		return this.user.getCustomerInfoModel();
	}
	@Override
	public int resetDefaultPaymentValueType() {
		return user.resetDefaultPaymentValueType();
	}

	@Override
	public void refreshFdCustomer() throws FDResourceException{
		user.refreshFdCustomer();
	}

	@Override
	public FDDeliveryZoneInfo overrideZoneInfo(ErpAddressModel address,
			FDDeliveryZoneInfo deliveryZoneInfo) throws FDResourceException,
			FDInvalidAddressException {
		return user.overrideZoneInfo(address, deliveryZoneInfo);
	}

	@Override
	public boolean getDpFreeTrialOptin() {
		return user.getDpFreeTrialOptin();
	}

	@Override
	public void setDpFreeTrialOptin(boolean dpFreeTrialOptin) {
		this.user.setDpFreeTrialOptin(dpFreeTrialOptin);

	}

	@Override
	public String getDpFreeTrialOptinStDate() throws FDResourceException {
		return user.getDpFreeTrialOptinStDate();
	}

	@Override
	public void updateDpFreeTrialOptin(boolean dpFreeTrialOptin) {
		this.user.updateDpFreeTrialOptin(dpFreeTrialOptin);
	}

	@Override
	public boolean isDPFreeTrialOptInEligible()
	{
		return this.user.isDPFreeTrialOptInEligible();
	}

	@Override
    public boolean applyFreeTrailOptinBasedDP() {
		return this.user.applyFreeTrailOptinBasedDP();
	}

	@Override
	public int getInformOrderModifyViewCount() { /* current estore, auto increment */
		return this.user.getInformOrderModifyViewCount(null, true);
	}
	
	@Override
	public int getInformOrderModifyViewCount(EnumEStoreId EStore) { /* auto increment */
		return this.user.getInformOrderModifyViewCount(EStore, true);
	}

	@Override
	public int getInformOrderModifyViewCount(EnumEStoreId EStore, boolean increment) {
		return this.user.getInformOrderModifyViewCount(EStore, increment);
	}

	@Override
	public void setInformOrderModifyViewCount(EnumEStoreId eStore, int newValue) {
		this.user.setInformOrderModifyViewCount(eStore, newValue);
	}

    public boolean isShowingInformOrderModify() {
		return showInformOrderModify;
	}

	public void setShowingInformOrderModify(boolean showInformOrderModify) {
		this.showInformOrderModify = showInformOrderModify;
	}
}
