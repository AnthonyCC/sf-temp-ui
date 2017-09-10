package com.freshdirect.mobileapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;
import com.freshdirect.fdstore.ewallet.EwalletUtil;
import com.freshdirect.fdstore.ewallet.PaymentData;
import com.freshdirect.fdstore.ewallet.PaymentMethodName;
import com.freshdirect.fdstore.ewallet.ValidationError;
import com.freshdirect.fdstore.ewallet.ValidationResult;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.EwalletPaymentMethod;
import com.freshdirect.mobileapi.controller.data.EwalletResponse;
import com.freshdirect.mobileapi.controller.data.request.EwalletRequest;
import com.freshdirect.mobileapi.controller.data.response.PaymentMethod;
import com.freshdirect.mobileapi.ewallet.EwalletMobileRequestProcessor;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletService {

	private static final org.apache.log4j.Category LOGGER = LoggerFactory.getInstance(EwalletService.class);
	private static String EWALLET_STATUS_ON="ON";
	private static String EWALLET_STATUS_OFF="OFF";
	private static String EWALLET_ERR_EMPTY_CART="ERR_EMPTY_CART";
	private static final String EXPRESSCHECKOUT_TRASCODE_EXP ="EXP";
	private static final String EXPRESSCHECKOUT_TRASCODE_PEX ="PEX";
	private static String EWALLET_FALSE="false";
	private static String EWALLET_TRUE="true";
	private static final String PARAM_REQUEST_TOKEN = "oauth_token";
	private static final String PARAM_OAUTH_VERIFIER = "oauth_verifier";
	private static final String PARAM_CHECKOUT_URL = "checkout_resource_url";
	private static final String PARAM_PP_PAYMENMETHOD_NONCE ="paymentMethodNonce";
	private static final String PARAM_FIRST_NAME ="firstName";
	private static final String PARAM_LAST_NAME ="lastName";
	private static final String PARAM_EMAILID ="email";
	private static final String WALLET_RESPONSE_STATUS = "status";
	private static final String WALLET_RESPONSE_SUCCESS = "success";
	private static final String WALLET_RESPONSE_FAIL = "fail";
	private static final String WALLET_RESPONSE_PARAM_MESSAGE = "message";
	private static final String WALLET_RESPONSE_CLIENT="Client";
	
	
	/**
	 * This method will check the Status of EWallet for Mobile App
	 * @param ewalletRequest
	 * @return
	 */
	public EwalletResponse getEwalletStatus(final EwalletRequest ewalletRequest){
		
		String walletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
		EwalletResponse eWalletStatusResult = new EwalletResponse(); 
		eWalletStatusResult.seteWalletStatus(walletStatus);
		eWalletStatusResult.setSuccessMessage("");
		
		return eWalletStatusResult;
	}

	/**
	 * Check the Status of Ewallet from Database
	 * @param ewalletType
	 * @return
	 */
	private String checkEWalletStatus(String ewalletType){
		boolean ewalletStatus =  false;
		if(EnumEwalletType.PP.getName().equalsIgnoreCase(ewalletType)){
			ewalletStatus = MobileApiProperties.isPayPalEnabled();
		}else if(EnumEwalletType.MP.getName().equalsIgnoreCase(ewalletType)){
			ewalletStatus = MobileApiProperties.isMasterpassEnabled();
		}
		if(ewalletStatus) {
			return EWALLET_STATUS_ON;
		}else {
			return EWALLET_STATUS_OFF;
		}
	}
	
	/**
	 * @param ewalletRequest
	 * @param user
	 * @param request
	 * @return
	 * @throws FDException
	 */
	public EwalletResponse standardCheckout(final EwalletRequest ewalletRequest, final SessionUser user,final HttpServletRequest request) throws FDException{
		
		EwalletResponse ewalletResponse = null;
		EwalletRequestData requestData = new EwalletRequestData();
		
		requestData.setAppBaseUrl(request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort());
		requestData.setContextPath(request.getContextPath());
		
		requestData.seteWalletType(ewalletRequest.geteWalletType());
		requestData.setMobileCallbackDomain(ewalletRequest.getCallBackUrl());
		FDSessionUser fdSessionUser =user.getFDSessionUser();
		// Prepare shopping cart for PostShopping Cart service call
		EwalletUtil.prepareShoppingCartItems(fdSessionUser,requestData, MobileApiProperties.getMediaPath());
		if(requestData.getShoppingCartItems()!=null && requestData.getShoppingCartItems().length() >0 ){
			 
			EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
			try{
				String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
				if(ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)){
					
					EwalletResponseData ewalletResponseData = mobileRequestProcessor.standardCheckout(requestData);
					
					// Error Handling - Check for Any error
					ValidationResult validationResult = ewalletResponseData.getValidationResult();
					if(validationResult != null && validationResult.getErrors() != null && !validationResult.getErrors().isEmpty()){
						ewalletResponse = prepareErrorResponse(validationResult);
						ewalletResponse.seteWalletStatus(ewalletStatus);
					}else{
						ewalletResponse = mapStdCheckoutResponseData(ewalletResponseData);
						// Update the Callback URL
						ewalletResponse.setCallbackUrl(ewalletRequest.getCallBackUrl());
						// Get the EWallet Status
						ewalletResponse.seteWalletStatus(ewalletStatus);
						ewalletResponse.setSuccessMessage("");
					}
				}else{
					ewalletResponse = new EwalletResponse();
					ewalletResponse.seteWalletStatus(ewalletStatus);
					ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF", ewalletRequest.geteWalletType()+" is disabled");
				}
				
			}catch(Exception exception){
				ewalletResponse = new EwalletResponse();
				ewalletResponse.addErrorMessage("Exception while calling Checkout Service for "+ewalletRequest.geteWalletType() +" EWallet Provider :", exception.getMessage());
				exception.printStackTrace();
				LOGGER.error("Error while calling Checkout Service", exception);
			}
		}else{
			ewalletResponse = new EwalletResponse();
			ewalletResponse.addErrorMessage(EWALLET_ERR_EMPTY_CART, "Shopping Cart is empty");
		}
		return ewalletResponse;
	}

	/**
	 * @param ewalletRequest
	 * @param user
	 * @param request
	 * @return
	 */
	public EwalletResponse standardCheckoutData(final EwalletRequest ewalletRequest,final SessionUser user,final HttpServletRequest request){
		EwalletResponse ewalletResponse = new EwalletResponse();
		try{
			String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
			if(ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)){
				EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
				EwalletRequestData requestData = createEwalletStdCheckoutRequestData(ewalletRequest,user.getFDSessionUser(),request);
				boolean isDebitCardSwitch = (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user.getFDSessionUser()));
				requestData.setDebitCardSwitch(isDebitCardSwitch);
				//  
				requestData.seteWalletType(ewalletRequest.geteWalletType());
				EwalletResponseData ewalletResponseData = mobileRequestProcessor.standardCheckoutData(requestData);
				
				if(ewalletResponseData.getPaymentMethod() != null) {
					ewalletResponse = mapCheckoutData(ewalletResponseData);
					ewalletResponse.seteWalletStatus(ewalletStatus);
				}
				ewalletResponse.setSuccessMessage("");
			}
			else{
				ewalletResponse = new EwalletResponse();
				ewalletResponse.seteWalletStatus(ewalletStatus);
				ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF", ewalletRequest.geteWalletType()+" is disabled");
			}
			ewalletResponse.seteWalletStatus(ewalletStatus);
		}catch(Exception exception){
			ewalletResponse = new EwalletResponse();
			ewalletResponse.addErrorMessage("Exception while calling CheckoutData Service for "+ewalletRequest.geteWalletType() +" EWallet Provider", exception.getMessage());
			exception.printStackTrace();
			LOGGER.error("Error while calling Checkout Data Service", exception);
		}
		return ewalletResponse;
	}

	/**
	 * @param ewalletRequest
	 * @return
	 */
	public EwalletResponse generateClientToken(final EwalletRequest ewalletRequest){
		EwalletResponse ewalletResponse = new EwalletResponse();
		String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
		try {
			if (ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)) {
				EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
				EwalletRequestData requestData = new EwalletRequestData();
				requestData.setCustomerId(ewalletRequest.getCustomerId());
				requestData.setEnumeWalletType(EnumEwalletType.getEnum(ewalletRequest.geteWalletType()));
				EwalletResponseData ewalletResponseData = mobileRequestProcessor.generateClientToken(requestData);
				
				// Error Handling - Check for Any error
				ValidationResult validationResult = ewalletResponseData.getValidationResult();
				if(validationResult != null && validationResult.getErrors() != null && !validationResult.getErrors().isEmpty()){
					ewalletResponse = prepareErrorResponse(validationResult);
					ewalletResponse.seteWalletStatus(ewalletStatus);
				}
				ewalletResponse.setTokenType(WALLET_RESPONSE_CLIENT);
				ewalletResponse.setRequestToken(ewalletResponseData.getToken());
			} else {
				ewalletResponse = new EwalletResponse();
				ewalletResponse.seteWalletStatus(ewalletStatus);
				ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF",
						ewalletRequest.geteWalletType() + " is disabled");
			}
		} catch (Exception exception) {
			ewalletResponse = new EwalletResponse();
			ewalletResponse.addErrorMessage(
					"Exception while getting the Client Token "
							+ ewalletRequest.geteWalletType()
							+ " EWallet Provider", exception.getMessage());
			exception.printStackTrace();
			LOGGER.error("Error while calling generateClientToken Service", exception);
		}
		ewalletResponse.seteWalletStatus(ewalletStatus);
		return ewalletResponse;
	}
	/**
	 * @param ewalletRequest
	 * @param user
	 * @return
	 */
	public EwalletResponse isPayPalWalletPaired(final EwalletRequest ewalletRequest,final SessionUser user){
		EwalletResponse ewalletResponse = new EwalletResponse();
		String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
		try {
			if (ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)) {
				boolean isPayPalPaired = false;
				List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(user.getFDSessionUser().getIdentity()).getPaymentMethods();
				if(paymentMethods != null){
					ErpPaymentMethodI paymentMethod = getEWalletPaymentMethod(paymentMethods,ewalletRequest.geteWalletType());
					
					if(paymentMethod != null){
						//Check for Valid Vault token
						boolean isValid = FDCustomerManager.isValidVaultToken(paymentMethod.getProfileID(), ewalletRequest.getCustomerId());
						if(isValid){
							com.freshdirect.mobileapi.model.PaymentMethod paymentMethodModel =com.freshdirect.mobileapi.model.PaymentMethod.wrap(paymentMethod);
							PaymentMethod resPaymentMethod = new PaymentMethod(paymentMethodModel,"");
							ewalletResponse.setPaymentMethod(resPaymentMethod);
							isPayPalPaired = true;
						}else{
							// If Paired PayPal account doesn't have valid Vault Token then delete the PayPal account/disconnect
							FDCustomerManager.removePaymentMethod(ewalletRequest.getFdActionInfo(), paymentMethod, FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user.getFDSessionUser()));
							// Delete the Vault token from CUST.CUST_EWALLET table as well.
							FDCustomerManager.deleteLongAccessToken(ewalletRequest.getCustomerId(), ""+EnumEwalletType.PP.getValue());
						}
					}
				}
				if(!isPayPalPaired){
					EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
					EwalletRequestData requestData = new EwalletRequestData();
					boolean isDebitCardSwitch = (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user.getFDSessionUser()));
					requestData.setDebitCardSwitch(isDebitCardSwitch);
					requestData.setEnumeWalletType(EnumEwalletType.getEnum(ewalletRequest.geteWalletType()));
					requestData.setCustomerId(user.getFDSessionUser().getIdentity().getErpCustomerPK());
					EwalletResponseData ewalletResponseData = mobileRequestProcessor.isPayPalPaired(requestData);
					// Error Handling - Check for Any error
					ValidationResult validationResult = ewalletResponseData.getValidationResult();
					if(validationResult != null && validationResult.getErrors() != null && !validationResult.getErrors().isEmpty()){
						ewalletResponse = prepareErrorResponse(validationResult);
						ewalletResponse.seteWalletStatus(ewalletStatus);
					}else{
						ErpPaymentMethodI paymentMethod = parsePaymentMethodForm(ewalletResponseData.getToken());
						com.freshdirect.mobileapi.model.PaymentMethod paymentMethodModel =com.freshdirect.mobileapi.model.PaymentMethod.wrap(paymentMethod);
						PaymentMethod resPaymentMethod = new PaymentMethod(paymentMethodModel,"");
						ewalletResponse.setPaymentMethod(resPaymentMethod);
					}
				}

			} else {
				ewalletResponse = new EwalletResponse();
				ewalletResponse.seteWalletStatus(ewalletStatus);
				ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF",
						ewalletRequest.geteWalletType() + " is disabled");
			}
		} catch (Exception exception) {
			ewalletResponse = new EwalletResponse();
			ewalletResponse.addErrorMessage(
					"Exception while checking PayPal whether wallet is paired or not "
							+ ewalletRequest.geteWalletType()
							+ " EWallet Provider", exception.getMessage());
			exception.printStackTrace();
			LOGGER.error("Error while calling IsPayPalWalletPaired Service", exception);
		}
		ewalletResponse.seteWalletStatus(ewalletStatus);
		return ewalletResponse;
	}
	
	/**
	 * This method used to disconnect the Wallet from User for the given Wallet Type.
	 * @param ewalletRequest
	 * @param user
	 * @return
	 */
	public EwalletResponse disconnectWallet(final EwalletRequest ewalletRequest){
		EwalletResponse ewalletResponse = new EwalletResponse();
		String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
		try {
			if (ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)) {
				EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
				EwalletRequestData requestData = new EwalletRequestData();
				requestData.setFdActionInfo(ewalletRequest.getFdActionInfo());
				requestData.setEnumeWalletType(EnumEwalletType.getEnum(ewalletRequest.geteWalletType()));
				requestData.setCustomerId(ewalletRequest.getCustomerId());
				requestData.setDebitCardSwitch(ewalletRequest.getFdActionInfo().isDebitCardSwitch());
				
				EwalletResponseData ewalletResponseData = mobileRequestProcessor.disconnectWallet(requestData);
				Map<String, String> result = ewalletResponseData.getResParam();
				if(result!= null){
					if(result.containsKey(WALLET_RESPONSE_STATUS) && result.get(WALLET_RESPONSE_STATUS).equals(WALLET_RESPONSE_SUCCESS) ){
						ewalletResponse.setStatus(WALLET_RESPONSE_SUCCESS);
					}else{
						ewalletResponse.setStatus(WALLET_RESPONSE_FAIL);
					}
					ewalletResponse.setSuccessMessage(result.get(WALLET_RESPONSE_PARAM_MESSAGE));
				}
			} else {
				ewalletResponse = new EwalletResponse();
				ewalletResponse.seteWalletStatus(ewalletStatus);
				ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF",
						ewalletRequest.geteWalletType() + " is disabled");
			}
		} catch (Exception exception) {
			ewalletResponse = new EwalletResponse();
			ewalletResponse.addErrorMessage(
					"Exception while disconnecting the PayPal account "
							+ ewalletRequest.geteWalletType()
							+ " EWallet Provider", exception.getMessage());
			exception.printStackTrace();
			LOGGER.error("Error while calling disconnect Paypal Account", exception);
		}
		ewalletResponse.seteWalletStatus(ewalletStatus);
		return ewalletResponse;
	}
	
	/**
	 * @param ewalletRequest
	 * @param isDebitCardSwitch 
	 * @return
	 */
	public EwalletResponse addPayPalWallet(final EwalletRequest ewalletRequest, boolean isDebitCardSwitch){
		EwalletResponse ewalletResponse = new EwalletResponse();
		String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
		try {
			if (ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)) {
				EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
				EwalletRequestData requestData = new EwalletRequestData();
				requestData.setEnumeWalletType(EnumEwalletType.getEnum(ewalletRequest.geteWalletType()));
				
				Map<String,String> reqParams = new HashMap<String, String>();
				reqParams.put(PARAM_PP_PAYMENMETHOD_NONCE, ewalletRequest.getTokenValue());
				reqParams.put(PARAM_FIRST_NAME, ewalletRequest.getFirstName());
				reqParams.put(PARAM_LAST_NAME, ewalletRequest.getLastName());
				reqParams.put(PARAM_EMAILID, ewalletRequest.getEmailId());
				requestData.setReqParams(reqParams);
				requestData.setCustomerId(ewalletRequest.getCustomerId());
				requestData.setFdActionInfo(ewalletRequest.getFdActionInfo());
				requestData.setDebitCardSwitch(isDebitCardSwitch);
				EwalletResponseData ewalletResponseData = mobileRequestProcessor.addPayPalWallet(requestData);
				ValidationResult validationResult = ewalletResponseData.getValidationResult();
				if(validationResult != null && validationResult.getErrors() != null && !validationResult.getErrors().isEmpty()){
					ewalletResponse = prepareErrorResponse(validationResult);
					ewalletResponse.seteWalletStatus(ewalletStatus);
				}else{
					ErpPaymentMethodI paymentMethod = ewalletResponseData.getPaymentMethod();
					if(paymentMethod != null){
						com.freshdirect.mobileapi.model.PaymentMethod paymentMethodModel =com.freshdirect.mobileapi.model.PaymentMethod.wrap(paymentMethod);
						PaymentMethod resPaymentMethod = new PaymentMethod(paymentMethodModel,"");
						ewalletResponse.setPaymentMethod(resPaymentMethod);
					}
				}
			} else {
				ewalletResponse = new EwalletResponse();
				ewalletResponse.seteWalletStatus(ewalletStatus);
				ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF",
						ewalletRequest.geteWalletType() + " is disabled");
			}
		} catch (Exception exception) {
			ewalletResponse = new EwalletResponse();
			ewalletResponse.addErrorMessage(
					"Exception while aading PayPal account for "
							+ ewalletRequest.geteWalletType()
							+ " EWallet Provider", exception.getMessage());
			exception.printStackTrace();
			LOGGER.error("Error while calling Add PayPal account Service", exception);
		}
		ewalletResponse.seteWalletStatus(ewalletStatus);
		return ewalletResponse;
		
	}
	
	private EwalletResponse prepareErrorResponse(ValidationResult validationResult){
		EwalletResponse ewalletResponse = new EwalletResponse();
		if(validationResult != null && validationResult.getErrors() !=null && !validationResult.getErrors().isEmpty()){
			List<ValidationError> resValidErrors = validationResult.getErrors();
			Map<String , String > errors = new HashMap<String , String>();
			for( ValidationError error : resValidErrors){
				errors.put(error.getName(),error.getError());
			}
			ewalletResponse.setErrors(errors);
		}
		return  ewalletResponse;
	}
	
	/**
	 * @param ewalletRequest
	 * @return
	 * @throws FDException 
	 */
	public EwalletResponse checkout(final EwalletRequest ewalletRequest, final SessionUser user,final HttpServletRequest request) throws FDException{
		
		EwalletResponse ewalletResponse = null;
		EwalletRequestData requestData = new EwalletRequestData();
		
		requestData.setAppBaseUrl(request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort());
		requestData.setContextPath(request.getContextPath());
		
		requestData.seteWalletType(ewalletRequest.geteWalletType());
		requestData.setMobileCallbackDomain(ewalletRequest.getCallBackUrl());
		requestData.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user.getFDSessionUser()));
		FDSessionUser fdSessionUser =user.getFDSessionUser();
		// Prepare shopping cart for PostShopping Cart service call
		EwalletUtil.prepareShoppingCartItems(fdSessionUser,requestData, MobileApiProperties.getMediaPath());
		if(requestData.getShoppingCartItems()!=null && requestData.getShoppingCartItems().length() >0 ){
			 
			EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
			try{
				String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
				if(ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)){
					
					EwalletResponseData ewalletResponseData = mobileRequestProcessor.checkout(requestData);
					
					ewalletResponse = mapCheckoutResponseData(ewalletResponseData);
					// Update the Callback URL
					ewalletResponse.setCallbackUrl(ewalletRequest.getCallBackUrl());
					// Get the EWallet Status
					ewalletResponse.seteWalletStatus(ewalletStatus);
					ewalletResponse.setSuccessMessage("");
				}else{
					ewalletResponse = new EwalletResponse();
					ewalletResponse.seteWalletStatus(ewalletStatus);
					ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF", ewalletRequest.geteWalletType()+" is disabled");
				}
				
			}catch(Exception exception){
				ewalletResponse = new EwalletResponse();
				ewalletResponse.addErrorMessage("Exception while calling Checkout Service for "+ewalletRequest.geteWalletType() +" EWallet Provider :", exception.getMessage());
				exception.printStackTrace();
				LOGGER.error("Error while calling Checkout Service", exception);
			}
		}else{
			ewalletResponse = new EwalletResponse();
			ewalletResponse.addErrorMessage(EWALLET_ERR_EMPTY_CART, "Shopping Cart is empty");
		}
		return ewalletResponse;
	}
	
	
	/**
	 * @param ewalletRequest
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public EwalletResponse checkoutData(final EwalletRequest ewalletRequest,final SessionUser user,final HttpServletRequest request){
		EwalletResponse ewalletResponse = new EwalletResponse();
		try{
			String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
			if(ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)){
				EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
				EwalletRequestData requestData = createEwalletRequestData(ewalletRequest,user.getFDSessionUser(),request);
				requestData.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user.getFDSessionUser()));
				//  
				requestData.seteWalletType(ewalletRequest.geteWalletType());
				EwalletResponseData ewalletResponseData = mobileRequestProcessor.checkoutData(requestData);
				
				if(ewalletResponseData.getPaymentMethod() != null) {
					ewalletResponse = mapCheckoutData(ewalletResponseData);
					ewalletResponse.seteWalletStatus(ewalletStatus);
				}
				ewalletResponse.setSuccessMessage("");
			}
			else{
				ewalletResponse = new EwalletResponse();
				ewalletResponse.seteWalletStatus(ewalletStatus);
				ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF", ewalletRequest.geteWalletType()+" is disabled");
			}
			ewalletResponse.seteWalletStatus(ewalletStatus);
		}catch(Exception exception){
			ewalletResponse = new EwalletResponse();
			ewalletResponse.addErrorMessage("Exception while calling CheckoutData Service for "+ewalletRequest.geteWalletType() +" EWallet Provider", exception.getMessage());
			exception.printStackTrace();
			LOGGER.error("Error while calling Checkout Data Service", exception);
		}
		return ewalletResponse;
	}
	
	
	/**
	 * @param ewalletRequest
	 * @return
	 * @throws FDResourceException 
	 */
	public EwalletResponse preCheckout(final EwalletRequest ewalletRequest,final SessionUser user) throws FDResourceException{
	
		EwalletResponse ewalletResponse = null;
		String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
		if(ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)){
			EwalletRequestData requestData = createPreCheckoutEwalletRequestData(ewalletRequest,user.getFDSessionUser());
			requestData.seteWalletType(ewalletRequest.geteWalletType());
			requestData.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user.getFDSessionUser()));
			EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
			try{
				EwalletResponseData ewalletResponseData = mobileRequestProcessor.preCheckoutData(requestData);
				ewalletResponse = mapPreCheckoutResponseData(ewalletResponseData);
				ewalletResponse.seteWalletStatus(ewalletStatus);
				ewalletResponse.setPreCheckoutTxnId(ewalletResponseData.getPreCheckoutTnxId());
				ewalletResponse.setSuccessMessage("");
			}catch(Exception exception){
				ewalletResponse = new EwalletResponse();
				ewalletResponse.addErrorMessage("Exception while calling PreCheckout Service for "+ewalletRequest.geteWalletType() +" EWallet Provider", exception.getMessage());
				exception.printStackTrace();
				LOGGER.error("Error while calling Checkout Service", exception);
			}
			
		}else{
			ewalletResponse = new EwalletResponse();
			ewalletResponse.seteWalletStatus(ewalletStatus);
			ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF", ewalletRequest.geteWalletType()+" is disabled");
		}
		return ewalletResponse;
	}
	
	/**
	 * @param ewalletRequest
	 * @param user
	 * @return
	 * @throws FDResourceException
	 */
	public EwalletResponse expressCheckout(final EwalletRequest ewalletRequest,SessionUser user, HttpServletRequest request) throws FDResourceException{
	
		EwalletResponse ewalletResponse = null;
		String ewalletStatus = checkEWalletStatus(ewalletRequest.geteWalletType());
		if(ewalletStatus.equalsIgnoreCase(EWALLET_STATUS_ON)){
			
			EwalletRequestData requestData = createExpCheckoutEwalletReqData(ewalletRequest,user.getFDSessionUser(),request);
			
			requestData.seteWalletType(ewalletRequest.geteWalletType());
			requestData.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user.getFDSessionUser()));
			EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
			try{
				EwalletResponseData ewalletResponseData = null;
				if (ewalletRequest.getTransCode().equals(EXPRESSCHECKOUT_TRASCODE_EXP)) {
					ewalletResponseData = mobileRequestProcessor.expressCheckoutWithoutPrecheckout(requestData);
				}else if(ewalletRequest.getTransCode().equals(EXPRESSCHECKOUT_TRASCODE_PEX)){
					// 
					List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(user.getFDSessionUser().getIdentity()).getPaymentMethods();
					if(paymentMethods != null){
						ErpPaymentMethodI paymentMethod = getEWalletPaymentMethod(paymentMethods,ewalletRequest.geteWalletType());
						PaymentData paymentData  = createPaymentData(paymentMethod);
						requestData.setPaymentData(paymentData);
						ewalletResponseData = mobileRequestProcessor.expressCheckout(requestData);
					}
				}
				ewalletResponse = mapExpCheckoutResponseData(ewalletResponseData);
				ewalletResponse.seteWalletStatus(ewalletStatus);
			}catch(Exception exception){
				ewalletResponse = new EwalletResponse();
				ewalletResponse.addErrorMessage("Exception while calling PreCheckout Service for "+ewalletRequest.geteWalletType() +" EWallet Provider", exception.getMessage());
				exception.printStackTrace();
				LOGGER.error("Error while calling Checkout Service", exception);
			}
			
		}else{
			ewalletResponse = new EwalletResponse();
			ewalletResponse.seteWalletStatus(ewalletStatus);
			ewalletResponse.addErrorMessage("EWALLET_STATUS_OFF", ewalletRequest.geteWalletType()+" is disabled");
		}
		return ewalletResponse;
	}
	
	/**
	 * @param payment
	 * @return
	 */
	private PaymentData createPaymentData(final ErpPaymentMethodI payment) {
        PaymentData paymentData = new PaymentData();
        paymentData.setId(payment.getPK().getId());
        paymentData.setTitle(payment.getName());
        if (payment.getCardType() != null) {
            paymentData.setType(payment.getCardType().getDisplayName());
        }
        paymentData.setNameOnCard(payment.getName());
        String maskedAccountNumber = payment.getMaskedAccountNumber();
        if (maskedAccountNumber != null)
        {
            if (8 < maskedAccountNumber.length()) {
                maskedAccountNumber = maskedAccountNumber.substring(maskedAccountNumber.length() - 8);
            }
            paymentData.setAccountNumber(maskedAccountNumber);
        }
        paymentData.setBestNumber(payment.getBestNumberForBillingInquiries());
        if (payment.getExpirationDate() != null) {
            paymentData.setExpiration(DateUtil.getCreditCardExpiryDate(payment.getExpirationDate()));
        }
        	
	        paymentData.setAddress1(payment.getAddress1());
	        paymentData.setAddress2(payment.getAddress2());
	        paymentData.setApartment(payment.getApartment());
	        paymentData.setCity(payment.getCity());
	        paymentData.setZip(payment.getZipCode());
	        paymentData.setState(payment.getState());
	        paymentData.setAbaRouteNumber(payment.getAbaRouteNumber());
       
        if (payment.getBankAccountType() != null) {
            paymentData.setBankAccountType(payment.getBankAccountType().getDescription());
        }
        paymentData.setBankName(payment.getBankName());
        
        paymentData.seteWalletID(payment.geteWalletID());
        paymentData.setVendorEWalletID(payment.getVendorEWalletID());
        return paymentData;
    }
	/**
	 * @param paymentMethods
	 * @param ewalletType
	 * @return
	 */
	private ErpPaymentMethodI getEWalletPaymentMethod(final List<ErpPaymentMethodI> paymentMethods, final String ewalletType){
		EnumEwalletType enumEwalletType = EnumEwalletType.getEnum(ewalletType);
		if(paymentMethods != null && !paymentMethods.isEmpty()){
			for(ErpPaymentMethodI paymentMethod : paymentMethods){
				if(paymentMethod.geteWalletID() != null && paymentMethod.geteWalletID().equals(""+enumEwalletType.getValue())){
					return paymentMethod;
				}
			}
		}
		return null;
	}
	

	/**
	 * @param ewalletResponseData
	 * @return
	 */
	private EwalletResponse mapExpCheckoutResponseData(EwalletResponseData ewalletResponseData){
		EwalletResponse ewalletResponse = new EwalletResponse();
		if(ewalletResponseData.getTransactionId() != null && ewalletResponseData.getPaymentMethod() != null){
			ewalletResponse.setCheckoutTransactionId(ewalletResponseData.getTransactionId());
			com.freshdirect.mobileapi.model.PaymentMethod paymentMethod =com.freshdirect.mobileapi.model.PaymentMethod.wrap(ewalletResponseData.getPaymentMethod());
			PaymentMethod resPaymentMethod = new PaymentMethod(paymentMethod);
			ewalletResponse.setPaymentMethod(resPaymentMethod);
			ewalletResponse.setSuccessMessage("");
		}
		return ewalletResponse;
		
	}
	/**
	 * @param ewalletResponseData
	 * @return
	 */
	private EwalletResponse mapPreCheckoutResponseData(EwalletResponseData ewalletResponseData){
		EwalletResponse ewalletResponse = new EwalletResponse();
		List<EwalletPaymentMethod> walletPaymentMethods = new ArrayList<EwalletPaymentMethod>();
		
		if(ewalletResponseData != null && ewalletResponseData.getPaymentDatas()!=null && !ewalletResponseData.getPaymentDatas().isEmpty()){
			for(PaymentData paymentData : ewalletResponseData.getPaymentDatas()){
				EwalletPaymentMethod ewalletPaymentMethod = new EwalletPaymentMethod();
				ewalletPaymentMethod.setCardId(paymentData.getId());
				ewalletPaymentMethod.setCardType(paymentData.getType());
				
					ewalletPaymentMethod.setMaskedAccountNumber("XXXXXXXX"+paymentData.getAccountNumber());
					
				ewalletPaymentMethod.setName(paymentData.getNameOnCard());
				if(paymentData.getExpiration()!=null){
					String expiration = paymentData.getExpiration();
					ewalletPaymentMethod.setExpirationMonth(expiration.split("/")[0]);
					ewalletPaymentMethod.setExpirationYear(expiration.split("/")[1]);
				}
				ewalletPaymentMethod.setPreferredCard(EWALLET_FALSE);
				if(ewalletResponseData.getPreferredMPCard().equals(paymentData.getId())){
					ewalletPaymentMethod.setPreferredCard(EWALLET_TRUE);
				}
				walletPaymentMethods.add(ewalletPaymentMethod);
			}
		}
		ewalletResponse.setPaymentMethods(walletPaymentMethods);
		return ewalletResponse;
	}
	
	/**
	 * @param ewalletRequest
	 * @param user
	 * @return
	 * @throws FDResourceException
	 */
	private EwalletRequestData createExpCheckoutEwalletReqData(final EwalletRequest ewalletRequest,final FDUserI user, final HttpServletRequest request) throws FDResourceException{
		EwalletRequestData ewalletRequestData = new EwalletRequestData();
		ewalletRequestData.seteWalletType(ewalletRequest.geteWalletType());
		ewalletRequestData.setCustomerId(user.getFDCustomer().getErpCustomerPK());
		ewalletRequestData.setMobileCallbackDomain(ewalletRequest.getOriginUrl());
		FDActionInfo fdActionInfo = AccountActivityUtil
				.getActionInfo(request.getSession());
		ewalletRequestData.setFdActionInfo(fdActionInfo);
		ewalletRequestData.setPaymentechEnabled(user.isPaymentechEnabled());
		ewalletRequestData.setCustomerId(user.getFDCustomer().getErpCustomerPK());
		ewalletRequestData.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user));
		if(ewalletRequest.getTransCode().equals(EXPRESSCHECKOUT_TRASCODE_EXP)){
			ewalletRequestData.setPrecheckoutTransactionId(ewalletRequest.getPrecheckoutTransactionId());
			ewalletRequestData.setPrecheckoutCardId(ewalletRequest.getEwalletCardId());
		}
		
		return ewalletRequestData;
	}
	
	/**
	 * @param ewalletRequest
	 * @param user
	 * @return
	 * @throws FDResourceException
	 */
	private EwalletRequestData createPreCheckoutEwalletRequestData(final EwalletRequest ewalletRequest,final FDUserI user) throws FDResourceException{
		EwalletRequestData ewalletRequestData = new EwalletRequestData();
		ewalletRequestData.seteWalletType(ewalletRequest.geteWalletType());
		ewalletRequestData.setCustomerId(user.getFDCustomer().getErpCustomerPK());
		ewalletRequestData.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user));
		
		return ewalletRequestData;
	}
	
	/**
	 * @param ewalletResponseData
	 * @return
	 */
	private EwalletResponse mapCheckoutData(EwalletResponseData ewalletResponseData){
		EwalletResponse ewalletResponse = new EwalletResponse();
		com.freshdirect.mobileapi.model.PaymentMethod paymentMethod =com.freshdirect.mobileapi.model.PaymentMethod.wrap(ewalletResponseData.getPaymentMethod());
		
		PaymentMethod resPaymentMethod = new PaymentMethod(paymentMethod);
		ewalletResponse.setPaymentMethod(resPaymentMethod);
		ewalletResponse.setCheckoutTransactionId(ewalletResponseData.getTransactionId());
		
		return ewalletResponse;
	}
	/**
	 * @param ewalletRequest
	 * @return
	 * @throws FDResourceException 
	 */
	private EwalletRequestData createEwalletRequestData(final EwalletRequest ewalletRequest,final FDUserI user, final HttpServletRequest request) throws FDResourceException{
		EwalletRequestData ewalletRequestData = new EwalletRequestData();
		ewalletRequestData.setPairingToken(ewalletRequest.getPairingToken());
		ewalletRequestData.setPairingVerifier(ewalletRequest.getPairingVerifer());
		Map<String,String> params = new HashMap<String,String>();
		params.put(PARAM_REQUEST_TOKEN, ewalletRequest.getOauthToken());
		params.put(PARAM_OAUTH_VERIFIER, ewalletRequest.getOauthVerifer());
		params.put(PARAM_CHECKOUT_URL, ewalletRequest.getCheckoutUrl());
		ewalletRequestData.setReqParams(params);
		ewalletRequestData.setPaymentechEnabled(user.isPaymentechEnabled());
		
		FDActionInfo fdActionInfo = AccountActivityUtil
				.getActionInfo(request.getSession());
		ewalletRequestData.setFdActionInfo(fdActionInfo);
		ewalletRequestData.setCustomerId(user.getFDCustomer().getErpCustomerPK());
		ewalletRequestData.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user));
	
		return ewalletRequestData;
	}
	
	/**
	 * @param ewalletResponseData
	 * @return
	 */
	private EwalletResponse mapStdCheckoutResponseData(final EwalletResponseData ewalletResponseData){
		EwalletResponse ewalletResponse = new EwalletResponse();
		ewalletResponse.setAllowedCardTypes(ewalletResponseData.getAllowedPaymentMethodTypes().split(","));
		ewalletResponse.setCallbackUrl(ewalletResponseData.getCallbackUrl());
		ewalletResponse.setRequestToken(ewalletResponseData.getToken());
		ewalletResponse.setExpressCheckout(EWALLET_FALSE);
		ewalletResponse.setReqDatatype(ewalletResponseData.getReqDatatype());
		ewalletResponse.setMerchantCheckoutId(ewalletResponseData.geteWalletIdentifier());
		ewalletResponse.setSuppressShippingEnable(ewalletResponseData.getSuppressShippingEnable());
		ewalletResponse.setLoyaltyEnabled(ewalletResponseData.getLoyaltyEnabled());
		ewalletResponse.setRequestBasicCkt(ewalletResponseData.getRequestBasicCkt());
		ewalletResponse.setVersion(ewalletResponseData.getVersion());
		return ewalletResponse;
	}
	
	/**
	 * @param ewalletRequest
	 * @param user
	 * @param request
	 * @return
	 * @throws FDResourceException
	 */
	private EwalletRequestData createEwalletStdCheckoutRequestData(final EwalletRequest ewalletRequest,final FDUserI user, final HttpServletRequest request) throws FDResourceException{
		EwalletRequestData ewalletRequestData = new EwalletRequestData();
		Map<String,String> params = new HashMap<String,String>();
		params.put(PARAM_REQUEST_TOKEN, ewalletRequest.getOauthToken());
		params.put(PARAM_OAUTH_VERIFIER, ewalletRequest.getOauthVerifer());
		params.put(PARAM_CHECKOUT_URL, ewalletRequest.getCheckoutUrl());
		ewalletRequestData.setReqParams(params);
		ewalletRequestData.setPaymentechEnabled(user.isPaymentechEnabled());
		FDActionInfo fdActionInfo = AccountActivityUtil.getActionInfo(request.getSession());
		ewalletRequestData.setFdActionInfo(fdActionInfo);
		ewalletRequestData.setCustomerId(user.getFDCustomer().getErpCustomerPK());
		ewalletRequestData.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user));
	
		return ewalletRequestData;
	}
	/**
	 * @param ewalletResponseData
	 * @return
	 */
	private EwalletResponse mapCheckoutResponseData(final EwalletResponseData ewalletResponseData){
		
		EwalletResponse ewalletResponse = new EwalletResponse();
		ewalletResponse.setAllowedCardTypes(ewalletResponseData.getAllowedPaymentMethodTypes().split(","));
		ewalletResponse.setCallbackUrl(ewalletResponseData.getCallbackUrl());
		ewalletResponse.setRequestToken(ewalletResponseData.getToken());
		ewalletResponse.setParingToken(ewalletResponseData.getPairingToken());
		ewalletResponse.setExpressCheckout(ewalletResponseData.geteWalletExpressCheckout());
		ewalletResponse.setReqDatatype(ewalletResponseData.getReqDatatype());
		ewalletResponse.setMerchantCheckoutId(ewalletResponseData.geteWalletIdentifier());
		ewalletResponse.setSuppressShippingEnable(ewalletResponseData.getSuppressShippingEnable());
		ewalletResponse.setLoyaltyEnabled(ewalletResponseData.getLoyaltyEnabled());
		ewalletResponse.setRequestBasicCkt(ewalletResponseData.getRequestBasicCkt());
		ewalletResponse.setVersion(ewalletResponseData.getVersion());
		ewalletResponse.setReqPairing(ewalletResponseData.getPairingRequest());
		return ewalletResponse;
	}

	/**
	 * @param tokenValue
	 * @return
	 */
	private ErpPaymentMethodI parsePaymentMethodForm(String tokenValue) {
        EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(PaymentMethodName.PAYMENT_METHOD_TYPE);
        ErpPaymentMethodI paymentMethod = null;
        
        paymentMethod = PaymentManager.createInstance(paymentMethodType);
        paymentMethod.setProfileID(tokenValue);
        paymentMethod.seteWalletID("2");
        return paymentMethod;
    }
	
	 static String scrubAccountNumber(String number){
	        StringBuffer digitsOnly = new StringBuffer();
	        for (int i=0; i<number.length(); i++) {
	            char c = number.charAt(i);
	            if (Character.isDigit(c)) {
	                digitsOnly.append(c);
	            }
	        }
	        return digitsOnly.toString();
	    }
}

