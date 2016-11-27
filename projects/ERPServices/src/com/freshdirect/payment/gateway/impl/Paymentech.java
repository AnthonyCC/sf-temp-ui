package com.freshdirect.payment.gateway.impl;
import org.apache.commons.lang.StringUtils;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.GatewayAdapter;
import com.freshdirect.payment.PaylinxResourceException;
import com.freshdirect.payment.gateway.BillingInfo;
import com.freshdirect.payment.gateway.CreditCard;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.TransactionType;
import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.interfaces.ResponseIF;
import com.paymentech.orbital.sdk.request.FieldNotFoundException;
import com.paymentech.orbital.sdk.transactionProcessor.TransactionException;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;
public class Paymentech implements Gateway {

	private static final String INVALID_REQUEST="Request is INVALID.";
	
	public Response addProfile(Request request) {
		
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.ADD_PROFILE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		Response response = processProfileRequest(request);
		GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
	}
	
	public Response updateProfile(Request request) {
		
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.UPDATE_PROFILE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		Response response = processProfileRequest(request);
		GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
	}
	
	public Response getProfile(Request request) {
		
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.GET_PROFILE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		Response response = processProfileRequest(request);
		//GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}
	public Response deleteProfile(Request request) {
		
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.DELETE_PROFILE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		Response response = processProfileRequest(request);
		GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
	}

	public Response verify(Request request){
		Response response = null;
		if(request==null)throw new Error(INVALID_REQUEST);
		if(TransactionType.CC_VERIFY.equals(request.getTransactionType())||
			TransactionType.ACH_VERIFY.equals(request.getTransactionType())	)
			response = PaymentechHelper.processRequest(request);
		else
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}
	
	public ErpAuthorizationModel verify(String merchantId,ErpPaymentMethodI paymentMethod) throws ErpTransactionException {
		ErpAuthorizationModel authModel = null;
		
		try {
		Request request=GatewayAdapter.getVerifyRequest(merchantId,paymentMethod);
		Response response = verify(request);
		authModel=GatewayAdapter.getVerifyResponse(response,paymentMethod.isBypassAVSCheck());
		} catch(PaylinxResourceException pe){
			throw new ErpTransactionException(pe);
		}catch(Exception e){
			throw new ErpTransactionException(e);
		}
		return authModel;
		
		
		
	}
	private Response setException(Request request,ResponseImpl response, String exceptionMsg) {
		response.setRequestProcessed(false);
		response.setStatusCode("ERROR");
		response.setStatusMessage(exceptionMsg);
		if(null==response.getBillingInfo()) {
			response.setBillingInfo(request.getBillingInfo());
		}
		return response;
	}
	public Response authorize(Request request){
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.AUTHORIZE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		Response response = PaymentechHelper.processRequest(request);
		GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}
	@Override
	public ErpAuthorizationModel authorize(ErpPaymentMethodI paymentMethod,
			String orderNumber, double authorizationAmount, double tax,
			String merchantId) throws ErpTransactionException {
		
		ErpAuthorizationModel authModel;
		Response response;
		try {
			Request request=GatewayAdapter.getRequest(TransactionType.AUTHORIZE, paymentMethod, authorizationAmount, tax, orderNumber, merchantId);
			response = authorize(request);
			authModel = GatewayAdapter.getAuthResponse(response);
			
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
		return authModel;
		
		
		
	}
	
	public Response capture(Request request){
			if(request==null)throw new Error(INVALID_REQUEST);
			if(!TransactionType.CAPTURE.equals(request.getTransactionType()))
				throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
			Response response = PaymentechHelper.processRequest(request);
			GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
			return response;
	}
	@Override
	public ErpCaptureModel capture(ErpAuthorizationModel auth,
			ErpPaymentMethodI paymentMethod, double amount, double tax,
			String orderNumber) throws ErpTransactionException {
		ErpCaptureModel capture;
		if (!EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()))
				orderNumber = auth.getGatewayOrderID();
		
		if(StringUtils.isEmpty(auth.getGatewayOrderID()))
			auth.setGatewayOrderID(orderNumber);
		Request request=GatewayAdapter.getCaptureRequest(paymentMethod, auth, amount, tax); 
		Response response;
		try {
			response = capture(request);
			capture = GatewayAdapter.getCaptureResponse(response,auth);
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}		
		return capture;
		
		
	}

	public Response issueCashback(Request request){
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.CASHBACK.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		Response response = PaymentechHelper.processRequest(request);
		GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}
	@Override
	public ErpCashbackModel issueCashback(String orderNumber,
			ErpPaymentMethodI paymentMethod, double amount, double tax,
			ErpAffiliate affiliate) throws ErpTransactionException {
		ErpCashbackModel cashback = null;
		try {
		Request request = GatewayAdapter.getCashbackRequest(paymentMethod, amount, tax, orderNumber, affiliate.getMerchant(paymentMethod.getCardType()));
		Response response;
		
		response = issueCashback(request);
		cashback= GatewayAdapter.getCashbackResponse(response, paymentMethod);
		cashback.setAmount(amount);
		cashback.setTax(tax);
		cashback.setAffiliate(affiliate);
		} catch (Exception e) {
			throw new ErpTransactionException(e.getMessage());
		}
	return cashback;
	}

	@Override
	public Response reverseAuthorize(Request request) {
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.REVERSE_AUTHORIZE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		Response response = PaymentechHelper.processRequest(request);
		GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
	}

	public GatewayType getType() {
		return GatewayType.PAYMENTECH;
	}
	
	
	private Response processProfileRequest(Request request) {
		
		ResponseImpl response=new ResponseImpl(request);
		RequestIF _request=getGatewayProfileRequest(request,response);
		ResponseIF _response=getGatewayResponse(request,response,_request);
		return PaymentechHelper.setResponse(request,response,_request,_response);
	}
	
	private RequestIF getGatewayProfileRequest(Request request,ResponseImpl response) {
		RequestIF gatewayReq=null;
		try {
			gatewayReq=PaymentechHelper.createProfileRequest(request);
		} catch (InitializationException e) {
			e.printStackTrace();
			setException(request,response,e.toString());
		} catch (FieldNotFoundException e) {
			setException(request,response,e.toString());
		} catch(Exception e) {
			e.printStackTrace();
			setException(request,response,e.toString());
		}		
		return gatewayReq;
	}
	private ResponseIF getGatewayResponse(Request request,ResponseImpl response,RequestIF gatewayReq) {
		ResponseIF gatewayResponse=null;
		try {
			gatewayResponse=PaymentechHelper.processRequest(gatewayReq);
			
		} catch (InitializationException e) {
			e.printStackTrace();
			setException(request,response,e.toString());
			
		} catch (TransactionException e) {
			e.printStackTrace();
			setException(request,response,e.toString());
		}
		return gatewayResponse;
	}

	public Response voidCapture(Request request){
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.VOID_CAPTURE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		Response response =  PaymentechHelper.processRequest(request);
		GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}
	@Override
	public ErpVoidCaptureModel voidCapture(ErpPaymentMethodI paymentMethod, ErpCaptureModel capture) throws ErpTransactionException {
		try{
		Request request = GatewayAdapter.getVoidCaptureRequest(paymentMethod, capture);
		Response response;
		ErpVoidCaptureModel voidCapture = null;
		response = voidCapture(request);
		voidCapture = GatewayAdapter.getVoidCaptureResponse(response, capture);
		return voidCapture;
		}
		catch(PaylinxResourceException pe){
			throw new ErpTransactionException(pe);
		}
	}
	
	
	
	public void settleTransactions(Merchant merchant)  {
		
		RequestIF request = null;
        try {
            request = new com.paymentech.orbital.sdk.request.Request(RequestIF.END_OF_DAY_TRANSACTION);
            
            request.setFieldValue("MerchantID", PaymentechConstants.MerchantID.get(merchant).getValue());
            request.setFieldValue("BIN", PaymentechConstants.BIN.SALEM.getCode());
            ResponseIF response = PaymentechHelper.processRequest(request);
           
        } catch (InitializationException ie) {
            System.err.println("Unable to initialize request object");
            System.err.println(ie.getMessage());
            ie.printStackTrace();
            
        } catch (FieldNotFoundException fnfe) {
            System.err.println("Unable to find XML field in template");
            System.err.println(fnfe.getMessage());
            fnfe.printStackTrace();
           
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
		
	}

	@Override
	public boolean isValidToken(String token, String customerId) {
		// TODO Auto-generated method stub
		return false;
	}

		
	
}
