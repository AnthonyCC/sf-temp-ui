package com.freshdirect.routing.service.proxy;

import java.util.Collection;
import java.util.Date;

import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.IRoutingInfoService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class RoutingInfoServiceProxy  extends BaseServiceProxy  {
	
	public Collection getRoutingScenarios() throws RoutingServiceException {
		return getService().getRoutingScenarios();
	}
	
	
	public IServiceTimeScenarioModel getRoutingScenario(Date deliveryDate)  throws RoutingServiceException {
		return getService().getRoutingScenario(deliveryDate);
	}
	
	public IRoutingInfoService getService() {
		return RoutingServiceLocator.getInstance().getRoutingInfoService();
	}
}
