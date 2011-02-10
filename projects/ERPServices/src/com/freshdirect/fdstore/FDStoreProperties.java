package com.freshdirect.fdstore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;


public class FDStoreProperties {

	private static final Category LOGGER = LoggerFactory.getInstance( FDStoreProperties.class );

        public static interface ConfigLoadedListener {
            void configLoaded();
        }

        private static List<ConfigLoadedListener> listeners = new ArrayList<ConfigLoadedListener>();

	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd");

	private final static String PROP_PROVIDER_URL		= "fdstore.providerURL";
	private final static String PROP_INIT_CTX_FACTORY	= "fdstore.initialContextFactory";

	private final static String PROP_CRM_GEOCODELINK 	= "fdstore.crm.geocodeLink";
	private final static String PROP_CRM_CASE_LIST_LENGTH = "fdstore.crm.caseListLength";
	private final static String PROP_CRM_CASE_HISTORY_LIST_LENGTH = "fdstore.crm.caseHistoryListLength";
	private final static String PROP_CRM_DISABLE_TIME_WINDOW_CHECK = "fdstore.crm.debugIssueCreditsPage";
	private final static String PROP_FDSTORE_WEB_CAREERLINK = "fdstore.web.careerLink";
	private final static String PROP_FDFACTORY_HOME		= "fdstore.fdFactory.home";
	private final static String PROP_SAPGATEWAY_HOME	= "fdstore.sapGateway.home";
	private final static String PROP_KANAGATEWAY_HOME	= "fdstore.kanaGateway.home";
	private final static String PROP_COMPLAINTMGR_HOME	= "fdstore.complaintManager.home";
	private final static String PROP_CALLCENTERMGR_HOME	= "fdstore.callCenterManager.home";
	private final static String PROP_FDCUSTMGR_HOME		= "fdstore.fdCustomerManager.home";
	private final static String PROP_FDPROMOTIONMGR_HOME= "fdstore.fdPromotionManager.home";
	private final static String PROP_FDPROMOTIONMGR_NEW_HOME= "fdstore.fdPromotionManagerNew.home";
	private final static String PROP_DLVMANAGER_HOME	= "fdstore.deliveryManager.home";
	private final static String PROP_DLVRESTRICTION_MGR_HOME="freshdirect.delivery.DeliveryRestrictionManager";
	private final static String PROP_CONTENTMANAGER_HOME= "fdstore.ContentManager.home";
	private final static String PROP_FDCUSTOMER_HOME    = "fdstore.fdcustomer.home";
	private final static String PROP_ERPCUSTOMER_HOME   = "fdstore.erpcustomer.home";
	private final static String PROP_CONTFACTORY_HOME   = "fdstore.contentFactory.home";
	private final static String PROP_FDORDER_HOME		= "fdstore.fdorder.home";
	private final static String PROP_ROUTINGGATEWAY_HOME	= "fdstore.routingGateway.home";//freshdirect.routing.Gateway

	private final static String PROP_DLV_INSTRUCTION_SPECIAL_CHAR  = "fdstore.address.validation";

	private final static String PROP_PREVIEW_MODE       = "fdstore.preview";
    private final static String PROP_ANNOTATION_MODE	= "fdstore.annotation";
    private final static String PROP_ANNOTATION_ERPSY	= "fdstore.annotation.erpsy";
    private final static String PROP_MEDIA_PATH		    = "fdstore.media.path";
    private final static String PROP_CALLCENTER_PW		= "fdstore.callCenter.pw";
    private final static String PROP_CUSTOMER_SERVICE_EMAIL = "fdstore.customerService.email";
	private final static String PROP_EMAIL_PRODUCT = "fdstore.email.product";
	private final static String PROP_EMAIL_FEEDBACK = "fdstore.email.feedback";
	private final static String PROP_EMAIL_CHEFSTABLE = "fdstore.email.chefstable";
	private final static String PROP_EMAIL_VENDING = "fdstore.email.vending";
    private final static String PROP_HOLIDAY_LOOKAHEAD_DAYS = "fdstore.holidayLookaheadDays";
    private final static String PROP_DLV_PROMO_EXP_DATE = "fdstore.dlvPromo.expDate";
	private final static String PROP_EMAIL_PROMOTION = "fdstore.email.promotion";
	private final static String PROP_CUTOFF_WARN	    = "fdstore.cutoffWarning";
	private final static String PROP_CUTOFF_DEFAULT_ZONE_CODE	    = "fdstore.cutoffDefaultZoneCode";

	private final static String PROP_PRERESERVE_HOURS = "fdstore.preReserve.hours";

	private final static String PROP_AD_SERVER_URL	    = "fdstore.adServerURL";
	private final static String PROP_AD_SERVER_ENABLED  = "fdstore.adServerEnabled";
	private final static String PROP_AD_SERVER_UPDATES_URL = "fdstore.adServerUpdatesURL";

	private final static String PROP_AD_SERVER_PROFILE_ATTRIBS = "fdstore.adServerProfileAttribs";

	private final static String PROP_AD_SERVER_USES_DEFERRED_IMAGE_LOADING = "fdstore.adServerUsesDeferredImageLoading";

	private final static String PROP_IMPRESSION_LIMIT = "fdstore.impressionLimit";
	private final static String PROP_WINBACK_ROOT = "fdstore.winbackRoot";
	private final static String PROP_MARKETING_PROMO_ROOT = "fdstore.marketingPromoRoot";

	private final static String PROP_REFRESHSECS_PRODUCTINFO  = "fdstore.refreshSecs.productInfo";
	private final static String PROP_REFRESHSECS_PRODUCT  = "fdstore.refreshSecs.product";
	private final static String PROP_REFRESHSECS_ZONE  = "fdstore.refreshSecs.zone";
	private final static String PROP_PRODUCT_CACHE_SIZE	= "fdstore.product.cache.size";

	private final static String PROP_ZONE_CACHE_SIZE	= "fdstore.zone.cache.size";

	private final static String PROP_CMS_MOSTLY_READONLY = "fdstore.cms.readonly.optimization";

	private final static String PROP_PRELOAD_STORE = "fdstore.preLoad";
	private final static String PROP_WARMUP_CLASS = "fdstore.preLoad.class";
	private final static String PROP_PRELOAD_NEWNESS = "fdstore.preLoad.newness";
	private final static String PROP_PRELOAD_REINTRODUCED = "fdstore.preLoad.reintroduced";
	private final static String PROP_PRELOAD_SMARTSTORE = "fdstore.preLoad.smartStore";
	private final static String PROP_PRELOAD_AUTOCOMPLETIONS = "fdstore.preLoad.autocompletions";

	private final static String PROP_CMS_MEDIABASEURL = "cms.mediaBaseURL";

	private final static String PROP_PAYMENT_METHOD_MANAGER_HOME = "fdstore.PaymentMethodManager.home";

	private final static String PROP_RESTRICTED_PAYMENT_METHOD_HOME = "freshdirect.payment.RestrictedPaymentMethod.home";

    private final static String PROP_HAMPTONS = "fdstore.hamptons";

    private final static String PROP_EXTERNAL_FRAUD_CHECK_PM = "fraud.check.paymentMethod.external";

	private final static String PROP_MAX_REFERRALS = "referral.maxReferrals";
	private final static String PROP_NUM_DAYS_MAX_REFERRALS = "referral.numDaysMaxReferrals";
	private final static String PROP_FDREFERRALMGR_HOME		= "fdstore.fdReferralManager.home";

	private final static String PROP_USE_MULTIPLE_PROMOTIONS  = "fdstore.useMultiplePromotions";

	private final static String PROP_DATA_COLLECTION_ENABLED  = "fdstore.dataCollectionEnabled";
	private final static String PROP_PRODUCT_RECOMMEND_ENABLED  = "fdstore.productRecommendEnabled";

	//Delivery Pass Store properties.

	private final static String BSGS_SIGNUP_URL  = "fdstore.bsgsSignupUrl";
	private final static String UNLIMITED_SIGNUP_URL  = "fdstore.unlimitedSignupUrl";
	private final static String UNLIMITED_PROMOTIONAL_SIGNUP_URL = "fdstore.unlimitedPromotionalSignupUrl";
	private final static String UNLIMITED_AMAZON_PRIME_SIGNUP_URL = "fdstore.unlimitedAmazonPrimeSignupUrl";

	private final static String CRM_BSGS_SIGNUP_URL  = "fdstore.callCenter.bsgsSignupUrl";
	private final static String CRM_UNLIMITED_SIGNUP_URL  = "fdstore.callCenter.unlimitedSignupUrl";
	private final static String CRM_UNLIMITED_PROMOTIONAL_SIGNUP_URL = "fdstore.callCenter.unlimitedPromotionalSignupUrl";
	private final static String CRM_UNLIMITED_AMAZON_PRIME_SIGNUP_URL = "fdstore.callCenter.unlimitedAmazonPrimeSignupUrl";

	private final static String BSGS_PROFILE_POSFIX = "fdstore.bsgsProfilePosfix";
	private final static String UNLIMITED_PROFILE_POSFIX = "fdstore.unlimitedProfilePosfix";
	private final static String UNLIMITED_PROMOTIONAL_PROFILE = "fdstore.unlimitedPromotionalProfile";
	private final static String UNLIMITED_AMAZON_PRIME_PROFILE = "fdstore.unlimitedAmazonPrimeProfile";
	private static final String CRM_CREDIT_ISSUE_BCC = "fdstore.callCenter.creditIssue.bcc";

	private final static String DLV_PASS_PROMOTION_PREFIX = "fdstore.dlvPassPromotionPrefix";
	private final static String DLV_PASS_MAX_PURCHASE_LIMIT="fdstore.dlvPass.maxPurchaseLimit";
	private final static String DLV_PASS_AUTORENEWAL_DEFAULT="fdstore.dlvPass.defautRenewalSKU";

	// SmartStore
	private final static String DYF_STRATEGY_CACHE_ENTRIES = "fdstore.strategy.cache.entries";

	private final static String SS_GLOBAL_POPULARITY_SCORING = "fdstore.scoring.globalPopularity";
	private final static String SS_USER_POPULARITY_SCORING = "fdstore.scoring.userPopularity";
	private final static String SS_SHORT_TERM_POPULARITY_SCORING = "fdstore.scoring.shortTermPopularity";

	// DYF Site Feature
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
	private final static String RFL_PRG_PAGINATION_SIZE="fdstore.referral.paginationSize";

	// cut off time properties

	private static final String CUT_OFF_TIME_SUN="fdstore.cut_off_day_1";

	private static final String CUT_OFF_TIME_MON="fdstore.cut_off_day_2";

	private static final String CUT_OFF_TIME_TUES="fdstore.cut_off_day_3";

	private static final String CUT_OFF_TIME_WED="fdstore.cut_off_day_4";

	private static final String CUT_OFF_TIME_THUS="fdstore.cut_off_day_5";

	private static final String CUT_OFF_TIME_FRI="fdstore.cut_off_day_6";

	private static final String CUT_OFF_TIME_SAT="fdstore.cut_off_day_7";

	// customer service hours properties

	private static final String CLICK_TO_CALL="fdstore.click_to_call";

	private static final String CUST_SERV_HOURS_SUN="fdstore.cust_serv_day_1";

	private static final String CUST_SERV_HOURS_MON="fdstore.cust_serv_day_2";

	private static final String CUST_SERV_HOURS_TUES="fdstore.cust_serv_day_3";

	private static final String CUST_SERV_HOURS_WED="fdstore.cust_serv_day_4";

	private static final String CUST_SERV_HOURS_THUS="fdstore.cust_serv_day_5";

	private static final String CUST_SERV_HOURS_FRI="fdstore.cust_serv_day_6";

	private static final String CUST_SERV_HOURS_SAT="fdstore.cust_serv_day_7";



	private final static String SKU_AVAILABILITY_REFRESH_PERIOD = "fdstore.sku.availability.refresh";

	private static long lastRefresh = 0;
	private final static long REFRESH_PERIOD = 5 * 60 * 1000;
	//Added for controlling number of orders processed during Mass Cancellation and Mass Returns.
	private final static String PROP_CRM_ORDER_PRC_LIMIT = "fdstore.orderProcessingLimit";

	//Customer Created List settings
	private final static String CCL_ENABLED = "fdstore.ccl.enabled";

	private final static String CCL_AJAX_DEBUG_CLIENT = "fdstore.ccl.ajax.debug.client";

	private final static String CCL_AJAX_DEBUG_JSONRPC = "fdstore.ccl.ajax.debug.jsonrpc";

	private final static String CCL_AJAX_DEBUG_FACADE = "fdstore.ccl.ajax.debug.facade";

	private final static String CCL_AJAX_DEBUG_FACADE_EXCEPTION = "fdstore.ccl.ajax.debug.facade_exception";


//	Added for controlling case creation during the retention program survey processing.
	private final static String PROP_RETPRG_CREATECASE = "fdstore.retentionProgram.createCase";

	// Marketing Admin
	private final static String MKT_ADMIN_FILE_UPLOAD_SIZE="frshdirect.mktadmin.fileupload.size";

	private final static String DISTRIBUTION_SAMPLES_DIR = "fdstore.test.distributions.path";

	//refresh delay in minutes for FDInventoryCache
	private final static String PROP_INVENOTRY_REFRESH_PERIOD = "fdstore.refresh.inventory";

	//refresh delay in minutes for FDAttributesCache
	private final static String PROP_ATTRIBUTES_REFRESH_PERIOD = "fdstore.refresh.attributes";

	//refresh delay in minutes for FDNutritionCache
	private final static String PROP_NUTRITION_REFRESH_PERIOD = "fdstore.refresh.nutrition";

	//Refresh delay in seconds for Runtime Promotion cache.
	private final static String PROP_PROMOTION_RT_REFRESH_PERIOD = "promotion.rt.refresh.period";

	//Alternate zipcode to handle new zipcodes which are split from old zipcodes.
	private final static String PROP_GEOCODE_ALTZIPCODE = "fdstore.geocode.alternateZipcode";

//	Alternate zipcode to handle new zipcodes which are split from old zipcodes.
	private final static String PROP_GEOCODE_ISNEWFORMAT = "fdstore.geocode.isNewFormat";

	//	Handle Advanced Order date
	private final static String ADVANCE_ORDER_START = "fdstore.advance.order.start";
	private final static String ADVANCE_ORDER_END = "fdstore.advance.order.end";

	// Advance Order with days gap
	private final static String ADVANCE_ORDER_GAP = "fdstore.advance.order.isGap";
	private final static String ADVANCE_ORDER_NEW_START = "fdstore.advance.order.newstart";
	private final static String ADVANCE_ORDER_NEW_END = "fdstore.advance.order.newend";

	private static final String MRKTING_ADMIN_URL="fdstore.mktAdmin.URL";

	//Enable/Disable DCPD Alias Category Handling.
	private static final String DCPD_ALIAS_HANDLING_ENABLED="fdstore.dcpd.alias.handling.enabled";

	private static Properties config;

	private final static Properties defaults = new Properties();

	private static final String HP_LETTER_MEDIA_PATH1="fdstore.mediapath.newcustomer";
	
	private static final String HP_LETTER_MEDIA_PATH2="fdstore.mediapath.oldcustomer";

	// Produce Rating changes
	private static final String PRODUCE_RATING_ENABLED="fdstore.isProduceRatingEnabled";
	//additional ratings 2009.06
	private static final String PRODUCE_RATING_PREFIXES="fdstore.produceRatingPrefixes";
	
	// Freshness Guaranteed On/Off switch
	private static final String FRESHNESS_GUARANTEED_ENABLED="fdstore.isFreshnessGuaranteedEnabled";
	// freshness guaranteed on/off sku prefix based
	private static final String FRESHNESS_GUARANTEED_PREFIXES="fdstore.freshnessGuaranteedPrefixes";
	
	private static final String HPLETTER_MEDIA_ENABLED="fdstore.isHomePageMediaEnabled";

	//Deals changes.
	private static final String DEALS_SKU_PREFIX="fdstore.deals.skuPrefix";
	private static final String DEALS_LOWER_LIMIT="fdstore.deals.lowerLimit";;
	private static final String DEALS_UPPER_LIMIT="fdstore.deals.upperLimit";

	private static final String BURST_LOWER_LIMIT="fdstore.burst.lowerLimit";;
	private static final String BURST_UPPER_LIMIT="fdstore.burst.upperLimit";	
	
	private static final String MAX_FEATURED_DEALS_FOR_PAGE="fdstore.deals.maxFeaturedDeals";
	private static final String MAX_FEATURED_DEALS_PER_LINE="fdstore.deals.maxFeaturedDealsPerLine";
	private static final String MIN_FEATURED_DEALS_FOR_PAGE ="fdstore.deals.minFeaturedDeals";

	private static final String TEMP_DIR = "tmpdir";
	
	private static final String PROP_GEOCODE_USELOCATIONDB = "fdstore.geocode.useLocationDB";
	private static final String PROP_ROUTING_SENDADDRESS = "fdstore.routing.sendAddress";

	
	// Smart Search
	/**
	 * @deprecated
	 */
	private static final String SMART_SEARCH_ENABLED = "fdstore.newSearch.enabled";

//	COOL info
    private final static String PROP_COOLINFO_REFRESH_PERIOD = "fdstore.refresh.coolinfo";
    
    private static final String IMPRESSION_LOGGING = "fdstore.impression.logging";
    
//  Survey Def
	private final static String PROP_SURVEYDEF_CACHE_SIZE	= "fdstore.surveyDef.cache.size";
	private static final String PROP_REFRESHSECS_SURVEYDEF = "fdstore.refreshSecs.surveyDef";
	private final static String PROP_FDSURVEY_HOME		= "fdstore.fdSurvey.home";
	
//What's Good Department
	private final static String PROP_FDWHATSGOOD_ENABLED				= "fdstore.fdwhatsgood.enabled";
	private final static String PROP_FDWHATSGOOD_PEAKPRODUCE_ENABLED	= "fdstore.fdwhatsgood_peakproduce.enabled";
	private final static String PROP_FDWHATSGOOD_BBLOCK_ENABLED			= "fdstore.fdwhatsgood_bblock.enabled";
	//new prop to set dynamic rows
	private final static String PROP_FDWHATSGOOD_ROWS					= "fdstore.fdwhatsgood.rows";
	//allow debug messages via property (default to false : off)
	private final static String PROP_FDWHATSGOOD_DEBUG_ENABLED			= "fdstore.fdwhatsgood.debugEnabled";
	
	
	
	//Smart Savings
	
	private static final String SMART_SAVINGS_FEATURE_ENABLED  = "fdstore.smartsavings.enabled";
	
	private static final String DYNAMIC_ROUTING_ENABLED = "fdstore.dynamicrouting.enabled";
	
	// iPhone non-customer email media path
	private final static String PROP_MEDIA_IPHONE_TEMPLATE_PATH	= "fdstore.media.iphone.template.path"; //Location of different ftl template of iphone.
	private final static String IPHONE_EMAIL_SUBJECT = "fdstore.media.iphone.email.subject";
	
	//Gift Cards
	private static final String PROP_GIFT_CARD_SKU_CODE = "fdstore.giftcard.skucode";
	private final static String PROP_MEDIA_GIFT_CARD_TEMPLATE_PATH	= "fdstore.media.giftcard.template.path"; //Location of different ftl templates of giftcards.
	private static final String PROP_GC_TEMPLATE_BASE_URL = "fdstore.giftcard.template.baseurl";
	private static final String PROP_GC_MIN_AMOUNT = "fdstore.giftcard.minimum.amount";
	private static final String PROP_GC_MAX_AMOUNT = "fdstore.giftcard.maximum.amount";
	private final static String PROP_GIFT_CARD_RECIPIENT_MAX = "giftcard.recipient.max";
	private static final String PROP_GC_ENABLED="fdstore.isGiftCardEnabled";
	private static final String PROP_GC_LANDING_URL="fdstore.giftCardLandingUrl";
	private static final String PROP_GC_DEPTID="fdstore.giftCardDeptId";
	private static final String PROP_GC_CATID="fdstore.giftCardCatId";
	private static final String PROP_GC_PRODNAME="fdstore.giftCardProdName";
	private static final String GIVEX_BLACK_HOLE_ENABLED="givex.black.hole.enabled";
	// Robin Hood
	private static final String ROBIN_HOOD_ENABLED="fdstore.isRobinHoodEnabled";
	private static final String ROBIN_HOOD_LANDING_URL="fdstore.robinHoodLandingUrl";
	private static final String PROP_ROBIN_HOOD_SKU_CODE = "fdstore.robinhood.skucode";
	private static final String ROBIN_HOOD_STATUS = "fdstore.robinhood.status";
	
	private static final String PROP_ROUTING_PROVIDER_URL="fdstore.routing.providerURL";
	
	//CT & PR1
	private static final String CT_DELIVERY_CAPACITY_FILE_NAME = "fdstore.deliverycapacity.filename";	
	
	private static final String PR1_DELIVERY_CAPACITY_FILE_NAME = "fdstore.pr1.filename";	
	
	//MEAT DEALS and EDLP
	private final static String DEPT_MEAT_DEALS = "fdstore.department.meatdeals.isEnabled";
	private final static String DEPT_EDLP = "fdstore.department.edlp.isEnabled";
	private final static String DEPT_MEAT_DEALS_CATID = "fdstore.department.meatdeals.catId";
	private final static String DEPT_EDLP_CATID = "fdstore.department.edlp.catId";
	
	//Mobile
	private final static String MOBILE_IPHONE_LANDING_ENABLED = "fdstore.mobile.iPhone.landingEnabled";
	private final static String MOBILE_IPHONE_LANDING_PAGE = "fdstore.mobile.iPhone.landingPage";
	
	private final static String PROP_GC_NSM_AUTHSKIP_SECS  = "fdstore.gcnsm.authskip.secs";
	private final static String PROP_GC_NSM_FREQ_SECS  = "fdstore.gcnsm.frequency.secs";
	
	private final static String PROP_ZONE_PICKUP_ZIPCODE = "fdstore.zone.pricing.pickup.zipcode";
	
	//Windows steering
	private final static String WINDOW_STEERING_PROMOTION_PREFIX="fdstore.windowsteering.promo.prefix";
	
	//Zone Pricing
	private final static String PROP_ZONE_PRICING_AD_ENABLED = "fdstore.zone.pricing.ad.enabled";
	private final static String PROP_ZONE_PRICING_ENABLED = "fdstore.zone.pricing.enabled";
	
	//Standing Orders
	private final static String SO_GLOBAL_ENABLER = "fdstore.standingorders.enabled";
	
	//Standing Orders
	private final static String CLIENT_CODES_GLOBAL_ENABLER = "fdstore.clientcodes.enabled";

	//new products revamp
	private static final String PROP_NEWPRODUCTS_DEPTID="fdstore.newProducts.DeptId";
	private static final String PROP_NEWPRODUCTS_CATID="fdstore.newProducts.CatId";
	private static final String PROP_NEWPRODUCTS_GROUPS="fdstore.newproducts.groups";
	
	
	//Contact us FAQs
	private static final String PROP_FAQ_SECTIONS="fdstore.faq.sections";
	
	private static final String PROP_CRM_HELP_LINK_ADDR_VALIDATION ="crm.help.link.addr.validation";
	private static final String PROP_CRM_HELP_LINK_GIFT_CARD ="crm.help.link.giftcard";
	private static final String PROP_CRM_HELP_LINK_MAIN_HELP ="crm.help.link.main.help";
	private static final String PROP_CRM_HELP_LINK_CUST_PROFILE ="crm.help.link.cust.profile";
	private static final String PROP_CRM_HELP_LINK_TIMESLOT ="crm.help.link.request.timeslot";
	private static final String PROP_CRM_HELP_LINK_FD_UPDATES ="crm.help.link.fdUpdates";
	private static final String PROP_CRM_HELP_LINK_PROMOTIONS ="crm.help.link.promotions";
	private static final String PROP_CRM_HELP_LINK_CASE_MEDIA ="crm.help.link.case.media";
	private static final String PROP_CRM_HELP_LINK_CASE_MORE_ISSUES ="crm.help.link.case.moreIssues";
	private static final String PROP_CRM_HELP_LINK_CASE_CUST_TONE ="crm.help.link.case.cust.tone";
	private static final String PROP_CRM_HELP_LINK_CASE_RESOL_SATISFY ="crm.help.link.resol.satisfactory";
	private static final String PROP_CRM_HELP_LINK_CASE_RESOLV_FIRST ="crm.help.link.resolv.first";
	private static final String PROP_CRM_HELP_LINK_CASE_FIRST_CONTACT ="crm.help.link.first.contact";
	
	//Email Opt-Down (APPDEV-662)
	private static final String PROP_EMAIL_OPTDOWN_ENABLED ="fdstore.email.optdown.enabled";
	
	//Google Maps API key
	// It can be obtained from http://code.google.com/apis/maps/signup.html
	private static final String GMAPS_API_KEY = "gmaps.api.key";

	// APPDEV-1091
	private static final String PROMO_PUBLISH_URL_KEY = "promo.publish.url";
	//   valid values can be 'master' or 'replica'
	private static final String PROMO_PUBLISH_NODE_TYPE = "promo.publish.node";
	
	private static final String PROMO_VALID_RT_STATUSES = "promo.valid.rt.statuses";
	
	private final static String PROP_REDEMPTION_CNT_REFRESH_PERIOD = "promotion.redeem.cnt.refresh.period";
	
	private final static String PROP_REDEMPTION_SERVER_COUNT = "promotion.redemption.server.count";
	
	//Delivery Pass at Checkout (APPDEV-664)
	private final static String PROP_DP_CART_ENABLED = "fdstore.dpcart.enabled";
	
	private final static String PROP_PROMO_LINE_ITEM_EMAIL = "promotion.lineitem.email.display";
	

	// 4mm department internal cache refresh interval - in minutes
	private final static String PROP_4MM_REFRESH_INTERVAL = "fdstore.4mm.cache.refresh.interval";
	
	private static final String PROP_ROUTING_UNASSIGNEDPROCESSINGLIMIT = "fdstore.routing.unassignedprocesinglimit";
	
	//Brand media replacement (APPDEV-1308)
	private final static String PROP_BRAND_MEDIA_IDS = "fdstore.brand.media.ids";
	private final static String PROP_WS_DISCOUNT_AMOUNT_LIST = "fdstore.ws.discount.list";
	// [APPDEV-1283] Wine Revamp
	private final static String WINE_SHOW_RATINGS_KEY = "fdstore.wine.newPricingRatingEnabled";
	
	private final static String WINE_PRICE_BUCKET_BOUND_PREFIX = "fdstore.wine.priceBucketBound";
	
	private final static String ADVERTISING_TILE_ENABLED = "fdstore.advertisingTile.enabled";
	
	private final static String PROP_CRM_MENU_ROLES_REFRESH_PERIOD = "crm.roles.menu.refresh.period";
	private final static String PROP_CRM_LDAP_USERS_REFRESH_PERIOD = "crm.ldap.users.refresh.period";
	private final static String PROP_CRM_LDAP_ACCESS_HOST_NAME_PRIMARY = "crm.ldap.access.primary.host.name";
	private final static String PROP_CRM_CC_DETAILS_LOOKUP_LIMIT="crm.cc.details.limit";
	private final static String PROP_CRM_CC_SECURITY_EMAIL ="crm.cc.security.email.addr";
	private final static String PROP_CRM_CC_SECURITY_EMAIL_ENABLED="crm.cc.security.email.enabled";
	private final static String PROP_CRM_CC_SECURITY_EMAIL_SUBJECT="crm.cc.security.email.subject";
	private final static String PROP_CRM_CC_DETAILS_ACCESS_KEY="crm.cc.details.access.key";
	private final static String PROP_CRM_FORGOT_LDAP_PASSWORD_URL="crm.ldap.password.reset.url";
	private final static String PROP_CRM_SECURITY_SKIP_FILE_TYPES="crm.security.skip.filetypes";
	private final static String PROP_CRM_SECURITY_SKIP_FOLDERS="crm.security.skip.folders";
	private final static String PROP_CRM_AGENTS_CACHE_REFRESH_PERIOD="crm.agents.cache.refresh.period";
	
	private final static String MYFD_ENABLED = "myfd.enabled";
	private static final String MYFD_POLLDADDY_API_KEY = "myfd.pollDaddy.apiKey";

	static {
		defaults.put(PROP_ROUTING_PROVIDER_URL,"t3://localhost:7001");
		defaults.put(PROP_PROVIDER_URL, 	"t3://localhost:7001");
		defaults.put(PROP_INIT_CTX_FACTORY,	"weblogic.jndi.WLInitialContextFactory");
		defaults.put(PROP_CRM_GEOCODELINK, "http://www.geocode.com/EZLI/LoginServlet?uname=ECM0001468&pword=Lzxjb&servID=USA_Geo_002&formAction=GetInputFormServlet&submit=Login&cmd=li");
		defaults.put(PROP_CRM_CASE_LIST_LENGTH, "100");
		defaults.put(PROP_CRM_CASE_HISTORY_LIST_LENGTH, "25");
		defaults.put(PROP_CRM_DISABLE_TIME_WINDOW_CHECK, "false");
		defaults.put(PROP_FDSTORE_WEB_CAREERLINK, "http://jobs-freshdirect.icims.com");
		defaults.put(PROP_FDFACTORY_HOME,	"freshdirect.fdstore.Factory");
		defaults.put(PROP_SAPGATEWAY_HOME,	"freshdirect.sap.Gateway");
		defaults.put(PROP_KANAGATEWAY_HOME,	"freshdirect.kana.Gateway");
		defaults.put(PROP_COMPLAINTMGR_HOME,"freshdirect.erp.ComplaintManager");
		defaults.put(PROP_CALLCENTERMGR_HOME,"freshdirect.fdstore.CallCenterManager");
		defaults.put(PROP_FDCUSTMGR_HOME,	"freshdirect.fdstore.CustomerManager");
		defaults.put(PROP_FDPROMOTIONMGR_HOME,	"freshdirect.fdstore.PromotionManager");
		defaults.put(PROP_FDPROMOTIONMGR_NEW_HOME,	"freshdirect.fdstore.PromotionManagerNew");
		
		defaults.put(PROP_DLVMANAGER_HOME,	"freshdirect.delivery.DeliveryManager");
		defaults.put(PROP_DLVRESTRICTION_MGR_HOME,	"freshdirect.delivery.DeliveryRestrictionManager");
		defaults.put(PROP_FDCUSTOMER_HOME,  "freshdirect.fdstore.Customer");
		defaults.put(PROP_EMAIL_PROMOTION, "PromotionNotification@freshdirect.com");
		defaults.put(PROP_ERPCUSTOMER_HOME, "freshdirect.erp.Customer");
		defaults.put(PROP_CONTFACTORY_HOME, "freshdirect.content.ContentFactory");
		defaults.put(PROP_FDORDER_HOME,     "freshdirect.fdstore.Order");
		defaults.put(PROP_ROUTINGGATEWAY_HOME,	"freshdirect.routing.Gateway");
		//checks for all special characters
		defaults.put(PROP_DLV_INSTRUCTION_SPECIAL_CHAR, "[~ | \\` | \" | \\! | \\@ | \\# | \\ $ | \\% | \\^ | \\& | \\* | \\( | \\) | \\- | _ | + | \\= | \\n | \\r]");

        defaults.put(PROP_PREVIEW_MODE,     "false");
        defaults.put(PROP_ANNOTATION_MODE,	"false");
        defaults.put(PROP_ANNOTATION_ERPSY,	"http://ems1.nyc1.freshdirect.com:8000/ERPSAdmin");
        defaults.put(PROP_CALLCENTER_PW,    "");
        defaults.put(PROP_CUSTOMER_SERVICE_EMAIL, "service@freshdirect.com");
		defaults.put(PROP_EMAIL_PRODUCT, "products@freshdirect.com");
		defaults.put(PROP_EMAIL_FEEDBACK, "feedback@freshdirect.com");
		defaults.put(PROP_EMAIL_CHEFSTABLE, "chefstable@freshdirect.com");
		defaults.put(PROP_EMAIL_VENDING, "vendinginfo@freshdirect.com");		
		defaults.put(PROP_CONTENTMANAGER_HOME,"freshdirect.content.ContentManager");
		defaults.put(PROP_HOLIDAY_LOOKAHEAD_DAYS, "21");
		defaults.put(PROP_AD_SERVER_ENABLED, "false");

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

		defaults.put(PROP_AD_SERVER_USES_DEFERRED_IMAGE_LOADING, "true");

		defaults.put(PROP_PAYMENT_METHOD_MANAGER_HOME,	"freshdirect.paymentmethod.PaymentMethodManager");

		defaults.put(PROP_RESTRICTED_PAYMENT_METHOD_HOME,	"freshdirect.payment.RestrictedPaymentMethod");

		defaults.put(PROP_REFRESHSECS_PRODUCTINFO, "600");
		defaults.put(PROP_REFRESHSECS_ZONE, "600");
		defaults.put(PROP_REFRESHSECS_PRODUCT, "7200");
		defaults.put(PROP_PRODUCT_CACHE_SIZE, "30000");
		defaults.put(PROP_ZONE_CACHE_SIZE, "10000");

		// mktadmin
		defaults.put(MKT_ADMIN_FILE_UPLOAD_SIZE, "2000");


		/*
		defaults.put(PROP_CUTOFF_WARN + Calendar.MONDAY, "23");
		defaults.put(PROP_CUTOFF_WARN + Calendar.TUESDAY, "23");
		defaults.put(PROP_CUTOFF_WARN + Calendar.WEDNESDAY, "23");
		defaults.put(PROP_CUTOFF_WARN + Calendar.THURSDAY, "23");
		defaults.put(PROP_CUTOFF_WARN + Calendar.FRIDAY, "19");
		defaults.put(PROP_CUTOFF_WARN + Calendar.SATURDAY, "20");
		defaults.put(PROP_CUTOFF_WARN + Calendar.SUNDAY, "23");
		*/

		defaults.put(PROP_CUTOFF_WARN, "1");
		defaults.put(PROP_CUTOFF_DEFAULT_ZONE_CODE, "922");

		defaults.put(PROP_PRERESERVE_HOURS, "1");

		defaults.put(PROP_DLV_PROMO_EXP_DATE, "2004-01-01");

		defaults.put(PROP_PRELOAD_STORE, "true");
		// No default for PROP_WARMUP_CLASS
		defaults.put(PROP_PRELOAD_NEWNESS, "true");
		defaults.put(PROP_PRELOAD_REINTRODUCED, "true");
		defaults.put(PROP_PRELOAD_SMARTSTORE, "true");
		defaults.put(PROP_PRELOAD_AUTOCOMPLETIONS, "true");

		defaults.put(PROP_CMS_MEDIABASEURL, "http://www.freshdirect.com");

		defaults.put(PROP_HAMPTONS, "false");

		defaults.put(PROP_EXTERNAL_FRAUD_CHECK_PM, "true");

		defaults.put(PROP_MAX_REFERRALS, "500");
		defaults.put(PROP_NUM_DAYS_MAX_REFERRALS, "1"); // max 500 referrals for 1 day
		defaults.put(PROP_FDREFERRALMGR_HOME,	"freshdirect.fdstore.ReferralManager");


		defaults.put(PROP_USE_MULTIPLE_PROMOTIONS, "false");

		defaults.put(PROP_DATA_COLLECTION_ENABLED, "false");

		defaults.put(PROP_PRODUCT_RECOMMEND_ENABLED, "false");

		defaults.put(BSGS_SIGNUP_URL, "/product.jsp?productId=mkt_fd_dlvpss_b10g5f&catId=gro_gear_dlvpass");
		defaults.put(UNLIMITED_SIGNUP_URL, "/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
		defaults.put(UNLIMITED_PROMOTIONAL_SIGNUP_URL, "/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
		defaults.put(UNLIMITED_AMAZON_PRIME_SIGNUP_URL , "/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
		defaults.put(CRM_BSGS_SIGNUP_URL, "/order/product.jsp?productId=mkt_fd_dlvpss_b10g5f&catId=gro_gear_dlvpass");
		defaults.put(CRM_UNLIMITED_SIGNUP_URL, "/order/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
		defaults.put(CRM_UNLIMITED_PROMOTIONAL_SIGNUP_URL, "/order/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
		defaults.put(CRM_UNLIMITED_AMAZON_PRIME_SIGNUP_URL, "/order/product.jsp?productId=mkt_fd_dlvpss_unl6m&catId=gro_gear_dlvpass");
		defaults.put(CRM_CREDIT_ISSUE_BCC, "");

		defaults.put(BSGS_PROFILE_POSFIX, "BuySomeGetSome");
		defaults.put(UNLIMITED_PROFILE_POSFIX, "Unlimited");
		defaults.put(UNLIMITED_PROMOTIONAL_PROFILE , "Unlimited");
		defaults.put(UNLIMITED_AMAZON_PRIME_PROFILE , "Unlimited");
		defaults.put(DLV_PASS_AUTORENEWAL_DEFAULT,"MKT0072630");

		defaults.put(DLV_PASS_PROMOTION_PREFIX, "FDDELIVERS");
		defaults.put(DLV_PASS_MAX_PURCHASE_LIMIT,"1");


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
		defaults.put(SMARTSTORE_PERSONAL_SCORES_CAHCE_TIMEOUT, "" + (30*60));
		
		defaults.put(DISTRIBUTION_SAMPLES_DIR,"");

		defaults.put(PROP_INVENOTRY_REFRESH_PERIOD, "10");
		defaults.put(PROP_ATTRIBUTES_REFRESH_PERIOD, "10");
		defaults.put(PROP_NUTRITION_REFRESH_PERIOD, "10");
		defaults.put(PROP_PROMOTION_RT_REFRESH_PERIOD, "1200");

		defaults.put(PROP_GEOCODE_ISNEWFORMAT, "true");

		defaults.put(ADVANCE_ORDER_START, "2004-01-01");
		defaults.put(ADVANCE_ORDER_END, "2004-01-02");
		// Advance Order Gap
		defaults.put(ADVANCE_ORDER_GAP, "false");
		defaults.put(ADVANCE_ORDER_NEW_START, "2004-01-01");
		defaults.put(ADVANCE_ORDER_NEW_END, "2004-01-02");
		
		// mrkting admin
		defaults.put(MRKTING_ADMIN_URL, "http://adm.freshdirect.com/MrktAdmin");
		//DCPD ALIAS Handling.
		defaults.put(DCPD_ALIAS_HANDLING_ENABLED, "true");

		// produce rating enabled
		defaults.put(PRODUCE_RATING_ENABLED, "true");
		//produce rating sku prefixes
		defaults.put(PRODUCE_RATING_PREFIXES, "FRU,VEG,YEL,WIN");

		// freshness guaranteed on/off switch
		defaults.put(FRESHNESS_GUARANTEED_ENABLED, "true");
		// freshness guaranteed on/off sku prefix specific
		defaults.put(FRESHNESS_GUARANTEED_PREFIXES, "FRU,VEG,MEA,SEA,DEL,CHE,DAI,HMR,VAR,CAT,BAK,PAS,YEL");
		
		defaults.put(HP_LETTER_MEDIA_PATH1, "/media/editorial/home/letter/hp_letter_new.html");
		defaults.put(HP_LETTER_MEDIA_PATH2, "/media/editorial/home/letter/hp_letter_customer.html");
		defaults.put(HPLETTER_MEDIA_ENABLED, "true");

		//deals
		defaults.put(DEALS_SKU_PREFIX,"GRO,FRO,SPE,DAI,HBA");
		defaults.put(DEALS_LOWER_LIMIT,"1");
		defaults.put(DEALS_UPPER_LIMIT,"95");
		defaults.put(BURST_LOWER_LIMIT,"10");
		defaults.put(BURST_UPPER_LIMIT,"75");
		defaults.put(MAX_FEATURED_DEALS_FOR_PAGE,"5");
		defaults.put(MAX_FEATURED_DEALS_PER_LINE,"5");
		defaults.put(MIN_FEATURED_DEALS_FOR_PAGE,"3");
		
		defaults.put(TEMP_DIR, "/tmp");
		
		defaults.put(PROP_GEOCODE_USELOCATIONDB, "false");
		defaults.put(PROP_ROUTING_SENDADDRESS, "false");

		defaults.put(SMART_SEARCH_ENABLED, "false");
		
		defaults.put(PROP_COOLINFO_REFRESH_PERIOD, "10");
		
		defaults.put(IMPRESSION_LOGGING, "false");
		
		defaults.put(PROP_SURVEYDEF_CACHE_SIZE, "25");
		defaults.put(PROP_REFRESHSECS_SURVEYDEF, "600");
		defaults.put(PROP_FDSURVEY_HOME,	"freshdirect.fdstore.FDSurvey");

		defaults.put(SMART_SAVINGS_FEATURE_ENABLED, "true");
		
//		What's Good Department
		defaults.put(PROP_FDWHATSGOOD_ENABLED, "false");
		defaults.put(PROP_FDWHATSGOOD_PEAKPRODUCE_ENABLED, "true");
		defaults.put(PROP_FDWHATSGOOD_BBLOCK_ENABLED, "false");
		defaults.put(PROP_FDWHATSGOOD_ROWS, "");
		defaults.put(PROP_FDWHATSGOOD_DEBUG_ENABLED, "false");
		
		defaults.put(DYNAMIC_ROUTING_ENABLED, "true");

		// iphone 
		defaults.put(PROP_MEDIA_IPHONE_TEMPLATE_PATH,"media/mobile/iphone/");
		defaults.put(IPHONE_EMAIL_SUBJECT, "FreshDirect SmartPhone Shopping");
		
		// Gift Card
		defaults.put(PROP_GC_ENABLED, "false");
		defaults.put(PROP_GC_LANDING_URL, "/gift_card/purchase/landing.jsp");
		defaults.put(PROP_GIFT_CARD_SKU_CODE, "MKT0074896");
		defaults.put(PROP_GC_TEMPLATE_BASE_URL,"http://www.freshdirect.com");		
		defaults.put(PROP_MEDIA_GIFT_CARD_TEMPLATE_PATH,"/media/editorial/giftcards/");
		defaults.put(PROP_GIFT_CARD_RECIPIENT_MAX, "10");	
		defaults.put(PROP_GC_MIN_AMOUNT, "20");
		defaults.put(PROP_GC_MAX_AMOUNT, "5000");
		defaults.put(PROP_GC_DEPTID, "GC_testDept");
		defaults.put(PROP_GC_CATID, "GC_testCat");
		defaults.put(PROP_GC_PRODNAME, "GC_testProd");
		defaults.put(GIVEX_BLACK_HOLE_ENABLED, "false");
		
		// Robin Hood
		defaults.put(ROBIN_HOOD_ENABLED, "false");
		defaults.put(ROBIN_HOOD_LANDING_URL, "/robin_hood/landing.jsp");
		defaults.put(PROP_ROBIN_HOOD_SKU_CODE, "MKT0075239");
		defaults.put(ROBIN_HOOD_STATUS, "BUY");
		
		
		defaults.put(CT_DELIVERY_CAPACITY_FILE_NAME, "ctprofile.xml");		
		defaults.put(PR1_DELIVERY_CAPACITY_FILE_NAME, "pr1profile.xml");		
		
		//Meat Deals and EDLP
		defaults.put(DEPT_MEAT_DEALS, "true");
		defaults.put(DEPT_EDLP, "true");
		defaults.put(DEPT_MEAT_DEALS_CATID, "");
		defaults.put(DEPT_EDLP_CATID, "");
		
		//Mobile
		defaults.put(MOBILE_IPHONE_LANDING_ENABLED, "false");
		defaults.put(MOBILE_IPHONE_LANDING_PAGE, "/media/mobile/supported.ftl");
		
		defaults.put(PROP_GC_NSM_AUTHSKIP_SECS, "600");
		defaults.put(PROP_GC_NSM_FREQ_SECS, "600");
		defaults.put(PROP_ZONE_PRICING_ENABLED, "true");		
		defaults.put(PROP_ZONE_PRICING_AD_ENABLED, "true");
		defaults.put(PROP_ZONE_PICKUP_ZIPCODE, "11101");
		
		//Window Steering
		defaults.put(WINDOW_STEERING_PROMOTION_PREFIX,"WS_");
		
		//Standing Orders
		defaults.put(SO_GLOBAL_ENABLER, "false");
		
		//Client Codes
		defaults.put(CLIENT_CODES_GLOBAL_ENABLER, "false");

		//new products revamp
		defaults.put(PROP_NEWPRODUCTS_DEPTID, "newproduct");
		defaults.put(PROP_NEWPRODUCTS_CATID, "newproduct_cat");
		defaults.put(PROP_NEWPRODUCTS_CATID, "newproduct_cat");
		defaults.put(PROP_NEWPRODUCTS_GROUPS, "<W2,W2-W4,M1-M2,M2-M3,>M3");
		
		//comma separated list of faq section ids from CMS.
		defaults.put(PROP_FAQ_SECTIONS, "payment,order_today,prblem_my_order,order_change,delivery_feedback,promotion,gen_feedback,req_feedback,website_technical,acct_info,what_we_do,signing_up,security,shopping,home_delivery,cos,chef_table,vending,inside");
		
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

		//Email Opt-Down (APPDEV-662)
		defaults.put(PROP_EMAIL_OPTDOWN_ENABLED, "false");
		
		// APPDEV-1091 Promo Publish URL
		defaults.put(PROMO_PUBLISH_URL_KEY, "/promo_publish");
		defaults.put(PROMO_PUBLISH_NODE_TYPE, "master");
		//APPDEV-659
		defaults.put(PROMO_VALID_RT_STATUSES, "LIVE");
		
		defaults.put(PROP_REDEMPTION_CNT_REFRESH_PERIOD, "300"); //every 5 mins
		defaults.put(PROP_REDEMPTION_SERVER_COUNT, "5");
		defaults.put(PROP_PROMO_LINE_ITEM_EMAIL, "true");
		
		//Delivery Pass at Checkout (APPDEV-664)
		defaults.put(PROP_DP_CART_ENABLED, "false");
		
		defaults.put(PROP_4MM_REFRESH_INTERVAL, "5");		
		defaults.put(PROP_ROUTING_UNASSIGNEDPROCESSINGLIMIT, "8");

		//Brand media replacement (APPDEV-1308)
		defaults.put(PROP_BRAND_MEDIA_IDS, "none");
		defaults.put(PROP_WS_DISCOUNT_AMOUNT_LIST, "2");
		
		// [APPDEV-1283] Wine Revamp
		defaults.put(WINE_SHOW_RATINGS_KEY, Boolean.toString(true));
		
		defaults.put(ADVERTISING_TILE_ENABLED, Boolean.toString(false));
		
		defaults.put(PROP_CRM_MENU_ROLES_REFRESH_PERIOD, "3600"); //every 60 mins
		defaults.put(PROP_CRM_LDAP_USERS_REFRESH_PERIOD, "3600"); //every 60 mins
		defaults.put(PROP_CRM_AGENTS_CACHE_REFRESH_PERIOD, "1800");//every 30 mins
		
		defaults.put(PROP_CRM_LDAP_ACCESS_HOST_NAME_PRIMARY, "t3://127.0.0.1:7001");
		defaults.put(PROP_CRM_CC_DETAILS_LOOKUP_LIMIT, "10");//10
		defaults.put(PROP_CRM_CC_SECURITY_EMAIL_ENABLED, Boolean.toString(false));
		defaults.put(PROP_CRM_CC_SECURITY_EMAIL, "infosec@freshdirect.com");//infosec@freshdirect.com
		defaults.put(PROP_CRM_CC_SECURITY_EMAIL_SUBJECT, "ALERT: CC/EC decryptions above threshold");
		defaults.put(PROP_CRM_CC_DETAILS_ACCESS_KEY, "9ac7ec230e0e4513578f309d6d3579ad");
		defaults.put(PROP_CRM_FORGOT_LDAP_PASSWORD_URL, "http://myaccount.freshdirect.com/cp/login/Login.aspx");
		defaults.put(PROP_CRM_SECURITY_SKIP_FILE_TYPES, "css,js,jspf,gif,jpg,xls,ico,txt");
		defaults.put(PROP_CRM_SECURITY_SKIP_FOLDERS, "");
		
		defaults.put(MYFD_ENABLED, "false");

		refresh();
	}

	private FDStoreProperties() {
	}

	private static void refresh() {
		refresh(false);
	}

	private synchronized static void refresh(boolean force) {
		long t = System.currentTimeMillis();
		if (force || (t - lastRefresh > REFRESH_PERIOD)) {
			config = ConfigHelper.getPropertiesFromClassLoader("fdstore.properties", defaults);
			lastRefresh = t;
			LOGGER.info("Loaded configuration from fdstore.properties: " + config);
			fireEvent();
		}
	}


	private static String get(String key) {
		refresh();
		return config.getProperty(key);
	}

	/**
	 *  A method to set a specific property.
	 *  Use with care - it's here for testing purposes only.
	 *
	 *  @param key the name of the property to set.
	 *  @param value the new value of the property.
	 */
	public static void set(String key, String value) {
		refresh();
		config.setProperty(key, value);
	}
	public static String getDlvInstructionsSpecialChar() {
		return get(PROP_DLV_INSTRUCTION_SPECIAL_CHAR);
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
		return (isCaseHistory)?get(PROP_CRM_CASE_HISTORY_LIST_LENGTH):get(PROP_CRM_CASE_LIST_LENGTH);
	}

	/**
	 * This property disables time window check on CRM issue credits page.
	 * 
	 * For debug purposes.
	 * 
	 * @return
	 */
	public static boolean getDisableTimeWindowCheck() {
		return (new Boolean(get(PROP_CRM_DISABLE_TIME_WINDOW_CHECK)).booleanValue());
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

	public static String getFDCustomerHome() {
		return get(PROP_FDCUSTOMER_HOME);
	}

	public static String getErpCustomerHome() {
		return get(PROP_ERPCUSTOMER_HOME);
	}

	public static String getContentFactoryHome() {
		return get(PROP_CONTFACTORY_HOME);
	}

	public static String getFDOrderHome(){
		return get(PROP_FDORDER_HOME);
	}

	public static String getPaymentMethodManagerHome() {
		return get(PROP_PAYMENT_METHOD_MANAGER_HOME);
	}

	public static String getRestrictedPaymentMethodHome() {
		return get(PROP_RESTRICTED_PAYMENT_METHOD_HOME);
	}

	public static boolean getPreviewMode() {
        return (new Boolean(get(PROP_PREVIEW_MODE))).booleanValue();
    }

    public static boolean isAnnotationMode() {
        return (new Boolean(get(PROP_ANNOTATION_MODE))).booleanValue();
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

	public static String getCustomerServiceEmail(){
		return get(PROP_CUSTOMER_SERVICE_EMAIL);
	}

	public static int getHolidayLookaheadDays(){
		return Integer.parseInt(get(PROP_HOLIDAY_LOOKAHEAD_DAYS));
	}

	public static double getCutoffWarnStart() {
		String prop = get(PROP_CUTOFF_WARN);
		return prop == null ? -1 : Double.parseDouble(prop);
	}

	public static String getCutoffDefaultZoneCode() {
		return get(PROP_CUTOFF_DEFAULT_ZONE_CODE);
	}

	public static int getPreReserveHours() {
		return Integer.parseInt(get(PROP_PRERESERVE_HOURS));
	}

	public static String getAdServerUrl(){
		return get(PROP_AD_SERVER_URL);
	}

	public static boolean isAdServerEnabled(){
		return Boolean.valueOf(get(PROP_AD_SERVER_ENABLED)).booleanValue();
	}

	public static String getAdServerUpdatesURL(){
		return get(PROP_AD_SERVER_UPDATES_URL);
	}

	public static int getImpressionLimit(){
		String s = get(PROP_IMPRESSION_LIMIT);
		return s == null ? 0 : Integer.parseInt(s);
	}

	public static String getWinbackRoot(){
		return get(PROP_WINBACK_ROOT);
	}

	public static String getMarketingPromoRoot(){
		return get(PROP_MARKETING_PROMO_ROOT);
	}

	public static int getRefreshSecsProductInfo(){
		return Integer.parseInt(get(PROP_REFRESHSECS_PRODUCTINFO));
	}

	public static int getRefreshSecsProduct(){
		return Integer.parseInt(get(PROP_REFRESHSECS_PRODUCT));
	}


	public static int getRefreshSecsZone(){
		return Integer.parseInt(get(PROP_REFRESHSECS_ZONE));
	}

	public static int getProductCacheSize() {
		return Integer.parseInt(get(PROP_PRODUCT_CACHE_SIZE));
	}


	public static int getZoneCacheSize() {
		return Integer.parseInt(get(PROP_ZONE_CACHE_SIZE));
	}


	public static String getProductEmail() {
		return get(PROP_EMAIL_PRODUCT);
	}

	public static String getFeedbackEmail(){
		return get(PROP_EMAIL_FEEDBACK);
	}

	public static String getVendingEmail(){
		return get(PROP_EMAIL_VENDING);
	}

	public static String getChefsTableEmail(){
		return get(PROP_EMAIL_CHEFSTABLE);
	}

	public static String getExtraAdServerProfileAttributes(){
		return get(PROP_AD_SERVER_PROFILE_ATTRIBS);
	}

	public static boolean getAdServerUsesDeferredImageLoading() {
		return Boolean.valueOf(get(PROP_AD_SERVER_USES_DEFERRED_IMAGE_LOADING)).booleanValue();
	}


	public static boolean performStorePreLoad(){
		return Boolean.valueOf(get(PROP_PRELOAD_STORE)).booleanValue();
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

	public static Date getDlvPromoExpDate(){
		Date d = null;
		try{
			d = SF.parse(get(PROP_DLV_PROMO_EXP_DATE));
		}catch(ParseException e){
			new FDRuntimeException("fdstore.dlvPromo.expDate property in fdstore.properties is not in correct yyyy-MM-dd format");
		}
		return d;
	}


	public static String getCmsMediaBaseURL(){
		return get(PROP_CMS_MEDIABASEURL);
	}

	public static String getCheckExternalForPaymentMethodFraud() {
		return config.getProperty(PROP_EXTERNAL_FRAUD_CHECK_PM);
	}

	public static Context getInitialContext() throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.PROVIDER_URL, getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory() );
		return new InitialContext(env);
	}
	public static Context getRoutingInitialContext() throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.PROVIDER_URL, getRoutingProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory() );
		return new InitialContext(env);
	}

	public static boolean getHamptons() {
        return (new Boolean(get(PROP_HAMPTONS))).booleanValue();
    }

	public static String getMaxReferrals() {
		return config.getProperty(PROP_MAX_REFERRALS);
    }

	public static String getNumDaysMaxReferrals() {
		return config.getProperty(PROP_NUM_DAYS_MAX_REFERRALS);
    }

	public static String getFDReferralManagerHome() {
		return get(PROP_FDREFERRALMGR_HOME);
	}

	public static boolean useMultiplePromotions(){
		return Boolean.valueOf(get(PROP_USE_MULTIPLE_PROMOTIONS)).booleanValue();
	}

	public static boolean isDataCollectionEnabled() {
		return Boolean.valueOf(get(PROP_DATA_COLLECTION_ENABLED)).booleanValue();
	}

	public static boolean isProductRecommendEnabled() {
		return Boolean.valueOf(get(PROP_PRODUCT_RECOMMEND_ENABLED)).booleanValue();
	}
	public static String getBSGSSignUpUrl(boolean isCallCenter){
		if(isCallCenter){
			return get(CRM_BSGS_SIGNUP_URL);
		}else {
			return get(BSGS_SIGNUP_URL);
		}
	}

	public static String getUnlimitedSignUpUrl(boolean isCallCenter){
		if(isCallCenter) {
			return get(CRM_UNLIMITED_SIGNUP_URL);
		}else{
			return get(UNLIMITED_SIGNUP_URL);
		}
	}

	public static String getUnlimitedPromotionalSignUpUrl(boolean isCallCenter){
		if(isCallCenter) {
			return get(CRM_UNLIMITED_PROMOTIONAL_SIGNUP_URL);
		}else{
			return get(UNLIMITED_PROMOTIONAL_SIGNUP_URL);
		}
	}

	public static String getUnlimitedAmazonPrimeSignUpUrl(boolean isCallCenter){
		if(isCallCenter) {
			return get(CRM_UNLIMITED_AMAZON_PRIME_SIGNUP_URL);
		}else{
			return get(UNLIMITED_AMAZON_PRIME_SIGNUP_URL);
		}
	}

	public static String getBSGSProfilePosfix(){
		return get(BSGS_PROFILE_POSFIX);
	}

	public static String getUnlimitedProfilePosfix(){
		return get(UNLIMITED_PROFILE_POSFIX);
	}
	public static String getUnlimitedPromotionalProfile(){
		return get(UNLIMITED_PROMOTIONAL_PROFILE);
	}

	public static String getUnlimitedAmazonPrimeProfile(){
		return get(UNLIMITED_AMAZON_PRIME_PROFILE );
	}


	public static String getDlvPassPromoPrefix() {
		return get(DLV_PASS_PROMOTION_PREFIX);
	}

	public static int getOrderProcessingLimit() {
		return Integer.parseInt(get(PROP_CRM_ORDER_PRC_LIMIT));
	}

	public static int getReferralPrgPaginationSize(){
		return Integer.parseInt(get(RFL_PRG_PAGINATION_SIZE));
	}

	public static int getSkuAvailabilityRefreshPeriod(){
		return Integer.parseInt(get(SKU_AVAILABILITY_REFRESH_PERIOD));
	}

	public static boolean isRetProgramCreateCase() {
        return (new Boolean(get(PROP_RETPRG_CREATECASE))).booleanValue();
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
	public static int getMktAdminFileUploadSize(){
		return Integer.parseInt(get(MKT_ADMIN_FILE_UPLOAD_SIZE));
	}

	public static String getWarmupClass() {
		return get(PROP_WARMUP_CLASS);
	}

	public static int getInventoryRefreshPeriod(){
		return Integer.parseInt(get(PROP_INVENOTRY_REFRESH_PERIOD));
	}

	public static boolean isCclAjaxDebugFacade() {
		return Boolean.valueOf(get(CCL_AJAX_DEBUG_FACADE)).booleanValue();
	}

	public static boolean isCclAjaxDebugJsonRpc() {
		return Boolean.valueOf(get(CCL_AJAX_DEBUG_JSONRPC)).booleanValue();
	}

	public static int getAttributesRefreshPeriod(){
		return Integer.parseInt(get(PROP_ATTRIBUTES_REFRESH_PERIOD));
	}

	public static String getCclAjaxDebugFacadeException() {
		refresh(true);
		return get(CCL_AJAX_DEBUG_FACADE_EXCEPTION);
	}

	public static int getNutritionRefreshPeriod(){
		return Integer.parseInt(get(PROP_NUTRITION_REFRESH_PERIOD));
	}

	public static boolean isCclEnabled() {
		return Boolean.valueOf(get(CCL_ENABLED)).booleanValue();
	}

	// Is SmartStore DYF feature enabled?
	public static boolean isDYFEnabled() {
		return Boolean.valueOf(get(DYF_ENABLED)).booleanValue();
	}

	public static float getDYFFreqboughtTopPercent() {
		return Float.parseFloat(get(DYF_FREQBOUGHT_TOPPERCENT));
	}

	public static int getDYFFreqboughtTopN() {
		return Integer.parseInt(get(DYF_FREQBOUGHT_TOPN));
	}

	public static String getSampleDistributionsPath() {
		return (String)get(DISTRIBUTION_SAMPLES_DIR);
	}

	public static int getPromotionRTRefreshPeriod() {
		return Integer.parseInt(config.getProperty(PROP_PROMOTION_RT_REFRESH_PERIOD));
	}

	public static String getAlternateZipcodeForGeocode(String srcZipcode) {
		return get(PROP_GEOCODE_ALTZIPCODE+"."+srcZipcode);
	}

	// cut off time
	public static String getCutOffTimeRange(int day) {
		return get("fdstore.cut_off_day_"+day);
	}

	// click to call display toggle
	public static boolean getClickToCall() {
        return (new Boolean(get(CLICK_TO_CALL))).booleanValue();
    }

	// customer service hours
	public static String getCustServHoursRange(int day) {
		return get("fdstore.cust_serv_day_"+day);
	}

	public static boolean isNewGeocodeFormat() {
        return (new Boolean(get(PROP_GEOCODE_ISNEWFORMAT))).booleanValue();
    }


	public static boolean isDCPDAliasHandlingEnabled() {
		return Boolean.valueOf(get(DCPD_ALIAS_HANDLING_ENABLED)).booleanValue();
	}

	public static DateRange getAdvanceOrderRange() {
		Date dStart = null;
		Date dEnd = null;
		try{
			dStart = SF.parse(get(ADVANCE_ORDER_START));
		}catch(ParseException e){
		    try {
		        dStart = SF.parse("2000-01-01");
		        LOGGER.warn("fdstore.advance.order.start property in fdstore.properties is not in correct yyyy-MM-dd format, defaulting to 2000-01-01");
		    } catch(ParseException f) {
			  throw new FDRuntimeException("Error parsing advance start date, default value");
		    }
		}
		try{
			dEnd = SF.parse(get(ADVANCE_ORDER_END));
		}catch(ParseException e){
		    try {
		        dEnd = SF.parse("2000-01-01");
		        LOGGER.warn("fdstore.advance.order.end property in fdstore.properties is not in correct yyyy-MM-dd format, defaulting to 2000-01-01");
		    } catch(ParseException f) {
			  throw new FDRuntimeException("Error parsing advance end date, default value");
		    }
		}

		return new DateRange(DateUtil.truncate(dStart),DateUtil.truncate(dEnd));
	}

	//  marketing admin changes
	public static String getMarketingAdminUrl() {
		return get(MRKTING_ADMIN_URL);
	}

	public static String getDefaultRenewalDP() {
		return get(DLV_PASS_AUTORENEWAL_DEFAULT);
	}

	public static boolean IsProduceRatingEnabled() {
		return Boolean.valueOf(get(PRODUCE_RATING_ENABLED)).booleanValue();
	}
	//	ratings
	public static String getRatingsSkuPrefixes(){
		return get(PRODUCE_RATING_PREFIXES);
	}

	// freshness guaranteed
	public static boolean IsFreshnessGuaranteedEnabled() {
		return Boolean.valueOf(get(FRESHNESS_GUARANTEED_ENABLED)).booleanValue();
	}
	public static String getFreshnessGuaranteedSkuPrefixes(){
		return get(FRESHNESS_GUARANTEED_PREFIXES);
	}

	//  marketing admin changes
	public static String getHPLetterMediaPathForNewUser() {
		return get(HP_LETTER_MEDIA_PATH1);
	}

	public static String getHPLetterMediaPathForOldUser() {
		return get(HP_LETTER_MEDIA_PATH2);
	}

	public static boolean IsHomePageMediaEnabled() {
		return Boolean.valueOf(get(HPLETTER_MEDIA_ENABLED)).booleanValue();
	}

	public static String getTemporaryDirectory() {
	    return get(TEMP_DIR);
	}

	// iphone email template non-customer
	public static String getMediaIPhoneTemplatePath() {
		return get(PROP_MEDIA_IPHONE_TEMPLATE_PATH);
	}

	public static String getIPhoneEmailSubject() {
		return get(IPHONE_EMAIL_SUBJECT);
	}

	// Gift Card
	public static boolean isGiftCardEnabled() {
		return Boolean.valueOf(get(PROP_GC_ENABLED)).booleanValue();
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
		return Integer.parseInt(config.getProperty(PROP_GIFT_CARD_RECIPIENT_MAX));
	}
	public static double getGiftCardMinAmount() {
		return Double.parseDouble(config.getProperty(PROP_GC_MIN_AMOUNT));
	}
	public static double getGiftCardMaxAmount() {
		return Double.parseDouble(config.getProperty(PROP_GC_MAX_AMOUNT));
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

	//deals
	public static String getDealsSkuPrefixes(){
		return get(DEALS_SKU_PREFIX);
	}

	public static int getDealsLowerLimit(){
		return Integer.parseInt(get(DEALS_LOWER_LIMIT));
	}
	public static int getDealsUpperLimit(){
		return Integer.parseInt(get(DEALS_UPPER_LIMIT));
	}

	public static int getBurstsLowerLimit(){
		try {
			return Integer.parseInt(get(BURST_LOWER_LIMIT));
		} catch (NumberFormatException e) {
			return 10;
		}
	}
	public static int getBurstUpperLimit(){
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

	public static boolean canUseLocationDB() {
        return (new Boolean(get(PROP_GEOCODE_USELOCATIONDB))).booleanValue();
    }

	public static boolean canSendRoutingAddress() {
        return (new Boolean(get(PROP_ROUTING_SENDADDRESS))).booleanValue();
    }


	public static boolean isCmsReadonlyOptimization() {
	   return Boolean.valueOf(get(PROP_CMS_MOSTLY_READONLY)).booleanValue();
	}

	/**
	 * Is Smart Search feature enabled?
	 *
	 * @deprecated Property is no longer used
	 */
	public static boolean isSmartSearchEnabled() {
		return (new Boolean(get(SMART_SEARCH_ENABLED))).booleanValue();
	}

	public static int getSmartstoreNewproductsDays() {
		return Integer.parseInt(get(SMARTSTORE_NEWPRODUCTS_DAYS));
	}

	public static Set<String> getSmartstorePreloadFactors() {
		String frs = get(SMARTSTORE_PRELOAD_FACTORS);
		if (frs == null)
			return Collections.<String>emptySet();

		String[] factors = frs.split(",");
		Set<String> fs = new HashSet<String>(factors.length);
		for (int i = 0; i < factors.length; i++) {
			String f = factors[i].trim();
			if (f.length() != 0)
				fs.add(f);
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

	public static int getCOOLInfoRefreshPeriod(){
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

	//What's Good
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
	public static boolean isWhatsGoodDebugOn() {
		return Boolean.valueOf(get(PROP_FDWHATSGOOD_DEBUG_ENABLED)).booleanValue();
	}


	/**
	 * Used for testing, do not call from the App.
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
 			if (addr.length() != 0)
 				bccs.add(addr);
 		}
 		return bccs;
 	}

	public static boolean IsAdvanceOrderGap() {
		return Boolean.valueOf(get(ADVANCE_ORDER_GAP)).booleanValue();
	}

	public static DateRange getAdvanceOrderNewRange() {
		Date dStart = null;
		Date dEnd = null;
		try{
			dStart = SF.parse(get(ADVANCE_ORDER_NEW_START));
		}catch(ParseException e){
		    try {
		        dStart = SF.parse("2000-01-01");
		        LOGGER.warn("fdstore.advance.order.newstart property in fdstore.properties is not in correct yyyy-MM-dd format, defaulting to 2000-01-01");
		    } catch(ParseException f) {
			  throw new FDRuntimeException("Error parsing advance new start date, default value");
		    }
		}
		try{
			dEnd = SF.parse(get(ADVANCE_ORDER_NEW_END));
		}catch(ParseException e){
		    try {
		        dEnd = SF.parse("2000-01-01");
		        LOGGER.warn("fdstore.advance.order.newend property in fdstore.properties is not in correct yyyy-MM-dd format, defaulting to 2000-01-01");
		    } catch(ParseException f) {
			  throw new FDRuntimeException("Error parsing advance new end date, default value");
		    }
		}

		return new DateRange(DateUtil.truncate(dStart),DateUtil.truncate(dEnd));
	}

	public static String getRoutingGatewayHome() {
		return get(PROP_ROUTINGGATEWAY_HOME);
	}
	public static boolean isDynamicRoutingEnabled() {
        return (new Boolean(get(DYNAMIC_ROUTING_ENABLED))).booleanValue();
    }

	public static boolean isGivexBlackHoleEnabled() {
        return (new Boolean(get(GIVEX_BLACK_HOLE_ENABLED))).booleanValue();
    }

	public static String getRoutingProviderURL() {
		return config.getProperty(PROP_ROUTING_PROVIDER_URL);
	}
	public static String getCTCapacityFileName()
	{
		return get(CT_DELIVERY_CAPACITY_FILE_NAME);
	}

	public static String getPR1CapacityFileName()
	{
		return get(PR1_DELIVERY_CAPACITY_FILE_NAME);
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
    		if (s.length() > 0)
    			siteFeatures.add(s);
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

	//Mobile
	public static boolean isIphoneLandingEnabled() {
		return Boolean.valueOf(get(MOBILE_IPHONE_LANDING_ENABLED)).booleanValue();
	}
	public static String getIphoneLandingPage() {
		return get(MOBILE_IPHONE_LANDING_PAGE);
	}


	public static String getDefaultPickupZoneId() {
		return get(PROP_ZONE_PICKUP_ZIPCODE);
	}

	public static int getNSMAuthSkipSecsForGC(){
		return Integer.parseInt(get(PROP_GC_NSM_AUTHSKIP_SECS));
	}

	public static int getNSMFreqSecsForGC(){
		return Integer.parseInt(get(PROP_GC_NSM_FREQ_SECS));
	}

	public static boolean isZonePricingEnabled(){
		return Boolean.valueOf(config.getProperty(PROP_ZONE_PRICING_ENABLED)).booleanValue();
	}

	public static boolean isZonePricingAdEnabled(){
		return Boolean.valueOf(config.getProperty(PROP_ZONE_PRICING_AD_ENABLED)).booleanValue();
	}

	public static String getWindowSteeringPromoPrefix() {
		return get(WINDOW_STEERING_PROMOTION_PREFIX);
	}

	/**
	 * OSCACHE should be disabled when we are in some development environment.
	 * ( = annotation or preview mode )
	 * Jsp-s can refer to this when deciding to use oscache or not.
	 * @return true if we are in production mode (use oscache), false if we are in development mode (disable oscache)
	 */
	public static boolean useOscache() {
		return !( isAnnotationMode() || getPreviewMode() );
	}

	public static boolean isStandingOrdersEnabled() {
		return Boolean.valueOf(config.getProperty(SO_GLOBAL_ENABLER)).booleanValue();
	}

	public static boolean isClientCodesEnabled() {
		return Boolean.valueOf(config.getProperty(CLIENT_CODES_GLOBAL_ENABLER)).booleanValue();
	}

	//new products revamp

	public static String getNewProductsDeptId() {
		return get(PROP_NEWPRODUCTS_DEPTID);
	}

	public static String getNewProductsCatId() {
		return get(PROP_NEWPRODUCTS_CATID);
	}

	public static String getNewProductsGrouping() {
		return get(PROP_NEWPRODUCTS_GROUPS);
	}

	public static String getFaqSections(){
		return get(PROP_FAQ_SECTIONS);
	}

	public static String getCrmAddressValiationHelpLink(){
		return get(PROP_CRM_HELP_LINK_ADDR_VALIDATION);
	}
	public static String getCrmGiftCardHelpLink(){
		return get(PROP_CRM_HELP_LINK_GIFT_CARD);
	}
	public static String getCrmMainHelpLink(){
		return get(PROP_CRM_HELP_LINK_MAIN_HELP);
	}
	public static String getCrmCustProfileHelpLink(){
		return get(PROP_CRM_HELP_LINK_CUST_PROFILE);
	}
	public static String getCrmTimeSlotHelpLink(){
		return get(PROP_CRM_HELP_LINK_TIMESLOT);
	}
	public static String getCrmFDUpdatesHelpLink(){
		return get(PROP_CRM_HELP_LINK_FD_UPDATES);
	}
	public static String getCrmPromotionsHelpLink(){
		return get(PROP_CRM_HELP_LINK_PROMOTIONS);
	}
	public static String getCrmCaseMediaHelpLink(){
		return get(PROP_CRM_HELP_LINK_CASE_MEDIA);
	}
	public static String getCrmCaseMoreIssuesHelpLink(){
		return get(PROP_CRM_HELP_LINK_CASE_MORE_ISSUES);
	}
	public static String getCrmCaseCustomerToneHelpLink(){
		return get(PROP_CRM_HELP_LINK_CASE_CUST_TONE);
	}
	public static String getCrmCaseFirstContactHelpLink(){
		return get(PROP_CRM_HELP_LINK_CASE_FIRST_CONTACT);
	}
	public static String getCrmCaseResolvFirstHelpLink(){
		return get(PROP_CRM_HELP_LINK_CASE_RESOLV_FIRST);
	}
	public static String getCrmResolutionSatisfHelpLink(){
		return get(PROP_CRM_HELP_LINK_CASE_RESOL_SATISFY);
	}
	
	//Email Opt-Down (APPDEV-662)
	public static boolean isEmailOptdownEnabled() {
		return Boolean.valueOf(config.getProperty(PROP_EMAIL_OPTDOWN_ENABLED)).booleanValue();
	}

	/**
	 * Returns API key to access Google Maps service
	 * No default value.
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
		return "master".equalsIgnoreCase( get(PROMO_PUBLISH_NODE_TYPE).trim() );
	}

	public static boolean isPromoPublishNodeReplica() {
		return "replica".equalsIgnoreCase( get(PROMO_PUBLISH_NODE_TYPE) );
	}

	public static String getPromoValidRTStatuses() {
		return get(PROMO_VALID_RT_STATUSES);
 	}

	public static int getRedeemCntRefreshPeriod() {
		return Integer.parseInt(config.getProperty(PROP_REDEMPTION_CNT_REFRESH_PERIOD));
	}

	public static int getRedemptionServerCount() {
		return Integer.parseInt(config.getProperty(PROP_REDEMPTION_SERVER_COUNT));
	}

	//Delivery Pass at Checkout (APPDEV-664)
	public static boolean isDPCartEnabled() {
		return Boolean.valueOf(config.getProperty(PROP_DP_CART_ENABLED)).booleanValue();
	}

	//Brand media replacement (APPDEV-1308)
	public static String getBrandMediaIds() {
		return get(PROP_BRAND_MEDIA_IDS);
	}

	public static boolean isPromoLineItemEmailDisplay() {
		return "true".equalsIgnoreCase( get(PROP_PROMO_LINE_ITEM_EMAIL) );
	}

	public static int get4mmRefreshInterval() {
		return Integer.parseInt(config.getProperty(PROP_4MM_REFRESH_INTERVAL));
	}

	public static int getUnassignedProcessingLimit() {
		return Integer.parseInt(get(PROP_ROUTING_UNASSIGNEDPROCESSINGLIMIT));
	}

	public static String getWSDiscountAmountList() {
		return get(PROP_WS_DISCOUNT_AMOUNT_LIST);
 	}
	
	public static void forceRefresh() {
		refresh(true);
	}

	/**
	 * Global switch to turn on/off wine ratings display. Defaulted to on.
	 * @return
	 */
	public static boolean isWineShowRatings() {
		return Boolean.toString(true).equalsIgnoreCase( get(WINE_SHOW_RATINGS_KEY) );
	}

	public static boolean isAdvertisingTileEnabled() {
		return Boolean.parseBoolean( get(ADVERTISING_TILE_ENABLED) );
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
			if (index == 0)
				defaultValue = 0.0;
			else if (index == 1)
				defaultValue = 10.0;
			else if (index == 2)
				defaultValue = 20.0;
			else
				defaultValue = 40.0;
			LOGGER.warn("cannot parse " + WINE_PRICE_BUCKET_BOUND_PREFIX + index +
					", using default value " + defaultValue, e);
			return defaultValue;
		}
	}
	
	public static int getCrmMenuRolesRefreshPeriod() {
		return Integer.parseInt(config.getProperty(PROP_CRM_MENU_ROLES_REFRESH_PERIOD));
	}
	
	public static int getCrmLDAPUsersRefreshPeriod() {
		return Integer.parseInt(config.getProperty(PROP_CRM_LDAP_USERS_REFRESH_PERIOD));
	}
	
	public static int getCrmAgentsCacheRefreshPeriod() {
		return Integer.parseInt(config.getProperty(PROP_CRM_AGENTS_CACHE_REFRESH_PERIOD));
	}
	
	public static String getCrmLDAPPrimaryHostName() {
		return config.getProperty(PROP_CRM_LDAP_ACCESS_HOST_NAME_PRIMARY);
	}
	public static int getCrmCCDetailsLookupLimit() {
		return Integer.parseInt(config.getProperty(PROP_CRM_CC_DETAILS_LOOKUP_LIMIT));
	}
	
	public static boolean isCrmCCSecurityNotificationEnabled() {
        return (new Boolean(get(PROP_CRM_CC_SECURITY_EMAIL_ENABLED))).booleanValue();
    }
	
	public static String getCrmCCSecurityEmail() {
		return config.getProperty(PROP_CRM_CC_SECURITY_EMAIL);
	}
	public static String getCrmCCSecurityEmailSubject() {
		return config.getProperty(PROP_CRM_CC_SECURITY_EMAIL_SUBJECT);
	}
	
	public static String getCrmCCDetailsAccessKey(){
		return config.getProperty(PROP_CRM_CC_DETAILS_ACCESS_KEY);
	}
	
	public static String getCrmForgotPasswordUrl(){
		return config.getProperty(PROP_CRM_FORGOT_LDAP_PASSWORD_URL);
	}

	public static String getCrmSecuritySkippedFileTypes(){
		return config.getProperty(PROP_CRM_SECURITY_SKIP_FILE_TYPES);
	}

	public static String getCrmSecuritySkippedFolders(){
		return config.getProperty(PROP_CRM_SECURITY_SKIP_FOLDERS);
	}

	public static boolean isMyfdEnabled() {
		return Boolean.parseBoolean(config.getProperty(MYFD_ENABLED)); 
	}

	public static String getMyFdPollDaddyApiKey() {
		return get(MYFD_POLLDADDY_API_KEY);
	}
}
