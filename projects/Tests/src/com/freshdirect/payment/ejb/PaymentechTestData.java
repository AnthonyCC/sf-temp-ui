package com.freshdirect.payment.ejb;

import java.util.Calendar;
import java.util.Random;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpECheckModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.payment.EnumBankAccountType;
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
		   CreditCard card = null;
		   if(ccType.toString().equals("VISA")) {
			   card = getVisa(); 
		   }else if(ccType.toString().equals("MASTERCARD")) {
			   card = getMC(); 
		   }else if(ccType.toString().equals("AMEX")) {
			   card = getAMEX();
		   }else if(ccType.toString().equals("DISCOVER")) {
			   card = getDISC();
		   }
		return card;
	   }
	   static CreditCard getDummy() {
		   return PaymentMethodFactory.getCreditCard();
	   }
	   static CreditCard getVisa() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			cc.setCreditCardType(CreditCardType.VISA);
			
			cc.setAccountNumber("4112344112344113");
			cc.setCustomerName("Visa One");
			cc.setAddressLine1("1 Northeastern Blvd");
			cc.setCity("Bedford");
			cc.setState("NH");
			cc.setZipCode("03101");
			cc.setCVV("321");
			cc.setExpirationDate(now.getTime());
			System.out.println("cc: "+cc.toString());
			return cc;
		}
	   
	   static CreditCard getMC() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			
			cc.setCreditCardType(CreditCardType.MASTERCARD);
			cc.setAccountNumber("5112345112345114");
			cc.setCustomerName("MC One");
			cc.setAddressLine1("5 Northeastern Blvd");
			cc.setCity("Bedford");
			cc.setState("NH");
			cc.setZipCode("03101");
			cc.setCVV("123");
			cc.setExpirationDate(now.getTime());
			return cc;
		}
	   
	   static CreditCard getAMEX() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			
			cc.setCreditCardType(CreditCardType.AMEX);
			cc.setAccountNumber("341134113411347");
			cc.setCustomerName("AMEX One");
			cc.setAddressLine1("4 Northeastern Blvd");
			cc.setCity("Salem");
			cc.setState("NH");
			cc.setZipCode("03105");
			cc.setCVV("1234");
			cc.setExpirationDate(now.getTime());
			return cc;
		}
	   
	   static CreditCard getDISC() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			
			cc.setCreditCardType(CreditCardType.DISCOVER);
			cc.setAccountNumber("6559906559906557");
			cc.setCustomerName("DISC One");
			cc.setAddressLine1("5 Northeastern Blvd");
			cc.setCity("Bedford");
			cc.setState("NH");
			cc.setZipCode("03109");
			cc.setCVV("124");
			cc.setExpirationDate(now.getTime());
			return cc;
		}
	   
	   static CreditCard getMasterpassCard() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			
			/*cc.setCreditCardType(CreditCardType.MASTERCARD);
			cc.setAccountNumber("5112345112345114");
			cc.setCustomerName("MP One");
			cc.setAddressLine1("5 Northeastern Blvd");
			cc.setCity("Bedford");
			cc.setState("NH");
			cc.setZipCode("03101");
			cc.setCVV("123");
			cc.setExpirationDate(now.getTime());
			cc.setEwalletId("1");
			cc.setVendorEWalletID("101");*/
			
			cc.setCreditCardType(CreditCardType.MASTERCARD);
			cc.setAccountNumber("5555555555554444");
			cc.setCustomerName("MP One");
			cc.setAddressLine1("23-30 Borden Avenue Apt.");
			cc.setCity("Queens");
			cc.setState("NY");
			cc.setZipCode("11101");
			cc.setCVV("123");
			cc.setExpirationDate(now.getTime());
			cc.setEwalletId("1");
			cc.setVendorEWalletID("101");
			return cc;
		}
	   
	   static CreditCard getMasterpassCardAMEX() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
			
			cc.setCreditCardType(CreditCardType.AMEX);
			cc.setAccountNumber("341134113411347");
			cc.setCustomerName("AMEX One");
			cc.setAddressLine1("4 Northeastern Blvd");
			cc.setCity("Salem");
			cc.setState("NH");
			cc.setZipCode("03105");
			cc.setCVV("1234");
			cc.setExpirationDate(now.getTime());
			cc.setEwalletId("1");
			cc.setVendorEWalletID("101");
			return cc;
		}
	   
	   static CreditCard getMasterpassCardDISC() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			CreditCard cc=PaymentMethodFactory.getCreditCard();
					
			
			cc.setCreditCardType(CreditCardType.DISCOVER);
			cc.setAccountNumber("6559906559906557");
			cc.setCustomerName("DISC One");
			cc.setAddressLine1("5 Northeastern Blvd");
			cc.setCity("Bedford");
			cc.setState("NH");
			cc.setZipCode("03109");
			cc.setCVV("124");
			cc.setExpirationDate(now.getTime());
			cc.setEwalletId("1");
			cc.setVendorEWalletID("101");
			return cc;
		}
			   
	   static ErpPaymentMethodI parsePaymentMethod(CreditCard card) {
			// TODO Auto-generated method stub
			//ErpPaymentMethodI paymentmethod = (ErpPaymentMethodModel) PaymentechTestData.getCreditCard(CreditCardType.VISA);
			//ErpPaymentMethodI paymentmethod = (ErpPaymentMethodI) new ErpCustomerCreditModel();
			ErpCreditCardModel paymentmethod = new ErpCreditCardModel();
			if (card.getCreditCardType().toString().equals("VISA")){
			paymentmethod.setCardType(EnumCardType.VISA);
			} else if (card.getCreditCardType().toString().equals("MASTERCARD")){
				paymentmethod.setCardType(EnumCardType.MC);
			}else if (card.getCreditCardType().toString().equals("AMEX")){
				paymentmethod.setCardType(EnumCardType.AMEX);
				paymentmethod.setBypassAVSCheck(true);
			}else if (card.getCreditCardType().toString().equals("DISCOVER")){
				paymentmethod.setCardType(EnumCardType.DISC);
			}
			paymentmethod.setAccountNumber(card.getAccountNumber());
			paymentmethod.getAddress().setAddress1(card.getAddressLine1());
			paymentmethod.getAddress().setCity(card.getCity());
			paymentmethod.getAddress().setState(card.getState());
			paymentmethod.getAddress().setZipCode(card.getZipCode());
			
			paymentmethod.setAddress1(card.getAddressLine1());
			paymentmethod.setCity(card.getCity());
			paymentmethod.setState(card.getState());
			paymentmethod.setZipCode(card.getZipCode());
			
			paymentmethod.setName(card.getCustomerName());
			
			paymentmethod.setCVV(card.getCVV());
			paymentmethod.setExpirationDate(card.getExpirationDate());
			
			paymentmethod.seteWalletID(card.getEwalletId());
			paymentmethod.setVendorEWalletID(card.getVendorEWalletID());
			
			return paymentmethod;
		}
	   
	   public static ErpPaymentMethodI getECheck() {
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, 600);
			ErpECheckModel ec=new ErpECheckModel();
			
			ec.setAbaRouteNumber("122000247");
			ec.setCardType(EnumCardType.ECP);
			ec.setBankAccountType(EnumBankAccountType.PERSONAL_CHECKING);
			ec.setAccountNumber("0888271156");
			ec.setName("ECheck One");
			ec.setAddress1("100 Austin St");
			ec.setCity("Forest Hills");
			ec.setState("NY");
			ec.setZipCode("11415");
			ec.setBypassAVSCheck(true);
			return ec;
		}
	  
	 	   
	  
	}
