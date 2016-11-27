package com.freshdirect.routing.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumHandOffDispatchStatus;
import com.freshdirect.routing.dao.IHandOffDAO;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.BuildingOperationDetails;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.HandOffBatch;
import com.freshdirect.routing.model.HandOffBatchAction;
import com.freshdirect.routing.model.HandOffBatchDepotSchedule;
import com.freshdirect.routing.model.HandOffBatchDepotScheduleEx;
import com.freshdirect.routing.model.HandOffBatchDispatch;
import com.freshdirect.routing.model.HandOffBatchPlan;
import com.freshdirect.routing.model.HandOffBatchPlanResource;
import com.freshdirect.routing.model.HandOffBatchRoute;
import com.freshdirect.routing.model.HandOffBatchRouteBreak;
import com.freshdirect.routing.model.HandOffBatchSession;
import com.freshdirect.routing.model.HandOffBatchStop;
import com.freshdirect.routing.model.HandOffBatchTrailer;
import com.freshdirect.routing.model.HandOffDispatch;
import com.freshdirect.routing.model.HandOffDispatchResource;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IBuildingOperationDetails;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchAction;
import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IHandOffBatchDepotScheduleEx;
import com.freshdirect.routing.model.IHandOffBatchPlan;
import com.freshdirect.routing.model.IHandOffBatchPlanResource;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchRouteBreak;
import com.freshdirect.routing.model.IHandOffBatchSession;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.model.IHandOffBatchTrailer;
import com.freshdirect.routing.model.IHandOffDispatch;
import com.freshdirect.routing.model.IHandOffDispatchResource;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.PackagingModel;
import com.freshdirect.routing.model.TruckPreferenceStat;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.routing.truckassignment.Truck;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffDispatchIn;

public class HandOffDAO extends BaseDAO implements IHandOffDAO   {
	
	private static final String INSERT_HANDOFFBATCH = "INSERT INTO TRANSP.HANDOFF_BATCH ( BATCH_ID,"+
															"DELIVERY_DATE, BATCH_STATUS, SYS_MESSAGE, SCENARIO, CUTOFF_DATETIME, IS_COMMIT_ELIGIBLE)" +
															" VALUES ( ?,?,?,?,?,?,?)";
	
	private static final String INSERT_HANDOFFBATCHACTION = "INSERT INTO TRANSP.HANDOFF_BATCHACTION ( BATCH_ID,"+
											"ACTION_DATETIME, ACTION_TYPE, ACTION_BY ) VALUES ( ?,?,?,?)";
	
	private static final String INSERT_HANDOFFBATCH_SESSION = "INSERT INTO TRANSP.HANDOFF_BATCHSESSION ( BATCH_ID,"+
							"SESSION_NAME, REGION ) VALUES (?,?,?)";
	
	private static final String INSERT_HANDOFFBATCH_DEPOTSCHEDULE = "INSERT INTO TRANSP.HANDOFF_BATCHDEPOTSCHEDULE ( BATCH_ID,"+
							"AREA , DEPOTARRIVALTIME, TRUCKDEPARTURETIME, ORIGIN_ID ) VALUES (?,?,?,?,?)";
	
	private static final String INSERT_HANDOFFBATCH_STOP = "INSERT INTO TRANSP.HANDOFF_BATCHSTOP ( BATCH_ID,"+
																	"WEBORDER_ID, ERPORDER_ID, " +
																	"AREA, DELIVERY_TYPE, WINDOW_STARTTIME, WINDOW_ENDTIME, ROUTING_STARTTIME, ROUTING_ENDTIME, LOCATION_ID, " +
																	"SESSION_NAME, ROUTE_NO, ROUTING_ROUTE_NO, " +
																	"STOP_ARRIVALDATETIME,  STOP_DEPARTUREDATETIME, TRAVELTIME , SERVICETIME, SERVICE_ADDR2, ORDERSIZE_SF, MOBILE_NUMBER, IS_DYNAMIC ) " +
																	"VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
	
	private static final String GET_HANDOFFBATCHNEXTSEQ_QRY = "SELECT TRANSP.HANDOFFBATCHSEQ.nextval FROM DUAL";
	
	private static String GET_ORDERSBY_DATE_CUTOFF = "SELECT /*+ USE_NL(s, sa) */ c.id customer_id, fdc.id fdc_id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, "
		+ "ci.cell_phone, ci.mobile_number, s.id weborder_id, s.sap_number erporder_id, sa.requested_date, s.status, sa.amount, di.starttime, di.endtime, "
		+ "di.cutofftime, di.zone ZONE, di.address1, di.address2, di.apartment, di.city, di.state, di.zip, di.country, di.delivery_type, rs.type, rs.NUM_CARTONS, " 
		+ " rs.NUM_FREEZERS, rs.NUM_CASES, rs.id, ts.routing_start_time, ts.routing_end_time, ts.is_dynamic "
		+ "from cust.customer c, cust.fdcustomer fdc " 
		+ ", cust.customerinfo ci " 
		+ ", cust.sale s, cust.salesaction sa " 
		+ ", cust.deliveryinfo di, dlv.reservation rs, dlv.timeslot ts "
		+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id " 
		+ "and s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
		+ "and s.type ='REG' "
		+ "and s.status <> 'CAN' "
		+ "and sa.id = di.salesaction_id and rs.id = di.reservation_id and rs.timeslot_id = ts.id and ts.base_date = sa.requested_date ";
		
	private static String GET_ORDERSBY_DATE_CUTOFFSTANDBY = "SELECT /*+ USE_NL(s, sa) */ c.id customer_id, fdc.id fdc_id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, "
		+ "ci.cell_phone, ci.mobile_number, s.id weborder_id, s.sap_number erporder_id, sa.requested_date, s.status, sa.amount, di.starttime, di.endtime, "
		+ "di.cutofftime, di.zone ZONE, di.address1, di.address2, di.apartment, di.city, di.state, di.zip, di.country, di.delivery_type, rs.type, rs.NUM_CARTONS , " 
		+ "rs.NUM_FREEZERS , rs.NUM_CASES. rs.id, ts.routing_start_time, ts.routing_end_time, ts.is_dynamic  "
		+ "from cust.customer@DBSTOSBY.NYC.FRESHDIRECT.COM c, cust.fdcustomer@DBSTOSBY.NYC.FRESHDIRECT.COM fdc " 
		+ ", cust.customerinfo@DBSTOSBY.NYC.FRESHDIRECT.COM ci " 
		+ ", cust.sale@DBSTOSBY.NYC.FRESHDIRECT.COM s, cust.salesaction@DBSTOSBY.NYC.FRESHDIRECT.COM sa " 
		+ ", cust.deliveryinfo@DBSTOSBY.NYC.FRESHDIRECT.COM di, dlv.reservation@DBSTOSBY.NYC.FRESHDIRECT.COM rs , dlv.timeslot@DBSTOSBY.NYC.FRESHDIRECT.COM ts "
		+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id " 
		+ "and s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
		+ "and s.type ='REG' "
		+ "and s.status <> 'CAN' "
		+ "and sa.id = di.salesaction_id and rs.id = di.reservation_id and rs.timeslot_id = ts.id and ts.base_date = sa.requested_date ";
	
	private static String GET_ORDERSTATSBY_DATE_CUTOFF = "SELECT /*+ USE_NL(s, sa) */ s.status, count(*) as order_count "
		+ "from cust.customer c, cust.fdcustomer fdc " 
		+ ", cust.customerinfo ci " 
		+ ", cust.sale s, cust.salesaction sa " 
		+ ", cust.deliveryinfo di, dlv.reservation rs , dlv.timeslot ts "
		+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id " 
		+ "and s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
		+ "and s.type ='REG' "
		+ "and s.status <> 'CAN' "
		+ "and sa.id = di.salesaction_id and rs.id = di.reservation_id and rs.timeslot_id = ts.id and ts.base_date = sa.requested_date ";
	
	private static String GET_ORDERSTATSBY_DATE_CUTOFFSTANDBY = "SELECT /*+ USE_NL(s, sa) */ s.status, count(*) as order_count "
		+ "from cust.customer@DBSTOSBY.NYC.FRESHDIRECT.COM c, cust.fdcustomer@DBSTOSBY.NYC.FRESHDIRECT.COM fdc " 
		+ ", cust.customerinfo@DBSTOSBY.NYC.FRESHDIRECT.COM ci " 
		+ ", cust.sale@DBSTOSBY.NYC.FRESHDIRECT.COM s, cust.salesaction@DBSTOSBY.NYC.FRESHDIRECT.COM sa " 
		+ ", cust.deliveryinfo@DBSTOSBY.NYC.FRESHDIRECT.COM di, dlv.reservation@DBSTOSBY.NYC.FRESHDIRECT.COM rs  , dlv.timeslot@DBSTOSBY.NYC.FRESHDIRECT.COM ts"
		+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id " 
		+ "and s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
		+ "and s.type ='REG' "
		+ "and s.status <> 'CAN' "
		+ "and sa.id = di.salesaction_id and rs.id = di.reservation_id and rs.timeslot_id = ts.id and ts.base_date =  sa.requested_date ";
	
	private static final String UPDATE_HANDOFFBATCH_MESSAGE = "UPDATE TRANSP.HANDOFF_BATCH SET SYS_MESSAGE = ? where BATCH_ID = ?";
	
	private static final String UPDATE_HANDOFFBATCH_STATUS = "UPDATE TRANSP.HANDOFF_BATCH SET BATCH_STATUS = ? where BATCH_ID = ?";
	
	private static final String UPDATE_HANDOFFBATCH_COMMITELIGIBLE = "UPDATE TRANSP.HANDOFF_BATCH SET IS_COMMIT_ELIGIBLE = ? where BATCH_ID = ?";
	
	private static final String GET_HANDOFFBATCH_DATERANGE = "SELECT b.BATCH_ID, b.DELIVERY_DATE, b.SCENARIO, b.CUTOFF_DATETIME" +
			", B.BATCH_STATUS, B.SYS_MESSAGE, B.IS_COMMIT_ELIGIBLE " +
			", BA.ACTION_BY, BA.ACTION_DATETIME, BA.ACTION_TYPE , BS.SESSION_NAME, BS.REGION,tr.is_depot, " +
			" BD.AREA DEPOT_AREA, BD.DEPOTARRIVALTIME, BD.TRUCKDEPARTURETIME, BD.ORIGIN_ID, " +
			" (select count(1) from transp.HANDOFF_BATCHSTOP xS where xS.BATCH_ID = B.BATCH_ID) STOPCOUNT " +
			" from transp.HANDOFF_BATCH b, transp.HANDOFF_BATCHACTION ba, transp.HANDOFF_BATCHSESSION bs, transp.HANDOFF_BATCHDEPOTSCHEDULE bd , transp.trn_region tr " +
			" where B.DELIVERY_DATE > (sysdate - ?) and B.BATCH_ID = BA.BATCH_ID and tr.CODE(+) = bs.region and B.BATCH_ID = BS.BATCH_ID(+) and B.BATCH_ID = BD.BATCH_ID(+) " +
			" order by b.BATCH_ID, b.DELIVERY_DATE, BA.ACTION_DATETIME";
	
	private static final String GET_HANDOFFBATCH_DELIVERYDATE = "SELECT b.BATCH_ID, b.DELIVERY_DATE, b.SCENARIO" +
			", b.CUTOFF_DATETIME, B.BATCH_STATUS, B.SYS_MESSAGE, B.IS_COMMIT_ELIGIBLE " +
			", BA.ACTION_BY, BA.ACTION_DATETIME, BA.ACTION_TYPE , BS.SESSION_NAME, BS.REGION,tr.is_depot," +
			" BD.AREA DEPOT_AREA, BD.DEPOTARRIVALTIME, BD.TRUCKDEPARTURETIME, BD.ORIGIN_ID , " +
			" (select count(1) from transp.HANDOFF_BATCHSTOP xS where xS.BATCH_ID = B.BATCH_ID) STOPCOUNT " +
			" from transp.HANDOFF_BATCH b, transp.HANDOFF_BATCHACTION ba, transp.HANDOFF_BATCHSESSION bs, transp.HANDOFF_BATCHDEPOTSCHEDULE bd, transp.trn_region tr   " +
			" where B.DELIVERY_DATE = ? and B.BATCH_ID = BA.BATCH_ID  and tr.CODE(+) = bs.region and B.BATCH_ID = BS.BATCH_ID(+) and B.BATCH_ID = BD.BATCH_ID(+)  " +
			" order by b.BATCH_ID, b.DELIVERY_DATE, BA.ACTION_DATETIME";
	
	private static final String GET_HANDOFFBATCH_BYID = "SELECT b.BATCH_ID, b.DELIVERY_DATE, b.SCENARIO, b.CUTOFF_DATETIME" +
			", B.BATCH_STATUS, B.SYS_MESSAGE, B.IS_COMMIT_ELIGIBLE " +
			", BA.ACTION_BY, BA.ACTION_DATETIME, BA.ACTION_TYPE , BS.SESSION_NAME, BS.REGION, tr.is_depot, " +
			" BD.AREA DEPOT_AREA, BD.DEPOTARRIVALTIME, BD.TRUCKDEPARTURETIME, BD.ORIGIN_ID, " +
			" (select count(1) from transp.HANDOFF_BATCHSTOP xS where xS.BATCH_ID = B.BATCH_ID) STOPCOUNT " +
			" from transp.HANDOFF_BATCH b, transp.HANDOFF_BATCHACTION ba, transp.HANDOFF_BATCHSESSION bs, transp.HANDOFF_BATCHDEPOTSCHEDULE bd, transp.trn_region tr  " +
			" where B.BATCH_ID = ? and B.BATCH_ID = BA.BATCH_ID and tr.CODE(+) = bs.region and B.BATCH_ID = BS.BATCH_ID(+) and B.BATCH_ID = BD.BATCH_ID(+)  " +
			" order by b.BATCH_ID, b.DELIVERY_DATE, BA.ACTION_DATETIME";
	
	private static final String GET_HANDOFFBATCH_ROUTECNT = "select R.AREA area, count(1) NOOFROUTES " +
			"from TRANSP.HANDOFF_BATCH b, TRANSP.HANDOFF_BATCHROUTE r where B.DELIVERY_DATE = ? " +
			"and B.BATCH_STATUS IN ('CPD/ADC','CPD','CPD/ADF') and B.BATCH_ID = R.BATCH_ID group by R.AREA";
	
	private static final String GET_HANDOFFBATCH_DISPATCHCNT = "select R.DISPATCHTIME DISPATCHTIME, max(DISPATCHSEQUENCE) CURRDISPATCHSEQ " +
	"from TRANSP.HANDOFF_BATCH b, TRANSP.HANDOFF_BATCHROUTE r where B.DELIVERY_DATE = ? " +
	"and B.BATCH_STATUS IN ( 'CPD/ADC','CPD','CPD/ADF') and B.BATCH_ID = R.BATCH_ID group by R.DISPATCHTIME";
	
	private static final String CLEAR_HANDOFFBATCH_SESSION = "DELETE FROM TRANSP.HANDOFF_BATCHSESSION X WHERE X.BATCH_ID = ?";
	
	private static final String CLEAR_HANDOFFBATCH_DEPOTSCHEDULE = "DELETE FROM TRANSP.HANDOFF_BATCHDEPOTSCHEDULE X WHERE X.BATCH_ID = ?";
	
	private static final String CLEAR_HANDOFFBATCH_STOPS = "DELETE FROM TRANSP.HANDOFF_BATCHSTOP X WHERE X.BATCH_ID = ?";
	
	private static final String CLEAR_HANDOFFBATCH_ROUTES = "DELETE FROM TRANSP.HANDOFF_BATCHROUTE X WHERE X.BATCH_ID = ?";
	
	private static final String CLEAR_HANDOFFBATCH_ROUTEBREAKS = "DELETE FROM TRANSP.HANDOFF_BATCHROUTE_BREAKS X WHERE X.BATCH_ID = ?";
	
	private static final String CLEAR_HANDOFFBATCH_TRAILERS = "DELETE FROM TRANSP.HANDOFF_BATCHTRAILER X WHERE X.BATCH_ID = ?";

	private static final String CLEAR_HANDOFFBATCH_DISPATCHES = "DELETE FROM TRANSP.HANDOFF_BATCHDISPATCHEX X WHERE X.BATCH_ID = ?";
	
	private static final String INSERT_HANDOFFBATCH_DISPATCH = "INSERT INTO TRANSP.HANDOFF_BATCHDISPATCHEX ( BATCH_ID , DISPATCHTIME, PLANNED_RESOURCES ," +
		"ACTUAL_RESOURCES ,STATUS) VALUES ( ?,?,?,?,?)";
			
	private static final String INSERT_HANDOFFBATCH_ROUTE = "INSERT INTO TRANSP.HANDOFF_BATCHROUTE ( BATCH_ID , SESSION_NAME, ROUTE_NO ," +
			"ROUTING_ROUTE_NO ,AREA ,STARTTIME,COMPLETETIME ,DISTANCE, TRAVELTIME, SERVICETIME, DISPATCHTIME, DISPATCHSEQUENCE,RN_ROUTE_ID, ORIGIN_ID ) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String INSERT_HANDOFFBATCH_ROUTEBREAK = "INSERT INTO TRANSP.HANDOFF_BATCHROUTE_BREAKS ( BATCH_ID , SESSION_NAME, ROUTE_NO ," +
			"BREAK_ID, BREAK_START_TIME, BREAK_END_TIME ) VALUES ( ?,?,?,?,?,? )";
	
	private static final String UPDATE_HANDOFFBATCH_STOP = "UPDATE TRANSP.HANDOFF_BATCHSTOP X set X.ROUTE_NO = ?,X.ROUTING_ROUTE_NO = ?, " +
			"X.SESSION_NAME = ?, X.STOP_SEQUENCE = ?,  X.STOP_ARRIVALDATETIME = ? ,  X.STOP_DEPARTUREDATETIME = ?, X.TRAVELTIME = ? , X.SERVICETIME = ?, X.ORDERSIZE_RT = ?, X.DLV_ETA_STARTTIME = ?, X.DLV_ETA_ENDTIME = ? " +
			"WHERE X.BATCH_ID = ? and X.WEBORDER_ID = ?";
			
	private static final String CLEAR_HANDOFFBATCH_STOPROUTE = "UPDATE TRANSP.HANDOFF_BATCHSTOP X set X.ROUTE_NO = null,X.ROUTING_ROUTE_NO = null, " +
			"X.STOP_SEQUENCE = null,  X.STOP_ARRIVALDATETIME = null,  X.STOP_DEPARTUREDATETIME = null, X.TRAVELTIME = null , X.SERVICETIME = null  " +
			"WHERE X.BATCH_ID = ?";
	
	private static final String GET_HANDOFFBATCH_STOPS = "select hb.DELIVERY_DATE, s.BATCH_ID , s.WEBORDER_ID , s.ERPORDER_ID , s.AREA, s.DELIVERY_TYPE " +
			", s.WINDOW_STARTTIME, s.WINDOW_ENDTIME, s.ROUTING_STARTTIME, s.ROUTING_ENDTIME, s.DLV_ETA_STARTTIME, s.DLV_ETA_ENDTIME, s.LOCATION_ID , s.SESSION_NAME " +
			",s.ROUTE_NO ,s.ROUTING_ROUTE_NO ,s.STOP_SEQUENCE ,s.STOP_ARRIVALDATETIME ,s.STOP_DEPARTUREDATETIME, s.IS_EXCEPTION, s.SERVICE_ADDR2, s.ORDERSIZE_SF  " +
			",dd.ADDR_TYPE ,dd.COMPANY_NAME ,dd.DIFFICULT_TO_DELIVER ,dd.DIFFICULT_REASON ,dd.ADDITIONAL  " +
			",dd.IS_DOORMAN  ,dd.IS_WALKUP  ,dd.IS_ELEVATOR ,dd.IS_HOUSE  ,dd.IS_FREIGHT_ELEVATOR " +
			",dd.SVC_ENT  ,dd.SVC_SCRUBBED_STREET  ,dd.SVC_CITY ,dd.SVC_STATE ,dd.SVC_ZIP ,dd.HAND_TRUCK_ALLOWED " +
			",dd.WALK_UP_FLOORS ,dd.CREATED_BY ,dd.MODTIME ,dd.SVC_CROSS_STREET ,dd.CROSS_STREET ,dd.OTHER " +
			",bo.BLDG_START_HOUR ,bo.BLDG_END_HOUR  ,bo.BLDG_COMMENTS  ,bo.SERVICE_START_HOUR " +
			",bo.SERVICE_END_HOUR  ,bo.SERVICE_COMMENTS  ,bo.DAY_OF_WEEK, ta.DELIVERYMODEL, tz.MANIFEST_ETA_ENABLED " +
			",b.SCRUBBED_STREET bSCRUBBED_STREET ,b.ZIP bzip ,b.COUNTRY  bCOUNTRY,b.CITY bCITY, b.STATE bSTATE, " +
			"b.LONGITUDE  BLONG,b.LATITUDE BLAT, l.APARTMENT LOCAPART, s.is_dynamic " +
			"from transp.HANDOFF_BATCH hb , transp.HANDOFF_BATCHSTOP s , DLV.DELIVERY_LOCATION l , dlv.delivery_building b" +
			", DLV.DELIVERY_BUILDING_DETAIL dd, TRANSP.TRN_AREA ta, TRANSP.ZONE tz, (select distinct xbo.* from transp.HANDOFF_BATCH xhb , transp.HANDOFF_BATCHSTOP xs " +
			", DLV.DELIVERY_LOCATION xl , dlv.delivery_building xb  , DLV.DELIVERY_BUILDING_DETAIL xdd, DLV.DELIVERY_BUILDING_DETAIL_OPS xbo  " +
			"where xhb.BATCH_ID = ? and xHB.BATCH_ID = xS.BATCH_ID and xS.LOCATION_ID = xL.ID and xL.BUILDINGID = xB.ID " +
			"and xB.ID = xDD.DELIVERY_BUILDING_ID and xB.ID = xBO.DELIVERY_BUILDING_ID  " +
			"and to_char(xHB.DELIVERY_DATE ,'D')  = xBO.DAY_OF_WEEK) bo  " +
			"where hb.BATCH_ID = ? and HB.BATCH_ID = S.BATCH_ID and S.LOCATION_ID = L.ID and L.BUILDINGID = B.ID " +
			"and B.ID = DD.DELIVERY_BUILDING_ID(+) and B.ID = bo.DELIVERY_BUILDING_ID(+) and s.area = ta.code(+) and s.area = tz.zone_code(+)"; 
	
	private static final String GET_HANDOFFBATCH_ROUTES = "select r.BATCH_ID, r.SESSION_NAME, r.ROUTE_NO, r.ROUTING_ROUTE_NO "+
            " ,r.AREA, r.STARTTIME, r.COMPLETETIME, r.DISTANCE, r.TRAVELTIME, r.SERVICETIME, r.DISPATCHTIME, r.DISPATCHSEQUENCE, r.TRAILER_NO, "+
            " decode(TR.IS_DEPOT, 'X', r.ORIGIN_ID, null) as ORIGIN_ID, "+
            " decode(TR.IS_DEPOT, 'X', r.ORIGIN_ID ||' / '|| (SELECT description from transp.TRN_FACILITY where facility_code = r.ORIGIN_ID ), null) as DEPOT_PARKLOCATION "+
            " from transp.HANDOFF_BATCH b, TRANSP.HANDOFF_BATCHROUTE r, TRANSP.TRN_AREA a, TRANSP.TRN_REGION tr "+
            " where B.BATCH_ID = ? and B.BATCH_ID = R.BATCH_ID and R.AREA=A.CODE AND TR.CODE = A.REGION_CODE";
	
	private static final String GET_HANDOFFBATCH_DISPATCHES = "select d.DISPATCHTIME, d.PLANNED_RESOURCES ," +
			" d.ACTUAL_RESOURCES , d.STATUS " +
			" from  TRANSP.HANDOFF_BATCHDISPATCHEX d where d.BATCH_ID = ? order by d.DISPATCHTIME";
	
	private static final String GET_HANDOFFBATCH_STOPBYROUTE = "select hb.DELIVERY_DATE, s.BATCH_ID , s.WEBORDER_ID , s.ERPORDER_ID " +
			", s.AREA, s.DELIVERY_TYPE, s.WINDOW_STARTTIME, s.WINDOW_ENDTIME, s.ROUTING_STARTTIME, s.ROUTING_ENDTIME, s.DLV_ETA_STARTTIME, s.DLV_ETA_ENDTIME, s.LOCATION_ID , s.SESSION_NAME, s.ROUTE_NO " +
			", s.ROUTING_ROUTE_NO , s.STOP_SEQUENCE , s.STOP_ARRIVALDATETIME , s.STOP_DEPARTUREDATETIME, s.IS_EXCEPTION " +
			", b.SCRUBBED_STREET bSCRUBBED_STREET, b.ZIP bzip ,b.COUNTRY  bCOUNTRY, b.CITY bCITY, b.STATE bSTATE" +
			", b.LONGITUDE  BLONG, b.LATITUDE BLAT, l.APARTMENT LOCAPART" +
			", r.ROUTE_NO RROUTE_NO,r.ROUTING_ROUTE_NO RROUTING_ROUTE_NO, r.RN_ROUTE_ID RN_ROUTE_ID, r.AREA RAREA " +
			", r.STARTTIME RSTARTTIME , r.COMPLETETIME RCOMPLETETIME  ,r.DISTANCE RDISTANCE" +
			", r.TRAVELTIME RTRAVELTIME, r.SERVICETIME RSERVICETIME, z.MANIFEST_ETA_ENABLED, s.is_dynamic  " +
			" from transp.HANDOFF_BATCH hb , TRANSP.HANDOFF_BATCHROUTE r, transp.HANDOFF_BATCHSTOP s" +
			", DLV.DELIVERY_LOCATION l , dlv.delivery_building b, transp.ZONE z" +
			" where HB.DELIVERY_DATE  = ? and HB.BATCH_STATUS IN ('CPD/ADC','CPD','CPD/ADF') and R.ROUTE_NO = ? " +
			"and HB.BATCH_ID = R.BATCH_ID and HB.BATCH_ID = s.BATCH_ID  and R.ROUTE_NO = S.ROUTE_NO " +
			"and S.LOCATION_ID = L.ID and L.BUILDINGID = B.ID and s.area = z.zone_code(+) order by s.ROUTE_NO, s.STOP_SEQUENCE";
	
	private static final String UPDATE_HANDOFFBATCH_STOPEXCEPTION = "UPDATE TRANSP.HANDOFF_BATCHSTOP SET IS_EXCEPTION = 'X' " +
																		" where BATCH_ID = ? AND WEBORDER_ID in (";
	
	private static final String CLEAR_HANDOFFBATCH_STOPEXCEPTION = "UPDATE TRANSP.HANDOFF_BATCHSTOP SET IS_EXCEPTION = NULL " +
							" where BATCH_ID = ? and IS_EXCEPTION = 'X' ";
	
	private static final String UPDATE_HANDOFFBATCH_STOP_ERPNO = "UPDATE TRANSP.HANDOFF_BATCHSTOP X set X.ERPORDER_ID = ? " +
				"WHERE X.BATCH_ID = ? and X.WEBORDER_ID = ?";
	
	private static final String INSERT_HANDOFFBATCH_DEPOTSCHEDULE_EX = "INSERT INTO TRANSP.HANDOFF_BATCHDEPOTSCHEDULE_EX ( DAY_OF_WEEK, CUTOFF_DATETIME, "+
				"AREA , DEPOTARRIVALTIME, TRUCKDEPARTURETIME, ORIGIN_ID ) VALUES (?,?,?,?,?,?)";
	
	private static final String CLEAR_HANDOFFBATCH_DEPOTSCHEDULE_EX = "DELETE FROM TRANSP.HANDOFF_BATCHDEPOTSCHEDULE_EX X WHERE X.DAY_OF_WEEK = ? and CUTOFF_DATETIME = ?";

	private static final String GET_HANDOFFBATCH_DEPOTSCHEDULE_EX = "SELECT * FROM TRANSP.HANDOFF_BATCHDEPOTSCHEDULE_EX X WHERE X.DAY_OF_WEEK = ? and CUTOFF_DATETIME = ?";
		
	private static final String GET_HANDOFFBATCH_DEPOTSCHEDULE_DAYOFWEEK = "SELECT * FROM TRANSP.HANDOFF_BATCHDEPOTSCHEDULE_EX X WHERE upper(X.DAY_OF_WEEK) = upper(?)";
	
	private static final String GET_HANDOFFBATCH_LASTCOMMITEDBATCH = "select b.BATCH_ID from TRANSP.HANDOFF_BATCH b " +
			"where b.DELIVERY_DATE = ? and b.BATCH_STATUS IN ('CPD/ADC','CPD','CPD/ADF') order by CUTOFF_DATETIME desc";

	private static final String INSERT_HANDOFFBATCH_TRAILER = "INSERT INTO TRANSP.HANDOFF_BATCHTRAILER ( BATCH_ID , TRAILER_NO ," +
			" DISPATCHTIME, COMPLETETIME, CROSSDOCK_CODE ) VALUES ( ?,?,?,?,? )";

	private static final String UPDATE_HANDOFFBATCH_ROUTE = "UPDATE TRANSP.HANDOFF_BATCHROUTE X set X.TRAILER_NO = ? "+
			"WHERE X.BATCH_ID = ? and X.ROUTE_NO = ?";

	private static final String GET_HANDOFFBATCH_TRAILERS = "select r.BATCH_ID, r.TRAILER_NO " +
			" , r.COMPLETETIME, r.DISPATCHTIME, r.CROSSDOCK_CODE  " +
			"from transp.HANDOFF_BATCH b, TRANSP.HANDOFF_BATCHTRAILER r  where B.BATCH_ID = ? and B.BATCH_ID = R.BATCH_ID";
		
	private static final String GET_HANDOFFBATCH_TRAILERCNT = "select T.CROSSDOCK_CODE, count(1) NOOFTRAILERS " +
			"from TRANSP.HANDOFF_BATCH b, TRANSP.HANDOFF_BATCHTRAILER t where B.DELIVERY_DATE = ? " +
			"and B.BATCH_STATUS IN ('CPD/ADC','CPD','CPD/ADF') and B.BATCH_ID = T.BATCH_ID group by T.CROSSDOCK_CODE";

	
	/*Auto dispatch queries*/
	private static final String GET_HANDOFFBATCH_ASSETTRUCKS = "SELECT A.ASSET_NO, A.ASSET_STATUS, A.ASSET_TYPE " +
			" FROM TRANSP.ASSET A WHERE A.ASSET_TYPE in ('TRUCK', 'TRAILER') and A.ASSET_STATUS = 'ACT' " +
			" and A.ASSET_NO not in (SELECT D.PHYSICAL_TRUCK FROM TRANSP.DISPATCH D WHERE D.DISPATCH_DATE = ? AND D.PHYSICAL_TRUCK IS NOT NULL)"; 
	
	private static final String GET_HANDOFFBATCH_TRUCKPREFERECES = "SELECT * FROM TRANSP.TRUCK_PREFERENCE"; 
	
	@SuppressWarnings("unused")
	private static final String GET_RESOURCE_TRUCKSTATISTICS = "SELECT DR.RESOURCE_ID as KRONOS_ID, D.TRUCK as TRUCK, COUNT(D.DISPATCH_ID) as COUNT"
														+" from TRANSP.DISPATCH D,  TRANSP.DISPATCH_RESOURCE DR"
														+" WHERE D.TRUCK is NOT NULL and D.DISPATCH_DATE > trunc(sysdate-180) and" 
														+" D.DISPATCH_ID=DR.DISPATCH_ID and DR.role='001' "
														+" Group by DR.RESOURCE_ID,D.TRUCK "
														+" Order by RESOURCE_ID ASC, COUNT DESC";
	
	private static final String GET_HANDOFFBATCHDISPATCHSEQ_QRY = "select TRANSP.DISPATCHSEQ.nextval FROM DUAL";
				
	private static final String GET_HANDOFFBATCH_DISPATCHROUTES = "SELECT R.AREA, R.ROUTE_NO, R.STARTTIME, R.DISPATCHTIME, "+
         " (SELECT min(S.WINDOW_STARTTIME) from TRANSP.HANDOFF_BATCHSTOP s where S.BATCH_ID = R.BATCH_ID and S.ROUTE_NO = R.ROUTE_NO ) FIRSTDLVTIME, "+                                                          
         " decode(TR.IS_DEPOT , 'X', (SELECT max(S.STOP_DEPARTUREDATETIME ) + 25/1440  from TRANSP.HANDOFF_BATCHSTOP s where S.BATCH_ID = R.BATCH_ID and S.ROUTE_NO = R.ROUTE_NO), R.COMPLETETIME) COMPLETETIME "+ 
         " from transp.HANDOFF_BATCH b, TRANSP.HANDOFF_BATCHROUTE R, TRANSP.TRN_AREA A, TRANSP.TRN_REGION TR " +
         " where B.DELIVERY_DATE = ? "+
         " and to_char(b.cutoff_datetime, 'HH:MI AM') = to_char(?, 'HH:MI AM') " +
         " and B.BATCH_ID = R.BATCH_ID and R.AREA=A.CODE AND TR.CODE = A.REGION_CODE and B.BATCH_STATUS IN ('CPD/ADC','CPD','CPD/ADF') ";

	private static final String BATCH_COMPLETED_DISPATCH_CND = " (select bd.dispatchtime from transp.handoff_batchdispatchex bd where bd.batch_id = ? and bd.status = 'CPD' "+ 
			            " and bd.dispatchtime not in (select distinct DX.DISPATCHTIME from TRANSP.HANDOFF_BATCH bx , TRANSP.HANDOFF_BATCHDISPATCHEX dx, TRANSP.TRN_CUTOFF cx "+
			            " where bx.batch_id = dx.batch_id and cx.cutoff_time = bx.cutoff_datetime and bx.delivery_date =  ? "+
			            " and cx.sequenceno < (select cz.sequenceno from TRANSP.HANDOFF_BATCH bz, TRANSP.TRN_CUTOFF cz where cz.cutoff_time = bz.cutoff_datetime and bz.batch_id = ? ) "+
			            " and dx.status = 'CPD' and bx.BATCH_STATUS IN ('CPD/ADC','CPD','CPD/ADF')))";
	
	private static final String GET_HANDOFFBATCH_PLANS = "select x.*, (select maximum_no from transp.zone z, TRANSP.ZONETYPE_RESOURCE zr " +
			"where Z.ZONE_TYPE = ZR.ZONETYPE_ID and ZR.ROLE = '003' and x.ZONE = Z.ZONE_CODE) RESOURCE_COUNT from "+
             " ( "+
                	" select p.* from transp.plan p where p.plan_date = ? and to_char(p.cutoff_datetime, 'HH:MI AM') = to_char(?, 'HH:MI AM') "+
                " UNION "+
                	" select p.* from transp.plan p where p.plan_date = ? and p.is_bullpen = 'Y' " +
                		" and p.truck_dispatchtime in "+ BATCH_COMPLETED_DISPATCH_CND + " "+
             " ) x  order by x.plan_date, x.zone, x.dispatch_grouptime, x.truck_dispatchtime, x.sequence ";
		
	private static final String GET_HANDOFFBATCH_PLANRESOURCES = 
				" select pr.* from transp.plan p, transp.plan_resource pr "+ 
					" where pr.plan_id = p.plan_id and p.plan_date = ? and to_char(p.cutoff_datetime, 'HH:MI AM') = to_char(?, 'HH:MI AM') "+
			" UNION "+
				" select pr.* from transp.plan p, transp.plan_resource pr where pr.plan_id = p.plan_id and p.plan_date = ? and p.is_bullpen = 'Y' " +
	  				" and p.truck_dispatchtime in "+ BATCH_COMPLETED_DISPATCH_CND + " ";
   	
	private static final String INSERT_HANDOFFBATCH_AUTODISPATCHES = "INSERT INTO TRANSP.DISPATCH ( DISPATCH_ID, DISPATCH_DATE, ORIGIN_FACILITY, DESTINATION_FACILITY, " +
			" ZONE, SUPERVISOR_ID, ROUTE, DISPATCH_GROUPTIME, TRUCK_DISPATCHTIME, PLAN_ID, ISBULLPEN, REGION, PHYSICAL_TRUCK, CUTOFF_DATETIME, DISPATCH_TYPE, START_TIME ) " +
			" VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
	
	private static final String INSERT_HANDOFFBATCH_AUTODISPATCHRESOURCES = "INSERT INTO TRANSP.DISPATCH_RESOURCE ( DISPATCH_ID, RESOURCE_ID , ROLE ) VALUES ( ?,?,? )";
		
	private static final String CLEAR_HANDOFFBATCH_AUTODISPATCHES = "DELETE FROM TRANSP.DISPATCH D WHERE D.DISPATCH_DATE = ? " +
			" and to_char(d.cutoff_datetime, 'HH:MI AM') = to_char(?, 'HH:MI AM') " +
			" or " +
			" (D.ISBULLPEN = 'Y' and D.TRUCK_DISPATCHTIME in "+ BATCH_COMPLETED_DISPATCH_CND + " )";
	
	private static final String CLEAR_HANDOFFBATCH_AUTODISPATCHRESOURCES  = "DELETE FROM TRANSP.DISPATCH_RESOURCE DR WHERE DR.DISPATCH_ID IN (SELECT D.DISPATCH_ID FROM TRANSP.DISPATCH D WHERE D.DISPATCH_DATE = ? " +
			" and to_char(d.cutoff_datetime, 'HH:MI AM') = to_char(?, 'HH:MI AM') " +
			" or " +
			" (D.ISBULLPEN = 'Y' and D.TRUCK_DISPATCHTIME in "+ BATCH_COMPLETED_DISPATCH_CND + " )" +
			" )";
	
	private static final String GET_HANDOFFBATCH_DISPATCH_QRY = " select d.*, dr.*, f.facilitytype_code, p.max_time "+
            " from transp.dispatch d, transp.dispatch_resource dr, transp.trn_facility f, transp.plan p "+
            " where d.dispatch_date = ? and to_char(d.cutoff_datetime, 'HH:MI AM') = to_char(?, 'HH:MI AM') "+
            " and d.destination_facility = f.id and dr.dispatch_id = d.dispatch_id  and p.plan_id(+) = d.plan_id and d.route is not null "+ 
            " order by d.zone, d.route ";
	
	private static final String UPDATE_HANDOFFDISPATCH_TRUCK = "UPDATE TRANSP.DISPATCH X set X.PHYSICAL_TRUCK = ? WHERE X.DISPATCH_ID = ? ";
	
	private static final String GET_HANDOFFBATCH_ROUTE_BREAKS = "select * from TRANSP.HANDOFF_BATCHROUTE_BREAKS b where b.batch_id = ?";
	
	public List<IHandOffBatchRoute> getHandOffBatchRoutes(final String batchId) throws SQLException {

		final List<IHandOffBatchRoute> result = new ArrayList<IHandOffBatchRoute>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_HANDOFFBATCH_ROUTES);
				ps.setString(1, batchId);
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
							
						IHandOffBatchRoute infoModel = new HandOffBatchRoute();
						result.add(infoModel);
						
						infoModel.setRouteId(rs.getString("ROUTE_NO"));
						Array array = rs.getArray("ROUTING_ROUTE_NO");
						if(array != null) {							 							
							infoModel.setRoutingRouteId(new ArrayList<String>(Arrays.asList((String[]) array.getArray())));
						}
						infoModel.setArea(rs.getString("AREA"));
						infoModel.setSessionName(rs.getString("SESSION_NAME"));
						infoModel.setStartTime(rs.getTimestamp("STARTTIME"));
						infoModel.setCompletionTime(rs.getTimestamp("COMPLETETIME"));
						infoModel.setDistance(rs.getDouble("DISTANCE"));
						infoModel.setTravelTime(rs.getDouble("TRAVELTIME"));
						infoModel.setServiceTime(rs.getDouble("SERVICETIME"));
						
						infoModel.setDispatchTime(new RoutingTimeOfDay(rs.getTimestamp("DISPATCHTIME")));
						infoModel.setDispatchSequence(rs.getInt("DISPATCHSEQUENCE"));
						infoModel.setTrailerId(rs.getString("TRAILER_NO"));
						infoModel.setOriginId(rs.getString("ORIGIN_ID"));
						infoModel.setDepotParkingLocation(rs.getString("DEPOT_PARKLOCATION"));
					} while(rs.next());		        		    	
				}
		}
		);
		return result;
	}
	
	public List<IHandOffBatchStop> getHandOffBatchStops(final String batchId, final boolean filterException) throws SQLException {

		final List<IHandOffBatchStop> result = new ArrayList<IHandOffBatchStop>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_HANDOFFBATCH_STOPS);
				ps.setString(1, batchId);
				ps.setString(2, batchId);
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
							
						IHandOffBatchStop infoModel = new HandOffBatchStop();
						
						infoModel.setOrderNumber(rs.getString("WEBORDER_ID"));
						infoModel.setErpOrderNumber(rs.getString("ERPORDER_ID"));
						infoModel.setRouteId(rs.getString("ROUTE_NO"));
						infoModel.setRoutingRouteId(rs.getString("ROUTING_ROUTE_NO"));
						infoModel.setStopNo(rs.getInt("STOP_SEQUENCE"));
						infoModel.setStopArrivalTime(rs.getTimestamp("STOP_ARRIVALDATETIME"));
						infoModel.setStopDepartureTime(rs.getTimestamp("STOP_DEPARTUREDATETIME"));
						infoModel.setException("X".equalsIgnoreCase(rs.getString("IS_EXCEPTION")));
						infoModel.setServiceAddress2(rs.getString("SERVICE_ADDR2"));
						infoModel.setDynamic("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")));
						
						
						IDeliveryModel deliveryModel = new DeliveryModel();
						deliveryModel.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
						deliveryModel.setDeliveryStartTime(rs.getTimestamp("WINDOW_STARTTIME"));
						deliveryModel.setDeliveryEndTime(rs.getTimestamp("WINDOW_ENDTIME"));
						deliveryModel.setRoutingStartTime(rs.getTimestamp("ROUTING_STARTTIME"));
						deliveryModel.setRoutingEndTime(rs.getTimestamp("ROUTING_ENDTIME"));
						
						if("X".equalsIgnoreCase(rs.getString("MANIFEST_ETA_ENABLED"))) {
							deliveryModel.setDeliveryETAStartTime(rs.getTimestamp("DLV_ETA_STARTTIME"));
							deliveryModel.setDeliveryETAEndTime(rs.getTimestamp("DLV_ETA_ENDTIME"));
						}
						
						deliveryModel.setServiceType(rs.getString("DELIVERY_TYPE"));
						deliveryModel.setDeliveryModel(rs.getString("DELIVERYMODEL"));
						deliveryModel.setCalculatedOrderSize(rs.getDouble("ORDERSIZE_SF"));						
						
						IZoneModel zoneModel = new ZoneModel();
						zoneModel.setZoneNumber(rs.getString("AREA"));
						deliveryModel.setDeliveryZone(zoneModel);
						
						IBuildingModel bmodel = new BuildingModel();		
						
						bmodel.setSrubbedStreet(rs.getString("bSCRUBBED_STREET"));
						bmodel.setStreetAddress1(rs.getString("bSCRUBBED_STREET"));						
						bmodel.setCity(rs.getString("bCITY"));		
						bmodel.setState(rs.getString("bSTATE"));					
						bmodel.setZipCode(rs.getString("bZIP"));
						bmodel.setCountry(rs.getString("bCOUNTRY"));
						
						IGeographicLocation geoLoc = new GeographicLocation();
						geoLoc.setLatitude(rs.getString("BLAT"));
						geoLoc.setLongitude(rs.getString("BLONG"));
																	
						bmodel.setGeographicLocation(geoLoc);
					
						bmodel.setAddrType(rs.getString("ADDR_TYPE"));
						bmodel.setCompanyName(rs.getString("COMPANY_NAME"));
						bmodel.setSvcScrubbedStreet(rs.getString("SVC_SCRUBBED_STREET"));
						bmodel.setSvcCrossStreet(rs.getString("SVC_CROSS_STREET"));
						bmodel.setSvcCity(rs.getString("SVC_CITY"));
						bmodel.setSvcState(rs.getString("SVC_STATE"));
						bmodel.setSvcZip(rs.getString("SVC_ZIP"));
						
						bmodel.setDoorman(getBoolean(rs.getString("IS_DOORMAN")));
						bmodel.setWalkup(getBoolean(rs.getString("IS_WALKUP")));
						bmodel.setElevator(getBoolean(rs.getString("IS_ELEVATOR")));
						bmodel.setSvcEnt(getBoolean(rs.getString("SVC_ENT")));
						bmodel.setHouse(getBoolean(rs.getString("IS_HOUSE")));
						bmodel.setFreightElevator(getBoolean(rs.getString("IS_FREIGHT_ELEVATOR")));
						bmodel.setHandTruckAllowed(getBoolean(rs.getString("HAND_TRUCK_ALLOWED")));
						bmodel.setDifficultToDeliver(getBoolean(rs.getString("DIFFICULT_TO_DELIVER")));
						
						bmodel.setWalkUpFloors(rs.getInt("WALK_UP_FLOORS"));
						bmodel.setOther(rs.getString("OTHER"));
						bmodel.setDifficultReason(rs.getString("DIFFICULT_REASON"));
						
						bmodel.setCrossStreet(rs.getString("CROSS_STREET"));
						if(rs.getString("DAY_OF_WEEK") != null) {
							IBuildingOperationDetails operationDetail = new BuildingOperationDetails();
							operationDetail.setDayOfWeek(rs.getString("DAY_OF_WEEK"));
							operationDetail.setBldgStartHour(rs.getTimestamp("BLDG_START_HOUR"));
							operationDetail.setBldgEndHour(rs.getTimestamp("BLDG_END_HOUR"));
							operationDetail.setServiceStartHour(rs.getTimestamp("SERVICE_START_HOUR"));
							operationDetail.setServiceEndHour(rs.getTimestamp("SERVICE_END_HOUR"));
							operationDetail.setBldgComments(rs.getString("BLDG_COMMENTS"));
							operationDetail.setServiceComments(rs.getString("SERVICE_COMMENTS"));
							
							Set<IBuildingOperationDetails> operationDetails = new HashSet<IBuildingOperationDetails>();
							operationDetails.add(operationDetail);
							bmodel.setOperationDetails(operationDetails);
						}
						
						ILocationModel locationModel = new LocationModel(bmodel);
						locationModel.setApartmentNumber(rs.getString("LOCAPART"));
												
						deliveryModel.setDeliveryLocation(locationModel);
						infoModel.setDeliveryInfo(deliveryModel);
						if(!infoModel.isException() || !filterException) {
							result.add(infoModel);
						}
					} while(rs.next());		        		    	
				}
		}
		);
		return result;
	}
	
	public IHandOffBatchRoute getHandOffBatchStopsByRoute(final Date deliveryDate, final String routeId, final boolean filterException) throws SQLException {
		final List<IHandOffBatchRoute> result = new ArrayList<IHandOffBatchRoute>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_HANDOFFBATCH_STOPBYROUTE);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(2, routeId);
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				@SuppressWarnings("unchecked")
				public void processRow(ResultSet rs) throws SQLException {	
					IHandOffBatchRoute routeModel = null;
					
					do { 
						if(routeModel == null) {
							routeModel = new HandOffBatchRoute();
							
							routeModel.setRouteId(rs.getString("RROUTE_NO"));
							Array array = rs.getArray("RN_ROUTE_ID");
							if(array != null) {															
								routeModel.setRoadNetRouteIds(new ArrayList<String>(Arrays.asList((String[]) array.getArray())));
							}
							
							array = rs.getArray("RROUTING_ROUTE_NO");
							if(array != null) {															
								routeModel.setRoutingRouteId(new ArrayList<String>(Arrays.asList((String[]) array.getArray())));
							}
							
							//routeModel.setRoutingRouteId(rs.getString("RROUTING_ROUTE_NO"));
							routeModel.setArea(rs.getString("RAREA"));
							routeModel.setSessionName(rs.getString("SESSION_NAME"));
							routeModel.setStartTime(rs.getTimestamp("RSTARTTIME"));
							routeModel.setCompletionTime(rs.getTimestamp("RCOMPLETETIME"));
							routeModel.setDistance(rs.getDouble("RDISTANCE"));
							routeModel.setTravelTime(rs.getDouble("RTRAVELTIME"));
							routeModel.setServiceTime(rs.getDouble("RSERVICETIME"));
							routeModel.setStops(new TreeSet());
							result.add(routeModel);
						}
						IHandOffBatchStop infoModel = new HandOffBatchStop();
						
						infoModel.setOrderNumber(rs.getString("WEBORDER_ID"));
						infoModel.setErpOrderNumber(rs.getString("ERPORDER_ID"));
						infoModel.setRouteId(rs.getString("ROUTE_NO"));
						infoModel.setRoutingRouteId(rs.getString("ROUTING_ROUTE_NO"));
						infoModel.setStopNo(rs.getInt("STOP_SEQUENCE"));
						infoModel.setStopArrivalTime(rs.getTimestamp("STOP_ARRIVALDATETIME"));
						infoModel.setStopDepartureTime(rs.getTimestamp("STOP_DEPARTUREDATETIME"));
						infoModel.setException("X".equalsIgnoreCase(rs.getString("IS_EXCEPTION")));
						infoModel.setDynamic("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")));
						
						IDeliveryModel deliveryModel = new DeliveryModel();
						deliveryModel.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
						deliveryModel.setDeliveryStartTime(rs.getTimestamp("WINDOW_STARTTIME"));
						deliveryModel.setDeliveryEndTime(rs.getTimestamp("WINDOW_ENDTIME"));
						deliveryModel.setRoutingStartTime(rs.getTimestamp("ROUTING_STARTTIME"));
						deliveryModel.setRoutingEndTime(rs.getTimestamp("ROUTING_ENDTIME"));
						
						if("X".equalsIgnoreCase(rs.getString("MANIFEST_ETA_ENABLED"))) {
							deliveryModel.setDeliveryETAStartTime(rs.getTimestamp("DLV_ETA_STARTTIME"));
							deliveryModel.setDeliveryETAEndTime(rs.getTimestamp("DLV_ETA_ENDTIME"));
						}
						
						IBuildingModel bmodel = new BuildingModel();		
						
						bmodel.setSrubbedStreet(rs.getString("bSCRUBBED_STREET"));
						bmodel.setStreetAddress1(rs.getString("bSCRUBBED_STREET"));						
						bmodel.setCity(rs.getString("bCITY"));		
						bmodel.setState(rs.getString("bSTATE"));					
						bmodel.setZipCode(rs.getString("bZIP"));
						bmodel.setCountry(rs.getString("bCOUNTRY"));
						
						IGeographicLocation geoLoc = new GeographicLocation();
						geoLoc.setLatitude(rs.getString("BLAT"));
						geoLoc.setLongitude(rs.getString("BLONG"));
																	
						bmodel.setGeographicLocation(geoLoc);
																	
						ILocationModel locationModel = new LocationModel(bmodel);
						locationModel.setApartmentNumber(rs.getString("LOCAPART"));
												
						deliveryModel.setDeliveryLocation(locationModel);
						infoModel.setDeliveryInfo(deliveryModel);
						if(!infoModel.isException() || filterException) {
							routeModel.getStops().add(infoModel);
						}
					} while(rs.next());		        		    	
				}
		}
		);
		return result.size() > 0 ? result.get(0) : null;
	}
	
	
	
	public void clearHandOffBatchStopsRoute(String handOffBatchId) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_STOPROUTE, new Object[] {handOffBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void clearHandOffBatchDepotSchedule(String handOffBatchId) throws SQLException {
		
		Connection connection = null;		
		try {			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_DEPOTSCHEDULE, new Object[] {handOffBatchId});			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void clearHandOffBatchDepotScheduleEx(String dayOfWeek, Date cutOffTime) throws SQLException {
		
		Connection connection = null;		
		try {			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_DEPOTSCHEDULE_EX, new Object[] {dayOfWeek, cutOffTime});			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	

	public void updateHandOffBatchStopRoute(List<IHandOffBatchStop> dataList) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),UPDATE_HANDOFFBATCH_STOP);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.INTEGER));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(IHandOffBatchStop model : dataList) {
				//System.out.println("ORDER --->"+model.getBatchId()+"-"+model.getSessionName()+"-"+model.getRouteId()+"-"+model.getOrderNumber());
				batchUpdater.update(new Object[]{ model.getRouteId()
											, model.getRoutingRouteId()
											, model.getSessionName()
											, model.getStopNo()
											, model.getStopArrivalTime()
											, model.getStopDepartureTime()
											, model.getTravelTime()
											, model.getServiceTime()
											, model.getOrderSize()
											, model.getDeliveryInfo() != null ? model.getDeliveryInfo().getDeliveryETAStartTime() : null
											, model.getDeliveryInfo() != null ? model.getDeliveryInfo().getDeliveryETAEndTime() : null
											, model.getBatchId()
											, model.getOrderNumber()
											
									});
			}			
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public void updateHandOffBatchStopErpNo(List<IHandOffBatchStop> dataList) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),UPDATE_HANDOFFBATCH_STOP_ERPNO);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(IHandOffBatchStop model : dataList) {
				
				batchUpdater.update(new Object[]{ model.getErpOrderNumber()
						, model.getBatchId()
						, model.getOrderNumber()
				});
			}			
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	
	
	public void addNewHandOffBatchRoutes(List<IHandOffBatchRoute> dataList) throws SQLException {
		Connection connection = null;
		try{
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_HANDOFFBATCH_ROUTE);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.ARRAY));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.ARRAY));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			ArrayDescriptor desc = ArrayDescriptor.createDescriptor("TRANSP.HANDOFF_ROUTING_ROUTE_NO", connection);
			ARRAY routingRouteId = null;
			ArrayDescriptor rndesc = ArrayDescriptor.createDescriptor("TRANSP.HANDOFF_ROADNET_ROUTE_NO", connection);
			ARRAY roadNetRouteIds = null;
			for(IHandOffBatchRoute model : dataList) {
				if(model.getRoutingRouteId() != null) {					
					routingRouteId = new ARRAY(desc, connection, RoutingUtil.toStringArray(model.getRoutingRouteId()));
				}
				if(model.getRoadNetRouteIds() != null) {					
					roadNetRouteIds = new ARRAY(rndesc, connection, RoutingUtil.toStringArray(model.getRoadNetRouteIds()));
				}
				
				//System.out.println("ROUTE --->"+model.getBatchId()+"-"+model.getSessionName()+"-"+model.getRouteId());
				batchUpdater.update(new Object[]{ model.getBatchId()
											, model.getSessionName()
											, model.getRouteId()
											, routingRouteId
											, model.getArea()
											, model.getStartTime()
											, model.getCompletionTime()
											, model.getDistance()
											, model.getTravelTime()
											, model.getServiceTime()
											, model.getDispatchTime().getAsDate()
											, model.getDispatchSequence()
											, roadNetRouteIds
											, model.getOriginId()
									});
			}			
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public void addNewHandOffBatchDispatches(String handOffBatchId, Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus) throws SQLException {
		Connection connection = null;
		try{
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_HANDOFFBATCH_DISPATCH);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			

			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			if(dispatchStatus != null) {			
				for(Map.Entry<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispMpp : dispatchStatus.entrySet()) {
				
					batchUpdater.update(new Object[]{ handOffBatchId
												, new Timestamp(dispMpp.getKey().getAsDate().getTime())
												, 0
												, 0
												, dispMpp.getValue().value()												
										});
				}
			}
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public void clearHandOffBatchSession(String handOffBatchId) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_SESSION, new Object[] {handOffBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void clearHandOffBatchStops(String handOffBatchId) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_STOPS, new Object[] {handOffBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}

	public void clearHandOffBatchRoutes(String handOffBatchId) throws SQLException {
	
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_ROUTES, new Object[] {handOffBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void clearHandOffBatchRouteBreaks(String handOffBatchId) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_ROUTEBREAKS, new Object[] {handOffBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}	
	
	
	public void clearHandOffBatchTrailers(String handOffBatchId) throws SQLException {

		Connection connection = null;
		try {
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_TRAILERS, new Object[] {handOffBatchId});

			connection=this.jdbcTemplate.getDataSource().getConnection();	

		} finally {
			if(connection!=null) connection.close();
		}
	}

	public void clearHandOffBatchDispatches(String handOffBatchId) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_DISPATCHES, new Object[] {handOffBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}

	public Map<String, Integer> getHandOffBatchRouteCnt(final Date deliveryDate) throws SQLException {
		
		final Map<String, Integer> batchMapping = new HashMap<String, Integer>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_HANDOFFBATCH_ROUTECNT);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						batchMapping.put(rs.getString("area"), rs.getInt("NOOFROUTES"));
					} while(rs.next());		        		    	
				}
		}
		);
		
		return batchMapping;
	}
	
	public Map<RoutingTimeOfDay, Integer> getHandOffBatchDispatchCnt(final Date deliveryDate) throws SQLException {
		
		final Map<RoutingTimeOfDay, Integer> batchMapping = new HashMap<RoutingTimeOfDay, Integer>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_HANDOFFBATCH_DISPATCHCNT);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						batchMapping.put(new RoutingTimeOfDay(rs.getTimestamp("DISPATCHTIME")), rs.getInt("CURRDISPATCHSEQ"));
					} while(rs.next());		        		    	
				}
		}
		);
		
		return batchMapping;
	}
	
	
	@SuppressWarnings("unchecked")
	public IHandOffBatch getHandOffBatchById(final String batchId) throws SQLException {

		final Map<String, IHandOffBatch> batchMapping = new HashMap<String, IHandOffBatch>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_HANDOFFBATCH_BYID);
				ps.setString(1, batchId);
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						processBatchFromResultset(rs, batchMapping);
					} while(rs.next());		        		    	
				}
		}
		);
		Collection result = batchMapping.values();
		if(result != null && result.size() > 0) {
			return (IHandOffBatch)result.iterator().next();
		}
		return null;
	}
	
	public Set<IHandOffBatch> getHandOffBatch(final Date deliveryDate) throws SQLException {

		final Set<IHandOffBatch> result = new HashSet<IHandOffBatch>();
		final Map<String, IHandOffBatch> batchMapping = new HashMap<String, IHandOffBatch>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				if(deliveryDate == null) {
					ps = connection.prepareStatement(GET_HANDOFFBATCH_DATERANGE);
					ps.setInt(1, 7);
				} else {
					ps = connection.prepareStatement(GET_HANDOFFBATCH_DELIVERYDATE);
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				}
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						processBatchFromResultset(rs, batchMapping);
					} while(rs.next());		        		    	
				}
		}
		);
		result.addAll(batchMapping.values());
		return result;
	}
	
	private void processBatchFromResultset(ResultSet rs, Map<String, IHandOffBatch> batchMapping) throws SQLException {
		
		String _batchId = rs.getString("BATCH_ID");
		String _sessionName = rs.getString("SESSION_NAME");
		String _actionBy = rs.getString("ACTION_BY");
		String _depotArea = rs.getString("DEPOT_AREA");
		int noOfOrders = rs.getInt("STOPCOUNT");
				
		if(!batchMapping.containsKey(_batchId)) {
			
			IHandOffBatch _batch = new HandOffBatch();
			_batch.setBatchId(_batchId);
			_batch.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
			_batch.setServiceTimeScenario(rs.getString("SCENARIO"));
			_batch.setCutOffDateTime(rs.getTimestamp("CUTOFF_DATETIME"));			
			_batch.setStatus(EnumHandOffBatchStatus.getEnum(rs.getString("BATCH_STATUS")));
			_batch.setSystemMessage(rs.getString("SYS_MESSAGE"));
			_batch.setEligibleForCommit("X".equalsIgnoreCase(rs.getString("IS_COMMIT_ELIGIBLE")));
			_batch.setNoOfOrders(noOfOrders);
			
			_batch.setAction(new TreeSet<IHandOffBatchAction>());
			_batch.setSession(new TreeSet<IHandOffBatchSession>());
			_batch.setDepotSchedule(new TreeSet<IHandOffBatchDepotSchedule>());
			batchMapping.put(_batchId, _batch);
		}
		
		if(_actionBy != null) {
			IHandOffBatchAction action = new HandOffBatchAction();
			action.setActionBy(_actionBy);
			action.setActionDateTime(rs.getTimestamp("ACTION_DATETIME"));
			action.setActionType(EnumHandOffBatchActionType.getEnum(rs.getString("ACTION_TYPE")));
			action.setBatchId(_batchId);
			batchMapping.get(_batchId).getAction().add(action);
		}
		
		if(_sessionName != null) {
			
			IHandOffBatchSession session = new HandOffBatchSession();
			session.setBatchId(_batchId);
			session.setRegion(rs.getString("REGION"));
			session.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
			session.setSessionName(_sessionName);
			
			batchMapping.get(_batchId).getSession().add(session);
		}	

		if(_depotArea != null) {
			
			IHandOffBatchDepotSchedule depotSchedule = new HandOffBatchDepotSchedule();
			depotSchedule.setBatchId(_batchId);
			depotSchedule.setArea(_depotArea);
			depotSchedule.setDepotArrivalTime(rs.getTimestamp("DEPOTARRIVALTIME"));
			depotSchedule.setTruckDepartureTime(rs.getTimestamp("TRUCKDEPARTURETIME"));
			depotSchedule.setOriginId(rs.getString("ORIGIN_ID"));
			batchMapping.get(_batchId).getDepotSchedule().add(depotSchedule);
		}		
	}
	
	public void addNewHandOffBatchStops(List<IHandOffBatchStop> dataList) throws SQLException {
		Connection connection = null;
		if(dataList != null && dataList.size() > 0) {
			try{
				BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_HANDOFFBATCH_STOP);
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.compile();
	
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				for(IHandOffBatchStop model : dataList) {
					batchUpdater.update(new Object[]{ model.getBatchId()
												, model.getOrderNumber()
												, model.getErpOrderNumber()
												, model.getDeliveryInfo().getDeliveryZone().getArea().getAreaCode()
												, model.getDeliveryInfo().getServiceType()
												, model.getDeliveryInfo().getDeliveryStartTime()
												, model.getDeliveryInfo().getDeliveryEndTime()
												, model.getDeliveryInfo().getRoutingStartTime()
												, model.getDeliveryInfo().getRoutingEndTime()
												, model.getDeliveryInfo().getDeliveryLocation().getLocationId()
												, model.getSessionName()
												, model.getRouteId()
												, model.getRoutingRouteId()
												, model.getStopArrivalTime()
												, model.getStopDepartureTime()
												, model.getTravelTime()
												, model.getServiceTime()
												, model.getDeliveryInfo().getDeliveryLocation().getBuilding().getStreetAddress2()
												, model.getDeliveryInfo().getCalculatedOrderSize()
												, model.getMobileNumber()
												, model.isDynamic()?"X":null
										});
				}			
				batchUpdater.flush();
			}finally{
				if(connection!=null) connection.close();
			}
		}
	}
	
	public void addNewHandOffBatchDepotSchedules(Set<IHandOffBatchDepotSchedule> dataList) throws SQLException {
		Connection connection = null;
		if(dataList != null && dataList.size() > 0) {
			try{
				BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_HANDOFFBATCH_DEPOTSCHEDULE);
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				
				batchUpdater.compile();
	
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				for(IHandOffBatchDepotSchedule model : dataList) {
					batchUpdater.update(new Object[]{ model.getBatchId()
												, model.getArea()
												, model.getDepotArrivalTime()
												, model.getTruckDepartureTime()
												, model.getOriginId()
										});
				}			
				batchUpdater.flush();
			}finally{
				if(connection!=null) connection.close();
			}
		}
	}
	
	public void addNewHandOffBatchDepotSchedulesEx(Set<IHandOffBatchDepotScheduleEx> dataList) throws SQLException {
		Connection connection = null;
		if(dataList != null && dataList.size() > 0) {
			try{
				BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_HANDOFFBATCH_DEPOTSCHEDULE_EX);
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				
				batchUpdater.compile();
	
				
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				for(IHandOffBatchDepotScheduleEx model : dataList) {
					batchUpdater.update(new Object[]{ model.getDayOfWeek()
												, model.getCutOffDateTime()
												, model.getArea()
												, model.getDepotArrivalTime()
												, model.getTruckDepartureTime()
												, model.getOriginId()
										});
				}			
				batchUpdater.flush();
			}finally{
				if(connection!=null) connection.close();
			}
		}
	}
	
	public void addNewHandOffBatchSession(String handOffBatchId, String sessionName, String region) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(INSERT_HANDOFFBATCH_SESSION
											, new Object[] {handOffBatchId, sessionName, region});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}

	public void updateHandOffBatchMessage(String handOffBatchId, String message) throws SQLException {
		
		Connection connection = null;		
		try {
			if(message != null) {
				message = message.substring(0 , Math.min(message.length(), 1024));
			}
			this.jdbcTemplate.update(UPDATE_HANDOFFBATCH_MESSAGE
											, new Object[] {message, handOffBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void updateHandOffBatchStatus(String handOffBatchId, EnumHandOffBatchStatus status) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(UPDATE_HANDOFFBATCH_STATUS
											, new Object[] {status.value(), handOffBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void updateHandOffBatchCommitEligibility(String handOffBatchId, boolean isEligibleForCommit) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(UPDATE_HANDOFFBATCH_COMMITELIGIBLE
											, new Object[] {isEligibleForCommit ? "X" : null, handOffBatchId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public Map<EnumSaleStatus, Integer> getOrderStatsByCutoff(final Date deliveryDate, final Date cutOff) throws SQLException {

		final Map<EnumSaleStatus, Integer> result = new HashMap<EnumSaleStatus, Integer>();
		
		final StringBuffer cutoffQuery = new StringBuffer();final StringBuffer cutoffsbyQuery = new StringBuffer();
		
		cutoffQuery.append(GET_ORDERSTATSBY_DATE_CUTOFF).append(" and ((ts.premium_cutoff_time is not null and to_char(ts.premium_cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM')) or (ts.premium_cutoff_time is null and to_char(di.cutofftime, 'HH:MI AM')=  to_char(?, 'HH:MI AM'))) group by s.status");
		cutoffsbyQuery.append(GET_ORDERSTATSBY_DATE_CUTOFFSTANDBY).append(" and ((ts.premium_cutoff_time is not null and to_char(ts.premium_cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM')) or (ts.premium_cutoff_time is null and to_char(di.cutofftime, 'HH:MI AM')=  to_char(?, 'HH:MI AM'))) group by s.status");
		
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {	
				String query = cutoffQuery.toString();
				if(RoutingServicesProperties.getRoutingCutOffStandByEnabled()) {
					query = cutoffsbyQuery.toString();
				}
				PreparedStatement ps =
					connection.prepareStatement(query);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new Timestamp(cutOff.getTime()));
				ps.setTimestamp(3, new Timestamp(cutOff.getTime()));
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						result.put(EnumSaleStatus.getSaleStatus(rs.getString("status")), rs.getInt("order_count"));						
	
					} while(rs.next());		        		    	
				}
		}
		);
		return result;
	}
	
	public List<IHandOffBatchStop> getOrderByCutoff(final Date deliveryDate, final Date cutOff) throws SQLException {

		final List<IHandOffBatchStop> result = new ArrayList<IHandOffBatchStop>();
		final StringBuffer cutoffQuery = new StringBuffer();final StringBuffer cutoffsbyQuery = new StringBuffer();
		
		cutoffQuery.append(GET_ORDERSBY_DATE_CUTOFF).append(" and ((ts.premium_cutoff_time is not null and to_char(ts.premium_cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM')) or (ts.premium_cutoff_time is null and to_char(di.cutofftime, 'HH:MI AM')=  to_char(?, 'HH:MI AM'))) ");
		cutoffsbyQuery.append(GET_ORDERSBY_DATE_CUTOFFSTANDBY).append(" and ((ts.premium_cutoff_time is not null and to_char(ts.premium_cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM')) or (ts.premium_cutoff_time is null and to_char(di.cutofftime, 'HH:MI AM')=  to_char(?, 'HH:MI AM'))) ");
	
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {	
				String query = cutoffQuery.toString();
				if(RoutingServicesProperties.getRoutingCutOffStandByEnabled()) {
					query = cutoffsbyQuery.toString();
				}
				PreparedStatement ps =
					connection.prepareStatement(query);
				
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new Timestamp(cutOff.getTime()));
				ps.setTimestamp(3, new Timestamp(cutOff.getTime()));
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
							
						IHandOffBatchStop infoModel = new HandOffBatchStop();
						result.add(infoModel);
						infoModel.setOrderNumber(rs.getString("weborder_id"));
						infoModel.setErpOrderNumber(rs.getString("erporder_id"));
						infoModel.setCustomerNumber(rs.getString("customer_id"));
						infoModel.setStatus(EnumSaleStatus.getSaleStatus(rs.getString("status")));
						infoModel.setMobileNumber(rs.getString("mobile_number"));
						//infoModel.setCustomerName(rs.getString("customer_id"));
						infoModel.setDynamic("X".equalsIgnoreCase(rs.getString("is_dynamic")));
						
						IDeliveryModel deliveryModel = new DeliveryModel();
						deliveryModel.setDeliveryDate(deliveryDate);
						deliveryModel.setDeliveryStartTime(new Date(rs.getTimestamp("STARTTIME").getTime()));
						deliveryModel.setDeliveryEndTime(new Date(rs.getTimestamp("ENDTIME").getTime()));
						deliveryModel.setRoutingStartTime((rs.getTimestamp("ROUTING_START_TIME")!=null)?RoutingDateUtil.getNormalDate(deliveryDate, rs.getTimestamp("ROUTING_START_TIME")):deliveryModel.getDeliveryStartTime());
						deliveryModel.setRoutingEndTime((rs.getTimestamp("ROUTING_END_TIME")!=null)?RoutingDateUtil.getNormalDate(deliveryDate, rs.getTimestamp("ROUTING_END_TIME")):deliveryModel.getDeliveryEndTime());
						deliveryModel.setServiceType(rs.getString("DELIVERY_TYPE"));
						//deliveryModel.setDeliveryModel(tmpInputModel.getDeliveryModel());
						
						IPackagingModel tmpPackageModel = new PackagingModel();
						tmpPackageModel.setNoOfCartons(rs.getLong("NUM_CARTONS"));
						tmpPackageModel.setNoOfCases(rs.getLong("NUM_CASES"));
						tmpPackageModel.setNoOfFreezers(rs.getLong("NUM_FREEZERS"));
						deliveryModel.setPackagingDetail(tmpPackageModel);
						
						IZoneModel zoneModel = new ZoneModel();
						zoneModel.setZoneNumber(rs.getString("ZONE"));
						deliveryModel.setDeliveryZone(zoneModel);
						
						IBuildingModel bmodel = new BuildingModel();		
												
						bmodel.setStreetAddress1(rs.getString("ADDRESS1"));
						bmodel.setStreetAddress2(rs.getString("ADDRESS2"));						
						bmodel.setCity(rs.getString("CITY"));		
						bmodel.setState(rs.getString("STATE"));					
						bmodel.setZipCode(rs.getString("ZIP"));
						bmodel.setCountry(rs.getString("COUNTRY"));
						
						ILocationModel locationModel = new LocationModel(bmodel);
						locationModel.setApartmentNumber(rs.getString("APARTMENT"));
						deliveryModel.setReservationId(rs.getString("id"));
						
						deliveryModel.setDeliveryLocation(locationModel);
						infoModel.setDeliveryInfo(deliveryModel);
						
						
	
					} while(rs.next());		        		    	
				}
		}
		);
		return result;
	}
	
	public String getNewHandOffBatchId() throws SQLException {
		return ""+jdbcTemplate.queryForLong(GET_HANDOFFBATCHNEXTSEQ_QRY);
	}
	
	public String getNewHandOffAutoDispatchId() throws SQLException {
		return ""+jdbcTemplate.queryForLong(GET_HANDOFFBATCHDISPATCHSEQ_QRY);
	}
		
	public String addNewHandOffBatch(Date deliveryDate, String scenario, Date cutOffDateTime, boolean isStandByMode) throws SQLException {
			
		Connection connection=null;
		String handOffBatchId = null;
		try {
			handOffBatchId = this.getNewHandOffBatchId();
			String isCommitEligible = (isStandByMode ? null : "X");
			this.jdbcTemplate.update(INSERT_HANDOFFBATCH
											, new Object[] {handOffBatchId
											, deliveryDate
											, EnumHandOffBatchStatus.NEW.value()
											, "New Batch Created"
											, scenario
											, new Timestamp(cutOffDateTime.getTime())
											, isCommitEligible});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
		return handOffBatchId;
	}
	
	public void addNewHandOffBatchAction(String handOffBatchId, Date actionDateTime
												, EnumHandOffBatchActionType actionType
												, String userId) throws SQLException {
		
		Connection connection=null;
		
		try {
			
			this.jdbcTemplate.update(INSERT_HANDOFFBATCHACTION
											, new Object[] {handOffBatchId
											, new Timestamp(actionDateTime.getTime())
											, actionType.value()
											, userId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void updateHandOffStopException(String handOffBatchId, List<String> exceptionOrderIds) throws SQLException {
		
		Connection connection = null;		
		try {
			if(exceptionOrderIds != null && exceptionOrderIds.size() > 0) {
				StringBuffer updateQ = new StringBuffer();
				updateQ.append(UPDATE_HANDOFFBATCH_STOPEXCEPTION);
				int intCount = 0;
				for(String expOrder : exceptionOrderIds) {
					updateQ.append("'").append(expOrder).append("'");
					intCount++;
					if(intCount != exceptionOrderIds.size()) {
						updateQ.append(",");
					}
				}
				updateQ.append(")");
				this.jdbcTemplate.update(updateQ.toString()
												, new Object[] {handOffBatchId});
				
				connection=this.jdbcTemplate.getDataSource().getConnection();
			}
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public void clearHandOffStopException(String handOffBatchId) throws SQLException {
		
		Connection connection = null;		
		try {
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_STOPEXCEPTION
					, new Object[] {handOffBatchId});

			connection=this.jdbcTemplate.getDataSource().getConnection();
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public Set<IHandOffBatchDepotScheduleEx> getHandOffBatchDepotSchedulesEx(final String dayOfWeek, final Date cutOffTime) throws SQLException {

		final Set<IHandOffBatchDepotScheduleEx> result = new HashSet<IHandOffBatchDepotScheduleEx>();
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_HANDOFFBATCH_DEPOTSCHEDULE_EX);
				ps.setString(1, dayOfWeek);
				ps.setTimestamp(2, new java.sql.Timestamp(cutOffTime.getTime()));
								
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						IHandOffBatchDepotScheduleEx depotSchedule = new HandOffBatchDepotScheduleEx();
						depotSchedule.setDayOfWeek(rs.getString("DAY_OF_WEEK"));
						depotSchedule.setCutOffDateTime(rs.getTimestamp("CUTOFF_DATETIME"));
						depotSchedule.setArea(rs.getString("AREA"));
						depotSchedule.setDepotArrivalTime(rs.getTimestamp("DEPOTARRIVALTIME"));
						depotSchedule.setTruckDepartureTime(rs.getTimestamp("TRUCKDEPARTURETIME"));
						depotSchedule.setOriginId(rs.getString("ORIGIN_ID"));
						result.add(depotSchedule);
					} while(rs.next());		        		    	
				}
		}
		);
		
		return result;
	}
	
	public Map<String, Map<RoutingTimeOfDay, Set<IHandOffBatchDepotScheduleEx>>> getHandOffBatchDepotSchedulesEx(final String dayOfWeek) throws SQLException {

		final Map<String, Map<RoutingTimeOfDay, Set<IHandOffBatchDepotScheduleEx>>> result = new HashMap<String, Map<RoutingTimeOfDay, Set<IHandOffBatchDepotScheduleEx>>>();
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_HANDOFFBATCH_DEPOTSCHEDULE_DAYOFWEEK);
				ps.setString(1, dayOfWeek);
							
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						IHandOffBatchDepotScheduleEx depotSchedule = new HandOffBatchDepotScheduleEx();
						depotSchedule.setDayOfWeek(rs.getString("DAY_OF_WEEK"));
						depotSchedule.setCutOffDateTime(rs.getTimestamp("CUTOFF_DATETIME"));
						depotSchedule.setArea(rs.getString("AREA"));
						depotSchedule.setDepotArrivalTime(rs.getTimestamp("DEPOTARRIVALTIME"));
						depotSchedule.setTruckDepartureTime(rs.getTimestamp("TRUCKDEPARTURETIME"));
						depotSchedule.setOriginId(rs.getString("ORIGIN_ID"));
						
						depotSchedule.setDepotArrivalTime(DateUtil.getServerTime(depotSchedule.getDepotArrivalTime()));
						depotSchedule.setTruckDepartureTime(DateUtil.getServerTime(depotSchedule.getTruckDepartureTime()));
						
						RoutingTimeOfDay _cutoffTime = new RoutingTimeOfDay(depotSchedule.getCutOffDateTime());
						if(!result.containsKey(depotSchedule.getArea()))
						{
							result.put(depotSchedule.getArea(), new HashMap<RoutingTimeOfDay, Set<IHandOffBatchDepotScheduleEx>>());
						}
						if(!result.get(depotSchedule.getArea()).containsKey(_cutoffTime))
						{
							result.get(depotSchedule.getArea()).put(_cutoffTime, new HashSet<IHandOffBatchDepotScheduleEx>());
						}
						result.get(depotSchedule.getArea()).get(_cutoffTime).add(depotSchedule);
						
					} while(rs.next());		        		    	
				}
		}
		);
		
		return result;
	}
	
	public List<HandOffDispatchIn> getHandOffBatchDispatches(final String handOffBatchId) throws SQLException {

		final List<HandOffDispatchIn> result = new ArrayList<HandOffDispatchIn>();
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_HANDOFFBATCH_DISPATCHES);
				ps.setString(1, handOffBatchId);
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						HandOffBatchDispatch _dispIn = new HandOffBatchDispatch();
						_dispIn.setDispatchTime(rs.getTimestamp("DISPATCHTIME"));
						_dispIn.setStatus(EnumHandOffDispatchStatus.getEnum(rs.getString("STATUS")));
						result.add(_dispIn);						
					} while(rs.next());		        		    	
				}
		}
		);
		
		return result;
	}
	
	

	public Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> getHandOffBatchDispatchStatus(final String handOffBatchId) throws SQLException {

		final Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> result = new TreeMap<RoutingTimeOfDay, EnumHandOffDispatchStatus>();
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_HANDOFFBATCH_DISPATCHES);
				ps.setString(1, handOffBatchId);
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do {						
						result.put( new RoutingTimeOfDay(rs.getTimestamp("DISPATCHTIME"))
										, EnumHandOffDispatchStatus.getEnum(rs.getString("STATUS")));						
					} while(rs.next());		        		    	
				}
		}
		);
		
		return result;
	}
	
	
	public List<IHandOffBatchRoute> getHandOffBatchDispatchRoutes(final String handoffBatchId, final Date deliveryDate, final Date cutOffDate) throws SQLException {

		final List<IHandOffBatchRoute> result = new ArrayList<IHandOffBatchRoute>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_HANDOFFBATCH_DISPATCHROUTES);			    
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new Timestamp(cutOffDate.getTime()));
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do {							
						IHandOffBatchRoute infoModel = new HandOffBatchRoute();
						result.add(infoModel);
						
						infoModel.setRouteId(rs.getString("ROUTE_NO"));
						infoModel.setArea(rs.getString("AREA"));
						infoModel.setStartTime(rs.getTimestamp("STARTTIME"));
						infoModel.setDispatchTime(new RoutingTimeOfDay(rs.getTimestamp("DISPATCHTIME")));
						infoModel.setFirstDeliveryTime(rs.getTimestamp("FIRSTDLVTIME"));
						infoModel.setCompletionTime(rs.getTimestamp("COMPLETETIME"));
					} while(rs.next());		        		    	
				}
		}
		);
		return result;
	}
	
	public List<IHandOffBatchPlan> getHandOffBatchPlans(final String handoffBatchId, final Date deliveryDate, final Date cutOffDate) throws SQLException {
		final List<IHandOffBatchPlan> result = new ArrayList<IHandOffBatchPlan>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				String query = GET_HANDOFFBATCH_PLANS;
								
				PreparedStatement ps =
					connection.prepareStatement(query);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new Timestamp(cutOffDate.getTime()));
				ps.setDate(3, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(4, handoffBatchId);
				ps.setDate(5, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(6, handoffBatchId);
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {	
										
					do {
						IHandOffBatchPlan planModel = new HandOffBatchPlan();
						result.add(planModel);
						
						planModel.setPlanId(rs.getString("PLAN_ID"));
						planModel.setPlanDate(rs.getTimestamp("PLAN_DATE"));
						planModel.setOriginFacility(rs.getString("ORIGIN_FACILITY"));
						planModel.setDestinationFacility(rs.getString("DESTINATION_FACILITY"));
						planModel.setZoneCode(rs.getString("ZONE"));
						planModel.setRegion(rs.getString("REGION"));
						planModel.setSupervisorId(rs.getString("SUPERVISOR_ID"));
						planModel.setDispatchGroup(rs.getTimestamp("DISPATCH_GROUPTIME"));
						planModel.setDispatchTime(rs.getTimestamp("TRUCK_DISPATCHTIME"));
						planModel.setEndTime(rs.getTimestamp("TRUCK_ENDTIME"));
						planModel.setMaxTime(rs.getTimestamp("MAX_TIME"));
						planModel.setCutOffTime(rs.getTimestamp("CUTOFF_DATETIME"));
						planModel.setSequence(rs.getInt("SEQUENCE"));
						planModel.setIsBullpen(rs.getString("IS_BULLPEN"));
						planModel.setBatchPlanResources(new TreeSet());
						planModel.setIsOpen(rs.getString("IS_OPEN"));
						planModel.setIsTeamOverride("Y".equalsIgnoreCase(rs.getString("ISTEAMOVERRIDE")) ? true : false);
						planModel.setRunnerMax(rs.getInt("RESOURCE_COUNT"));
	
					} while (rs.next());		        		    	
				}
			}
		);
		return result;
	}
	

	public List<IHandOffBatchPlanResource> getHandOffBatchPlanResources(final String handoffBatchId, final Date deliveryDate, final Date cutOffDate) throws SQLException {
		final List<IHandOffBatchPlanResource> result = new ArrayList<IHandOffBatchPlanResource>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				String query = GET_HANDOFFBATCH_PLANRESOURCES;
							
				PreparedStatement ps =
					connection.prepareStatement(query);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new Timestamp(cutOffDate.getTime()));
				ps.setDate(3, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(4, handoffBatchId);
				ps.setDate(5, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(6, handoffBatchId);
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {	
										
					do {					
							IHandOffBatchPlanResource planResourceModel = new HandOffBatchPlanResource();
							result.add(planResourceModel);
							
							planResourceModel.setPlanId(rs.getString("PLAN_ID"));
							planResourceModel.setResourceId(rs.getString("RESOURCE_ID"));						
							planResourceModel.setEmployeeRoleType(rs.getString("ROLE"));
							planResourceModel.setAdjustmentTime(rs.getTimestamp("ADJUSTMENT_TIME"));				
					} while(rs.next());		        		    	
				}
			}
		);
		return result;
	}
	
	public List<Truck> getAvailableTrucksInService(final Date deliveryDate) throws SQLException {
		final List<Truck> result = new ArrayList<Truck>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_HANDOFFBATCH_ASSETTRUCKS);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {			 
					
					do { 
						Truck truckModel = new Truck(rs.getString("ASSET_NO"));						
						truckModel.setInService("ACT".equals(rs.getString("ASSET_STATUS"))? true : false);						
						truckModel.setTrailer("TRAILER".equals(rs.getString("ASSET_TYPE"))? true : false);
						result.add(truckModel);												
					} while(rs.next());		        		    	
				}
			}
		);
		return result;
	}
	
	public List<TruckPreferenceStat> getEmployeeTruckPreferences() throws SQLException {
		final List<TruckPreferenceStat> result = new ArrayList<TruckPreferenceStat>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_HANDOFFBATCH_TRUCKPREFERECES);
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {	
										
					do {	
						TruckPreferenceStat truckPrefModel = new TruckPreferenceStat();
						truckPrefModel.setEmployeeId(rs.getString("KRONOS_ID"));
						truckPrefModel.setTruckId(rs.getString("TRUCK_NUMBER"));
						truckPrefModel.setPrefKey(rs.getString("PREFERENCE_KEY"));
						result.add(truckPrefModel);
					} while (rs.next());		        		    	
				}
			}
		);
		return result;
	}
	
	public void clearHandOffBatchAutoDispatchResources(String handoffBatchId, Date deliveryDate, Date cutOffDate) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_AUTODISPATCHRESOURCES, new Object[] {deliveryDate, new Timestamp(cutOffDate.getTime()), handoffBatchId, deliveryDate, handoffBatchId});
			
			connection = this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
		
	
	public void clearHandOffBatchAutoDispatches(String handoffBatchId, Date deliveryDate, Date cutOffDate) throws SQLException {
		
		Connection connection = null;		
		try {
			
			this.jdbcTemplate.update(CLEAR_HANDOFFBATCH_AUTODISPATCHES, new Object[] {deliveryDate, new Timestamp(cutOffDate.getTime()), handoffBatchId, deliveryDate, handoffBatchId});
			
			connection = this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}

	@SuppressWarnings("unchecked")
	public void addNewHandOffBatchAutoDispatches(Collection dataList) throws SQLException {
		Connection connection = null;
		
		if(dataList != null && dataList.size() > 0) {
			
			try{
				BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_HANDOFFBATCH_AUTODISPATCHES);
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.DATE));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
				batchUpdater.compile();
				
				BatchSqlUpdate batchResourceUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_HANDOFFBATCH_AUTODISPATCHRESOURCES);
				batchResourceUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchResourceUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchResourceUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
				batchResourceUpdater.compile();
	
				connection = this.jdbcTemplate.getDataSource().getConnection();
				
				String handOffDispatchId = null;
				Iterator<IHandOffDispatch> itr = dataList.iterator();
				
				while(itr.hasNext()){
					IHandOffDispatch model = itr.next();
					handOffDispatchId = this.getNewHandOffAutoDispatchId();
					batchUpdater.update(new Object[]{ handOffDispatchId
												, model.getDispatchDate()
												, model.getOriginFacility()
												, model.getDestinationFacility()
												, model.getZone()
												, model.getSupervisorId()
												, model.getRoute()
												, model.getDispatchGroup()
												, model.getDispatchTime()
												, model.getPlanId()
												, model.getIsBullpen()
												, model.getRegion()
												, model.getTruck()
												, model.getCutoffTime()
												, model.getDispatchType()
												, model.getDispatchTime()
										});
						Set<IHandOffDispatchResource> dispatchResources = model.getDispatchResources();
						if(dispatchResources != null){
							for(IHandOffDispatchResource resModel : dispatchResources){
								batchResourceUpdater.update(new Object[]{ handOffDispatchId
															, resModel.getResourceId()
															, resModel.getEmployeeRoleType()
													});
							}
						}
					
				}
				batchUpdater.flush();
				batchResourceUpdater.flush();
			} finally{
				if(connection!=null) connection.close();
			}
		}
	}
	
	public String getLastCommittedHandOffBatch(final Date deliveryDate) throws SQLException  {
		final List<String> result = new ArrayList<String>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
								
				PreparedStatement ps = connection.prepareStatement(GET_HANDOFFBATCH_LASTCOMMITEDBATCH);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
											
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					result.add(rs.getString("BATCH_ID"));					        		    	
				}
		}
		);
		
		return result.size() > 0 ? result.get(0) : null;
	}

	public void addNewHandOffBatchTrailers(List<IHandOffBatchTrailer> dataList) throws SQLException {
		Connection connection = null;
		try{
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_HANDOFFBATCH_TRAILER);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));			
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
									
			for(IHandOffBatchTrailer model : dataList) {
			
				batchUpdater.update(new Object[]{ model.getBatchId()											
											, model.getTrailerId()
											, model.getDispatchTime().getAsDate()
											, model.getCompletionTime()
											, model.getOriginId()
									});
}
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}

	public void updateHandOffBatchTrailerRoute(List<IHandOffBatchTrailer> dataList) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),UPDATE_HANDOFFBATCH_ROUTE);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();
			connection = this.jdbcTemplate.getDataSource().getConnection();
			for(IHandOffBatchTrailer trailerModel : dataList) {
				Iterator<IRouteModel> itr = trailerModel.getRoutes().iterator();
				IRouteModel model = null;
				while(itr.hasNext()) {
					model = itr.next();
					batchUpdater.update(new Object[]{ trailerModel.getTrailerId()											
												, trailerModel.getBatchId()
												, model.getRouteId()
										});
				}
			}
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}

	public Map<String, Integer> getHandOffBatchTrailerCnt(final Date deliveryDate) throws SQLException {
		
		final Map<String, Integer> batchMapping = new HashMap<String, Integer>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_HANDOFFBATCH_TRAILERCNT);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						batchMapping.put(rs.getString("CROSSDOCK_CODE"), rs.getInt("NOOFTRAILERS"));
					} while(rs.next());		        		    	
				}
		}
		);
		
		return batchMapping;
	}
	
	public List<IHandOffBatchTrailer> getHandOffBatchTrailers(final String batchId) throws SQLException {

		final List<IHandOffBatchTrailer> result = new ArrayList<IHandOffBatchTrailer>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_HANDOFFBATCH_TRAILERS);
				ps.setString(1, batchId);
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
							
						IHandOffBatchTrailer infoModel = new HandOffBatchTrailer();
						result.add(infoModel);
						
						infoModel.setTrailerId(rs.getString("TRAILER_NO"));
						infoModel.setDispatchTime(new RoutingTimeOfDay(rs.getTimestamp("DISPATCHTIME")));
						infoModel.setCompletionTime(rs.getTimestamp("COMPLETETIME"));
						infoModel.setOriginId(rs.getString("CROSSDOCK_CODE"));
					} while(rs.next());
				}
		}
		);
		return result;
	}
	
	public Map<String, IHandOffDispatch> getHandOffBatchDispatchs(final Date deliveryDate, final Date cutOffDate) throws SQLException {
		
		final Map<String, IHandOffDispatch> dispatchMapping = new HashMap<String, IHandOffDispatch>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				String query = GET_HANDOFFBATCH_DISPATCH_QRY;
								
				PreparedStatement ps =
					connection.prepareStatement(query);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new Timestamp(cutOffDate.getTime()));
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {	
										
					do {
						
						String dispatchId = rs.getString("DISPATCH_ID");
						
						if(!dispatchMapping.containsKey(dispatchId)){
							IHandOffDispatch dispatch = new HandOffDispatch();
							dispatch.setDispatchId(dispatchId);
							dispatch.setZone(rs.getString("ZONE"));
							dispatch.setRegion(rs.getString("REGION"));
							dispatch.setDispatchDate(rs.getTimestamp("DISPATCH_DATE")); 
							dispatch.setDispatchGroup(rs.getTimestamp("DISPATCH_GROUPTIME"));
							dispatch.setDispatchTime(rs.getTimestamp("TRUCK_DISPATCHTIME"));
							dispatch.setIsBullpen(rs.getString("ISBULLPEN"));
							dispatch.setSupervisorId(rs.getString("SUPERVISOR_ID"));					
							dispatch.setRoute(rs.getString("ROUTE"));
							dispatch.setTruck(rs.getString("TRUCK"));							
							dispatch.setCutoffTime(rs.getTimestamp("CUTOFF_DATETIME"));	 
							dispatch.setOriginFacility(rs.getString("ORIGIN_FACILITY"));
							dispatch.setDestinationFacility(rs.getString("DESTINATION_FACILITY"));
							dispatch.setTrailer("CD".equals(rs.getString("FACILITYTYPE_CODE")) ? true : false );
														
							dispatchMapping.put(dispatchId, dispatch);
						}
						IHandOffDispatchResource resource  = new HandOffDispatchResource();
						resource.setDispatchId(dispatchId);
						resource.setResourceId(rs.getString("RESOURCE_ID"));
						resource.setEmployeeRoleType(rs.getString("ROLE"));
						
						dispatchMapping.get(dispatchId).getDispatchResources().add(resource);
	
					} while (rs.next());		        		    	
				}
			}
		);
		return dispatchMapping;
	}
	
	public void updateHandOffDispatchTruckInfo(List<IHandOffDispatch> dataList) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),UPDATE_HANDOFFDISPATCH_TRUCK);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));			
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();
			connection = this.jdbcTemplate.getDataSource().getConnection();
			for(IHandOffDispatch dispatchModel : dataList) {
				batchUpdater.update( new Object[]{ dispatchModel.getTruck(), dispatchModel.getDispatchId() });
			}
			batchUpdater.flush();
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	private static final String GET_HANDOFFBATCH_STOPCNT = "SELECT COUNT(*) STOPCNT FROM TRANSP.HANDOFF_BATCHSTOP X WHERE X.BATCH_ID = ?";
			
	@Override
	public int getStopCount(final String batchId) throws SQLException {
		
		final List<Integer> stopCnt = new ArrayList<Integer>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps = null;
				ps = connection.prepareStatement(GET_HANDOFFBATCH_STOPCNT);
				ps.setString(1, batchId);
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do { 
						stopCnt.add(rs.getInt("STOPCNT"));
					} while(rs.next());  		    	
				}
		}
		);
		return stopCnt.size()>0?stopCnt.get(0):0;
	}
	
	@Override
	public void addNewHandOffRouteBreaks(List<IHandOffBatchRouteBreak> dataList)
			throws SQLException {
		Connection connection = null;
		try{
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_HANDOFFBATCH_ROUTEBREAK);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(IHandOffBatchRouteBreak model : dataList) {
				
				batchUpdater.update(new Object[]{ model.getBatchId()
											, model.getSessionName()
											, model.getRouteId()
											, model.getBreakId()
											, model.getStartTime()
											, model.getEndTime()
									});
			}			
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public List<IHandOffBatchRouteBreak> getHandOffBatchRouteBreaks(final String batchId) throws SQLException {

		final List<IHandOffBatchRouteBreak> result = new ArrayList<IHandOffBatchRouteBreak>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_HANDOFFBATCH_ROUTE_BREAKS);
				ps.setString(1, batchId);
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
				public void processRow(ResultSet rs) throws SQLException {				    	
					do {	
						IHandOffBatchRouteBreak infoModel = new HandOffBatchRouteBreak();
						result.add(infoModel);
						infoModel.setRouteId(rs.getString("ROUTE_NO"));
						infoModel.setSessionName(rs.getString("SESSION_NAME"));
						infoModel.setBreakId(rs.getString("BREAK_ID"));
						infoModel.setStartTime(rs.getTimestamp("BREAK_START_TIME"));
						infoModel.setEndTime(rs.getTimestamp("BREAK_END_TIME"));
					} while(rs.next());		        		    	
				}
		}
		);
		return result;
	}

	private static String UPDATE_ORDER_RESERVATION_QRY="" +
			" UPDATE DLV.RESERVATION R SET R.UNASSIGNED_ACTION = 'RESERVE_TIMESLOT', R.UNASSIGNED_DATETIME = SYSDATE, R.IN_UPS = 'X', R.STATUS_CODE = '10' WHERE r.ID in " +
			"( select r.id from cust.sale s, cust.salesaction sa, cust.deliveryinfo di, dlv.reservation r "+ 
			" where s.id = sa.sale_id and s.cromod_date = sa.action_date and sa.requested_date >= trunc(sysdate) and SA.ACTION_TYPE in ('CRO','MOD') and sa.id = DI.SALESACTION_ID and DI.RESERVATION_ID = R.ID and s.type ='REG' "+ 
			" and s.status <> 'CAN' and s.id = ? ) ";
		
	public void updateOrderUnassignedInfo(List<IHandOffBatchStop> unassignedOrders) throws SQLException {
			
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),UPDATE_ORDER_RESERVATION_QRY);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			batchUpdater.compile();			
			
			for(IHandOffBatchStop model : unassignedOrders) {
				
				batchUpdater.update(new Object[]{ model.getOrderNumber()
									});
			}			
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}

}
