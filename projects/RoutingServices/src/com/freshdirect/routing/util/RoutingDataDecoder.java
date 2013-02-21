package com.freshdirect.routing.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.routing.constants.EnumRoutingNotification;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.DeliveryReservation;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.DeliverySlotCost;
import com.freshdirect.routing.model.DeliveryWindowMetrics;
import com.freshdirect.routing.model.DrivingDirection;
import com.freshdirect.routing.model.DrivingDirectionArc;
import com.freshdirect.routing.model.EquipmentType;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IBuildingModel;
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
import com.freshdirect.routing.model.IRegionModel;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingDepotId;
import com.freshdirect.routing.model.IRoutingEquipmentType;
import com.freshdirect.routing.model.IRoutingNotificationModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.OrderModel;
import com.freshdirect.routing.model.PathDirection;
import com.freshdirect.routing.model.RegionModel;
import com.freshdirect.routing.model.RouteModel;
import com.freshdirect.routing.model.RoutingDepotId;
import com.freshdirect.routing.model.RoutingEquipmentType;
import com.freshdirect.routing.model.RoutingNotificationModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.RoutingStopModel;
import com.freshdirect.routing.model.WaveInstance;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.routing.proxy.stub.transportation.ChangedOrderIdentity;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryCost;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstance;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow;
import com.freshdirect.routing.proxy.stub.transportation.DirectionArc;
import com.freshdirect.routing.proxy.stub.transportation.DirectionData;
import com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity;
import com.freshdirect.routing.proxy.stub.transportation.LocationIdentity;
import com.freshdirect.routing.proxy.stub.transportation.Notification;
import com.freshdirect.routing.proxy.stub.transportation.PathDirections;
import com.freshdirect.routing.proxy.stub.transportation.ReserveResult;
import com.freshdirect.routing.proxy.stub.transportation.ReserveResultType;
import com.freshdirect.routing.proxy.stub.transportation.RouteChangeNotification;
import com.freshdirect.routing.proxy.stub.transportation.RoutingRoute;
import com.freshdirect.routing.proxy.stub.transportation.RoutingStop;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity;
import com.freshdirect.routing.proxy.stub.transportation.SchedulerOrdersCanceledNotification;
import com.freshdirect.routing.proxy.stub.transportation.StopType;
import com.freshdirect.routing.proxy.stub.transportation.TimePeriodBasedTravelSpeedsType;

public class RoutingDataDecoder {
	
		
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
	
	public static IDrivingDirection decodeDrivingDirections(List<DirectionData> directions) {
		IDrivingDirection result =  new DrivingDirection();
		result.setPathDirections(new ArrayList());
		for(DirectionData direction : directions)
		{
			result.setTime(result.getTime()+direction.getTime());
			result.setDistance(result.getDistance()+direction.getDistance());
			if(direction.getPathDirections() != null) {
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
	
	public static List decodeRouteList(RoutingRoute[] routes) {
		return decodeRouteListEx(routes, false);
	}
	
	public static List decodeRouteListEx(RoutingRoute[] routes, boolean retrieveBlankStops) {
		
		List result = null;
		if(routes != null) {
			result = new ArrayList();
			
			for(int intCount=0; intCount < routes.length; intCount++) {
				result.add(decodeRoute(routes[intCount], retrieveBlankStops));
			}
		}
		return result;
	}

	public static IRouteModel decodeRoute(RoutingRoute route, boolean retrieveBlankStops) {
		
		IRouteModel result = null;
		
		if(route != null) {
			
			result = new RouteModel();
			result.setRouteId(route.getRouteID());
			result.setStartTime(route.getStartTime() != null ? route.getStartTime().getTime() : null);
			result.setCompletionTime(route.getCompleteTime() != null ? route.getCompleteTime().getTime() : null);
			result.setRoadNetRouteId(new Integer((route.getRouteIdentity()!=null)?route.getRouteIdentity().getInternalRouteID():0).toString());		
			result.setDistance(route.getDistance());
			result.setTravelTime(route.getTravelTime());
			result.setServiceTime(route.getServiceTime());
						
			//result.setCutOffTime(getWaveCutOffTime(route.); UPS Batch System doesnt have wavecode could be a problem for sameday
			result.setMaxRunTime(route.getMaximumTime());
			result.setPreferredRunTime(route.getPreferredTime());
						
			result.setOriginId(route.getOrigin() != null ? route.getOrigin().getLocationID() : null);
			
			result.setStops(new TreeSet());
			
			IRoutingStopModel _stop = null;
			ILocationModel _locModel = null;
			IBuildingModel building = null;
			IGeographicLocation _geoLocModel = null;
			IDeliveryModel deliveryInfo = null;
			
			RoutingStop _refStop = null;
			int lastSequence = 0;
			
			if(route.getStops() != null) {
				//System.out.println(route.getRouteID()+"->"+route.getStops().length+retrieveBlankStops);
				for(int intCount=0;intCount<route.getStops().length ;intCount++) {
					_refStop = route.getStops()[intCount];
					
					if(_refStop.getSequenceNumber() >= 0 || (retrieveBlankStops && _refStop.getStopType()!=null && StopType._stpDepot.equals(_refStop.getStopType().getValue()))) {
						//System.out.println("\t"+_refStop.getSequenceNumber()+" "+_refStop.getLocationIdentity().getLocationID()+" "+_refStop.getOrders()+" "+_refStop.getStopType());
										
						_stop = new RoutingStopModel(_refStop.getSequenceNumber() >= 0 ? _refStop.getSequenceNumber() : lastSequence);
						lastSequence =  _refStop.getSequenceNumber();
							
						building = new BuildingModel();
						
						building.setSrubbedStreet(_refStop.getAddress().getLine1());
						building.setStreetAddress1(_refStop.getAddress().getLine1());
						building.setCity(_refStop.getAddress().getRegion1()); 
						building.setState(_refStop.getAddress().getRegion3());
						building.setZipCode(_refStop.getAddress().getPostalCode());
						
						_locModel = new LocationModel(building);
											
						_geoLocModel = new GeographicLocation();
						_geoLocModel.setLatitude(""+_refStop.getLatitude());
						_geoLocModel.setLongitude(""+_refStop.getLongitude());
										
						
						building.setGeographicLocation(_geoLocModel);
						deliveryInfo = new DeliveryModel();
						deliveryInfo.setDeliveryLocation(_locModel);
						
						if(RoutingServicesProperties.sortStopbyWindow())
						{
							if(_refStop.getTw1OpenTime() != null && _refStop.getTw1OpenTime().getAsCalendar()!=null)
								deliveryInfo.setDeliveryStartTime(_refStop.getTw1OpenTime().getAsCalendar().getTime());
							if(_refStop.getTw1CloseTime() != null && _refStop.getTw1CloseTime().getAsCalendar()!=null)
								deliveryInfo.setDeliveryEndTime(_refStop.getTw1CloseTime().getAsCalendar().getTime());
						}
						
						_stop.setDeliveryInfo(deliveryInfo);
						
						//_stop.setStopArrivalTime(Calendar.getInstance().getTime());
						_stop.setOrderNumber((_refStop.getOrders() != null
													&& _refStop.getOrders().length > 0 ? _refStop.getOrders()[0].getOrderNumber() 
																							: IRoutingStopModel.DEPOT_STOPNO 
																							+ (_refStop.getArrival() != null 
																									? _refStop.getArrival().getTimeInMillis() : 
																										_refStop.getStopIdentity().getInternalStopID())));
						_stop.setStopArrivalTime(_refStop.getArrival() != null ? _refStop.getArrival().getTime() : null);
						_stop.setStopDepartureTime(_refStop.getArrival() != null ? RoutingDateUtil.addSeconds(_refStop.getArrival().getTime()
														, _refStop.getServiceTime()) : null);
						
						_stop.setTravelTime(_refStop.getTravelTime());
						_stop.setServiceTime(_refStop.getServiceTime());
						
						result.getStops().add(_stop);
					}
				}
				//System.out.println(result.getRouteId()+"->"+result.getStops().size());
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
				
		/*metrics.setTotalCapacityTime((RoutingDateUtil.getDiffInSeconds
										(metrics.getDeliveryEndTime(), metrics.getDeliveryStartTime())
											* window.getAllocatedVehicles())/60.0);*/
		//This change is to compliment UPS changes for wave settings, now we have more accurate data
		metrics.setTotalCapacityTime(window.getAllocatedWorkingTime()/60.0);
				
		metrics.setAllocatedVehicles(window.getAllocatedVehicles());
		metrics.setVehiclesInUse(window.getVehiclesInUse());
		
	//	metrics.setConfirmedDeliveryQuantity(window.getConfirmed().getDeliveryQuantity());
		metrics.setConfirmedItems(window.getConfirmed().getItems());
	//	metrics.setConfirmedPickupQuantity(window.getConfirmed().getPickupQuantity());
		metrics.setConfirmedServiceTime(window.getConfirmed().getServiceTime()/60.0);
		metrics.setConfirmedTravelTime(window.getConfirmed().getTravelTime()/60.0);
		
	//	metrics.setReservedDeliveryQuantity(window.getReserved().getDeliveryQuantity());
		metrics.setReservedItems(window.getReserved().getItems());
	//	metrics.setReservedPickupQuantity(window.getReserved().getPickupQuantity());
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
		//deliverySlot.setCapacity(deliveryCost.getCapacity());
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
	//	deliverySlot.setTotalPUQuantity(deliveryCost.getTotalPUQuantity());
	//	deliverySlot.setTotalQuantity(deliveryCost.getTotalQuantity());
		deliverySlot.setTotalRouteCost(deliveryCost.getTotalRouteCost());
		deliverySlot.setTotalRunTime(deliveryCost.getTotalRunTime());
		deliverySlot.setTotalServiceTime(deliveryCost.getTotalServiceTime());
		deliverySlot.setTotalTravelTime(deliveryCost.getTotalTravelTime());
		deliverySlot.setTotalWaitTime(deliveryCost.getTotalWaitTime());
		deliverySlot.setUnavailabilityReason(deliveryCost.getUnavailabilityReason());
		deliverySlot.setWaveVehicles(deliveryCost.getWaveVehicles());
		deliverySlot.setWaveVehiclesInUse(deliveryCost.getWaveVehiclesInUse());
		if(deliveryCost.getWaveStartTime()!=null)
			deliverySlot.setWaveStartTime(deliveryCost.getWaveStartTime().getAsCalendar().getTime());
		deliverySlot.setWaveOrdersTaken(deliveryCost.getWaveOrdersTaken());
		if(deliveryCost.getTotalQuantities()!=null)
		deliverySlot.setTotalQuantities(deliveryCost.getTotalQuantities().getSize1());
		deliverySlot.setNewRoute(deliveryCost.getNewRoute());
		if(deliveryCost.getCapacities()!=null)
		deliverySlot.setCapacities(deliveryCost.getCapacities().getSize1());
		
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
				}  else {
					Notification _tmpNotification = (Notification)notification;
					IRoutingNotificationModel _tmpModel = new RoutingNotificationModel();
						
					_tmpModel.setNotificationId(_tmpNotification.getNotificationIdentity().getIdentity());
					_tmpModel.setSchedulerId(new RoutingSchedulerIdentity());
					result.add(_tmpModel);
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
    		dModel.setCalculatedOrderSize(model.getQuantities().getSize1());
			dModel.setCalculatedServiceTime(model.getServiceTime());
    		
    		IZoneModel zModel = new ZoneModel();
    		IAreaModel areaModel = new AreaModel();
    		areaModel.setAreaCode(model.getIdentity().getArea());
    		IRegionModel _rModel = new RegionModel();
    		_rModel.setRegionCode(model.getIdentity().getRegionId());
    		areaModel.setRegion(_rModel);
    		zModel.setArea(areaModel);
    		
    		dModel.setDeliveryZone(zModel);
    		
    		ILocationModel locModel = new LocationModel(new BuildingModel());    		    		
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
	
	public static List<IWaveInstance> decodeWaveInstanceList(DeliveryWaveInstance[] waveInstances) {
		
		List<IWaveInstance> result = null;
		if(waveInstances != null) {
			result = new ArrayList<IWaveInstance>();
			
			for(int intCount=0; intCount < waveInstances.length; intCount++) {
				result.add(decodeWaveInstance(waveInstances[intCount]));
			}
		}
		return result;
	}
	
	public static IWaveInstance decodeWaveInstance(DeliveryWaveInstance waveInstance) {
		
		IWaveInstance result = null;
		if(waveInstance != null) {
			result = new WaveInstance();
			
			result.setMaxRunTime(waveInstance.getMaximumRuntime());
			result.setNoOfResources(waveInstance.getNumberOfVehicles());
			result.setPreferredRunTime(waveInstance.getPreferredRuntime());
			result.setRoutingWaveInstanceId(""+waveInstance.getWaveIdentity().getInternalWavePKey());
			result.setCutOffTime(waveInstance.getWaveCode() != null ? new RoutingTimeOfDay(RoutingDateUtil.getWaveCutOffTime(waveInstance.getWaveCode())) : null);
			result.setWaveStartTime(waveInstance.getStartTime() != null ? new RoutingTimeOfDay(waveInstance.getStartTime()
																						.getAsCalendar().getTime()) : null);
			result.setAdvancedRushHour(waveInstance.getAdvancedRushHour());
			result.setCapacityCheck1(waveInstance.getCapacityCheck1());
			result.setCapacityCheck2(waveInstance.getCapacityCheck2());
			result.setCapacityCheck3(waveInstance.getCapacityCheck3());
			result.setDepotId(decodeIRoutingDepotId(waveInstance.getDepot()));
			result.setEquipmentType(decodeEquipmentType(waveInstance.getEquipmentType()));
			result.setHourlyWage(waveInstance.getHourlyWage());
			result.setHourlyWageDuration(waveInstance.getHourlyWageDuration());
			result.setInboundStemTimeAdjustmentSeconds(waveInstance.getInboundStemTimeAdjustmentSeconds());
			result.setOutboundStemTimeAdjustmentSeconds(waveInstance.getOutboundStemTimeAdjustmentSeconds());
			result.setOvertimeWage(waveInstance.getOvertimeWage());
			result.setRushHourModel(waveInstance.getRushHourModel());			
			result.setTimePeriodTravelSpeedsType(decodeTimePeriodTravelSpeedsType(waveInstance.getTimePeriodTravelSpeedsType()));
			result.setTODRestrictionModel(waveInstance.getTODRestrictionModel());
			
		}
		return result;
	}
	
	public static IRoutingEquipmentType decodeEquipmentType(EquipmentTypeIdentity type) {
		
		IRoutingEquipmentType equipmentType = new RoutingEquipmentType();
		equipmentType.setEquipmentTypeID(type.getEquipmentTypeID());
		equipmentType.setRegionID(type.getRegionID());
		return equipmentType;
	}
	
	public static String decodeTimePeriodTravelSpeedsType(TimePeriodBasedTravelSpeedsType timePeriodBasedTravelSpeedsType) {
		
		return timePeriodBasedTravelSpeedsType.getValue();
	}

	public static IRoutingDepotId decodeIRoutingDepotId(LocationIdentity id) {
		
		IRoutingDepotId depot = new RoutingDepotId();
		depot.setLocationId(id.getLocationID());
		depot.setLocationType(id.getLocationType());
		depot.setRegionID(id.getRegionID());
		return depot;
	}

	public static List<EquipmentType> decodeEquipmentTypes(com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] types){
			List<EquipmentType> equipmentTypes = new ArrayList<EquipmentType>();
			if(types!=null)
			{
				for(int i=0;i<types.length;i++)
				{
					equipmentTypes.add(decodeEquipmentType(types[i]));
				}
			}
			return equipmentTypes;
			
	}

	private static EquipmentType decodeEquipmentType(
			com.freshdirect.routing.proxy.stub.transportation.EquipmentType type) {
		EquipmentType equipmentType = new EquipmentType();
		equipmentType.setId(type.getEquipmentTypeIdentity().getEquipmentTypeID());
		equipmentType.setDescription(type.getDescription());
		equipmentType.setFuelConsumption(type.getFuelConsumption());
		equipmentType.setHeight(type.getHeight());
		equipmentType.setWeight(type.getWeight());
		equipmentType.setRushHourRestrictions(type.getRushHourRestrictions());
		equipmentType.setTravelRestrictions(type.getTravelRestrictions());
		return equipmentType;
	}
	
	public static List<IRouteModel> decodeDeliveryAreaRoutes(DeliveryAreaRoute[] routes){
		List<IRouteModel> result = null;
		if(routes != null) {
			result = new ArrayList<IRouteModel>();
			
			for(int intCount=0; intCount < routes.length; intCount++) {
				result.add(decodeDeliveryAreaRoute(routes[intCount]));
			}
		}
		return result;
	}
	
	public static IRouteModel decodeDeliveryAreaRoute(DeliveryAreaRoute route) {
		
		IRouteModel result = null;
		
		if(route != null) {
			
			result = new RouteModel();
			
			result.setStartTime(route.getStartTime() != null ? route.getStartTime().getAsCalendar().getTime() : null);
			result.setOriginId(route.getDepotLocationId());
			result.setRouteId(Integer.toString(route.getIdentity().getRouteId()));
			result.setStops(new TreeSet());
			IRoutingStopModel _stop = null;
			IDeliveryModel deliveryInfo = null;
			result.setWaveId(""+route.getWaveId());
			
			DeliveryAreaOrder _refStop = null;
			int lastSequence = 0;
			
			if(route.getOrders() != null) {
				for(int intCount=0;intCount<route.getOrders().length ;intCount++) {
					_refStop = route.getOrders()[intCount];
					
					if(_refStop.getSequenceNumber() >= 0 && _refStop.getConfirmed()) {
						_stop = new RoutingStopModel(_refStop.getSequenceNumber() >= 0 ? _refStop.getSequenceNumber() : lastSequence);
						lastSequence =  _refStop.getSequenceNumber();
						deliveryInfo = new DeliveryModel();
						if(_refStop.getDeliveryWindowStart() != null && _refStop.getDeliveryWindowStart().getAsCalendar()!=null)
								deliveryInfo.setDeliveryStartTime(_refStop.getDeliveryWindowStart().getAsCalendar().getTime());
						if(_refStop.getDeliveryWindowEnd() != null && _refStop.getDeliveryWindowEnd().getAsCalendar()!=null)
								deliveryInfo.setDeliveryEndTime(_refStop.getDeliveryWindowEnd().getAsCalendar().getTime());
					
						
						_stop.setDeliveryInfo(deliveryInfo);
						_stop.setStopArrivalTime(_refStop.getPlannedArrivalTime().getTime());
						_stop.setOrderNumber(_refStop.getReferenceNumber());
						_stop.setServiceTime(_refStop.getServiceTime());
						
						result.getStops().add(_stop);
					}
				}
			}
		}
		return result;
	}	
}
