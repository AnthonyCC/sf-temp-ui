package com.freshdirect.routing.service.impl;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder;
import com.freshdirect.routing.proxy.stub.transportation.Location;
import com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder;
import com.freshdirect.routing.proxy.stub.transportation.RoutingSession;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_PortType;
import com.freshdirect.routing.service.IRoutingEngineService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDataEncoder;

public class RoutingEngineService implements IRoutingEngineService {
	
	public void saveLocations(Collection orderList, String region, String locationType) throws RoutingServiceException {
		
		try {
			TransportationWebService_PortType port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
			Location[] result = port.saveLocations(RoutingDataEncoder.encodeLocationList(orderList, region, locationType));
			if(result != null && result.length >0) {
				throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_SAVEERROR);
			}
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		}
	}
	
	public void purgeOrders(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		
		try {
			TransportationWebService_PortType port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerPurge(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), false);
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		}
	}
	
	public List schedulerBulkReserveOrder(IRoutingSchedulerIdentity schedulerId, Collection orderList
												, String region, String locationType
												, String orderType) throws RoutingServiceException {
		List unassignedList = new ArrayList();
		try {
			TransportationWebService_PortType port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
			System.out.println("################### BULK RESERVE ORDER ################## "+schedulerId);
			DeliveryAreaOrder[] dlvOrderList = RoutingDataEncoder.encodeOrderList(orderList, schedulerId
																		, region
																		, locationType
																		, orderType);
			
			DeliveryAreaOrder[] unassignedOrders = port.schedulerBulkReserveOrders(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
													, dlvOrderList
													, RoutingDataEncoder.encodeSchedulerBulkReserveOrdersOptions());
			
			if(unassignedOrders != null) {
				unassignedList.addAll(Arrays.asList(unassignedOrders));
			}
			
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (MalformedURLException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		}
		return unassignedList;
	}
	
	public void sendRoutesToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException {
		try {
			TransportationWebService_PortType port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerSendRoutesToRoadnetEx(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), sessionDescription);
			
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		}
	}
	
	public List saveUnassignedToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionId, Collection orderList) throws RoutingServiceException {
		List unassignedList = new ArrayList();
		try {			
			TransportationWebService_PortType port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
			RoutingImportOrder[] unassignedOrders = port.saveRoutingImportOrders(schedulerId.getRegionId()
													, RoutingDataEncoder.encodeImportOrderList(schedulerId, sessionId, orderList)
													, RoutingDataEncoder.encodeTimeZoneOptions());
			if(unassignedOrders != null) {
				unassignedList.addAll(Arrays.asList(unassignedOrders));
			}
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		}
		return unassignedList;
	}
	
	public String retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException {
		String sessionId = null;
		try {
			TransportationWebService_PortType port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
			RoutingSession[] routingSession = port.retrieveRoutingSessionsByCriteria(RoutingDataEncoder.encodeRoutingSessionCriteria(schedulerId, sessionDescription)
													, RoutingDataEncoder.encodeRouteInfoRetrieveOptions());
			if(routingSession != null && routingSession.length > 0) {
				sessionId = ""+routingSession[0].getSessionIdentity().getInternalSessionID();
			}
			
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		}
		
		return sessionId;
	}	
}
