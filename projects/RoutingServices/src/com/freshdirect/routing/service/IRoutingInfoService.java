package com.freshdirect.routing.service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IRoutingInfoService {		
	
	Collection getRoutingScenarios()  throws RoutingServiceException;
	
	IServiceTimeScenarioModel getRoutingScenarioByDate(final Date deliveryDate)  throws RoutingServiceException;
	
	IServiceTimeScenarioModel getRoutingScenarioByCode(final String code)  throws RoutingServiceException;
	
	Map<String, IZoneScenarioModel> getRoutingScenarioMapping(final String code)  throws RoutingServiceException;
	
	Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws RoutingServiceException;
}
