package com.freshdirect.routing.service.proxy;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.DepotLocationModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.delivery.model.UnassignedDlvReservationModel;
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
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IUnassignedModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.service.IDeliveryService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class DeliveryServiceProxy extends BaseServiceProxy {
		
	public IDeliveryModel getDeliveryInfo(String saleId) throws RoutingServiceException {
		
		return getService().getDeliveryInfo(saleId);	
	}
	
	public String getDeliveryType(String zoneCode) throws RoutingServiceException {
		return getService().getDeliveryType(zoneCode);
	}
	
	public String getDeliveryZoneType(String zoneCode) throws RoutingServiceException {
		return getService().getDeliveryZoneType(zoneCode);
	}
	
	public IDeliveryService getService() {
		return RoutingServiceLocator.getInstance().getDeliveryService();
	}
	
	public double getServiceTime(IOrderModel orderModel, IServiceTimeScenarioModel scenario, RoutingActivityType routingType) throws RoutingServiceException {
		return getService().getServiceTime(orderModel, scenario, routingType);	
	}
	
	public Map getDeliveryZoneDetails()  throws RoutingServiceException {
		return getService().getDeliveryZoneDetails();
	}
	
	public List getLateDeliveryOrders(String query) throws RoutingServiceException {
		return getService().getLateDeliveryOrders(query);
	}
	
	public List getRoutes(Date routeDate, String internalSessionID, String routeID, String regionId) throws RoutingServiceException {
		return getService().getRoutes(routeDate, internalSessionID, routeID, regionId);
	}
	
	public IDrivingDirection buildDriverDirections(Set<String> routeIDs, String sessionID, IRoutingSchedulerIdentity schedulerId)  throws RoutingServiceException {
		return getService().buildDriverDirections(routeIDs, sessionID, schedulerId);
	}
	
	public IZoneModel getDeliveryZone(String zoneCode)  throws RoutingServiceException {
		return getService().getDeliveryZone(zoneCode);
	}
	
	public Map<String, List<IDeliverySlot>> getTimeslotsByDate(Date deliveryDate, Date cutOffTime, String zoneCode, EnumLogicalOperator condition) throws RoutingServiceException {
		return getService().getTimeslotsByDate(deliveryDate, cutOffTime, zoneCode, condition);
	}
	
	public Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(Date deliveryDate, Date cutOffTime, String zoneCode, EnumLogicalOperator condition) throws RoutingServiceException { 
		return getService().getTimeslotsByDateEx(deliveryDate, cutOffTime, zoneCode, condition);
	}
	
	public List<IUnassignedModel> getUnassigned(Date deliveryDate, Date cutOffTime, String zoneCode) throws RoutingServiceException {
		return getService().getUnassigned(deliveryDate, cutOffTime, zoneCode);
	}
	
	public IOrderModel getRoutingOrderByReservation(String reservationId) throws RoutingServiceException {
		return getService().getRoutingOrderByReservation(reservationId);
	}
	
	public int updateRoutingOrderByReservation(String reservationId, double orderSize, double serviceTime) throws RoutingServiceException {
		return getService().updateRoutingOrderByReservation(reservationId, orderSize, serviceTime);
	}
	
	public int updateTimeslotForStatus(String timeslotId, boolean isClosed, String type, Date baseDate, String cutOff) throws RoutingServiceException {
		return getService().updateTimeslotForStatus(timeslotId, isClosed, type, baseDate, cutOff);
	}
	
	public int updateTimeslotForDynamicStatus(String timeslotId, boolean isDynamic, String type, Date baseDate, String cutOff) throws RoutingServiceException {
		return getService().updateTimeslotForDynamicStatus(timeslotId, isDynamic, type, baseDate, cutOff);
	}
	
	public List<IRouteModel> retrieveRoutingSession(IRoutingSchedulerIdentity schedulerId, String sessionDescription, boolean retrieveBlankStops) throws RoutingServiceException {
		return getService().retrieveRoutingSession(schedulerId, sessionDescription, retrieveBlankStops);
	}
	
	public List<IOrderModel> getRoutingOrderByDate(Date deliveryDate, String zoneCode, boolean filterExpiredCancelled) throws RoutingServiceException {
		return getService().getRoutingOrderByDate(deliveryDate, zoneCode, filterExpiredCancelled);
	}
	
	public List<IDeliverySlot> getTimeslots(Date deliveryDate, Date cutOffTime, double latitude, double longitude, final String serviceType) throws RoutingServiceException {
		return getService().getTimeslots(deliveryDate, cutOffTime, latitude, longitude, serviceType);
	}
	public List<UnassignedDlvReservationModel> getUnassignedReservations(Date deliveryDate, Date cutOff) throws RoutingServiceException {
		return getService().getUnassignedReservations(deliveryDate, cutOff);
	}
	public Map<String, DepotLocationModel> getDepotLocations() throws RoutingServiceException {
		return getService().getDepotLocations();
	}

	public DlvTimeslotModel getTimeslotById(String timeslotId) {
		return getService().getTimeslotById(timeslotId);
	}

	public IDeliveryReservation reserveTimeslotEx(DlvReservationModel reservation,
			ContactAddressModel address, FDTimeslot _timeslot,
			TimeslotEventModel event) {
		return getService().reserveTimeslotEx(reservation,
				 address, _timeslot,
				 event);
		
	}

	public void commitReservationEx(DlvReservationModel reservation,
			ContactAddressModel address, TimeslotEventModel event) {
		getService().commitReservationEx(reservation, address, event);
		
	}

	public void updateReservationEx(DlvReservationModel reservation,
			ContactAddressModel address, FDTimeslot _timeslot) {
		getService().updateReservationEx(reservation, address,_timeslot);
		
	}

	public List<UnassignedDlvReservationModel> getUnassignedReservationsEx(
			Date deliveryDate, Date cutOff) {
		return getService().getUnassignedReservationsEx(deliveryDate, cutOff);
	}

	public void flagExpiredReservations() {
		getService().flagExpiredReservations();
	}
}
