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
		
	private final static String PROP_DEFAULT_TRUCKREGION		= "routingservices.default.truckregion";
	
	private final static String PROP_DEFAULT_DEPOTREGION		= "routingservices.default.depotregion";
	
	private final static String PROP_DEFAULT_LOCATIONTYPE		= "routingservices.default.locationtype";
	
	private final static String PROP_DEFAULT_ORDERTYPE		= "routingservices.default.ordertype";
	
	private final static String PROP_DEFAULT_FIXEDSERVICETIME		= "routingservices.default.fixedservicetime";
	
	private final static String PROP_DEFAULT_VARIABLESERVICETIME		= "routingservices.default.variableservicetime";
	
	private final static String PROP_INCRECEMT_WINDOWENDTIME		= "routingservices.increment.windowendtime";
	
	private final static String PROP_TIMEWINDOW_FACTOR		= "routingservices.timewindow.factor";
	
	private final static String PROP_REMOVESCHEDULER_ENABLED		= "routingservices.removescheduler.enabled";
	
	private final static String PROP_LOADBALANCE_ENABLED		= "routingservices.loadbalance.enabled";
	
	private final static String PROP_ROUTING_FLOWTYPE		= "routingservices.routing.flowtype";
	
	private final static String PROP_ROUTINGPARAM_CONFIRM		= "routingservices.routingparam.confirm";
	private final static String PROP_ROUTINGPARAM_SEQUENCED		= "routingservices.routingparam.sequenced";
	private final static String PROP_ROUTINGPARAM_SINGLEROUTE		= "routingservices.routingparam.singleroute";
	private final static String PROP_ROUTINGPARAM_MOVABLE		= "routingservices.routingparam.movable";
	
	private final static String PROP_ROUTINGPARAM_RETRIEVEACTIVITIES		= "routingservices.routingparam.retrieveactivities";
	private final static String PROP_ROUTINGPARAM_RETRIEVEEQUIPMENT		= "routingservices.routingparam.retrieveequipment";
	private final static String PROP_ROUTINGPARAM_RETRIEVEACTIVE		= "routingservices.routingparam.retrieveactive";
	private final static String PROP_ROUTINGPARAM_RETRIEVEBUILT		= "routingservices.routingparam.retrievebuilt";
	private final static String PROP_ROUTINGPARAM_RETRIEVEPUBLISHED		= "routingservices.routingparam.retrievepublished";
	
	private final static String PROP_LATEDELIVERY_QUERY		= "routingservices.latedelivery.query";
	
	private final static String PROP_LDPROCESSING_ENABLED		= "routingservices.ldprocessing.enabled";
	
	private final static String PROP_DEFAULTDEPOT_LOCATIONTYPE		= "routingservices.defaultdepot.locationtype";
	
	private static long lastRefresh = 0;
	private final static long REFRESH_PERIOD = 5 * 60 * 1000;
	
	private final static String PROP_TRANSPORTATIONSUITE_PROXY_URL		= "routingservices.transportationsuite.proxyURL";
	
	private final static String PROP_TRANSPORTATIONSUITEBATCH_PROVIDER_URL		= "routingservices.transportationsuitebatch.providerURL";
	
	private final static String PROP_TRANSPORTATIONSUITEDBATCH_PROVIDER_URL		= "routingservices.transportationsuitedbatch.providerURL";
	
	private final static String PROP_ROADNETBATCH_PROVIDER_URL		= "routingservices.roadnetbatch.providerURL";
	
	private final static String PROP_TRANSPORTATIONSUITE_USEPROXY		= "routingservices.transportationsuite.useproxy";
	
	private final static String PROP_ORDER_ESTIMATIONRANGE		= "routingservices.order.estimationrange";
	
	private final static String PROP_SERVICE_TIMEOUT="routingservices.serviceTimeOut";
	
	private final static String PROP_BATCH_SERVICE_TIMEOUT="routingservices.batchServiceTimeOut";
	
	private final static String PROP_DEFAULT_NOTIFICATIONRECEIVER		= "routingservices.default.notificationreceiver";
	
	private final static String PROP_DYNAMICANALYZE_MULTITHREADENABLED		= "routingservices.dynamicanalyze.multithreadenabled";
	
	private final static String PROP_ORDERMETRICSUSEACTUAL_THRESHOLD		= "routingservices.ordermetricsuseactual.threshold";
	
	private final static String PROP_ROUTINGSUBSCRIPTION_MAIL_TO="routingsubscription.mail.to";

	private final static String PROP_ROUTINGSUBSCRIPTION_MAIL_CC="routingsubscription.mail.cc";

	private final static String PROP_ROUTINGSUBSCRIPTION_MAIL_FROM = "routingsubscription.mail.from";
	
	private final static String PROP_DEPOT_DEPARTTIMEDIFF = "routingservices.depot.departtimediff";
	
	private final static String PROP_CUTOFFSIMSTANDBY_ENABLED		= "routingservices.cutoffsimstandby.enabled";
	
	private final static String PROP_ROADNETSESSIONSIZE_THRESHOLD = "routingservices.roadnetsessionsize.threshold";
	
	private final static String PROP_HANDOFFROUTEIN_MULTITHREADENABLED		= "routingservices.handoffroutein.multithreadenabled";
	
	private final static String PROP_DEFAULT_PLANTCODE		= "routingservices.default.plantcode";
	
	private final static String PROP_HANDOFF_MAILFROM		= "routingservices.handoff.mailfrom";
	
	private final static String PROP_HANDOFF_MAILTO		= "routingservices.handoff.mailto";
	
	private final static String PROP_HANDOFF_MAILCC		= "routingservices.handoff.mailcc";
	
	private final static String PROP_HANDOFF_MAILSUBJECT		= "routingservices.handoff.mailsubject";
	
	private final static String PROP_JDBCBATCHUPDATE_THRESHOLD		= "routingservices.jdbcbatchupdate.threshold";
	
	private final static String PROP_DEFAULT_ORDERSIZE		= "routingservices.default.ordersize";
	
	private final static String PROP_DEFAULT_SERVICETIME		= "routingservices.default.servicetime";
	
	private final static String PROP_HANDOFF_DISPATCHCORRELATIONENABLED		= "routingservices.handoff.dispatchcorrelationenabled";
	
	private final static String PROP_ROUTING_DYNASYNC_ENABLED		= "routingservices.routing.dynasyncenabled";
	
	private final static String PROP_ROUTING_BATCHSYNC_ENABLED		= "routingservices.handoff.batchsyncenabled";
	
	private static final Category LOGGER = LoggerFactory.getInstance( RoutingServicesProperties.class );
	
	static {
		
		defaults.put(PROP_TRANSPORTATIONSUITE_PROVIDER_URL, 	"http://localhost:81");
		defaults.put(PROP_ROADNET_PROVIDER_URL, 	"http://localhost:82");	
		
		defaults.put(PROP_DEFAULT_TRUCKREGION, 	"FD");
		defaults.put(PROP_DEFAULT_DEPOTREGION, 	"MDP");
		defaults.put(PROP_DEFAULT_LOCATIONTYPE, 	"SIT");
		defaults.put(PROP_DEFAULTDEPOT_LOCATIONTYPE, 	"MDP");
		defaults.put(PROP_DEFAULT_ORDERTYPE, 	"DEF");
		defaults.put(PROP_DEFAULT_FIXEDSERVICETIME, 	"5");
		defaults.put(PROP_DEFAULT_VARIABLESERVICETIME, 	"5");
		defaults.put(PROP_INCRECEMT_WINDOWENDTIME, 	"true");
		defaults.put(PROP_TIMEWINDOW_FACTOR, 	"5");
		defaults.put(PROP_REMOVESCHEDULER_ENABLED, 	"true");
		defaults.put(PROP_LOADBALANCE_ENABLED, 	"true");
		defaults.put(PROP_ROUTING_FLOWTYPE, "LINEAR");
		
		defaults.put(PROP_ROUTINGPARAM_CONFIRM, "true");
		defaults.put(PROP_ROUTINGPARAM_SEQUENCED, "false");
		defaults.put(PROP_ROUTINGPARAM_SINGLEROUTE, "false");
		defaults.put(PROP_ROUTINGPARAM_MOVABLE, "true");
		
		defaults.put(PROP_DYNAMICANALYZE_MULTITHREADENABLED, "false");
		
		defaults.put(PROP_ROUTINGPARAM_RETRIEVEACTIVITIES, "true");
		defaults.put(PROP_ROUTINGPARAM_RETRIEVEEQUIPMENT, "true");
		defaults.put(PROP_ROUTINGPARAM_RETRIEVEACTIVE, "true");
		defaults.put(PROP_ROUTINGPARAM_RETRIEVEBUILT, "true");
		defaults.put(PROP_ROUTINGPARAM_RETRIEVEPUBLISHED, "true");
		defaults.put(PROP_LDPROCESSING_ENABLED, 	"true");
		
		defaults.put(PROP_TRANSPORTATIONSUITE_USEPROXY, 	"false");
		defaults.put(PROP_ORDER_ESTIMATIONRANGE, 	"5");
		
		defaults.put(PROP_SERVICE_TIMEOUT, "120");// in seconds 
		defaults.put(PROP_BATCH_SERVICE_TIMEOUT, "600");// in seconds
		
		defaults.put(PROP_DEFAULT_NOTIFICATIONRECEIVER, "STO");
		defaults.put(PROP_ORDERMETRICSUSEACTUAL_THRESHOLD, "60");
		
		defaults.put(PROP_ROUTINGSUBSCRIPTION_MAIL_TO, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_ROUTINGSUBSCRIPTION_MAIL_CC, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_ROUTINGSUBSCRIPTION_MAIL_FROM, "applicationdevelopment@freshdirect.com");
		
		defaults.put(PROP_DEPOT_DEPARTTIMEDIFF, "0");
		
		defaults.put(PROP_CUTOFFSIMSTANDBY_ENABLED, "false");
		
		defaults.put(PROP_ROADNETSESSIONSIZE_THRESHOLD, "3000");
		defaults.put(PROP_JDBCBATCHUPDATE_THRESHOLD, "2000");
		defaults.put(PROP_HANDOFFROUTEIN_MULTITHREADENABLED, "true");
		defaults.put(PROP_DEFAULT_PLANTCODE, "1000");
		
		defaults.put(PROP_HANDOFF_MAILFROM, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_HANDOFF_MAILTO, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_HANDOFF_MAILCC, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_HANDOFF_MAILSUBJECT, "HandOff Process Notification");
		
		defaults.put(PROP_DEFAULT_ORDERSIZE, "5");
		defaults.put(PROP_DEFAULT_SERVICETIME, "180");
		
		defaults.put(PROP_HANDOFF_DISPATCHCORRELATIONENABLED, "true");
		
		defaults.put(PROP_ROUTING_DYNASYNC_ENABLED, "true");
		defaults.put(PROP_ROUTING_BATCHSYNC_ENABLED, "true");
		
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
	
	public static int getDepotDepartTimeDiff() {
		return getIntVal(get(PROP_DEPOT_DEPARTTIMEDIFF));
	}
	
	public static String getRoutingFlowType() {
		return get(PROP_ROUTING_FLOWTYPE);
	}
	
	public static String getTransportationSuiteBatchProviderURL() {
		return get(get(PROP_TRANSPORTATIONSUITEBATCH_PROVIDER_URL));
	}
	
	public static String getTransportationSuiteDBatchProviderURL() {
		return get(get(PROP_TRANSPORTATIONSUITEDBATCH_PROVIDER_URL));
	}
	
	public static String getRoadNetBatchProviderURL() {
		return get(get(PROP_ROADNETBATCH_PROVIDER_URL));
	}
	
	public static String getTransportationSuiteProviderURL() {
		return get(get(PROP_TRANSPORTATIONSUITE_PROVIDER_URL));
	}

	public static String getTransportationSuiteProxyURL() {
		return get(get(PROP_TRANSPORTATIONSUITE_PROXY_URL));
	}


	public static String getTransportationSuiteProviderURL(String serviceType) {
		String _refUrl = get(PROP_TRANSPORTATIONSUITE_PROVIDER_URL+"."+serviceType);
		if(_refUrl != null) {
			return get(_refUrl);
		}
		return null;
	}

	public static String getRoadNetProviderURL() {
		return get(get(PROP_ROADNET_PROVIDER_URL));
	}
	
	public static int getDefaultFixedServiceTime() {
		return getIntVal(get(PROP_DEFAULT_FIXEDSERVICETIME));
	}
	
	public static int getDefaultVariableServiceTime() {
		return getIntVal(get(PROP_DEFAULT_VARIABLESERVICETIME));
	}
	
	public static String getDefaultTruckRegion() {
		return get(PROP_DEFAULT_TRUCKREGION);
	}
	
	public static String getDefaultDepotRegion() {
		return get(PROP_DEFAULT_DEPOTREGION);
	}
	
	public static String getDefaultLocationType() {
		return get(PROP_DEFAULT_LOCATIONTYPE);
	}
	
	public static String getDefaultOrderType() {
		return get(PROP_DEFAULT_ORDERTYPE);
	}
	
	public static String getRoutingSubscriptionMailTo() {
		return get(PROP_ROUTINGSUBSCRIPTION_MAIL_TO);
	}
	
	public static String getRoutingSubscriptionMailCC() {
		return get(PROP_ROUTINGSUBSCRIPTION_MAIL_CC);
	}
	
	public static String getRoutingSubscriptionMailFrom() {
		return get(PROP_ROUTINGSUBSCRIPTION_MAIL_FROM);
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
	
	public static boolean isAnalyzeMultiThread() {
        return (new Boolean(get(PROP_DYNAMICANALYZE_MULTITHREADENABLED))).booleanValue();
    }
	
	public static boolean isHandOffRouteInMultiThread() {
        return (new Boolean(get(PROP_HANDOFFROUTEIN_MULTITHREADENABLED))).booleanValue();
    }
	
	
	public static boolean isLDProcessingEnabled() {
        return (new Boolean(get(PROP_LDPROCESSING_ENABLED))).booleanValue();
    }
	
	public static boolean isProxyEnabled() {
		return (new Boolean(get(PROP_TRANSPORTATIONSUITE_USEPROXY))).booleanValue();
	}
	
	public static int getDefaultTimeWindowFactor() {
		return getIntVal(get(PROP_TIMEWINDOW_FACTOR));
	}
	
	public static boolean getRoutingParamConfirm() {
        return (new Boolean(get(PROP_ROUTINGPARAM_CONFIRM))).booleanValue();
    }
	
	public static boolean getRoutingParamSequenced() {
        return (new Boolean(get(PROP_ROUTINGPARAM_SEQUENCED))).booleanValue();
    }
	
	public static boolean getRoutingParamSingleRoute() {
        return (new Boolean(get(PROP_ROUTINGPARAM_SINGLEROUTE))).booleanValue();
    }
	
	public static boolean getRoutingParamMovable() {
        return (new Boolean(get(PROP_ROUTINGPARAM_MOVABLE))).booleanValue();
    }
	
	public static boolean getRoutingParamRetrieveActivities() {
        return (new Boolean(get(PROP_ROUTINGPARAM_RETRIEVEACTIVITIES))).booleanValue();
    }
	
	public static boolean getRoutingParamRetrieveEquipment() {
        return (new Boolean(get(PROP_ROUTINGPARAM_RETRIEVEEQUIPMENT))).booleanValue();
    }
	
	public static boolean getRoutingParamRetrieveActive() {
        return (new Boolean(get(PROP_ROUTINGPARAM_RETRIEVEACTIVE))).booleanValue();
    }
	
	public static boolean getRoutingParamRetrieveBuilt() {
        return (new Boolean(get(PROP_ROUTINGPARAM_RETRIEVEBUILT))).booleanValue();
    }
	
	public static boolean getRoutingParamRetrievePublished() {
        return (new Boolean(get(PROP_ROUTINGPARAM_RETRIEVEPUBLISHED))).booleanValue();
    }
	
	public static String getRoutingLateDeliveryQuery() {
        return get(PROP_LATEDELIVERY_QUERY);
    }
	
	public static int getDefaultOrderEstimationRange() {
		return getIntVal(get(PROP_ORDER_ESTIMATIONRANGE));
	}
	
	public static String getDefaultNotificationReceiver() {
		return get(PROP_DEFAULT_NOTIFICATIONRECEIVER);
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
	
	public static int getServiceTimeout() {
		return getIntVal(get(PROP_SERVICE_TIMEOUT));
	}

	public static int getBatchServiceTimeout() {
		return getIntVal(get(PROP_BATCH_SERVICE_TIMEOUT));
	}
	
	public static int getOMUseOriginalThreshold() {
		return getIntVal(get(PROP_ORDERMETRICSUSEACTUAL_THRESHOLD));
	}
	
	public static boolean getRoutingCutOffStandByEnabled() {
        return (new Boolean(get(PROP_CUTOFFSIMSTANDBY_ENABLED))).booleanValue();
    }
	
	public static boolean getHandOffDispatchCorrelationEnabled() {
		return (new Boolean(get(PROP_HANDOFF_DISPATCHCORRELATIONENABLED))).booleanValue();
	}
	
	public static boolean getRoutingDynaSyncEnabled() {
		return (new Boolean(get(PROP_ROUTING_DYNASYNC_ENABLED))).booleanValue();
	}
	
	public static boolean getRoutingBatchSyncEnabled() {
		return (new Boolean(get(PROP_ROUTING_BATCHSYNC_ENABLED))).booleanValue();
	}
	
	public static int getRoadnetSessionSizeThreshold() {
		return getIntVal(get(PROP_ROADNETSESSIONSIZE_THRESHOLD));
	}
	
	public static int getJDBCBatchUpdateThreshold() {
		return getIntVal(get(PROP_JDBCBATCHUPDATE_THRESHOLD));
	}
	
	public static String getDefaultPlantCode() {
		return get(PROP_DEFAULT_PLANTCODE);
	}
	
	public static String getHandOffMailFrom() {
		return get(PROP_HANDOFF_MAILFROM);
	}
	
	public static String getHandOffMailTo() {
		return get(PROP_HANDOFF_MAILTO);
	}
	
	public static String getHandOffMailCC() {
		return get(PROP_HANDOFF_MAILCC);
	}
	
	public static String getHandOffMailSubject() {
		return get(PROP_HANDOFF_MAILSUBJECT);
	}
	
	public static int getDefaultOrderSize() {
		return getIntVal(get(PROP_DEFAULT_ORDERSIZE));
	}
	
	public static int getDefaultServiceTime() {
		return getIntVal(get(PROP_DEFAULT_SERVICETIME));
	}
	
}
