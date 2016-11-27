package com.freshdirect.routing.service.proxy;

import java.util.List;

import com.freshdirect.routing.service.IUtilityService;
import com.freshdirect.routing.service.RoutingServiceLocator;

public class UtilityProxy extends BaseServiceProxy {
	
	public boolean isValidExpression(String expression, List variableList) {
		// TODO Auto-generated method stub
		return getService().isValidExpression(expression, variableList);
	}
	
	public IUtilityService getService() {
		return RoutingServiceLocator.getInstance().getUtilityService();
	}
}
