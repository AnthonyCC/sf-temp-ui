package com.freshdirect.mobileapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;
import com.freshdirect.fdstore.ewallet.PaymentData;
import com.freshdirect.fdstore.ewallet.ValidationError;
import com.freshdirect.fdstore.ewallet.ValidationResult;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.EwalletPaymentMethod;
import com.freshdirect.mobileapi.controller.data.EwalletResponse;
import com.freshdirect.mobileapi.controller.data.request.EwalletRequest;
import com.freshdirect.mobileapi.controller.data.response.PaymentMethod;
import com.freshdirect.mobileapi.ewallet.EwalletMobileRequestProcessor;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.MobileApiProperties;
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
		boolean ewalletStatus =  FDCustomerManager.getEwalletMobileStatusByType(ewalletType);
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
		prepareShoppingCartItems(fdSessionUser,requestData);
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
		FDSessionUser fdSessionUser =user.getFDSessionUser();
		// Prepare shopping cart for PostShopping Cart service call
		prepareShoppingCartItems(fdSessionUser,requestData);
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
			EwalletMobileRequestProcessor mobileRequestProcessor = new EwalletMobileRequestProcessor();
			try{
				EwalletResponseData ewalletResponseData = null;
				if (ewalletRequest.getTransCode().equals(EXPRESSCHECKOUT_TRASCODE_EXP)) {
					ewalletResponseData = mobileRequestProcessor.expressCheckoutWithoutPrecheckout(requestData);
				}else if(ewalletRequest.getTransCode().equals(EXPRESSCHECKOUT_TRASCODE_PEX)){
					// 
					List<ErpPaymentMethodI> paymentMethods = FDCustomerFactory.getErpCustomer(user.getFDSessionUser().getIdentity()).getPaymentMethods();
					if(paymentMethods != null){
						ErpPaymentMethodI paymentMethod = getEWalletPaymentMethoh(paymentMethods,ewalletRequest.geteWalletType());
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
        if (maskedAccountNumber != null) {
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
	private ErpPaymentMethodI getEWalletPaymentMethoh(final List<ErpPaymentMethodI> paymentMethods, final String ewalletType){
		
		if(paymentMethods != null && !paymentMethods.isEmpty()){
			for(ErpPaymentMethodI paymentMethod : paymentMethods){
				if(paymentMethod.geteWalletID() != null && paymentMethod.geteWalletID().equals("1")){
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
	 * Method prepares the ShoppingCart XML 
	 * @param user
	 */
	private void prepareShoppingCartItems(FDUserI user,EwalletRequestData ewalletRequestData) {
		FDCartModel fdCart = user.getShoppingCart();
		if(fdCart!=null){
			List<FDCartLineI> fdCartLines = fdCart.getOrderLines();
			if(fdCartLines != null && fdCartLines.size()>0){
				double totalPrice = 0;
				// Will store all the Cart Items
				StringBuffer cartItems = new StringBuffer();
				String subTotalTag = "";
				for (FDCartLineI cartLine : fdCartLines) {
					StringBuffer cartItem = new StringBuffer();
					ProductModel productNode = cartLine.lookupProduct();
		
					String productDesc = StringEscapeUtils.escapeXml(cartLine
							.getDescription());
		
					if (cartLine.getConfigurationDesc() != null
							&& !cartLine.getConfigurationDesc().isEmpty()) {
		
						StringEscapeUtils.escapeXml(productDesc + " ("
								+ cartLine.getConfigurationDesc() + ")");
					}
					if (productDesc != null && productDesc.length() >= 80) {
						productDesc = productDesc.substring(0, 80) + "...";
					}
					productDesc = productDesc + "(" + cartLine.getUnitPrice() + ")";
		
					String saleUnitCode = cartLine.getConfiguration().getSalesUnit();
		
					FDSalesUnit[] salesUnits = cartLine.lookupFDProduct().getSalesUnits();
		
					String saleUnitDescr = "";
					for (FDSalesUnit saleUnit : salesUnits) {
						if (saleUnit.getName().equals(saleUnitCode)) {
							saleUnitDescr = saleUnit.getDescription();
							break;
						}
					}
					String quantity = "0";
					try {
						if (saleUnitCode.equalsIgnoreCase("EA")
								|| saleUnitCode.equalsIgnoreCase("CS") || saleUnitCode.equalsIgnoreCase("BNC")) {
							quantity = ""
									+ (new Double(cartLine.getQuantity()).longValue());
						} else {
							String qtyStr = "" + cartLine.getQuantity();
							String fractionPart = qtyStr
									.substring(qtyStr.indexOf(".") + 1);
							if (fractionPart != null && fractionPart.length() > 0) {
								if (Integer.parseInt(fractionPart) > 0) {
									productDesc = productDesc + " (Qty: " + qtyStr
											+ ")";
									quantity = "0";
								} else {
									if (saleUnitDescr != null
											&& saleUnitDescr.length() > 0) {
										productDesc = productDesc + " (Qty: "
												+ saleUnitDescr + ")";
										quantity = "0";
									} else {
										quantity = ""
												+ (new Double(cartLine.getQuantity())
														.longValue());
									}
		
								}
							}
						}
					} catch (Exception e) {
						LOGGER.error("Error while creating Shopping Cart for EWallet service provider",e);
					}
					
					if(productDesc != null && productDesc.length() > 99 ){
						productDesc = productDesc.substring(0, 94) + "...";
					}
					String prodDes = StringEscapeUtils.escapeXml(productDesc);
					if(null != prodDes && prodDes.length() > 99){				
						productDesc = prodDes.substring(0, 94) + "...";
					}
					cartItem.append("<ShoppingCartItem>");
					cartItem.append("<Description>"+productDesc+ "</Description>");
					cartItem.append("<Quantity>" + quantity + "</Quantity>");
					cartItem.append("<Value>"
							+ (new Double(cartLine.getPrice() * 100)).longValue()
							+ "</Value>");
					cartItem.append("<ImageURL>" + MobileApiProperties.getMediaPath()
							+ StringEscapeUtils.escapeXml(productNode.getProdImage().getPathWithPublishId())
							+ "</ImageURL>");
					cartItem.append("</ShoppingCartItem>");
					cartItems.append(cartItem);
					totalPrice += cartLine.getPrice();
				}
				subTotalTag = "<Subtotal>" + new Double(totalPrice * 100).longValue()
						+ "</Subtotal>";
				ewalletRequestData.setShoppingCartItems(subTotalTag+ cartItems.toString());
			}
		}
	}
}

