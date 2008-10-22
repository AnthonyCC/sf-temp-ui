package com.freshdirect.routing.service;

import java.util.Map;

import com.freshdirect.routing.model.IDeliveryModel;
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
	
}
