package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.dao.IDeliveryDetailsDAO;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.service.IDeliveryService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDataDecoder;
import com.freshdirect.routing.util.RoutingDataEncoder;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.ServiceTimeUtil;

public class DeliveryService extends BaseService implements IDeliveryService {
	
	private IDeliveryDetailsDAO deliveryDAOImpl;
	
	public IDeliveryDetailsDAO getDeliveryDAOImpl() {
		return deliveryDAOImpl;
	}

	public void setDeliveryDAOImpl(IDeliveryDetailsDAO deliveryDAOImpl) {
		this.deliveryDAOImpl = deliveryDAOImpl;
	}

	public double getServiceTime(IDeliveryModel model, String serviceTimeFactorExpression
											, String serviceTimeExpression) throws RoutingServiceException {
		String serviceTimeType = model.getDeliveryLocation().getServiceTimeType();
		String zoneType = model.getDeliveryZone().getZoneType();
				
		try {
			IServiceTimeModel serviceTime = deliveryDAOImpl.getServiceTime(serviceTimeType,zoneType);	
			if(serviceTime == null) {				
				throw new RoutingServiceException(null, IIssue.PROCESS_SERVICETIME_NOTFOUND);
			}
			double valX = ServiceTimeUtil.evaluateExpression(serviceTimeFactorExpression
					, ServiceTimeUtil.getServiceTimeFactorParams(model.getPackagingInfo()));			
			return ServiceTimeUtil.evaluateExpression(serviceTimeExpression
					, ServiceTimeUtil.getServiceTimeParams(serviceTime.getFixedServiceTime(), serviceTime.getVariableServiceTime(), valX ));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_SERVICETIME_NOTFOUND);
		}	
		
	}
	
	public double getServiceTime(IDeliveryModel model, String serviceTimeFactorExpression
									, String serviceTimeExpression, int intFixedServiceTime, int intVariableServiceTime) throws RoutingServiceException {
		double valX = ServiceTimeUtil.evaluateExpression(serviceTimeFactorExpression
				, ServiceTimeUtil.getServiceTimeFactorParams(model.getPackagingInfo()));			
		return ServiceTimeUtil.evaluateExpression(serviceTimeExpression
				, ServiceTimeUtil.getServiceTimeParams(intFixedServiceTime, intVariableServiceTime, valX ));
		
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
	
	public List getRoutes(Date routeDate, String internalSessionID, String routeID) throws RoutingServiceException {
		try {
			TransportationWebService port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
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
	
	public double estimateOrderServiceTime(IOrderModel orderModel, IServiceTimeScenarioModel scenario)  throws RoutingServiceException  {
				
		IDeliveryModel model = orderModel.getDeliveryInfo();
		
		String zoneType = model.getDeliveryZone().getZoneType();
		
		if(zoneType == null || zoneType.trim().length() == 0) {
			zoneType = scenario.getDefaultZoneType();
			model.getDeliveryZone().setZoneType(zoneType);
		}
		
		String serviceTimeType = model.getDeliveryLocation().getServiceTimeType();
		if(serviceTimeType == null || serviceTimeType.trim().length() == 0) {
			serviceTimeType = scenario.getDefaultServiceTimeType();
			model.getDeliveryLocation().setServiceTimeType(serviceTimeType);			

		}
		double serviceTime = 0.0;
		try {
			serviceTime = getServiceTime(model, scenario.getServiceTimeFactorFormula(), scenario.getServiceTimeFormula());
		} catch(RoutingServiceException e) {			
			try {
				serviceTime = getServiceTime(model,scenario.getServiceTimeFactorFormula()
													, scenario.getServiceTimeFormula(), RoutingServicesProperties.getDefaultFixedServiceTime()
													, RoutingServicesProperties.getDefaultVariableServiceTime());
			} catch(RoutingServiceException ex) {
				ex.printStackTrace();
			}
		}
		
		return serviceTime;		
	}
	
	public Map<String, List<IDeliverySlot>> getTimeslotsByDate(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getTimeslotsByDate(deliveryDate, cutOffTime, zoneCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL);
		}
	}
	
	public Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getTimeslotsByDateEx(deliveryDate, cutOffTime, zoneCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RoutingServiceException(e, IIssue.PROCESS_LOADLATEDELIVERYORDERS_UNSUCCESSFUL);
		}
	}
	
	public List<IOrderModel> getUnassigned(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws RoutingServiceException {
		
		try {
			return deliveryDAOImpl.getUnassigned(deliveryDate, cutOffTime, zoneCode);
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
		
}
