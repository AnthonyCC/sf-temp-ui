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
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.FDCouponProperties;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.FulfillmentContext;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumAlertType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDPAutoRenewalType;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
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
import com.freshdirect.fdstore.OncePerRequestDateCache;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.EnumWinePrice;
import com.freshdirect.fdstore.content.HolidayGreeting;
import com.freshdirect.fdstore.content.MyFD;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.fdstore.customer.adapter.PromotionContextAdapter;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.fdstore.lists.CclUtils;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.FDPromotionVisitor;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.SignupDiscountRule;
import com.freshdirect.fdstore.promotion.WaiveDeliveryCharge;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.fdstore.rollout.EnumFeatureRolloutStrategy;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.rules.EligibilityCalculator;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.survey.EnumSurveyType;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyConstants;
import com.freshdirect.fdstore.survey.FDSurveyFactory;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.util.IgnoreCaseString;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.fdstore.zone.FDZoneInfoManager;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.logistics.analytics.model.SessionEvent;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.FulfillmentInfo;
import com.freshdirect.logistics.delivery.model.SalesArea;
import com.freshdirect.smartstore.fdstore.CohortSelector;
import com.freshdirect.smartstore.fdstore.DatabaseScoreFactorProvider;

public class FDUser extends ModelSupport implements FDUserI {

    private final static Category LOGGER = LoggerFactory.getInstance(FDUser.class);

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
    // added for zone pricing to capture zp service type.
    private EnumServiceType zpServiceType = null;

    private FDIdentity identity;
    private String custSapId;
    private SessionEvent event;

    private AddressModel address;
    private FDReservation reservation;
    private FDCartModel shoppingCart /*= initializeCart()*/; // It was causing issue in SF2.0

    private FDCartModel soTemplateCart = new FDCartModel();
    // Creating a dummy cart for gift card processing.
    private FDCartModel dummyCart = new FDCartModel();
    private FDRecipientList recipientList;
    private FDBulkRecipientList bulkRecipientList;

    private SignupDiscountRule signupDiscountRule;
    private boolean promotionAddressMismatch = false;
    private String redeemedPromotionCode;

    private String cookie;
    private boolean loggedIn = false;

    private boolean surveySkipped = false;
    public String fromLogin;
    
    private transient ErpCustomerInfoModel customerInfoModel;
    private transient FDOrderHistory cachedOrderHistory;
    private transient CustomerAvgOrderSize historicOrderSize;
    private transient FDCustomerModel cachedFDCustomer;
    protected transient FDPromotionEligibility promotionEligibility;
    private transient Boolean checkEligible;
    private transient Boolean referrerEligible;
    private transient String regRefTrackingCode;
    private transient List<FDCustomerListInfo> cclListInfos;
    private transient List<FDCustomerListInfo> soListInfos;

    private String lastRefTrackingCode;

    private String lastRefProgId = null;
    private String lastRefTrkDtls = null;
    private String lastRefProgInvtId = null;

    private String userId;

    private boolean active = false;
    private boolean receiveFDemails = true;

    private boolean isHomePageLetterVisited = false;
    private int campaignMsgViewed = 0;

    // Contains user specific Delivery Pass Details.
    private FDUserDlvPassInfo dlvPassInfo;

    private Map<String, AssignedCustomerParam> assignedCustomerParams;

    private EnumWinePrice preferredWinePrice = null;

    /*
     * This attribute caches the list of product keys that are already evaluated for DCPD along with its DCPD promo info. Only used by Web tier for applying promotions. So
     * transient.
     */
    private transient DCPDPromoProductCache dcpdPromoProductCache;
    // New Promotion History cache. PERF-22.
    private transient ErpPromotionHistory cachedPromoHistory;

    // Cohort ID
    private String cohortName;

    // Eligible Promo Variant Map
    private Map promoVariantMap;
    private String savingsVariantId;
    private boolean savingsVariantFound;

    private boolean isPostPromoConflictEnabled;
    private boolean isPromoConflictResolutionApplied;

    private FDGiftCardInfoList cachedGiftCards;

    // Create a dummy cart for Donation Orders.
    private FDCartModel donationCart = new FDCartModel();
    private Integer donationTotalQuantity = 0;

    private Map<String, ErpGCDlvInformationHolder> cachedRecipientInfo = null;

    private UserContext userContext;

    protected Boolean isSOEligible = null;
    protected Boolean hasEBTAlert = null;
    protected Boolean isEcheckRestricted = null;
    protected EnumDPAutoRenewalType hasAutoRenewDP = null;

    protected Boolean cliCodeEligible = null;
    private Set<String> allAppliedPromos = new HashSet<String>();

    protected SortedSet<IgnoreCaseString> clientCodesHistory = null;
    private Map<String, Integer> promoErrorCodes = new ConcurrentHashMap<String, Integer>();

    private MasqueradeContext masqueradeContext;

    private int ctSlots;
    private double percSlotsSold;

    private Date registrationDate;

    private static final Date EPOCH = new Date(0);

    // mergePendingOrder (APPDEV-2031)
    private boolean showPendingOrderOverlay = true;
    private boolean suspendShowPendingOrderOverlay = false;
    private FDCartModel mergePendCart = new FDCartModel();

    /* Appdev-1888 */
    private String referralLink;
    private String referralPrgmId;
    private String referralCustomerId;
    List<PromotionI> referralPromoList = new ArrayList<PromotionI>();
    Double totalCredit = null;
    Boolean referralFlag = null;
    boolean referralFraud = false;
    public String tsaPromoCode = null;

    private EnumGiftCardType giftCardType = null;

    private boolean ebtAccepted = false;

    private List<ErpAddressModel> cachedAllHomeAddresses;
    private List<ErpAddressModel> cachedAllCorporateAddresses;
    private boolean robot; // true if user object represents a search bot or crawler

    private Set<String> steeringSlotIds = new HashSet<String>();

    private Set<ExternalCampaign> externalPromoCampaigns = new HashSet<ExternalCampaign>();

    private FDCustomerCouponWallet couponWallet;

    private ExternalCampaign externalCampaign;

    private String defaultListId = null;

    private boolean anyNewOrder = false;

    private String clientIp;
    private String serverName;

    /* APPDEV-3756 */
    private boolean isGlobalNavTutorialSeen = false;
    private List<ProductReference> productSamples;
    private Date firstOrderDate = null;
    private Date firstOrderDateByStore = null;

    private boolean crmMode;
    private String rafClickId;
    private String rafPromoCode;

    /* APPDEV-4381 */
    private Date tcAcknowledgeDate = null;
    private boolean tcAcknowledge = false;

    private boolean vHPopupDisplay = false;

    private Collection<FDStandingOrder> validSO3s = new ArrayList<FDStandingOrder>();
    
    private Collection<FDStandingOrder> allSO3s = new ArrayList<FDStandingOrder>();
    
    private Map<String, Object> validSO3Data = new HashMap<String, Object>();

    private boolean refreshSO3 = true;
    
    private boolean refreshSO3Settings = true;

    private boolean isZipCheckPopupUsed = false;
    
    private Map<String,String> soCartLineMessagesMap=new HashMap<String,String>();
    
    private boolean refreshSoCartOverlay = true;
    
    private boolean  soCartOverlayFirstTime = false;
    
    private boolean refreshNewSoFeature = true;
    
    private boolean soFeatureOverlay = false;
    
	public Date getTcAcknowledgeDate() {
        return tcAcknowledgeDate;
    }

    public void setTcAcknowledgeDate(Date tcAcknowledgeDate) {
        this.tcAcknowledgeDate = tcAcknowledgeDate;

    }

    @Override
    public boolean getTcAcknowledge() {
        // Until T&C is enabled, treat customers have accepted T&C.
        if (!FDStoreProperties.isTCEnabled()) {
            return true;
        }

        try {
            this.cachedFDCustomer = this.getFDCustomer();
        } catch (FDResourceException e) {
            LOGGER.warn("Error in getFDCustomer() " + e);
        }
        if (this.cachedFDCustomer != null && this.cachedFDCustomer.getCustomerEStoreModel() != null) {
            return this.cachedFDCustomer.getCustomerEStoreModel().getTcAcknowledge();
        } else {
            return false;
        }

    }

    public void setTcAcknowledge(boolean tcAcknowledge) {
        // this.tcAcknowledge = tcAcknowledge;
        if (null == this.cachedFDCustomer) {
            try {
                this.cachedFDCustomer = FDCustomerFactory.getFDCustomer(this.identity);
            } catch (FDResourceException e) {
                LOGGER.warn("Error in setTcAcknowledge() " + e);
            }
        }
        if (null != this.cachedFDCustomer) {
            this.cachedFDCustomer.getCustomerEStoreModel().setTcAcknowledge(true);
        }
    }

    @Override
    public String getTsaPromoCode() {
        return tsaPromoCode;
    }

    private FDCartModel initializeCart() {
        if (this.shoppingCart == null) {
            this.shoppingCart = new FDCartModel();
            this.shoppingCart.setEStoreId(this.getUserContext().getStoreContext().getEStoreId());
        }
        return this.shoppingCart;
    }

    @Override
    public void setTsaPromoCode(String tsaPromoCode) {
        this.tsaPromoCode = tsaPromoCode;
    }

    @Override
    public FDUserDlvPassInfo getDlvPassInfo() {
        return dlvPassInfo;
    }

    public void setDlvPassInfo(FDUserDlvPassInfo dlvPassInfo) {
        this.dlvPassInfo = dlvPassInfo;
    }

    public FDUser(PrimaryKey pk) {
        super();
        this.shoppingCart = initializeCart(); // Added due to the issue in SF 2.0
        this.setPK(pk);
    }

    public FDUser() {
        super();
      this.shoppingCart =  initializeCart(); // Added due to the issue in SF 2.0
    }

    public FDUser(UserContext userContext) {
        super();
        this.userContext = userContext;
     // Added due to the issue in SF 2.0
        if (this.shoppingCart == null) {
            this.shoppingCart = new FDCartModel();
            this.shoppingCart.setEStoreId(this.getUserContext().getStoreContext().getEStoreId());
        }
    }
    
    // Used only for Storefront 2.0
    public FDUser(PrimaryKey pk,UserContext userContext) {
        super();
        this.setPK(pk);
        this.userContext = userContext;
    }

    @Override
    public EnumTransactionSource getApplication() {
        return application;
    }

    public void setApplication(EnumTransactionSource source) {
        this.application = source;
    }

    @Override
    public String getCookie() {
        return this.cookie;
    }

    @Override
    public void setCookie(String cookie) {
        this.cookie = cookie;
        this.invalidateCache();
    }

    public String getState() {
        return this.address == null ? null : this.address.getState();
    }

    @Override
    public String getZipCode() {
        return this.address == null ? null : this.address.getZipCode();
    }
    @Override
    public void setZipCode(String zipCode) {
    	setZipCode(zipCode, true);
    }
    
    public void setZipCode(String zipCode, boolean populateUserContext) {
        AddressModel a = new AddressModel();
        a.setZipCode(zipCode);
        this.address = a;
        this.invalidateCache();
        if (populateUserContext) {
	        this.userContext = null;
	        this.userContext = getUserContext();
        }
    }

    @Override
    public void setAddress(AddressModel a) {
        this.address = a;
        this.invalidateCache();
        this.userContext = null;
        this.userContext = getUserContext();

    }

    public void setAddress(AddressModel a, boolean populateUserContext) {
        this.address = a;
        this.invalidateCache();
        if(populateUserContext){
        	this.userContext = null;
        	this.userContext = getUserContext();
        }

    }

    
    @Override
    public AddressModel getAddress() {
        return this.address;
    }

    @Override
    public String getPrimaryKey() {
        return super.getId();
    }

    @Override
    public FDIdentity getIdentity() {
        return this.identity;
    }

    @Override
    public void setIdentity(FDIdentity identity) {
        this.identity = identity;
        this.invalidateCache();

    }

    @Override
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

    @Override
    public boolean isInZone() {
        return this.isDeliverableUser();
    }

    @Override
    public boolean isDeliverableUser() {
        return this.availableServices.contains(EnumServiceType.HOME) || this.availableServices.contains(EnumServiceType.DEPOT)
                || this.availableServices.contains(EnumServiceType.CORPORATE);
    }

    @Override
    public void isLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Override
    public FDCartModel getShoppingCart() {
        return this.shoppingCart;
    }

    @Override
    public void setShoppingCart(FDCartModel cart) {
        this.shoppingCart = cart;
    }

    @Override
    public boolean isSurveySkipped() {
        return surveySkipped;
    }

    @Override
    public void setSurveySkipped(boolean surveySkipped) {
        this.surveySkipped = surveySkipped;
    }

    @Override
    public boolean isFraudulent() throws FDResourceException {
        if (this.identity == null) {
            return false;
        }
        return !this.getFDCustomer().isEligibleForSignupPromo();
    }

    @Override
    public FDCustomerModel getFDCustomer() throws FDResourceException {
        if (this.identity == null) {
            throw new IllegalStateException("No identity");
        }
        if (this.cachedFDCustomer == null) {
            this.cachedFDCustomer = FDCustomerFactory.getFDCustomer(this.identity);
        }
        return this.cachedFDCustomer;

    }

    @Override
    public double getMaxSignupPromotion() {
        if (this.signupDiscountRule != null) {
            return this.signupDiscountRule.getMaxAmount();
        } else {
            return 0.0;
        }
    }

    @Override
    public SignupDiscountRule getSignupDiscountRule() {
        if (this.promotionEligibility == null) {
            updateUserState(false);
        }
        return this.signupDiscountRule;
    }

    @Override
    public void setSignupDiscountRule(SignupDiscountRule discountRule) {
        this.signupDiscountRule = discountRule;
    }

    @Override
    public boolean isPromotionAddressMismatch() {
        return promotionAddressMismatch;
    }

    @Override
    public void setPromotionAddressMismatch(boolean b) {
        promotionAddressMismatch = b;
    }

    @Override
    public void setRedeemedPromotion(PromotionI promotion) {
        this.redeemedPromotionCode = promotion == null ? null : promotion.getPromotionCode();
    }

    @Override
    public PromotionI getRedeemedPromotion() {
        return this.redeemedPromotionCode == null ? null : PromotionFactory.getInstance().getPromotion(this.redeemedPromotionCode);
    }

    /* APPDEV-1888 */
    @Override
    public List<PromotionI> getReferralPromoList() {
        return referralPromoList;
    }

    @Override
    public void setReferralPromoList() {
        // load referral promotion only to refer a friend target customers
        if (this.getIdentity() != null && FDStoreProperties.isExtoleRafEnabled() ? this.getRafPromoCode() != null : this.getReferralCustomerId() != null)
            try {
                referralPromoList = FDPromotionNewManager.getReferralPromotions(this.getIdentity().getErpCustomerPK());
            } catch (FDResourceException e) {
                LOGGER.error("Error getting referral promotions.", e);
            }
    }

    @Override
    public void updateUserState() {
    	this.updateUserState(true);        
    }
    
    @Override
    public void updateUserState(boolean syncServiceType){
    	try {
	    	if(syncServiceType){
	    		//sync the selected service type with the address's service type from the cart.
	        	if(null !=this.getShoppingCart().getDeliveryAddress()){
	        		ErpAddressModel address = this.getShoppingCart().getDeliveryAddress();
	        		if(!this.getSelectedServiceType().equals(address.getServiceType())){
	        			this.setSelectedServiceType(address.getServiceType());
	        			this.setZPServiceType(address.getServiceType());
	        		}
	        	}
	    	}
    	
        	
            this.getShoppingCart().recalculateTaxAndBottleDeposit(getZipCode());
            this.getShoppingCart().updateSurcharges(new FDRulesContextImpl(this));
            /* APPDEV-1888 */
            if (this.getReferralPromoList().size() == 0) {
                this.setReferralPromoList();
            }
            this.applyPromotions();
            this.applyOrderMinimum();
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e.getMessage());
        }
    	
    }

    @Override
    public void applyOrderMinimum() {
        if (this.getReservation() != null)
            TimeslotLogic.applyOrderMinimum(this, this.getReservation().getTimeslot());
        if (this.getShoppingCart() != null && this.getShoppingCart().getDeliveryReservation() != null)
            TimeslotLogic.applyOrderMinimum(this, this.getShoppingCart().getDeliveryReservation().getTimeslot());
    }

    private void applyPromotions() {
        // clear previous promotions
        this.setSignupDiscountRule(null);
        this.setPromotionAddressMismatch(false);

        this.getShoppingCart().clearSampleLines();
        this.getShoppingCart().setDiscounts(new ArrayList<ErpDiscountLineModel>());
        this.getShoppingCart().clearSkuCount();
        this.getShoppingCart().clearLineItemDiscounts();
        this.clearPromoErrorCodes();
        this.getShoppingCart().setDlvPassExtn(null);
        this.getShoppingCart().setDlvPromotionApplied(false);
        if ((this.getShoppingCart().getDeliveryPassCount() > 0) || (this.isDlvPassActive())) {
            this.getShoppingCart().setDlvPassApplied(true);
        }

        // evaluate special dlv charge override
        WaiveDeliveryCharge.apply(this);

        this.promotionEligibility = new FDPromotionEligibility();
        // apply promotions
        this.promotionEligibility = FDPromotionVisitor.evaluateAndApplyPromotions(new PromotionContextAdapter(this), promotionEligibility);
        // Add all applied promotion codes so far to this list. Used by MaxRedemptionStrategy
        this.allAppliedPromos.addAll(promotionEligibility.getAppliedPromotionCodes());
    }

    @Override
    public String getFirstName() throws FDResourceException {
        ErpCustomerInfoModel info = getCustomerInfoModel();
        if (info == null) {
            return "";
        }
        return info.getFirstName();
    }

    @Override
    public String getLastName() throws FDResourceException {
        ErpCustomerInfoModel info = getCustomerInfoModel();
        if (info == null) {
            return "";
        }
        return info.getLastName();
    }

    @Override
    public String getDepotCode() {
        return this.depotCode;
    }

    @Override
    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
        this.invalidateCache();
    }

    @Override
    public boolean isDepotUser() {
        return depotCode != null;
    }

    @Override
    public boolean isCorporateUser() {
        return EnumServiceType.CORPORATE.equals(this.selectedServiceType); // || this.availableServices.contains(EnumServiceType.CORPORATE);
    }

    @Override
    public void invalidateCache() {
        // Commented as part of PERF-22 task.
        // this.cachedOrderHistory = null;
        this.signupDiscountRule = null;
        this.cachedFDCustomer = null;
        this.customerInfoModel = null;
        this.promotionEligibility = null;
        this.checkEligible = null;
        this.referrerEligible = null;
        this.regRefTrackingCode = null;
        this.cclListInfos = null;
        this.soListInfos = null;
        this.cachedPromoHistory = null;
        this.promoVariantMap = null;
        this.isSOEligible = null;
        this.hasEBTAlert = null;
        this.vHPopupDisplay = false;
        this.isEcheckRestricted = null;
        this.hasAutoRenewDP = null;

    }

    /*
     * This method was introduced as part of PERF-22 task. Seperate invalidation of Order History Cache from other caches.
     */
    @Override
    public void invalidateOrderHistoryCache() {
        this.cachedOrderHistory = null;
    }

    @Override
    public OrderHistoryI getOrderHistory() throws FDResourceException {
        if (this.cachedOrderHistory == null) {
            this.cachedOrderHistory = FDCustomerManager.getOrderHistoryInfo(this.identity);
        }

        return this.cachedOrderHistory;
    }

    @Override
    public Date getFirstOrderDate() throws FDResourceException {
        if (null == firstOrderDate) {
            OrderHistoryI orderHistory = this.getOrderHistory();
            if (null != orderHistory) {
                firstOrderDate = orderHistory.getFirstOrderDate();
            }
        }
        return firstOrderDate;
    }

    /*
     * private OrderHistoryI getOrderHistoryInfo() throws FDResourceException {
     * 
     * This change is rollbacked temporarily. if(EnumTransactionSource.CUSTOMER_REP.equals(application)){ //If CRM load entire order history. return
     * FDCustomerManager.getOrderHistoryInfo(this.identity); } else { //Load only Order History Summary. return FDCustomerManager.getWebOrderHistoryInfo(this.identity); }
     * 
     * //Load Entire order history inspite of CRM or WEB. return FDCustomerManager.getOrderHistoryInfo(this.identity); }
     */

    @Override
    public CustomerAvgOrderSize getHistoricOrderSize() throws FDResourceException {
        if (this.historicOrderSize == null && this.identity != null) {
            this.historicOrderSize = FDCustomerManager.getHistoricOrderSize(this.identity.getErpCustomerPK());
        }
        return this.historicOrderSize;
    }

    @Override
    public int getOrderCountForChefsTableEligibility() throws FDResourceException {
        OrderHistoryI orderHistory = getOrderHistory();
        return orderHistory.getOrderCountForChefsTableEligibility();
    }

    @Override
    public String getOrderTotalForChefsTableEligibility() throws FDResourceException {
        OrderHistoryI orderHistory = getOrderHistory();

        return NumberFormat.getCurrencyInstance(Locale.US).format(orderHistory.getOrderSubTotalForChefsTableEligibility());
    }

    @Override
    public String getOrderCountRemainingForChefsTableEligibility() throws FDResourceException {
        ChoiceFormat fmt = new ChoiceFormat("1#one |2#two |3#three | 4#four | 5#five");

        int orderCount = getOrderCountForChefsTableEligibility();
        if (orderCount == 0 || (orderCount >= CHEFS_TABLE_ORDER_COUNT_QUALIFIER) || (CHEFS_TABLE_ORDER_COUNT_QUALIFIER - orderCount > CHEFS_TABLE_GETTING_CLOSE_COUNT)) {
            return "";
        }
        return fmt.format(CHEFS_TABLE_ORDER_COUNT_QUALIFIER - orderCount);
    }

    @Override
    public String getOrderTotalRemainingForChefsTableEligibility() throws FDResourceException {
        OrderHistoryI orderHistory = getOrderHistory();
        double orderTotal = orderHistory.getOrderSubTotalForChefsTableEligibility();
        if (orderTotal == 0.0 || (orderTotal >= CHEFS_TABLE_ORDER_TOTAL_QUALIFIER) || CHEFS_TABLE_ORDER_TOTAL_QUALIFIER - orderTotal > CHEFS_TABLE_GETTING_CLOSE_TOTAL) {
            return "";
        }

        return new DecimalFormat("$#0").format(CHEFS_TABLE_ORDER_TOTAL_QUALIFIER - orderTotal);
    }

    @Override
    public boolean isCloseToCTEligibilityByOrderCount() throws FDResourceException {
        int orderCount = getOrderCountForChefsTableEligibility();
        if ((CHEFS_TABLE_ORDER_COUNT_QUALIFIER - orderCount <= CHEFS_TABLE_GETTING_CLOSE_COUNT) && (CHEFS_TABLE_ORDER_COUNT_QUALIFIER - orderCount > 0)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCloseToCTEligibilityByOrderTotal() throws FDResourceException {
        double orderTotal = getOrderHistory().getOrderSubTotalForChefsTableEligibility();
        if ((CHEFS_TABLE_ORDER_TOTAL_QUALIFIER - orderTotal <= CHEFS_TABLE_GETTING_CLOSE_TOTAL) && (CHEFS_TABLE_ORDER_TOTAL_QUALIFIER - orderTotal > 0.0)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasQualifiedForCT() throws FDResourceException {
        double orderTotal = getOrderHistory().getOrderSubTotalForChefsTableEligibility();
        int orderCount = getOrderCountForChefsTableEligibility();

        if ((orderTotal >= CHEFS_TABLE_ORDER_TOTAL_QUALIFIER) || (orderCount >= CHEFS_TABLE_ORDER_COUNT_QUALIFIER)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isOkayToDisplayCTEligibility() throws FDResourceException {
        if (!isCloseToCTEligibilityByOrderTotal() && !isCloseToCTEligibilityByOrderCount()) {
            return false;
        }
        Calendar cal = new GregorianCalendar(Locale.getDefault());
        cal = Calendar.getInstance();
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (lastDay == cal.get(Calendar.DAY_OF_MONTH) || cal.get(Calendar.DAY_OF_MONTH) <= 2) {
            return false;
        }
        return true;
    }

    @Override
    public String getEndChefsTableQualifyingDate() throws FDResourceException {
        Calendar cal = new GregorianCalendar(Locale.getDefault());
        cal = Calendar.getInstance();
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return new SimpleDateFormat("MMMMM d, yyyy").format(cal.getTime());
    }

    @Override
    public ErpPromotionHistory getPromotionHistory() throws FDResourceException {
        if (this.cachedPromoHistory == null) {
            this.cachedPromoHistory = FDCustomerManager.getPromoHistoryInfo(this.identity);
        }
        return this.cachedPromoHistory;
    }

    /**
     * @return number of valid orders, corrected in modify order mode
     */
    @Override
    public int getAdjustedValidOrderCount() throws FDResourceException {
        int orderCount = this.getOrderHistory().getValidOrderCount();
        if (this.getShoppingCart() instanceof FDModifyCartModel) {
            // we're in modify order mode, subtract one
            orderCount--;
        }
        return orderCount;
    }

    /**
     * @return number of valid orders, corrected in modify order mode
     */
    @Override
    public int getAdjustedValidOrderCount(EnumEStoreId storeId) throws FDResourceException {
        int orderCount = this.getOrderHistory().getValidOrderCount(storeId);
        if (this.getShoppingCart() instanceof FDModifyCartModel) {
            // we're in modify order mode, subtract one
            orderCount--;
        }
        return orderCount;
    }

    /**
     * @return number of valid orders by delivery type
     */
    @Override
    public int getAdjustedValidOrderCount(EnumDeliveryType deliveryType) throws FDResourceException {
        int orderCount = this.getOrderHistory().getValidOrderCount(deliveryType);
        if (this.getShoppingCart() instanceof FDModifyCartModel) {
            // we're in modify order mode, subtract one
            orderCount--;
        }
        return orderCount;
    }

    /**
     * @return number of valid orders by delivery type
     */
    @Override
    public int getValidOrderCount(EnumDeliveryType deliveryType) throws FDResourceException {
        return this.getOrderHistory().getValidOrderCount(deliveryType);
    }

    /**
     * @return number of valid ECheck orders, corrected in modify order mode
     */
    @Override
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
    @Override
    public int getValidPhoneOrderCount() throws FDResourceException {
        return this.getOrderHistory().getValidPhoneOrderCount();
    }

    @Override
    public FDPromotionEligibility getPromotionEligibility() {
        if (this.promotionEligibility == null) {
            this.updateUserState(false);
        }

        return this.promotionEligibility;
    }

    @Override
    public boolean isEligibleForSignupPromotion() {
        return this.getPromotionEligibility().isEligibleForType(EnumPromotionType.SIGNUP);
    }

    @Override
    public PromotionI getEligibleSignupPromotion() {
        Set promoSet = this.getPromotionEligibility().getEligiblePromotionCodes(EnumPromotionType.SIGNUP);
        if (null == promoSet || promoSet.isEmpty()) {
            return null;
        }
        String code = (String) promoSet.iterator().next();
        return PromotionFactory.getInstance().getPromotion(code);
    }

    /**
     * @return true if the order minimum has been met (FDUserI.MINIMUM_ORDER_AMOUNT)
     */
    @Override
    public boolean isOrderMinimumMet() throws FDResourceException {
        return this.isOrderMinimumMet(false, null);
    }

    public boolean isOrderMinimumMet(Double overrideMinimumAmount) throws FDResourceException {
        return this.isOrderMinimumMet(false, overrideMinimumAmount);
    }

    @Override
    public boolean isOrderMinimumMet(boolean alcohol) throws FDResourceException {
        return isOrderMinimumMet(alcohol, null);
    }

    public boolean isOrderMinimumMet(boolean alcohol, Double overrideMinimumAmount) throws FDResourceException {
        double subTotal = alcohol ? this.shoppingCart.getSubTotalWithoutAlcohol() : this.shoppingCart.getSubTotal();
        return subTotal >= (overrideMinimumAmount == null ? getMinimumOrderAmount() : overrideMinimumAmount);
    }

    @Override
    public boolean isOrderMinimumMetWithoutWine() throws FDResourceException {
        double subTotal = this.shoppingCart.getSubTotalWithoutWineAndSpirit();
        return subTotal >= getMinimumOrderAmount();
    }

    @Override
    public double getMinimumOrderAmount() {
        if (getShoppingCart() != null && getShoppingCart().getDeliveryAddress() != null) {
            try {
                String county = FDDeliveryManager.getInstance().getCounty(getShoppingCart().getDeliveryAddress());
                String zip = getShoppingCart().getDeliveryAddress().getZipCode();
                String zipcodes = FDStoreProperties.getSuffolkZips();
                if ("SUFFOLK".equalsIgnoreCase(county) && (zipcodes.indexOf(zip) == -1)) {
                    return ErpServicesProperties.getSufFolkCountyMinimumOrderAmount();
                }
            } catch (FDResourceException e) {
                throw new FDRuntimeException(e);
            }
        }
        return EnumServiceType.CORPORATE.equals(this.getSelectedServiceType()) ? ErpServicesProperties.getMinCorpOrderAmount() : ((EnumEStoreId.FDX.equals(this.getUserContext()
                .getStoreContext().getEStoreId())) ? ErpServicesProperties.getFDXMinimumOrderAmount() : ErpServicesProperties.getMinimumOrderAmount());
    }

    @Override
    public float getQuantityMaximum(ProductModel product) {
        float pMax = 200;

        if (this.getMasqueradeContext() != null) {
            return pMax;
        } else {
            return product.enforceQuantityMax() || (!this.isCorporateUser()) ? product.getQuantityMaximum() : pMax;
        }

    }

    @Override
    public boolean isPickupUser() {
        return this.availableServices.contains(EnumServiceType.PICKUP) || this.availableServices.contains(EnumServiceType.HOME)
                || this.availableServices.contains(EnumServiceType.CORPORATE);
    }

    @Override
    public boolean isPickupOnly() {
        return !this.availableServices.contains(EnumServiceType.DEPOT) && !this.availableServices.contains(EnumServiceType.CORPORATE)
                && !this.availableServices.contains(EnumServiceType.HOME) && this.availableServices.contains(EnumServiceType.PICKUP);
    }

    @Override
    public boolean isNotServiceable() {
        return this.availableServices.isEmpty();
    }

    @Override
    public boolean isHomeUser() {
        return this.availableServices.contains(EnumServiceType.HOME);
    }

    @Override
    public FDReservation getReservation() {
        Date now = new Date();
        if (this.reservation != null) {
            if (reservation.getExpirationDateTime().before(now) || reservation.getExpirationDateTime().equals(now)) {
                return null;
            }
        }

        return this.reservation;
    }

    @Override
    public void setReservation(FDReservation reservation) {
        this.reservation = reservation;
    }

    @Override
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

    @Override
    public boolean isVoucherHolder() throws FDResourceException {
        return isVHInDelivery() || isVHOutOfDelivery();
    }

    @Override
    public boolean isVHInDelivery() throws FDResourceException {
        if (this.identity == null) {
            return false;
        }
        FDCustomerModel customer = this.getFDCustomer();
        if (customer == null || customer.getProfile() == null) {
            return false;
        } else {
            return customer.getProfile().isVHInDelivery();
        }
    }

    @Override
    public boolean isVHOutOfDelivery() throws FDResourceException {
        if (this.identity == null) {
            return false;
        }
        FDCustomerModel customer = this.getFDCustomer();
        if (customer == null || customer.getProfile() == null) {
            return false;
        } else {
            return customer.getProfile().isVHOutOfDelivery();
        }
    }

    @Override
    public String getChefsTableInduction() throws FDResourceException {

        FDCustomerModel customer = this.getFDCustomer();
        if (customer == null || customer.getProfile() == null) {
            return "0";
        } else {
            return customer.getProfile().getChefsTableInduction();
        }
    }

    @Override
    public String getWinback() throws FDResourceException {
        if (this.identity == null) {
            return "false";
        }
        FDCustomerModel customer = this.getFDCustomer();
        if (null == customer || null == customer.getProfile() || null == customer.getProfile().getWinback()) {
            return "false";
        } else {
            String value = customer.getProfile().getWinback().trim();
            if ("".equals(value)) {
                return "false";
            }
            return value;
        }
    }

    @Override
    public String getWinbackPath() throws FDResourceException {
        // winback path is in the form of "YYMMDD_winback_segment"
        String winback = getWinback();
        if (winback.equals("false"))
            return winback;

        StringTokenizer st = new StringTokenizer(winback, "_");
        int countTokens = st.countTokens();
        if (countTokens < 3)
            return "false";
        st.nextToken(); // date token which we don't need
        return FDStoreProperties.getWinbackRoot() + st.nextToken() + "/" + st.nextToken() + ".html";
    }

    @Override
    public String getMarketingPromoPath() throws FDResourceException {
        // marketingPromo path is in the form of "campaign_campaign2_segment"
        // a valid marketing promo value is in the form of "mktg_deli_default"
        String mktgPromo = getMarketingPromo();
        if (mktgPromo.equals("false"))
            return mktgPromo;

        StringTokenizer st = new StringTokenizer(mktgPromo, "_");
        int countTokens = st.countTokens();
        if (countTokens < 3)
            return "false";
        return FDStoreProperties.getMarketingPromoRoot() + st.nextToken() + "/" + st.nextToken() + "/" + st.nextToken() + ".html";
    }

    @Override
    public String getMarketingPromo() throws FDResourceException {

        if (this.identity == null) {
            return "false";
        }
        FDCustomerModel customer = this.getFDCustomer();
        if (null == customer || null == customer.getProfile() || null == customer.getProfile().getMarketingPromo()) {
            return "false";
        } else {
            String value = customer.getProfile().getMarketingPromo().trim();
            if ("".equals(value)) {
                return "false";
            }
            return value;
        }
    }

    @Override
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

    @Override
    public EnumServiceType getSelectedServiceType() {
        // FDX-2029 API - COS Delivery fee and order minimum used instead of FK
        if (userContext != null && userContext.getStoreContext() != null && EnumEStoreId.FDX.equals(userContext.getStoreContext().getEStoreId())) {
            return EnumServiceType.HOME;
        } else {
            return selectedServiceType == null ? EnumServiceType.HOME : this.selectedServiceType;
        }
    }

    @Override
    public EnumServiceType getUserServiceType() {
        return this.userServiceType != null ? this.userServiceType : (null != getSelectedServiceType() ? getSelectedServiceType() : EnumServiceType.HOME);// EnumServiceType.HOME ;
    }

    public void setUserServiceType(EnumServiceType serviceType) {
        this.userServiceType = serviceType;
    }

    @Override
    public EnumServiceType getZPServiceType() {
        return this.zpServiceType != null ? this.zpServiceType : EnumServiceType.HOME;
    }

    @Override
    public void setZPServiceType(EnumServiceType serviceType) {
        this.zpServiceType = serviceType;
    }

    @Override
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

    @Override
    public String getCustomerServiceContact() {
        try {
        	// ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId();
        	boolean isChefsTable = this.isChefsTable();
        	return getCustomerServiceContact(isChefsTable, isChefsTable? null : extractStateFromAddress(), 
        			 ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId());
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e);
        }
    }
    /**
     * 
     * @param isChefsTable boolean, obvious
     * @param state String state, two digit state, we are looking for PA use case
     * @param eStoreId  EnumEStoreId, logic is either FK or FDX
     * @return the proper contact phone number from FDStoreProperties.java
     */
    public String getCustomerServiceContact(boolean isChefsTable, String state, EnumEStoreId  eStoreId ) {
    	if(EnumEStoreId.FD.equals(eStoreId))
    		return getCustomerServiceContact(isChefsTable,state);
    	else {
    		return  FDStoreProperties.FOODKICK_SERVICE_CONTACT;
    	}
 
  }
    


    public String getCustomerServiceContact(boolean isChefsTable, String state) {
        
//        String state = "";
        String contactNumber =   FDStoreProperties.getDefaultCustomerServiceContact() ; //1-866-283-7374";// DEFAULT
        if (isChefsTable) {
            contactNumber = FDStoreProperties.getChefsTableCustomerServiceContact();// "1-866-511-1240";
        } else {
//            state = extractStateFromAddress();
            if ("PA".equalsIgnoreCase(state)) {
                contactNumber = FDStoreProperties.getPennsylvaniaCustomerServiceContact() ;// "1-215-825-5726";
            }
        }
        return contactNumber;

        
    }
    private String extractStateFromAddress() {
        String state = "";
        if (this.getShoppingCart() != null && this.getShoppingCart().getDeliveryAddress() != null) {
            state = this.getShoppingCart().getDeliveryAddress().getState();
        } else if (this.getState() != null) {
            state = this.getState();
        }
        return state;
    }

    @Override
    public String getCustomerServiceContactMediaPath() {

        try {
            String state = "";
            String contactMedia = "/media/editorial/site_pages/contact_serivce_number.html";// DEFAULT
            if (this.isChefsTable()) {
                contactMedia = "/media/editorial/site_pages/chef_contact_serivce_number.html";
            } else {
                state = extractStateFromAddress();
                if ("PA".equalsIgnoreCase(state)) {
                    contactMedia = "/media/editorial/site_pages/contact_serivce_number_PA.html";
                }
            }
            return contactMedia;

        } catch (FDResourceException e) {
            throw new FDRuntimeException(e);
        }

    }

    /**
     * Returns the appropriate customer service email address
     *
     * @return serviceEmail email address
     */
    @Override
    public String getCustomerServiceEmail() throws FDResourceException {
        String serviceEmail = SERVICE_EMAIL;
        if (isChefsTable()) {
            serviceEmail = FDStoreProperties.getChefsTableEmail();
        } else if (isDepotUser()) {
            try {
				serviceEmail = FDDeliveryManager.getInstance().getCustomerServiceEmail(getDepotCode());
			} catch (Exception e) {
				//Ignore
				LOGGER.warn("Exception while fetching customer service email by depot code: "+getDepotCode(),e);
			}
        } else if (isCorporateUser()) {
            serviceEmail = "corporateservice@freshdirect.com";
        }
        /*if (isChefsTable()) {
            serviceEmail = FDStoreProperties.getChefsTableEmail();
        }*/
        return serviceEmail;
    }

    @Override
    public boolean isCheckEligible() {
        // return true;
        if (checkEligible == null) {
            EligibilityCalculator calc = new EligibilityCalculator("ECHECK");
            checkEligible = Boolean.valueOf(calc.isEligible(new FDRulesContextImpl(this)));
        }
        return checkEligible.booleanValue();
    }

    @Override
    public Collection<ErpPaymentMethodI> getPaymentMethods() {
        try {
            return FDCustomerManager.getPaymentMethods(this.identity);
        } catch (FDResourceException e) {
            return new ArrayList<ErpPaymentMethodI>(); // empty list
        }
    }

    @Override
    public String getUserId() {
        try {
            // load user id 'user@host.com' lazily
            if (this.identity != null && (userId == null || "".equals(userId))) {
                ErpCustomerModel model = FDCustomerFactory.getErpCustomer(this.identity);
                userId = (model != null) ? model.getUserId() : "";
            }
        } catch (FDResourceException e) {
            userId = ""; // empty string
        }
        return userId;
    }
    
    public void setUserId(String userId){
    	this.userId =userId;
    }

    @Override
    public String getLastRefTrackingCode() {
        return this.lastRefTrackingCode;
    }

    @Override
    public void setLastRefTrackingCode(String lastRefTrackingCode) {
        this.lastRefTrackingCode = lastRefTrackingCode;
    }

    @Override
    public boolean isReferrerRestricted() throws FDResourceException {
        if (this.identity == null) {
            return false;
        }
        return FDCustomerManager.isReferrerRestricted(this.identity);
    }

    @Override
    public boolean isReferrerEligible() throws FDResourceException {
        if (referrerEligible == null) {
            EligibilityCalculator calc = new EligibilityCalculator("REFERRER");
            referrerEligible = Boolean.valueOf(calc.isEligible(new FDRulesContextImpl(this)));
        }
        return referrerEligible.booleanValue();
        
    }

    @Override
    public boolean isECheckRestricted() throws FDResourceException {
        if (this.identity == null) {
            return false;
        }
        if(isEcheckRestricted == null){
        	isEcheckRestricted = Boolean.FALSE;
        	if(FDCustomerManager.isECheckRestricted(this.identity)){
        		isEcheckRestricted = Boolean.TRUE;
        	}
        }
        return isEcheckRestricted.booleanValue();
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

    @Override
    public String getDefaultCounty() throws FDResourceException {
        String county = null;

        // if user is pickup user default county = 'PICKUP'
        if (EnumServiceType.PICKUP.equals(this.getSelectedServiceType()) || EnumServiceType.DEPOT.equals(this.getSelectedServiceType())) {
            county = EnumServiceType.PICKUP.getName();
        }

        // check address on cart, recognize user handles all the complex logic with defaultAddresses
        if (county == null && this.shoppingCart != null) {
            county = FDDeliveryManager.getInstance().getCounty(this.getShoppingCart().getDeliveryAddress());
        }

        // if we got nothing so far return county of zipcode on zipcheck
        if (this.getZipCode() != null && (county == null || "".equals(county))) {
            county = FDDeliveryManager.getInstance().lookupCountyByZip(this.getZipCode());
        }

        return county;
    }

    @Override
    public String getDefaultState() throws FDResourceException {
        String state = null;

        // check address on cart, recognize user handles all the complex logic with defaultAddresses
        if (this.shoppingCart != null && this.shoppingCart.getDeliveryAddress() != null) {
            state = NVL.apply(this.shoppingCart.getDeliveryAddress().getState(), "");
        }

        // if we got nothing so far return state of zipcode on zipcheck
        if (this.getZipCode() != null && (state == null || "".equals(state))) {
            state = FDDeliveryManager.getInstance().lookupStateByZip(this.getZipCode());
        }

        return state;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean isReceiveFDEmails() {
        return this.receiveFDemails;
    }

    public void setReceiveFDEmails(boolean receiveFDEmails) {
        this.receiveFDemails = receiveFDEmails;
    }

    /**
     * @return Returns the deliveryPassStatus.
     */
    @Override
    public EnumDlvPassStatus getDeliveryPassStatus() {
        if (dlvPassInfo != null) {
            return dlvPassInfo.getStatus();
        }
        // Return Default value;
        return EnumDlvPassStatus.NONE;

    }

    @Override
    public boolean isDlvPassNone() {
        return (dlvPassInfo == null) || (EnumDlvPassStatus.NONE.equals(dlvPassInfo.getStatus()));
    }

    @Override
    public boolean isDlvPassActive() {
        if (EnumEStoreId.FDX.equals(this.getUserContext().getStoreContext().getEStoreId())) {
            return false;
        }
        if (dlvPassInfo == null) {
            return false;
        }
        if (!dlvPassInfo.isUnlimited()) {
            // BSGS Pass
            /*
             * 2nd Condition happens only for BSGS pass. Let say user places order A using a last delivery of a BSGS pass. The pass goes to expired pending. The next day the user
             * modifies the order still the BSGS pass should be applied even if the status is expired pending. Thats why this.getShoppingCart().isDlvPassAlreadyApplied() check is
             * made.
             */
            return (EnumDlvPassStatus.ACTIVE.equals(dlvPassInfo.getStatus()) || (this.isDlvPassExpiredPending() && this.getShoppingCart().isDlvPassAlreadyApplied()));
        }
        // Unlimited Pass.
        if (EnumDlvPassStatus.ACTIVE.equals(dlvPassInfo.getStatus())) {
            Date today = new Date();
            return today.before(dlvPassInfo.getExpDate());
        } else {
            return false;
        }

    }

    @Override
    public boolean isDlvPassExpired() {
        if (dlvPassInfo == null) {
            return false;
        }
        if (!dlvPassInfo.isUnlimited()) {
            // BSGS Pass
            return EnumDlvPassStatus.EXPIRED.equals(dlvPassInfo.getStatus());
        }
        // Unlimited Pass.
        if (EnumDlvPassStatus.EXPIRED.equals(dlvPassInfo.getStatus())) {
            return true;
        } else {
            // Safe Check - Go by expiration date.
            if (null == dlvPassInfo.getExpDate()) {
                return false;
            }
            Date today = new Date();
            return today.after(dlvPassInfo.getExpDate());
        }
    }

    @Override
    public boolean isDlvPassPending() {
        if (dlvPassInfo == null) {
            return false;
        }
        return EnumDlvPassStatus.PENDING.equals(dlvPassInfo.getStatus());

    }

    @Override
    public boolean isDlvPassExpiredPending() {
        if (dlvPassInfo == null) {
            return false;
        }
        return EnumDlvPassStatus.EXPIRED_PENDING.equals(dlvPassInfo.getStatus());

    }

    @Override
    public boolean isDlvPassCancelled() {
        if (dlvPassInfo == null) {
            return false;
        }
        return (EnumDlvPassStatus.CANCELLED.equals(dlvPassInfo.getStatus()) || EnumDlvPassStatus.ORDER_CANCELLED.equals(dlvPassInfo.getStatus()));
    }

    @Override
    public boolean isDlvPassReturned() {
        if (dlvPassInfo == null) {
            return false;
        }
        return EnumDlvPassStatus.PASS_RETURNED.equals(dlvPassInfo.getStatus());
    }

    @Override
    public boolean isDlvPassShortShipped() {
        if (dlvPassInfo == null) {
            return false;
        }
        return EnumDlvPassStatus.SHORT_SHIPPED.equals(dlvPassInfo.getStatus());
    }

    @Override
    public boolean isDlvPassSettlementFailed() {
        if (dlvPassInfo == null) {
            return false;
        }
        return EnumDlvPassStatus.SETTLEMENT_FAILED.equals(dlvPassInfo.getStatus());
    }

    @Override
    public void performDlvPassStatusCheck() throws FDResourceException {
        if (this.isDlvPassActive()) {
            if (!(this.getShoppingCart().isChargeWaived(EnumChargeType.DELIVERY))) {
                // If delivery promotion was applied, do not reapply the waiving of dlv charge.
                this.getShoppingCart().setChargeWaived(EnumChargeType.DELIVERY, true, DlvPassConstants.PROMO_CODE, this.isWaiveDPFuelSurCharge(false));
                this.getShoppingCart().setDlvPassApplied(true);
            }
        } else if ((this.getShoppingCart() instanceof FDModifyCartModel) && (this.getDlvPassInfo().isUnlimited())) {

            String dpId = ((FDModifyCartModel) this.getShoppingCart()).getOriginalOrder().getDeliveryPassId();
            if (dpId != null && !dpId.equals("")) {
                List passes = FDCustomerManager.getDeliveryPassesByStatus(this.getIdentity(), EnumDlvPassStatus.ACTIVE);
                DeliveryPassModel dlvPass = null;
                Date today = new Date();
                for (int i = 0; i < passes.size(); i++) {
                    dlvPass = (DeliveryPassModel) passes.get(i);

                    if (today.after(dlvPass.getExpirationDate()) && EnumDlvPassStatus.ACTIVE.equals(dlvPass.getStatus()) && dlvPass.getId().equals(dpId)) {
                        this.getShoppingCart().setChargeWaived(EnumChargeType.DELIVERY, true, DlvPassConstants.PROMO_CODE, this.isWaiveDPFuelSurCharge(false));
                        this.getShoppingCart().setDlvPassApplied(true);
                        this.getShoppingCart().setDlvPassPremiumAllowedTC(dlvPass.getPurchaseDate().after(FDStoreProperties.getDlvPassNewTCDate()));
                        break;

                    }
                }
            }
        }
    }

    @Override
    public boolean isEligibleForDeliveryPass() throws FDResourceException {
        EnumDlvPassProfileType profileType = getEligibleDeliveryPass();
        if (profileType.equals(EnumDlvPassProfileType.NOT_ELIGIBLE))
            return false;
        return true;

    }

    @Override
    public EnumDlvPassProfileType getEligibleDeliveryPass() throws FDResourceException {
        if (this.identity != null) {
            FDCustomerModel customer = this.getFDCustomer();
            String customerPK = customer.getErpCustomerPK();

            if (customer != null && customer.getProfile() != null) {
                ProfileModel p = customer.getProfile();
                String profileValue = p.getDeliveryPass();
                if ((profileValue == null) || (profileValue != null && profileValue.trim().equals(""))) {
                    // return EnumDlvPassProfileType.NOT_ELIGIBLE;
                    return EnumDlvPassProfileType.UNLIMITED;
                }
                if (isEligibleForBSGS(profileValue, customerPK)) {
                    return EnumDlvPassProfileType.BSGS;
                }

                if (profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedAmazonPrimeProfile()) != -1) {
                    if (isEligibleForAmazonPrime(profileValue.trim(), customerPK))
                        return EnumDlvPassProfileType.AMAZON_PRIME;
                    else
                        return EnumDlvPassProfileType.UNLIMITED;
                }
                if (profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedPromotionalProfile()) != -1) {

                    if (isEligibleForPromotionalProfile(profileValue.trim(), customerPK))
                        return EnumDlvPassProfileType.PROMOTIONAL_UNLIMITED;
                    else
                        return EnumDlvPassProfileType.UNLIMITED;
                }
                if (profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedProfilePosfix()) != -1)
                    return EnumDlvPassProfileType.UNLIMITED;
            }
        }
        // return EnumDlvPassProfileType.NOT_ELIGIBLE;
        return EnumDlvPassProfileType.UNLIMITED;
    }

    private boolean isEligibleForBSGS(String profileValue, String customerID) throws FDResourceException {

        boolean isEligible = false;
        if (profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getBSGSProfilePosfix()) != -1) {
            isEligible = true;
        }
        return isEligible;
    }

    private boolean isEligibleForAmazonPrime(String profileValue, String customerID) throws FDResourceException {

        boolean isEligible = false;
        if (profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedAmazonPrimeProfile()) != -1) {
            isEligible = !FDCustomerManager.hasPurchasedPass(customerID);// ,getDeliveryPassType(FDStoreProperties.getUnlimitedAmazonPrimeProfile()).getCode()
        }
        return isEligible;
    }

    private boolean isEligibleForPromotionalProfile(String profileValue, String customerID) throws FDResourceException {

        boolean isEligible = false;
        if (profileValue != null && profileValue.trim().indexOf(FDStoreProperties.getUnlimitedPromotionalProfile()) != -1) {
            isEligible = !FDCustomerManager.hasPurchasedPass(customerID);
        }
        return isEligible;
    }

    @Override
    public String getDlvPassProfileValue() throws FDResourceException {
        if (this.identity != null) {
            FDCustomerModel customer = this.getFDCustomer();
            if (customer != null && customer.getProfile() != null) {
                ProfileModel p = customer.getProfile();
                if (p.getDeliveryPass() != null)
                    return p.getDeliveryPass();
            }
        }
        return "";
    }

    @Override
    public void updateDlvPassInfo() throws FDResourceException {
        try {
            FDUserDlvPassInfo dpInfo = FDCustomerManager.getDeliveryPassInfo(this);
            this.setDlvPassInfo(dpInfo);
        } catch (FDResourceException fe) {
            throw fe;
        }
    }

    @Override
    public void setLastRefProgramId(String progId) {
        this.lastRefProgId = progId;
    }

    @Override
    public String getLastRefProgId() {
        return this.lastRefProgId;
    }

    @Override
    public void setLastRefTrkDtls(String trkDtls) {
        this.lastRefTrkDtls = trkDtls;
    }

    @Override
    public String getLastRefTrkDtls() {
        return this.lastRefTrkDtls;
    }

    @Override
    public void setLastRefProgInvtId(String progInvtId) {
        this.lastRefProgInvtId = progInvtId;
    }

    @Override
    public ErpCustomerInfoModel getCustomerInfoModel() throws FDResourceException {
        if (identity == null) {
            return null;
        } else {
            if (customerInfoModel == null) {
                customerInfoModel = FDCustomerFactory.getErpCustomerInfo(identity);
            }
            return customerInfoModel;
        }
    }

    public void resetCustomerInfoModel() throws FDResourceException {
        customerInfoModel = FDCustomerFactory.getErpCustomerInfo(identity);
    }

    @Override
    public String getLastRefProgInvtId() {
        return this.lastRefProgInvtId;
    }

    @Override
    public double getBaseDeliveryFee() {
        return ErpServicesProperties.getBaseDeliveryFee();
    }

    @Override
    public double getMinCorpOrderAmount() {
        return ErpServicesProperties.getMinCorpOrderAmount();
    }

    @Override
    public double getCorpDeliveryFee() {
        return ErpServicesProperties.getCorpDeliveryFee();

    }

    @Override
    public double getCorpDeliveryFeeMonday() {
        return ErpServicesProperties.getCorpDeliveryFeeMonday();
    }

    @Override
    public int getUsableDeliveryPassCount() {
        if (dlvPassInfo != null)
            return dlvPassInfo.getUsablePassCount();
        else
            return 0;
    }

    @Override
    public boolean isProduceRatingEnabled() {
        return true;
    }

    @Override
    public boolean isGiftCardsEnabled() {
        return true;
    }

    @Override
    public boolean isCCLEnabled() {
        return true;
    }

    @Override
    public boolean isCCLInExperienced() {
        return CclUtils.isCCLInExperienced(this, getCustomerCreatedListInfos());
    }

    // -- DYF --- //

    @Override
    public boolean isDYFEnabled() {
        return true;
    }

    // Zone Pricing
    public boolean isZonePricingEnabled() {
        return true;
    }

    @Override
    /*public EnumDPAutoRenewalType hasAutoRenewDP() throws FDResourceException {
        if (this.identity != null) {
        	if(null == hasAutoRenewDP){
	            FDCustomerModel customer = this.getFDCustomer();
	            String customerPK = customer.getErpCustomerPK();
	
	            hasAutoRenewDP = FDCustomerManager.hasAutoRenewDP(customerPK);
	            return hasAutoRenewDP;
        	}
        	return hasAutoRenewDP;
        }
        return EnumDPAutoRenewalType.NONE;
    }*/
    
    public EnumDPAutoRenewalType hasAutoRenewDP() throws FDResourceException {
        if (this.identity != null) {
            String customerPK = identity.getErpCustomerPK();

            return FDCustomerManager.hasAutoRenewDP(customerPK);
        }
        return EnumDPAutoRenewalType.NONE;
    }

    @Override
    public AssignedCustomerParam getAssignedCustomerParam(String promoId) {
        if (assignedCustomerParams != null) {
            return this.assignedCustomerParams.get(promoId);
        }
        return null;
    }

    @Override
    public List<FDCustomerListInfo> getCustomerCreatedListInfos() {
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

    @Override
    public List<FDCustomerListInfo> getStandingOrderListInfos() {
        if (getLevel() == FDUserI.GUEST) {
            // We don't have an identity
            return null;
        }
        if (soListInfos == null) {
            try {
                soListInfos = FDListManager.getStandingOrderListInfos(this);
            } catch (Exception e) {
                throw new FDRuntimeException(e);
            }
        }
        return soListInfos;
    }

    public void setAssignedCustomerParams(Map<String, AssignedCustomerParam> assignedCustomerParams) {
        this.assignedCustomerParams = assignedCustomerParams;
    }

    @Override
    public DCPDPromoProductCache getDCPDPromoProductCache() {
        if (this.dcpdPromoProductCache == null) {
            this.dcpdPromoProductCache = new DCPDPromoProductCache();
        }
        return dcpdPromoProductCache;
    }

    @Override
    public boolean isHomePageLetterVisited() {
        return isHomePageLetterVisited;
    }

    @Override
    public void setHomePageLetterVisited(boolean isHomePageLetterVisited) {
        this.isHomePageLetterVisited = isHomePageLetterVisited;
    }

    @Override
    public boolean isCampaignMsgLimitViewed() {
        if (getCampaignMsgViewed() >= FDStoreProperties.getImpressionLimit())
            return true;
        return false;
    }

    @Override
    public int getCampaignMsgViewed() {
        return campaignMsgViewed;
    }

    @Override
    public void setCampaignMsgViewed(int campaignMsgViewed) {
        this.campaignMsgViewed = campaignMsgViewed;
    }

    /**
     * Returns user's cohort ID
     *
     * @param user
     * @return
     */
    @Override
    public String getCohortName() {
        return this.cohortName;
    }

    @Override
    public void setCohortName(String cohortName) {
        this.cohortName = cohortName;
    }

    public void createCohortName() throws FDResourceException {
        this.cohortName = CohortSelector.getInstance().getCohortName(getPrimaryKey());
        FDCustomerManager.storeCohortName(this);
    }

    @Override
    public int getTotalCartSkuQuantity(String args[]) {
        Collection<String> c = Arrays.asList(args);
        Set<String> argSet = new HashSet<String>(c);
        if (args == null) {
            // System.out.println("** args :"+args);
            return 0;
        }

        if (this.shoppingCart == null || this.shoppingCart.getOrderLines() == null)
            return 0;
        int count = 0;
        for (Iterator j = this.shoppingCart.getOrderLines().iterator(); j.hasNext();) {
            FDCartLineI line = (FDCartLineI) j.next();
            for (Iterator<String> i = argSet.iterator(); i.hasNext();) {
                String sku = i.next();
                if (sku.equals(line.getSkuCode())) {
                    count += line.getQuantity();
                }
            }
        }

        return count;

    }

    @Override
    public Map getPromoVariantMap() {
        if (this.promoVariantMap == null) {
            this.updateUserState(false);
        }
        return this.promoVariantMap;
    }

    @Override
    public void setPromoVariantMap(Map pvMap) {
        this.promoVariantMap = pvMap;
    }

    @Override
    public PromoVariantModel getPromoVariant(String variantId) {
        return (PromoVariantModel) this.getPromoVariantMap().get(variantId);
    }

    @Override
    public String getSavingsVariantId() {
        return savingsVariantId;
    }

    @Override
    public void setSavingsVariantId(String savingsVariantId) {
        this.savingsVariantId = savingsVariantId;
    }

    @Override
    public boolean isSavingsVariantFound() {
        return savingsVariantFound;
    }

    @Override
    public void setSavingsVariantFound(boolean savingsVariantFound) {
        this.savingsVariantFound = savingsVariantFound;
    }

    /**
     * @return Always returns null
     * @see com.freshdirect.fdstore.customer.FDUserI#getFavoriteTabFeature()
     */
    @Override
    public String getFavoriteTabFeature() {
        return null;
    }

    /**
     * Calling this function has no effect (only FDSessionUser implements it)
     *
     * @param feature
     *            ignored
     * @see com.freshdirect.fdstore.customer.FDUserI#setFavoriteTabFeature(java.lang.String)
     */
    @Override
    public void setFavoriteTabFeature(String feature) {
        // has no effect
    }

    @Override
    public boolean isPostPromoConflictEnabled() {
        return isPostPromoConflictEnabled;
    }

    @Override
    public void setPostPromoConflictEnabled(boolean isPostPromoConflictEnabled) {
        this.isPostPromoConflictEnabled = isPostPromoConflictEnabled;
    }

    @Override
    public boolean isPromoConflictResolutionApplied() {
        return isPromoConflictResolutionApplied;
    }

    @Override
    public void setPromoConflictResolutionApplied(boolean isPromoConflictResolutionApplied) {
        this.isPromoConflictResolutionApplied = isPromoConflictResolutionApplied;
    }

    @Override
    public FDGiftCardInfoList getGiftCardList() {
        if (getLevel() != FDUserI.SIGNED_IN) { // [APPDEV-4924]-Load giftcards only for logged-in users.
            // We don't have an identity
            return null;
        }
        if (cachedGiftCards == null) {
            try {
                cachedGiftCards = FDCustomerManager.getGiftCards(identity);
                // getGCRecipientInfo();

            } catch (Exception e) {
                throw new FDRuntimeException(e);
            }
        }
        return cachedGiftCards;
    }

    private void getGCRecipientInfo() throws FDResourceException {
        if (null != cachedGiftCards && null != cachedGiftCards.getGiftcards() && !cachedGiftCards.getGiftcards().isEmpty()) {
            List<String> saleIds = new ArrayList<String>();
            for (Iterator iterator = cachedGiftCards.getGiftcards().iterator(); iterator.hasNext();) {
                FDGiftCardModel lFDGiftCardModel = (FDGiftCardModel) iterator.next();
                if (!saleIds.contains(lFDGiftCardModel.getGiftCardModel().getPurchaseSaleId())) {
                    saleIds.add(lFDGiftCardModel.getGiftCardModel().getPurchaseSaleId());
                }

            }
            if (!saleIds.isEmpty()) {
                // Get the recipient info for all these distinct saleIds.
                cachedRecipientInfo = FDCustomerManager.getGiftCardRecepientsForOrders(saleIds);
            }
        }
    }

    @Override
    public String getGCSenderName(String certNum, String saleId) {
        if (getLevel() == FDUserI.GUEST) {
            // We don't have an identity
            return null;
        }
        ErpGCDlvInformationHolder holder = null;
        try {
            if (null == cachedRecipientInfo) {
                cachedRecipientInfo = new ConcurrentHashMap<String, ErpGCDlvInformationHolder>();
            }

            if (null != cachedRecipientInfo) {
                if (cachedRecipientInfo.containsKey(certNum)) {
                    holder = cachedRecipientInfo.get(certNum);
                } else {
                    holder = FDCustomerManager.GetGiftCardRecipentByCertNum(certNum);
                    cachedRecipientInfo.put(certNum, holder);
                }
                if (null != holder) {
                    return holder.getRecepientModel().getSenderName();
                }
            }
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e);
        }
        return null;
    }

    @Override
    public void invalidateGiftCards() {
        this.cachedGiftCards = null;
    }

    @Override
    public double getGiftcardBalance() {
        if (this.getGiftCardList() == null)
            return 0.0;
        if (this.getShoppingCart() instanceof FDModifyCartModel) {
            return this.getGiftCardList().getTotalBalance();
        } else {
            // Clear all hold amounts.
            if (null != this.getGiftCardList()) {
                this.getGiftCardList().clearAllHoldAmount();
                return this.getGiftCardList().getTotalBalance();
            }
            return 0;
        }

    }

    @Override
    public FDCartModel getGiftCart() {
        return this.dummyCart;
    }

    @Override
    public void setGiftCart(FDCartModel dcart) {
        this.dummyCart = dcart;
    }

    @Override
    public FDRecipientList getRecipientList() {
        if (null == this.recipientList) {
            this.recipientList = new FDRecipientList();
        }
        return this.recipientList;
    }

    @Override
    public void setRecipientList(FDRecipientList r) {
        this.recipientList = r;
    }

    @Override
    public boolean isGCOrderMinimumMet() {
        double subTotal = this.getRecipientList().getSubtotal(null);
        return subTotal >= this.getMinimumOrderAmount();
    }

    @Override
    public double getGCMinimumOrderAmount() {
        return MIN_GC_ORDER_AMOUNT;
    }

    @Override
    public FDBulkRecipientList getBulkRecipentList() {
        if (null == this.bulkRecipientList) {
            this.bulkRecipientList = new FDBulkRecipientList();
        }
        return this.bulkRecipientList;
    }

    @Override
    public void setBulkRecipientList(FDBulkRecipientList r) {
        this.bulkRecipientList = r;
    }

    @Override
    public Integer getDonationTotalQuantity() {
        return donationTotalQuantity;
    }

    @Override
    public void setDonationTotalQuantity(Integer donationTotalQuantity) {
        this.donationTotalQuantity = donationTotalQuantity;
    }

    @Override
    public FDCartModel getDonationCart() {
        return donationCart;
    }

    @Override
    public void setDonationCart(FDCartModel dcart) {
        this.donationCart = dcart;
    }

    @Override
    public double getGiftcardsTotalBalance() {
        if (this.getGiftCardList() == null)
            return 0.0;
        if (this.getShoppingCart() instanceof FDModifyCartModel) {
            return this.getGiftCardList().getGiftcardsTotalBalance();
        } else {
            // Clear all hold amounts.
            if (null != this.getGiftCardList()) {
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
        if (servicesBasedOnAddress == null) {
            ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(getIdentity().getErpCustomerPK());
            List<ErpAddressModel> shipToAddresses = erpCustomer.getShipToAddresses();
            servicesBasedOnAddress = new HashSet<EnumServiceType>();
            for (ErpAddressModel m : shipToAddresses) {
                servicesBasedOnAddress.add(m.getServiceType());
            }
        }
        return servicesBasedOnAddress;
    }

    // Profile key
    private static String PROFILE_SO_KEY = "so.enabled";
    private static String PROFILE_SO3_KEY = "so3.enabled";

    /**
     * Ensures StandingOrder feature is enabled for the customer
     * 
     * 1. Check personal availability in profile attributes 2. Check global availability in fdstore.properties
     * 
     */
    protected boolean isSOEnabled() {
        // Check personal flag in user profile
        boolean isEnabledInProfile = false;
        try {
            // TODO need to change this once get identity issue is resolved
            if (getLevel() >= 2) {
                isEnabledInProfile = Boolean.valueOf(getFDCustomer().getProfile().getAttribute(PROFILE_SO_KEY)).booleanValue();
            }
            if (isEnabledInProfile) {
                LOGGER.debug("SO enabled in customer profile");
                return true;
            }
        } catch (FDResourceException e) {
        }

        // Check global flag
        final boolean standingOrdersEnabled = FDStoreProperties.isStandingOrdersEnabled();
        LOGGER.debug("Standing Orders " + (standingOrdersEnabled ? "" : "NOT") + " enabled globally");
        return standingOrdersEnabled;
    }

    /**
     * Ensures customer is eligible for StandingOrder feature.
     * 
     * 1. Check if so is enabled (globally or for the user) 2. Check if customer satisfies every requirement.
     * 
     */
    @Override
    public boolean isEligibleForStandingOrders() {
        if (isSOEligible == null) {
            isSOEligible = Boolean.FALSE;
            EnumEStoreId eStoreId = (null !=this.getUserContext() && null !=this.getUserContext().getStoreContext()) ? this.getUserContext().getStoreContext().getEStoreId() : EnumEStoreId.FD;
            if (!EnumEStoreId.FDX.equals(eStoreId) && isSOEnabled()) {
                isSOEligible = hasCorporateOrder() || isCorporateUser();
            }
        }

        // LOGGER.debug("Customer eligible for SO: " + isSOEligible);

        return isSOEligible.booleanValue();
    }

    private Boolean hasCorporateOrder() {
        try {
            FDOrderHistory h = (FDOrderHistory) getOrderHistory();

            // System.err.println("order info: " + h.getFDOrderInfos().size() + " == total " + h.getTotalOrderCount());
            for (FDOrderInfoI i : h.getFDOrderInfos()) {
                // LOGGER.debug("Sale ID=" + i.getErpSalesId() + "; DLV TYPE=" + i.getDeliveryType() + "; SO ID=" + i.getStandingOrderId());
                if (EnumDeliveryType.CORPORATE.equals(i.getDeliveryType()) || i.getStandingOrderId() != null) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        } catch (FDResourceException e) {
            LOGGER.error("Order info crashed; exc=" + e);
            return null;
        }
    }

    @Override
    public boolean isEligibleForClientCodes() {
        if (cliCodeEligible == null)
            cliCodeEligible = FDStoreProperties.isClientCodesEnabled() && hasCorporateOrder();

        // LOGGER.debug("Customer eligible for Client Codes: " + cliCodeEligible);

        return cliCodeEligible != null ? cliCodeEligible : false;
    }

    @Override
    public FDStandingOrder getCurrentStandingOrder() {
        throw new IllegalArgumentException("Calling getCurrentStandingOrder() in FDUser is not allowed.");
    }

    @Override
    public EnumCheckoutMode getCheckoutMode() {
        return EnumCheckoutMode.NORMAL;
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
        return getUserContext().getPricingContext();
    }

    @Override
    public UserContext getUserContext() {
        try {

            if (userContext == null) {
                userContext = new UserContext();

                StoreContext storeContext = StoreContext.createStoreContext(EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId())));
                userContext.setStoreContext(storeContext); // TODO this should be changed once FDX_CMS is merged!!! also check StoreContext.createDefault()

                userContext.setFdIdentity(getIdentity()); // TODO maybe FDIdentity should be removed from FDUser
                userContext.setCustSapId(getCustSapId()); // Avalara needs customer's SAP Id.
                ErpAddressModel address = null;
                if (this.getAddress() != null && this.getAddress().isCustomerAnonymousAddress()) {
                    address = new ErpAddressModel(this.getAddress());
                } else if (identity != null) {
                    address = getFulfillmentAddress(identity, storeContext.getEStoreId());
                } else if (this.getAddress() != null) {
                    address = new ErpAddressModel(this.getAddress());
                }

                userContext = setFulfillmentAndPricingContext(userContext, address, true);
            }
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e, e.getMessage());
        }

        return userContext;
    }
    
    public UserContext getUserContext(boolean override) {
        try {

            if (userContext == null) {
                userContext = new UserContext();

                StoreContext storeContext = StoreContext.createStoreContext(EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId())));
                userContext.setStoreContext(storeContext); // TODO this should be changed once FDX_CMS is merged!!! also check StoreContext.createDefault()

                userContext.setFdIdentity(getIdentity()); // TODO maybe FDIdentity should be removed from FDUser
                userContext.setCustSapId(getCustSapId()); // Avalara needs customer's SAP Id.
                ErpAddressModel address = null;
                if (this.getAddress() != null && this.getAddress().isCustomerAnonymousAddress()) {
                    address = new ErpAddressModel(this.getAddress());
                } else if (identity != null) {
                    address = getFulfillmentAddress(identity, storeContext.getEStoreId());
                } else if (this.getAddress() != null) {
                    address = new ErpAddressModel(this.getAddress());
                }

                userContext = setFulfillmentAndPricingContext(userContext, address, override);
            }
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e, e.getMessage());
        }

        return userContext;
    }

    private boolean isAddressValidForFulfillmentCheck(ErpAddressModel address) {

        return (address != null && !StringUtil.isEmpty(address.getZipCode())) ? true : false;
    }

    private FulfillmentInfo getFulfillmentInfo(ErpAddressModel address, Date deliveryDate, boolean override) {
        try {      	
        	FDDeliveryZoneInfo deliveryZoneInfo = FDDeliveryManager.getInstance().getZoneInfo(address, (deliveryDate!=null)?deliveryDate: getNextDay(), getHistoricOrderSize(), this.getRegionSvcType(address.getId()),
                     address.getCustomerId());  
        	if(override){
	        	//APPDEV 6442 FDC transition changes
	        	FDDeliveryZoneInfo f = overrideZoneInfo(address, deliveryZoneInfo);       	 
	            if (f!=null && f.getFulfillmentInfo()!=null){
	            	return f.getFulfillmentInfo();
	            }
        	}
            //If the user does not have a reservation and does not fall under the look ahead days (as determined by overrideZoneInfo() ) - return the original fulfillment call
            return deliveryZoneInfo.getFulfillmentInfo();
        } catch (FDInvalidAddressException e) {
            e.printStackTrace();
        } catch (FDResourceException e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
    public  FDDeliveryZoneInfo overrideZoneInfo(ErpAddressModel address,
			FDDeliveryZoneInfo deliveryZoneInfo) throws FDResourceException,FDInvalidAddressException {
		int lookAheadDays = FDStoreProperties.getFdcTransitionLookAheadDays();
		if(lookAheadDays > 0){
		FDDeliveryZoneInfo reservationDeliveryZoneInfo = getReservationDeliveryZoneInfo(); 
		//Case 1: If the user has a reservation, we will be using the fulfillment information associated with user's reservation as the user context at the time of login
		if(null!=reservationDeliveryZoneInfo){
			return reservationDeliveryZoneInfo;
		}
		//Case 2: If the user does not have any reservation, we will follow the look ahead days strategy to set the user context at the time of login
		// The lookAheadDays property value is provided and maintained by SAP. If the value is 0, the code shall not be executed
		if(null==reservationDeliveryZoneInfo) {
			Date day = DateUtil.truncate(DateUtil.addDays(today(), lookAheadDays));
			FDDeliveryZoneInfo fdcDeliveryZoneInfo = FDDeliveryManager.getInstance().getZoneInfo(address, day, getHistoricOrderSize(), this.getRegionSvcType(address.getId()),
		            address.getCustomerId());
			if (futurePlantLogin( deliveryZoneInfo, fdcDeliveryZoneInfo))
		        return fdcDeliveryZoneInfo;
			}
		}
		return null;
	}

	private boolean futurePlantLogin(FDDeliveryZoneInfo deliveryZoneInfo,
			FDDeliveryZoneInfo fdcDeliveryZoneInfo) {
		boolean isDifferentPlant = false;
		if (fdcDeliveryZoneInfo != null && fdcDeliveryZoneInfo.getFulfillmentInfo() !=null && deliveryZoneInfo != null && deliveryZoneInfo.getFulfillmentInfo() !=null ) {
			String oldPlant = null;
			String newPlant = null;
			oldPlant = deliveryZoneInfo.getFulfillmentInfo().getPlantCode();
			newPlant = fdcDeliveryZoneInfo.getFulfillmentInfo().getPlantCode();

			if (!oldPlant.equals(newPlant)) {
				isDifferentPlant = true;
			}
		}
		return isDifferentPlant;

	}
	
	private FDDeliveryZoneInfo getReservationDeliveryZoneInfo() {

		Date standardReservationDeliveryDate = null;
		Date weeklyOrOneTimeReservationDeliveryDate = null;
		ErpAddressModel standardReservationAddress = null;
		ErpAddressModel weeklyOrOneTimeReservationAddress = null;
		//Extract the delivery date and address from the user's standard reservation which is set to user's shopping cart
		if (null != this.getShoppingCart() & null != this.getShoppingCart().getDeliveryReservation()) {
			standardReservationDeliveryDate = this.getShoppingCart().getDeliveryReservation().getDeliveryDate();
			standardReservationAddress = this.getShoppingCart().getDeliveryAddress();
		}
		// Extract the delivery date and address from the user's weekly or one time reservation which is set to user's object
		if (null != this.getReservation()) {
			weeklyOrOneTimeReservationDeliveryDate = this.getReservation().getDeliveryDate();
			weeklyOrOneTimeReservationAddress = this.getReservation().getAddress();
		}

		// Weekly or One time reservation has higher precedence than Standard reservation
		if (null != weeklyOrOneTimeReservationDeliveryDate & null != weeklyOrOneTimeReservationAddress) {
			try {
				return FDDeliveryManager.getInstance().getZoneInfo(
								weeklyOrOneTimeReservationAddress,
								weeklyOrOneTimeReservationDeliveryDate,
								getHistoricOrderSize(),
								this.getRegionSvcType(weeklyOrOneTimeReservationAddress.getId()),
								weeklyOrOneTimeReservationAddress.getCustomerId());
			} catch (FDResourceException e) {
				e.printStackTrace();
			} catch (FDInvalidAddressException e) {
				e.printStackTrace();
			}
		}
		//checking if the user has any standard reservation
		if (null != standardReservationDeliveryDate & null != standardReservationAddress) {
			try {
				return FDDeliveryManager.getInstance().getZoneInfo(
								standardReservationAddress,
								standardReservationDeliveryDate,
								getHistoricOrderSize(),
								this.getRegionSvcType(standardReservationAddress.getId()),
								standardReservationAddress.getCustomerId());
			} catch (FDResourceException e) {
				e.printStackTrace();
			} catch (FDInvalidAddressException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private UserContext setFulfillmentAndPricingContext(UserContext userContext, ErpAddressModel address, boolean override) throws FDResourceException {

        FulfillmentContext fulfillmentContext = new FulfillmentContext();
        if (this.getZipCode() != null && FDStoreProperties.isAlcoholRestrictionByContextEnabled()) {
            // [APPDEV-2857] Blocking Alcohol for customers outside of Alcohol Delivery Area
            boolean alcoholRestrictedByContext = FDUserUtil.isAlcoholRestricted(this.getZipCode());
            fulfillmentContext.setAlcoholRestricted(alcoholRestrictedByContext);
        }

//        String pricingZoneId = FDZoneInfoManager.findZoneId(getZPServiceType().getName(), address != null ? address.getZipCode() : getZipCode());
        String zipCode = address != null ? address.getZipCode() : getZipCode();
        boolean isPickupOnlyORNotServicebleZip = isPickupOnlyORNotServicebleZip(userContext, zipCode);
        /*Adding this condition as part of APPDEV 6036
         * We are passing the zone pricing service type to be in sync with user selected service type*/
        EnumServiceType zpServiceType = address != null ? address.getServiceType() : getZPServiceType();
        String pricingZoneId = FDZoneInfoManager.findZoneId(zpServiceType.getName(), zipCode ,isPickupOnlyORNotServicebleZip);

        FulfillmentInfo fulfillmentInfo = null;
        ZoneInfo zoneInfo = null;

        if (isAddressValidForFulfillmentCheck(address)) {
            fulfillmentInfo = getFulfillmentInfo(address, (this.getShoppingCart() instanceof FDModifyCartModel)? 
            		(this.getShoppingCart()!=null && this.getShoppingCart().getDeliveryReservation()!=null 
            		&& this.getShoppingCart().getDeliveryReservation().getTimeslot()!=null 
            		&& this.getShoppingCart().getDeliveryReservation().getTimeslot().getDeliveryDate()!=null ? 
            		this.getShoppingCart().getDeliveryReservation().getTimeslot().getDeliveryDate() : null):
            		(this.getShoppingCart()!=null && this.getShoppingCart().getDeliveryReservation()!=null 
            		&& this.getShoppingCart().getDeliveryReservation().getTimeslot()!=null 
            		&& this.getShoppingCart().getDeliveryReservation().getTimeslot().getDeliveryDate()!=null
            		&& address!=null && address.getPK()!=null 
            		&& this.getShoppingCart().getDeliveryReservation().getAddressId()!=null
            		&& this.getShoppingCart().getDeliveryReservation().getAddressId().equals(address.getId())
            		&& !this.getShoppingCart().getDeliveryReservation().getTimeslot().getDeliveryDate().before(today()))?
            				this.getShoppingCart().getDeliveryReservation().getTimeslot().getDeliveryDate():null, override);
        }

        if (fulfillmentInfo == null) {
            // default the fulfillments
            if (EnumEStoreId.FDX.equals(userContext.getStoreContext().getEStoreId())) {
                fulfillmentContext.setPlantId(FDStoreProperties.getDefaultFdxPlantID());
                fulfillmentContext.setSalesOrg(FDStoreProperties.getDefaultFdxSalesOrg());
                fulfillmentContext.setDistChannel(FDStoreProperties.getDefaultFdxDistributionChannel());
                zoneInfo = new ZoneInfo(pricingZoneId, FDStoreProperties.getDefaultFdxSalesOrg(), FDStoreProperties.getDefaultFdxDistributionChannel(),
                        ZoneInfo.PricingIndicator.BASE, new ZoneInfo(pricingZoneId, FDStoreProperties.getDefaultFdxSalesOrgParent(),
                                FDStoreProperties.getDefaultFdxDistributionChannelParent()));
            } else {
                fulfillmentContext.setPlantId(FDStoreProperties.getDefaultFdPlantID());
                fulfillmentContext.setSalesOrg(FDStoreProperties.getDefaultFdSalesOrg());
                fulfillmentContext.setDistChannel(FDStoreProperties.getDefaultFdDistributionChannel());
                zoneInfo = new ZoneInfo(pricingZoneId, FDStoreProperties.getDefaultFdSalesOrg(), FDStoreProperties.getDefaultFdDistributionChannel());
            }

        } else {
            fulfillmentContext.setPlantId(fulfillmentInfo.getPlantCode());
            fulfillmentContext.setSalesOrg(fulfillmentInfo.getSalesArea().getSalesOrg());
            fulfillmentContext.setDistChannel(fulfillmentInfo.getSalesArea().getDistChannel());
            zoneInfo = getZoneInfo(pricingZoneId, fulfillmentInfo.getSalesArea());
        }
        userContext.setFulfillmentContext(fulfillmentContext);
        userContext.setPricingContext(new PricingContext(zoneInfo));
        return userContext;
    }

	private boolean isPickupOnlyORNotServicebleZip(UserContext userContext, String zipCode) throws FDResourceException {
		boolean isPickupOnlyORNotServicebleZip = true;
		if(null != zipCode){
			Set availableServices= FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, userContext !=null && userContext.getStoreContext()!=null?userContext.getStoreContext().getEStoreId():EnumEStoreId.FD).getAvailableServices();
	//		setAvailableServices(availableServices);
	//		boolean isPickupOnlyORNotServicebleZip = isPickupOnly() || isNotServiceable();
	        isPickupOnlyORNotServicebleZip = (null ==availableServices || availableServices.isEmpty())
	        		||(!availableServices.contains(EnumServiceType.DEPOT) && !availableServices.contains(EnumServiceType.CORPORATE) && !availableServices.contains(EnumServiceType.HOME) && availableServices.contains(EnumServiceType.PICKUP));
		}
		return isPickupOnlyORNotServicebleZip;
	}
	
    /** use getUserContext().setPricingContext() instead */
    @Deprecated
    protected void setPricingContext(PricingContext pricingContext) {
        getUserContext().setPricingContext(pricingContext);
    }

    /** use getUserContext().resetPricingContext() instead */
    @Override
    @Deprecated
    public void resetPricingContext() {
        getUserContext().resetPricingContext();
    }

    /**
     * Added for Junit testing.<br>
     * Use getUserContext().setDefaultPricingContext() instead
     */
    @Deprecated
    public void setDefaultPricingContext() {
        getUserContext().setDefaultPricingContext();
    }

    public String constructZoneIdForQueryString() {
        String zoneIdParam = "";
        try {
        	boolean isPickupOnlyORNotServicebleZip = isPickupOnly() || isNotServiceable();
            String zoneId = FDZoneInfoManager.findZoneId(getZPServiceType().getName(), getZipCode(), isPickupOnlyORNotServicebleZip);
            if (zoneId.equalsIgnoreCase(ZonePriceListing.MASTER_DEFAULT_ZONE)) {
                zoneIdParam = "zonelevel=true && mzid=" + zoneId;
            } else if (zoneId.equalsIgnoreCase(ZonePriceListing.RESIDENTIAL_DEFAULT_ZONE) || zoneId.equalsIgnoreCase(ZonePriceListing.CORPORATE_DEFAULT_ZONE)) {
                zoneIdParam = "zonelevel=true && szid=" + zoneId + "mzid=" + ZonePriceListing.MASTER_DEFAULT_ZONE;
            } else {
                zoneIdParam = "zonelevel=true && zid=" + zoneId;
                zoneId = FDZoneInfoManager.findZoneId(getZPServiceType().getName(), null);
                zoneIdParam = zoneIdParam + "&& szid=" + zoneId + "mzid=" + ZonePriceListing.MASTER_DEFAULT_ZONE;
            }
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e.getMessage());
        }
        return zoneIdParam;
    }

    @Override
    public SortedSet<IgnoreCaseString> getClientCodesHistory() {
        if (clientCodesHistory == null) {
            clientCodesHistory = new TreeSet<IgnoreCaseString>();
            try {
                clientCodesHistory.addAll(FDCustomerManager.getOrderClientCodesForUser(getIdentity()));
            } catch (FDResourceException e) {
                LOGGER.warn("unable to retrieve order client codes for: " + getIdentity(), e);
            }
            clientCodesHistory.addAll(getCartClientCodes());
        }
        return clientCodesHistory;
    }

    private Collection<? extends IgnoreCaseString> getCartClientCodes() {
        Set<IgnoreCaseString> ccs = new HashSet<IgnoreCaseString>();
        for (FDCartLineI ol : getShoppingCart().getOrderLines())
            for (ErpClientCode cc : ol.getClientCodes())
                ccs.add(new IgnoreCaseString(cc.getClientCode()));
        return ccs;
    }

    @Override
    public Set<String> getAllAppliedPromos() {
        return allAppliedPromos;
    }

    @Override
    public void addPromoErrorCode(String promoCode, int errorCode) {
        promoErrorCodes.put(promoCode, errorCode);
    }

    @Override
    public int getPromoErrorCode(String promoCode) {
        Integer code = this.promoErrorCodes.get(promoCode);
        if (code == null)
            return 0;
        return code;
    }

    @Override
    public void clearPromoErrorCodes() {
        this.promoErrorCodes.clear();
    }

    @Override
    public void clearAllAppliedPromos() {
        this.allAppliedPromos.clear();
    }

    @Override
    public void setMasqueradeContext(MasqueradeContext ctx) {
        this.masqueradeContext = ctx;
    }

    @Override
    public MasqueradeContext getMasqueradeContext() {
        return masqueradeContext;
    }

    @Deprecated
    @Override
    public EnumWinePrice getPreferredWinePrice() {
        if (identity == null) {
            return EnumWinePrice.ONE;
        }
        if (preferredWinePrice == null) {
            preferredWinePrice = DatabaseScoreFactorProvider.getInstance().getPreferredWinePrice(identity.getErpCustomerPK());
            if (preferredWinePrice == null) {
                preferredWinePrice = EnumWinePrice.ONE;
            }
        }
        return preferredWinePrice;
    }

    @Override
    public int getTotalCTSlots() {
        return ctSlots;
    }

    @Override
    public void setTotalCTSlots(int slots) {
        this.ctSlots = slots;
    }

    @Override
    public double getPercSlotsSold() {
        return percSlotsSold;
    }

    @Override
    public void setPercSlotsSold(double percSlotsSold) {
        this.percSlotsSold = percSlotsSold;
    }

    @Override
    public String getGreeting() throws FDResourceException {
        StoreModel store = (StoreModel) ContentFactory.getInstance().getContentNode("Store", "FreshDirect");
        FDSurvey customerProfileSurvey = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.CUSTOMER_PROFILE_SURVEY, EnumServiceType.HOME);
        FDSurveyResponse surveyResponse = FDSurveyFactory.getCustomerProfileSurveyInfo(getIdentity(), EnumServiceType.HOME);
        if (customerProfileSurvey != null && surveyResponse != null) {
            String[] answers = surveyResponse.getAnswer("Occasions_Events");
            if (answers != null && answers.length != 0) {
                boolean found = false;
                for (String answer : answers)
                    if ("birthdays".equals(answer)) {
                        found = true;
                        break;
                    }
                if (found) {
                    String[] birthday = surveyResponse.getAnswer(FDSurveyConstants.BIRTHDAY);
                    if (birthday != null && birthday.length == 2)
                        try {
                            String month = birthday[0];
                            int day = Integer.parseInt(birthday[1]);
                            Calendar cal = Calendar.getInstance();
                            Date now = new Date();
                            cal.setTime(now);
                            String thisMonth = new SimpleDateFormat("MMM").format(now);
                            if (thisMonth.equalsIgnoreCase(month) && cal.get(Calendar.DAY_OF_MONTH) == day)
                                return "Happy birthday, " + getFirstName() + "!";
                        } catch (NumberFormatException e) {
                        }
                }
            }
        }
        MyFD myfd = null;
        if (store != null) {
            myfd = store.getMyFD();
            if (myfd != null) {
                List<HolidayGreeting> greetings = myfd.getHolidayGreetings();
                Date now = new Date();
                for (HolidayGreeting greeting : greetings) {
                    Date start = greeting.getIntervalStartDate();
                    Date end = greeting.getIntervalEndDate();
                    if (start == null || end == null)
                        continue;
                    if (now.before(start) || now.after(end))
                        continue;
                    if (customerProfileSurvey == null || surveyResponse == null)
                        continue;
                    // SELECT a.name
                    // FROM CUST.survey_qa qa
                    // INNER JOIN CUST.survey_question q
                    // ON qa.question = q.id
                    // INNER JOIN cust.survey_answer a
                    // ON qa.answer = a.id
                    // WHERE q.name = 'Occasions_Events';
                    String[] answers = surveyResponse.getAnswer("Occasions_Events");
                    if (answers == null || answers.length == 0)
                        continue;
                    boolean found = false;
                    String code = greeting.getCode();
                    for (String a : answers)
                        if (a != null && a.equals(code)) {
                            found = true;
                            break;
                        }
                    if (!found) {
                        continue;
                    }
                    String text = greeting.getGreetingText();
                    text = text.replace("%firstname", getFirstName());
                    return text;
                }
            }
        }
        return "Hello " + getFirstName() + "!";
    }

    @Override
    public Date getRegistrationDate() {
        if (registrationDate == null) {
            if (getIdentity() != null && getIdentity().getErpCustomerPK() != null) {
                ErpActivityRecord template = new ErpActivityRecord();
                template.setCustomerId(getIdentity().getErpCustomerPK());
                template.setActivityType(EnumAccountActivityType.CREATE_ACCOUNT);
                try {
                    Collection<ErpActivityRecord> results = ActivityLog.getInstance().findActivityByTemplate(template);
                    if (results.size() > 0) {
                        registrationDate = results.iterator().next().getDate();
                    } else {
                        registrationDate = EPOCH;
                    }
                } catch (FDResourceException e) {
                    registrationDate = EPOCH;
                }
            } else {
                registrationDate = EPOCH;
            }
        }
        if (registrationDate != EPOCH) {
            return registrationDate;
        } else {
            return null;
        }
    }

    @Override
    public SessionEvent getSessionEvent() {
        return event;
    }

    @Override
    public void setSessionEvent(SessionEvent event) {
        this.event = event;
    }

    @Override
    public boolean getShowPendingOrderOverlay() {
        boolean showOverlay = false;
        boolean isAddOnOrder = getMasqueradeContext() != null && getMasqueradeContext().isAddOnOrderEnabled();
        if (!isAddOnOrder) {
            showOverlay = FDStoreProperties.isPendingOrderPopupEnabled()
                    && (FDStoreProperties.isPendingOrderPopupMocked() || (this.showPendingOrderOverlay && !this.suspendShowPendingOrderOverlay))
                    && (getMasqueradeContext() == null || (getMasqueradeContext() != null && getMasqueradeContext().isEmptyMakeGoodOrderLineIdQuantities()));
        }
        return showOverlay;
    }

    @Override
    public void setShowPendingOrderOverlay(boolean showPendingOrderOverlay) {
        this.showPendingOrderOverlay = showPendingOrderOverlay;
    }

    @Override
    public void setSuspendShowPendingOrderOverlay(boolean suspendShowPendingOrderOverlay) {
        this.suspendShowPendingOrderOverlay = suspendShowPendingOrderOverlay;
    }

    @Override
    public boolean isSupendShowPendingOrderOverlay() {
        return this.suspendShowPendingOrderOverlay;
    }

    /* check if user has a valid (regular) pending order (so exclude all other types) */
    @Override
    public boolean hasPendingOrder() throws FDResourceException {
        return hasPendingOrder(false, false);
    }

    /* check if user has a valid pending order. inclusions optional. */
    @Override
    public boolean hasPendingOrder(boolean incGiftCardOrds, boolean incDonationOrds) throws FDResourceException {
        List<FDOrderInfoI> orderHistoryInfo = new ArrayList<FDOrderInfoI>(getPendingOrders(incGiftCardOrds, incDonationOrds, true));

        return (orderHistoryInfo.size() > 0) ? true : false;
    }

    /* return List of orderInfos for all pending orders (regular orders only), sorted. */
    @Override
    public List<FDOrderInfoI> getPendingOrders() throws FDResourceException {
        return getPendingOrders(false, false, true);
    }

    /* return List of orderInfos for all pending orders inclusions optional. */
    @Override
    public List<FDOrderInfoI> getPendingOrders(boolean incGiftCardOrds, boolean incDonationOrds, boolean sorted) throws FDResourceException {

        FDOrderHistory history = (FDOrderHistory) getOrderHistory();// Changed to fetch from cache.
        EnumEStoreId eStore = getUserContext().getStoreContext().getEStoreId();
        Date currentDate = new Date();
        List<FDOrderInfoI> orderHistoryInfo = new ArrayList<FDOrderInfoI>(history.getFDOrderInfos(EnumSaleType.REGULAR, eStore));

        if (incGiftCardOrds) {
            // Add gift cards orders too.
            orderHistoryInfo.addAll(history.getFDOrderInfos(EnumSaleType.GIFTCARD, eStore));
        }

        if (incDonationOrds) {
            // ADD Donation Orders too-for Robin Hood.
            orderHistoryInfo.addAll(history.getFDOrderInfos(EnumSaleType.DONATION, eStore));
        }

        List<FDOrderInfoI> validPendingOrders = new ArrayList<FDOrderInfoI>();

        // LOGGER.debug("getPendingOrders(incGiftCardOrds:"+incGiftCardOrds+", incDonationOrds:"+incDonationOrds+", sorted:"+sorted+")");
        for (Iterator<FDOrderInfoI> hIter = orderHistoryInfo.iterator(); hIter.hasNext();) {
            FDOrderInfoI orderInfo = hIter.next();

            // LOGGER.debug("SaleId:"+orderInfo.getErpSalesId()+", getOrderStatus:"+orderInfo.getOrderStatus().toString()
            // +", isModifiable:"+orderInfo.isModifiable()+", getDeliveryCutoffTime:"+orderInfo.getDeliveryCutoffTime());

            if (orderInfo.isModifiable()) {

                if (orderInfo.getOrderStatus() == EnumSaleStatus.REFUSED_ORDER)
                    continue;

                /*
                 * if we wanted individual types of GC or donation orders, use this code String ordDeliveryType = orderInfo.getDeliveryType().toString();
                 * 
                 * //gift cards String gcCodePersonal = EnumDeliveryType.GIFT_CARD_PERSONAL.getCode(); String gcCodeCorporate = EnumDeliveryType.GIFT_CARD_CORPORATE.getCode();
                 * 
                 * if ((ordDeliveryType).equals(gcCodePersonal)) continue; if ((ordDeliveryType).equals(gcCodeCorporate)) continue;
                 * 
                 * //robin hood String donatePersonal = EnumDeliveryType.DONATION_INDIVIDUAL.getCode(); String donateCorporate = EnumDeliveryType.DONATION_BUSINESS.getCode();
                 * 
                 * if ((ordDeliveryType).equals(donatePersonal)) continue; if ((ordDeliveryType).equals(donateCorporate)) continue;
                 */

                if (currentDate.before(orderInfo.getDeliveryCutoffTime())) {
                    validPendingOrders.add(orderInfo);
                }
            } else if (orderInfo.isNewOrder() && EnumSaleType.REGULAR.equals(orderInfo.getSaleType())) {
                this.setAnyNewOrder(true);
            }
        }

        if (sorted) {
            Collections.sort(orderHistoryInfo, ORDER_COMPARATOR);
        }

        return validPendingOrders;
    }

    /** Sorts orders by dlv. start time, descending */
    private final static Comparator<FDOrderInfoI> ORDER_COMPARATOR = new Comparator<FDOrderInfoI>() {

        @Override
        public int compare(FDOrderInfoI o1, FDOrderInfoI o2) {
            return (o2).getRequestedDate().compareTo((o1).getRequestedDate());
        }
    };

    @Override
    public FDCartModel getMergePendCart() {
        return (this.mergePendCart == null) ? this.mergePendCart = new FDCartModel() : this.mergePendCart;
    }

    @Override
    public void setMergePendCart(FDCartModel mergePendCart) {
        if (mergePendCart == null || mergePendCart.getClass().isAssignableFrom(getMergePendCart().getClass())) {
            this.mergePendCart = mergePendCart;
        }
    }

    @Override
    public void setReferralLink() {
        try {
            this.referralLink = FDReferralManager.getReferralLink(this.getIdentity().getErpCustomerPK());
        } catch (FDResourceException e) {
            LOGGER.error("Exception getting referralLink", e);
        }
        ;
    }

    @Override
    public String getReferralLink() {
        if (referralLink == null)
            setReferralLink();
        return referralLink;
    }

    @Override
    public void setReferralPrgmId(String referralPrgmId) {
        this.referralPrgmId = referralPrgmId;
    }

    @Override
    public String getReferralPrgmId() {
        return referralPrgmId;
    }

    @Override
    public void setReferralCustomerId(String referralCustomerId) {
        this.referralCustomerId = referralCustomerId;
    }

    @Override
    public String getReferralCustomerId() {
        return referralCustomerId;
    }

    @Override
    public double getAvailableCredit() {
        if (totalCredit == null)
            setAvailableCredit();
        return totalCredit.doubleValue();
    }

    public void setAvailableCredit() {
        try {
            totalCredit = FDReferralManager.getAvailableCredit(this.getIdentity().getErpCustomerPK());
        } catch (FDResourceException e) {
            LOGGER.error("Exception getting totalCredit", e);
        }
    }

    @Override
    public boolean isReferralProgramAvailable() {
        if (referralFlag == null)
            setReferralPromoAvailable();
        return referralFlag.booleanValue();
    }

    public void setReferralPromoAvailable() {
        try {
            referralFlag = getOrderHistory().hasSettledOrders();//FDReferralManager.getReferralDisplayFlag(this.getIdentity().getErpCustomerPK());
            LOGGER.debug("Getting ref display for :" + this.getIdentity().getErpCustomerPK() + "-and flag is:" + referralFlag);
        } catch (FDResourceException e) {
            LOGGER.error("Exception getting totalCredit", e);
        }
    }

    @Override
    public void setReferralPromotionFraud(boolean fraud) {
        this.referralFraud = fraud;
    }

    @Override
    public boolean isReferralPromotionFraud() {
        return referralFraud;
    }

    public boolean isEligibleForDDPP() throws FDResourceException {
        if (null == identity) {
            return false;
        }
        return this.getFDCustomer().isEligibleForDDPP();
    }

    @Override
    public boolean isPopUpPendingOrderOverlay() {
        boolean showOverlay = false;
        try {
            showOverlay = getShowPendingOrderOverlay() && getLevel() >= RECOGNIZED
                    && (getMasqueradeContext() == null || (getMasqueradeContext() != null && getMasqueradeContext().isEmptyMakeGoodOrderLineIdQuantities()))
                    && (hasPendingOrder() || FDStoreProperties.isPendingOrderPopupMocked());
        } catch (FDResourceException e) {
            LOGGER.debug("a really unexpected and really unnecessarily delegated exception", e);
        }
        return showOverlay;
    }

    @Override
    public EnumGiftCardType getGiftCardType() {
        return giftCardType;
    }

    @Override
    public void setGiftCardType(EnumGiftCardType giftCardType) {
        this.giftCardType = giftCardType;
    }

    @Override
    public boolean isEbtAccepted() {
        return ebtAccepted;
    }

    @Override
    public void setEbtAccepted(boolean ebtAccepted) {
        this.ebtAccepted = ebtAccepted;
    }

    @Override
    public boolean isDpNewTcBlocking() {
        return isDpNewTcBlocking(true);
    }

    @Override
    public boolean isDpNewTcBlocking(boolean includeViewCount) {
        boolean isBlocking = false;

        // guestAllowed pages will have a null identity
        if (identity == null) {
            return isBlocking;
        }

        try {
            ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);

            int dpTcViewCount = cm.getDpTcViewCount();
            Date dpTcAgreeDate = cm.getDpTcAgreeDate();
            Date dpNewTcStartDate = FDStoreProperties.getDlvPassNewTCDate();
            Calendar calNow = Calendar.getInstance();
            Calendar calNewTcStart = Calendar.getInstance();
            calNewTcStart.setTime(dpNewTcStartDate);
            Calendar calAgree = null;
            if (dpTcAgreeDate != null) {
                calAgree = Calendar.getInstance();
                calAgree.setTime(dpTcAgreeDate);
            }

            if (this.isDlvPassActive() && calNewTcStart.getTime().after(this.getDlvPassInfo().getPurchaseDate())) { // exclude users with no pass, and ones that purchased after new
                                                                                                                    // terms start
                if (calNow.getTime().after(dpNewTcStartDate)) { // check that new terms should be in effect
                    if (dpTcAgreeDate == null || (calAgree != null && calAgree.getTime().before(dpNewTcStartDate))) { // either never agreed, or agree before new terms
                        if (dpTcViewCount < FDStoreProperties.getDpTcViewLimit() || Boolean.FALSE.equals(includeViewCount)) { // check view count
                            isBlocking = true;
                        }
                    }
                }
            }
        } catch (FDResourceException e) {
            LOGGER.error("Error checking isDpNewTcBlocking in FDUser.", e);
        }

        return isBlocking;
    }

    @Override
    public boolean isWaiveDPFuelSurCharge(boolean includeViewCount) {
        boolean isBlocking = false;

        // guestAllowed pages will have a null identity
        if (identity == null) {
            return isBlocking;
        }

        try {
            ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);

            int dpTcViewCount = cm.getDpTcViewCount();
            Date dpTcAgreeDate = cm.getDpTcAgreeDate();
            Date dpNewTcStartDate = FDStoreProperties.getDlvPassNewTCDate();
            Calendar calNow = Calendar.getInstance();
            Calendar calNewTcStart = Calendar.getInstance();
            calNewTcStart.setTime(dpNewTcStartDate);
            Calendar calAgree = null;
            if (dpTcAgreeDate != null) {
                calAgree = Calendar.getInstance();
                calAgree.setTime(dpTcAgreeDate);
            }

            if (this.isDlvPassActive()) { // exclude users with no pass, and ones that purchased after new terms start
                if (calNow.getTime().after(dpNewTcStartDate)) { // check that new terms should be in effect
                    if (((this.getDlvPassInfo().getPurchaseDate() != null && this.getDlvPassInfo().getPurchaseDate().before(calNewTcStart.getTime())) && calAgree == null)
                            || (calAgree != null && calAgree.getTime().before(dpNewTcStartDate))) { // either never agreed, or agree before new terms
                        if (dpTcViewCount < FDStoreProperties.getDpTcViewLimit() || Boolean.FALSE.equals(includeViewCount)) { // check view count
                            isBlocking = true;
                        }
                    }
                }
            }
        } catch (FDResourceException e) {
            LOGGER.error("Error checking isDpNewTcBlocking in FDUser.", e);
        }

        return isBlocking;
    }

    @Override
    public boolean hasEBTAlert() {
        if (hasEBTAlert == null) {
            hasEBTAlert = Boolean.FALSE;

            try {
                if (null != getIdentity() && FDCustomerManager.isOnAlert(getIdentity().getErpCustomerPK(), EnumAlertType.EBT.getName())) {
                    hasEBTAlert = Boolean.TRUE;
                    ;
                }
            } catch (FDResourceException e) {
                LOGGER.error("Error checking hasEBTAlert in FDUser.", e);
            }
        }

        LOGGER.debug("Customer has an EBT Alert: " + hasEBTAlert);

        return hasEBTAlert.booleanValue();
    }

    @Override
    public Set<String> getSteeringSlotIds() {
        return steeringSlotIds;
    }

    @Override
    public void setSteeringSlotIds(Set<String> steeringSlotIds) {
        this.steeringSlotIds = steeringSlotIds;
    }

    @Override
    public synchronized List<ErpAddressModel> getAllHomeAddresses() throws FDResourceException {
        if (identity != null && cachedAllHomeAddresses == null) {
            cachedAllHomeAddresses = getAllAddressesForServiceType(EnumServiceType.HOME);
        }
        return cachedAllHomeAddresses;
    }

    @Override
    public synchronized List<ErpAddressModel> getAllCorporateAddresses() throws FDResourceException {
        if (identity != null && cachedAllCorporateAddresses == null) {
            cachedAllCorporateAddresses = getAllAddressesForServiceType(EnumServiceType.CORPORATE);
        }
        return cachedAllCorporateAddresses;
    }

    private List<ErpAddressModel> getAllAddressesForServiceType(EnumServiceType serviceType) throws FDResourceException {
        List<ErpAddressModel> addresses = new ArrayList<ErpAddressModel>();

        for (ErpAddressModel erpAddress : FDCustomerFactory.getErpCustomer(identity.getErpCustomerPK()).getShipToAddresses()) {
            if (erpAddress != null && serviceType.equals(erpAddress.getServiceType())) {
                addresses.add(erpAddress);
            }
        }
        return addresses;
    }

    @Override
    public synchronized void invalidateAllAddressesCaches() {
        cachedAllHomeAddresses = null;
        cachedAllCorporateAddresses = null;
    }

    /** returns address used by location handler bar */
    @Override
    public AddressModel getSelectedAddress() {
        FDCartModel cart = getShoppingCart();

        if (cart != null) {
            AddressModel cartAddress = cart.getDeliveryAddress();
            if (cartAddress != null) {
                return cartAddress;
            }
        }
        return address;
    }

    @Override
    public EnumDeliveryStatus getDeliveryStatus() throws FDResourceException {
        FDDeliveryServiceSelectionResult serviceResult = null;

        AddressModel selectedAddress = getSelectedAddress();
        String address1 = selectedAddress.getAddress1();
        if (address1 != null && address1.length() > 0) { // only check by address if necessary
            try {
                serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByAddress(selectedAddress);
            } catch (FDInvalidAddressException e) {
                // this should never occur as address has already been validated
                LOGGER.error("invalid address, fallback to zip code check", e);
            }
        }
        if (serviceResult == null) {
            serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(selectedAddress.getZipCode(), this.getUserContext().getStoreContext().getEStoreId());
        }

        return serviceResult.getServiceStatus(getUserServiceType());
    }

    @Override
    public Set<ExternalCampaign> getExternalPromoCampaigns() {
        return externalPromoCampaigns;
    }

    @Override
    public void setExternalPromoCampaigns(Set<ExternalCampaign> externalPromoCampaigns) {
        this.externalPromoCampaigns = externalPromoCampaigns;
    }

    @Override
    public void setExternalCampaign(ExternalCampaign externalCampaign) {
        this.externalCampaign = externalCampaign;
    }

    @Override
    public ExternalCampaign getExternalCampaign() {
        return externalCampaign;
    }

    @Override
    public FDCustomerCouponWallet getCouponWallet() {
        return couponWallet;
    }

    @Override
    public void setCouponWallet(FDCustomerCouponWallet couponWallet) {
        this.couponWallet = couponWallet;
    }

    // Get Coupon Customer based on UPC
    @Override
    public FDCustomerCoupon getCustomerCoupon(String upc, EnumCouponContext ctx) {
        return FDUserCouponUtil.getCustomerCoupon(this, upc, ctx, couponWallet);
    }

    @Override
    public FDCustomerCoupon getCustomerCoupon(FDProductInfo prodInfo, EnumCouponContext ctx, String catId, String prodId) {
        return FDUserCouponUtil.getCustomerCoupon(this, prodInfo, ctx, catId, prodId, couponWallet);
    }

    @Override
    public FDCustomerCoupon getCustomerCoupon(FDCartLineI cartLine, EnumCouponContext ctx) {
        return FDUserCouponUtil.getCustomerCoupon(this, cartLine, ctx, couponWallet);
    }

    // Get Coupon Customer based on CartLine
    @Override
    public FDCustomerCoupon getCustomerCoupon(FDCartLineI cartLine, EnumCouponContext ctx, String catId, String prodId) {
        return FDUserCouponUtil.getCustomerCoupon(this, cartLine, ctx, catId, prodId, couponWallet);
    }

    @Override
    public void updateClippedCoupon(String couponId) {
        FDUserCouponUtil.updateClippedCoupon(couponId, couponWallet);
    }

    @Override
    public boolean isEligibleForCoupons() throws FDResourceException {
        boolean isEligible = false;
        if (FDCouponProperties.isCouponsEnabled() || (null != identity && this.getFDCustomer().isEligibleForCoupons())) {
            isEligible = true;
        }
        return isEligible;
    }

    @Override
    public boolean isCouponsSystemAvailable() throws FDResourceException {
        boolean isCouponsSystemAvailable = true;
        if (isEligibleForCoupons() && FDCouponProperties.isCouponsBlackHoleEnabled()) {
            isCouponsSystemAvailable = false;
        }
        return isCouponsSystemAvailable;
    }

    @Override
    public boolean isCouponEvaluationRequired() {
        return null != getCouponWallet() ? getCouponWallet().isCouponEvaluationRequired() : false;
    }

    @Override
    public void setCouponEvaluationRequired(boolean couponEvaluationRequired) {
        if (null != getCouponWallet()) {
            getCouponWallet().setCouponEvaluationRequired(couponEvaluationRequired);
        }
    }

    @Override
    public boolean isRefreshCouponWalletRequired() {
        return null != getCouponWallet() ? getCouponWallet().isRefreshCouponWalletRequired() : false;
    }

    @Override
    public void setRefreshCouponWalletRequired(boolean refreshCouponWalletRequired) {
        if (null != getCouponWallet()) {
            getCouponWallet().setRefreshCouponWalletRequired(refreshCouponWalletRequired);
        }
    }

    @Override
    public boolean isRobot() {
        return robot;
    }

    @Override
    public void setRobot(boolean robot) {
        this.robot = robot;
    }

    @Override
    public String getDefaultListId() {
        return defaultListId;
    }

    @Override
    public void setDefaultListId(String listId) {
        defaultListId = listId;
    }

    @Override
    public EnumRegionServiceType getRegionSvcType(String addressId) {

        if (this.getShoppingCart() != null && this.getShoppingCart().getDeliveryReservation() != null && this.getShoppingCart().getDeliveryReservation().getAddressId() != null
                && this.getShoppingCart().getDeliveryReservation().getAddressId().equals(addressId)) {
            return this.getShoppingCart().getDeliveryReservation().getRegionSvcType();
        } else if (this.getReservation() != null && this.getReservation().getAddressId() != null && this.getReservation().getAddressId().equals(addressId)) {
            return this.getReservation().getRegionSvcType();
        } else {
            return null;
        }
    }

    @Override
    public boolean isPaymentechEnabled() {
        if (FDStoreProperties.isPaymentechGatewayEnabled())
            return true;
        try {
            if (getIdentity() != null && !StringUtil.isEmpty(getIdentity().getErpCustomerPK())) {
                return FDCustomerManager.isFeatureEnabled(getIdentity().getErpCustomerPK(), EnumSiteFeature.PAYMENTECH_GATEWAY);
            }
        } catch (FDResourceException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public double getMinHomeOrderAmount() {

        if (getShoppingCart() != null && getShoppingCart().getDeliveryAddress() != null) {
            try {
                String county = FDDeliveryManager.getInstance().getCounty(getShoppingCart().getDeliveryAddress());
                String zip = getShoppingCart().getDeliveryAddress().getZipCode();
                String zipcodes = FDStoreProperties.getSuffolkZips();
                if ("SUFFOLK".equalsIgnoreCase(county) && (zipcodes.indexOf(zip) == -1)) {
                    return ErpServicesProperties.getSufFolkCountyMinimumOrderAmount();
                }
            } catch (FDResourceException e) {
                throw new FDRuntimeException(e);
            }
        }
        return ErpServicesProperties.getMinimumOrderAmount();

    }

    @Override
    public boolean isAnyNewOrder() {
        return anyNewOrder;
    }

    @Override
    public void setAnyNewOrder(boolean anyNewOrder) {
        this.anyNewOrder = anyNewOrder;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Calling this function always returns false (only FDSessionUser implements it)
     */
    @Override
    public boolean hasJustLoggedIn() {
        return false;
    }

    /**
     * Calling this function always returns false (only FDSessionUser implements it)
     */
    @Override
    public boolean hasJustLoggedIn(boolean clear) {
        return false;
    }

    /**
     * Calling this function has no effect (only FDSessionUser implements it)
     */
    @Override
    public void setJustLoggedIn(boolean val) {
    }

    /**
     * Calling this function always returns false (only FDSessionUser implements it)
     */
    @Override
    public boolean hasJustSignedUp() {
        return false;
    }

    /**
     * Calling this function always returns false (only FDSessionUser implements it)
     */
    @Override
    public boolean hasJustSignedUp(boolean clear) {
        return false;
    }

    /**
     * Calling this function always returns false (only FDSessionUser implements it)
     */
    @Override
    public void setJustSignedUp(boolean val) {
    }

    /**
     * Calling this function always returns false (only FDSessionUser implements it)
     */
    public boolean isRafFriendSignedUp() {
        return false;
    }

    /**
     * Calling this function always returns false (only FDSessionUser implements it)
     */
    public boolean isRafFriendSignedUp(boolean clear) {
        return false;
    }

    /**
     * Calling this function always returns false (only FDSessionUser implements it)
     */
    public void setRafFriendSignedUp(boolean val) {
    }

    @Override
    public boolean isGlobalNavTutorialSeen() {
        return isGlobalNavTutorialSeen;
    }

    @Override
    public void setGlobalNavTutorialSeen(boolean isGlobalNavTutorialSeen) {
        this.isGlobalNavTutorialSeen = isGlobalNavTutorialSeen;
    }

    @Override
    public List<ProductReference> getProductSamples() {
        return this.productSamples;
    }

    @Override
    public void setProductSample(List<ProductReference> productSamples) {
        this.productSamples = productSamples;
    }

    /* return List of orderInfos for all orders in En-route status. */
    @Override
    public List<FDOrderInfoI> getScheduledOrdersForDelivery(boolean sorted) throws FDResourceException {
        LOGGER.debug("getScheduledOrdersForDelivery: " + sorted);

        FDOrderHistory history = (FDOrderHistory) getOrderHistory();// Changed to fetch from cache.
        List<FDOrderInfoI> orderHistoryInfo = new ArrayList<FDOrderInfoI>(history.getFDOrderInfos(EnumSaleType.REGULAR));

        List<FDOrderInfoI> validScheduledOrders = new ArrayList<FDOrderInfoI>();
        if (orderHistoryInfo != null) {
            for (Iterator<FDOrderInfoI> hIter = orderHistoryInfo.iterator(); hIter.hasNext();) {
                FDOrderInfoI orderInfo = hIter.next();
                if (EnumSaleStatus.ENROUTE.equals(orderInfo.getOrderStatus())) {
                    validScheduledOrders.add(orderInfo);
                }
            }
        }
        LOGGER.debug("Total Orders scheduled for delivery: " + validScheduledOrders.size());
        if (sorted && !validScheduledOrders.isEmpty()) {
            Collections.sort(validScheduledOrders, ORDER_DELIVERY_STARTTIME_COMPARATOR);
        }

        return validScheduledOrders;
    }

    /** Sorts orders by dlv. start time, ascending */
    private final static Comparator<FDOrderInfoI> ORDER_DELIVERY_STARTTIME_COMPARATOR = new Comparator<FDOrderInfoI>() {

        @Override
        public int compare(FDOrderInfoI o1, FDOrderInfoI o2) {
            return (o1).getDeliveryStartTime().compareTo((o2).getDeliveryStartTime());
        }
    };

    @Override
    public boolean isProductSample(ProductReference prodRef) {
        List<ProductReference> productSamples = getProductSamples();
        if (null != productSamples && !productSamples.isEmpty())
            for (ProductReference prod : productSamples) {
                if (null != prod && prod.equals(prodRef)) {
                    return true;
                }
            }
        return false;
    }

    private ErpAddressModel getFulfillmentAddress(FDIdentity identity, EnumEStoreId eStoreId) throws FDResourceException {

        ErpAddressModel address = this.getShoppingCart().getDeliveryAddress();
        if (address == null)
            address = FDCustomerManager.getLastOrderAddress(identity, eStoreId);

        if (EnumEStoreId.FDX.equals(eStoreId) && address == null) {
            address = FDCustomerManager.getLastOrderAddress(identity, EnumEStoreId.FD);
        }
        return address;
    }

    private static Date today() {
        Date d = OncePerRequestDateCache.getToday();
        if (d == null) {
            d = new Date();
        }
        return d;
    }
    
    private Date getNextDay() {
        Date today=today();
        if (EnumEStoreId.FDX.equals(getUserContext().getStoreContext().getEStoreId())){
        	return today;
        } else {
        Calendar cal=Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
        }
    }

    private static ZoneInfo getZoneInfo(String pricingZone, SalesArea salesArea) {
        if (salesArea.getDefaultSalesArea() != null && salesArea.getDefaultSalesArea().getSalesOrg() != null) {
            return new ZoneInfo(pricingZone, salesArea.getSalesOrg(), salesArea.getDistChannel(),
                    ZoneInfo.PricingIndicator.BASE.getValue().equals(salesArea.getPricingIndicator()) ? ZoneInfo.PricingIndicator.BASE : ZoneInfo.PricingIndicator.SALE,
                    getZoneInfo(pricingZone, salesArea.getDefaultSalesArea()));
        } else {
            return new ZoneInfo(pricingZone, salesArea.getSalesOrg(), salesArea.getDistChannel());
        }
    }

    @Override
    public void resetUserContext() {
        this.userContext = null;
    }

    @Override
    public boolean isCrmMode() {
        return crmMode;
    }

    @Override
    public void setCrmMode(boolean flag) {
        crmMode = flag;
    }

    @Override
    public boolean isVHPopupDisplay() {
        // TODO Auto-generated method stub
        return this.vHPopupDisplay;
    }

    @Override
    public void setVHPopupDisplay(boolean flag) {
        this.vHPopupDisplay = flag;
    }

    /**
     * @return the rafClickId
     */
    @Override
    public String getRafClickId() {
        return rafClickId;
    }

    /**
     * @param rafClickId
     *            the rafClickId to set
     */
    @Override
    public void setRafClickId(String rafClickId) {
        this.rafClickId = rafClickId;
    }

    /**
     * @return the rafPromoCode
     */
    @Override
    public String getRafPromoCode() {
        return rafPromoCode;
    }

    /**
     * @param rafPromoCode
     *            the rafPromoCode to set
     */
    @Override
    public void setRafPromoCode(String rafPromoCode) {
        this.rafPromoCode = rafPromoCode;
    }

    @Override
    public Date getFirstOrderDateByStore(EnumEStoreId eStoreId) throws FDResourceException {
        if (null == firstOrderDateByStore) {
            OrderHistoryI orderHistory = this.getOrderHistory();
            if (null != orderHistory) {
                firstOrderDateByStore = orderHistory.getFirstOrderDateByStore(eStoreId);
            }
        }
        return firstOrderDateByStore;
    }

    @Override
    public FDCartModel getSoTemplateCart() {
        return soTemplateCart;
    }

    @Override
    public void setSoTemplateCart(FDCartModel soTemplateCart) {
        this.soTemplateCart = soTemplateCart;
    }

    @Override
    public boolean isCustomerHasStandingOrders() {
        boolean isCustomerHasSO = false;

        final FDStandingOrdersManager m = FDStandingOrdersManager.getInstance();
        try {
            try {
                isCustomerHasSO = m.checkCustomerHasSo(this.identity);
            } catch (FDInvalidConfigurationException e) {
                LOGGER.error("Error in isCustomerHasStandingOrders : customer id " + this.identity, e);
            }
        } catch (FDResourceException e) {
            LOGGER.error("Error in isCustomerHasStandingOrders  : customer id " + this.identity, e);
        }
        return isCustomerHasSO;
    }

    @Override
    public void setCurrentStandingOrder(FDStandingOrder currentStandingOrder) {
        throw new IllegalArgumentException("Calling setCurrentStandingOrder() in FDUser is not allowed.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.fdstore.customer.FDUserI#isNewSO3Enabled() To enable customer to SO 3
     */

    @Override
    public boolean isNewSO3Enabled() {
        return !EnumFeatureRolloutStrategy.NONE.equals(FeatureRolloutArbiter.getFeatureRolloutStrategy(EnumRolloutFeature.standingorder3_0, this));
        /*
         * // Check personal flag in user profile boolean isSO3EnabledInProfile=false; try { //TODO need to change this once get identity issue is resolved if(getLevel()>=2){
         * isSO3EnabledInProfile = Boolean.valueOf(getFDCustomer().getProfile().getAttribute(PROFILE_SO3_KEY)).booleanValue(); }
         * 
         * } catch (FDResourceException e) { LOGGER.error("Error in isNewSO3Enabled  : customer id " + this.identity, e);
         * 
         * } return isSO3EnabledInProfile;
         */
    }

    /* session only */
    @Override
    public boolean isSoContainerOpen() {
        throw new IllegalArgumentException("Calling isSoContainerOpen() in FDUser is not allowed (use FDSessionUser instead).");
    }

    @Override
    public Collection<FDStandingOrder> getValidSO3() {
        return this.validSO3s;
    }

    @Override
    public void setValidSO3(Collection<FDStandingOrder> validSO3s) {

        this.validSO3s = validSO3s;
    }

    @Override
    public Collection<FDStandingOrder> getAllSO3() {
        return this.allSO3s;
    }

    @Override
    public void setAllSO3(Collection<FDStandingOrder> allSO3s) {
        this.allSO3s = allSO3s;
    }
    
    @Override
    public boolean isRefreshSO3() {
        return this.refreshSO3;
    }

    @Override
    public void setRefreshSO3(boolean isRefreshSO3) {
        this.refreshSO3 = isRefreshSO3;
        if(refreshSO3){
			setRefreshSO3Settings(true);
		}
        
    }

    @Override
    public boolean isZipCheckPopupUsed() {
        return isZipCheckPopupUsed;
    }

    public void setZipCheckPopupUsed(boolean isZipCheckPopupUsed) {
        this.isZipCheckPopupUsed = isZipCheckPopupUsed;
    }

	@Override
    public Map<String, String> getSoCartLineMessagesMap() {
		return soCartLineMessagesMap;
	}

	@Override
    public void setSoCartLineMessagesMap(Map<String, String> soCartLineMessagesMap) {
		this.soCartLineMessagesMap = soCartLineMessagesMap;
	}

	public String getCustSapId() {
		return custSapId;
	}

	public void setCustSapId(String custSapId) {
		this.custSapId = custSapId;
	}

	@Override
    public boolean isSoCartOverlayFirstTime() {
		return soCartOverlayFirstTime;
	}

	@Override
    public void setSoCartOverlayFirstTime(boolean soCartOverlayFirstTime) {
		this.soCartOverlayFirstTime = soCartOverlayFirstTime;
		if(soCartOverlayFirstTime){
			setRefreshSO3Settings(true);
		}
	}

	@Override
    public boolean isRefreshSoCartOverlay() {
		return refreshSoCartOverlay;
	}

	@Override
    public void setRefreshSoCartOverlay(boolean refreshSoCartOverlay) {
		this.refreshSoCartOverlay = refreshSoCartOverlay;
		if(refreshSoCartOverlay){
			setRefreshSO3Settings(true);
		}
	}
	
	public String isFromLogin() {
		return fromLogin;
	}


	public void setFromLogin(String fromLogin) {
		this.fromLogin = fromLogin;
	}


	@Override
    public boolean isRefreshNewSoFeature() {
		return refreshNewSoFeature;
	}

	@Override
    public void setRefreshNewSoFeature(boolean refreshNewSoFeature) {
		this.refreshNewSoFeature = refreshNewSoFeature;
		if(refreshNewSoFeature){
			setRefreshSO3Settings(true);
		}
		
	}

	@Override
    public boolean isSoFeatureOverlay() {
		return soFeatureOverlay;
	}

	@Override
    public void setSoFeatureOverlay(boolean soFeatureOverlay) {
		this.soFeatureOverlay = soFeatureOverlay;
	}

	// Only created for jackson parsing in Storefront 2.0
	public void setReferrerEligible(Boolean referrerEligible) {
		this.referrerEligible = referrerEligible;
	}

	@Override
    public void setValidSO3Data(Map<String, Object> validSO3Data){
		this.validSO3Data = validSO3Data;
	}
    
    @Override
    public Map<String, Object> getValidSO3Data(){
    	return this.validSO3Data;
    }
		
    @Override
    public boolean isRefreshSO3Settings(){
    	return this.refreshSO3Settings;
    }

    @Override
    public void setRefreshSO3Settings(boolean isRefreshSO3Settings){
    	this.refreshSO3Settings = isRefreshSO3Settings;
    }
    
    @Override
    public int resetDefaultPaymentValueType() {
		try {
			return FDCustomerManager.resetDefaultPaymentValueType(this.getFDCustomer().getId());
		} catch (FDResourceException e) {
			LOGGER.error("Error in resetting default payment method in fdcustomer: " + this.identity, e);
		}
		return 0;		
	}

	@Override
	public void refreshFdCustomer() throws FDResourceException{
		if (this.identity == null) {
            throw new IllegalStateException("No identity");
        }
		this.cachedFDCustomer = FDCustomerFactory.getFDCustomer(this.identity);
	}
}
