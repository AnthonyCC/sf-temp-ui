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
public class EwalletExpressController extends BaseController{

	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(EwalletExpressController.class);
	
	private static final String ACTION_EWALLET_PRECHECKOUT ="ewalletPrecheckout";
	private static final String ACTION_EWALLET_EXPRESSCHECKOUT ="ewalletExpresscheckout";
	private static final String EXPRESSCHECKOUT_TRASCODE_EXP ="EXP";
	private static final String EXPRESSCHECKOUT_TRASCODE_PEX ="PEX";
	
	
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
        if(ACTION_EWALLET_PRECHECKOUT.equals(action))	
        {
        	if(requestMessage != null){
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_EWALLET_PRECHECKOUT);
        		if(errorMsg !=null && errorMsg.isEmpty()){
		        	EwalletService ewalletService = new EwalletService(); 
		        	res = ewalletService.preCheckout(requestMessage,user);
        		}else{
        			res.addErrorMessages(errorMsg);
        		}
	         	setResponseMessage(model, res, user);
        	}
        	setResponseMessage(model, res, user);
            return model;
        }else if(ACTION_EWALLET_EXPRESSCHECKOUT.equals(action)){
        	if(requestMessage != null){
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_EWALLET_EXPRESSCHECKOUT);
        		if(errorMsg !=null && errorMsg.isEmpty()){
		        	EwalletService ewalletService = new EwalletService(); 
		        	res = ewalletService.expressCheckout(requestMessage,user,request);
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
		if(ACTION_EWALLET_PRECHECKOUT.equals(action)){
			if(requestMessage.geteWalletType() == null || requestMessage.geteWalletType().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "eWalletType input is missing");
			}
		}if(ACTION_EWALLET_EXPRESSCHECKOUT.equals(action)){
			if(requestMessage.geteWalletType() == null || requestMessage.geteWalletType().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "eWalletType input is missing");
			}if(requestMessage.getOriginUrl() == null || requestMessage.getOriginUrl().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "OriginUrl input is missing");
			}if(requestMessage.getTransCode() == null || requestMessage.getTransCode().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "TranCode input is missing");
			}if(requestMessage.getTransCode() != null && requestMessage.getTransCode().trim().length() > 0){
				if(!checkValidTransCode(requestMessage.getTransCode().trim())) {
					errors.put("ERR_INVALID_INPUT", "Invalid Transaction Code.");
				}
				if(requestMessage.getTransCode().equals(EXPRESSCHECKOUT_TRASCODE_EXP)){
					if(requestMessage.getEwalletCardId() == null || requestMessage.getEwalletCardId().length() == 0){
						errors.put("ERR_INPUT_MISSING", "EWallet Card ID is missing for ExpressCheckout");
					}
					if(requestMessage.getPrecheckoutTransactionId() == null || requestMessage.getPrecheckoutTransactionId().length() == 0){
						errors.put("ERR_INPUT_MISSING", "Precheckout Transaction ID is required for Express checkout (TransCode : PEX");
					}
				}
			}
		}
		return errors;
	}
	
	/**
	 * Methos checks for valid TransCode 
	 * @param transCode
	 * @return
	 */
	private boolean checkValidTransCode(String transCode){
		
		if(transCode.equals(EXPRESSCHECKOUT_TRASCODE_EXP)){
			return true;
		}
		if(transCode.equals(EXPRESSCHECKOUT_TRASCODE_PEX)){
			return true;
		}
		
		return false;
	}
}
