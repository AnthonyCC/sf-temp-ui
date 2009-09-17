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
	
	private final static String PROP_TRANSPORTATION_FORMAT_TRUCKSCHEDULE_ROUTINGIN		= "transportation.format.truckscheduleroutingin";
	
		
	private final static String PROP_TRANSPORTATION_FILENAME_ORDEROUT		= "transportation.filename.orderout";
	
	private final static String PROP_TRANSPORTATION_FILENAME_LOCATIONOUT		= "transportation.filename.locationout";
	
	private final static String PROP_TRANSPORTATION_FILENAME_ROUTINGOUTORDER		= "transportation.filename.routingoutorder";
	
	private final static String PROP_TRANSPORTATION_FILENAME_ROUTINGOUTTRUCK		= "transportation.filename.routingouttruck";
	
	private final static String PROP_TRANSPORTATION_FILENAME_CUTOFFREPORT		= "transportation.filename.cutoffreport";
	
	private final static String PROP_TRANSPORTATION_FILENAME_DRIVINGDIRECTION		= "transportation.filename.drivingdirection";
	
	
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
	
	// remember it is in minutes
	private static final String PROP_TRANSPORTATION_TRK_CACHE_EXPIRY_TIME="transportation.truck.cache.expiry.time";
	private static final String PROP_TRANSPORTATION_ROUTE_CACHE_EXPIRY_TIME="transportation.route.cache.expiry.time";
	private static final String PROP_TRANSPORTATION_EMPLOYEE_CACHE_EXPIRY_TIME="transportation.employee.cache.expiry.time";
	
	private static long lastRefresh = 0;
	private final static long REFRESH_PERIOD = 5 * 60 * 1000;
	
	private final static long PROP_TRANSPORTATION_DISPATCH_DASHBOARD_DISPATCH_PERIOD = 60 * 60 * 1000;
	private final static long PROP_TRANSPORTATION_DISPATCH_DASHBOARD_DISPATCHED_PERIOD = 10 * 60 * 1000;	
	
	private final static String PROP_TRANSPORTATION_CELLDATA_SEPERATOR		= "transportation.celldata.seperator";
	
	private final static String PROP_TRANSPORTATION_DEPOT_DEPARTTIMEDIFF		= "transportation.depot.departtimediff";
	
	private final static String PROP_TRANSPORTATION_DEPOT_USESTPARRTIME		= "transportation.depot.usestoparrtime";
	
	private final static String PROP_TRANSPORTATION_SAPORDERFILE_ENCODING		= "transportation.saporderfile.encoding";
	
	private final static String PROP_TRANSPORTATION_DISPATCH_REFRESHTIME		= "transportation.dispatch.refreshtime";
	
	private final static String PROP_TRANSPORTATION_DISPATCH_PAGEREFRESHTIME		= "transportation.dispatch.pagerefreshtime";
	
	private final static String PROP_TRANSPORTATION_DISPATCH_PAGESIZE		= "transportation.dispatch.pagesize";
	private final static String PROP_TRANSPORTATION_DISPATCH_MAX_READY		= "transportation.dispatch.maxready";
	
	private final static String PROP_KRONOS_BLACKHOLE		= "kronos.blackhole";
	
	private final static String PROP_AIRCLIC_BLACKHOLE		= "airclic.blackhole";
	
	private final static String PROP_TRANSPORTATION_FILENAME_CUMMUNITYRPT		= "transportation.filename.communityrpt";
	
	private final static String KRONOS_UPLOAD_ALL="transportation.filename.kronosuploadall";
	private final static String KRONOS_UPLOAD_ALL_EMPTY="transportation.filename.kronosuploadallempty";
	
	private final static String PROP_TRANSPORTATION_AUTODISPATCH_VALIDATION		= "transportation.autodispatch.validation";	
	private final static String PROP_TRANSPORTATION_PLAN_VALIDATION		= "transportation.plan.validation";
	private final static String PROP_TRANSPORTATION_DISPATCH_VALIDATION		= "transportation.dispatch.validation";
	
	private final static String PROP_TRANSPORTATION_EARLYWARNING_PAGEREFRESHTIME = "transportation.earlywarning.pagerefreshtime";
	
	static {
				
	
		defaults.put(PROP_TRANSPORTATION_FORMAT_ORDERLOCATION_SAPOUT, 	"com/freshdirect/transadmin/datamanager/orderlocation_out_erp.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ORDER_ROUTINGIN, 	"com/freshdirect/transadmin/datamanager/order_in_routing.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_LOCATION_ROUTINGIN, 	"com/freshdirect/transadmin/datamanager/location_in_routing.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ORDERROUTE_ROUTINGOUT, 	"com/freshdirect/transadmin/datamanager/orderroute_out_routing.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ORDER_SAPIN, 	"com/freshdirect/transadmin/datamanager/order_in_erp.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_ROUTE_SAPIN, 	"com/freshdirect/transadmin/datamanager/route_in_erp.xml");
		
		defaults.put(PROP_TRANSPORTATION_FORMAT_TRUCKSCHEDULE_ROUTINGIN, 	"com/freshdirect/transadmin/datamanager/truckschedule_in_routing.xml");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_ORDEROUT,"trn_order_");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_LOCATIONOUT,"trn_location_");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_ROUTINGOUTORDER,"trn_orderassignment_");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_ROUTINGOUTTRUCK,"trn_truck_");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_CUTOFFREPORT,"trn_cutoffreport_");
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_DRIVINGDIRECTION,"trn_drivingdirection_");
		
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
		
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_DRIVER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_DRIVER_MAX, "10");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_HELPER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_HELPER_MAX, "10");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_RUNNER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_RUNNER_MAX, "10");
		defaults.put(PROP_TRANSPORTATION_CELLDATA_SEPERATOR, "\n\r");
		
		defaults.put(PROP_TRANSPORTATION_TRK_CACHE_EXPIRY_TIME, "60");
		defaults.put(PROP_TRANSPORTATION_ROUTE_CACHE_EXPIRY_TIME, "60");
		defaults.put(PROP_TRANSPORTATION_EMPLOYEE_CACHE_EXPIRY_TIME, "60");
		defaults.put(PROP_TRANSPORTATION_DEPOT_DEPARTTIMEDIFF, "0");
		
		defaults.put(PROP_TRANSPORTATION_DEPOT_USESTPARRTIME, "true");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_REFRESHTIME, "45");
		
		defaults.put(PROP_TRANSPORTATION_DISPATCH_PAGEREFRESHTIME, "10");
		
		defaults.put(PROP_TRANSPORTATION_EARLYWARNING_PAGEREFRESHTIME, "60000");
		
		defaults.put(PROP_TRANSPORTATION_DISPATCH_PAGESIZE, "16");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_MAX_READY, "8");
		
		defaults.put(PROP_KRONOS_BLACKHOLE, "false");
		defaults.put(PROP_AIRCLIC_BLACKHOLE, "false");
		defaults.put(PROP_TRANSPORTATION_FILENAME_CUMMUNITYRPT, "trn_communityreport_");
		defaults.put(KRONOS_UPLOAD_ALL, "Upload_All.csv");
		defaults.put(KRONOS_UPLOAD_ALL_EMPTY, "Upload_All_Empty.csv");
		defaults.put(PROP_TRANSPORTATION_AUTODISPATCH_VALIDATION, "false");
		defaults.put(PROP_TRANSPORTATION_PLAN_VALIDATION, "false");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_VALIDATION, "false");
		
		refresh();		
	}

	/** Creates new ErpServiceProperties */
    public TransportationAdminProperties() {
    }
    
    private static void refresh() {
		refresh(false);
	}
	
    public static String getSapOrderFileEncoding() {
    	return get(PROP_TRANSPORTATION_SAPORDERFILE_ENCODING);    	
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
    
    public static String getRoutingTruckScheduleFormat() {
    	return get(PROP_TRANSPORTATION_FORMAT_TRUCKSCHEDULE_ROUTINGIN);
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
    
    public static String getRoutingCutOffRptFilename() {
    	return get(PROP_TRANSPORTATION_FILENAME_CUTOFFREPORT);
    }
    
    public static String getRoutingDrvDirectionRptFilename() {
    	return get(PROP_TRANSPORTATION_FILENAME_DRIVINGDIRECTION);
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
	
	public static int getRouteCacheExpiryTime() {
		return getIntVal(get(PROP_TRANSPORTATION_ROUTE_CACHE_EXPIRY_TIME));
	}
	
	public static int getTruckCacheExpiryTime() {
		return getIntVal(get(PROP_TRANSPORTATION_TRK_CACHE_EXPIRY_TIME));
	}

	public static int getEmployeeCacheExpiryTime() {
		return getIntVal(get(PROP_TRANSPORTATION_EMPLOYEE_CACHE_EXPIRY_TIME));
	}

	public static String getCellDataSeperator() {
		return get(PROP_TRANSPORTATION_CELLDATA_SEPERATOR);
	}
	
	public static int getDepotDepartTimeDiff() {
		return getIntVal(get(PROP_TRANSPORTATION_DEPOT_DEPARTTIMEDIFF));
	}
	
	public static boolean useStopArrivalTime() {
        return (new Boolean(get(PROP_TRANSPORTATION_DEPOT_USESTPARRTIME))).booleanValue();
    }
	
	public static long getDispatchPeriod() {
		return PROP_TRANSPORTATION_DISPATCH_DASHBOARD_DISPATCH_PERIOD;
	}
	public static long getDispatchedPeriod() {
		return PROP_TRANSPORTATION_DISPATCH_DASHBOARD_DISPATCHED_PERIOD;
	}
	
	public static int getDispatchDashboardRefreshTime() 
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_REFRESHTIME));
	}
	
	public static int getCapacityRefreshTime() 	{
		return getIntVal(get(PROP_TRANSPORTATION_EARLYWARNING_PAGEREFRESHTIME));		
	}
	
	public static int getDispatchDashboardPageRefreshTime() 
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_PAGEREFRESHTIME));
	}
	
	public static int getDispatchDashboardPageSize() 
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_PAGESIZE));
	}
	public static int getMaxReady() 
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_MAX_READY));
	}
	
	public static boolean isKronosBlackhole() {
        return (new Boolean(get(PROP_KRONOS_BLACKHOLE))).booleanValue();		
    }
	
	public static boolean isAirclicBlackhole() {
        return (new Boolean(get(PROP_AIRCLIC_BLACKHOLE))).booleanValue();
    }
	
	public static String getCommunityRptFileName() {
		return get(PROP_TRANSPORTATION_FILENAME_CUMMUNITYRPT);
	}
	public static String getKronosUploadAllFileName() {
		return get(KRONOS_UPLOAD_ALL);
	}
	public static String getKronosUploadAllEmptyFileName() {
		return get(KRONOS_UPLOAD_ALL_EMPTY);
	}
	
	public static boolean isPlanValidation() {
        return (new Boolean(get(PROP_TRANSPORTATION_PLAN_VALIDATION))).booleanValue();
    }
	public static boolean isAutoDispatchValidation() {
        return (new Boolean(get(PROP_TRANSPORTATION_AUTODISPATCH_VALIDATION))).booleanValue();
    }
	public static boolean isDispatchValidation() {
        return (new Boolean(get(PROP_TRANSPORTATION_DISPATCH_VALIDATION))).booleanValue();
    }
}
