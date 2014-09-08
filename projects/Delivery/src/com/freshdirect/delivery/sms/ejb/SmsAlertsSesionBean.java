package com.freshdirect.delivery.sms.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.sms.NextStopSmsInfo;
import com.freshdirect.delivery.sms.OrderDlvInfo;
import com.freshdirect.delivery.sms.SmsAlertETAInfo;
import com.freshdirect.delivery.sms.SmsCustInfo;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sms.EnumSMSAlertStatus;
import com.freshdirect.sms.FDSmsGateway;
import com.freshdirect.sms.model.st.STSmsResponse;
import com.freshdirect.sms.service.SmsServiceException;

public class SmsAlertsSesionBean extends SessionBeanSupport {

	private static final long serialVersionUID = 644348982911326540L;

	private static Category LOGGER = LoggerFactory.getInstance(SmsAlertsSesionBean.class);

	private static final String OPT_IN_MESSAGE = "Reply YES to get automated FreshTexts msgs. Consent not required to purchase. Up to 5msgs/order. Msg&Data rates may apply. Reply HELP for help, STOP to cancel.";
	private static final String NEXT_STOP_TEXT_1 = "Your FreshDirect delivery is next! Delivery Service Representative ";
	private static final String NEXT_STOP_TEXT_2 = " will be there soon.";
	private static final String OPTIN_ALERT_TYPE = "OPTIN";
	private static final String OPTOUT_ALERT_TYPE = "OPTOUT";
	private static final String HELP_ALERT_TYPE = "HELP";
	private static final String ETA_ALERT_TYPE = "SMS_ETA";
	private static final String DLV_ATTEMPTED_ALERT_TYPE = "DLV_ATTEMPTED";
	private static final String UNATTENDED_OR_DOORMAN_ALERT_TYPE = "UNATTENDED_DOORMAN";
	private static final String WRONG_RESPONSE_ALERT_TYPE = "WRONG RESPONSE";
	private static final String NEXT_STOP_ALERT_TYPE = "NEXT_STOP";
	private static final String OPTIN_CONFIRMATION_SUCCESS_MESSAGE = "You are now enrolled and consent to receive FreshTexts Messages. 5msgs/order & more. Msg&Data rates may apply. Reply HELP for help, STOP to cancel.";
	private static final String OPTOUT_SUCCESS_MESSAG = "FreshTexts Messages: You are unsubscribed and will not receive additional FreshTexts messages. Reply HELP for help.";
	private static final String HELP_MESSAGE = "FreshTexts Messages: For more Help call 866-283-7374. Up to 5msgs/order. Msg&Data rates may apply. Reply STOP to cancel.";
	private static final String WRONG_RESPONSE_MESSAGE = "FreshTexts Messages: Sorry we didn't understand your response. Reply HELP for help, STOP to cancel.";
	private static final String ETA_MESSAGE_TEXT_1 = "Hello! Your FreshDirect order is getting a few finishing touches, and will be delivered today between ";
	private static final String ETA_MESSAGE_TEXT_2 = ". Reply HELP for help.";
	private static final String UATTENDED_OR_DOORMAN_DELIVERY_TEXT = "Special delivery! Your order was just left at your home at your preferred location. We hope you love it! Reply HELP for help. ";
	private static final String DLV_ATTEMPTED_TEXT = "We just missed you! A 1st delivery attempt was made to deliver your order. We'll do our best to redeliver your order later. Reply HELP for help.";

	/**
	 * This method sends the optin sms message to customer when he enrolls
	 * 
	 * @param mobileNumber
	 * @return
	 */
	public boolean smsOptIn(String customerId, String mobileNumber) {
		boolean isSent = false;

		LOGGER.debug("Starting the optin session Bean");
		
		try {		
			STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(mobileNumber, OPT_IN_MESSAGE);
	
			if (smsResponseModel != null) {
				Connection con = null;
				smsResponseModel.setDate(new Date());
				try {
					con = this.getConnection();
					this.updateSmsAlertCaptured(con, smsResponseModel, OPTIN_ALERT_TYPE, customerId);
	
				} catch (Exception e) {
					LOGGER.warn(e);
	
					throw new EJBException(e);
				} finally {
					try {
						if (con != null) {
							con.close();
							con = null;
						}
					} catch (SQLException se) {
						LOGGER.warn("Exception while trying to cleanup", se);
					}
				}
				if (smsResponseModel.getStatus() != null && smsResponseModel.getStatus().equalsIgnoreCase("SUCCESS")) {
					isSent = true;
				}
			}
		} catch (SmsServiceException e){
			LOGGER.info("smsOptIn failed with exception: " + e);
		}

		return isSent;
	}

	/**
	 * This method checks the customerInfo for pending sms Alerts which are
	 * expired ( based on configured time) and expires them.
	 * 
	 */
	public void expireOptin() {
		Date currentTime = new Date();
		Connection con = null;
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		ResultSet rs=null;
		try {
			con = this.getConnection();
			String getExpiredOptin = "select customer_id, mobile_number, ORDER_NOTIFICATION, ORDEREXCEPTION_NOTIFICATION, SMS_OFFERS_ALERT, PARTNERMESSAGE_NOTIFICATION "
					+ " from cust.customerinfo where ORDER_NOTIFICATION='P' or ORDEREXCEPTION_NOTIFICATION='P' or SMS_OFFERS_ALERT='P' or PARTNERMESSAGE_NOTIFICATION='P' and  1440*(to_date(?,'MM/DD/YYYY HH:MI:SS AM')-SMS_OPTIN_DATE) >?";
			ps = con.prepareStatement(getExpiredOptin);
			ps.setString(1, sdf.format(currentTime));
			ps.setInt(2, getExpiryTime());
			rs = ps.executeQuery();
			String updateCustomerInfo = "update cust.customerinfo set ORDER_NOTIFICATION=?, ORDEREXCEPTION_NOTIFICATION=?, SMS_OFFERS_ALERT=?, PARTNERMESSAGE_NOTIFICATION=? where customer_id=?";
			ps1 = con.prepareStatement(updateCustomerInfo);
			int batchCount = 0;
			while (rs.next()) {
				if (rs.getString("ORDER_NOTIFICATION") != null
						&& rs.getString("ORDER_NOTIFICATION").equals(
								EnumSMSAlertStatus.PENDING.value())) {
					ps1.setString(1, EnumSMSAlertStatus.NONE.value());
				} else {
					ps1.setString(1, rs.getString("ORDER_NOTIFICATION"));
				}
				if (rs.getString("ORDEREXCEPTION_NOTIFICATION") != null
						&& rs.getString("ORDEREXCEPTION_NOTIFICATION").equals(
								EnumSMSAlertStatus.PENDING.value())) {
					ps1.setString(2, EnumSMSAlertStatus.NONE.value());
				} else {
					ps1.setString(2, rs.getString("ORDEREXCEPTION_NOTIFICATION"));
				}
				if (rs.getString("SMS_OFFERS_ALERT") != null
						&& rs.getString("SMS_OFFERS_ALERT").equals(
								EnumSMSAlertStatus.PENDING.value())) {
					ps1.setString(3, EnumSMSAlertStatus.NONE.value());
				} else {
					ps1.setString(3, rs.getString("SMS_OFFERS_ALERT"));
				}
				if (rs.getString("PARTNERMESSAGE_NOTIFICATION") != null
						&& rs.getString("PARTNERMESSAGE_NOTIFICATION").equals(
								EnumSMSAlertStatus.PENDING.value())) {
					ps1.setString(4, EnumSMSAlertStatus.NONE.value());
				} else {
					ps1.setString(4, rs.getString("PARTNERMESSAGE_NOTIFICATION"));
				}
				ps1.setString(5, rs.getString("customer_id"));
				ps1.addBatch();
				batchCount++;

			}
			if (batchCount > 0) {
				ps1.executeBatch();
			}
			

		} catch (Exception e) {
			LOGGER.warn(e);

			throw new EJBException(e);
		} finally {
			try {
				if(rs!=null){
					rs.close();
				}
				if(ps1!=null){
					ps1.close();
				}
				if(ps!=null){
					ps.close();
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}

	}

	/**
	 * This Method will update the sms_recieved Table and if the response is YES
	 * from the customer this will update all the pending alerts to S. if the
	 * response from customer is NO all pending alerts will be removed (set to
	 * N) This method will also send the sms replies to the customer messages
	 * received.
	 * 
	 * @param mobileNumber
	 * @param shortCode
	 * @param carrierName
	 * @param receivedDate
	 * @param message
	 */
	public void updateSmsReceived(String mobileNumber, String shortCode,
			String carrierName, Date receivedDate, String message) {
		Connection con = null;
		try {
			con = this.getConnection();
			LOGGER.debug("MobileNumber = " + mobileNumber);
			PhoneNumber phone = new PhoneNumber(mobileNumber);
			String confirmed = isConfirmed(message);
			List<SmsCustInfo> customerInfoList = getCustomerInfoList(con, phone.getPhone());
			for (SmsCustInfo customerInfo : customerInfoList) {
				updateSmsMessagesReceived(phone.getPhone(), shortCode, carrierName, receivedDate, message, customerInfo, con, confirmed);
				if(!confirmed.equals("YES") && !confirmed.equals("STOP")){
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.warn(e);

			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}

			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}

	}
	
	private void updateSmsMessagesReceived(String mobileNumber, String shortCode,
			String carrierName, Date receivedDate, String message, SmsCustInfo customerInfo, Connection con, String confirmed) throws SQLException, SmsServiceException {
		
		PreparedStatement ps=null, ps1 = null;
		try{
		String customerId = customerInfo.getCustomerId();
		String updateSmsReceived = "insert into MIS.SMS_RECEIVED (SMS_ID, CUSTOMER_ID, MOBILE_NUMBER, SHORT_CODE, CARRIER_NAME, RECEIVED_DATE, MESSAGE)"
				+ " VALUES(?, ?, ?, ?, ?, ?, ?)";
		ps = con.prepareStatement(updateSmsReceived);
		ps.setInt(1, Integer.parseInt(SequenceGenerator.getNextIdFromSequence(con, "MIS.SMS_RECEIVED_SEQ")));
		ps.setString(2, customerId);
		ps.setString(3, mobileNumber);
		ps.setString(4, shortCode);
		ps.setString(5, carrierName);
		ps.setTimestamp(6, new java.sql.Timestamp(receivedDate.getTime()));
		ps.setString(7, message);

		ps.executeUpdate();
		
		
		LOGGER.debug("confirmed : " + confirmed);
		String notice = customerInfo.getSmsOrderNotice()!=null?customerInfo.getSmsOrderNotice():EnumSMSAlertStatus.NONE.value();
		String exceptions = customerInfo.getSmsOrderException()!=null?customerInfo.getSmsOrderException():EnumSMSAlertStatus.NONE.value();
		String offer = customerInfo.getSmsOffers()!=null?customerInfo.getSmsOffers():EnumSMSAlertStatus.NONE.value();
		String pMessage = customerInfo.getSmsPartnerMessage()!=null?customerInfo.getSmsPartnerMessage():EnumSMSAlertStatus.NONE.value();

		
		
		if (confirmed.equalsIgnoreCase("YES")) {
			String updateConfirmed = "update cust.customerinfo set ORDER_NOTIFICATION=?, ORDEREXCEPTION_NOTIFICATION=?, SMS_OFFERS_ALERT=?, PARTNERMESSAGE_NOTIFICATION=?, SMS_OPTIN_DATE=? where customer_id=? ";
			ps1 = con.prepareStatement(updateConfirmed);
			ps1.setString(1, notice.equals(EnumSMSAlertStatus.PENDING.value()) ? EnumSMSAlertStatus.SUBSCRIBED.value() : notice);
			ps1.setString(2, exceptions.equals(EnumSMSAlertStatus.PENDING.value()) ? EnumSMSAlertStatus.SUBSCRIBED.value(): exceptions);
			ps1.setString(3, offer.equals(EnumSMSAlertStatus.PENDING.value()) ? EnumSMSAlertStatus.SUBSCRIBED.value() : offer);
			ps1.setString(4, pMessage.equals(EnumSMSAlertStatus.PENDING.value()) ? EnumSMSAlertStatus.SUBSCRIBED.value() : pMessage);
			ps1.setTimestamp(5, new java.sql.Timestamp(receivedDate.getTime()));
			ps1.setString(6, customerId);
			ps1.executeUpdate();
			
			STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(mobileNumber, OPTIN_CONFIRMATION_SUCCESS_MESSAGE);
			smsResponseModel.setDate(new Date());
			
			updateSmsAlertCaptured(con, smsResponseModel, OPTIN_ALERT_TYPE, customerId);

		}  else if (confirmed.equalsIgnoreCase("STOP")) {
			String updateStop = "update cust.customerinfo set mobile_number=null, ORDER_NOTIFICATION=?, ORDEREXCEPTION_NOTIFICATION=?, SMS_OFFERS_ALERT=?, PARTNERMESSAGE_NOTIFICATION=?, SMS_OPTIN_DATE=? where customer_id=?";
			ps1 = con.prepareStatement(updateStop);
			ps1.setString(1, EnumSMSAlertStatus.NONE.value());
			ps1.setString(2, EnumSMSAlertStatus.NONE.value());
			ps1.setString(3, EnumSMSAlertStatus.NONE.value());
			ps1.setString(4, EnumSMSAlertStatus.NONE.value());
			ps1.setTimestamp(5, new java.sql.Timestamp(receivedDate.getTime()));
			ps1.setString(6, customerId);
			ps1.executeUpdate();
			
			STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(mobileNumber, OPTOUT_SUCCESS_MESSAG);
			smsResponseModel.setDate(new Date());
			updateSmsAlertCaptured(con, smsResponseModel, OPTOUT_ALERT_TYPE, customerId);
		} else if (confirmed.equalsIgnoreCase("HELP")) {
			STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(mobileNumber, HELP_MESSAGE);
			smsResponseModel.setDate(new Date());
			updateSmsAlertCaptured(con, smsResponseModel, HELP_ALERT_TYPE, customerId);
		} else {
			STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(mobileNumber, WRONG_RESPONSE_MESSAGE);
			smsResponseModel.setDate(new Date());
			updateSmsAlertCaptured(con, smsResponseModel, WRONG_RESPONSE_ALERT_TYPE, customerId);
		}
		} finally{
			if (ps1 != null){
				ps1.close();
			}
			if(ps!=null){
				ps.close();
			}
		}

		
		
	}

	/**
	 * This method populates the table dlv.next_stop_sms from the view
	 * dlv.transit and updates the successful imports in sms_transit_imports and
	 * starts sending sms messages to the next stops in the time window.
	 * 
	 */
	public void sendNextStopSms() {

		SmsDAO smsDAO = new SmsDAO();
		Connection con = null;

		try {
			con = this.getConnection();
			List<NextStopSmsInfo> nextStopSmsInfo = smsDAO.getNextStopSmsInfo(con);

			for (NextStopSmsInfo entry : nextStopSmsInfo) {
				String customerId=entry.getCustomerId();
				boolean isSubscribedForOrderNotices = isSubscribed(con, NEXT_STOP_ALERT_TYPE, customerId);
				if (isSubscribedForOrderNotices) {
					String dlvManagerName = getDlvManagerName(con, entry.getDlvManager()).toUpperCase();
					STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(entry.getMobileNumber(),
									NEXT_STOP_TEXT_1 + dlvManagerName + NEXT_STOP_TEXT_2);
					smsResponseModel.setDate(new Date());
					smsResponseModel.setOrderId(entry.getOrderId());

					updateSmsAlertCaptured(con, smsResponseModel, NEXT_STOP_ALERT_TYPE, customerId);
				}
			}
		} catch (Exception e) {
			LOGGER.warn(e);

			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}

	}

	/**
	 * This method sends the ETA sms to all the customers enrolled in
	 * order_notices every day.
	 */
	public void sendETASms() {

		Connection con = null;
		SmsDAO smsDAO = new SmsDAO();
		try {
			con = this.getConnection();
			List<SmsAlertETAInfo> etaInfoList = smsDAO.getETAInfo(con);
			for (int i = 0; i < etaInfoList.size(); i++) {

				boolean isSubscribed = isSubscribed(con, ETA_ALERT_TYPE,
						etaInfoList.get(i).getCustomerId());
				if (isSubscribed) {
					STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(etaInfoList.get(i)
									.getMobileNumber(), ETA_MESSAGE_TEXT_1
									+ getTime(etaInfoList.get(i).getEtaStartTime())+ " and "
									+ getTime(etaInfoList.get(i).getEtaEndTime())
									+ ETA_MESSAGE_TEXT_2);
					
					smsResponseModel.setDate(new Date());
					smsResponseModel.setOrderId(etaInfoList.get(i).getOrderId());
					updateSmsAlertCaptured(con, smsResponseModel, ETA_ALERT_TYPE, etaInfoList.get(i).getCustomerId());
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}

	}

	/**
	 * This method sends the Unattended or doorman delivery sms messages to the
	 * subscribed customers
	 * 
	 */
	public void sendUnattendedDoormanDlvSms() {

		SmsDAO smsDAO = new SmsDAO();
		Connection con = null;
		try {
			con = this.getConnection();
			List<OrderDlvInfo> unattendedDoormanDlvInfo = smsDAO
					.getUnattendedDoormanDlvInfo(con);
			for (OrderDlvInfo entry : unattendedDoormanDlvInfo) {

				boolean isSubscribed = isSubscribed(con, UNATTENDED_OR_DOORMAN_ALERT_TYPE, entry.getCustomerId());
				
				if (isSubscribed) {
					STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(entry.getMobileNumber(), UATTENDED_OR_DOORMAN_DELIVERY_TEXT);
					smsResponseModel.setDate(new Date());
					smsResponseModel.setOrderId(entry.getOrderId());
					updateSmsAlertCaptured(con, smsResponseModel, UNATTENDED_OR_DOORMAN_ALERT_TYPE, entry.getCustomerId());
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}

	}

	/**
	 * This method checks any delivery attempted SMS are to be sent and sends
	 * them to the respective customers.
	 * 
	 */
	public void sendDlvAttemptedSms() {
		SmsDAO smsDAO = new SmsDAO();
		Connection con = null;
		try {
			con = this.getConnection();
			List<OrderDlvInfo> dlvAttemptedInfo = smsDAO.getDlvAttemptedInfo(con);
			
			for (OrderDlvInfo entry : dlvAttemptedInfo) {
				if (isSubscribed(con, DLV_ATTEMPTED_ALERT_TYPE, entry.getCustomerId())) {
					STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(entry.getMobileNumber(), DLV_ATTEMPTED_TEXT);
					smsResponseModel.setDate(new Date());
					smsResponseModel.setOrderId(entry.getOrderId());
					updateSmsAlertCaptured(con, smsResponseModel, DLV_ATTEMPTED_ALERT_TYPE, entry.getCustomerId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}

	}

	/**
	 * updated the sms_alerts_capture with sms sent to the customer
	 * 
	 * @param smsResponseModel
	 * @throws SQLException
	 */
	private void updateSmsAlertCaptured(Connection con,
			STSmsResponse smsResponseModel, String alertType,
			String customerId) throws SQLException {
		LOGGER.debug("Started updateSMSAlertCaptuerd ");
		
		PhoneNumber phone = new PhoneNumber(String.valueOf(smsResponseModel.getSms_to()));
		PreparedStatement ps = null;
		try{
		ps = con
				.prepareStatement("INSERT INTO MIS.SMS_ALERT_CAPTURE "
						+ "(ID, USER_ID, ALERT_TYPE,CREATE_DATE,INSERT_DATE,STATUS,ERROR,ERROR_DESC,MOBILE_NUMBER,MESSAGE,STI_SMS_ID,ORDER_ID)"
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		
		
		ps.setInt(1, Integer.parseInt(SequenceGenerator.getNextIdFromSequence(
				con, "MIS.SMS_ALERT_SEQ")));
		
		ps.setString(2, customerId);
		ps.setString(3, alertType);
		ps.setTimestamp(4, new java.sql.Timestamp(smsResponseModel.getDate().getTime()));
		ps.setTimestamp(5, new java.sql.Timestamp(smsResponseModel.getDate().getTime()));
		ps.setString(6, smsResponseModel.getStatus());
		ps.setInt(7, smsResponseModel.getError());
		ps.setString(8, smsResponseModel.getError_description());
		ps.setString(9, phone.getPhone());
		ps.setString(10, smsResponseModel.getSms_msg());
		ps.setInt(11, smsResponseModel.getSms_id());
		ps.setString(12, smsResponseModel.getOrderId() != null ? smsResponseModel.getOrderId() : null);
		
		ps.executeUpdate();
		}catch(SQLException e){
			
		}finally{
			if(ps!=null){
				ps.close();
			}
		}

	}

	private List<SmsCustInfo> getCustomerInfoList(Connection con, String mobileNumber)
			throws SQLException {
		List<SmsCustInfo> customerInfoList = new ArrayList<SmsCustInfo>();
		PreparedStatement ps = null;
		ResultSet rs=null;
		try{
		ps = con
				.prepareStatement("select customer_id, ORDER_NOTIFICATION, ORDEREXCEPTION_NOTIFICATION, SMS_OFFERS_ALERT, PARTNERMESSAGE_NOTIFICATION from cust.customerinfo where mobile_number = ? ");
		ps.setString(1, mobileNumber);
		
		rs = ps.executeQuery();
		while (rs.next()) {
			SmsCustInfo smsCustInfo = new SmsCustInfo();
			smsCustInfo.setCustomerId(rs.getString("customer_id"));
			smsCustInfo.setSmsOrderNotice(rs.getString("ORDER_NOTIFICATION"));
			smsCustInfo.setSmsOrderException(rs.getString("ORDEREXCEPTION_NOTIFICATION"));
			smsCustInfo.setSmsOffers(rs.getString("SMS_OFFERS_ALERT"));
			smsCustInfo.setSmsPartnerMessage(rs.getString("PARTNERMESSAGE_NOTIFICATION"));
			customerInfoList.add(smsCustInfo);
		}
		}finally{
			close(rs,ps);
		}

		return customerInfoList;
	}

	private String isConfirmed(String text) {
		String confirmed = "OTHER";
		text = text.toUpperCase();
		if (text.contains("YES")) {
			confirmed = "YES";
		} else if (text.contains("NO")) {
			confirmed = "NO";
		} else if (text.contains("STOP")||text.contains("END")||text.contains("QUIT")||text.contains("UNSUBSCRIBE")||text.contains("CANCEL")) {
			confirmed = "STOP";
		} else if (text.contains("HELP")) {
			confirmed = "HELP";
		}
		return confirmed;
	}

	private String getDlvManagerName(Connection con, String employeeId)
			throws SQLException {
		String dlvManagerName = "";
		PreparedStatement ps=null; ResultSet rs  = null;
		try{
		String dlvnameQuery = "select FIRSTNM  from TRANSP.KRONOS_EMPLOYEE where personnum=?";
		ps = con.prepareStatement(dlvnameQuery);
		ps.setString(1, employeeId);
		rs = ps.executeQuery();
		if (rs.next()) {
			dlvManagerName = rs.getString("FIRSTNM");
		}
		}finally{
			close(rs,ps);
		}
		
		return dlvManagerName;
	}

	private boolean isSubscribed(Connection con, String alertType,
			String customerId) throws SQLException {
		
		
		boolean isSubscribed = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
		if (alertType.equals(NEXT_STOP_ALERT_TYPE)
				|| alertType.equals(ETA_ALERT_TYPE)) {
			
			ps = con.prepareStatement("select ORDER_NOTIFICATION from cust.customerinfo where customer_id = ? ");
			ps.setString(1, customerId);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (EnumSMSAlertStatus.SUBSCRIBED.value().equals(rs.getString("ORDER_NOTIFICATION"))) {
					isSubscribed = true;
				}
			}
		} else if(alertType.equals(DLV_ATTEMPTED_ALERT_TYPE)
				|| alertType.equals(UNATTENDED_OR_DOORMAN_ALERT_TYPE)){
			ps = con.prepareStatement("select ORDEREXCEPTION_NOTIFICATION from cust.customerinfo where customer_id = ? ");
			ps.setString(1, customerId);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (EnumSMSAlertStatus.SUBSCRIBED.value().equals(rs.getString("ORDEREXCEPTION_NOTIFICATION"))) {
					isSubscribed = true;
				}
			}
		}
		}finally{
			close(rs,ps);
		}
		return isSubscribed;
	}

	private int getExpiryTime() {
		int expiryTime = 60;
		Context ctx = null;
		try {
			ctx = getInitialContext();
			expiryTime = Integer.parseInt((String) ctx.lookup(DlvProperties.getSmsExpireInMins()));
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return expiryTime;
	}

	private Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL,DlvProperties.getProviderURL());
		return new InitialContext(h);
	}

	private String getTime(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		String hour="";
		String mins="";
		if(calendar.get(Calendar.HOUR) == 12 || calendar.get(Calendar.HOUR) == 0 || calendar.get(Calendar.HOUR) == 24){
			hour = "12";
		}else{
			hour = String.valueOf(calendar.get(Calendar.HOUR));
		}
		mins=String.valueOf(calendar.get(Calendar.MINUTE));
		return (hour+":"+mins + "  " + calendar.getDisplayName(
				Calendar.AM_PM, Calendar.SHORT, Locale.US));
	}

	private void close(ResultSet rs, PreparedStatement ps) throws SQLException{
		if(rs!=null){
			rs.close();
		}if(ps!=null){
			ps.close();
		}
	}
}
