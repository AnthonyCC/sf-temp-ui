/*
 * ErpServiceProperties.java
 *
 * Created on December 12, 2001, 6:34 PM
 */

package com.freshdirect.routing.util;


import java.util.Properties;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class RoutingServicesProperties {
	
	private static Properties config;

	private final static Properties defaults = new Properties();
		
	private final static String PROP_TRANSPORTATIONSUITE_PROVIDER_URL		= "routingservices.transportationsuite.providerURL";
	
	private final static String PROP_ROADNET_PROVIDER_URL		= "routingservices.roadnet.providerURL";
		
	private final static String PROP_DEFAULT_REGION		= "routingservices.default.region";
	
	private final static String PROP_DEFAULT_LOCATIONTYPE		= "routingservices.default.locationtype";
	
	private final static String PROP_DEFAULT_ORDERTYPE		= "routingservices.default.ordertype";
	
	private final static String PROP_DEFAULT_FIXEDSERVICETIME		= "routingservices.default.fixedservicetime";
	
	private final static String PROP_DEFAULT_VARIABLESERVICETIME		= "routingservices.default.variableservicetime";
	
	private final static String PROP_INCRECEMT_WINDOWENDTIME		= "routingservices.increment.windowendtime";
	
	private final static String PROP_TIMEWINDOW_FACTOR		= "routingservices.timewindow.factor";
	
	private final static String PROP_REMOVESCHEDULER_ENABLED		= "routingservices.removescheduler.enabled";
	
	private final static String PROP_LOADBALANCE_ENABLED		= "routingservices.loadbalance.enabled";
	
	private static long lastRefresh = 0;
	private final static long REFRESH_PERIOD = 5 * 60 * 1000;
	
	private static final Category LOGGER = LoggerFactory.getInstance( RoutingServicesProperties.class );
	
	static {
		
		defaults.put(PROP_TRANSPORTATIONSUITE_PROVIDER_URL, 	"http://localhost:81");
		defaults.put(PROP_ROADNET_PROVIDER_URL, 	"http://localhost:82");		
		defaults.put(PROP_DEFAULT_REGION, 	"FD");
		defaults.put(PROP_DEFAULT_LOCATIONTYPE, 	"SIT");
		defaults.put(PROP_DEFAULT_ORDERTYPE, 	"DEF");
		defaults.put(PROP_DEFAULT_FIXEDSERVICETIME, 	"5");
		defaults.put(PROP_DEFAULT_VARIABLESERVICETIME, 	"5");
		defaults.put(PROP_INCRECEMT_WINDOWENDTIME, 	"true");
		defaults.put(PROP_TIMEWINDOW_FACTOR, 	"5");
		defaults.put(PROP_REMOVESCHEDULER_ENABLED, 	"true");
		defaults.put(PROP_LOADBALANCE_ENABLED, 	"true");
		refresh();		
	}

	/** Creates new ErpServiceProperties */
    public RoutingServicesProperties() {
    }
    
    private static void refresh() {
		refresh(false);
	}
	
	private synchronized static void refresh(boolean force) {
		long t = System.currentTimeMillis();
		if (force || (t - lastRefresh > REFRESH_PERIOD)) {
			config = ConfigHelper.getPropertiesFromClassLoader("routingservices.properties", defaults);
			lastRefresh = t;
			LOGGER.info("Loaded configuration from routingservices.properties: " + config);
		}
	}

	
	private static String get(String key) {
		refresh();
		return config.getProperty(key);
	}
		
	public static void set(String key, String value) {
		config.setProperty(key, value);
	}

	public static String getTransportationSuiteProviderURL() {
		return get(PROP_TRANSPORTATIONSUITE_PROVIDER_URL);
	}
	
	public static String getRoadNetProviderURL() {
		return get(PROP_ROADNET_PROVIDER_URL);
	}
	
	public static int getDefaultFixedServiceTime() {
		return getIntVal(get(PROP_DEFAULT_FIXEDSERVICETIME));
	}
	
	public static int getDefaultVariableServiceTime() {
		return getIntVal(get(PROP_DEFAULT_VARIABLESERVICETIME));
	}
	
	public static String getDefaultRegion() {
		return get(PROP_DEFAULT_REGION);
	}
	
	public static String getDefaultLocationType() {
		return get(PROP_DEFAULT_LOCATIONTYPE);
	}
	
	public static String getDefaultOrderType() {
		return get(PROP_DEFAULT_ORDERTYPE);
	}
	
	public static boolean isIncrementWindowEndTime() {
        return (new Boolean(get(PROP_INCRECEMT_WINDOWENDTIME))).booleanValue();
    }
	
	public static boolean isRemoveSchedulerEnabled() {
        return (new Boolean(get(PROP_REMOVESCHEDULER_ENABLED))).booleanValue();
    }
	
	public static boolean isLoadBalanceEnabled() {
        return (new Boolean(get(PROP_LOADBALANCE_ENABLED))).booleanValue();
    }
	
	
	public static int getDefaultTimeWindowFactor() {
		return getIntVal(get(PROP_TIMEWINDOW_FACTOR));
	}
	
	private static int getIntVal(String strVal) {
		int intVal = 0;
		try {
			intVal = Integer.parseInt(strVal.trim());
		} catch(NumberFormatException exp) {
			// DO nothing
		}
		return intVal;
	}

}
