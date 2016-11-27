package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.NewBrowseResult;
import com.freshdirect.mobileapi.controller.data.request.BrowseQuery;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.mobileapi.util.NewBrowseUtil;

public class NewBrowseController extends BaseController{
	
	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(NewBrowseController.class);
	
	 private static final String ACTION_GET_CATEGORIES = "getCategories";
	 
	 @Override
		protected boolean validateUser() {
			return false;
		}
	    
	 
	 /* (non-Javadoc)
	     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
	     */
	    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
	            SessionUser user) throws FDException, ServiceException, JsonException {

	    	if (user == null) {
	    		user = fakeUser(request.getSession());
	    	}

	    	// Retrieving any possible payload
	        String postData = getPostData(request, response);
	        BrowseQuery requestMessage = null;

	        LOG.debug("NewBrowseController PostData received: [" + postData + "]");
	        if (StringUtils.isNotEmpty(postData)) {
	            requestMessage = parseRequestObject(request, response, BrowseQuery.class);
	        }
	        NewBrowseResult result = new NewBrowseResult();
	        
	        if(ACTION_GET_CATEGORIES.equals(action)) {
	        	
	        	result = NewBrowseUtil.getCategoriesForNewBrowse(requestMessage, user, request,model,response);
	        	
	        }
	        
	        setResponseMessage(model, result, user);
	        return model;
	        	
	        }
	

}
