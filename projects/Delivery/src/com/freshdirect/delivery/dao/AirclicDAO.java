package com.freshdirect.delivery.dao;

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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.delivery.constants.EnumDeliveryMenuOption;
import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.AirclicCartonInfo;
import com.freshdirect.delivery.model.AirclicCartonScanDetails;
import com.freshdirect.delivery.model.AirclicMessageVO;
import com.freshdirect.delivery.model.AirclicNextelVO;
import com.freshdirect.delivery.model.AirclicTextMessageVO;
import com.freshdirect.delivery.model.DeliveryExceptionModel;
import com.freshdirect.delivery.model.DeliveryManifestVO;
import com.freshdirect.delivery.model.DeliverySummaryModel;
import com.freshdirect.delivery.model.DispatchNextTelVO;
import com.freshdirect.delivery.model.RouteNextelVO;
import com.freshdirect.delivery.model.SignatureVO;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.BuildingOperationDetails;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IBuildingOperationDetails;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.sms.CrmSmsDisplayInfo;
import com.freshdirect.sms.EnumSMSAlertStatus;
import com.freshdirect.sms.SmsPrefereceFlag;


public class AirclicDAO {
	
	public static List<AirclicMessageVO> getMessages(Connection conn)
			throws DlvResourceException {
		List<AirclicMessageVO> messages = new ArrayList<AirclicMessageVO>();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement("select * FROM DLV.AIRCLIC_MESSAGE");

			rs = ps.executeQuery();
			while (rs.next()) {
				AirclicMessageVO message = new AirclicMessageVO();
				message.setMessage(rs.getString("message"));
				message.setDescription(rs.getString("description"));
				messages.add(message);

			}
		} catch (SQLException e) {
			throw new DlvResourceException(e);
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

		return messages;
	}
	
	private static final String ROUTE_DWLD_QUERY = "select * FROM DLV.routedownload rd where ";
	
	public static Map<String,Set<String>> getUserId(Connection conn, AirclicTextMessageVO textMessage) throws DlvResourceException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,Set<String>> userIds = new HashMap<String,Set<String>>();
		String[] routes;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		try
		{
			CriteriaBuilder builder = new CriteriaBuilder();
			builder.addSql("to_char(scandate,'MM/dd/yyyy') = ?", new Object[]{df.format(textMessage.getDeliveryDate())} );
			
			if(textMessage.getOrderId()!=null)
				builder.addSql("order_number = ?", new Object[]{textMessage.getOrderId()} );
			if(textMessage.getRoute()!=null)
			{
				routes = textMessage.getRoute().split(",");
				builder.addInString("route", routes);
			}
			
			ps = conn.prepareStatement(ROUTE_DWLD_QUERY+builder.getCriteria());
		
			Object[] params = builder.getParams();
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				if(!userIds.containsKey(rs.getString("userId")))
				{
					Set routeSet = new HashSet();
					routeSet.add(rs.getString("route"));
					userIds.put(rs.getString("userId"),routeSet);
				}
				else
				{
					userIds.get(rs.getString("userId")).add(rs.getString("route"));
				}
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
			

			return userIds;
	}
	
	public static void sendMessage(Connection conn, Map<String,Set<String>> userIds, AirclicTextMessageVO message) throws SQLException
	{
		//airclic table

		PreparedStatement ps = null;
		try
		{
			
			
			if(userIds!=null && userIds.size()>0)
			{
				if(message.getOrderId()!=null)
				{
					ps = conn.prepareStatement("INSERT INTO DLV.ORDERUPDATE(USERID, ORDER_NUMBER, NOTE) VALUES(?,?,?)");
					Set<String> userIdSet = userIds.keySet();
					for(String userId: userIdSet)
					{
						ps.setString(1, userId);
						ps.setString(2, message.getOrderId());
						ps.setString(3, message.getMessage());
						ps.addBatch();
					}
					ps.executeBatch();
				}
				else
				{

					ps = conn.prepareStatement("INSERT INTO DLV.ROUTEUPDATE(USERID, ROUTE_NUMBER, NOTE) VALUES(?,?,?)");
					Set<String> userIdSet = userIds.keySet();
					
					for(String userId: userIdSet)
					{
						Set<String> routeSet = userIds.get(userId);
						
						for(String routeId: routeSet)
						{
							ps.setString(1, userId);
							ps.setString(2, routeId);
							ps.setString(3, message.getMessage());
							ps.addBatch();
						}
					}
						ps.executeBatch();
				
				}
				
			}
			if(message.getOrderId()!=null)
			{
				// @TODO update the airclic ORDERS table NOTE column if userId does not exist

				ps = conn.prepareStatement("UPDATE  DLV.ORDERS SET NOTE =? WHERE ORDER_NUMBER=?");
				
				
					ps.setString(1, message.getMessage());
					ps.setString(2, message.getOrderId());
					if(ps.executeUpdate()==0)
						throw new SQLException("Order is not in airclic.");
			
			}
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
			ps = conn.prepareStatement("INSERT INTO DLV.AIRCLIC_TXTMESSAGE(ID, CREATEDATE, DELIVERYDATE, SENDER, MESSAGE, " +
					"SOURCE, ROUTE, STOP, ORDERID) VALUES(?,SYSDATE,?,?,?,?,?,?,?)");
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
	
	public static SignatureVO getSignatureDetails(Connection conn, String order) throws DlvResourceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		SignatureVO vo = null;
		try {
			ps = conn.prepareStatement("select sale_id, signature_timestamp, deliveredto , recipient , contains_alcohol  from cust.sale_signature where sale_id = ?");

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
		} 
		catch(SQLException e)
		{
			throw new DlvResourceException(e);
		}
		finally {
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

	public static byte[] getSignature(Connection conn, String order) throws DlvResourceException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		byte[] _image = null;
		try {
			ps = conn.prepareStatement("select signature from cust.sale_signature where sale_id = ?");

			ps.setString(1, order);
			rs = ps.executeQuery();
			if (rs.next()) {
				_image = rs.getBytes("signature");
			}
		} 
		catch(SQLException e)
		{
			throw new DlvResourceException(e);
		}
		finally {
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
			ps = conn.prepareStatement("UPDATE DLV.AIRCLIC_TXTMESSAGE SET SENT_TO_AIRCLIC = ? WHERE ID = ?");
			ps.setString(1,message.getSentToAirclic());
			ps.setString(2,message.getId());
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
			ps = conn.prepareStatement("SELECT ORDERID ,ROUTE,MESSAGE, DELIVERYDATE FROM  DLV.AIRCLIC_TXTMESSAGE WHERE SENT_TO_AIRCLIC = 'N' AND DELIVERYDATE = TRUNC(SYSDATE)");
			rs = ps.executeQuery();
			while(rs.next())
			{
				AirclicTextMessageVO vo = new AirclicTextMessageVO();
				vo.setOrderId(rs.getString("orderid"));
				vo.setRoute(rs.getString("route"));
				vo.setMessage(rs.getString("message"));
				vo.setDeliveryDate(rs.getDate("DELIVERYDATE"));
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
	public static void getSignatureData(Connection conn, Date deliveryDate) throws DlvResourceException 
	{
		PreparedStatement ps = null,ps1 = null;
		ResultSet rs = null;
		byte[] in = null;
		
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		Date toTime = null;
		Date fromTime = null;
		try	{
			toTime = new Date();
			long start = System.currentTimeMillis();
			
			ps = conn.prepareStatement("SELECT NVL(MAX(LAST_EXPORT),SYSDATE-1/24) LAST_EXPORT FROM CUST.SALESIGN_EXPORT " +
					"WHERE SUCCESS= 'Y'");
			
			rs = ps.executeQuery();
			if(rs.next())
				fromTime = new java.util.Date(rs.getTimestamp("LAST_EXPORT").getTime());
			
			ps = conn.prepareStatement("SELECT WEBORDERNUM,INSERT_TIMESTAMP, DELIVEREDTO, RECIPIENT, CONTAINS_ALCOHOL, SIGNATURE FROM (SELECT DISTINCT " +
					"WEBORDERNUM, EVENTID FROM DLV.CARTONSTATUS CS WHERE  CS.INSERT_TIMESTAMP  between to_date(?,'MM/DD/YYYY HH:MI:SS AM') and " +
					"to_date(?,'MM/DD/YYYY HH:MI:SS AM') AND CS.CARTONSTATUS = 'DELIVERED') CS, DLV.SIGNATURE S WHERE S.INSERT_TIMESTAMP between " +
					"to_date(?,'MM/DD/YYYY HH:MI:SS AM') and to_date(?,'MM/DD/YYYY HH:MI:SS AM') AND S.EVENTID = CS.EVENTID"); 
			//toTime is required here because we want to know till what time we are getting the signatures. The same to Time
			//will be updated in the last export.
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			ps.setString(3, sdf.format(fromTime));
			ps.setString(4, sdf.format(toTime));
			
			rs = ps.executeQuery();
			
			ps1 = conn.prepareStatement("INSERT INTO CUST.SALE_SIGNATURE (SALE_ID, SIGNATURE_TIMESTAMP, DELIVEREDTO, RECIPIENT, CONTAINS_ALCOHOL, SIGNATURE) " +
					"VALUES (?,?,?,?,?,?)");
			
			int batchCount = 0;
			
			while (rs.next()) {
				
				in=rs.getBytes("SIGNATURE");
				
				if(in!=null && in.length>0)
					{
						ps1.setBytes(6, in);
					  	ps1.setString(1, rs.getString("WEBORDERNUM"));
					  	ps1.setTimestamp(2, rs.getTimestamp("INSERT_TIMESTAMP"));
					  	ps1.setString(3, rs.getString("DELIVEREDTO"));
					  	ps1.setString(4, rs.getString("RECIPIENT"));
					  	ps1.setString(5, rs.getString("CONTAINS_ALCOHOL"));
					    ps1.addBatch();
					    batchCount++;
					}
				
			}
			if(batchCount>0)
			ps1.executeBatch();
			
			ps1 = conn.prepareStatement("INSERT INTO CUST.SALESIGN_EXPORT(LAST_EXPORT, SUCCESS) VALUES (?,'Y')");
			ps1.setTimestamp(1, new java.sql.Timestamp(toTime.getTime()));
			ps1.execute();
			
			long end = System.currentTimeMillis();
			
			System.out.println("Time spent syncing the signatures "+(end-start)/(1000)+" sec");
		} 
		catch(SQLException e)
		{
			throw new DlvResourceException(e);
		}
		finally {
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
					.prepareStatement(" select resource_id, e.personfullname, nextel_no, d.dispatch_id " +
									  " from transp.dispatch d, transp.dispatch_resource dr, transp.kronos_employee e where d.dispatch_id = dr.dispatch_id and e.personnum = dr.resource_id and d.dispatch_date = ? ");
	
			ps.setDate(1, new java.sql.Date(dispatchDate.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				DispatchNextTelVO empNextTel = new DispatchNextTelVO();
				empNextTel.setEmployeeId(rs.getString("resource_id"));
				empNextTel.setEmployeeName(rs.getString("personfullname"));
				empNextTel.setNextTelNo(rs.getString("nextel_no"));
				empNextTel.setDispatchId(rs.getString("dispatch_id"));			
				
				result.put(empNextTel.getEmployeeId(), empNextTel);
			}
			
		}
		catch (Exception e) 
		{	
			throw new DlvResourceException(e);
		} 
		finally {
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
					.prepareStatement(" select employee, e.personfullname, asset, route, scandate "+
									  " from transp.assetstatus a1, transp.kronos_employee e where a1.scandate = ( " +
									  " select max(scandate) "+
									  " from transp.assetstatus a where a.scandate BETWEEN TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') AND TO_DATE(?, 'mm/dd/yyyy:hh:mi:ssam') "+ 
									  " AND a.ASSET LIKE 'NX%' AND a.assetstatus='OUT' and a.employee=a1.employee) and a1.employee=e.PERSONNUM ");
	
			ps.setString(1, scanStartTime);
            ps.setString(2, scanEndTime);
          
			rs = ps.executeQuery();
			while (rs.next()) {
				AirclicNextelVO nextTelVO = new AirclicNextelVO();
				nextTelVO.setEmployee(rs.getString("employee"));
				nextTelVO.setEmployeeName(rs.getString("personfullname"));
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
	
	public static List<AirclicCartonInfo> lookupCartonScanHistory(Connection conn, String salePk) throws SQLException {
		
		long start = System.currentTimeMillis();
		/* Carton scanning info */
		List<AirclicCartonInfo> cartons = new ArrayList<AirclicCartonInfo>();
		PreparedStatement ps =
			conn.prepareStatement(
				"SELECT ci.sale_id, ci.carton_number, ci.carton_type, ct.scandate, ct.action, ct.cartonstatus, ct.employee, ct.route, ct.stop, ct.nextelId, ct.userId, "+
						" ct.deliveredto, ct.returnreason, decode(ct.cartonstatus,'MOT',E.PERSONFULLNAME,' ') motdrivername "+						
						" FROM cust.carton_info ci, dlv.cartontracking ct, transp.kronos_employee e "+
						" WHERE ci.carton_number=ct.cartonId(+) and e.personnum(+)=ct.employee "+
						" AND ci.sale_id = ? "+
						" ORDER BY TO_NUMBER(ci.carton_number) asc, ct.scandate desc");
		
		ps.setString(1, salePk);

		ResultSet rs = ps.executeQuery();
		String currentCartonNumber = "";
		AirclicCartonInfo ci = null;
		List<AirclicCartonScanDetails> cartonDetailList = null;
		while (rs.next()) {
			String saleId = rs.getString("SALE_ID");
			String cartonNumber = rs.getString("CARTON_NUMBER");
			String cartonType = rs.getString("CARTON_TYPE");
			Date scanTime = rs.getTimestamp("SCANDATE");
			String action = rs.getString("ACTION");
			String cartonStatus = rs.getString("CARTONSTATUS");
			String employee = rs.getString("EMPLOYEE");
			String route = rs.getString("ROUTE");
			String stop = rs.getString("STOP");
			String nextel = rs.getString("NEXTELID");
			String userId = rs.getString("USERID");
			String deliveredTo = rs.getString("DELIVEREDTO");
			String returnReason = rs.getString("RETURNREASON");
			String motDriverName = rs.getString("MOTDRIVERNAME");

			if(!cartonNumber.equals(currentCartonNumber)) {
				ci = new AirclicCartonInfo(saleId, cartonNumber, cartonType);
				cartonDetailList = new ArrayList<AirclicCartonScanDetails>();
				ci.setDetails(cartonDetailList);
				cartons.add(ci);
				currentCartonNumber = cartonNumber;
			}
			
			AirclicCartonScanDetails cd = new AirclicCartonScanDetails(scanTime, action, cartonStatus, employee, route, stop, nextel, userId, deliveredTo, returnReason, motDriverName);
			cartonDetailList.add(cd);
		}

		rs.close();
		ps.close();
		
		long end = System.currentTimeMillis();
		
		System.out.println("Time spent loading carton scanning history for order # "+ salePk + " is "+(end-start)/(1000)+" sec");
		
		return cartons;
	}
	
	public static List<AirclicTextMessageVO> lookupAirclicMessages(Connection conn,  String orderId) throws DlvResourceException {
		
		long start = System.currentTimeMillis();
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<AirclicTextMessageVO> voList = new ArrayList<AirclicTextMessageVO>();
		try
		{
			ps = conn.prepareStatement("SELECT * FROM  DLV.AIRCLIC_TXTMESSAGE WHERE ORDERID = ? ");
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			while(rs.next())
			{
				AirclicTextMessageVO vo = new AirclicTextMessageVO();
				vo.setOrderId(rs.getString("orderid"));
				vo.setRoute(rs.getString("route"));
				vo.setMessage(rs.getString("message"));
				vo.setCreateDate(rs.getDate("createDate"));
				vo.setSender(rs.getString("sender"));
				vo.setSource(rs.getString("source"));
				vo.setStop(rs.getInt("stop"));
				vo.setSentToAirclic(rs.getString("SENT_TO_AIRCLIC"));
				voList.add(vo);
			}
		} catch (SQLException e) {
			throw new DlvResourceException(e);
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
		long end = System.currentTimeMillis();
		
		System.out.println("Time spent loading airclic messages for order # "+ orderId + " is "+(end-start)/(1000)+" sec");
		
		return voList;
	}
	
	public static DeliveryManifestVO getDeliveryManifest(Connection conn,  String orderId, Date deliveryDate) throws DlvResourceException {
			
			long start = System.currentTimeMillis();
			
			DeliveryManifestVO result = new DeliveryManifestVO();
			ResultSet rs = null;
			PreparedStatement ps = null;
			try
			{
				ps = conn.prepareStatement("select di.last_name,di.first_name, di.delivery_instructions, (select count(*) from cust.carton_info where sale_id=s.id) cartonCnt, "+			            
						" hb.DELIVERY_DATE, bs.WEBORDER_ID, bs.AREA, bs.DELIVERY_TYPE, " +
						" bs.WINDOW_STARTTIME ,bs.WINDOW_ENDTIME, bs.DLV_ETA_STARTTIME ,bs.DLV_ETA_ENDTIME, bs.LOCATION_ID, bs.STOP_SEQUENCE, bs.STOP_ARRIVALDATETIME, bs.STOP_DEPARTUREDATETIME, bs.SERVICE_ADDR2,  " +
						" dd.ADDR_TYPE ,dd.COMPANY_NAME ,dd.DIFFICULT_TO_DELIVER ,dd.DIFFICULT_REASON ,dd.ADDITIONAL,  " +
						" dd.IS_DOORMAN, dd.IS_WALKUP,dd.IS_ELEVATOR ,dd.IS_HOUSE  ,dd.IS_FREIGHT_ELEVATOR, " +
						" dd.SVC_ENT, dd.SVC_SCRUBBED_STREET, dd.SVC_CITY, dd.SVC_STATE, dd.SVC_ZIP, dd.HAND_TRUCK_ALLOWED, " +
						" dd.WALK_UP_FLOORS ,dd.CREATED_BY ,dd.MODTIME, dd.SVC_CROSS_STREET, dd.CROSS_STREET ,dd.OTHER, " +
						" bo.BLDG_START_HOUR, bo.BLDG_END_HOUR, bo.BLDG_COMMENTS, bo.SERVICE_START_HOUR, " +
						" bo.SERVICE_END_HOUR, bo.SERVICE_COMMENTS, bo.DAY_OF_WEEK, ta.DELIVERYMODEL, " +
						" b.SCRUBBED_STREET bSCRUBBED_STREET,b.ZIP bzip,b.COUNTRY bCOUNTRY,b.CITY bCITY, b.STATE bSTATE, " +
						" b.LONGITUDE  BLONG,b.LATITUDE BLAT, l.APARTMENT LOCAPART, (select message from dlv.airclic_txtmessage m where m.createdate = "+ 
                        " (select max(createdate) from dlv.airclic_txtmessage where orderId = bs.WEBORDER_ID)) as LAST_AIRCLIC_MSG " +
			            " from cust.sale s, cust.salesaction sa, cust.deliveryinfo di, transp.handoff_batch hb, transp.handoff_batchstop bs, dlv.delivery_building b, dlv.delivery_location l, dlv.delivery_building_detail dd, transp.trn_area ta, "+
			            " (select distinct xbo.* from transp.HANDOFF_BATCH xhb , transp.HANDOFF_BATCHSTOP xs "+
			            " ,DLV.DELIVERY_LOCATION xl, dlv.delivery_building xb, DLV.DELIVERY_BUILDING_DETAIL  xdd, DLV.DELIVERY_BUILDING_DETAIL_OPS xbo "+
			            " where xhb.batch_status in ('CPD','CPD/ADC','CPD/ADF') and xhb.DELIVERY_DATE = ? "+
			            " and xHB.BATCH_ID = xS.BATCH_ID and xS.LOCATION_ID = xL.ID and xL.BUILDINGID = xB.ID "+
			            " and xB.ID = xDD.DELIVERY_BUILDING_ID and xB.ID = xBO.DELIVERY_BUILDING_ID  "+
			            " and to_char(xHB.DELIVERY_DATE ,'D')  = xBO.DAY_OF_WEEK) bo "+
			            " where s.id=sa.sale_id and sa.id = di.salesaction_id and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "+
			            " and s.id=bs.weborder_id and hb.batch_status in ('CPD','CPD/ADC','CPD/ADF') and hb.delivery_date = ? and bs.batch_id=hb.batch_id "+
			            " and BS.LOCATION_ID = L.ID and L.BUILDINGID = B.ID and B.ID = DD.DELIVERY_BUILDING_ID(+) and B.ID = bo.DELIVERY_BUILDING_ID(+) and bs.area = ta.code(+)"+
			            " and s.id= ?");
				
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(3, orderId);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
	
					result.setFirstName(rs.getString("FIRST_NAME"));
					result.setLastName(rs.getString("LAST_NAME"));
					result.setDeliveryInstructions(rs.getString("DELIVERY_INSTRUCTIONS"));
					result.setCartonCnt(rs.getInt("CARTONCNT"));
					result.setStopNo(rs.getInt("STOP_SEQUENCE"));
					result.setStopArrivalTime(rs.getTimestamp("STOP_ARRIVALDATETIME"));
					result.setStopDepartureTime(rs.getTimestamp("STOP_DEPARTUREDATETIME"));
					result.setLastAirclicMsg(rs.getString("LAST_AIRCLIC_MSG"));
					
					IDeliveryModel deliveryModel = new DeliveryModel();
					deliveryModel.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
					deliveryModel.setDeliveryStartTime(rs.getTimestamp("WINDOW_STARTTIME"));
					deliveryModel.setDeliveryEndTime(rs.getTimestamp("WINDOW_ENDTIME"));
					deliveryModel.setServiceType(rs.getString("DELIVERY_TYPE"));
					deliveryModel.setDeliveryModel(rs.getString("DELIVERYMODEL"));
					deliveryModel.setDeliveryETAStartTime(rs.getTimestamp("DLV_ETA_STARTTIME"));
					deliveryModel.setDeliveryETAEndTime(rs.getTimestamp("DLV_ETA_ENDTIME"));
					
					IZoneModel zoneModel = new ZoneModel();
					zoneModel.setZoneNumber(rs.getString("AREA"));
					deliveryModel.setDeliveryZone(zoneModel);
					
					IBuildingModel bmodel = new BuildingModel();		
					
					bmodel.setSrubbedStreet(rs.getString("bSCRUBBED_STREET"));
					bmodel.setStreetAddress1(rs.getString("bSCRUBBED_STREET"));						
					bmodel.setCity(rs.getString("bCITY"));		
					bmodel.setState(rs.getString("bSTATE"));					
					bmodel.setZipCode(rs.getString("bZIP"));
					bmodel.setCountry(rs.getString("bCOUNTRY"));
					
					IGeographicLocation geoLoc = new GeographicLocation();
					geoLoc.setLatitude(rs.getString("BLAT"));
					geoLoc.setLongitude(rs.getString("BLONG"));
																
					bmodel.setGeographicLocation(geoLoc);
				
					bmodel.setAddrType(rs.getString("ADDR_TYPE"));
					bmodel.setCompanyName(rs.getString("COMPANY_NAME"));
					bmodel.setSvcScrubbedStreet(rs.getString("SVC_SCRUBBED_STREET"));
					bmodel.setSvcCrossStreet(rs.getString("SVC_CROSS_STREET"));
					bmodel.setSvcCity(rs.getString("SVC_CITY"));
					bmodel.setSvcState(rs.getString("SVC_STATE"));
					bmodel.setSvcZip(rs.getString("SVC_ZIP"));
					
					bmodel.setDoorman(getBoolean(rs.getString("IS_DOORMAN")));
					bmodel.setWalkup(getBoolean(rs.getString("IS_WALKUP")));
					bmodel.setElevator(getBoolean(rs.getString("IS_ELEVATOR")));
					bmodel.setSvcEnt(getBoolean(rs.getString("SVC_ENT")));
					bmodel.setHouse(getBoolean(rs.getString("IS_HOUSE")));
					bmodel.setFreightElevator(getBoolean(rs.getString("IS_FREIGHT_ELEVATOR")));
					bmodel.setHandTruckAllowed(getBoolean(rs.getString("HAND_TRUCK_ALLOWED")));
					bmodel.setDifficultToDeliver(getBoolean(rs.getString("DIFFICULT_TO_DELIVER")));
					
					bmodel.setWalkUpFloors(rs.getInt("WALK_UP_FLOORS"));
					bmodel.setOther(rs.getString("OTHER"));
					bmodel.setDifficultReason(rs.getString("DIFFICULT_REASON"));
					
					bmodel.setCrossStreet(rs.getString("CROSS_STREET"));
					if(rs.getString("DAY_OF_WEEK") != null) {
						IBuildingOperationDetails operationDetail = new BuildingOperationDetails();
						operationDetail.setDayOfWeek(rs.getString("DAY_OF_WEEK"));
						operationDetail.setBldgStartHour(rs.getTimestamp("BLDG_START_HOUR"));
						operationDetail.setBldgEndHour(rs.getTimestamp("BLDG_END_HOUR"));
						operationDetail.setServiceStartHour(rs.getTimestamp("SERVICE_START_HOUR"));
						operationDetail.setServiceEndHour(rs.getTimestamp("SERVICE_END_HOUR"));
						operationDetail.setBldgComments(rs.getString("BLDG_COMMENTS"));
						operationDetail.setServiceComments(rs.getString("SERVICE_COMMENTS"));
						
						Set<IBuildingOperationDetails> operationDetails = new HashSet<IBuildingOperationDetails>();
						operationDetails.add(operationDetail);
						bmodel.setOperationDetails(operationDetails);
					}
					
					ILocationModel locationModel = new LocationModel(bmodel);
					locationModel.setApartmentNumber(rs.getString("LOCAPART"));
											
					deliveryModel.setDeliveryLocation(locationModel);
					
					result.setDeliveryInfo(deliveryModel);
					
				}
				
			} catch (SQLException e) {
				throw new DlvResourceException(e);
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
			
			long end = System.currentTimeMillis();
			
			System.out.println("Time spent loading delivery manifest for order# "+ orderId + " is "+(end-start)/(1000)+" sec");
						
			return result;
	}
	
	public static boolean getBoolean(String value) {
		return (value != null && "1".equalsIgnoreCase(value));
	}
	
	private static final String GET_ROUTE_NEXTELS_QUERY = "select distinct rd.userid, E.PERSONFULLNAME as employeeName, dr.resource_id "+
			" from dlv.routedownload rd, transp.dispatch d, transp.dispatch_resource dr, transp.kronos_employee e "+
			" where dr.resource_id=e.personnum and rd.userid=dr.nextel_no and d.dispatch_date=trunc(rd.scandate) and d.route=rd.route "+
			" and d.dispatch_id=dr.dispatch_id and ";
	
	public static List<RouteNextelVO> getRouteNextels(Connection conn, AirclicTextMessageVO textMessage) throws DlvResourceException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteNextelVO> nextels = new ArrayList<RouteNextelVO>();
		String[] routes;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		try
		{
			CriteriaBuilder builder = new CriteriaBuilder();
			builder.addSql("to_char(scandate,'MM/dd/yyyy') = ?", new Object[]{df.format(textMessage.getDeliveryDate())} );
			
			if(textMessage.getOrderId()!=null)
				builder.addSql("rd.order_number = ?", new Object[]{textMessage.getOrderId()} );
			if(textMessage.getRoute()!=null)
				builder.addSql("rd.route = ?", new Object[]{textMessage.getRoute()} );
						
			ps = conn.prepareStatement(GET_ROUTE_NEXTELS_QUERY + builder.getCriteria());
		
			Object[] params = builder.getParams();
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				RouteNextelVO model = new RouteNextelVO(rs.getInt("userId"), rs.getString("EMPLOYEENAME"), rs.getString("RESOURCE_ID"));
				nextels.add(model);
			}
		} catch (SQLException e) {
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
		return nextels;
	}
	
	private static final String GET_ORDER_CALLLOG_QUERY = "select * from cust.ivr_calllog cl where cl.ordernumber = ? ";
	
	public static List<CallLogModel> getOrderCallLog(Connection conn, String orderId) throws DlvResourceException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CallLogModel> calllogs = new ArrayList<CallLogModel>();
		
		try
		{
			ps = conn.prepareStatement(GET_ORDER_CALLLOG_QUERY);
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			while (rs.next()) {
				CallLogModel model = new CallLogModel();
				model.setCallerGUIId(rs.getString("ID"));
				model.setCallerId(rs.getString("CALLERID"));
				model.setOrderNumber(rs.getString("ORDERNUMBER"));
				model.setStartTime(rs.getTimestamp("CALLTIME"));
				model.setDuration(rs.getInt("CALLDURATION"));
				model.setCallOutcome(rs.getString("CALL_OUTCOME"));
				model.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				model.setMenuOption(rs.getString("MENU_OPTION") != null ? EnumDeliveryMenuOption.getEnum(rs.getString("MENU_OPTION")).getDesc() : "");
				model.setTalkTime(rs.getInt("TALKTIME"));
				
				calllogs.add(model);
			}
		} catch (SQLException e) {
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
		return calllogs;
	}
	
	
	
	/*private static String GET_ESTIMATED_DELIVERYTIME_QUERY = "select bs.weborder_id, decode((select max(scandate) from dlv.cartonstatus where cartonstatus in ('DELIVERED','REFUSED') and webordernum=bs.weborder_id), null, (bs.stop_arrivaldatetime + (bo.scantime - bo.stop_arrivaldatetime)), "+
		" (select max(scandate) from dlv.cartonstatus where cartonstatus in ('DELIVERED','REFUSED') and webordernum=bs.weborder_id)) estimatedtime, "+
		" decode((select max(scandate) from dlv.cartonstatus where cartonstatus in ('DELIVERED','REFUSED') and webordernum=bs.weborder_id), null, '','X') deliverystatus "+
		" from transp.handoff_batch b, transp.handoff_batchstop bs, transp.handoff_batchroute r, "+
		" (select x.* from "+
		" (select bsx.stop_arrivaldatetime, (select max(scandate) from dlv.cartonstatus where cartonstatus in ('DELIVERED','REFUSED') and webordernum=bsx.weborder_id) as scantime "+
		" from transp.handoff_batch bx, transp.handoff_batchstop bsx, transp.handoff_batchroute rx "+
		" where bx.batch_id = bsx.batch_id and bsx.route_no=rx.route_no and bx.batch_id = rx.batch_id and rx.route_no = ? and bx.delivery_date = ?  and bx.batch_status in ('CPD','CPD/ADC','CPD/ADF') "+
		" order by scantime desc nulls last "+
		" ) x where rownum = 1) bo "+
		" where b.batch_id = bs.batch_id and bs.route_no = r.route_no and b.batch_id = r.batch_id and r.route_no = ? and b.delivery_date = ?  and b.batch_status in ('CPD','CPD/ADC','CPD/ADF') "+
		" and bs.weborder_id = ? ";
	
	private static String GET_ESTIMATED_DELIVERYTIME_QUERY = 
	
		" select bs.weborder_id, "+
			" bs.ROUTING_ROUTE_NO, "+
			" bs.stop_sequence, "+
			" DECODE(x.ATTEMPTED_TIME, NULL, 'F','T') AS ATTEMPTED_FLAG, "+
			" x.LATEISSUE_FLAG AS LATEISSUE_FLAG, "+
			" x.ORDER_MAX_TIME AS ORDER_MAXSCANTIME, "+
			" DECODE(y.ROUTE_MAXSCANTIME, null, bs.window_starttime, y.ROUTE_MAXSCANTIME) ROUTE_MAXSCANTIME, "+
			" DECODE(x.ATTEMPTED_TIME, null, (sysdate - (bs.servicetime+bs.traveltime)/86400), null) EMBARK_NEXTTIME, "+
			" ROUND((bs.servicetime + bs.traveltime)/60) work_time, "+
			" CASE WHEN X.ATTEMPTED_TIME IS NULL  "+
			" THEN "+
			" 	SUM(ROUND((BS.SERVICETIME + BS.TRAVELTIME)/60)) OVER (PARTITION BY BS.ROUTING_ROUTE_NO, DECODE(X.ATTEMPTED_TIME, NULL, 'F','T')  ORDER BY BS.STOP_SEQUENCE) "+  
			" ELSE 0  "+
			" END AS MIN_UNTIL_DELIVERY, "+
			" CASE WHEN X.ATTEMPTED_TIME IS NULL "+ 
			" THEN "+
			" 	GREATEST(DECODE(y.ROUTE_MAXSCANTIME, null, bs.window_starttime, y.ROUTE_MAXSCANTIME) + "+
			" 		SUM((BS.SERVICETIME + BS.TRAVELTIME)/86400) OVER (PARTITION BY BS.ROUTING_ROUTE_NO, DECODE(X.ATTEMPTED_TIME, NULL, 'F','T')  ORDER BY BS.STOP_SEQUENCE) "+
			" 		, DECODE(x.ATTEMPTED_TIME, null, (sysdate - (bs.servicetime+bs.traveltime)/86400), null) + "+
			" 		SUM((BS.SERVICETIME + BS.TRAVELTIME)/86400) OVER (PARTITION BY BS.ROUTING_ROUTE_NO, DECODE(X.ATTEMPTED_TIME, NULL, 'F','T')  ORDER BY BS.STOP_SEQUENCE) "+
			" 	) "+
			" ELSE NULL END as ESTIMATED_DLV_TIME "+
			" from transp.handoff_batch b, transp.handoff_batchstop bs, transp.handoff_batchroute r, "+
			" ( "+
			"      select distinct webordernum, routing_route_no, "+
			"      MAX(CASE WHEN CT.CARTONSTATUS IN  ('DELIVERED','REFUSED') THEN SCANDATE ELSE NULL END) OVER (PARTITION BY WEBORDERNUM) ATTEMPTED_TIME, "+
			"      MAX(SCANDATE) OVER (PARTITION BY WEBORDERNUM) ORDER_MAX_TIME, "+
			"      CASE WHEN LO.STOP_NUMBER IS NULL THEN 'F' ELSE 'T' END as LATEISSUE_FLAG "+            
			"      from transp.handoff_batch bx, transp.handoff_batchstop bsx, dlv.cartontracking ct, cust.lateissue_orders LO "+ 
			"      where bx.batch_id = bsx.batch_id  and BSx.route_no = ? and bx.delivery_date = ? and bx.batch_status in ('CPD','CPD/ADC','CPD/ADF') "+ 
			"      and bsx.weborder_id = ct.webordernum(+) "+
			"      and bsx.weborder_id = lo.stop_number(+) "+
			"      and ct.cartonstatus in ('DELIVERED','REFUSED','IN_TRANSIT') "+
			"  ) X, "+
			"  ( "+
			"      select bsx.ROUTING_ROUTE_NO, max(ct.scandate) AS  ROUTE_MAXSCANTIME "+ 
			"      from transp.handoff_batch bx, transp.handoff_batchstop bsx, dlv.cartontracking ct "+ 
			"      where bx.batch_id = bsx.batch_id and bsx.route_no = ? and bx.delivery_date = ? and bx.batch_status in ('CPD','CPD/ADC','CPD/ADF') "+ 
			"      and bsx.weborder_id=ct.webordernum(+) "+
			"      and ct.cartonstatus in ('DELIVERED','REFUSED','IN_TRANSIT') "+
			"      GROUP BY BSX.ROUTING_ROUTE_NO "+
			"  ) Y "+                                 
			" where b.batch_id = bs.batch_id "+ 
			" and bs.route_no = r.route_no and b.batch_id = r.batch_id "+ 
			" and r.route_no = ? and b.delivery_date = ?  and b.batch_status in ('CPD','CPD/ADC','CPD/ADF') "+
			" and x.webordernum(+) = bs.weborder_id "+
			" and y.ROUTING_ROUTE_NO(+) = bs.ROUTING_ROUTE_NO "+
			" and bs.weborder_id = ? ";
	
	*/
	
	private static String GET_ESTIMATED_DELIVERYTIME_QUERY = "select dlm.dlv_attempted_flag, dlm.eventlog_flag, estimated_dlv_time " +
			"  from mis.order_delivery_metric dlm where dlm.weborder_id = ? ";
	
	
	private static String GET_CARTONINFO_EXCEPTION = "select cartonid, cartonstatus "+
		" from dlv.cartonstatus where cartonstatus in ('RETURNED','WRONG_ITEMS','MISSING','REFUSED','PARTIAL REFUSED','DAMAGED','PARTIAL DAMAGED','PLANT LATE','TRNS_DR') "+
		" and webordernum = ? "+
		" order by cartonid, cartonstatus";
	
	private static String GET_DELIVERY_ATTEMPTS = "select webordernum, cartonid, "+
        " lag(cartonstatus) over (partition by webordernum order by scandate) previousstatus, cartonstatus currentstatus "+
        " from dlv.cartontracking where cartonstatus in ('IN_TRANSIT', 'DELIVERED','REFUSED') "+
        " and webordernum = ?";
	
	private static String GET_CARTONSCANSTATUS_QUERY = " select cartonstatus, max(scandate) SCANDATE from dlv.cartonstatus where webordernum = ? group by cartonstatus ";

	private static String GET_DELIVERY_REQ_QUERY = " select menu_option, call_outcome, calltime from cust.ivr_calllog where ordernumber = ? and menu_option in ('EDR','DAR')  ";
	
	private final static String GET_DELIVERY_ETA_WINDOW_QUERY = "SELECT s.id sale_id, bs.dlv_eta_starttime, bs.dlv_eta_endtime, z.EMAIL_ETA_ENABLED, z.SMS_ETA_ENABLED, z.MANIFEST_ETA_ENABLED "+
            "	from cust.sale s, cust.salesaction sa, cust.deliveryinfo di, dlv.reservation rs, dlv.timeslot ts, transp.zone z, transp.handoff_batch b, transp.handoff_batchstop bs "+
			"	where s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and s.cromod_date = sa.action_date and sa.action_type IN ('CRO', 'MOD')  "+
			"	and s.type = 'REG' and s.status <> 'CAN' and sa.id = di.salesaction_id and di.zone = z.zone_code "+
			"	and di.reservation_id = rs.ID and rs.timeslot_id = ts.id and ts.base_date = sa.requested_date "+                 
			"	and b.batch_status IN ('CPD/ADC','CPD','CPD/ADF') and b.BATCH_ID = bs.BATCH_ID and s.id =bs.weborder_id "+
			"	and sa.requested_date = b.delivery_date "+
			"	and s.id = ?";
	
	public static DeliverySummaryModel lookUpDeliverySummary(Connection conn, String orderId, String routeNo, Date deliveryDate) throws DlvResourceException {
		
		DeliverySummaryModel model = new DeliverySummaryModel();
		
		long start = System.currentTimeMillis();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(GET_ESTIMATED_DELIVERYTIME_QUERY);
			ps.setString(1, orderId);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {									
				model.setOrderDelivered("T".equals(rs.getString("DLV_ATTEMPTED_FLAG")) || "T".equals(rs.getString("EVENTLOG_FLAG")));
				model.setEstimatedDlvTime(rs.getTimestamp("ESTIMATED_DLV_TIME"));
			}			
			
		} catch (SQLException e) {
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
		
		try{
			ps = conn.prepareStatement(GET_DELIVERY_ATTEMPTS);			
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			int deliveryAttempts = 0;
			while(rs.next()) {	
				String prevStatus = rs.getString("PREVIOUSSTATUS");
				String currentStatus = rs.getString("CURRENTSTATUS");
				if(prevStatus != null && !prevStatus.equals(currentStatus) && (currentStatus.equals("DELIVERED") || currentStatus.equals("REFUSED"))){
					deliveryAttempts++;
				}				
			}
			model.setDeliveryAttempts(deliveryAttempts);
			
		} catch (SQLException e) {
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
		
		model.setMessages(AirclicDAO.lookupAirclicMessages(conn, orderId));
		
		try{
			ps = conn.prepareStatement(GET_CARTONINFO_EXCEPTION);			
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			Map<String, List<String>> exceptions = new HashMap<String, List<String>>();
			while(rs.next()) {	
				String cartonStatus = rs.getString("CARTONSTATUS");
				String cartonNumber = rs.getString("CARTONID");
				if("TRNS_DR".equals(cartonStatus)){
					cartonStatus = "LATE BOX";
				}				
				if(!exceptions.containsKey(cartonStatus)){
					exceptions.put(cartonStatus, new ArrayList<String>());
				}
				exceptions.get(cartonStatus).add(cartonNumber);				
			}
			model.setExceptions(exceptions);
			
		} catch (SQLException e) {
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
		
		try{
			ps = conn.prepareStatement(GET_CARTONSCANSTATUS_QUERY);			
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			boolean isDelivered = false;
			Date maxScanTime = null;
			String deliveryStatus = "N/A";
			while(rs.next()) {	
				String cartonStatus = rs.getString("CARTONSTATUS");
				Date scanDate = rs.getTimestamp("SCANDATE");
				
				if(cartonStatus.equals("DELIVERED")){
					deliveryStatus = cartonStatus;
					maxScanTime = scanDate;
					isDelivered = true;
					break;
				}
				if(!isDelivered && !cartonStatus.equals("DELIVERED") && (maxScanTime == null || (maxScanTime != null && scanDate.after(maxScanTime)))){
					deliveryStatus = cartonStatus;
					maxScanTime = scanDate;
				}
			}						
			
			model.setDeliveryStatus(deliveryStatus + (maxScanTime != null ? " @" + DateUtil.formatTime(maxScanTime) : ""));
			
		} catch (SQLException e) {
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
		
		try {
			ps = conn.prepareStatement(GET_DELIVERY_REQ_QUERY);			
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			
			Date maxDlvAccessCallTime = null;
			Date maxEarlyDlvCallTime = null;
			
			while(rs.next()){
				String requestType = rs.getString("MENU_OPTION");
				String callOutcome = rs.getString("CALL_OUTCOME");
				Date callTime = rs.getTimestamp("CALLTIME");
				
				if(EnumDeliveryMenuOption.DELIVERY_ACCESS.getName().equals(requestType) && (maxDlvAccessCallTime == null || (callTime != null && callTime.after(maxDlvAccessCallTime))) ) {
					model.setDeliveryAccessReq(true);
					model.setDlvAccessStatus((null != callOutcome && !"".equals(callOutcome) && !"NoAnswer".equals(callOutcome) && !"ReceiverRejected".equals(callOutcome)) ? "Y" : "N");
				}
				
				if(EnumDeliveryMenuOption.EARLY_DELIVERY.getName().equals(requestType) && (maxEarlyDlvCallTime == null || (callTime != null && callTime.after(maxEarlyDlvCallTime))) ) {
					model.setEarlyDeliveryReq(true);
					model.setEarlyDlvStatus((null != callOutcome && !"".equals(callOutcome) && !"NoAnswer".equals(callOutcome) && !"ReceiverRejected".equals(callOutcome)) ? "Y" : "N");
				}				
			}
			
			StringBuffer strBuf = new StringBuffer();
			if(model.isEarlyDeliveryReq()){
				strBuf.append("Early ").append("(").append(model.getEarlyDlvStatus()).append(")").append(" / ");
			}
			if(model.isDeliveryAccessReq()){
				strBuf.append("Access ").append("(").append(model.getEarlyDlvStatus()).append(")");
			}
			model.setCustomerContactStatus(strBuf.toString());
			
		} catch (SQLException e) {
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
		
		try{
			ps = conn.prepareStatement(GET_DELIVERY_ETA_WINDOW_QUERY);
			ps.setString(1, orderId);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				model.setDeliveryETAStart(rs.getTimestamp("DLV_ETA_STARTTIME"));
				model.setDeliveryETAEnd(rs.getTimestamp("DLV_ETA_ENDTIME"));
			}
			
		} catch (SQLException e) {
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
		
		long end = System.currentTimeMillis();
		
		System.out.println("Time spent in looking up delivery summary for order# "+ orderId + " is "+(end-start)/(1000)+" sec");
		
		return model;
	}

	public static Map<String, DeliveryExceptionModel>  getCartonScanInfo(Connection conn) throws DlvResourceException	{
		
		Map<String, DeliveryExceptionModel> result = new HashMap<String, DeliveryExceptionModel>();
		
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null;

		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		Date toTime = null;
		Date fromTime = null;
		try	{
			toTime = new Date();
			long start = System.currentTimeMillis();
			
			ps = conn.prepareStatement("SELECT NVL(MAX(LAST_EXPORT),SYSDATE-1/24) LAST_EXPORT FROM DLV.CREATECASE_EXPORT " +
					"WHERE SUCCESS= 'Y'");
			
			rs = ps.executeQuery();
			if(rs.next()) {
				fromTime = new java.util.Date(rs.getTimestamp("LAST_EXPORT").getTime());
			}
				
			ps = conn.prepareStatement("select webordernum, cartonid, insert_timestamp, returnreason, (select max(scandate) from dlv.cartontracking where webordernum = ct.webordernum and cartonstatus = 'REFUSED'" +
					" and ct.INSERT_TIMESTAMP between to_date(?,'MM/DD/YYYY HH:MI:SS AM') and to_date(?,'MM/DD/YYYY HH:MI:SS AM')) lastrefusedtime "+
					" from dlv.cartontracking ct where ct.INSERT_TIMESTAMP between to_date(?,'MM/DD/YYYY HH:MI:SS AM') and to_date(?,'MM/DD/YYYY HH:MI:SS AM') and ct.cartonstatus='REFUSED'"
					);
			
			//toTime is required here because we want to know till what time we are creating the cases. The same to Time
			//will be updated in the last export.
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			ps.setString(3, sdf.format(fromTime));
			ps.setString(4, sdf.format(toTime));
			
			rs = ps.executeQuery();
			
			DeliveryExceptionModel model = null;
			while(rs.next()){
				String orderId = rs.getString("WEBORDERNUM");
				String cartonId = rs.getString("CARTONID");				
				String returnReason = rs.getString("RETURNREASON");
				Date lastRefusedTime = rs.getTimestamp("LASTREFUSEDTIME");
				
				if(!result.containsKey(orderId) && !StringUtil.isEmpty(orderId)){
					model = new DeliveryExceptionModel();
					model.setOrderId(orderId);
					result.put(orderId, model);
				}
				
				result.get(orderId).setLastRefusedScan(lastRefusedTime);
				result.get(orderId).setReturnReason(returnReason);
				
				if(!result.get(orderId).getRefusedCartons().contains(cartonId)){
					result.get(orderId).getRefusedCartons().add(cartonId);
				}
			}
			
			ps = conn.prepareStatement(" select cl.ordernumber, cl.call_outcome from cust.ivr_calllog cl where cl.menu_option = 'EDR' " +
					" and cl.INSERT_TIMESTAMP between to_date(?,'MM/DD/YYYY HH:MI:SS AM') and to_date(?,'MM/DD/YYYY HH:MI:SS AM')"
					);
			
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			
			rs = ps.executeQuery();
			while(rs.next()){
				String orderId = rs.getString("ORDERNUMBER");
				String callOutcome = rs.getString("CALL_OUTCOME");
				
				if(!StringUtil.isEmpty(orderId)) {
					if(!result.containsKey(orderId) ) {
						model = new DeliveryExceptionModel();
						model.setOrderId(orderId);
						result.put(orderId, model);
					}
					result.get(orderId).setEarlyDeliveryReq(true);
					result.get(orderId).setEarlyDlvStatus((null != callOutcome && !"".equals(callOutcome) && !"NoAnswer".equals(callOutcome) && !"ReceiverRejected".equals(callOutcome)) ? "Accepted" : "Rejected");
				}				
			}
			
			ps = conn.prepareStatement(" select ci.sale_id WEBORDERNUM, ct.cartonid, ct.insert_timestamp, (select max(scandate) from dlv.cartontracking where cartonid = ct.cartonid and cartonstatus = 'TRNS_DR' "+
				    " and ct.INSERT_TIMESTAMP between to_date(?,'MM/DD/YYYY HH:MI:SS AM') and to_date(?,'MM/DD/YYYY HH:MI:SS AM') and pd_recipient <> '123457') scantime "+ 
				    " from dlv.cartontracking ct, cust.carton_info ci "+
				    " where ci.carton_number=ct.cartonid and ct.INSERT_TIMESTAMP between to_date(?,'MM/DD/YYYY HH:MI:SS AM') and to_date(?,'MM/DD/YYYY HH:MI:SS AM') "+
				    " and ct.cartonstatus='TRNS_DR' and ct.pd_recipient <> '123457'"
				    );
			//toTime is required here because we want to know till what time we are creating the cases. The same to Time
			//will be updated in the last export.
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			ps.setString(3, sdf.format(fromTime));
			ps.setString(4, sdf.format(toTime));
			
			rs = ps.executeQuery();
						
			while(rs.next()){
				String orderId = rs.getString("WEBORDERNUM");
				String cartonId = rs.getString("CARTONID");
				Date scanTime = rs.getTimestamp("SCANTIME");
				
				if(!result.containsKey(orderId) && !StringUtil.isEmpty(orderId)){
					model = new DeliveryExceptionModel();
					model.setOrderId(orderId);
					result.put(orderId, model);
				}
				
				result.get(orderId).setLateBoxScantime(scanTime);
								
				if(!result.get(orderId).getLateBoxes().contains(cartonId)){
					result.get(orderId).getLateBoxes().add(cartonId);
				}
			}
				
			ps1 = conn.prepareStatement("INSERT INTO DLV.CREATECASE_EXPORT(LAST_EXPORT, SUCCESS) VALUES (?,'Y')");
			ps1.setTimestamp(1, new java.sql.Timestamp(toTime.getTime()));
			ps1.execute();
			
			long end = System.currentTimeMillis();
			
			System.out.println("Time spent syncing the signatures "+(end-start)/(1000)+" sec");
		} catch(SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (ps1 != null)
					ps1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static List<CrmSmsDisplayInfo> getSmsInfo(Connection con, String orderId) throws SQLException{
		List<CrmSmsDisplayInfo> smsInfo = new ArrayList<CrmSmsDisplayInfo>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			String getSmsInfo ="select s.create_date, s.alert_type, s.message, s.status from mis.sms_alert_capture s,transp.handoff_batchstop bs  where "
					+"where s.mobile_number=bs.mobile_number and bs.weborder_id=s.order_id and bs.weborder_id =?";
			ps= con.prepareStatement(getSmsInfo);
			ps.setString(1,orderId);
			rs = ps.executeQuery();
			while(rs.next()){
				CrmSmsDisplayInfo tempSmsInfo= new CrmSmsDisplayInfo();
				tempSmsInfo.setTimeSent(rs.getTimestamp("create_date"));
				tempSmsInfo.setAlertType(rs.getString("alert_type"));
				tempSmsInfo.setMessage(rs.getString("alert_type"));
				tempSmsInfo.setStatus(rs.getString("status").equalsIgnoreCase("SUCCESS")?"Delivered":"Failed");
				smsInfo.add(tempSmsInfo);
			}
		} catch (SQLException e) {
			throw new SQLException();
		} finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(ps!=null){
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return smsInfo;
	}
   
}
