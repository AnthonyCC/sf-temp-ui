package com.freshdirect.payment.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.client.RestTemplate;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.framework.util.log.LoggerFactory;


import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.GatewayAdapter;
import com.freshdirect.payment.PaylinxResourceException;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.impl.GatewayLogActivity;
import com.freshdirect.payment.gateway.impl.PaymentGatewayResponse;

public class PaymentechService extends AbstractService implements Gateway {

	private final static Category LOGGER = LoggerFactory
			.getInstance(PaymentechService.class);

	private static final String UPDATE_PROFILE_API ="/updateProfile";
	private static final String PROFILE_API = "/profiles";
	private static final String VERIFY_API ="/verify";
	private static final String AUTHORIZE_API ="/authorize";
	private static final String CAPTURE_API ="/capture";
	private static final String ISSUE_CASHBACK_API ="/issueCashback";
	private static final String REVERSE_AUTHORIZE_API ="/reverseAuthorize";
	private static final String VOID_CAPTURE_API ="/voidCapture";

	
	private static final String INVALID_REQUEST="Request is INVALID.";
	
	
	
	private static Gateway INSTANCE;


	public static Gateway getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PaymentechService();

		return INSTANCE;
	}



	@Override
	public GatewayType getType() {
		// TODO Auto-generated method stub
		return GatewayType.PAYMENTECH;
	}



	@Override
	public Response reverseAuthorize(Request request)
			throws ErpTransactionException {
		 LOGGER.info("reverseAuthorize Method Start ");
		 PaymentGatewayResponse  serviceResponse = null;
		  try {
			  String inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
			  serviceResponse =  getData(inputJson, getOrbitalEndPoint(REVERSE_AUTHORIZE_API), PaymentGatewayResponse.class);
		} catch (FDPayPalServiceException e) {
			 LOGGER.error("reverseAuthorize Method Exception  : "+e.getMessage());
				throw new ErpTransactionException(e);
		}
		  LOGGER.info("reverseAuthorize Method End ");
		  Response response = GatewayAdapter.getResponse(serviceResponse, request);
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}


	@Override
	public Response addProfile(Request request) throws ErpTransactionException {
		 LOGGER.info("addProfile Method Start ");
		PaymentGatewayResponse  serviceResponse = null;
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.ADD_PROFILE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		  try {
			String inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
			serviceResponse =  getData(inputJson, getOrbitalEndPoint(PROFILE_API), PaymentGatewayResponse.class);
			
		} catch (FDPayPalServiceException e) {
			 LOGGER.error("addProfile Method Exception: "+e.getMessage());
				throw new ErpTransactionException(e);
		}
		  LOGGER.info("addProfile Method End ");
		  Response response = GatewayAdapter.getResponse(serviceResponse, request);
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}


	@Override
	public Response updateProfile(Request request)
			throws ErpTransactionException {
		 LOGGER.info("updateProfile Method Start ");
		 PaymentGatewayResponse   serviceResponse = null;
		 if(request==null)throw new Error(INVALID_REQUEST);
			if(!TransactionType.UPDATE_PROFILE.equals(request.getTransactionType()))
				throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
			
		  try {
			  String inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
			   serviceResponse =  getData(inputJson, getOrbitalEndPoint(UPDATE_PROFILE_API), PaymentGatewayResponse.class);
		} catch (FDPayPalServiceException e) {
			 LOGGER.error("updateProfile Method Exception: "+e.getMessage());
				throw new ErpTransactionException(e);
		}
		  LOGGER.info("updateProfile Method Start ");
		  Response response = GatewayAdapter.getResponse(serviceResponse, request);
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}


	@Override
	public Response deleteProfile(Request request)
			throws ErpTransactionException {
		 LOGGER.info("deleteProfile Method Start ");
		 
		 if(request==null)throw new Error(INVALID_REQUEST);
			if(!TransactionType.DELETE_PROFILE.equals(request.getTransactionType()))
				throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
			
		 PaymentGatewayResponse  serviceResponse = null;
		 String profileId=null;
			if(request.getBillingInfo()!=null&&request.getBillingInfo().getPaymentMethod()!=null)
			profileId = request.getBillingInfo().getPaymentMethod().getBillingProfileID();
					 
				RestTemplate restTemplate = getRestTemplate();
				restTemplate.delete(getOrbitalEndPoint(PROFILE_API)+profileId);
			 
		  LOGGER.info("deleteProfile Method End ");
		  Response response = GatewayAdapter.getResponse(serviceResponse, request);
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}


	@Override
	public Response getProfile(Request request) throws ErpTransactionException {
		 LOGGER.info("getProfile Method Start ");
		 
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.GET_PROFILE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		
		String profileId=null;
		if(request.getBillingInfo()!=null&&request.getBillingInfo().getPaymentMethod()!=null)
		profileId = request.getBillingInfo().getPaymentMethod().getBillingProfileID();
		
		 PaymentGatewayResponse  serviceResponse = null;
		  try {
			  String inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
			  serviceResponse =  getData(inputJson, getOrbitalEndPoint(PROFILE_API)+"/"+profileId, PaymentGatewayResponse.class);
		} catch (FDPayPalServiceException e) {
			 LOGGER.info("getProfile Method Exception : "+e.getMessage());
				throw new ErpTransactionException(e);
		}
		  LOGGER.info("getProfile Method End ");
		  Response response = GatewayAdapter.getResponse(serviceResponse, request);
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return response;
		
	}


	@Override
	public boolean isValidToken(String token, String customerId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	


	@Override
	public ErpAuthorizationModel verify(String merchantId,
			ErpPaymentMethodI paymentMethod) throws ErpTransactionException {
		 LOGGER.info("Verify Method Start ");
		 ErpAuthorizationModel authModel = null;
		PaymentGatewayResponse  serviceResponse = null;
		Request request = GatewayAdapter.getVerifyRequest(merchantId,paymentMethod);
		
		if(request==null)throw new Error(INVALID_REQUEST);
		if(TransactionType.CC_VERIFY.equals(request.getTransactionType())||
			TransactionType.ACH_VERIFY.equals(request.getTransactionType())){
		
		  try {
			String inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
			serviceResponse =  getData(inputJson, getOrbitalEndPoint(VERIFY_API), PaymentGatewayResponse.class);

		
		  LOGGER.info("Verify Method End ");
		  Response response = GatewayAdapter.getResponse(serviceResponse, request);
		  authModel=GatewayAdapter.getVerifyResponse(response,paymentMethod.isBypassAVSCheck());
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		  	} catch (FDPayPalServiceException e) {
				 LOGGER.error("Verify Method Exception: "+e.getMessage());
					throw new ErpTransactionException(e);
			} catch(PaylinxResourceException pe){
				LOGGER.error("Verify Method Exception: "+pe.getMessage());
				throw new ErpTransactionException(pe);
			}catch(Exception e){
				LOGGER.error("Verify Method Exception: "+e.getMessage());
				throw new ErpTransactionException(e);
			}
			
		}else {
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		}
		return authModel;
		
	}



	@Override
	public ErpAuthorizationModel authorize(ErpPaymentMethodI paymentMethod,
			String orderNumber, double authorizationAmount, double tax,
			String merchantId) throws ErpTransactionException {
		 LOGGER.info("Authorize  Method Start ");
		 ErpAuthorizationModel authModel = null;
		PaymentGatewayResponse  serviceResponse = null;
		Response response=null;
		Request request = GatewayAdapter.getRequest(TransactionType.AUTHORIZE, paymentMethod, authorizationAmount, tax, orderNumber, merchantId);
		
		
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.AUTHORIZE.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		
		
		  try {
			String inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
			serviceResponse =  getData(inputJson, getOrbitalEndPoint(AUTHORIZE_API), PaymentGatewayResponse.class);
			response = GatewayAdapter.getResponse(serviceResponse, request);
			authModel = GatewayAdapter.getAuthResponse(response);
		  } catch (FDPayPalServiceException e) {
			 LOGGER.error("Authorize Method Exception: "+e.getMessage());
				throw new ErpTransactionException(e);
		}catch (PaylinxResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  LOGGER.info("Authorize  Method End ");
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return authModel;
		
	}



	@Override
	public ErpCaptureModel capture(ErpAuthorizationModel auth,
			ErpPaymentMethodI paymentMethod, double amount, double tax,
			String orderNumber) throws ErpTransactionException {
		 LOGGER.info("capture Method Start ");
		 ErpCaptureModel capture = null;
			if (!EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()))
				orderNumber = auth.getGatewayOrderID();
			
			if(StringUtils.isEmpty(auth.getGatewayOrderID()))
				auth.setGatewayOrderID(orderNumber);
			Request request=GatewayAdapter.getCaptureRequest(paymentMethod, auth, amount, tax); 
			
			if(request==null)throw new Error(INVALID_REQUEST);
			if(!TransactionType.CAPTURE.equals(request.getTransactionType()))
				throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
			
		Response response =null;
		PaymentGatewayResponse  serviceResponse = null;
		  try {
			String inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
			serviceResponse =  getData(inputJson, getOrbitalEndPoint(CAPTURE_API), PaymentGatewayResponse.class);
			response = GatewayAdapter.getResponse(serviceResponse, request); 
		  LOGGER.info("capture Method End");
			  if(!EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()) && !response.isRequestProcessed()){
					ErpAuthorizationModel newAuth = this.authorize(paymentMethod, orderNumber, amount, tax, auth.getMerchantId());
					request=GatewayAdapter.getCaptureRequest(paymentMethod, newAuth, amount, tax);
					inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
					serviceResponse =  getData(inputJson, getOrbitalEndPoint(CAPTURE_API), PaymentGatewayResponse.class);
					response = GatewayAdapter.getResponse(serviceResponse, request);
			  }
			  
			  capture = GatewayAdapter.getCaptureResponse(response,auth);
			} catch (FDPayPalServiceException e) {
				 LOGGER.error("capture Method Exception "+e.getMessage());
					throw new ErpTransactionException(e);
			} catch (PaylinxResourceException ex){
				 LOGGER.error("capture Method Exception "+ex.getMessage());
					throw new ErpTransactionException(ex);
			}
				
		
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return capture;
		
	}



	@Override
	public ErpVoidCaptureModel voidCapture(ErpPaymentMethodI paymentMethod,
			ErpCaptureModel capModel) throws ErpTransactionException {
		 LOGGER.info("voidCapture Method Start ");
		 PaymentGatewayResponse  serviceResponse = null;
		 Request request =GatewayAdapter.getVoidCaptureRequest(paymentMethod, capModel);
		 
		 if(request==null)throw new Error(INVALID_REQUEST);
			if(!TransactionType.VOID_CAPTURE.equals(request.getTransactionType()))
				throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
			
		  try {
			String inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
			serviceResponse =  getData(inputJson, getOrbitalEndPoint(VOID_CAPTURE_API), PaymentGatewayResponse.class);
		} catch (FDPayPalServiceException e) {
			 LOGGER.error("voidCapture Method Exception : "+e.getMessage());
				throw new ErpTransactionException(e);
		}
		  LOGGER.info("voidCapture Method End ");
		  Response response = GatewayAdapter.getResponse(serviceResponse, request); 
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return null;
		
	}



	@Override
	public ErpCashbackModel issueCashback(String orderNumber,
			ErpPaymentMethodI paymentMethod, double amount, double tax,
			ErpAffiliate affiliate) throws ErpTransactionException {
		 LOGGER.info("issueCashback Method Start ");
		 PaymentGatewayResponse  serviceResponse = null;
		 ErpCashbackModel cashback = null;
		 Request request =GatewayAdapter.getCashbackRequest(paymentMethod, amount, tax, orderNumber, affiliate.getMerchant(paymentMethod.getCardType()));
		 
		if(request==null)throw new Error(INVALID_REQUEST);
		if(!TransactionType.CASHBACK.equals(request.getTransactionType()))
			throw new Error("Transaction Type "+request.getTransactionType()+" is INVALID for this call.");
		
		  try {
			  String inputJson = buildRequest(GatewayAdapter.getPaymentGatewayRequest(request));
			  serviceResponse =  getData(inputJson, getOrbitalEndPoint(ISSUE_CASHBACK_API), PaymentGatewayResponse.class);
		} catch (FDPayPalServiceException e) {
			LOGGER.error("issueCashback Method Exception: "+e.getMessage());
				throw new ErpTransactionException(e);
		}
		  LOGGER.info("issueCashback Method End ");
		  Response response = GatewayAdapter.getResponse(serviceResponse, request); 
		  
		  	cashback= GatewayAdapter.getCashbackResponse(response, paymentMethod);
			cashback.setAmount(amount);
			cashback.setTax(tax);
			cashback.setAffiliate(affiliate);
			
		  GatewayLogActivity.logActivity(GatewayType.PAYMENTECH, response);
		return cashback;
		
	}
	
	
}
