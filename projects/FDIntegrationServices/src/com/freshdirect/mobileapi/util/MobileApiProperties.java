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

    private static final Category LOGGER = LoggerFactory.getInstance(MobileApiProperties.class);

    private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd");

    private final static String PROP_DISCOVERY_URL = "mobileapi.discovery.url";

    private final static String PROP_CURRENT_VERSION = "mobileapi.version.current";

    private final static String PROP_VERSION_ACTION = "mobileapi.version.action.";

    private final static String PROP_CART_MISC_CHARGE_LABEL = "mobileapi.cart.misc.label";

    private final static String PROP_MEDIA_PATH = "mobileapi.media.path";

    public final static String UPGRADE = "UPGRADE";

    public final static String INCOMPATIBLE = "INCOMPATIBLE";

    private static long lastRefresh = 0;

    private final static long REFRESH_PERIOD = 5 * 60 * 1000;

    private static Properties config;

    private final static Properties defaults = new Properties();

    static {
        defaults.put(PROP_CART_MISC_CHARGE_LABEL, "Fuel Surcharge");
        defaults.put(PROP_MEDIA_PATH, "http://www.freshdirect.com");
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
            LOGGER.info("Loaded configuration from mobileapi.properties: " + config);
        }
    }

    private static String get(String key) {
        refresh();
        return config.getProperty(key);
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

    public static String getDiscoveryServiceUrl() {
        return get(PROP_DISCOVERY_URL);
    }

    public static String getMediaPath(){
        return get(PROP_MEDIA_PATH);
    }
}
