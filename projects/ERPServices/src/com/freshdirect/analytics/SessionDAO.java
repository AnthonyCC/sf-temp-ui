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
import java.util.Calendar;
import java.util.Date;
import javax.naming.Context;
import javax.sql.DataSource;
import org.apache.log4j.Category;
import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;


public class SessionDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(SessionDAO.class);
	
	private static final String SESSION_INSERT="INSERT INTO MIS.session_event (customer_id, login_time, logout_time, cutoff, avail_count, " +
			"sold_count, hidden_count, zone, last_get_timeslot, is_timeout, last_gettype, order_id, sector) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	public static void insert(SessionEvent sessionEvent, Connection conn) 
		{
		PreparedStatement ps = null;
		try
		{
				ps = conn.prepareStatement(SESSION_INSERT);
				ps.setString(1, sessionEvent.getCustomerId());
				ps.setTimestamp(2, new Timestamp(sessionEvent.getLoginTime().getTime()));
			    ps.setTimestamp(3, new Timestamp(sessionEvent.getLogoutTime().getTime()));
			    if(sessionEvent.getCutOff()!=null)
			    	ps.setTimestamp(4, new Timestamp(sessionEvent.getCutOff().getTime()));
			    else
			    	ps.setNull(4, java.sql.Types.TIMESTAMP);
			    if(sessionEvent.getAvailCount()!=null)
			    	ps.setInt(5, sessionEvent.getAvailCount());
			    else
			    	ps.setNull(5, java.sql.Types.INTEGER);
			    if(sessionEvent.getSoldCount()!=null)
			    	ps.setInt(6, sessionEvent.getSoldCount());
			    else
			    	ps.setNull(6, java.sql.Types.INTEGER);
			    if(sessionEvent.getHiddenCount()!=null)
			    	ps.setInt(7, sessionEvent.getHiddenCount());
			    else
			    	ps.setNull(7, java.sql.Types.INTEGER);
			  
			    ps.setString(8, sessionEvent.getZone());
			    ps.setString(9, sessionEvent.getLastTimeslot());
			    ps.setString(10, sessionEvent.getIsTimeout());
			    ps.setString(11, sessionEvent.getPageType());
			    ps.setString(12, sessionEvent.getOrderId());
			    ps.setString(13, sessionEvent.getSector());
			    ps.execute();
		}
		catch(Exception e)
		{
			LOGGER.info("There was an exception while inserting the session record for customer "+sessionEvent.getCustomerId());
			e.printStackTrace();
		}
		finally{
			
			try{
				if(ps!=null) ps.close();
			}
			catch(SQLException sqle)
			{
				LOGGER.info(sqle.getMessage());
			}
		}

	}
}

