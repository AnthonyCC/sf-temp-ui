package com.freshdirect.payment;

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
import com.freshdirect.customer.ErpEbtCardModel;
import com.freshdirect.customer.ErpPayPalCardModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.ecomm.gateway.PaymentsService;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.payment.ejb.PaymentGatewayContext;
import com.freshdirect.payment.ejb.PaymentHome;
import com.freshdirect.payment.ejb.PaymentSB;
import com.freshdirect.payment.fraud.ejb.RestrictedPaymentMethodSessionBean;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.impl.GatewayFactory;
public class PaymentManager {

	private static Category LOGGER = LoggerFactory.getInstance(PaymentManager.class);
	private static ServiceLocator serviceLocator = null;
	private static PaymentHome paymentHome = null;
	private static final String ECHECK_VERIFY_UNAVAIL_MSG="This feature is not available for E-Checks";
	static {
		try {
			serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
		} catch (Exception e) {
			LOGGER.error("PaymentManager.serviceLocator initialization exception: " + e.getMessage());
		}
	}	
	public ErpAuthorizationModel verify(final String merchant,ErpPaymentMethodI paymentMethod) throws ErpTransactionException {
		PaymentGatewayContext context = new PaymentGatewayContext(GatewayType.PAYMENTECH, GatewayType.PAYMENTECH);
		
		Gateway gateway = GatewayFactory.getGateway(context);
		ErpAuthorizationModel auth = gateway.verify(merchant,paymentMethod);
		return auth;
	}
	
	
	public ErpAuthorizationModel authorize(
		ErpPaymentMethodI paymentMethod,
		String orderNumber,
		double authorizationAmount,
		double tax,
		String merchantId) throws ErpTransactionException {
		
		if(EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
			return authorizeECheck(paymentMethod,authorizationAmount,tax,merchantId,orderNumber);
		}
		PaymentGatewayContext context = null;
		if(EnumCardType.PAYPAL.equals(paymentMethod.getCardType())){
			context = new PaymentGatewayContext(GatewayType.PAYPAL, null);
		}else{
			context = new PaymentGatewayContext(GatewayType.PAYMENTECH, null);
		}
				
		Gateway gateway = GatewayFactory.getGateway(context);
		ErpAuthorizationModel auth = gateway.authorize(
				 paymentMethod,
				 orderNumber,
				 authorizationAmount,
				 tax,
				 merchantId);
		
		return auth;
	}


	public ErpCashbackModel returnCashback(String orderNumber, ErpPaymentMethodI paymentMethod, double amount, double tax, ErpAffiliate affiliate)
		throws ErpTransactionException {
		
		PaymentGatewayContext context = null;
		if (!paymentMethod.getCardType().equals(EnumCardType.PAYPAL)) {
			context = new PaymentGatewayContext(GatewayType.PAYMENTECH, null);
		} else {
			context = new PaymentGatewayContext(GatewayType.PAYPAL, null);
		}
		Gateway gateway = GatewayFactory.getGateway(context);
		ErpCashbackModel cashback = gateway.issueCashback(orderNumber, paymentMethod, amount, tax, affiliate);
		return cashback;
	}
	
	
	public void captureAuthorization(String saleId) throws ErpTransactionException {
		
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PaymentSB)){
				PaymentsService.getInstance().captureAuthorization(saleId);
			}
			else{
				if (paymentHome == null) {
					getPaymentHome();
				}
				PaymentSB paymentSB = paymentHome.create();
				paymentSB.captureAuthorization(saleId);
			}
			
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
	}

	public void voidCaptures(String saleId) throws ErpTransactionException {
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PaymentSB)) {
				PaymentsService.getInstance().voidCaptures(saleId);
			} else {
				if (paymentHome == null) {
					getPaymentHome();
				}
				PaymentSB paymentSB = paymentHome.create();
				paymentSB.voidCaptures(saleId);
			}
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
	}

	public static ErpPaymentMethodI createInstance(String id, String customerId, EnumPaymentMethodType paymentMethodType) {

		ErpPaymentMethodI paymentMethod = null;
		if (EnumPaymentMethodType.GIFTCARD.equals(paymentMethodType)) {
			paymentMethod = new ErpGiftCardModel();
		}
		else if (EnumPaymentMethodType.ECHECK.equals(paymentMethodType)) {
			paymentMethod = new ErpECheckModel();
		} else if (EnumPaymentMethodType.EBT.equals(paymentMethodType)) {
			paymentMethod = new ErpEbtCardModel(); 
		} else if (EnumPaymentMethodType.PAYPAL.equals(paymentMethodType)) {
			paymentMethod = new ErpPayPalCardModel(); 
		}else {
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

	private static void getPaymentHome() throws Exception {
		
		if (serviceLocator == null) {
			serviceLocator = new ServiceLocator(ErpServicesProperties.getInitialContext());
		}
		paymentHome = (PaymentHome) serviceLocator.getRemoteHome("freshdirect.payment.Payment");
		
	}
	
	private ErpAuthorizationModel authorizeECheck(
			ErpPaymentMethodI paymentMethod,
			double authorizationAmount,
			double tax,
			String merchantId,
			String orderNumber) {
			
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
			String maskedAccountNumber = paymentMethod.getMaskedAccountNumber();
			if(!StringUtil.isEmpty(maskedAccountNumber))
				auth.setCcNumLast4(maskedAccountNumber.substring(maskedAccountNumber.length() - 4));
			
			auth.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
			auth.setBankAccountType(paymentMethod.getBankAccountType());
			auth.setCardType(paymentMethod.getCardType());
			GatewayType gType=GatewayType.PAYMENTECH;
					
			if(GatewayType.PAYMENTECH.equals(gType)) {
				auth.setProfileID(paymentMethod.getProfileID());
				auth.setGatewayOrderID(orderNumber);
				
			}
			RestrictedPaymentMethodSessionBean sb = new RestrictedPaymentMethodSessionBean();
			boolean isAuthorized = (sb.checkBadAccount(paymentMethod, false)) ? false : true;// TBD-change this to use profile

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
	
	/**
	 * Check whether Valut TOken is valid or not
	 * @param token
	 * @return
	 */
	public boolean isValidVaultToken(String token, String customerId){
			PaymentGatewayContext context = null;
			context = new PaymentGatewayContext(GatewayType.PAYPAL, null);
			Gateway gateway = GatewayFactory.getGateway(context);
			return gateway.isValidToken(token,customerId);
		
	}
}
