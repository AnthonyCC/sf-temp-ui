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

public class TimeSlotLogDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(TimeSlotLogDAO.class);
	
	private static final String EMPTY_TIMESLOT="1";
	private static final String AVAILABLE_TIMESLOT="0";
	private static final String TIMESLOT_LOG_INSERT="INSERT INTO DLV.TIMESLOT_LOG (ID, EVENT_DTM,RESERVATION_ID, ORDER_ID, CUSTOMER_ID, EVENTTYPE,RESPONSE_TIME,COMMENTS) VALUES (?,SYSDATE,?,?,?,?,?,?)";
	private static final String TIMESLOT_LOG_DTL_INSERT="INSERT INTO DLV.TIMESLOT_LOG_DTL (TIMESLOT_LOG_ID, BASE_DATE, START_TIME, END_TIME, IS_EMPTY) VALUES (?,?,?,?,?)";
	
	public static void addEntry(Connection conn,String reservationId,String orderId,String customerId,RoutingActivityType actionType,List<java.util.List<IDeliverySlot>> slots, int responseTime,String comments ) throws SQLException{
		
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
	    ps.setString(5, actionType.value());
	    ps.setInt(6, responseTime);
	    ps.setString(7, comments);
	    ps.execute();
	    ps.close();
	    if (slots==null) return;
	    
	    ps=conn.prepareStatement(TIMESLOT_LOG_DTL_INSERT);
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
		    	ps.addBatch();
		    }
		}
		int[] count=ps.executeBatch();
		ps.close();
	}
}
