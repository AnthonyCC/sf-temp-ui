package com.freshdirect.delivery.dao;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;


public class AirclicDAO {

	
	public static List<AirclicMessageVO> getMessages(Connection conn)
	{
		List<AirclicMessageVO> messages = new ArrayList<AirclicMessageVO>();
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		try
		{
		ps =
				conn.prepareStatement("select * FROM DLV.AIRCLIC_MESSAGE");
			
			rs = ps.executeQuery();
			while (rs.next()) {
					AirclicMessageVO message = new AirclicMessageVO();
					message.setMessage(rs.getString("message"));
					message.setDescription(rs.getString("description"));
					messages.add(message);
					
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
		finally
		{
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			

			return messages;
	}
	
	private static final String ROUTE_DWLD_QUERY = "select * FROM DLV.routedownload rd where to_char(scandate,'mm/dd/yyyy') = ? and route = ?";
	
	public static List<String> getUserId(Connection conn, AirclicTextMessageVO textMessage)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		List<String> userIds = new ArrayList<String>();
		try
		{
			DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
			
			CriteriaBuilder builder = new CriteriaBuilder();
			if(textMessage.getOrderId()!=null)
				builder.addSql(" and order_number = ?", new Object[]{textMessage.getOrderId()} );
			ps = conn.prepareStatement(ROUTE_DWLD_QUERY+builder.getCriteria());
		
			ps.setString(1, df.format(textMessage.getDeliveryDate()));
			ps.setString(2, textMessage.getRoute());
			Object[] params = builder.getParams();
			if(params!=null && params.length>0)
			ps.setObject(3, params[0]);
			
			rs = ps.executeQuery();
			while (rs.next()) {
				userIds.add(rs.getString("userId"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
		finally
		{
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			

			return userIds;
	}
	
	public static void sendMessage(Connection conn, List<String> userIds, AirclicTextMessageVO message) throws DlvResourceException
	{
		//airclic table

		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			
			
			if(userIds!=null && userIds.size()>0)
			{
				if(message.getOrderId()!=null)
				{
					ps = conn.prepareStatement("INSERT INTO DLV.ORDERUPDATE(USERID, ORDER_NUMBER, NOTE) VALUES(?,?,?)");
					
					for(String userId: userIds)
					{
						ps.setString(1, userId);
						ps.setString(2, message.getOrderId());
						ps.setString(3, message.getMessage());
						ps.addBatch();
					}
					int[] test =	ps.executeBatch();
				}
				else
				{

					ps = conn.prepareStatement("INSERT INTO DLV.ROUTEUPDATE(USERID, ROUTE_NUMBER, NOTE) VALUES(?,?,?)");
					
					for(String userId: userIds)
					{
						ps.setString(1, userId);
						ps.setString(2, message.getRoute());
						ps.setString(3, message.getMessage());
						ps.addBatch();
					}
						ps.executeBatch();
				
				}
				
			}
			else if(message.getOrderId()!=null)
			{
				// @TODO update the airclic ORDERS table NOTE column if userId does not exist

				ps = conn.prepareStatement("UPDATE  DLV.ORDERS SET NOTE =? WHERE ORDER_NUMBER=?");
				
				
					ps.setString(1, message.getMessage());
					ps.setString(2, message.getOrderId());
					ps.execute();
			
			}
		}
		catch(SQLException e)
		{
			throw new DlvResourceException(e);
		}
		finally
		{
			try {
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public static void saveMessageInQueue(Connection conn, AirclicTextMessageVO message) throws DlvResourceException
	{
		PreparedStatement ps = null;
	
		try
		{
			ps = conn.prepareStatement("INSERT INTO DLV.AIRCLIC_TXTMESSAGE(ID, CREATEDATE, DELIVERYDATE, SENDER, MESSAGE, SOURCE, ROUTE, STOP, ORDERID) VALUES(?,SYSDATE,?,?,?,?,?,?,?)");
			ps.setString(1, message.getId());
			ps.setDate(2, new java.sql.Date(message.getDeliveryDate().getTime()));
			ps.setString(3, message.getSender());
			ps.setString(4, message.getMessage());
			ps.setString(5, message.getSource());
			ps.setString(6, message.getRoute());
			ps.setInt(7, message.getStop());
			ps.setString(8, message.getOrderId());
			ps.execute();
		}
		catch(SQLException e)
		{
			throw new DlvResourceException(e);
		}
		finally
		{
			try {
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static byte[] getSignature(Connection conn,String order) throws IOException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		OutputStream out = null;byte[] signBytes = null;
		try
		{
			ps = conn.prepareStatement("select signature from cust.signature " +
					"where sale_id = ?");
			
			
			ps.setString(1, order);
			rs = ps.executeQuery();
			if (rs.next()) {
				signBytes = rs.getBytes("signature");
				}
		}
		catch(SQLException e)
		{
			System.out.println(e);
		}
		finally
		{
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return signBytes;
	}
	public static void updateMessage(Connection conn, AirclicTextMessageVO message) throws DlvResourceException
	{
		PreparedStatement ps = null;
	
		try
		{
			ps = conn.prepareStatement("UPDATE DLV.AIRCLIC_TXTMESSAGE SET SENT_TO_AIRCLIC = 'Y' WHERE ID = ?");
			ps.setString(1,message.getId());
			ps.execute();
		}
		catch(SQLException e)
		{
			throw new DlvResourceException(e);
		}
		finally
		{
			try {
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public static List<AirclicTextMessageVO> getUnsentMessages(Connection conn) throws DlvResourceException
	{
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<AirclicTextMessageVO> voList = new ArrayList<AirclicTextMessageVO>();
		try
		{
			ps = conn.prepareStatement("SELECT ORDERID ,ROUTE,MESSAGE FROM  DLV.AIRCLIC_TXTMESSAGE WHERE SENT_TO_AIRCLIC = 'N' AND DELIVERYDATE = TRUNC(SYSDATE)");
			rs = ps.executeQuery();
			while(rs.next())
			{
				AirclicTextMessageVO vo = new AirclicTextMessageVO();
				vo.setOrderId(rs.getString("orderid"));
				vo.setRoute(rs.getString("route"));
				vo.setMessage(rs.getString("message"));
				voList.add(vo);
			}
		}
		catch(SQLException e)
		{
			throw new DlvResourceException(e);
		}
		finally
		{
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return voList;
	}
	public static void getSignatureData(Connection conn, Date deliveryDate) throws IOException
	{
		PreparedStatement ps = null,ps1 = null;
		ResultSet rs = null;
		//InputStream in = null;
		byte[] in = null;
		String order, dateStr=null;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		if(deliveryDate!=null)
			dateStr = df.format(deliveryDate);
		
		try
		{
		long start = System.currentTimeMillis();
		ps = conn.prepareStatement("select webordernum,SIGNATURE from (select distinct webordernum, EVENTID, to_char(scandate,'mm/dd/yyyy')  from dlv.cartonstatus cs" +
				" where  scandate  between to_date( ?, 'mm/dd/yyyy hh24:mi:ss') and  to_date( ?, 'mm/dd/yyyy hh24:mi:ss') and CS.CARTONSTATUS = 'DELIVERED'  ) CS," +
				" dlv.signature s   where  S.EVENTID = CS.EVENTID");
		
		ps.setString(1,  dateStr+" 00:00:00");
		ps.setString(2,  dateStr+" 23:59:59");
		
		rs = ps.executeQuery();
		ps1 = conn.prepareStatement("insert into cust.signature(sale_id, signature) values (?,?)");
		
		while (rs.next()) {
			order = rs.getString("webordernum");
			in=rs.getBytes("SIGNATURE");
			
			if(in!=null)
				{
				  	ps1.setString(1, order);
				    ps1.setBytes(2, in);
				    ps1.addBatch();
				}
			
		}

		ps1.executeBatch();
		
		long end = System.currentTimeMillis();
		
		System.out.println("Time spent syncing the signatures "+(end-start)/(1000)+" sec");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			finally
			{
				try {
					if(rs!=null)
						rs.close();
					if(ps!=null)
						ps.close();
					if(ps1!=null)
						ps1.close();
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
			
		}

	
	public static void main(String s[]) throws SQLException, NamingException, IOException
	{
		
		Connection conn = getDataSource().getConnection();
		getSignatureData(conn, null);
		
	}

		public static DataSource getDataSource() throws NamingException {
	        InitialContext initCtx = null;
	    try {
	        initCtx = getInitialContext();
	        return (DataSource) initCtx.lookup("fddatasource");
	    } finally {
	        if (initCtx!=null) try {
	              initCtx.close();
	        } catch (NamingException ne) {}
	    }
	 }
	   static public InitialContext getInitialContext() throws NamingException {
	        Hashtable h = new Hashtable();
	        h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	        h.put(Context.PROVIDER_URL, "t3://localhost:7001");
	        return new InitialContext(h);
	  }
	
}
