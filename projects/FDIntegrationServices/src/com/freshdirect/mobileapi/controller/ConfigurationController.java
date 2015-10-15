package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.freshdirect.FDCouponProperties;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.response.HomeResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
/**
 * @author Rob
 *
 */
public class ConfigurationController extends BaseController {

    private static  org.apache.log4j.Category LOGGER = LoggerFactory.getInstance(ConfigurationController.class);

    private static final String PARAM_KEY = "key";
    private static final String ACTION_GET_ALL_CONFIG = "all";
    public static final Integer DEFAULT_MAX = 998;	

    protected boolean validateUser() {
        return false;
    }

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
    		SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
    	if(ACTION_GET_ALL_CONFIG.equals(action)){
    		getAllDetails(model, user, request);
    		return model;
    	} else {
    		String key = request.getParameter(PARAM_KEY);
    		if(configParams.get(key) != null) {
    			model.addObject("data", configParams.get(key));
    		} else {
    			String fdStoreProp = FDStoreProperties.get(key);
    			if(fdStoreProp != null && fdStoreProp.trim().length() > 0) {
    				model.addObject("data", fdStoreProp);
    			} else {
    				if(FDCouponProperties.PROP_COUPONS_ENABLED.equalsIgnoreCase(key)){
    					if(null !=user && null !=user.getFDSessionUser()){
    						model.addObject("data", String.valueOf(user.getFDSessionUser().isEligibleForCoupons()));
    					}
    				}else{
    					model.addObject("data", FDCouponProperties.get(key));
    				}
    			}
    		}
    		return model;
    	}
    }
    
    private ModelAndView getAllDetails(ModelAndView model, SessionUser user,
    		HttpServletRequest request) throws JsonException, FDException {
    	HomeResponse response = new HomeResponse();
    	response.setConfiguration(getConfiguration(user));
    	setResponseMessage(model, response, user);
    	return model;
    }

}
