package com.freshdirect.routing.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface IRoutingInfoService {		
	
	Collection getRoutingScenarios()  throws RoutingServiceException;
	
	IServiceTimeScenarioModel getRoutingScenarioByDate(final Date deliveryDate)  throws RoutingServiceException;
	
	IServiceTimeScenarioModel getRoutingScenarioByCode(final String code)  throws RoutingServiceException;
	
	Map<String, IZoneScenarioModel> getRoutingScenarioMapping(final String code)  throws RoutingServiceException;
	
	Map<String, IServiceTimeTypeModel> getRoutingServiceTimeTypes()  throws RoutingServiceException;
	
	int flagReRouteReservation(Date deliveryDate, List<String> zones) throws RoutingServiceException;
	
	Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> getPlannedDispatchTree(Date deliveryDate)  throws RoutingServiceException;
	
	Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> getWaveInstanceTree(Date deliveryDate, EnumWaveInstanceStatus status)  throws RoutingServiceException;
	
	List<IWaveInstance> getWaveInstanceWithErrors()  throws RoutingServiceException;
	
	Set<String> getInSyncWaveInstanceZones(Date deliveryDate)  throws RoutingServiceException;
	
	boolean isPlanPublished(Date deliveryDate)  throws RoutingServiceException;
	
	Map<Date, List<String>> getDynamicEnabledZoneMapping()  throws RoutingServiceException;
}
