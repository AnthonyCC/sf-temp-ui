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
		
	public List<IWaveInstance> retrieveWaveInstancesBatch(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		try {
			return retrieveWaveInstances(getTransportationSuiteBatchService(schedulerId), schedulerId);
		}  catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public List<IWaveInstance> retrieveWaveInstances(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		try {
			return retrieveWaveInstances(getTransportationSuiteService(schedulerId), schedulerId);
		}  catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public List<IWaveInstance> retrieveWaveInstances(TransportationWebService port, IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
				
		List<IWaveInstance> result = null;
		
		try {
										
			result = RoutingDataDecoder.decodeWaveInstanceList(port.schedulerRetrieveDeliveryWaveInstancesByCriteria(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
																	, RoutingDataEncoder.encodeSchedulerDeliveryWaveInstanceCriteria()
																	, RoutingDataEncoder.encodeSchedulerRetrieveDeliveryWaveInstanceOptions()));
			
			
		} catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}

		return result;
	}
	
	public List<String> saveWaveInstancesBatch(IRoutingSchedulerIdentity schedulerId
											, IWaveInstance waveInstance
												, boolean force) throws RoutingServiceException {
		try {
			return this.saveWaveInstances(getTransportationSuiteBatchService(schedulerId), schedulerId, waveInstance, force);
		}  catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public List<String> saveWaveInstances(IRoutingSchedulerIdentity schedulerId
											, IWaveInstance waveInstance
												, boolean force) throws RoutingServiceException {
		try {
			return this.saveWaveInstances(getTransportationSuiteService(schedulerId), schedulerId, waveInstance, force);
		}  catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
	}
	
	public List<String> saveWaveInstances(TransportationWebService port, IRoutingSchedulerIdentity schedulerId
																, IWaveInstance waveInstance
																, boolean force) throws RoutingServiceException {
		
		List<String> result = null;
		try {
									
			String[] unassignedOrders = port.schedulerSaveDeliveryWaveInstance(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
															, RoutingDataEncoder.encodeWaveInstanceIdentity(waveInstance)
															, RoutingDataEncoder.encodeDeliveryWaveAttributes(waveInstance)
															, RoutingDataEncoder.encodeSchedulerSaveDeliveryWaveInstanceOptions(force));
			result = new ArrayList<String>();
			if(unassignedOrders != null) {				
				for(String unassigned : unassignedOrders) {
					result.add(unassigned);
				}
			}
			
		} catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEWAVEINSTANCE_UNSUCCESSFUL);
		}
		return result;
	}
	
}
