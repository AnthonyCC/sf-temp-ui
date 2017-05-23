package com.freshdirect.payment.gateway.impl;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.NotFoundException;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType; 
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.GatewayAdapter;
import com.freshdirect.payment.PayPalAccountModel;
import com.freshdirect.payment.PayPalCaptureResponse;
import com.freshdirect.payment.PayPalResponse;
import com.freshdirect.payment.PaylinxResourceException;
import com.freshdirect.payment.TransactionModel;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.payment.gateway.Gateway;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.TransactionType;
import com.freshdirect.payment.gateway.ewallet.impl.EWalletLogActivity;
import com.freshdirect.payment.service.FDPayPalService;

/**
 * @author Aniwesh Vatsal
 *
 */
public class PayPal implements Gateway {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6093295523348808195L;
	
	private static final String PAYPAL_VERIFY_VAULT_TOKEN_TXN="VerifyVaultToken";
	private static final String PAYPAL_TXN_SUCCESS="SUCCESS";
	private static final String PAYPAL_TXN_FAIL="FAIL";
	private final static Category LOGGER = LoggerFactory.getInstance( PayPal.class );

	
	@Override
	public GatewayType getType() {
		return GatewayType.PAYPAL;
		
	}
	
	@Override
	public ErpAuthorizationModel verify(String merchantId,
			ErpPaymentMethodI paymentMethod) throws ErpTransactionException {
		return null;
	}

	@Override
	public ErpAuthorizationModel authorize(ErpPaymentMethodI paymentMethod,
			String orderNumber, double authorizationAmount, double tax,
			String merchantId) throws ErpTransactionException {
		ErpAuthorizationModel authModel = null;
		ResponseImpl response = null;
		
		Merchant merchant=getMerchant(merchantId);
		String merchantEmailID = PayPalConstants.MerchantID.get(merchant).getValue();
		Request gatewayRequest = GatewayAdapter.getRequest(TransactionType.AUTHORIZE ,paymentMethod, authorizationAmount, tax, orderNumber, merchantId);
		try {
			// 1. Validate the Vault Token
			boolean isVaultTokenValid = isValidToken(paymentMethod.getProfileID(), paymentMethod.getCustomerId());
			if(isVaultTokenValid){
				authModel = null;
				

				PayPalResponse payPalResponse = FDPayPalService.getInstance().authorize(paymentMethod,orderNumber, authorizationAmount, tax, merchantId, merchantEmailID);

				
				if (payPalResponse!= null && payPalResponse.getStatus().equalsIgnoreCase(com.freshdirect.payment.Result.STATUS_SUCCESS)) {
					authModel = GatewayAdapter.getPPAuthResponse(payPalResponse,paymentMethod);
					if(authModel != null){
						authModel.setMerchantId(merchantId);
						authModel.setGatewayOrderID(orderNumber);
						authModel.setAmount(authorizationAmount);
						authModel.setTax(tax);
					}
					gatewayRequest.getBillingInfo().setTransactionRef(authModel.getSequenceNumber());
					gatewayRequest.getBillingInfo().getPaymentMethod().setType(PaymentMethodType.PP);
					response = new ResponseImpl(gatewayRequest);
					response.setEwalletId(paymentMethod.geteWalletID());
					response.setEwalletTxId(authModel.getEwalletTxId());
					response.setRequestProcessed(true);
					response.setApproved(true);
					response.setAVSMatch(true);
					response.setResponseCode(payPalResponse.getTransactionModel().getProcessorResponseCode());
					response.setStatusCode(EnumPaymentResponse.APPROVED.getName());
					response.setStatusMessage(EnumPaymentResponse.APPROVED.getDescription());
					response.setDeclined(false);
					GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
				} else {
					authModel = new ErpAuthorizationModel();
					authModel.setTransactionSource(EnumTransactionSource.SYSTEM);
					authModel.setProfileID(paymentMethod.getProfileID());
					authModel.setResponseCode(EnumPaymentResponse.DECLINED);
					authModel.setCustomerId(paymentMethod.getCustomerId());
					authModel.setMerchantId(merchantId);
					authModel.setGatewayOrderID(orderNumber);
					authModel.setAmount(authorizationAmount);
					authModel.setTax(tax);
					authModel.setCardType(EnumCardType.PAYPAL);
					authModel.setPaymentMethodType(EnumPaymentMethodType.PAYPAL);
					gatewayRequest.getBillingInfo().getPaymentMethod().setType(PaymentMethodType.PP);
					response = new ResponseImpl(gatewayRequest);
					response.setEwalletId(paymentMethod.geteWalletID());
					response.setRequestProcessed(false);
//					response.setApproved(false);
					response.setAVSMatch(true);
					response.setDeclined(true);
					response.setStatusCode(EnumPaymentResponse.DECLINED.getCode());
					response.setStatusMessage(EnumPaymentResponse.DECLINED.getDescription());
					if(payPalResponse != null && payPalResponse.getTransactionModel() != null){
						authModel.setDescription(payPalResponse.getTransactionModel().getProcessorResponseText());
						authModel.setEwalletTxId(payPalResponse.getTransactionModel().getId());
						response.setResponseCode(payPalResponse.getTransactionModel().getProcessorResponseCode());
						response.setStatusMessage(payPalResponse.getTransactionModel().getProcessorResponseText());
						response.setEwalletTxId(payPalResponse.getTransactionModel().getId());
					}
					GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
				}
			}else{
				authModel = new ErpAuthorizationModel();
				response = new ResponseImpl(gatewayRequest);
				String errDesc = "Paypal is unable to process your payment at this time.";
				getEewalletFailResponse(authModel, response, paymentMethod,
						orderNumber, authorizationAmount, tax,
						merchantId, errDesc);
				GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
			}
		} catch(AuthenticationException authenticationException){
			authModel = new ErpAuthorizationModel();
			response = new ResponseImpl(gatewayRequest);
			String errDesc = "Cannot Connect to PayPal at this time.";
			getEewalletFailResponse(authModel, response, paymentMethod,
					orderNumber, authorizationAmount, tax,
					merchantId, errDesc);
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
		}catch (Exception e) {
			authModel = new ErpAuthorizationModel();
			response = new ResponseImpl(gatewayRequest);
			String errDesc = "Paypal is unable to process your payment at this time.";
			getEewalletFailResponse(authModel, response, paymentMethod,
					orderNumber, authorizationAmount, tax,
					merchantId, errDesc);
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
			
		}
		return authModel;
	}
	
	private void getEewalletFailResponse(ErpAuthorizationModel authModel, ResponseImpl response, ErpPaymentMethodI paymentMethod,
			String orderNumber, double authorizationAmount, double tax,
			String merchantId, String errDesc) {
	
		authModel.setTransactionSource(EnumTransactionSource.SYSTEM);
		authModel.setResponseCode(EnumPaymentResponse.ERROR);
		authModel.setProfileID(paymentMethod.getProfileID());
		authModel.setDescription(EnumPaymentResponse.ERROR.getDescription()+"."+errDesc);
		authModel.setCustomerId(paymentMethod.getCustomerId());
		authModel.setMerchantId(merchantId);
		authModel.setGatewayOrderID(orderNumber);
		authModel.setAmount(authorizationAmount);
		authModel.setTax(tax);
		
		response.getRequest().getBillingInfo().getPaymentMethod().setType(PaymentMethodType.PP);
		response.setEwalletId(paymentMethod.geteWalletID());
		response.setRequestProcessed(false);
		response.setApproved(false);
		response.setAVSMatch(false);
		response.setStatusCode(EnumPaymentResponse.ERROR.getName());
		response.setStatusMessage(EnumPaymentResponse.ERROR.getDescription()+"."+errDesc);
		response.setError(true);
	}

	private static Merchant getMerchant(String merchantID) {
		
		if(StringUtils.isEmpty(merchantID))
			return Merchant.FRESHDIRECT;
		else if (Merchant.FRESHDIRECT.name().equalsIgnoreCase(merchantID))
			return Merchant.FRESHDIRECT;
		else if (Merchant.FDW.name().equalsIgnoreCase(merchantID))
			return Merchant.FDW;
		else if (Merchant.FDX.name().equalsIgnoreCase(merchantID))
			return Merchant.FDX;
		else 
			return Merchant.USQ;
	}
	
	@Override
	public ErpCaptureModel capture(ErpAuthorizationModel authorization,
			ErpPaymentMethodI paymentMethod, double amount, double tax,
			String saleId) throws ErpTransactionException {
		

		String txId = authorization.getEwalletTxId();

		PayPalResponse payPalResponse = FDPayPalService.getInstance().capture(authorization, paymentMethod, amount, tax, saleId);

		
		saleId = authorization.getGatewayOrderID();
		if (StringUtils.isEmpty(saleId)) {
			authorization.setGatewayOrderID(saleId);
		}
		//Gateway logging and return object preparation
		Request request = GatewayAdapter.getCaptureRequest(paymentMethod, authorization, amount, tax);
		request.getBillingInfo().getPaymentMethod().setType(PaymentMethodType.PP);
		ResponseImpl response = new ResponseImpl(request);
		response.setBillingInfo(request.getBillingInfo());
		
		ErpCaptureModel captureModel = new ErpCaptureModel();
		if (payPalResponse!= null && payPalResponse.getStatus().equalsIgnoreCase(com.freshdirect.payment.Result.STATUS_SUCCESS)) {
			try {
				setSuccessResponse(response, payPalResponse);
				captureModel = GatewayAdapter.getCaptureResponse(response, authorization);
				captureModel.setCardType(EnumCardType.PAYPAL);
				captureModel.setEwalletTxId(payPalResponse.getTransactionModel().getId());
				captureModel.setProfileID(authorization.getProfileID());
				captureModel.setPaymentMethodType(EnumPaymentMethodType.PAYPAL);
			} catch (PaylinxResourceException e ) {
				setFailureResponse(response, payPalResponse);
				GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
				throw new ErpTransactionException(e.getMessage());
			}
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
		} else {
			// START APPDEV-5490
			setFailureResponse(response, payPalResponse);
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);

			 if(PayPalCaptureResponse.Processor.SETTLEMENT_DECLINED.getResponseCode().equals(response.getStatusCode())
					 || PayPalCaptureResponse.Processor.RISK_REJECTED.getResponseCode().equals(response.getStatusCode())){
				 captureModel.setCardType(EnumCardType.PAYPAL);
				 captureModel.setSettlementResponseCode(response.getStatusCode()); 

			 }else{
					throw new ErpTransactionException(payPalResponse.getMessage());
			 }
			 //END APPDEV-5490
		}
		
		return captureModel;
	}

	public Response voidCapture(Request request) throws ErpTransactionException {
		String txId = request.getBillingInfo().getEwalletTxId();
		if (txId == null || txId.isEmpty()) {
			throw new ErpTransactionException("Transaction Id is empty or null while reverse auth of PayPal order ");
		}
		

		PayPalResponse payPalResponse = FDPayPalService.getInstance().voidCapture(txId);
						
		request.getBillingInfo().getPaymentMethod().setType(PaymentMethodType.PP);
		ResponseImpl response = new ResponseImpl(request);
		response.setBillingInfo(request.getBillingInfo());
		if (payPalResponse!= null && payPalResponse.getStatus().equalsIgnoreCase(com.freshdirect.payment.Result.STATUS_SUCCESS)) {
			setSuccessResponse(response, payPalResponse);
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
		} else {
			setFailureResponse(response, payPalResponse);
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
			throw new ErpTransactionException("PayPal error " + payPalResponse.getMessage());
		}
		
		return response;
	}
	
	@Override
	public ErpVoidCaptureModel voidCapture(ErpPaymentMethodI paymentMethod,
			ErpCaptureModel request) throws ErpTransactionException {
		throw new FDRuntimeException("PayPal - this interface method is not applicable at this time");
	}

	@Override
	public Response reverseAuthorize(Request request)
			throws ErpTransactionException {
		String txId = request.getBillingInfo().getEwalletTxId();
		if (txId == null || txId.isEmpty()) {
			throw new ErpTransactionException("Transaction Id is empty or null while reverse auth of PayPal order ");
		}
		

		PayPalResponse payPalResponse = FDPayPalService.getInstance().reverseAuthorise(txId);
		request.getBillingInfo().getPaymentMethod().setType(PaymentMethodType.PP);
		ResponseImpl response = new ResponseImpl(request);
		response.setBillingInfo(request.getBillingInfo());
		if (payPalResponse!= null && payPalResponse.getStatus().equalsIgnoreCase(com.freshdirect.payment.Result.STATUS_SUCCESS)) {
			setSuccessResponse(response, payPalResponse);
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
		} else {
			setFailureResponse(response, payPalResponse);
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
			//throw new ErpTransactionException("PayPal error " + payPalResponse.getMessage());
		}
		
		return response;
	}

	@Override
	public ErpCashbackModel issueCashback(String orderNumber,
			ErpPaymentMethodI paymentMethod, double amount, double tax,
			ErpAffiliate affiliate) throws ErpTransactionException {
		String txId = paymentMethod.geteWalletTrxnId();
		if (txId == null || txId.isEmpty()) {
			throw new ErpTransactionException("Transaction Id is empty or null while issue cashback of PayPal order " + orderNumber);
		}

		PayPalResponse payPalResponse = FDPayPalService.getInstance().issueCashback(orderNumber, paymentMethod, amount, tax);
		ErpCashbackModel cashback = null;
		
		Request request = GatewayAdapter.getCashbackRequest(paymentMethod, amount, tax, 
								orderNumber, affiliate.getMerchant(paymentMethod.getCardType()));
		request.getBillingInfo().getPaymentMethod().setType(PaymentMethodType.PP);
		ResponseImpl response = new ResponseImpl(request);
		response.setBillingInfo(request.getBillingInfo());
		if (payPalResponse!= null && payPalResponse.getStatus().equalsIgnoreCase(com.freshdirect.payment.Result.STATUS_SUCCESS)) {
			setSuccessResponse(response, payPalResponse);
			response.getBillingInfo().setTransactionRef(payPalResponse.getTransactionModel().getCaptureId());
			cashback= GatewayAdapter.getCashbackResponse(response, paymentMethod);
			cashback.setCustomerId(paymentMethod.getCustomerId());
			cashback.setAmount(amount);
			cashback.setTax(tax);
			cashback.setAffiliate(affiliate);
			cashback.setCardType(EnumCardType.PAYPAL);
			cashback.setPaymentMethodType(EnumPaymentMethodType.PAYPAL);
			cashback.setProfileID(txId);
			cashback.setGatewayOrderID(payPalResponse.getTransactionModel().getOrderId());
			cashback.setEwallet_tx_id(payPalResponse.getTransactionModel().getId());
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
		} else {
			setFailureResponse(response, payPalResponse);
			GatewayLogActivity.logActivity(GatewayType.PAYPAL, response);
			throw new ErpTransactionException("PayPal error " + payPalResponse.getMessage());
		}

		return cashback;
	}

	@Override
	public Response addProfile(Request request) throws ErpTransactionException {
		// Not Applicable for PayPal
		return null;
	}

	@Override
	public Response updateProfile(Request request)
			throws ErpTransactionException {
		// Not Applicable for PayPal
		return null;
	}

	@Override
	public Response deleteProfile(Request request)
			throws ErpTransactionException {
		// Not Applicable for PayPal
		return null;
	}

	@Override
	public Response getProfile(Request request) throws ErpTransactionException {
		// Not Applicable for PayPal
		return null;
	}

	@Override
	public boolean isValidToken(String token, String customerId){
		try{

			PayPalResponse payPalResponse = FDPayPalService.getInstance().findToken(token, customerId);
			PayPalAccountModel paypalAccount=payPalResponse.getPayPalAccountModel();
			if(paypalAccount != null && paypalAccount.getToken() != null){
				logPPEwalletRequestResponse(PAYPAL_VERIFY_VAULT_TOKEN_TXN, PAYPAL_TXN_SUCCESS, customerId);
				return true;
			}else{
				logPPEwalletRequestResponse(PAYPAL_VERIFY_VAULT_TOKEN_TXN, PAYPAL_TXN_FAIL, customerId);
				return false;
			}
		}catch(NotFoundException exception){
			exception.printStackTrace();
			return false;
		}catch(AuthenticationException authenticationException){// When not able to interact with PayPal
			return true;
		}catch(FDPayPalServiceException exception){
			exception.printStackTrace();
			return false;
		}
	}

	
	
	/**
	 * @param trxType
	 * @param txnStatus
	 * @param customerId
	 */
	private void logPPEwalletRequestResponse(String trxType, String txnStatus, String customerId) {
		try{
			EwalletActivityLogModel eWalletLogModel = new EwalletActivityLogModel();
			
	        eWalletLogModel.seteWalletID(""+EnumEwalletType.PP.getValue());
	       	eWalletLogModel.setCustomerId(customerId);
	       	
	        StringBuffer eWalletTxnRequest= new StringBuffer(); 
	        StringBuffer eWalletTxnResponse= new StringBuffer();
	        
	        if(trxType.equalsIgnoreCase(PAYPAL_VERIFY_VAULT_TOKEN_TXN)){
	        	eWalletTxnRequest.append("Verify vault token");
	        	// Create Response String
	        	eWalletTxnResponse= new StringBuffer();
	        	
	        	if(txnStatus != null && txnStatus.equals(PAYPAL_TXN_SUCCESS)){
	        		eWalletTxnResponse.append("Vault token is Valid");
	        	}else{
	        		eWalletTxnResponse.append("Vault token is InValid");
	        	}
	        }
	        eWalletLogModel.setRequest(eWalletTxnRequest.toString());
	        eWalletLogModel.setResponse(eWalletTxnResponse.toString());
	        
	        eWalletLogModel.setTransactionType(trxType);
	        Timestamp timeNow = new Timestamp(Calendar.getInstance().getTimeInMillis()); 
	        eWalletLogModel.setCreationTimeStamp(timeNow);
	        eWalletLogModel.setStatus(txnStatus);
	        eWalletLogModel.setOrderId("");
	        EWalletLogActivity.logActivity(eWalletLogModel);
	        
			}catch(Exception e){	// Any exception then ignore
				e.printStackTrace();
		}
	}
	
	private void setSuccessResponse(ResponseImpl response, PayPalResponse payPalResponse) {
		TransactionModel result = payPalResponse.getTransactionModel();
		response.setResponseCode(result.getProcessorResponseCode());
		response.setStatusMessage(result.getMessage());
		response.setApproved(true);
		response.setDeclined(false);
		response.setError(false);
		response.setEwalletId("" + EnumEwalletType.PP.getValue());
		response.setEwalletTxId(result.getId());
		response.setRequestProcessed(true);
		
		response.getBillingInfo().getPaymentMethod().setType(PaymentMethodType.PP);
		response.getBillingInfo().setTransactionRef(result.getAuthorizationId());
		// Added settlement processor status code as part of APPDEV-5490
		if(null==response.getStatusCode() && null!=result.getProcessorSettlementResponseCode()){
			response.setStatusCode(result.getProcessorSettlementResponseCode());

		}
		
	}
	
	private void setFailureResponse(ResponseImpl response, PayPalResponse payPalResponse) {
		TransactionModel result = null;
		if(null != payPalResponse)
		result = payPalResponse.getTransactionModel();
		response.setError(true);
		response.setApproved(false);
		response.setRequestProcessed(true);
//		Transaction trxn = result.getTransaction();
		
		
		// Added settlement processor status code as part of APPDEV-5490
		if(result!=null){
			response.setStatusMessage(result.getMessage());
			response.setStatusCode(result.getProcessorSettlementResponseCode());
			response.setResponseCode(result.getProcessorResponseCode());
		}

		response.setEwalletId("" + EnumEwalletType.PP.getValue());
		if(result != null ){
			response.setEwalletTxId(result.getId());
			response.getBillingInfo().setTransactionRef(result.getAuthorizationId());
		}
		response.getBillingInfo().getPaymentMethod().setType(PaymentMethodType.PP);
	}

}
