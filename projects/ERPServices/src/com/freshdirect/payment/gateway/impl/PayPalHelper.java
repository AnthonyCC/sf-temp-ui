package com.freshdirect.payment.gateway.impl;

import java.text.SimpleDateFormat;

import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.PaymentMethod;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.request.FieldNotFoundException;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

/**
 * @author Aniwesh Vatsal
 * 
 */
class PayPalHelper {
	private static final SimpleDateFormat SF = new SimpleDateFormat("MMyy");

	
	private static Response setException(ResponseImpl response, String exceptionMsg) {
		response.setRequestProcessed(false);
		response.setStatusCode("ERROR");
		response.setStatusMessage(exceptionMsg);
		if (null == response.getBillingInfo()) {
			response.setBillingInfo(response.getRequest().getBillingInfo());
		}
		return response;
	}
	
	
	public static Response processResponse(Request request, Response response){
		
		if(response == null){
			response = new ResponseImpl(request);
		}
		
		return null;
	}
	
	private static void setPaymentMethodInfo(String transaction,
			PaymentMethod paymentMethod, Request request)
			throws FieldNotFoundException, InitializationException {
		if (PaymentMethodType.CREDIT_CARD.equals(paymentMethod.getType())) {

			CreditCard creditCard = (CreditCard) paymentMethod;
			
//			PaymentechRequestHelper.setCCInfo(transaction, request, creditCard);
		} 
		
//		PaymentechRequestHelper.setAddressInfo(transaction, request,
//				paymentMethod);
	}
	
	 private static boolean isValid(Request request) {
			
		 if(request.getBillingInfo().getPaymentMethod()==null){
			 return false;
		 }
		 return true;
	 }
}
