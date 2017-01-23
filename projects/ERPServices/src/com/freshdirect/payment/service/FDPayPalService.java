package com.freshdirect.payment.service;

import org.apache.log4j.Category;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.exceptions.AuthenticationException;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDPayPalServiceException;
//import com.freshdirect.fdstore.ewallet.impl.EWalletRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;


import com.freshdirect.payment.PayPalRequest;
import com.freshdirect.payment.PayPalResponse;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;

public class FDPayPalService extends AbstractService implements IPayPalService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDPayPalService.class);

	private static final String CREATE_CUSTOMER_API ="/create/customer";
	private static final String CREATE_PAYMENT_API ="/create/payment";
	private static final String GENERATE_TOKEN_API = "/token/get";
	private static final String CAPTURE_SETTLEMENT ="/capture";
	private static final String ISSUE_CASHBACK_API ="/issueCashback";
	private static final String REVERSE_AUTHORIZE ="/reverseAuthorize";
	private static final String VOID_CAPTURE ="/voidCapture";
	private static final String FIND_TOKEN_API ="/findToken";
	private static final String AUTHORISE_PAYMENT_API ="/authorize";
	private static IPayPalService INSTANCE;


	public static IPayPalService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDPayPalService();

		return INSTANCE;
	}

	

	@Override
	public GatewayType getType() {
		// TODO Auto-generated method stub
/*		AddressVerificationRequest request = new AddressVerificationRequest();
		request.setAddress(address);
		String inputJson = buildRequest(request);
		AddressVerificationResponse response =  getData(inputJson, getEndPoint(ADDRESSVERIFY_API), AddressVerificationResponse.class);
		return response;*/
		return null;
	}

	@SuppressWarnings("unchecked")
	public PayPalResponse createCustomer(String customerId,String fName,String lName) throws FDPayPalServiceException{
		PayPalRequest paypalRequest = new PayPalRequest();
		paypalRequest.setCustomerId(customerId);
		paypalRequest.setCustomerFirstName(fName);
		paypalRequest.setCustomerLastName(lName);
		String inputJson = buildRequest(paypalRequest);
		PayPalResponse  response = null;
		try{
		  response =  getData(inputJson, getEndPoint(CREATE_CUSTOMER_API), PayPalResponse.class);
		}catch(FDPayPalServiceException exception){
			exception.printStackTrace();
			throw exception;
		}catch(AuthenticationException exception){
			throw exception;
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return response;
		
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public PayPalResponse createPaymentMethod (
			String customerId,String paymentMethodNonce,String deviceId) throws FDPayPalServiceException {
		// TODO Auto-generated method stub
		PayPalRequest paymentMethodRequest = new PayPalRequest();
		paymentMethodRequest.setCustomerId(customerId);
		paymentMethodRequest.setPaymentMethodNonce(paymentMethodNonce);
		paymentMethodRequest.setDeviceId(deviceId);
		String inputJson = buildRequest(paymentMethodRequest);
		PayPalResponse  response =  getData(inputJson, getEndPoint(CREATE_PAYMENT_API), PayPalResponse.class);
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PayPalResponse authorize(
			ErpPaymentMethodI paymentMethod, String orderNumber,
			double authorizationAmount, double tax, String merchantId,
			String merchentMailId) throws FDPayPalServiceException {
		PayPalRequest paypalRequest = new PayPalRequest();
		
		paypalRequest.setDeviceId(paymentMethod.getDeviceId());
		paypalRequest.setProfileId(paymentMethod.getProfileID());
		paypalRequest.setOrderNumber(orderNumber);
		paypalRequest.setAuthorizationAmount(String.valueOf(authorizationAmount));
		paypalRequest.setTax(String.valueOf(tax));
		paypalRequest.setMerchantId(merchantId);
		paypalRequest.setMerchentEmailId(merchentMailId);
		
		PayPalResponse payPalResponse =null;
		String inputJson = buildRequest(paypalRequest);
		payPalResponse =  getData(inputJson, getEndPoint(AUTHORISE_PAYMENT_API), PayPalResponse.class);
		return payPalResponse;
	}



	@SuppressWarnings("unchecked")
	@Override
	public PayPalResponse capture(
			ErpAuthorizationModel authorization,
			ErpPaymentMethodI paymentMethod, double amount, double tax,
			String saleId)  {
		
		PayPalRequest paypalRequest = new PayPalRequest();
		paypalRequest.setTaxId(authorization.getEwalletTxId());
		paypalRequest.setAuthorizationAmount(String.valueOf(amount));
		paypalRequest.setTax(String.valueOf(tax));
		PayPalResponse payPalResponse = null;
		String inputJson;
		try {
			inputJson = buildRequest(paypalRequest);
			payPalResponse =  getData(inputJson, getEndPoint(CAPTURE_SETTLEMENT), PayPalResponse.class);
		} catch (FDPayPalServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return payPalResponse;
	}



	@SuppressWarnings("unchecked")
	@Override
	public PayPalResponse voidCapture(String taxId) {
		
		PayPalRequest paypalRequest = new PayPalRequest();
		paypalRequest.setTaxId(String.valueOf(taxId));
		PayPalResponse payPalResponse = null;
		String inputJson;
		try {
			inputJson = buildRequest(paypalRequest);
			payPalResponse =  getData(inputJson, getEndPoint(VOID_CAPTURE), PayPalResponse.class);
		} catch (FDPayPalServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return payPalResponse;
		}
		
		return payPalResponse;
	}
	@SuppressWarnings("unchecked")
	@Override
	public PayPalResponse reverseAuthorise(String taxId) {
		
		PayPalRequest paypalRequest = new PayPalRequest();
		paypalRequest.setTaxId(String.valueOf(taxId));
		PayPalResponse payPalResponse = null;
		String inputJson;
		try {
			inputJson = buildRequest(paypalRequest);
			payPalResponse =  getData(inputJson, getEndPoint(REVERSE_AUTHORIZE), PayPalResponse.class);
		} catch (FDPayPalServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return payPalResponse;
		}
		
		return payPalResponse;
	}



	@SuppressWarnings("unchecked")
	@Override
	public PayPalResponse issueCashback(
			String orderNumber, ErpPaymentMethodI paymentMethod, double amount,
			double tax) {
			PayPalRequest paypalRequest = new PayPalRequest();
			paypalRequest.setTaxId(paymentMethod.geteWalletTrxnId());
			paypalRequest.setAuthorizationAmount(String.valueOf(amount));
			Result<Transaction>  transactions =null;
			PayPalResponse payPalResponse = null;
			String inputJson;
			try {
				inputJson = buildRequest(paypalRequest);
				payPalResponse =  getData(inputJson, getEndPoint(ISSUE_CASHBACK_API), PayPalResponse.class);
			} catch (FDPayPalServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return payPalResponse;
			}
			
			return payPalResponse;
		}



	@Override
	public Response addProfile(Request request) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Response updateProfile(Request request) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Response deleteProfile(Request request) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Response getProfile(Request request) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public PayPalResponse findToken(String token, String customerId) {
		
	PayPalRequest paypalRequest = new PayPalRequest();
	paypalRequest.setToken(token);
	paypalRequest.setCustomerId(customerId);
	PayPalResponse  payPalResponse =null;
	String inputJson;
	try {
		inputJson = buildRequest(paypalRequest);
		payPalResponse =  getData(inputJson, getEndPoint(FIND_TOKEN_API), PayPalResponse.class);
	} catch (FDPayPalServiceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		payPalResponse =new PayPalResponse();
		return payPalResponse;
	}
	return payPalResponse;
	}



	@Override
	public String generateToken() throws FDPayPalServiceException{
		// TODO Auto-generated method stub
		String response = null;

			response = httpGetData( getEndPoint(GENERATE_TOKEN_API), String.class);
			response=response.replaceAll("\"", "");

		return response;
	}



	
}
