/*
 * 
 * GatewayActivityLogSessionBean.java
 */
package com.freshdirect.payment.ejb;

/**
 * 
 * @author tbalumuri
 */
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.ECheck;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.ejb.FDGatewayActivityLogModel;



public class GatewayActivityLogSessionBean extends GatewaySessionBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( GatewayActivityLogSessionBean.class );
	
	public void logActivity(GatewayType gatewayType,Response response) {
		
		try {
			FDGatewayActivityLogModel logModel= getLogModel(gatewayType,response);
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
		if(billingInfo==null)
			billingInfo=response.getRequest().getBillingInfo();
		PaymentMethod pm=null;
		if(billingInfo!=null) {
			pm=billingInfo.getPaymentMethod();
			logModel.setAmount(billingInfo.getAmount());
			if (billingInfo.getMerchant()!=null)
				logModel.setMerchant(billingInfo.getMerchant().name());
			logModel.setGatewayOrderID(billingInfo.getTransactionID());
			logModel.setTxRefNum(billingInfo.getTransactionRef());
			logModel.setTxRefIdx(billingInfo.getTransactionRefIndex());
			if(StringUtil.isEmpty(pm.getMaskedAccountNumber())){
				pm=response.getRequest().getBillingInfo().getPaymentMethod();
			}
			if(pm!=null && !StringUtil.isEmpty(pm.getMaskedAccountNumber())) {
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
				}else if (PaymentMethodType.PP.equals(pm.getType())) {
					logModel.setCardType(CreditCardType.PYPL);
				}
				else if(PaymentMethodType.ECHECK.equals(pm.getType())){
					ECheck ec=(ECheck)pm;
					logModel.setBankAccountType(ec.getBankAccountType());
				}
				
				if(pm.getEwalletId() != null && pm.getEwalletId().equals(""+EnumEwalletType.PP.getValue())){
					logModel.setDeviceId(pm.getDeviceId());
				}
				// Update EWallet ID
				logModel.seteWalletId(response.getEwalletId());
				logModel.seteWalletTxId(response.getEwalletTxId());
				logModel.setEStoreId(billingInfo.getEStoreId());
			}
			
		}
		return logModel;
		
	}

}
