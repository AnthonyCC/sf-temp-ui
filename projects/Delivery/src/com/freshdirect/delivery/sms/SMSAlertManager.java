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
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

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
	public boolean smsOptIn(String customerId,String mobileNumber, String eStoreId) throws FDResourceException{
		boolean isSent=false;
		// we need to format the mobile number to fit the request
		PhoneNumber phone = new PhoneNumber(mobileNumber);
		
		
		
		//Call ejb to persist the information in the DB (After test)
		try{
			lookupSmsAlertsHome();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("sms.ejb.SmsAlertsSB")){
				LOGGER.debug("calling FDECommerceService.smsOptIn()");
				isSent =  FDECommerceService.getInstance().smsOptIn(customerId, mobileNumber, eStoreId);
			}
			else{
				SmsAlertsSB smsAlertSB = smsAlertsHome.create();
				LOGGER.debug("calling smsAlertSB.smsOptIn()");
				isSent = smsAlertSB.smsOptIn(customerId,mobileNumber, eStoreId);
			}
		}catch (NamingException e) {
			throw new FDResourceException(e);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}
		
		
		return isSent;
	}
	public boolean smsOptInNonMarketing(String customerId,String mobileNumber, String eStoreId) throws FDResourceException{
		boolean isSent=false;
		PhoneNumber phone = new PhoneNumber(mobileNumber);
		try{
			lookupSmsAlertsHome();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("sms.ejb.SmsAlertsSB")){
				LOGGER.debug("calling FDECommerceService.smsOptInNonMarketing()");
				isSent =  FDECommerceService.getInstance().smsOptInNonMarketing(customerId, mobileNumber, eStoreId);
			}
			else{
				SmsAlertsSB smsAlertSB = smsAlertsHome.create();
				LOGGER.debug("calling smsAlertSB.smsOptInNonMarketing()");
				isSent = smsAlertSB.smsOptInNonMarketing(customerId,mobileNumber, eStoreId);
			}
		}catch (NamingException e) {
			throw new FDResourceException(e);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}
		
		
		return isSent;
	}
	public boolean smsOptInMarketing(String customerId,String mobileNumber, String eStoreId) throws FDResourceException{
		boolean isSent=false;
		PhoneNumber phone = new PhoneNumber(mobileNumber);
		try{
			lookupSmsAlertsHome();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("sms.ejb.SmsAlertsSB")){
				LOGGER.debug("calling FDECommerceService.smsOptInMarketing()");
				isSent =  FDECommerceService.getInstance().smsOptInMarketing(customerId, mobileNumber, eStoreId);
			}
			else{
				SmsAlertsSB smsAlertSB = smsAlertsHome.create();
				LOGGER.debug("calling smsAlertSB.smsOptInMarketing()");
				isSent = smsAlertSB.smsOptInMarketing(customerId,mobileNumber, eStoreId);
			}
		}catch (NamingException e) {
			throw new FDResourceException(e);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}
		
		
		return isSent;
	}
	
	
	public boolean smsOrderConfirmation(String customerId, String mobileNumber, String orderId, String eStore) throws FDResourceException{
		boolean isSent=false;
		PhoneNumber phone = new PhoneNumber(mobileNumber);
		try{
			lookupSmsAlertsHome();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("sms.ejb.SmsAlertsSB")){
				LOGGER.debug("calling FDECommerceService.smsOrderConfirmation()");
				isSent = FDECommerceService.getInstance().smsOrderConfirmation(customerId, mobileNumber, orderId, eStore);
			}
			else{
				SmsAlertsSB smsAlertSB = smsAlertsHome.create();
				LOGGER.debug("calling smsAlertSB.smsOrderConfirmation()");
				isSent = smsAlertSB.smsOrderConfirmation(customerId, mobileNumber, orderId, eStore);
			}
		}catch (NamingException e) {
			throw new FDResourceException(e);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}
		return isSent;
	}
	
	public boolean smsOrderModification(String customerId, String mobileNumber, String orderId, String eStore) throws FDResourceException{
		boolean isSent=false;
		PhoneNumber phone = new PhoneNumber(mobileNumber);
		try{
			lookupSmsAlertsHome();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("sms.ejb.SmsAlertsSB")){
				LOGGER.debug("calling FDECommerceService.smsOrderModification()");
				isSent = FDECommerceService.getInstance().smsOrderModification(customerId, mobileNumber, orderId, eStore);
			}
			else{
				SmsAlertsSB smsAlertSB = smsAlertsHome.create();
				LOGGER.debug("calling smsAlertSB.smsOrderModification()");
				isSent = smsAlertSB.smsOrderModification(customerId, mobileNumber, orderId, eStore);
			}
		}catch (NamingException e) {
			throw new FDResourceException(e);
		} catch (RemoteException e) {
			throw new FDResourceException(e);
		} catch (CreateException e) {
			throw new FDResourceException(e);
		}
		return isSent;
	}
	
	public boolean smsOrderCancel(String customerId, String mobileNumber, String orderId, String eStore) throws FDResourceException{
		boolean isSent=false;
		try{
			lookupSmsAlertsHome();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("sms.ejb.SmsAlertsSB")){
				LOGGER.debug("calling FDECommerceService.smsOrderCancel()");
				isSent = FDECommerceService.getInstance().smsOrderCancel(customerId, mobileNumber, orderId, eStore);
			}
			else{
				SmsAlertsSB smsAlertSB = smsAlertsHome.create();
				LOGGER.debug("calling smsAlertSB.smsOrderCancel()");
				isSent = smsAlertSB.smsOrderCancel(customerId, mobileNumber, orderId, eStore );
			}
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
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("sms.ejb.SmsAlertsSB")){
				FDECommerceService.getInstance().updateSmsReceived(formattedMobileNumber, shortCode, carrierName, date, message, eStoreId);
			}
			else{
				SmsAlertsSB smsAlertSB = smsAlertsHome.create();
				smsAlertSB.updateSmsReceived(formattedMobileNumber, shortCode, carrierName, date, message, eStoreId);
			}
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
