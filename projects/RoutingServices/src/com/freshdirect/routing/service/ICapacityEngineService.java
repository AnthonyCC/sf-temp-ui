package com.freshdirect.routing.service;

import java.util.List;

import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.service.exception.RoutingServiceException;


public interface ICapacityEngineService {
	
	List<IWaveInstance> retrieveWaveInstances(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException;
	
	List<String> saveWaveInstances(IRoutingSchedulerIdentity schedulerId, IWaveInstance waveInstance, boolean force) throws RoutingServiceException;
}
