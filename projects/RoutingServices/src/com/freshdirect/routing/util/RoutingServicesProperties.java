/*
 * ErpServiceProperties.java
 *
 * Created on December 12, 2001, 6:34 PM
 */

package com.freshdirect.routing.util;


import java.util.Properties;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class RoutingServicesProperties {
	
	private static Properties config;

	private final static Properties defaults = new Properties();
		
	private final static String PROP_TRANSPORTATIONSUITE_PROVIDER_URL		= "routingservices.transportationsuite.providerURL";
	
	private final static String PROP_ROADNET_PROVIDER_URL		= "routingservices.roadnet.providerURL";
	
	//private final static String PROP_SERVICETIMEFACTOR_EXPRESSION		= "routingservices.servicetimefactor.expression";
	//private final static String PROP_SERVICETIME_EXPRESSION		= "routingservices.servicetime.expression";
	
	//private final static String PROP_PACKAGING_DEFAULTCARTONCOUNT		= "routingservices.packaging.defaultcartoncount";
	
	//private final static String PROP_PACKAGING_DEFAULTCASECOUNT		= "routingservices.packaging.defaultcasecount";
	
	//private final static String PROP_PACKAGING_DEFAULTFREEZERCOUNT		= "routingservices.packaging.defaultfreezercount";
	
	//private final static String PROP_TOTALSIZE1_EXPRESSION		= "routingservices.totalsize1.expression";
	
	//private final static String PROP_TOTALSIZE2_EXPRESSION		= "routingservices.totalsize2.expression";
	
	//private final static String PROP_DEFAULT_SERVICETIMETYPE		= "routingservices.default.servicetimetype";
	
	//private final static String PROP_DEFAULT_ZONETYPE		= "routingservices.default.zonetype";
	
	private final static String PROP_DEFAULT_REGION		= "routingservices.default.region";
	
	private final static String PROP_DEFAULT_LOCATIONTYPE		= "routingservices.default.locationtype";
	
	private final static String PROP_DEFAULT_ORDERTYPE		= "routingservices.default.ordertype";
	
	private final static String PROP_DEFAULT_FIXEDSERVICETIME		= "routingservices.default.fixedservicetime";
	
	private final static String PROP_DEFAULT_VARIABLESERVICETIME		= "routingservices.default.variableservicetime";
	
	private final static String PROP_INCRECEMT_WINDOWENDTIME		= "routingservices.increment.windowendtime";
	
	private static long lastRefresh = 0;
	private final static long REFRESH_PERIOD = 5 * 60 * 1000;
	
	private static final Category LOGGER = LoggerFactory.getInstance( RoutingServicesProperties.class );
	
	static {
		
		defaults.put(PROP_TRANSPORTATIONSUITE_PROVIDER_URL, 	"http://localhost:81");
		defaults.put(PROP_ROADNET_PROVIDER_URL, 	"http://localhost:82");
		//defaults.put(PROP_SERVICETIMEFACTOR_EXPRESSION, 	"(x+y+z)/3");
		//defaults.put(PROP_SERVICETIME_EXPRESSION, 	"a+(b*x)");
		//defaults.put(PROP_TOTALSIZE1_EXPRESSION, 	"x+y");
		//defaults.put(PROP_TOTALSIZE2_EXPRESSION, 	"z");
		//defaults.put(PROP_PACKAGING_DEFAULTCARTONCOUNT, 	"3");
		//defaults.put(PROP_PACKAGING_DEFAULTCASECOUNT, 	"3");
		//defaults.put(PROP_PACKAGING_DEFAULTFREEZERCOUNT, 	"3");
		defaults.put(PROP_DEFAULT_REGION, 	"FD");
		defaults.put(PROP_DEFAULT_LOCATIONTYPE, 	"SIT");
		defaults.put(PROP_DEFAULT_ORDERTYPE, 	"DEF");
		defaults.put(PROP_DEFAULT_FIXEDSERVICETIME, 	"5");
		defaults.put(PROP_DEFAULT_VARIABLESERVICETIME, 	"5");
		defaults.put(PROP_INCRECEMT_WINDOWENDTIME, 	"true");
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
	
	/*public static String getServiceTimeFactorExpression() {
		return get(PROP_SERVICETIMEFACTOR_EXPRESSION);
	}
	
	public static String getServiceTimeExpression() {
		return get(PROP_SERVICETIME_EXPRESSION);
	}
	
	public static String getTotalSize1Expression() {
		return get(PROP_TOTALSIZE1_EXPRESSION);
	}
	
	public static String getTotalSize2Expression() {
		return get(PROP_TOTALSIZE2_EXPRESSION);
	}
	
	public static String getDefaultServiceTimeType() {
		return get(PROP_DEFAULT_SERVICETIMETYPE);
	}
	
	public static String getDefaultZoneType() {
		return get(PROP_DEFAULT_ZONETYPE);
	}
		
	public static int getDefaultCartonCount() {
		return getIntVal(get(PROP_PACKAGING_DEFAULTCARTONCOUNT));
	}
	
	public static int getDefaultCaseCount() {
		return getIntVal(get(PROP_PACKAGING_DEFAULTCASECOUNT));
	}
	
	public static int getDefaultFreezerCount() {
		return getIntVal(get(PROP_PACKAGING_DEFAULTFREEZERCOUNT));
	}*/
	
	
	
	
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
