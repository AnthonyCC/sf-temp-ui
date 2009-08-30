package com.freshdirect.routing.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder;
import com.freshdirect.routing.proxy.stub.transportation.Location;
import com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder;
import com.freshdirect.routing.proxy.stub.transportation.RoutingSession;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerCalculateDeliveryWindowMetrics;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.service.IRoutingEngineService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDataDecoder;
import com.freshdirect.routing.util.RoutingDataEncoder;

public class RoutingEngineService extends BaseService implements IRoutingEngineService {
	
	public void saveLocations(Collection orderList, String region, String locationType) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(null);
			Location[] result = port.saveLocations(RoutingDataEncoder.encodeLocationList(orderList, region, locationType));
			if(result != null && result.length >0) {
				throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_SAVEERROR);
			}		
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		}
	}
	
	public void purgeOrders(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerPurge(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), false);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
		}
	}
	
	public void schedulerRemoveFromServer(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerRemoveFromServer(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId));
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_NOTFOUND);
		}
	}
	
	public List schedulerBulkReserveOrder(IRoutingSchedulerIdentity schedulerId, Collection orderList
												, String region, String locationType
												, String orderType) throws RoutingServiceException {
		List unassignedList = new ArrayList();
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			
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
			
		} catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
		}
		return unassignedList;
	}
	
	public void schedulerBalanceRoutes(IRoutingSchedulerIdentity schedulerId,
											String balanceBy, double balanceFactor) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerBalanceRoutes(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), 
					RoutingDataEncoder.encodeBalanceRoutesOptions(balanceBy, balanceFactor));
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_BALANCEROUTES_UNSUCCESSFUL);
		}
	}
	
	public void sendRoutesToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException {
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerSendRoutesToRoadnetEx(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), sessionDescription);
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_SENDROUTES_UNSUCCESSFUL);
		}
	}
	
	public List saveUnassignedToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionId, Collection orderList) throws RoutingServiceException {
		List unassignedList = new ArrayList();
		try {			
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			RoutingImportOrder[] unassignedOrders = port.saveRoutingImportOrders(schedulerId.getRegionId()
													, RoutingDataEncoder.encodeImportOrderList(schedulerId, sessionId, orderList)
													, RoutingDataEncoder.encodeTimeZoneOptions());
			if(unassignedOrders != null) {
				unassignedList.addAll(Arrays.asList(unassignedOrders));
			}
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_SENDUNASSIGNED_UNSUCCESSFUL);
		}
		return unassignedList;
	}
	
	public String retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException {
		String sessionId = null;
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			RoutingSession[] routingSession = port.retrieveRoutingSessionsByCriteria(RoutingDataEncoder.encodeRoutingSessionCriteria(schedulerId, sessionDescription)
													, RoutingDataEncoder.encodeRouteInfoRetrieveOptions());			
			if(routingSession != null && routingSession.length > 0) {				
				sessionId = ""+routingSession[0].getSessionIdentity().getInternalSessionID();
			}
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVESESSION_UNSUCCESSFUL);
		}
		
		return sessionId;
	}
	
		
	public List<IDeliverySlot> schedulerAnalyzeOrder(IOrderModel orderModel, String locationType
			, String orderType, Date startDate, int noOfDays, List<IDeliverySlot> slots) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schId = RoutingDataEncoder.encodeSchedulerId(null, orderModel);
			TransportationWebService port = getTransportationSuiteService(schId);
			
			List _tmpLocOrders = new ArrayList();
			_tmpLocOrders.add(orderModel);
			Location[] result = port.saveLocations(RoutingDataEncoder.encodeLocationList
													(_tmpLocOrders, schId.getRegionId(), locationType));
			if(result != null && result.length >0) {
				throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_SAVEERROR);
			}
			
			return RoutingDataDecoder.decodeDeliveryWindows(
						port.schedulerAnalyzeOrder(RoutingDataEncoder.encodeAnalyzeOrder(schId									
								, orderModel
								, locationType
								, orderType),
								RoutingDataEncoder.encodeSchedulerAnalyzeOptions(schId.getRegionId()
										, orderModel.getDeliveryInfo().getDeliveryZone().getArea().getAreaCode()
										, startDate
										, noOfDays, slots)));

		} catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_ANALYZE_UNSUCCESSFUL);
		}
	}
	
		
	
	public IDeliveryReservation schedulerReserveOrder(IOrderModel orderModel,IDeliverySlot deliverySlot,
			String locationType
			, String orderType) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel);
			deliverySlot.setSchedulerId(schedulerId);//?? should I?
			TransportationWebService port = getTransportationSuiteService(schedulerId);
						
			
			return RoutingDataDecoder.decodeDeliveryReservation(
										port.schedulerReserveOrder(
												RoutingDataEncoder.encodeSchedulerIdentity(schedulerId) ,
												RoutingDataEncoder.encodeOrder(
												schedulerId
												, orderModel
												, locationType
												, orderType
												, false),
												RoutingDataEncoder.encodeDeliveryWindow(deliverySlot,schedulerId) ,
												RoutingDataEncoder.encodeSchedulerReserveOrderOptions()));

		} catch (Exception exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_RESERVE_UNSUCCESSFUL);
		}
	}

	public void schedulerConfirmOrder(IOrderModel orderModel) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel);
			TransportationWebService port = getTransportationSuiteService(schedulerId);			
						
			port.schedulerConfirmOrder(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), 
											orderModel.getOrderNumber());
			

		} catch (RemoteException exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_CONFIRM_UNSUCCESSFUL);
		}
	}
	
	public void schedulerUpdateOrder(IOrderModel orderModel, String previousOrderNumber) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel);
			TransportationWebService port = getTransportationSuiteService(schedulerId);			
					
			
			port.schedulerUpdateOrder(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
											, RoutingDataEncoder.encodeDeliveryAreaOrderIdentity(schedulerId, previousOrderNumber)
											, RoutingDataEncoder.encodeSchedulerUpdateOrderOptions(orderModel.getOrderNumber()));

		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_UPDATE_UNSUCCESSFUL);
		}
	}
	
	public void schedulerCancelOrder(IOrderModel orderModel) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel);
			TransportationWebService port = getTransportationSuiteService(schedulerId);			
					
			
			port.schedulerCancelOrder(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), orderModel.getOrderNumber());

		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_CANCEL_UNSUCCESSFUL);
		}
	}
	
	public List<IDeliveryWindowMetrics> retrieveCapacityMetrics(IRoutingSchedulerIdentity schedulerId, List<IDeliverySlot> slots) throws RoutingServiceException {
		
		List<IDeliveryWindowMetrics> result = null;
		try {
			TransportationWebService port = getTransportationSuiteService(schedulerId);
			
			SchedulerCalculateDeliveryWindowMetrics criteria = new SchedulerCalculateDeliveryWindowMetrics();
			
			criteria.setSchedulerIdentity(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId));
			
			SchedulerDeliveryWindowMetricsOptions options = RoutingDataEncoder.encodeSchedulerDeliveryWindowMetricsOptions();
			options.setDeliveryWindows(RoutingDataEncoder.encodeDeliveryWindowBaseList(slots));
			
						
			result = RoutingDataDecoder.decodeDeliveryWindowMetrics(
											port.schedulerCalculateDeliveryWindowMetrics(
													RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
													, options));
			
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEMETRICS_UNSUCCESSFUL);
		}

		return result;
	}
	
}
