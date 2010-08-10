package com.freshdirect.delivery.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliverySlotCost;

public class TimeSlotLogDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(TimeSlotLogDAO.class);
	
	private static final String EMPTY_TIMESLOT="1";
	private static final String AVAILABLE_TIMESLOT="0";
	private static final String TIMESLOT_LOG_INSERT="INSERT INTO DLV.TIMESLOT_LOG (ID, EVENT_DTM,RESERVATION_ID, " +
			"ORDER_ID, CUSTOMER_ID, EVENTTYPE,RESPONSE_TIME,COMMENTS) VALUES (?,SYSDATE,?,?,?,?,?,?)";
	
	private static final String TIMESLOT_LOG_DTL_INSERT="INSERT INTO DLV.TIMESLOT_LOG_DTL (TIMESLOT_LOG_ID, BASE_DATE, START_TIME" +
			", END_TIME, IS_EMPTY, ZONE_CODE) VALUES (?,?,?,?,?,?)";
	
	private static final String TIMESLOT_LOG_DTL_WITH_COST_INSERT="INSERT INTO DLV.TIMESLOT_LOG_DTL (TIMESLOT_LOG_ID, BASE_DATE, START_TIME, END_TIME, IS_EMPTY, ADD_DISTANCE, ADD_RUNTIME, ADD_STOPCOST, CAPACITY, COSTPERMILE, FIXED_RT_COST, MAXRUNTIME,"+
	"OT_HOURLY_WAGE,PERCENT_AVAIL,PREF_RUNTIME , REG_HOURLY_WAGE, REG_WAGE_SECS, ROUTE_ID ,STOP_SEQ ,TOTAL_DISTANCE, TOTAL_PU_QTY" +
	", TOTAL_QTY, TOTAL_ROUTE_COST , TOTAL_RUNTIME ,  TOTAL_SVC_TIME,TOTAL_TRAVEL_TIME,  TOTAL_WAIT_TIME,  IS_AVAIL " +
	",  IS_FILTERED ,  IS_MISSED_TW, ZONE_CODE"+
	") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String TIMESLOT_COST_LOG_INSERT=" INSERT INTO DLV.TIMESLOT_COST_LOG " +
	"(TIMESLOT_LOG_ID , ADD_DISTANCE, ADD_RUNTIME, ADD_STOPCOST, CAPACITY, COSTPERMILE, FIXED_RT_COST, MAXRUNTIME,"+
	"OT_HOURLY_WAGE,PERCENT_AVAIL,PREF_RUNTIME , REG_HOURLY_WAGE, REG_WAGE_SECS, ROUTE_ID ,STOP_SEQ ,"+ 
	" TOTAL_DISTANCE, TOTAL_PU_QTY, TOTAL_QTY, TOTAL_ROUTE_COST , TOTAL_RUNTIME ,  TOTAL_SVC_TIME,"+ 
	" TOTAL_TRAVEL_TIME,  TOTAL_WAIT_TIME,  IS_AVAIL ,  IS_FILTERED ,  IS_MISSED_TW, ZONE_CODE)"+
	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
	  
	
	public static void addEntry(Connection conn,String reservationId,String orderId,String customerId
				,RoutingActivityType actionType,List<java.util.List<IDeliverySlot>> slots, int responseTime,String comments ) 
										throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(TIMESLOT_LOG_INSERT);
		PreparedStatement ps1=null;
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
	    ps.setString(5, actionType.value());
	    ps.setInt(6, responseTime);
	    ps.setString(7, comments);
	    ps.execute();
	    ps.close();
	    if (slots==null) return;
	    boolean isAnalyseCall=isAnalyzeCall(actionType);
	    if(isAnalyseCall) {
	    	ps=conn.prepareStatement(TIMESLOT_LOG_DTL_WITH_COST_INSERT);
	    } else {
	    
	    	ps=conn.prepareStatement(TIMESLOT_LOG_DTL_INSERT);
	    }
	    for (List<IDeliverySlot> list : slots) {
	    	
		    for (IDeliverySlot slot : list) {
		    	
		    	ps.setString(1,id);
		    	ps.setTimestamp(2, new java.sql.Timestamp(DateUtil.truncate(slot.getSchedulerId().getDeliveryDate()).getTime()));
		    	ps.setTimestamp(3, new java.sql.Timestamp(slot.getStartTime().getTime()));
		    	ps.setTimestamp(4, new java.sql.Timestamp(slot.getStopTime().getTime()));
		    	if((slot.getDeliveryCost() != null && slot.getDeliveryCost().isAvailable())
		    			|| !RoutingActivityType.GET_TIMESLOT.equals(actionType)) {
		    		ps.setString(5, AVAILABLE_TIMESLOT);
		    	}
		    	else {
		    		ps.setString(5, EMPTY_TIMESLOT);		    		
		    	}
		    	if(isAnalyseCall ) {
		    		IDeliverySlotCost cost=slot.getDeliveryCost();
		    		
		    		if(cost!=null) {
			    		ps.setInt(6, cost.getAdditionalDistance());
			    		ps.setInt(7, cost.getAdditionalRunTime());
			    		ps.setInt(8, cost.getAdditionalStopCost());
			    		ps.setInt(9, cost.getCapacity());
			    		ps.setInt(10, cost.getCostPerMile());
			    		ps.setInt(11, cost.getFixedRouteSetupCost());
			    		ps.setInt(12, cost.getMaxRunTime());
			    		ps.setInt(13, cost.getOvertimeHourlyWage());
			    		ps.setDouble(14, cost.getPercentageAvailable());
			    		ps.setInt(15, cost.getPrefRunTime());
			    		ps.setInt(16, cost.getRegularHourlyWage());
			    		ps.setInt(17, cost.getRegularWageDurationSeconds());
			    		ps.setInt(18, cost.getRouteId());
			    		ps.setInt(19, cost.getStopSequence());
			    		ps.setInt(20, cost.getTotalDistance());
			    		ps.setInt(21, cost.getTotalPUQuantity());
			    		ps.setInt(22, cost.getTotalQuantity());
			    		ps.setInt(23, cost.getTotalRouteCost());
			    		ps.setInt(24, cost.getTotalRunTime());
			    		ps.setInt(25, cost.getTotalServiceTime());
			    		ps.setInt(26, cost.getTotalTravelTime());
			    		ps.setInt(27, cost.getTotalWaitTime());
			    		ps.setString(28, get(cost.isAvailable()));
			    		ps.setString(29, get(cost.isFiltered()));
			    		ps.setString(30, get(cost.isMissedTW()));			    		
		    		} else {
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
			    		ps.setInt(27, 0);
  		    		    ps.setNull(28, java.sql.Types.VARCHAR);
			    		ps.setNull(29, java.sql.Types.VARCHAR);
			    		ps.setNull(30, java.sql.Types.VARCHAR);			    		
		    		}
		    		ps.setString(31, slot.getZoneCode());
		    	} else {
		    		ps.setString(6, slot.getZoneCode());
		    	}
		    	
		    	ps.addBatch();
		    }
		}
		int[] count=ps.executeBatch();
		ps.close();
	}
	
	private static String get(boolean value) {
		return value?"Y":"N";
	}
	
	private static boolean isAnalyzeCall(RoutingActivityType actionType) {
		return RoutingActivityType.GET_TIMESLOT.equals(actionType);
	}
}

