package com.freshdirect.routing.service;

import java.util.Collection;
import java.util.Date;

import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IRoutingInfoService {
	
	Collection getRoutingScenarios() throws RoutingServiceException;
	
	IServiceTimeScenarioModel getRoutingScenario(Date deliveryDate)  throws RoutingServiceException;
}
