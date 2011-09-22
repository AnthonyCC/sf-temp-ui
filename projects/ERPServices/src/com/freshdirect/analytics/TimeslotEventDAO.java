package com.freshdirect.analytics;

/**
 * 
 * @author tbalumuri
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Category;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;


public class TimeslotEventDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(TimeslotEventDAO.class);
	private static final String TIMESLOT_LOG_INSERT="INSERT INTO DLV.TIMESLOT_EVENT_HDR (ID, EVENT_DTM,RESERVATION_ID, " +
			"ORDER_ID, CUSTOMER_ID, EVENTTYPE,RESPONSE_TIME,COMMENTS,TransactionSource,DlvPassApplied,DeliveryCharge,isDeliveryChargeWaived,zonectactive) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String TIMESLOT_LOG_DTL_INSERT="INSERT INTO DLV.TIMESLOT_EVENT_DTL (TIMESLOT_LOG_ID, BASE_DATE, START_TIME" +
			", END_TIME, ZONE_CODE) VALUES (?,?,?,?,?)";
	
	private static final String TIMESLOT_LOG_DTL_WITH_COST_INSERT="INSERT INTO DLV.TIMESLOT_EVENT_DTL (TIMESLOT_LOG_ID, BASE_DATE, START_TIME, " +
			"END_TIME, ADD_DISTANCE, ADD_RUNTIME, ADD_STOPCOST, CAPACITY, COSTPERMILE, FIXED_RT_COST, MAXRUNTIME,"+
	"OT_HOURLY_WAGE,PERCENT_AVAIL,PREF_RUNTIME , REG_HOURLY_WAGE, REG_WAGE_SECS, ROUTE_ID ,STOP_SEQ ,TOTAL_DISTANCE, TOTAL_PU_QTY" +
	", TOTAL_QTY, TOTAL_ROUTE_COST , TOTAL_RUNTIME ,  TOTAL_SVC_TIME,TOTAL_TRAVEL_TIME,  TOTAL_WAIT_TIME,  IS_AVAIL " +
	",  IS_FILTERED ,  IS_MISSED_TW,ZONE_CODE, ws_amount,alcohol_restriction,holiday_restriction,ecofriendlyslot,neighbourhoodslot" +
	", totalCapacity,CTCapacity,Manually_Closed,cutoff, storefront_avl,CT_Allocated,Total_Allocated, WAVE_VEHICLES,WAVE_VEHICLES_IN_USE," +
	"WAVE_STARTTIME,UNAVAILABILITY_REASON,WAVE_ORDERS_TAKEN,TOTAL_QUANTITIES, NEWROUTE, CAPACITIES, GEORESTRICTED)" +
	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
	private static final String TIMESLOT_EVENT_DETAIL_QRY = "SELECT * FROM DLV.TIMESLOT_EVENT_DTL WHERE TIMESLOT_LOG_ID = ?";
	
	private static final String EVENTS_QRY = "select * from dlv.session_event s where s.logout_time between ? and ?";
	
	private static final String TIMESLOT_EVENTS_CUSTOMER_QRY = " select * from dlv.timeslot_event_hdr where id in  (select max(to_number(id)) from dlv.timeslot_event_hdr where event_dtm" +
	" between to_date(?,  'MM-DD-YYYY HH12:MI:SS AM') and to_date(?,  'MM-DD-YYYY HH12:MI:SS AM') and customer_id = ? and " +
	"transactionsource = 'WEB' group by eventtype)";
	
	private static final String ORDER_EVENTS_QRY = "SELECT S.CUSTOMER_ID, S.CROMOD_DATE, SA.REQUESTED_DATE FROM cust.sale s, cust.salesaction sa, DLV.SESSION_EVENT se" +
			" WHERE s.ID=sa.SALE_ID AND se.CUSTOMER_ID = s.CUSTOMER_ID AND se.LOGOUT_TIME BETWEEN ? AND ? " +
			" AND s.CUSTOMER_ID=sa.CUSTOMER_ID AND s.CROMOD_DATE=sa.ACTION_DATE AND s.CROMOD_DATE BETWEEN se.LOGIN_TIME AND se.LOGOUT_TIME " +
			"AND sa.ACTION_TYPE IN ('CRO','MOD') AND sa.REQUESTED_DATE > TRUNC(SYSDATE)" +
			" AND s.type='REG'";
	
	private static final String CANCEL_RESERVATION_EVENTS_QRY = "SELECT H1.*, D1.ZONE_CODE, D1.CUTOFF, D1.BASE_DATE, D1.TIMESLOT_LOG_ID FROM DLV.TIMESLOT_EVENT_HDR H1, DLV.TIMESLOT_EVENT_DTL D1 WHERE" +
			" D1.TIMESLOT_LOG_ID = H1.ID AND H1.EVENT_DTM BETWEEN SYSDATE-1/48 AND SYSDATE AND H1.TRANSACTIONSOURCE = 'SYS' AND " +
			"H1.EVENTTYPE = 'CANCEL_TIMESLOT'";
	
	public static List getEvents(Connection conn, Date startTime, Date endTime) throws SQLException
	{
		PreparedStatement ps = null; ResultSet rs = null;
		List events = new ArrayList();
		
		try
		{
		ps = conn.prepareStatement(EVENTS_QRY);
		ps.setTimestamp(1, new java.sql.Timestamp(startTime.getTime()));
		ps.setTimestamp(2, new java.sql.Timestamp(endTime.getTime()));
		
		rs = ps.executeQuery();
		while(rs.next()){
			
			SessionEvent event = new SessionEvent();
			event.setCustomerId(rs.getString("customer_id"));
			event.setLoginTime(rs.getTimestamp("login_time"));
			event.setLogoutTime(rs.getTimestamp("logout_time"));
			event.setCutOff(rs.getTimestamp("cutoff"));
			event.setZone(rs.getString("zone"));
			event.setLastTimeslot(rs.getString("last_get_timeslot"));
			event.setIsTimeout(rs.getString("is_timeout"));
			event.setAvailCount(rs.getInt("avail_count"));
			event.setSoldCount(rs.getInt("sold_count"));
			event.setOrderPlaced(rs.getString("order_placed"));
			event.setPageType(rs.getString("last_gettype"));
			events.add(event);
		}
		
		}
		catch(SQLException e)
		{
			LOGGER.warn("Exception while getEvents  ", e);
		}
		finally
		{
			try
			{
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
			}
			catch(SQLException e)
			{
				LOGGER.warn("Exception while cleaning up   ", e);
			}
		}
		return events;
	}
	public static List getCancelledReservationEvents(Connection conn)
	{
		PreparedStatement ps = null; ResultSet rs = null;
		List events = new ArrayList();
		
		try
		{
		ps = conn.prepareStatement(CANCEL_RESERVATION_EVENTS_QRY);
		rs = ps.executeQuery();
		while(rs.next()){
			TimeslotEventModel event = new TimeslotEventModel();
			event.setOrderId(rs.getString("order_id"));
			event.setCustomerId(rs.getString("customer_Id"));
			event.setEventType(EventType.getEnum(rs.getString("eventtype")));
			event.setId(rs.getString("id"));
			event.setResponseTime(rs.getInt("response_time"));
			event.setReservationId(rs.getString("reservation_id"));
			event.setEventDate(rs.getTimestamp("event_dtm"));	
			event.setTransactionSource(rs.getString("transactionsource"));
			TimeslotEventDetailModel eventD = new TimeslotEventDetailModel();
			eventD.setDeliveryDate(rs.getDate("base_date"));
			eventD.setCutOff(rs.getTimestamp("cutoff"));
			eventD.setZoneCode(rs.getString("zone_code"));
			eventD.setId(rs.getString("timeslot_log_id"));
			List<TimeslotEventDetailModel> eventDL = new ArrayList<TimeslotEventDetailModel>();
			eventDL.add(eventD);
			event.setDetail(eventDL);
			events.add(event);
		}
		}
		catch(SQLException e)
		{
			LOGGER.warn("Exception while getEvents  ", e);
		}
		finally
		{
			try
			{
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				
			}
			catch(SQLException e)
			{
				LOGGER.warn("Exception while cleaning up   ", e);
			}
		}
		return events;
	}
	public static List getOrders(Connection conn, Date startTime, Date endTime)
	{
		PreparedStatement ps = null; ResultSet rs = null;
		List events = new ArrayList();
		try
		{
			ps = conn.prepareStatement(ORDER_EVENTS_QRY);
			ps.setTimestamp(1, new java.sql.Timestamp(startTime.getTime()));
			ps.setTimestamp(2, new java.sql.Timestamp(endTime.getTime()));
			
			rs = ps.executeQuery();
			while(rs.next()){
			OrderEvent event = new OrderEvent();
			event.setCustomerId(rs.getString("customer_id"));
			event.setCreateDate(rs.getTimestamp("cromod_date"));
			event.setDeliveryDate(rs.getTimestamp("requested_date"));
			events.add(event);
			}
		}
			catch(SQLException e)
			{
				LOGGER.warn("Exception while getEvents  ", e);
			}
			finally
			{
				try
				{
					if(rs!=null) rs.close();
					if(ps!=null) ps.close();
					
				}
				catch(SQLException e)
				{
					LOGGER.warn("Exception while cleaning up   ", e);
				}
			}
		return events;
	}
	
	public static List getEvents(Connection conn, String customerId, long sessionCreationTime) throws SQLException
	{
		PreparedStatement ps = null; ResultSet rs = null;
		List events = new ArrayList();
		try
		{
		ps = conn.prepareStatement(TIMESLOT_EVENTS_CUSTOMER_QRY);
		DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a"); //E, dd MMM yyyy HH:mm:ss Z
		Date currentTime = new Date();
		Timestamp date = new Timestamp(sessionCreationTime);
		String currentTimeStr = formatter.format(currentTime);
		String sessionCreationStr = formatter.format(date);
		ps.setString(1, sessionCreationStr);
		ps.setString(2, currentTimeStr);
		ps.setString(3, customerId);
		
		rs = ps.executeQuery();
		
		while(rs.next()){
			TimeslotEventModel event = new TimeslotEventModel();
			event.setOrderId(rs.getString("order_id"));
			event.setCustomerId(rs.getString("customer_Id"));
			event.setEventType(EventType.getEnum(rs.getString("eventtype")));
			event.setId(rs.getString("id"));
			event.setResponseTime(rs.getInt("response_time"));
			event.setReservationId(rs.getString("reservation_id"));
			event.setEventDate(rs.getTimestamp("event_dtm"));	
			event.setLogoutTime(date.getTime());
			event.setDetail(getEventDetails(conn, rs.getString("id")));
			events.add(event);
		}
		}
		catch(SQLException e)
		{
			LOGGER.warn("Exception while getEvents  ", e);
		}
		finally
		{
			try
			{
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				
			}
			catch(SQLException e)
			{
				LOGGER.warn("Exception while cleaning up   ", e);
			}
		}
		return events;
	}
	
	
	private static List<TimeslotEventDetailModel> getEventDetails(Connection conn, String id) throws SQLException
	{
		PreparedStatement ps = null; ResultSet rs = null;
		List<TimeslotEventDetailModel> details = new ArrayList<TimeslotEventDetailModel>();
		try
		{
		ps = conn.prepareStatement(TIMESLOT_EVENT_DETAIL_QRY);
		ps.setString(1, id);
		rs = ps.executeQuery();
		
		while(rs.next())
		{
			TimeslotEventDetailModel detail = new TimeslotEventDetailModel();
			detail.setStoreFrontAvailable(rs.getString("storefront_avl"));
			detail.setDeliveryDate(rs.getDate("base_date"));
			detail.setCutOff(rs.getTimestamp("cutoff"));
			detail.setZoneCode(rs.getString("zone_code"));
			detail.setId(rs.getString("timeslot_log_id"));
			
			details.add(detail);
		}
		}
		catch(SQLException e)
		{
			LOGGER.warn("Exception while getEvents  ", e);
		}
		finally
		{
			try
			{
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				
			}
			catch(SQLException e)
			{
				LOGGER.warn("Exception while cleaning up   ", e);
			}
		}
		return details;
	}
	public static void addEntry(Connection conn,TimeslotEventModel event) 
										throws SQLException {
		PreparedStatement ps = conn.prepareStatement(TIMESLOT_LOG_INSERT);
		
		if(event != null)
		{
			ps.setString(1, event.getId());
			ps.setTimestamp(2, new java.sql.Timestamp(event.getEventDate().getTime()));
			if(event.getReservationId()==null || "".equals(event.getReservationId())) {
				ps.setNull(3,java.sql.Types.VARCHAR);
			}
			else {
				ps.setString(3,event.getReservationId());
			}
			ps.setString(4, event.getOrderId());
		    ps.setString(5, event.getCustomerId());
		    ps.setString(6, event.getEventType().value());
		    ps.setInt(7, event.getResponseTime());
		    ps.setString(8, event.getAddress());
	   
	    	ps.setString(9, event.getTransactionSource());
	    	ps.setString(10,(event.isDlvPassApplied())?"Y":"N");
	    	ps.setDouble(11, event.getDeliveryCharge());
	    	ps.setString(12, (event.isDeliveryChargeWaived())?"Y":"N");
	    	ps.setString(13, (event.isZoneCtActive())?"Y":"N");
	    	
	    	ps.execute();
	 	    ps.close();
	    } else {
	    	return;
	    }
		
		if(event.getDetail() != null) {
		    boolean isAnalyseCall=isAnalyzeCall(event.getEventType());
		    if(isAnalyseCall) {
		    	ps=conn.prepareStatement(TIMESLOT_LOG_DTL_WITH_COST_INSERT);
		    } else {
		      	ps=conn.prepareStatement(TIMESLOT_LOG_DTL_INSERT);
		    }
		    for (TimeslotEventDetailModel eventD : event.getDetail())  {
		    	if(eventD!=null) {
		    		ps.setString(1,event.getId());

		    		if(eventD.getDeliveryDate()!=null)
		    			ps.setTimestamp(2, new java.sql.Timestamp(DateUtil.truncate(eventD.getDeliveryDate()).getTime()));
		    		if(eventD.getStartTime()!=null)
		    			ps.setTimestamp(3, new java.sql.Timestamp(eventD.getStartTime().getTime()));
		    		if(eventD.getStopTime()!=null)
		    			ps.setTimestamp(4, new java.sql.Timestamp(eventD.getStopTime().getTime()));

		    		if(isAnalyseCall ) {

		    			RoutingModel cost=eventD.getRoutingModel();

		    			if(cost!=null) {
		    				ps.setInt(5, cost.getAdditionalDistance());
		    				ps.setInt(6, cost.getAdditionalRunTime());
		    				ps.setInt(7, cost.getAdditionalStopCost());
		    				ps.setInt(8, cost.getCapacity());
		    				ps.setInt(9, cost.getCostPerMile());
		    				ps.setInt(10, cost.getFixedRouteSetupCost());
		    				ps.setInt(11, cost.getMaxRunTime());
		    				ps.setInt(12, cost.getOvertimeHourlyWage());
		    				ps.setDouble(13, cost.getPercentageAvailable());
		    				ps.setInt(14, cost.getPrefRunTime());
		    				ps.setInt(15, cost.getRegularHourlyWage());
		    				ps.setInt(16, cost.getRegularWageDurationSeconds());
		    				ps.setInt(17, cost.getRouteId());
		    				ps.setInt(18, cost.getStopSequence());
		    				ps.setInt(19, cost.getTotalDistance());
		    				ps.setInt(20, cost.getTotalPUQuantity());
		    				ps.setInt(21, cost.getTotalQuantity());
		    				ps.setInt(22, cost.getTotalRouteCost());
		    				ps.setInt(23, cost.getTotalRunTime());
		    				ps.setInt(24, cost.getTotalServiceTime());
		    				ps.setInt(25, cost.getTotalTravelTime());
		    				ps.setInt(26, cost.getTotalWaitTime());
		    				ps.setString(27, get(cost.isAvailable()));
		    				ps.setString(28, get(cost.isFiltered()));
		    				ps.setString(29, get(cost.isMissedTW()));	
		    				ps.setInt(43, cost.getWaveVehicles());
		    				ps.setInt(44, cost.getWaveVehiclesInUse());
		    				if(cost.getWaveStartTime() != null)
		    					ps.setTimestamp(45, new Timestamp(cost.getWaveStartTime().getTime()));
		    				else
		    					ps.setNull(45, java.sql.Types.TIMESTAMP );

		    				ps.setString(46, cost.getUnavailabilityReason());
		    				ps.setInt(47, cost.getWaveOrdersTaken());
		    				ps.setDouble(48,cost.getTotalQuantities());
		    				ps.setString(49,get(cost.isNewRoute()));
		    				ps.setDouble(50,cost.getCapacities());

		    			} else {
		    				ps.setInt(5, 0);
		    				ps.setInt(6, 0);
		    				ps.setInt(7, 0);
		    				ps.setInt(8, 0);
		    				ps.setInt(9, 0);
		    				ps.setInt(10, 0);
		    				ps.setInt(11, 0);
		    				ps.setInt(12,0);
		    				ps.setInt(13, 0);
		    				ps.setDouble(14, 0);
		    				ps.setInt(15, 0);
		    				ps.setInt(16, 0);
		    				ps.setInt(17, 0);
		    				ps.setInt(18, 0);
		    				ps.setInt(19, 0);
		    				ps.setInt(20,0);
		    				ps.setInt(21, 0);
		    				ps.setInt(22, 0);
		    				ps.setInt(23, 0);
		    				ps.setInt(24, 0);
		    				ps.setInt(25, 0);
		    				ps.setInt(26, 0);

		    				ps.setNull(27, java.sql.Types.VARCHAR);
		    				ps.setNull(28, java.sql.Types.VARCHAR);
		    				ps.setNull(29, java.sql.Types.VARCHAR);	

		    				ps.setInt(43, 0);
		    				ps.setInt(44, 0);
		    				ps.setNull(45,java.sql.Types.TIMESTAMP);
		    				ps.setNull(46,  java.sql.Types.VARCHAR);
		    				ps.setInt(47, 0);
		    				ps.setDouble(48, 0);
		    				ps.setNull(49, java.sql.Types.VARCHAR);
		    				ps.setDouble(50, 0);


		    			}
		    			ps.setString(30, eventD.getZoneCode());
		    			ps.setDouble(31, eventD.getWs_amount());
		    			ps.setString(32, get(eventD.isAlcohol_restriction()));
		    			ps.setString(33,get(eventD.isHoliday_restriction()));
		    			ps.setString(34,get(eventD.isEcofriendlyslot()));
		    			ps.setString(35,get(eventD.isNeighbourhoodslot()));
		    			ps.setInt(36,eventD.getTotalCapacity());
		    			ps.setInt(37,eventD.getCtCapacity());
		    			ps.setString(38,get(eventD.isManuallyClosed()));
		    			if(eventD.getCutOff()!=null)
		    				ps.setTimestamp(39,new java.sql.Timestamp(eventD.getCutOff().getTime()));
		    			ps.setString(40,eventD.getStoreFrontAvailable());
		    			ps.setInt(41,eventD.getCtAllocated());
		    			ps.setInt(42,eventD.getTotalAllocated());
		    			ps.setString(51, get(eventD.isGeoRestricted()));

		    		} else {
		    			ps.setString(5, eventD.getZoneCode());
		    		}

		    		ps.addBatch();
		    	}
		}
	    ps.executeBatch();
		ps.close();
	    }
		
	}
	
	
	
	private static String get(boolean value) {
		return value?"Y":"N";
	}
	
	private static Boolean getBoolean(String s)
	{
		if("Y".equals(s))
			return true;
		else
			return false;
	}

	private static boolean isAnalyzeCall(EventType eventType) {
		return EventType.GET_TIMESLOT.equals(eventType) || EventType.CHECK_TIMESLOT.equals(eventType);
	}
}

