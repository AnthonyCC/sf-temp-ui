package com.freshdirect.delivery.sms;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.backoffice.service.BackOfficeClientService;
import com.freshdirect.backoffice.service.IBackOfficeClientService;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.ecommerce.data.delivery.sms.RecievedSmsData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
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
	
	private static final String CANCEL="CANCEL";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
	
	
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
				LOGGER.debug("calling FDECommerceService.smsOptIn()");
				isSent =  FDECommerceService.getInstance().smsOptIn(customerId, mobileNumber, eStoreId);
			
		
		return isSent;
	}
	public boolean smsOptInNonMarketing(String customerId,String mobileNumber, String eStoreId) throws FDResourceException{
		boolean isSent=false;
		LOGGER.debug("calling FDECommerceService.smsOptInNonMarketing()");
		isSent = FDECommerceService.getInstance().smsOptInNonMarketing(customerId, mobileNumber, eStoreId);
		
		return isSent;
	}
	public boolean smsOptInMarketing(String customerId,String mobileNumber, String eStoreId) throws FDResourceException{
		boolean isSent=false;
		
				LOGGER.debug("calling FDECommerceService.smsOptInMarketing()");
				isSent =  FDECommerceService.getInstance().smsOptInMarketing(customerId, mobileNumber, eStoreId);
			
			
		
		
		return isSent;
	}
	
	
	public boolean smsOrderConfirmation(String customerId, String mobileNumber, String orderId, String eStore) throws FDResourceException{
		boolean isSent=false;
			LOGGER.debug("calling FDECommerceService.smsOrderConfirmation()");
				isSent = FDECommerceService.getInstance().smsOrderConfirmation(customerId, mobileNumber, orderId, eStore);
			
		return isSent;
	}
	
	public boolean smsOrderModification(String customerId, String mobileNumber, String orderId, String eStore) throws FDResourceException{
		boolean isSent=false;
			LOGGER.debug("calling FDECommerceService.smsOrderModification()");
				isSent = FDECommerceService.getInstance().smsOrderModification(customerId, mobileNumber, orderId, eStore);
			
		return isSent;
	}
	
	public boolean smsOrderCancel(String customerId, String mobileNumber, String orderId, String eStore) throws FDResourceException{
		boolean isSent=false;
			LOGGER.debug("calling FDECommerceService.smsOrderCancel()");
				isSent = FDECommerceService.getInstance().smsOrderCancel(customerId, mobileNumber, orderId, eStore);
			
		return isSent;
	}
	
	
	
	
	
	
	public void captureMessageRelayed(String mobileNumber, String shortCode, String carrierName, String receivedDate, String message, EnumEStoreId eStoreId) throws FDResourceException{
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sdf.parse(receivedDate);
			String formattedMobileNumber=formatMobileNumber(mobileNumber);
			if (CANCEL.equalsIgnoreCase(message)) {
				 boolean isCaseCreated=false;
				try {
					LOGGER.info("Start:::::SMS response is sending to Backoffice. MobileNumber:"+mobileNumber);									
					IBackOfficeClientService service = BackOfficeClientService.getInstance();
					  isCaseCreated=service.createCaseByRecievedSmsData(populateRecievedSmsData(mobileNumber, carrierName, message,formateDate(receivedDate)));
					}
					catch (Exception e) {
						LOGGER.info("While sending SMS response to Backoffice got an exception for the MobileNumber:"+mobileNumber +e);
						}
							FDECommerceService.getInstance().updateSmsReceived(
									formattedMobileNumber, shortCode, carrierName,
									date, message, eStoreId,isCaseCreated);
						
					
			} else {
					FDECommerceService.getInstance().updateSmsReceived(
							formattedMobileNumber, shortCode, carrierName,
							date, message, eStoreId,false);
				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	}

	public Date formateDate(String receivedDate) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
		return formatter.parse(receivedDate);
	}

	public static RecievedSmsData populateRecievedSmsData(String mobileNumber,
			String carrierName, String message, Date receivedDate) {
		RecievedSmsData recievedSmsData=new RecievedSmsData();
		recievedSmsData.setMobileNumber(mobileNumber);
		recievedSmsData.setMessage(message);
		recievedSmsData.setCarrierName(carrierName);
		recievedSmsData.setReceivedDate(receivedDate);
		recievedSmsData.seteStoreId(EnumEStoreId.FD.getContentId());
		return recievedSmsData;
	}	
	private String formatMobileNumber(String mobileNumber){
		
		if(StringUtils.isNotEmpty(mobileNumber) && mobileNumber.length() > 10){
			return String.valueOf(mobileNumber.toCharArray(), mobileNumber.length()-10, 10);
		}
		return mobileNumber;
	}
	

}
