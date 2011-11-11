package com.freshdirect.analytics;

/**
 * 
 * @author tbalumuri
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.sql.DataSource;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;


public class RollDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(RollDAO.class);
	
	private static final String ROLL_INSERT="INSERT INTO MIS.roll_event (ID, CUSTOMER_ID, CREATEDATE, UNAVAILABLE_PCT, ZONE, CUTOFF, LOG_ID, DELIVERY_DATE) " +
			"VALUES (?,?,?,?,?,?,?,?)";
	
	
	public static void insert(Connection conn, List<RollEvent> roll) 
	{
		PreparedStatement ps = null;
		
	
		    try {
		    	ps = conn.prepareStatement(ROLL_INSERT);
		    	for (RollEvent rollEvent : roll)  {
					String id = SequenceGenerator.getNextId(conn, "mis", "event_detection_SEQUENCE");
					ps.setString(1, id);
					ps.setString(2, rollEvent.getCustomerId());
				    ps.setTimestamp(3, new java.sql.Timestamp(rollEvent.getCreateDate().getTime()));
				    ps.setFloat(4, rollEvent.getUnavailablePct());
				    ps.setString(5, rollEvent.getZone());
				    ps.setTimestamp(6,  new java.sql.Timestamp(rollEvent.getCutOff().getTime()));
				    ps.setString(7, rollEvent.getLogId());
				    ps.setDate(8, new java.sql.Date(rollEvent.getDeliveryDate().getTime()));
				    ps.addBatch();
		    	}
		    	ps.executeBatch();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				
					try {
						if(ps!=null) ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
			}
	}
}

