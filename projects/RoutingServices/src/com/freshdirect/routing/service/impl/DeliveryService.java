package com.freshdirect.routing.service.impl;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Category;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.DepotLocationModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.UnassignedDlvReservationModel;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.constants.RoutingActivityType;
import com.freshdirect.routing.dao.IDeliveryDetailsDAO;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTime;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IUnassignedModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.model.ServiceTime;
import com.freshdirect.routing.model.TimeslotCapacityModel;
import com.freshdirect.routing.proxy.stub.transportation.DirectionData;
import com.freshdirect.routing.proxy.stub.transportation.RoutingRoute;
import com.freshdirect.routing.proxy.stub.transportation.RoutingSession;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.service.IDeliveryService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDataDecoder;
import com.freshdirect.routing.util.RoutingDataEncoder;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.routing.util.ServiceTimeUtil;

public class DeliveryService extends BaseService implements IDeliveryService {
	
	private IDeliveryDetailsDAO deliveryDAOImpl;
	private static final Category LOGGER = LoggerFactory.getInstance(DeliveryService.class);
	
	public IDeliveryDetailsDAO getDeliveryDAOImpl() {
		return deliveryDAOImpl;
	}

	public void setDeliveryDAOImpl(IDeliveryDetailsDAO deliveryDAOImpl) {
		this.deliveryDAOImpl = deliveryDAOImpl;
	}

	
	public IServiceTime getServiceTime(IOrderModel orderModel, IServiceTimeScenarioModel scenario, RoutingActivityType routingType) throws RoutingServiceException {
		
		IZoneModel zone = orderModel.getDeliveryInfo().getDeliveryZone();
		ILocationModel location = orderModel.getDeliveryInfo().getDeliveryLocation();
		IBuildingModel building = location.getBuilding();
		
		IZoneScenarioModel zoneScenario = null;
		
		if(scenario != null && scenario.getZoneConfiguration() != null) {
			if(scenario.getZoneConfiguration().containsKey(zone.getZoneNumber())) {
				zoneScenario = scenario.getZoneConfiguration().get(zone.getZoneNumber());
			} else {
				zoneScenario = scenario.getZoneConfiguration().get(CONSTANT_ALLZONES);
			}
		}
		//if zoneScenario is null then there is no scenario level override, let find serviceTime first
		/*if(location.getServiceTimeOverride() > 0) {
			return location.getServiceTimeOverride();
		} else if(building.getServiceTimeOverride() > 0) {
			return building.getServiceTimeOverride();
		} else if(zoneScenario != null && zoneScenario.getServiceTimeOverride() > 0) {
			return zoneScenario.getServiceTimeOverride();
		}*/
		int stPriority = 0;
		// Couldn't find serviceTime going to find the serviceTimeType
		IServiceTimeTypeModel serviceTimeType = null;
		if(location.getServiceTimeType() != null
				&& location.getServiceTimeType().getCode() != null) {
			stPriority = 3;
			serviceTimeType = location.getServiceTimeType();
		} else if(building.getServiceTimeType() != null
				&& building.getServiceTimeType().getCode() != null) {
			stPriority = 2;
			serviceTimeType = building.getServiceTimeType();
		} else if(zoneScenario != null && zoneScenario.getServiceTimeType() != null
				&& zoneScenario.getServiceTimeType().getCode() != null) {				
			stPriority = 1;
			serviceTimeType = zoneScenario.getServiceTimeType();
		} else {
			serviceTimeType = zone.getServiceTimeType();
		}
		// Got the serviceTimeType lets calculate
		double valX = ServiceTimeUtil.evaluateExpression(scenario.getServiceTimeFactorFormula()
											, ServiceTimeUtil.getServiceTimeFactorParams(orderModel.getDeliveryInfo().getPackagingDetail()));			
		double valY = ServiceTimeUtil.evaluateExpression(scenario.getServiceTimeFormula()
											, ServiceTimeUtil.getServiceTimeParams(serviceTimeType.getFixedServiceTime(), serviceTimeType.getVariableServiceTime(), valX ));
		
		double valZ = serviceTimeType.getStopServiceTime();
		
		int adjustmentPriority = 0;
		//Lets see if we need a adjustment
		double serviceTimeAdjustment = 0;
		EnumArithmeticOperator adjustmentOperator = null;
		if(location.getAdjustmentOperator() != null) {
			adjustmentPriority = 3;
			adjustmentOperator = location.getAdjustmentOperator();
			serviceTimeAdjustment = location.getServiceTimeAdjustment();
		} else if(building.getAdjustmentOperator() != null) {
			adjustmentPriority = 2;
			adjustmentOperator = building.getAdjustmentOperator();
			serviceTimeAdjustment = building.getServiceTimeAdjustment();
		} else if(zoneScenario != null && zoneScenario.getAdjustmentOperator() != null) {
			adjustmentPriority = 1;
			adjustmentOperator = zoneScenario.getAdjustmentOperator();
			serviceTimeAdjustment = zoneScenario.getServiceTimeAdjustment();
			
		} 
		
		
		if(adjustmentOperator != null && adjustmentPriority > stPriority) {
			if(adjustmentOperator.equals(EnumArithmeticOperator.SUB)) {
				serviceTimeAdjustment = -serviceTimeAdjustment;
			}
			
			if(!RoutingActivityType.GET_TIMESLOT.equals(routingType)){
				if(orderModel.getDeliveryInfo().getReservedOrdersAtBuilding()!=0 && zone.getSvcAdjReductionFactor()>0)
					serviceTimeAdjustment = serviceTimeAdjustment * zone.getSvcAdjReductionFactor();
			}
			if((valZ + serviceTimeAdjustment) > 0) {
				valZ = valZ + serviceTimeAdjustment;
			} else {
				valZ = 0;
			}
		}
		IServiceTime serviceTime = new ServiceTime();
		serviceTime.setOrderServiceTime(valY);
		serviceTime.setStopServiceTime(valZ);
		return serviceTime;
	}
	
		
	public IDeliveryModel getDeliveryInfo(String saleId) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getDeliveryInfo(saleId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_DELIVERYINFO_NOTFOUND);
		}
	}
	
	public String getDeliveryType(String zoneCode) throws RoutingServiceException {
		try {
			return deliveryDAOImpl.getDeliveryType(zoneCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_DELIVERYTYPE_NOTFOUND);
		}
	}
	
	public String getDeliveryZoneType(String zoneCode) throws RoutingServiceException {
		try {
			return deliveryDAOImpl.getDeliveryZoneType(zoneCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_ZONETYPE_NOTFOUND);
		}
	}
	
	public Map getDeliveryZoneDetails() throws RoutingServiceException {
		try {
			return deliveryDAOImpl.getDeliveryZoneDetails();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RoutingServiceException(e, IIssue.PROCESS_ZONEINFO_NOTFOUND);
		}
		catch (Error r) {
			r.printStackTrace();
			return null;
		}
	}
	
	public List getLateDeliveryOrders(String query) throws RoutingServiceException {
		try {
			return deliveryDAOImpl.getLateDeliveryOrders(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL);
		}
	}
	
	public List getRoutes(Date routeDate, String internalSessionID, String routeID, String regionId) throws RoutingServiceException {
		try {
			TransportationWebService port = getTransportationSuiteBatchService(null);
			return RoutingDataDecoder.decodeRouteList(port.retrieveRoutingRoutesByCriteria
																(RoutingDataEncoder.encodeRouteCriteria(routeDate, internalSessionID, routeID, regionId)
															, RoutingDataEncoder.encodeRouteInfoRetrieveOptionsEx())); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public IDrivingDirection buildDriverDirections(Set<String> routeIDs, String sessionID, IRoutingSchedulerIdentity schedulerId)  throws RoutingServiceException {
		try {
			TransportationWebService port = getTransportationSuiteService(schedulerId.getRegionId());
			List<DirectionData> directions = new ArrayList<DirectionData>();
			for(String routeID: routeIDs)
			{
				DirectionData direction =  port.buildRoutingDriverDirections
					(RoutingDataEncoder.encodeRoutingRouteIdentity(routeID, sessionID, schedulerId.getRegionId()));
				directions.add(direction);
			}
			if(directions.size()>0)
			return RoutingDataDecoder.decodeDrivingDirections(directions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected TransportationWebService getTransportationSuiteService(String region) throws RemoteException {
		
		try {
			
			if(region!=null) {		
				return RoutingServiceLocator.getInstance().getTransportationSuiteService(region);
			} else {
				return RoutingServiceLocator.getInstance().getDrivingDirectionProviderService();
			}
			
		} catch(AxisFault ax) {
			ax.printStackTrace();
			throw new RemoteException();
		}
	}
	
	
	public Map<String, List<IDeliverySlot>> getTimeslotsByDate(Date deliveryDate, Date cutOffTime, String zoneCode, EnumLogicalOperator condition) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getTimeslotsByDate(deliveryDate, cutOffTime, zoneCode, condition);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL);
		}
	}
	
	public Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(Date deliveryDate, Date cutOffTime, String zoneCode, EnumLogicalOperator condition, boolean filterTimeslots) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getTimeslotsByDateEx(deliveryDate, cutOffTime, zoneCode, condition, filterTimeslots);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL);
		}
	}
	
	public List<IUnassignedModel> getUnassigned(Date deliveryDate, Date cutOffTime, String zoneCode, EnumLogicalOperator condition) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getUnassigned(deliveryDate, cutOffTime, zoneCode, condition);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_UNASSIGNED_UNSUCCESSFUL);
		}
	}
	
	public IOrderModel getRoutingOrderByReservation(String reservationId) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getRoutingOrderByReservation(reservationId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_UNASSIGNED_UNSUCCESSFUL);
		}
	}
	
	public int updateRoutingOrderByReservation(String reservationId, double orderSize, double serviceTime) throws RoutingServiceException  {
		try {
			return deliveryDAOImpl.updateRoutingOrderByReservation(reservationId, orderSize, serviceTime);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_UNASSIGNED_UNSUCCESSFUL);
		}
	}
	
	public int updateTimeslotForStatus(String rootId, boolean isClosed, String type, Date baseDate, String cutOff) throws RoutingServiceException {
		try {
			if("2".equalsIgnoreCase(type)) {
				return deliveryDAOImpl.updateTimeslotForStatusByRegion(baseDate, rootId, cutOff, isClosed);
			} else if("1".equalsIgnoreCase(type)) {
				return deliveryDAOImpl.updateTimeslotForStatusByZone(baseDate, rootId, cutOff, isClosed);
			} else {
				return deliveryDAOImpl.updateTimeslotForStatus(rootId, isClosed);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_UNASSIGNED_UNSUCCESSFUL);
		}
	}
	
	public int updateTimeslotForDynamicStatus(String rootId, boolean isDynamic, String type, Date baseDate, String cutOff) throws RoutingServiceException {
		try {
			if("2".equalsIgnoreCase(type)) {
				return deliveryDAOImpl.updateTimeslotForDynamicStatusByRegion(baseDate, rootId, cutOff, isDynamic);
			} else if("1".equalsIgnoreCase(type)) {
				return deliveryDAOImpl.updateTimeslotForDynamicStatusByZone(baseDate, rootId, cutOff, isDynamic);
			} else {
				return deliveryDAOImpl.updateTimeslotForDynamicStatus(rootId, isDynamic);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_UNASSIGNED_UNSUCCESSFUL);
		}
	}
	
	public IZoneModel getDeliveryZone(String zoneCode)  throws RoutingServiceException  {
		IZoneModel zoneModel = null;
		Map zoneMapping = getDeliveryZoneDetails();
		if(zoneMapping != null) {
			zoneModel = (IZoneModel)zoneMapping.get(zoneCode);
		}
		return zoneModel;
	}
	
	public List<IRouteModel> retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription, boolean retrieveBlankStops) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			RoutingSession[] sessions = port.retrieveRoutingSessionsByCriteria(RoutingDataEncoder.encodeRoutingSessionCriteria(schedulerId, sessionDescription)
													, RoutingDataEncoder.encodeRouteInfoRetrieveFullOptions());			
			if(sessions != null) {
				for(RoutingSession session : sessions) {
					RoutingRoute[] routes = session.getRoutes();
					return RoutingDataDecoder.decodeRouteListEx(routes, retrieveBlankStops);
				}
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new RoutingServiceException(exp, IIssue.PROCESS_RETRIEVESESSION_UNSUCCESSFUL);
		}
		
		return null;
	}
	
	public List<IOrderModel> getRoutingOrderByDate(Date deliveryDate, String zoneCode, boolean filterExpiredCancelled) throws RoutingServiceException {
		try {
			return deliveryDAOImpl.getRoutingOrderByDate(deliveryDate, zoneCode, filterExpiredCancelled);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_UNASSIGNED_UNSUCCESSFUL);
		}
	}
	
	public List<IDeliverySlot> getTimeslots(Date deliveryDate, Date cutOffTime, double latitude, double longitude, final String serviceType) throws RoutingServiceException {
		try{
			return deliveryDAOImpl.getTimeslots(deliveryDate, cutOffTime, latitude, longitude, serviceType);
		}catch(SQLException e){
			throw new RoutingServiceException(e, IIssue.PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL);
		}
	}
		
	public List<UnassignedDlvReservationModel> getUnassignedReservations(Date deliveryDate, Date cutOff) throws RoutingServiceException{
		try{
			return deliveryDAOImpl.getUnassignedReservations(deliveryDate, cutOff);
		}catch(SQLException e){
			throw new RoutingServiceException(e, "Unable to pull unassigned");
		}
	}
	public Map<String, DepotLocationModel> getDepotLocations() throws RoutingServiceException{
		try{
			return deliveryDAOImpl.getDepotLocations();
		}catch(SQLException e){
			throw new RoutingServiceException(e, "Unable to pull depots");
		}
	}

	@Override
	public DlvTimeslotModel getTimeslotById(String timeslotId) throws RoutingServiceException {
		try{
			return deliveryDAOImpl.getTimeslotById(timeslotId);
		}catch(SQLException e){
			throw new RoutingServiceException(e, "Unable to pull timeslots");
		}
	}

	@Override
	public IDeliveryReservation reserveTimeslotEx(DlvReservationModel reservation,
			ContactAddressModel address, FDTimeslot timeslot,
			TimeslotEventModel event) throws RoutingServiceException  {
		// TODO Auto-generated method stub


		IDeliveryReservation _reservation = null;

		if(reservation == null || address == null || timeslot.getDlvTimeslot().getRoutingSlot()	== null 
														|| !timeslot.getDlvTimeslot().getRoutingSlot().isDynamicActive()) {
			return null;
		}
		if (RoutingActivityType.RESERVE_TIMESLOT.equals(reservation.getUnassignedActivityType())) {
			if(reservation.getStatusCode() == 5) {
				_reservation = doReserveEx(reservation, address, timeslot, event);
			} else if(reservation.getStatusCode() == 15 || reservation.getStatusCode() == 20) {
				clearUnassignedInfo(reservation.getId());
			} else if(reservation.getStatusCode() == 10) {
				_reservation = doReserveEx(reservation, address, timeslot, event);
				if( _reservation != null && _reservation.isReserved()) {
					doConfirmEx(reservation, address, event);
				}
			}
		} else {
			_reservation = doReserveEx(reservation, address, timeslot, event);
		}

		return _reservation;
	
	}
	
	private void doConfirmEx(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) {

		long startTime=System.currentTimeMillis();
		IOrderModel order= com.freshdirect.routing.util.RoutingUtil.getOrderModel(reservation, address, reservation.getOrderId(), reservation.getId());

		try {
            com.freshdirect.routing.util.RoutingUtil.confirmTimeslot(reservation, order);
			long endTime=System.currentTimeMillis();
			/*event = buildEvent(null, event, reservation,order,address, EventType.CONFIRM_TIMESLOT,(int)(endTime-startTime));
			if(event!=null && event.getId()!=null)
				logTimeslots(event);*/
			if(reservation.isUnassigned()) {
				clearUnassignedInfo(reservation.getId());
			}

		} catch (Exception e) {
			/*event = buildEvent(null, event, reservation,order,address, EventType.CONFIRM_TIMESLOT,0);
			if(event!=null && event.getId()!=null)
				logTimeslots(event);*/
			e.printStackTrace();
			LOGGER.debug("Exception in commitReservationEx():"+order.getOrderNumber()+"-->"+e.toString());
			setUnassignedInfo(reservation.getId(),RoutingActivityType.CONFIRM_TIMESLOT);
		}
	}


	public void setUnassignedInfo(String reservationId, RoutingActivityType activity ) throws RoutingServiceException  {
		try{
			deliveryDAOImpl.setUnassignedInfo(reservationId, activity.value());
		}catch(SQLException e){
			throw new RoutingServiceException(e, "Unable to pull timeslots");
		}
		
		

	}
	
	
	
	public void clearUnassignedInfo(String reservationId) throws RoutingServiceException  {
		try{
			 deliveryDAOImpl.clearUnassignedInfo(reservationId);
		}catch(SQLException e){
			throw new RoutingServiceException(e, "Unable to pull timeslots");
		}
	}
	
	
	private IDeliveryReservation doReserveEx(DlvReservationModel reservation,ContactAddressModel address , FDTimeslot timeslot, TimeslotEventModel event) {

		long startTime=System.currentTimeMillis();
		IOrderModel order= RoutingUtil.getOrderModel(reservation, address, reservation.getOrderId(), reservation.getId());
		IDeliveryReservation _reservation=null;
		try {
			 _reservation = RoutingUtil.reserveTimeslot(reservation, order, timeslot);
			long endTime=System.currentTimeMillis();
			/*event = buildEvent(null, event, reservation,order,address, EventType.RESERVE_TIMESLOT, (int)(endTime-startTime));
			if(event!=null && event.getId()!=null)
				logTimeslots(event);*/
			if(_reservation==null || !_reservation.isReserved()) {
					setUnassignedInfo(reservation.getId(), RoutingActivityType.RESERVE_TIMESLOT);					
			} else {
				    this.setReservationReservedMetrics(reservation.getId()
															, order.getDeliveryInfo().getCalculatedOrderSize()
															, order.getDeliveryInfo().getCalculatedServiceTime().getOrderServiceTime()
															, EnumRoutingUpdateStatus.SUCCESS);
					clearUnassignedInfo(reservation.getId());
					if(!EnumRoutingUpdateStatus.OVERRIDDEN.equals(reservation.getUpdateStatus())
												&& order != null && order.getDeliveryInfo() != null 
																&& order.getDeliveryInfo().getPackagingDetail() != null
																	&& reservation.getMetricsSource() == null) {
						setReservationMetricsDetails(reservation.getId()
														, order.getDeliveryInfo().getPackagingDetail().getNoOfCartons()
														, order.getDeliveryInfo().getPackagingDetail().getNoOfCases()
														, order.getDeliveryInfo().getPackagingDetail().getNoOfFreezers()
														, order.getDeliveryInfo().getPackagingDetail().getSource());
					}
			}

		} catch (Exception e) {
			/*event = buildEvent(null, event, reservation,order,address, EventType.RESERVE_TIMESLOT, 0);
			if(event!=null && event.getId()!=null)
				logTimeslots(event);*/
			e.printStackTrace();
			LOGGER.debug("Exception in reserveTimeslotEx():"+e.toString());
			setUnassignedInfo(reservation.getId(),RoutingActivityType.RESERVE_TIMESLOT);			
		}
		setRoutingIndicator(reservation.getId(), order.getOrderNumber());
		return _reservation;
	}
	


	public void setReservationMetricsDetails(String reservationId, long noOfCartons, long noOfCases
												, long noOfFreezers, EnumOrderMetricsSource source) throws RoutingServiceException  {
		

		try{
			 deliveryDAOImpl.setReservationMetricsDetails(reservationId, noOfCartons, noOfCases
						, noOfFreezers, source);
		}catch(SQLException e){
			throw new RoutingServiceException(e, "SQLException in DlvManagerDAO.setReservationMetricsDetails_Source() call ");
		}

	}
	

	public void setRoutingIndicator(String reservationId, String orderNum ) throws RoutingServiceException  {
		
		try{
			 deliveryDAOImpl.setInUPS(reservationId,orderNum );
		}catch(SQLException e){
			throw new RoutingServiceException(e, "SQLException in DlvManagerDAO.setUPSIndicator() call ");
		}
	}

	
	public void setReservationReservedMetrics(String reservationId, double reservedOrderSize
													, double reservedServiceTime, EnumRoutingUpdateStatus status) throws RoutingServiceException  {

		try{
			 deliveryDAOImpl.setReservationReservedMetrics(reservationId, reservedOrderSize, reservedServiceTime, status.value());
		}catch(SQLException e){
			throw new RoutingServiceException(e, "SQLException in DlvManagerDAO.setUPSIndicator() call ");
		}
	}

	public void commitReservationEx(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) {

		if(reservation==null || address==null ||!reservation.isInUPS()/*|| reservation.getUnassignedActivityType().*/)
			return ;

		if (RoutingActivityType.CONFIRM_TIMESLOT.equals(reservation.getUnassignedActivityType())) {
			if(reservation.getStatusCode() == 10) {
				doConfirmEx(reservation, address, event);
			} else if(reservation.getStatusCode() == 15 || reservation.getStatusCode() == 20) {
				doReleaseReservationEx(reservation, address,event);				
			} /*else if(reservation.getStatusCode() == 5) {
				updateReservationEx(reservation, address);
			}*/
		} else if (reservation.getUnassignedActivityType() == null){
			doConfirmEx(reservation, address, event);
		}	else if (RoutingActivityType.CANCEL_TIMESLOT.equals(reservation.getUnassignedActivityType())) {
			doReleaseReservationEx(reservation, address,event);
		}
	}
	
	private void doReleaseReservationEx(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) {

		long startTime=System.currentTimeMillis();
		if(reservation==null || address==null ||!reservation.isInUPS())
			return ;

		IOrderModel order= RoutingUtil.getOrderModel(reservation, address, reservation.getOrderId(), reservation.getId());
		try {
            RoutingUtil.cancelTimeslot(reservation, order);
            long endTime=System.currentTimeMillis();
			/*event = buildEvent(null,event, reservation,order,address, EventType.CANCEL_TIMESLOT,(int)(endTime-startTime));
			if(event!=null && event.getId()!=null)
				logTimeslots(event);*/
			if(reservation.isUnassigned()) {
				clearUnassignedInfo(reservation.getId());
			}

		} catch (Exception e) {
			/*event = buildEvent(null,event, reservation,order,address, EventType.CANCEL_TIMESLOT,0);
			if(event!=null && event.getId()!=null)
				logTimeslots(event);*/
			e.printStackTrace();
			LOGGER.debug("Exception in releaseReservationEx() reservationId="+reservation.getId()+":"+e.toString());
			setUnassignedInfo(reservation.getId(),RoutingActivityType.CANCEL_TIMESLOT);
		}

	}

	@Override
	public void updateReservationEx(DlvReservationModel reservation,
			ContactAddressModel address, FDTimeslot timeslot) {

		if(reservation==null || !reservation.isInUPS() || reservation.getUnassignedActivityType() != null)
			return ;
		IOrderModel order = RoutingUtil.getOrderModel(reservation, address, reservation.getOrderId(), reservation.getId());
		
		try {
			if(reservation.getStatusCode() == 15 || reservation.getStatusCode() == 20) {
				setReservationMetricsStatus(reservation.getId(), EnumRoutingUpdateStatus.SUCCESS);
			} else {
				order = RoutingUtil.calculateReservationSize(reservation, order, timeslot);
				if(((reservation.getReservedOrderSize() != null
						&& order.getDeliveryInfo().getCalculatedOrderSize() < reservation.getReservedOrderSize())
						|| (reservation.getReservedServiceTime() != null
								&& order.getDeliveryInfo().getCalculatedServiceTime().getOrderServiceTime() < reservation.getReservedServiceTime()))
								&& (timeslot != null && DateUtil.getDiffInMinutes(Calendar.getInstance().getTime()
														,(timeslot.getDlvTimeslot().getPremiumCutoffTime()!=null)?timeslot.getDlvTimeslot().getPremiumCutoffAsDate():timeslot.getDlvTimeslot().getCutoffTimeAsDate())
										> RoutingServicesProperties.getOMUseOriginalThreshold()) 
								&& !EnumRoutingUpdateStatus.OVERRIDDEN.equals(reservation.getUpdateStatus())) {
						return;
					
				} else {
					
					boolean isUpdated = RoutingUtil.updateReservation(reservation, order);
					
					if(isUpdated) {
						setReservationReservedMetrics(reservation.getId(), order.getDeliveryInfo().getCalculatedOrderSize()
															, order.getDeliveryInfo().getCalculatedServiceTime().getOrderServiceTime()
															, EnumRoutingUpdateStatus.SUCCESS);
					} else {
						if(!EnumRoutingUpdateStatus.OVERRIDDEN.equals(reservation.getUpdateStatus())) {
							this.setReservationMetricsStatus(reservation.getId(), EnumRoutingUpdateStatus.FAILED);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Exception in updateReservationEx():"+e.toString());
			e.printStackTrace();			
		}
	}

	public void setReservationMetricsStatus(String reservationId, EnumRoutingUpdateStatus status) throws RoutingServiceException {
		

		try{
			 deliveryDAOImpl.setReservationMetricsStatus(reservationId, status.value());
		}catch(SQLException e){
			throw new RoutingServiceException(e, "SQLException in setReservationMetricsStatus() call ");
		}
	}


	@Override
	public List<UnassignedDlvReservationModel> getUnassignedReservationsEx(
			Date deliveryDate, Date cutOff) {
		try{
			return deliveryDAOImpl.getUnassignedReservationsEx(deliveryDate, cutOff);
		}catch(SQLException e){
			throw new RoutingServiceException(e, "Unable to pull unassigned");
		}
	}

	@Override
	public void flagExpiredReservations() {

		try{
			deliveryDAOImpl.flagExpiredReservations();
		}catch(SQLException e){
			LOGGER.debug("Unable to flag expired reservations."+e.toString());
		}
		
	}
	
	public void updateTimeslotMetrics(List<TimeslotCapacityModel> timeslotMetrics) throws RoutingServiceException {
		try {
			deliveryDAOImpl.updateTimeslotMetrics(timeslotMetrics);
		} catch (SQLException e) {
			throw new RoutingServiceException(e,
					"SQLException in updateTimeslotMetrics() call ");
		}
	}

}
