package com.freshdirect.payment;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AuthorizationCommand {
	
	private static final Category LOGGER = LoggerFactory.getInstance(AuthorizationCommand.class);
	

	private final List<AuthorizationInfo>		authInfos;
	private int									authCount;
	private final List<ErpAuthorizationModel>	auths;
	
	public AuthorizationCommand(List<AuthorizationInfo> authInfo, int authCount) {
		this.authInfos = authInfo;
		this.authCount = authCount;
		this.auths = new ArrayList<ErpAuthorizationModel>();
	}

	public void execute() {
		

		//TODO might have to rethink this try/for block
		//declaring it over here for the logger message in catch block
		String saleId = null;
		try {
			for ( AuthorizationInfo info : authInfos ) {
				saleId = info.getSaleId();
				ErpAuthorizationModel auth = this.authorize(info);
				auth.setAffiliate(info.getAffiliate());
				auth.setIsChargePayment(info.isAdditionalCharge());
				LOGGER.info("Authorizing sale " + info.getSaleId() + " - response code " + auth.getResponseCode() + " - merchant " + info.getMerchantId()+", for $"+info.getAmount());
				this.auths.add(auth);
				if(!auth.isApproved()) {
					break;
				}
			}
		} catch (ErpTransactionException te) {
			LOGGER.warn("Placing order -ErpTransactionException encountered. saleId=" + saleId, te);
			return;
		}

	}

	public List<ErpAuthorizationModel> getAuthorizations() {
		return this.auths;
	}

	private ErpAuthorizationModel authorize(AuthorizationInfo info) throws ErpTransactionException {

		ErpPaymentMethodI paymentMethod = info.getPaymentMethod();
		EnumPaymentType pt = paymentMethod.getPaymentType();
		double authAmount = info.getAmount();

		if (EnumPaymentType.ON_FD_ACCOUNT.equals(pt) || EnumPaymentType.MAKE_GOOD.equals(pt)) {
			return createFDAuthorization(paymentMethod, authAmount, 0.0);
		}
		
		if (EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())) {
			return createEBTAuthorization(paymentMethod, authAmount, 0.0);
		}

		if ((EnumCardType.DISC.equals(paymentMethod.getCardType()) || EnumCardType.AMEX.equals(paymentMethod.getCardType())) && authAmount < 1.0) {
			LOGGER.info(info.getSaleId()+" - required authorization for :"+authAmount);
			authAmount = 1.0;
		}

		// authorizing for amount+tax as amount and zero for tax as requested by SAP
		
		// Paymentech requires us to send unique order number as part of each auth TX
		// therefore we are appending the - with number of auths to the sale id
		String orderNumber = info.getSaleId() + "X" + ++this.authCount;

		PaymentManager paymentManager = new PaymentManager();
		return paymentManager.authorize(paymentMethod, orderNumber, authAmount, 0.0, info.getMerchantId());
	}

	private ErpAuthorizationModel createFDAuthorization(ErpPaymentMethodI pm, double amount, double tax){
		ErpAuthorizationModel auth = new ErpAuthorizationModel();
		auth.setTransactionSource(EnumTransactionSource.SYSTEM);
		auth.setReturnCode(0);
		auth.setAuthCode("FD1000");
		auth.setResponseCode(EnumPaymentResponse.APPROVED);
		auth.setDescription("FD APPROVAL");
		auth.setSequenceNumber("FD AUTH");
		auth.setAvs("Y");
		auth.setAmount(amount);
		auth.setTax(tax);
		auth.setCardType(pm.getCardType());
		if((!StringUtil.isEmpty(pm.getMaskedAccountNumber())) &&(4<=pm.getMaskedAccountNumber().length()) ) {
			auth.setCcNumLast4(pm.getMaskedAccountNumber().substring(pm.getMaskedAccountNumber().length()-4));
		}
		auth.setProfileID(pm.getProfileID());
		return auth;
	}
	
	private ErpAuthorizationModel createEBTAuthorization(ErpPaymentMethodI pm, double amount, double tax){
		ErpAuthorizationModel auth = new ErpAuthorizationModel();
		auth.setTransactionSource(EnumTransactionSource.SYSTEM);
		auth.setReturnCode(0);
		auth.setAuthCode("EBT1000");
		auth.setResponseCode(EnumPaymentResponse.APPROVED);
		auth.setDescription("EBT APPROVAL");
		auth.setSequenceNumber("EBT AUTH");
		auth.setAvs("Y");
		auth.setAmount(amount);
		auth.setTax(tax);
		auth.setCardType(pm.getCardType());
		String accountNumber = pm.getAccountNumber();
		if(accountNumber != null && accountNumber.length() >= 4){
			auth.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
		}
		return auth;
	}
	
}
