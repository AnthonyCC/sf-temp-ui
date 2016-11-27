package com.freshdirect.mobileapi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.EwalletResponse;
import com.freshdirect.mobileapi.controller.data.request.EwalletRequest;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.EwalletService;
import com.freshdirect.mobileapi.service.ServiceException;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletCheckoutController extends BaseController{

	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(EwalletCheckoutController.class);
	
	private static final String ACTION_EWALLET_CHECKOUT ="ewalletCheckout";
	private static final String ACTION_EWALLET_CHECKOUT_DATA ="ewalletCheckoutData";
	
	
	/* (non-Javadoc)
	 * @see com.freshdirect.mobileapi.controller.BaseController#validateUser()
	 */
	@Override
    protected boolean validateUser() {
        return true;
    }
	/* (non-Javadoc)
	 * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
	 */
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws JsonException, FDException,
			ServiceException, NoSessionException {
		
		LOG.debug("EwalletCheckoutController processRequest");
		EwalletResponse res = new EwalletResponse();
		
    	// Retrieving any possible payload
        String postData = getPostData(request, response);
        LOG.debug("EwalletCheckoutController PostData received: [" + postData + "]");
        
        EwalletRequest requestMessage = null;
        
        if (StringUtils.isNotEmpty(postData)) {
            requestMessage = parseRequestObject(request, response, EwalletRequest.class);
        }else{
        	res.addErrorMessage("ERR_PAYLOAD", "Required input is missing");
        	setResponseMessage(model, res, user);
        	return model;
        }
        // Checkout Service Call
        if(ACTION_EWALLET_CHECKOUT.equals(action))	
        {
        	if(requestMessage != null){
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_EWALLET_CHECKOUT);
        		if(errorMsg !=null && errorMsg.isEmpty()){
		        	EwalletService ewalletService = new EwalletService(); 
		        	res = ewalletService.checkout(requestMessage,user,request);
        		}else{
        			res.addErrorMessages(errorMsg);
        		}
	         	setResponseMessage(model, res, user);
        	}
        	setResponseMessage(model, res, user);
            return model;
        }
        if(ACTION_EWALLET_CHECKOUT_DATA.equals(action)){
        	if(requestMessage != null) {
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_EWALLET_CHECKOUT_DATA);
        		if(errorMsg !=null && errorMsg.isEmpty()){
        			// Core Checkout Data Service -- Light Box finished 
        			EwalletService ewalletService = new EwalletService(); 
		        	res = ewalletService.checkoutData(requestMessage,user,request);
        		}else{
        			res.addErrorMessages(errorMsg);
        		}
        		
        		setResponseMessage(model, res, user);
        	}
        	setResponseMessage(model, res, user);
            return model;
        }
        
		return null;
	}

	/**
	 * This method check all the required fields are available on payload
	 * @param requestMessage
	 * @param response
	 * @return
	 */
	private Map<String,String> checkRequiredData(final EwalletRequest requestMessage, final EwalletResponse response,final String action){
		
		Map<String,String> errors = new HashMap<String, String>();
		if(ACTION_EWALLET_CHECKOUT.equals(action)){
			if(requestMessage.geteWalletType() == null || requestMessage.geteWalletType().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "eWalletType input is missing");
			}if(requestMessage.getCallBackUrl() == null || requestMessage.getCallBackUrl().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "callBackUrl input is missing");
			}
		}else if(ACTION_EWALLET_CHECKOUT_DATA.equals(action)){
			if(requestMessage.geteWalletType() == null || requestMessage.geteWalletType().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "eWalletType input is missing");
			}if(requestMessage.getOauthToken() == null || requestMessage.getOauthToken().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "oauthToken input is missing");
			}if(requestMessage.getOauthVerifer() == null || requestMessage.getOauthVerifer().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "oauthVerifer input is missing");
			}if(requestMessage.getPairingToken() == null || requestMessage.getPairingToken().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "pairingToken input is missing");
			}if(requestMessage.getPairingVerifer()== null || requestMessage.getPairingVerifer().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "pairingVerifer input is missing");
			}if(requestMessage.getCheckoutUrl()== null || requestMessage.getCheckoutUrl().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "checkoutUrl input is missing");
			}
		}
		
		return errors;
	}
}
