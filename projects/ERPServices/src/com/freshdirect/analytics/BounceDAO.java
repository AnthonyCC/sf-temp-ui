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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.sql.DataSource;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;


public class BounceDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(BounceDAO.class);
	
	private static final String BOUNCE_INSERT="INSERT INTO MIS.bounce_event (id, customer_id, status, createdate, delivery_date, cutoff, zone, log_id, type) " +
			"VALUES (?,?,?,?,?,?,?,?,?)";
	private static final String BOUNCE_UPDATE = "UPDATE MIS.bounce_event SET STATUS ='CANCELLED', LASTUPDATE=? where id = ?";
	private static final String BOUNCE_SELECT = "SELECT id FROM MIS.bounce_event WHERE CUSTOMER_ID =? AND " +
									"STATUS = ? AND createdate between to_date(?, 'MM-DD-YYYY HH24:MI:SS')-1 and to_date(?,'MM-DD-YYYY HH24:MI:SS')";
	
	
	private static final String CANCEL_BOUNCE = "update MIS.bounce_event set status = 'CANCELLED', lastupdate=sysdate where id in (SELECT be.id FROM cust.sale s, " +
			"cust.salesaction sa, MIS.bounce_event be WHERE s.ID=sa.SALE_ID AND s.CUSTOMER_ID=sa.CUSTOMER_ID  and be.customer_id = s.customer_id" +
			" and s.CROMOD_DATE=sa.ACTION_DATE AND sa.ACTION_TYPE IN ('CRO') AND sa.REQUESTED_DATE > TRUNC(SYSDATE)  and be.status = 'NEW' " +
			"and trunc(S.CROMOD_DATE) = trunc(be.createdate) AND s.type='REG' AND s.status <> 'CAN')";
	
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
	
	
	public static void insert(Connection conn, List<BounceEvent> bounce) 
	{
		PreparedStatement ps = null;
		
	
		    try {
		    	ps = conn.prepareStatement(BOUNCE_INSERT);
		    	for (BounceEvent bounceEvent : bounce)  {
					String id = SequenceGenerator.getNextId(conn, "DLV", "event_detection_SEQUENCE");
					ps.setString(1, id);
					ps.setString(2, bounceEvent.getCustomerId());
				    ps.setString(3, bounceEvent.getStatus());
				    ps.setTimestamp(4,new java.sql.Timestamp(bounceEvent.getCreateDate().getTime()));
				    ps.setDate(5, new java.sql.Date(bounceEvent.getDeliveryDate().getTime()));
				    if(bounceEvent.getCutOff()!=null)
				    ps.setTimestamp(6,  new java.sql.Timestamp(bounceEvent.getCutOff().getTime()));
				    else
				    ps.setNull(6, java.sql.Types.TIMESTAMP);
				    ps.setString(7, bounceEvent.getZone());
				    ps.setString(8, bounceEvent.getLogId());
				    ps.setString(9, bounceEvent.getPageType());
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
	
	public static void insert(String customerId,String status, Date deliveryDate, Date cutoff, String zone, String log_id, String type) throws SQLException
		{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
				conn =getConnection();
				ps = conn.prepareStatement(BOUNCE_INSERT);
				String id = SequenceGenerator.getNextId(conn, "DLV", "event_detection_SEQUENCE");
				ps.setString(1, id);
				ps.setString(2, customerId);
			    ps.setString(3, status);
			    Calendar cal = Calendar.getInstance();
			    ps.setTimestamp(4,new java.sql.Timestamp(cal.getTimeInMillis()));
			    ps.setDate(5, new java.sql.Date(deliveryDate.getTime()));
			    if(cutoff!=null)
			    ps.setTimestamp(6,  new java.sql.Timestamp(cutoff.getTime()));
			    else
			    ps.setNull(6, java.sql.Types.TIMESTAMP);
			    ps.setString(7, zone);
			    ps.setString(8, log_id);
			    ps.setString(9, type);
			    ps.execute();
			  
		//	}
		}
		catch(Exception e)
		{
			LOGGER.info("There was an exception while inserting the bounce record for customer: "+customerId+" Message:"+e.getMessage());
		}
		finally
		{
			try
			{
				if(ps!=null)
					ps.close();
				if(conn!=null)
				  conn.close();
			}
			catch(SQLException e)
			{
				LOGGER.info("There was an exception cleaning up the resources",e);
			}
		}

	}
	
	
	public static void customUpdate(String id) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
	try
	{
			conn =getConnection();
			ps = conn.prepareStatement(BOUNCE_UPDATE);
			ps.setDate(1, (java.sql.Date)new Date());
			ps.setString(2, id);
		    ps.execute();
	}
	catch(Exception e)
	{
		LOGGER.info("There was an exception while updating the bounce", e);
	}
	finally
	{
		try
		{
			if(ps!=null)
				ps.close();
			if(conn!=null)
			  conn.close();
		}
		catch(SQLException e)
		{
			LOGGER.info("There was an exception cleaning up the resources",e);
		}
	}
}
	public static String wasBounce(String customerId, String status) throws SQLException
	{
	String id = null;
	Connection conn = null;
	PreparedStatement ps = null;
	try
	{
		conn =getConnection();
		ps = conn.prepareStatement(BOUNCE_SELECT);
		ps.setString(1, customerId);
		ps.setString(2, status);
		DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy kk:mm:ss"); //E, dd MMM yyyy HH:mm:ss Z
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String str = formatter.format(cal.getTime());
		System.err.println(str);
		ps.setString(3, str);
		ps.setString(4, str);
	    ResultSet rs = ps.executeQuery();
	   
	    if(rs.next())
	    {
	    	id = rs.getString("id");
	    }
	}
	catch(Exception e)
	{
		LOGGER.info("There was an exception while selecting the bounce record for customer "+customerId, e);
	}
	finally
	{
		try
		{
			if(ps!=null)
				ps.close();
			if(conn!=null)
			  conn.close();
		}
		catch(SQLException e)
		{
			LOGGER.info("There was an exception cleaning up the resources", e);
		}
	}
	return id;

	}	
	
	public static void cancelBounce(Connection conn)
	{
		PreparedStatement ps = null;
		try
		{
		ps = conn.prepareStatement(CANCEL_BOUNCE);
		ps.execute();
		}
		catch(Exception e)
		{
			LOGGER.info(e.getMessage());
		}
		finally
		{
			try
			{
				if(ps!=null)
					ps.close();
			}
			catch(SQLException e)
			{
				LOGGER.info("There was an exception cleaning up the resources", e);
			}
		}
	}
	
}

