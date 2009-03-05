package com.freshdirect.routing.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IDeliveryService {
	
	double getServiceTime(IDeliveryModel model, String serviceTimeFactorExpression
			, String serviceTimeExpression) throws RoutingServiceException;
	
	IDeliveryModel getDeliveryInfo(String saleId) throws RoutingServiceException;
	
	String getDeliveryType(String zoneCode) throws RoutingServiceException;
	
	String getDeliveryZoneType(String zoneCode) throws RoutingServiceException;	
	
	double getServiceTime(IDeliveryModel model, String serviceTimeFactorExpression
			, String serviceTimeExpression, int intFixedServiceTime, int intVariableServiceTime) throws RoutingServiceException;
	
	Map getDeliveryZoneDetails()  throws RoutingServiceException;
	
	List getLateDeliveryOrders(String query) throws RoutingServiceException;
	
	List getRoutes(Date routeDate, String internalSessionID, String routeID) throws RoutingServiceException;
	
	IDrivingDirection buildDriverDirections(List destinations)  throws RoutingServiceException;
	
}
