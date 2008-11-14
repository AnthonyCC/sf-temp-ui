/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDPAutoRenewalType;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.deliverypass.ejb.DlvPassManagerSB;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.adapter.PromotionContextAdapter;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.lists.CclUtils;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.FDPromotionVisitor;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.SignupDiscountRule;
import com.freshdirect.fdstore.promotion.WaiveDeliveryCharge;
import com.freshdirect.fdstore.rules.EligibilityCalculator;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.rules.FeeCalculator;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.util.SiteFeatureHelper;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.fdstore.promotion.AudienceStrategy;
	
/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDUser extends ModelSupport implements FDUserI {
	public static final String SERVICE_EMAIL = "service@freshdirect.com";

	private EnumTransactionSource application; 
    private String depotCode;
    
    private Set availableServices = new HashSet();
    private EnumServiceType selectedServiceType = null;
    // for new COS customers
    private EnumServiceType userServiceType = null;
    
    private FDIdentity identity;
    private AddressModel address;
    private FDReservation reservation;
    private FDCartModel shoppingCart = new FDCartModel();

    private SignupDiscountRule signupDiscountRule;
    private boolean promotionAddressMismatch = false;
	private String redeemedPromotionCode;
    
    private String cookie;
    private boolean loggedIn = false;
    
    private boolean surveySkipped = false;

    private transient ErpCustomerInfoModel customerInfoModel;
    private transient OrderHistoryI cachedOrderHistory;
    private transient FDCustomerModel cachedFDCustomer;
	private transient FDPromotionEligibility promotionEligibility;
	private transient Boolean checkEligible;
	private transient Boolean referrerEligible;
	private transient String regRefTrackingCode;
	private transient List cclListInfos;
	
	private String lastTrackingCode;
	private String lastRefTrackingCode;
	
	private String lastRefProgId=null;
	private String lastRefTrkDtls=null;
	private String lastRefProgInvtId=null;
	
	private String userId;
	
	private boolean active = false;
	private boolean receiveFDemails = true;
	
	private boolean isHomePageLetterVisited=false;
	
	//Contains user specific Delivery Pass Details.
	private FDUserDlvPassInfo dlvPassInfo;
	
	private Map assignedCustomerParams;

	/*
	 * This attribute caches the list of product keys that are already
	 * evaluated for DCPD along with its DCPD promo info. Only used by
	 * Web tier for applying promotions. So transient.
	 */
	private transient DCPDPromoProductCache dcpdPromoProductCache;
	//New Promotion History cache. PERF-22.
	private transient ErpPromotionHistory cachedPromoHistory;
	
	public FDUserDlvPassInfo getDlvPassInfo() {
		return dlvPassInfo;
	}

	public void setDlvPassInfo(FDUserDlvPassInfo dlvPassInfo) {
		this.dlvPassInfo = dlvPassInfo;
	}

	public FDUser(PrimaryKey pk) {
		super();
		this.setPK(pk);
	}
	
	public FDUser() {
		super();
	}
	
	public EnumTransactionSource getApplication() {
		return application;
	}

	public void setApplication(EnumTransactionSource source) {
		this.application = source; 
	}
    
	public String getCookie() {
        return this.cookie;
    }
    
    public void setCookie(String cookie) {
        this.cookie = cookie;
        this.invalidateCache();
    }
    
    public String getZipCode() {
        return this.address == null ? null : this.address.getZipCode();
    }
    
    public void setZipCode(String zipCode) {
        AddressModel a = new AddressModel();
        a.setZipCode(zipCode);
        this.address = a;
        this.invalidateCache();
    }
    
    public void setAddress(AddressModel a) {
        this.address = a;
        this.invalidateCache();
    }
    
    public AddressModel getAddress() {
        return this.address;
    }
    
    public FDIdentity getIdentity() {
        return this.identity;
    }
    
    public void setIdentity(FDIdentity identity) {
        this.identity = identity;
        this.invalidateCache();
    }
    
    public int getLevel() {
        if (identity == null) {
            return GUEST;
        } else if ((identity != null) && !loggedIn) {
            return RECOGNIZED;
        } else if ((identity != null) && loggedIn) {
            return SIGNED_IN;
        }
        return -1;
    }
    
    public boolean isInZone() {
        return this.isDeliverableUser();
    }
    
    public boolean isDeliverableUser() {
    	return this.availableServices.contains(EnumServiceType.HOME) || this.availableServices.contains(EnumServiceType.DEPOT) || this.availableServices.contains(EnumServiceType.CORPORATE);
    }
    
    public void isLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    public FDCartModel getShoppingCart() {
        return this.shoppingCart;
    }
    
    public void setShoppingCart(FDCartModel cart) {
        this.shoppingCart = cart;
    }
    
    public boolean isSurveySkipped() {
        return surveySkipped;
    }
    
    public void setSurveySkipped(boolean surveySkipped) {
        this.surveySkipped = surveySkipped;
    }
    
    public boolean isFraudulent() throws FDResourceException {
    	if (this.identity==null) {
    		return false;
    	}
		return !this.getFDCustomer().isEligibleForSignupPromo();
    }
    
	public FDCustomerModel getFDCustomer() throws FDResourceException { 
		if (this.identity==null) {
			throw new IllegalStateException("No identity");
		}
		if (this.cachedFDCustomer==null) {
			this.cachedFDCustomer = FDCustomerFactory.getFDCustomer(this.identity);
		}
		return this.cachedFDCustomer;

	}
    
    public double getMaxSignupPromotion() {
        if (this.signupDiscountRule != null) {
            return this.signupDiscountRule.getMaxAmount();
        } else {
            return 0.0;
        }
    }

	public SignupDiscountRule getSignupDiscountRule(){
		if (this.promotionEligibility == null) {
			updateUserState();
		}
		return this.signupDiscountRule;
	}
    
    public void setSignupDiscountRule(SignupDiscountRule discountRule){
        this.signupDiscountRule = discountRule;
    }
    
    public boolean isPromotionAddressMismatch() {
        return promotionAddressMismatch;
    }
    
    public void setPromotionAddressMismatch(boolean b) {
        promotionAddressMismatch = b;
    }

	public void setRedeemedPromotion(PromotionI promotion) {
		this.redeemedPromotionCode = promotion==null ? null : promotion.getPromotionCode();
	}
	
	public PromotionI getRedeemedPromotion() {
		return this.redeemedPromotionCode == null
			? null
			: PromotionFactory.getInstance().getPromotion(this.redeemedPromotionCode);
	}
    
    public void updateUserState(){
		try {
			this.getShoppingCart().recalculateTaxAndBottleDeposit(getZipCode());
			this.updateSurcharges();
			this.applyPromotions();			
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e.getMessage());
		}
    }
    
    private void applyPromotions(){
    	// clear previous promotions
    	this.setSignupDiscountRule(null);
		this.setPromotionAddressMismatch(false);
		
		this.getShoppingCart().clearSampleLines();
		this.getShoppingCart().setDiscounts(new ArrayList());
		// evaluate special dlv charge override
		WaiveDeliveryCharge.apply(this);

		// apply promotions
		this.promotionEligibility = FDPromotionVisitor.applyPromotions(new PromotionContextAdapter(this));
    }


    private void updateSurcharges() {
		this.getShoppingCart().clearCharge(EnumChargeType.DELIVERY);
		this.getShoppingCart().clearCharge(EnumChargeType.MISCELLANEOUS);
		
		AddressModel address = this.shoppingCart.getDeliveryAddress();
		if (address != null) {
			// DLV
			FeeCalculator calc = new FeeCalculator("DLV");
			double dlvFee = calc.calculateFee(new FDRulesContextImpl(this));
			this.shoppingCart.setChargeAmount(EnumChargeType.DELIVERY, dlvFee);

			// MISC
			calc = new FeeCalculator("MISC");
			double miscFee = calc.calculateFee(new FDRulesContextImpl(this));
			this.shoppingCart.setChargeAmount(EnumChargeType.MISCELLANEOUS, miscFee);

		}

		// DLV & MISC tax
		double taxRate = 0.0;
		for (Iterator i = this.shoppingCart.getOrderLines().iterator(); i.hasNext();) {
			FDCartLineI cartLine = (FDCartLineI) i.next();
			if (cartLine.hasTax()) {
				taxRate = cartLine.getTaxRate();
				break;
			}
		}
		ErpChargeLineModel c = this.shoppingCart.getCharge(EnumChargeType.DELIVERY);
		if (c != null) {
			c.setTaxRate(taxRate);
		}
		c = this.shoppingCart.getCharge(EnumChargeType.MISCELLANEOUS);
		if (c != null) {
			c.setTaxRate(taxRate);
		}

	}
    
    public String getFirstName() throws FDResourceException {
    	ErpCustomerInfoModel info = getCustomerInfoModel();
		if (info == null) {
			return "";
		}
		return info.getFirstName();
    }

    public String getLastName() throws FDResourceException {
    	ErpCustomerInfoModel info = getCustomerInfoModel();
		if (info == null) {
			return "";
		}
		return info.getLastName();
    }

    public String getDepotCode() {
        return this.depotCode;
    }
    
    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
        this.invalidateCache();
    }
    
    public boolean isDepotUser() {
        return depotCode != null;
    }
    
	public boolean isCorporateUser() {
		return EnumServiceType.CORPORATE.equals(this.selectedServiceType); //  || this.availableServices.contains(EnumServiceType.CORPORATE);
	}
    
    public void invalidateCache() {
    	//Commented as part of PERF-22 task.
        //this.cachedOrderHistory = null;
        this.signupDiscountRule = null;
        this.cachedFDCustomer = null;
        this.customerInfoModel = null;
		this.promotionEligibility = null;
		this.checkEligible = null;
		this.referrerEligible = null;
		this.regRefTrackingCode = null;
		this.cclListInfos = null;
		this.cachedPromoHistory = null;
    }
    /*
     * This method was introduced as part of PERF-22 task.
     * Seperate invalidation of Order History Cache from other caches.
     */
    public void invalidateOrderHistoryCache() {
    	this.cachedOrderHistory = null;
    }
    
    public OrderHistoryI getOrderHistory() throws FDResourceException {
        if (this.cachedOrderHistory==null) {
            this.cachedOrderHistory = getOrderHistoryInfo();
        }
        return this.cachedOrderHistory;
    }
    
   private OrderHistoryI getOrderHistoryInfo() throws FDResourceException {
	   /*
	    * This change is rollbacked temporarily.
    	if(EnumTransactionSource.CUSTOMER_REP.equals(application)){
    		//If CRM load entire order history.
    		return FDCustomerManager.getOrderHistoryInfo(this.identity);
    	} else {
    		//Load only Order History Summary.
    		return FDCustomerManager.getWebOrderHistoryInfo(this.identity);
    	}
    	*/
	   //Load Entire order history inspite of CRM or WEB.
	   return FDCustomerManager.getOrderHistoryInfo(this.identity);
    }
   
    public ErpPromotionHistory getPromotionHistory() throws FDResourceException {
        if (this.cachedPromoHistory==null) {
            this.cachedPromoHistory = FDCustomerManager.getPromoHistoryInfo(this.identity);
        }
        return this.cachedPromoHistory;
    }
    
    /**
     * @return number of valid orders, corrected in modify order mode
     */
    public int getAdjustedValidOrderCount() throws FDResourceException {
    	int orderCount = this.getOrderHistory().getValidOrderCount();
        if (this.getShoppingCart() instanceof FDModifyCartModel) {
            // we're in modify order mode, subtract one
            orderCount--;
        }
        return orderCount;
    }
    
    /**
     * @return number of valid ECheck orders, corrected in modify order mode
     */
    public int getAdjustedValidECheckOrderCount() throws FDResourceException {
    	int orderCount = this.getOrderHistory().getValidECheckOrderCount();	
        if (this.getShoppingCart() instanceof FDModifyCartModel) {
            // we're in modify order mode, subtract one
            orderCount--;
        }
        return orderCount;
    }
    /**
     * @return number of delivered orders
     */
    public int getDeliveredOrderCount() throws FDResourceException {
        int orderCount = this.getOrderHistory().getDeliveredOrderCount();
        return orderCount;
    }
    
    /**
     * @return number of phone orders
     */
    public int getValidPhoneOrderCount() throws FDResourceException {
        return this.getOrderHistory().getValidPhoneOrderCount();
    }
    
	public FDPromotionEligibility getPromotionEligibility(){
		if (this.promotionEligibility==null) {
			this.updateUserState();
		}

		return this.promotionEligibility;
	}

	public boolean isEligibleForSignupPromotion(){
		return this.getPromotionEligibility().isEligibleForType(EnumPromotionType.SIGNUP);
	}

	public PromotionI getEligibleSignupPromotion(){
		Set promoSet = this.getPromotionEligibility().getEligiblePromotionCodes(EnumPromotionType.SIGNUP);
		if (promoSet.isEmpty()) {
			return null;
		}
		String code = (String) promoSet.iterator().next();
		return PromotionFactory.getInstance().getPromotion(code);
	}
    
    /**
     * @return true if the order minimum has been met (FDUserI.MINIMUM_ORDER_AMOUNT)
     */
	public boolean isOrderMinimumMet() throws FDResourceException {
		return this.isOrderMinimumMet(false);
	}
    
    public boolean isOrderMinimumMet(boolean alcohol) throws FDResourceException {
		double subTotal = alcohol ? this.shoppingCart.getSubTotalWithoutAlcohol() : this.shoppingCart.getSubTotal();
		return subTotal >= this.getMinimumOrderAmount();
    }
    
    public double getMinimumOrderAmount() {
		if (getShoppingCart() != null && getShoppingCart().getDeliveryAddress() != null){
			try {
				String county = FDDeliveryManager.getInstance().getCounty(getShoppingCart().getDeliveryAddress());
				if("SUFFOLK".equalsIgnoreCase(county)){
					return 100;
				}
			} catch (FDResourceException e) {
				throw new FDRuntimeException(e);
			}

		}

		return EnumServiceType.CORPORATE.equals(this.getSelectedServiceType()) ? MIN_CORP_ORDER_AMOUNT : MINIMUM_ORDER_AMOUNT;
	}

	public float getQuantityMaximum(ProductModel product) {
		return product.enforceQuantityMax() || (!this.isCorporateUser()) 
		      ? product.getQuantityMaximum()  : 200;
	}
    
    public boolean isPickupUser() {
    	return this.availableServices.contains(EnumServiceType.PICKUP) || this.availableServices.contains(EnumServiceType.HOME) || this.availableServices.contains(EnumServiceType.CORPORATE); 
    }
    
    public boolean isPickupOnly() {
    	return !this.availableServices.contains(EnumServiceType.DEPOT) && !this.availableServices.contains(EnumServiceType.CORPORATE) &&
    	!this.availableServices.contains(EnumServiceType.HOME) && this.availableServices.contains(EnumServiceType.PICKUP);
    }
    
    public  boolean isNotServiceable() {
    	return this.availableServices.isEmpty();
    }
    
    public boolean isHomeUser() {
    	return this.availableServices.contains(EnumServiceType.HOME);
    }
    
    public FDReservation getReservation(){
    	Date now = new Date();
    	if(this.reservation != null){
    		if(reservation.getExpirationDateTime().before(now) || reservation.getExpirationDateTime().equals(now) ){
    			return null;
    		}
    	}
    	
    	return this.reservation;
    }
    
    public void setReservation(FDReservation reservation){
    	this.reservation = reservation;
    }
    
	public boolean isChefsTable() throws FDResourceException {
	    if (this.identity == null) {
			return false;
		}
		FDCustomerModel customer = this.getFDCustomer();
		if (customer == null || customer.getProfile() == null) {
			return false;
		} else {
			return customer.getProfile().isChefsTable();
		}
	}

	public boolean isEligibleForPreReservation() throws FDResourceException {
	    if (this.identity == null) {
			return false;
		}
		FDCustomerModel customer = this.getFDCustomer();
		if (customer == null || customer.getProfile() == null) {
			return false;
		}
		ProfileModel p = customer.getProfile();
		return p.isVIPCustomer() || p.isChefsTable() || p.isCOSPilot();
	}
	
	public EnumServiceType getSelectedServiceType(){
		AddressModel address = this.shoppingCart.getDeliveryAddress();
		return address != null ? address.getServiceType() : this.selectedServiceType ;
	}
	
	
	public EnumServiceType getUserServiceType(){		
		return this.userServiceType != null ? this.userServiceType : EnumServiceType.HOME ;
	}

	public void setUserServiceType(EnumServiceType serviceType) {
		this.userServiceType = serviceType;
	}

	
	public void setSelectedServiceType(EnumServiceType serviceType) {
		this.selectedServiceType = serviceType;
	}
	
	public void setAvailableServices(Set availableServices) {
		this.availableServices = Collections.unmodifiableSet(availableServices);
	}
	
	public String getCustomerServiceContact() {
		try {
			return this.isChefsTable() ? "1-866-511-1240" : "1-212-796-8002";
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}


	/**
	 * Returns the appropriate customer service email address
	 * 
	 * @return serviceEmail email address
	 */
	public String getCustomerServiceEmail() throws FDResourceException {
		String serviceEmail = SERVICE_EMAIL;
		if (isDepotUser()){
			serviceEmail = FDDepotManager.getInstance().getCustomerServiceEmail(getDepotCode());
		} else if(isCorporateUser()){
			serviceEmail = "corporateservice@freshdirect.com";
		}
		if (isChefsTable()){
		    serviceEmail = FDStoreProperties.getChefsTableEmail();
		}
		return serviceEmail;
	}


	public boolean isCheckEligible()  {
		if (checkEligible == null) {			
			EligibilityCalculator calc = new EligibilityCalculator("ECHECK");
			checkEligible = new Boolean(calc.isEligible(new FDRulesContextImpl(this)));
		}
		return checkEligible.booleanValue();
    }
	
	public Collection getPaymentMethods() {
		try {
			return FDCustomerManager.getPaymentMethods(this.identity);
		} catch (FDResourceException e) {
			return new ArrayList(); // empty list
		}
	}

	public String getUserId() {
		try {
			// load user id 'user@host.com' lazily
			if (this.identity != null && (userId == null || "".equals(userId))) {
				ErpCustomerModel model = FDCustomerFactory.getErpCustomer(this.identity);
				userId = (model != null) ? model.getUserId() : "";
			}
		} catch (FDResourceException e) {
			userId =  ""; // empty string
		}
		return userId;
	}
	
	public String getLastTrackingCode() {
		return this.lastTrackingCode;
	}
	
	public void setLastTrackingCode (String lastTrackingCode) {
		this.lastTrackingCode = lastTrackingCode;
	}

	public String getLastRefTrackingCode() {
		return this.lastRefTrackingCode;
	}
	
	public void setLastRefTrackingCode (String lastRefTrackingCode) {
		this.lastRefTrackingCode = lastRefTrackingCode;
	}

	public boolean isReferrerRestricted() throws FDResourceException {
	    if (this.identity == null) {
			return false;
		}
		return FDCustomerManager.isReferrerRestricted(this.identity);
	}

	public boolean isReferrerEligible() throws FDResourceException {
		if (referrerEligible == null) { 			
			EligibilityCalculator calc = new EligibilityCalculator("REFERRER");
			referrerEligible = new Boolean(calc.isEligible(new FDRulesContextImpl(this)));
		}
		return referrerEligible.booleanValue();
	}

	public boolean isECheckRestricted() throws FDResourceException {
	    if (this.identity == null) {
			return false;
		}
		return FDCustomerManager.isECheckRestricted(this.identity);
	}

	public String getRegRefTrackingCode() {
		try {
			if (identity != null && regRefTrackingCode == null) {
				ErpCustomerModel model = FDCustomerFactory.getErpCustomer(this.identity);
				regRefTrackingCode = (model != null) ? model.getCustomerInfo().getRegRefTrackingCode() : "";
			}
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
		return regRefTrackingCode;
	}
	
	public String getDefaultCounty() throws FDResourceException{
		String county = null;
		
		//if user is pickup user default county = 'PICKUP'
		if(EnumServiceType.PICKUP.equals(this.getSelectedServiceType()) || EnumServiceType.DEPOT.equals(this.getSelectedServiceType())){
			county = EnumServiceType.PICKUP.getName();
		}
		
		//check address on cart, recognize user handles all the complex logic with defaultAddresses
		if(county == null && this.shoppingCart != null){
			county = FDDeliveryManager.getInstance().getCounty(this.getShoppingCart().getDeliveryAddress());
		}
		
		//if we got nothing so far return county of zipcode on zipcheck
		if(this.getZipCode() != null && (county == null || "".equals(county))){
			county = FDDeliveryManager.getInstance().lookupCountyByZip(this.getZipCode());
		}
		
		return county;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	public boolean isReceiveFDEmails(){
		return this.receiveFDemails;
	}
	
	public void setReceiveFDEmails(boolean receiveFDEmails) {
		this.receiveFDemails = receiveFDEmails;
	}

	/**
	 * @return Returns the deliveryPassStatus.
	 */
	public EnumDlvPassStatus getDeliveryPassStatus() {
		if(dlvPassInfo != null){
			return dlvPassInfo.getStatus();
		} 
		//Return Default value;
		return EnumDlvPassStatus.NONE;
		
	}
	public boolean isDlvPassNone(){
		return (dlvPassInfo == null) || (EnumDlvPassStatus.NONE.equals(dlvPassInfo.getStatus()));
	}
	
	public boolean isDlvPassActive(){
		if(dlvPassInfo == null){
			return false;
		} 
		if(!dlvPassInfo.isUnlimited()){
			//BSGS Pass
			/* 2nd Condition happens only for BSGS pass.
			 * Let say user places order A using a last delivery of a BSGS pass. The pass
			 * goes to expired pending. The next day the user modifies the order still the BSGS
			 * pass should be applied even if the status is expired pending.
			 * Thats why this.getShoppingCart().isDlvPassAlreadyApplied() check is made.
			 */			
			return (EnumDlvPassStatus.ACTIVE.equals(dlvPassInfo.getStatus()) ||  
					(this.isDlvPassExpiredPending() && this.getShoppingCart().isDlvPassAlreadyApplied()));
		}
		//Unlimited Pass.	
		if(EnumDlvPassStatus.ACTIVE.equals(dlvPassInfo.getStatus())) {
			Date today = new Date();
			return today.before(dlvPassInfo.getExpDate());
		} else {
			return false;
		}

	}
	
	public boolean isDlvPassExpired(){
		if(dlvPassInfo == null){
			return false;
		} 
		if(!dlvPassInfo.isUnlimited()){
			//BSGS Pass
			return EnumDlvPassStatus.EXPIRED.equals(dlvPassInfo.getStatus()) ;
		}
		//Unlimited Pass.	
		if(EnumDlvPassStatus.EXPIRED.equals(dlvPassInfo.getStatus())) {
			return true;
		}else{
			//Safe Check - Go by expiration date.
			if(null==dlvPassInfo.getExpDate()) {
				return false;
			}
			Date today = new Date();
			return today.after(dlvPassInfo.getExpDate());
		}
	}
	
	public boolean isDlvPassPending(){
		if(dlvPassInfo == null){
			return false;
		}
		return EnumDlvPassStatus.PENDING.equals(dlvPassInfo.getStatus());
		
	}

	public boolean isDlvPassExpiredPending(){
		if(dlvPassInfo == null){
			return false;
		}
		return EnumDlvPassStatus.EXPIRED_PENDING.equals(dlvPassInfo.getStatus());
		
	}

	public boolean isDlvPassCancelled(){
		if(dlvPassInfo == null){
			return false;
		}
		return (EnumDlvPassStatus.CANCELLED.equals(dlvPassInfo.getStatus())  ||
				EnumDlvPassStatus.ORDER_CANCELLED.equals(dlvPassInfo.getStatus()));	
	}
	
	public boolean isDlvPassReturned(){
		if(dlvPassInfo == null){
			return false;
		}
		return EnumDlvPassStatus.PASS_RETURNED.equals(dlvPassInfo.getStatus());
	}
	
	public boolean isDlvPassShortShipped(){
		if(dlvPassInfo == null){
			return false;
		}
		return EnumDlvPassStatus.SHORT_SHIPPED.equals(dlvPassInfo.getStatus());
	}
	
	public boolean isDlvPassSettlementFailed(){
		if(dlvPassInfo == null){
			return false;
		}
		return EnumDlvPassStatus.SETTLEMENT_FAILED.equals(dlvPassInfo.getStatus());	
	}
	
	public void performDlvPassStatusCheck()  throws FDResourceException {
		if(this.isDlvPassActive()){
			if(!(this.getShoppingCart().isChargeWaived(EnumChargeType.DELIVERY))){
				//If delivery promotion was applied, do not reapply the waiving of dlv charge.
				this.getShoppingCart().setChargeWaived(EnumChargeType.DELIVERY,true, DlvPassConstants.PROMO_CODE);
				this.getShoppingCart().setDlvPassApplied(true);
			}
		}/*else if(this.isDlvPassExpired()   && this.getShoppingCart().isDlvPassAlreadyApplied()){
			
			this.getShoppingCart().setChargeWaived(EnumChargeType.DELIVERY,true, DlvPassConstants.PROMO_CODE);
			this.getShoppingCart().setDlvPassApplied(true);
		}*/
		else if((this.getShoppingCart() instanceof FDModifyCartModel)&&(this.getDlvPassInfo().isUnlimited())) {
			
			String dpId=((FDModifyCartModel)this.getShoppingCart()).getOriginalOrder().getDeliveryPassId();
			if(dpId!=null && !dpId.equals("")) {
				List passes=FDCustomerManager.getDeliveryPassesByStatus(this.getIdentity(), EnumDlvPassStatus.ACTIVE);
				DeliveryPassModel dlvPass=null;
				Date today = new Date();
				for(int i=0;i<passes.size();i++) {
					dlvPass=(DeliveryPassModel)passes.get(i);
					
					if(today.after(dlvPass.getExpirationDate()) && EnumDlvPassStatus.ACTIVE.equals(dlvPass.getStatus()) &&dlvPass.getId().equals(dpId)){
						this.getShoppingCart().setChargeWaived(EnumChargeType.DELIVERY,true, DlvPassConstants.PROMO_CODE);
						this.getShoppingCart().setDlvPassApplied(true);
						break;
					
					}
				}
			}
		}
	}

	public boolean isEligibleForDeliveryPass() throws FDResourceException {
		/*if(EnumDlvPassProfileType.BSGS.equals(getEligibleDeliveryPass()) || EnumDlvPassProfileType.UNLIMITED.equals(getEligibleDeliveryPass())) {
			return true;
		} else {
			return false;
		}
		return true;*/
			
		EnumDlvPassProfileType profileType=getEligibleDeliveryPass();
		if(profileType.equals(EnumDlvPassProfileType.NOT_ELIGIBLE))
			return false;
		return true;
		
	}
	
	public  EnumDlvPassProfileType getEligibleDeliveryPass() throws FDResourceException {
	    if (this.identity != null) {
			FDCustomerModel customer = this.getFDCustomer();
			String customerPK=customer.getErpCustomerPK();
			
			if (customer != null && customer.getProfile() != null) {
				ProfileModel p = customer.getProfile();
				String profileValue = p.getDeliveryPass();
				if((profileValue==null)||(profileValue!=null && profileValue.trim().equals(""))) {
					//return EnumDlvPassProfileType.NOT_ELIGIBLE;
					return EnumDlvPassProfileType.UNLIMITED;
				}
				if(isEligibleForBSGS(profileValue,customerPK)) {
					return EnumDlvPassProfileType.BSGS;
				}
				
				if(profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedAmazonPrimeProfile()) != -1) {
					if(isEligibleForAmazonPrime(profileValue.trim(),customerPK))
						return EnumDlvPassProfileType.AMAZON_PRIME;
					else
						return EnumDlvPassProfileType.UNLIMITED;
				}
				if(profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedPromotionalProfile()) != -1) {
					
					if(isEligibleForPromotionalProfile(profileValue.trim(),customerPK))
						return EnumDlvPassProfileType.PROMOTIONAL_UNLIMITED;
					else
						return EnumDlvPassProfileType.UNLIMITED;
				}
				if(profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedProfilePosfix()) != -1)
					return EnumDlvPassProfileType.UNLIMITED;
			}
		} 
		//return EnumDlvPassProfileType.NOT_ELIGIBLE;
	    return EnumDlvPassProfileType.UNLIMITED;
	}
	
	private boolean isEligibleForBSGS(String profileValue, String customerID) throws FDResourceException {
		
		boolean isEligible=false;
		if(profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getBSGSProfilePosfix()) != -1) {
			isEligible=true;
		}
		return isEligible;
	}
	
	private boolean isEligibleForAmazonPrime(String profileValue, String customerID) throws FDResourceException {
		
		boolean isEligible=false;
		if(profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedAmazonPrimeProfile()) != -1) {
			isEligible=!FDCustomerManager.hasPurchasedPass(customerID);//,getDeliveryPassType(FDStoreProperties.getUnlimitedAmazonPrimeProfile()).getCode()
		}
		return isEligible;
	}
	
	private boolean isEligibleForPromotionalProfile(String profileValue, String customerID) throws FDResourceException {
		
		boolean isEligible=false;
		if(profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedPromotionalProfile()) != -1) {
			isEligible=!FDCustomerManager.hasPurchasedPass(customerID);
		}
		return isEligible;
	}

	public String getDlvPassProfileValue() throws FDResourceException {
	    if (this.identity != null) {
			FDCustomerModel customer = this.getFDCustomer();
			if (customer != null && customer.getProfile() != null) {
				ProfileModel p = customer.getProfile();
				if(p.getDeliveryPass() != null )
					return p.getDeliveryPass();
			}
		} 
		return "";
	}
	
	public void updateDlvPassInfo() throws FDResourceException {
		try{
			FDUserDlvPassInfo dpInfo = FDCustomerManager.getDeliveryPassInfo(this);
			this.setDlvPassInfo(dpInfo);
		}catch(FDResourceException fe){
			throw fe;
		}
	}

	public void setLastRefProgramId(String progId) {
		// TODO Auto-generated method stub
		this.lastRefProgId=progId;
	}

	public String getLastRefProgId() {
		// TODO Auto-generated method stub
		return this.lastRefProgId;
	}

	public void setLastRefTrkDtls(String trkDtls) {
		// TODO Auto-generated method stub
		this.lastRefTrkDtls=trkDtls;
	}

	public String getLastRefTrkDtls() {
		// TODO Auto-generated method stub
		return this.lastRefTrkDtls;
	}
	
    public void setLastRefProgInvtId (String progInvtId)
    {
    	this.lastRefProgInvtId=progInvtId;
    }
	

	public ErpCustomerInfoModel getCustomerInfoModel() throws FDResourceException {
		if(identity == null) {
			return null;
		} else {
			if(customerInfoModel == null) {
				customerInfoModel = FDCustomerFactory.getErpCustomerInfo(identity);
			}
			return customerInfoModel;
		}
	}
	public String getLastRefProgInvtId()
	{
		return this.lastRefProgInvtId;
	}
	 
	public double getBaseDeliveryFee() {
		return BASE_DELIVERY_FEE;
	}
	
	public double getMinCorpOrderAmount() {
		return MIN_CORP_ORDER_AMOUNT;
	}
	
	public double getCorpDeliveryFee() {
		return CORP_DELIVERY_FEE;
	}
	
	public int getUsableDeliveryPassCount() {
		if(dlvPassInfo!=null)
			return dlvPassInfo.getUsablePassCount();
		else
			return 0;
	}
	

	public boolean isProduceRatingEnabled() {	
		return SiteFeatureHelper.isEnabled(EnumSiteFeature.RATING, this);
	}

	
	public boolean isCCLEnabled() {	
		return SiteFeatureHelper.isEnabled(EnumSiteFeature.CCL, this);
	}

	public boolean isCCLInExperienced() {
		try {
			return CclUtils.isCCLInExperienced(this, getCustomerCreatedListInfos());
		} catch (FDResourceException e) {
			return false;
		}
	}


	
	// -- DYF --- //


	public boolean isDYFEnabled() {
		return SiteFeatureHelper.isEnabled(EnumSiteFeature.DYF, this);
	}


	public EnumDPAutoRenewalType hasAutoRenewDP() throws FDResourceException {
	    if (this.identity != null) {
			FDCustomerModel customer = this.getFDCustomer();
			String customerPK=customer.getErpCustomerPK();

			return FDCustomerManager.hasAutoRenewDP(customerPK);
	    }
	    return EnumDPAutoRenewalType.NONE;
	}
	
	public AssignedCustomerParam getAssignedCustomerParam(String promoId) {
		if(assignedCustomerParams != null) {
			return (AssignedCustomerParam)this.assignedCustomerParams.get(promoId);	
		}
		return null;
	}

	public List getCustomerCreatedListInfos() {
		if (getLevel() == FDUserI.GUEST) {
			// We don't have an identity 
			return null;
		}
		if (cclListInfos == null) {
			try {
				cclListInfos = FDListManager.getCustomerCreatedListInfos(this);
			} catch (Exception e) {
				throw new FDRuntimeException(e);
			}
		}
		return cclListInfos;
	}

	public void setAssignedCustomerParams(Map assignedCustomerParams) {
		this.assignedCustomerParams = assignedCustomerParams;
	}
	
	public DCPDPromoProductCache getDCPDPromoProductCache(){
		if(this.dcpdPromoProductCache == null){
			this.dcpdPromoProductCache = new DCPDPromoProductCache();
		}
		return dcpdPromoProductCache;
	}

	public boolean isHomePageLetterVisited() {
		return isHomePageLetterVisited;
	}

	public void setHomePageLetterVisited(boolean isHomePageLetterVisited) {
		this.isHomePageLetterVisited = isHomePageLetterVisited;
	}

}
	



	

