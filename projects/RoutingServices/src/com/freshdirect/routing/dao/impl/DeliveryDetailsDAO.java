package com.freshdirect.routing.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.DepotLocationModel;
import com.freshdirect.delivery.EnumRegionServiceType;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.EnumReservationClass;
import com.freshdirect.delivery.model.UnassignedDlvReservationModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.constants.EnumReservationStatus;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.dao.IDeliveryDetailsDAO;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.DeliveryWindowMetrics;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IRegionModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IUnassignedModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.PackagingModel;
import com.freshdirect.routing.model.RegionModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.ServiceTimeTypeModel;
import com.freshdirect.routing.model.TimeslotCapacityModel;
import com.freshdirect.routing.model.UnassignedModel;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.routing.util.RoutingUtil;

public class DeliveryDetailsDAO extends BaseDAO implements IDeliveryDetailsDAO {
		
	
	private static final String GET_DELIVERYINFO_QRY = "select di.SCRUBBED_ADDRESS SCRUBBED_ADDRESS, di.DELIVERY_TYPE DELIVERY_TYPE, "+
			"di.APARTMENT APARTMENT,di.CITY CITY, di.STATE STATE, di.COUNTRY COUNTRY, " +
			"di.ZIP ZIP, di.ZONE ZONE "+			
			"from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "+
			"where s.id = ? and s.id = sa.sale_id and s.customer_id = sa.customer_id and sa.id = di.salesaction_id "+ 
			"and sa.action_type in ('CRO','MOD') and sa.action_date=s.CROMOD_DATE and s.type='REG'";

	private static final String GET_DELIVERYTYPE_QRY = "select r.SERVICE_TYPE SERVICE_TYPE from dlv.region r, dlv.region_data rd, dlv.zone z "+
			"where rd.id = z.region_data_id and rd.region_id = r.id and z.zone_code = ? "  +
			"and rd.start_date = (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id=r.id)";
	
	private static final String GET_DELIVERYZONETYPE_QRY = "select z.ZONE_TYPE SERVICE_TYPE from transp.trn_zone z where z.zone_number = ? ";
	
	private static final String GET_DELIVERYZONEDETAILS_QRY = "select z.SERVICETIME_TYPE SERVICETIME_TYPE, z.ZONE_CODE ZONE_NUMBER, z.ZONE_TYPE ZONE_TYPE, z.SVC_ADJ_REDUCTION_FACTOR, a.CODE AREACODE, a.ACTIVE ACTIVE, " +
			"tr.IS_DEPOT IS_DEPOT, tr.code REGION_CODE, tr.name REGION_NAME, tr.description REGION_DESCR, a.BALANCE_BY BALANCE_BY, a.LOADBALANCE_FACTOR LOADBALANCE_FACTOR, a.NEEDS_LOADBALANCE NEEDS_LOADBALANCE from transp.zone z, transp.trn_area a, transp.trn_region tr " +
			" where z.area = a.code  and a.region_code = tr.code  and (z.OBSOLETE <> 'X' or z.OBSOLETE IS NULL)";
	
	private static final String GET_TIMESLOTSBYDATE_QRY = "select t.ID REF_ID, ta.AREA, ta.POSTTRIP_TIME  POST_TRIP, ta.PRETRIP_TIME PRE_TRIP, ta.STEM_MAX_TIME STEM_MAX, z.ZONE_CODE, z.NAME, t.START_TIME , t.END_TIME, t.ROUTING_START_TIME , t.ROUTING_END_TIME" +
			", case when t.premium_cutoff_time is null then TO_CHAR(t.CUTOFF_TIME, 'HH_MI_PM') else TO_CHAR(t.premium_cutoff_time, 'HH_MI_PM') end WAVE_CODE, t.IS_DYNAMIC IS_DYNAMIC, t.IS_CLOSED IS_CLOSED, tr.IS_DEPOT IS_DEPOT, tr.code REGION_CODE, tr.name REGION_NAME, tr.description REGION_DESCR " +
			" from dlv.timeslot t , dlv.zone z, transp.zone ta, transp.trn_area a, transp.trn_region tr " +
			" where t.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE and a.region_code = tr.code " +
			" and t.base_date = ?";
	
	private static final String GET_UNASSIGNED_QRY = "select r.ID RID, r.STATUS_CODE STATUS, r.CUSTOMER_ID CID, r.TYPE RTYPE, r.ORDER_ID OID," +
			" r.UNASSIGNED_DATETIME UDATETIME, r.UNASSIGNED_ACTION UACTION, r.IS_FORCED FORCEFLAG, r.CHEFSTABLE CTFLAG, t.BASE_DATE BDATE, t.START_TIME STIME, t.CUTOFF_TIME, " +
			"t.END_TIME ETIME, s.CROMOD_DATE SCROMODDATE, z.ZONE_CODE ZCODE," +
			"R.NUM_CARTONS NCARTONS, R.NUM_FREEZERS NFREEZERS, R.NUM_CASES NCASES, " +
			" r.ORDER_SIZE OSIZE, r.SERVICE_TIME SRTIME, R.RESERVED_ORDER_SIZE ROSIZE, R.RESERVED_SERVICE_TIME RSRTIME, r.UPDATE_STATUS UPDSTATUS, t.IS_DYNAMIC IS_DYNAMIC, t.IS_CLOSED IS_CLOSED " +
			"from dlv.reservation r, dlv.timeslot t, cust.sale s, dlv.zone z " +
			"where t.BASE_DATE = ?  AND (unassigned_datetime IS NOT NULL OR (UPDATE_STATUS IS NOT NULL AND UPDATE_STATUS in ('FLD','OVD'))) and r.TIMESLOT_ID = t.ID and r.ORDER_ID = s.ID(+) and t.ZONE_ID = z.ID";
	
	private static final String GET_UNASSIGNEDRESERVATION_QRY = "select r.ID RID, r.STATUS_CODE STATUS, r.CUSTOMER_ID CID, r.TYPE RTYPE, r.ORDER_ID OID," +
			" r.UNASSIGNED_DATETIME UDATETIME, r.UNASSIGNED_ACTION UACTION, r.IS_FORCED FORCEFLAG, r.CHEFSTABLE CTFLAG, t.BASE_DATE BDATE, t.START_TIME STIME, " +
			"t.END_TIME ETIME, s.CROMOD_DATE SCROMODDATE, z.ZONE_CODE ZCODE, " +
			"R.NUM_CARTONS NCARTONS, R.NUM_FREEZERS NFREEZERS, R.NUM_CASES NCASES, " +
			"r.ORDER_SIZE OSIZE,r.SERVICE_TIME SRTIME, R.RESERVED_ORDER_SIZE ROSIZE, R.RESERVED_SERVICE_TIME RSRTIME, r.UPDATE_STATUS UPDSTATUS " +
			"from dlv.reservation r, dlv.timeslot t, cust.sale s, dlv.zone z " +
			"where r.ID = ? and r.TIMESLOT_ID = t.ID and r.ORDER_ID = s.ID(+) and t.ZONE_ID = z.ID";
	
	private static final String GET_RESERVATIONBYDATE_QRY = "select r.ID RID, r.STATUS_CODE STATUS, r.CUSTOMER_ID CID, r.TYPE RTYPE, r.ORDER_ID OID," +
	" r.UNASSIGNED_DATETIME UDATETIME, r.UNASSIGNED_ACTION UACTION, r.IS_FORCED FORCEFLAG, r.CHEFSTABLE CTFLAG, t.BASE_DATE BDATE, t.START_TIME STIME, " +
	"t.END_TIME ETIME, z.ZONE_CODE ZCODE, R.NUM_CARTONS NCARTONS, R.NUM_FREEZERS NFREEZERS, R.NUM_CASES NCASES, " +
	"r.ORDER_SIZE OSIZE, r.SERVICE_TIME SRTIME, R.RESERVED_ORDER_SIZE ROSIZE, R.RESERVED_SERVICE_TIME RSRTIME, r.UPDATE_STATUS UPDSTATUS " +
	"from dlv.reservation r, dlv.timeslot t, dlv.zone z " +
	"where t.BASE_DATE = ? and r.TIMESLOT_ID = t.ID and t.ZONE_ID = z.ID and z.ZONE_CODE = ?";
	
	private static final String UPDATE_UNASSIGNEDRESERVATION_QRY = "update dlv.reservation r " +
											"set R.ORDER_SIZE = ? , R.SERVICE_TIME = ? , r.UPDATE_STATUS = ? where r.ID = ?  ";
	
	private static final String UPDATE_TIMESLOTFORSTATUS_QRY = "update dlv.timeslot t set t.IS_CLOSED = ? where t.ID=? ";
	
	private static final String UPDATE_TIMESLOTFORSTATUSBYZONE_QRY = "update dlv.timeslot tx set tx.IS_CLOSED = ? where tx.ID in (select t.id from dlv.timeslot t, dlv.zone z  where t.base_date=? and z.zone_code = ? and t.zone_id = z.id)";	
	
	private static final String UPDATE_TIMESLOTFORSTATUSBYREGION_QRY = "update dlv.timeslot tx set tx.IS_CLOSED = ? where tx.ID in (select t.id from dlv.timeslot t, dlv.zone z, transp.zone zt where t.base_date=? and zt.region = ? and t.zone_id = z.id and z.zone_code=zt.zone_code)";
	
	private static final String UPDATE_TIMESLOTFORDYNAMICSTATUS_QRY = "update dlv.timeslot t set t.IS_DYNAMIC = ? where t.ID=? ";
	
	private static final String UPDATE_TIMESLOTFORDYNAMICSTATUSBYZONE_QRY = "update dlv.timeslot tx set tx.IS_DYNAMIC = ? where tx.ID in (select t.id from dlv.timeslot t, dlv.zone z  where t.base_date=? and z.zone_code = ? and t.zone_id = z.id)";	
	
	private static final String UPDATE_TIMESLOTFORDYNAMICSTATUSBYREGION_QRY = "update dlv.timeslot tx set tx.IS_DYNAMIC = ? where tx.ID in (select t.id from dlv.timeslot t, dlv.zone z, transp.zone zt where t.base_date=? and zt.region = ? and t.zone_id = z.id and z.zone_code=zt.zone_code)";
	
	private static final String UPDATE_TIMESLOTCUTUFF_QRYAPPEND = " and tx.cutoff_time = (select cutoff_time from transp.trn_cutoff c where c.id = ?) ";
	
	private static final String EARLY_WARNING_QUERY =
		"select base_date, code, name, st, et, rst, ret, is_dynamic, sum(orders) as total_order, sum(capacity) as capacity, "
			+ "sum(total_alloc) as total_alloc, "
			+ "sum(base_alloc) as base_alloc, "
			+ "sum(base_orders) as base_orders, "
			+ "sum(ct_capacity) as ct_capacity, "
			+ "sum(ct_alloc) as ct_alloc, "
			+ "sum(ct_orders) as ct_orders, "
			+ "sum(premium_capacity) as premium_capacity, "
			+ "sum(premium_alloc) as premium_alloc, "
			+ "sum(premium_basealloc) as premium_basealloc, "
            + "sum(premium_ct_alloc) as premium_ct_alloc "
			+ "from "
			+ "( "
			+ "select z.zone_code as code, z.name, ts.base_date, ts.capacity, ts.ct_capacity,  ts.premium_capacity, ts.START_TIME st, ts.END_TIME et, ts.ROUTING_START_TIME rst, ts.ROUTING_END_TIME ret, z.ct_active as ct_active, ts.is_dynamic, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = '10' ) as orders, "
			+ "decode((sysdate-(TO_DATE(TO_CHAR(ts.base_date - 1, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time - (z.ct_release_time/60/24), 'HH24:MI:SS'), " 
			+ "'YYYY-MM-DD HH24:MI:SS'))- abs(sysdate-(TO_DATE(TO_CHAR(ts.base_date - 1, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time - (z.ct_release_time/60/24), " 
			+ "'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS')))),0,(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> '15' and status_code <> '20' " 
			+ "and class is null),(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> '15' and status_code <> '20' and chefstable = ' ' " 
			+ "and class is null)+ts.ct_capacity)+ts.premium_capacity as total_alloc, "
			+ "decode((sysdate-(TO_DATE(TO_CHAR(ts.base_date, 'YYYY-MM-DD')||' '||to_char(ts.premium_cutoff_time - (z.premium_ct_release_time/60/24), 'HH24:MI:SS'), " 
			+ "'YYYY-MM-DD HH24:MI:SS'))- abs(sysdate-(TO_DATE(TO_CHAR(ts.base_date, 'YYYY-MM-DD')||' '||to_char(ts.premium_cutoff_time - (z.premium_ct_release_time/60/24), " 
			+ "'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS')))),0,(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> '15' and status_code <> '20' " 
			+ "and class is not null),(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> '15' and status_code <> '20' and class = 'P')+ts.premium_ct_capacity) as premium_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> '15' and status_code <> '20' and chefstable = ' ' and class = 'P') as premium_basealloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> '15' and status_code <> '20' and chefstable = 'X' and class = 'PC') as premium_ct_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = '10' and chefstable = ' ' and class is null) as base_orders, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> '15' and status_code <> '20' and chefstable = ' ' and class is null) as base_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and  status_code <> '15' and status_code <> '20' and chefstable = 'X' and class is null) as ct_alloc, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = '10' and chefstable = 'X' and class is null) as ct_orders "
			+ "from dlv.timeslot ts, dlv.zone z "
			+ "where ts.zone_id=z.id and ts.base_date = ? "
			;
			
	private static final String ORDERSIZE_ESTIMATION_QUERY = "select Ceil(Avg(tbl.NUM_REGULAR_CARTONS)) CCOUNT, " +
			"Ceil(Avg(tbl.NUM_FREEZER_CARTONS)) FCOUNT, Ceil(Avg(tbl.NUM_ALCOHOL_CARTONS)) ACOUNT " +
			"from (select NUM_REGULAR_CARTONS, NUM_FREEZER_CARTONS, NUM_ALCOHOL_CARTONS from cust.sale s " +
			"where s.CUSTOMER_ID = ? and s.STATUS = 'STL' and s.TYPE = 'REG' order by s.CROMOD_DATE desc) tbl where rownum <= ?";
	
	private static final String GET_TIMESLOTSBYDATEANDZONE_QRY =
		"select t.id REF_ID, t.base_date, t.start_time, t.end_time,t.ROUTING_START_TIME, t.ROUTING_END_TIME, t.cutoff_time, t.status, t.zone_id, t.capacity, z.zone_code, t.ct_capacity" +
		", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.POSTTRIP_TIME POST_TRIP, ta.PRETRIP_TIME PRE_TRIP, z.NAME ZONE_NAME, " +
		"case when t.premium_cutoff_time is null then TO_CHAR(t.CUTOFF_TIME, 'HH_MI_PM') else TO_CHAR(t.premium_cutoff_time, 'HH_MI_PM') end WAVE_CODE, t.IS_DYNAMIC IS_DYNAMIC, t.IS_CLOSED IS_CLOSED, a.DELIVERY_RATE AREA_DLV_RATE,  " 
		+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = ' ' and class is null) as base_allocation, " 
		+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = 'X' and class is null) as ct_allocation, " 
		+ "(select z.ct_release_time from dlv.zone z where z.id = t.zone_id) as ct_release_time, "
		+ "(select z.ct_active from dlv.zone z where z.id = t.zone_id) as ct_active, tr.IS_DEPOT IS_DEPOT, tr.code REGION_CODE, tr.name REGION_NAME, tr.description REGION_DESCR "
		+ "from dlv.region r, dlv.region_data rd, dlv.timeslot t, dlv.zone z, transp.zone ta, transp.trn_area a,transp.trn_region tr "
		+ "where r.service_type = ? and r.id = rd.region_id "
		+ "and rd.id = z.region_data_id "
		+ "and mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?, ?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
		+ "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id) "
		+ 	"or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id)) "
		+ "and t.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE  and a.region_code = tr.code  and t.base_date >= rd.start_date "
		+ "and t.base_date >= ? and t.base_date < ? "
		+ "and ((t.premium_cutoff_Time is null and to_date(to_char(t.base_date-1, 'MM/DD/YY ') || to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE) or " +
		" (t.premium_cutoff_Time is not null and to_date(to_char(t.base_date, 'MM/DD/YY ') || to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE)) ";
	
	private static final String TIMESLOT_BY_ID =
			"select distinct ts.id, ts.base_date, ts.start_time, ts.end_time,ts.ROUTING_START_TIME, ts.ROUTING_END_TIME,  ts.cutoff_time, ts.premium_cutoff_time,ts.status, ts.zone_id, ts.capacity, ts.ct_capacity" +
			", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.POSTTRIP_TIME stemfrom, ta.PRETRIP_TIME stemto, ta.ZONE_ECOFRIENDLY ecoFriendly, ta.STEERING_RADIUS steeringRadius, z.NAME ZONE_NAME, " +
			"case when ts.premium_cutoff_time is null then TO_CHAR(ts.CUTOFF_TIME, 'HH_MI_PM') else TO_CHAR(ts.premium_cutoff_time, 'HH_MI_PM') end WAVE_CODE, " +
			"ts.IS_DYNAMIC IS_DYNAMIC, ts.IS_CLOSED IS_CLOSED, tr.IS_DEPOT IS_DEPOT, tr.code REGION_CODE, tr.name REGION_NAME, tr.description REGION_DESCR, a.DELIVERY_RATE AREA_DLV_RATE,"
				+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
				+ "where zone_id = ts.zone_id AND ts.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and chefstable = ' ' and class is null) as base_allocation, "
				+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
				+ "where zone_id = ts.zone_id AND ts.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and chefstable = 'X' and class is null) as ct_allocation, "
				+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
				+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active, "
				+ "(select z.zone_code from dlv.zone z where z.id=ts.zone_id ) as zone_code "
				+ " from dlv.timeslot ts, dlv.zone z, transp.zone ta, transp.trn_area a, transp.trn_region tr, dlv.reservation r "
				+ "where ts.id = ? AND ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE and a.region_code = tr.code AND ts.id=r.TIMESLOT_ID(+)";

		public DlvTimeslotModel getTimeslotById(final String timeslotId) throws SQLException {
			final DlvTimeslotModel _tmpSlot = new DlvTimeslotModel();
			 PreparedStatementCreator creator=new PreparedStatementCreator() {
		            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
		                PreparedStatement ps =
		                    connection.prepareStatement(TIMESLOT_BY_ID);
		                ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		    			ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		    			ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		    			ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());				
		    			
		    			ps.setString(5, timeslotId);
		
		                return ps;
		            }  
		        };
		       
		        jdbcTemplate.query(creator, 
		         		  new RowCallbackHandler() { 
		         		      public void processRow(ResultSet rs) throws SQLException {
		         		    	
		         		    	do {


		         					PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
		         					java.util.Date baseDate = rs.getDate("BASE_DATE");
		         					TimeOfDay startTime = new TimeOfDay(rs.getTime("START_TIME"));
		         					TimeOfDay endTime = new TimeOfDay(rs.getTime("END_TIME"));
		         					TimeOfDay cutoffTime = new TimeOfDay(rs.getTime("CUTOFF_TIME"));
		         					Date premiumCutoffTime = rs.getTime("PREMIUM_CUTOFF_TIME");
		         					EnumTimeslotStatus status = EnumTimeslotStatus.getEnum(rs.getInt("STATUS"));
		         					int capacity = rs.getInt("CAPACITY");
		         					int baseAllocation = rs.getInt("BASE_ALLOCATION");
		         					int ctAllocation = rs.getInt("CT_ALLOCATION");
		         					String zoneId = rs.getString("ZONE_ID");
		         					int ctCapacity = rs.getInt("CT_CAPACITY");
		         					int ctReleaseTime = rs.getInt("CT_RELEASE_TIME");
		         					boolean ctActive = "X".equals(rs.getString("CT_ACTIVE"));
		         					String zoneCode = rs.getString("ZONE_CODE");
		         					_tmpSlot.setPK(pk);
		         					_tmpSlot.setZoneId(zoneId);
		         					_tmpSlot.setBaseDate(baseDate);
		         					_tmpSlot.setStartTime(startTime);
		         					_tmpSlot.setEndTime(endTime);
		         					_tmpSlot.setCutoffTime(cutoffTime);
		         					_tmpSlot.setStatus(status);
		         					_tmpSlot.setCapacity(capacity);
		         					_tmpSlot.setChefsTableCapacity(ctCapacity);
		         					_tmpSlot.setBaseAllocation(baseAllocation);
		         					_tmpSlot.setChefsTableAllocation(ctAllocation);
		         					_tmpSlot.setCtReleaseTime(ctReleaseTime);
		         					_tmpSlot.setCtActive(ctActive);
		         					_tmpSlot.setZoneCode(zoneCode);
		         					
		         					//this method is only used to update the reservation size. It will use the premium cutoff to determine when it should update.
		         					_tmpSlot.setPremiumSlot((premiumCutoffTime!=null)?true:false);
		         					_tmpSlot.setPremiumCutoffTime((premiumCutoffTime!=null)?new TimeOfDay(premiumCutoffTime):null);
		         					
		         					// Prepare routing slot configuration from result set
		         					IDeliverySlot routingSlot = new DeliverySlot();
		         					
		         					routingSlot.setDisplayStartTime(rs.getTimestamp("START_TIME"));
		         					routingSlot.setDisplayStopTime(rs.getTimestamp("END_TIME"));
		         					
		         					routingSlot.setRoutingStartTime(rs.getTimestamp("ROUTING_START_TIME"));
		         					routingSlot.setRoutingStopTime(rs.getTimestamp("ROUTING_END_TIME"));
		         					
		         					routingSlot.setStartTime((routingSlot.getRoutingStartTime()!=null)?routingSlot.getRoutingStartTime():routingSlot.getDisplayStartTime());
		         					routingSlot.setStopTime((routingSlot.getRoutingStopTime()!=null)?routingSlot.getRoutingStopTime():routingSlot.getDisplayStopTime());
		         					
		         					
		         					routingSlot.setWaveCode(rs.getString("WAVE_CODE"));
		         					routingSlot.setDynamicActive("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")) ? true : false);
		         					routingSlot.setManuallyClosed("X".equalsIgnoreCase(rs.getString("IS_CLOSED")) ? true : false);
		         					routingSlot.setZoneCode(zoneCode);
		         					routingSlot.setEcoFriendly(rs.getBigDecimal("ecoFriendly"));
		         					routingSlot.setSteeringRadius(rs.getBigDecimal("steeringRadius"));
		         							
		         					IRoutingSchedulerIdentity _schId = new RoutingSchedulerIdentity();
		         					_schId.setDeliveryDate(baseDate);
		         					
		         					IAreaModel _aModel = new AreaModel();
		         					_aModel.setAreaCode(rs.getString("AREA_CODE"));
		         					_aModel.setDeliveryRate(rs.getDouble("AREA_DLV_RATE"));
		         					_aModel.setPostTripTime(rs.getInt("stemfrom"));
		         					_aModel.setPreTripTime(rs.getInt("stemto"));
		         					
		         					IRegionModel _rModel = new RegionModel();
		         					_rModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
		         					_rModel.setRegionCode(rs.getString("REGION_CODE"));
		         					_rModel.setName(rs.getString("REGION_NAME"));
		         					_rModel.setDescription(rs.getString("REGION_DESCR"));
		         					_aModel.setRegion(_rModel);
		         					
		         					_schId.setRegionId(RoutingUtil.getRegion(_aModel));
		         					_schId.setArea(_aModel);
		         					_schId.setDepot(_aModel.isDepot());
		         					
		         					routingSlot.setSchedulerId(_schId);
		         					_tmpSlot.setRoutingSlot(routingSlot);
		         					        		    	
		         		      } while(rs.next());
		         		  }
		        }); 
			return _tmpSlot;
		}

	public IPackagingModel getHistoricOrderSize(final String customerId, final int range) throws SQLException {
		
		final IPackagingModel model = new PackagingModel();
		
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(ORDERSIZE_ESTIMATION_QUERY);
                ps.setString(1, customerId);
                ps.setInt(2, range);
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	do {
       		    		model.setNoOfCartons(rs.getInt("CCOUNT")+ rs.getInt("ACOUNT"));
       		    		model.setNoOfFreezers(rs.getInt("FCOUNT"));       		    		
       		    	}  while(rs.next());		        		    	
       		      }
       		  }
       	); 
        
		return model;
	}
	
	public IDeliveryModel getDeliveryInfo(final String saleId) throws SQLException {
		 final IDeliveryModel model = new DeliveryModel();	         
         PreparedStatementCreator creator=new PreparedStatementCreator() {
             public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                 PreparedStatement ps =
                     connection.prepareStatement(GET_DELIVERYINFO_QRY);
                 ps.setString(1,saleId);
                 return ps;
             }  
         };
         jdbcTemplate.query(creator, 
        		  new RowCallbackHandler() { 
        		      public void processRow(ResultSet rs) throws SQLException {
        		    	
        		    	do { 
        		    		IZoneModel tmpModel = new ZoneModel();
				    		tmpModel.setZoneNumber(rs.getString("ZONE"));
        		    		model.setDeliveryZone(tmpModel);
        		    		
        		    		IBuildingModel building = new BuildingModel();
        		    		
        		    		building.setSrubbedStreet(rs.getString("SCRUBBED_ADDRESS"));
        		    		building.setStreetAddress1(building.getSrubbedStreet());        		    		
        		    		building.setZipCode(rs.getString("ZIP"));
        		    		building.setState(rs.getString("STATE"));
        		    		building.setCity(rs.getString("CITY"));
        		    		building.setCountry(rs.getString("COUNTRY"));
        		    		
        		    		ILocationModel locModel = new LocationModel(building);
        		    		locModel.setApartmentNumber(rs.getString("APARTMENT"));
        		    		model.setDeliveryLocation(locModel);
        		    		        		    		
        		    		
        		    	}  while(rs.next());		        		    	
        		      }
        		  }
        	); 
         
		return model;
	}
	
		
	public String getDeliveryType(final String zoneCode) throws SQLException {
		
		return (String)jdbcTemplate.queryForObject(GET_DELIVERYTYPE_QRY, new Object[]{zoneCode}, String.class);	
	}	
	
	public String getDeliveryZoneType(final String zoneCode) throws SQLException {
				
		return (String)jdbcTemplate.queryForObject(GET_DELIVERYZONETYPE_QRY, new Object[]{zoneCode}, String.class);    		
	}	
	
	public Map getDeliveryZoneDetails() throws SQLException {
		final Map zoneDetailsMap = new HashMap();
				
		jdbcTemplate.query(GET_DELIVERYZONEDETAILS_QRY, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		String zoneCode = rs.getString("ZONE_NUMBER");
				    		IZoneModel tmpModel = new ZoneModel();
				    		tmpModel.setZoneNumber(zoneCode);
				    						    		
				    		tmpModel.setZoneType(rs.getString("ZONE_TYPE"));
				    		tmpModel.setSvcAdjReductionFactor(rs.getDouble("SVC_ADJ_REDUCTION_FACTOR"));
				    		IAreaModel tmpAreaModel = new AreaModel();
				    		tmpAreaModel.setAreaCode(rs.getString("AREACODE"));
				    		tmpAreaModel.setBalanceBy(rs.getString("BALANCE_BY"));
				    		tmpAreaModel.setLoadBalanceFactor(rs.getDouble("LOADBALANCE_FACTOR"));
				    		tmpAreaModel.setNeedsLoadBalance("X".equalsIgnoreCase(rs.getString("NEEDS_LOADBALANCE")));
				    		
				    		
				    		tmpAreaModel.setActive("X".equalsIgnoreCase(rs.getString("ACTIVE")));
				    		
				    		IRegionModel _rModel = new RegionModel();
         					_rModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
         					_rModel.setRegionCode(rs.getString("REGION_CODE"));
         					_rModel.setName(rs.getString("REGION_NAME"));
         					_rModel.setDescription(rs.getString("REGION_DESCR"));
         					
				    		tmpAreaModel.setRegion(_rModel);
         					
				    		IServiceTimeTypeModel serviceTimeType = new ServiceTimeTypeModel();
        		    		serviceTimeType.setCode( rs.getString("SERVICETIME_TYPE"));
        		    		tmpModel.setServiceTimeType(serviceTimeType);
        		    		
				    		tmpModel.setArea(tmpAreaModel);
				    		zoneDetailsMap.put(zoneCode, tmpModel);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return zoneDetailsMap;
	}
	
	public List getLateDeliveryOrders(String query) throws SQLException {
		final List lateDeliveryOrders = new ArrayList();
		
		jdbcTemplate.query(query, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		lateDeliveryOrders.add(rs.getString("ERPID"));
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return lateDeliveryOrders;
	}
	
	public Map<String, List<IDeliverySlot>> getTimeslotsByDate(final Date deliveryDate, final Date cutOffTime, final String zoneCode, final EnumLogicalOperator condition) throws SQLException {
		
		final Map<String, List<IDeliverySlot>> timeslotByArea = new TreeMap<String, List<IDeliverySlot>>();
		
		final StringBuffer query = new StringBuffer();
		query.append(GET_TIMESLOTSBYDATE_QRY);
		String conditionValue = "=";
		if(condition != null) {
			conditionValue = condition.getName();
		}
		if(cutOffTime != null) {
			query.append(" and ((t.premium_cutoff_Time is not null and t.premium_cutoff_Time "+conditionValue+" ?) or (t.premium_cutoff_Time is null and t.CUTOFF_TIME "+conditionValue+" ?))");
		}
		query.append(" order by z.ZONE_CODE, t.START_TIME");
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(query.toString());
                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
                
                if(cutOffTime != null) {
                	ps.setTimestamp(2, new java.sql.Timestamp(cutOffTime.getTime()));
                	ps.setTimestamp(3, new java.sql.Timestamp(cutOffTime.getTime()));
                }
                return ps;
            }  
        };
        
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		String zCode = rs.getString("ZONE_CODE");
				    		IDeliverySlot tmpModel = new DeliverySlot();
				    		
				    		tmpModel.setWaveCode(rs.getString("WAVE_CODE"));
				    		tmpModel.setDynamicActive("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")) ? true : false);
				    		tmpModel.setManuallyClosed("X".equalsIgnoreCase(rs.getString("IS_CLOSED")) ? true : false);
				    		tmpModel.setReferenceId(rs.getString("REF_ID"));
				    		
				    		tmpModel.setDisplayStartTime(rs.getTimestamp("START_TIME"));
				    		tmpModel.setDisplayStopTime(rs.getTimestamp("END_TIME"));
				    		
				    		tmpModel.setRoutingStartTime(rs.getTimestamp("ROUTING_START_TIME"));
				    		tmpModel.setRoutingStopTime(rs.getTimestamp("ROUTING_END_TIME"));
				    		
				    		tmpModel.setStartTime((tmpModel.getRoutingStartTime()!=null)?tmpModel.getRoutingStartTime():tmpModel.getDisplayStartTime());
				    		tmpModel.setStopTime((tmpModel.getRoutingStopTime()!=null)?tmpModel.getRoutingStopTime():tmpModel.getDisplayStopTime());
				    		
				    		if(!timeslotByArea.containsKey(zCode)) {
				    			timeslotByArea.put(zCode, new ArrayList<IDeliverySlot>());
				    		}
				    		timeslotByArea.get(zCode).add(tmpModel);
				    		
				    		IRoutingSchedulerIdentity _schId = new RoutingSchedulerIdentity();
							_schId.setDeliveryDate(deliveryDate);
							
							IAreaModel _aModel = new AreaModel();
							_aModel.setAreaCode(rs.getString("AREA"));
							_aModel.setPostTripTime(rs.getInt("POST_TRIP"));
							_aModel.setPreTripTime(rs.getInt("PRE_TRIP"));	
							
							IRegionModel _rModel = new RegionModel();
         					_rModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
         					_rModel.setRegionCode(rs.getString("REGION_CODE"));
         					_rModel.setName(rs.getString("REGION_NAME"));
         					_rModel.setDescription(rs.getString("REGION_DESCR"));
         					_aModel.setRegion(_rModel);
         					
         					_schId.setRegionId(RoutingUtil.getRegion(_aModel));
							_schId.setArea(_aModel);
							
							tmpModel.setSchedulerId(_schId);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return timeslotByArea;
	}
	
	public Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(final Date deliveryDate, final Date cutOffTime, final String zoneCode, final EnumLogicalOperator condition, final boolean filterTimeslots) throws SQLException {
		
		final Map<String, List<IDeliveryWindowMetrics>> timeslotByZone = new TreeMap<String, List<IDeliveryWindowMetrics>>();
		
		final StringBuffer query = new StringBuffer();
		query.append(EARLY_WARNING_QUERY);
		
		String conditionValue = "=";
		if(condition != null) {
			conditionValue = condition.getName();
		}
		if(filterTimeslots) {
			query.append(" and ts.capacity <> 0 ");
		}
		if(cutOffTime != null) {
			query.append(" and ((ts.premium_cutoff_Time is not null and ts.premium_cutoff_Time "+conditionValue+" ?) or (ts.premium_cutoff_Time is null and ts.CUTOFF_TIME "+conditionValue+" ?))");
		}
		
		query.append(") group by code, name, base_date, st, et, rst, ret, is_dynamic " +
				"order by code, name, base_date, st, et");
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(query.toString());
                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
                
                if(cutOffTime != null) {
                	ps.setTimestamp(2, new java.sql.Timestamp(cutOffTime.getTime()));
                	ps.setTimestamp(3, new java.sql.Timestamp(cutOffTime.getTime()));
                }
                
                return ps;
            }  
        };
        
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do { 
				    		IDeliveryWindowMetrics metrics = new DeliveryWindowMetrics();				    		
				    		String zCode = rs.getString("code");
				    		
				    		metrics.setDeliveryDate(rs.getDate("base_date"));
				    		metrics.setDisplayStartTime(rs.getTimestamp("st"));
				    		metrics.setDisplayEndTime(rs.getTimestamp("et"));
				    		metrics.setDeliveryStartTime((rs.getTimestamp("rst")!=null)?rs.getTimestamp("rst"):metrics.getDisplayStartTime());
				    		metrics.setDeliveryEndTime((rs.getTimestamp("ret")!=null)?rs.getTimestamp("ret"):metrics.getDisplayEndTime());
				    			
				    		metrics.setOrderCapacity(rs.getInt("capacity"));
				    		metrics.setTotalConfirmedOrders(rs.getInt("total_order"));
				    		metrics.setTotalAllocatedOrders(rs.getInt("total_alloc"));
				    		
				    		metrics.setBaseAllocation(rs.getInt("base_alloc"));
				    		metrics.setChefsTableAllocation(rs.getInt("ct_alloc"));
				    		metrics.setPremiumAllocation(rs.getInt("premium_basealloc"));
				    		metrics.setPremiumCtAllocation(rs.getInt("premium_ct_alloc"));
				    		metrics.setDynamic("X".equalsIgnoreCase(rs.getString("is_dynamic")) ? true : false);
				    		
				    		if(!timeslotByZone.containsKey(zCode)) {
				    			timeslotByZone.put(zCode, new ArrayList<IDeliveryWindowMetrics>());
				    		}
				    		timeslotByZone.get(zCode).add(metrics);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return timeslotByZone;
	}
	
	public List<IUnassignedModel> getUnassigned(final Date deliveryDate, final Date cutOffTime
													, final String zoneCode, final EnumLogicalOperator condition) throws SQLException {
		
		final List<IUnassignedModel> orders = new ArrayList<IUnassignedModel>();

		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_UNASSIGNED_QRY);
		
		String conditionValue = "=";
		if(condition != null) {
			conditionValue = condition.getName();
		}
		if(cutOffTime != null) {
			updateQ.append(" and t.CUTOFF_TIME "+conditionValue+" ? ");
		}		
		
		updateQ.append(" ORDER BY z.ZONE_CODE, t.START_TIME ");		
		

		PreparedStatementCreator creator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(updateQ.toString());
                ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
                
                if(cutOffTime != null) {
                	ps.setTimestamp(2, new java.sql.Timestamp(cutOffTime.getTime()));
                }
                
                return ps;
            }  
        };
		
        
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do { 
				    		IOrderModel order = new OrderModel();	
				    		order.setOrderNumber(rs.getString("OID"));
				    		order.setCustomerNumber(rs.getString("CID"));
				    		order.setCreateModifyTime(rs.getTimestamp("SCROMODDATE"));
				    		order.setUnassignedTime(rs.getTimestamp("UDATETIME"));
				    		order.setUnassignedAction(rs.getString("UACTION"));
				    		
				    		order.setOverrideOrderSize(rs.getDouble("OSIZE"));
				    		order.setOverrideServiceTime(rs.getDouble("SRTIME"));
				    		
				    		order.setReservedOrderSize(rs.getDouble("ROSIZE"));
				    		order.setReservedServiceTime(rs.getDouble("RSRTIME"));
							
				    		EnumRoutingUpdateStatus _status = EnumRoutingUpdateStatus.getEnum(rs.getString("UPDSTATUS"));
				    		if(_status != null) {
				    			order.setUpdateStatus(_status.value());
				    		}
				    						    						    						    		
				    		IDeliveryModel dModel = new DeliveryModel();
				    		dModel.setDeliveryDate(rs.getDate("BDATE"));
				    		dModel.setDeliveryStartTime(rs.getTimestamp("STIME"));
				    		dModel.setDeliveryEndTime(rs.getTimestamp("ETIME"));
				    		dModel.setDeliveryCutoffTime(rs.getTimestamp("CUTOFF_TIME"));
				    		dModel.setReservationId(rs.getString("RID"));
				    						    		
				    		IPackagingModel pModel = new PackagingModel();
				    		pModel.setNoOfCartons(rs.getLong("NCARTONS"));
				    		pModel.setNoOfCases(rs.getLong("NCASES"));
				    		pModel.setNoOfFreezers(rs.getLong("NFREEZERS"));
				    		dModel.setPackagingDetail(pModel);
				    		
				    		IZoneModel zModel = new ZoneModel();
				    		zModel.setZoneNumber(rs.getString("ZCODE"));
				    		
				    		dModel.setDeliveryZone(zModel);
				    		
				    		order.setDeliveryInfo(dModel);
				    		
				    		IDeliverySlot slot = new DeliverySlot();
				    		slot.setDynamicActive("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")) ? true : false);
				    		slot.setManuallyClosed("X".equalsIgnoreCase(rs.getString("IS_CLOSED")) ? true : false);
				    		
				    		IUnassignedModel root = new UnassignedModel();
				    		root.setOrder(order);
				    		root.setSlot(slot);
				    		root.setIsChefsTable(rs.getString("CTFLAG"));
				    		root.setIsForced(rs.getString("FORCEFLAG"));
				    					    		
				    		
				    		orders.add(root);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return orders;
	}
	
	public IOrderModel getRoutingOrderByReservation(final String reservationId) throws SQLException {

		final IOrderModel order = new OrderModel();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_UNASSIGNEDRESERVATION_QRY);
				ps.setString(1, reservationId);

				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do { 
						
					order.setOrderNumber(rs.getString("OID"));
					order.setCustomerNumber(rs.getString("CID"));
					order.setCreateModifyTime(rs.getTimestamp("SCROMODDATE"));
					order.setUnassignedTime(rs.getTimestamp("UDATETIME"));
					order.setUnassignedAction(rs.getString("UACTION"));
					
					order.setOverrideOrderSize(rs.getDouble("OSIZE"));
		    		order.setOverrideServiceTime(rs.getDouble("SRTIME"));
		    		
		    		order.setReservedOrderSize(rs.getDouble("ROSIZE"));
		    		order.setReservedServiceTime(rs.getDouble("RSRTIME"));
		    		
					EnumRoutingUpdateStatus _status = EnumRoutingUpdateStatus.getEnum(rs.getString("UPDSTATUS"));
		    		if(_status != null) {
		    			order.setUpdateStatus(_status.value());
		    		}
					
					IDeliveryModel dModel = new DeliveryModel();
					dModel.setDeliveryDate(rs.getDate("BDATE"));
					dModel.setDeliveryStartTime(rs.getTimestamp("STIME"));
					dModel.setDeliveryEndTime(rs.getTimestamp("ETIME"));
					dModel.setReservationId(rs.getString("RID"));
					
					IPackagingModel pModel = new PackagingModel();
					pModel.setNoOfCartons(rs.getLong("NCARTONS"));
					pModel.setNoOfCases(rs.getLong("NCASES"));
					pModel.setNoOfFreezers(rs.getLong("NFREEZERS"));
					dModel.setPackagingDetail(pModel);
					
					IZoneModel zModel = new ZoneModel();
					zModel.setZoneNumber(rs.getString("ZCODE"));

					dModel.setDeliveryZone(zModel);

					order.setDeliveryInfo(dModel);

				} while(rs.next());		        		    	
			}
		}
		);
		return order;
	}
	
	public List<IOrderModel> getRoutingOrderByDate(final Date deliveryDate
															, final String zoneCode, final boolean filterExpiredCancelled) throws SQLException {

		final List<IOrderModel> result = new ArrayList<IOrderModel>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(GET_RESERVATIONBYDATE_QRY);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(2, zoneCode);
				
				return ps;
			}  
		};

		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do { 
					int statusCode = rs.getInt("STATUS");
					if(filterExpiredCancelled && (statusCode == 15 || statusCode == 20)) {
						continue;
					}
					IOrderModel order = new OrderModel();	
					result.add(order);
					
					order.setOrderNumber(rs.getString("OID"));
					order.setCustomerNumber(rs.getString("CID"));
					
					order.setUnassignedTime(rs.getTimestamp("UDATETIME"));
					order.setUnassignedAction(rs.getString("UACTION"));
					
					order.setOverrideOrderSize(rs.getDouble("OSIZE"));
		    		order.setOverrideServiceTime(rs.getDouble("SRTIME"));
		    		
		    		order.setReservedOrderSize(rs.getDouble("ROSIZE"));
		    		order.setReservedServiceTime(rs.getDouble("RSRTIME"));
		    		
					EnumRoutingUpdateStatus _status = EnumRoutingUpdateStatus.getEnum(rs.getString("UPDSTATUS"));
		    		if(_status != null) {
		    			order.setUpdateStatus(_status.value());
		    		}
					
					IDeliveryModel dModel = new DeliveryModel();
					dModel.setDeliveryDate(rs.getDate("BDATE"));
					dModel.setDeliveryStartTime(rs.getTimestamp("STIME"));
					dModel.setDeliveryEndTime(rs.getTimestamp("ETIME"));
					dModel.setReservationId(rs.getString("RID"));
					
					IPackagingModel pModel = new PackagingModel();
					pModel.setNoOfCartons(rs.getLong("NCARTONS"));
					pModel.setNoOfCases(rs.getLong("NCASES"));
					pModel.setNoOfFreezers(rs.getLong("NFREEZERS"));
					dModel.setPackagingDetail(pModel);
					
					IZoneModel zModel = new ZoneModel();
					zModel.setZoneNumber(rs.getString("ZCODE"));

					dModel.setDeliveryZone(zModel);

					order.setDeliveryInfo(dModel);

				} while(rs.next());		        		    	
			}
		}
		);
		return result;
	}
	
	
	public int updateRoutingOrderByReservation(final String reservationId
												, final double overrideOrderSize
												, final double overriderServiceTime) throws SQLException {

		Connection connection=null;
		int result = 0;
		try{
			result = this.jdbcTemplate.update(UPDATE_UNASSIGNEDRESERVATION_QRY, new Object[] {new Double(overrideOrderSize), new Double(overriderServiceTime)
												, EnumRoutingUpdateStatus.OVERRIDDEN.value(), reservationId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForStatus(final String timeslotId, final boolean isClosed) throws SQLException {

		Connection connection=null;
		int result = 0;
		try{
			result = this.jdbcTemplate.update(UPDATE_TIMESLOTFORSTATUS_QRY, new Object[] {(isClosed ? "X" : null), timeslotId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForStatusByZone(final Date baseDate, final String zoneCode, final String cutOff, final boolean isClosed) throws SQLException {

		Connection connection=null;
		int result = 0;
		StringBuffer strUpdateQuery = new StringBuffer();
		strUpdateQuery.append(UPDATE_TIMESLOTFORSTATUSBYZONE_QRY);
		try{			
			if(cutOff == null || cutOff.trim().length() == 0) {
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isClosed ? "X" : null), baseDate, zoneCode});
			} else {
				strUpdateQuery.append(UPDATE_TIMESLOTCUTUFF_QRYAPPEND);
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isClosed ? "X" : null), baseDate, zoneCode, cutOff});
			}
			
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForStatusByRegion(final Date baseDate, final String regionCode, final String cutOff, final boolean isClosed) throws SQLException {

		Connection connection=null;
		int result = 0;
		StringBuffer strUpdateQuery = new StringBuffer();
		strUpdateQuery.append(UPDATE_TIMESLOTFORSTATUSBYREGION_QRY);
		try{
			if(cutOff == null || cutOff.trim().length() == 0) {
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isClosed ? "X" : null), baseDate, regionCode});
			} else {
				strUpdateQuery.append(UPDATE_TIMESLOTCUTUFF_QRYAPPEND);
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isClosed ? "X" : null), baseDate, regionCode, cutOff});
			}			
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForDynamicStatus(final String timeslotId, final boolean isDynamic) throws SQLException {

		Connection connection=null;
		int result = 0;
		try{
			result = this.jdbcTemplate.update(UPDATE_TIMESLOTFORDYNAMICSTATUS_QRY, new Object[] {(isDynamic ? "X" : null), timeslotId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public int updateTimeslotForDynamicStatusByZone(final Date baseDate, final String zoneCode,final String cutOff, final boolean isDynamic) throws SQLException {

		Connection connection=null;
		int result = 0;
		StringBuffer strUpdateQuery = new StringBuffer();
		strUpdateQuery.append(UPDATE_TIMESLOTFORDYNAMICSTATUSBYZONE_QRY);
		try{
			if(cutOff == null || cutOff.trim().length() == 0) {
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isDynamic ? "X" : null), baseDate, zoneCode});
			} else {
				strUpdateQuery.append(UPDATE_TIMESLOTCUTUFF_QRYAPPEND);
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isDynamic ? "X" : null), baseDate, zoneCode, cutOff});
			}			
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	private static String FETCH_UNASSIGNED_RESERVATIONS_QUERY="SELECT  R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID, "+
	" T.BASE_DATE,to_char(T.CUTOFF_TIME, 'HH:MI AM') CUTOFF_TIME,T.START_TIME STIME, T.END_TIME ETIME, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE" +
	", R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
	", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES, R.CLASS,R.BUILDINGID, R.LOCATIONID, R.PREV_BLDG_RSV_CNT, " +
	" A.ID as ADDRESS, A.FIRST_NAME,A.LAST_NAME,A.ADDRESS1,A.ADDRESS2,A.APARTMENT,A.CITY,A.STATE,A.ZIP,A.COUNTRY, "+
	" A.PHONE,A.PHONE_EXT,A.DELIVERY_INSTRUCTIONS,A.SCRUBBED_ADDRESS,A.ALT_DEST,A.ALT_FIRST_NAME, "+
	" A.ALT_LAST_NAME,A.ALT_APARTMENT,A.ALT_PHONE,A.ALT_PHONE_EXT,A.LONGITUDE,A.LATITUDE,A.SERVICE_TYPE, "+
	" A.COMPANY_NAME,A.ALT_CONTACT_PHONE,A.ALT_CONTACT_EXT,A.UNATTENDED_FLAG,A.UNATTENDED_INSTR,A.CUSTOMER_ID, RG.SERVICE_TYPE REGION_SVC_TYPE  "+
	" FROM DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z,CUST.ADDRESS A, DLV.REGION_DATA RD, DLV.REGION RG  "+
	" WHERE R.ADDRESS_ID=A.ID(+) AND R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID AND Z.REGION_DATA_ID = RD.ID AND RD.REGION_ID = RG.ID AND t.BASE_DATE=TRUNC(?) " +
	" AND (unassigned_action IS NOT NULL OR (UPDATE_STATUS IS NOT NULL AND UPDATE_STATUS <> 'SUS')) " +
	" AND ((t.premium_cutoff_time is null and to_char(t.cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM')) or " +
	"(t.premium_cutoff_time is not null and to_char(t.premium_cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM'))) ";
	
	public List<UnassignedDlvReservationModel> getUnassignedReservations(final Date deliveryDate, final Date cutOff) throws SQLException {
	
		final List<UnassignedDlvReservationModel>  reservations = new ArrayList<UnassignedDlvReservationModel>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(FETCH_UNASSIGNED_RESERVATIONS_QUERY);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new java.sql.Timestamp(cutOff.getTime()));
				ps.setTimestamp(3, new java.sql.Timestamp(cutOff.getTime()));
				
				return ps;
			}  
		};
		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do { 
					UnassignedDlvReservationModel reservation = new UnassignedDlvReservationModel(
							new PrimaryKey(rs.getString("ID")),
							rs.getString("ORDER_ID"),
							rs.getString("CUSTOMER_ID"),
							rs.getInt("STATUS_CODE"),
							rs.getTimestamp("EXPIRATION_DATETIME"),
							rs.getString("TIMESLOT_ID"),
							rs.getString("ZONE_ID"),
							EnumReservationType.getEnum(rs.getString("TYPE")),
							rs.getString("ADDRESS")!=null?getAddress(rs):null,
							rs.getDate("BASE_DATE"),
							rs.getString("CUTOFF_TIME"),rs.getTimestamp("stime"), rs.getTimestamp("etime"),
							rs.getString("ZONE_CODE"),
							com.freshdirect.routing.constants.RoutingActivityType.getEnum( rs.getString("UNASSIGNED_ACTION")) ,
							"X".equalsIgnoreCase(rs.getString("IN_UPS"))?true:false
							, rs.getBigDecimal("ORDER_SIZE") != null ? new Double(rs.getDouble("ORDER_SIZE")) : null
							, rs.getBigDecimal("SERVICE_TIME") != null ? new Double(rs.getDouble("SERVICE_TIME")) : null
							, rs.getBigDecimal("RESERVED_ORDER_SIZE") != null ? new Double(rs.getDouble("RESERVED_ORDER_SIZE")) : null
							, rs.getBigDecimal("RESERVED_SERVICE_TIME") != null ? new Double(rs.getDouble("RESERVED_SERVICE_TIME")) : null
							, rs.getBigDecimal("NUM_CARTONS") != null ? new Long(rs.getLong("NUM_CARTONS")) : null
							, rs.getBigDecimal("NUM_CASES") != null ? new Long(rs.getLong("NUM_CASES")) : null
							, rs.getBigDecimal("NUM_FREEZERS") != null ? new Long(rs.getLong("NUM_FREEZERS")) : null
							, EnumReservationClass.getEnum(rs.getString("CLASS"))
							, EnumRoutingUpdateStatus.getEnum(rs.getString("UPDATE_STATUS"))
							, EnumOrderMetricsSource.getEnum(rs.getString("METRICS_SOURCE"))
							, rs.getString("BUILDINGID"),rs.getString("LOCATIONID"),rs.getInt("PREV_BLDG_RSV_CNT"), EnumRegionServiceType.getEnum(rs.getString("REGION_SVC_TYPE")));
							
					reservations.add(reservation);
				}while(rs.next());
			}
		});

		return reservations;
		
	}
	
	private static String FETCH_UNASSIGNED_RESERVATIONS_QUERY_EX="SELECT  R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID, "+
	" T.BASE_DATE,to_char(T.CUTOFF_TIME, 'HH:MI AM') CUTOFF_TIME,T.START_TIME STIME, T.END_TIME ETIME, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE" +
	", R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
	", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES,  R.CLASS,R.BUILDINGID, R.LOCATIONID, R.PREV_BLDG_RSV_CNT, " +
	" A.ID as ADDRESS, A.FIRST_NAME,A.LAST_NAME,A.ADDRESS1,A.ADDRESS2,A.APARTMENT,A.CITY,A.STATE,A.ZIP,A.COUNTRY, "+
	" A.PHONE,A.PHONE_EXT,A.DELIVERY_INSTRUCTIONS,A.SCRUBBED_ADDRESS,A.ALT_DEST,A.ALT_FIRST_NAME, "+
	" A.ALT_LAST_NAME,A.ALT_APARTMENT,A.ALT_PHONE,A.ALT_PHONE_EXT,A.LONGITUDE,A.LATITUDE,A.SERVICE_TYPE, "+
	" A.COMPANY_NAME,A.ALT_CONTACT_PHONE,A.ALT_CONTACT_EXT,A.UNATTENDED_FLAG,A.UNATTENDED_INSTR,A.CUSTOMER_ID, RG.SERVICE_TYPE REGION_SVC_TYPE  "+
	" FROM DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z,CUST.ADDRESS A, DLV.REGION_DATA RD, DLV.REGION RG   "+
	" WHERE R.ADDRESS_ID=A.ID(+) AND R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID AND Z.REGION_DATA_ID = RD.ID AND RD.REGION_ID = RG.ID AND t.BASE_DATE=TRUNC(?) " +
	" AND (r.unassigned_action IS NOT NULL) AND r.status_code = '10'" +
	" AND ((t.premium_cutoff_time is null and to_char(t.cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM')) or " +
	"(t.premium_cutoff_time is not null and to_char(t.premium_cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM'))) ";
	
	public List<UnassignedDlvReservationModel> getUnassignedReservationsEx(final Date deliveryDate, final Date cutOff) throws SQLException {
	
		final List<UnassignedDlvReservationModel>  reservations = new ArrayList<UnassignedDlvReservationModel>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(FETCH_UNASSIGNED_RESERVATIONS_QUERY_EX);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new java.sql.Timestamp(cutOff.getTime()));
				ps.setTimestamp(3, new java.sql.Timestamp(cutOff.getTime()));
				
				return ps;
			}  
		};
		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do { 
					UnassignedDlvReservationModel reservation = new UnassignedDlvReservationModel(
							new PrimaryKey(rs.getString("ID")),
							rs.getString("ORDER_ID"),
							rs.getString("CUSTOMER_ID"),
							rs.getInt("STATUS_CODE"),
							rs.getTimestamp("EXPIRATION_DATETIME"),
							rs.getString("TIMESLOT_ID"),
							rs.getString("ZONE_ID"),
							EnumReservationType.getEnum(rs.getString("TYPE")),
							rs.getString("ADDRESS")!=null?getAddress(rs):null,
							rs.getDate("BASE_DATE"),
							rs.getString("CUTOFF_TIME"),rs.getTimestamp("stime"), rs.getTimestamp("etime"),
							rs.getString("ZONE_CODE"),
							com.freshdirect.routing.constants.RoutingActivityType.getEnum( rs.getString("UNASSIGNED_ACTION")) ,
							"X".equalsIgnoreCase(rs.getString("IN_UPS"))?true:false
							, rs.getBigDecimal("ORDER_SIZE") != null ? new Double(rs.getDouble("ORDER_SIZE")) : null
							, rs.getBigDecimal("SERVICE_TIME") != null ? new Double(rs.getDouble("SERVICE_TIME")) : null
							, rs.getBigDecimal("RESERVED_ORDER_SIZE") != null ? new Double(rs.getDouble("RESERVED_ORDER_SIZE")) : null
							, rs.getBigDecimal("RESERVED_SERVICE_TIME") != null ? new Double(rs.getDouble("RESERVED_SERVICE_TIME")) : null
							, rs.getBigDecimal("NUM_CARTONS") != null ? new Long(rs.getLong("NUM_CARTONS")) : null
							, rs.getBigDecimal("NUM_CASES") != null ? new Long(rs.getLong("NUM_CASES")) : null
							, rs.getBigDecimal("NUM_FREEZERS") != null ? new Long(rs.getLong("NUM_FREEZERS")) : null
							, EnumReservationClass.getEnum(rs.getString("CLASS"))
							, EnumRoutingUpdateStatus.getEnum(rs.getString("UPDATE_STATUS"))
							, EnumOrderMetricsSource.getEnum(rs.getString("METRICS_SOURCE"))
							, rs.getString("BUILDINGID"),rs.getString("LOCATIONID"),rs.getInt("PREV_BLDG_RSV_CNT"), EnumRegionServiceType.getEnum(rs.getString("REGION_SVC_TYPE")));
							
					reservations.add(reservation);
				}while(rs.next());
			}
		});

		return reservations;
		
	}
	
	
	private static String FETCH_DEPOTS ="SELECT L.*, D.PICKUP, D.CORPORATE_DEPOT, D.DEPOT_CODE FROM DLV.DEPOT D, DLV.LOCATION L WHERE L.DEPOT_ID = D.ID";
	
	public Map<String, DepotLocationModel> getDepotLocations() throws SQLException {
		
		final Map<String, DepotLocationModel>  depotLocations = new HashMap<String, DepotLocationModel>();
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
				PreparedStatement ps =
					connection.prepareStatement(FETCH_DEPOTS);
				
				return ps;
			}  
		};
		jdbcTemplate.query(creator, 
				new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do { 
					DepotLocationModel depotLocation = new DepotLocationModel(rs.getString("DEPOT_CODE"), 
							new AddressModel(rs.getString("ADDRESS1"), rs.getString("APARTMENT"), rs.getString("CITY"), rs.getString("STATE"), 
									 rs.getString("ZIPCODE")));
					depotLocations.put(rs.getString("ID"), depotLocation);
				}while(rs.next());
			}
		});

		return depotLocations;
		
	}
	
	
	
	
	private static PhoneNumber convertPhoneNumber(String phone, String extension) {
		return "() -".equals(phone) ? null : new PhoneNumber(phone, NVL.apply(extension, ""));
	}
	private static ErpAddressModel getAddress(ResultSet rs) throws SQLException{
		
		ErpAddressModel model=new ErpAddressModel();
		model.setFirstName(rs.getString("FIRST_NAME"));
		model.setLastName(rs.getString("LAST_NAME"));
		model.setAddress1(rs.getString("ADDRESS1"));
		model.setAddress2(NVL.apply(rs.getString("ADDRESS2"), "").trim());
		model.setApartment(NVL.apply(rs.getString("APARTMENT"), "").trim());
		model.setCity(rs.getString("CITY"));
		model.setState(rs.getString("STATE"));
		model.setZipCode(rs.getString("ZIP"));
		model.setCountry(rs.getString("COUNTRY"));
		model.setPhone(convertPhoneNumber( rs.getString("PHONE"), rs.getString("PHONE_EXT") ));
		model.setAltContactPhone(convertPhoneNumber( rs.getString("ALT_PHONE"), rs.getString("ALT_PHONE_EXT") ));
		model.setInstructions(rs.getString("DELIVERY_INSTRUCTIONS"));
		model.setServiceType(EnumServiceType.getEnum(rs.getString("SERVICE_TYPE")));
		model.setCompanyName(rs.getString("COMPANY_NAME"));
		model.setAltDelivery(EnumDeliverySetting.getDeliverySetting(rs.getString("ALT_DEST")));
		model.setAltFirstName(rs.getString("ALT_FIRST_NAME"));
		model.setAltLastName(rs.getString("ALT_LAST_NAME"));
		model.setAltApartment(rs.getString("ALT_APARTMENT"));
		model.setAltPhone(convertPhoneNumber(rs.getString("ALT_CONTACT_PHONE"), rs.getString("ALT_CONTACT_EXT")));		
		model.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.fromSQLValue(rs.getString("UNATTENDED_FLAG")));
		model.setUnattendedDeliveryInstructions(rs.getString("UNATTENDED_INSTR"));
		model.setCustomerId(rs.getString("CUSTOMER_ID"));
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setLongitude(Double.parseDouble((rs.getBigDecimal("LONGITUDE")!=null)?rs.getBigDecimal("LONGITUDE").toString():"0"));
		addressInfo.setLatitude(Double.parseDouble((rs.getBigDecimal("LATITUDE")!=null)?rs.getBigDecimal("LATITUDE").toString():"0"));
		model.setAddressInfo(addressInfo);
		return model;
		
	}
	public int updateTimeslotForDynamicStatusByRegion(final Date baseDate, final String regionCode,final String cutOff, final boolean isDynamic) throws SQLException {

		Connection connection=null;
		int result = 0;
		StringBuffer strUpdateQuery = new StringBuffer();
		strUpdateQuery.append(UPDATE_TIMESLOTFORDYNAMICSTATUSBYREGION_QRY);
		try{
			if(cutOff == null || cutOff.trim().length() == 0) {
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isDynamic ? "X" : null), baseDate, regionCode});
			} else {
				strUpdateQuery.append(UPDATE_TIMESLOTCUTUFF_QRYAPPEND);
				result = this.jdbcTemplate.update(strUpdateQuery.toString(), new Object[] {(isDynamic ? "X" : null), baseDate, regionCode, cutOff});
			}
			
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		return result;
	}
	
	public List<IDeliverySlot> getTimeslots(final Date deliveryDate, final Date cutOffTime, 
			final double latitude, final double longitude, final String serviceType) throws SQLException {
		
		final List<IDeliverySlot> timeslots = new ArrayList<IDeliverySlot>();
		
		final StringBuffer query = new StringBuffer();
		query.append(GET_TIMESLOTSBYDATEANDZONE_QRY);
		
		if(cutOffTime != null) {
			query.append(" and ((t.premium_cutoff_Time is not null and t.premium_cutoff_Time = ?) or (t.premium_cutoff_Time is null and t.CUTOFF_TIME = ?))");
		}
		query.append(" order by t.base_date, z.zone_code, t.start_time");
		
		final Calendar endDate = Calendar.getInstance();					
		endDate.setTime(deliveryDate);
		endDate.add(Calendar.DATE, 1);
		
		
		PreparedStatementCreator creator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(query.toString());
                ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
        		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
        		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
        		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
        		ps.setString(5, serviceType);
        		//ps.setDouble(6, longitude);
        		ps.setBigDecimal(6, new java.math.BigDecimal(longitude));
        		//ps.setDouble(7, latitude);
        		ps.setBigDecimal(7, new java.math.BigDecimal(latitude));
                
        		ps.setDate(8, new java.sql.Date(deliveryDate.getTime()));
        		ps.setDate(9, new java.sql.Date(endDate.getTime().getTime()));
        		ps.setDate(10, new java.sql.Date(deliveryDate.getTime()));
        		ps.setDate(11, new java.sql.Date(endDate.getTime().getTime()));
                
        		if(cutOffTime != null) {
                	ps.setTimestamp(12, new java.sql.Timestamp(cutOffTime.getTime()));
                	ps.setTimestamp(13, new java.sql.Timestamp(cutOffTime.getTime()));
                }
                return ps;
            }  
        };
        
		jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		
				    		IDeliverySlot tmpModel = new DeliverySlot();
				    		tmpModel.setReferenceId(rs.getString("REF_ID"));
				    		
				    		tmpModel.setDisplayStartTime(rs.getTimestamp("START_TIME"));
				    		tmpModel.setDisplayStopTime(rs.getTimestamp("END_TIME"));
				    		
				    		tmpModel.setRoutingStartTime(rs.getTimestamp("ROUTING_START_TIME"));
				    		tmpModel.setRoutingStopTime(rs.getTimestamp("ROUTING_END_TIME"));
				    		
				    		tmpModel.setStartTime((tmpModel.getRoutingStartTime()!=null)?tmpModel.getRoutingStartTime():tmpModel.getDisplayStartTime());
				    		tmpModel.setStopTime((tmpModel.getRoutingStopTime()!=null)?tmpModel.getRoutingStopTime():tmpModel.getDisplayStopTime());
				    		
				    		
				    		tmpModel.setZoneCode(rs.getString("ZONE_CODE"));
				    		tmpModel.setWaveCode(rs.getString("WAVE_CODE"));
				    		tmpModel.setDynamicActive("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")) ? true : false);
				    		tmpModel.setManuallyClosed("X".equalsIgnoreCase(rs.getString("IS_CLOSED")) ? true : false);				    		
				    					    						    		
				    		IRoutingSchedulerIdentity _schId = new RoutingSchedulerIdentity();
							_schId.setDeliveryDate(deliveryDate);
							
							IAreaModel _aModel = new AreaModel();
							_aModel.setAreaCode(rs.getString("AREA_CODE"));
							_aModel.setPostTripTime(rs.getInt("POST_TRIP"));
							_aModel.setPreTripTime(rs.getInt("PRE_TRIP"));							
						
							IRegionModel _rModel = new RegionModel();
         					_rModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
         					_rModel.setRegionCode(rs.getString("REGION_CODE"));
         					_rModel.setName(rs.getString("REGION_NAME"));
         					_rModel.setDescription(rs.getString("REGION_DESCR"));
         					_aModel.setRegion(_rModel);
         					
							_schId.setRegionId(RoutingUtil.getRegion(_aModel));
							_schId.setArea(_aModel);
							
							tmpModel.setSchedulerId(_schId);
							
							timeslots.add(tmpModel);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return timeslots;
	}


	private static final String MARK_RESERVATION_ASSIGNED_QUERY="UPDATE DLV.RESERVATION SET UNASSIGNED_DATETIME=null, UNASSIGNED_ACTION=null, MODIFIED_DTTM=SYSDATE WHERE ID=?";
	public void clearUnassignedInfo(String reservationId)  throws SQLException {
		
		
		Connection connection=null;
		int result;
		try{
			result = this.jdbcTemplate.update(MARK_RESERVATION_ASSIGNED_QUERY, new Object[] {reservationId});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		if(result != 1) 
			throw new SQLException("Cannot find reservation to clear unasssignedInfo for id: " + reservationId);
	}
	
	
	
	private static final String UPDATE_RESERVATIONSOURCE_QUERY = "UPDATE DLV.RESERVATION SET NUM_CARTONS = ? " +
																	", NUM_FREEZERS = ? , NUM_CASES = ?" +
																		" , METRICS_SOURCE = ? WHERE ID=?";
	public void setReservationMetricsDetails(String reservationId
													, long noOfCartons, long noOfCases, long noOfFreezers
													, EnumOrderMetricsSource source)  throws SQLException {
		Connection connection=null;
		int result;
		try{
			result = this.jdbcTemplate.update(UPDATE_RESERVATIONSOURCE_QUERY, new Object[] {new java.math.BigDecimal(noOfCartons),new java.math.BigDecimal(noOfFreezers),
					new java.math.BigDecimal(noOfCases), (source != null ? source.value() : null),reservationId });
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		if(result != 1) 
			throw new SQLException("Cannot find reservation to METRICS UPDATE STATUS for id: " + reservationId);
	}

	private static final String UNASSIGN_RESERVATION_QUERY="UPDATE DLV.RESERVATION SET UNASSIGNED_DATETIME=SYSDATE, MODIFIED_DTTM=SYSDATE, UNASSIGNED_ACTION=? WHERE ID=?";
	
	public void setUnassignedInfo(String reservationId,
			String action) throws SQLException {
		
		Connection connection=null;
		int result;
		try{
			result = this.jdbcTemplate.update(UNASSIGN_RESERVATION_QUERY, new Object[] {action,reservationId });
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		if(result != 1) 
			throw new SQLException("Cannot find reservation to UNASSIGN for id: " + reservationId);
	}

	private static final String SET_RESERVATION_SET_TO_UPS_FLAG_QUERY = "UPDATE DLV.RESERVATION SET IN_UPS='X', ROUTING_ORDER_ID=? WHERE ID=?";
	
	@Override
	public void setInUPS(String reservationId, String orderNum) throws SQLException {
	
		Connection connection=null;
		int result;
		try{
			result = this.jdbcTemplate.update(SET_RESERVATION_SET_TO_UPS_FLAG_QUERY, new Object[] {orderNum,reservationId });
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		if(result != 1) 
			throw new SQLException("Cannot find reservation to set IN_UPS for id: " + reservationId);
	}
	
	private static final String UPDATE_RESERVATIONMETRICSSTATUS_QUERY = "UPDATE DLV.RESERVATION SET RESERVED_ORDER_SIZE = ?" +
			"																, RESERVED_SERVICE_TIME = ?, UPDATE_STATUS = ? WHERE ID=?";

	public void setReservationReservedMetrics(String reservationId,
			double reservedOrderSize, double reservedServiceTime,
			String status) throws SQLException {
		
		Connection connection=null;
		int result;
		try{
			result = this.jdbcTemplate.update(UPDATE_RESERVATIONMETRICSSTATUS_QUERY, new Object[] { new java.math.BigDecimal(reservedOrderSize),new java.math.BigDecimal(reservedServiceTime),
					status,reservationId });
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		if(result != 1) 
			throw new SQLException("Cannot find reservation to METRICS UPDATE STATUS for id: " + reservationId);
	}

	private static final String UPDATE_RESERVATIONSTATUSONLY_QUERY = "UPDATE DLV.RESERVATION SET UPDATE_STATUS = ? WHERE ID=?";
	
	public void setReservationMetricsStatus(String reservationId, String status) throws SQLException {
		
		
		Connection connection=null;
		int result;
		try{
			result = this.jdbcTemplate.update(UPDATE_RESERVATIONSTATUSONLY_QUERY, new Object[] {status,reservationId });
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
		if(result != 1) 
			throw new SQLException("Cannot find reservation to METRICS UPDATE STATUS for id: " + reservationId);
	
		
	}
	

	private static final String FLAG_EXPIRED_RESERVATIONS_QUERY = "update DLV.reservation set status_code = 20, UNASSIGNED_DATETIME = sysdate,UNASSIGNED_ACTION = 'CANCEL_TIMESLOT'," +
			" MODIFIED_DTTM = sysdate where expiration_datetime <= sysdate and status_code = 5 and UNASSIGNED_ACTION is null and IN_UPS = 'X'";
	@Override
	public void flagExpiredReservations() throws SQLException {
		
		Connection connection=null;
		try{
			this.jdbcTemplate.update(FLAG_EXPIRED_RESERVATIONS_QUERY);
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
	}

	private static final String UPDATE_TIMESLOT_METRICS = " UPDATE DLV.TIMESLOT T set T.CAPACITY = ?, T.CT_CAPACITY = ?, T.PREMIUM_CAPACITY = ?, T.PREMIUM_CT_CAPACITY = ? "
			+ " WHERE T.ID IN (SELECT TS.ID FROM DLV.TIMESLOT TS, DLV.ZONE Z WHERE Z.ID = TS.ZONE_ID AND TS.BASE_DATE = ? AND TS.START_TIME = ? AND TS.END_TIME = ? AND Z.ZONE_CODE = ? ) ";

	public void updateTimeslotMetrics(List<TimeslotCapacityModel> timeslotMetrics) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),	UPDATE_TIMESLOT_METRICS);
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));			
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.NUMERIC));
			batchUpdater.declareParameter(new SqlParameter(Types.DATE));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.TIMESTAMP));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			batchUpdater.compile();
			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			
			for(TimeslotCapacityModel _tsMetric : timeslotMetrics) {				
				batchUpdater.update(new Object[] { _tsMetric.getCapacity(),
													_tsMetric.getChefsTableCapacity(),
													_tsMetric.getPremiumCapacity(),
													_tsMetric.getPremiumCtCapacity(),
													_tsMetric.getBaseDate(),
													_tsMetric.getStartTime(),
													_tsMetric.getEndTime(),
													_tsMetric.getArea()
				});
			}	
			batchUpdater.flush();
		} finally {
			if (connection != null)
				connection.close();
		}
	}
}
