package com.freshdirect.routing.service.impl;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceStub;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class BaseService {
	
	protected TransportationWebService getTransportationSuiteService(IRoutingSchedulerIdentity schedulerId) throws RemoteException {
		
		try {
			
			if(schedulerId != null &&  schedulerId.getArea() != null && schedulerId.getArea().getAreaCode() != null) {		
				return RoutingServiceLocator.getInstance().getTransportationSuiteService(schedulerId.getArea().getAreaCode());
			} else {
				return RoutingServiceLocator.getInstance().getTransportationSuiteService();
			}
			
		} catch(AxisFault ax) {
			ax.printStackTrace();
			throw new RemoteException();
		}
	}
	
	protected TransportationWebService getTransportationSuiteBatchService(IRoutingSchedulerIdentity schedulerId) throws RemoteException {
		
		try {
			
			if(schedulerId != null &&  schedulerId.isDepot()) {		
				return RoutingServiceLocator.getInstance().getTransportationSuiteDBatchProviderService();
			} else {
				return RoutingServiceLocator.getInstance().getTransportationSuiteBatchProviderService();
			}
			
		} catch(AxisFault ax) {
			ax.printStackTrace();
			throw new RemoteException();
		}
	}
	
	public RouteNetWebService getRouteNetBatchService() throws RemoteException {
		try {
			return RoutingServiceLocator.getInstance().getRouteNetBatchService();
		} catch(AxisFault ax) {
			ax.printStackTrace();
			throw new RemoteException();
		}
	}
	
	public RouteNetWebService getRouteNetService() throws RemoteException {
		try {
			return RoutingServiceLocator.getInstance().getRouteNetBatchService();
		} catch(AxisFault ax) {
			ax.printStackTrace();
			throw new RemoteException();
		}
	}
}
