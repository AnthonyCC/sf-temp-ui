package com.freshdirect.routing.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.axis.types.Time;

import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.proxy.stub.transportation.Address;
import com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity;
import com.freshdirect.routing.proxy.stub.transportation.Location;
import com.freshdirect.routing.proxy.stub.transportation.LocationIdentity;
import com.freshdirect.routing.proxy.stub.transportation.RoutingDetailLevel;
import com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder;
import com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions;
import com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria;
import com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerBalancingFactor;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerRouteBalancingOptions;
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
															, region, locationType, areaCode);
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
					result[intCount++] = encodeOrder(schedulerId, orderModel, region, locationType, orderType);
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
		order.setOrderNumber(orderModel.getIdentity().getOrderNumber());
		order.setSessionIdentity(encodeRoutingSessionIdentity(schedulerId.getRegionId(), sessionId));
		order.setDeliveryDate(orderModel.getIdentity().getDeliveryDate());
		order.setLocationIdentity(encodeLocationIdentity(orderModel.getIdentity().getRegionId()
										,orderModel.getLocationType(),orderModel.getLocationId()));
		order.setTw1Open(orderModel.getDeliveryWindowStart());
		order.setTw1Close(orderModel.getDeliveryWindowEnd());
		
		CategoryQuantities quantities = new CategoryQuantities(orderModel.getQuantity(), 0, 0, null, null, null);
		order.setQuantities(quantities);
		
		order.setAdditionalServiceTime(new Integer(orderModel.getServiceTime()));
				
		return order;
	}

	public static DeliveryAreaOrder encodeOrder(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel
													, String region, String locationType
													, String orderType) {
		
		DeliveryAreaOrder order = new DeliveryAreaOrder();
		order.setIdentity(new DeliveryAreaOrderIdentity(schedulerId.getRegionId(),schedulerId.getArea().getAreaCode()
															,schedulerId.getDeliveryDate(), orderModel.getOrderNumber()));
		order.setOrderType(orderType);
		//Calendar tmpReservedTime = Calendar.getInstance();
		//tmpReservedTime.setTime(orderModel.getDeliveryInfo().getDeliveryStartTime());
		order.setReservedTime(baseCalendar);
		order.setConfirmed(true);
		
		order.setQuantity((int)orderModel.getDeliveryInfo().getPackagingInfo().getTotalSize1());
		
		order.setDeliveryWindowStart(getTime(orderModel.getDeliveryInfo().getDeliveryStartTime()));
		order.setDeliveryWindowEnd(getTime(orderModel.getDeliveryInfo().getDeliveryEndTime()));
		order.setServiceTime((int)(orderModel.getDeliveryInfo().getServiceTime()*60));
		order.setLocationId(orderModel.getDeliveryInfo().getDeliveryLocation().getLocationId());
		order.setLocationType(locationType);
		order.setDescription(locationType);
		
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
		location.setTimeZone(TimeZoneValue.fromString(TimeZoneValue._tmzNone));
						
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
	
	public static LocationIdentity encodeLocationIdentity(String regionId, String locationType, String locationId) {
		//param1 regionId;
		//param2 locationType;
		//param3 locationId;
		return new LocationIdentity(regionId,locationType,locationId);
	}
	
	public static SchedulerIdentity encodeSchedulerIdentity(IRoutingSchedulerIdentity schedulerId) {
		//param1 regionId;
		//param2 area;
		//param3 deliveryDate;
		return new SchedulerIdentity(schedulerId.getRegionId(), schedulerId.getArea().getAreaCode(), schedulerId.getDeliveryDate());
	}
	
	public static SchedulerBulkReserveOrdersOptions encodeSchedulerBulkReserveOrdersOptions() {
		
		//param1 confirm;
		//param2 sequenced;
		//param3 singleRoute;
		//param4 movable;
		return new SchedulerBulkReserveOrdersOptions(true, false, false, false);
	}
	
	public static RoutingSessionCriteria encodeRoutingSessionCriteria(IRoutingSchedulerIdentity schedulerId, String sessionDescription) {
		//param1 regionIdentity;
		//param2 dateStart;
		//param3 dateEnd;
		//param4 scenario;
		//param5 description;		
		return new RoutingSessionCriteria(schedulerId.getRegionId(), null, null, null, sessionDescription);
	}
	
	public static RoutingRouteInfoRetrieveOptions encodeRouteInfoRetrieveOptions() {
		//param1 level;
		//param2 retrieveActivities;
		//param3 retrieveEquipment;
		//param4 retrieveActive;
		//param5 retrieveBuilt;
		//param6 retrievePublished;
		return new RoutingRouteInfoRetrieveOptions(RoutingDetailLevel.fromValue(RoutingDetailLevel._rdlSession), true, true, true, true, true,
				encodeTimeZoneOptions());
	}
	
	public static RoutingSessionIdentity encodeRoutingSessionIdentity(String regionId, String sessionId) {
		//param1 regionIdentity;
		//param2 sessionId; 
		return new RoutingSessionIdentity(regionId, getIntVal(sessionId));
	}
	
	public static TimeZoneOptions encodeTimeZoneOptions() {
		
		return new TimeZoneOptions(true, TimeZoneOptionsType.fromValue(TimeZoneOptionsType._tzoLocalTimeZone)
										, TimeZoneValue.fromValue(TimeZoneValue._tmzNone));
	}
	
	public static SchedulerBalanceRoutesOptions encodeBalanceRoutesOptions(String balanceBy, double balanceFactor) {
		
		return new SchedulerBalanceRoutesOptions(new SchedulerRouteBalancingOptions(
				SchedulerBalancingFactor.fromValue(balanceBy), balanceFactor, null));
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

}
