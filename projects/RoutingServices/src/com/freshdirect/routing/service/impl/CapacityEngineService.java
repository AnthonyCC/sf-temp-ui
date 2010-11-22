package com.freshdirect.routing.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.service.ICapacityEngineService;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDataDecoder;
import com.freshdirect.routing.util.RoutingDataEncoder;

public class CapacityEngineService extends BaseService implements ICapacityEngineService {
		
	public List<IWaveInstance> retrieveWaveInstances(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
				
		List<IWaveInstance> result = null;
		
		try {
			TransportationWebService port = getTransportationSuiteService(schedulerId);
								
			result = RoutingDataDecoder.decodeWaveInstanceList(port.schedulerRetrieveDeliveryWaveInstancesByCriteria(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
																	, RoutingDataEncoder.encodeSchedulerDeliveryWaveInstanceCriteria()
																	, RoutingDataEncoder.encodeSchedulerRetrieveDeliveryWaveInstanceOptions()));
			
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}

		return result;
	}
	
	public List<String> saveWaveInstances(IRoutingSchedulerIdentity schedulerId
																, IWaveInstance waveInstance
																, boolean force) throws RoutingServiceException {
		
		List<String> result = null;
		try {
			TransportationWebService port = getTransportationSuiteService(schedulerId);
						
			String[] unassignedOrders = port.schedulerSaveDeliveryWaveInstance(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
															, RoutingDataEncoder.encodeWaveInstanceIdentity(waveInstance)
															, RoutingDataEncoder.encodeDeliveryWaveAttributes(waveInstance)
															, RoutingDataEncoder.encodeSchedulerSaveDeliveryWaveInstanceOptions(force));
			if(unassignedOrders != null) {
				result = new ArrayList<String>();
				for(String unassigned : unassignedOrders) {
					result.add(unassigned);
				}
			}
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_SAVEWAVEINSTANCE_UNSUCCESSFUL);
		}
		return result;
	}
	
}
