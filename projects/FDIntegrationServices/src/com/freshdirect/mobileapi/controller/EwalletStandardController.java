package com.freshdirect.mobileapi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.EwalletResponse;
import com.freshdirect.mobileapi.controller.data.request.EwalletRequest;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.EwalletService;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletStandardController extends BaseController{

	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(EwalletStandardController.class);
	
	private static final String ACTION_EWALLET_STDCHECKOUT ="stdCheckout";
	private static final String ACTION_EWALLET_STDRESSCHECKOUT_DATA ="stdCheckoutData";
	private static final String ACTION_EWALLET_GENERATE_CLIENT_TOKEN = "generateClientToken";
	private static final String ACTION_EWALLET_ISPAYPAL_WALLET_PAIRED ="isPayPalWalletPaired";
	private static final String ACTION_UPDATE_PAYPAL_WALLET_TOKEN ="updatePayPalWalletToken";
	private static final String ACTION_DISCONNECT_WALLET ="disconnectWallet";
	
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
        if(ACTION_EWALLET_STDCHECKOUT.equals(action))	
        {
        	if(requestMessage != null){
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_EWALLET_STDCHECKOUT);
        		if(errorMsg !=null && errorMsg.isEmpty()){
		        	EwalletService ewalletService = new EwalletService(); 
		        	res = ewalletService.standardCheckout(requestMessage,user,request);
        		}else{
        			res.addErrorMessages(errorMsg);
        		}
	         	setResponseMessage(model, res, user);
        	}
        	setResponseMessage(model, res, user);
            return model;
        }else if(ACTION_EWALLET_STDRESSCHECKOUT_DATA.equals(action)){
        	if(requestMessage != null){
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_EWALLET_STDRESSCHECKOUT_DATA);
        		if(errorMsg !=null && errorMsg.isEmpty()){
		        	EwalletService ewalletService = new EwalletService(); 
		        	res = ewalletService.standardCheckoutData(requestMessage,user,request);
        		}else{
        			res.addErrorMessages(errorMsg);
        		}
	         	setResponseMessage(model, res, user);
        	}
        	setResponseMessage(model, res, user);
            return model;
        }else if(ACTION_EWALLET_GENERATE_CLIENT_TOKEN.equals(action)){
        	if(requestMessage != null){
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_EWALLET_GENERATE_CLIENT_TOKEN);
        		if(errorMsg !=null && errorMsg.isEmpty()){
		        	EwalletService ewalletService = new EwalletService(); 
		        	requestMessage.setCustomerId(user.getFDSessionUser().getFDCustomer().getErpCustomerPK());
		        	res = ewalletService.generateClientToken(requestMessage);
        		}else{
        			res.addErrorMessages(errorMsg);
        		}
	         	setResponseMessage(model, res, user);
        	}
        	setResponseMessage(model, res, user);
            return model;
        }
        else if(ACTION_EWALLET_ISPAYPAL_WALLET_PAIRED.equals(action)){
        	if(requestMessage != null){
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_EWALLET_ISPAYPAL_WALLET_PAIRED);
        		if(errorMsg !=null && errorMsg.isEmpty()){
		        	EwalletService ewalletService = new EwalletService(); 
		        	FDActionInfo fdActionInfo = AccountActivityUtil.getActionInfo(request.getSession());
        			requestMessage.setFdActionInfo(fdActionInfo);
		        	requestMessage.setCustomerId(user.getFDSessionUser().getFDCustomer().getErpCustomerPK());
		        	res = ewalletService.isPayPalWalletPaired(requestMessage,user);
        		}else{
        			res.addErrorMessages(errorMsg);
        		}
	         	setResponseMessage(model, res, user);
        	}
        	setResponseMessage(model, res, user);
            return model;
        }
        else if(ACTION_UPDATE_PAYPAL_WALLET_TOKEN.equals(action)){
        	if(requestMessage != null){
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_UPDATE_PAYPAL_WALLET_TOKEN);
        		if(errorMsg !=null && errorMsg.isEmpty()){
		        	EwalletService ewalletService = new EwalletService(); 
		        	FDActionInfo fdActionInfo = AccountActivityUtil.getActionInfo(request.getSession());
        			requestMessage.setFdActionInfo(fdActionInfo);
		        	requestMessage.setCustomerId(user.getFDSessionUser().getFDCustomer().getErpCustomerPK());
		        	res = ewalletService.addPayPalWallet(requestMessage);
        		}else{
        			res.addErrorMessages(errorMsg);
        		}
	         	setResponseMessage(model, res, user);
        	}
        	setResponseMessage(model, res, user);
            return model;
        }
        else if(ACTION_DISCONNECT_WALLET.equals(action)){
        	if(requestMessage != null){
        		Map<String,String> errorMsg = checkRequiredData(requestMessage,res,ACTION_DISCONNECT_WALLET);
        		if(errorMsg !=null && errorMsg.isEmpty()){
        			FDActionInfo fdActionInfo = AccountActivityUtil.getActionInfo(request.getSession());
        			requestMessage.setFdActionInfo(fdActionInfo);
        			requestMessage.setCustomerId(user.getFDSessionUser().getFDCustomer().getErpCustomerPK());
		        	EwalletService ewalletService = new EwalletService(); 
		        	res = ewalletService.disconnectWallet(requestMessage);
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
	 * This method check all the required fields are available on payload based on the action
	 * @param requestMessage
	 * @param response
	 * @return
	 */
	private Map<String,String> checkRequiredData(final EwalletRequest requestMessage, final EwalletResponse response,final String action){
		
		Map<String,String> errors = new HashMap<String, String>();
		if(requestMessage.geteWalletType() == null || requestMessage.geteWalletType().trim().length()==0){
			errors.put("ERR_INPUT_MISSING", "eWalletType input is missing");
		}
		if(ACTION_EWALLET_STDCHECKOUT.equals(action)){
			if(requestMessage.getCallBackUrl() == null || requestMessage.getCallBackUrl().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "callBackUrl is missing input is missing");
			}
		}if(ACTION_EWALLET_STDRESSCHECKOUT_DATA.equals(action)){
			if(requestMessage.getOauthToken() == null || requestMessage.getOauthToken().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "oauthToken input is missing");
			}if(requestMessage.getOauthVerifer() == null || requestMessage.getOauthVerifer().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "oauthVerifer input is missing");
			}if(requestMessage.getCheckoutUrl() == null || requestMessage.getCheckoutUrl().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "checkoutUrl input is missing");
			}
		}if(ACTION_UPDATE_PAYPAL_WALLET_TOKEN.equals(action)){
			if(requestMessage.getTokenType() == null || requestMessage.getTokenType().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "Token Type input is missing");
			}if(requestMessage.getTokenValue() == null || requestMessage.getTokenValue().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "Token Value input is missing");
			}if(requestMessage.getFirstName() == null || requestMessage.getFirstName().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "First Name input is missing");
			}if(requestMessage.getLastName() == null || requestMessage.getLastName().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "Last Name input is missing");
			}if(requestMessage.getEmailId() == null || requestMessage.getEmailId().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "Email Id input is missing");
			}if(requestMessage.getDeviceId() == null || requestMessage.getDeviceId().trim().length()==0){
				errors.put("ERR_INPUT_MISSING", "Device Id input is missing");
			}
		}
		return errors;
	}
	
}
