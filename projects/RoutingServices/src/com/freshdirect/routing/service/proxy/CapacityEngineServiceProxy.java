package com.freshdirect.routing.service.proxy;

import java.util.List;

import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.service.ICapacityEngineService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class CapacityEngineServiceProxy extends BaseServiceProxy {
	
	public List<IWaveInstance> retrieveWaveInstances(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		return getService().retrieveWaveInstances(schedulerId);
	}
	
	public List<String> saveWaveInstances(IRoutingSchedulerIdentity schedulerId
												, IWaveInstance waveInstance
													, boolean force) throws RoutingServiceException {
		
		return getService().saveWaveInstances(schedulerId, waveInstance, force);
	}
	
	public List<IWaveInstance> retrieveWaveInstancesBatch(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		return getService().retrieveWaveInstancesBatch(schedulerId);
	}
	
	public List<String> saveWaveInstancesBatch(IRoutingSchedulerIdentity schedulerId
												, IWaveInstance waveInstance
													, boolean force) throws RoutingServiceException {
		
		return getService().saveWaveInstancesBatch(schedulerId, waveInstance, force);
	}
	
	public ICapacityEngineService getService() {
		return RoutingServiceLocator.getInstance().getCapacityEngineService();
	}
}
