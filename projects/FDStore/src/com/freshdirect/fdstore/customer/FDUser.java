/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDPAutoRenewalType;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.adapter.PromotionContextAdapter;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.fdstore.lists.CclUtils;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.FDPromotionVisitor;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.SignupDiscountRule;
import com.freshdirect.fdstore.promotion.WaiveDeliveryCharge;
import com.freshdirect.fdstore.rules.EligibilityCalculator;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.rules.FeeCalculator;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.util.SiteFeatureHelper;
import com.freshdirect.fdstore.zone.FDZoneInfoManager;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.smartstore.fdstore.CohortSelector;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDUser extends ModelSupport implements FDUserI {
	private static final long serialVersionUID = 8492744405934393676L;

	public static final String SERVICE_EMAIL = "service@freshdirect.com";
	public final static int CAMPAIGN_MSG_VIEW_LIMIT = 4;

	private EnumTransactionSource application;
    private String depotCode;

    private Set availableServices = new HashSet();
    private Collection<EnumServiceType> servicesBasedOnAddress;
    private EnumServiceType selectedServiceType = null;
    // for new COS customers
    private EnumServiceType userServiceType = null;

    private FDIdentity identity;
    private AddressModel address;
    private FDReservation reservation;
    private FDCartModel shoppingCart = new FDCartModel();
    
    //Creating a dummy cart for gift card processing.
    private FDCartModel dummyCart = new FDCartModel();
    private FDRecipientList recipientList;
    private FDBulkRecipientList bulkRecipientList;
    
    private SignupDiscountRule signupDiscountRule;
    private boolean promotionAddressMismatch = false;
	private String redeemedPromotionCode;

    private String cookie;
    private boolean loggedIn = false;

    private boolean surveySkipped = false;

    private transient ErpCustomerInfoModel customerInfoModel;
    private transient OrderHistoryI cachedOrderHistory;
    private transient FDCustomerModel cachedFDCustomer;
	protected transient FDPromotionEligibility promotionEligibility;
	private transient Boolean checkEligible;
	private transient Boolean referrerEligible;
	private transient String regRefTrackingCode;
	private transient List cclListInfos;

	private String lastRefTrackingCode;

	private String lastRefProgId=null;
	private String lastRefTrkDtls=null;
	private String lastRefProgInvtId=null;

	private String userId;

	private boolean active = false;
	private boolean receiveFDemails = true;

	private boolean isHomePageLetterVisited=false;
	private int campaignMsgViewed = 0;

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

	// Cohort ID
	private String cohortName;

	//Eligible Promo Variant Map
	private Map promoVariantMap;
	private String savingsVariantId;
	private boolean savingsVariantFound;

	private boolean isPostPromoConflictEnabled;
	private boolean isPromoConflictResolutionApplied;
	
	private FDGiftCardInfoList cachedGiftCards;
	
	//Create a dummy cart for Donation Orders.
	private FDCartModel donationCart = new FDCartModel();
	private Integer donationTotalQuantity = 0;
	
	private Map cachedRecipientInfo = null;
	
	private PricingContext pricingContext;
	
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

    public String getPrimaryKey() {
    	return super.getId();
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
		this.getShoppingCart().clearLineItemDiscounts();
		//this.setPromoVariantMap(null);
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
		this.promoVariantMap = null;
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

   public int getOrderCountForChefsTableEligibility() throws FDResourceException {
	   return FDCustomerManager.getOrderCountForChefsTableEligibility(this.identity);
   }

   public String getOrderTotalForChefsTableEligibility() throws FDResourceException {
	   OrderHistoryI orderHistory=getOrderHistory();

	   return NumberFormat.getCurrencyInstance(Locale.US).format(orderHistory.getOrderSubTotalForChefsTableEligibility());
   }

   public String getOrderCountRemainingForChefsTableEligibility() throws FDResourceException {
	   ChoiceFormat fmt = new ChoiceFormat(
	      "1#one |2#two |3#three | 4#four | 5#five");

		int orderCount = getOrderCountForChefsTableEligibility();
		if(orderCount == 0 || (orderCount >= CHEFS_TABLE_ORDER_COUNT_QUALIFIER) ||
				(CHEFS_TABLE_ORDER_COUNT_QUALIFIER - orderCount > CHEFS_TABLE_GETTING_CLOSE_COUNT)) {
			return "";
		}
		return fmt.format(CHEFS_TABLE_ORDER_COUNT_QUALIFIER - orderCount);
	}

	public String getOrderTotalRemainingForChefsTableEligibility() throws FDResourceException  {
		OrderHistoryI orderHistory=getOrderHistory();
		double orderTotal = orderHistory.getOrderSubTotalForChefsTableEligibility();
		if(orderTotal == 0.0 || (orderTotal >= CHEFS_TABLE_ORDER_TOTAL_QUALIFIER) ||
				CHEFS_TABLE_ORDER_TOTAL_QUALIFIER - orderTotal > CHEFS_TABLE_GETTING_CLOSE_TOTAL) {
			return "";
		}

		return new DecimalFormat("$#0").format(CHEFS_TABLE_ORDER_TOTAL_QUALIFIER - orderTotal);
	}

	public boolean isCloseToCTEligibilityByOrderCount() throws FDResourceException {
		int orderCount = getOrderCountForChefsTableEligibility();
		if( (CHEFS_TABLE_ORDER_COUNT_QUALIFIER - orderCount <= CHEFS_TABLE_GETTING_CLOSE_COUNT) &&
				(CHEFS_TABLE_ORDER_COUNT_QUALIFIER - orderCount > 0) ) {
			return true;
		}
		return false;
	}

	public boolean isCloseToCTEligibilityByOrderTotal() throws FDResourceException {
		double orderTotal = getOrderHistory().getOrderSubTotalForChefsTableEligibility();
		if( (CHEFS_TABLE_ORDER_TOTAL_QUALIFIER - orderTotal <= CHEFS_TABLE_GETTING_CLOSE_TOTAL) &&
				(CHEFS_TABLE_ORDER_TOTAL_QUALIFIER - orderTotal > 0.0 ) ) {
			return true;
		}
		return false;
	}

	public boolean hasQualifiedForCT() throws FDResourceException {
		double orderTotal = getOrderHistory().getOrderSubTotalForChefsTableEligibility();
		int orderCount = getOrderCountForChefsTableEligibility();

		if( (orderTotal >= CHEFS_TABLE_ORDER_TOTAL_QUALIFIER) ||
			(orderCount >= CHEFS_TABLE_ORDER_COUNT_QUALIFIER) ) {
				return true;
		}

		return false;
 	}



	public boolean isOkayToDisplayCTEligibility() throws FDResourceException {
		if(!isCloseToCTEligibilityByOrderTotal() && !isCloseToCTEligibilityByOrderCount()) {
			return false;
		}
		Calendar cal = new GregorianCalendar(Locale.getDefault());
		cal = Calendar.getInstance();
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if( lastDay == cal.get(Calendar.DAY_OF_MONTH) ||
				cal.get(Calendar.DAY_OF_MONTH) <= 2) {
			return false;
		}
		return true;
	}

	public String getEndChefsTableQualifyingDate() throws FDResourceException {
		Calendar cal = new GregorianCalendar(Locale.getDefault());
		cal = Calendar.getInstance();
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		return new SimpleDateFormat("MMMMM d, yyyy").format(cal.getTime());
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

	public String getChefsTableInduction() throws FDResourceException {

		FDCustomerModel customer = this.getFDCustomer();
		if (customer == null || customer.getProfile() == null) {
			return "0";
		} else {
			return customer.getProfile().getChefsTableInduction();
		}
	}

	public String getWinback() throws FDResourceException {
		if (this.identity == null) {
			return "false";
		}
		FDCustomerModel customer = this.getFDCustomer();
		if (null == customer || null == customer.getProfile() || null == customer.getProfile().getWinback()) {
			return "false";
		} else {
			String value = customer.getProfile().getWinback().trim();
			if("".equals(value)) {
				return "false";
			}
			return value;
		}
	}

	public String getWinbackPath() throws FDResourceException {
		// winback path is in the form of "YYMMDD_winback_segment"
		String winback = getWinback();
		if(winback.equals("false"))
			return winback;

		StringTokenizer st = new StringTokenizer(winback, "_");
		int countTokens = st.countTokens();
		if (countTokens < 3)
			return "false";
		st.nextToken(); // date token which we don't need
		return FDStoreProperties.getWinbackRoot() + st.nextToken()+ "/" + st.nextToken() + ".html";
	}

	public String getMarketingPromoPath() throws FDResourceException {
		// marketingPromo path is in the form of "campaign_campaign2_segment"
		// a valid marketing promo value is in the form of "mktg_deli_default"		
		String mktgPromo = getMarketingPromo();
		if(mktgPromo.equals("false"))
			return mktgPromo;

		StringTokenizer st = new StringTokenizer(mktgPromo, "_");
		int countTokens = st.countTokens();
		if (countTokens < 3)
			return "false";
		return FDStoreProperties.getMarketingPromoRoot() + st.nextToken() + "/" + st.nextToken()+ "/" + st.nextToken() + ".html";
	}

	public String getMarketingPromo() throws FDResourceException {

		if (this.identity == null) {
			return "false";
		}
		FDCustomerModel customer = this.getFDCustomer();
		if (null == customer || null == customer.getProfile() || null == customer.getProfile().getMarketingPromo()) {
			return "false";
		} else {
			String value = customer.getProfile().getMarketingPromo().trim();
			if("".equals(value)) {
				return "false";
			}
			return value;
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
	
	@Override
        public boolean hasServiceBasedOnUserAddress(EnumServiceType type) {
            try {
                return type != null && getUserServicesBasedOnAddresses().contains(type);
            } catch (FDResourceException e) {
                throw new FDRuntimeException(e);
            }
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
//		return true;
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

	public double getCorpDeliveryFeeMonday() {
		return CORP_DELIVERY_FEE_MONDAY;
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
	
	public boolean isGiftCardsEnabled() {
		return SiteFeatureHelper.isEnabled(EnumSiteFeature.GIFT_CARDS, this);
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

	public boolean isCampaignMsgLimitViewed() {
		if(getCampaignMsgViewed() >= FDStoreProperties.getImpressionLimit())
			return true;
		return false;
	}

	public int getCampaignMsgViewed() {
		return campaignMsgViewed;
	}

	public void setCampaignMsgViewed(int campaignMsgViewed) {
		this.campaignMsgViewed = campaignMsgViewed;
	}
	/**
	 * Returns user's cohort ID
	 *
	 * @param user
	 * @return
	 */
	public String getCohortName() {
		return this.cohortName;
	}

	public void setCohortName(String cohortName) {
		this.cohortName = cohortName;
	}

	public void createCohortName() throws FDResourceException {
		this.cohortName = CohortSelector.getInstance().getCohortName(getPrimaryKey());
		FDCustomerManager.storeCohortName(this);
	}
	
	public int getTotalCartSkuQuantity(String args[]){
		Collection c = Arrays.asList(args);
		Set argSet = new HashSet(c);
        if(args==null) {
                    //System.out.println("** args :"+args);
                    return 0;
        }

        if(this.shoppingCart==null || this.shoppingCart.getOrderLines()==null) return 0;
            int count=0;
                    for (Iterator j = this.shoppingCart.getOrderLines().iterator(); j.hasNext();) {
                                FDCartLineI line = (FDCartLineI) j.next();
                                for(Iterator i=argSet.iterator();i.hasNext();)
                                {
                                            String sku=(String)i.next();
                                            if (sku.equals(line.getSkuCode()))
                                            {
                                                        count += line.getQuantity();
                                            }
                                }
           }

        return count;

	}

	public Map getPromoVariantMap(){
		if (this.promoVariantMap==null) {
			this.updateUserState();
		}
		return this.promoVariantMap;
	}

	public void setPromoVariantMap(Map pvMap) {
		this.promoVariantMap = pvMap;
	}

	public PromoVariantModel getPromoVariant(String variantId) {
		return (PromoVariantModel) this.getPromoVariantMap().get(variantId);
	}

	public String getSavingsVariantId() {
		return savingsVariantId;
	}
	public void setSavingsVariantId(String savingsVariantId) {
		this.savingsVariantId = savingsVariantId;
	}

	public boolean isSavingsVariantFound() {
		return savingsVariantFound;
	}

	public void setSavingsVariantFound(boolean savingsVariantFound) {
		this.savingsVariantFound = savingsVariantFound;
	}


	/**
	 * @return Always returns null
	 * @see com.freshdirect.fdstore.customer.FDUserI#getFavoriteTabFeature()
	 */
	public String getFavoriteTabFeature() {
		return null;
	}

	/**
	 * Calling this function has no effect (only FDSessionUser implements it)
	 *
	 * @param feature ignored
	 * @see com.freshdirect.fdstore.customer.FDUserI#setFavoriteTabFeature(java.lang.String)
	 */
	public void setFavoriteTabFeature(String feature) {
		// has no effect
	}

	public boolean isPostPromoConflictEnabled() {
		return isPostPromoConflictEnabled;
	}

	public void setPostPromoConflictEnabled(boolean isPostPromoConflictEnabled) {
		this.isPostPromoConflictEnabled = isPostPromoConflictEnabled;
	}

	public boolean isPromoConflictResolutionApplied() {
		return isPromoConflictResolutionApplied;
	}

	public void setPromoConflictResolutionApplied(
			boolean isPromoConflictResolutionApplied) {
		this.isPromoConflictResolutionApplied = isPromoConflictResolutionApplied;
	}

	public FDGiftCardInfoList getGiftCardList() {
		if (getLevel() == FDUserI.GUEST) {
			// We don't have an identity 
			return null;
		}
		if (cachedGiftCards == null) {
			try {
				cachedGiftCards = FDCustomerManager.getGiftCards(identity);
//				getGCRecipientInfo();
				
			} catch (Exception e) {
				throw new FDRuntimeException(e);
			}
		}
		return cachedGiftCards;
	}

	private void getGCRecipientInfo() throws FDResourceException {
		if(null != cachedGiftCards && null != cachedGiftCards.getGiftcards() && !cachedGiftCards.getGiftcards().isEmpty()){
			List saleIds = new ArrayList();
			for (Iterator iterator = cachedGiftCards.getGiftcards().iterator(); iterator
					.hasNext();) {
				FDGiftCardModel lFDGiftCardModel = (FDGiftCardModel) iterator.next();
				if(!saleIds.contains(lFDGiftCardModel.getGiftCardModel().getPurchaseSaleId())){
					saleIds.add(lFDGiftCardModel.getGiftCardModel().getPurchaseSaleId());
				}
				
			}
			if(!saleIds.isEmpty()){
				//Get the recipient info for all these distinct saleIds.
				cachedRecipientInfo = FDCustomerManager.getGiftCardRecepientsForOrders(saleIds);
			}
		}
	}
	
	public String getGCSenderName(String certNum, String saleId){
		if (getLevel() == FDUserI.GUEST) {
			// We don't have an identity 
			return null;
		}
		ErpGCDlvInformationHolder holder = null;
		try {
			if(null == cachedRecipientInfo){
//				getGCRecipientInfo();
				cachedRecipientInfo = new HashMap();
			}
			/*if(null !=cachedRecipientInfo){
				if(cachedRecipientInfo.containsKey(saleId)){
					 recipientList = (List)cachedRecipientInfo.get(saleId);
				}else{
					recipientList = FDCustomerManager.getGiftCardRecepientsForOrder(saleId);
					cachedRecipientInfo.put(saleId, recipientList);				
				}
				
				if(null != recipientList && !recipientList.isEmpty()){
					for (Iterator iterator = recipientList.iterator(); iterator
							.hasNext();) {
						ErpGCDlvInformationHolder holder = (ErpGCDlvInformationHolder) iterator.next();
						if(holder.getCertificationNumber().equalsIgnoreCase(certNum)){
							return holder.getRecepientModel().getSenderName();
						}
						
					}
				}
			}*/
			if(null !=cachedRecipientInfo){
				if(cachedRecipientInfo.containsKey(certNum)){
					 holder = (ErpGCDlvInformationHolder)cachedRecipientInfo.get(certNum);
				}else{
					holder = FDCustomerManager.GetGiftCardRecipentByCertNum(certNum);
					cachedRecipientInfo.put(certNum, holder);				
				}
				if(null != holder){
					return holder.getRecepientModel().getSenderName();
				}
			}
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
		return null;
	}
	
	public void invalidateGiftCards() {
		this.cachedGiftCards = null;
	}
	
	public double getGiftcardBalance() {
		if(this.getGiftCardList() == null) return 0.0;
		if(this.getShoppingCart() instanceof FDModifyCartModel) {
			return this.getGiftCardList().getTotalBalance();
		} else {
			//Clear all hold amounts.
			if(null!=this.getGiftCardList()){
				this.getGiftCardList().clearAllHoldAmount();
				return this.getGiftCardList().getTotalBalance();
			}
			return 0;
		}
	}
	
	public FDCartModel getGiftCart() {
		return this.dummyCart;
	}
	
	public void setGiftCart(FDCartModel dcart) {
		this.dummyCart = dcart;
	}
	
	public FDRecipientList getRecipentList(){
		if( null == this.recipientList) {
			this.recipientList = new FDRecipientList();
		}
		return this.recipientList;
	}
	
	public void setRecipientList(FDRecipientList r) {
		this.recipientList = r;
	}
	
    public boolean isGCOrderMinimumMet() {
		double subTotal = this.getRecipentList().getSubtotal();
		return subTotal >= this.getMinimumOrderAmount();
    }
    
    public double getGCMinimumOrderAmount() {
		return MIN_GC_ORDER_AMOUNT;
	}
    

	public FDBulkRecipientList getBulkRecipentList(){
		if( null == this.bulkRecipientList) {
			this.bulkRecipientList = new FDBulkRecipientList();
		}
		return this.bulkRecipientList;
	}
	
	public void setBulkRecipientList(FDBulkRecipientList r) {
		this.bulkRecipientList = r;
	}
    
	public Integer getDonationTotalQuantity(){
		return donationTotalQuantity;
	}
	
	public void setDonationTotalQuantity(Integer donationTotalQuantity){
		this.donationTotalQuantity = donationTotalQuantity;
	}

	
	public FDCartModel getDonationCart() {		
		return donationCart;
	}

	
	public void setDonationCart(FDCartModel dcart) {
		this.donationCart = dcart;		
	}

	public double getGiftcardsTotalBalance(){
		if(this.getGiftCardList() == null) return 0.0;
		if(this.getShoppingCart() instanceof FDModifyCartModel) {
			return this.getGiftCardList().getGiftcardsTotalBalance();
		} else {
			//Clear all hold amounts.
			if(null!=this.getGiftCardList()){
				this.getGiftCardList().clearAllHoldAmount();
				return this.getGiftCardList().getGiftcardsTotalBalance();
			}
			return 0;
		}
	}
	
	 public int getTotalRegularOrderCount() throws FDResourceException {
	        return this.getOrderHistory().getTotalRegularOrderCount();
	    }

	 public Collection<EnumServiceType> getUserServicesBasedOnAddresses() throws FDResourceException {
	     if (servicesBasedOnAddress==null) {
	         ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(getIdentity().getErpCustomerPK());	     
	         List<ErpAddressModel> shipToAddresses = erpCustomer.getShipToAddresses();
	         servicesBasedOnAddress = new HashSet<EnumServiceType> ();
	         for (ErpAddressModel m : shipToAddresses) {
	             servicesBasedOnAddress.add(m.getServiceType());
	         }
	     }
	     return servicesBasedOnAddress;
	 }
	 
	 public String getPricingZoneId(){
		 if(this.pricingContext == null) return null;
		 return this.pricingContext.getZoneId();
	 }

	public PricingContext getPricingContext() {
		try {
			if(this.pricingContext == null){
				//Pricing context is yet to be set. Load it from DB.
				String zoneId=FDZoneInfoManager.findZoneId(getSelectedServiceType().getName(), getZipCode());
				this.pricingContext = new PricingContext(zoneId);
			}
		}catch (FDResourceException e) {
			throw new FDRuntimeException(e.getMessage());
		}
		return pricingContext;
	}

	protected void setPricingContext(PricingContext pricingContext) {
		this.pricingContext = pricingContext;
	}

	public void resetPricingContext(){
		this.setPricingContext(null);
	} 
	//Added for Junit testing.
	public void createDummyPricingContext() {
		this.setPricingContext(new PricingContext(ZonePriceListing.MASTER_DEFAULT_ZONE));
	}
	
	public String constructZoneIdForQueryString(){
		String zoneIdParam = "";
		try {
			String zoneId = FDZoneInfoManager.findZoneId(getSelectedServiceType().getName(), getZipCode());
			if(zoneId.equalsIgnoreCase(ZonePriceListing.MASTER_DEFAULT_ZONE)){
				zoneIdParam = "zonelevel=true && mzid="+zoneId;
			}else if(zoneId.equalsIgnoreCase(ZonePriceListing.RESIDENTIAL_DEFAULT_ZONE)||zoneId.equalsIgnoreCase(ZonePriceListing.CORPORATE_DEFAULT_ZONE)){
				zoneIdParam = "zonelevel=true && szid="+zoneId+"mzid="+ZonePriceListing.MASTER_DEFAULT_ZONE;
			}else{
				zoneIdParam = "zonelevel=true && zid="+zoneId;
				zoneId = FDZoneInfoManager.findZoneId(getSelectedServiceType().getName(),null);
				zoneIdParam = zoneIdParam + "&& szid="+zoneId+"mzid="+ZonePriceListing.MASTER_DEFAULT_ZONE;
			}
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e.getMessage());
		}
		return zoneIdParam;
	}
}

