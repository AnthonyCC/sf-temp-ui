package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.deliverypass.EnumDPAutoRenewalType;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.EnumWinePrice;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.DCPDPromoProductCache;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.SignupDiscountRule;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.util.IgnoreCaseString;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionImpressionLogEntry;
import com.freshdirect.smartstore.fdstore.SessionImpressionLog;


public class FDSessionUser implements FDUserI, HttpSessionBindingListener {
	
	private static final long serialVersionUID = 587469031501334715L;

	private static Logger LOGGER = LoggerFactory.getInstance( FDSessionUser.class );

    private final FDUser user;

    private long lastCartSaveTime;

    private final static long SAVE_PERIOD = 2 * 60 * 1000; // 2 minutes

	private int failedAuthorizations = 0;
	private boolean healthWarningAcknowledged = false;

	private Date startDate;
	private long lastRequestDate;
	private Map<String,SessionImpressionLogEntry> impressions = new ConcurrentHashMap<String,SessionImpressionLogEntry>();

	private String sessionId = null;

	private String tabSiteFeature = null;
	
	//No. of Gift Card apply attempts
	private int gcRetryCount = 0;
	
	private String lastSenderName = null;
	private String lastSenderEmail = null;
	
	private boolean lastRecipAdded;
	
	//Invalid Payment Method on Signup.
	
	private ErpPaymentMethodI invalidPaymentMethod;
	private boolean gcSignupError;
	private String gcFraudReason;
	
	private boolean isAddressVerificationError;
    private String addressVerficationMsg;
    
    private List<String> oneTimeGCPaymentError=null;
    
    //Vending changes
    private boolean lastCOSSurveySuccess;	//holds if survey was success
    private String lastCOSSurvey = null;	//holds name of survey
    
	public void setLastCOSSurveySuccess(boolean lastCOSSurveySuccess) {
		this.lastCOSSurveySuccess = lastCOSSurveySuccess;
	}
	
	private FDStandingOrder currentStandingOrder;

	private EnumCheckoutMode checkoutMode = EnumCheckoutMode.NORMAL;
	
	
	
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
	        } //else get defaulted to website
	    }
		this.user.setApplication(src);

        lastCartSaveTime = System.currentTimeMillis();
    }

    public void valueBound(HttpSessionBindingEvent event) {
        LOGGER.debug("FDUser bound to session " + event.getSession().getId());
        this.saveCart();
        this.impressions.clear();
        this.startDate = new Date();
        this.lastRequestDate = startDate.getTime();
        sessionId = event.getSession().getId();

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

    public void valueUnbound(HttpSessionBindingEvent event) {
        LOGGER.debug("FDUser unbound from session " + event.getSession().getId());
        this.saveCart(true);
        this.saveImpressions();
        
        // clear masquerade agent, and log this event
        String masqueradeAgent = getMasqueradeAgent();
        
        if ( masqueradeAgent != null ) {
        	logMasqueradeLogout( masqueradeAgent );
        }
        
        sessionId = null;
    }
    
    private void logMasqueradeLogout( String masqueradeAgent ) {
        LOGGER.debug( "logMasqueradeLogout()" );
    	try {
    		FDActionInfo ai = new FDActionInfo( EnumTransactionSource.WEBSITE, this.getIdentity(), "Masquerade logout", masqueradeAgent + " logged out as " + this.getUserId(), null, EnumAccountActivityType.MASQUERADE_LOGOUT );
    		ActivityLog.getInstance().logActivity( ai.createActivity() );
    	} catch ( FDResourceException ex ) {
    		LOGGER.error( "Activity logging failed due to FDResourceException.", ex );
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
                logEntries.get(i).setZoneId(user.getPricingZoneId());
                System.out.println("SessionImpressionLogEntry:sessionId:"+logEntries.get(i).getSessionId());
            }
            
            SessionImpressionLog.getInstance().saveLogEntries(logEntries);
        }
    }

    public void logImpression(String variant, int productCount) {
        if (sessionId == null) {
            throw new IllegalStateException("current FD user not bound to session");
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
            throw new IllegalStateException("current FD user not bound to session");
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
        if (!forceSave && (System.currentTimeMillis() - lastCartSaveTime) < SAVE_PERIOD ) return;
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

	private boolean isCartPersistent() {
		//
		// don't save users that don't have a primary key
		//
//		if (this.user.isAnonymous()) return false;
		//
		// don't save not persistent carts or in modify mode
		//
		if (this.getShoppingCart() instanceof FDModifyCartModel) return false;
		//
		// save carts automatically for users who are:
		//  already registered
		//  anonymous and in our delivery area,
		//  anonymous and will be in our delivery area within a year
		//
		if (this.getLevel() >= GUEST || this.isInZone()) {
			return true;
		}
		return false;
	}

	public EnumTransactionSource getApplication() {
		return this.user.getApplication();
	}

	public int getFailedAuthorizations(){
		return this.failedAuthorizations;
	}

	public void incrementFailedAuthorizations(){
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

    public String getCookie() {
        return this.user.getCookie();
    }

    public void setCookie(String cookie) {
        this.user.setCookie(cookie);
    }

    public String getZipCode() {
        return this.user.getZipCode();
    }

    public void setZipCode(String zipCode) {
        this.user.setZipCode(zipCode);
    }

    public void setAddress(AddressModel a) {
        this.user.setAddress(a);
    }

	public AddressModel getAddress() {
		return this.user.getAddress();
	}

	public String getPrimaryKey() {
		return this.user.getPrimaryKey();
	}

    public FDIdentity getIdentity() {
        return this.user.getIdentity();
    }

    public void setIdentity(FDIdentity identity) {
        this.user.setIdentity(identity);
    }

    public int getLevel() {
        return this.user.getLevel();
    }

    public boolean isInZone() {
        return this.user.isInZone();
    }

    public void isLoggedIn(boolean loggedIn) {
        this.user.isLoggedIn(loggedIn);
    }

    public FDCartModel getShoppingCart() {
        return this.user.getShoppingCart();
    }

    public void setShoppingCart(FDCartModel cart) {
        this.user.setShoppingCart(cart);
    }

    public boolean isSurveySkipped() {
		return this.user.isSurveySkipped();
    }

    public void setSurveySkipped(boolean skipped) {
    	this.user.setSurveySkipped(skipped);
    }

	public boolean isFraudulent() throws FDResourceException {
		return this.user.isFraudulent();
	}

	public FDPromotionEligibility getPromotionEligibility() {
		return this.user.getPromotionEligibility();
	}

    public double getMaxSignupPromotion() {
        return this.user.getMaxSignupPromotion();
    }

	public SignupDiscountRule getSignupDiscountRule() {
		return this.user.getSignupDiscountRule();
	}

	public boolean isPromotionAddressMismatch() {
		return this.user.isPromotionAddressMismatch();
	}

	public void setRedeemedPromotion(PromotionI promotion) {
		this.user.setRedeemedPromotion(promotion);
	}

	public PromotionI getRedeemedPromotion() {
		return this.user.getRedeemedPromotion();
	}

    public void updateUserState() {
        this.user.updateUserState();
    }



    public String getFirstName() throws FDResourceException {
        return this.user.getFirstName();
    }

    public String getDepotCode() {
        return this.user.getDepotCode();
    }

	public void setDepotCode(String depotCode) {
        this.user.setDepotCode(depotCode);
    }

    public boolean isDepotUser() {
        return this.user.isDepotUser();
    }

    public boolean isCorporateUser() {
    	return this.user.isCorporateUser();
    }

	public void invalidateCache() {
    	this.user.invalidateCache();
	}

	public OrderHistoryI getOrderHistory() throws FDResourceException {
		return this.user.getOrderHistory();
	}

    public int getAdjustedValidOrderCount() throws FDResourceException {
    	return this.user.getAdjustedValidOrderCount();
    }

    public int getDeliveredOrderCount() throws FDResourceException {
    	return this.user.getDeliveredOrderCount();
    }

    public int getValidPhoneOrderCount() throws FDResourceException {
    	return this.user.getValidPhoneOrderCount();
    }

	public boolean isEligibleForSignupPromotion() {
		return this.user.isEligibleForSignupPromotion();
	}

	public PromotionI getEligibleSignupPromotion() {
		return this.user.getEligibleSignupPromotion();
	}

    public boolean isOrderMinimumMet() throws FDResourceException {
    	return this.user.isOrderMinimumMet();
    }

    public boolean isOrderMinimumMet(boolean alcohol) throws FDResourceException {
    	return this.user.isOrderMinimumMet(alcohol);
    }

    public double getMinimumOrderAmount(){
    	return this.user.getMinimumOrderAmount();
    }

	public float getQuantityMaximum(ProductModel product) {
		return this.user.getQuantityMaximum(product);
	}

	public int getOrderCountForChefsTableEligibility() throws FDResourceException {
		return this.user.getOrderCountForChefsTableEligibility();
	}

	public String getOrderTotalForChefsTableEligibility() throws FDResourceException {
		return this.user.getOrderTotalForChefsTableEligibility();
	}

	public String getOrderCountRemainingForChefsTableEligibility() throws FDResourceException {
		return this.user.getOrderCountRemainingForChefsTableEligibility();
	}

	public String getOrderTotalRemainingForChefsTableEligibility() throws FDResourceException  {
		return this.user.getOrderTotalRemainingForChefsTableEligibility();
	}

	public boolean isCloseToCTEligibilityByOrderCount() throws FDResourceException {
		return this.user.isCloseToCTEligibilityByOrderCount();
	}

	public boolean isCloseToCTEligibilityByOrderTotal() throws FDResourceException  {
		return this.user.isCloseToCTEligibilityByOrderTotal();
	}

	public boolean isOkayToDisplayCTEligibility() throws FDResourceException {
		return this.user.isOkayToDisplayCTEligibility();
	}

	public boolean hasQualifiedForCT() throws FDResourceException {
		return this.user.hasQualifiedForCT();
    }

	public String getEndChefsTableQualifyingDate() throws FDResourceException {
		return this.user.getEndChefsTableQualifyingDate();
	}

    public boolean isPickupOnly() {
        return this.user.isPickupOnly();
    }

    public boolean isPickupUser() {
        return this.user.isPickupUser();
    }

    public boolean isNotServiceable() {
        return this.user.isNotServiceable();
    }

    public boolean isDeliverableUser() {
        return this.user.isDeliverableUser();
    }

    public boolean isHomeUser() {
        return this.user.isHomeUser();
    }

    public FDCustomerModel getFDCustomer() throws FDResourceException {
        return this.user.getFDCustomer();
    }

    public FDReservation getReservation(){
    	return this.user.getReservation();
    }

    public void setReservation(FDReservation reservation){
    	this.user.setReservation(reservation);
    }

	public boolean isChefsTable() throws FDResourceException{
		return this.user.isChefsTable();
	}

	public String getChefsTableInduction() throws FDResourceException {
		return this.user.getChefsTableInduction();
	}

	public String getWinback() throws FDResourceException {
		return this.user.getWinback();
	}

	public String getWinbackPath() throws FDResourceException {
		return this.user.getWinbackPath();
	}

	public String getMarketingPromo() throws FDResourceException {
		return this.user.getMarketingPromo();
	}

	public String getMarketingPromoPath() throws FDResourceException {
		return this.user.getMarketingPromoPath();
	}

	public boolean isEligibleForPreReservation() throws FDResourceException{
		return this.user.isEligibleForPreReservation();
	}

	public EnumServiceType getSelectedServiceType() {
		return this.user.getSelectedServiceType();
	}


	public EnumServiceType getUserServiceType(){
		return user.getUserServiceType() ;
	}

	public EnumServiceType getZPServiceType(){
		return this.user.getZPServiceType();
	}

	public void setZPServiceType(EnumServiceType serviceType) {
		this.user.setZPServiceType(serviceType);
	}

	public void setSelectedServiceType(EnumServiceType serviceType){
		this.user.setSelectedServiceType(serviceType);
	}

	public void setAvailableServices(Set availableServices) {
		user.setAvailableServices(availableServices);
	}

	public String getCustomerServiceContact() {
		return this.user.getCustomerServiceContact();
	}

    public String getCustomerServiceEmail() throws FDResourceException {
		return this.user.getCustomerServiceEmail();
	}

	public boolean isCheckEligible() {
        return (user != null) ? user.isCheckEligible() : false;
    }

	public Collection<ErpPaymentMethodI> getPaymentMethods() {
        return (user != null) ? user.getPaymentMethods() : Collections.<ErpPaymentMethodI>emptyList();
	}

	public String  getUserId() {
        return (user != null) ? user.getUserId() : "";
	}

	public String getLastRefTrackingCode() {
		return this.user.getLastRefTrackingCode();
	}

	public void setLastRefTrackingCode (String lastRefTrackingCode) {
		this.user.setLastRefTrackingCode(lastRefTrackingCode);
	}

	public boolean isReferrerRestricted() throws FDResourceException {
        return (user != null) ? user.isReferrerRestricted() : false;
	}

	public boolean isReferrerEligible() throws FDResourceException {
        return (user != null) ? user.isReferrerEligible() : false;
	}

	public boolean isECheckRestricted() throws FDResourceException {
        return (user != null) ? user.isECheckRestricted() : false;
	}

	public String getRegRefTrackingCode() {
        return (user != null) ? user.getRegRefTrackingCode() : "";
	}

	public String getDefaultCounty() throws FDResourceException{
		return (user != null) ? user.getDefaultCounty() : null;
	}

	public boolean isActive() {
		return user.isActive();
	}

	public boolean isReceiveFDEmails() {
		return user.isReceiveFDEmails();
	}

	public void performDlvPassStatusCheck()throws FDResourceException {
		user.performDlvPassStatusCheck();
	}
	public FDUserDlvPassInfo getDlvPassInfo(){
		return this.user.getDlvPassInfo();
	}

	/**
	 * @return Returns the deliveryPassStatus.
	 */
	public EnumDlvPassStatus getDeliveryPassStatus() {
		return this.user.getDeliveryPassStatus();
	}

	public boolean isEligibleForDeliveryPass() throws FDResourceException {
		return this.user.isEligibleForDeliveryPass();
	}

	public EnumDlvPassProfileType getEligibleDeliveryPass() throws FDResourceException {
		return this.user.getEligibleDeliveryPass();
	}

	public String getDlvPassProfileValue() throws FDResourceException{
		return this.user.getDlvPassProfileValue();
	}

	public boolean isDlvPassNone(){
		return this.user.isDlvPassNone();
	}
	public boolean isDlvPassActive(){
		return this.user.isDlvPassActive();
	}


	public boolean isDlvPassPending(){
		return this.user.isDlvPassPending();
	}

	public boolean isDlvPassExpiredPending(){
		return this.user.isDlvPassExpiredPending();
	}

	public boolean isDlvPassExpired(){
		return this.user.isDlvPassExpired();
	}

	public boolean isDlvPassCancelled(){
		return this.user.isDlvPassCancelled();
	}

	public boolean isDlvPassShortShipped(){
		return this.user.isDlvPassShortShipped();
	}

	public boolean isDlvPassSettlementFailed(){
		return this.user.isDlvPassSettlementFailed();
	}

	public boolean isDlvPassReturned() {
		return this.user.isDlvPassReturned();
	}

	public void updateDlvPassInfo() throws FDResourceException{
		user.updateDlvPassInfo();
	}
	public void setLastRefProgramId(String progId) {
		this.user.setLastRefProgramId(progId);
	}

	public String getLastRefProgId() {
		return this.user.getLastRefProgId();
	}

	public void setLastRefTrkDtls(String trkDtls) {
		this.user.setLastRefTrkDtls(trkDtls);
	}

	public String getLastRefTrkDtls() {
		return this.user.getLastRefTrkDtls();
	}

	public void setLastRefProgInvtId (String progInvtId) {
    	this.user.setLastRefProgInvtId(progInvtId);
    }

	public String getLastRefProgInvtId() {
		 return this.user.getLastRefProgInvtId();
	}

	public String getLastName() throws FDResourceException {
		return this.user.getLastName();
	}

	 public double getBaseDeliveryFee(){
    	return this.user.getBaseDeliveryFee();
    }

	 public double getCorpDeliveryFee(){
    	return this.user.getCorpDeliveryFee();
    }

	 public double getCorpDeliveryFeeMonday(){
	    	return this.user.getCorpDeliveryFeeMonday();
	 }

	 public double getMinCorpOrderAmount(){
    	return this.user.getMinCorpOrderAmount();
    }

	public int getUsableDeliveryPassCount() {
	    	return user.getUsableDeliveryPassCount();
	}

	public EnumDPAutoRenewalType hasAutoRenewDP() throws FDResourceException {
		return user.hasAutoRenewDP();
	}

	public AssignedCustomerParam getAssignedCustomerParam(String promoId) {
		return user.getAssignedCustomerParam(promoId);
	}

	public boolean isGiftCardsEnabled() {
		return this.user.isGiftCardsEnabled();
	}
	
	public boolean isCCLEnabled() {
		return this.user.isCCLEnabled();
	}

	public boolean isCCLInExperienced() {
		return this.user.isCCLInExperienced();
	}

	public List<FDCustomerListInfo> getCustomerCreatedListInfos() {
		return this.user.getCustomerCreatedListInfos();
	}

	public List<FDCustomerListInfo> getStandingOrderListInfos() {
		return this.user.getStandingOrderListInfos();
	}

	public DCPDPromoProductCache getDCPDPromoProductCache(){
		return this.user.getDCPDPromoProductCache();
	}

	public ErpPromotionHistory getPromotionHistory() throws FDResourceException {
		return this.user.getPromotionHistory();
	}

    /**
     * This method was introduced as part of PERF-22 task.
     * Separate invalidation of Order History Cache from other caches.
     */
    public void invalidateOrderHistoryCache() {
    	this.user.invalidateOrderHistoryCache();
    }

    public int getAdjustedValidECheckOrderCount() throws FDResourceException{
    	return this.user.getAdjustedValidECheckOrderCount();
    }

	public boolean isDYFEnabled() {
		return user.isDYFEnabled();
	}

	public boolean isProduceRatingEnabled() {
		return user.isProduceRatingEnabled();
	}

	public boolean isHomePageLetterVisited() {
		return user.isHomePageLetterVisited();
	}

	public void setHomePageLetterVisited(boolean isHomePageLetterVisited) {
		user.setHomePageLetterVisited(isHomePageLetterVisited);
	}

	public boolean isCampaignMsgLimitViewed() {
		return user.isCampaignMsgLimitViewed();
	}

	public int getCampaignMsgViewed() {
		return user.getCampaignMsgViewed();
	}

	public void setCampaignMsgViewed(int campaignMsgViewed) {
		user.setCampaignMsgViewed(campaignMsgViewed);
	}
	
	@Override
	public boolean hasServiceBasedOnUserAddress(EnumServiceType type) {
	    return user.hasServiceBasedOnUserAddress(type);
	}

	public static FDUserI getFDSessionUser(HttpSession session) {
	    if (session==null) {
	        return null;
	    }
            return (FDUserI) session.getAttribute(SessionName.USER);
	}

	public String getCohortName() {
		return this.user.getCohortName();
	}

	public void setCohortName(String cohortName) {
		this.user.setCohortName(cohortName);
	}


	public int getTotalCartSkuQuantity(String[] args) {
		return user.getTotalCartSkuQuantity(args);
	}

	public String getFavoriteTabFeature() {
		return tabSiteFeature;
	}

	public void setFavoriteTabFeature(String feature) {
		this.tabSiteFeature = feature;
	}

	public Map getPromoVariantMap() {
		return this.user.getPromoVariantMap();
	}

	public void setPromoVariantMap(Map pvMap) {
		user.setPromoVariantMap(pvMap);
	}

	public String getSavingsVariantId() {
		return this.user.getSavingsVariantId();
	}
	public void setSavingsVariantId(String savingsVariantId) {
		this.user.setSavingsVariantId(savingsVariantId);
	}

	public boolean isSavingsVariantFound() {
		return this.user.isSavingsVariantFound();
	}

	public void setSavingsVariantFound(boolean savingsVariantFound) {
		this.user.setSavingsVariantFound(savingsVariantFound);
	}


	public PromoVariantModel getPromoVariant(String variantId) {
	   return user.getPromoVariant(variantId);
	}

	public boolean isPostPromoConflictEnabled() {
		return user.isPostPromoConflictEnabled();
	}

	public void setPostPromoConflictEnabled(boolean isPostPromoConflictEnabled) {
		user.setPostPromoConflictEnabled(isPostPromoConflictEnabled);
	}

	public void setPromotionAddressMismatch(boolean b) {
		user.setPromotionAddressMismatch(b);
	}

	public void setSignupDiscountRule(SignupDiscountRule discountRule) {
        user.setSignupDiscountRule(discountRule);
	}

	public boolean isPromoConflictResolutionApplied() {
		return user.isPromoConflictResolutionApplied();
	}

	public void setPromoConflictResolutionApplied(boolean isPromoConflictResolutionApplied) {
		user.setPromoConflictResolutionApplied(isPromoConflictResolutionApplied);
	}
	
	public FDGiftCardInfoList getGiftCardList() {
		return user.getGiftCardList();
	}
	
	public FDCartModel getGiftCart() {
		return this.user.getGiftCart();
	}
	
	public FDRecipientList getRecipientList(){
		return this.user.getRecipientList();
	}
	
	public void setRecipientList(FDRecipientList r){
		this.user.setRecipientList(r);
	}
	
	public void setGiftCart(FDCartModel dcart) {
		this.user.setGiftCart(dcart);
	}
	
	public double getGiftcardBalance() {
		return this.user.getGiftcardBalance();
	}
	
	public boolean isGCOrderMinimumMet() {
		return this.user.isGCOrderMinimumMet();
	}
	
	public double getGCMinimumOrderAmount() {
		return this.user.getGCMinimumOrderAmount();
	}
	
	public void invalidateGiftCards() {
		this.user.invalidateGiftCards();
	}
	
	public FDBulkRecipientList getBulkRecipentList(){
		 return user.getBulkRecipentList();  
	}
	
	public void setBulkRecipientList(FDBulkRecipientList r) {
		user.setBulkRecipientList(r);
	}
	
	 public int getGCRetryCount(){
         return this.gcRetryCount;
   }

   public void incrementGCRetryCount(){
         this.gcRetryCount++;
   }
  
   public void resetGCRetryCount(){
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
	
	public Integer getDonationTotalQuantity(){
		return this.user.getDonationTotalQuantity();
	}
	
	public void setDonationTotalQuantity(Integer donationTotalQuantity){
		this.user.setDonationTotalQuantity(donationTotalQuantity);
	}
	
	public FDCartModel getDonationCart() {		
		return this.user.getDonationCart();
	}

	
	public void setDonationCart(FDCartModel dcart) {
		this.user.setDonationCart(dcart);
	}

	public double getGiftcardsTotalBalance() {
		return this.user.getGiftcardsTotalBalance();
	}
	
	public String getGCSenderName(String certNum, String custId){
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
	
    public String getPricingZoneId(){
    	return this.user.getPricingZoneId();
    }

	public PricingContext getPricingContext() {
		return this.user.getPricingContext();
	}

	/*
	public void setPricingContext(PricingContext pricingContext){
		this.user.setPricingContext(pricingContext);
	}
	*/
	public void resetPricingContext() {
		this.user.resetPricingContext();
	}

	@Override
	public SortedSet<IgnoreCaseString> getClientCodesHistory() {
		return user.getClientCodesHistory();
	}
	public Set<String> getAllAppliedPromos(){
		return this.getUser().getAllAppliedPromos();
	}
	
	public void clearAllAppliedPromos(){
		this.user.clearAllAppliedPromos();
	}

	public void addPromoErrorCode(String promoCode, int errorCode) {
		this.user.addPromoErrorCode(promoCode, errorCode);
	}

	public int getPromoErrorCode(String promoCode) {
		return this.user.getPromoErrorCode(promoCode);
	}
	
	public void clearPromoErrorCodes(){
		this.user.clearPromoErrorCodes();
	}

	public String getMasqueradeAgent() {
		return user.getMasqueradeAgent();
	}

	public void setMasqueradeAgent(String agent) {
		user.setMasqueradeAgent(agent);
	}

	public EnumWinePrice getPreferredWinePrice() {
		return user.getPreferredWinePrice();
	}

	public String getGreeting() throws FDResourceException {
		return user.getGreeting();
	}

	@Override
	public Date getRegistrationDate() {
		return user.getRegistrationDate();
	}
}

