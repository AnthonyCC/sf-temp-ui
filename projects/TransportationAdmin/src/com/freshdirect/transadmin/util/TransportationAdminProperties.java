/*
 * ErpServiceProperties.java
 *
 * Created on December 12, 2001, 6:34 PM
 */

package com.freshdirect.transadmin.util;


import java.util.Properties;

import com.freshdirect.framework.util.ConfigHelper;

public class TransportationAdminProperties {
	
	private static Properties config;

	private final static Properties defaults = new Properties();
		
	private final static String PROP_TRANSPORTATION_FORMAT_ORDERLOCATION_SAPOUT		= "transportation.format.orderlocationsapout";
	
	private final static String PROP_TRANSPORTATION_FORMAT_ORDER_ROUTINGIN		= "transportation.format.orderroutingin";
	
	private final static String PROP_TRANSPORTATION_FORMAT_LOCATION_ROUTINGIN		= "transportation.format.locationroutingin";
	
	private final static String PROP_TRANSPORTATION_FORMAT_ORDERROUTE_ROUTINGOUT		= "transportation.format.orderrouteroutingout";
	
	private final static String PROP_TRANSPORTATION_FORMAT_ORDER_SAPIN		= "transportation.format.ordersapin";
	
	private final static String PROP_TRANSPORTATION_FORMAT_ROUTE_SAPIN		= "transportation.format.routesapin";
	
		
	private final static String PROP_TRANSPORTATION_FILENAME_ORDEROUT		= "transportation.filename.orderout";
	
	private final static String PROP_TRANSPORTATION_FILENAME_LOCATIONOUT		= "transportation.filename.locationout";
	
	private final static String PROP_TRANSPORTATION_FILENAME_ROUTINGOUTORDER		= "transportation.filename.routingoutorder";
	
	private final static String PROP_TRANSPORTATION_FILENAME_ROUTINGOUTTRUCK		= "transportation.filename.routingouttruck";
	
	
	private final static String PROP_TRANSPORTATION_FILENAME_EXTGEOINLOCDB		= "transportation.filename.extgeolocdb";
		
	
	private final static String PROP_TRANSPORTATION_FORMAT_ERROR		= "transportation.format.error";
	
	private final static String PROP_TRANSPORTATION_FILENAME_ERROR		= "transportation.filename.error";
	
	private final static String PROP_TRANSPORTATION_ERRORFILENAME_SUFFIX		= "transportation.errorfilename.suffix";
	
	private final static String PROP_TRANSPORTATION_FILENAME_SUFFIX		= "transportation.filename.suffix";
	
	private final static String PROP_TRANSPORTATION_LOCATIONNAME_PREFIX		= "transportation.locationame.prefix";
	
	private final static String PROP_TRANSPORTATION_EQUIPMENTTYPE_PREFIX		= "transportation.equipmenttype.prefix";
	
	private final static String PROP_TRANSPORTATION_DEFAULT_COUNTRY		= "transportation.default.country";
	
	private final static String PROP_TRANSPORTATION_DEFAULT_MAPKEY		= "transportation.default.mapkey";
	
	private final static String PROP_TRANSPORTATION_FILE_SEPARATOR		= "transportation.default.fileseparator";
	
	private final static String PROP_TRANSPORTATION_DOWNLOAD_PROVIDERURL		= "transportation.download.providerURL";
	
	private final static String PROP_TRANSPORTATION_DOWNLOAD_FOLDER		= "transportation.download.folder";	
	
	
	/* Bullpen*/
	private final static String PROP_TRANSPORTATION_BULLPEN_DRIVER_REQ="transportation.bullpen.driver.req";
	private final static String PROP_TRANSPORTATION_BULLPEN_DRIVER_MAX="transportation.bullpen.driver.max";
	private final static String PROP_TRANSPORTATION_BULLPEN_HELPER_REQ="transportation.bullpen.helper.req";
	private final static String PROP_TRANSPORTATION_BULLPEN_HELPER_MAX="transportation.bullpen.helper.max";
	private final static String PROP_TRANSPORTATION_BULLPEN_RUNNER_REQ="transportation.bullpen.runner.req";
	private final static String PROP_TRANSPORTATION_BULLPEN_RUNNER_MAX="transportation.bullpen.runner.max";
	
	/* Zonetype*/
	private final static String PROP_TRANSPORTATION_ZONETYPE_DRIVER_REQ="transportation.zonetype.driver.req";
	private final static String PROP_TRANSPORTATION_ZONETYPE_DRIVER_MAX="transportation.zonetype.driver.max";
	private final static String PROP_TRANSPORTATION_ZONETYPE_HELPER_REQ="transportation.zonetype.helper.req";
	private final static String PROP_TRANSPORTATION_ZONETYPE_HELPER_MAX="transportation.zonetype.helper.max";
	private final static String PROP_TRANSPORTATION_ZONETYPE_RUNNER_REQ="transportation.zonetype.runner.req";
	private final static String PROP_TRANSPORTATION_ZONETYPE_RUNNER_MAX="transportation.zonetype.runner.max";
	
	private static long lastRefresh = 0;
	private final static long REFRESH_PERIOD = 5 * 60 * 1000;
	
	static {
				
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ORDERLOCATION_SAPOUT, 	"com/freshdirect/transadmin/datamanager/orderlocation_out_erp.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ORDER_ROUTINGIN, 	"com/freshdirect/transadmin/datamanager/order_in_routing.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_LOCATION_ROUTINGIN, 	"com/freshdirect/transadmin/datamanager/location_in_routing.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ORDERROUTE_ROUTINGOUT, 	"com/freshdirect/transadmin/datamanager/orderroute_out_routing.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ORDER_SAPIN, 	"com/freshdirect/transadmin/datamanager/order_in_erp.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ROUTE_SAPIN, 	"com/freshdirect/transadmin/datamanager/route_in_erp.xml");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_ORDEROUT,"trn_order_");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_LOCATIONOUT,"trn_location_");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_ROUTINGOUTORDER,"trn_orderassignment_");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_ROUTINGOUTTRUCK,"trn_truck_");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_SUFFIX,"prn");
		
		defaults.put(PROP_TRANSPORTATION_LOCATIONNAME_PREFIX,"LOC_");
		
		defaults.put(PROP_TRANSPORTATION_EQUIPMENTTYPE_PREFIX,"FDT_");
		
		defaults.put(PROP_TRANSPORTATION_DEFAULT_COUNTRY,"US");
		
		defaults.put(PROP_TRANSPORTATION_DEFAULT_MAPKEY,"ABQIAAAAfVoMocpSDwijA4jg7oArwxRSY3LvYPilQO9bLyn0b52peXkumBS12_vtsrd6kaDHSJwpbH-EdOSnOA");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_ERROR,"trn_error_");
		
		defaults.put(PROP_TRANSPORTATION_ERRORFILENAME_SUFFIX,"csv");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ERROR,"com/freshdirect/transadmin/datamanager/routingerrormapping.xml");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_EXTGEOINLOCDB,"com/freshdirect/transadmin/datamanager/externalgeocodes_in_locationdb.xml");
		
		defaults.put(PROP_TRANSPORTATION_FILE_SEPARATOR,"\r\n");
		
		defaults.put(PROP_TRANSPORTATION_DOWNLOAD_PROVIDERURL,"http://crm:7001/TrnAdmin/download.do");
		
		defaults.put(PROP_TRANSPORTATION_DOWNLOAD_FOLDER,"");
		
		defaults.put(PROP_TRANSPORTATION_BULLPEN_DRIVER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_DRIVER_MAX, "5");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_HELPER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_HELPER_MAX, "5");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_RUNNER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_RUNNER_MAX, "5");
		
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_DRIVER_REQ, "1");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_DRIVER_MAX, "10");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_HELPER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_HELPER_MAX, "10");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_RUNNER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_RUNNER_MAX, "10");		
		
		refresh();		
	}

	/** Creates new ErpServiceProperties */
    public TransportationAdminProperties() {
    }
    
    private static void refresh() {
		refresh(false);
	}
	
    public static String getDownloadFolder() {
    	return get(PROP_TRANSPORTATION_DOWNLOAD_FOLDER);    	
    }
    
    public static String getDownloadProviderUrl() {
    	return get(PROP_TRANSPORTATION_DOWNLOAD_PROVIDERURL);
    }
    public static String getFileSeparator() {
    	return get(PROP_TRANSPORTATION_FILE_SEPARATOR);
    }
    
    public static String getExtGeoLocationDBInputFormat() {
    	return get(PROP_TRANSPORTATION_FILENAME_EXTGEOINLOCDB);
    }
    
    public static String getErpOrderLocationOutputFormat() {
    	return get(PROP_TRANSPORTATION_FORMAT_ORDERLOCATION_SAPOUT);
    }
    
    public static String getRoutingOrderInputFormat() {
    	return get(PROP_TRANSPORTATION_FORMAT_ORDER_ROUTINGIN);
    }
    
    public static String getRoutingLocationInputFormat() {
    	return get(PROP_TRANSPORTATION_FORMAT_LOCATION_ROUTINGIN);
    }
    
    public static String getRoutingOrderRouteOutputFormat() {
    	return get(PROP_TRANSPORTATION_FORMAT_ORDERROUTE_ROUTINGOUT);
    }
    
    public static String getErpOrderInputFormat() {
    	return get(PROP_TRANSPORTATION_FORMAT_ORDER_SAPIN);
    }
    
    public static String getErpRouteInputFormat() {
    	return get(PROP_TRANSPORTATION_FORMAT_ROUTE_SAPIN);
    }
	   
	
    public static String getOrderOutputFilename() {
    	return get(PROP_TRANSPORTATION_FILENAME_ORDEROUT);
    }
	
    public static String getLocationOutputFilename() {
    	return get(PROP_TRANSPORTATION_FILENAME_LOCATIONOUT);
    }
	
    public static String getRoutingOutputOrderFilename() {
    	return get(PROP_TRANSPORTATION_FILENAME_ROUTINGOUTORDER);
    }
	
    public static String getRoutingOutputTruckFilename() {
    	return get(PROP_TRANSPORTATION_FILENAME_ROUTINGOUTTRUCK);
    }
	
    public static String getFilenameSuffix() {
    	return get(PROP_TRANSPORTATION_FILENAME_SUFFIX);
    }
    
    public static String getRoutingErrorFilename() {
    	return get(PROP_TRANSPORTATION_FILENAME_ERROR);
    }
	
    public static String getErrorFilenameSuffix() {
    	return get(PROP_TRANSPORTATION_ERRORFILENAME_SUFFIX);
    }
	
    public static String getLocationNamePrefix() {
    	return get(PROP_TRANSPORTATION_LOCATIONNAME_PREFIX);
    }
	
    public static String getEquipmentTypePrefix() {
    	return get(PROP_TRANSPORTATION_EQUIPMENTTYPE_PREFIX);
    }
	
    public static String getDefaultCountry() {
    	return get(PROP_TRANSPORTATION_DEFAULT_COUNTRY);
    }
    
    public static String getDefaultMapKey() {
    	return get(PROP_TRANSPORTATION_DEFAULT_MAPKEY);
    }
	
	private synchronized static void refresh(boolean force) {
		long t = System.currentTimeMillis();
		if (force || (t - lastRefresh > REFRESH_PERIOD)) {
			config = ConfigHelper.getPropertiesFromClassLoader("transportation.properties", defaults);
			lastRefresh = t;			
		}
	}

	 public static String getErrorFormat() {
	   	return get(PROP_TRANSPORTATION_FORMAT_ERROR);
	 }
	 
	public static String get(String key) {
		refresh();
		return config.getProperty(key);
	}
		
	public static void set(String key, String value) {
		config.setProperty(key, value);
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

	public static int getDriverReqForBullpen() {
		return getIntVal(get(PROP_TRANSPORTATION_BULLPEN_DRIVER_REQ));
	}
	public static int getDriverMaxForBullpen() {
		return getIntVal(get(PROP_TRANSPORTATION_BULLPEN_DRIVER_MAX));
	}
	public static int getHelperReqForBullpen() {
		return getIntVal(get(PROP_TRANSPORTATION_BULLPEN_HELPER_REQ));
	}
	public static int getHelperMaxForBullpen() {
		return getIntVal(get(PROP_TRANSPORTATION_BULLPEN_HELPER_MAX));
	}
	public static int getRunnerReqForBullpen() {
		return getIntVal(get(PROP_TRANSPORTATION_BULLPEN_RUNNER_REQ));
	}
	public static int getRunnerMaxForBullpen() {
		return getIntVal(get(PROP_TRANSPORTATION_BULLPEN_RUNNER_MAX));
	}
	public static int getDriverReqForZonetype() {
		return getIntVal(get(PROP_TRANSPORTATION_ZONETYPE_DRIVER_REQ));
	}
	public static int getDriverMaxForZonetype() {
		return getIntVal(get(PROP_TRANSPORTATION_ZONETYPE_DRIVER_MAX));
	}
	public static int getHelperReqForZonetype() {
		return getIntVal(get(PROP_TRANSPORTATION_ZONETYPE_HELPER_REQ));
	}
	public static int getHelperMaxForZonetype() {
		return getIntVal(get(PROP_TRANSPORTATION_ZONETYPE_HELPER_MAX));
	}
	public static int getRunnerReqForZonetype() {
		return getIntVal(get(PROP_TRANSPORTATION_ZONETYPE_RUNNER_REQ));
	}
	public static int getRunnerMaxForZonetype() {
		return getIntVal(get(PROP_TRANSPORTATION_ZONETYPE_RUNNER_MAX));
	}
	
	
}
