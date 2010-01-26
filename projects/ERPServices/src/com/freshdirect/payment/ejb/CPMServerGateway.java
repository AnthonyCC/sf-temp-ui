/*
 * PaylinxGatewaySessionBean.java
 *
 * Created on October 12, 2001, 4:01 PM
 */

package com.freshdirect.payment.ejb;

/**
 *
 * @author  knadeem
 * @version
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJBException;

import lcc.japi.LCC;
import lcc.japi.LCCTransaction;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAccountVerificationModel;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpReversalModel;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaylinxException;
import com.freshdirect.payment.PaylinxResourceException;
import com.freshdirect.payment.PaylinxResponseModel;

public class CPMServerGateway {
	
	private static final Category LOGGER = LoggerFactory.getInstance( CPMServerGateway.class );
	private static final SimpleDateFormat SF = new SimpleDateFormat("MMyy");
	private static final int SERVER_PORT;
	private static final String CPM_SERVER = ErpServicesProperties.getCybersourceIp();//"10.53.5.11"
	private static final int USE_SSL = 0; // 0 = NO, 1 = YES
	private static final String CHASE_MERCHANT_ID = ErpServicesProperties.getChaseMerchantId();
	private static final String MERCHANT_ID = ErpServicesProperties.getCybersourceName(); // "Chase" for test purposes only
	private static final String AVS_CHECK = ErpServicesProperties.getAvsCheck(); //to take into account the results of AVS CHECK
	
	//CyberSource Return codes
	private static final int ERR_SOCKET_WRITE 				= -15;
	private static final int ERR_SOCKET_CONN_REFUSED 		= -14;
	private static final int ERR_SOCKET_CONNECT				= -13;
	private static final int ERR_SOCKET_CREATE				= -12;
	private static final int ERR_SOCKET_LIB_INIT				= -11;
	
	private static final int ERR_COULDNOT_CONNECT_PROCESSOR 	= 135;
	private static final int ERR_TIMEOUT						= 136;
	
	static{
		int port = 0;
		try{
			port = Integer.parseInt(ErpServicesProperties.getCybersourcePort());
		}catch(NumberFormatException ne){
			port = 1530; // non encryption 1531 for encryption
		}
		SERVER_PORT = port; 
	}
	
	public static PaylinxResponseModel authorizeCreditCard(ErpPaymentMethodI paymentMethod, double amount, double tax, String saleId, String merchantId) throws PaylinxResourceException{
		PaylinxResponseModel response = new PaylinxResponseModel();
		
		if (paymentMethod == null || !EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {
			response.setResponseCode("FD101");
			response.setResponseMessage("Payment method is not a credit card");
		}
		
		LCCTransaction trans = createCCTransaction(paymentMethod, amount, tax);
		trans.SetValue(LCC.ID_ORDER_NUMBER, saleId);
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		
		if(merchantId == null || "".equals(merchantId)){
			merchantId = MERCHANT_ID;
		}
		trans.SetValue(LCC.ID_MERCHANT_ID, merchantId);
		
		ErpAuthorizationModel model = runCCAuthorizationTransaction(trans);

		// check to see if there's a response code we don't currently handle.
		if (model.getResponseCode() == null && model.getProcResponseCode() != null) {
			LOGGER.error("Paylinx.authorizeCreditCard: (NOT FATAL) - Process Response code not recognized " + model.getProcResponseCode() 
					+ " for sale id = " + saleId);
		}
		model.setAmount(amount);
		model.setTax(tax);
		model.setPaymentMethodType(paymentMethod.getPaymentMethodType());
		model.setCardType(paymentMethod.getCardType());
		String accountNumber = paymentMethod.getAccountNumber();
		model.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
		
		if(!"true".equalsIgnoreCase(AVS_CHECK)){
			model.setAvs("Y");
		}
		
		response.setAuthorizationModel(model);
		response.setResponseCode(String.valueOf(model.getReturnCode()));
		
		return response;		
	}
	
	private static LCCTransaction createCCTransaction(ErpPaymentMethodI paymentMethod, double amount, double tax){
			
		// Create an instance of a LCC Transaction
		LCCTransaction trans = new LCCTransaction();
		// Set the necessary fields for the transaction we intend to perform
		trans.SetValue(LCC.ID_E_COMMERCE_TYPE, "02");
		trans.SetValue(LCC.ID_ACCOUNT_NUMBER, paymentMethod.getAccountNumber());
		trans.SetValue(LCC.ID_CARD_TYPE, paymentMethod.getCardType().getPaylinxId());  // 001 VISA, 002 MC, 003 AMEX, 004 DISC 
		trans.SetValue(LCC.ID_EXPIRATION_DATE, SF.format(paymentMethod.getExpirationDate()));
		trans.SetValue(LCC.ID_AMOUNT, String.valueOf(Math.round(amount*100.0)));//to make 15.50 1550
		trans.SetValue(LCC.ID_TAX_AMOUNT, String.valueOf(Math.round(tax*100.0)));
		trans.SetValue(LCC.ID_CUSTOMER_NAME, StringUtils.left(paymentMethod.getName(), 25));
		trans.SetValue(LCC.ID_CUSTOMER_STREET, StringUtils.left(paymentMethod.getAddress1(), 20));//take first 20 characters of address as chase only accepts that
		trans.SetValue(LCC.ID_CUSTOMER_CITY, StringUtils.left(paymentMethod.getCity(), 20));
		trans.SetValue(LCC.ID_CUSTOMER_STATE, StringUtils.left(paymentMethod.getState(), 2));
		trans.SetValue(LCC.ID_CUSTOMER_ZIP, StringUtils.left(paymentMethod.getZipCode(), 5));
		
		return trans;
	}
	
	private static ErpAuthorizationModel runCCAuthorizationTransaction(LCCTransaction trans) throws PaylinxResourceException {
		ErpAuthorizationModel model = new ErpAuthorizationModel();
		model.setTransactionSource(EnumTransactionSource.SYSTEM);
		int rCode = trans.RunTransaction(LCC.ID_AUTHORIZATION);
		model.setReturnCode(rCode);
		
		LOGGER.debug("Authorization returned wiht rCode: "+rCode);
		
		if ( rCode != 0 ) {
			throw new PaylinxResourceException(getErrorMessage(rCode));
		} else {
			model.setAuthCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
			model.setProcResponseCode(trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE));
			
			EnumPaymentResponse response = EnumPaymentResponse.getEnum(model.getProcResponseCode());
			if (response == null) {
				LOGGER.warn("Unknown proc response code " + model.getProcResponseCode() + ", setting to CALL");
				response = EnumPaymentResponse.CALL;
			}

			model.setResponseCode(response);
			model.setDescription(trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
			model.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
			model.setMerchantId(trans.GetValue(LCC.ID_MERCHANT_ID));
			
			if(ErpServicesProperties.isAvsAddressMatchReqd())
			    model.setAvs(trans.GetValue(LCC.ID_ADDRESS_MATCH));
			else
			    model.setAvs(trans.GetValue(LCC.ID_ZIP_MATCH));
		}
		
		return model;
	}
	
	public static ErpAuthorizationModel manuallyAuthorizeCreditCard(ErpAuthorizationModel authorization, String approvalCode) {
		LCCTransaction trans = new LCCTransaction();
		
		trans.SetValue(LCC.ID_SEQUENCE_NUMBER, authorization.getSequenceNumber());
		trans.SetValue(LCC.ID_MERCHANT_ID, getCorrectMerchantId(authorization.getMerchantId()));
		trans.SetValue(LCC.ID_APPROVAL_CODE, approvalCode);
		try{
			ErpAuthorizationModel model =  runManualCCAuthorization(trans);
			model.setPaymentMethodType(authorization.getPaymentMethodType());
			model.setCardType(authorization.getCardType());
			model.setCcNumLast4(authorization.getCcNumLast4());
			model.setAmount(authorization.getAmount());
			model.setSequenceNumber(authorization.getSequenceNumber());
			return model;
		}catch(PaylinxException pe){
			throw new EJBException(pe);
		}
	}
	
	public static ErpAuthorizationModel runManualCCAuthorization(LCCTransaction trans) throws PaylinxException {
		ErpAuthorizationModel model = new ErpAuthorizationModel();
		model.setTransactionSource(EnumTransactionSource.CUSTOMER_REP);
		
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		int rCode = trans.RunTransaction(LCC.ID_MANUAL_AUTHORIZATION);
		model.setReturnCode(rCode);
		LOGGER.debug("\n\nManual Authorization Returned with:" + rCode);
		
		if (rCode != 0){
			throw new PaylinxException(trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
		}else{
			model.setAuthCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
			model.setResponseCode(EnumPaymentResponse.APPROVED);
			model.setDescription("Manual Auth");
			//model.setResponseCode(EnumPaymentResponse.getPaymentResponse(trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE)));
			//model.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
			//model.setDescription(trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
			model.setAvs(trans.GetValue(LCC.ID_ADDRESS_MATCH));
		}
		
		return model;
	}
	
	public static ErpCaptureModel captureCCAuthorization(ErpAuthorizationModel authorization, double actualAmount, double actualTax) throws PaylinxException {
		//!!! have to create an enumeration for all the paylinx return types and their values
		LCCTransaction trans = new LCCTransaction();//createTransaction();
		trans.SetValue(LCC.ID_SEQUENCE_NUMBER, authorization.getSequenceNumber());
		trans.SetValue(LCC.ID_MERCHANT_ID, getCorrectMerchantId(authorization.getMerchantId()));
		trans.SetValue(LCC.ID_AMOUNT, String.valueOf(Math.round(actualAmount*100.0)));//to make 15.50 1550
		trans.SetValue(LCC.ID_TAX_AMOUNT, String.valueOf(Math.round(actualTax*100.0)));
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		
		ErpCaptureModel model = runCaptureCCTransaction(trans);
		model.setTransactionSource(EnumTransactionSource.SYSTEM);
		model.setAmount(actualAmount);
		model.setTax(actualTax);
		model.setPaymentMethodType(authorization.getPaymentMethodType());
		model.setCcNumLast4(authorization.getCcNumLast4());
		model.setCardType(authorization.getCardType());
		model.setAffiliate(authorization.getAffiliate());
		return model;
	}
	
	private static ErpCaptureModel runCaptureCCTransaction(LCCTransaction trans) throws PaylinxException{
		ErpCaptureModel model = new ErpCaptureModel();
		int rCode = trans.RunTransaction(LCC.ID_CAPTURE);
		
		//model.setReturnCode(rCode);
		if (rCode != 0){
			throw new PaylinxException(trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
		}else{
			model.setAuthCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
			model.setResponseCode(trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE));
			model.setDescription(trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
			model.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
			model.setMerchantId(trans.GetValue(LCC.ID_MERCHANT_ID));
		}
		return model;
	}
	
	public static ErpReversalModel reverseCCAuthorization(ErpAuthorizationModel authorization, double amount) {
		LCCTransaction trans = new LCCTransaction();
		
		trans.SetValue(LCC.ID_SEQUENCE_NUMBER, authorization.getSequenceNumber());
		trans.SetValue(LCC.ID_AMOUNT, String.valueOf(Math.round(amount*100.0)));
		trans.SetValue(LCC.ID_MERCHANT_ID, MERCHANT_ID);
		//trans.SetValue(LCC.ID_APPROVAL_CODE, authorization.getApprovalCode());
		authorization.setPaymentMethodType(authorization.getPaymentMethodType());
		authorization.setCcNumLast4(authorization.getCcNumLast4());
		authorization.setCardType(authorization.getCardType());
		
		try{
			ErpReversalModel model = runCCReversalTransaction(trans);
			return model;
		}catch(PaylinxException pe){
			throw new EJBException(pe);
		}
	}
	
	private static ErpReversalModel runCCReversalTransaction(LCCTransaction trans) throws PaylinxException {
		ErpReversalModel model = new ErpReversalModel();
		model.setTransactionSource(EnumTransactionSource.CUSTOMER_REP);
		
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		int rCode = trans.RunTransaction(LCC.ID_REVERSAL);
		
		LOGGER.debug("ReversalTransaction return code:" + rCode);
		model.setReturnCode(rCode);
		
		if (rCode != 0) {
			throw new PaylinxException(trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
		}else {
			model.setReturnMessage(trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
			model.setApprovalCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
			model.setResponseCode(trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE));
			model.setResponseMessage(trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
			model.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
			model.setAddressMatch(trans.GetValue(LCC.ID_ADDRESS_MATCH));
			model.setZipMatch(trans.GetValue(LCC.ID_ZIP_MATCH));
			model.setProcessorResponseCode(trans.GetValue(LCC.ID_PROCESSOR_AUTH_RESPONSE_CODE));
			model.setProcessorAVSResult(trans.GetValue(LCC.ID_PROCESSOR_AVS_RESULT));
		}
		return model;
	}

	public static ErpVoidCaptureModel voidCCCapture(ErpCaptureModel capture) throws PaylinxResourceException {
		LCCTransaction trans = new LCCTransaction();
		trans.SetValue(LCC.ID_SEQUENCE_NUMBER, capture.getSequenceNumber());
		trans.SetValue(LCC.ID_MERCHANT_ID, getCorrectMerchantId(capture.getMerchantId()));
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		int rCode = trans.RunTransaction(LCC.ID_VOID_TRANSACTION);
		
		if(rCode != 0){
			throw new PaylinxResourceException("Exception while trying to void capture rCode: "+rCode);
		}
		
		ErpVoidCaptureModel voidCapture = new ErpVoidCaptureModel();
		voidCapture.setAmount(capture.getAmount());
		voidCapture.setTax(capture.getTax());
		voidCapture.setTransactionDate(new Date());
		voidCapture.setAuthCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
		voidCapture.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
		voidCapture.setMerchantId(trans.GetValue(LCC.ID_MERCHANT_ID));
		voidCapture.setPaymentMethodType(capture.getPaymentMethodType());
		voidCapture.setCcNumLast4(capture.getCcNumLast4());
		voidCapture.setCardType(capture.getCardType());
		return voidCapture;
	}
		
	public static ErpCashbackModel returnCCCashback(String saleId, ErpPaymentMethodI paymentMethod, double amount, double tax, String merchantId) {
		try{			
			LCCTransaction trans = createCCTransaction(paymentMethod, amount, tax);
			trans.SetValue(LCC.ID_ORDER_NUMBER, saleId);
			ErpCashbackModel model = runReturnCCTransaction(trans, merchantId);
			model.setAmount(amount);
			model.setTax(tax);
			model.setPaymentMethodType(paymentMethod.getPaymentMethodType());
			model.setCardType(paymentMethod.getCardType());
			String accountNumber = paymentMethod.getAccountNumber();
			model.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
			return model;
		}catch(PaylinxException pe){
			throw new EJBException(pe);
		}
				
	}
	
	private static ErpCashbackModel runReturnCCTransaction(LCCTransaction trans, String merchantId) throws PaylinxException {
		ErpCashbackModel model = new ErpCashbackModel();
		
		model.setTransactionSource(EnumTransactionSource.CUSTOMER_REP);
		trans.SetValue(LCC.ID_MERCHANT_ID, merchantId);
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		
		int rCode = trans.RunTransaction(LCC.ID_RETURN);
		
		LOGGER.debug("ReturnTransaction return code:" + rCode);
		model.setReturnCode(rCode);
		
		if (rCode != 0) {
			throw new PaylinxException(trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
		}else {
			model.setAuthCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
			model.setResponseCode(trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE));
			model.setDescription(trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
			model.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
			model.setAvs(trans.GetValue(LCC.ID_ADDRESS_MATCH));
		}
		return model;
	}
	
	/*
	 * Start of EChecks paylinx payment processing 
	 */

	public static ErpAccountVerificationModel verifyECAccount(ErpPaymentMethodI paymentMethod)  throws PaylinxResourceException {
		if (paymentMethod == null || !EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
			throw new EJBException("Payment method type is not eCheck");
		}
		LCCTransaction trans = createECTransaction(paymentMethod, 0.0, 0.0);
		ErpAccountVerificationModel model = runECAccountVerify(trans);
		model.setAmount(0.0);
		model.setTax(0.0);
		model.setPaymentMethodType(paymentMethod.getPaymentMethodType());
		model.setCardType(paymentMethod.getCardType());
		String accountNumber = paymentMethod.getAccountNumber();
		model.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
		model.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
		model.setBankAccountType(paymentMethod.getBankAccountType());
		return model;				
	}
	
	private static ErpAccountVerificationModel runECAccountVerify(LCCTransaction trans) throws PaylinxResourceException {
		ErpAccountVerificationModel model = new ErpAccountVerificationModel();
		model.setTransactionSource(EnumTransactionSource.SYSTEM);
		trans.SetValue(LCC.ID_MERCHANT_ID, MERCHANT_ID);
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		int rCode = trans.RunTransaction(LCC.ID_ACH_VERIFY);
		
		LOGGER.debug("EC Account Verification returned with rCode: "+rCode);
		
		if ( rCode != 0 ) {
			throw new PaylinxResourceException(getErrorMessage(rCode));
		} else {
			model.setVerificationResult(trans.GetValue(LCC.ID_VERIFICATION_RESULT));
			model.setAuthCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
			model.setDescription(trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
			model.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
			model.setMerchantId(trans.GetValue(LCC.ID_MERCHANT_ID));
		}
		
		return model;
	}

	public static ErpCaptureModel captureECAuthorization(ErpAuthorizationModel authorization, ErpPaymentMethodI paymentMethod, double amount, double tax, String saleId) throws PaylinxResourceException{
		
		if (paymentMethod == null || !EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
			throw new PaylinxResourceException("Payment method type is not eCheck");
		}
		
		LCCTransaction trans = createECCaptureTransaction(paymentMethod, amount, tax);
		trans.SetValue(LCC.ID_ORDER_NUMBER, saleId);
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		
		String merchantId = authorization.getMerchantId();
		if(merchantId == null || "".equals(merchantId)){
			merchantId = MERCHANT_ID;
		}
		trans.SetValue(LCC.ID_MERCHANT_ID, merchantId);
		
		ErpCaptureModel model = runECCaptureTransaction(trans);
		model.setAmount(amount);
		model.setTax(tax);
		model.setPaymentMethodType(authorization.getPaymentMethodType());
		model.setCcNumLast4(authorization.getCcNumLast4());
		model.setAbaRouteNumber(authorization.getAbaRouteNumber());
		model.setBankAccountType(authorization.getBankAccountType());
		model.setIsChargePayment(authorization.getIsChargePayment());
		model.setAffiliate(authorization.getAffiliate());
		return model;
		
	}
	
	private static LCCTransaction createECCaptureTransaction(ErpPaymentMethodI paymentMethod, double amount, double tax){
			
		// Create an instance of a LCC Transaction
		LCCTransaction trans = new LCCTransaction();
		// Set the necessary fields for the transaction we intend to perform
		trans.SetValue(LCC.ID_E_COMMERCE_TYPE, "02");
		trans.SetValue(LCC.ID_ACCOUNT_TYPE, paymentMethod.getBankAccountType().getName());
		trans.SetValue(LCC.ID_BANK_ACCOUNT_NUMBER, paymentMethod.getAccountNumber());
		trans.SetValue(LCC.ID_BANK_ID, paymentMethod.getAbaRouteNumber());
		trans.SetValue(LCC.ID_AMOUNT, String.valueOf(Math.round(amount*100.0)));//to make 15.50 1550
//		trans.SetValue(LCC.ID_TAX_AMOUNT, String.valueOf(Math.round(tax*100.0)));
		trans.SetValue(LCC.ID_CUSTOMER_NAME, StringUtils.left(paymentMethod.getName(), 25));
//		trans.SetValue(LCC.ID_CUSTOMER_STREET, StringUtils.left(paymentMethod.getAddress1(), 20));//take first 20 characters of address as chase only accepts that
//		trans.SetValue(LCC.ID_CUSTOMER_CITY, StringUtils.left(paymentMethod.getCity(), 20));
//		trans.SetValue(LCC.ID_CUSTOMER_STATE, StringUtils.left(paymentMethod.getState(), 2));
//		trans.SetValue(LCC.ID_CUSTOMER_ZIP, StringUtils.left(paymentMethod.getZipCode(), 5));
		return trans;
	}

	private static ErpCaptureModel runECCaptureTransaction(LCCTransaction trans) throws PaylinxResourceException {
		ErpCaptureModel model = new ErpCaptureModel();
		model.setTransactionSource(EnumTransactionSource.SYSTEM);
		int rCode = trans.RunTransaction(LCC.ID_ACH_DEPOSIT);
		
		LOGGER.debug("Authorization returned with rCode: "+rCode);
		
		if ( rCode != 0 ) {
			throw new PaylinxResourceException(getErrorMessage(rCode));
		} else {
			model.setAuthCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
			model.setDescription(trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
			model.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
			model.setMerchantId(trans.GetValue(LCC.ID_MERCHANT_ID));
		}
		
		return model;
	}
	
	public static ErpCashbackModel returnECCashback(String saleId, ErpPaymentMethodI paymentMethod, double amount, double tax, String merchantId) {
		try{			
			if (paymentMethod == null || !EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
				throw new EJBException("Payment method type is not eCheck");
			}
			LCCTransaction trans = createECTransaction(paymentMethod, amount, tax);
			trans.SetValue(LCC.ID_ORDER_NUMBER, saleId);
			ErpCashbackModel model = runReturnECTransaction(trans, merchantId);
			model.setAmount(amount);
			model.setTax(tax);
			model.setPaymentMethodType(paymentMethod.getPaymentMethodType());
			model.setCardType(paymentMethod.getCardType());
			String accountNumber = paymentMethod.getAccountNumber();
			model.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
			model.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
			model.setBankAccountType(paymentMethod.getBankAccountType());
			return model;
		}catch(PaylinxException pe){
			throw new EJBException(pe);
		}
				
	}
	
	private static LCCTransaction createECTransaction(ErpPaymentMethodI paymentMethod, double amount, double tax){
		
		// Create an instance of a LCC Transaction
		LCCTransaction trans = new LCCTransaction();
		// Set the necessary fields for the transaction we intend to perform
		trans.SetValue(LCC.ID_E_COMMERCE_TYPE, "02");
		trans.SetValue(LCC.ID_ACCOUNT_TYPE, paymentMethod.getBankAccountType().getName());
		trans.SetValue(LCC.ID_BANK_ACCOUNT_NUMBER, paymentMethod.getAccountNumber());
		trans.SetValue(LCC.ID_BANK_ID, paymentMethod.getAbaRouteNumber());
		trans.SetValue(LCC.ID_CUSTOMER_NAME, StringUtils.left(paymentMethod.getName(), 25));
		trans.SetValue(LCC.ID_AMOUNT, String.valueOf(Math.round(amount*100.0)));//to make 15.50 1550
		trans.SetValue(LCC.ID_TAX_AMOUNT, String.valueOf(Math.round(tax*100.0)));
		
		return trans;
	}

	private static ErpCashbackModel runReturnECTransaction(LCCTransaction trans, String merchantId) throws PaylinxException {
		ErpCashbackModel model = new ErpCashbackModel();
		
		model.setTransactionSource(EnumTransactionSource.CUSTOMER_REP);
		trans.SetValue(LCC.ID_MERCHANT_ID, merchantId);
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		
		int rCode = trans.RunTransaction(LCC.ID_ACH_REFUND);
		
		LOGGER.debug("ReturnTransaction return code:" + rCode);
		model.setReturnCode(rCode);
		
		if (rCode != 0) {
			throw new PaylinxException(trans.GetValue(LCC.ID_RETURN_CODE_MESSAGE));
		}else {
			model.setAuthCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
			model.setResponseCode(trans.GetValue(LCC.ID_AUTH_RESPONSE_CODE));
			model.setDescription(trans.GetValue(LCC.ID_AUTH_RESPONSE_MESSAGE));
			model.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
		}
		return model;
	}
	
	public static ErpVoidCaptureModel voidECCapture(ErpCaptureModel capture) throws PaylinxResourceException {

		LCCTransaction trans = new LCCTransaction();
		trans.SetValue(LCC.ID_SEQUENCE_NUMBER, capture.getSequenceNumber());
		trans.SetValue(LCC.ID_MERCHANT_ID, getCorrectMerchantId(capture.getMerchantId()));
		trans.SetConnectionInformation(CPM_SERVER, SERVER_PORT, USE_SSL);
		int rCode = trans.RunTransaction(LCC.ID_ACH_VOID);
		
		if(rCode != 0){
			throw new PaylinxResourceException("Exception while trying to void capture rCode: "+rCode);
		}
		
		ErpVoidCaptureModel voidCapture = new ErpVoidCaptureModel();
		voidCapture.setAmount(capture.getAmount());
		voidCapture.setTax(capture.getTax());
		voidCapture.setTransactionDate(new Date());
		voidCapture.setAuthCode(trans.GetValue(LCC.ID_APPROVAL_CODE));
		voidCapture.setSequenceNumber(trans.GetValue(LCC.ID_SEQUENCE_NUMBER));
		voidCapture.setMerchantId(trans.GetValue(LCC.ID_MERCHANT_ID));
		voidCapture.setPaymentMethodType(capture.getPaymentMethodType());
		voidCapture.setCardType(capture.getCardType());
		voidCapture.setCcNumLast4(capture.getCcNumLast4());
		voidCapture.setAbaRouteNumber(capture.getAbaRouteNumber());
		voidCapture.setBankAccountType(capture.getBankAccountType());
		voidCapture.setIsChargePayment(capture.getIsChargePayment());			
		
		return voidCapture;
	}
	
	private static String getCorrectMerchantId(String merchantId) {
		return "".equals(NVL.apply(merchantId, "").trim()) ? CHASE_MERCHANT_ID : merchantId;
	}
	
	private static String getErrorMessage(int rCode){
		
		switch(rCode){
			case ERR_SOCKET_CONN_REFUSED:
			case ERR_SOCKET_CONNECT:
			case ERR_SOCKET_CREATE:
			case ERR_SOCKET_LIB_INIT:
			case ERR_SOCKET_WRITE:
					return "Cannot connect to cybersource server";


			case  ERR_COULDNOT_CONNECT_PROCESSOR:
			case  ERR_TIMEOUT:
					return "Cannot connect to Payment Processor";
					
			default:
				return "Paylinx error "+rCode;
		}
		
	}
}
