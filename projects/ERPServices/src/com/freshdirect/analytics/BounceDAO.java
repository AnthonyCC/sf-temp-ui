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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;


public class BounceDAO {

	private static final Category LOGGER = LoggerFactory.getInstance(BounceDAO.class);
	
	private static final String BOUNCE_INSERT="INSERT INTO MIS.bounce_event (id, customer_id, status, createdate, delivery_date, cutoff, zone, log_id, type, sector) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?)";
	private static final String CANCEL_BOUNCE = "update MIS.bounce_event set status = 'CANCELLED', lastupdate=sysdate where id in (SELECT be.id FROM cust.sale s, " +
			"cust.salesaction sa, MIS.bounce_event be WHERE s.ID=sa.SALE_ID AND s.CUSTOMER_ID=sa.CUSTOMER_ID  and be.customer_id = s.customer_id" +
			" and s.CROMOD_DATE=sa.ACTION_DATE AND sa.ACTION_TYPE IN ('CRO', 'MOD') AND sa.REQUESTED_DATE > TRUNC(SYSDATE)  and be.status = 'NEW' " +
			"and trunc(S.CROMOD_DATE) = trunc(be.createdate) AND s.type='REG' AND s.status <> 'CAN')";
	
	private static final String BOUNCE_SELECT = "select count(distinct(customer_id)) cnt, createdate, zone, cutoff from mis.bounce_event where status = 'NEW' and " +
			"type in ('DELIVERYINFO', 'CHECKOUT','RESERVED_SLOT') and to_char(delivery_date, 'mm/dd/yyyy') = ? and zone=? group by  zone, cutoff,createdate " +
			"order by  createdate  asc";
	
	private static final String BOUNCE_SELECT_BYZONE = "select count(distinct(customer_id)) cnt, zone, cutoff,sector from mis.bounce_event where status = 'NEW' and " +
			"type in ('DELIVERYINFO', 'CHECKOUT','RESERVED_SLOT') and to_char(delivery_date, 'mm/dd/yyyy') = ? group by  zone, cutoff,sector " +
			"order by zone,cutoff,sector  asc";
	
	
	
	public static void insert(Connection conn, List<BounceEvent> bounce) 
	{
		PreparedStatement ps = null;
		
	
		    try {
		    	ps = conn.prepareStatement(BOUNCE_INSERT);
		    	for (BounceEvent bounceEvent : bounce)  {
					String id = SequenceGenerator.getNextId(conn, "mis", "event_detection_SEQUENCE");
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
				    ps.setString(10, bounceEvent.getSector());
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

	
	public static List<BounceData> getData(Connection conn, String deliveryDate,String zone) 
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		DateFormat df = new SimpleDateFormat("hh:mm a");
		Date createDate = null;
		List<BounceData> dataList = new ArrayList<BounceData>();
		Calendar cal = Calendar.getInstance();
		 try {
		    	ps = conn.prepareStatement(BOUNCE_SELECT);
		    	ps.setString(1, deliveryDate);
		    	ps.setString(2, zone);
		    	rs = ps.executeQuery();
		    	
		    	while(rs.next())
		    	{
		    		BounceData data = new BounceData();
		    		data.setCnt(rs.getInt("cnt"));
		    		data.setCutOff(new Date(rs.getTimestamp("cutoff").getTime()));
		    		data.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
		    		data.setZone(rs.getString("zone"));
		    		createDate = new Date(rs.getTimestamp("createdate").getTime());
		    		cal.setTime(createDate);
		    		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE)%30);
		    		data.setSnapshotTime(cal.getTime());
		    		data.setSnapshotTimeFormatted(sdf.format(cal.getTime()));
		    		dataList.add(data);
		    		
		    	}
		    	
		 }
		 catch(Exception e)
			{
				LOGGER.info(e.getMessage());
				e.printStackTrace();
			}
		    	
		 finally
			{
				try
				{
					if(rs!=null)
						rs.close();
					if(ps!=null)
						ps.close();
				}
				catch(SQLException e)
				{
					LOGGER.info("There was an exception cleaning up the resources", e);
				}
			}
		 return dataList;
		
	}
	
	public static List<BounceData> getDataByZone(Connection conn, String deliveryDate) 
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BounceData> dataList = new ArrayList<BounceData>();
		DateFormat df = new SimpleDateFormat("hh:mm a");
		 try {
		    	ps = conn.prepareStatement(BOUNCE_SELECT_BYZONE);
		    	ps.setString(1, deliveryDate);
		    	rs = ps.executeQuery();
		    	
		    	while(rs.next())
		    	{
		    		BounceData data = new BounceData();
		    		data.setCnt(rs.getInt("cnt"));
		    		data.setCutOff(new Date(rs.getTimestamp("cutoff").getTime()));
		    		data.setCutoffTimeFormatted(df.format(new Date(rs.getTimestamp("cutoff").getTime())));
		    		data.setZone(rs.getString("zone"));
		    		data.setSector(rs.getString("sector"));
		    		dataList.add(data);
		    		
		    	}
		    	
		 }
		 catch(Exception e)
			{
				LOGGER.info(e.getMessage());
				e.printStackTrace();
			}
		    	
		 finally
			{
				try
				{
					if(rs!=null)
						rs.close();
					if(ps!=null)
						ps.close();
				}
				catch(SQLException e)
				{
					LOGGER.info("There was an exception cleaning up the resources", e);
				}
			}
		 return dataList;
		
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

