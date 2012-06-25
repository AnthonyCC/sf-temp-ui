package com.freshdirect.mobileapi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.Lookup;
import com.freshdirect.mobileapi.controller.data.response.LookupResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;

/**
 * @author Sivachandar
 *
 */
public class LookupController extends BaseController {
    private static final org.apache.log4j.Category LOG = LoggerFactory.getInstance(LookupController.class);


    private static final String ACTION_GET_COUNTRIES = "getCountries";

    private static final String ACTION_GET_REGIONS = "getRegions";        
    

    protected boolean validateUser() {
        return false;
    }
    
    /* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {
    	
    	// Retrieving any possible payload
    	String lookupKey = request.getParameter("lookupKey");        
    	LookupResponse result = new LookupResponse();
    	
        if (ACTION_GET_COUNTRIES.equals(action)) {
        	
        	List<BillingCountryInfo> list = BillingCountryInfo.getEnumList();
	        List<Lookup> countries = new ArrayList<Lookup>();
			for(BillingCountryInfo countryInfo : list) {
				countries.add(new Lookup(countryInfo.getCode(), countryInfo.getName()));
            }
			result.setLookup(countries);
        } else if(ACTION_GET_REGIONS.equals(action)) {
        	
        	lookupKey = lookupKey != null ? lookupKey : "US";
			BillingCountryInfo bc = BillingCountryInfo.getEnum(lookupKey);
			List<Lookup> regions = new ArrayList<Lookup>();
			
			if(bc != null) {
				List<BillingRegionInfo> _list = bc.getRegions();
			         
				for(BillingRegionInfo regionInfo : _list) {
					regions.add(new Lookup(regionInfo.getCode(), regionInfo.getName()));
				}
			}
			result.setLookup(regions);
        }
        
        setResponseMessage(model, result, user);
        return model;
    }    
}
