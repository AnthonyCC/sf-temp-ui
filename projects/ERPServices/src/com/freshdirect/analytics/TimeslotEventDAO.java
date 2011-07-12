package com.freshdirect.analytics;

/**
 * 
 * @author tbalumuri
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;


public class TimeslotEventDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(TimeslotEventDAO.class);
	private static final String TIMESLOT_LOG_INSERT="INSERT INTO DLV.TIMESLOT_LOG_TMP (ID, EVENT_DTM,RESERVATION_ID, " +
			"ORDER_ID, CUSTOMER_ID, EVENTTYPE,RESPONSE_TIME,COMMENTS,TransactionSource,DlvPassApplied,DeliveryCharge,isDeliveryChargeWaived,zonectactive) " +
			"VALUES (?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String TIMESLOT_LOG_DTL_INSERT="INSERT INTO DLV.TIMESLOT_LOG_DTL_TMP (TIMESLOT_LOG_ID, BASE_DATE, START_TIME" +
			", END_TIME, ZONE_CODE) VALUES (?,?,?,?,?)";
	
	private static final String TIMESLOT_LOG_DTL_WITH_COST_INSERT="INSERT INTO DLV.TIMESLOT_LOG_DTL_TMP (TIMESLOT_LOG_ID, BASE_DATE, START_TIME, " +
			"END_TIME, ADD_DISTANCE, ADD_RUNTIME, ADD_STOPCOST, CAPACITY, COSTPERMILE, FIXED_RT_COST, MAXRUNTIME,"+
	"OT_HOURLY_WAGE,PERCENT_AVAIL,PREF_RUNTIME , REG_HOURLY_WAGE, REG_WAGE_SECS, ROUTE_ID ,STOP_SEQ ,TOTAL_DISTANCE, TOTAL_PU_QTY" +
	", TOTAL_QTY, TOTAL_ROUTE_COST , TOTAL_RUNTIME ,  TOTAL_SVC_TIME,TOTAL_TRAVEL_TIME,  TOTAL_WAIT_TIME,  IS_AVAIL " +
	",  IS_FILTERED ,  IS_MISSED_TW,ZONE_CODE, ws_amount,alcohol_restriction,holiday_restriction,ecofriendlyslot,neighbourhoodslot" +
	", totalCapacity,CTCapacity,Manually_Closed,cutoff, storefront_avl,CT_Allocated,Total_Allocated, WAVE_VEHICLES,WAVE_VEHICLES_IN_USE," +
	"WAVE_STARTTIME,UNAVAILABILITY_REASON,WAVE_ORDERS_TAKEN,TOTAL_QUANTITIES, NEWROUTE, CAPACITIES, GEORESTRICTED)" +
	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	public static void addEntry(Connection conn,String reservationId,String orderId,String customerId
				,EventType eventType,TimeslotEventModel event, int responseTime, String comments ) 
										throws SQLException {
		PreparedStatement ps = conn.prepareStatement(TIMESLOT_LOG_INSERT);
		String id = SequenceGenerator.getNextId(conn, "DLV", "TIMESLOT_LOG_SEQUENCE");
		ps.setString(1, id);
		if(reservationId==null || "".equals(reservationId)) {
			ps.setNull(2,java.sql.Types.VARCHAR);
		}
		else {
			ps.setString(2,reservationId);
		}
		ps.setString(3, orderId);
	    ps.setString(4, customerId);
	    ps.setString(5, eventType.value());
	    ps.setInt(6, responseTime);
	    ps.setString(7, comments);
	    if(event != null)
	    {
	    	ps.setString(8, event.getTransactionSource());
	    	ps.setString(9,(event.isDlvPassApplied())?"Y":"N");
	    	ps.setDouble(10, event.getDeliveryCharge());
	    	ps.setString(11, (event.isDeliveryChargeWaived())?"Y":"N");
	    	ps.setString(12, (event.isZoneCtActive())?"Y":"N");
	    	
	    }
	    else
	    {
	    	ps.setString(8, "");
	    	ps.setString(9,"");
	    	ps.setDouble(10, 0);
	    	ps.setString(11, "");
	    	ps.setString(12, "");
	    }
	    ps.execute();
	    ps.close();
	    if (event==null) return;
	    boolean isAnalyseCall=isAnalyzeCall(eventType);
	    if(isAnalyseCall) {
	    	ps=conn.prepareStatement(TIMESLOT_LOG_DTL_WITH_COST_INSERT);
	    } else {
	    
	    	ps=conn.prepareStatement(TIMESLOT_LOG_DTL_INSERT);
	    }
	    for (List<TimeslotEventDetailModel> list : event.getDetail()) {
	    	
		    for (TimeslotEventDetailModel eventD : list) {
		    	if(eventD!=null)
		    		{
			    	ps.setString(1,id);
			    	
			    	ps.setTimestamp(2, new java.sql.Timestamp(DateUtil.truncate(eventD.getDeliveryDate()).getTime()));
			    	ps.setTimestamp(3, new java.sql.Timestamp(eventD.getStartTime().getTime()));
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
			    		ps.setTimestamp(39,new java.sql.Timestamp(eventD.getCutOff().getTime()));
			    		ps.setString(40,get(eventD.isStorefront_avl()));
			    		ps.setInt(41,eventD.getCtAllocated());
			    		ps.setInt(42,eventD.getTotalAllocated());
			    		ps.setString(51, get(eventD.isGeoRestricted()));
			    		
			    	} else {
			    		ps.setString(5, eventD.getZoneCode());
			    	}
			    	
			    	ps.addBatch();
		    	}
		    }
		}
		ps.executeBatch();
		ps.close();
	}
	
	private static String get(boolean value) {
		return value?"Y":"N";
	}
	
	private static boolean isAnalyzeCall(EventType eventType) {
		return EventType.GET_TIMESLOT.equals(eventType);
	}
}

