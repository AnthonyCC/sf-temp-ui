package com.freshdirect.delivery.sms.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.TransitInfo;
import com.freshdirect.delivery.sms.NextStopSmsInfo;
import com.freshdirect.delivery.sms.OrderDlvInfo;
import com.freshdirect.delivery.sms.SmsAlertETAInfo;

public class SmsDAO {
	
	private static final String NEXT_STOP_ALERT_TYPE = "NEXT_STOP";
	private static final String ETA_ALERT_TYPE = "SMS_ETA";
	private static final String UNATTENDED_OR_DOORMAN_ALERT_TYPE = "UNATTENDED_DOORMAN";
	private static final String DLV_ATTEMPTED_ALERT_TYPE = "DLV_ATTEMPTED";
	
	public List<NextStopSmsInfo> getNextStopSmsInfo(Connection con) throws DlvResourceException{
		
		List<NextStopSmsInfo> result= new ArrayList<NextStopSmsInfo>();
		List<TransitInfo> transitList = getTransitData(con);
		PreparedStatement ps = null;
		ResultSet rs = null;
		for (int i = 0; i < transitList.size(); i++) {
			try {
				ps = con.prepareStatement("select distinct bs.mobile_number, Z.NEXTSTOP_SMS_ENABLED, S.CUSTOMER_ID, bs.weborder_id from transp.handoff_batchstop bs, transp.handoff_batch b , TRANSP.TRN_AREA ta, TRANSP.ZONE z, cust.sale s "
						+ " where BS.AREA = TA.CODE and Z.AREA = ta.code and b.batch_id =  bs.batch_id "
						+ " and B.BATCH_STATUS in ('CPD/ADC','CPD','CPD/ADF') and bs.weborder_id=S.ID and  "
						+ " b.delivery_date = trunc(sysdate) and  bs.route_no=? and bs.stop_sequence=? and BS.mobile_number is not null");
				ps.setString(1, transitList.get(i).getRoute());
				ps.setInt(2, transitList.get(i).getNextStop());
				rs = ps.executeQuery();

				if (rs.next()) {
					if (rs.getString("MOBILE_NUMBER") != null
							|| (!"".equals(rs.getString("MOBILE_NUMBER")))
							|| rs.getString("NEXTSTOP_SMS_ENABLED") != null
							|| (!"".equals(rs.getString("NEXTSTOP_SMS_ENABLED")))) {
						
						NextStopSmsInfo _nextStopSmsModel = new NextStopSmsInfo();
						result.add(_nextStopSmsModel);
						
						_nextStopSmsModel.setCustomerId(rs.getString("CUSTOMER_ID"));
						_nextStopSmsModel.setMobileNumber(rs.getString("MOBILE_NUMBER"));
						_nextStopSmsModel.setDlvManager(transitList.get(i).getEnployeeId());
						_nextStopSmsModel.setOrderId(rs.getString("weborder_id"));						
					}
				}
			}catch(SQLException e)
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
		}
		
		return result;
	}
	
	private List<TransitInfo> getTransitData(Connection con) throws DlvResourceException{
		List<TransitInfo> transitList = new ArrayList<TransitInfo>();
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null;
		Date fromTime = null;
		Date toTime = null;
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		try{
			toTime = new Date();

			fromTime = getLastExport(con, NEXT_STOP_ALERT_TYPE);

			ps = con.prepareStatement("select TRANSIT_DATE, ROUTE, EMPLOYEE, LOCATION_SOURCE, LOCATION_DESTINATION, TRANSACTIONID, INSERT_TIMESTAMP from dlv.transit where INSERT_TIMESTAMP between to_date(?,'MM/DD/YYYY HH:MI:SS AM') and " +
								"to_date(?,'MM/DD/YYYY HH:MI:SS AM')");
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			rs = ps.executeQuery();
			
			ps1 = con.prepareStatement("INSERT INTO DLV.NEXTSTOP_SMS(TRANSIT_DATE, ROUTE, EMPLOYEE, LOCATION_SOURCE, LOCATION_DESTINATION, TRANSACTIONID, INSERT_TIMESTAMP)"+
								" VALUES (?, ?, ?, ?, ?, ?, ?)");
			int batchCount = 0;
			while (rs.next()) {
				ps1.setTimestamp(1, rs.getTimestamp("TRANSIT_DATE"));
				ps1.setString(2, rs.getString("ROUTE"));
				ps1.setString(3, rs.getString("EMPLOYEE"));
				ps1.setInt(4, rs.getInt("LOCATION_SOURCE"));
				ps1.setInt(5, rs.getInt("LOCATION_DESTINATION"));
				ps1.setString(6, rs.getString("TRANSACTIONID"));
				ps.setTimestamp(7, rs.getTimestamp("INSERT_TIMESTAMP"));
				
				ps1.addBatch();
				batchCount++;
			    
				TransitInfo _transitModel = new TransitInfo();
				_transitModel.setTransitDate(rs.getTimestamp("TRANSIT_DATE"));
				_transitModel.setRoute(rs.getString("ROUTE"));
				_transitModel.setNextStop(rs.getInt("LOCATION_DESTINATION"));
				_transitModel.setInsertTimeStamp(rs.getTimestamp("INSERT_TIMESTAMP"));
				_transitModel.setEnployeeId(rs.getString("EMPLOYEE"));
				transitList.add(_transitModel);
			}
			
			if (batchCount > 0)
				ps1.executeBatch();
			
			updateLastExport(con, NEXT_STOP_ALERT_TYPE);
			
		}catch(SQLException e)
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
				e.printStackTrace();
			}
		}
		return transitList;
	}
	
	public List<SmsAlertETAInfo> getETAInfo(Connection con) throws DlvResourceException{
		List<SmsAlertETAInfo> etaInfoList = new ArrayList<SmsAlertETAInfo>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String GET_ETA_WINDOW = "SELECT BS.WEBORDER_ID, bs.DLV_ETA_STARTTIME ,bs.DLV_ETA_ENDTIME, BS.MOBILE_NUMBER, z.SMS_ETA_ENABLED,S.CUSTOMER_ID" + 
					"  from transp.handoff_batch b, transp.handoff_batchstop bs, TRANSP.TRN_AREA ta, TRANSP.ZONE z, cust.sale s" + 
					"  where  b.batch_status IN ('CPD/ADC','CPD','CPD/ADF') and b.BATCH_ID = bs.BATCH_ID" +
					"  and   BS.AREA = TA.CODE and Z.AREA = ta.code and bs.weborder_id=S.ID"+
					"  and b.delivery_date = trunc(sysdate) and bs.DLV_ETA_STARTTIME is not null and mobile_number is not null";

			ps = con.prepareStatement(GET_ETA_WINDOW);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("MOBILE_NUMBER") != null
						|| (!rs.getString("MOBILE_NUMBER").equals(""))) {
					
					SmsAlertETAInfo smsAlertETAInfo=new SmsAlertETAInfo();
					smsAlertETAInfo.setCustomerId(rs.getString("CUSTOMER_ID"));
					smsAlertETAInfo.setMobileNumber(rs.getString("MOBILE_NUMBER"));
					smsAlertETAInfo.setEtaStartTime(rs.getTimestamp("DLV_ETA_STARTTIME"));
					smsAlertETAInfo.setEtaEndTime(rs.getTimestamp("DLV_ETA_ENDTIME"));
					smsAlertETAInfo.setEtaEnabled(rs.getString("SMS_ETA_ENABLED")!=null?true:false);
					smsAlertETAInfo.setOrderId(rs.getString("WEBORDER_ID"));
					etaInfoList.add(smsAlertETAInfo);
				}
			}
			updateLastExport(con, ETA_ALERT_TYPE);
		}catch(SQLException e)
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
		return etaInfoList;
	}
	
	public List<OrderDlvInfo> getUnattendedDoormanDlvInfo(Connection con) throws DlvResourceException{
		
		List<OrderDlvInfo> result = new ArrayList<OrderDlvInfo>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Date toTime = new Date();
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		try{
			Date fromTime = getLastExport(con, UNATTENDED_OR_DOORMAN_ALERT_TYPE);
			String sql = "select CI.CUSTOMER_ID, BS.MOBILE_NUMBER, z.DLV_UNATTENDED_SMS_ENABLED, BS.WEBORDER_ID " + 
					"from DLV.CARTONTRACKING cs, transp.handoff_Batch b, transp.handoff_batchstop bs, cust.customerinfo ci, TRANSP.TRN_AREA ta, TRANSP.ZONE z " + 
					"where  b.batch_id = bs.batch_id and B.DELIVERY_DATE = TRUNC(SYSDATE) and B.BATCH_STATUS in ('CPD/ADC','CPD','CPD/ADF') " + 
					"and BS.WEBORDER_ID = CS.WEBORDERNUM   " + 
					"AND CARTONSTATUS =  'DELIVERED'   " + 
					"AND CS.DELIVEREDTO IN ('Doorman','Unattended Delivery')" + 
					"AND CS.USERID=CI.EMAIL" + 
					"AND   BS.AREA = TA.CODE and Z.AREA = ta.code" + 
					"AND CS.INSERT_TIMESTAMP BETWEEN to_date(?,'MM/DD/YYYY HH:MI:SS AM') AND to_date(?,'MM/DD/YYYY HH:MI:SS AM')";
			
			/*ps = con.prepareStatement("select CI.CUSTOMER_ID, BS.MOBILE_NUMBER, z.DLV_UNATTENDED_SMS_ENABLED, BS.WEBORDER_ID   " +
					"from DLV.CARTONTRACKING cs, transp.handoff_Batch b, transp.handoff_batchstop bs, cust.customerinfo ci, TRANSP.TRN_AREA ta, TRANSP.ZONE z " + 
					"where  b.batch_id = bs.batch_id and B.DELIVERY_DATE = TRUNC(SYSDATE) and B.BATCH_STATUS in ('CPD/ADC','CPD','CPD/ADF') " + 
					"and BS.WEBORDER_ID = CS.WEBORDERNUM   " + 
					"AND CARTONSTATUS =  'DELIVERED'   " + 
					"AND CS.DELIVEREDTO IN ('Doorman','Unattended Delivery') " +
					"AND CS.USERID=CI.EMAIL"+
					"AND   BS.AREA = TA.CODE and Z.AREA = ta.code"+
					"AND CS.INSERT_TIMESTAMP BETWEEN to_date(?,'MM/DD/YYYY HH:MI:SS AM') AND to_date(?,'MM/DD/YYYY HH:MI:SS AM')");*/
			ps = con.prepareStatement(sql);
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString("MOBILE_NUMBER")!=null || (!rs.getString("MOBILE_NUMBER").equals(""))
						||rs.getString("DLV_UNATTENDED_SMS_ENABLED")!=null || (!rs.getString("DLV_UNATTENDED_SMS_ENABLED").equals(""))){
					
					OrderDlvInfo _orderInfo = new OrderDlvInfo();
					result.add(_orderInfo);
					
					_orderInfo.setCustomerId(rs.getString("CUSTOMER_ID"));
					_orderInfo.setMobileNumber(rs.getString("MOBILE_NUMBER"));
					_orderInfo.setOrderId(rs.getString("WEBORDER_ID"));
				}
				
			}
			updateLastExport(con, UNATTENDED_OR_DOORMAN_ALERT_TYPE);
		}catch(SQLException e)
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
	
	public List<OrderDlvInfo> getDlvAttemptedInfo(Connection con) throws DlvResourceException{
		List<OrderDlvInfo> dlvAttemptedInfo = new ArrayList<OrderDlvInfo>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Date toTime = new Date();
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		try{
			Date fromTime = getLastExport(con, DLV_ATTEMPTED_ALERT_TYPE);
			
			ps = con.prepareStatement("select BS.MOBILE_NUMBER, S.CUSTOMER_ID, z.DLV_ATTEMPTED_SMS_ENABLED, BS.WEBORDER_ID "
					+ " from DLV.CARTONTRACKING cs, transp.handoff_Batch b, transp.handoff_batchstop bs,  cust.sale s, TRANSP.TRN_AREA ta, TRANSP.ZONE z "
					+ " where  b.batch_id = bs.batch_id and B.DELIVERY_DATE =trunc(sysdate)  "
					+ " and B.BATCH_STATUS in ('CPD/ADC','CPD','CPD/ADF') "
					+ " and BS.WEBORDER_ID = CS.WEBORDERNUM  "
					+ " and BS.AREA = TA.CODE and Z.AREA = ta.code and bs.weborder_id=S.ID "
					+ " and CS.RETURNREASON IS NOT NULL  "
					+ " AND CARTONSTATUS =  'REFUSED'   "
					+ " AND RETURNREASON = 'CNH' AND CS.INSERT_TIMESTAMP BETWEEN to_date(?,'MM/DD/YYYY HH:MI:SS AM') AND to_date(?,'MM/DD/YYYY HH:MI:SS AM')");
			
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString("MOBILE_NUMBER")!=null || (!rs.getString("MOBILE_NUMBER").equals(""))
						||rs.getString("DLV_ATTEMPTED_SMS_ENABLED")!=null || (!rs.getString("DLV_ATTEMPTED_SMS_ENABLED").equals(""))){
					
					OrderDlvInfo tempInfo = new OrderDlvInfo();
					tempInfo.setCustomerId(rs.getString("CUSTOMER_ID"));
					tempInfo.setMobileNumber(rs.getString("MOBILE_NUMBER"));
					tempInfo.setOrderId(rs.getString("WEBORDER_ID"));
					dlvAttemptedInfo.add(tempInfo);
				}
			}
			updateLastExport(con, DLV_ATTEMPTED_ALERT_TYPE);
			
		}catch(SQLException e)
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
		return dlvAttemptedInfo;
	}
	
	/**
	 * gets the last export for the given sms alert type.
	 * @throws DlvResourceException 
	 */
	private Date getLastExport(Connection con, String alertType) throws DlvResourceException {
		Date lastExport=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = con.prepareStatement("SELECT NVL(MAX(LAST_EXPORT),SYSDATE-1/24) LAST_EXPORT FROM DLV.SMS_TRANSIT_EXPORT "
					+ "WHERE SUCCESS= 'Y' and SMS_ALERT_TYPE=?");
			ps.setString(1, alertType);
			rs = ps.executeQuery();
			if (rs.next()) {
				lastExport = new java.util.Date(rs.getTimestamp("LAST_EXPORT").getTime());
			}
		} catch(SQLException e) {
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
		return lastExport;
	}
	
	/**
	 * updated the SMS_transit_export table for COrresponding alert type.
	 * @throws DlvResourceException 
	 */
	private void updateLastExport(Connection con, String alertType) throws DlvResourceException {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("INSERT INTO DLV.SMS_TRANSIT_EXPORT(LAST_EXPORT, SUCCESS, SMS_ALERT_TYPE) VALUES (?,'Y',?)");
			ps.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
			ps.setString(2, alertType);
			ps.execute();
		} catch(SQLException e) {
			throw new DlvResourceException(e);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
