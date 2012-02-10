package com.freshdirect.delivery.dao;

import java.io.IOException;
import java.io.OutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicNextelVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;
import com.freshdirect.delivery.model.DispatchNextTelVO;
import com.freshdirect.delivery.model.SignatureVO;
import com.freshdirect.framework.util.DateUtil;


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
	
	public static Set<String> getUserId(Connection conn, AirclicTextMessageVO textMessage)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		Set<String> userIds = new HashSet<String>();
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
	
	public static void sendMessage(Connection conn, Set<String> userIds, AirclicTextMessageVO message) throws DlvResourceException
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
	
	public static SignatureVO getSignatureDetails(Connection conn, String order)
			throws IOException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		OutputStream out = null;
		SignatureVO vo = null;
		try {
			ps = conn
					.prepareStatement("select sale_id, signature_timestamp, deliveredto , recipient , contains_alcohol  from cust.sale_signature where sale_id = ?");

			ps.setString(1, order);
			rs = ps.executeQuery();
			if (rs.next()) {
				vo = new SignatureVO();
				vo.setOrderNo(rs.getString("sale_id"));
				vo.setSignatureTime(rs.getTimestamp("signature_timestamp"));
				vo.setDeliveredTo(rs.getString("deliveredto"));
				vo.setRecipient(rs.getString("recipient"));
				vo.setContainsAlcohol("0".equalsIgnoreCase(rs
						.getString("contains_alcohol")) ? false : true);
				// vo.setSignature(rs.getBytes("signature"));
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return vo;
	}

	public static byte[] getSignature(Connection conn, String order)
			throws IOException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		OutputStream out = null;
		byte[] _image = null;
		try {
			ps = conn
					.prepareStatement("select signature from cust.sale_signature where sale_id = ?");

			ps.setString(1, order);
			rs = ps.executeQuery();
			if (rs.next()) {
				_image = rs.getBytes("signature");
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return _image;
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
		String order;
		
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		Date toTime = null;
		
		try	{
			toTime = new Date();
			long start = System.currentTimeMillis();
			
			ps = conn.prepareStatement("SELECT WEBORDERNUM,SIGNATURE_TIMESTAMP, DELIVEREDTO, RECIPIENT, CONTAINS_ALCOHOL, SIGNATURE FROM (SELECT DISTINCT " +
					"WEBORDERNUM, EVENTID FROM DLV.CARTONSTATUS CS WHERE  CS.SCANDATE  between (SELECT MAX(LAST_EXPORT) LAST_EXPORT FROM CUST.SALESIGN_EXPORT " +
					"WHERE SUCCESS= 'Y') and to_date(?,'MM/DD/YYYY HH:MI:SS AM') AND CS.CARTONSTATUS = 'DELIVERED'  ) CS, DLV.SIGNATURE S " +
					"WHERE S.EVENTID = CS.EVENTID"); //toTime is required here because we want to know till what time we are getting the signatures. The same to Time
			//will be updated in the last export.
			ps.setString(1, sdf.format(toTime));
			rs = ps.executeQuery();
			
			ps1 = conn.prepareStatement("INSERT INTO CUST.SALE_SIGNATURE (SALE_ID, SIGNATURE_TIMESTAMP, DELIVEREDTO, RECIPIENT, CONTAINS_ALCOHOL, SIGNATURE) " +
					"VALUES (?,?,?,?,?,?)");
			
			while (rs.next()) {
				
				in=rs.getBytes("SIGNATURE");
				
				if(in!=null && in.length>0)
					{
					  	ps1.setString(1, rs.getString("WEBORDERNUM"));
					  	ps1.setTimestamp(2, rs.getTimestamp("SIGNATURE_TIMESTAMP"));
					  	ps1.setString(3, rs.getString("DELIVEREDTO"));
					  	ps1.setString(4, rs.getString("RECIPIENT"));
					  	ps1.setString(5, rs.getString("CONTAINS_ALCOHOL"));
					    ps1.setBytes(6, in);
					    ps1.addBatch();
					}
				
			}
	
			ps1.executeBatch();
			
			ps1 = conn.prepareStatement("INSERT INTO CUST.SALESIGN_EXPORT(LAST_EXPORT, SUCCESS) VALUES (?,'Y')");
			ps1.setTimestamp(1, new java.sql.Timestamp(toTime.getTime()));
			ps1.execute();
			
			long end = System.currentTimeMillis();
			
			System.out.println("Time spent syncing the signatures "+(end-start)/(1000)+" sec");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (ps1 != null)
					ps1.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static Map<String, DispatchNextTelVO> getDispatchResourceNextTel(Connection conn, Date dispatchDate) throws DlvResourceException {
		
		Map<String, DispatchNextTelVO> result = new HashMap<String, DispatchNextTelVO>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn
					.prepareStatement(" select resource_id, nextel_no, d.dispatch_id " +
									  " from transp.dispatch d, transp.dispatch_resource dr where d.dispatch_id = dr.dispatch_id and d.dispatch_date = ? ");
	
			ps.setDate(1, new java.sql.Date(dispatchDate.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				DispatchNextTelVO empNextTel = new DispatchNextTelVO();
				empNextTel.setEmployeeId(rs.getString("resource_id"));
				empNextTel.setNextTelNo(rs.getString("nextel_no"));
				empNextTel.setDispatchId(rs.getString("dispatch_id"));			
				
				result.put(empNextTel.getEmployeeId(), empNextTel);
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new DlvResourceException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return result;		
	}

	public static Map<String, AirclicNextelVO> getNXOutScan(Connection conn, Date scanDate) throws DlvResourceException {
		
		Map<String, AirclicNextelVO> result = new HashMap<String, AirclicNextelVO>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			final String scanStartTime = DateUtil.getDate(scanDate)+":12:00:00AM";
			final String scanEndTime = DateUtil.getDate(scanDate)+":11:59:59PM";
		
			ps = conn
					.prepareStatement(" select employee, asset, route, scandate "+
									  " from transp.assetstatus a1 where a1.scandate = ( " +
									  " select max(scandate) "+
									  " from transp.assetstatus a where a.scandate BETWEEN TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') AND TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') "+ 
									  " AND a.ASSET LIKE 'NX%' AND a.assetstatus='OUT' and a.employee=a1.employee)");
	
			ps.setString(1, scanStartTime);
            ps.setString(2, scanEndTime);
          
			rs = ps.executeQuery();
			while (rs.next()) {
				AirclicNextelVO nextTelVO = new AirclicNextelVO();
				nextTelVO.setEmployee(rs.getString("employee"));
				nextTelVO.setNextTelNo(rs.getString("asset") != null ? rs.getString("asset").replaceAll("[a-zA-Z]+","") : null);
				nextTelVO.setRouteNo(rs.getString("route"));
				nextTelVO.setScanDate(rs.getTimestamp("scandate"));
				
				result.put(nextTelVO.getEmployee(), nextTelVO);
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new DlvResourceException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return result;		
	}
	
	public static Map<String, String> getNextTelAssets(Connection conn) throws DlvResourceException {
		
		Map<String, String> result = new HashMap<String, String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(" select asset_no ASSETNO, asset_description as NEXTELNO from transp.asset a where a.asset_status not in 'O/S' ");
	
			rs = ps.executeQuery();
			while (rs.next()) {				
				result.put(rs.getString("NEXTELNO") != null ? rs.getString("NEXTELNO").replace("-", "") : null, rs.getString("ASSETNO"));
			}			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new DlvResourceException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return result;		
	}
	
	
	public static void updateEmployeeNexTelData(Connection conn, List<DispatchNextTelVO> employeeNextTels) throws DlvResourceException {
		
		PreparedStatement ps = null;	
		try	{			
			if(employeeNextTels !=null && employeeNextTels.size()>0)
			{
				ps = conn.prepareStatement(" UPDATE TRANSP.DISPATCH_RESOURCE set NEXTEL_NO = ? WHERE RESOURCE_ID = ? and DISPATCH_ID = ? ");
					
				for(DispatchNextTelVO empNextTel : employeeNextTels)	{
					ps.setString(1, empNextTel.getNextTelNo());	
					ps.setString(2, empNextTel.getEmployeeId());
					ps.setString(3, empNextTel.getDispatchId());
					ps.addBatch();
				}
				ps.executeBatch();							
			}
		} catch(SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if(ps!=null) ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String s[]) throws SQLException, NamingException, IOException	{
		
		Connection conn = getDataSource().getConnection();
		getSignatureData(conn, null);
		
	}

	public static DataSource getDataSource() throws NamingException {
		InitialContext initCtx = null;
		try {
			initCtx = getInitialContext();
			return (DataSource) initCtx.lookup("fddatasource");
		} finally {
			if (initCtx != null)
				try {
					initCtx.close();
				} catch (NamingException ne) {
				}
		}
	}

	static public InitialContext getInitialContext() throws NamingException {
		Hashtable h = new Hashtable();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, "t3://localhost:7001");
		return new InitialContext(h);
	}
	
	   
	   
}
