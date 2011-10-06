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
	

	private static Connection getConnection() throws Exception
	{
		Connection conn = null;
		try
		{
		Context ctx = ErpServicesProperties.getInitialContext();
		DataSource ds = (DataSource) ctx.lookup("fddatasource");
		conn = ds.getConnection();
		}
		catch(Exception e)
		{
			LOGGER.info(e.getMessage());
			throw e;
		}
		return conn;
	
	}
	
	public static void insert(Connection conn, List<RollEvent> roll) 
	{
		PreparedStatement ps = null;
		
	
		    try {
		    	ps = conn.prepareStatement(ROLL_INSERT);
		    	for (RollEvent rollEvent : roll)  {
					String id = SequenceGenerator.getNextId(conn, "DLV", "event_detection_SEQUENCE");
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
	public static void insert(String customerId, float unavailable_pct,Date deliveryDate, Date cutoff, String zone,String log_id) 
		{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
				conn = getConnection();
				ps = conn.prepareStatement(ROLL_INSERT);
				String id = SequenceGenerator.getNextId(conn, "DLV", "event_detection_SEQUENCE");
				ps.setString(1, id);
				ps.setString(2, customerId);
				Calendar cal = Calendar.getInstance();
			    ps.setTimestamp(3,new java.sql.Timestamp(cal.getTimeInMillis()));
			    ps.setFloat(4, unavailable_pct);
			    ps.setString(5, zone);
			    ps.setTimestamp(6,  new java.sql.Timestamp(cutoff.getTime()));
			    ps.setString(7, log_id);
			    ps.setDate(8, new java.sql.Date(deliveryDate.getTime()));
			    ps.execute();
			   
		}
		catch(Exception e)
		{
			LOGGER.info("There was an exception while inserting the roll record for customerId "+customerId+" Message:"+e.getMessage());
		}
		finally
		{
			try
			{
				 ps.close();
				 conn.close();
			}
			catch(Exception e)
			{
				LOGGER.info("There was an exception cleaning up the resources");
			}
		}

	}
}

