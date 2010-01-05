package com.freshdirect.routing.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.axis2.databinding.types.Time;

import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IGeoPoint;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.proxy.stub.roadnet.MapPoint;
import com.freshdirect.routing.proxy.stub.transportation.Address;
import com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow;
import com.freshdirect.routing.proxy.stub.transportation.Location;
import com.freshdirect.routing.proxy.stub.transportation.LocationIdentity;
import com.freshdirect.routing.proxy.stub.transportation.RouteIdentity;
import com.freshdirect.routing.proxy.stub.transportation.RoutingDetailLevel;
import com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder;
import com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria;
import com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions;
import com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria;
import com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerBalancingFactor;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerCalculateDeliveryWindowMetrics;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowBase;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsType;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerRouteBalancingOptions;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions;
import com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions;
import com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptionsType;
import com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue;

public class RoutingDataEncoder {
	
	private static Calendar baseCalendar = Calendar.getInstance();
	public static Location[] encodeLocationList(Collection lstOrders, String region, String locationType) {
		
		Location[] result = null;
		if(lstOrders != null) {
			result = new Location[lstOrders.size()];
			Iterator tmpIterator = lstOrders.iterator();
			IOrderModel orderModel = null;			
			
			int intCount = 0;
			while(tmpIterator.hasNext()) {
				orderModel = (IOrderModel)tmpIterator.next();
				if(orderModel != null && orderModel.getDeliveryInfo() != null 
											&& orderModel.getDeliveryInfo().getDeliveryLocation() != null) {
					String areaCode = null;
					if(orderModel.getDeliveryInfo().getDeliveryZone() != null) {
						areaCode = orderModel.getDeliveryInfo().getDeliveryZone().getArea().getAreaCode();						
					} 
					result[intCount++] = encodeLocation(orderModel.getDeliveryInfo().getDeliveryLocation()
															, region
															, locationType, areaCode);
				}
			}
		}
		return result;
	}
	
	public static DeliveryAreaOrder[] encodeOrderList(Collection lstOrders, IRoutingSchedulerIdentity schedulerId
														, String region, String locationType
														, String orderType) {
		
		DeliveryAreaOrder[] result = null;
		if(lstOrders != null) {
			result = new DeliveryAreaOrder[lstOrders.size()];
			Iterator tmpIterator = lstOrders.iterator();
			IOrderModel orderModel = null;			
			int intCount = 0;
			while(tmpIterator.hasNext()) {
				orderModel = (IOrderModel)tmpIterator.next();
				if(orderModel != null && orderModel.getDeliveryInfo() != null 
											&& orderModel.getDeliveryInfo().getDeliveryLocation() != null) {					
					result[intCount++] = encodeOrder(schedulerId, orderModel, locationType, orderType, true);
				}
			}
		}
		return result;
	}
	
	public static RoutingImportOrder[] encodeImportOrderList(IRoutingSchedulerIdentity schedulerId, String sessionId, Collection lstOrders) {
		
		RoutingImportOrder[] result = null;
		if(lstOrders != null) {
			result = new RoutingImportOrder[lstOrders.size()];
			Iterator tmpIterator = lstOrders.iterator();
			DeliveryAreaOrder orderModel = null;			
			int intCount = 0;
			while(tmpIterator.hasNext()) {
				orderModel = (DeliveryAreaOrder)tmpIterator.next();
				if(orderModel != null) {					
					result[intCount++] = encodeImportOrder(schedulerId, sessionId, orderModel);
				}
			}
		}
		return result;
	}

	public static RoutingImportOrder encodeImportOrder(IRoutingSchedulerIdentity schedulerId
															, String sessionId, DeliveryAreaOrder orderModel) {
		
		RoutingImportOrder order = new RoutingImportOrder();
		order.setOrderNumber(orderModel.getIdentity().getOrderNumber() != null 
								? orderModel.getIdentity().getOrderNumber().toUpperCase() : 
									orderModel.getIdentity().getOrderNumber());
		order.setSessionIdentity(encodeRoutingSessionIdentity(schedulerId.getRegionId(), sessionId));
		order.setDeliveryDate(orderModel.getIdentity().getDeliveryDate());
		order.setLocationIdentity(encodeLocationIdentity(orderModel.getIdentity().getRegionId()
										,orderModel.getLocationType(),orderModel.getLocationId()));
		order.setTw1Open(orderModel.getDeliveryWindowStart());
		order.setTw1Close(orderModel.getDeliveryWindowEnd());
		
		
		CategoryQuantities quantities = new CategoryQuantities();
		quantities.setSize1(orderModel.getQuantity());
		quantities.setSize2(0);
		quantities.setSize3(0);
		
		quantities.setSubtotalCategory1(null);
		quantities.setSubtotalCategory2(null);
		quantities.setSubtotalCategory3(null);
		order.setQuantities(quantities);
		
		order.setAdditionalServiceTime(new Integer(orderModel.getServiceTime()));
				
		return order;
	}

	public static DeliveryAreaOrder encodeOrder(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel
													, String locationType
													, String orderType
													, boolean needTimeSlot) {
		
		DeliveryAreaOrder order = new DeliveryAreaOrder();
		order.setIdentity(encodeDeliveryAreaOrderIdentity(schedulerId.getRegionId(),schedulerId.getArea().getAreaCode()
				,schedulerId.getDeliveryDate(), orderModel.getDeliveryInfo().getReservationId()));
		order.setOrderType(orderType);
		
		order.setReservedTime(baseCalendar);
		order.setConfirmed(true);
		
		order.setQuantity((int)orderModel.getDeliveryInfo().getPackagingInfo().getTotalSize1());
		
		if(needTimeSlot) {
			order.setDeliveryWindowStart(getTime(orderModel.getDeliveryInfo().getDeliveryStartTime()));
			order.setDeliveryWindowEnd(getTime(orderModel.getDeliveryInfo().getDeliveryEndTime()));
		}
		order.setServiceTime((int)(orderModel.getDeliveryInfo().getServiceTime()*60));
		order.setLocationId(orderModel.getDeliveryInfo().getDeliveryLocation().getLocationId());
		order.setLocationType(locationType);
		order.setDescription(orderModel.getOrderNumber());
		
		order.setLatitude((int)(getVal(orderModel.getDeliveryInfo().getDeliveryLocation()
								.getGeographicLocation().getLatitude())*1000000));
		order.setLongitude((int)(getVal(orderModel.getDeliveryInfo().getDeliveryLocation()
								.getGeographicLocation().getLongitude())*1000000));
		return order;
	}
	
	public static DeliveryAreaOrder encodeAnalyzeOrder(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel
			, String locationType
			, String orderType) {

		DeliveryAreaOrder order = new DeliveryAreaOrder();
		order.setIdentity(encodeDeliveryAreaOrderIdentity(schedulerId.getRegionId(),schedulerId.getArea().getAreaCode()
				,schedulerId.getDeliveryDate(), orderModel.getDeliveryInfo().getReservationId()));
		order.setOrderType(orderType);
		order.setReservedTime(baseCalendar);
		
		order.setQuantity((int)orderModel.getDeliveryInfo().getPackagingInfo().getTotalSize1());

		order.setServiceTime((int)(orderModel.getDeliveryInfo().getServiceTime()*60));
		order.setLocationId(orderModel.getDeliveryInfo().getDeliveryLocation().getLocationId());
		order.setLocationType(locationType);
		order.setDescription(orderModel.getOrderNumber());

		order.setLatitude((int)(getVal(orderModel.getDeliveryInfo().getDeliveryLocation()
				.getGeographicLocation().getLatitude())*1000000));
		order.setLongitude((int)(getVal(orderModel.getDeliveryInfo().getDeliveryLocation()
				.getGeographicLocation().getLongitude())*1000000));
		return order;
	}
	
	public static Location encodeLocation(ILocationModel locModel, String region
											, String locationType, String areaCode) {
		
		Location location = new Location();
		location.setLocationIdentity(encodeLocationIdentity(region, locationType, locModel.getLocationId()));
		location.setLatitude((int)(getVal(locModel.getGeographicLocation().getLatitude())*1000000));
		location.setLongitude((int)(getVal(locModel.getGeographicLocation().getLongitude())*1000000));
		
		location.setTimeWindowFactor(RoutingServicesProperties.getDefaultTimeWindowFactor());
		location.setZoneID(areaCode);		
		location.setTimeZone(TimeZoneValue.tmzNone);
						
		Address address = new Address();
		address.setLine1(locModel.getStreetAddress1());
		address.setLine2(locModel.getApartmentNumber());
		address.setRegion1(locModel.getCity());
		address.setRegion3(locModel.getState());
		address.setPostalCode(locModel.getZipCode());
		address.setCountry(locModel.getCountry());
		
		location.setAddress(address);
		
		return location;
	}
	
	public static MapPoint[] encodeGeoPointList(Collection lstData) {
		
		MapPoint[] result = null;
		if(lstData != null) {
			result = new MapPoint[lstData.size()];
			Iterator tmpIterator = lstData.iterator();
			IGeoPoint _tmpModel = null;			
			
			int intCount = 0;
			while(tmpIterator.hasNext()) {
				_tmpModel = (IGeoPoint)tmpIterator.next();				
				result[intCount] = new MapPoint();				
				result[intCount].setLatitude(_tmpModel.getLatitude());
				result[intCount].setLongitude(_tmpModel.getLongitude());
				intCount++;
			}
		}
		return result;
	}
	
	public static DeliveryAreaOrderIdentity encodeDeliveryAreaOrderIdentity
												(String regionId, String area, Date deliveryDate, String orderNum) {
		DeliveryAreaOrderIdentity delOrderId = new DeliveryAreaOrderIdentity();
		delOrderId.setArea(area);
		delOrderId.setDeliveryDate(deliveryDate);
		delOrderId.setOrderNumber(orderNum != null ? orderNum.toUpperCase() : orderNum);
		delOrderId.setRegionId(regionId);
		return delOrderId;
	}
	
	public static LocationIdentity encodeLocationIdentity(String regionId, String locationType, String locationId) {
		//param1 regionId;
		//param2 locationType;
		//param3 locationId;
		LocationIdentity locId = new LocationIdentity();
		locId.setLocationID(locationId);
		locId.setRegionID(regionId);
		locId.setLocationType(locationType);
		return locId;
	}
	
	public static SchedulerIdentity encodeSchedulerIdentity(IRoutingSchedulerIdentity schedulerId) {
		//param1 regionId;
		//param2 area;
		//param3 deliveryDate;
		SchedulerIdentity schId = new SchedulerIdentity();
		schId.setRegionId(schedulerId.getRegionId());
		schId.setArea(schedulerId.getArea().getAreaCode());
		schId.setDeliveryDate(schedulerId.getDeliveryDate());
				
		return schId;
	}
	
	public static DeliveryAreaOrderIdentity encodeDeliveryAreaOrderIdentity(IRoutingSchedulerIdentity schedulerId, String orderNo) {
		//param1 regionId;
		//param2 area;
		//param3 deliveryDate;
		DeliveryAreaOrderIdentity schId = new DeliveryAreaOrderIdentity();
		schId.setRegionId(schedulerId.getRegionId());
		schId.setArea(schedulerId.getArea().getAreaCode());
		schId.setDeliveryDate(schedulerId.getDeliveryDate());
		
		schId.setOrderNumber(orderNo != null ? orderNo.toUpperCase() : orderNo);
		return schId;
	}
	
	public static SchedulerUpdateOrderOptions encodeSchedulerUpdateOrderOptions(String orderNo) {
		//param1 regionId;
		//param2 area;
		//param3 deliveryDate;
		SchedulerUpdateOrderOptions options = new SchedulerUpdateOrderOptions();
		options.setNewOrderNumber(orderNo);
		
		return options;
	}
	
	
	
	
	
	public static SchedulerBulkReserveOrdersOptions encodeSchedulerBulkReserveOrdersOptions() {
		
		//param1 confirm;
		//param2 sequenced;
		//param3 singleRoute;
		//param4 movable;
		SchedulerBulkReserveOrdersOptions schBuldRsvOrdersOptions = new SchedulerBulkReserveOrdersOptions();
		schBuldRsvOrdersOptions.setConfirm(RoutingServicesProperties.getRoutingParamConfirm());
		schBuldRsvOrdersOptions.setSequenced(RoutingServicesProperties.getRoutingParamSequenced());
		schBuldRsvOrdersOptions.setSingleRoute(RoutingServicesProperties.getRoutingParamSingleRoute());
		schBuldRsvOrdersOptions.setMovable(RoutingServicesProperties.getRoutingParamMovable());
		return schBuldRsvOrdersOptions;		
	}
	
	public static RoutingSessionCriteria encodeRoutingSessionCriteria(IRoutingSchedulerIdentity schedulerId, String sessionDescription) {
		//param1 regionIdentity;
		//param2 dateStart;
		//param3 dateEnd;
		//param4 scenario;
		//param5 description;		
		RoutingSessionCriteria routingSessionCrt = new RoutingSessionCriteria();
		routingSessionCrt.setRegionIdentity(schedulerId.getRegionId());
		routingSessionCrt.setDateStart(null);
		routingSessionCrt.setDateEnd(null);
		routingSessionCrt.setScenario(null);
		routingSessionCrt.setDescription(sessionDescription);
		return routingSessionCrt;
	}
	
	public static RouteIdentity encodeRouteIdentity(Date routeDate, String routeId) {
		//param1 regionIdentity;
		//param2 dateStart;
		//param3 dateEnd;
		//param4 scenario;
		//param5 description;	
		RouteIdentity identity = new RouteIdentity();
		identity.setRegionID(RoutingServicesProperties.getDefaultTruckRegion());
		identity.setRouteDate(routeDate);
		identity.setRouteID(routeId);
		return identity;
	}
	
	public static RoutingRouteCriteria encodeRouteCriteria(Date routeDate, String internalSessionID, String routeID) {
		//param1 regionIdentity;
		//param2 dateStart;
		//param3 dateEnd;
		//param4 scenario;
		//param5 description;
		RoutingRouteCriteria criteria = new RoutingRouteCriteria();
		criteria.setRegionIdentity(RoutingServicesProperties.getDefaultTruckRegion());
		criteria.setRouteID(routeID);
		criteria.setDateStart(routeDate);
		criteria.setInternalSessionID(Integer.parseInt(internalSessionID));
		
		return criteria;
	}
	
	
	
	public static RoutingRouteInfoRetrieveOptions encodeRouteInfoRetrieveOptionsEx() {
		//param1 level;
		//param2 retrieveActivities;
		//param3 retrieveEquipment;
		//param4 retrieveActive;
		//param5 retrieveBuilt;
		//param6 retrievePublished;
		RoutingRouteInfoRetrieveOptions routingRetrieveOptions = new RoutingRouteInfoRetrieveOptions();
		routingRetrieveOptions.setLevel(RoutingDetailLevel.rdlStop);
		routingRetrieveOptions.setRetrieveActivities(RoutingServicesProperties.getRoutingParamRetrieveActivities());
		routingRetrieveOptions.setRetrieveEquipment(RoutingServicesProperties.getRoutingParamRetrieveEquipment());
		routingRetrieveOptions.setRetrieveActive(RoutingServicesProperties.getRoutingParamRetrieveActive());
		routingRetrieveOptions.setRetrieveBuilt(RoutingServicesProperties.getRoutingParamRetrieveBuilt());
		routingRetrieveOptions.setRetrievePublished(RoutingServicesProperties.getRoutingParamRetrievePublished());
		routingRetrieveOptions.setTimeZoneOptions(encodeTimeZoneOptions());
		return routingRetrieveOptions;
		
	}	
	
	
	public static RoutingRouteInfoRetrieveOptions encodeRouteInfoRetrieveOptions() {
		//param1 level;
		//param2 retrieveActivities;
		//param3 retrieveEquipment;
		//param4 retrieveActive;
		//param5 retrieveBuilt;
		//param6 retrievePublished;
		RoutingRouteInfoRetrieveOptions routingRetrieveOptions = new RoutingRouteInfoRetrieveOptions();
		routingRetrieveOptions.setLevel(RoutingDetailLevel.rdlSession);
		routingRetrieveOptions.setRetrieveActivities(RoutingServicesProperties.getRoutingParamRetrieveActivities());
		routingRetrieveOptions.setRetrieveEquipment(RoutingServicesProperties.getRoutingParamRetrieveEquipment());
		routingRetrieveOptions.setRetrieveActive(RoutingServicesProperties.getRoutingParamRetrieveActive());
		routingRetrieveOptions.setRetrieveBuilt(RoutingServicesProperties.getRoutingParamRetrieveBuilt());
		routingRetrieveOptions.setRetrievePublished(RoutingServicesProperties.getRoutingParamRetrievePublished());
		routingRetrieveOptions.setTimeZoneOptions(encodeTimeZoneOptions());
		return routingRetrieveOptions;		
	}
	
	public static RoutingSessionIdentity encodeRoutingSessionIdentity(String regionId, String sessionId) {
		//param1 regionIdentity;
		//param2 sessionId; 
		RoutingSessionIdentity rtSessionId = new RoutingSessionIdentity();
		rtSessionId.setInternalSessionID(getIntVal(sessionId));
		rtSessionId.setRegionID(regionId);
		return rtSessionId;
	}
	
	public static TimeZoneOptions encodeTimeZoneOptions() {
		
		TimeZoneOptions timeZoneOptions = new TimeZoneOptions();
		timeZoneOptions.setEmbeddedInTimestamp(true);
		timeZoneOptions.setOptionType(TimeZoneOptionsType.tzoLocalTimeZone);
		timeZoneOptions.setTimeZone(TimeZoneValue.tmzNone);
		return timeZoneOptions;
	}
	
	public static SchedulerBalanceRoutesOptions encodeBalanceRoutesOptions(String balanceBy, double balanceFactor) {
		
		SchedulerBalanceRoutesOptions schBalRoutesOptions = new SchedulerBalanceRoutesOptions();
		SchedulerRouteBalancingOptions schRoutesOptions = new SchedulerRouteBalancingOptions();
		schRoutesOptions.setBalanceBy(SchedulerBalancingFactor.Factory.fromValue(balanceBy));
		schRoutesOptions.setCostVsBalanceWeight(balanceFactor);
		schRoutesOptions.setDynamicWindowInfo(null);
		schBalRoutesOptions.setBalancingOptions(schRoutesOptions);
		return schBalRoutesOptions;
	}
	
	public static SchedulerAnalyzeOptions encodeSchedulerAnalyzeOptions(String region
			, String area
			, Date startDate
			, int noOfDays
			, List<IDeliverySlot> slots) {
		SchedulerAnalyzeOptions options = new SchedulerAnalyzeOptions();
		options.setArea(area);
//		options.setDynamicWindowInfo(param);
//		options.setExcludeCutoffRoutes(param);

		options.setNumDays(noOfDays);
		options.setRegionId(region);
		options.setStartDate(startDate);
//		options.setUseCalendar(param);

		if(slots != null) {
			options.setUserDeliveryWindows(encodeDeliveryWindowBaseList(slots));
		}
		return options;
	}

	public SchedulerCalculateDeliveryWindowMetrics encodeSchedulerCalculateDeliveryWindowMetrics() {
		SchedulerCalculateDeliveryWindowMetrics metrics = new SchedulerCalculateDeliveryWindowMetrics();
		metrics.setSchedulerIdentity(null);
		SchedulerDeliveryWindowMetricsOptions options = new SchedulerDeliveryWindowMetricsOptions();
		SchedulerDeliveryWindowBase base = new SchedulerDeliveryWindowBase();
		base.setStart(null);
		base.setEnd(null);
		options.setDeliveryWindows(new SchedulerDeliveryWindowBase[]{base});


		metrics.setOptions(options);
		return metrics;
	}
	
	public static SchedulerDeliveryWindowBase[] encodeDeliveryWindowBaseList(List<IDeliverySlot> slots) {
		
		SchedulerDeliveryWindowBase[] result = null;
		if(slots != null) {
			result = new SchedulerDeliveryWindowBase[slots.size()];
			Iterator<IDeliverySlot> tmpIterator = slots.iterator();
			IDeliverySlot slot = null;			
			int intCount = 0;
			while(tmpIterator.hasNext()) {
				slot = tmpIterator.next();
				if(slot != null) {					
					result[intCount++] = encodeDeliveryWindowBase(slot);
				}
			}
		}
		return result;
	}

	public static SchedulerDeliveryWindowBase encodeDeliveryWindowBase(IDeliverySlot slot) {
		SchedulerDeliveryWindowBase window = new SchedulerDeliveryWindowBase();
		
		window.setStart(getTime(slot.getStartTime()));
		window.setEnd(getTime(slot.getStopTime()));
		window.setWaveCode(slot.getWaveCode());
		return window;
	}
	
	public static DeliveryWindow encodeDeliveryWindow(IDeliverySlot slot, IRoutingSchedulerIdentity schedulerId) {
		DeliveryWindow window = new DeliveryWindow();
		window.setSchedulerIdentity(encodeSchedulerIdentity(schedulerId));
		window.setStartTime(getTime(slot.getStartTime()));
		window.setStopTime(getTime(slot.getStopTime()));
		window.setWaveCode(slot.getWaveCode());
		return window;
	}
	
	public static SchedulerReserveOrderOptions encodeSchedulerReserveOrderOptions() {
		SchedulerReserveOrderOptions options = new SchedulerReserveOrderOptions();
		options.setConfirm(false);
		//options.setExcludeCutoffRoutes(0);
		
		options.setMovable(true);
		
		return options;
	}
	
	public static IRoutingSchedulerIdentity encodeSchedulerId(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel) {

    	if(schedulerId == null) {
    		schedulerId = new RoutingSchedulerIdentity();
    	}
    	schedulerId.setRegionId(RoutingUtil.getRegion(orderModel.getDeliveryInfo().getDeliveryZone()));
    	schedulerId.setArea(orderModel.getDeliveryInfo().getDeliveryZone().getArea());
    	schedulerId.setDeliveryDate(orderModel.getDeliveryInfo().getDeliveryDate());
    	schedulerId.setDepot(orderModel.getDeliveryInfo().getDeliveryZone().getArea().isDepot());
    	return schedulerId;
    }
	
	public static SchedulerDeliveryWindowMetricsOptions encodeSchedulerDeliveryWindowMetricsOptions() {
		
		
		SchedulerDeliveryWindowMetricsOptions schMetricsOptions = new SchedulerDeliveryWindowMetricsOptions();
		schMetricsOptions.setType(SchedulerDeliveryWindowMetricsType.sdwmfPlannedArrivalTime);
		//schMetricsOptions.setType(SchedulerDeliveryWindowMetricsType.sdwmfAssignedDeliveryWindow);
		schMetricsOptions.setTimeZone(TimeZoneValue.tmzEasternTimeUSCanada);
						
		return schMetricsOptions;
	}
	
	public static com.freshdirect.routing.proxy.stub.roadnet.Address encodeAddress(String street, String zipCode, String country) {
		
		com.freshdirect.routing.proxy.stub.roadnet.Address address = new com.freshdirect.routing.proxy.stub.roadnet.Address();
		address.setLine1(street);
		address.setPostalCode(zipCode);
		address.setCountry(country);
		return address;
	}
		
	private static Time getTime(Date date) {
		
		try {
			// Faking time zoen since Routing system doesn't consider timezone
			return new Time(RoutingDateUtil.formatTime(date));
		} catch(ParseException e) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return new Time(calendar);
		}		
	}
	
	private static double getVal(String val) {
		double result = 0.0;
		try {
			result = Double.parseDouble(val);
		} catch(Exception e) {
			
		}
		return result;
	}
	
	private static int getIntVal(String val) {
		int result = 0;
		try {
			result = Integer.parseInt(val);
		} catch(Exception e) {
			
		}
		return result;
	}
	
	public static void main(String arg[]) {
		
		ArrayList tmp = new ArrayList();
		Collections.min(tmp);
	}

}
