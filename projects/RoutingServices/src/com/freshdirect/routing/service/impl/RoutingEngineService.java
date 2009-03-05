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
			TransportationWebService_PortType port = getTransportationSuiteService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerPurge(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), false);
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
		}
	}
	
	public void schedulerRemoveFromServer(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		
		try {
			TransportationWebService_PortType port = getTransportationSuiteService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerRemoveFromServer(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId));
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
			TransportationWebService_PortType port = getTransportationSuiteService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			
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
			throw new RoutingServiceException(exp, IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
		} catch (MalformedURLException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
		} catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
		}
		return unassignedList;
	}
	
	public void schedulerBalanceRoutes(IRoutingSchedulerIdentity schedulerId,
											String balanceBy, double balanceFactor) throws RoutingServiceException {
		
		try {
			TransportationWebService_PortType port = getTransportationSuiteService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerBalanceRoutes(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), 
					RoutingDataEncoder.encodeBalanceRoutesOptions(balanceBy, balanceFactor));
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_BALANCEROUTES_UNSUCCESSFUL);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_BALANCEROUTES_UNSUCCESSFUL);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_BALANCEROUTES_UNSUCCESSFUL);
		}
	}
	
	public void sendRoutesToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException {
		try {
			TransportationWebService_PortType port = getTransportationSuiteService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerSendRoutesToRoadnetEx(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), sessionDescription);
			
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_SENDROUTES_UNSUCCESSFUL);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_SENDROUTES_UNSUCCESSFUL);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_SENDROUTES_UNSUCCESSFUL);
		}
	}
	
	public List saveUnassignedToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionId, Collection orderList) throws RoutingServiceException {
		List unassignedList = new ArrayList();
		try {			
			TransportationWebService_PortType port = getTransportationSuiteService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			RoutingImportOrder[] unassignedOrders = port.saveRoutingImportOrders(schedulerId.getRegionId()
													, RoutingDataEncoder.encodeImportOrderList(schedulerId, sessionId, orderList)
													, RoutingDataEncoder.encodeTimeZoneOptions());
			if(unassignedOrders != null) {
				unassignedList.addAll(Arrays.asList(unassignedOrders));
			}
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_SENDUNASSIGNED_UNSUCCESSFUL);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_SENDUNASSIGNED_UNSUCCESSFUL);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_SENDUNASSIGNED_UNSUCCESSFUL);
		}
		return unassignedList;
	}
	
	public String retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException {
		String sessionId = null;
		try {
			TransportationWebService_PortType port = getTransportationSuiteService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			RoutingSession[] routingSession = port.retrieveRoutingSessionsByCriteria(RoutingDataEncoder.encodeRoutingSessionCriteria(schedulerId, sessionDescription)
													, RoutingDataEncoder.encodeRouteInfoRetrieveOptions());			
			if(routingSession != null && routingSession.length > 0) {				
				sessionId = ""+routingSession[0].getSessionIdentity().getInternalSessionID();
			}
			
		} catch (ServiceException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVESESSION_UNSUCCESSFUL);
		} catch (MalformedURLException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVESESSION_UNSUCCESSFUL);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVESESSION_UNSUCCESSFUL);
		}
		
		return sessionId;
	}
	
	private TransportationWebService_PortType getTransportationSuiteService(IRoutingSchedulerIdentity schedulerId) 
																throws ServiceException, MalformedURLException {
		if(schedulerId != null &&  schedulerId.isDepot()) {			
			return RoutingServiceLocator.getInstance().getTransportationSuiteService("DEPOT");
		} else {
			return RoutingServiceLocator.getInstance().getTransportationSuiteService();
		}
	}
}
