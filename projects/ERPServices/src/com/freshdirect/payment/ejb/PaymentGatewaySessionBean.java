/*
 * 
 * PaymentGatewaySessionBean.java
 * Date: Sep 23, 2002 Time: 12:09:07 PM
 */
package com.freshdirect.payment.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import javax.naming.*;
import javax.jms.*;

import com.freshdirect.payment.command.PaymentCommandI;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.ECheck;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.ejb.FDGatewayActivityLogModel;

import com.freshdirect.framework.core.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;



public class PaymentGatewaySessionBean extends GatewaySessionBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( PaymentGatewaySessionBean.class );
	//Time To Live as capture messages are critical 24 hrs
	public final static long TTL_CRITICAL = 24*60*60*1000; 
	
	private long timeout;
	
	public void updateSaleDlvStatus(PaymentCommandI command) {
		this.enqueue(command);
	}
	
	private ObjectMessage enqueue(PaymentCommandI capture)  {
		try {
			ObjectMessage captureMsg = this.qsession.createObjectMessage();
			captureMsg.setObject(capture);
			this.qsender.send(captureMsg, DeliveryMode.PERSISTENT, 0, TTL_CRITICAL);
			
			return captureMsg;
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing Capture Message",ex);
			throw new EJBException(ex);
		}
	}


	protected void initialize(Context ctx) throws NamingException, CreateException {
		super.initialize(ctx);
		Integer timeoutInt = (Integer) ctx.lookup("java:comp/env/Timeout");
		this.timeout = timeoutInt.intValue();
	}
	
	public void logActivity(GatewayType gatewayType,Response response) {
		FDGatewayActivityLogModel logModel= getLogModel(gatewayType,response);
		try {
			ObjectMessage logMsg = this.qsession.createObjectMessage();
			logMsg.setStringProperty("MessageType","LOG_GATEWAY_ACTIVITY" );
			logMsg.setObject(logModel);
			this.qsender.send(logMsg);
		} catch (Exception ex) {
			LOGGER.warn("Exception during sending message to queue.", ex);
		}
	}
	
	private FDGatewayActivityLogModel getLogModel(GatewayType gatewayType,Response response) {
		FDGatewayActivityLogModel logModel=new FDGatewayActivityLogModel();
		logModel.setTransactionType(response.getTransactionType());
		logModel.setGatewayType(gatewayType);
		logModel.setApproved(response.isApproved());
		logModel.setAuthCode(response.getAuthCode());
		logModel.setAVSMatch(response.isAVSMatch());
		logModel.setAvsResponse(response.getAVSResponse());
		logModel.setProcessingError(response.isError());
		logModel.setDeclined(response.isDeclined());
		logModel.setRequestProcessed(response.isRequestProcessed());
		logModel.setResponseCode(response.getResponseCode());
		logModel.setResponseCodeAlt(response.getResponseCodeAlt());
		logModel.setStatusCode(response.getStatusCode());
		logModel.setStatusMsg(response.getStatusMessage());
		
		if(TransactionType.CC_VERIFY.equals(response.getTransactionType())) {
			logModel.setCVVMatch(response.isCVVMatch());
			logModel.setCvvResponse(response.getCVVResponse());
		}
		BillingInfo billingInfo=response.getBillingInfo();
		PaymentMethod pm=null;
		if(billingInfo!=null) {
			pm=billingInfo.getPaymentMethod();
			logModel.setAmount(billingInfo.getAmount());
			if (billingInfo.getMerchant()!=null)
				logModel.setMerchant(billingInfo.getMerchant().name());
			logModel.setGatewayOrderID(billingInfo.getTransactionID());
			logModel.setTxRefNum(billingInfo.getTransactionRef());
			logModel.setTxRefIdx(billingInfo.getTransactionRefIndex());
			if(pm!=null) {
				int l=pm.getMaskedAccountNumber().length();
				logModel.setAccountNumLast4(l>4?
						pm.getMaskedAccountNumber().substring(l-4,l):pm.getMaskedAccountNumber());
				logModel.setCustomerId(pm.getCustomerID());
				logModel.setProfileId(pm.getBillingProfileID());
				logModel.setCustomerName(pm.getCustomerName());
				logModel.setAddressLine1(pm.getAddressLine1());
				logModel.setAddressLine2(pm.getAddressLine2());
				logModel.setCity(pm.getCity());
				logModel.setState(pm.getState());
				logModel.setCountryCode(pm.getCountry());
				logModel.setZipCode(pm.getZipCode());
				logModel.setPaymentType(pm.getType());
				if(PaymentMethodType.CREDIT_CARD.equals(pm.getType())) {
				   CreditCard cc=(CreditCard)pm;
					logModel.setCardType(cc.getCreditCardType());
					logModel.setExpirationDate(cc.getExpirationDate());
				} else if(PaymentMethodType.ECHECK.equals(pm.getType())){
					ECheck ec=(ECheck)pm;
					logModel.setBankAccountType(ec.getBankAccountType());
				}
			}
			
		}
		return logModel;
		
	}

}

	