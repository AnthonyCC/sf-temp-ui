package com.freshdirect.athena.util;

import java.util.Properties;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.athena.config.ISystemProperty;

public class AthenaProperties implements ISystemProperty {
	
	private static final Category LOGGER =  Logger.getLogger(AthenaProperties.class);
	
	private static Properties config;	
    private final static Properties defaults = new Properties();
    private static long lastRefresh = 0;
    private final static long REFRESH_PERIOD = 5 * 60 * 1000;
    
	static {
        defaults.put(CONFIG_REFRESH_FREQUENCY, "300000");
        defaults.put(DEFAULT_CACHE_FEQUENCY, "600000");
        defaults.put(XCELSIUS_ROOT, "xcelsius");
        refresh();
    }
	
	public static int getConfigRefreshFrequency() {
        return Integer.parseInt(get(CONFIG_REFRESH_FREQUENCY));
    }
	
	public static int getDefaultCacheFrequency() {
        return Integer.parseInt(get(DEFAULT_CACHE_FEQUENCY));
    }
	
	public static String getDefaultXcelsiusRoot() {
        return get(XCELSIUS_ROOT);
    }

    private AthenaProperties() {
    }

    private static void refresh() {
        refresh(false);
    }

    private synchronized static void refresh(boolean force) {
        long t = System.currentTimeMillis();

        if (force || ((t - lastRefresh) > REFRESH_PERIOD)) {
            config = ResourceUtil.getPropertiesFromClassLoader("athena.properties", defaults);
            lastRefresh = t;
            LOGGER.info("Loaded configuration from athena.properties: " + config);
        }
    }

    public static String get(String key) {
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
    
    
}
