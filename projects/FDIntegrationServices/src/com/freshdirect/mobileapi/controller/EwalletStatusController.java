package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.EwalletResponse;
import com.freshdirect.mobileapi.controller.data.Message;
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
public class EwalletStatusController extends BaseController{

	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(EwalletStatusController.class);
	
	private static final String ACTION_GET_EWALLET_STATUS ="getEWalletStatus";
	
	@Override
    protected boolean validateUser() {
        return false;
    }
	/* (non-Javadoc)
	 * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
	 */
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws JsonException, FDException,
			ServiceException, NoSessionException {
		
		LOG.debug("EwalletStatusController processRequest");
		
		if (user == null) {
    		user = fakeUser(request.getSession());
    	}

    	// Retrieving any possible payload
        String postData = getPostData(request, response);
        LOG.debug("EwalletStatusController PostData received: [" + postData + "]");
        
        EwalletRequest requestMessage = null;
        
        if (StringUtils.isNotEmpty(postData)) {
            requestMessage = parseRequestObject(request, response, EwalletRequest.class);
        }
        
        if(ACTION_GET_EWALLET_STATUS.equals(action))	// Get EWallet Status 
        {
        	EwalletResponse res = null;
        	if(requestMessage != null){
	        	EwalletService ewalletService = new EwalletService(); 
	        	res = ewalletService.getEwalletStatus(requestMessage);              
	         	setResponseMessage(model, res, user);
        	}else{
        		res = new EwalletResponse();
        		res.setStatus(Message.STATUS_FAILED);
        		res.addErrorMessage("WalletType", "Input EWallet type is missing");
        	}
        	setResponseMessage(model, res, user);
            return model;
        }
        
		return null;
	}

}
