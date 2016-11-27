package com.freshdirect.routing.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.DepotLocationModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.UnassignedDlvReservationModel;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.routing.constants.RoutingActivityType;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTime;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IUnassignedModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.TimeslotCapacityModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public interface IDeliveryService {
	
	String CONSTANT_ALLZONES = "ALL";
	
	IDeliveryModel getDeliveryInfo(String saleId) throws RoutingServiceException;
	
	String getDeliveryType(String zoneCode) throws RoutingServiceException;
	
	String getDeliveryZoneType(String zoneCode) throws RoutingServiceException;	
	
	IServiceTime getServiceTime(IOrderModel orderModel, IServiceTimeScenarioModel scenario, RoutingActivityType routingType) throws RoutingServiceException;
	
	Map getDeliveryZoneDetails()  throws RoutingServiceException;
	
	List getLateDeliveryOrders(String query) throws RoutingServiceException;
	
	List getRoutes(Date routeDate, String internalSessionID, String routeID, String regionId) throws RoutingServiceException;
	
	IDrivingDirection buildDriverDirections(Set<String> routeIDs, String sessionID, IRoutingSchedulerIdentity schedulerId)  throws RoutingServiceException;
		
	IZoneModel getDeliveryZone(String zoneCode)  throws RoutingServiceException ;
	
	Map<String, List<IDeliverySlot>> getTimeslotsByDate(Date deliveryDate, Date cutOffTime, String zoneCode, EnumLogicalOperator condition) throws RoutingServiceException;
	
	Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(Date deliveryDate, Date cutOffTime, String zoneCode, EnumLogicalOperator condition, boolean filterTimeslots) throws RoutingServiceException;
	
	List<IUnassignedModel> getUnassigned(Date deliveryDate, Date cutOffTime, String zoneCode, EnumLogicalOperator condition) throws RoutingServiceException;
	
	IOrderModel getRoutingOrderByReservation(String reservationId) throws RoutingServiceException;
	
	int updateRoutingOrderByReservation(String reservationId, double orderSize, double serviceTime) throws RoutingServiceException;
	
	int updateTimeslotForStatus(String timeslotId, boolean isClosed, String type, Date baseDate, String cutOff) throws RoutingServiceException;
	
	int updateTimeslotForDynamicStatus(String timeslotId, boolean isDynamic, String type, Date baseDate, String cutOff) throws RoutingServiceException;
	
	List<IRouteModel> retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription, boolean retrieveBlankStops) throws RoutingServiceException;
	
	List<IOrderModel> getRoutingOrderByDate(Date deliveryDate, String zoneCode, boolean filterExpiredCancelled) throws RoutingServiceException;
	
	List<IDeliverySlot> getTimeslots(Date deliveryDate, Date cutOffTime, double latitude, double longitude, final String serviceType) throws RoutingServiceException;
	
	List<UnassignedDlvReservationModel> getUnassignedReservations(Date deliveryDate, Date cutOff) throws RoutingServiceException;
	
	Map<String, DepotLocationModel> getDepotLocations() throws RoutingServiceException;

	DlvTimeslotModel getTimeslotById(String timeslotId);

	IDeliveryReservation reserveTimeslotEx(DlvReservationModel reservation,
			ContactAddressModel address, FDTimeslot _timeslot,
			TimeslotEventModel event);

	void commitReservationEx(DlvReservationModel reservation,
			ContactAddressModel address, TimeslotEventModel event);

	void updateReservationEx(DlvReservationModel reservation,
			ContactAddressModel address, FDTimeslot _timeslot);

	List<UnassignedDlvReservationModel> getUnassignedReservationsEx(
			Date deliveryDate, Date cutOff);

	void flagExpiredReservations();
	
	void updateTimeslotMetrics(List<TimeslotCapacityModel> timeslotMetrics) throws RoutingServiceException;
}
