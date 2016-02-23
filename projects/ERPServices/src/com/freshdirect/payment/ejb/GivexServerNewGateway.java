package com.freshdirect.payment.ejb;

import java.io.IOException;
import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumGivexErrorType;
import com.freshdirect.payment.GCExceptionType;
import com.freshdirect.payment.GiveXRequest;
import com.freshdirect.payment.GivexException;
import com.freshdirect.payment.GivexResponse;
import com.freshdirect.payment.GivexResponseModel;

public class GivexServerNewGateway extends BaseServerGateway {
	
	
	public static final String register_api = "/register";
	public static final String transferBalance_api = "/transferBalance";
	public static final String getBalance_api = "/getBalance";
	public static final String preAuth_api = "/preAuth";
	public static final String postAuth_api = "/postAuth";
	public static final String cancelPreAuth_api = "/cancelPreAuthorization";
	
	
	
	private static final Category LOGGER = LoggerFactory.getInstance( GivexServerNewGateway.class );
	
	public GivexResponseModel registerCard(double amount,String reference) throws GivexException{

		try{
			GiveXRequest request = new GiveXRequest();
			request.setAmount(amount);
			request.setReference(reference);
			
			GivexResponse response = call(register_api, request);	
			
			return parseResponse(response);
		}catch(IOException e){
			throw new GivexException(EnumGivexErrorType.ERROR_TIME_OUT.getDescription(), EnumGivexErrorType.ERROR_TIME_OUT.getErrorCode());
		}
	}
	
	
	private GivexResponseModel parseResponse(GivexResponse response) throws GivexException, IOException {
		if(response.getStatus() == null){
			throw new IOException();
		}else if(response.getStatus().equals("FAILED") &&  response.getException().getType().equals(GCExceptionType.ApplicationException.name()))
				throw new GivexException(response.getException().getMessage(), response.getException().getCode());
		else if(response.getStatus().equals("FAILED") &&  response.getException().getType().equals(GCExceptionType.RemoteException.name()))
			throw new RemoteException();
		else if(response.getStatus().equals("FAILED") &&  response.getException().getType().equals(GCExceptionType.IOException.name()))
			throw new IOException();
		else
			return new GivexResponseModel(response.getAuthCode(), response.getGivexNumber(), response.getCertBalance(), response.getExpiryDate(), response.getSecurityCode(), response.getAmount());
	}


	public GivexResponseModel transferBalance(ErpPaymentMethodI fromMethod,ErpPaymentMethodI toMethod,double amount,String reference) 
			throws IOException, GivexException{
		
			GiveXRequest request = new GiveXRequest();
			 
			request.setPaymentMethod(new PaymentMethodData(fromMethod.getAccountNumber(), fromMethod.getPaymentMethodType().getName()));
			request.setPaymentMethodTo(new PaymentMethodData(toMethod.getAccountNumber(), toMethod.getPaymentMethodType().getName()));
			request.setAmount(amount);
			request.setReference(reference);
			
			GivexResponse response = call(transferBalance_api, request);	
			
			return parseResponse(response);

	}
	
	
	
	public GivexResponseModel getBalance(ErpPaymentMethodI paymentMethod) throws IOException, GivexException{
		
		
			GiveXRequest request = new GiveXRequest();
			request.setPaymentMethod(new PaymentMethodData(paymentMethod.getAccountNumber(), paymentMethod.getPaymentMethodType().getName()));
			
			GivexResponse response = call(getBalance_api, request);		
			
			return parseResponse(response);

	}
	
	
	public GivexResponseModel preAuthGiftCard(ErpPaymentMethodI paymentMethod,double amount,String reference) throws GivexException{
	try{	
			GiveXRequest request = new GiveXRequest();
			request.setPaymentMethod(new PaymentMethodData(paymentMethod.getAccountNumber(), paymentMethod.getPaymentMethodType().getName()));
			request.setAmount(amount);
			request.setReference(reference);
			
			GivexResponse response = call(preAuth_api, request);		
			
			return parseResponse(response);
	}catch(IOException e){
		throw new GivexException(EnumGivexErrorType.ERROR_TIME_OUT.getDescription(), EnumGivexErrorType.ERROR_TIME_OUT.getErrorCode());
	}

	}
	
	
	public GivexResponseModel postAuthGiftCard(ErpPaymentMethodI paymentMethod,double amount,long authCode,String reference) throws GivexException{
	try{
			GiveXRequest request = new GiveXRequest();
			request.setPaymentMethod(new PaymentMethodData(paymentMethod.getAccountNumber(), paymentMethod.getPaymentMethodType().getName()));
			request.setAmount(amount);
			request.setAuthCode(authCode);
			request.setReference(reference);
			
			GivexResponse response = call(postAuth_api, request);
			
			return parseResponse(response);
	}catch(IOException e){
		throw new GivexException(EnumGivexErrorType.ERROR_TIME_OUT.getDescription(), EnumGivexErrorType.ERROR_TIME_OUT.getErrorCode());
	}

	
	}
	
	public GivexResponseModel cancelPreAuthorization(ErpPaymentMethodI paymentMethod,long authCode,String reference) throws GivexException{
	
	try{
			GiveXRequest request = new GiveXRequest();
			request.setPaymentMethod(new PaymentMethodData(paymentMethod.getAccountNumber(), paymentMethod.getPaymentMethodType().getName()));
			request.setAuthCode(authCode);
			request.setReference(reference);
			
			GivexResponse response = call(cancelPreAuth_api, request);		
			
			return parseResponse(response);
	}catch(IOException e){
		throw new GivexException(EnumGivexErrorType.ERROR_TIME_OUT.getDescription(), EnumGivexErrorType.ERROR_TIME_OUT.getErrorCode());
	}

	}

	private static GivexServerNewGateway gateway = null;
	public static GivexServerNewGateway getInstance() {
		if (gateway == null) {
				gateway = new GivexServerNewGateway();
		}
		return gateway;
	}

}
