package com.freshdirect.payment;

import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpECheckModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.CPMServerGateway;
import com.freshdirect.payment.ejb.PaymentHome;
import com.freshdirect.payment.ejb.PaymentSB;
import com.freshdirect.payment.fraud.ejb.RestrictedPaymentMethodSessionBean;
public class PaymentManager {

	private static Category LOGGER = LoggerFactory.getInstance(PaymentManager.class);
	private static ServiceLocator serviceLocator = null;
	private static PaymentHome paymentHome = null;

	static {
		try {
			serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
		} catch (NamingException e) {
			LOGGER.error("PaymentManager.serviceLocator initialization exception: " + e.getMessage());
		}
	}

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

	public void captureAuthorization(String saleId) throws ErpTransactionException {
		if (paymentHome == null) {
			getPaymentHome();
		}
		try {
			PaymentSB paymentSB = paymentHome.create();
			paymentSB.captureAuthorization(saleId);
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
	}

	public void deliveryConfirm(String saleId) throws ErpTransactionException {
		if (paymentHome == null) {
			getPaymentHome();
		}
		try {
			PaymentSB paymentSB = paymentHome.create();
			paymentSB.deliveryConfirm(saleId);
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
	}
	
	public void unconfirm(String saleId) throws ErpTransactionException {
		if (paymentHome == null) {
			getPaymentHome();
		}
		try {
			PaymentSB paymentSB = paymentHome.create();
			paymentSB.unconfirm(saleId);
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
	}

	public void voidCaptures(String saleId) throws ErpTransactionException {
		if (paymentHome == null) {
			getPaymentHome();
		}
		try {
			PaymentSB paymentSB = paymentHome.create();
			paymentSB.voidCaptures(saleId);
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
	}


	public ErpCashbackModel returnCashback(String orderNumber, ErpPaymentMethodI paymentMethod, double amount, double tax, ErpAffiliate affiliate)
		throws ErpTransactionException {
		ErpCashbackModel cashback = null;
		if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
			cashback = CPMServerGateway.returnECCashback(orderNumber, paymentMethod, amount, 0.0, affiliate.getMerchant(paymentMethod.getCardType()));
		} else { // default to credit card
			cashback = CPMServerGateway.returnCCCashback(orderNumber, paymentMethod, amount, 0.0, affiliate.getMerchant(paymentMethod.getCardType()));
		}
		cashback.setAffiliate(affiliate);
		return cashback;
	}

	public static ErpPaymentMethodI createInstance(String id, String customerId, EnumPaymentMethodType paymentMethodType) {

		ErpPaymentMethodI paymentMethod = null;

		if (EnumPaymentMethodType.ECHECK.equals(paymentMethodType)) {
			paymentMethod = new ErpECheckModel();
		} else {
			paymentMethod = new ErpCreditCardModel(); // default as of now    		
		}
		if (id != null && !"".equals(id)) {
			((ErpPaymentMethodModel) paymentMethod).setPK(new PrimaryKey(id));
		}
		if (customerId != null && !"".equals(customerId)) {
			paymentMethod.setCustomerId(customerId);
		}

		return paymentMethod;
	}

	public static ErpPaymentMethodI createInstance(EnumPaymentMethodType paymentMethodType) {
		return createInstance(null, null, paymentMethodType);
	}

	public static ErpPaymentMethodI createInstance() {
		return createInstance(null);
	}

	private static void getPaymentHome() {
		try {
			if (serviceLocator == null) {
				serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
			}
			paymentHome = (PaymentHome) serviceLocator.getRemoteHome("freshdirect.payment.Payment", PaymentHome.class);
		} catch (NamingException ex) {
			throw new EJBException(ex);
		}
	}
}
