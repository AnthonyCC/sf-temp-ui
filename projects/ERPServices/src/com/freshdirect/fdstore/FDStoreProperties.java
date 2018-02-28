package com.freshdirect.fdstore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;

public class FDStoreProperties {

	// OAUTHDEMO
	public static HashMap<String, String> VENDOR_MAP = new HashMap<String, String>();
	
    private static final Category LOGGER = LoggerFactory.getInstance(FDStoreProperties.class);
    private static List<ConfigLoadedListener> listeners = new ArrayList<ConfigLoadedListener>();
    private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd");
    private static final String PROP_EWALLET_MASTERPASS_LIGHT_BOX_URL = "masterpass.lightbox.url";
    private static final String PROP_EWALLET_MASTERPASS_ENV_PROP_NAME = "masterpass.environment";
    private static final String PROP_EWALLET_MP_BTN_IMG_URL = "masterpass.button.img.url";
    private static final String PROP_EWALLET_MP_LOGO_URL = "masterpass.logo.url";
    private static final String SUB_DOMAIN = "fdstore.media.subdomain";
    private final static String PROP_PROVIDER_URL = "fdstore.providerURL";
    private final static String PROP_INIT_CTX_FACTORY = "fdstore.initialContextFactory";
    private final static String PROP_CRM_GEOCODELINK = "fdstore.crm.geocodeLink";
    private final static String PROP_CRM_CASE_LIST_LENGTH = "fdstore.crm.caseListLength";
    private final static String PROP_CRM_CASE_HISTORY_LIST_LENGTH = "fdstore.crm.caseHistoryListLength";
    private final static String PROP_CRM_DISABLE_TIME_WINDOW_CHECK = "fdstore.crm.debugIssueCreditsPage";
    private final static String PROP_FDSTORE_WEB_CAREERLINK = "fdstore.web.careerLink";
    private final static String PROP_FDFACTORY_HOME = "fdstore.fdFactory.home";
    private final static String PROP_SAPGATEWAY_HOME = "fdstore.sapGateway.home";
    private final static String PROP_KANAGATEWAY_HOME = "fdstore.kanaGateway.home";
    private final static String PROP_COMPLAINTMGR_HOME = "fdstore.complaintManager.home";
    private final static String PROP_CALLCENTERMGR_HOME = "fdstore.callCenterManager.home";
    private final static String PROP_FDCUSTMGR_HOME = "fdstore.fdCustomerManager.home";
    private final static String PROP_EWALLET_HOME = "fdstore.ErpEWallet.home";
    private final static String PROP_EWALLET_SERVICE_HOME = "fdstore.EWalletService.home";
    private final static String PROP_MPService_HOME = "fdstore.MPService.home";
    private final static String PROP_PPService_HOME = "fdstore.PPService.home";
    private final static String PROP_EXTERNAL_ACCOUNTMGR_HOME = "fdstore.externalLoginManager.home";
    private final static String PROP_FDPROMOTIONMGR_HOME = "fdstore.fdPromotionManager.home";
    private final static String PROP_FDPROMOTIONMGR_NEW_HOME = "fdstore.fdPromotionManagerNew.home";
    private final static String PROP_DLVMANAGER_HOME = "fdstore.deliveryManager.home";
    private final static String PROP_RULESMANAGER_HOME = "fdstore.rulesManager.home";
    private final static String PROP_AIRCLICMANAGER_HOME = "fdstore.airclicManager.home";
    private final static String PROP_DLVRESTRICTION_MGR_HOME = "freshdirect.delivery.DeliveryRestrictionManager";
    private final static String PROP_CONTENTMANAGER_HOME = "fdstore.ContentManager.home";
    private final static String PROP_FDCUSTOMER_HOME = "fdstore.fdcustomer.home";
    private final static String PROP_ERPCUSTOMER_HOME = "fdstore.erpcustomer.home";
    private final static String PROP_ERPCUSTOMERINFO_HOME = "fdstore.erpcustomerinfo.home";
    private final static String PROP_CONTFACTORY_HOME = "fdstore.contentFactory.home";
    private final static String PROP_FDORDER_HOME = "fdstore.fdorder.home";
    private final static String PROP_DLV_INSTRUCTION_SPECIAL_CHAR = "fdstore.address.validation";
    private final static String PROP_PREVIEW_MODE = "fdstore.preview";
    private final static String PROP_FLUSH_OSCACHE = "fdstore.flushOscache";
    private final static String PROP_ANNOTATION_MODE = "fdstore.annotation";
    private final static String PROP_ANNOTATION_ERPSY = "fdstore.annotation.erpsy";
    private final static String PROP_MEDIA_PATH = "fdstore.media.path";
    private final static String PROP_CALLCENTER_PW = "fdstore.callCenter.pw";

    private final static String PROP_CUSTOMER_SERVICE_EMAIL = "fdstore.customerService.email";
    private final static String PROP_EMAIL_PRODUCT = "fdstore.email.product";
    private final static String PROP_EMAIL_FEEDBACK = "fdstore.email.feedback";
    private final static String PROP_EMAIL_CHEFSTABLE = "fdstore.email.chefstable";
    private final static String PROP_EMAIL_VENDING = "fdstore.email.vending";
    // FDX
    private final static String PROP_EMAIL_FDX_ANNOUNCE = "fdstore.email.fdx.announce";
    private final static String PROP_EMAIL_FDX_ORDER = "fdstore.email.fdx.order";
    private final static String PROP_EMAIL_FDX_ACTSERVICE = "fdstore.email.fdx.actservice";
    private final static String PROP_EMAIL_FDX_SIDEKICKS = "fdstore.email.fdx.sidekicks";
    private final static String PROP_EMAIL_FDX_PRODUCT_REQUEST = "fdstore.email.fdx.productrequest";

    private final static String PROP_HOLIDAY_LOOKAHEAD_DAYS = "fdstore.holidayLookaheadDays";
    private final static String PROP_DLV_PROMO_EXP_DATE = "fdstore.dlvPromo.expDate";
    private final static String PROP_EMAIL_PROMOTION = "fdstore.email.promotion";
    private final static String PROP_CUTOFF_WARN = "fdstore.cutoffWarning";
    private final static String PROP_CUTOFF_DEFAULT_ZONE_CODE = "fdstore.cutoffDefaultZoneCode";
    private final static String PROP_PRERESERVE_HOURS = "fdstore.preReserve.hours";
    private final static String PROP_AD_SERVER_URL = "fdstore.adServerURL";
    private final static String PROP_AD_SERVER_ENABLED = "fdstore.adServerEnabled";
    private final static String PROP_DLVFEE_TIER_ENABLED = "fdstore.dlvfeeTierEnabled";
    private final static String PROP_AD_SERVER_UPDATES_URL = "fdstore.adServerUpdatesURL";
    private final static String PROP_AD_SERVER_PROFILE_ATTRIBS = "fdstore.adServerProfileAttribs";
    private final static String PROP_AD_SERVER_USES_DEFERRED_IMAGE_LOADING = "fdstore.adServerUsesDeferredImageLoading";
    private final static String PROP_IMPRESSION_LIMIT = "fdstore.impressionLimit";
    private final static String PROP_WINBACK_ROOT = "fdstore.winbackRoot";
    private final static String PROP_MARKETING_PROMO_ROOT = "fdstore.marketingPromoRoot";
    private final static String PROP_REFRESHSECS_PRODUCTINFO = "fdstore.refreshSecs.productInfo";
    private final static String PROP_REFRESHSECS_GROUPSCALE = "fdstore.refreshSecs.groupscale";
    private final static String PROP_REFRESHSECS_UPCPRODUCTINFO = "fdstore.refreshSecs.upcProductInfo";
    private final static String PROP_REFRESHSECS_PRODUCT = "fdstore.refreshSecs.product";
    private final static String PROP_REFRESHSECS_ZONE = "fdstore.refreshSecs.zone";
    private final static String PROP_PRODUCT_CACHE_SIZE = "fdstore.product.cache.size";
    private final static String PROP_ZONE_CACHE_SIZE = "fdstore.zone.cache.size";
    private final static String PROP_GRP_CACHE_SIZE = "fdstore.grp.cache.size";
    private final static String PROP_MEDIACONTENT_CACHE_SIZE = "fdstore.mediaConent.cache.size";
    private final static String PROP_CMS_MOSTLY_READONLY = "fdstore.cms.readonly.optimization";
    private final static String PROP_PRELOAD_STORE = "fdstore.preLoad";
    private final static String PROP_PRELOAD_PROMOTIONS = "fdstore.preLoad.promotions";
    private final static String PROP_PRELOAD_GROUPS = "fdstore.preLoad.groupscale";
    private final static String PROP_WARMUP_CLASS = "fdstore.preLoad.class";
    private final static String PROP_PRELOAD_NEWNESS = "fdstore.preLoad.newness";
    private final static String PROP_PRELOAD_REINTRODUCED = "fdstore.preLoad.reintroduced";
    private final static String PROP_PRELOAD_SMARTSTORE = "fdstore.preLoad.smartStore";
    private final static String PROP_PRELOAD_AUTOCOMPLETIONS = "fdstore.preLoad.autocompletions";
    private final static String PROP_CMS_MEDIABASEURL = "cms.mediaBaseURL";
    private final static String PROP_PAYMENT_METHOD_MANAGER_HOME = "fdstore.PaymentMethodManager.home";
    private final static String PROP_RESTRICTED_PAYMENT_METHOD_HOME = "freshdirect.payment.RestrictedPaymentMethod.home";
    private final static String PROP_SUMMERSERVICE = "fdstore.summerserviceEnabled";
    private final static String PROP_EXTERNAL_FRAUD_CHECK_PM = "fraud.check.paymentMethod.external";
    private final static String PROP_MAX_REFERRALS = "referral.maxReferrals";
    private final static String PROP_NUM_DAYS_MAX_REFERRALS = "referral.numDaysMaxReferrals";
    private final static String PROP_FDREFERRALMGR_HOME = "fdstore.fdReferralManager.home";
    private final static String PROP_USE_MULTIPLE_PROMOTIONS = "fdstore.useMultiplePromotions";
    private final static String PROP_DATA_COLLECTION_ENABLED = "fdstore.dataCollectionEnabled";
    private final static String PROP_PRODUCT_RECOMMEND_ENABLED = "fdstore.productRecommendEnabled";
    private final static String PROP_PRODUCT_RECOMMEND_CHECK_CACHE_ENABLED = "fdstore.productRecommendCheckCacheEnabled";
    // Delivery Pass Store properties.
    private final static String BSGS_SIGNUP_URL = "fdstore.bsgsSignupUrl";
    private final static String UNLIMITED_SIGNUP_URL = "fdstore.unlimitedSignupUrl";
    private final static String UNLIMITED_PROMOTIONAL_SIGNUP_URL = "fdstore.unlimitedPromotionalSignupUrl";
    private final static String UNLIMITED_AMAZON_PRIME_SIGNUP_URL = "fdstore.unlimitedAmazonPrimeSignupUrl";
    private final static String CRM_BSGS_SIGNUP_URL = "fdstore.callCenter.bsgsSignupUrl";
    private final static String CRM_UNLIMITED_SIGNUP_URL = "fdstore.callCenter.unlimitedSignupUrl";
    private final static String CRM_UNLIMITED_PROMOTIONAL_SIGNUP_URL = "fdstore.callCenter.unlimitedPromotionalSignupUrl";
    private final static String CRM_UNLIMITED_AMAZON_PRIME_SIGNUP_URL = "fdstore.callCenter.unlimitedAmazonPrimeSignupUrl";
    private final static String BSGS_PROFILE_POSFIX = "fdstore.bsgsProfilePosfix";
    private final static String UNLIMITED_PROFILE_POSFIX = "fdstore.unlimitedProfilePosfix";
    private final static String UNLIMITED_PROMOTIONAL_PROFILE = "fdstore.unlimitedPromotionalProfile";
    private final static String UNLIMITED_AMAZON_PRIME_PROFILE = "fdstore.unlimitedAmazonPrimeProfile";
    private static final String CRM_CREDIT_ISSUE_BCC = "fdstore.callCenter.creditIssue.bcc";
    private final static String DLV_PASS_PROMOTION_PREFIX = "fdstore.dlvPassPromotionPrefix";
    private final static String DLV_PASS_MAX_PURCHASE_LIMIT = "fdstore.dlvPass.maxPurchaseLimit";
    private final static String DLV_PASS_AUTORENEWAL_DEFAULT = "fdstore.dlvPass.defautRenewalSKU";

    // SmartStore
    private final static String DYF_STRATEGY_CACHE_ENTRIES = "fdstore.strategy.cache.entries";
    private final static String SS_GLOBAL_POPULARITY_SCORING = "fdstore.scoring.globalPopularity";
    private final static String SS_USER_POPULARITY_SCORING = "fdstore.scoring.userPopularity";
    private final static String SS_SHORT_TERM_POPULARITY_SCORING = "fdstore.scoring.shortTermPopularity";

    // DYF Site Feature
    @Deprecated
    private final static String DYF_ENABLED = "fdstore.dyf.enabled";

    // DYF FREQBOUGHT PARAMETERS
    private final static String DYF_FREQBOUGHT_TOPN = "fdstore.dyf.freqbought.topN";
    private final static String DYF_FREQBOUGHT_TOPPERCENT = "fdstore.dyf.freqbought.topPercent";
    private final static String SMARTSTORE_NEWPRODUCTS_DAYS = "fdstore.smartstore.newProducts.days";
    private final static String SMARTSTORE_PRELOAD_FACTORS = "fdstore.smartstore.preloadFactors";
    private final static String SMARTSTORE_CACHE_DATA_SOURCES = "fdstore.smartstore.cacheDataSources";
    private final static String SMARTSTORE_CACHE_DATA_SOURCES_SIZE = "fdstore.smartstore.cacheDataSources.size";
    public final static String SMARTSTORE_CACHE_ONLINE_FACTORS = "fdstore.smartstore.cacheOnlineFactors";
    public final static String SMARTSTORE_CMS_RECOMM_REFRESH_RATE = "fdstore.smartstore.cmsRecommenderRefreshRate";

    // maximum number of entries (users) in smartstore personalized scores cache, default 500
    private final static String SMARTSTORE_PERSONAL_SCORES_CACHE_ENTRIES = "fdstore.smartstore.personalScores.cache.entries";

    // timeout of a cache entry in seconds, default 30*60
    private final static String SMARTSTORE_PERSONAL_SCORES_CAHCE_TIMEOUT = "fdstore.smartstore.personalScores.cache.timeout";
    private final static String SMARTSTORE_OFFLINE_REC_RECENT_DAYS = "fdstore.smartstore.offlineRecommender.noOfRecentDays";
    private final static String SMARTSTORE_OFFLINE_REC_MAX_AGE = "fdstore.smartstore.offlineRecommender.maxRecommendationAge";
    private final static String SMARTSTORE_OFFLINE_REC_SITE_FEATURES = "fdstore.smartstore.offlineRecommender.siteFeatures";
    private final static String SMARTSTORE_OFFLINE_REC_THREAD_COUNT = "fdstore.smartstore.offlineRecommender.threadCount";
    private final static String SMARTSTORE_OFFLINE_REC_WINDOW_LENGTH = "fdstore.smartstore.offlineRecommender.windowLength";

    // Referral Program admin
    private final static String RFL_PRG_PAGINATION_SIZE = "fdstore.referral.paginationSize";

    // cut off time properties
    private static final String CUT_OFF_TIME_SUN = "fdstore.cut_off_day_1";
    private static final String CUT_OFF_TIME_MON = "fdstore.cut_off_day_2";
    private static final String CUT_OFF_TIME_TUES = "fdstore.cut_off_day_3";
    private static final String CUT_OFF_TIME_WED = "fdstore.cut_off_day_4";
    private static final String CUT_OFF_TIME_THUS = "fdstore.cut_off_day_5";
    private static final String CUT_OFF_TIME_FRI = "fdstore.cut_off_day_6";
    private static final String CUT_OFF_TIME_SAT = "fdstore.cut_off_day_7";

    // customer service hours properties
    private static final String CLICK_TO_CALL = "fdstore.click_to_call";
    private static final String CUST_SERV_HOURS_SUN = "fdstore.cust_serv_day_1";
    private static final String CUST_SERV_HOURS_MON = "fdstore.cust_serv_day_2";
    private static final String CUST_SERV_HOURS_TUES = "fdstore.cust_serv_day_3";
    private static final String CUST_SERV_HOURS_WED = "fdstore.cust_serv_day_4";
    private static final String CUST_SERV_HOURS_THUS = "fdstore.cust_serv_day_5";
    private static final String CUST_SERV_HOURS_FRI = "fdstore.cust_serv_day_6";
    private static final String CUST_SERV_HOURS_SAT = "fdstore.cust_serv_day_7";
    private final static String SKU_AVAILABILITY_REFRESH_PERIOD = "fdstore.sku.availability.refresh";
    private static long lastRefresh = 0;
    private final static long REFRESH_PERIOD = 5 * 60 * 1000;

    // Added for controlling number of orders processed during Mass Cancellation and Mass Returns.
    private final static String PROP_CRM_ORDER_PRC_LIMIT = "fdstore.orderProcessingLimit";

    // Customer Created List settings
    @Deprecated
    private final static String CCL_ENABLED = "fdstore.ccl.enabled";
    private final static String CCL_AJAX_DEBUG_CLIENT = "fdstore.ccl.ajax.debug.client";
    private final static String CCL_AJAX_DEBUG_JSONRPC = "fdstore.ccl.ajax.debug.jsonrpc";
    private final static String CCL_AJAX_DEBUG_FACADE = "fdstore.ccl.ajax.debug.facade";
    private final static String CCL_AJAX_DEBUG_FACADE_EXCEPTION = "fdstore.ccl.ajax.debug.facade_exception";

    // Added for controlling case creation during the retention program survey processing.
    private final static String PROP_RETPRG_CREATECASE = "fdstore.retentionProgram.createCase";

    // Marketing Admin
    private final static String MKT_ADMIN_FILE_UPLOAD_SIZE = "frshdirect.mktadmin.fileupload.size";
    private final static String DISTRIBUTION_SAMPLES_DIR = "fdstore.test.distributions.path";

    // refresh delay in minutes for FDInventoryCache
    private final static String PROP_INVENOTRY_REFRESH_PERIOD = "fdstore.refresh.inventory";

    // refresh delay in minutes for FDAttributesCache
    private final static String PROP_ATTRIBUTES_REFRESH_PERIOD = "fdstore.refresh.attributes";

    // refresh delay in minutes for FDNutritionCache
    private final static String PROP_NUTRITION_REFRESH_PERIOD = "fdstore.refresh.nutrition";

    // Refresh delay in seconds for Runtime Promotion cache.
    private final static String PROP_PROMOTION_RT_REFRESH_PERIOD = "promotion.rt.refresh.period";

    // Handle Advanced Order date
    private final static String ADVANCE_ORDER_START = "fdstore.advance.order.start";
    private final static String ADVANCE_ORDER_END = "fdstore.advance.order.end";

    // Advance Order with days gap
    private final static String ADVANCE_ORDER_GAP = "fdstore.advance.order.isGap";
    private final static String ADVANCE_ORDER_NEW_START = "fdstore.advance.order.newstart";
    private final static String ADVANCE_ORDER_NEW_END = "fdstore.advance.order.newend";
    private static final String MRKTING_ADMIN_URL = "fdstore.mktAdmin.URL";

    // Enable/Disable DCPD Alias Category Handling.
    private static final String DCPD_ALIAS_HANDLING_ENABLED = "fdstore.dcpd.alias.handling.enabled";
    private static Properties config;
    private final static Properties defaults = new Properties();
    private static final String HP_LETTER_MEDIA_PATH1 = "fdstore.mediapath.newcustomer";
    private static final String HP_LETTER_MEDIA_PATH2 = "fdstore.mediapath.oldcustomer";

    // Produce Rating changes
    private static final String PRODUCE_RATING_ENABLED = "fdstore.isProduceRatingEnabled";

    // additional ratings 2009.06
    private static final String PRODUCE_RATING_PREFIXES = "fdstore.produceRatingPrefixes";

    // Freshness Guaranteed On/Off switch
    private static final String FRESHNESS_GUARANTEED_ENABLED = "fdstore.isFreshnessGuaranteedEnabled";

    // freshness guaranteed on/off sku prefix based
    private static final String FRESHNESS_GUARANTEED_PREFIXES = "fdstore.freshnessGuaranteedPrefixes";
    private static final String HPLETTER_MEDIA_ENABLED = "fdstore.isHomePageMediaEnabled";
    private static final String HP_CATEGORY_LINKS_FALLBACK = "fdstore.homePage.categoryLinksPathFallback";

    // Deals changes.
    private static final String DEALS_SKU_PREFIX = "fdstore.deals.skuPrefix";
    private static final String DEALS_LOWER_LIMIT = "fdstore.deals.lowerLimit";
    private static final String DEALS_UPPER_LIMIT = "fdstore.deals.upperLimit";
    private static final String BURST_LOWER_LIMIT = "fdstore.burst.lowerLimit";
    private static final String BURST_UPPER_LIMIT = "fdstore.burst.upperLimit";
    private static final String MAX_FEATURED_DEALS_FOR_PAGE = "fdstore.deals.maxFeaturedDeals";
    private static final String MAX_FEATURED_DEALS_PER_LINE = "fdstore.deals.maxFeaturedDealsPerLine";
    private static final String MIN_FEATURED_DEALS_FOR_PAGE = "fdstore.deals.minFeaturedDeals";
    private static final String TEMP_DIR = "tmpdir";

    private static final String PROP_UPS_BLACKHOLE_ENABLED = "fdstore.ups.blackhole.enabled";

    private static final String PROP_MEAL_KIT_MATERIAL_GROUP = "fdstore.meal.kit.material.group";

    // CORS domain settings
    private static final String CORS_DOMAIN = "fdstore.CORS.domain";

    public static final String PROP_SO3_ACTIVATE_CUTOFF_TIME = "fdstore.so3.activate.cutoff.time";
    
    public static final String DATABASE_IN_CONDITION_LIMIT = "fdstore.db.in.condition.limit";
    // Smart Search
    /**
     * @deprecated
     */
    @Deprecated
    private static final String SMART_SEARCH_ENABLED = "fdstore.newSearch.enabled";
    private static final String DID_YOU_MEAN_RATIO = "fdstore.search.didYouMean.ratio";
    private static final String DID_YOU_MEAN_THRESHOLD = "fdstore.search.didYouMean.threshold";
    private static final String DID_YOU_MEAN_MAXHITS = "fdstore.search.didYouMean.maxHits";
    private static final String PRIMARY_HOME_KEYWORDS_ENABLED = "fdstore.search.primaryHomeKeywordsEnabled";
    private static final String SEARCH_RECURSE_PARENT_ATTRIBUTES_ENABLED = "fdstore.search.recurseParentAttributesEnabled";
    private static final String SEARCH_GLOBALNAV_AUTOCOMPLETE_ENABLE = "fdstore.search.globalnav.autocomplete.enable";

    // COOL info
    private final static String PROP_COOLINFO_REFRESH_PERIOD = "fdstore.refresh.coolinfo";
    private static final String IMPRESSION_LOGGING = "fdstore.impression.logging";

    // Survey Def
    private final static String PROP_SURVEYDEF_CACHE_SIZE = "fdstore.surveyDef.cache.size";
    private static final String PROP_REFRESHSECS_SURVEYDEF = "fdstore.refreshSecs.surveyDef";
    private final static String PROP_FDSURVEY_HOME = "fdstore.fdSurvey.home";

    // What's Good Department
    private final static String PROP_FDWHATSGOOD_ENABLED = "fdstore.fdwhatsgood.enabled";
    private final static String PROP_FDWHATSGOOD_PEAKPRODUCE_ENABLED = "fdstore.fdwhatsgood_peakproduce.enabled";
    private final static String PROP_FDWHATSGOOD_BBLOCK_ENABLED = "fdstore.fdwhatsgood_bblock.enabled";

    // new prop to set dynamic rows
    private final static String PROP_FDWHATSGOOD_ROWS = "fdstore.fdwhatsgood.rows";

    // allow debug messages via property (default to false : off)
    private final static String PROP_FDWHATSGOOD_DEBUG_ENABLED = "fdstore.fdwhatsgood.debugEnabled";

    // Smart Savings
    private static final String SMART_SAVINGS_FEATURE_ENABLED = "fdstore.smartsavings.enabled";

    // iPhone non-customer email media path
    private final static String PROP_MEDIA_IPHONE_TEMPLATE_PATH = "fdstore.media.iphone.template.path"; // Location of different ftl template of iphone.
    private final static String IPHONE_EMAIL_SUBJECT = "fdstore.media.iphone.email.subject";

    // Gift Cards
    private static final String PROP_GIFT_CARD_SKU_CODE = "fdstore.giftcard.skucode";
    private final static String PROP_MEDIA_GIFT_CARD_TEMPLATE_PATH = "fdstore.media.giftcard.template.path"; // Location of different ftl templates of giftcards.
    private static final String PROP_GC_TEMPLATE_BASE_URL = "fdstore.giftcard.template.baseurl";
    private static final String PROP_GC_MIN_AMOUNT = "fdstore.giftcard.minimum.amount";
    private static final String PROP_GC_MAX_AMOUNT = "fdstore.giftcard.maximum.amount";
    private final static String PROP_GIFT_CARD_RECIPIENT_MAX = "giftcard.recipient.max";
    @Deprecated
    private static final String PROP_GC_ENABLED = "fdstore.isGiftCardEnabled";
    private static final String PROP_GC_LANDING_URL = "fdstore.giftCardLandingUrl";
    private static final String PROP_GC_DEPTID = "fdstore.giftCardDeptId";
    private static final String PROP_GC_CATID = "fdstore.giftCardCatId";
    private static final String PROP_GC_PRODNAME = "fdstore.giftCardProdName";
    private static final String GIVEX_BLACK_HOLE_ENABLED = "givex.black.hole.enabled";
    private static final String GIVEX_SECURITY_FIX_ENABLED = "givex.security.fix.enabled";
    private static final String PROP_GC_OOO = "fdstore.giftcardoutoforder";

    // Robin Hood
    private static final String ROBIN_HOOD_ENABLED = "fdstore.isRobinHoodEnabled";
    private static final String ROBIN_HOOD_LANDING_URL = "fdstore.robinHoodLandingUrl";
    private static final String PROP_ROBIN_HOOD_SKU_CODE = "fdstore.robinhood.skucode";
    private static final String ROBIN_HOOD_STATUS = "fdstore.robinhood.status";

    // MEAT DEALS and EDLP
    private final static String DEPT_MEAT_DEALS = "fdstore.department.meatdeals.isEnabled";
    private final static String DEPT_EDLP = "fdstore.department.edlp.isEnabled";
    private final static String DEPT_MEAT_DEALS_CATID = "fdstore.department.meatdeals.catId";
    private final static String DEPT_EDLP_CATID = "fdstore.department.edlp.catId";

    // Mobile
    // iphone
    private final static String MOBILE_IPHONE_LANDING_ENABLED = "fdstore.mobile.iPhone.landingEnabled";
    private final static String MOBILE_IPHONE_LANDING_PAGE = "fdstore.mobile.iPhone.landingPage";

    // android
    private final static String MOBILE_ANDROID_LANDING_ENABLED = "fdstore.mobile.Android.landingEnabled";
    private final static String MOBILE_ANDROID_LANDING_PAGE = "fdstore.mobile.Android.landingPage";
    private final static String PROP_GC_NSM_AUTHSKIP_SECS = "fdstore.gcnsm.authskip.secs";
    private final static String PROP_GC_NSM_FREQ_SECS = "fdstore.gcnsm.frequency.secs";

    private final static String PROP_ZONE_PICKUP_ZIPCODE = "fdstore.zone.pricing.pickup.zipcode";
    private final static String PROP_FDX_ZONE_ZIPCODE = "fdstore.fdx.zone.zipcode";

    // Windows steering
    private final static String WINDOW_STEERING_PROMOTION_PREFIX = "fdstore.windowsteering.promo.prefix";

    // Zone Pricing
    private final static String PROP_ZONE_PRICING_AD_ENABLED = "fdstore.zone.pricing.ad.enabled";
    @Deprecated
    private final static String PROP_ZONE_PRICING_ENABLED = "fdstore.zone.pricing.enabled";

    // Standing Orders
    private final static String SO_GLOBAL_ENABLER = "fdstore.standingorders.enabled";
    private final static String SO_OVERLAP_WINDOWS = "fdstore.standingorders.overlap.windows.enabled";

    // Standing Orders
    private final static String CLIENT_CODES_GLOBAL_ENABLER = "fdstore.clientcodes.enabled";

    // new products revamp
    private static final String PROP_NEWPRODUCTS_DEPTID = "fdstore.newProducts.DeptId";
    private static final String PROP_NEWPRODUCTS_CATID = "fdstore.newProducts.CatId";
    private static final String PROP_NEWPRODUCTS_CATID_FDX = "fdstore.newProducts.CatId_fdx";
    private static final String PROP_NEWPRODUCTS_GROUPS = "fdstore.newproducts.groups";

    // Contact us FAQs
    private static final String PROP_FAQ_SECTIONS = "fdstore.faq.sections";
    private static final String PROP_CRM_HELP_LINK_ADDR_VALIDATION = "crm.help.link.addr.validation";
    private static final String PROP_CRM_HELP_LINK_GIFT_CARD = "crm.help.link.giftcard";
    private static final String PROP_CRM_HELP_LINK_MAIN_HELP = "crm.help.link.main.help";
    private static final String PROP_CRM_HELP_LINK_CUST_PROFILE = "crm.help.link.cust.profile";
    private static final String PROP_CRM_HELP_LINK_TIMESLOT = "crm.help.link.request.timeslot";
    private static final String PROP_CRM_HELP_LINK_FD_UPDATES = "crm.help.link.fdUpdates";
    private static final String PROP_CRM_HELP_LINK_PROMOTIONS = "crm.help.link.promotions";
    private static final String PROP_CRM_HELP_LINK_CASE_MEDIA = "crm.help.link.case.media";
    private static final String PROP_CRM_HELP_LINK_CASE_MORE_ISSUES = "crm.help.link.case.moreIssues";
    private static final String PROP_CRM_HELP_LINK_CASE_CUST_TONE = "crm.help.link.case.cust.tone";
    private static final String PROP_CRM_HELP_LINK_CASE_RESOL_SATISFY = "crm.help.link.resol.satisfactory";
    private static final String PROP_CRM_HELP_LINK_CASE_RESOLV_FIRST = "crm.help.link.resolv.first";
    private static final String PROP_CRM_HELP_LINK_CASE_FIRST_CONTACT = "crm.help.link.first.contact";

    // Email Opt-Down (APPDEV-662)
    private static final String PROP_EMAIL_OPTDOWN_ENABLED = "fdstore.email.optdown.enabled";

    // Google Maps API key
    // It can be obtained from http://code.google.com/apis/maps/signup.html
    private static final String GMAPS_API_KEY = "gmaps.api.key";

    // APPDEV-1091
    private static final String PROMO_PUBLISH_URL_KEY = "promo.publish.url";

    // valid values can be 'master' or 'replica'
    private static final String PROMO_PUBLISH_NODE_TYPE = "promo.publish.node";
    private static final String PROMO_VALID_RT_STATUSES = "promo.valid.rt.statuses";
    private final static String PROP_REDEMPTION_CNT_REFRESH_PERIOD = "promotion.redeem.cnt.refresh.period";
    private final static String PROP_REDEMPTION_SERVER_COUNT = "promotion.redemption.server.count";

    // Delivery Pass at Checkout (APPDEV-664)
    private final static String PROP_DP_CART_ENABLED = "fdstore.dpcart.enabled";
    private final static String PROP_PROMO_LINE_ITEM_EMAIL = "promotion.lineitem.email.display";

    // 4mm department internal cache refresh interval - in minutes
    private final static String PROP_4MM_REFRESH_INTERVAL = "fdstore.4mm.cache.refresh.interval";

    // Brand media replacement (APPDEV-1308)
    private final static String PROP_BRAND_MEDIA_IDS = "fdstore.brand.media.ids";
    private final static String PROP_WS_DISCOUNT_AMOUNT_LIST = "fdstore.ws.discount.list";

    // [APPDEV-1283] Wine Revamp
    private final static String WINE_SHOW_RATINGS_KEY = "fdstore.wine.newPricingRatingEnabled";
    private final static String WINE_PRICE_BUCKET_BOUND_PREFIX = "fdstore.wine.priceBucketBound";
    private final static String ADVERTISING_TILE_ENABLED = "fdstore.advertisingTile.enabled";
    private final static String PROP_CRM_MENU_ROLES_REFRESH_PERIOD = "crm.roles.menu.refresh.period";
    private final static String PROP_CRM_LDAP_USERS_REFRESH_PERIOD = "crm.ldap.users.refresh.period";
    private final static String PROP_CRM_LDAP_ACCESS_HOST_NAME_PRIMARY = "crm.ldap.access.primary.host.name";
    private final static String PROP_CRM_CC_DETAILS_LOOKUP_LIMIT = "crm.cc.details.limit";
    private final static String PROP_CRM_CC_SECURITY_EMAIL = "crm.cc.security.email.addr";
    private final static String PROP_CRM_CC_SECURITY_EMAIL_ENABLED = "crm.cc.security.email.enabled";
    private final static String PROP_CRM_CC_SECURITY_EMAIL_SUBJECT = "crm.cc.security.email.subject";
    private final static String PROP_CRM_CC_DETAILS_ACCESS_KEY = "crm.cc.details.access.key";
    private final static String PROP_CRM_FORGOT_LDAP_PASSWORD_URL = "crm.ldap.password.reset.url";
    private final static String PROP_CRM_SECURITY_SKIP_FILE_TYPES = "crm.security.skip.filetypes";
    private final static String PROP_CRM_SECURITY_SKIP_FOLDERS = "crm.security.skip.folders";
    private final static String PROP_CRM_AGENTS_CACHE_REFRESH_PERIOD = "crm.agents.cache.refresh.period";

    private static final String PROP_CLICK2CALL_CALL_BACL_URL = "fdstore.c2c.callback.url";

    // APPDEV-1215 Sustainable Seafood
    private final static String PROP_SEAFOODSUSTAIN_ENABLED = "fdstore.seafoodsustain.enabled";

    // SEM Project (APPDEV-1598)
    private static final String PROP_SEM_PIXELS = "fdstore.sem.pixels";
    private static final String PROP_SEM_CONFIGS = "fdstore.sem.configs";
    private static final String PROP_SEM_REFRESH_PERIOD = "fdstore.sem.refresh";

    private final static String PROP_DUMPGROUPEXPORT_ENABLED = "fdstore.dumpgroupexport.enabled";
    private final static String PROP_VALIDATIONGROUPEXPORT_ENABLED = "fdstore.validation.groupexport.enabled";
    private final static String PROP_VALIDATIONGROUPEXPORTSAPINPUT_ENABLED = "fdstore.validation.groupexportsapinput.enabled";
    private final static String PROP_GROUPSCALE_ENABLED = "fdstore.groupscale.enabled";
    private final static String PROP_LIMITED_AVAILABILITY_ENABLED = "fdstore.limited.availability.enabled";

    // [APPDEV-1208] Time slot Redesign
    private static final String PROP_CT_TIMESLOT_LABEL = "fdstore.chefstable.ts.label";
    private static final String PROP_PROMO_TIMESLOT_LABEL = "fdstore.promo.ts.label";
    private static final String PROP_ALCOHOL_TIMESLOT_LABEL = "fdstore.alcohol.ts.label";
    private static final String PROP_ECOFRIENDLY_TIMESLOT_LABEL = "fdstore.ecofriendly.ts.label";
    private static final String PROP_MINORDER_TIMESLOT_LABEL = "fdstore.minorder.ts.label";
    private static final String PROP_BUILDINGFAVS_TIMESLOT_LABEL = "fdstore.buildingfavs.ts.label";
    // APPDEV-3107 SAP upgrade customer messaging
    private static final String PROP_TIMESLOT_MSGING = "fdstore.ts.specialmessaging";

    private final static String PROP_STANDING_ORDER_REPORT_TO_EMAIL = "fdstore.standingorder.report.email.to";
    private final static String PROP_STANDING_ORDER_REPORT_EMAIL_SUBJECT = "fdstore.standingorder.report.email.subject";

    /*
     * private final static String PROP_STANDING_ORDER_SOFT_LIMIT = "fdstore.standingorder.softlimit"; private final static String PROP_STANDING_ORDER_HARD_LIMIT =
     * "fdstore.standingorder.hardlimit";
     */

    private final static String PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_ENABLED = "fdstore.mktadmin.auto.upload.report.email.enabled";
    private final static String PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_TO = "fdstore.mktadmin.auto.upload.report.email.to";
    private final static String PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_SUBJECT = "fdstore.mktadmin.auto.upload.report.email.subject";
    private final static String PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_CC = "fdstore.mktadmin.auto.upload.report.email.cc";
    private final static String PROP_MKTADMIN_AUTOUPLOAD_URL = "fdstore.mktadmin.auto.upload.url";
    private final static String PROP_TRUCK_REFRESH_URL = "fdstore.transapp.truck.refresh.url";
    private final static String PROP_EMP_REFRESH_URL = "fdstore.transapp.emp.refresh.url";
    private final static String PROP_MKTADMIN_USER_NAME = "fdstore.mktadmin.username";
    private final static String PROP_MKTADMIN_PASSWORD = "fdstore.mktadmin.password";

    // Enable Timeslot GRID
    private final static String PROP_TIMESLOT_GRID = "fdstore.isNewFDTimeslotGridEnabled";

    private final static String WS_PROMOTION_PRODUCTION_MODE = "fdstore.ws.promotion.production.mode";
    private static final String PROP_PAYMENT_METHOD_VERIFICATION_ENABLED = "fdstore.paymentmethod.verify";
    private static final String PROP_PAYMENT_METHOD_VERIFICATION_LIMIT = "fdstore.paymentmethod.verify.limit";

    private static final String PROP_ORDER_HISTORY_QUERY_ID = "fdstore.orderhistory.query.id";

    // [APPDEV-1846] Pop up for DP copy update
    private static final String PROP_TWO_MONTH_TRIAL_PASS_SKU = "fdstore.twomonth.trialdp.sku";
    private static final String PROP_TWO_MONTH_TRIAL_PASS_PRICE = "fdstore.twomonth.trialdp.price";
    private static final String PROP_ONE_YEAR_DELIVERY_PASS_SKU = "fdstore.oneyear.dp.sku";
    private static final String PROP_SIX_MONTH_DELIVERY_PASS_SKU = "fdstore.sixmonth.dp.sku";
    private static final String PROP_ONE_MONTH_DELIVERY_PASS_SKU = "fdstore.onemonth.dp.sku";

    // APPDEV-1850 build versioning of JavaScript/CSS files
    private static final String BUILDVER_ENABLE = "fdstore.buildver.enable";

    // APPDEV-1920 Remove the "new feature" alert on quickbuy
    private static final String SMARTSTORE_QUICKBUY_NEWALERT_ENABLED = "fdstore.smartstore.quickbuy.newalert.enabled";

    private static final String IPHONE_SEARCH_FILTER_DISCONTINUED_ONLY = "fdstore.iphone.search.filterDiscontinuedOnly";

    // Event Management (Roll and Bounce) Capacity Management Phase 2
    private static final String SESSION_LOGGING_ENABLED = "session.logging.enabled";
    private static final String REAL_TIME_EVENT_ANALYSIS = "realtime.event.analysis";
    private static final String EVENT_KB_SOURCE = "event.kb.source";
    private static final String RULES_REPOSITORY = "rules.repository";
    private static final String DEBUG_EVENT_ANALYSIS = "debug.event.analysis";
    private static final String PROP_COUNTRY_INFO_REFRESH_INTERVAL = "fdstore.countryInfo.refresh.minutes";
    // APPDEV-2072 google analytics environment specific key
    private static final String PROP_GOOGLE_ANALYTICS_KEY = "fdstore.google.analytics.key";
    // APPDEV-3522 Google Analytics: Upgrade to Universal Analytics: Updates for Page Load Times
    private static final String PROP_GOOGLE_ANALYTICS_UNIVERSAL = "fdstore.google.analytics.universal";

    private static final String PROP_GOOGLE_ANALYTICS_DOMAIN = "fdstore.google.analytics.domain";

    // APPDEV-6030 Google Tag Manager
    private static final String PROP_GOOGLE_TAG_MANAGER_KEY = "fdstore.google.tagmanager.key";
    // APPDEV-6285 support multiple environments in GTM
    private static final String PROP_GOOGLE_TAG_MANAGER_AUTH_TOKEN = "fdstore.google.tagmanager.authtoken";
    private static final String PROP_GOOGLE_TAG_MANAGER_PREVIEW_ID = "fdstore.google.tagmanager.previewid";

    // APPDEV-2062 CS Hours.
    private static final String CUST_SERV_HOURS_DAYS = "fdstore.cust_serv_days";
    private static final String CUST_SERV_HOURS_HOURS = "fdstore.cust_serv_hours";

    private static final String CUST_SERV_HOURS_DAYS_FDX = "fdstore.fdx.cust_serv_days";
    private static final String CUST_SERV_HOURS_HOURS_FDX = "fdstore.fdx.cust_serv_hours";

    private static final String PROP_AVAILABILITY_DAYS_IN_PAST_TO_LOOKUP = "fdstore.availdays.past.lookup";

    // [APPDEV-1993] voiceshot
    private static final String PROP_VS_USERNAME = "fdstore.voiceshot.username";
    private static final String PROP_VS_PASSWORD = "fdstore.voiceshot.password";
    private static final String PROP_VS_URL = "fdstore.voiceshot.url";
    private final static String PROP_ENABLE_AIRCLIC = "fdstore.enable.airclic";

    private final static String PROP_FD_GIVEX_WEB_USER = "fdstore.givex.web.user";
    private final static String PROP_FD_GIVEX_WEB_USER_PASSWD = "fdstore.givex.web.user.passwd";
    private final static String PROP_GIVEX_WEB_SERVER_URL = "fdstore.givex.web.url";

    private final static String PROP_DAY_OF_WEEK_FOR_COS_MON_AUTHS = "fdstore.cos.montue.auth.dayofweek";
    private final static String PROP_PRODUCTPROMO_INFO_HOME = "fdstore.ProductPromotionManager.home";

    private final static String FACEBOOK_APP_KEY = "facebook.app.key";

    // APPDEV-2155 Standing Order UI
    private static final String PROP_EMAIL_STANDING_ORDER_CS = "fdstore.email.standingordercs";
    private static final String PROP_PHONE_STANDING_ORDER_CS = "fdstore.phone.standingordercs";

    private final static String PROP_WEBPURIFY_URL = "fdstore.webpurify.url";

    private final static String PROP_WEBPURIFY_KEY = "fdstore.webpurify.key";

    private final static String CLOUD_SPONGE_DOMAIN_KEY = "cloudsponge.domain.key";
    private final static String CLOUD_SPONGE_ADDRESS_IMPORTS = "cloudsponge.usage";

    private final static String PENDING_ORDER_POPUP_ENABLED = "fdstore.pendingOrderPopup.enabled";
    private final static String PENDING_ORDER_POPUP_MOCKED = "fdstore.pendingOrderPopup.mocked";
    private final static String PROP_DDPP_ENABLED = "fdstore.ddpp.enabled";

    private static final String PROP_TRANS_EMAIL_ENABLED = "fdstore.trans.email.enabled";
    private final static String PROP_TRANS_EMAIL_TYPES = "fdstore.trans.email.types";

    private final static String PROP_MODIFY_ORDER_TOTAL_MAX = "fdstore.modify.order.maxtotal";

    private final static String PROP_LIGHT_SIGNUP_ENABLED = "fdstore.signuplight.enabled";
    private final static String PROP_AJAX_SIGNUP_ENABLED = "fdstore.ajaxsignup.enabled";
    private final static String PROP_LIGHT_SIGNUP_ANTS_ENABLED = "fdstore.signuplight.ants.enabled";

    // APPDEV-2394 Coremetrics Implementation
    private final static String PROP_COREMETRICS_ENABLED = "fdstore.coremetrics.enabled";
    private final static String PROP_COREMETRICS_CLIENT_ID = "fdstore.coremetrics.clientid";
    private final static String PROP_COREMETRICS_DATA_COLLECTION_METHOD = "fdstore.coremetrics.datacollectionmethod";
    private final static String PROP_COREMETRICS_DATA_COLLECTION_DOMAIN = "fdstore.coremetrics.datacollectiondomain";
    private final static String PROP_COREMETRICS_COOKIE_DOMAIN = "fdstore.coremetrics.cookiedomain";
    private final static String PROP_COREMETRICS_CATID_DIRS = "fdstore.coremetrics.catid.dirs";
    private final static String PROP_COREMETRICS_CATID_BLOG = "fdstore.coremetrics.catid.blog";
    private final static String PROP_COREMETRICS_CATID_OTHERPAGE = "fdstore.coremetrics.catid.otherpage";
    private final static String PROP_COREMETRICS_FTP_URL = "fdstore.coremetrics.ftp.url";
    private final static String PROP_COREMETRICS_FTP_PASSWORD = "fdstore.coremetrics.ftp.password";
    private final static String PROP_COREMETRICS_FTP_SECURE = "fdstore.coremetrics.ftp.secure";
    private final static String PROP_COREMETRICS_FTP_SFTP_PORT = "fdstore.coremetrics.ftp.sftpport";

    // APPDEV-2446 Bazaarvoice Implementation
    private final static String PROP_BAZAARVOICE_FTP_URL = "fdstore.bazaarvoice.ftp.url";
    private final static String PROP_BAZAARVOICE_FTP_USERNAME = "fdstore.bazaarvoice.ftp.username";
    private final static String PROP_BAZAARVOICE_FTP_PASSWORD = "fdstore.bazaarvoice.ftp.password";
    private final static String PROP_BAZAARVOICE_BVAPI_URL = "fdstore.bazaarvoice.bvapi.url";
    private final static String PROP_BAZAARVOICE_ENABLED = "fdstore.bazaarvoice.enabled";
    private final static String PROP_BAZAARVOICE_DOWNLOAD_FEED_SOURCEPATH = "fdstore.bazaarvoice.download.feed.sourcepath";
    private final static String PROP_BAZAARVOICE_DOWNLOAD_FEED_FILE = "fdstore.bazaarvoice.download.feed.file";
    private final static String PROP_BAZAARVOICE_DOWNLOAD_FEED_TARGETPATH = "fdstore.bazaarvoice.download.feed.target.path";
    private final static String PROP_BAZAARVOICE_EXCLUDED_DEPTS = "fdstore.bazaarvoice.excluded.depts";

    private final static String ALLOW_DISCOUNTS_ON_PREMIUM_SLOT = "fdstore.allow.discount.premium.slot";

    private final static String DLV_PASS_NEW_TC_DATE = "fdstore.dlvpass.newtc.date";
    private final static String SAME_DAY_MEDIA_AFTER_CUTOFF = "fdstore.sameday.aftercutoffmedia.duration";

    private final static String PROP_GIFTCARD_DONATION_ENABLED = "fdstore.giftcard.donation.enabled";

    /* APPDEV-2475 DP T&C */
    private final static String PROP_DLV_PASS_NEW_TC_VIEWLIMIT = "fdstore.dlvpass.newtc.viewlimit";
    private final static String PROP_AUTO_APPLY_DONATION_GC = "fdstore.auto.apply.donation.gc";

    private final static String PROP_LEAD_TIME_OASAD_OFF = "fdstore.leadtime.turnoff";

    private final static String PROP_ENABLE_DELIVERYCENTER = "fdstore.delivery.center.enabled";
    private final static String PROP_HRPROMO_CODES = "promo.hrpromocodes";
    private final static String PROP_SHOW_AUTO_LATE_CREDIT_BUTTON = "fdstore.crm.show.autolatedelivery.button";
    private final static String NUMBER_OF_TOP_FAVOURITES = "fdstore.search.favourites.top.number";
    private final static String FAVOURITES_NUMBER_SWITCH = "fdstore.search.favourites.switch";
    private final static String NUMBER_OF_PRESPICKS_TOP_FAVOURITES = "fdstore.prespicks.favourites.top.number";
    private final static String NUMBER_OF_BROWSE_TOP_FAVOURITES = "fdstore.browse.favourites.top.number";

    /* APPDEV-2723 */
    private final static String PROP_COHORT_MATCHER = "fdstore.cohortmatcher";

    /* [APPDEV-2525]-ECoupons */
    private final static String PROP_FDCOUPONMGR_HOME = "fdstore.fdCouponManager.home";

    // APPDEV-2448 IP Sniff
    private final static String PROP_IP_LOCATOR_ENABLED = "fdstore.iplocator.enabled";
    private final static String PROP_IP_LOCATOR_CLIENT_ID = "fdstore.iplocator.clientid";
    private final static String PROP_IP_LOCATOR_URL = "fdstore.iplocator.url";
    private final static String PROP_IP_LOCATORV4_URL = "fdstore.iplocatorv4.url";
    private final static String PROP_IP_LOCATORV4_ENABLED = "fdstore.iplocatorv4.enabled";
    private final static String PROP_IP_LOCATOR_TIMEOUT = "fdstore.iplocator.timeout";
    private final static String PROP_IP_LOCATOR_ROLLOUT_PERCENT = "fdstore.iplocator.rolloutpercent";
    private final static String PROP_IP_LOCATOR_EVENT_LOG_ENABLED = "fdstore.iplocator.eventlogenabled";

    private final static String PROP_HOST_URL = "fdstore.host.url";
    private final static String PROP_SOCIAL_BUTTONS_ENABLED = "fdstore.socialbuttons.enabled";

    private final static String PROP_USQ_LEGAL_WARNING = "fdstore.store.usq_legal_warning";

    public final static String PRODUCT_RATING_REFRESH_PERIOD = "fdstore.productrating.refreshperiod";

    public final static String PRODUCT_RATING_RELOAD = "fdstore.productrating.reload";

    private final static String PROP_ALCOHOLFILTERING_ENABLED = "fdstore.alcoholfiltering.enabled";
    private final static String CHECK_LOCAL_INVENTORY_ENABLED = "fdstore.check.localinventory.enabled";

    private final static String PROP_PAYMENTECH_GATEWAY_ENABLED = "fdstore.paymentech.enabled";

    // APPDEV-2817 Link to DeliveryPass category from top nav of Delivery Info page
    private final static String SHOW_DLVPASS_LINK_ON_DELINFO = "fdstore.dlvinfo.showdlvpasslink";

    private final static String PROP_SOY_DEBUG = "soy.debug";

    private final static String PROP_EH_CACHE_ENABLED = "ehcache.enabled";
    private final static String PROP_EH_CACHE_MANAGEMENT_ENABLED = "ehcache.management.enabled";
    private final static String PROP_FD_TC_ENABLED = "terms.condition.enabled";

    private final static String PROP_QS_IGNORE_PARTIAL_ROLLOUT = "quickshop.ignorePartialRollout";
    private final static String PROP_QS_ENABLED = "quickshop.enabled";

    private final static String PROP_ASSIGNED_CUSTOMER_PARAMS_QUERY_ID = "fdstore.assignedCustomerParams.query.id";

    // APPDEV-3100 2013 Wine Transition
    private final static String PROP_WINE_ASSID = "fdstore.wine.assid";

    private final static String PROP_SUFFOLK_ZIPS = "prop.suffolk.zips";

    private final static String PROP_BROWSE_ROLLOUT_REDIRECT_ENABLED = "fdstore.browse.rollout.redirectenabled";
    private final static String PROP_MEDIA_RENDER_UTILS_REALLY_CLOSE = "fdstore.media.render.utils.reallyclose";
    private final static String PROP_MEDIA_RENDER_UTILS_SOURCE_ENCODING = "fdstore.media.render.utils.sourceencoding";

    // optimizing
    private final static String PROP_OPT_SOCIAL_FOOTER_STATIC = "fdstore.optimize.social.footer.static";

    // New ATP Strategy to handle flip
    private static final String PROP_STANDING_ORDER_IGNOREATPFAILURE = "fdstore.standingorder.ignoreatpfailure";

    // template redesign
    private static final String PROP_MAX_XSELL_PRODS = "fdstore.xsell.max";
    private static final String PROP_BROWSE_PAGESIZE = "fdstore.browse.pagesize";
    private static final String PROP_PRES_PICKS_PAGESIZE = "fdstore.prespicks.pagesize";
    private static final String PROP_STAFF_PICKS_PAGESIZE = "fdstore.staffpicks.pagesize";
    // private static final String PROP_STAFF_PICKS_PICKID = "fdstore.staffpicks.picksid"; Commenting as part of APPDEV 5988
    private static final String PROP_STAFF_PICKS_FEATLIMIT = "fdstore.staffpicks.featlimit";
    private static final String PROP_NEWPRODUCTS_PAGESIZE = "fdstore.newproducts.pagesize";
    private static final String PROP_ECOUPON_PAGESIZE = "fdstore.ecoupon.pagesize";
    private static final String PROP_SEARCH_PAGESIZE = "fdstore.search.pagesize";
    private static final String PROP_QUICKSHOP_PAGESIZE = "fdstore.quickshop.pagesize";
    private static final String PROP_BROWSE_POPULAR_CATEGORIES_MAX = "fdstore.browse.popularcategories.max";

    // Ignore Foodily P3P Policy(similar to facebook and google)
    private static final String PROP_3RDPARTY_P3PENABLED = "fdstore.3rdparty.p3penabled";

    // sms
    private final static String PROP_ST_PROVIDER_URL = "fdstore.ST.provider.url";
    private final static String PROP_ST_USERNAME = "fdstore.ST.username";
    private final static String PROP_ST_PASSWORD = "fdstore.ST.password";
    private final static String PROP_ST_FDX_USERNAME = "fdstore.ST.Fdxusername";
    private final static String PROP_ST_FDX_PASSWORD = "fdstore.ST.Fdxpassword";

    private final static String PROP_ST_CONNECTION_TIMEOUT_PERIOD = "fdstore.ST.connection.timeout.period";
    private static final String PROP_ST_READ_TIMEOUT_PERIOD = "fdstore.ST.read.timeout.period";
    private static final String PROP_SMS_OVERLAY_FLAG = "fdstore.SMS.overlay.flag";

    private static final String PROP_SEARCH_CAROUSEL_PRODUCT_LIMIT = "fdstore.search.carousel.product.limit";

    private static final String PROP_PRESIDENT_PICK_PAGING_ENABLED = "fdstore.prespicks.paging.enabled";
    private static final String PROP_ALL_DEALS_CACHE_ENABLED = "fdstore.all_deals_cache.enabled";

    private static final String PROP_SITEMAP_ENABLED = "fdstore.sitemap.enabled";
    private static final String PROP_SITEMAP_PASSWORDS = "fdstore.sitemap.passwords";

    // Early AM timeSlot
    private static final String PROP_EARLY_AM_HOUR = "fdstore.early.window.hour";
    private static final String PROP_EARLY_AM_MINUTE = "fdstore.early.window.minute";

    /* Alt. Pickup convenience APPDEV-3623 */
    private static final String PROP_DEPOT_CACHE_REFRESH = "fdstore.depot.cache.refresh";

    private static final String PROP_LOG_RECOMMENDTATIONS_RESULTS = "fdstore.log.recommendations_results";

    // [APPDEV-3438] Unit Price Display
    private final static String UNIT_PRICE_DISPLAY_ENABLED = "fdstore.unitprice.enabled";

    // Recaptcha
    private final static String PROP_RECAPTCHA_PUBLIC_KEY = "fdstore.recaptcha.publickey";
    private final static String PROP_RECAPTCHA_PRIVATE_KEY = "fdstore.recaptcha.privatekey";

    private static final String PROP_LOGISTICS_CONNECTION_TIMEOUT = "fdstore.logistics.conn.timeout";
    private static final String PROP_LOGISTICS_CONN_READ_TIMEOUT = "fdstore.logistics.conn.read.timeout";
    private static final String PROP_LOGISTICS_CONNECTION_REQUEST_TIMEOUT = "fdstore.logistics.conn.request.timeout";

    private static final String PROP_LOGISTICS_CONNECTION_POOL = "fdstore.logistics.conn.pool";

    // Max Invalid Login counts for Recaptcha
    private final static String PROP_MAX_INVALID_LOGIN_ATTEMPT = "fdstore.max.invalid.login.count";

    // Limiting the quicksearch results in mobile
    private final static String QUICKSHOP_ALL_ITEMS_MAX = "fdstore.quickshop.max.results";

    // Limiting orderHistory to show only last 13 months
    private static final String PROP_ORDER_HISTORY_FROM_IN_MONTHS = "fdstore.orderhistory.from.months";

    private static final String PROP_QUICKSHOP_PAST_ORDERS_VISIBLE_MENU_ITEMS_COUNT = "fdstore.quickshop.past_orders.menuitem.count";

    private static final String PROP_TIP_RANGE_CONFIG = "fdstore.tip.range.config";
    private static final String PROP_LOGISTICS_API_URL = "fdstore.logisticsapi.url";
    private static final String PROP_FDCOMMERCE_API_URL = "fdstore.fdcommerceapi.url";

    private static final String PROP_PAYPAL_API_URL = "fdstore.paypalapi.url";
    private static final String PROP_MASTERPASSS_API_URL = "fdstore.masterpassapi.url";
    
    private static final String PROP_ORBITAL_API_URL = "fdstore.orbitalapi.url";
    private static final String PROP_OMS_API_URL = "fdstore.omsapi.url";
    private static final String PROP_LOGISTICS_COMPANY_CODE = "fdstore.logistics.companycode";
    private static final String PROP_PRODUCTFAMILY = "fdstore.productfamily";

    private static final String PROP_GIVEXGATEWAY_ENDPOINT = "fdstore.givexgateway.url";

    // APPDEV - 4159 - Creating of variables for maximum size of columns in promo table

    private static final String PROMO_OLDCOLUMN_MAX_LIMIT = "fdstore.promopublish.oldValuecolumn.maxsize";
    private static final String PROMO_NEWCOLUMN_MAX_LIMIT = "fdstore.promopublish.newValuecolumn.maxsize";

    private static final String CATEGORY_TOP_ITEM_CACHE_SIZE = "fdstore.category.top.item.cache.size";
    private static final String CATEGORY_TOP_ITEM_CACHE_MAXIMAL_SIZE = "fdstore.category.top.item.cache.maximal.size";

    private static final String PROP_PRODUCT_SAMPLES_MAX_BUY_PRODUCTS_LIMIT = "fdstore.product.samples.max.buy.products.limit";
    private static final String PROP_PRODUCT_SAMPLES_MAX_BUY_QUANTITY_LIMIT = "fdstore.product.samples.max.buy.quantity.limit";
    private static final String PROP_FEED_PUBLISH_URL = "fdstore.feed.publish.url";
    private static final String PROP_FEED_PUBLISH_FROM_BKOFFICE = "fdstore.feed.publish.bkoffice";
    private static final String PROP_FEED_PUBLISH_BKOFFICE_URL = "fdstore.feed.publish.bkoffice.url";
    private static final String CTCAPACITY_ELIGIBLE_PROFILES = "fdstore.ctcapacity.eligibleprofiles";
    private static final String PROP_CORE_NON_CORE_GLOBAL_NAV_SWITCH_ENABLED = "fdstore.corenoncore.globalnav.switch.enabled";
    private static final String PROP_MIDDLETIER_PROVIDER_URL = "fdstore.middletier.providerURL";
    private static final String PROP_HOLIDAY_MEAL_BUNDLE_CATEGORY_ID = "fdstore.hmb.category.id";
    // APPDEV - 4354 - ATP in overlay
    private static final String PROP_ATP_AVAILABILTY_MOCK_ENABLED = "fdstore.atp.availability.mock.enabled";
    private static final String PROP_EWALLET_ENCRYPTION_KEY = "fdstore.ewallet.encryption.key";
    private static final String PROP_EWALLET_ENCRYPTION_ALGORITHM = "fdstore.ewallet.encryption.algorithm";

    private static final String PROP_SOCIAL_ONEALL_SUBDOMAIN = "fdstore.social.oneall.subdomain";
    private static final String PROP_SOCIAL_ONEALL_PUBLICKEY = "fdstore.social.oneall.publickey";
    private static final String PROP_SOCIAL_ONEALL_PRIVATEKEY = "fdstore.social.oneall.privatekey";
    private static final String PROP_SOCIAL_ONEALL_POSTURL = "fdstore.social.oneall.posturl";
    private static final String PROP_SOCIAL_LOGIN_ENABLED = "fdstore.social.login.enabled";
    private static final String PROP_DEPLOYMENT_LOCAL = "fdstore.deployment.local";
    private static final String PROP_DEVELOPER_DISABLE_AVAIL_LOOKUP = "fdstore.developer.disableAvailabilityLookup";
    private static final String PROP_FDX_SMS_ORDER_CONFIRMATION = "fdstore.sms.order.confirmation";
    private static final String PROP_FDX_SMS_ORDER_MODIFICATION = "fdstore.sms.order.modification";
    private static final String PROP_FDX_SMS_ORDER_CANCEL = "fdstore.sms.order.cancel";

    private static final String PROP_FDX_APP_APPLE_URL = "fdstore.fdx.app.url.apple";

    // fdx new locationbar
    private final static String PROP_FDX_LOCATIONBAR = "fdstore.fdxlocationbar.enabled";
    private final static String PROP_FDX_LOCATIONBAR_FDXTAB = "fdstore.fdxlocationbar.fdxtab.enabled";

    private final static String PROP_ETIPPING_ENABLED = "fdstore.etipping.enabled";

    /* APPDEV-4216 Refer a Friend- Extole Integration */

    public final static String PROP_FDEXTOLEMGR_HOME = "freshdirect.fdstore.FDExtoleManager";
    public final static String PROP_EXTOLE_SFTP_HOST = "extole.sftp.host";
    public final static String PROP_EXTOLE_SFTP_USERNAME = "extole.sftp.username";
    public final static String PROP_EXTOLE_SFTP_FILE_DOWNLOADER_REMOTE_WORKDIR = "extole.sftp.remotedirectory";
    public final static String PROP_EXTOLE_SFTP_FILE_DOWNLOADER_LOCAL_WORKDIR = "extole.sftp.localdirectory";

    public static final String PROP_FEATURE_ROLLOUT_NEW_SO = "feature.rollout.new.standindorder.enabled";

    public static final String PROP_SCHEME_HTTPS = "scheme.https";
    public static final String PROP_EXTOLE_BASE_URL = "prop.base.url";

    public static final String PROP_EXTOLE_ENDPOINT_CREATE_CONVERSION = "extole.endpoint.create.conversion";
    public static final String PROP_EXTOLE_ENDPOINT_APPROVE_CONVERSION = "extole.endpoint.approve.conversion";

    public static final String PROP_EXTOLE_API_KEY = "extole.api.key";
    public static final String PROP_EXTOLE_API_SECRET = "extole.api.secret";
    public static final String PROP_EXTOLE_BASE_FILE_NAME = "extole.base.file.name";
    public static final String PROP_EXTOLE_SFTP_PRIVATE_KEY = "extole.sftp.private.key";
    public static final String PROP_CRM_REFERRAL_HISTORY_PAGE_ENABLED = "crm.referral.history.page.enabled";
    public static final String PROP_EXTOLE_RAF_ENABLED = "fdstore.extole.raf.enabled";
    public static final String PROP_EXTOLE_MICROSITE_URL = "fdstore.extole.microsite.url";
    public static final String PROP_EXTOLE_MICROSITE_SUB_URL = "fdstore.extole.microsite.sub.url";
    public static final String PROP_EXTOLE_MICROSITE_GLOBAL_NAV_URL = "fdstore.extole.microsite.global.nav.url";
    public final static String PROP_FD_BRAND_PRODUCTS_AD_HOME = "freshdirect.fdstore.fdBrandProductsAdManager";
    public final static String PROP_HL_PRODUCTS_COUNT = "fdstore.hlproductscount";

    /**
     * URL to the CMS Admin REST interface
     */
    private static final String PROP_CMS_ADMIN_REST_URL = "cms.adminapp.path";
    /**
     * URL to the CMS Admin UI
     */
    private static final String PROP_CMS_ADMIN_UI_URL = "cms.adminapp.ui.url";

    // [APPDEV-4650]
    public static final String PROP_ENABLE_XC_FOR_CRM_AGENTS = "crm.xc.enabled";

    // Avalara Tax
    private final static String PROP_AVALARA_TAX_ENABLED = "fdstore.tax.avalara.enabled";
    private final static String PROP_AVALARA_BASE_URL = "fdstore.tax.avalara.base.url";
    private final static String PROP_AVALARA_LICENSE_KEY = "fdstore.tax.avalara.license.key";
    private final static String PROP_AVALARA_ACCOUNT_NUMBER = "fdstore.tax.avalara.account.number";
    private static final String PROP_AVALARA_COMPANY_CODE = "fdstore.tax.avalara.company.code";

    private final static String PROP_AVALARA_CRON_THREAD_COUNT = "fdstore.tax.avalara.cron.threads.count";

    private final static String PROP_HOOK_LOGIC_BLACKHOLE_ENABLE = "fdstore.hooklogic.blackhole.enabled";
    private final static String PROP_HOOK_LOGIC_CATEGORY_ENABLE = "fdstore.hooklogic.category.enabled";
    private final static String PROP_HOOK_LOGIC_ORDER_FEED_MINS = "fdstore.hooklogic.orderfeed.minutes";
    private final static String PROP_HOOK_LOGIC_ALLOW_OWN_ROWS = "fdstore.hooklogic.allow.own.rows";
    private final static String PROP_HOOK_LOGIC_CATEGORY_EXCLUDE_DEP_CAT_IDS = "fdstore.hooklogic.excluded.dep.category.ids";

    // PayPal
    private static final String PROP_EWALLET_PAYPAL_ENV_PROP_NAME = "paypal.environment";
    private static final String PROP_EWALLET_PAYPAL_ENABLED = "fdstore.ewallet.paypal.enabled";
    private static final String PROP_EWALLET_MASTERPASS_ENABLED = "fdstore.ewallet.masterpass.enabled";

    public static final String PROP_EDT_EST_TIMESLOT_CONVERSION_ENABLED = "fdstore.edt.est.timeslot.conversion.enabled";// It should be 'true' only for FDX.

    // erpsy linking
    public static final String PROP_ERPSYLINK_STOREFRONT_FD = "fdstore.erpsylink.storefront.fd";
    public static final String PROP_ERPSYLINK_STOREFRONT_FDX = "fdstore.erpsylink.storefront.fdx";

    private static final String PROP_ADDRESS_MISMATCH_ENABLED = "fdstore.address.mismatch.enabled";

    private static final String PROP_BROWSE_AGGREGATED_CATEGORIES = "fdstore.browse.aggregated.categories";

    private static final String PROP_GROUP_SCALE_PERF_IMPROVE_ENABLED = "fdstore.group.scale.perf.improve.enabled";

    private static final String PROP_PRICE_CONFIG_CONVERSION_LIMIT = "fdstore.price.config.conversion.limit";
    private static final String PROP_PRICE_CONFIG_DEPARTMENTS = "fdstore.price.config.deparments.cms.ids";

    private final static String PROP_DEFAULT_FDX_PLANTID = "fdstore.default.fdx.plantid";
    private final static String PROP_DEFAULT_FDX_DISTRIBUTION_CHANNEL = "fdstore.default.fdx.distribution.channel";
    private final static String PROP_DEFAULT_FDX_DISTRIBUTION_CHANNEL_PARENT = "fdstore.default.fdx.distribution.channel.parent";
    private final static String PROP_DEFAULT_FDX_SALESORG = "fdstore.default.fdx.salesorg";
    private final static String PROP_DEFAULT_FDX_SALESORG_PARENT = "fdstore.default.fdx.salesorg.parent";
    private final static String PROP_DEFAULT_FD_PLANTID = "fdstore.default.fd.plantid";
    private final static String PROP_DEFAULT_FD_DISTRIBUTION_CHANNEL = "fdstore.default.fd.distribution.channel";
    private final static String PROP_DEFAULT_FD_SALESORG = "fdstore.default.fd.salesorg";

    private static final String PROP_PRODUCT_FEED_GENERATION_DEVELOPER_MODE_ENABLED = "fdstore.dev.productfeedgeneration.enabled";

    // UNBXD integration
    private static final String PROP_UNBXD_API_KEY = "fdstore.unbxd.apikey";
    private static final String PROP_UNBXD_SITE_KEY = "fdstore.unbxd.sitekey";
    private static final String PROP_UNBXD_COS_SITE_KEY = "fdstore.unbxd.cos.sitekey";
    private static final String PROP_UNBXD_BASE_URL = "fdstore.unbxd.baseurl";
    private static final String PROP_UNBXD_FALLBACK_ON_ERROR = "fdstore.unbxd.fallback.on.error";
    private static final String PROP_UNBXD_TRACKING_BASE_URL = "fdstore.unbxd.tracking.base_url";

    // APPDEV -5516 :Cart Carousel - Grand Giving Donation Technology
    private static final String PROP_DONATION_PRODUCT_SAMPLES_ENABLED = "fdstore.donation.product.samples.enabled";
    private static final String PROP_DONATION_PRODUCT_SAMPLES_ID = "fdstore.donation.product.samples.productId";

    private static final String PROP_VIEWCART_PAGE_NEW_CUSTOMER_CAROUSEL_SITE_FEATURES = "fdstore.viewcart.new.customer.carousel.site.features";
    private static final String PROP_VIEWCART_PAGE_CURRENT_CUSTOMER_CAROUSEL_SITE_FEATURES = "fdstore.viewcart.current.customer.carousel.site.features";
    private static final String PROP_CHECKOUT_PAGE_NEW_CUSTOMER_CAROUSEL_SITE_FEATURES = "fdstore.checkout.new.customer.carousel.site.features";
    private static final String PROP_CHECKOUT_PAGE_CURRENT_CUSTOMER_CAROUSEL_SITE_FEATURES = "fdstore.checkout.current.customer.carousel.site.features";
    private static final String PROP_CHECKOUT_PAGE_COS_CUSTOMER_DISPLAY_DELIVERY_FEE_HEADER = "fdstore.checkout.cos.customer.display.delivery.fee";

    // APPDEV-5893
    private static final String PROP_USER_CART_SAVE_INTERVAL = "fdstore.user.cart.save.interval";

    private static final String PROP_HOMEPAGE_REDESIGN_CURRENTCOS_USER_CONTAINER_CONTENT_KEY = "fdstore.homepageredesign.currentCOSUserModuleContainerContentKey";
    private static final String PROP_HOMEPAGE_REDESIGN_NEWCOS_USER_CONTAINER_CONTENT_KEY = "fdstore.homepageredesign.newCOSUserModuleContainerContentKey";

    private static final String PROP_HOMEPAGE_REDESIGN_CURRENT_USER_CONTAINER_CONTENT_KEY = "fdstore.homepageredesign.currentUserModuleContainerContentKey";
    private static final String PROP_HOMEPAGE_REDESIGN_NEW_USER_CONTAINER_CONTENT_KEY = "fdstore.homepageredesign.newUserModuleContainerContentKey";

    private static final String PROP_HOMEPAGE_REDESIGN_MODULE_PRODUCT_LIMIT_MAX = "fdstore.homepageredesign.moduleProductLimitMax";
    private static final String PROP_HOMEPAGE_REDESIGN_PRESPICKS_CATEGORY_ID = "fdstore.homepageredesign.presPicksCategoryId";
    private static final String PROP_HOMEPAGE_REDESIGN_STAFFPICKS_CATEGORY_ID = "fdstore.homepageredesign.staffPicksCategoryId";

    // APPDEV-5927
    private static final String PROP_PLANT1300_PRICE_INDICATOR = "fdstore.plant1300.price.indicator";
    private static final String PROP_PLANT1310_PRICE_INDICATOR = "fdstore.plant1310.price.indicator";
    private static final String PROP_PLANTWDC_PRICE_INDICATOR = "fdstore.plantwdc.price.indicator";

    private static final String PROP_MAT_SALESORG__EXPORT_PICKPLANT_VALIDATION_ENABLED = "fdstore.salesarea.exp.pickplant.val.enabled";
    private static final String PROP_EXTRA_LOG_FOR_LOGIN_FAILS_ENABLED = "fdstore.extralog.login.fail.enabled";

    private static final String PROP_SF_2_0_ENABLED = "fdstore.storefront_2_0.enabled";
    private static final String PROP_MEALBUNDLE_CARTONVIEW_ENABLED = "fdstore.mealbundle_cartonview.enabled";

    private final static String PROP_QS_TOP_ITEMS_PERF_OPT_ENABLED = "fdstore.quickshop.topitems.perf.optimize.enabled";
    private final static String PROP_ZIP_CHECK_OVER_LAY_ENABLED = "fdstore.zipcheck.overlay.enabled";
    /* APPDEV-5781 */
    private final static String PROP_OBSOLETE_MERGECARTPAGE_ENABLED = "fdstore.obsolete.mergecartpage.enabled";

    private static final String PROP_DFP_ENABLED = "fdstore.dfp.enabled";
    private static final String PROP_DFP_ID = "fdstore.dfp.id";

	private final static String PROP_CLUSTER_NAME = "fdsystem.cluster.name";
	private final static String PROP_NODE_NAME = "fdsystem.node.name";

    private final static String PROP_PRODUCT_CACHE_OPTIMIZATION_ENABLED = "fdstore.product.cache.optimization.enabled";

    private final static String PROP_REQUEST_SCHEME_FOR_REDIRECT_URL = "fdstore.request.scheme.redirecturl";
	private static final String PROP_PAYMENT_VERIFICATION_ENABLED = "payment.verification.enabled";

    /* APPDEV 6174
     * IBM SilverPopup urls, tokens*/
    private final static String IBM_ACCESSTOKEN_URL = "fdstore.ibm.accesstoken.url";
    private final static String IBM_PUSHNOTIFICATION_URL = "fdstore.ibm.pushnotification.url";
    private final static String IBM_WATSON_EMAIL_CAMPAIGN_URL = "fdstore.ibm.watsonemailcampaign.url";
    //https://api3.ibmmarketingcould.com/XMLAPI
    private final static String IBM_CLIENT_ID = "fdstore.ibm.client.id";
    private final static String IBM_CAMPAIGN_CLIENT_ID = "fdstore.ibm.campaign.client.id";
    private final static String IBM_CLIENT_SECRET = "fdstore.ibm.client.secret";
    private final static String IBM_CAMPAIGN_CLIENT_SECRET = "fdstore.ibm.campaign.client.secret";
    private final static String IBM_REFRESH_TOKEN = "fdstore.ibm.refresh.token";
    private final static String IBM_CAMPAIGN_REFRESH_TOKEN = "fdstore.ibm.campaign.refresh.token";
	private static final String PAYMENT_TLSSHA_ENABLED = "fdstore.payment.tls.sha.enabled";
    private static final String PROP_REFRESHZONE_ENABLED = "fdstore.refresh.zone.enabled";

    private static final String GLOBAL_SF2_0_ENABLED = "fdstore_sf20_global_enabled";

    private static final String PROP_DEBIT_SWITCH_NOTICE_ENABLED = "fdstore.debitCardSwitchNotice.enabled";

    private static final String PROP_LOG_AKAMAI_HEADER_ENABLED = "fdstore.akamai.edgescape.header.logging.enabled";

    public final static long TEN_DAYS_IN_MILLIS = 1000 * 60 * 60 * 24 * 10;

    //APPDEV 6442 FDC Transition

    public final static String PROP_FDC_TRANSITION_LOOK_AHEAD_DAYS = "fdstore.fdctransition.lookAheadDays";
    public final static String CUSTOMER_SERVICE_CONTACT	= "default.customer.service.contact";
 	public final static String CHEFSTABLE_CONTACT_NUMBER = "chefstable.contact.number";
 	public final static String FOODKICK_SERVICE_CONTACT	=	"foodkick.service.contact";
 	public final static String PENNSYLVANIA_SERVICE_CONTACT	= "pennsylvania.service.contact";

 	//Setting the product as free as SAP cannot get the value of a product as zero
 	public final static String PROP_ENABLE_FREE_PRODUCT = "fdstore.enable.free.product";
 	//appdev-6259
 	public final static String PROP_ENABLE_WEBSITE_MOBILE_SAME_NUTRITION_SOY = "enable.website.mobile.same.nutrition.soy";
 	
 	//appdev-6184
 	public final static String PROP_ENABLE_FDX_DISTINCT_AVAILABILITY = "enable.fdx.distinct.availability";
 	 
 	public final static String PROP_ENABLE_REPEAT_WARMUP = "enable.repeat.warmup";

	// OAuth2 Expirations
	public final static String DEFAULT_CODE_EXPIRATION = "fdstore.oauth2.defaultCodeExpirationInSec";
	public final static String DEFAULT_TOKEN_EXPIRATION = "fdstore.oauth2.defaultTokenExpirationInSec";
	public final static String DEFAULT_REFRESH_TOKEN_EXPIRATION = "fdstore.oauth2.defaultRefreshTokenExpirationInSec";
	// OAuth2 client data
	public final static String OAUTH2_CLIENT_IDS = "fdstore.oauth2.clientIds";
	public final static String OAUTH2_CLIENT_SECRETS = "fdstore.oauth2.clientSecrets";
	public final static String OAUTH2_CLIENT_REDIRECT_URIS = "fdstore.oauth2.clientRedirectUris";
	
	public final static String PROP_FDC_NEW_BACKIN_USE_FD_ENABLED = "fdstore.new.backinstock.fdc.use.fd.enabled";
	
	public final static String PROP_FD_DP_FREE_TRIAL_OPTIN_FEATURE_ENABLED = "fdstore.fd.dp.freetrial.optin.feature.enabled";
	

 	static {
        defaults.put(PROP_PROVIDER_URL, "t3://localhost:7001");
        defaults.put(PROP_INIT_CTX_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        defaults.put(PROP_CRM_GEOCODELINK,
                "http://www.geocode.com/EZLI/LoginServlet?uname=ECM0001468&pword=Lzxjb&servID=USA_Geo_002&formAction=GetInputFormServlet&submit=Login&cmd=li");
        defaults.put(PROP_CRM_CASE_LIST_LENGTH, "100");
        defaults.put(PROP_CRM_CASE_HISTORY_LIST_LENGTH, "25");
        defaults.put(PROP_CRM_DISABLE_TIME_WINDOW_CHECK, "false");
        defaults.put(PROP_FDSTORE_WEB_CAREERLINK, "http://jobs-freshdirect.icims.com");
        defaults.put(PROP_FDFACTORY_HOME, "freshdirect.fdstore.Factory");
        defaults.put(PROP_SAPGATEWAY_HOME, "freshdirect.sap.Gateway");
        defaults.put(PROP_KANAGATEWAY_HOME, "freshdirect.kana.Gateway");
        defaults.put(PROP_COMPLAINTMGR_HOME, "freshdirect.erp.ComplaintManager");
        defaults.put(PROP_CALLCENTERMGR_HOME, "freshdirect.fdstore.CallCenterManager");
        defaults.put(PROP_FDCUSTMGR_HOME, "freshdirect.fdstore.CustomerManager");
        defaults.put(PROP_EWALLET_HOME, "freshdirect.fdstore.ErpEWallet");
        defaults.put(PROP_EWALLET_SERVICE_HOME, "freshdirect.fdstore.EWalletService");
        defaults.put(PROP_MPService_HOME, "freshdirect.fdstore.MPService");
        defaults.put(PROP_PPService_HOME, "freshdirect.fdstore.PPService");

        defaults.put(PROP_EWALLET_ENCRYPTION_ALGORITHM, "AES");
        defaults.put(PROP_EWALLET_ENCRYPTION_KEY,
                "Z8LSq0wWwB5v+6YJzurcP463H3F12iZh74fDj4S74oUH4EONkiKb2FmiWUbtFh97GG/c/lbDE47mvw6j94yXxKHOpoqu6zpLKMKPcOoSppcVWb2q34qENBJkudXUh4MWcreondLmLL2UyydtFKuU9Sa5VgY/CzGaVGJABK2ZR94=");
        defaults.put(PROP_FDPROMOTIONMGR_HOME, "freshdirect.fdstore.PromotionManager");
        defaults.put(PROP_FDPROMOTIONMGR_NEW_HOME, "freshdirect.fdstore.PromotionManagerNew");

        defaults.put(PROP_DLVMANAGER_HOME, "freshdirect.delivery.DeliveryManager");
        defaults.put(PROP_RULESMANAGER_HOME, "freshdirect.fdstore.RulesManager");
        defaults.put(PROP_AIRCLICMANAGER_HOME, "freshdirect.delivery.AirclicManager");
        defaults.put(PROP_DLVRESTRICTION_MGR_HOME, "freshdirect.delivery.DeliveryRestrictionManager");
        defaults.put(PROP_FDCUSTOMER_HOME, "freshdirect.fdstore.Customer");
        defaults.put(PROP_EMAIL_PROMOTION, "PromotionNotification@freshdirect.com");
        defaults.put(PROP_ERPCUSTOMER_HOME, "freshdirect.erp.Customer");
        defaults.put(PROP_ERPCUSTOMERINFO_HOME, "freshdirect.erp.CustomerInfo");
        defaults.put(PROP_CONTFACTORY_HOME, "freshdirect.content.ContentFactory");
        defaults.put(PROP_FDORDER_HOME, "freshdirect.fdstore.Order");
        // checks for all special characters
        defaults.put(PROP_DLV_INSTRUCTION_SPECIAL_CHAR, "[~ | \\` | \" | \\! | \\@ | \\# | \\ $ | \\% | \\^ | \\& | \\* | \\( | \\) | \\- | _ | + | \\= | \\n | \\r]");

        defaults.put(PROP_PREVIEW_MODE, "false");
        defaults.put(PROP_FLUSH_OSCACHE, "false");
        defaults.put(PROP_ANNOTATION_MODE, "false");
        defaults.put(PROP_ANNOTATION_ERPSY, "http://ems1.nyc1.freshdirect.com:8000/ERPSAdmin");
        defaults.put(PROP_CALLCENTER_PW, "");

        defaults.put(PROP_CUSTOMER_SERVICE_EMAIL, "service@freshdirect.com");
        defaults.put(PROP_EMAIL_PRODUCT, "products@freshdirect.com");
        defaults.put(PROP_EMAIL_FEEDBACK, "feedback@freshdirect.com");
        defaults.put(PROP_EMAIL_CHEFSTABLE, "chefstable@freshdirect.com");
        defaults.put(PROP_EMAIL_VENDING, "vendinginfo@freshdirect.com");

        // FDX emails
        defaults.put(PROP_EMAIL_FDX_ANNOUNCE, "announcements@foodkick.com");
        defaults.put(PROP_EMAIL_FDX_ORDER, "order@foodkick.com");
        defaults.put(PROP_EMAIL_FDX_ACTSERVICE, "accountservices@foodkick.com");
        defaults.put(PROP_EMAIL_FDX_SIDEKICKS, "sidekicks@foodkick.com");
        defaults.put(PROP_EMAIL_FDX_PRODUCT_REQUEST, "FK_Merchants@freshdirect.onmicrosoft.com");

        defaults.put(PROP_CONTENTMANAGER_HOME, "freshdirect.content.ContentManager");
        defaults.put(PROP_HOLIDAY_LOOKAHEAD_DAYS, "21");
        defaults.put(PROP_AD_SERVER_ENABLED, "false");
        defaults.put(PROP_DLVFEE_TIER_ENABLED, "false");

        // cut off time
        defaults.put(CUT_OFF_TIME_SUN, "0-20");
        defaults.put(CUT_OFF_TIME_MON, "0-20");
        defaults.put(CUT_OFF_TIME_TUES, "0-20");
        defaults.put(CUT_OFF_TIME_WED, "0-20");
        defaults.put(CUT_OFF_TIME_THUS, "0-20");
        defaults.put(CUT_OFF_TIME_FRI, "0-17");
        defaults.put(CUT_OFF_TIME_SAT, "0-20");

        // customer service hours
        defaults.put(CLICK_TO_CALL, "false");
        defaults.put(CUST_SERV_HOURS_SUN, "07:30-11:59");
        defaults.put(CUST_SERV_HOURS_MON, "06:30-11:59");
        defaults.put(CUST_SERV_HOURS_TUES, "06:30-11:59");
        defaults.put(CUST_SERV_HOURS_WED, "06:30-11:59");
        defaults.put(CUST_SERV_HOURS_THUS, "06:30-11:59");
        defaults.put(CUST_SERV_HOURS_FRI, "06:30-23:00");
        defaults.put(CUST_SERV_HOURS_SAT, "07:30-22:00");

        defaults.put(PROP_AD_SERVER_PROFILE_ATTRIBS, "");

        defaults.put(PROP_AD_SERVER_USES_DEFERRED_IMAGE_LOADING, "false");

        defaults.put(PROP_PAYMENT_METHOD_MANAGER_HOME, "freshdirect.paymentmethod.PaymentMethodManager");

        defaults.put(PROP_RESTRICTED_PAYMENT_METHOD_HOME, "freshdirect.payment.RestrictedPaymentMethod");

        defaults.put(PROP_REFRESHSECS_PRODUCTINFO, "600");
        defaults.put(PROP_REFRESHSECS_GROUPSCALE, "600");
        defaults.put(PROP_REFRESHSECS_UPCPRODUCTINFO, "900");
        defaults.put(PROP_REFRESHSECS_ZONE, "600");
        defaults.put(PROP_REFRESHSECS_PRODUCT, "7200");
        defaults.put(PROP_PRODUCT_CACHE_SIZE, "60000");//Changed from 45000 to 60000 on Feb 4th Patch
        defaults.put(PROP_ZONE_CACHE_SIZE, "10000");
        defaults.put(PROP_GRP_CACHE_SIZE, "10000");
        defaults.put(PROP_MEDIACONTENT_CACHE_SIZE, "0");
        // mktadmin
        defaults.put(MKT_ADMIN_FILE_UPLOAD_SIZE, "2000");

        /*
         * defaults.put(PROP_CUTOFF_WARN + Calendar.MONDAY, "23"); defaults.put(PROP_CUTOFF_WARN + Calendar.TUESDAY, "23"); defaults.put(PROP_CUTOFF_WARN + Calendar.WEDNESDAY,
         * "23"); defaults.put(PROP_CUTOFF_WARN + Calendar.THURSDAY, "23"); defaults.put(PROP_CUTOFF_WARN + Calendar.FRIDAY, "19"); defaults.put(PROP_CUTOFF_WARN +
         * Calendar.SATURDAY, "20"); defaults.put(PROP_CUTOFF_WARN + Calendar.SUNDAY, "23");
         */
        defaults.put(PROP_CUTOFF_WARN, "1");
        defaults.put(PROP_CUTOFF_DEFAULT_ZONE_CODE, "922");

        defaults.put(PROP_PRERESERVE_HOURS, "1");

        defaults.put(PROP_DLV_PROMO_EXP_DATE, "2004-01-01");

        defaults.put(PROP_PRELOAD_STORE, "true");
        defaults.put(PROP_PRELOAD_PROMOTIONS, "true");
        defaults.put(PROP_PRELOAD_GROUPS, "true");
        // No default for PROP_WARMUP_CLASS
        defaults.put(PROP_PRELOAD_NEWNESS, "true");
        defaults.put(PROP_PRELOAD_REINTRODUCED, "true");
        defaults.put(PROP_PRELOAD_SMARTSTORE, "true");
        defaults.put(PROP_PRELOAD_AUTOCOMPLETIONS, "true");

        defaults.put(PROP_CMS_MEDIABASEURL, "http://www.freshdirect.com");

        defaults.put(PROP_SUMMERSERVICE, "false");

        defaults.put(PROP_EXTERNAL_FRAUD_CHECK_PM, "true");

        defaults.put(PROP_MAX_REFERRALS, "500");
        defaults.put(PROP_NUM_DAYS_MAX_REFERRALS, "1"); // max 500 referrals for 1 day
        defaults.put(PROP_FDREFERRALMGR_HOME, "freshdirect.fdstore.ReferralManager");

        defaults.put(PROP_USE_MULTIPLE_PROMOTIONS, "false");

        defaults.put(PROP_DATA_COLLECTION_ENABLED, "false");

        defaults.put(PROP_PRODUCT_RECOMMEND_ENABLED, "false");

        defaults.put(PROP_PRODUCT_RECOMMEND_CHECK_CACHE_ENABLED, "true");

        defaults.put(BSGS_SIGNUP_URL, "/product.jsp?productId=mkt_fd_dlvpss_b10g5f&catId=gro_gear_dlvpass");
        defaults.put(UNLIMITED_SIGNUP_URL, "/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
        defaults.put(UNLIMITED_PROMOTIONAL_SIGNUP_URL, "/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
        defaults.put(UNLIMITED_AMAZON_PRIME_SIGNUP_URL, "/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
        defaults.put(CRM_BSGS_SIGNUP_URL, "/order/product.jsp?productId=mkt_fd_dlvpss_b10g5f&catId=gro_gear_dlvpass");
        defaults.put(CRM_UNLIMITED_SIGNUP_URL, "/order/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
        defaults.put(CRM_UNLIMITED_PROMOTIONAL_SIGNUP_URL, "/order/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
        defaults.put(CRM_UNLIMITED_AMAZON_PRIME_SIGNUP_URL, "/order/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
        defaults.put(CRM_CREDIT_ISSUE_BCC, "");

        defaults.put(BSGS_PROFILE_POSFIX, "BuySomeGetSome");
        defaults.put(UNLIMITED_PROFILE_POSFIX, "Unlimited");
        defaults.put(UNLIMITED_PROMOTIONAL_PROFILE, "Unlimited");
        defaults.put(UNLIMITED_AMAZON_PRIME_PROFILE, "Unlimited");
        defaults.put(DLV_PASS_AUTORENEWAL_DEFAULT, "MKT0072630");

        defaults.put(DLV_PASS_PROMOTION_PREFIX, "FDDELIVERS");
        defaults.put(DLV_PASS_MAX_PURCHASE_LIMIT, "1");

        defaults.put(PROP_CRM_ORDER_PRC_LIMIT, "250");
        defaults.put(RFL_PRG_PAGINATION_SIZE, "10");

        defaults.put(SKU_AVAILABILITY_REFRESH_PERIOD, "30");

        defaults.put(PROP_RETPRG_CREATECASE, "false");

        defaults.put(CCL_ENABLED, "false");
        defaults.put(CCL_AJAX_DEBUG_CLIENT, "false");
        defaults.put(CCL_AJAX_DEBUG_JSONRPC, "false");
        defaults.put(CCL_AJAX_DEBUG_FACADE, "false");
        defaults.put(CCL_AJAX_DEBUG_FACADE_EXCEPTION, "");

        defaults.put(DYF_ENABLED, "false");

        defaults.put(DYF_FREQBOUGHT_TOPN, "10");
        defaults.put(DYF_FREQBOUGHT_TOPPERCENT, "10.0");

        defaults.put(DYF_STRATEGY_CACHE_ENTRIES, "1000");

        defaults.put(SMARTSTORE_NEWPRODUCTS_DAYS, "30");

        defaults.put(SMARTSTORE_CACHE_DATA_SOURCES, "true");
        defaults.put(SMARTSTORE_CACHE_DATA_SOURCES_SIZE, "150");
        defaults.put(SMARTSTORE_CACHE_ONLINE_FACTORS, "true");
        defaults.put(SMARTSTORE_CMS_RECOMM_REFRESH_RATE, Long.toString(Long.MAX_VALUE / 60 / 1000));

        defaults.put(SMARTSTORE_OFFLINE_REC_RECENT_DAYS, "365");

        defaults.put(SMARTSTORE_OFFLINE_REC_MAX_AGE, "5");

        defaults.put(SMARTSTORE_OFFLINE_REC_THREAD_COUNT, "5");

        defaults.put(SMARTSTORE_OFFLINE_REC_WINDOW_LENGTH, "300");

        defaults.put(SMARTSTORE_PERSONAL_SCORES_CACHE_ENTRIES, "500");
        defaults.put(SMARTSTORE_PERSONAL_SCORES_CAHCE_TIMEOUT, "" + (30 * 60));

        defaults.put(DISTRIBUTION_SAMPLES_DIR, "");

        defaults.put(PROP_INVENOTRY_REFRESH_PERIOD, "10");
        defaults.put(PROP_ATTRIBUTES_REFRESH_PERIOD, "10");
        defaults.put(PROP_NUTRITION_REFRESH_PERIOD, "10");
        defaults.put(PROP_PROMOTION_RT_REFRESH_PERIOD, "1200");

        defaults.put(ADVANCE_ORDER_START, "2004-01-01");
        defaults.put(ADVANCE_ORDER_END, "2004-01-02");
        // Advance Order Gap
        defaults.put(ADVANCE_ORDER_GAP, "false");
        defaults.put(ADVANCE_ORDER_NEW_START, "2004-01-01");
        defaults.put(ADVANCE_ORDER_NEW_END, "2004-01-02");

        // mrkting admin
        defaults.put(MRKTING_ADMIN_URL, "http://adm.freshdirect.com/MrktAdmin");
        // DCPD ALIAS Handling.
        defaults.put(DCPD_ALIAS_HANDLING_ENABLED, "true");

        // produce rating enabled
        defaults.put(PRODUCE_RATING_ENABLED, "true");
        // produce rating sku prefixes
        defaults.put(PRODUCE_RATING_PREFIXES, "FRU,VEG,YEL,WIN,SEA");

        // freshness guaranteed on/off switch
        defaults.put(FRESHNESS_GUARANTEED_ENABLED, "true");
        // freshness guaranteed on/off sku prefix specific
        defaults.put(FRESHNESS_GUARANTEED_PREFIXES, "FRU,VEG,MEA,SEA,DEL,CHE,DAI,HMR,VAR,CAT,BAK,PAS,YEL");

        defaults.put(HP_LETTER_MEDIA_PATH1, "/media/editorial/home/letter/hp_letter_new.html");
        defaults.put(HP_LETTER_MEDIA_PATH2, "/media/editorial/home/letter/hp_letter_customer.html");
        defaults.put(HPLETTER_MEDIA_ENABLED, "true");

        // deals
        defaults.put(DEALS_SKU_PREFIX, "GRO,FRO,SPE,DAI,HBA");
        defaults.put(DEALS_LOWER_LIMIT, "1");
        defaults.put(DEALS_UPPER_LIMIT, "95");
        defaults.put(BURST_LOWER_LIMIT, "10");
        defaults.put(BURST_UPPER_LIMIT, "75");
        defaults.put(MAX_FEATURED_DEALS_FOR_PAGE, "5");
        defaults.put(MAX_FEATURED_DEALS_PER_LINE, "5");
        defaults.put(MIN_FEATURED_DEALS_FOR_PAGE, "3");

        defaults.put(TEMP_DIR, "/tmp");

        defaults.put(PROP_UPS_BLACKHOLE_ENABLED, "false");

        defaults.put(SMART_SEARCH_ENABLED, "false");
        defaults.put(DID_YOU_MEAN_RATIO, "5.0");
        defaults.put(DID_YOU_MEAN_THRESHOLD, "0.6");
        defaults.put(DID_YOU_MEAN_MAXHITS, "20");
        defaults.put(PRIMARY_HOME_KEYWORDS_ENABLED, "false");
        defaults.put(SEARCH_RECURSE_PARENT_ATTRIBUTES_ENABLED, "false");
        defaults.put(SEARCH_GLOBALNAV_AUTOCOMPLETE_ENABLE, "false");

        defaults.put(PROP_COOLINFO_REFRESH_PERIOD, "10");

        defaults.put(IMPRESSION_LOGGING, "false");

        defaults.put(PROP_SURVEYDEF_CACHE_SIZE, "25");
        defaults.put(PROP_REFRESHSECS_SURVEYDEF, "600");
        defaults.put(PROP_FDSURVEY_HOME, "freshdirect.fdstore.FDSurvey");

        defaults.put(SMART_SAVINGS_FEATURE_ENABLED, "true");

        defaults.put(PROP_LIMITED_AVAILABILITY_ENABLED, "true");

        // CORS
        defaults.put(CORS_DOMAIN, "*");

        // What's Good Department
        defaults.put(PROP_FDWHATSGOOD_ENABLED, "false");
        defaults.put(PROP_FDWHATSGOOD_PEAKPRODUCE_ENABLED, "true");
        defaults.put(PROP_FDWHATSGOOD_BBLOCK_ENABLED, "false");
        defaults.put(PROP_FDWHATSGOOD_ROWS, "");
        defaults.put(PROP_SUFFOLK_ZIPS, "11701,11702,11703,11704,11706,11717,11720,11724,11725,11726,11729,11731,11735,11740,11743,11746,11747,11749,11751,11755,11757,11767,11780,11787,11788,11795,11798");

        defaults.put(PROP_FDWHATSGOOD_DEBUG_ENABLED, "false");

        // iphone
        defaults.put(PROP_MEDIA_IPHONE_TEMPLATE_PATH, "media/mobile/iphone/");
        defaults.put(IPHONE_EMAIL_SUBJECT, "FreshDirect SmartPhone Shopping");

        // Gift Card
        defaults.put(PROP_GC_ENABLED, "false");
        defaults.put(PROP_GC_LANDING_URL, "/gift_card/purchase/landing.jsp");
        defaults.put(PROP_GIFT_CARD_SKU_CODE, "MKT0074896");
        defaults.put(PROP_GC_TEMPLATE_BASE_URL, "http://www.freshdirect.com");
        defaults.put(PROP_MEDIA_GIFT_CARD_TEMPLATE_PATH, "/media/editorial/giftcards/");
        defaults.put(PROP_GIFT_CARD_RECIPIENT_MAX, "10");
        defaults.put(PROP_GC_MIN_AMOUNT, "20");
        defaults.put(PROP_GC_MAX_AMOUNT, "5000");
        defaults.put(PROP_GC_DEPTID, "GC_testDept");
        defaults.put(PROP_GC_CATID, "GC_testCat");
        defaults.put(PROP_GC_PRODNAME, "GC_testProd");
        defaults.put(GIVEX_BLACK_HOLE_ENABLED, "false");

        defaults.put(GIVEX_SECURITY_FIX_ENABLED, "true");
        defaults.put(PROP_GC_OOO, "false");

        // Robin Hood
        defaults.put(ROBIN_HOOD_ENABLED, "false");
        defaults.put(ROBIN_HOOD_LANDING_URL, "/robin_hood/landing.jsp");
        defaults.put(PROP_ROBIN_HOOD_SKU_CODE, "MKT0075239");
        defaults.put(ROBIN_HOOD_STATUS, "BUY");

        // Meat Deals and EDLP
        defaults.put(DEPT_MEAT_DEALS, "true");
        defaults.put(DEPT_EDLP, "true");
        defaults.put(DEPT_MEAT_DEALS_CATID, "");
        defaults.put(DEPT_EDLP_CATID, "");

        // Mobile
        defaults.put(MOBILE_IPHONE_LANDING_ENABLED, "false");
        defaults.put(MOBILE_IPHONE_LANDING_PAGE, "/media/mobile/supported.ftl");
        defaults.put(MOBILE_ANDROID_LANDING_ENABLED, "false");
        defaults.put(MOBILE_ANDROID_LANDING_PAGE, "/media/mobile/supported.ftl");

        defaults.put(PROP_GC_NSM_AUTHSKIP_SECS, "600");
        defaults.put(PROP_GC_NSM_FREQ_SECS, "600");
        defaults.put(PROP_ZONE_PRICING_ENABLED, "true");
        defaults.put(PROP_ZONE_PRICING_AD_ENABLED, "true");
        defaults.put(PROP_ZONE_PICKUP_ZIPCODE, "07076");
        defaults.put(PROP_FDX_ZONE_ZIPCODE, "10036");

        // Window Steering
        defaults.put(WINDOW_STEERING_PROMOTION_PREFIX, "WS_");

        // Standing Orders
        defaults.put(SO_GLOBAL_ENABLER, "true");
        defaults.put(SO_OVERLAP_WINDOWS, "true");

        // Client Codes
        defaults.put(CLIENT_CODES_GLOBAL_ENABLER, "false");

        // new products revamp
        defaults.put(PROP_NEWPRODUCTS_DEPTID, "newproduct");
        defaults.put(PROP_NEWPRODUCTS_CATID, "newproduct_cat");
        defaults.put(PROP_NEWPRODUCTS_CATID_FDX, "newproduct_cat_fdx");
        defaults.put(PROP_NEWPRODUCTS_GROUPS, "<W2,W2-W4,M1-M2,M2-M3,>M3");

        // comma separated list of faq section ids from CMS.
        defaults.put(
                PROP_FAQ_SECTIONS,
                "acct_info,cos,chef_table,delivery_feedback,gen_feedback,home_delivery,inside,order_change,order_today,payment,prblem_my_order,promotion,req_feedback,security,shopping,signing_up,vending,website_technical,what_we_do");

        // CRM Help Links to confluence.
        defaults.put(PROP_CRM_HELP_LINK_ADDR_VALIDATION, "http://home.freshdirect.com/confluence/display/CRM/Address+Validation");
        defaults.put(PROP_CRM_HELP_LINK_GIFT_CARD, "http://home.freshdirect.com/confluence/display/CRM/Gift+Card");
        defaults.put(PROP_CRM_HELP_LINK_MAIN_HELP, "http://home.freshdirect.com/confluence/display/CRM/Home");
        defaults.put(PROP_CRM_HELP_LINK_CUST_PROFILE, "http://home.freshdirect.com/confluence/display/CRM/Customer+Profiles");
        defaults.put(PROP_CRM_HELP_LINK_TIMESLOT, "http://home.freshdirect.com/confluence/display/CRM/Timeslot+Request");
        defaults.put(PROP_CRM_HELP_LINK_FD_UPDATES, "http://home.freshdirect.com/confluence/display/CRM/FD+Updates");
        defaults.put(PROP_CRM_HELP_LINK_PROMOTIONS, "http://home.freshdirect.com/confluence/display/CRM/Promotion");
        defaults.put(PROP_CRM_HELP_LINK_CASE_MEDIA, "http://home.freshdirect.com/confluence/display/CRM/Media");
        defaults.put(PROP_CRM_HELP_LINK_CASE_MORE_ISSUES, "http://home.freshdirect.com/confluence/display/CRM/More+than+one+issue+with+order");
        defaults.put(PROP_CRM_HELP_LINK_CASE_CUST_TONE, "http://home.freshdirect.com/confluence/display/CRM/Customer%27s+tone");
        defaults.put(PROP_CRM_HELP_LINK_CASE_RESOL_SATISFY, "http://home.freshdirect.com/confluence/display/CRM/Resolution+satisfactory");
        defaults.put(PROP_CRM_HELP_LINK_CASE_RESOLV_FIRST, "http://home.freshdirect.com/confluence/display/CRM/Resolved+on+first+contact");
        defaults.put(PROP_CRM_HELP_LINK_CASE_FIRST_CONTACT, "http://home.freshdirect.com/confluence/display/CRM/First+contact+for+issue");

        // Email Opt-Down (APPDEV-662)
        defaults.put(PROP_EMAIL_OPTDOWN_ENABLED, "false");

        // APPDEV-1091 Promo Publish URL
        defaults.put(PROMO_PUBLISH_URL_KEY, "/promo_publish");
        defaults.put(PROMO_PUBLISH_NODE_TYPE, "master");
        // APPDEV-659
        defaults.put(PROMO_VALID_RT_STATUSES, "LIVE");

        defaults.put(PROP_REDEMPTION_CNT_REFRESH_PERIOD, "300"); // every 5 mins
        defaults.put(PROP_REDEMPTION_SERVER_COUNT, "5");
        defaults.put(PROP_PROMO_LINE_ITEM_EMAIL, "true");

        // Delivery Pass at Checkout (APPDEV-664)
        defaults.put(PROP_DP_CART_ENABLED, "false");

        defaults.put(PROP_4MM_REFRESH_INTERVAL, "5");

        // Brand media replacement (APPDEV-1308)
        defaults.put(PROP_BRAND_MEDIA_IDS, "none");
        defaults.put(PROP_WS_DISCOUNT_AMOUNT_LIST, "2");

        // [APPDEV-1283] Wine Revamp
        defaults.put(WINE_SHOW_RATINGS_KEY, Boolean.toString(true));

        defaults.put(ADVERTISING_TILE_ENABLED, Boolean.toString(false));

        defaults.put(PROP_CRM_MENU_ROLES_REFRESH_PERIOD, "3600"); // every 60 mins
        defaults.put(PROP_CRM_LDAP_USERS_REFRESH_PERIOD, "3600"); // every 60 mins
        defaults.put(PROP_CRM_AGENTS_CACHE_REFRESH_PERIOD, "1800");// every 30 mins

        defaults.put(PROP_CRM_LDAP_ACCESS_HOST_NAME_PRIMARY, "t3://127.0.0.1:7001");
        defaults.put(PROP_CRM_CC_DETAILS_LOOKUP_LIMIT, "10"); // 10
        defaults.put(PROP_CRM_CC_SECURITY_EMAIL_ENABLED, Boolean.toString(false));
        defaults.put(PROP_CRM_CC_SECURITY_EMAIL, "infosec@freshdirect.com"); // infosec@freshdirect.com
        defaults.put(PROP_CRM_CC_SECURITY_EMAIL_SUBJECT, "ALERT: CC/EC decryptions above threshold");
        defaults.put(PROP_CRM_CC_DETAILS_ACCESS_KEY, "9ac7ec230e0e4513578f309d6d3579ad");
        defaults.put(PROP_CRM_FORGOT_LDAP_PASSWORD_URL, "http://myaccount.freshdirect.com/cp/login/Login.aspx");
        defaults.put(PROP_CRM_SECURITY_SKIP_FILE_TYPES, "css,js,jspf,gif,jpg,xls,ico,txt");
        defaults.put(PROP_CRM_SECURITY_SKIP_FOLDERS, "");
        defaults.put(PROP_CLICK2CALL_CALL_BACL_URL, "https://cim1.custserv.ca/system/web/view/live/templates/freshdirect/callbackICMFrame.html");

        // APPDEV-1215 Sustainable Seafood
        defaults.put(PROP_SEAFOODSUSTAIN_ENABLED, "false");
        defaults.put(PROP_FEED_PUBLISH_URL, "http://localhost:7001/crm/feed_publish");
        defaults.put(PROP_FEED_PUBLISH_FROM_BKOFFICE, "true");
        defaults.put(PROP_FEED_PUBLISH_BKOFFICE_URL, "http://bsl.stdev14.nj01/FDService/service/v0/createfeed");
        // SEM Project (APPDEV-1598)
        defaults.put(PROP_SEM_PIXELS, "");
        defaults.put(PROP_SEM_CONFIGS, "");
        defaults.put(PROP_SEM_REFRESH_PERIOD, "5"); // MINUTE * this value

        defaults.put(PROP_DUMPGROUPEXPORT_ENABLED, "false");
        defaults.put(PROP_VALIDATIONGROUPEXPORT_ENABLED, "true");
        defaults.put(PROP_VALIDATIONGROUPEXPORTSAPINPUT_ENABLED, "true");
        defaults.put(PROP_GROUPSCALE_ENABLED, "true");

        defaults.put(PROP_CT_TIMESLOT_LABEL, "Chef's Table Delivery Times");
        defaults.put(PROP_PROMO_TIMESLOT_LABEL, "Discount Delivery Times");
        defaults.put(PROP_ALCOHOL_TIMESLOT_LABEL, "Alcohol Delivery Restriction");
        defaults.put(PROP_ECOFRIENDLY_TIMESLOT_LABEL, "Eco-Friendly");
        defaults.put(PROP_MINORDER_TIMESLOT_LABEL, "Premium Timeslot");
        defaults.put(PROP_BUILDINGFAVS_TIMESLOT_LABEL, "Delivery Helper");
        // APPDEV-3107 SAP upgrade customer messaging
        defaults.put(PROP_TIMESLOT_MSGING, "d=01/01/2000:m=no_media.html");

        defaults.put(PROP_STANDING_ORDER_REPORT_TO_EMAIL, "applicationdevelopment@freshdirect.com");
        defaults.put(PROP_STANDING_ORDER_REPORT_EMAIL_SUBJECT, "Standing Orders Cron Report: ");
        defaults.put(PROP_TIMESLOT_GRID, "true");
        defaults.put(WS_PROMOTION_PRODUCTION_MODE, "true");

        defaults.put(PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_ENABLED, "true");
        defaults.put(PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_TO, "applicationdevelopment@freshdirect.com");
        defaults.put(PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_CC, "applicationdevelopment@freshdirect.com");
        defaults.put(PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_SUBJECT, "Auto Upload Customer Restriction List's Status");
        defaults.put(PROP_MKTADMIN_AUTOUPLOAD_URL, "/mktAdmin_autoUpload");
        defaults.put(PROP_TRUCK_REFRESH_URL, "http://localhost:7001/TrnAdmin/truckRefresh.do");
        defaults.put(PROP_EMP_REFRESH_URL, "http://localhost:7001/TrnAdmin/empRefresh.do");
        defaults.put(PROP_MKTADMIN_USER_NAME, "qaadmin");
        defaults.put(PROP_MKTADMIN_PASSWORD, "password01");

        defaults.put(PROP_PAYMENT_METHOD_VERIFICATION_ENABLED, "false");
        defaults.put(PROP_PAYMENT_METHOD_VERIFICATION_LIMIT, "5");

        defaults.put(PROP_ORDER_HISTORY_QUERY_ID, "3");

        defaults.put(PROP_TWO_MONTH_TRIAL_PASS_SKU, "MKT0072335");
        defaults.put(PROP_TWO_MONTH_TRIAL_PASS_PRICE, "$69.00");
        defaults.put(PROP_ONE_YEAR_DELIVERY_PASS_SKU, "mkt_dpass_auto14mo");
        defaults.put(PROP_SIX_MONTH_DELIVERY_PASS_SKU, "mkt_dpss_6m_autrnwl");
        defaults.put(PROP_ONE_MONTH_DELIVERY_PASS_SKU, "mkt_dpss_onemonth");

        defaults.put(BUILDVER_ENABLE, "true");

        defaults.put(SMARTSTORE_QUICKBUY_NEWALERT_ENABLED, "false");

        defaults.put(IPHONE_SEARCH_FILTER_DISCONTINUED_ONLY, "false");

        defaults.put(SESSION_LOGGING_ENABLED, "true");
        defaults.put(REAL_TIME_EVENT_ANALYSIS, "false");
        defaults.put(EVENT_KB_SOURCE, "local");
        defaults.put(RULES_REPOSITORY, "Events.drl");
        defaults.put(DEBUG_EVENT_ANALYSIS, "false");
        defaults.put(PROP_COUNTRY_INFO_REFRESH_INTERVAL, "5");

        // APPDEV-6030 Google Tag Manager
        defaults.put(PROP_GOOGLE_TAG_MANAGER_KEY, "GTM-TMBSNJH");//GTM-KFMTML");
        // APPDEV-6285 support multiple environments in GTM
        defaults.put(PROP_GOOGLE_TAG_MANAGER_AUTH_TOKEN, "");
        defaults.put(PROP_GOOGLE_TAG_MANAGER_PREVIEW_ID, "");

        // APPDEV-2072 google analytics key
        defaults.put(PROP_GOOGLE_ANALYTICS_KEY, "UA-20535945-18"); // default to an empty string
        defaults.put(PROP_GOOGLE_ANALYTICS_DOMAIN, ".freshdirect.com");
        // APPDEV-3522 Google Analytics: Upgrade to Universal Analytics: Updates for Page Load Times
        defaults.put(PROP_GOOGLE_ANALYTICS_UNIVERSAL, "true");

        defaults.put(PROP_AVAILABILITY_DAYS_IN_PAST_TO_LOOKUP, "7");
        defaults.put(CUST_SERV_HOURS_DAYS, "Monday through Sunday");
        defaults.put(CUST_SERV_HOURS_HOURS, "6:30 AM to 12 AM");
        defaults.put(CUST_SERV_HOURS_DAYS_FDX, "Monday through Sunday");
        defaults.put(CUST_SERV_HOURS_HOURS_FDX, "6:30 AM to 12 AM");

        // [APPDEV-1993] voiceshot
        defaults.put(PROP_VS_USERNAME, "mtrachtenberg");
        defaults.put(PROP_VS_PASSWORD, "whitshell");
        defaults.put(PROP_VS_URL, "http://api.voiceshot.com/ivrapi.asp");
        defaults.put(PROP_ENABLE_AIRCLIC, "false");
        defaults.put(PROP_FD_GIVEX_WEB_USER, "freshdirect");
        defaults.put(PROP_DAY_OF_WEEK_FOR_COS_MON_AUTHS, "6");
        defaults.put(PROP_FD_GIVEX_WEB_USER_PASSWD, "fd8848admin");
        defaults.put(PROP_GIVEX_WEB_SERVER_URL, "https://dev-wwws.givex.com/portal/login.py?_LANGUAGE_:en");
        // APPDEV-2155 Standing Order UI
        defaults.put(PROP_EMAIL_STANDING_ORDER_CS, "StandingOrders@FreshDirect.com");
        defaults.put(PROP_PHONE_STANDING_ORDER_CS, "");
        defaults.put(PROP_PRODUCTPROMO_INFO_HOME, "freshdirect.fdstore.ProductPromotionInfoManager");
        // APPDEV-2252 Standing Order - Order Minimum Failure Threshold
        /*
         * defaults.put(PROP_STANDING_ORDER_SOFT_LIMIT, "50.0"); defaults.put(PROP_STANDING_ORDER_HARD_LIMIT, "50.0");
         */

        defaults.put(PROP_WEBPURIFY_URL, "https://api1.webpurify.com/services/rest/?api_key=%API_KEY%&method=%METHOD%&format=json&lang=%LANG%&callback=%CALLBACK%&text=%TEXT%");
        defaults.put(PROP_WEBPURIFY_KEY, "1c7d26c10e564e6629234974ff556aa0");

        defaults.put(FACEBOOK_APP_KEY, "331367173579737");

        defaults.put(CLOUD_SPONGE_DOMAIN_KEY, "K3D6375BGJRXBSJR8456");
        defaults.put(CLOUD_SPONGE_ADDRESS_IMPORTS, "true");
        defaults.put(PROP_DDPP_ENABLED, "true");
        defaults.put(PROP_TRANS_EMAIL_ENABLED, "false");
        defaults.put(PROP_TRANS_EMAIL_TYPES, "ORDER_SUBMIT,ORDER_MODIFY,FINAL_INCOICE,ORDER_CANCEL,CHARGE_ORDER,CREDIT_CONFIRM,FORGOT_PASSWD,"
                + "AUTH_FAILURE,CUST_REMINDER,RECIPE_MAIL,TELL_A_FRIEND,TELLAFRIEND_RECIPE,TELLAFRIEND_PRODUCT,GC_ORDER_SUBMIT, "
                + " GC_BULK_ORDER_SUBMIT,GC_AUTH_FAILURE,GC_CANCEL_PURCHASER,GC_CANCEL_RECIPENT,GC_BALANCE_TRANSFER,"
                + "GC_CREDIT_CONFIRM,RH_ORDER_CONFIRM,GC_RECIPENT_ORDER,SMART_STORE_DYF");

        defaults.put(PROP_MODIFY_ORDER_TOTAL_MAX, "1500");
        defaults.put(PENDING_ORDER_POPUP_ENABLED, "true");
        defaults.put(PENDING_ORDER_POPUP_MOCKED, "false");

        defaults.put(PROP_LIGHT_SIGNUP_ENABLED, "true");
        defaults.put(PROP_AJAX_SIGNUP_ENABLED, "true");
        defaults.put(PROP_LIGHT_SIGNUP_ANTS_ENABLED, "true");

        // defaults for test environment
        defaults.put(PROP_COREMETRICS_ENABLED, "true");
        defaults.put(PROP_COREMETRICS_CLIENT_ID, "60391309");
        defaults.put(PROP_COREMETRICS_DATA_COLLECTION_METHOD, "false");
        defaults.put(PROP_COREMETRICS_DATA_COLLECTION_DOMAIN, "testdata.coremetrics.com");
        defaults.put(PROP_COREMETRICS_COOKIE_DOMAIN, "freshdirect.com");
        defaults.put(PROP_COREMETRICS_CATID_DIRS, "help,your_account,quickshop,checkout,gift_card,robin_hood,about,survey,login,site_access,registration,wine,4mm");
        defaults.put(PROP_COREMETRICS_CATID_BLOG, "blog");
        defaults.put(PROP_COREMETRICS_CATID_OTHERPAGE, "other_page");
        defaults.put(PROP_COREMETRICS_FTP_URL, "ftp.coremetrics.com");
        defaults.put(PROP_COREMETRICS_FTP_PASSWORD, "Delivers2u!");
        defaults.put(PROP_COREMETRICS_FTP_SECURE, "false");
        defaults.put(PROP_COREMETRICS_FTP_SFTP_PORT, "998");

        defaults.put(PROP_BAZAARVOICE_FTP_USERNAME, "freshdirect");
        defaults.put(PROP_BAZAARVOICE_FTP_URL, "ftp.bazaarvoice.com");
        defaults.put(PROP_BAZAARVOICE_FTP_PASSWORD, "1hOGyoFPLYo");
        defaults.put(PROP_BAZAARVOICE_BVAPI_URL, "//display-stg.ugc.bazaarvoice.com/static/freshdirect/bvapi.js");
        defaults.put(PROP_BAZAARVOICE_ENABLED, "false");
        defaults.put(PROP_BAZAARVOICE_DOWNLOAD_FEED_SOURCEPATH, "feeds");
        defaults.put(PROP_BAZAARVOICE_DOWNLOAD_FEED_FILE, "bv_freshdirect_standard_client_feed.xml.gz");
        defaults.put(PROP_BAZAARVOICE_DOWNLOAD_FEED_TARGETPATH, "/opt/fdlog/bv_feed/");
        defaults.put(PROP_BAZAARVOICE_EXCLUDED_DEPTS, "veg,fru,sea,mea,usq");

        defaults.put(ALLOW_DISCOUNTS_ON_PREMIUM_SLOT, "false");
        defaults.put(DLV_PASS_NEW_TC_DATE, "2012-05-09");
        defaults.put(SAME_DAY_MEDIA_AFTER_CUTOFF, "30");

        defaults.put(PROP_GIFTCARD_DONATION_ENABLED, "true");

        defaults.put(PROP_DLV_PASS_NEW_TC_VIEWLIMIT, "3");
        defaults.put(PROP_AUTO_APPLY_DONATION_GC, "true");
        defaults.put(PROP_LEAD_TIME_OASAD_OFF, "false");

        defaults.put(PROP_ENABLE_DELIVERYCENTER, "true");
        defaults.put(PROP_HRPROMO_CODES, "EMPLOYEE,FDREWARD1,FDREWARD2,FDREWARD3,FDREWARD4");
        defaults.put(PROP_SHOW_AUTO_LATE_CREDIT_BUTTON, "true");

        defaults.put(NUMBER_OF_TOP_FAVOURITES, "3");
        defaults.put(FAVOURITES_NUMBER_SWITCH, "true");
        defaults.put(NUMBER_OF_PRESPICKS_TOP_FAVOURITES, "5");
        defaults.put(NUMBER_OF_BROWSE_TOP_FAVOURITES, "3");
        defaults.put(PROP_COHORT_MATCHER, "");
        defaults.put(PROP_USQ_LEGAL_WARNING, "true");
        defaults.put(PRODUCT_RATING_REFRESH_PERIOD, "12");
        defaults.put(PRODUCT_RATING_RELOAD, "true");
        defaults.put(PROP_ALCOHOLFILTERING_ENABLED, "true");

        defaults.put(PROP_SOY_DEBUG, false);

        defaults.put(PROP_IP_LOCATOR_ENABLED, "true");
        defaults.put(PROP_IP_LOCATOR_CLIENT_ID, "e8lQTiN_7IOJZhUTjNQ_5t**");//103310996");
        defaults.put(PROP_IP_LOCATOR_URL, "https://iplocator.melissadata.net/v2/REST/Service.svc/doIPLocation");
        defaults.put(PROP_IP_LOCATORV4_URL, "http://globalip.melissadata.net/v4/WEB/iplocation/doiplocation");
        defaults.put(PROP_IP_LOCATORV4_ENABLED, "true");
        defaults.put(PROP_IP_LOCATOR_TIMEOUT, "3000");
        defaults.put(PROP_IP_LOCATOR_ROLLOUT_PERCENT, "100");
        defaults.put(PROP_IP_LOCATOR_EVENT_LOG_ENABLED, "true");
        defaults.put(PROP_HOST_URL, "http://www.freshdirect.com");
        defaults.put(PROP_SOCIAL_BUTTONS_ENABLED, "true");
        defaults.put(CHECK_LOCAL_INVENTORY_ENABLED, "false");
        defaults.put(PROP_FDCOUPONMGR_HOME, "freshdirect.fdstore.CouponManager");

        defaults.put(PROP_EH_CACHE_ENABLED, "true");
        defaults.put(PROP_EH_CACHE_MANAGEMENT_ENABLED, "false");

        defaults.put(PROP_FD_TC_ENABLED, "false");
        defaults.put(PROP_QS_IGNORE_PARTIAL_ROLLOUT, "false");
        defaults.put(PROP_QS_ENABLED, "true");

        defaults.put(PROP_PAYMENTECH_GATEWAY_ENABLED, "true");

        // APPDEV-2817 Link to DeliveryPass category from top nav of Delivery Info page
        defaults.put(SHOW_DLVPASS_LINK_ON_DELINFO, "false");

        defaults.put(PROP_ASSIGNED_CUSTOMER_PARAMS_QUERY_ID, "1");

        defaults.put(GMAPS_API_KEY, "AIzaSyAALx7g2uVEDP46IaGU_zxYT5gBSKac2ks");

        // APPDEV-3100 2013 Wine Transition
        defaults.put(PROP_WINE_ASSID, "FDW");

        defaults.put(PROP_BROWSE_ROLLOUT_REDIRECT_ENABLED, "true");

        defaults.put("feature.rollout.pdplayout2014", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.pplayout2014", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.leftnav2014", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.searchredesign2014", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.leftnavtut2014", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.browseflyoutrecommenders", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.quickshop2_2", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.mobweb", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.mobwebindexopt", "GLOBAL:ENABLED,true;");
        // defaults.put("feature.rollout.sociallogin", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.printinvoice", "GLOBAL:ENABLED,true;");

        /* APPDEV-5916 */
        defaults.put("feature.rollout.carttabcars", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.standingorder3_0", "GLOBAL:ENABLED,false;");
        defaults.put("feature.rollout.browseaggregatedcategories1_0", "GLOBAL:ENABLED,false;");
        defaults.put("feature.rollout.debitCardSwitch", "GLOBAL:ENABLED,true;");

        defaults.put(PROP_MEDIA_RENDER_UTILS_REALLY_CLOSE, "true");
        defaults.put(PROP_MEDIA_RENDER_UTILS_SOURCE_ENCODING, "ISO-8859-1");

        // optimize
        defaults.put(PROP_OPT_SOCIAL_FOOTER_STATIC, "true");
        defaults.put(PROP_STANDING_ORDER_IGNOREATPFAILURE, "true");

        // template redesign
        defaults.put(PROP_MAX_XSELL_PRODS, "3");
        defaults.put(PROP_BROWSE_PAGESIZE, "30");
        defaults.put(PROP_PRES_PICKS_PAGESIZE, "30");
        defaults.put(PROP_STAFF_PICKS_PAGESIZE, "99");
        // defaults.put(PROP_STAFF_PICKS_PICKID, "3000000060");
        defaults.put(PROP_STAFF_PICKS_FEATLIMIT, "4");
        defaults.put(PROP_NEWPRODUCTS_PAGESIZE, "30");
        defaults.put(PROP_ECOUPON_PAGESIZE, "30");
        defaults.put(PROP_SEARCH_PAGESIZE, "30");
        defaults.put(PROP_QUICKSHOP_PAGESIZE, "30");
        defaults.put(PROP_BROWSE_POPULAR_CATEGORIES_MAX, "5");

        defaults.put(PROP_3RDPARTY_P3PENABLED, "true");

        // SMS notification
        defaults.put(PROP_ST_PROVIDER_URL, "http://api.rtdl.us/API2/Messaging/SendSMS/submit");
        defaults.put(PROP_ST_USERNAME, "fresh_direct");
        defaults.put(PROP_ST_PASSWORD, "6zDqxrz7Qa");
        // SMS FDX Notification
        defaults.put(PROP_ST_FDX_USERNAME, "fresh_direct2");
        defaults.put(PROP_ST_FDX_PASSWORD, "Mc49s0Kq");

        defaults.put(PROP_ST_CONNECTION_TIMEOUT_PERIOD, "5");
        defaults.put(PROP_ST_READ_TIMEOUT_PERIOD, "5");
        defaults.put(PROP_SMS_OVERLAY_FLAG, "false");

        defaults.put(PROP_SEARCH_CAROUSEL_PRODUCT_LIMIT, "25");

        defaults.put(PROP_PRESIDENT_PICK_PAGING_ENABLED, "false");

        defaults.put(PROP_ALL_DEALS_CACHE_ENABLED, "true");

        defaults.put(PROP_SITEMAP_ENABLED, "true");
        defaults.put(PROP_SITEMAP_PASSWORDS, "fd8848admin,GetMe2TheSitem@p");

        // Early Am Defaults
        defaults.put(PROP_EARLY_AM_HOUR, "6");
        defaults.put(PROP_EARLY_AM_MINUTE, "0");

        /* Alt. Pickup convenience APPDEV-3623 */
        defaults.put(PROP_DEPOT_CACHE_REFRESH, "480"); // 1000 * 60 * 480 = 8 hours

        defaults.put(PROP_LOG_RECOMMENDTATIONS_RESULTS, "false");

        // Unit Price Display
        defaults.put(UNIT_PRICE_DISPLAY_ENABLED, "true");

        // Limiting quickshop all items result in Mobile to set max
        defaults.put(QUICKSHOP_ALL_ITEMS_MAX, "600");

        defaults.put(PROP_ORDER_HISTORY_FROM_IN_MONTHS, "13");

        defaults.put(PROP_QUICKSHOP_PAST_ORDERS_VISIBLE_MENU_ITEMS_COUNT, "8");

        defaults.put("feature.rollout.quickshop2_0", "GLOBAL:ENABLED,false;");

        defaults.put("feature.rollout.akamaiimageconvertor", "GLOBAL:ENABLED,false;");

        // Default reCaptcha Public & Private krys
        defaults.put(PROP_RECAPTCHA_PUBLIC_KEY, "6LdmgQYTAAAAAEqZbKoF4WpDqFU7pyAO-40mxdnc");
        defaults.put(PROP_RECAPTCHA_PRIVATE_KEY, "6LdmgQYTAAAAAJcKVYSoFavVDLSLdV3x-fWsOtqH");
        defaults.put(PROP_MAX_INVALID_LOGIN_ATTEMPT, "10");
        defaults.put(PROP_TIP_RANGE_CONFIG, "0,25,0.5;");

        defaults.put(SUB_DOMAIN, "");

        // Product Family
        defaults.put(PROP_PRODUCTFAMILY, "true");
        defaults.put(PROP_LOGISTICS_API_URL, "http://logisticsdev1.nj01/");
        defaults.put(PROP_FDCOMMERCE_API_URL, "http://localhost:8080");
        defaults.put(PROP_PAYPAL_API_URL, "http://logisticsdev1.nj01/paypal");
        defaults.put(PROP_MASTERPASSS_API_URL, "http://logisticsdev1.nj01/paypal");
        
        defaults.put(PROP_ORBITAL_API_URL, "http://logisticsdev1.nj01/paypal");
        defaults.put(PROP_OMS_API_URL, "http://crmdev1.nj01/");

        defaults.put(PROP_GIVEXGATEWAY_ENDPOINT, "http://logisticsdev1api.nj01/givex/giftcard/");

        // APPDEV - 4159 - Setting default values for maximum size of columns in promo table
        defaults.put(PROMO_OLDCOLUMN_MAX_LIMIT, "2999");
        defaults.put(PROMO_NEWCOLUMN_MAX_LIMIT, "2999");

        defaults.put(CATEGORY_TOP_ITEM_CACHE_SIZE, "5");
        defaults.put(CATEGORY_TOP_ITEM_CACHE_MAXIMAL_SIZE, "10");
        defaults.put(PROP_PRODUCT_SAMPLES_MAX_BUY_PRODUCTS_LIMIT, "2");
        defaults.put(PROP_PRODUCT_SAMPLES_MAX_BUY_QUANTITY_LIMIT, "1");

        defaults.put("feature.rollout.checkout1_0", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.checkout2_0", "GLOBAL:ENABLED,false;");

        defaults.put(PROP_LOGISTICS_COMPANY_CODE, EnumCompanyCode.fd.name());
        defaults.put(PROP_LOGISTICS_CONNECTION_TIMEOUT, 120);
        defaults.put(PROP_LOGISTICS_CONNECTION_POOL, 15);
        defaults.put(PROP_LOGISTICS_CONN_READ_TIMEOUT, 120);
        defaults.put(PROP_LOGISTICS_CONNECTION_REQUEST_TIMEOUT, 60);
        defaults.put(CTCAPACITY_ELIGIBLE_PROFILES, "MktgSegment");
        defaults.put(PROP_CORE_NON_CORE_GLOBAL_NAV_SWITCH_ENABLED, "false");
        defaults.put(PROP_EXTERNAL_ACCOUNTMGR_HOME, "freshdirect.fdstore.ExternalAccountManager");
        defaults.put(PROP_MIDDLETIER_PROVIDER_URL, "http://devmidtier.freshdirect.com/dl");
        defaults.put(PROP_ATP_AVAILABILTY_MOCK_ENABLED, "false");
        defaults.put(PROP_HOLIDAY_MEAL_BUNDLE_CATEGORY_ID, "meals_entrees_holiday_thanksgiving");
        defaults.put(PROP_EWALLET_MASTERPASS_LIGHT_BOX_URL, "https://sandbox.masterpass.com/lightbox/Switch/integration/MasterPass.client.js");
        defaults.put(PROP_EWALLET_MASTERPASS_ENV_PROP_NAME, "Sandbox-Profile.ini");
        defaults.put(PROP_EWALLET_PAYPAL_ENV_PROP_NAME, "PayPal-Profile.ini");
        defaults.put(PROP_EWALLET_MP_BTN_IMG_URL, "https://static.masterpass.com/dyn/img/btn/global/mp_chk_btn_147x034px.svg");
        defaults.put(PROP_EWALLET_MP_LOGO_URL, "https://www.mastercard.com/mc_us/wallet/img/en/US/mp_acc_046px_gif.gif");

        defaults.put(PROP_SOCIAL_ONEALL_SUBDOMAIN, "freshdirect");
        defaults.put(PROP_SOCIAL_ONEALL_PUBLICKEY, "493e89df-35af-48ff-a856-125064fed179");
        defaults.put(PROP_SOCIAL_ONEALL_PRIVATEKEY, "64bf95b4-9dea-4832-8528-31bba3ae09d6");
        defaults.put(PROP_SOCIAL_ONEALL_POSTURL, ".api.oneall.com");
        defaults.put(PROP_SOCIAL_LOGIN_ENABLED, "false");
        defaults.put(PROP_DEPLOYMENT_LOCAL, "false");
        defaults.put(PROP_DEVELOPER_DISABLE_AVAIL_LOOKUP, "false");
        defaults.put(PROP_FDX_SMS_ORDER_CONFIRMATION, "false");
        defaults.put(PROP_FDX_SMS_ORDER_MODIFICATION, "false");
        defaults.put(PROP_FDX_SMS_ORDER_CANCEL, "false");
        defaults.put(PROP_FDX_APP_APPLE_URL, "https://itunes.apple.com/us/app/foodkick/id1049036650?pt=309390&ct=FKWeb&mt=8");

        defaults.put(PROP_FDX_LOCATIONBAR, "true");
        defaults.put(PROP_FDX_LOCATIONBAR_FDXTAB, "true");
        defaults.put(PROP_ETIPPING_ENABLED, "true");

        // Extole related

        defaults.put(PROP_FDEXTOLEMGR_HOME, "freshdirect.fdstore.FDExtoleManager");
        defaults.put(PROP_EXTOLE_SFTP_HOST, "sftp.extole.com");
        defaults.put(PROP_EXTOLE_SFTP_USERNAME, "c553175139");
        defaults.put(PROP_EXTOLE_SFTP_FILE_DOWNLOADER_REMOTE_WORKDIR, "/dropbox");
        defaults.put(PROP_EXTOLE_SFTP_FILE_DOWNLOADER_LOCAL_WORKDIR, "/opt/fdlog/referralcredits");
        defaults.put(PROP_SCHEME_HTTPS, "https");
        defaults.put(PROP_EXTOLE_BASE_URL, "api.extole.com");
        defaults.put(PROP_EXTOLE_ENDPOINT_CREATE_CONVERSION, "/v3/events/convert");
        defaults.put(PROP_EXTOLE_ENDPOINT_APPROVE_CONVERSION, "/v3/events/approve");
        defaults.put(PROP_EXTOLE_API_KEY, "553175139-1");
        defaults.put(PROP_EXTOLE_API_SECRET, "53bfce3a534749c09ff79860833fddb0");
        defaults.put(PROP_EXTOLE_BASE_FILE_NAME, "FreshDirect_EarnedRewards_");
        defaults.put(PROP_EXTOLE_SFTP_PRIVATE_KEY, "/fddata/storefront/fdconf/int01/FreshDirect/config/security/extole_sftp.key");
        defaults.put(PROP_CRM_REFERRAL_HISTORY_PAGE_ENABLED, "false");

        defaults.put(PROP_ENABLE_XC_FOR_CRM_AGENTS, "true");
        defaults.put(PROP_EXTOLE_RAF_ENABLED, "true");
        defaults.put(PROP_EXTOLE_MICROSITE_URL, "https://refer.freshdirect.com/myaccountscre");
        defaults.put(PROP_EXTOLE_MICROSITE_SUB_URL, "https://refer.freshdirect.com/myaccountsub");
        defaults.put(PROP_EXTOLE_MICROSITE_GLOBAL_NAV_URL, "https://refer.freshdirect.com/globalnav");
        defaults.put(PROP_FD_BRAND_PRODUCTS_AD_HOME, "freshdirect.fdstore.BrandProductsAdManager");

        defaults.put(PROP_FEATURE_ROLLOUT_NEW_SO, "true");
        defaults.put(PROP_SO3_ACTIVATE_CUTOFF_TIME, "23");// 11pm - Hour of the day in 24hr format.

        defaults.put(PROP_EDT_EST_TIMESLOT_CONVERSION_ENABLED, false);// It should be 'true' only for FDX.

        defaults.put(PROP_EWALLET_PAYPAL_ENABLED, "true");
        defaults.put(PROP_EWALLET_MASTERPASS_ENABLED, "true");
        defaults.put(PROP_HOOK_LOGIC_BLACKHOLE_ENABLE, "false");
        defaults.put(PROP_HOOK_LOGIC_CATEGORY_ENABLE, "true");
        defaults.put(PROP_HOOK_LOGIC_ORDER_FEED_MINS, "15");// default is last 15 mins orders.
        defaults.put(PROP_HOOK_LOGIC_ALLOW_OWN_ROWS, "true"); // can HL items be on a row by themselves?

        defaults.put(PROP_ERPSYLINK_STOREFRONT_FD, "http://web01.web.stdev01.nj01:7001");
        defaults.put(PROP_ERPSYLINK_STOREFRONT_FDX, "http://web01.web.stdev06.nj01:7001");
        defaults.put(PROP_HL_PRODUCTS_COUNT, "5");

        defaults.put(PROP_BROWSE_AGGREGATED_CATEGORIES, "Category:bgril,Category:cchm,Category:cbrst,Category:bground");
        defaults.put(PROP_ADDRESS_MISMATCH_ENABLED, "true");
        defaults.put(PROP_GROUP_SCALE_PERF_IMPROVE_ENABLED, "true");
        defaults.put(PROP_PRICE_CONFIG_CONVERSION_LIMIT, "1.00");

        defaults.put(PROP_DEFAULT_FDX_PLANTID, "1300");
        defaults.put(PROP_DEFAULT_FDX_DISTRIBUTION_CHANNEL, "01");
        defaults.put(PROP_DEFAULT_FDX_DISTRIBUTION_CHANNEL_PARENT, "01");
        defaults.put(PROP_DEFAULT_FDX_SALESORG, "1300");
        defaults.put(PROP_DEFAULT_FDX_SALESORG_PARENT, "0001");
        defaults.put(PROP_DEFAULT_FD_PLANTID, "1000");
        defaults.put(PROP_DEFAULT_FD_DISTRIBUTION_CHANNEL, "01");
        defaults.put(PROP_DEFAULT_FD_SALESORG, "0001");

        defaults.put(PROP_UNBXD_API_KEY, "91a4d42b07d3346afbae9ee63134c5d2");
        defaults.put(PROP_UNBXD_SITE_KEY, "freshdirect_dev-u1469033821585");
        defaults.put(PROP_UNBXD_COS_SITE_KEY, "cos_dev-u1508499587553");
        defaults.put(PROP_UNBXD_BASE_URL, "http://search.unbxdapi.com/");
        defaults.put(PROP_UNBXD_FALLBACK_ON_ERROR, "false");
        defaults.put(PROP_UNBXD_TRACKING_BASE_URL, "http://tracker.unbxdapi.com/v2/1p.jpg");
        defaults.put("feature.rollout.unbxdintegrationblackhole2016", "GLOBAL:ENABLED,true;");
        defaults.put("feature.rollout.unbxdanalytics2016", "GLOBAL:ENABLED,true;");


        defaults.put(PROP_MEAL_KIT_MATERIAL_GROUP, "MEALKIT");

        defaults.put(PROP_PRODUCT_FEED_GENERATION_DEVELOPER_MODE_ENABLED, "false");

        // APPDEV - 5516 Cart Carousel - Grand Giving Donation Technology
        defaults.put(PROP_DONATION_PRODUCT_SAMPLES_ENABLED, "false");
        defaults.put(PROP_DONATION_PRODUCT_SAMPLES_ID, "");

        defaults.put(PROP_VIEWCART_PAGE_NEW_CUSTOMER_CAROUSEL_SITE_FEATURES, "PRODUCT_SAMPLE, C_YMAL, FAVORITES");
        defaults.put(PROP_VIEWCART_PAGE_CURRENT_CUSTOMER_CAROUSEL_SITE_FEATURES, "PRODUCT_SAMPLE, DYF, TOP_ITEMS_QS");
        defaults.put(PROP_CHECKOUT_PAGE_NEW_CUSTOMER_CAROUSEL_SITE_FEATURES, "C_YMAL, FAVORITES, PRODUCT_SAMPLE");
        defaults.put(PROP_CHECKOUT_PAGE_CURRENT_CUSTOMER_CAROUSEL_SITE_FEATURES, "DYF, TOP_ITEMS_QS, PRODUCT_SAMPLE");
        defaults.put(PROP_CHECKOUT_PAGE_COS_CUSTOMER_DISPLAY_DELIVERY_FEE_HEADER, false);
        
        defaults.put(PROP_USER_CART_SAVE_INTERVAL, "0");

        defaults.put(PROP_HOMEPAGE_REDESIGN_CURRENTCOS_USER_CONTAINER_CONTENT_KEY, "ModuleContainer:mc_hp_ato_exist_cust");
        defaults.put(PROP_HOMEPAGE_REDESIGN_NEWCOS_USER_CONTAINER_CONTENT_KEY, "ModuleContainer:mc_hp_ato_new_cust");

        defaults.put(PROP_HOMEPAGE_REDESIGN_CURRENT_USER_CONTAINER_CONTENT_KEY, "ModuleContainer:mc_hp_exist_cust");
        defaults.put(PROP_HOMEPAGE_REDESIGN_NEW_USER_CONTAINER_CONTENT_KEY, "ModuleContainer:mc_hp_new_cust");
        defaults.put(PROP_HOMEPAGE_REDESIGN_MODULE_PRODUCT_LIMIT_MAX, "12");
        defaults.put(PROP_HOMEPAGE_REDESIGN_PRESPICKS_CATEGORY_ID, "picks_love");
        defaults.put(PROP_HOMEPAGE_REDESIGN_STAFFPICKS_CATEGORY_ID, "prod_assort");

        defaults.put(PROP_PLANT1300_PRICE_INDICATOR, "BASE");
        defaults.put(PROP_PLANT1310_PRICE_INDICATOR, "BASE");
        defaults.put(PROP_PLANTWDC_PRICE_INDICATOR, "BASE");

        defaults.put(PROP_SF_2_0_ENABLED, "false");
        defaults.put(PROP_MAT_SALESORG__EXPORT_PICKPLANT_VALIDATION_ENABLED, "true");
        defaults.put(PROP_EXTRA_LOG_FOR_LOGIN_FAILS_ENABLED, "false");
        defaults.put(PROP_MEALBUNDLE_CARTONVIEW_ENABLED, "true");

        defaults.put(PROP_QS_TOP_ITEMS_PERF_OPT_ENABLED, "true");
        defaults.put(PROP_ZIP_CHECK_OVER_LAY_ENABLED, "false");

        /* APPDEV-5781 */
        defaults.put(PROP_OBSOLETE_MERGECARTPAGE_ENABLED, "false");

        defaults.put(PROP_DFP_ENABLED, "false");
        defaults.put(PROP_DFP_ID, "1072054678");

        defaults.put(PROP_CLUSTER_NAME, "localhost");
        defaults.put(PROP_NODE_NAME, "localhost");


        defaults.put(PROP_PRODUCT_CACHE_OPTIMIZATION_ENABLED, "true");

        /* IBM silverpopup */
        defaults.put(IBM_ACCESSTOKEN_URL, "https://api3.ibmmarketingcloud.com/oauth/token");
        // for Dev DB
        defaults.put(IBM_PUSHNOTIFICATION_URL, "https://api3.silverpop.com:443/rest/databases/5979940/establishidentity/");
        									 // https://api3.silverpop.com:443/rest/databases/{databaseid}/establishidentity/
     //   defaults.put(IBM_WATSON_EMAIL_CAMPAIGN_URL, "https://api3.ibmmarketingcloud.com/XMLAPI");
        defaults.put(IBM_WATSON_EMAIL_CAMPAIGN_URL, "http://transact3.silverpop.com/XTMail");
        										
        
        //private final static String IBM_WATSON_EMAIL_CAMPAIGN_URL = "fdstore.ibm.watsonemailcampaign.url";
        //https://api3.ibmmarketingcould.com/XMLAPI
        // for Prod DB
        //defaults.put(IBM_PUSHNOTIFICATION_URL, "https://api3.silverpop.com:443/rest/databases/3745165/establishidentity/");
        defaults.put(IBM_CLIENT_ID, "42c3eede-b1b2-43d2-b503-55682f190c2d");
        
        defaults.put(IBM_CAMPAIGN_CLIENT_ID, "899e322b-977e-4a50-8320-7bba87b54085");
        
        defaults.put(IBM_CLIENT_SECRET, "5f154ee0-bae6-4833-9ce2-e013b1b3c7d5");
        defaults.put(IBM_CAMPAIGN_CLIENT_SECRET, "e1a1982c-9d7b-480d-b871-f76354ce8cce");
        
        defaults.put(IBM_REFRESH_TOKEN, "r_3872jS_Gh7VmanX2TcazBB_MJ1C_RBqbJWY6gvh3koS1");
        defaults.put(IBM_CAMPAIGN_REFRESH_TOKEN, "rqeZfstHf754FkCIR2K5NC6O0IVE6ZxqQbBM_X1gTDcsS1");//"rqeZfstHf754FkCIR2K5NC6O0IVE6ZxqQbBM_X1gTDcsS1"
     
        defaults.put(PAYMENT_TLSSHA_ENABLED,"true");
        defaults.put(PROP_PAYMENT_VERIFICATION_ENABLED, "true");
        defaults.put(GLOBAL_SF2_0_ENABLED, "false");

        //DCS-23
        defaults.put(PROP_DEBIT_SWITCH_NOTICE_ENABLED, "false");

        defaults.put(PROP_LOG_AKAMAI_HEADER_ENABLED,"false");

        //APPDEV-6442
        defaults.put(PROP_FDC_TRANSITION_LOOK_AHEAD_DAYS, "0");

        //CUSTOMER CONTACT NUMBERS
 	   	defaults.put(CUSTOMER_SERVICE_CONTACT, "1-866-283-7374");
 	   	defaults.put(CHEFSTABLE_CONTACT_NUMBER, "1-866-511-1240");
 	   	defaults.put(FOODKICK_SERVICE_CONTACT, "1-718-513-2785");
 	   	defaults.put(PENNSYLVANIA_SERVICE_CONTACT, "1-215-825-5726");

 	   	//Delivery Pass sent as a free products for trail
        defaults.put(PROP_ENABLE_FREE_PRODUCT,"false"); // Enable free product
        
        defaults.put(PROP_ENABLE_WEBSITE_MOBILE_SAME_NUTRITION_SOY,"false");

        defaults.put(PROP_ENABLE_FDX_DISTINCT_AVAILABILITY,"true");
        

        defaults.put("feature.rollout.cosRedesign2017", "GLOBAL:ENABLED,false;");
        defaults.put(PROP_ENABLE_REPEAT_WARMUP, "true"); // controls manual warmup: LIVE PUBLISH
        

        // Auth Code : 2 minutes
		defaults.put(DEFAULT_CODE_EXPIRATION, "120");
		// Access Token : 30 days
		defaults.put(DEFAULT_TOKEN_EXPIRATION, "2592000");
		// Refresh Token : 180 days
		defaults.put(DEFAULT_REFRESH_TOKEN_EXPIRATION, "15552000");
		defaults.put(PROP_FDC_NEW_BACKIN_USE_FD_ENABLED,"false");
		
		defaults.put(DATABASE_IN_CONDITION_LIMIT, "50");
		defaults.put(PROP_FD_DP_FREE_TRIAL_OPTIN_FEATURE_ENABLED, "false");
		
        refresh();
    }

    private FDStoreProperties() {
    }

    private static void refresh() {
        refresh(false);
    }

    private static void refresh(boolean force) {
        long t = System.currentTimeMillis();

        if (force || ((t - lastRefresh) > REFRESH_PERIOD)) {
        	synchronized (FDStoreProperties.class){
        		 if (force || ((t - lastRefresh) > REFRESH_PERIOD)) {//double check
		            config = ConfigHelper.getPropertiesFromClassLoader("fdstore.properties", defaults);
		            lastRefresh = t;
		            LOGGER.info("Loaded configuration from fdstore.properties: " + config);
		            fireEvent();
        		 }
        	}
        }
    }

    private static List<String> getAsList(String key) {
        String value = get(key);
        return value == null ? Collections.<String> emptyList() : Arrays.asList(value.trim().split("\\s*,\\s*"));
    }

    public static String get(String key) {
        refresh();
        return config.getProperty(key);        
    }
    
    public static String get(String key, String defaultValue) {
        refresh();
        return config.getProperty(key, defaultValue);        
    }

    /**
     * A method to set a specific property. Use with care - it's here for testing purposes only.
     *
     * @param key
     *            the name of the property to set.
     * @param value
     *            the new value of the property.
     */
    public static void set(String key, String value) {
        refresh();
        config.setProperty(key, value);
    }

    public static boolean isAirclicEnabled() {
        return (Boolean.valueOf(get(PROP_ENABLE_AIRCLIC))).booleanValue();
    }

    public static String getDlvInstructionsSpecialChar() {
        return get(PROP_DLV_INSTRUCTION_SPECIAL_CHAR);
    }

    public static String getProfanityCheckURL() {
        return get(PROP_WEBPURIFY_URL);
    }

    public static String getProfanityCheckPass() {
        return get(PROP_WEBPURIFY_KEY);
    }

    public static String getProviderURL() {
        return get(PROP_PROVIDER_URL);
    }

    public static String getInitialContextFactory() {
        return get(PROP_INIT_CTX_FACTORY);
    }

    public static String getCareerLink() {
        return get(PROP_FDSTORE_WEB_CAREERLINK);
    }

    public static String getGeocodeLink() {
        return get(PROP_CRM_GEOCODELINK);
    }

    public static String getCaseListLength(boolean isCaseHistory) {
        return (isCaseHistory) ? get(PROP_CRM_CASE_HISTORY_LIST_LENGTH) : get(PROP_CRM_CASE_LIST_LENGTH);
    }

    /**
     * This property disables time window check on CRM issue credits page.
     *
     * For debug purposes.
     *
     * @return
     */
    public static boolean getDisableTimeWindowCheck() {
        return (Boolean.valueOf(get(PROP_CRM_DISABLE_TIME_WINDOW_CHECK)).booleanValue());
    }

    public static String getFDFactoryHome() {
        return get(PROP_FDFACTORY_HOME);
    }

    public static String getSapGatewayHome() {
        return get(PROP_SAPGATEWAY_HOME);
    }

    public static String getKanaGatewayHome() {
        return get(PROP_KANAGATEWAY_HOME);
    }

    public static String getComplaintManagerHome() {
        return get(PROP_COMPLAINTMGR_HOME);
    }

    public static String getCallCenterManagerHome() {
        return get(PROP_CALLCENTERMGR_HOME);
    }

    public static String getPromotionEmail() {
        return get(PROP_EMAIL_PROMOTION);
    }

    public static String getFDCustomerManagerHome() {
        return get(PROP_FDCUSTMGR_HOME);
    }

    public static String getErpEWalletHome() {
        return get(PROP_EWALLET_HOME);
    }

    public static String getEwalletServiceHome() {
        return get(PROP_EWALLET_SERVICE_HOME);
    }

    public static String getMPServiceHome() {
        return get(PROP_MPService_HOME);
    }

    public static String getPPServiceHome() {
        return get(PROP_PPService_HOME);
    }

    public static String getExternalAccountManagerHome() {
        return get(PROP_EXTERNAL_ACCOUNTMGR_HOME);
    }

    public static String getEwalletEncryptionKey() {
        return get(PROP_EWALLET_ENCRYPTION_KEY);
    }

    public static String getEwalletEncryptionAlgorithm() {
        return get(PROP_EWALLET_ENCRYPTION_ALGORITHM);
    }

    public static String getFDPromotionManagerHome() {
        return get(PROP_FDPROMOTIONMGR_HOME);
    }

    public static String getFDPromotionManagerNewHome() {
        return get(PROP_FDPROMOTIONMGR_NEW_HOME);
    }

    public static String getContentManagerHome() {
        return get(PROP_CONTENTMANAGER_HOME);
    }

    public static String getDlvRestrictionManagerHome() {
        return get(PROP_DLVRESTRICTION_MGR_HOME);
    }

    public static String getDeliveryManagerHome() {
        return get(PROP_DLVMANAGER_HOME);
    }

    public static String getRulesManagerHome() {
        return get(PROP_RULESMANAGER_HOME);
    }

    public static String getAirclicManagerHome() {
        return get(PROP_AIRCLICMANAGER_HOME);
    }

    public static String getFDCustomerHome() {
        return get(PROP_FDCUSTOMER_HOME);
    }

    public static String getErpCustomerHome() {
        return get(PROP_ERPCUSTOMER_HOME);
    }

    public static String getErpCustomerInfoHome() {
        return get(PROP_ERPCUSTOMERINFO_HOME);
    }

    public static String getContentFactoryHome() {
        return get(PROP_CONTFACTORY_HOME);
    }

    public static String getFDOrderHome() {
        return get(PROP_FDORDER_HOME);
    }

    public static String getPaymentMethodManagerHome() {
        return get(PROP_PAYMENT_METHOD_MANAGER_HOME);
    }

    public static String getRestrictedPaymentMethodHome() {
        return get(PROP_RESTRICTED_PAYMENT_METHOD_HOME);
    }

    public static boolean getPreviewMode() {
        return (Boolean.valueOf(get(PROP_PREVIEW_MODE))).booleanValue();
    }

    public static boolean isFlushOscache() {
        return (Boolean.valueOf(get(PROP_FLUSH_OSCACHE))).booleanValue();
    }

    public static boolean isAnnotationMode() {
        return (Boolean.valueOf(get(PROP_ANNOTATION_MODE))).booleanValue();
    }

    public static String getAnnotationErpsy() {
        return get(PROP_ANNOTATION_ERPSY);
    }

    public static String getMediaPath() {
        return get(PROP_MEDIA_PATH);
    }

    public static String getCallCenterPW() {
        return get(PROP_CALLCENTER_PW);
    }

    public static String getCustomerServiceEmail() {
        return get(PROP_CUSTOMER_SERVICE_EMAIL);
    }

    public static int getHolidayLookaheadDays() {
        return Integer.parseInt(get(PROP_HOLIDAY_LOOKAHEAD_DAYS));
    }

    public static double getCutoffWarnStart() {
        String prop = get(PROP_CUTOFF_WARN);

        return (prop == null) ? (-1) : Double.parseDouble(prop);
    }

    public static String getCutoffDefaultZoneCode() {
        return get(PROP_CUTOFF_DEFAULT_ZONE_CODE);
    }

    public static int getPreReserveHours() {
        return Integer.parseInt(get(PROP_PRERESERVE_HOURS));
    }

    public static String getAdServerUrl() {
        return get(PROP_AD_SERVER_URL);
    }

    public static boolean isAdServerEnabled() {
        return Boolean.valueOf(get(PROP_AD_SERVER_ENABLED)).booleanValue();
    }

    public static boolean isDlvFeeTierEnabled() {
        return Boolean.valueOf(get(PROP_DLVFEE_TIER_ENABLED)).booleanValue();
    }

    public static String getAdServerUpdatesURL() {
        return get(PROP_AD_SERVER_UPDATES_URL);
    }

    public static int getImpressionLimit() {
        String s = get(PROP_IMPRESSION_LIMIT);

        return (s == null) ? 0 : Integer.parseInt(s);
    }

    public static String getWinbackRoot() {
        return get(PROP_WINBACK_ROOT);
    }

    public static String getMarketingPromoRoot() {
        return get(PROP_MARKETING_PROMO_ROOT);
    }

    public static int getRefreshSecsProductInfo() {
        return Integer.parseInt(get(PROP_REFRESHSECS_PRODUCTINFO));
    }

    public static int getRefreshSecsGroupScaleInfo() {
        return Integer.parseInt(get(PROP_REFRESHSECS_GROUPSCALE));
    }

    public static int getRefreshSecsUPCProductInfo() {
        return Integer.parseInt(get(PROP_REFRESHSECS_UPCPRODUCTINFO));
    }

    public static int getRefreshSecsProduct() {
        return Integer.parseInt(get(PROP_REFRESHSECS_PRODUCT));
    }

    public static int getRefreshSecsZone() {
        return Integer.parseInt(get(PROP_REFRESHSECS_ZONE));
    }

    public static int getProductCacheSize() {
        return Integer.parseInt(get(PROP_PRODUCT_CACHE_SIZE));
    }

    public static int getZoneCacheSize() {
        return Integer.parseInt(get(PROP_ZONE_CACHE_SIZE));
    }

    public static int getGrpCacheSize() {
        return Integer.parseInt(get(PROP_GRP_CACHE_SIZE));
    }

    public static int getMediaContentCacheSize() {
    	return Integer.parseInt(get(PROP_MEDIACONTENT_CACHE_SIZE));
    }
    public static String getProductEmail() {
        return get(PROP_EMAIL_PRODUCT);
    }

    public static String getFeedbackEmail() {
        return get(PROP_EMAIL_FEEDBACK);
    }

    public static String getVendingEmail() {
        return get(PROP_EMAIL_VENDING);
    }

    public static String getChefsTableEmail() {
        return get(PROP_EMAIL_CHEFSTABLE);
    }

    public static String getExtraAdServerProfileAttributes() {
        return get(PROP_AD_SERVER_PROFILE_ATTRIBS);
    }

    public static boolean getAdServerUsesDeferredImageLoading() {
        return Boolean.valueOf(get(PROP_AD_SERVER_USES_DEFERRED_IMAGE_LOADING)).booleanValue();
    }

    public static boolean performStorePreLoad() {
        return Boolean.valueOf(get(PROP_PRELOAD_STORE)).booleanValue();
    }

    public static boolean performPromotionsPreLoad() {
        return Boolean.valueOf(get(PROP_PRELOAD_PROMOTIONS)).booleanValue();
    }

    public static boolean performGroupScalePreLoad() {
        return Boolean.valueOf(get(PROP_PRELOAD_GROUPS)).booleanValue();
    }

    public static boolean isPreloadAutocompletions() {
        return Boolean.valueOf(get(PROP_PRELOAD_AUTOCOMPLETIONS)).booleanValue();
    }

    public static boolean isPreloadSmartStore() {
        return Boolean.valueOf(get(PROP_PRELOAD_SMARTSTORE)).booleanValue();
    }

    public static boolean isPreloadNewness() {
        return Boolean.valueOf(get(PROP_PRELOAD_NEWNESS)).booleanValue();
    }

    public static boolean isPreloadReintroduced() {
        return Boolean.valueOf(get(PROP_PRELOAD_REINTRODUCED)).booleanValue();
    }

    public static Date getDlvPromoExpDate() {
        Date d = null;

        try {
            d = SF.parse(get(PROP_DLV_PROMO_EXP_DATE));
        } catch (ParseException e) {
            new FDRuntimeException("fdstore.dlvPromo.expDate property in fdstore.properties is not in correct yyyy-MM-dd format");
        }

        return d;
    }

    public static String getCmsMediaBaseURL() {
        return get(PROP_CMS_MEDIABASEURL);
    }

    public static String getCheckExternalForPaymentMethodFraud() {
        return get(PROP_EXTERNAL_FRAUD_CHECK_PM);
    }

    public static Context getInitialContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.PROVIDER_URL, getProviderURL());
        env.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory());

        return new InitialContext(env);
    }

    public static boolean isSummerServiceEnabled() {
        return (Boolean.valueOf(get(PROP_SUMMERSERVICE))).booleanValue();
    }

    public static String getMaxReferrals() {
        return get(PROP_MAX_REFERRALS);
    }

    public static String getNumDaysMaxReferrals() {
        return get(PROP_NUM_DAYS_MAX_REFERRALS);
    }

    public static String getFDReferralManagerHome() {
        return get(PROP_FDREFERRALMGR_HOME);
    }

    public static boolean useMultiplePromotions() {
        return Boolean.valueOf(get(PROP_USE_MULTIPLE_PROMOTIONS)).booleanValue();
    }

    public static boolean isDataCollectionEnabled() {
        return Boolean.valueOf(get(PROP_DATA_COLLECTION_ENABLED)).booleanValue();
    }

    public static boolean isProductRecommendEnabled() {
        return Boolean.valueOf(get(PROP_PRODUCT_RECOMMEND_ENABLED)).booleanValue();
    }
    public static boolean isProductRecommendCheckCacheEnabled() {
        return Boolean.valueOf(get(PROP_PRODUCT_RECOMMEND_CHECK_CACHE_ENABLED)).booleanValue();
    }
    public static String getBSGSSignUpUrl(boolean isCallCenter) {
        if (isCallCenter) {
            return get(CRM_BSGS_SIGNUP_URL);
        } else {
            return get(BSGS_SIGNUP_URL);
        }
    }

    public static String getUnlimitedSignUpUrl(boolean isCallCenter) {
        if (isCallCenter) {
            return get(CRM_UNLIMITED_SIGNUP_URL);
        } else {
            return get(UNLIMITED_SIGNUP_URL);
        }
    }

    public static String getUnlimitedPromotionalSignUpUrl(boolean isCallCenter) {
        if (isCallCenter) {
            return get(CRM_UNLIMITED_PROMOTIONAL_SIGNUP_URL);
        } else {
            return get(UNLIMITED_PROMOTIONAL_SIGNUP_URL);
        }
    }

    public static String getUnlimitedAmazonPrimeSignUpUrl(boolean isCallCenter) {
        if (isCallCenter) {
            return get(CRM_UNLIMITED_AMAZON_PRIME_SIGNUP_URL);
        } else {
            return get(UNLIMITED_AMAZON_PRIME_SIGNUP_URL);
        }
    }

    public static String getBSGSProfilePosfix() {
        return get(BSGS_PROFILE_POSFIX);
    }

    public static String getUnlimitedProfilePosfix() {
        return get(UNLIMITED_PROFILE_POSFIX);
    }

    public static String getUnlimitedPromotionalProfile() {
        return get(UNLIMITED_PROMOTIONAL_PROFILE);
    }

    public static String getUnlimitedAmazonPrimeProfile() {
        return get(UNLIMITED_AMAZON_PRIME_PROFILE);
    }

    public static String getDlvPassPromoPrefix() {
        return get(DLV_PASS_PROMOTION_PREFIX);
    }

    public static int getOrderProcessingLimit() {
        return Integer.parseInt(get(PROP_CRM_ORDER_PRC_LIMIT));
    }

    public static int getReferralPrgPaginationSize() {
        return Integer.parseInt(get(RFL_PRG_PAGINATION_SIZE));
    }

    public static int getSkuAvailabilityRefreshPeriod() {
        return Integer.parseInt(get(SKU_AVAILABILITY_REFRESH_PERIOD));
    }

    public static boolean isRetProgramCreateCase() {
        return (Boolean.valueOf(get(PROP_RETPRG_CREATECASE))).booleanValue();
    }

    public static int getMaxDlvPassPurchaseLimit() {
        return Integer.parseInt(get(DLV_PASS_MAX_PURCHASE_LIMIT));
    }

    public static int getMaxDyfStrategyCacheEntries() {
        return Integer.parseInt(get(DYF_STRATEGY_CACHE_ENTRIES));
    }

    // Customer Created Lists
    public static boolean isCclAjaxDebugClient() {
        return Boolean.valueOf(get(CCL_AJAX_DEBUG_CLIENT)).booleanValue();
    }

    // Marketing Admin
    public static int getMktAdminFileUploadSize() {
        return Integer.parseInt(get(MKT_ADMIN_FILE_UPLOAD_SIZE));
    }

    public static String getWarmupClass() {
        return get(PROP_WARMUP_CLASS);
    }

    public static int getInventoryRefreshPeriod() {
        return Integer.parseInt(get(PROP_INVENOTRY_REFRESH_PERIOD));
    }

    public static boolean isCclAjaxDebugFacade() {
        return Boolean.valueOf(get(CCL_AJAX_DEBUG_FACADE)).booleanValue();
    }

    public static boolean isCclAjaxDebugJsonRpc() {
        return Boolean.valueOf(get(CCL_AJAX_DEBUG_JSONRPC)).booleanValue();
    }

    public static int getAttributesRefreshPeriod() {
        return Integer.parseInt(get(PROP_ATTRIBUTES_REFRESH_PERIOD));
    }

    public static String getCclAjaxDebugFacadeException() {
        refresh(true);

        return get(CCL_AJAX_DEBUG_FACADE_EXCEPTION);
    }

    public static int getNutritionRefreshPeriod() {
        return Integer.parseInt(get(PROP_NUTRITION_REFRESH_PERIOD));
    }

    @Deprecated
    public static boolean isCclEnabled() {
        // return Boolean.valueOf(get(CCL_ENABLED)).booleanValue();
        return true;
    }

    // Is SmartStore DYF feature enabled?
    @Deprecated
    public static boolean isDYFEnabled() {
        // return Boolean.valueOf(get(DYF_ENABLED)).booleanValue();
        return true;
    }

    public static float getDYFFreqboughtTopPercent() {
        return Float.parseFloat(get(DYF_FREQBOUGHT_TOPPERCENT));
    }

    public static int getDYFFreqboughtTopN() {
        return Integer.parseInt(get(DYF_FREQBOUGHT_TOPN));
    }

    public static String getSampleDistributionsPath() {
        return get(DISTRIBUTION_SAMPLES_DIR);
    }

    public static int getPromotionRTRefreshPeriod() {
        return Integer.parseInt(get(PROP_PROMOTION_RT_REFRESH_PERIOD));
    }

    // cut off time
    public static String getCutOffTimeRange(int day) {
        return get("fdstore.cut_off_day_" + day);
    }

    // click to call display toggle
    public static boolean getClickToCall() {
        return (Boolean.valueOf(get(CLICK_TO_CALL))).booleanValue();
    }

    // customer service hours
    public static String getCustServHoursRange(int day) {
        return get("fdstore.cust_serv_day_" + day);
    }

    public static boolean isDCPDAliasHandlingEnabled() {
        return Boolean.valueOf(get(DCPD_ALIAS_HANDLING_ENABLED)).booleanValue();
    }

    public static DateRange getAdvanceOrderRange() {
        Date dStart = null;
        Date dEnd = null;

        try {
            dStart = SF.parse(get(ADVANCE_ORDER_START));
        } catch (ParseException e) {
            try {
                dStart = SF.parse("2000-01-01");
                LOGGER.warn("fdstore.advance.order.start property in fdstore.properties is not in correct yyyy-MM-dd format, defaulting to 2000-01-01");
            } catch (ParseException f) {
                throw new FDRuntimeException("Error parsing advance start date, default value");
            }
        }

        try {
            dEnd = SF.parse(get(ADVANCE_ORDER_END));
        } catch (ParseException e) {
            try {
                dEnd = SF.parse("2000-01-01");
                LOGGER.warn("fdstore.advance.order.end property in fdstore.properties is not in correct yyyy-MM-dd format, defaulting to 2000-01-01");
            } catch (ParseException f) {
                throw new FDRuntimeException("Error parsing advance end date, default value");
            }
        }

        return new DateRange(DateUtil.truncate(dStart), DateUtil.truncate(dEnd));
    }

    // marketing admin changes
    public static String getMarketingAdminUrl() {
        return get(MRKTING_ADMIN_URL);
    }

    public static String getDefaultRenewalDP() {
        return get(DLV_PASS_AUTORENEWAL_DEFAULT);
    }

    @Deprecated
    public static boolean IsProduceRatingEnabled() {
        // return Boolean.valueOf(get(PRODUCE_RATING_ENABLED)).booleanValue();
        return true;
    }

    // ratings
    public static String getRatingsSkuPrefixes() {
        return get(PRODUCE_RATING_PREFIXES);
    }

    // freshness guaranteed
    public static boolean IsFreshnessGuaranteedEnabled() {
        return Boolean.valueOf(get(FRESHNESS_GUARANTEED_ENABLED)).booleanValue();
    }

    public static String getFreshnessGuaranteedSkuPrefixes() {
        return get(FRESHNESS_GUARANTEED_PREFIXES);
    }

    // marketing admin changes
    public static String getHPLetterMediaPathForNewUser() {
        return get(HP_LETTER_MEDIA_PATH1);
    }

    public static String getHPLetterMediaPathForOldUser() {
        return get(HP_LETTER_MEDIA_PATH2);
    }

    public static boolean IsHomePageMediaEnabled() {
        return Boolean.valueOf(get(HPLETTER_MEDIA_ENABLED)).booleanValue();
    }

    public static String getHPCategoryLinksFallback() {
        return get(HP_CATEGORY_LINKS_FALLBACK);
    }

    public static String getTemporaryDirectory() {
        return get(TEMP_DIR);
    }

    // CORS
    public static String getCORSDomain() {
        return get(CORS_DOMAIN);
    }

    // iphone email template non-customer
    public static String getMediaIPhoneTemplatePath() {
        return get(PROP_MEDIA_IPHONE_TEMPLATE_PATH);
    }

    public static String getIPhoneEmailSubject() {
        return get(IPHONE_EMAIL_SUBJECT);
    }

    // Gift Card
    @Deprecated
    public static boolean isGiftCardEnabled() {
        // return Boolean.valueOf(get(PROP_GC_ENABLED)).booleanValue();
        return true;
    }

    public static String getGiftCardLandingUrl() {
        return get(PROP_GC_LANDING_URL);
    }

    public static String getGiftcardSkucode() {
        return get(PROP_GIFT_CARD_SKU_CODE);
    }

    public static String getMediaGiftCardTemplatePath() {
        return get(PROP_MEDIA_GIFT_CARD_TEMPLATE_PATH);
    }

    public static String getGiftCardDeptId() {
        return get(PROP_GC_DEPTID);
    }

    public static String getGiftCardCatId() {
        return get(PROP_GC_CATID);
    }

    public static String getGiftCardProdName() {
        return get(PROP_GC_PRODNAME);
    }

    public static int getGiftCardRecipientLimit() {
        return Integer.parseInt(get(PROP_GIFT_CARD_RECIPIENT_MAX));
    }

    public static double getGiftCardMinAmount() {
        return Double.parseDouble(get(PROP_GC_MIN_AMOUNT));
    }

    public static double getGiftCardMaxAmount() {
        return Double.parseDouble(get(PROP_GC_MAX_AMOUNT));
    }

    public static boolean isGiftCardOutOfOrder() {
        return Boolean.valueOf(get(PROP_GC_OOO)).booleanValue();
    }

    // Robin Hood
    public static boolean isRobinHoodEnabled() {
        return Boolean.valueOf(get(ROBIN_HOOD_ENABLED)).booleanValue();
    }

    public static String getRobinHoodLandingUrl() {
        return get(ROBIN_HOOD_LANDING_URL);
    }

    public static String getRobinHoodSkucode() {
        return get(PROP_ROBIN_HOOD_SKU_CODE);
    }

    public static String getRobinHoodStatus() {
        return get(ROBIN_HOOD_STATUS);
    }

    // deals
    public static String getDealsSkuPrefixes() {
        return get(DEALS_SKU_PREFIX);
    }

    public static int getDealsLowerLimit() {
        return Integer.parseInt(get(DEALS_LOWER_LIMIT));
    }

    public static int getDealsUpperLimit() {
        return Integer.parseInt(get(DEALS_UPPER_LIMIT));
    }

    public static int getBurstsLowerLimit() {
        try {
            return Integer.parseInt(get(BURST_LOWER_LIMIT));
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    public static int getBurstUpperLimit() {
        try {
            return Integer.parseInt(get(BURST_UPPER_LIMIT));
        } catch (NumberFormatException e) {
            return 75;
        }
    }

    public static final int getMaxFeaturedDealsForPage() {
        return Integer.parseInt(get(MAX_FEATURED_DEALS_FOR_PAGE));
    }

    public static final int getMaxFeaturedDealsPerLine() {
        return Integer.parseInt(get(MAX_FEATURED_DEALS_PER_LINE));
    }

    public static int getMinFeaturedDealsForPage() {
        return Integer.parseInt(get(MIN_FEATURED_DEALS_FOR_PAGE));
    }

    public static boolean isUPSBlackholeEnabled() {
        return (Boolean.valueOf(get(PROP_UPS_BLACKHOLE_ENABLED))).booleanValue();
    }

    public static boolean isCmsReadonlyOptimization() {
        return Boolean.valueOf(get(PROP_CMS_MOSTLY_READONLY)).booleanValue();
    }

    /**
     * Is Smart Search feature enabled?
     *
     * @deprecated Property is no longer used
     */
    @Deprecated
    public static boolean isSmartSearchEnabled() {
        // return (Boolean.valueOf(get(SMART_SEARCH_ENABLED))).booleanValue();
        return true;
    }

    public static int getSmartstoreNewproductsDays() {
        return Integer.parseInt(get(SMARTSTORE_NEWPRODUCTS_DAYS));
    }

    public static Set<String> getSmartstorePreloadFactors() {
        String frs = get(SMARTSTORE_PRELOAD_FACTORS);

        if (frs == null) {
            return Collections.<String> emptySet();
        }

        String[] factors = frs.split(",");
        Set<String> fs = new HashSet<String>(factors.length);

        for (int i = 0; i < factors.length; i++) {
            String f = factors[i].trim();

            if (f.length() != 0) {
                fs.add(f);
            }
        }

        return fs;
    }

    public static boolean isSmartstoreDataSourcesCached() {
        return Boolean.valueOf(get(SMARTSTORE_CACHE_DATA_SOURCES)).booleanValue();
    }

    public static boolean isSmartstoreOnlineFactorsCached() {
        return Boolean.valueOf(get(SMARTSTORE_CACHE_ONLINE_FACTORS)).booleanValue();
    }

    public static int getSmartstorePersonalizedScoresCacheEntries() {
        return Integer.parseInt(get(SMARTSTORE_PERSONAL_SCORES_CACHE_ENTRIES));
    }

    public static int getSmartstorePersonalizedScoresCacheTimeout() {
        return Integer.parseInt(get(SMARTSTORE_PERSONAL_SCORES_CAHCE_TIMEOUT));
    }

    public static int getCOOLInfoRefreshPeriod() {
        return Integer.parseInt(get(PROP_COOLINFO_REFRESH_PERIOD));
    }

    public static int getSmartStoreDataSourceCacheSize() {
        return Integer.parseInt(get(SMARTSTORE_CACHE_DATA_SOURCES_SIZE));
    }

    public static boolean isDetailedImpressionLoggingOn() {
        return Boolean.valueOf(get(IMPRESSION_LOGGING)).booleanValue();
    }

    public static int getSurveyDefCacheSize() {
        return Integer.parseInt(get(PROP_SURVEYDEF_CACHE_SIZE));
    }

    public static int getRefreshSecsSurveyDef() {
        return Integer.parseInt(get(PROP_REFRESHSECS_SURVEYDEF));
    }

    public static String getFDSurveyHome() {
        return get(PROP_FDSURVEY_HOME);
    }

    public static boolean isSmartSavingsEnabled() {
        return Boolean.valueOf(get(SMART_SAVINGS_FEATURE_ENABLED)).booleanValue();
    }

    // What's Good
    public static boolean isWhatsGoodEnabled() {
        return Boolean.valueOf(get(PROP_FDWHATSGOOD_ENABLED)).booleanValue();
    }

    public static boolean isWhatsGoodPeakProduceEnabled() {
        return Boolean.valueOf(get(PROP_FDWHATSGOOD_PEAKPRODUCE_ENABLED)).booleanValue();
    }

    public static boolean isWhatsGoodButchersBlockEnabled() {
        return Boolean.valueOf(get(PROP_FDWHATSGOOD_BBLOCK_ENABLED)).booleanValue();
    }

    public static String getWhatsGoodRows() {
        return get(PROP_FDWHATSGOOD_ROWS);
    }

    public static String getSuffolkZips() {
        return get(PROP_SUFFOLK_ZIPS);
    }

    public static boolean isWhatsGoodDebugOn() {
        return Boolean.valueOf(get(PROP_FDWHATSGOOD_DEBUG_ENABLED)).booleanValue();
    }

    /**
     * Used for testing, do not call from the App.
     *
     * @param lastRefresh
     */
    public static void setLastRefresh(long lastRefresh) {
        FDStoreProperties.lastRefresh = lastRefresh;
    }

    public static Collection<String> getIssueCreditBccAddresses() {
        String[] bcc = get(CRM_CREDIT_ISSUE_BCC).split(",");
        List<String> bccs = new ArrayList<String>(bcc.length);

        for (int i = 0; i < bcc.length; i++) {
            String addr = bcc[i].trim();

            if (addr.length() != 0) {
                bccs.add(addr);
            }
        }

        return bccs;
    }

    public static boolean IsAdvanceOrderGap() {
        return Boolean.valueOf(get(ADVANCE_ORDER_GAP)).booleanValue();
    }

    public static DateRange getAdvanceOrderNewRange() {
        Date dStart = null;
        Date dEnd = null;

        try {
            dStart = SF.parse(get(ADVANCE_ORDER_NEW_START));
        } catch (ParseException e) {
            try {
                dStart = SF.parse("2000-01-01");
                LOGGER.warn("fdstore.advance.order.newstart property in fdstore.properties is not in correct yyyy-MM-dd format, defaulting to 2000-01-01");
            } catch (ParseException f) {
                throw new FDRuntimeException("Error parsing advance new start date, default value");
            }
        }

        try {
            dEnd = SF.parse(get(ADVANCE_ORDER_NEW_END));
        } catch (ParseException e) {
            try {
                dEnd = SF.parse("2000-01-01");
                LOGGER.warn("fdstore.advance.order.newend property in fdstore.properties is not in correct yyyy-MM-dd format, defaulting to 2000-01-01");
            } catch (ParseException f) {
                throw new FDRuntimeException("Error parsing advance new end date, default value");
            }
        }

        return new DateRange(DateUtil.truncate(dStart), DateUtil.truncate(dEnd));
    }

    public static boolean isGivexBlackHoleEnabled() {
        return (Boolean.valueOf(get(GIVEX_BLACK_HOLE_ENABLED))).booleanValue();
    }

    public static boolean isGivexSecurityFixEnabled() {
        return (Boolean.valueOf(get(GIVEX_SECURITY_FIX_ENABLED))).booleanValue();
    }

    public static String getGCTemplateBaseUrl() {
        return get(PROP_GC_TEMPLATE_BASE_URL);
    }

    public static String getGlobalPopularityScoring() {
        return get(SS_GLOBAL_POPULARITY_SCORING);
    }

    public static String getUserPopularityScoring() {
        return get(SS_USER_POPULARITY_SCORING);
    }

    public static String getShortTermPopularityScoring() {
        return get(SS_SHORT_TERM_POPULARITY_SCORING);
    }

    public static int getOfflineRecommenderNoOfRecentDays() {
        return Integer.parseInt(get(SMARTSTORE_OFFLINE_REC_RECENT_DAYS));
    }

    public static int getOfflineRecommenderMaxAge() {
        return Integer.parseInt(get(SMARTSTORE_OFFLINE_REC_MAX_AGE));
    }

    public static String[] getOfflineRecommenderSiteFeatures() {
        String[] raw = get(SMARTSTORE_OFFLINE_REC_SITE_FEATURES).split("[,;]");
        List<String> siteFeatures = new ArrayList<String>();

        for (String s : raw) {
            s = s.trim();

            if (s.length() > 0) {
                siteFeatures.add(s);
            }
        }

        return siteFeatures.toArray(new String[0]);
    }

    public static int getOfflineRecommenderThreadCount() {
        return Integer.parseInt(get(SMARTSTORE_OFFLINE_REC_THREAD_COUNT));
    }

    public static int getOfflineRecommenderWindowLength() {
        return Integer.parseInt(get(SMARTSTORE_OFFLINE_REC_WINDOW_LENGTH));
    }

    public static boolean isDeptMeatDealsEnabled() {
        return Boolean.valueOf(get(DEPT_MEAT_DEALS)).booleanValue();
    }

    public static boolean isDeptEDLPEnabled() {
        return Boolean.valueOf(get(DEPT_EDLP)).booleanValue();
    }

    public static String getDeptMeatDealsCatId() {
        return get(DEPT_MEAT_DEALS_CATID);
    }

    public static String getDeptEDLPCatId() {
        return get(DEPT_EDLP_CATID);
    }

    public static long getCmsRecommenderRefreshRate() {
        return Long.parseLong(get(SMARTSTORE_CMS_RECOMM_REFRESH_RATE));
    }

    // Mobile
    public static boolean isIphoneLandingEnabled() {
        return Boolean.valueOf(get(MOBILE_IPHONE_LANDING_ENABLED)).booleanValue();
    }

    public static String getIphoneLandingPage() {
        return get(MOBILE_IPHONE_LANDING_PAGE);
    }

    public static boolean isAndroidLandingEnabled() {
        return Boolean.valueOf(get(MOBILE_ANDROID_LANDING_ENABLED)).booleanValue();
    }

    public static String getAndroidLandingPage() {
        return get(MOBILE_ANDROID_LANDING_PAGE);
    }

    public static String getDefaultPickupZoneId() {
        return get(PROP_ZONE_PICKUP_ZIPCODE);
    }

    public static String getDefaultFdxZoneId() {
        return get(PROP_FDX_ZONE_ZIPCODE);
    }

    public static int getNSMAuthSkipSecsForGC() {
        return Integer.parseInt(get(PROP_GC_NSM_AUTHSKIP_SECS));
    }

    public static int getNSMFreqSecsForGC() {
        return Integer.parseInt(get(PROP_GC_NSM_FREQ_SECS));
    }

    @Deprecated
    public static boolean isZonePricingEnabled() {
        // return Boolean.valueOf(get(PROP_ZONE_PRICING_ENABLED)).booleanValue();
        return true;
    }

    public static boolean isZonePricingAdEnabled() {
        return Boolean.valueOf(get(PROP_ZONE_PRICING_AD_ENABLED)).booleanValue();
    }

    public static String getWindowSteeringPromoPrefix() {
        return get(WINDOW_STEERING_PROMOTION_PREFIX);
    }

    /**
     * OSCACHE should be disabled when we are in some development environment. ( = annotation or preview mode ) Jsp-s can refer to this when deciding to use oscache or not.
     *
     * @return true if we are in production mode (use oscache), false if we are in development mode (disable oscache)
     */
    public static boolean useOscache() {
        return !(isAnnotationMode() || getPreviewMode());
    }

    public static boolean isStandingOrdersEnabled() {
        return Boolean.valueOf(get(SO_GLOBAL_ENABLER)).booleanValue();
    }

    public static boolean isStandingOrdersOverlapWindowsEnabled() {
        return Boolean.valueOf(get(SO_OVERLAP_WINDOWS)).booleanValue();
    }

    public static boolean isClientCodesEnabled() {
        return Boolean.valueOf(get(CLIENT_CODES_GLOBAL_ENABLER)).booleanValue();
    }

    // new products revamp
    public static String getNewProductsDeptId() {
        return get(PROP_NEWPRODUCTS_DEPTID);
    }

    public static String getNewProductsCatId() {
        return get(PROP_NEWPRODUCTS_CATID);
    }

    public static String getNewProductsCatFDX() {
        return get(PROP_NEWPRODUCTS_CATID_FDX);
    }

    public static String getNewProductsGrouping() {
        return get(PROP_NEWPRODUCTS_GROUPS);
    }

    public static String getFaqSections() {
        return get(PROP_FAQ_SECTIONS);
    }

    public static String getCrmAddressValiationHelpLink() {
        return get(PROP_CRM_HELP_LINK_ADDR_VALIDATION);
    }

    public static String getCrmGiftCardHelpLink() {
        return get(PROP_CRM_HELP_LINK_GIFT_CARD);
    }

    public static String getCrmMainHelpLink() {
        return get(PROP_CRM_HELP_LINK_MAIN_HELP);
    }

    public static String getCrmCustProfileHelpLink() {
        return get(PROP_CRM_HELP_LINK_CUST_PROFILE);
    }

    public static String getCrmTimeSlotHelpLink() {
        return get(PROP_CRM_HELP_LINK_TIMESLOT);
    }

    public static String getCrmFDUpdatesHelpLink() {
        return get(PROP_CRM_HELP_LINK_FD_UPDATES);
    }

    public static String getCrmPromotionsHelpLink() {
        return get(PROP_CRM_HELP_LINK_PROMOTIONS);
    }

    public static String getCrmCaseMediaHelpLink() {
        return get(PROP_CRM_HELP_LINK_CASE_MEDIA);
    }

    public static String getCrmCaseMoreIssuesHelpLink() {
        return get(PROP_CRM_HELP_LINK_CASE_MORE_ISSUES);
    }

    public static String getCrmCaseCustomerToneHelpLink() {
        return get(PROP_CRM_HELP_LINK_CASE_CUST_TONE);
    }

    public static String getCrmCaseFirstContactHelpLink() {
        return get(PROP_CRM_HELP_LINK_CASE_FIRST_CONTACT);
    }

    public static String getCrmCaseResolvFirstHelpLink() {
        return get(PROP_CRM_HELP_LINK_CASE_RESOLV_FIRST);
    }

    public static String getCrmResolutionSatisfHelpLink() {
        return get(PROP_CRM_HELP_LINK_CASE_RESOL_SATISFY);
    }

    // Email Opt-Down (APPDEV-662)
    public static boolean isEmailOptdownEnabled() {
        return Boolean.valueOf(get(PROP_EMAIL_OPTDOWN_ENABLED)).booleanValue();
    }

    /**
     * Returns API key to access Google Maps service No default value.
     *
     * @return
     */
    public static String getGoogleMapsAPIKey() {
        return get(GMAPS_API_KEY);
    }

    /**
     * APPDEV-1091
     *
     * @return URL to promotion publish servlet
     */
    public static String getPromoPublishURL() {
        return get(PROMO_PUBLISH_URL_KEY);
    }

    public static String getPromoPublishNodeType() {
        return get(PROMO_PUBLISH_NODE_TYPE);
    }

    public static boolean isPromoPublishNodeMaster() {
        return "master".equalsIgnoreCase(get(PROMO_PUBLISH_NODE_TYPE).trim());
    }

    public static boolean isPromoPublishNodeReplica() {
        return "replica".equalsIgnoreCase(get(PROMO_PUBLISH_NODE_TYPE));
    }

    public static String getPromoValidRTStatuses() {
        return get(PROMO_VALID_RT_STATUSES);
    }

    public static int getRedeemCntRefreshPeriod() {
        return Integer.parseInt(get(PROP_REDEMPTION_CNT_REFRESH_PERIOD));
    }

    public static int getRedemptionServerCount() {
        return Integer.parseInt(get(PROP_REDEMPTION_SERVER_COUNT));
    }

    // Delivery Pass at Checkout (APPDEV-664)
    public static boolean isDPCartEnabled() {
        return Boolean.valueOf(get(PROP_DP_CART_ENABLED)).booleanValue();
    }

    // Brand media replacement (APPDEV-1308)
    public static String getBrandMediaIds() {
        return get(PROP_BRAND_MEDIA_IDS);
    }

    public static boolean isPromoLineItemEmailDisplay() {
        return "true".equalsIgnoreCase(get(PROP_PROMO_LINE_ITEM_EMAIL));
    }

    public static int get4mmRefreshInterval() {
        return Integer.parseInt(get(PROP_4MM_REFRESH_INTERVAL));
    }

    public static String getWSDiscountAmountList() {
        return get(PROP_WS_DISCOUNT_AMOUNT_LIST);
    }

    public static void forceRefresh() {
        refresh(true);
    }

    /**
     * Global switch to turn on/off wine ratings display. Defaulted to on.
     *
     * @return
     */
    public static boolean isWineShowRatings() {
        return Boolean.toString(true).equalsIgnoreCase(get(WINE_SHOW_RATINGS_KEY));
    }

    public static boolean isAdvertisingTileEnabled() {
        return Boolean.parseBoolean(get(ADVERTISING_TILE_ENABLED));
    }

    public static void addConfigLoadedListener(ConfigLoadedListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    private static void fireEvent() {
        synchronized (listeners) {
            for (ConfigLoadedListener listener : listeners) {
                listener.configLoaded();
            }
        }
    }

    public static double getWinePriceBucketBound(int index) {
        String value = get(WINE_PRICE_BUCKET_BOUND_PREFIX + index);

        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            double defaultValue;

            if (index == 0) {
                defaultValue = 0.0;
            } else if (index == 1) {
                defaultValue = 10.0;
            } else if (index == 2) {
                defaultValue = 20.0;
            } else {
                defaultValue = 40.0;
            }

            //LOGGER.warn("cannot parse " + WINE_PRICE_BUCKET_BOUND_PREFIX + index + ", using default value " + defaultValue, e);

            return defaultValue;
        }
    }

    public static int getCrmMenuRolesRefreshPeriod() {
        return Integer.parseInt(get(PROP_CRM_MENU_ROLES_REFRESH_PERIOD));
    }

    public static int getCrmLDAPUsersRefreshPeriod() {
        return Integer.parseInt(get(PROP_CRM_LDAP_USERS_REFRESH_PERIOD));
    }

    public static int getCrmAgentsCacheRefreshPeriod() {
        return Integer.parseInt(get(PROP_CRM_AGENTS_CACHE_REFRESH_PERIOD));
    }

    public static String getCrmLDAPPrimaryHostName() {
        return get(PROP_CRM_LDAP_ACCESS_HOST_NAME_PRIMARY);
    }

    public static int getCrmCCDetailsLookupLimit() {
        return Integer.parseInt(get(PROP_CRM_CC_DETAILS_LOOKUP_LIMIT));
    }

    public static boolean isCrmCCSecurityNotificationEnabled() {
        return (Boolean.valueOf(get(PROP_CRM_CC_SECURITY_EMAIL_ENABLED))).booleanValue();
    }

    public static String getCrmCCSecurityEmail() {
        return get(PROP_CRM_CC_SECURITY_EMAIL);
    }

    public static String getCrmCCSecurityEmailSubject() {
        return get(PROP_CRM_CC_SECURITY_EMAIL_SUBJECT);
    }

    public static String getCrmCCDetailsAccessKey() {
        return get(PROP_CRM_CC_DETAILS_ACCESS_KEY);
    }

    public static String getCrmForgotPasswordUrl() {
        return get(PROP_CRM_FORGOT_LDAP_PASSWORD_URL);
    }

    public static String getCrmSecuritySkippedFileTypes() {
        return get(PROP_CRM_SECURITY_SKIP_FILE_TYPES);
    }

    public static String getCrmSecuritySkippedFolders() {
        return get(PROP_CRM_SECURITY_SKIP_FOLDERS);
    }

    public static String getC2CCallBackUrl() {
        return get(PROP_CLICK2CALL_CALL_BACL_URL);
    }

    // SEM Project (APPDEV-1598
    public static String getSemPixels() {
        return get(PROP_SEM_PIXELS);
    }

    public static String getSemConfigs() {
        return get(PROP_SEM_CONFIGS);
    }

    public static int getSemPixelRefreshPeriod() {
        return Integer.parseInt(get(PROP_SEM_REFRESH_PERIOD));
    }

    // APPDEV-1215 Sustainable Seafood
    public static boolean isSeafoodSustainEnabled() {
        return (new Boolean(get(PROP_SEAFOODSUSTAIN_ENABLED))).booleanValue();
    }

    // Property to enable dumping data from SAP for Group Scale Export
    public static boolean isDumpGroupExportEnabled() {
        return (new Boolean(get(PROP_DUMPGROUPEXPORT_ENABLED))).booleanValue();
    }

    // Property to enable validation on storefront for Group Scale Export
    public static boolean isValidationGroupExportEnabled() {
        return (new Boolean(get(PROP_VALIDATIONGROUPEXPORT_ENABLED))).booleanValue();
    }

    // Property to enable validation on storefront for Group Scale Export Input from SAP
    public static boolean isValidationGroupExportInputEnabled() {
        return (new Boolean(get(PROP_VALIDATIONGROUPEXPORTSAPINPUT_ENABLED))).booleanValue();
    }

    // Property to enable or disbale group scale storefront.
    public static boolean isGroupScaleEnabled() {
        return (new Boolean(get(PROP_GROUPSCALE_ENABLED))).booleanValue();
    }

    public static double getDidYouMeanRatio() {
        String ratio = get(DID_YOU_MEAN_RATIO);
        if (ratio == null)
            ratio = "5.0";
        try {
            return Double.parseDouble(ratio);
        } catch (Exception e) {
            return 5.0;
        }
    }

    public static double getDidYouMeanThreshold() {
        String weight = get(DID_YOU_MEAN_THRESHOLD);
        if (weight == null)
            weight = "0.6";
        try {
            return Double.parseDouble(weight);
        } catch (Exception e) {
            return 0.6;
        }
    }

    public static int getDidYouMeanMaxHits() {
        String maxHits = get(DID_YOU_MEAN_MAXHITS);
        if (maxHits == null)
            maxHits = "20";
        try {
            return Integer.parseInt(maxHits);
        } catch (Exception e) {
            return 20;
        }
    }

    public static boolean isPrimaryHomeKeywordsEnabled() {
        return Boolean.valueOf(get(PRIMARY_HOME_KEYWORDS_ENABLED));
    }

    public static boolean isSearchRecurseParentAttributesEnabled() {
        return Boolean.valueOf(get(SEARCH_RECURSE_PARENT_ATTRIBUTES_ENABLED));
    }

    public static boolean isSearchGlobalnavAutocompleteEnabled() {
        return Boolean.valueOf(get(SEARCH_GLOBALNAV_AUTOCOMPLETE_ENABLE));
    }

    public static String getChefstableLabel() {
        return get(PROP_CT_TIMESLOT_LABEL);
    }

    public static boolean isLimitedAvailabilityEnabled() {
        return (new Boolean(get(PROP_LIMITED_AVAILABILITY_ENABLED))).booleanValue();
    }

    public static String getPromotionLabel() {
        return get(PROP_PROMO_TIMESLOT_LABEL);
    }

    public static String getEcoFriendlyLabel() {
        return get(PROP_ECOFRIENDLY_TIMESLOT_LABEL);
    }

    public static String getMinOrderLabel() {
        return get(PROP_MINORDER_TIMESLOT_LABEL);
    }

    public static String getMyBuildingFavsLabel() {
        return get(PROP_BUILDINGFAVS_TIMESLOT_LABEL);
    }

    // APPDEV-3107 SAP upgrade customer messaging
    public static String getTSSpecialMessaging() {
        return get(PROP_TIMESLOT_MSGING);
    }

    public static String getAlcoholRestrictedLabel() {
        return get(PROP_ALCOHOL_TIMESLOT_LABEL);
    }

    public static String getStandingOrderReportToEmail() {
        return get(PROP_STANDING_ORDER_REPORT_TO_EMAIL);
    }

    public static String getStandingOrderReportEmailSubject() {
        return get(PROP_STANDING_ORDER_REPORT_EMAIL_SUBJECT);
    }

    public static boolean isNewFDTimeslotGridEnabled() {
        return (Boolean.valueOf(get(PROP_TIMESLOT_GRID))).booleanValue();
    }

    public static boolean isWSPromotionProductionMode() {
        return (Boolean.valueOf(get(WS_PROMOTION_PRODUCTION_MODE))).booleanValue();
    }

    public static boolean isMktAdminAutouploadEmailEnabled() {
        return (Boolean.valueOf(get(PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_ENABLED))).booleanValue();
    }

    public static String getMktAdminAutouploadReportToEmail() {
        return get(PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_TO);
    }

    public static String getMktAdminAutouploadReportEmailSubject() {
        return get(PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_SUBJECT);
    }

    public static String getMktAdminAutouploadReportCCEmail() {
        return get(PROP_MKTADMIN_AUTOUPLOAD_REPORT_EMAIL_CC);
    }

    public static String getMktAdminAutouploadUrl() {
        return get(PROP_MKTADMIN_AUTOUPLOAD_URL);
    }

    public static String getMktAdminUserName() {
        return get(PROP_MKTADMIN_USER_NAME);
    }

    public static String getMktAdminPassword() {
        return get(PROP_MKTADMIN_PASSWORD);
    }

    public static String getTwoMonthTrailDPSku() {
        return get(PROP_TWO_MONTH_TRIAL_PASS_SKU);
    }

    public static String getTwoMonthTrailDPrice() {
        return get(PROP_TWO_MONTH_TRIAL_PASS_PRICE);
    }

    public static String getOneYearDPSku() {
        return get(PROP_ONE_YEAR_DELIVERY_PASS_SKU);
    }

    public static String getSixMonthDPSku() {
        return get(PROP_SIX_MONTH_DELIVERY_PASS_SKU);
    }

    public static String getOneMonthDPSku() {
        return get(PROP_ONE_MONTH_DELIVERY_PASS_SKU);
    }

    public static boolean isPaymentMethodVerificationEnabled() {
        return (new Boolean(get(PROP_PAYMENT_METHOD_VERIFICATION_ENABLED))).booleanValue();
    }

    public static int getPaymentMethodVerificationLimit() {
        return Integer.parseInt(get(PROP_PAYMENT_METHOD_VERIFICATION_LIMIT));
    }

    public static int getOrderHistoryQueryId() {
        return Integer.parseInt(get(PROP_ORDER_HISTORY_QUERY_ID));
    }

    public static boolean isBuildverEnabled() {
        return Boolean.valueOf(get(BUILDVER_ENABLE));
    }

    public static boolean isQBNewAlertEnabled() {
        return (new Boolean(get(SMARTSTORE_QUICKBUY_NEWALERT_ENABLED))).booleanValue();
    }

    public static boolean isIPhoneSearchFilterDiscontinuedOnly() {
        return Boolean.parseBoolean(get(IPHONE_SEARCH_FILTER_DISCONTINUED_ONLY));
    }

    public static boolean isSessionLoggingEnabled() {
        return (Boolean.valueOf(get(SESSION_LOGGING_ENABLED))).booleanValue();
    }

    public static int getCountryInfoRefreshInterval() {
        return Integer.parseInt(get(PROP_COUNTRY_INFO_REFRESH_INTERVAL));
    }

    // APPDEV-6030 Google Tag Manager
    public static String getGoogleTagManagerKey() {
        return get(PROP_GOOGLE_TAG_MANAGER_KEY);
    }

    // APPDEV-6285 support multiple environments in GTM
    public static String getGoogleTagManagerAuthToken() {
        return get(PROP_GOOGLE_TAG_MANAGER_AUTH_TOKEN);
    }

    public static String getGoogleTagManagerPreviewId() {
        return get(PROP_GOOGLE_TAG_MANAGER_PREVIEW_ID);
    }

    // APPDEV-2072 google analytics key
    public static String getGoogleAnalyticsKey() {
        return get(PROP_GOOGLE_ANALYTICS_KEY);
    }

    public static String getGoogleAnlayticsDomain() {
        return get(PROP_GOOGLE_ANALYTICS_DOMAIN);
    }

    // APPDEV-2072 google analytics key
    public static boolean isGoogleAnalyticsUniversal() {
        return (new Boolean(get(PROP_GOOGLE_ANALYTICS_UNIVERSAL))).booleanValue();
    }

    // APPDEV-991
    public static int getAvailDaysInPastToLookup() {
        return Integer.parseInt(get(PROP_AVAILABILITY_DAYS_IN_PAST_TO_LOOKUP));
    }

    public static String getCSContactDays() {
        return get(CUST_SERV_HOURS_DAYS);
    }

    public static String getCSContactHours() {
        return get(CUST_SERV_HOURS_HOURS);
    }

    public static interface ConfigLoadedListener {

        void configLoaded();
    }

    public static String getVSUserName() {
        return get(PROP_VS_USERNAME);
    }

    public static String getVSPassword() {
        return get(PROP_VS_PASSWORD);
    }

    public static String getVSURL() {
        return get(PROP_VS_URL);
    }

    public static String getStandingOrderCsEmail() {
        return get(PROP_EMAIL_STANDING_ORDER_CS);
    }

    public static String getStandingOrderCsPhone() {
        return get(PROP_PHONE_STANDING_ORDER_CS);
    }

    public static String getFDGivexWebUser() {
        return config.getProperty(PROP_FD_GIVEX_WEB_USER);
    }

    public static String getFDGivexWebUserPassword() {
        return config.getProperty(PROP_FD_GIVEX_WEB_USER_PASSWD);
    }

    public static String getGivexWebServerURL() {
        return config.getProperty(PROP_GIVEX_WEB_SERVER_URL);
    }

    public static int getDayOfWeekForCOSMondayAuths() {
        return Integer.parseInt(get(PROP_DAY_OF_WEEK_FOR_COS_MON_AUTHS));
    }

    /**
     * Used at store-front GUI. Sum of prices in the cart must be higher than this value in order to be able to submit order.
     *
     * @return Soft limit. Default is $50.
     */
    /*
     * public static double getStandingOrderSoftLimit() { return Double.parseDouble(get(PROP_STANDING_ORDER_SOFT_LIMIT)); }
     */

    /**
     * Used at processing Standing Orders. After removing discontinued products from the cart, sum must be higher than this value.
     *
     * @return Hard limit. Default is $50.
     */
    /*
     * public static double getStandingOrderHardLimit() { return Double.parseDouble(get(PROP_STANDING_ORDER_HARD_LIMIT)); }
     */

    public static String getProductPromotionInfoHome() {
        return get(PROP_PRODUCTPROMO_INFO_HOME);
    }

    public static String getFacebookAppKey() {
        return config.getProperty(FACEBOOK_APP_KEY);
    }

    public static String getCloudSpongeDomainKey() {
        return config.getProperty(CLOUD_SPONGE_DOMAIN_KEY);
    }

    public static String getCouldSpongeAddressImports() {
        return config.getProperty(CLOUD_SPONGE_ADDRESS_IMPORTS);
    }

    public static boolean isPendingOrderPopupEnabled() {
        return Boolean.valueOf(get(PENDING_ORDER_POPUP_ENABLED));
    }

    public static boolean isPendingOrderPopupMocked() {
        return Boolean.valueOf(get(PENDING_ORDER_POPUP_MOCKED));
    }

    public static boolean isDDPPEnabled() {
        return (new Boolean(get(PROP_DDPP_ENABLED))).booleanValue();
    }

    public static String getModifyOrderMaxTotal() {
        return config.getProperty(PROP_MODIFY_ORDER_TOTAL_MAX);
    }

    public static int getSameDayMediaAfterCutoffDuration() {
        return Integer.parseInt(get(SAME_DAY_MEDIA_AFTER_CUTOFF));
    }

    public static boolean isTransactionEmailEnabled() {
        return Boolean.valueOf(get(PROP_TRANS_EMAIL_ENABLED)).booleanValue();
    }

    private static String[] tranTypes = null;

    public static boolean isTransactionEmailEnabled(String tranType) {
        if (!isTransactionEmailEnabled())
            return false;
        if (tranTypes == null) {
            String tranTypesStr = get(PROP_TRANS_EMAIL_TYPES);
            if (tranTypesStr != null) {
                StringTokenizer tokens = new StringTokenizer(tranTypesStr, ",");
                tranTypes = new String[tokens.countTokens()];
                int i = 0;
                while (tokens.hasMoreTokens()) {
                    tranTypes[i++] = tokens.nextToken();
                }
            } else
                return false;
        }

        for (int i = 0; i < tranTypes.length; i++) {
            if (tranType.equalsIgnoreCase(tranTypes[i]))
                return true;
        }

        return false;
    }

    public static boolean allowDiscountsOnPremiumSlots() {
        return Boolean.valueOf(get(ALLOW_DISCOUNTS_ON_PREMIUM_SLOT)).booleanValue();
    }

    public static Date getDlvPassNewTCDate() {
        Date date = null;

        try {
            date = SF.parse(get(DLV_PASS_NEW_TC_DATE));
        } catch (ParseException e) {
            try {
                date = SF.parse("2012-05-09");
                LOGGER.warn("fdstore.dlvpass.newtc.date property in fdstore.properties is not in correct yyyy-MM-dd format, defaulting to 2199-01-01");
            } catch (ParseException f) {
                throw new FDRuntimeException("Error parsing dlv tc date, default value");
            }
        }
        return date;

    }

    public static boolean isLightSignupEnabled() {
        return (Boolean.valueOf(get(PROP_LIGHT_SIGNUP_ENABLED))).booleanValue();
    }

    public static boolean isAjaxSignupEnabled() {
        return (Boolean.valueOf(get(PROP_AJAX_SIGNUP_ENABLED))).booleanValue();
    }

    public static boolean isLightSignupAntsEnabled() {
        return (Boolean.valueOf(get(PROP_LIGHT_SIGNUP_ANTS_ENABLED))).booleanValue();
    }

    public static boolean isCoremetricsEnabled() {
        return (Boolean.valueOf(get(PROP_COREMETRICS_ENABLED))).booleanValue();
    }

    public static String getCoremetricsClientId() {
        return get(PROP_COREMETRICS_CLIENT_ID);
    }

    public static String getCoremetricsDataCollectionMethod() {
        return get(PROP_COREMETRICS_DATA_COLLECTION_METHOD);
    }

    public static String getCoremetricsDataCollectionDomain() {
        return get(PROP_COREMETRICS_DATA_COLLECTION_DOMAIN);
    }

    public static String getCoremetricsCookieDomain() {
        return get(PROP_COREMETRICS_COOKIE_DOMAIN);
    }

    public static String getCoremetricsCatIdDirs() {
        return get(PROP_COREMETRICS_CATID_DIRS);
    }

    public static String getCoremetricsCatIdBlog() {
        return get(PROP_COREMETRICS_CATID_BLOG);
    }

    public static String getCoremetricsCatIdOtherPage() {
        return get(PROP_COREMETRICS_CATID_OTHERPAGE);
    }

    public static String getCoremetricsFtpUrl() {
        return get(PROP_COREMETRICS_FTP_URL);
    }

    public static String getCoremetricsFtpPassword() {
        return get(PROP_COREMETRICS_FTP_PASSWORD);
    }

    public static boolean isCoremetricsFtpSecure() {
        return Boolean.valueOf(get(PROP_COREMETRICS_FTP_SECURE));
    }

    public static int getCoremetricsFtpSftpPort() {
        return Integer.parseInt(get(PROP_COREMETRICS_FTP_SFTP_PORT));
    }

    @Deprecated
    public static String getBazaarvoiceFtpUsername() {
        return get(PROP_BAZAARVOICE_FTP_USERNAME);
    }

    @Deprecated
    public static String getBazaarvoiceFtpUrl() {
        return get(PROP_BAZAARVOICE_FTP_URL);
    }

    @Deprecated
    public static String getBazaarvoiceFtpPassword() {
        return get(PROP_BAZAARVOICE_FTP_PASSWORD);
    }

    @Deprecated
    public static String getBazaarvoiceBvapiUrl() {
        return get(PROP_BAZAARVOICE_BVAPI_URL);
    }

    @Deprecated
    public static boolean isBazaarvoiceEnabled() {
        return (Boolean.valueOf(get(PROP_BAZAARVOICE_ENABLED))).booleanValue();
    }

    @Deprecated
    public static String getBazaarvoiceDownloadFeedSourcePath() {
        return get(PROP_BAZAARVOICE_DOWNLOAD_FEED_SOURCEPATH);
    }

    @Deprecated
    public static String getBazaarvoiceDownloadFeedFile() {
        return get(PROP_BAZAARVOICE_DOWNLOAD_FEED_FILE);
    }

    @Deprecated
    public static String getBazaarvoiceDownloadFeedTargetPath() {
        return get(PROP_BAZAARVOICE_DOWNLOAD_FEED_TARGETPATH);
    }

    @Deprecated
    public static List<String> getBazaarvoiceExcludedDepts() {
        String[] source = get(PROP_BAZAARVOICE_EXCLUDED_DEPTS).split(",");

        List<String> theList = new ArrayList<String>();
        for (String dept : source) {
            theList.add(dept.trim());
        }

        return theList;
    }

    public static boolean isGiftCardDonationEnabled() {
        return (Boolean.valueOf(get(PROP_GIFTCARD_DONATION_ENABLED))).booleanValue();
    }

    public static int getDpTcViewLimit() {
        return Integer.parseInt(get(PROP_DLV_PASS_NEW_TC_VIEWLIMIT));
    }

    public static boolean isAutoApplyDonationGiftCardsEnabled() {
        return (Boolean.valueOf(get(PROP_AUTO_APPLY_DONATION_GC))).booleanValue();
    }

    public static boolean isLeadTimeOasAdTurnedOff() {
        return (Boolean.valueOf(get(PROP_LEAD_TIME_OASAD_OFF))).booleanValue();
    }

    public static boolean isDeliveryCenterEnabled() {
        return (Boolean.valueOf(get(PROP_ENABLE_DELIVERYCENTER))).booleanValue();
    }

    public static String getTruckRefreshUrl() {
        return get(PROP_TRUCK_REFRESH_URL);
    }

    public static String getEmployeeRefreshUrl() {
        return get(PROP_EMP_REFRESH_URL);
    }

    public static String getHRPromoCodes() {
        return get(PROP_HRPROMO_CODES);
    }

    public static boolean isAutoLateCreditButtonOn() {
        return (Boolean.valueOf(get(PROP_SHOW_AUTO_LATE_CREDIT_BUTTON))).booleanValue();
    }

    public static String getCohortMatcher() {
        return get(PROP_COHORT_MATCHER);
    }

    public static int getSearchPageTopFavouritesNumber() {
        return Integer.parseInt(get(NUMBER_OF_TOP_FAVOURITES));
    }

    public static int getPresPicksPageTopFavouritesNumber() {
        return Integer.parseInt(get(NUMBER_OF_PRESPICKS_TOP_FAVOURITES));
    }

    public static int getBrowsePageTopFavouritesNumber() {
        return Integer.parseInt(get(NUMBER_OF_BROWSE_TOP_FAVOURITES));
    }

    public static boolean isFavouritesTopNumberFilterSwitchedOn() {
        return (Boolean.valueOf(get(FAVOURITES_NUMBER_SWITCH))).booleanValue();
    }

    public static boolean isIpLocatorEnabled() {
        return (Boolean.valueOf(get(PROP_IP_LOCATOR_ENABLED))).booleanValue();
    }

    public static String getIpLocatorClientId() {
        return get(PROP_IP_LOCATOR_CLIENT_ID);
    }

    public static String getIpLocatorUrl() {
        return get(PROP_IP_LOCATOR_URL);
    }

    public static String getIpLocatorV4Url() {
        return get(PROP_IP_LOCATORV4_URL);
    }

    public static boolean isIpLocatorV4Enabled() {
        return (Boolean.valueOf(get(PROP_IP_LOCATORV4_ENABLED))).booleanValue();
    }

    public static int getIpLocatorTimeout() {
        return Integer.parseInt(get(PROP_IP_LOCATOR_TIMEOUT));
    }

    public static int getIpLocatorRolloutPercent() {
        return Integer.parseInt(get(PROP_IP_LOCATOR_ROLLOUT_PERCENT));
    }

    public static boolean isIpLocatorEventLogEnabled() {
        return (Boolean.valueOf(get(PROP_IP_LOCATOR_EVENT_LOG_ENABLED))).booleanValue();
    }

    public static String getHostUrl() {
        return get(PROP_HOST_URL);
    }

    public static boolean isSocialButtonsEnabled() {
        return (Boolean.valueOf(get(PROP_SOCIAL_BUTTONS_ENABLED))).booleanValue();
    }

    public static boolean isUSQLegalWarningSwitchedOn() {
        return (Boolean.valueOf(get(PROP_USQ_LEGAL_WARNING))).booleanValue();
    }

    public static int getProductRatingRefreshInterval() {
        return Integer.parseInt(get(PRODUCT_RATING_REFRESH_PERIOD));
    }

    public static boolean isProductRatingReload() {
        return (Boolean.valueOf(get(PRODUCT_RATING_RELOAD))).booleanValue();
    }

    public static boolean isAlcoholRestrictionByContextEnabled() {
        return (Boolean.valueOf(get(PROP_ALCOHOLFILTERING_ENABLED))).booleanValue();
    }

    public static boolean isCheckLocalInventoryEnabled() {
        return (Boolean.valueOf(get(CHECK_LOCAL_INVENTORY_ENABLED))).booleanValue();
    }

    public static String getFDCouponManagerHome() {
        return get(PROP_FDCOUPONMGR_HOME);
    }

    public static boolean isSoyDebugMode() {
        return (Boolean.valueOf(get(PROP_SOY_DEBUG))).booleanValue();
    }

    public static boolean isEhCacheEnabled() {
        return (Boolean.valueOf(get(PROP_EH_CACHE_ENABLED))).booleanValue();
    }

    public static boolean isEhCacheManagementEnabled() {
        return (Boolean.valueOf(get(PROP_EH_CACHE_MANAGEMENT_ENABLED))).booleanValue();
    }

    public static boolean isQuickshopIgnorePartialRollout() {
        return (Boolean.valueOf(get(PROP_QS_IGNORE_PARTIAL_ROLLOUT))).booleanValue();
    }

    public static boolean isQuickshopEnabled() {
        return (Boolean.valueOf(get(PROP_QS_ENABLED))).booleanValue();
    }

    public static boolean isPaymentechGatewayEnabled() {
        return Boolean.valueOf(get(PROP_PAYMENTECH_GATEWAY_ENABLED)).booleanValue();
    }

    // APPDEV-2817 Link to DeliveryPass category from top nav of Delivery Info page
    public static boolean doDpDeliveryInfoLink() {
        return (Boolean.valueOf(get(SHOW_DLVPASS_LINK_ON_DELINFO))).booleanValue();
    }

    public static int getAssignedCustomerParamsQueryId() {
        return Integer.parseInt(get(PROP_ASSIGNED_CUSTOMER_PARAMS_QUERY_ID));
    }

    // APPDEV-3100 2013 Wine Transition
    public static String getWineAssid() {
        return get(PROP_WINE_ASSID);
    }

    public static boolean isMediaUtilsReallyClose() {
        return Boolean.valueOf(get(PROP_MEDIA_RENDER_UTILS_REALLY_CLOSE)).booleanValue();
    }

    public static String getMediaUtilsSourceEncoding() {
        return get(PROP_MEDIA_RENDER_UTILS_SOURCE_ENCODING);
    }

    public static boolean isBrowseRolloutRedirectEnabled() {
        return Boolean.valueOf(get(PROP_BROWSE_ROLLOUT_REDIRECT_ENABLED)).booleanValue();
    }

    // optimize
    public static boolean isSocialFooterStatic() {
        return Boolean.valueOf(get(PROP_OPT_SOCIAL_FOOTER_STATIC)).booleanValue();
    }

    public static boolean isIgnoreATPFailureForSO() {
        return Boolean.valueOf(get(PROP_STANDING_ORDER_IGNOREATPFAILURE)).booleanValue();
    }

    // template redesign
    public static int getMaxXsellProds() {
        return Integer.parseInt(get(PROP_MAX_XSELL_PRODS));
    }

    public static int getBrowsePageSize() {
        return Integer.parseInt(get(PROP_BROWSE_PAGESIZE));
    }

    public static int getPresPicksPageSize() {
        return Integer.parseInt(get(PROP_PRES_PICKS_PAGESIZE));
    }

    public static int getStaffPicksPageSize() {
        return Integer.parseInt(get(PROP_STAFF_PICKS_PAGESIZE));
    }

    /*
     * public static String getStaffPicksPickId() { return get(PROP_STAFF_PICKS_PICKID); }
     */

    public static int getStaffPicksPageFeatLimit() {
        return Integer.parseInt(get(PROP_STAFF_PICKS_FEATLIMIT));
    }

    public static int getNewProductsPageSize() {
        return Integer.parseInt(get(PROP_NEWPRODUCTS_PAGESIZE));
    }

    public static int getEcouponPageSize() {
        return Integer.parseInt(get(PROP_ECOUPON_PAGESIZE));
    }

    public static int getSearchPageSize() {
        return Integer.parseInt(get(PROP_SEARCH_PAGESIZE));
    }

    public static int getQuickShopPageSize() {
        return Integer.parseInt(get(PROP_QUICKSHOP_PAGESIZE));
    }

    public static int getPopularCategoriesMax() {
        return Integer.parseInt(get(PROP_BROWSE_POPULAR_CATEGORIES_MAX));
    }

    public static boolean isP3PPolicyEnabled() {
        return Boolean.valueOf(get(PROP_3RDPARTY_P3PENABLED)).booleanValue();
    }

    // SMS notification
    public static String getSingleTouchServiceURL() {
        return get(PROP_ST_PROVIDER_URL);
    }

    public static String getSTUsername() {
        return get(PROP_ST_USERNAME);
    }

    public static String getSTPassword() {
        return get(PROP_ST_PASSWORD);
    }

    public static String getMasterpassLightBoxURL() {
        return get(PROP_EWALLET_MASTERPASS_LIGHT_BOX_URL);
    }

    public static String getMasterpassEnvironment() {
        return get(PROP_EWALLET_MASTERPASS_ENV_PROP_NAME);
    }

    public static String getPayPalEnvironment() {
        return get(PROP_EWALLET_PAYPAL_ENV_PROP_NAME);
    }

    public static String getMasterpassBtnImgURL() {
        return get(PROP_EWALLET_MP_BTN_IMG_URL);
    }

    public static String getMasterpassLogoURL() {
        return get(PROP_EWALLET_MP_LOGO_URL);
    }

    public static String getSTFdxUsername() {
        return get(PROP_ST_FDX_USERNAME);
    }

    public static String getSTFdxPassword() {
        return get(PROP_ST_FDX_PASSWORD);
    }

    public static Integer getSTConnectionTimeoutPeriod() {
        return Integer.parseInt(get(PROP_ST_CONNECTION_TIMEOUT_PERIOD));
    }

    public static int getRTConnectionTimeoutPeriod() {
        return Integer.parseInt(get(PROP_ST_READ_TIMEOUT_PERIOD));
    }

    public static int getSearchCarouselProductLimit() {
        return Integer.parseInt(get(PROP_SEARCH_CAROUSEL_PRODUCT_LIMIT));
    }

    public static boolean getSMSOverlayFlag() {
        return Boolean.valueOf(get(PROP_SMS_OVERLAY_FLAG)).booleanValue();
    }

    public static boolean isPresidentPicksPagingEnabled() {
        return Boolean.valueOf(get(PROP_PRESIDENT_PICK_PAGING_ENABLED)).booleanValue();
    }

    public static boolean isAllDealsCacheEnabled() {
    	return ! isLocalDeployment() && Boolean.valueOf(get(PROP_ALL_DEALS_CACHE_ENABLED)).booleanValue();
    }

    public static boolean isSiteMapEnabled() {
        return (Boolean.valueOf(get(PROP_SITEMAP_ENABLED))).booleanValue();
    }

    public static List<String> getSitemapPasswords() {
        List<String> passwords = new ArrayList<String>();
        for (String token : get(PROP_SITEMAP_PASSWORDS).split(",")) {
            passwords.add(token.trim());
        }

        return passwords;
    }

    // Early AM
    public static int getEarlyAMWindowHour() {
        try {
            return Integer.parseInt(get(PROP_EARLY_AM_HOUR));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getEarlyAMWindowMinute() {
        try {
            return Integer.parseInt(get(PROP_EARLY_AM_MINUTE));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /* Alt. Pickup convenience APPDEV-3623 */
    public static int getDepotCacheRefreshPeriod() {
        try {
            return Integer.parseInt(get(PROP_DEPOT_CACHE_REFRESH));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean isLogRecommenderResults() {
        return (Boolean.valueOf(get(PROP_LOG_RECOMMENDTATIONS_RESULTS))).booleanValue();
    }

    public static boolean isUnitPriceDisplayEnabled() {
        return (Boolean.valueOf(get(UNIT_PRICE_DISPLAY_ENABLED))).booleanValue();
    }

    public static String getLogisticsAPIUrl() {
        return get(PROP_LOGISTICS_API_URL);
    }

    public static String getFdCommerceApiUrl() {
        return get(PROP_FDCOMMERCE_API_URL);
    }

    public static String getPayPalAPIUrl() {
        return get(PROP_PAYPAL_API_URL);
    }
    public static String getMasterPassAPIUrl() {
        return get(PROP_MASTERPASSS_API_URL);
    }
    public static String getOrbitalAPIUrl() {
        return get(PROP_ORBITAL_API_URL);
    }
    public static String getOMSAPIUrl() {
        return get(PROP_OMS_API_URL);
    }

    public static String getGiveXGatewayEndPoint() {
        return get(PROP_GIVEXGATEWAY_ENDPOINT);
    }

    public static String getLogisticsCompanyCode() {
        return get(PROP_LOGISTICS_COMPANY_CODE);
    }

    public static int getLogisticsConnectionTimeout() {
        try {
            return Integer.parseInt(get(PROP_LOGISTICS_CONNECTION_TIMEOUT));
        } catch (NumberFormatException e) {
            return 60;
        }
    }

    public static int getLogisticsConnectionPool() {
        try {
            return Integer.parseInt(get(PROP_LOGISTICS_CONNECTION_POOL));
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    public static int getLogisticsConnectionReadTimeout() {
        try {
            return Integer.parseInt(get(PROP_LOGISTICS_CONN_READ_TIMEOUT));
        } catch (NumberFormatException e) {
            return 300;
        }
    }

    public static int getLogisticsConnectionRequestTimeout() {
        try {
            return Integer.parseInt(get(PROP_LOGISTICS_CONNECTION_REQUEST_TIMEOUT));
        } catch (NumberFormatException e) {
            return 60;
        }
    }

    public static String getFDExtoleManagerHome() {
        return get(PROP_FDEXTOLEMGR_HOME);
    }

    // Recaptcha getter methods
    public static String getRecaptchaPublicKey() {
        return StringUtils.defaultIfEmpty(get(PROP_RECAPTCHA_PUBLIC_KEY), "6LeEWAITAAAAAJTkH82Z7gsg1J28IHsjtPjBoPHX");
    }

    public static String getRecaptchaPrivateKey() {
        return StringUtils.defaultIfEmpty(get(PROP_RECAPTCHA_PRIVATE_KEY), "6LeEWAITAAAAACmiHCIyTDfZz_SkCPtPIN_c1HSN");
    }

    public static int getMaxInvalidLoginAttempt() {
        try {
            return Integer.parseInt(get(PROP_MAX_INVALID_LOGIN_ATTEMPT));
        } catch (Exception e) {
            return 5;
        }
    }

    public static int getQuickShopResultMaxLimit() {
        try {
            return Integer.parseInt(get(QUICKSHOP_ALL_ITEMS_MAX));
        } catch (NumberFormatException e) {
            return 600;
        }
    }

    public static int getOrderHistoryFromInMonths() {
        try {
            return Integer.parseInt(get(PROP_ORDER_HISTORY_FROM_IN_MONTHS));
        } catch (NumberFormatException nfe) {
            return 13;
        }
    }

    public static int getPastOrdersVisibleItemsCount() {
        return Integer.parseInt(get(PROP_QUICKSHOP_PAST_ORDERS_VISIBLE_MENU_ITEMS_COUNT));
    }

    public static String getTipRangeConfig() {
        return get(PROP_TIP_RANGE_CONFIG);
    }

    // APPDEV-4270 : WebPerformacne issue code changes for subdomains
    public static String getSubdomains() {
        List<String> domains = new ArrayList<String>();
        String subDomainList = get(SUB_DOMAIN);
        String responseDomain = "";
        if (subDomainList != null && !"".equals(subDomainList)) {
            domains = Arrays.asList(subDomainList.split(","));
            responseDomain = domains.get(new Random().nextInt(domains.size()));
        }
        return responseDomain;
    }

    public static boolean isProductFamilyEnabled() {
        return (new Boolean(get(PROP_PRODUCTFAMILY))).booleanValue();
    }

    // APPDEV - 4159 - Creating Getter Methods to get the value of maximum size of columns in promo table

    public static int getPromoOldCoumnSize() {
        return Integer.parseInt(get(PROMO_OLDCOLUMN_MAX_LIMIT));
    }

    public static int getPromoNewCoumnSize() {
        return Integer.parseInt(get(PROMO_NEWCOLUMN_MAX_LIMIT));
    }

    public static int getCategoryTopItemCacheSize() {
        return Integer.parseInt(get(CATEGORY_TOP_ITEM_CACHE_SIZE));
    }

    public static int getCategoryTopItemCacheMaximalSize() {
        return Integer.parseInt(get(CATEGORY_TOP_ITEM_CACHE_MAXIMAL_SIZE));
    }

    public static int getProductSamplesMaxBuyProductsLimit() {
        return Integer.parseInt(get(PROP_PRODUCT_SAMPLES_MAX_BUY_PRODUCTS_LIMIT));
    }

    public static int getProductSamplesMaxQuantityLimit() {
        return Integer.parseInt(get(PROP_PRODUCT_SAMPLES_MAX_BUY_QUANTITY_LIMIT));
    }

    public static String getFeedPublishURL() {
        return get(PROP_FEED_PUBLISH_URL);
    }
    
    public static String getFeedPublishCheck() {
        return get(PROP_FEED_PUBLISH_FROM_BKOFFICE);
    }
    
    public static String getFeedPublishBackofficeURL() {
        return get(PROP_FEED_PUBLISH_BKOFFICE_URL);
    }

    public static String getCtCapacityEligibleProfiles() {
        return get(CTCAPACITY_ELIGIBLE_PROFILES);
    }

    public static boolean isCoreNonCoreGlobalNavSwitchEnabled() {
        return Boolean.parseBoolean(get(PROP_CORE_NON_CORE_GLOBAL_NAV_SWITCH_ENABLED));
    }

    public static boolean getAtpAvailabiltyMockEnabled() {
        return (Boolean.valueOf(get(PROP_ATP_AVAILABILTY_MOCK_ENABLED))).booleanValue();
    }

    public static String getCSContactDaysFDX() {
        return get(CUST_SERV_HOURS_DAYS_FDX);
    }

    public static String getCSContactHoursFDX() {
        return get(CUST_SERV_HOURS_HOURS_FDX);
    }

    public static String getAnnounceEmailFDX() {
        return get(PROP_EMAIL_FDX_ANNOUNCE);
    }

    public static String getOrderEmailFDX() {
        return get(PROP_EMAIL_FDX_ORDER);
    }

    public static String getActServiceEmailFDX() {
        return get(PROP_EMAIL_FDX_ACTSERVICE);
    }

    public static String getSidekicksEmailFDX() {
        return get(PROP_EMAIL_FDX_SIDEKICKS);
    }

    public static String getProductRequestEmailFDX() {
        return get(PROP_EMAIL_FDX_PRODUCT_REQUEST);
    }

    public static String getMiddleTierProviderURL() {
        return get(PROP_MIDDLETIER_PROVIDER_URL);
    }

    public static String getHolidayMealBundleCategoryId() {
        return get(PROP_HOLIDAY_MEAL_BUNDLE_CATEGORY_ID);
    }

    public static String getSocialOneAllSubdomain() {
        return get(PROP_SOCIAL_ONEALL_SUBDOMAIN);
    }

    public static String getSocialOneAllPrivateKey() {
        return get(PROP_SOCIAL_ONEALL_PRIVATEKEY);
    }

    public static String getSocialOneAllPublicKey() {
        return get(PROP_SOCIAL_ONEALL_PUBLICKEY);
    }

    public static String getSocialOneAllPostUrl() {
        return get(PROP_SOCIAL_ONEALL_POSTURL);
    }

    public static boolean isSocialLoginEnabled() {
        return (Boolean.valueOf(get(PROP_SOCIAL_LOGIN_ENABLED))).booleanValue();
    }

    public static boolean isLocalDeployment() {
        return (Boolean.valueOf(get(PROP_DEPLOYMENT_LOCAL))).booleanValue();
    }

    public static boolean isDeveloperDisableAvailabilityLookup() {
        return (Boolean.valueOf(get(PROP_DEVELOPER_DISABLE_AVAIL_LOOKUP))).booleanValue();
    }

    public static boolean getSmsOrderConfirmation() {
        return Boolean.parseBoolean(get(PROP_FDX_SMS_ORDER_CONFIRMATION));
    }

    public static boolean getSmsOrderModification() {
        return Boolean.parseBoolean(get(PROP_FDX_SMS_ORDER_MODIFICATION));
    }

    public static boolean getSmsOrderCancel() {
        return Boolean.parseBoolean(get(PROP_FDX_SMS_ORDER_CANCEL));
    }

    public static boolean isFdxLocationbarEnabled() {
        return Boolean.parseBoolean(get(PROP_FDX_LOCATIONBAR));
    }

    public static boolean isFdxTabEnabled() {
        return Boolean.parseBoolean(get(PROP_FDX_LOCATIONBAR_FDXTAB));
    }

    public static boolean isTCEnabled() {
        return (Boolean.valueOf(get(PROP_FD_TC_ENABLED))).booleanValue();
    }

    public static Properties getConfig() {
        return config;
    }

    public static String getFdxAppUrl_Apple() {
        return get(PROP_FDX_APP_APPLE_URL);
    }

    public static String getCMSAdminServiceURL() {
        return get(PROP_CMS_ADMIN_REST_URL);
    }

    public static String getCMSAdminUiURL() {
        return get(PROP_CMS_ADMIN_UI_URL);
    }

    public static boolean isETippingEnabled() {
        return Boolean.parseBoolean(get(PROP_ETIPPING_ENABLED));
    }

    public static String getPropExtoleSftpHost() {
        return get(PROP_EXTOLE_SFTP_HOST);
    }

    public static String getPropExtoleSftpUsername() {
        return get(PROP_EXTOLE_SFTP_USERNAME);
    }

    public static String getPropExtoleSftpFileDownloaderRemoteWorkdir() {
        return get(PROP_EXTOLE_SFTP_FILE_DOWNLOADER_REMOTE_WORKDIR);
    }

    public static String getPropBaseUrl() {
        return get(PROP_EXTOLE_BASE_URL);
    }

    public static String getPropEndpointCreateConversion() {
        return get(PROP_EXTOLE_ENDPOINT_CREATE_CONVERSION);
    }

    public static String getPropEndpointApproveConversion() {
        return get(PROP_EXTOLE_ENDPOINT_APPROVE_CONVERSION);
    }

    public static String getPropExtoleApiKey() {
        return get(PROP_EXTOLE_API_KEY);
    }

    public static String getPropExtoleApiSecret() {
        return get(PROP_EXTOLE_API_SECRET);
    }

    public static String getPropSchemeHttps() {
        return get(PROP_SCHEME_HTTPS);
    }

    public static String getPropExtoleSftpFileDownloaderLocalWorkdir() {
        return get(PROP_EXTOLE_SFTP_FILE_DOWNLOADER_LOCAL_WORKDIR);
    }

    public static String getPropBaseFileName() {
        return get(PROP_EXTOLE_BASE_FILE_NAME);
    }

    public static boolean isCRMReferralHistoryEnabled() {
        return Boolean.parseBoolean(get(PROP_CRM_REFERRAL_HISTORY_PAGE_ENABLED));
    }

    public static boolean isExtoleRafEnabled() {
        return Boolean.parseBoolean(get(PROP_EXTOLE_RAF_ENABLED));
    }

    /**
     * If true, CSR agents are allowed to use new XC pages in masquerade mode. Defaulted to true
     *
     * @ticket APPDEV-4660
     * @return
     */
    public static boolean isExpressCheckoutEnabledForCSR() {
        return Boolean.parseBoolean(get(PROP_ENABLE_XC_FOR_CRM_AGENTS));
    }

    public static String getPropExtoleMicrositeUrl() {
        return get(PROP_EXTOLE_MICROSITE_URL);
    }

    public static String getPropExtoleMicrositeSubUrl() {
        return get(PROP_EXTOLE_MICROSITE_SUB_URL);
    }

    public static String getPropExtoleMicrositeGlobalNavUrl() {
        return get(PROP_EXTOLE_MICROSITE_GLOBAL_NAV_URL);
    }

    public static String getAvalaraBaseURL() {
        return StringUtils.defaultString(get(PROP_AVALARA_BASE_URL));
    }

    public static String getAvalaraAccountNumber() {
        return StringUtils.defaultString(get(PROP_AVALARA_ACCOUNT_NUMBER));
    }

    public static String getAvalaraLicenseKey() {
        return StringUtils.defaultString(get(PROP_AVALARA_LICENSE_KEY));
    }

    public static boolean getAvalaraTaxEnabled() {
        return BooleanUtils.toBoolean(get(PROP_AVALARA_TAX_ENABLED));
    }

    public static String getAvalaraCompanyCode() {
        return StringUtils.defaultString(get(PROP_AVALARA_COMPANY_CODE), "0011");
    }

    public static int getAvalaraCronThreadCount() {
        return Integer.parseInt(get(PROP_AVALARA_CRON_THREAD_COUNT) != null ? get(PROP_AVALARA_CRON_THREAD_COUNT) : "10");
    }

    //@ IBM silverpopup chnages
    public static String getIBMAccessTokenURL(){
    	return get(IBM_ACCESSTOKEN_URL);
    }

    public static String getIBMPushNotificationURL() {
        return get(IBM_PUSHNOTIFICATION_URL);
    }
    public static String getIBMWatsonEmailCampaignUrl() {
        return get(IBM_WATSON_EMAIL_CAMPAIGN_URL);
    }
    
    public static String getIBMClientID() {
		return get(IBM_CLIENT_ID);
	}
    
    public static String getIBMCampaignClientID() {
		return get(IBM_CAMPAIGN_CLIENT_ID);
	}

    public static String getIBMClientSecret() {
		return get(IBM_CLIENT_SECRET);
	}
    
    public static String getIBMCampaignClientSecret() {
		return get(IBM_CAMPAIGN_CLIENT_SECRET);
	}

    public static String getIBMRefreshToken() {
		return get(IBM_REFRESH_TOKEN);
	}
    
    public static String getIBMCampaignRefreshToken() {
		return get(IBM_CAMPAIGN_REFRESH_TOKEN);
	}
    //end IBM silverpopup end

    public static int getSO3ActivateCutoffTime() {
        return Integer.parseInt(get(PROP_SO3_ACTIVATE_CUTOFF_TIME));
    }

    public static String getFDBrandProductsAdManagerHome() {
        return get(PROP_FD_BRAND_PRODUCTS_AD_HOME);
    }

    // Eastern Daylight Time <> Eastern Standard Time (Daylight Saving Time) conversion enabled
    public static boolean isEdtEstTimeslotConversionEnabled() {
        return Boolean.valueOf(get(PROP_EDT_EST_TIMESLOT_CONVERSION_ENABLED)).booleanValue();
    }

    public static boolean isPayPalEnabled() {
        return Boolean.valueOf(get(PROP_EWALLET_PAYPAL_ENABLED)).booleanValue();
    }

    public static boolean isMasterpassEnabled() {
        return Boolean.valueOf(get(PROP_EWALLET_MASTERPASS_ENABLED)).booleanValue();
    }

    public static boolean isAddressMismatchEnabled() {
        return Boolean.valueOf(get(PROP_ADDRESS_MISMATCH_ENABLED)).booleanValue();
    }

    public static String getErpsyLinkStorefrontFD() {
        return get(PROP_ERPSYLINK_STOREFRONT_FD);
    }

    public static String getErpsyLinkStorefrontFDX() {
        return get(PROP_ERPSYLINK_STOREFRONT_FDX);
    }

    public static boolean isHookLogicBlackHoleEnabled() {
        return (Boolean.valueOf(get(PROP_HOOK_LOGIC_BLACKHOLE_ENABLE))).booleanValue();
    }

    public static boolean isHookLogicForCategoriesEnabled() {
        return (Boolean.valueOf(get(PROP_HOOK_LOGIC_CATEGORY_ENABLE))).booleanValue();
    }

    public static boolean getHookLogicAllowOwnRows() {
        return (Boolean.valueOf(get(PROP_HOOK_LOGIC_ALLOW_OWN_ROWS))).booleanValue();
    }

    public static int getHlProductsCount() {
        return Integer.parseInt(get(PROP_HL_PRODUCTS_COUNT));
    }

    public static int getHlOrderFeedMins() {
        return Integer.parseInt(get(PROP_HOOK_LOGIC_ORDER_FEED_MINS));
    }

    public static String getDefaultFdxPlantID() {
        return get(PROP_DEFAULT_FDX_PLANTID);
    }

    public static String getDefaultFdxDistributionChannel() {
        return get(PROP_DEFAULT_FDX_DISTRIBUTION_CHANNEL);
    }

    public static String getDefaultFdxDistributionChannelParent() {
        return get(PROP_DEFAULT_FDX_DISTRIBUTION_CHANNEL_PARENT);
    }

    public static String getDefaultFdxSalesOrg() {
        return get(PROP_DEFAULT_FDX_SALESORG);
    }

    public static String getDefaultFdxSalesOrgParent() {
        return get(PROP_DEFAULT_FDX_SALESORG_PARENT);
    }

    public static String getDefaultFdPlantID() {
        return get(PROP_DEFAULT_FD_PLANTID);
    }

    public static String getDefaultFdDistributionChannel() {
        return get(PROP_DEFAULT_FD_DISTRIBUTION_CHANNEL);
    }

    public static String getDefaultFdSalesOrg() {
        return get(PROP_DEFAULT_FD_SALESORG);
    }

    public static Set<String> getBrowseAggregatedCategories() {
        Set<String> result = new HashSet<String>();
        String aggregatedCategoriesText = get(PROP_BROWSE_AGGREGATED_CATEGORIES);
        if (aggregatedCategoriesText != null) {
            String[] aggregatedCategoryItems = aggregatedCategoriesText.split(",");
            if (aggregatedCategoryItems != null) {
                for (String aggregatedCategoryItem : aggregatedCategoryItems) {
                    if (aggregatedCategoryItem != null) {
                        result.add(aggregatedCategoryItem);
                    }
                }
            }
        }
        return result;
    }

    public static boolean isGroupScalePerfImproveEnabled() {
        return (Boolean.valueOf(get(PROP_GROUP_SCALE_PERF_IMPROVE_ENABLED))).booleanValue();
    }

    public static double getPriceConfigConversionLimit() {
        return Double.parseDouble(get(PROP_PRICE_CONFIG_CONVERSION_LIMIT));
    }

    public static String getPriceConfigDepartments() {
        return get(PROP_PRICE_CONFIG_DEPARTMENTS);
    }

    public static String getUnbxdApiKey() {
        return get(PROP_UNBXD_API_KEY);
    }

    public static String getUnbxdSiteKey() {
        return get(PROP_UNBXD_SITE_KEY);
    }

    public static String getUnbxdCosSiteKey() {
        return get(PROP_UNBXD_COS_SITE_KEY);
    }

    public static String getUnbxdBaseUrl() {
        return get(PROP_UNBXD_BASE_URL);
    }

    public static boolean getUnbxdFallbackOnError() {
        return get(PROP_UNBXD_FALLBACK_ON_ERROR).equalsIgnoreCase("true");
    }

    public static String getUnbxdTrackingServiceBaseURL() {
        return get(PROP_UNBXD_TRACKING_BASE_URL);
    }

    public static boolean isProductFeedGenerationDeveloperModeEnabled() {
        return Boolean.valueOf(get(PROP_PRODUCT_FEED_GENERATION_DEVELOPER_MODE_ENABLED)).booleanValue();
    }

    public static boolean isPropDonationProductSamplesEnabled() {
        return (Boolean.valueOf(get(PROP_DONATION_PRODUCT_SAMPLES_ENABLED))).booleanValue();
    }

    public static boolean isSF2_0_AndServiceEnabled(String beanName) {
    	return ((Boolean.valueOf(get(PROP_SF_2_0_ENABLED))).booleanValue()&&FDEcommProperties.isServiceEnabled(beanName));
    }

    public static boolean isMealBundleCartonLinkEnabled() {
        return (Boolean.valueOf(get(PROP_MEALBUNDLE_CARTONVIEW_ENABLED))).booleanValue();
    }

    public static List<String> getPropDonationProductSamplesId() {
        return getAsList(PROP_DONATION_PRODUCT_SAMPLES_ID);
    }

    public static List<String> getViewcartNewCustomerCarouselSiteFeatures() {
        return getAsList(PROP_VIEWCART_PAGE_NEW_CUSTOMER_CAROUSEL_SITE_FEATURES);
    }

    public static List<String> getViewcartCurrentCustomerCarouselSiteFeatures() {
        return getAsList(PROP_VIEWCART_PAGE_CURRENT_CUSTOMER_CAROUSEL_SITE_FEATURES);
    }

    public static List<String> getCheckoutNewCustomerCarouselSiteFeatures() {
        return getAsList(PROP_CHECKOUT_PAGE_NEW_CUSTOMER_CAROUSEL_SITE_FEATURES);
    }

    public static List<String> getCheckoutCurrentCustomerCarouselSiteFeatures() {
        return getAsList(PROP_CHECKOUT_PAGE_CURRENT_CUSTOMER_CAROUSEL_SITE_FEATURES);
    }

    public static boolean shouldShowDeliveryFeeForCheckoutPageCosCustomer() {
    	 return (Boolean.valueOf(get(PROP_CHECKOUT_PAGE_COS_CUSTOMER_DISPLAY_DELIVERY_FEE_HEADER))).booleanValue();
    }
    public static int getUserCartSaveInterval() {
        return Integer.parseInt(get(PROP_USER_CART_SAVE_INTERVAL));
    }

    public static String getHomepageRedesignCurrentUserContainerContentKey() {
        return get(PROP_HOMEPAGE_REDESIGN_CURRENT_USER_CONTAINER_CONTENT_KEY);
    }

    public static String getHomepageRedesignNewUserContainerContentKey() {
        return get(PROP_HOMEPAGE_REDESIGN_NEW_USER_CONTAINER_CONTENT_KEY);
    }

    public static String getPropHomepageRedesignCurrentCosUserContainerContentKey() {
		return get(PROP_HOMEPAGE_REDESIGN_CURRENTCOS_USER_CONTAINER_CONTENT_KEY);
	}

    public static String getPropHomepageRedesignNewCosUserContainerContentKey() {
		return get(PROP_HOMEPAGE_REDESIGN_NEWCOS_USER_CONTAINER_CONTENT_KEY);
	}

	public static int getHomepageRedesignProductLimitMax() {
        return Integer.parseInt(get(PROP_HOMEPAGE_REDESIGN_MODULE_PRODUCT_LIMIT_MAX));
    }

    public static String getHomepageRedesignPrespicksCategoryId() {
        return get(PROP_HOMEPAGE_REDESIGN_PRESPICKS_CATEGORY_ID);
    }

    public static String getHomepageRedesignStaffpicksCategoryId() {
        return get(PROP_HOMEPAGE_REDESIGN_STAFFPICKS_CATEGORY_ID);
    }

    public static String getPropPlant1300PlantIndicator() {
        return get(PROP_PLANT1300_PRICE_INDICATOR);
    }

    public static String getPropPlant1310PlantIndicator() {
        return get(PROP_PLANT1310_PRICE_INDICATOR);
    }

    public static String getPropPlantWDCPlantIndicator() {
        return get(PROP_PLANTWDC_PRICE_INDICATOR);
    }

    public static List<String> getMealKitMaterialGroup() {
        String materialKits = get(PROP_MEAL_KIT_MATERIAL_GROUP);
        List<String> materialKitsList = new ArrayList<String>();
        if (materialKits != null) {
            materialKitsList = Arrays.asList(materialKits.split(","));
        }

        return materialKitsList;
    }

    public static List<String> getHookLogicExcludedDepOrCatIds() {
        String excludedDeptOrCatIds = get(PROP_HOOK_LOGIC_CATEGORY_EXCLUDE_DEP_CAT_IDS);
        List<String> excludedDeptOrCatIdsList = new ArrayList<String>();
        if (excludedDeptOrCatIds != null) {
            excludedDeptOrCatIdsList = Arrays.asList(excludedDeptOrCatIds.toLowerCase().split(","));
        }

        return excludedDeptOrCatIdsList;
    }

    public static boolean isPickPlantIdReqForMatSalesOrgExport() {
        return (Boolean.valueOf(get(PROP_MAT_SALESORG__EXPORT_PICKPLANT_VALIDATION_ENABLED))).booleanValue();
    }

    public static boolean isExtraLogForLoginFailsEnabled() {
        return (Boolean.valueOf(get(PROP_EXTRA_LOG_FOR_LOGIN_FAILS_ENABLED))).booleanValue();
    }

    public static boolean isQSTopItemsPerfOptimizationEnabled(){
    	return (Boolean.valueOf(get(PROP_QS_TOP_ITEMS_PERF_OPT_ENABLED))).booleanValue();
    }

    public static boolean isZipCheckOverLayEnabled(){
    	return (Boolean.valueOf(get(PROP_ZIP_CHECK_OVER_LAY_ENABLED))).booleanValue();
    }

	public static boolean isObsoleteMergeCartPageEnabled() {
    	return (Boolean.valueOf(get(PROP_OBSOLETE_MERGECARTPAGE_ENABLED))).booleanValue();
	}

	public static String getClusterName() {
		return get(PROP_CLUSTER_NAME);
	}

	public static String getNodeName() {
		return get(PROP_NODE_NAME);
	}

	public static boolean isProductCacheOptimizationEnabled(){
        return (Boolean.valueOf(get(PROP_PRODUCT_CACHE_OPTIMIZATION_ENABLED))).booleanValue();
	}

	public static String getRequestSchemeForRedirectUrl(){
		return get(PROP_REQUEST_SCHEME_FOR_REDIRECT_URL);
	}

	public static boolean isPaymentVerificationEnabled() {
		return (Boolean.valueOf(get(PROP_PAYMENT_VERIFICATION_ENABLED))).booleanValue();
	}

	public static boolean isDfpEnabled() {
		return (Boolean.valueOf(get(PROP_DFP_ENABLED))).booleanValue();
	}

	public static String getDfpId() {
		return get(PROP_DFP_ID);
	}


	public static boolean isTLSSHAEnabledForPaymentGateway() {
		return (Boolean.valueOf(get(PAYMENT_TLSSHA_ENABLED))).booleanValue();
	}

	public static boolean isRefreshZoneInfoEnabled() {
		return (Boolean.valueOf(get(PROP_REFRESHZONE_ENABLED))).booleanValue();
	}

	public static boolean isGlobalSF2_0PropertyEnabled() {
		return (Boolean.valueOf(get(GLOBAL_SF2_0_ENABLED))).booleanValue();
		}

    public static boolean isDebitSwitchNoticeEnabled() {
        return (Boolean.valueOf(get(PROP_DEBIT_SWITCH_NOTICE_ENABLED))).booleanValue();
    }

    public static boolean isLoggingAkamaiEdgescapgeHeaderInfoEnabled() {
        return (Boolean.valueOf(get(PROP_LOG_AKAMAI_HEADER_ENABLED))).booleanValue();
    }


	 public static int getFdcTransitionLookAheadDays() {
			return Integer.parseInt(get(PROP_FDC_TRANSITION_LOOK_AHEAD_DAYS));
		}


		public static String getDefaultCustomerServiceContact(){
			return get(CUSTOMER_SERVICE_CONTACT);
		}
		public static String getChefsTableCustomerServiceContact(){
			return get(CHEFSTABLE_CONTACT_NUMBER);
		}
		public static String getFoodKickCustomerServiceContact(){
			return get(FOODKICK_SERVICE_CONTACT);
		}
		public static String getPennsylvaniaCustomerServiceContact(){
			return get(PENNSYLVANIA_SERVICE_CONTACT);
		}

		//For SAP to get the Price of the Product as zero as it does not accept the Price to be zero we send as Free
		public static boolean getEnableFreeProduct(){
			return (Boolean.valueOf(get(PROP_ENABLE_FREE_PRODUCT))).booleanValue();
		}
		
		/**
		 * Website toggle to determine if we have turned on the feature for both website and mobile api <BR>
		 * to pull nutrition information from the same soy template.
		 * @return defaults to off, overide by an entry enable.website.mobile.same.nutrition.soy in fdstore.properties
		 */
		public static boolean getEnableWebsiteMobileSameNutritionSoy() {			
			return (Boolean.valueOf(get(PROP_ENABLE_WEBSITE_MOBILE_SAME_NUTRITION_SOY))).booleanValue();
		}
		

		public static boolean getEnableFDXDistinctAvailability() {			
			return (Boolean.valueOf(get(PROP_ENABLE_FDX_DISTINCT_AVAILABILITY))).booleanValue();
		}
		
		public static boolean isRepeatWarmupEnabled() {			
			return (Boolean.valueOf(get(PROP_ENABLE_REPEAT_WARMUP))).booleanValue();
		}		
	public static long getDefaultAuthCodeExpiration() {
		return Long.parseLong(get(DEFAULT_CODE_EXPIRATION));
	}

	public static long getDefaultAccessTokenExpiration() {
		return Long.parseLong(get(DEFAULT_TOKEN_EXPIRATION));
	}

	public static long getDefaultRefreshTokenExpiration() {
		return Long.parseLong(get(DEFAULT_REFRESH_TOKEN_EXPIRATION));
	}

	public static String getOAuth2ClientIds() {
		return get(OAUTH2_CLIENT_IDS);
	}

	public static String getOAuth2ClientSecrets() {
		return get(OAUTH2_CLIENT_SECRETS);
	}

	public static String getOAuth2ClientRedirectUris(String clientId) {
		return get(OAUTH2_CLIENT_REDIRECT_URIS + "." + clientId);
	}


	public static boolean isNewProductsForFdcUsingFdEnabled(){
		return (Boolean.valueOf(get(PROP_FDC_NEW_BACKIN_USE_FD_ENABLED))).booleanValue();
	}

	public static int getInConditionLimit() {
		return Integer.parseInt(get(DATABASE_IN_CONDITION_LIMIT));
	}
	
	public static boolean isDlvPassFreeTrialOptinFeatureEnabled(){
		return (Boolean.valueOf(get(PROP_FD_DP_FREE_TRIAL_OPTIN_FEATURE_ENABLED))).booleanValue();
	}
	
}
