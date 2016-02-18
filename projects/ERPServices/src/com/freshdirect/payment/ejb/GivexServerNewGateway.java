package com.freshdirect.payment.ejb;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.payment.GiveXRequest;
import com.freshdirect.payment.GivexException;
import com.freshdirect.payment.GivexResponseModel;

public class GivexServerNewGateway extends BaseServerGateway {
	
	
	public static final String register_api = "/register";
	public static final String transferBalance_api = "/transferBalance";
	public static final String getBalance_api = "/getBalance";
	public static final String preAuth_api = "/preAuth";
	public static final String postAuth_api = "/postAuth";
	public static final String cancelPreAuth_api = "/cancelPreAuthorization";
	
	
	
	private static final Category LOGGER = LoggerFactory.getInstance( GivexServerNewGateway.class );
	
	public static GivexResponseModel registerCard(double amount,String reference) throws GivexException{

		HttpURLConnection conn = null;
		try {

			//conn = getConnection(register_api);
			
			GiveXRequest request = new GiveXRequest();
			request.setAmount(amount);
			request.setReference(reference);
			
			GivexResponseModel response = getResponse(register_api, request);			
			return response;

		} catch (MalformedURLException e) {
			throw new GivexException(e);
		} catch (IOException e) {
			throw new GivexException(e);		
		} finally{
			if(conn!=null)conn.disconnect();	
		}
	

	}
	
	
	public static GivexResponseModel transferBalance(ErpPaymentMethodI fromMethod,ErpPaymentMethodI toMethod,double amount,String reference) 
			throws IOException, GivexException{
		HttpURLConnection conn = null;
		
		try {

			//conn = getConnection(transferBalance_api);
			
			GiveXRequest request = new GiveXRequest();
			request.setPaymentMethod(fromMethod);
			request.setPaymentMethodTo(toMethod);
			request.setAmount(amount);
			request.setReference(reference);
			
			GivexResponseModel response = getResponse(transferBalance_api, request);			
			return response;

		} catch (MalformedURLException e) {
			throw new GivexException(e);
		} catch (IOException e) {
			throw new GivexException(e);		
		}finally{
			if(conn!=null)conn.disconnect();	
		}
	

	}
	
	
	
	public static GivexResponseModel getBalance(ErpPaymentMethodI paymentMethod) throws IOException, GivexException{
		HttpURLConnection conn = null;
		
		try {
			//conn = getConnection(getBalance_api);
			
			
			GiveXRequest request = new GiveXRequest();
			request.setPaymentMethod(paymentMethod);
			
			GivexResponseModel response = getResponse(getBalance_api, request);		
			return response;

		} catch (MalformedURLException e) {
			throw new GivexException(e);
		} catch (IOException e) {
			throw new GivexException(e);		
		}finally{
			if(conn!=null)conn.disconnect();	
		}
	

	}
	
	
	public static GivexResponseModel preAuthGiftCard(ErpPaymentMethodI paymentMethod,double amount,String reference) throws GivexException{
		HttpURLConnection conn = null;
		
		try {

			//conn = getConnection(preAuth_api);
			
			
			GiveXRequest request = new GiveXRequest();
			request.setPaymentMethod(paymentMethod);
			request.setAmount(amount);
			request.setReference(reference);
			
			GivexResponseModel response = getResponse(preAuth_api, request);		
			return response;

		} catch (MalformedURLException e) {
			throw new GivexException(e);
		} catch (IOException e) {
			throw new GivexException(e);		
		}finally{
			if(conn!=null)conn.disconnect();	
		}
	

	}
	
	
	public static GivexResponseModel postAuthGiftCard(ErpPaymentMethodI paymentMethod,double amount,long authCode,String reference) throws GivexException{

		HttpURLConnection conn = null;
		
		try {

			//conn = getConnection(postAuth_api);
			
			GiveXRequest request = new GiveXRequest();
			request.setPaymentMethod(paymentMethod);
			request.setAmount(amount);
			request.setAuthCode(authCode);
			request.setReference(reference);
			
			GivexResponseModel response = getResponse(postAuth_api, request);		
			return response;

		} catch (MalformedURLException e) {
			throw new GivexException(e);
		} catch (IOException e) {
			throw new GivexException(e);		
		}finally{
			if(conn!=null)conn.disconnect();	
		}
	

	
	}
	
	public static GivexResponseModel cancelPreAuthorization(ErpPaymentMethodI paymentMethod,long authCode,String reference) throws GivexException{

		HttpURLConnection conn = null;
		

		try {

			//conn = getConnection(cancelPreAuth_api);
			
			
			GiveXRequest request = new GiveXRequest();
			request.setPaymentMethod(paymentMethod);
			request.setAuthCode(authCode);
			request.setReference(reference);
			
			GivexResponseModel response = getResponse(cancelPreAuth_api, request);		
			return response;

		} catch (MalformedURLException e) {
			throw new GivexException(e);
		} catch (IOException e) {
			throw new GivexException(e);		
		}finally{
			if(conn!=null)conn.disconnect();	
		}
	

	
	
	}
									
	public static void main(String args[]) throws FDResourceException, IOException{
		
		try{
			//GivexResponseModel response = registerCard(5.0, "timeOutTest2");
			//System.out.println("New Card Number "+response.getGivexNumber());
			//System.out.println("Auth Code: "+response.getAuthCode());
			ErpGiftCardModel pm = new ErpGiftCardModel();
			pm.setAccountNumber("60362847331161464760");
			System.out.println("Balance on new Card "+getBalance(pm).getCertBalance());			
		}catch(GivexException ge) {
			System.out.println("Error code "+ge.getErrorCode());
			System.out.println("Error Message "+ge.getMessage());
		}catch(Exception ex) {
			System.out.println("Exception Caught $$$$$$$$$$$$$$$$ ");
			ex.printStackTrace();
		}
	}

}
