package com.freshdirect.routing.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.axis2.AxisFault;

import com.freshdirect.routing.constants.IRoutingConstants;
import com.freshdirect.routing.constants.RoutingActivityType;
import com.freshdirect.routing.model.EquipmentType;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingNotificationModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder;
import com.freshdirect.routing.proxy.stub.transportation.Location;
import com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder;
import com.freshdirect.routing.proxy.stub.transportation.RoutingSession;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerCalculateDeliveryWindowMetrics;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.service.IRoutingEngineService;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDataDecoder;
import com.freshdirect.routing.util.RoutingDataEncoder;

public class RoutingEngineService extends BaseService implements IRoutingEngineService {
	
	public void saveLocations(Collection orderList, String region, String locationType) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(null);
			Location[] result = port.saveLocationsEx(RoutingDataEncoder.encodeLocationList(orderList, region, locationType)
														, RoutingDataEncoder.encodeSaveLocationsExOptions());
			
			if(result != null && result.length >0) {
				throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_SAVEERROR);
			}		
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		}
	}
	
	public void saveLocationsEx(Collection orderList, IRoutingSchedulerIdentity schedulerId, String region, String locationType) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);
			Location[] result = port.saveLocationsEx(RoutingDataEncoder.encodeLocationList(orderList, region, locationType)
														, RoutingDataEncoder.encodeSaveLocationsExOptions());
			
			if(result != null && result.length >0) {
				throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_SAVEERROR);
			}		
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_LOCATION_SAVEERROR);
		}
	}
	
	public void purgeBatchOrders(IRoutingSchedulerIdentity schedulerId, boolean reloadXml) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerPurge(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), reloadXml);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
		}
	}
	
	public void purgeOrders(IRoutingSchedulerIdentity schedulerId, boolean reloadXml) throws RoutingServiceException {
		
		try {
			System.out.println("Purge UPS Scheduler :"+schedulerId);
			TransportationWebService port = getTransportationSuiteService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerPurge(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), reloadXml);
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
		}
	}
	
	public void schedulerUnload(IRoutingSchedulerIdentity schedulerId) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			port.schedulerUnload(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId));
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
	
	public void sendRoutesToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionDescription, String waveCode) throws RoutingServiceException {
		try {
			TransportationWebService port = null;
			if(schedulerId.isDynamic())
				port = getTransportationSuiteService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			else
				port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			 
			port.schedulerSendRoutesToRoadnetEx(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
												, RoutingDataEncoder.encodeSchedulerSendRoutesToRoadnetExOptions(sessionDescription, waveCode));
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_SENDROUTES_UNSUCCESSFUL);
		}
	}
	
	public List saveUnassignedToRoadNet(IRoutingSchedulerIdentity schedulerId, String sessionId, Collection orderList) throws RoutingServiceException {
		List unassignedList = new ArrayList();
		try {			
			TransportationWebService port = null;
			if(schedulerId.isDynamic())
				port = getTransportationSuiteService(schedulerId);
			else
				port = getTransportationSuiteBatchService(schedulerId);
			
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
			TransportationWebService port = null;
			if(schedulerId.isDynamic())
				port = getTransportationSuiteService(schedulerId);
			else
				port = getTransportationSuiteBatchService(schedulerId);
			
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
	
		
	public void schedulerSaveLocation(IOrderModel orderModel, String locationType) throws RoutingServiceException {
		try {
			IRoutingSchedulerIdentity schId = RoutingDataEncoder.encodeSchedulerId(null, orderModel, RoutingActivityType.SAVE_LOCATION);
			TransportationWebService port = getTransportationSuiteService(schId);
			
			List _tmpLocOrders = new ArrayList();
			_tmpLocOrders.add(orderModel);
			
			Location[] result = port.saveLocationsEx(RoutingDataEncoder.encodeLocationList
													(_tmpLocOrders, schId.getRegionId(), locationType)
													, RoutingDataEncoder.encodeSaveLocationsExOptions());
			if(result != null && result.length >0) {
				throw new RoutingServiceException(null, IIssue.PROCESS_LOCATION_SAVEERROR);
			}						

		} catch (RemoteException exp) {			
			throw new RoutingServiceException(exp, IIssue.PROCESS_ANALYZE_UNSUCCESSFUL);
		}
	}
		
	public List<IDeliverySlot> schedulerAnalyzeOrder(IOrderModel orderModel, String locationType
			, String orderType, Date startDate, int noOfDays, List<IDeliverySlot> slots) throws RoutingServiceException {
		//System.out.println("schedulerAnalyzeOrder Order >"+orderModel);
		//System.out.println("schedulerAnalyzeOrder Slot >"+slots);
		try {
			IRoutingSchedulerIdentity schId = RoutingDataEncoder.encodeSchedulerId(null, orderModel, RoutingActivityType.GET_TIMESLOT);
			TransportationWebService port = getTransportationSuiteService(schId);
						
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
			throw new RoutingServiceException(exp, IIssue.PROCESS_ANALYZE_UNSUCCESSFUL);
		}
	}
	
		
	
	public IDeliveryReservation schedulerReserveOrder(IOrderModel orderModel,IDeliverySlot deliverySlot,
			String locationType
			, String orderType) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel, RoutingActivityType.RESERVE_TIMESLOT);
			deliverySlot.setSchedulerId(schedulerId);//?? should I?
			TransportationWebService port = getTransportationSuiteService(schedulerId);
						
			
			IDeliveryReservation reservation = RoutingDataDecoder.decodeDeliveryReservation(
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
			
			return reservation;
		} catch (Exception exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_RESERVE_UNSUCCESSFUL);
		}
	}

	public void schedulerConfirmOrder(IOrderModel orderModel) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel, RoutingActivityType.CONFIRM_TIMESLOT);
			TransportationWebService port = getTransportationSuiteService(schedulerId);			
						
			port.schedulerConfirmOrder(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId), 
											encodeString(orderModel.getDeliveryInfo().getReservationId()));
			

		} catch (RemoteException exp) {			
			throw new RoutingServiceException(exp, IIssue.PROCESS_CONFIRM_UNSUCCESSFUL);
		}
	}
	
	public boolean schedulerUpdateOrder(IOrderModel orderModel) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel, RoutingActivityType.UPDATE_TIMESLOT);
			TransportationWebService port = getTransportationSuiteService(schedulerId);			
					
			
			return port.schedulerUpdateOrder(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
											, RoutingDataEncoder.encodeDeliveryAreaOrderIdentity(schedulerId, orderModel)
											, RoutingDataEncoder.encodeSchedulerUpdateOrderOptions(orderModel));

		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_UPDATE_UNSUCCESSFUL);
		}
	}
	
	public boolean schedulerUpdateOrderNo(IOrderModel orderModel) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel, RoutingActivityType.UPDATE_TIMESLOT);
			TransportationWebService port = getTransportationSuiteService(schedulerId);			
					
			
			return port.schedulerUpdateOrder(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
											, RoutingDataEncoder.encodeDeliveryAreaOrderIdentity(schedulerId, orderModel)
											, RoutingDataEncoder.encodeSchedulerUpdateOrderNoOptions(orderModel));

		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_UPDATE_UNSUCCESSFUL);
		}
	}
	
	public void schedulerCancelOrder(IOrderModel orderModel) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel, RoutingActivityType.CANCEL_TIMESLOT);
			TransportationWebService port = getTransportationSuiteService(schedulerId);			
					
			
			port.schedulerCancelOrder(RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
											, encodeString(orderModel.getDeliveryInfo().getReservationId()));
			

		} catch (RemoteException exp) {
			try {
				IOrderModel _tmpOrder = schedulerRetrieveOrder(orderModel);
			} catch (RoutingServiceException rxp) {
				if(rxp.getIssue() != null && !rxp.getIssue().equalsIgnoreCase(IIssue.PROCESS_RETRIEVEORDER_NOTFOUND)) {
					throw new RoutingServiceException(exp, IIssue.PROCESS_CANCEL_UNSUCCESSFUL);
				} 
			}
		}
	}
	
	public IOrderModel schedulerRetrieveOrder(IOrderModel orderModel) throws RoutingServiceException {

		try {
			IRoutingSchedulerIdentity schedulerId = RoutingDataEncoder.encodeSchedulerId(null, orderModel, RoutingActivityType.RETRIEVE_ORDER);
			TransportationWebService port = getTransportationSuiteService(schedulerId);			


			return RoutingDataDecoder.decodeDeliveryAreaOrder(port.schedulerRetrieveOrderByIdentity(RoutingDataEncoder.encodeDeliveryAreaOrderIdentity(schedulerId.getRegionId(),schedulerId.getArea().getAreaCode()
					,schedulerId.getDeliveryDate(), orderModel.getOrderNumber()), RoutingDataEncoder.encodeDeliveryAreaOrderRetrieveOptions()));


		} catch (RemoteException exp) {  
			if(exp instanceof AxisFault) {
				AxisFault fault=(AxisFault)exp;
				if(fault.getMessage().equalsIgnoreCase(IRoutingConstants.ORDER_NOT_FOUND)) {
					throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEORDER_NOTFOUND);
				}
			}
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEORDER_UNSUCCESSFUL);

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
			/*int preRouteStemTime = schedulerId.getArea().getStemFromTime() == 0 
										? schedulerId.getArea().getStemToTime() : schedulerId.getArea().getStemFromTime(); 
			int postRouteStemTime = schedulerId.getArea().getStemToTime() == 0 
										? schedulerId.getArea().getStemFromTime() : schedulerId.getArea().getStemToTime();
			options.setPreRouteStemTimeAdjustmentSeconds(preRouteStemTime * 60);
			options.setPostRouteStemTimeAdjustmentSeconds(postRouteStemTime * 60);*/
			
			//options.setPreRouteStemTimeAdjustmentSeconds(schedulerId.getArea().getMaxStemTime() * 60);
			//options.setPostRouteStemTimeAdjustmentSeconds(schedulerId.getArea().getMaxStemTime() * 60);
						
			result = RoutingDataDecoder.decodeDeliveryWindowMetrics(
											port.schedulerCalculateDeliveryWindowMetrics(
													RoutingDataEncoder.encodeSchedulerIdentity(schedulerId)
													, options));
			
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVEMETRICS_UNSUCCESSFUL);
		}

		return result;
	}
	
	public List<IRoutingNotificationModel> retrieveNotifications() throws RoutingServiceException {
		
		List<IRoutingNotificationModel> result = null;
		
		try {
			TransportationWebService port = getTransportationSuiteService(null);
						
			result = RoutingDataDecoder.decodeNotifications(port.retrieveNotificationsByCriteria(RoutingDataEncoder.encodeNotificationCriteria()
																			, RoutingDataEncoder.encodeTimeZoneOptions()
																					, RoutingDataEncoder.encodeNotificationRetrieveOptions()));
			
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVENOTIFICATION_UNSUCCESSFUL);
		}
		return result;
	}
		
	public void deleteNotifications(List<IRoutingNotificationModel> notifications)  throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteService(null);
						
			port.deleteNotifications(RoutingDataEncoder.encodeNotificationIdentityList(notifications));
			
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_DELETENOTIFICATION_UNSUCCESSFUL);
		}
		
	}
	
		
	private String encodeString(String strRoot) {
		return strRoot != null ? strRoot.toUpperCase() : strRoot;
	}

	@Override
	public DeliveryAreaOrder getDeliveryAreaModel(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel
			, String region, String locationType
			, String orderType) {
		return RoutingDataEncoder.encodeBulkOrder(schedulerId, orderModel, locationType, orderType, true);
	}
	public List<EquipmentType> getEquipmentTypes(String region)  throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteService(null);
						
			return RoutingDataDecoder.decodeEquipmentTypes(
					port.retrieveEquipmentTypeByCriteria(RoutingDataEncoder.encodeEquipmentTypeCriteria(region), 
							RoutingDataEncoder.encodeTimeZoneOptions()));
			
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_DELETENOTIFICATION_UNSUCCESSFUL);
		}
	}
	
	public List<IRouteModel> getRoutesByCriteria(IRoutingSchedulerIdentity schedulerId, String waveCode)  throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteService(schedulerId);
			List<IRouteModel> routes = RoutingDataDecoder.decodeDeliveryAreaRoutes
					(port.schedulerRetrieveRoutesByCriteria(RoutingDataEncoder.encodeDeliveryAreaRouteCriteria(schedulerId, waveCode), 
			 RoutingDataEncoder.encodeDeliveryAreaRouteRetrieveOptions()));
	
			return routes;
			
		} catch (RemoteException exp) {
			throw new RoutingServiceException(exp, IIssue.PROCESS_DELETENOTIFICATION_UNSUCCESSFUL);
		}
		
		
	}

}
