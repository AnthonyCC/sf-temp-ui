package com.freshdirect.mobileapi.util;

import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class MobileApiProperties {

    private static final Category LOG = LoggerFactory.getInstance(MobileApiProperties.class);

    private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd");

    private final static String PROP_DISCOVERY_URL = "mobileapi.discovery.url";

    private final static String PROP_CURRENT_VERSION = "mobileapi.version.current";

    private final static String PROP_VERSION_ACTION = "mobileapi.version.action.";

    private final static String PROP_CART_MISC_CHARGE_LABEL = "mobileapi.cart.misc.label";

    private final static String PROP_MEDIA_PATH = "mobileapi.media.path";
    
    private final static String PROP_ADDITION_MEDIA_PATH = "mobileapi.media.additionpath";

    private final static String PROP_WHATS_GOOD_CAT_IDS = "mobileapi.whatsgood.catIds";

    private final static String PROP_QUICKSHOP_LIST_MAX = "mobileapi.quickshoplist.max";

    private final static String PROP_OAS_PROTOCOL = "mobileapi.oas.protocol";

    private final static String PROP_ALCOHOL_AGE_WARNING = "mobileapi.mediapath.alcohol.agewarning";

    private final static String PROP_ALCOHOL_HEALTH_WARNING = "mobileapi.mediapath.alcohol.healthwarning";
    
    private final static String PROP_UNATTENDED_DLV_MSG = "mobileapi.mediapath.unattende.ddlv.msg";

    private final static String PROP_OAS_CACHE_TIMEOUT = "mobileapi.oas.timeout";
    
    private final static String PROP_BROWSE_ENABLED = "mobileapi.browse.enabled";
    
    private final static String PROP_BASECONTROLLER_LOGGING_ENABLED = "mobileapi.basecontroller.logging.enabled";
    
    private final static String PROP_EXTERNAL_INTERFACE_ENABLED = "mobileapi.extint.enabled";
    
    private final static String PROP_SUSTAINABILITY_RATING_ENABLED = "mobileapi.sustainabilityrating.enabled";
    
    private final static String PROP_SAMEDAY_DP_COMPATIBLE = "mobileapi.samedaydp.compatible";

    private final static String PROP_MIDDLE_TIER_URL = "mobileapi.middletier.url";
    
    public final static String UPGRADE = "UPGRADE";

    public final static String INCOMPATIBLE = "INCOMPATIBLE";

    private static long lastRefresh = 0;

    private final static long REFRESH_PERIOD = 5 * 60 * 1000;
    
    private static Properties config;

	private static final String PROP_EWALLET_PAYPAL_ENABLED = "mobileapi.ewallet.paypal.enabled";
	private static final String PROP_EWALLET_MASTERPASS_ENABLED = "mobileapi.ewallet.masterpass.enabled";
	
    private static final String CORS_DOMAIN = "mobileapi.CORS.domain";
    private static final String PROP_3RDPARTY_P3PENABLED = "mobileapi.3rdparty.p3penabled";

    private final static Properties defaults = new Properties();

    static {
        defaults.put(PROP_CART_MISC_CHARGE_LABEL, "Fuel Surcharge");
        defaults.put(PROP_MEDIA_PATH, "http://www.freshdirect.com");
        defaults.put(PROP_QUICKSHOP_LIST_MAX, "100");
        defaults.put(PROP_OAS_PROTOCOL, "http");
        defaults.put(PROP_OAS_CACHE_TIMEOUT, "60");
        defaults.put(PROP_BROWSE_ENABLED, "true");
        defaults.put(PROP_BASECONTROLLER_LOGGING_ENABLED, "true");
        defaults.put(PROP_EXTERNAL_INTERFACE_ENABLED, "true");
        defaults.put(PROP_SUSTAINABILITY_RATING_ENABLED, "true");
        defaults.put(PROP_SAMEDAY_DP_COMPATIBLE, "true");
		defaults.put(PROP_ADDITION_MEDIA_PATH,"/media/editorial/win_fdw/icons/");
		defaults.put(PROP_EWALLET_PAYPAL_ENABLED, "true");
		defaults.put(PROP_EWALLET_MASTERPASS_ENABLED, "true");
		defaults.put(CORS_DOMAIN, "*");
		defaults.put(PROP_3RDPARTY_P3PENABLED, "true");

        refresh();
    }

    private MobileApiProperties() {
    }

    private static void refresh() {
        refresh(false);
    }

    private synchronized static void refresh(boolean force) {
        long t = System.currentTimeMillis();
        if (force || (t - lastRefresh > REFRESH_PERIOD)) {
            config = ConfigHelper.getPropertiesFromClassLoader("mobileapi.properties", defaults);
            lastRefresh = t;
            LOG.info("Loaded configuration from mobileapi.properties: " + config);
        }
    }

    private static String get(String key) {
        refresh();
        return config.getProperty(key);
    }

    private static int getInt(String key) {
        int value = 0;
        try {
            value = Integer.parseInt(get(key));
        } catch (NumberFormatException e) {
            LOG.error(key + " value was null. Returning '0' as value");
        }
        return value;
    }

    public static String getMiscChargeLabel() {
        return get(PROP_CART_MISC_CHARGE_LABEL);
    }

    public static String getApiVersionAction(String version) {
        return get(PROP_VERSION_ACTION + version);
    }

    public static String getCurrentApiVersion() {
        return get(PROP_CURRENT_VERSION);
    }
    
    public static String getMiddleTierUrl() {
    	String middleTier = get(PROP_MIDDLE_TIER_URL);
    	if(middleTier == null || middleTier.isEmpty())
    		return "http://8.42.36.54/dl"; // DEFAULT: This is the development server URL. 
    	return get(PROP_MIDDLE_TIER_URL);
    }

    public static String getDiscoveryServiceUrl() {
        return get(PROP_DISCOVERY_URL);
    }

    public static String getWhatsGoodCatIds() {
        return get(PROP_WHATS_GOOD_CAT_IDS);
    }

    public static String getMediaPath() {
        return get(PROP_MEDIA_PATH);
    }

    public static int getQuickshopListMax() {
        return getInt(PROP_QUICKSHOP_LIST_MAX);
    }

    public static int getOasCacheTimeout() {
        return getInt(PROP_OAS_CACHE_TIMEOUT);
    }

    public static String getOasCommunicationProtocol() {
        return get(PROP_OAS_PROTOCOL);
    }

    /*
     *     private final static String PROP_ALCOHOL_AGE_WARNING = "mobileapi.mediapath.alcohol.agewarning";
        private final static String PROP_ALCOHOL_HEALTH_WARNING = "mobileapi.mediapath.alcohol.healthwarning";

     */
    public static String getAlcoholAgeWarningMediaPath() {
        return get(PROP_ALCOHOL_AGE_WARNING);
    }

    public static String getAlcoholHealthWarningMediaPath() {
        return get(PROP_ALCOHOL_HEALTH_WARNING);
    }
    
    public static boolean isBrowseEnabled() {
        return (Boolean.valueOf(get(PROP_BROWSE_ENABLED)).booleanValue());
    }
    
    public static boolean isBaseControllerLoggingEnabled() {
        return (Boolean.valueOf(get(PROP_BASECONTROLLER_LOGGING_ENABLED)).booleanValue());
    }
    
    public static boolean isExternalInterfaceEnabled() {
        return (Boolean.valueOf(get(PROP_EXTERNAL_INTERFACE_ENABLED)).booleanValue());
    }
    
    public static boolean isSustainabilityRatingEnabled() {
        return (Boolean.valueOf(get(PROP_SUSTAINABILITY_RATING_ENABLED)).booleanValue());
    }
    
    public static boolean isSameDayDpCompatible() {
    	return (Boolean.valueOf(get(PROP_SAMEDAY_DP_COMPATIBLE)).booleanValue());
    }
    
    public static String getUnattendedDeliveryMediaPath() {
        return get(PROP_UNATTENDED_DLV_MSG);
    }
    
    public static String getAdditionMediaPath(){
    	return get(PROP_ADDITION_MEDIA_PATH);
    }

	public static boolean isPayPalEnabled() {
        return Boolean.valueOf(get(PROP_EWALLET_PAYPAL_ENABLED)).booleanValue();
    }
	
	public static boolean isMasterpassEnabled() {
        return Boolean.valueOf(get(PROP_EWALLET_MASTERPASS_ENABLED)).booleanValue();
    }
	
    public static String getCORSDomain() {
       return get(CORS_DOMAIN);
    }

    public static boolean isP3PPolicyEnabled() {
        return Boolean.valueOf(get(PROP_3RDPARTY_P3PENABLED)).booleanValue();
    }

}
