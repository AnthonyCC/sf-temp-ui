package com.freshdirect.routing.service;

import java.util.Collection;

import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IRoutingInfoService {
	
	Collection getRoutingScenarios() throws RoutingServiceException;
}
