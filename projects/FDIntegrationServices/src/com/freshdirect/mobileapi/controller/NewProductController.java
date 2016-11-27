package com.freshdirect.mobileapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.AllProductsResult;
import com.freshdirect.mobileapi.controller.data.request.NewProduct;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.BrowseUtil;

/**
 * @author av833967
 *
 */
public class NewProductController extends BaseController{

	private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(NewProductController.class);
	
	private static final String ACTION_GET_ALL_NEW_PRODUCTS ="getAllNewProducts";
	
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
		
		LOG.debug("NewProductController processRequest");
		
		if (user == null) {
    		user = fakeUser(request.getSession());
    	}

    	// Retrieving any possible payload
        String postData = getPostData(request, response);
        LOG.debug("NewProductController PostData received: [" + postData + "]");
        
        NewProduct requestMessage = null;
        
        if (StringUtils.isNotEmpty(postData)) {
            requestMessage = parseRequestObject(request, response, NewProduct.class);
        }
        
        
        if(ACTION_GET_ALL_NEW_PRODUCTS.equals(action))	// Get All the New Products 
        {
        	AllProductsResult res = new AllProductsResult();                
        	List<Product> products =  BrowseUtil.getAllNewProductList(user, request);
         	res.setProductsFromModel(products);
        	setResponseMessage(model, res, user);
            return model;
        }
        
		return null;
	}

}
