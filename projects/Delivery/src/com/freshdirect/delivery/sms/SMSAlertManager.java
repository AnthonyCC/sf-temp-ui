package com.freshdirect.delivery.sms;


import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.sms.ejb.SmsAlertsHome;
import com.freshdirect.delivery.sms.ejb.SmsAlertsSB;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * This class will handle all the communication with Single Touch API
 * 
 * Single Touch API being called is : sendSMS
 * 
 * Authentication via Digest two way protocol
 * 
 * @author vmohanchalla
 *
 */
public class SMSAlertManager {
	/*
	 * Logger instance 
	 */
	private static final Category LOGGER = LoggerFactory.getInstance(SMSAlertManager.class);
	
	
	
	
	private SmsAlertsHome smsAlertsHome=null;
	
	private static SMSAlertManager instance = new SMSAlertManager();
	
	
	
	public static SMSAlertManager getInstance() {
		return instance;
	}

	/**
	 * This method check sends in the opt-in alerts to the  given mobile number
	 * @param alerts
	 * @throws FDResourceException 
	 */
	public boolean smsOptIn(String customerId,String mobileNumber) throws FDResourceException{
		boolean isSent=false;
		// we need to format the mobile number to fit the request
		PhoneNumber phone = new PhoneNumber(mobileNumber);
		
		
		
		//Call ejb to persist the information in the DB (After test)
		try{
			lookupSmsAlertsHome();
			SmsAlertsSB smsAlertSB = smsAlertsHome.create();
			LOGGER.debug("calling smsAlertSB.smsOptIn()");
			isSent = smsAlertSB.smsOptIn(customerId,mobileNumber);
		}catch (NamingException e) {
			throw new FDResourceException(e);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}
		
		
		return isSent;
	}
	
	public void captureMessageRelayed(String mobileNumber, String shortCode, String carrierName, String receivedDate, String message, EnumEStoreId eStoreId) throws FDResourceException{
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sdf.parse(receivedDate);
			String formattedMobileNumber=formatMobileNumber(mobileNumber);
			lookupSmsAlertsHome();
			SmsAlertsSB smsAlertSB = smsAlertsHome.create();
			smsAlertSB.updateSmsReceived(formattedMobileNumber, shortCode, carrierName, date, message, eStoreId);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			throw new FDResourceException(e);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}
	}	
	private String formatMobileNumber(String mobileNumber){
		
		if(StringUtils.isNotEmpty(mobileNumber) && mobileNumber.length() > 10){
			return String.valueOf(mobileNumber.toCharArray(), mobileNumber.length()-10, 10);
		}
		return mobileNumber;
	}
	
	protected void lookupSmsAlertsHome() throws NamingException {
		if(smsAlertsHome != null)
			return;
		Context ctx = null;
		try {
			ctx = DlvProperties.getInitialContext();
			this.smsAlertsHome = (SmsAlertsHome) ctx.lookup( DlvProperties.getSmsAlertsHome() );
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {}
		}
	}

}
