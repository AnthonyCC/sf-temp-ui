package com.freshdirect.routing.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IUnassignedModel;
import com.freshdirect.routing.model.IZoneModel;
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
	
	double estimateOrderServiceTime(IOrderModel orderModel, IServiceTimeScenarioModel scenario)  throws RoutingServiceException;
	
	IZoneModel getDeliveryZone(String zoneCode)  throws RoutingServiceException ;
	
	Map<String, List<IDeliverySlot>> getTimeslotsByDate(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws RoutingServiceException;
	
	Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws RoutingServiceException;
	
	List<IUnassignedModel> getUnassigned(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws RoutingServiceException;
	
	IOrderModel getRoutingOrderByReservation(String reservationId) throws RoutingServiceException;
	
	int updateRoutingOrderByReservation(String reservationId, double orderSize, double serviceTime) throws RoutingServiceException;
	
	int updateTimeslotForStatus(String timeslotId, boolean isClosed, String type, Date baseDate, String cutOff) throws RoutingServiceException;
	
	int updateTimeslotForDynamicStatus(String timeslotId, boolean isDynamic, String type, Date baseDate, String cutOff) throws RoutingServiceException;
}
