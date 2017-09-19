package com.freshdirect.webapp.ajax.expresscheckout.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ewallet.EwalletConstants;
import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;
import com.freshdirect.fdstore.ewallet.EwalletUtil;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.ewallet.EwalletRequestProcessor;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class EwalletPaymentServlet extends BaseJsonServlet {

	private final static Category LOGGER = LoggerFactory
			.getInstance(EwalletPaymentServlet.class);

	private static final long serialVersionUID = -6000498208914127773L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {
		convertParamsToJSON(request);
		process(request, response, user);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {
		process(request, response, user);
	}

	/**
	 * @param request
	 * @param response
	 * @param user
	 * @throws HttpErrorResponse
	 */
	private void process(HttpServletRequest request,
			HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {
		try {
			
			LOGGER.debug("Inside EwalletPaymentServlet - process()");
			// Create EWallet Request Object
			EwalletRequestData ewalletRequestData = new EwalletRequestData();
			createEwalletRequestData(ewalletRequestData, request, response,user);
			
			ewalletRequestData.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user));
			
			// Get parameters from request object
			getRequestParameter(request, ewalletRequestData);

			// Create Ewallet Response Object
			EwalletResponseData ewalletResponseData = null;
			final FormDataRequest requestData = parseRequestData(request, FormDataRequest.class);
			
			// Read all the params from Request
			checkRequestParams(requestData,ewalletRequestData);
			
			PageAction pageAction = getPageAction(request, ewalletRequestData,
					requestData);

			boolean isPaypalDisconnected =false;
			if (pageAction != null) {
				switch (pageAction) {
				/* uncomment for Masrterpass Express Checkout
				 * 
				 * case MASTERPASS_START_PAIRING: {
					ewalletRequestData
							.seteWalletAction(EWALLET_MP_PAIRING_START);
					// Populate Shopping Cart Items
					prepareShoppingCartItems(user, ewalletRequestData);
					break;
				}
				case MASTERPASS_END_PAIRING: {
					ewalletRequestData.seteWalletAction(EWALLET_MP_PAIRING_END);
					break;
				}
				case MASTERPASS_EXPRESS_CHECKOUT: {
					ewalletRequestData.seteWalletAction(EWALLET_MP_EXPRESS_CHECKOUT);
					break;
				}
				case MASTERPASS_WALLET_ALL_PAYMETHOD_IN_EWALLET: {
    				ewalletRequestData.seteWalletAction(EWALLET_MP_GET_ALL_PAYMETHOD_IN_EWALLET);
    				break;
    			}
				case MASTERPASS_WALLET_ALL_PAYMETHOD_IN_EWALLET_MYACCOUNT: {
    				ewalletRequestData.seteWalletAction(EWALLET_MP_GET_ALL_PAYMETHOD_IN_EWALLET);
    				break;
    			}
    			case MASTERPASS_WALLET_CONNECT_START: {
    				ewalletRequestData.seteWalletAction(EWALLET_MP_CONNECT_START);
    				break;
    			}
    			case MASTERPASS_WALLET_CONNECT_END: {
    				ewalletRequestData.seteWalletAction(EWALLET_MP_CONNECT_END);
    				break;
    			}
    			case MASTERPASS_WALLET_DISCONNECT: {
    				ewalletRequestData.seteWalletAction(EWALLET_MP_DISCONNECT_EWALLET);
    				break;
    			}*/
    			case MASTERPASS_STANDARD_CHECKOUT: {
    				ewalletRequestData.seteWalletAction(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT);
    				EwalletUtil.prepareShoppingCartItems(user, ewalletRequestData, ewalletRequestData.getAppBaseUrl()
    						+ ewalletRequestData.getContextPath());
    				break;
    			}
    			case MASTERPASS_STANDARD_CHECKOUT_DATA: {
    				ewalletRequestData.seteWalletAction(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT_DATA);
    				// Check for Error response from EWallet Vendor
    				if( checkWalletErrorResponse(ewalletRequestData,response)){
    					response.sendRedirect("/expressco/checkout.jsp");
    				}
    				break;
    			}
    			case PAYPAL_START_PAIRING: {
    				ewalletRequestData.seteWalletAction(EwalletConstants.EWALLET_PP_START_PAIRING);
    				break;
    			}
    			case PAYPAL_END_PAIRING: {
    				ewalletRequestData.seteWalletAction(EwalletConstants.EWALLET_PP_END_PAIRING);
    				break;
    			}
    			case PAYPAL_START_CONNECTING: {
    				ewalletRequestData.seteWalletAction(EwalletConstants.EWALLET_PP_START_CONNECTING);
    				break;
    			}
    			
    			case PAYPAL_END_CONNECTING: {
    				ewalletRequestData.seteWalletAction(EwalletConstants.EWALLET_PP_END_CONNECTING);
    				break;
    			}
    			case PAYPAL_WALLET_DISCONNECT: {
    				ewalletRequestData.seteWalletAction(EwalletConstants.EWALLET_PP_DISCONNECT);
    					isPaypalDisconnected = true;
    				break;
    			}
    			case GET_PP_DEVICE_DATA: {
    				ewalletRequestData.seteWalletAction(EwalletConstants.GET_PP_DEVICE_DATA);
    				break;
    			}
				default:
					break;
				}
			}
			
			// Get the process to process the request
			EwalletRequestProcessor processor = new EwalletRequestProcessor();
			ewalletResponseData = processor.processRequest(ewalletRequestData);

			if(ewalletRequestData.geteWalletAction() != null && ewalletRequestData.geteWalletAction().equals(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT_DATA)){
				postStandardCheckoutData(ewalletResponseData, request, response, user);
			}
			
			if(ewalletRequestData.geteWalletAction() != null && ewalletRequestData.geteWalletAction().equals(EwalletConstants.EWALLET_PP_END_PAIRING)){
				user.getShoppingCart().setPaymentMethod(ewalletResponseData.getPaymentMethod());
				request.getSession().removeAttribute(EwalletConstants.EWALLET_SESSION_ATTRIBUTE_NAME);
			}
			if(isPaypalDisconnected){
				user.getShoppingCart().setPaymentMethod(null);
			}
			
			ValidationResult validationResult = convertValidationResult(ewalletResponseData,request,ewalletRequestData);
			
			final FormDataResponse eWalletSubmitResponse = FormDataService.defaultService().prepareFormDataResponse(requestData,validationResult);
			Map<String, Object> eWalletResponseMap = new HashMap<String, Object>();
			if(eWalletSubmitResponse.getSubmitForm().isSuccess()){
				eWalletResponseMap.put("eWalletResponseData", ewalletResponseData);
				request.getSession().removeAttribute(EwalletConstants.EWALLET_ERROR_CODE);
			}else{
				if(ewalletRequestData.geteWalletAction() != null && ewalletRequestData.geteWalletAction().equals(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT_DATA)){
					response.sendRedirect("/expressco/checkout.jsp");
				}else{
					request.getSession().removeAttribute(EwalletConstants.EWALLET_ERROR_CODE);
				}
				
			}
			
			// Submit the response
			eWalletSubmitResponse.getSubmitForm().setSuccess(eWalletSubmitResponse.getValidationResult().getErrors().isEmpty());
			 
			eWalletSubmitResponse.getSubmitForm().setResult(eWalletResponseMap);
			writeResponseData(response, eWalletSubmitResponse);
			
			LOGGER.debug("Exit EwalletPaymentServlet - process()");
		} catch (final Exception e) {
			returnHttpError(500, "Error while submit EWallet response to user "+ user.getUserId(), e);
		}
	}

	/**
	 * @param requestData
	 * @param ewalletRequestData
	 */
	private void checkRequestParams(FormDataRequest requestData, EwalletRequestData ewalletRequestData){
		
		if(requestData.getFormData().containsKey(EwalletConstants.PARAM_PP_PAYMENMETHOD_NONCE)){
			ewalletRequestData.getReqParams().put(EwalletConstants.PARAM_PP_PAYMENMETHOD_NONCE, (String)requestData.getFormData().get(EwalletConstants.PARAM_PP_PAYMENMETHOD_NONCE));
		}
		if(requestData.getFormData().containsKey(EwalletConstants.PARAM_FIRST_NAME)){
			ewalletRequestData.getReqParams().put(EwalletConstants.PARAM_FIRST_NAME, (String)requestData.getFormData().get(EwalletConstants.PARAM_FIRST_NAME));
		}
		if(requestData.getFormData().containsKey(EwalletConstants.PARAM_LAST_NAME)){
			ewalletRequestData.getReqParams().put(EwalletConstants.PARAM_LAST_NAME, (String)requestData.getFormData().get(EwalletConstants.PARAM_LAST_NAME));
		}
		if(requestData.getFormData().containsKey(EwalletConstants.PARAM_EMAILID)){
			ewalletRequestData.getReqParams().put(EwalletConstants.PARAM_EMAILID, (String)requestData.getFormData().get(EwalletConstants.PARAM_EMAILID));
		}
		if(requestData.getFormData().containsKey(EwalletConstants.PARAM_ORIGIN)){
			ewalletRequestData.getReqParams().put(EwalletConstants.PARAM_ORIGIN, (String)requestData.getFormData().get(EwalletConstants.PARAM_ORIGIN));
		}
		if(requestData.getFormData().containsKey(EwalletConstants.PARAM_DEVICEID)){
			ewalletRequestData.getReqParams().put(EwalletConstants.PARAM_DEVICEID, (String)requestData.getFormData().get(EwalletConstants.PARAM_DEVICEID));
		}
		if(requestData.getFormData().containsKey(EwalletConstants.EWALLET_TYPE)){
			ewalletRequestData.seteWalletType((String)requestData.getFormData().get(EwalletConstants.EWALLET_TYPE));
		}
	}
	/**
	 * User can click on "Cancel button on Masterpass Light UI before or after login to Wallet
	 * @param ewalletRequestData
	 * @param response
	 * @return
	 */
	private boolean checkWalletErrorResponse(EwalletRequestData ewalletRequestData,HttpServletResponse response){
		boolean retVal = false;
		if(ewalletRequestData.getReqParams() != null && ewalletRequestData.getReqParams().containsKey(EwalletConstants.MP_EWALLET_RESPONSE_STATUS)){
			String responseStatus = ewalletRequestData.getReqParams().get(EwalletConstants.MP_EWALLET_RESPONSE_STATUS);
			if(responseStatus.equals(EwalletConstants.MASTERPASS_CANCEL_STATUS) || responseStatus.equals(EwalletConstants.MASTERPASS_FAILURE_STATUS)){
				retVal =  true;
			}else{
				ewalletRequestData.seteWalletAction(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT_DATA);
				retVal =  false;
			}
		}
		return retVal;
	}
	/**
	 * @param ewalletResponseData
	 * @return
	 */
	private ValidationResult convertValidationResult(EwalletResponseData ewalletResponseData, HttpServletRequest request, EwalletRequestData ewalletRequestData){
		ValidationResult validationResult = new ValidationResult();
		com.freshdirect.fdstore.ewallet.ValidationResult validresult = null;
		String errorMsg = "";
		if(ewalletResponseData != null ){
			validresult = ewalletResponseData.getValidationResult();
		}
		// don't do validation for GET_PP_DEVICE_DATA action
		if(ewalletRequestData != null && !EwalletConstants.GET_PP_DEVICE_DATA.equals(ewalletRequestData.geteWalletAction())){
			if(validresult != null && validresult.getErrors() !=null && !validresult.getErrors().isEmpty()){
				List<ValidationError> resValidErrors = new ArrayList<ValidationError>();
				for(com.freshdirect.fdstore.ewallet.ValidationError error : validresult.getErrors()){
					ValidationError respError= new ValidationError();
					respError.setName(error.getName());
					respError.setError(error.getError());
					resValidErrors.add(respError);
					errorMsg = error.getName();
				}
				if(ewalletRequestData.geteWalletAction() != null && ewalletRequestData.geteWalletAction().equals(EwalletConstants.EWALLET_MP_STANDARD_CHECKOUT_DATA)){
					request.getSession().setAttribute(EwalletConstants.EWALLET_ERROR_CODE, errorMsg);
				}
				validationResult.setErrors(resValidErrors);
			}
		}
		
		return  validationResult;
	}
	/**
	 * @param ewalletResponseData
	 * @param request
	 * @param response
	 * @param user
	 * @throws IOException
	 * @throws ServletException
	 */
	private void postStandardCheckoutData(EwalletResponseData ewalletResponseData, HttpServletRequest request, HttpServletResponse response, FDUserI user) throws IOException, ServletException{
		if(ewalletResponseData != null && ewalletResponseData.getValidationResult()!=null){
			if(ewalletResponseData.getValidationResult().getErrors()!=null && !ewalletResponseData.getValidationResult().getErrors().isEmpty()){
				return;
			}
		}else{
			request.getSession().setAttribute(EwalletConstants.MASTERPASS_TRANSACTIONID, ewalletResponseData.getTransactionId());
			request.setAttribute(EwalletConstants.MASTERPASS_REQ_ATTR_ACTION_COMPLETED, EwalletConstants.MP_REQ_ATTR_ACTION_COMPLETED_VALUE);
			if (ewalletResponseData.getPaymentMethod() != null && ewalletResponseData.getPaymentMethod().getPK() != null)
				request.getSession().setAttribute("WALLET_CARD_ID",""+ewalletResponseData.getPaymentMethod().getPK().getId());
			user.getShoppingCart().setPaymentMethod(ewalletResponseData.getPaymentMethod());
			request.getRequestDispatcher(ewalletResponseData.getRedirectUrl()).forward(request, response);
		}
	}
	
	
	/**
	 * @param request
	 * @param ewalletRequestData
	 * @param requestData
	 * @return
	 */
	private PageAction getPageAction(HttpServletRequest request,
			EwalletRequestData ewalletRequestData,
			final FormDataRequest requestData) {
		PageAction pageAction = FormDataService.defaultService().getPageAction(requestData);

		/*
		 * Required for Masterpass Express Checkout
		 * 
		String action = "";
		if (request.getAttribute(ACTION) != null) {
			action = request.getAttribute(ACTION).toString();
			if (action.equals(EWALLET_MP_EXPRESS_CHECKOUT)) {
				pageAction = PageAction.parse(action);
				ewalletRequestData.seteWalletAction(EWALLET_MP_EXPRESS_CHECKOUT);
			}
			if(action.equals(EWALLET_MP_GET_ALL_PAYMETHOD_IN_EWALLET)){
        		pageAction = PageAction.parse(action);
        		ewalletRequestData.seteWalletAction(EWALLET_MP_GET_ALL_PAYMETHOD_IN_EWALLET);
        	}
        	if(action.equals(EWALLET_MP_DISCONNECT_EWALLET)){
        		pageAction = PageAction.parse(action);
        		ewalletRequestData.seteWalletAction(EWALLET_MP_DISCONNECT_EWALLET);
        	}
		}
		*/
		return pageAction;
	}

	/**
	 * @param request
	 * @param ewalletRequestData
	 */
	private void getRequestParameter(final HttpServletRequest request,
			EwalletRequestData ewalletRequestData) {
		Map<String,String> params = new Hashtable<String, String>(); 
		
		// Masterpass Standard Checkout request parameters after Light Box finished
		if (request.getParameter(EwalletConstants.PARAM_REQUEST_TOKEN) != null){
			params.put(EwalletConstants.PARAM_REQUEST_TOKEN, request.getParameter(EwalletConstants.PARAM_REQUEST_TOKEN));
		}
		if (request.getParameter(EwalletConstants.PARAM_OAUTH_VERIFIER) != null){
			params.put(EwalletConstants.PARAM_OAUTH_VERIFIER, request.getParameter(EwalletConstants.PARAM_OAUTH_VERIFIER));
		}
		if (request.getParameter(EwalletConstants.PARAM_CHECKOUT_URL) != null){
			params.put(EwalletConstants.PARAM_CHECKOUT_URL, request.getParameter(EwalletConstants.PARAM_CHECKOUT_URL));
		}
		if (request.getParameter(EwalletConstants.MP_EWALLET_RESPONSE_STATUS) != null) {
			params.put(EwalletConstants.MP_EWALLET_RESPONSE_STATUS, request.getParameter(EwalletConstants.MP_EWALLET_RESPONSE_STATUS));
			ewalletRequestData.seteWalletResponseStatus(request.getParameter(EwalletConstants.MP_EWALLET_RESPONSE_STATUS).toString());
		}
		// Uncomment for Masterpass ExpressCheckout
/*		
		if (request.getParameter(PARAM_PAIRING_TOKEN) != null){
			params.put(PARAM_PAIRING_TOKEN, request.getParameter(PARAM_PAIRING_TOKEN));
			ewalletRequestData.setPairingToken(request.getParameter(PARAM_PAIRING_TOKEN));
		}
		if (request.getParameter(PARAM_PAIRING_VERIFIER) != null){
			params.put(PARAM_PAIRING_VERIFIER, request.getParameter(PARAM_PAIRING_VERIFIER));
			ewalletRequestData.setPairingVerifier(request.getParameter(PARAM_PAIRING_VERIFIER));
		}
		
		if(request.getAttribute(EWALLET_PAIRED_PAYMENT_METHOD)!=null){
			params.put(EWALLET_PAIRED_PAYMENT_METHOD, request.getParameter(EWALLET_PAIRED_PAYMENT_METHOD));
			ewalletRequestData.setMpPairedPaymentMethod(request.getAttribute(EWALLET_PAIRED_PAYMENT_METHOD).toString());
		}
		if(request.getAttribute(EWALLET_PRECHECKOT_CARDID)!=null){
			params.put(EWALLET_PRECHECKOT_CARDID, request.getParameter(EWALLET_PRECHECKOT_CARDID));
			ewalletRequestData.setPrecheckoutCardId(request.getAttribute(EWALLET_PRECHECKOT_CARDID).toString());
		}
*/		
		if (request.getParameter(EwalletConstants.EWALLET_TYPE) != null) {
			ewalletRequestData.seteWalletType(request.getParameter(EwalletConstants.EWALLET_TYPE));
		} else {
			ewalletRequestData.seteWalletType(EwalletConstants.MP_EWALLET_NAME);
		}
		ewalletRequestData.setReqParams(params);
			
	}

	/**
	 * Method creates the EWalletRequetData object
	 * 
	 * @param ewalletRequestData
	 * @param request
	 * @param response
	 * @param user
	 * @throws FDResourceException
	 */
	private void createEwalletRequestData(
			EwalletRequestData ewalletRequestData, final HttpServletRequest request,
			final HttpServletResponse response, final FDUserI user)
			throws FDResourceException {
		// TODO get the Domain name and Server Name from Properties file
		ewalletRequestData.setAppBaseUrl(request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort());
		ewalletRequestData.setContextPath(request.getContextPath());
		ewalletRequestData.setCustomerId(user.getFDCustomer()
				.getErpCustomerPK());
		FDSessionUser sessionuser = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
		ewalletRequestData.setPaymentechEnabled(sessionuser.isPaymentechEnabled());
		FDActionInfo fdActionInfo = AccountActivityUtil
				.getActionInfo(request.getSession());
		ewalletRequestData.setFdActionInfo(fdActionInfo);
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

	/**
	 * @param request
	 * @return
	 */
	private String convertParamsToJSON(final HttpServletRequest request) {
		String json = "{\"fdform\": \"MP\", \"formdata\": {";
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				json += "\"" + paramName + "\":" + "\"" + paramValue
						+ (paramNames.hasMoreElements() ? "\"," : "\"");
			}
		}
		json += "}}";

		request.setAttribute("data", json);
		return json;
	}
}
