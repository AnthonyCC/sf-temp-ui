package com.freshdirect.fdstore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Category;

import com.freshdirect.ecommerce.data.enums.EnumPropertyType;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDEcommProperties {

    private static final Category LOGGER = LoggerFactory.getInstance(FDEcommProperties.class);
    private static List<ConfigLoadedListener> listeners = new ArrayList<ConfigLoadedListener>();
    private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd");
   
    private static Properties config;
    private final static Properties defaults = new Properties();
  
    private static long lastRefresh = 0;
    private final static long REFRESH_PERIOD = 5 * 60 * 1000;
    

    private final static String PROP_OBSOLETE_MERGECARTPAGE_ENABLED = "fdstore.obsolete.mergecartpage.enabled";
    private final static String FDMonitorSB = " monitor.FDMonitorSB";
    
    
    static {
        defaults.put(PROP_OBSOLETE_MERGECARTPAGE_ENABLED, "true");
        defaults.put(FDMonitorSB, "false");
        

        refresh();
    }

    private FDEcommProperties() {
    }

    private static void refresh() {
        refresh(false);
    }

    private synchronized static void refresh(boolean force) {
        long t = System.currentTimeMillis();

        if (true/*force || ((t - lastRefresh) > REFRESH_PERIOD)*/) {
            config = ConfigHelper.getPropertiesFromDB(EnumPropertyType.WEB.toString(),
            		FDStoreProperties.getClusterName(),
            		FDStoreProperties.getNodeName(), defaults);
            lastRefresh = t;
            LOGGER.info("Loaded configuration from FDStorePropertiesDB Database: " + config);
            fireEvent();
        }
    }



	public static String get(String key) {
        refresh();

        return config.getProperty(key);
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


    public static void forceRefresh() {
        refresh(true);
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

   

    public static interface ConfigLoadedListener {

        void configLoaded();
    }

    public static Properties getConfig() {
        return config;
    }


	public static boolean isObsoleteMergeCartPageEnabled() {
    	return (Boolean.valueOf(get(PROP_OBSOLETE_MERGECARTPAGE_ENABLED))).booleanValue();
	}
	public static boolean isServiceEnabled(String beanName) {
		refresh();
		if(config.containsKey(beanName))
    	return (Boolean.valueOf(config.getProperty(beanName))).booleanValue();
		return false;
	}
	
	
}
