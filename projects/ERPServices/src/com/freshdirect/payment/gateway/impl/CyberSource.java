package com.freshdirect.payment.gateway.impl;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaylinxException;
import com.freshdirect.payment.PaylinxResourceException;
import com.freshdirect.payment.PaylinxResponseModel;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.payment.ejb.CPMServerGateway;
import com.freshdirect.payment.ejb.PaymentHome;
import com.freshdirect.payment.fraud.ejb.RestrictedPaymentMethodSessionBean;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;

public class CyberSource implements Gateway{
	
	private static Category LOGGER = LoggerFactory.getInstance(PaymentManager.class);
	private static ServiceLocator serviceLocator = null;
	private static PaymentHome paymentHome = null;
	private static final String ECHECK_VERIFY_UNAVAIL_MSG="This feature is not available for E-Checks";

	@Override
	public GatewayType getType() {
		return GatewayType.CYBERSOURCE;
	}

	@Override
	public ErpAuthorizationModel verify(String merchantId,ErpPaymentMethodI paymentMethod) throws ErpTransactionException {
		
		try {
			
			if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
				throw new ErpTransactionException(ECHECK_VERIFY_UNAVAIL_MSG);
			} else { // default to credit card 
				PaylinxResponseModel paylinxResponse = null;
				
				paylinxResponse = CPMServerGateway.verifyCreditCard(paymentMethod);
				return paylinxResponse.getAuthorizationModel();
			}
		} catch (PaylinxResourceException pe) {
			LOGGER.debug(pe);
			throw new ErpTransactionException(pe);
		}
	}

	@Override
	public ErpAuthorizationModel authorize(
			ErpPaymentMethodI paymentMethod,
			String orderNumber,
			double authorizationAmount,
			double tax,
			String merchantId) throws ErpTransactionException {
			try {
				if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
					return authorizeECheck(paymentMethod, authorizationAmount, tax, merchantId);
				} else { // default ot credit card 
					PaylinxResponseModel paylinxResponse = null;
					if ((EnumCardType.DISC.equals(paymentMethod.getCardType()) || EnumCardType.AMEX.equals(paymentMethod.getCardType()))
						&& authorizationAmount < 1.0) {
						authorizationAmount = 1.0;
					}
					paylinxResponse = CPMServerGateway.authorizeCreditCard(paymentMethod, authorizationAmount, tax, orderNumber, merchantId);
					return paylinxResponse.getAuthorizationModel();
				}
			} catch (PaylinxResourceException pe) {
				LOGGER.debug(pe);
				throw new ErpTransactionException(pe);
			}

		}
	
	private ErpAuthorizationModel authorizeECheck(
			ErpPaymentMethodI paymentMethod,
			double authorizationAmount,
			double tax,
			String merchantId) {

			ErpAuthorizationModel auth = new ErpAuthorizationModel();
			auth.setPaymentMethodType(paymentMethod.getPaymentMethodType());
			if (merchantId != null && merchantId.length() > 0) {
				auth.setMerchantId(merchantId);
			} else {
				auth.setMerchantId(ErpServicesProperties.getCybersourceName());
			}
			auth.setTransactionSource(EnumTransactionSource.SYSTEM);
			auth.setAvs("Y");
			auth.setAmount(authorizationAmount);
			auth.setTax(tax);
			String accountNumber = paymentMethod.getAccountNumber();
			auth.setCcNumLast4(accountNumber.substring(accountNumber.length() - 4));
			auth.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
			auth.setBankAccountType(paymentMethod.getBankAccountType());
			auth.setCardType(paymentMethod.getCardType());

			RestrictedPaymentMethodSessionBean sb = new RestrictedPaymentMethodSessionBean();
			boolean isAuthorized = (sb.checkBadAccount(paymentMethod, false)) ? false : true;

			if (isAuthorized) {
				auth.setReturnCode(0);
				auth.setAuthCode("FD1000");
				auth.setResponseCode(EnumPaymentResponse.APPROVED);
				auth.setDescription("FD APPROVAL");
				auth.setSequenceNumber("FD AUTH");
			} else {
				auth.setReturnCode(-1);
				auth.setAuthCode("NA");
				auth.setResponseCode(EnumPaymentResponse.DECLINED);
				auth.setDescription("RESTRICTED PAYMENT METHOD");
				auth.setSequenceNumber("FD AUTH");
			}
			return auth;
		}


	@Override
	public ErpCaptureModel capture(ErpAuthorizationModel auth,
			ErpPaymentMethodI paymentMethod, double amount, double tax,
			String orderNumber) throws ErpTransactionException {
		ErpCaptureModel capture = null;
		try{
		if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
			capture = CPMServerGateway.captureECAuthorization(auth, paymentMethod, amount, 0.0, orderNumber);
		} else {
			capture = CPMServerGateway.captureCCAuthorization(auth, amount, 0.0);
		}
		}catch(PaylinxResourceException pre){
			throw new ErpTransactionException(pre);
		}
		catch(PaylinxException pe){
			throw new ErpTransactionException(pe);
		}
		return capture;
	
	}

	@Override
	public ErpVoidCaptureModel voidCapture(ErpPaymentMethodI paymentMethod, ErpCaptureModel capture) throws ErpTransactionException {
		ErpVoidCaptureModel voidCapture = null;
		try {
			if (EnumPaymentMethodType.ECHECK.equals(capture.getPaymentMethodType())) {
			voidCapture = CPMServerGateway.voidECCapture(capture);
			} else {
			voidCapture = CPMServerGateway.voidCCCapture(capture);  // default to credit card
			}
		}catch(PaylinxResourceException pre){
			throw new ErpTransactionException(pre);
		}
		return voidCapture;
	}

	@Override
	public Response reverseAuthorize(Request request) throws ErpTransactionException {
		throw new ErpTransactionException("Operation not supported");
	}

	@Override
	public ErpCashbackModel issueCashback(String orderNumber,
			ErpPaymentMethodI paymentMethod, double amount, double tax,
			ErpAffiliate affiliate) {
		ErpCashbackModel cashback = null;
		if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
			cashback = CPMServerGateway.returnECCashback(orderNumber, paymentMethod, amount, 0.0, affiliate.getMerchant(paymentMethod.getCardType()));
		} else { // default to credit card
			cashback = CPMServerGateway.returnCCCashback(orderNumber, paymentMethod, amount, 0.0, affiliate.getMerchant(paymentMethod.getCardType()));
		}
		cashback.setAffiliate(affiliate);
		return cashback;
	}

	@Override
	public Response addProfile(Request request) throws ErpTransactionException {
		throw new ErpTransactionException("Operation not supported");
	}

	@Override
	public Response updateProfile(Request request) throws ErpTransactionException {
		throw new ErpTransactionException("Operation not supported");
	}

	@Override
	public Response deleteProfile(Request request) throws ErpTransactionException {
		throw new ErpTransactionException("Operation not supported");
	}

	@Override
	public Response getProfile(Request request) throws ErpTransactionException {
		throw new ErpTransactionException("Operation not supported");
	}

	@Override
	public boolean isValidToken(String token, String customerId) {
		// TODO Auto-generated method stub
		return false;
	}
}