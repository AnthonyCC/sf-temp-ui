package com.freshdirect.routing.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.dao.IDeliveryDetailsDAO;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.model.IServiceTimeModel;
import com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetPortType;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_PortType;
import com.freshdirect.routing.service.IDeliveryService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingDataDecoder;
import com.freshdirect.routing.util.RoutingDataEncoder;
import com.freshdirect.routing.util.ServiceTimeUtil;

public class DeliveryService implements IDeliveryService {
	
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
			throw new RoutingServiceException(e, IIssue.PROCESS_ZONEINFO_NOTFOUND);
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
			TransportationWebService_PortType port = RoutingServiceLocator.getInstance().getTransportationSuiteService();
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
			RouteNetPortType port = RoutingServiceLocator.getInstance().getRouteNetService();
			return RoutingDataDecoder.decodeDrivingDirection(port.buildDriverDirections(RoutingDataEncoder.encodeGeoPointList(destinations)
												, new DriverDirectionsOptions()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
}
