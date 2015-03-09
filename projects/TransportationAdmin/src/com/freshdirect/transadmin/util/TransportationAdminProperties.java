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
	
	private final static String PROP_TRANSPORTATION_FILENAME_ROUTINGOUTORDERTRUCK		= "transportation.filename.routingoutordertruck";

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

	/* Trailer resource req */
	private final static String PROP_TRANSPORTATION_TRAILER_DRIVER_REQ = "transportation.trailer.driver.req";
	private final static String PROP_TRANSPORTATION_TRAILER_DRIVER_MAX = "transportation.trailer.driver.max";
	private final static String PROP_TRANSPORTATION_TRAILER_HELPER_REQ = "transportation.trailer.helper.req";
	private final static String PROP_TRANSPORTATION_TRAILER_HELPER_MAX = "transportation.trailer.helper.max";
	private final static String PROP_TRANSPORTATION_TRAILER_RUNNER_REQ = "transportation.trailer.runner.req";
	private final static String PROP_TRANSPORTATION_TRAILER_RUNNER_MAX = "transportation.trailer.runner.max";

	/* Shuttle resource req */
	private final static String PROP_TRANSPORTATION_SHUTTLE_DRIVER_REQ = "transportation.shuttle.driver.req";
	private final static String PROP_TRANSPORTATION_SHUTTLE_DRIVER_MAX = "transportation.shuttle.driver.max";
	private final static String PROP_TRANSPORTATION_SHUTTLE_HELPER_REQ = "transportation.shuttle.helper.req";
	private final static String PROP_TRANSPORTATION_SHUTTLE_HELPER_MAX = "transportation.shuttle.helper.max";
	private final static String PROP_TRANSPORTATION_SHUTTLE_RUNNER_REQ = "transportation.shuttle.runner.req";
	private final static String PROP_TRANSPORTATION_SHUTTLE_RUNNER_MAX = "transportation.shuttle.runner.max";

	/* handtruck resource req */
	private final static String PROP_TRANSPORTATION_HANDTRUCK_DRIVER_REQ = "transportation.handtruck.driver.req";
	private final static String PROP_TRANSPORTATION_HANDTRUCK_DRIVER_MAX = "transportation.handtruck.driver.max";
	private final static String PROP_TRANSPORTATION_HANDTRUCK_HELPER_REQ = "transportation.handtruck.helper.req";
	private final static String PROP_TRANSPORTATION_HANDTRUCK_HELPER_MAX = "transportation.handtruck.helper.max";
	private final static String PROP_TRANSPORTATION_HANDTRUCK_RUNNER_REQ = "transportation.handtruck.runner.req";
	private final static String PROP_TRANSPORTATION_HANDTRUCK_RUNNER_MAX = "transportation.handtruck.runner.max";

	
	// remember it is in minutes
	private static final String PROP_TRANSPORTATION_TRK_CACHE_EXPIRY_TIME="transportation.truck.cache.expiry.time";
	private static final String PROP_TRANSPORTATION_ROUTE_CACHE_EXPIRY_TIME="transportation.route.cache.expiry.time";
	private static final String PROP_TRANSPORTATION_EMPLOYEE_CACHE_EXPIRY_TIME="transportation.employee.cache.expiry.time";
	
	//minimum thresholds
	private static final String PROP_TRANSPORTATION_EMPLOYEE_CACHE_MIN_EXPIRY_TIME="transportation.employee.cache.min.expiry.time";
	private static final String PROP_TRANSPORTATION_TRK_CACHE_MIN_EXPIRY_TIME="transportation.employee.cache.min.expiry.time";
	private static final String PROP_TRANSPORTATION_ROUTE_CACHE_MIN_EXPIRY_TIME="transportation.route.cache.min.expiry.time";
	
	private static final String PROP_TRANSPORTATION_EQT_CACHE_EXPIRY_TIME="transportation.equipmentType.cache.expiry.time";
	
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
	private final static String KRONOS_UPLOAD_INDIVIDUAL_DAY = "transportation.filename.kronosuploadindividualday";

	private final static String PROP_TRANSPORTATION_AUTODISPATCH_VALIDATION		= "transportation.autodispatch.validation";
	private final static String PROP_TRANSPORTATION_PLAN_VALIDATION		= "transportation.plan.validation";
	private final static String PROP_TRANSPORTATION_DISPATCH_VALIDATION		= "transportation.dispatch.validation";

	private final static String PROP_TRANSPORTATION_EARLYWARNING_PAGEREFRESHTIME = "transportation.earlywarning.pagerefreshtime";

	private final static String PROP_TRANSPORTATION_EDITDYNAMICROUTINGFEATURE_ACCESSKEY = "transportation.editdynamicroutingfeature.accesskey";
	
	private final static String PROP_TRANSPORTATION_GENERATEPLAN_ACCESSKEY = "transportation.generateplan.accesskey";

	//Ready View
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_REFRESHTIME		= "transportation.dispatch.readyview.refreshtime";
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_PAGEREFRESHTIME		= "transportation.dispatch.readyview.pagerefreshtime";
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_PAGESIZE		= "transportation.dispatch.readyview.pagesize";
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_MAX_READY		= "transportation.dispatch.readyview.maxready";

	//waiting view
	private final static String PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_REFRESHTIME		= "transportation.dispatch.waitingview.refreshtime";
	private final static String PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_PAGEREFRESHTIME		= "transportation.dispatch.waitingview.pagerefreshtime";
	private final static String PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_PAGESIZE		= "transportation.dispatch.waitingview.pagesize";

	//NR view
	private final static String PROP_TRANSPORTATION_DISPATCH_NR_VIEW_REFRESHTIME		= "transportation.dispatch.nrview.refreshtime";
	private final static String PROP_TRANSPORTATION_DISPATCH_NR_VIEW_PAGEREFRESHTIME		= "transportation.dispatch.nrview.pagerefreshtime";
	private final static String PROP_TRANSPORTATION_DISPATCH_NR_VIEW_PAGESIZE		= "transportation.dispatch.nrview.pagesize";

	//view CSS
	//Ready View
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_HEADER_FONT = "transportation.dispatch.readyview.css.header.font";
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_HEADER_FONT_SIZE = "transportation.dispatch.readyview.css.header.font.size";
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_PAGE_HEADER_FONT		= "transportation.dispatch.readyview.css.page.header.font";
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_PAGE_HEADER_FONT_SIZE		= "transportation.dispatch.readyview.css.page.header.font.size";
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_PAGE_FONT		= "transportation.dispatch.readyview.css.page.font";
	private final static String PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_PAGE_FONT_SIZE		= "transportation.dispatch.readyview.css.page.font.size";

	//waiting View
	private final static String PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_HEADER_FONT = "transportation.dispatch.waiting.css.header.font";
	private final static String PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_HEADER_FONT_SIZE = "transportation.dispatch.waiting.css.header.font.size";
	private final static String PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_PAGE_HEADER_FONT		= "transportation.dispatch.waiting.css.page.header.font";
	private final static String PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_PAGE_HEADER_FONT_SIZE		= "transportation.dispatch.waiting.css.page.header.font.size";
	private final static String PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_PAGE_FONT		= "transportation.dispatch.waiting.css.page.font";
	private final static String PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_PAGE_FONT_SIZE		= "transportation.dispatch.waiting.css.page.font.size";

	//NR View
	private final static String PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_HEADER_FONT = "transportation.dispatch.nr.css.header.font";
	private final static String PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_HEADER_FONT_SIZE = "transportation.dispatch.nr.css.header.font.size";
	private final static String PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_PAGE_HEADER_FONT		= "transportation.dispatch.nr.css.page.header.font";
	private final static String PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_PAGE_HEADER_FONT_SIZE		= "transportation.dispatch.nr.css.page.header.font.size";
	private final static String PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_PAGE_FONT		= "transportation.dispatch.nr.css.page.font";
	private final static String PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_PAGE_FONT_SIZE		= "transportation.dispatch.nr.css.page.font.size";

	private final static String PROP_TRANSPORTATION_DEFAULT_GPSDOMAIN		= "transportation.default.gpsdomain";
	private final static String PROP_TRANSPORTATION_DEFAULT_GPSPLUGINKEY		= "transportation.default.gpspluginkey";
	private final static String PROP_TRANSPORTATION_GPSUPLOAD_FILENAME		= "transportation.gpsupload.filename";

	//Zone Expansion
	private final static String PROP_ZONE_EXPANSION_WORKTABLES		= "transportation.zoneexpansion.worktables";
	//Holiday Planning
	private final static String PROP_HOLIDAY_PLANNING_LABELS		= "transportation.holidayplanning.lables";
	private final static String PROP_EXPORT_SCHEDULES		= "transportation.filename.exportschedules";
	private final static String PROP_TRANSPORTATION_FORMAT_EMPPLOYEESCHEDULE_UPLOAD	= "transportation.format.employeescheduleout";
	private final static String PROP_TRANSPORTATION_FORMAT_SCRIB_UPLOAD	= "transportation.format.scribout";
	private final static String PROP_TRANSPORTATION_CSVFILE_SEPARATOR	= "transportation.default.csvfileseparator";
	//Maintenance Log
	private final static String PROP_VENDOR_DETAILS		= "transportation.issuelog.vendors";
	private final static String PROP_TRUCK_DAMAGE_LOCATIONS = "transportation.issuelog.truckdamagelocations";
	private final static String PROP_TRUCK_LOCATIONS = "transportation.maintenancelog.trucklocations";
	
	/* Crisis Manager */
	private static final String ADMIN_SERVICE_URL_KEY = "admin.service.url";
    private final static String PROP_TRANSPORTATION_FILENAME_MARKETINGORDERRPT = "transportation.filename.marketingorderrpt";
	private final static String PROP_TRANSPORTATION_FILENAME_VOICESHOTORDERRPT = "transportation.filename.voiceshotorderrpt";
	private final static String PROP_TRANSPORTATION_FILENAME_TIMESLOTEXCEPTIONRPT = "transportation.filename.timeslotexceptionrpt";
	private final static String PROP_TRANSPORTATION_FILENAME_SOSIMULATIONRPT = "transportation.filename.sosimulationrpt";
	private final static String PROP_TRANSPORTATION_FILENAME_SOFAILURERPT = "transportation.filename.sofailurerpt";	
	private final static String PROP_JDBCBATCHUPDATE_THRESHOLD		= "transportation.jdbcbatchupdate.threshold";
	
	private final static String PROP_FDPARKING_HQLOCATION		= "transportation.parking.fdheadquarter";
	private final static String PROP_FDPARKING_ONROAD		= "transportation.parking.fdonroad";
	private final static String PROP_FDTRUCKS_ONROAD_IDENTITY		= "transportation.trucks.onroad.identity";
	private final static String PROP_TRANSPORTATION_YARDMONITOR_PAGEREFRESHTIME = "transportation.yardmonitor.pagerefreshtime";

	private final static String PROP_ENABLE_AIRCLIC = "transportation.enable.airclic";
	private final static String PROP_TELARGO_BLACKHOLE = "transportation.telargo.blackhole";
	
	private static final String PROP_TRANSPORTATION_EMPLOYEE_PUNCHINFO_CACHE_EXPIRY_TIME = "transportation.employee.punchinfo.cache.expiry.time";
	private final static String PROP_TXTMESSAGE_ACCESSKEY	= "routingservices.txtmessage.accesskey";
	
	private final static String PROP_SHIFTEVENTLOG_TYPE	= "transportation.shifteventlog.type";
	private final static String PROP_EVENTLOG_MAILFROM		= "transportation.eventlog.mailfrom";	
	private final static String PROP_EVENTLOG_MAILTO		= "transportation.eventlog.mailto";	
	private final static String PROP_EVENTLOG_MAILCC		= "transportation.eventlog.mailcc";
	private final static String PROP_EVENTLOG_MAILSUBJECT = "transportation.eventlog.mailsubject";
	private final static String PROP_EVENTLOG_DATALOOKUP = "transportation.eventlog.lookup.days";
	private final static String PROP_KRONOS_CLOUD_ENABLE = "transportation.kronoscloud.enable";
	private final static String PROP_MUNI_METER_CARD_MAX_VALUE="munimeter.max.value";
	
	//gmap version
	private final static String PROP_GMAP_VERSION = "transportation.gmap.version";
	
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
		
		defaults.put(PROP_TRANSPORTATION_FILENAME_ROUTINGOUTORDERTRUCK,"trn_routestops_");

		defaults.put(PROP_TRANSPORTATION_FILENAME_CUTOFFREPORT,"trn_handoffreport_");

		defaults.put(PROP_TRANSPORTATION_FILENAME_DRIVINGDIRECTION,"trn_drivingdirection_");

		defaults.put(PROP_TRANSPORTATION_FILENAME_SUFFIX,"prn");

		defaults.put(PROP_TRANSPORTATION_LOCATIONNAME_PREFIX,"LOC_");

		defaults.put(PROP_TRANSPORTATION_EQUIPMENTTYPE_PREFIX,"FDT_");

		defaults.put(PROP_TRANSPORTATION_DEFAULT_COUNTRY,"US");

		defaults.put(PROP_TRANSPORTATION_DEFAULT_MAPKEY,"AIzaSyAALx7g2uVEDP46IaGU_zxYT5gBSKac2ks");

		defaults.put(PROP_TRANSPORTATION_FILENAME_ERROR,"trn_error_");

		defaults.put(PROP_TRANSPORTATION_ERRORFILENAME_SUFFIX,"csv");

		defaults.put(PROP_TRANSPORTATION_FORMAT_ERROR,"com/freshdirect/transadmin/datamanager/routingerrormapping.xml");

		defaults.put(PROP_TRANSPORTATION_FILENAME_EXTGEOINLOCDB,"com/freshdirect/transadmin/datamanager/externalgeocodes_in_locationdb.xml");

		defaults.put(PROP_TRANSPORTATION_FILE_SEPARATOR,"\r\n");
		
		defaults.put(PROP_TRANSPORTATION_CSVFILE_SEPARATOR,",");
		
		defaults.put(PROP_TRANSPORTATION_DOWNLOAD_PROVIDERURL,"http://crm:7001/TrnAdmin/download.do");

		defaults.put(PROP_TRANSPORTATION_DOWNLOAD_FOLDER,"");

		defaults.put(PROP_TRANSPORTATION_BULLPEN_DRIVER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_DRIVER_MAX, "5");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_HELPER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_HELPER_MAX, "5");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_RUNNER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_BULLPEN_RUNNER_MAX, "1");

		defaults.put(PROP_TRANSPORTATION_TRAILER_DRIVER_REQ, "1");
		defaults.put(PROP_TRANSPORTATION_TRAILER_DRIVER_MAX, "1");
		defaults.put(PROP_TRANSPORTATION_TRAILER_HELPER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_TRAILER_HELPER_MAX, "5");
		defaults.put(PROP_TRANSPORTATION_TRAILER_RUNNER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_TRAILER_RUNNER_MAX, "5");
		
		defaults.put(PROP_TRANSPORTATION_SHUTTLE_DRIVER_REQ, "1");
		defaults.put(PROP_TRANSPORTATION_SHUTTLE_DRIVER_MAX, "1");
		defaults.put(PROP_TRANSPORTATION_SHUTTLE_HELPER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_SHUTTLE_HELPER_MAX, "5");
		defaults.put(PROP_TRANSPORTATION_SHUTTLE_RUNNER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_SHUTTLE_RUNNER_MAX, "5");
		
		defaults.put(PROP_TRANSPORTATION_HANDTRUCK_DRIVER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_HANDTRUCK_DRIVER_MAX, "0");
		defaults.put(PROP_TRANSPORTATION_HANDTRUCK_HELPER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_HANDTRUCK_HELPER_MAX, "0");
		defaults.put(PROP_TRANSPORTATION_HANDTRUCK_RUNNER_REQ, "1");
		defaults.put(PROP_TRANSPORTATION_HANDTRUCK_RUNNER_MAX, "6");
	
		
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_DRIVER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_DRIVER_MAX, "10");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_HELPER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_HELPER_MAX, "10");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_RUNNER_REQ, "0");
		defaults.put(PROP_TRANSPORTATION_ZONETYPE_RUNNER_MAX, "10");
		defaults.put(PROP_TRANSPORTATION_CELLDATA_SEPERATOR, "\n\r");

		defaults.put(PROP_TRANSPORTATION_TRK_CACHE_EXPIRY_TIME, "60");
		defaults.put(PROP_TRANSPORTATION_ROUTE_CACHE_EXPIRY_TIME, "5");
		defaults.put(PROP_TRANSPORTATION_EMPLOYEE_CACHE_EXPIRY_TIME, "60");
		
		defaults.put(PROP_TRANSPORTATION_EMPLOYEE_PUNCHINFO_CACHE_EXPIRY_TIME, "5");
		
		defaults.put(PROP_TRANSPORTATION_TRK_CACHE_MIN_EXPIRY_TIME, "60000");
		defaults.put(PROP_TRANSPORTATION_EMPLOYEE_CACHE_MIN_EXPIRY_TIME, "60000");
		defaults.put(PROP_TRANSPORTATION_ROUTE_CACHE_MIN_EXPIRY_TIME, "60000");
		defaults.put(PROP_TRANSPORTATION_EQT_CACHE_EXPIRY_TIME, "60");
		
		
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
		defaults.put(KRONOS_UPLOAD_INDIVIDUAL_DAY, "Upload_Individual_days.xls");	
		defaults.put(PROP_TRANSPORTATION_AUTODISPATCH_VALIDATION, "false");
		defaults.put(PROP_TRANSPORTATION_PLAN_VALIDATION, "false");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_VALIDATION, "false");

		defaults.put(PROP_TRANSPORTATION_EDITDYNAMICROUTINGFEATURE_ACCESSKEY, "2746766f6bfb620d471e9a477fd63804");
		
		defaults.put(PROP_TRANSPORTATION_GENERATEPLAN_ACCESSKEY, "6a9d58f1ae9d7de8e8caab48b1dde402");

		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_REFRESHTIME, "45");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_PAGEREFRESHTIME, "10");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_PAGESIZE, "10");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_MAX_READY, "10");

		defaults.put(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_REFRESHTIME, "45");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_PAGEREFRESHTIME, "10");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_PAGESIZE, "16");

		defaults.put(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_REFRESHTIME, "45");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_PAGEREFRESHTIME, "10");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_PAGESIZE, "16");

		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_HEADER_FONT, "verdana, arial, helvetica, sans-serif");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_HEADER_FONT_SIZE, "20");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_PAGE_HEADER_FONT, "verdana, arial, helvetica, sans-serif");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_PAGE_HEADER_FONT_SIZE, "17");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_PAGE_FONT, "verdana, arial, helvetica, sans-serif");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_CSS_PAGE_FONT_SIZE, "15");

		defaults.put(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_HEADER_FONT, "verdana, arial, helvetica, sans-serif");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_HEADER_FONT_SIZE, "20");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_PAGE_HEADER_FONT, "verdana, arial, helvetica, sans-serif");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_PAGE_HEADER_FONT_SIZE, "17");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_PAGE_FONT, "verdana, arial, helvetica, sans-serif");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_CSS_PAGE_FONT_SIZE, "15");

		defaults.put(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_HEADER_FONT, "verdana, arial, helvetica, sans-serif");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_HEADER_FONT_SIZE, "20");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_PAGE_HEADER_FONT, "verdana, arial, helvetica, sans-serif");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_PAGE_HEADER_FONT_SIZE, "17");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_PAGE_FONT, "verdana, arial, helvetica, sans-serif");
		defaults.put(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_CSS_PAGE_FONT_SIZE, "15");

		defaults.put(PROP_TRANSPORTATION_DEFAULT_GPSDOMAIN,"http://mydomain.com");
		defaults.put(PROP_TRANSPORTATION_DEFAULT_GPSPLUGINKEY,"pasteYourKeyInHere");
		
		defaults.put(PROP_TRANSPORTATION_GPSUPLOAD_FILENAME,"Current.gpx");
		
		defaults.put(PROP_ZONE_EXPANSION_WORKTABLES,"ZONE_WORKTABLE=146,CT_ZONE_WORKTABLE=200,HAMP_ZONE_WORKTABLE=3268085,NJ_ZONE_WORKTABLE=2152018,COS_ZONE_WORKTABLE=1742307,CTCOS_ZONE_WORKTABLE=250");
		defaults.put(PROP_HOLIDAY_PLANNING_LABELS,"Snow, Heavy Rain, Memorial Day");

		defaults.put(PROP_EXPORT_SCHEDULES,"Export_Schedules_");
		defaults.put(PROP_TRANSPORTATION_FORMAT_EMPPLOYEESCHEDULE_UPLOAD, 	"com/freshdirect/transadmin/datamanager/employeeschedule_in.xml");
		defaults.put(PROP_TRANSPORTATION_FORMAT_SCRIB_UPLOAD, 	"com/freshdirect/transadmin/datamanager/scrib_in.xml");
		defaults.put(PROP_VENDOR_DETAILS,"HUB,MILEA,PUBLIC SERVICE,RYDER,UTF");
		defaults.put(PROP_TRUCK_DAMAGE_LOCATIONS,"Front,Back");
		defaults.put(PROP_TRUCK_LOCATIONS,"FreshDirect,Hub-Maspeth,Hub-Vernon,Hub-Dealer,Ryder,Ryder-Dealer,Milea-Queens,Milea-Bronx,Outside Vendor,Public Service,Public Service-Dealer,Body Shop,Rental Facility,Thermo King-Vernon,Thermo King-LI");	
		
		// APPDEV-1606 Order Crisis Management URL
        defaults.put(ADMIN_SERVICE_URL_KEY, "http://localhost:7001/admin_service");
        defaults.put(PROP_TRANSPORTATION_FILENAME_MARKETINGORDERRPT,"trn_marketingorderreport_");
    	defaults.put(PROP_TRANSPORTATION_FILENAME_VOICESHOTORDERRPT, "trn_voiceshotorderreport_");
    	defaults.put(PROP_TRANSPORTATION_FILENAME_TIMESLOTEXCEPTIONRPT,"trn_timeslotexceptionreport_");
		defaults.put(PROP_TRANSPORTATION_FILENAME_SOSIMULATIONRPT, "trn_sosimulationreport_");
		defaults.put(PROP_TRANSPORTATION_FILENAME_SOFAILURERPT, "trn_sofailurereport_");
		defaults.put(PROP_JDBCBATCHUPDATE_THRESHOLD, "999");

		defaults.put(PROP_FDPARKING_HQLOCATION, "FreshDirect Head Quarters");
		defaults.put(PROP_FDPARKING_ONROAD, "FreshDirect On-Road");
		defaults.put(PROP_FDTRUCKS_ONROAD_IDENTITY, "Zone");
		defaults.put(PROP_TRANSPORTATION_YARDMONITOR_PAGEREFRESHTIME, "600000");
		defaults.put(PROP_ENABLE_AIRCLIC, "false");
		defaults.put(PROP_TELARGO_BLACKHOLE, "true");
		
		
		defaults.put(PROP_TXTMESSAGE_ACCESSKEY, "e79c258648510d3050f7756aabed5154");
		defaults.put(PROP_SHIFTEVENTLOG_TYPE, "51");
		defaults.put(PROP_EVENTLOG_MAILFROM, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_EVENTLOG_MAILTO, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_EVENTLOG_MAILCC, "");
		defaults.put(PROP_EVENTLOG_MAILSUBJECT, "Eventlog Notification");
		defaults.put(PROP_EVENTLOG_DATALOOKUP, "-2");		
		defaults.put(PROP_KRONOS_CLOUD_ENABLE, "false");
		defaults.put(PROP_MUNI_METER_CARD_MAX_VALUE, "100.00");
		defaults.put(PROP_GMAP_VERSION, "3");
		
		refresh();
	}

	/** Creates new ErpServiceProperties */
    public TransportationAdminProperties() {
    }

    private static void refresh() {
		refresh(false);
	}
    
    public static boolean isAirclicEnabled() {
        return (Boolean.valueOf(get(PROP_ENABLE_AIRCLIC))).booleanValue();
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
    public static String getCSVFileSeparator() {
    	return get(PROP_TRANSPORTATION_CSVFILE_SEPARATOR);
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
    
    public static String getRoutingOutputRouteStopFilename() {
    	return get(PROP_TRANSPORTATION_FILENAME_ROUTINGOUTORDERTRUCK);
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

	public static int getTruckCacheMinExpiryTime() {
		return getIntVal(get(PROP_TRANSPORTATION_TRK_CACHE_MIN_EXPIRY_TIME));
	}

	public static int getEmployeeCacheMinExpiryTime() {
		return getIntVal(get(PROP_TRANSPORTATION_EMPLOYEE_CACHE_MIN_EXPIRY_TIME));
	}
	
	public static int getRouteCacheMinExpiryTime() {
		return getIntVal(get(PROP_TRANSPORTATION_ROUTE_CACHE_MIN_EXPIRY_TIME));
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
	public static String getKronosUploadIndividualFileName() {
		return get(KRONOS_UPLOAD_INDIVIDUAL_DAY);
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

	public static String getDynamicRoutingFeatureAccessKey() {
		return get(PROP_TRANSPORTATION_EDITDYNAMICROUTINGFEATURE_ACCESSKEY);
	}
	
	public static String getGeneratePlanFeatureAccessKey() {
		return get(PROP_TRANSPORTATION_GENERATEPLAN_ACCESSKEY);
	}
	

	public static int getDispatchDashboardReadyViewRefreshTime()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_REFRESHTIME));
	}
	public static int getDispatchDashboardPageReadyViewRefreshTime()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_PAGEREFRESHTIME));
	}
	public static int getDispatchDashboardPageReadyViewSize()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_PAGESIZE));
	}
	public static int getMaxReadyView()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_READY_VIEW_MAX_READY));
	}

	public static int getDispatchDashboardWaitingViewRefreshTime()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_REFRESHTIME));
	}
	public static int getDispatchDashboardPageWaitingViewRefreshTime()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_PAGEREFRESHTIME));
	}
	public static int getDispatchDashboardPageWaitingViewSize()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_WAITING_VIEW_PAGESIZE));
	}


	public static int getDispatchDashboardNRViewRefreshTime()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_REFRESHTIME));
	}
	public static int getDispatchDashboardPageNRViewRefreshTime()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_PAGEREFRESHTIME));
	}
	public static int getDispatchDashboardPageNRViewSize()
	{
		return getIntVal(get(PROP_TRANSPORTATION_DISPATCH_NR_VIEW_PAGESIZE));
	}
	public static String getCSSPropertyValue(String view,String page,String font)
	{
		String temp="PROP_TRANSPORTATION_DISPATCH_"+view+"_VIEW_CSS_"+page+"_FONT"+(font!=null?"_"+font:"");
		try {
			temp=(String)TransportationAdminProperties.class.getDeclaredField(temp).get(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return get(temp);
	}
	
	public static String getZoneExpansionTables(){
		return get(PROP_ZONE_EXPANSION_WORKTABLES);
	}

	public static String getDefaultGpsDomain() {
		return get(PROP_TRANSPORTATION_DEFAULT_GPSDOMAIN);
	}

	public static String getDefaultGpsPluginKey() {
		return get(PROP_TRANSPORTATION_DEFAULT_GPSPLUGINKEY);
    }
	
	public static String getGpsUploadFilename() {
		return get(PROP_TRANSPORTATION_GPSUPLOAD_FILENAME);
    }
	public static String getScribHolidayLabels() {
		return get(PROP_HOLIDAY_PLANNING_LABELS);
    }
	
	public static String getExportSchedulesFilename() {
		return get(PROP_EXPORT_SCHEDULES);
    }
	
	public static String getEmployeeScheduleOutputFormat() {
    	return get(PROP_TRANSPORTATION_FORMAT_EMPPLOYEESCHEDULE_UPLOAD);
    }
	
	public static String getScribOutputFormat() {
		return get(PROP_TRANSPORTATION_FORMAT_SCRIB_UPLOAD);
	}
	
	public static String getTransportationVendors() {
		return get(PROP_VENDOR_DETAILS);
    }
	
	public static String getTruckDamageLocations() {
		return get(PROP_TRUCK_DAMAGE_LOCATIONS);
    }
	
	public static String getTruckLocations() {
		return get(PROP_TRUCK_LOCATIONS);
    }

	public static String getMarketingOrderRptFileName() {
		return get(PROP_TRANSPORTATION_FILENAME_MARKETINGORDERRPT);
	}
		
	public static String getVoiceShotOrderRptFileName() {
		return get(PROP_TRANSPORTATION_FILENAME_VOICESHOTORDERRPT);
	}
	
	public static String getAdminServiceURL() {
	    return get(ADMIN_SERVICE_URL_KEY);
	}

	public static String getTimeSlotExceptionRptFileName() {
		return get(PROP_TRANSPORTATION_FILENAME_TIMESLOTEXCEPTIONRPT);
	}

	public static String getSOSimulationRptFileName() {
		return get(PROP_TRANSPORTATION_FILENAME_SOSIMULATIONRPT);
	}
	public static String getSOFailureRptFileName() {
		return get(PROP_TRANSPORTATION_FILENAME_SOFAILURERPT);
	}
	public static int getJDBCBatchUpdateThreshold() {
		return getIntVal(get(PROP_JDBCBATCHUPDATE_THRESHOLD));
	}
	public static int getDriverReqForTrailer() {
		return getIntVal(get(PROP_TRANSPORTATION_TRAILER_DRIVER_REQ));
	}
	public static int getDriverMaxForTrailer() {
		return getIntVal(get(PROP_TRANSPORTATION_TRAILER_DRIVER_MAX));
	}
	public static int getHelperReqForTrailer() {
		return getIntVal(get(PROP_TRANSPORTATION_TRAILER_HELPER_REQ));
	}
	public static int getHelperMaxForTrailer() {
		return getIntVal(get(PROP_TRANSPORTATION_TRAILER_HELPER_MAX));
	}
	public static int getRunnerReqForTrailer() {
		return getIntVal(get(PROP_TRANSPORTATION_TRAILER_RUNNER_REQ));
	}
	public static int getRunnerMaxForTrailer() {
		return getIntVal(get(PROP_TRANSPORTATION_TRAILER_RUNNER_MAX));
	}

	public static int getDriverReqForShuttle() {
		return getIntVal(get(PROP_TRANSPORTATION_SHUTTLE_DRIVER_REQ));
	}
	public static int getDriverMaxForShuttle() {
		return getIntVal(get(PROP_TRANSPORTATION_SHUTTLE_DRIVER_MAX));
	}
	public static int getHelperReqForShuttle() {
		return getIntVal(get(PROP_TRANSPORTATION_SHUTTLE_HELPER_REQ));
	}
	public static int getHelperMaxForShuttle() {
		return getIntVal(get(PROP_TRANSPORTATION_SHUTTLE_HELPER_MAX));
	}
	public static int getRunnerReqForShuttle() {
		return getIntVal(get(PROP_TRANSPORTATION_SHUTTLE_RUNNER_REQ));
	}
	public static int getRunnerMaxForShuttle() {
		return getIntVal(get(PROP_TRANSPORTATION_SHUTTLE_RUNNER_MAX));
	}
	
	public static int getDriverReqForHandTruck() {
		return getIntVal(get(PROP_TRANSPORTATION_HANDTRUCK_DRIVER_REQ));
	}
	public static int getDriverMaxForHandTruck() {
		return getIntVal(get(PROP_TRANSPORTATION_HANDTRUCK_DRIVER_MAX));
	}
	public static int getHelperReqForHandTruck() {
		return getIntVal(get(PROP_TRANSPORTATION_HANDTRUCK_HELPER_REQ));
	}
	public static int getHelperMaxForHandTruck() {
		return getIntVal(get(PROP_TRANSPORTATION_HANDTRUCK_HELPER_MAX));
	}
	public static int getRunnerReqForHandTruck() {
		return getIntVal(get(PROP_TRANSPORTATION_HANDTRUCK_RUNNER_REQ));
	}
	public static int getRunnerMaxForHandTruck() {
		return getIntVal(get(PROP_TRANSPORTATION_HANDTRUCK_RUNNER_MAX));
	}
	
	public static String getFDHeadQuarterKey() {
		return get(PROP_FDPARKING_HQLOCATION);
	}
	public static String getFDOnRoadKey() {
		return get(PROP_FDPARKING_ONROAD);
	}
	public static String getFDOnRoadTrucksIdentity() {
		return get(PROP_FDTRUCKS_ONROAD_IDENTITY);
	}
	public static int getYardMonitorRefreshTime() 	{
		return getIntVal(get(PROP_TRANSPORTATION_YARDMONITOR_PAGEREFRESHTIME));
	}

	public static boolean isTelargoServiceBlackhole() {
		return (new Boolean(get(PROP_TELARGO_BLACKHOLE))).booleanValue();
	}
	
	public static int getPunchInfoCacheExpiryTime() {
		return getIntVal(get(PROP_TRANSPORTATION_EMPLOYEE_PUNCHINFO_CACHE_EXPIRY_TIME));

	}
	
	public static boolean isKronosCloudEnabled() {
		return (new Boolean(get(PROP_KRONOS_CLOUD_ENABLE))).booleanValue();
	}

	public static String getAccessKey() {
		return get(PROP_TXTMESSAGE_ACCESSKEY);
	}
	public static String getShiftEventLogType() {
    	return get(PROP_SHIFTEVENTLOG_TYPE);
    }
	public static String getEventLogMailFrom() {
		return get(PROP_EVENTLOG_MAILFROM);
	}
	
	public static String getEventLogMailTo() {
		return get(PROP_EVENTLOG_MAILTO);
	}
	
	public static String getEventLogMailCC() {
		return get(PROP_EVENTLOG_MAILCC);
	}
	
	public static String getEventLogMailSubject() {
		return get(PROP_EVENTLOG_MAILSUBJECT);
	}

	public static int getEquipmentTypeCacheExpiryTime() {
		return getIntVal(get(PROP_TRANSPORTATION_EQT_CACHE_EXPIRY_TIME));
	}
	
	public static int getEventLogDataLookUpDays() {
		return getIntVal(get(PROP_EVENTLOG_DATALOOKUP));

	}
	
	public static double getMuniMeterMaxValue() {
		try {
			double value= Double.parseDouble(PROP_MUNI_METER_CARD_MAX_VALUE);
			return value;
		} catch (NumberFormatException e) {
			return 100.00;
		}
	}
	
	public static String getGMapVersion() {
		return get(PROP_GMAP_VERSION);
	}
	
}
