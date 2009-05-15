/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.deliverypass.EnumDPAutoRenewalType;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.SignupDiscountRule;
import com.freshdirect.fdstore.util.EnumSiteFeature;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public interface FDUserI extends java.io.Serializable {


    /** order minimum (before taxes and promotions are applied) */
    public final static double MINIMUM_ORDER_AMOUNT = 30.00; 
    public final static double MIN_CORP_ORDER_AMOUNT = 50.00;
    public final static double BASE_DELIVERY_FEE = 4.99; // Used for site text
    public final static double CORP_DELIVERY_FEE = 9.99; // Used for site text
    public final static double CORP_DELIVERY_FEE_MONDAY = 14.99;  // Used for site text

	public final static int GUEST = 0;              // anonymously cookied user who have not registered
	public final static int RECOGNIZED = 1;         // cookied user who has registered and has a known identity
	public final static int SIGNED_IN = 2;          // cookied user who has registered, has a known identity and is currently signed in
	public final static boolean ZIPCHECK = true;
    
	public EnumTransactionSource getApplication();

    public String getCookie();
    
    public void setCookie(String cookie);

	public String getZipCode();

	public void setZipCode(String zipCode);

	public void setAddress(AddressModel a);

	public AddressModel getAddress();

	public FDIdentity getIdentity();
	
	public String getPrimaryKey();

	public void setIdentity(FDIdentity identity);

	public int getLevel();

	public boolean isInZone();
        
    public void isLoggedIn(boolean loggedId);

	public FDCartModel getShoppingCart();

	public void setShoppingCart(FDCartModel cart);
    
    public boolean isSurveySkipped();
    
    public void setSurveySkipped(boolean skipped);
    
	public boolean isFraudulent() throws FDResourceException;
    
	public FDPromotionEligibility getPromotionEligibility();

	/** @deprecated */	
	public double getMaxSignupPromotion();
    
	public SignupDiscountRule getSignupDiscountRule();

	public boolean isPromotionAddressMismatch();

	public void setRedeemedPromotion(PromotionI promotion); 
	
	public PromotionI getRedeemedPromotion();

	public void updateUserState();

	public String getFirstName() throws FDResourceException;
	public String getLastName() throws FDResourceException;
    
    public FDCustomerModel getFDCustomer() throws FDResourceException;
    
    public String getDepotCode();

	public void setDepotCode(String depotCode);
    
    public boolean isDepotUser();
    
    public boolean isCorporateUser();

	/**
	 * Invalidate cached order history & promotions.
	 */
	public void invalidateCache();

	public OrderHistoryI getOrderHistory() throws FDResourceException;

    public int getAdjustedValidOrderCount() throws FDResourceException;
    
    public int getValidPhoneOrderCount() throws FDResourceException;
    
	public boolean isEligibleForSignupPromotion();
	
	public PromotionI getEligibleSignupPromotion();

    public boolean isOrderMinimumMet() throws FDResourceException;
    
    public double getMinimumOrderAmount();
    
	public boolean isOrderMinimumMet(boolean excludeBeer) throws FDResourceException;
	
	public float getQuantityMaximum(ProductModel product);
    
    public boolean isPickupOnly();
    
    public boolean isPickupUser();
    
    public boolean isNotServiceable();
    
    public boolean isDeliverableUser();
    
    public boolean isHomeUser();
    
    public FDReservation getReservation();
    
    public void setReservation(FDReservation reservation);
    
	public boolean isChefsTable() throws FDResourceException;
	
	public boolean isEligibleForPreReservation() throws FDResourceException;
	
	public EnumServiceType getSelectedServiceType();
	
	public void setSelectedServiceType(EnumServiceType serviceType);
	
	public String getCustomerServiceContact();
	
	public String getCustomerServiceEmail() throws FDResourceException;

	public boolean isCheckEligible();
    
	public Collection getPaymentMethods();
	
	public String getUserId ();
	
	public void setLastRefTrackingCode (String lastRefTrackingCode);
	
	public String getLastRefTrackingCode();
	
	public void setLastRefProgramId (String progId);
	
	public String getLastRefProgId();
	
	public void setLastRefTrkDtls(String trkDtls);
	
	public String getLastRefTrkDtls();
	
    public void setLastRefProgInvtId (String progId);
	
	public String getLastRefProgInvtId();

	
	public boolean isReferrerRestricted() throws FDResourceException;
	
	public boolean isReferrerEligible() throws FDResourceException;	

	public boolean isECheckRestricted() throws FDResourceException;

	
	
	public String getDefaultCounty() throws FDResourceException;
	
	public boolean isActive();
	
	public boolean isReceiveFDEmails();
	
	public boolean isDlvPassNone();
		
	public boolean isDlvPassActive();
	
	public boolean isDlvPassPending();
	
	public boolean isDlvPassExpiredPending();
	
	public boolean isDlvPassExpired();
	
	public boolean isDlvPassCancelled();
	
	public boolean isDlvPassReturned();
	
	public boolean isDlvPassShortShipped();
	
	public boolean isDlvPassSettlementFailed();
	
	public EnumDlvPassStatus getDeliveryPassStatus();
	
	public void performDlvPassStatusCheck()throws FDResourceException;
	
	public boolean isEligibleForDeliveryPass() throws FDResourceException;
	
	public EnumDlvPassProfileType getEligibleDeliveryPass() throws FDResourceException;
	
	public String getDlvPassProfileValue() throws FDResourceException;
	
	public FDUserDlvPassInfo getDlvPassInfo();
	
	public void updateDlvPassInfo() throws FDResourceException;
	
	public double getBaseDeliveryFee();
	
	public double getMinCorpOrderAmount();
	
	public double getCorpDeliveryFee();
	
	public double getCorpDeliveryFeeMonday();
	
	public int getUsableDeliveryPassCount();
	
	public EnumDPAutoRenewalType hasAutoRenewDP() throws FDResourceException;

	public AssignedCustomerParam getAssignedCustomerParam(String promoId);
	
	public boolean isProduceRatingEnabled();
	
	/* CCL */
	public boolean isCCLEnabled();
	
	public boolean isCCLInExperienced();
	
	public List getCustomerCreatedListInfos();
	
	public DCPDPromoProductCache getDCPDPromoProductCache();
	
	public ErpPromotionHistory getPromotionHistory() throws FDResourceException;
	
	/* SmartStore DYF */
	public boolean isDYFEnabled();
	
	public boolean isHomePageLetterVisited();
	
	
    /*
     * This method was introduced as part of PERF-22 task.
     * Separate invalidation of Order History Cache from other caches.
     */
    public void invalidateOrderHistoryCache();
    
    public int getAdjustedValidECheckOrderCount()  throws FDResourceException;
    
    /*
     * This method was introduced as part of new COS-changes
     * This will return the service type of FDuser table entry
     */
 
    public EnumServiceType getUserServiceType();
    
    public void setHomePageLetterVisited(boolean isHomePageLetterVisited);
    
    public String getCohortName();

    public void setCohortName(String cohortName);
    
    public int getTotalCartSkuQuantity(String args[]);
    
    /**
     * @return the user's favorite site feature for a cart tab
     */
    public String getFavoriteTabFeature();
    
    /**
     * sets the user's favorite site feature for a cart tab
     * 
     * @param feature
     */
    public void setFavoriteTabFeature(String feature);
    
    public Map getPromoVariantMap();
    
    public void setPromoVariantMap(Map pvMap);
    
	public String getSavingsVariantId();
	
	public void setSavingsVariantId(String savingsVariantId) ;
	
	public void setSavingsVariantFound(boolean savingsVariantFound) ;
	
	public boolean isSavingsVariantFound() ;

    public PromoVariantModel getPromoVariant(String variantId);
    
    public boolean isPostPromoConflictEnabled();

	public void setPromotionAddressMismatch(boolean b);

	public void setSignupDiscountRule(SignupDiscountRule discountRule);
	
	public void setPostPromoConflictEnabled(boolean isPostPromoConflictEnabled); 
	
	public boolean isPromoConflictResolutionApplied();

	public void setPromoConflictResolutionApplied(boolean isPromoConflictResolutionApplied); 
}
