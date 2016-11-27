package com.freshdirect.payment.gateway.impl;

import java.util.Arrays;
import java.util.List;

import com.freshdirect.payment.gateway.BankAccountType;
import com.freshdirect.payment.gateway.CreditCardType;
import com.paymentech.orbital.sdk.interfaces.ResponseIF;

final class PaymentechResponseHelper {
	final static List<String> validAVSResponses= Arrays.asList(PaymentechConstants.VALID_ZIP_CHECK_RESPONSES);

	public static boolean isZipMatch(String avsResponse) {
		
		if(validAVSResponses.contains(avsResponse))
			return true;
		return false;
	}

	public static boolean isCVVMatch(String cvvResponse) {
		
		if(PaymentechConstants.CVV_MATCH.equals(cvvResponse))
			return true;
		return false;
	}
	public static CreditCardType getCardBrand(ResponseIF response) {
		
		String val=response.getValue(PaymentechFields.NewOrderResponse.CardBrand.name());
		if(val!=null)val=val.trim();
		if(PaymentechConstants.CardType.VISA.getCode().equals(val))
			return CreditCardType.VISA;
		else if(PaymentechConstants.CardType.MASTERCARD.getCode().equals(val))
			return CreditCardType.MASTERCARD;	
		else if(PaymentechConstants.CardType.AMEX.getCode().equals(val))
			return CreditCardType.AMEX;	
		else if(PaymentechConstants.CardType.DISCOVER.getCode().equals(val))
			return CreditCardType.DISCOVER;	
		return null;
	}
	
	public static BankAccountType getBankAccountType(ResponseIF response) {
		
		String val=response.getValue(PaymentechFields.NewOrderResponse. CardBrand.name());
		
		return null;
	}
	
	public static String getAccountNumber(ResponseIF response) {
		return response.getValue(PaymentechFields.NewOrderResponse.AccountNum.name());
	}
	public static String getResponseCode(ResponseIF response) {
		return response.getValue(PaymentechFields.NewOrderResponse.RespCode.name());
	}
	public static String getHostResponseCode(ResponseIF response) {
		return response.getValue(PaymentechFields.NewOrderResponse.HostRespCode.name());
	}
	
	
}
