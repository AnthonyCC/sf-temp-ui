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
	
	private static final String SESSION_INSERT="INSERT INTO dlv.session_event (customer_id, login_time, logout_time) " +
			"VALUES (?,?,?)";
	
	
	public static void insert(String customerId, Date login_time, Date logout_time) 
		{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			
				Context ctx = ErpServicesProperties.getInitialContext();
				DataSource ds = (DataSource) ctx.lookup("fddatasource");
				conn = ds.getConnection();
				ps = conn.prepareStatement(SESSION_INSERT);
				ps.setString(1, customerId);
				ps.setTimestamp(2, new Timestamp(login_time.getTime()));
			    ps.setTimestamp(3, new Timestamp(logout_time.getTime()));
			    
			    ps.execute();
			    ps.close();
			    conn.close();
			
		}
		catch(Exception e)
		{
			LOGGER.info("There was an exception while inserting the session record for customer "+customerId);
			e.printStackTrace();
		}
		finally{
			
			try{
				ps.close();
				conn.close();
			}
			catch(SQLException sqle)
			{
				LOGGER.info(sqle.getMessage());
			}
		}

	}
	
	
	
	
}

