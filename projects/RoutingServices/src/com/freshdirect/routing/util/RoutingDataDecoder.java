package com.freshdirect.routing.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.routing.constants.EnumRoutingNotification;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.DeliveryReservation;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.DeliverySlotCost;
import com.freshdirect.routing.model.DeliveryWindowMetrics;
import com.freshdirect.routing.model.DrivingDirection;
import com.freshdirect.routing.model.DrivingDirectionArc;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliverySlotCost;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.model.IDrivingDirectionArc;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPathDirection;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingNotificationModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.PathDirection;
import com.freshdirect.routing.model.RouteModel;
import com.freshdirect.routing.model.RoutingNotificationModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.RoutingStopModel;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.routing.proxy.stub.roadnet.DirectionArc;
import com.freshdirect.routing.proxy.stub.roadnet.DirectionData;
import com.freshdirect.routing.proxy.stub.roadnet.PathDirections;
import com.freshdirect.routing.proxy.stub.transportation.ChangedOrderIdentity;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryCost;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow;
import com.freshdirect.routing.proxy.stub.transportation.Notification;
import com.freshdirect.routing.proxy.stub.transportation.ReserveResult;
import com.freshdirect.routing.proxy.stub.transportation.ReserveResultType;
import com.freshdirect.routing.proxy.stub.transportation.RouteChangeNotification;
import com.freshdirect.routing.proxy.stub.transportation.RoutingRoute;
import com.freshdirect.routing.proxy.stub.transportation.RoutingStop;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerOrdersCanceledNotification;

public class RoutingDataDecoder {
	
	public static List decodeRouteList(RoutingRoute[] routes) {
		
		List result = null;
		if(routes != null) {
			result = new ArrayList();
			
			for(int intCount=0; intCount < routes.length; intCount++) {
				result.add(decodeRoute(routes[intCount]));
			}
		}
		return result;
	}
	
	public static IDrivingDirection decodeDrivingDirection(DirectionData direction) {
		
		IDrivingDirection result = null;
		if(direction != null) {
			result = new DrivingDirection();
			result.setTime(direction.getTime());
			result.setDistance(direction.getDistance());

			if(direction.getPathDirections() != null) {
				result.setPathDirections(new ArrayList());
				for(int intCount=0; intCount < direction.getPathDirections().length; intCount++) {
					result.getPathDirections().add(decodePathDirection(direction.getPathDirections()[intCount]));
				}
			}
		}
		return result;
	}
	
	public static IPathDirection decodePathDirection(PathDirections path) {
		IPathDirection result = null;
		if(path != null) {
			result = new PathDirection();
			result.setTime(path.getTime());
			result.setDistance(path.getDistance());

			if(path.getDirectionArcs() != null) {
				result.setDirectionsArc(new ArrayList());
				for(int intCount=0; intCount < path.getDirectionArcs().length; intCount++) {
					result.getDirectionsArc().add(decodeDirectionArc(path.getDirectionArcs()[intCount]));
				}
			}
		}
		return result;
	}
	
	public static IDrivingDirectionArc decodeDirectionArc(DirectionArc arc) {
		IDrivingDirectionArc result = null;
		if(arc != null) {
			result = new DrivingDirectionArc();
			result.setTime(arc.getTime());
			result.setDistance(arc.getDistance());
			result.setInstruction(arc.getInstruction());
		}
		return result;
	}
	
	public static IRouteModel decodeRoute(RoutingRoute route) {
		
		IRouteModel result = null;
		
		if(route != null) {
			
			result = new RouteModel();
			result.setRouteId(route.getRouteID());
			result.setRouteStartTime(route.getStartTime().getTime());
			result.setStops(new TreeSet());
			
			IRoutingStopModel _stop = null;
			ILocationModel _locModel = null;
			IGeographicLocation _geoLocModel = null;
			
			RoutingStop _refStop = null;
			
			if(route.getStops() != null) {
				
				for(int intCount=0;intCount<route.getStops().length ;intCount++) {
					_refStop = route.getStops()[intCount];
					
					if(_refStop.getSequenceNumber() >= 0) {
						_stop = new RoutingStopModel(_refStop.getSequenceNumber());
						_locModel = new LocationModel();
						
						_locModel.setStreetAddress1(_refStop.getAddress().getLine1());
						_locModel.setCity(_refStop.getAddress().getRegion1()); 
						_locModel.setState(_refStop.getAddress().getRegion3());
						_locModel.setZipCode(_refStop.getAddress().getPostalCode());
						
						_stop.setLocation(_locModel);
						
						_geoLocModel = new GeographicLocation();
						_geoLocModel.setLatitude(""+_refStop.getLatitude());
						_geoLocModel.setLongitude(""+_refStop.getLongitude());
						
						_locModel.setGeographicLocation(_geoLocModel);
						
						_stop.setStopArrivalTime(Calendar.getInstance().getTime());
						result.getStops().add(_stop);
					}
				}
			}
			
		}
		return result;
	}
	
	public static List<IDeliveryWindowMetrics> decodeDeliveryWindowMetrics(SchedulerDeliveryWindowMetrics[] delWindowMetrics) {
		
		List<IDeliveryWindowMetrics> result = null;
		if(delWindowMetrics != null) {
			result = new ArrayList<IDeliveryWindowMetrics>(); 
			for (SchedulerDeliveryWindowMetrics window : delWindowMetrics) {
				result.add(deocdeDeliveryMetrics(window));
			}
			
		}
		return result;
	}
	
	public static IDeliveryWindowMetrics deocdeDeliveryMetrics(SchedulerDeliveryWindowMetrics window) {
		
		IDeliveryWindowMetrics metrics = new DeliveryWindowMetrics();
		metrics.setDeliveryStartTime(window.getWindow().getStart().getAsCalendar().getTime());
		metrics.setDeliveryEndTime(window.getWindow().getEnd().getAsCalendar().getTime());
				
		metrics.setTotalCapacityTime((RoutingDateUtil.getDiffInSeconds
										(metrics.getDeliveryEndTime(), metrics.getDeliveryStartTime())
											* window.getAllocatedVehicles())/60.0);
		
		metrics.setAllocatedVehicles(window.getAllocatedVehicles());
		metrics.setVehiclesInUse(window.getVehiclesInUse());
		
		metrics.setConfirmedDeliveryQuantity(window.getConfirmed().getDeliveryQuantity());
		metrics.setConfirmedItems(window.getConfirmed().getItems());
		metrics.setConfirmedPickupQuantity(window.getConfirmed().getPickupQuantity());
		metrics.setConfirmedServiceTime(window.getConfirmed().getServiceTime()/60.0);
		metrics.setConfirmedTravelTime(window.getConfirmed().getTravelTime()/60.0);
		
		metrics.setReservedDeliveryQuantity(window.getReserved().getDeliveryQuantity());
		metrics.setReservedItems(window.getReserved().getItems());
		metrics.setReservedPickupQuantity(window.getReserved().getPickupQuantity());
		metrics.setReservedServiceTime(window.getReserved().getServiceTime()/60.0);
		metrics.setReservedTravelTime(window.getReserved().getTravelTime()/60.0);
		
		
		
		return metrics;
	}
	
	public static List<IDeliverySlot> decodeDeliveryWindows(DeliveryWindow[] delWindows) {
		
		List<IDeliverySlot> result = null;
		if(delWindows != null) {
			result = new ArrayList<IDeliverySlot>(); 
			for (DeliveryWindow window : delWindows) {
				result.add(decodeDeliverySlot(window));
			}
			
		}
		return result;
	}
	
	public static IDeliverySlot decodeDeliverySlot(DeliveryWindow window) {
		
		IDeliverySlot slot = new DeliverySlot();
		slot.setDeliveryCost(decodeDeliverySlotCost(window.getDeliveryCost()));
		slot.setManuallyClosed(window.getManuallyClosed());
		slot.setSchedulerId(decodeSchedulerId(window.getSchedulerIdentity()));
		slot.setStartTime(window.getStartTime().getAsCalendar().getTime());
		slot.setStopTime(window.getStopTime().getAsCalendar().getTime());
		slot.setWaveCode(window.getWaveCode());
		
		return slot;
	}
	
	public static IDeliverySlotCost decodeDeliverySlotCost(DeliveryCost deliveryCost) {
		IDeliverySlotCost deliverySlot = new DeliverySlotCost();
		deliverySlot.setAdditionalDistance(deliveryCost.getAdditionalDistance());
		deliverySlot.setAdditionalRunTime(deliveryCost.getAdditionalRunTime());
		deliverySlot.setAdditionalStopCost(deliveryCost.getAdditionalStopCost());
		deliverySlot.setAvailable(deliveryCost.getAvailable());
		deliverySlot.setCapacity(deliveryCost.getCapacity());
		deliverySlot.setCostPerMile(deliveryCost.getCostPerMile());
		deliverySlot.setFiltered(deliveryCost.getFiltered());
		deliverySlot.setFixedRouteSetupCost(deliveryCost.getFixedRouteSetupCost());
		deliverySlot.setMaxRunTime(deliveryCost.getMaxRunTime());
		deliverySlot.setMissedTW(deliveryCost.getMissedTW());
		deliverySlot.setOvertimeHourlyWage(deliveryCost.getOvertimeHourlyWage());
		deliverySlot.setPrefRunTime(deliveryCost.getPrefRunTime());
		deliverySlot.setRegularHourlyWage(deliveryCost.getRegularHourlyWage());
		deliverySlot.setRegularWageDurationSeconds(deliveryCost.getRegularWageDurationSeconds());
		deliverySlot.setRouteId(deliveryCost.getRouteId());
		deliverySlot.setStopSequence(deliveryCost.getStopSequence());
		deliverySlot.setTotalDistance(deliveryCost.getTotalDistance());
		deliverySlot.setTotalPUQuantity(deliveryCost.getTotalPUQuantity());
		deliverySlot.setTotalQuantity(deliveryCost.getTotalQuantity());
		deliverySlot.setTotalRouteCost(deliveryCost.getTotalRouteCost());
		deliverySlot.setTotalRunTime(deliveryCost.getTotalRunTime());
		deliverySlot.setTotalServiceTime(deliveryCost.getTotalServiceTime());
		deliverySlot.setTotalTravelTime(deliveryCost.getTotalTravelTime());
		deliverySlot.setTotalWaitTime(deliveryCost.getTotalWaitTime());
		
		return deliverySlot;
	}
	
	public static IRoutingSchedulerIdentity decodeSchedulerId(SchedulerIdentity schIdentity) {
		IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
		IAreaModel areaModel = new AreaModel();
		areaModel.setAreaCode(schIdentity.getArea());
		schedulerId.setArea(areaModel);
		schedulerId.setDeliveryDate(schIdentity.getDeliveryDate());
		schedulerId.setRegionId(schIdentity.getRegionId());
		return schedulerId;
	}
	
	public static IDeliveryReservation decodeDeliveryReservation(ReserveResult result) {
		IDeliveryReservation reservation = new DeliveryReservation();
		
		if(result.getResult().equals(ReserveResultType.rrtSuccess)) {
			reservation.setReserved(true);
		} 
		if(result.getResult().equals(ReserveResultType.rrtNewCost)) {
			//reservation.setReserved(true);
			reservation.setNewCost(true);
		}
		if(result.getExpiration() != null) {
			reservation.setExpiryTime(result.getExpiration().getTime());
		}
		return reservation;
	}
	
	public static List<IRoutingNotificationModel> decodeNotifications(Notification[] notifications) {
		
		List<IRoutingNotificationModel> result = null;
		if(notifications != null) {
			result = new ArrayList<IRoutingNotificationModel>(); 
			for (Object notification : notifications) {
				if(notification instanceof SchedulerOrdersCanceledNotification) {
					SchedulerOrdersCanceledNotification _tmpNotification = (SchedulerOrdersCanceledNotification)notification;
					
					String[] orders = _tmpNotification.getOrderNumbers();
					for(int intCount=0; intCount< orders.length; intCount++) {
						IRoutingNotificationModel _tmpModel = new RoutingNotificationModel();
						_tmpModel.setNotificationType(EnumRoutingNotification.SchedulerOrdersCanceledNotification);
						_tmpModel.setOrderNumber(orders[intCount]);
						_tmpModel.setNotificationId(_tmpNotification.getNotificationIdentity().getIdentity());
						_tmpModel.setSchedulerId(decodeSchedulerId(_tmpNotification.getSchedulerIdentity()));
						result.add(_tmpModel);
					}					
				} else if(notification instanceof RouteChangeNotification) {
					RouteChangeNotification _tmpNotification = (RouteChangeNotification)notification;
					ChangedOrderIdentity[] orders = _tmpNotification.getOrders();
					
					for(int intCount=0; intCount< orders.length; intCount++) {
						IRoutingNotificationModel _tmpModel = new RoutingNotificationModel();
						_tmpModel.setNotificationType(EnumRoutingNotification.RouteChangeNotification);
						_tmpModel.setOrderNumber(orders[intCount].getOrderNumber());
						_tmpModel.setNotificationId(_tmpNotification.getNotificationIdentity().getIdentity());
						_tmpModel.setSchedulerId(new RoutingSchedulerIdentity());
						result.add(_tmpModel);
					}
				}
			}			
		}
		return result;
	}
	
	public static IOrderModel decodeDeliveryAreaOrder(DeliveryAreaOrder model) {
		
		IOrderModel order = null;
		if(model != null) {
			order = new OrderModel();	
    		order.setOrderNumber(model.getDescription());
    					    						    						    		
    		IDeliveryModel dModel = new DeliveryModel();
    		dModel.setDeliveryDate(model.getIdentity().getDeliveryDate());
    		dModel.setDeliveryStartTime(model.getDeliveryWindowStart().getAsCalendar().getTime());
    		dModel.setDeliveryEndTime(model.getDeliveryWindowEnd().getAsCalendar().getTime());
    		dModel.setReservationId(model.getIdentity().getOrderNumber());
    		dModel.setOrderSize(model.getQuantity());
			dModel.setServiceTime(model.getServiceTime());
    		
    		IZoneModel zModel = new ZoneModel();
    		IAreaModel areaModel = new AreaModel();
    		areaModel.setAreaCode(model.getIdentity().getArea());
    		zModel.setArea(areaModel);
    		
    		dModel.setDeliveryZone(zModel);
    		
    		ILocationModel locModel = new LocationModel();    		    		
    		locModel.setLocationId(model.getLocationId());
    		dModel.setDeliveryLocation(locModel);
    		    		
    		order.setDeliveryInfo(dModel);
		}
				
		return order;
	}
	
	public static IRoutingSchedulerIdentity decodeSchedulerId(DeliveryAreaOrderIdentity schIdentity) {
		IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
		IAreaModel areaModel = new AreaModel();
		areaModel.setAreaCode(schIdentity.getArea());
		schedulerId.setArea(areaModel);
		schedulerId.setDeliveryDate(schIdentity.getDeliveryDate());
		schedulerId.setRegionId(schIdentity.getRegionId());
		return schedulerId;
	}
	
}
