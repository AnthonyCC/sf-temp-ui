package com.freshdirect.payment.gateway.test;

import java.util.Calendar;
import java.util.Random;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.impl.BillingInfoFactory;
import com.freshdirect.payment.gateway.impl.PaymentMethodFactory;
import com.freshdirect.payment.gateway.impl.RequestFactory;

public class PaymentechTestData {
	
	
	static String getSaleID(String customerID) {
		Random randomGenerator = new Random();
	
		String saleId="";
		if(null==customerID) {
			saleId=new StringBuilder("0000").append(randomGenerator.nextInt(10000)).append("X").append(randomGenerator.nextInt(10000)).toString();	
		} else if("".equals(customerID.trim())){
			saleId=new StringBuilder("0000").append(randomGenerator.nextInt(10000)).append("X").append(randomGenerator.nextInt(10000)).toString();
		} else {
			saleId=new StringBuilder(customerID).append("X").append(randomGenerator.nextInt(10000)).toString();
		}
		return saleId;
	}
	  static Request getCCVerifyRequest(CreditCardType ccType) {
		  Request request=RequestFactory.getRequest(TransactionType.CC_VERIFY);
		  BillingInfo billingInfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,getCreditCard(ccType));
		  billingInfo.getPaymentMethod().setCustomerID("12345");
		  billingInfo.setTransactionID(getSaleID(billingInfo.getPaymentMethod().getCustomerID()));
		  request.setBillingInfo(billingInfo);
		  return request;
	  }
	  static Request getAddProfileRequest(CreditCardType ccType)  {
		  Request request=RequestFactory.getRequest(TransactionType.ADD_PROFILE);
		  BillingInfo billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,getCreditCard(ccType));
		  request.setBillingInfo(billinginfo);
		  return request;
	  }
	
	  static Request getGetProfileRequest(String profileID)  {
		  Request request=RequestFactory.getRequest(TransactionType.GET_PROFILE);
		  CreditCard cc=getDummy();
		  cc.setBillingProfileID(profileID);
		  BillingInfo billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,cc);
		  request.setBillingInfo(billinginfo);
		  return request;
	  }
	  
	  static Request getDeleteProfileRequest(String profileID)  {
		  Request request=RequestFactory.getRequest(TransactionType.DELETE_PROFILE);
		  CreditCard cc=getDummy();
		  cc.setBillingProfileID(profileID);
		  BillingInfo billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,cc);
		  request.setBillingInfo(billinginfo);
		  return request;
	  }
	  static Request getUpdateProfileRequest(CreditCardType ccType,String profileID)  {
		  Request request=RequestFactory.getRequest(TransactionType.UPDATE_PROFILE);
		  BillingInfo billinginfo=BillingInfoFactory.getBillingInfo(Merchant.FRESHDIRECT,getCreditCard(ccType));
		  billinginfo.getPaymentMethod().setBillingProfileID(profileID);
		  billinginfo.getPaymentMethod().setAddressLine1("100 Times Square");
		  billinginfo.getPaymentMethod().setCity("New York City");
		  billinginfo.getPaymentMethod().setZipCode("10000");
		  request.setBillingInfo(billinginfo);
		  return request;
	  }
	   static CreditCard getCreditCard(CreditCardType ccType) {
		   if(CreditCardType.VISA.equals(ccType)) {
			  return getVisa(); 
		   }
		 return getVisa();  
	   }
	   static CreditCard getDummy() {
		   return PaymentMethodFactory.getCreditCard();  
	   }
	   public static CreditCard getVisa() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			cc.setCreditCardType(CreditCardType.VISA);
			
			cc.setAccountNumber("4444444444444448");
			cc.setCustomerName("Visa One");
			cc.setAddressLine1("23-30 Borden Ave");
			cc.setCity("Long Island City");
			cc.setState("NY");
			cc.setZipCode("11101");
			cc.setCVV("411");
			cc.setExpirationDate(now.getTime());
			return cc;
		}
		/*public static ErpCaptureModel getCaptureModel() {
			ErpCaptureModel cap=new ErpCaptureModel();
			cap.setSequenceNumber("512D14BC8B7D1878179D095CCE4D448B1040538C");
			return cap;
		}
		public static ErpAuthorizationModel getAuthModel() {
			ErpAuthorizationModel auth=new ErpAuthorizationModel();
			auth.setSequenceNumber("512CE2DDDB6AF88CC603556C4532243B40AC53A4");
			auth.setAmount(50.0);
			auth.setTax(0);
			
			return auth;
		}
		public static ErpPaymentMethodI getPaymentModel() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			ErpCreditCardModel cc=new ErpCreditCardModel();
			
			cc.setCardType(EnumCardType.AMEX);
			cc.setAccountNumber("371449635398431");
			cc.setName("Alpha Beta");
			cc.setAddress1("23-30 Borden Ave");
			cc.setCity("Long Island City");
			cc.setState("NY");
			cc.setZipCode("11101");
			cc.setExpirationDate(now.getTime());
			return cc;
		}
		
		public static ErpPaymentMethodI getVisa() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			ErpCreditCardModel cc=new ErpCreditCardModel();
			
			cc.setCardType(EnumCardType.VISA);
			cc.setAccountNumber("4444444444444448");
			cc.setName("Visa One");
			cc.setAddress1("23-30 Borden Ave");
			cc.setCity("Long Island City");
			cc.setState("NY");
			cc.setZipCode("11101");
			cc.setCVV("411");
			cc.setExpirationDate(now.getTime());
			return cc;
		}
		public static ErpPaymentMethodI getMC() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			ErpCreditCardModel cc=new ErpCreditCardModel();
			
			cc.setCardType(EnumCardType.MC);
			cc.setAccountNumber("5500005555555559");
			cc.setName("MC One");
			cc.setAddress1("23-30 Borden Ave");
			cc.setCity("Long Island City");
			cc.setState("NY");
			cc.setZipCode("11101");
			cc.setCVV("411");
			cc.setExpirationDate(now.getTime());
			return cc;
		}
		public static ErpPaymentMethodI getDisc() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			ErpCreditCardModel cc=new ErpCreditCardModel();
			
			cc.setCardType(EnumCardType.DISC);
			cc.setAccountNumber("6011000995500000");
			cc.setName("Disc One");
			cc.setAddress1("23-30 Borden Ave");
			cc.setCity("Long Island City");
			cc.setState("NY");
			cc.setZipCode("11101");
			cc.setCVV("411");
			cc.setExpirationDate(now.getTime());
			return cc;
		}
		public static ErpPaymentMethodI getAmex() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			ErpCreditCardModel cc=new ErpCreditCardModel();
			
			cc.setCardType(EnumCardType.AMEX);
			cc.setAccountNumber("371449635398431");
			cc.setName("Amex One");
			cc.setAddress1("23-30 Borden Ave");
			cc.setCity("Long Island City");
			cc.setState("NY");
			cc.setZipCode("11101");
			cc.setCVV("4111");
			cc.setExpirationDate(now.getTime());
			return cc;
		}
		public static ErpPaymentMethodI getECheck() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			ErpECheckModel ec=new ErpECheckModel();
			
			ec.setAbaRouteNumber("122000247");
			ec.setBankAccountType(EnumBankAccountType.PERSONAL_CHECKING);
			ec.setAccountNumber("0888271156");
			ec.setName("ECheck One");
			ec.setAddress1("100 Austin St");
			ec.setCity("Forest Hills");
			ec.setState("NY");
			ec.setZipCode("11415");
			return ec;
		}
		
		public static ErpPaymentMethodI getInvalidECheck() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			ErpECheckModel ec=new ErpECheckModel();
			
			ec.setAbaRouteNumber("051001413");
			ec.setBankAccountType(EnumBankAccountType.PERSONAL_CHECKING);
			ec.setAccountNumber("12587458963");
			ec.setName("ECheck Invalid");
			ec.setAddress1("100 Austin St");
			ec.setCity("Forest Hills");
			ec.setState("NY");
			ec.setZipCode("11415");
			return ec;
		}
		
		public static void testCreateProfileCCVisa() throws Exception  {//29963367
			ChaseGateway gateway=new ChaseGateway();
			ErpPaymentMethodI paymentMethod=getVisa();
			gateway.addProfile(paymentMethod);
		}
		private static ErpPaymentMethodI flipAddress(ErpPaymentMethodI paymentMethod) {
			if("11415".equals(paymentMethod.getZipCode())) {
				paymentMethod.setAddress1("23-30 Borden Ave");
				paymentMethod.setCity("Long Island City");
				paymentMethod.setState("NY");
				paymentMethod.setZipCode("11101");
				
			} else {
				
				paymentMethod.setAddress1("100 Austin St");
				paymentMethod.setCity("Forest Hills");
				paymentMethod.setState("NY");
				paymentMethod.setZipCode("11415");
			}
			return  paymentMethod;
		}
		public static void testUpdateProfileCCVisa() throws Exception {
			String profile="29963367";
			ChaseGateway gateway=new ChaseGateway();
			ErpPaymentMethodI paymentMethod=gateway.getProfile(profile);
			paymentMethod=flipAddress(paymentMethod);
			gateway.updateProfile(paymentMethod, profile);
			//gateway.getProfile(profile);
			
		}
		public static void testUpdateProfileEC()throws  Exception{
			String profile="29945367";
			ChaseGateway gateway=new ChaseGateway();
			ErpPaymentMethodI paymentMethod=gateway.getProfile(profile);
			paymentMethod=flipAddress(paymentMethod);
			gateway.updateProfile(paymentMethod,profile );
			gateway.getProfile(profile);
			
		}
		public static void testAddProfileEC() throws  Exception{
			
		}
		public static void testVerifyEC() throws  Exception{
			ChaseGateway gateway=new ChaseGateway();
			ErpPaymentMethodI paymentMethod= getInvalidECheck();
			gateway.verifyECAccountTx(paymentMethod);
		}
		
		public static void testAuthAndCaptureVisa(ErpPaymentMethodI paymentMethod, double amount, double tax, String saleId, String merchantId, String profileID ) throws Exception {
			ChaseGateway gateway=new ChaseGateway();
			PaylinxResponseModel response=gateway.authorizeCCTx(paymentMethod, amount, tax, saleId, merchantId, profileID);
			ErpAuthorizationModel authorization=response.getAuthorizationModel();
			gateway.captureCCTx(authorization, saleId);
		}
		
		public static void testRefundCC(ErpPaymentMethodI paymentMethod, double amount, double tax, String saleId, String merchantId, String profileID ) throws Exception { 
			ChaseGateway gateway=new ChaseGateway();
			gateway.refundCCTx(saleId, paymentMethod, amount, tax, merchantId, profileID);
			
		}*/
	}
