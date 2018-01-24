package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.controller.data.request.RouteInfo;
import com.freshdirect.mobileapi.controller.data.response.RoutesDataMessage;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;

/**
 * @author Rob
 *
 */
public class RouteInfoController extends BaseController {

    private static final String ACTION_DELIVERY_INFO = "deliveryinfo";
    private static final String ACTION_DELIVERY_INFO_ALL = "deliveryinfoall";

    protected boolean validateUser() {
        return false;
    }

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
    	RouteInfo requestMessage = parseRequestObject(request, response,
    			RouteInfo.class);
		String routeNo = requestMessage.getRouteNo();
    	RoutesDataMessage responseMessage = new RoutesDataMessage();
    	//if(MobileApiProperties.isRouteDeliveryInfoEnabled()){
	    	if(ACTION_DELIVERY_INFO.equals(action)) {
	    		if(routeNo!=null){
	    			responseMessage.setRouteData(FDDeliveryManager.getInstance().getRouteDetails(routeNo));
	    		}
	        } else if(ACTION_DELIVERY_INFO_ALL.equals(action)) {
	    			responseMessage.setRouteData(FDDeliveryManager.getInstance().getRoutesDetailsByCurrentDate());
	        }
    	//}
    	setResponseMessage(model, responseMessage, user);
        return model;
    }
}
