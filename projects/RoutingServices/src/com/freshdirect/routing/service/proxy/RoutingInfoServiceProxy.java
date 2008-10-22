package com.freshdirect.routing.service.proxy;

import java.util.Collection;

import com.freshdirect.routing.service.IRoutingInfoService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class RoutingInfoServiceProxy  extends BaseServiceProxy  {
	
	public Collection getRoutingScenarios() throws RoutingServiceException {
		return getService().getRoutingScenarios();
	}
	
	public IRoutingInfoService getService() {
		return RoutingServiceLocator.getInstance().getRoutingInfoService();
	}
}
