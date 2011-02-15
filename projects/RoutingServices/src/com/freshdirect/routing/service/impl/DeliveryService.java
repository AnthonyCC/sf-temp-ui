package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.axis2.client.Stub;

import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.routing.dao.IDeliveryDetailsDAO;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IUnassignedModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.IZoneScenarioModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService;
import com.freshdirect.routing.proxy.stub.transportation.RoutingRoute;
import com.freshdirect.routing.proxy.stub.transportation.RoutingSession;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceStub;
import com.freshdirect.routing.service.IDeliveryService;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDataDecoder;
import com.freshdirect.routing.util.RoutingDataEncoder;
import com.freshdirect.routing.util.ServiceTimeUtil;

public class DeliveryService extends BaseService implements IDeliveryService {
	
	private IDeliveryDetailsDAO deliveryDAOImpl;
	
	public IDeliveryDetailsDAO getDeliveryDAOImpl() {
		return deliveryDAOImpl;
	}

	public void setDeliveryDAOImpl(IDeliveryDetailsDAO deliveryDAOImpl) {
		this.deliveryDAOImpl = deliveryDAOImpl;
	}

	
	public double getServiceTime(IOrderModel orderModel, IServiceTimeScenarioModel scenario) throws RoutingServiceException {
		
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
		if(location.getServiceTimeOverride() > 0) {
			return location.getServiceTimeOverride();
		} else if(building.getServiceTimeOverride() > 0) {
			return building.getServiceTimeOverride();
		} else if(zoneScenario != null && zoneScenario.getServiceTimeOverride() > 0) {
			return zoneScenario.getServiceTimeOverride();
		}
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
			if((valY + serviceTimeAdjustment) > 0) {
				valY = valY + serviceTimeAdjustment;
			} else {
				valY = 0;
			}
		}
		return valY;
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
	
	public static void main(String a[]) throws Exception {
		
		IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
		
		//String region = "MDP";
		//String sessionDescription = "chiguita_Depot_20100707_190849";
		
		String region = "FD";
		String sessionDescription = "chiguita_Trucks_20100707_190955";
		String url = "http://10.55.20.148:81";
		
		schedulerId.setRegionId(region);
				
		TransportationWebService port = new TransportationWebServiceStub(url);
		((Stub) port)._getServiceClient().getOptions().setTimeOutInMilliSeconds(30 * 1000);
		RoutingSession[] sessions = port.retrieveRoutingSessionsByCriteria(RoutingDataEncoder.encodeRoutingSessionCriteria(schedulerId, sessionDescription)
				, RoutingDataEncoder.encodeRouteInfoRetrieveFullOptions());			
		if(sessions != null) {
			for(RoutingSession session : sessions) {
				RoutingRoute[] routes = session.getRoutes();
				System.out.println(RoutingDataDecoder.decodeRouteList(routes));
			}
		}
		
	}
	
	public List getRoutes(Date routeDate, String internalSessionID, String routeID) throws RoutingServiceException {
		try {
			TransportationWebService port = getTransportationSuiteBatchService(null);
			return RoutingDataDecoder.decodeRouteList(port.retrieveRoutingRoutesByCriteria
																(RoutingDataEncoder.encodeRouteCriteria(routeDate, internalSessionID, routeID)
															, RoutingDataEncoder.encodeRouteInfoRetrieveOptionsEx())); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public IDrivingDirection buildDriverDirections(List destinations)  throws RoutingServiceException {
		try {
			RouteNetWebService port = getRouteNetBatchService();
			return RoutingDataDecoder.decodeDrivingDirection(port.buildDriverDirections(RoutingDataEncoder.encodeGeoPointList(destinations)
												, new DriverDirectionsOptions()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Map<String, List<IDeliverySlot>> getTimeslotsByDate(Date deliveryDate, Date cutOffTime, String zoneCode) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getTimeslotsByDate(deliveryDate, cutOffTime, zoneCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL);
		}
	}
	
	public Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(Date deliveryDate, Date cutOffTime, String zoneCode) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getTimeslotsByDateEx(deliveryDate, cutOffTime, zoneCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL);
		}
	}
	
	public List<IUnassignedModel> getUnassigned(Date deliveryDate, Date cutOffTime, String zoneCode) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getUnassigned(deliveryDate, cutOffTime, zoneCode);
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
	
	public List<IRouteModel> retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription) throws RoutingServiceException {
		
		try {
			TransportationWebService port = getTransportationSuiteBatchService(schedulerId);//RoutingServiceLocator.getInstance().getTransportationSuiteService();
			RoutingSession[] sessions = port.retrieveRoutingSessionsByCriteria(RoutingDataEncoder.encodeRoutingSessionCriteria(schedulerId, sessionDescription)
													, RoutingDataEncoder.encodeRouteInfoRetrieveFullOptions());			
			if(sessions != null) {
				for(RoutingSession session : sessions) {
					RoutingRoute[] routes = session.getRoutes();
					return RoutingDataDecoder.decodeRouteList(routes);
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
		
}
