package com.freshdirect.delivery.sms.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.model.TransitInfo;
import com.freshdirect.delivery.sms.NextStopSmsInfo;
import com.freshdirect.delivery.sms.OrderDlvInfo;
import com.freshdirect.delivery.sms.SmsAlertETAInfo;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.sms.model.st.STSmsResponse;

public class SmsDAO {
	
	private static final String NEXT_STOP_ALERT_TYPE = "NEXT_STOP";
	private static final String ETA_ALERT_TYPE = "SMS_ETA";
	private static final String UNATTENDED_OR_DOORMAN_ALERT_TYPE = "DLV_UNATTENDED";
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
							&& rs.getString("NEXTSTOP_SMS_ENABLED") != null) {
						
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
			
			ps = con.prepareStatement("select TRANSIT_DATE, ROUTE, EMPLOYEE, LOCATION_SOURCE, LOCATION_DESTINATION, TRANSACTIONID, INSERT_TIMESTAMP from dlv.transit where INSERT_TIMESTAMP is not null and TRANSIT_DATE is not null and INSERT_TIMESTAMP between to_date(?,'MM/DD/YYYY HH:MI:SS AM') and " +
								"to_date(?,'MM/DD/YYYY HH:MI:SS AM')");
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			rs = ps.executeQuery();
			
			ps1 = con.prepareStatement("INSERT INTO DLV.NEXTSTOP_SMS(TRANSIT_DATE, ROUTE, EMPLOYEE, LOCATION_SOURCE, LOCATION_DESTINATION, TRANSACTIONID, INSERT_TIMESTAMP)"+
								" VALUES (?, ?, ?, ?, ?, ?, ?)");
			int batchCount = 0;
			int limit=30;
			while (rs.next()) {
				Date _transitDate=rs.getTimestamp("TRANSIT_DATE");
				Date _insertTimeStamp= rs.getTimestamp("INSERT_TIMESTAMP");
				String _locationDestination = rs.getString("LOCATION_DESTINATION");
				
				long _diffInMins=(_insertTimeStamp.getTime()-_transitDate.getTime())/(60 * 1000);
				try {
					limit=DlvProperties.getNextStopSmsNoSend();
				} catch (NumberFormatException e) {
					limit=30;
				}
				if(_diffInMins < limit && StringUtil.isNumeric(_locationDestination)) {
					ps1.setTimestamp(1, rs.getTimestamp("TRANSIT_DATE"));
					ps1.setString(2, rs.getString("ROUTE"));
					ps1.setString(3, rs.getString("EMPLOYEE"));
					ps1.setString(4, rs.getString("LOCATION_SOURCE"));
					ps1.setString(5, _locationDestination);
					ps1.setString(6, rs.getString("TRANSACTIONID"));
					ps1.setTimestamp(7, rs.getTimestamp("INSERT_TIMESTAMP"));
					
					ps1.addBatch();
					batchCount++;
				    
					TransitInfo _transitModel = new TransitInfo();
					_transitModel.setTransitDate(rs.getTimestamp("TRANSIT_DATE"));
					_transitModel.setRoute(rs.getString("ROUTE"));
					_transitModel.setNextStop(Integer.parseInt(_locationDestination));
					_transitModel.setInsertTimeStamp(rs.getTimestamp("INSERT_TIMESTAMP"));
					_transitModel.setEnployeeId(rs.getString("EMPLOYEE"));
					transitList.add(_transitModel);
				}
			}
			
			if (batchCount > 0)
				ps1.executeBatch();
			
			updateLastExport(con, NEXT_STOP_ALERT_TYPE,toTime);
			
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
		Date toTime=new Date();
		try {
			String GET_ETA_WINDOW = "select BS.WEBORDER_ID, bs.DLV_ETA_STARTTIME ,bs.DLV_ETA_ENDTIME,BS.WINDOW_STARTTIME, BS.WINDOW_ENDTIME, BS.MOBILE_NUMBER, " +
					"z.SMS_ETA_ENABLED, z.DLV_WINDOW_REMINDER_ENABLED, S.CUSTOMER_ID " +
					"FROM TRANSP.HANDOFF_BATCH B, TRANSP.HANDOFF_BATCHACTION BA, TRANSP.HANDOFF_BATCHSTOP BS, CUST.SALE S, TRANSP.TRN_AREA TA, " +
					"TRANSP.ZONE Z, CUST.CUSTOMERINFO CI WHERE B.BATCH_ID = BA.BATCH_ID AND B.BATCH_ID = BS.BATCH_ID AND BS.AREA  = Z.ZONE_CODE " +
					"AND Z.AREA = TA.CODE  AND BS.MOBILE_NUMBER IS NOT NULL AND S.ID = BS.WEBORDER_ID AND B.BATCH_STATUS IN ('CPD/ADC','CPD','CPD/ADF') " +
					"AND ACTION_TYPE = 'COM' AND BA.ACTION_DATETIME > (SELECT NVL(MAX(LAST_EXPORT),SYSDATE-1/24) FROM DLV.SMS_TRANSIT_EXPORT ST " +
					"WHERE ST.SMS_ALERT_TYPE = 'SMS_ETA' AND ST.SUCCESS = 'Y') AND CI.CUSTOMER_ID = S.CUSTOMER_ID AND CI.ORDER_NOTIFICATION = 'S' " +
					"AND  (SMS_ETA_ENABLED IS NOT NULL OR DLV_WINDOW_REMINDER_ENABLED IS NOT NULL)";

			ps = con.prepareStatement(GET_ETA_WINDOW);
			rs = ps.executeQuery();
			while (rs.next()) {
					SmsAlertETAInfo smsAlertETAInfo=new SmsAlertETAInfo();
					smsAlertETAInfo.setCustomerId(rs.getString("CUSTOMER_ID"));
					smsAlertETAInfo.setMobileNumber(rs.getString("MOBILE_NUMBER"));
					smsAlertETAInfo.setEtaStartTime(rs.getTimestamp("DLV_ETA_STARTTIME"));
					smsAlertETAInfo.setEtaEndTime(rs.getTimestamp("DLV_ETA_ENDTIME"));
					smsAlertETAInfo.setEtaEnabled(rs.getString("SMS_ETA_ENABLED")!=null?true:false);
					smsAlertETAInfo.setOrderId(rs.getString("WEBORDER_ID"));
					smsAlertETAInfo.setWindowStartTime(rs.getTimestamp("WINDOW_STARTTIME"));
					smsAlertETAInfo.setWindowEndTime(rs.getTimestamp("WINDOW_ENDTIME"));
					if(rs.getString("SMS_ETA_ENABLED")!=null){
						smsAlertETAInfo.setETA(true);
					} else if (rs.getString("DLV_WINDOW_REMINDER_ENABLED")!=null){
						smsAlertETAInfo.setETA(false);
					} 
					etaInfoList.add(smsAlertETAInfo);
				}
			updateLastExport(con, ETA_ALERT_TYPE,toTime);
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
			String sql = "select  DISTINCT CI.CUSTOMER_ID, BS.MOBILE_NUMBER, z.DLV_UNATTENDED_SMS_ENABLED, BS.WEBORDER_ID " + 
					"from DLV.CARTONTRACKING cs, transp.handoff_Batch b, transp.handoff_batchstop bs,cust.sale s, cust.customerinfo ci, TRANSP.TRN_AREA ta, TRANSP.ZONE z " + 
					"where  b.batch_id = bs.batch_id and B.DELIVERY_DATE = TRUNC(SYSDATE) and B.BATCH_STATUS in ('CPD/ADC','CPD','CPD/ADF') " + 
					"and BS.WEBORDER_ID = CS.WEBORDERNUM   " + 
					"AND CARTONSTATUS =  'DELIVERED'   " + 
					"AND CS.DELIVEREDTO IN ('Doorman','Unattended Delivery') " + 
					"AND BS.WEBORDER_ID = S.ID and S.CUSTOMER_ID = CI.CUSTOMER_ID " + 
					"AND   BS.AREA = TA.CODE and Z.AREA = ta.code " + 
					"AND CS.INSERT_TIMESTAMP BETWEEN to_date(?,'MM/DD/YYYY HH:MI:SS AM') AND to_date(?,'MM/DD/YYYY HH:MI:SS AM')";
			
			
			ps = con.prepareStatement(sql);
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString("MOBILE_NUMBER")!=null 
						&& rs.getString("DLV_UNATTENDED_SMS_ENABLED")!=null ){
					
					OrderDlvInfo _orderInfo = new OrderDlvInfo();
					result.add(_orderInfo);
					
					_orderInfo.setCustomerId(rs.getString("CUSTOMER_ID"));
					_orderInfo.setMobileNumber(rs.getString("MOBILE_NUMBER"));
					_orderInfo.setOrderId(rs.getString("WEBORDER_ID"));
				}
				
			}
			updateLastExport(con, UNATTENDED_OR_DOORMAN_ALERT_TYPE,toTime);
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
			
			ps = con.prepareStatement("select DISTINCT BS.MOBILE_NUMBER, S.CUSTOMER_ID, z.DLV_ATTEMPTED_SMS_ENABLED, BS.WEBORDER_ID "
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
				if(null!=rs.getString("MOBILE_NUMBER") && null!=rs.getString("DLV_ATTEMPTED_SMS_ENABLED") ){
					
					OrderDlvInfo tempInfo = new OrderDlvInfo();
					tempInfo.setCustomerId(rs.getString("CUSTOMER_ID"));
					tempInfo.setMobileNumber(rs.getString("MOBILE_NUMBER"));
					tempInfo.setOrderId(rs.getString("WEBORDER_ID"));
					dlvAttemptedInfo.add(tempInfo);
				}
				
			}
			updateLastExport(con, DLV_ATTEMPTED_ALERT_TYPE,toTime);
			
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
	private void updateLastExport(Connection con, String alertType, Date toTime) throws DlvResourceException {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("INSERT INTO DLV.SMS_TRANSIT_EXPORT(LAST_EXPORT, SUCCESS, SMS_ALERT_TYPE) VALUES (?,'Y',?)");
			ps.setTimestamp(1, new java.sql.Timestamp(toTime.getTime()));
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
	
	public void batchSmsResponseUpdate(Connection con, List<STSmsResponse> etaSmsUpdateList) throws DlvResourceException{
		int batchCount = 0;
		PreparedStatement ps = null;
		try{
			
			ps = con.prepareStatement("INSERT INTO MIS.SMS_ALERT_CAPTURE "
					+ "(ID, USER_ID, ALERT_TYPE,CREATE_DATE,INSERT_DATE,STATUS,ERROR,ERROR_DESC,MOBILE_NUMBER,MESSAGE,STI_SMS_ID,ORDER_ID)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			
			for(STSmsResponse smsResponseModel : etaSmsUpdateList){
				String customerId = smsResponseModel.getCustomerId();
				//STSmsResponse smsResponseModel = etaSmsUpdateList.get(customerId);
				PhoneNumber phone = new PhoneNumber(String.valueOf(smsResponseModel.getSms_to()));
				ps.setInt(1, Integer.parseInt(SequenceGenerator.getNextIdFromSequence(
						con, "MIS.SMS_ALERT_SEQ")));
				ps.setString(2, customerId);
				//ETA_ALERT_TYPE
				ps.setString(3, ETA_ALERT_TYPE);
				ps.setTimestamp(4, new java.sql.Timestamp(smsResponseModel.getDate().getTime()));
				ps.setTimestamp(5, new java.sql.Timestamp(smsResponseModel.getDate().getTime()));
				ps.setString(6, smsResponseModel.getStatus());
				ps.setInt(7, smsResponseModel.getError());
				ps.setString(8, smsResponseModel.getError_description());
				ps.setString(9, phone.getPhone());
				ps.setString(10, smsResponseModel.getSms_msg());
				ps.setInt(11, smsResponseModel.getSms_id());
				ps.setString(12, smsResponseModel.getOrderId() != null ? smsResponseModel.getOrderId() : null);
				ps.addBatch();
				batchCount++;
			}
			if(batchCount>0){
				ps.executeBatch();
			}
		}catch(SQLException e) {
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
