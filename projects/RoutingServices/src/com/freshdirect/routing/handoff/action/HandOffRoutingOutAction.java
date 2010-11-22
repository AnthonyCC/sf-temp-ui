package com.freshdirect.routing.handoff.action;

import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_ROUTINGOUTCOMPLETED;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_ROUTINGOUTPROGRESS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumHandOffDispatchStatus;
import com.freshdirect.routing.handoff.action.AbstractHandOffAction.DispatchCorrelationResult;
import com.freshdirect.routing.model.HandOffBatchDepotSchedule;
import com.freshdirect.routing.model.HandOffBatchRoute;
import com.freshdirect.routing.model.HandOffBatchStop;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IHandOffBatchDepotScheduleEx;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchSession;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.RouteModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class HandOffRoutingOutAction extends AbstractHandOffAction {
	
	private Set<IHandOffBatchDepotScheduleEx> masterDepotSchedule;
	private String dayOfWeek;
	
	public HandOffRoutingOutAction(IHandOffBatch batch, String userId, String dayOfWeek,
										Set<IHandOffBatchDepotScheduleEx> masterDepotSchedule) {
		super(batch, userId);
		this.masterDepotSchedule = masterDepotSchedule;
		this.dayOfWeek = dayOfWeek;
	}

	public Object doExecute() throws Exception {
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		GeographyServiceProxy geoProxy = new GeographyServiceProxy();
		
		DeliveryServiceProxy dlvProxy = new DeliveryServiceProxy();
		Map<IHandOffBatchSession, Map<String, Set<IRouteModel>>> sessionMapping = new HashMap<IHandOffBatchSession
																						, Map<String, Set<IRouteModel>>>();
		
		List<IHandOffBatchStop> s_stops = new ArrayList<IHandOffBatchStop>();
		List<IHandOffBatchRoute> s_routes = new ArrayList<IHandOffBatchRoute>();
		IHandOffBatchStop s_stop = null;
		IHandOffBatchRoute s_route = null;
		
				
		proxy.addNewHandOffBatchDepotSchedules(this.getBatch().getBatchId(), this.getBatch().getDepotSchedule());
		proxy.addNewHandOffBatchDepotSchedulesEx(dayOfWeek,this.getBatch().getCutOffDateTime(), masterDepotSchedule);
		
		proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.PROCESSING);
		proxy.addNewHandOffBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
												, EnumHandOffBatchActionType.ROUTEOUT, this.getUserId());
		proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_ROUTINGOUTPROGRESS);
		Map<String, IAreaModel> areaLookup = geoProxy.getAreaLookup();
		Map<String, IZoneModel> zoneLookup = geoProxy.getZoneLookup();
		
		if(this.getBatch() != null && this.getBatch().getBatchId() != null) {
			
			if(this.getBatch() != null) {
				proxy.clearHandOffBatchStopRoute(this.getBatch().getDeliveryDate(), this.getBatch().getBatchId());
				Map<String, Integer> routeCnts = proxy.getHandOffBatchRouteCnt(this.getBatch().getDeliveryDate());
								
				Set<IHandOffBatchSession> sessions = this.getBatch().getSession();
				if(sessions != null) {
					for(IHandOffBatchSession session : sessions) {
						IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
						schedulerId.setRegionId(session.getRegion());
						schedulerId.setDeliveryDate(this.getBatch().getDeliveryDate());
						schedulerId.setDepot(session.isDepot());
						List<IRouteModel> routes = dlvProxy.retrieveRoutingSession(schedulerId, session.getSessionName());
						Map<String, Set<IRouteModel>> routeMapping = new HashMap<String, Set<IRouteModel>>();
						
						String routeArea = null;
						sessionMapping.put(session, routeMapping);
						for(IRouteModel route : routes) {
							routeArea = getRouteArea(route.getRouteId());
							if(!routeMapping.containsKey(routeArea)) {
								routeMapping.put(routeArea, new TreeSet<IRouteModel>(new RouteComparator()));
							}
							routeMapping.get(routeArea).add(route);
						}						
					}						
				}
				
				for(Map.Entry<IHandOffBatchSession, Map<String, Set<IRouteModel>>> sesEntry : sessionMapping.entrySet()) {
					if(sesEntry.getKey().isDepot()) {
						Map<String, Set<IRouteModel>> convertedDptRoutes = convertDptToRegOrders(sessionMapping.get(sesEntry.getKey()));						
						sessionMapping.put(sesEntry.getKey(), convertedDptRoutes);
					} 					
				}
				
				for(Map.Entry<IHandOffBatchSession, Map<String, Set<IRouteModel>>> sesEntry : sessionMapping.entrySet()) {
			
					for(Map.Entry<String, Set<IRouteModel>> areaEntry : sesEntry.getValue().entrySet()) {
						
						if(areaEntry.getValue() != null) {
							for(IRouteModel route : areaEntry.getValue()) {
								if(!routeCnts.containsKey(areaEntry.getKey())) {
									routeCnts.put(areaEntry.getKey(), 0);
								}
								routeCnts.put(areaEntry.getKey(), routeCnts.get(areaEntry.getKey()).intValue()+1);
								route.appendRoutingRoute(route.getRouteId());//New VArray Change
								route.setRouteId(areaLookup.get(areaEntry.getKey()).getPrefix()
														+ areaEntry.getKey()+ formatRouteNumber(routeCnts.get(areaEntry.getKey())));
								Iterator itr = route.getStops().iterator();
								IRoutingStopModel _stop = null;
								while(itr.hasNext()) {
									_stop = (IRoutingStopModel)itr.next();
									if(_stop.getStopArrivalTime() == null || _stop.getStopDepartureTime() == null) {
										throw new RoutingServiceException("Stop Arrival/Departure Time is not found. Check the routing session"
												, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
									}
									s_stop = new HandOffBatchStop(_stop);
									s_stop.setBatchId(this.getBatch().getBatchId());
									s_stop.setRouteId(route.getRouteId());
									s_stop.setSessionName(sesEntry.getKey().getSessionName());
									if(s_stop.getRoutingRouteId() == null
											&& route.getRoutingRouteId() != null
												&& route.getRoutingRouteId().size() > 0) {
										s_stop.setRoutingRouteId(route.getRoutingRouteId().get(0));
									}
									s_stops.add(s_stop);
								}
								s_route = new HandOffBatchRoute(route);
								s_route.setBatchId(this.getBatch().getBatchId());
								s_route.setArea(areaEntry.getKey());
								s_route.setSessionName(sesEntry.getKey().getSessionName());
								s_routes.add(s_route);
							}
						}
					}
			
				}
			}
		}
		
		
		//processResult(this.getBatch().getBatchId(), sessionMapping);
		DispatchCorrelationResult correlationResult = null;
		if(RoutingServicesProperties.getHandOffDispatchCorrelationEnabled()) {
			correlationResult = checkRouteMismatch(s_routes, areaLookup);	
			assignDispatchSequence(s_routes, zoneLookup, proxy.getHandOffBatchDispatchCnt(this.getBatch().getDeliveryDate()));		
		} else {
			// This is the rollback strategy for Dispatch Time Correlation.
			correlationResult = new DispatchCorrelationResult();
			Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus = new TreeMap<RoutingTimeOfDay, EnumHandOffDispatchStatus>();
			if(s_routes != null) {
				for(IHandOffBatchRoute _routeModel : s_routes) {
					_routeModel.setDispatchTime(_routeModel.getStartTime() != null 
							? new RoutingTimeOfDay(RoutingDateUtil.addMinutes(_routeModel.getStartTime(), -25)) : null);
					_routeModel.setDispatchSequence(0);
					dispatchStatus.put(_routeModel.getDispatchTime(), EnumHandOffDispatchStatus.COMPLETE);
				}
			}
		}
		proxy.updateHandOffBatchDetails(this.getBatch().getDeliveryDate(), s_routes, s_stops, correlationResult.getDispatchStatus());
		
		checkStopExceptions();
				
		proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.ROUTEGENERATED);
		proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_ROUTINGOUTCOMPLETED);
		return null;		
	}
	
	private void assignDispatchSequence(List<IHandOffBatchRoute> routes, Map<String, IZoneModel> zoneLookup
											, Map<RoutingTimeOfDay, Integer> currDispatchSequence) {
		
		if(routes != null) {
			//Group by Dispatch Time to Assign Sequence
			Map<RoutingTimeOfDay, List<IHandOffBatchRoute>> dispatchRouteMpp = new TreeMap<RoutingTimeOfDay, List<IHandOffBatchRoute>>();
			for(IHandOffBatchRoute route : routes) {
				if(!dispatchRouteMpp.containsKey(route.getDispatchTime())) {
					dispatchRouteMpp.put(route.getDispatchTime(), new ArrayList<IHandOffBatchRoute>());
				}
				dispatchRouteMpp.get(route.getDispatchTime()).add(route);
			}
			
			DispatchSequenceComparator seqComparator = new DispatchSequenceComparator(zoneLookup);
			
			for(Map.Entry<RoutingTimeOfDay, List<IHandOffBatchRoute>> dispatchRouteEntry : dispatchRouteMpp.entrySet()) {
				Collections.sort(dispatchRouteEntry.getValue(), seqComparator);
				for(IHandOffBatchRoute route : dispatchRouteEntry.getValue()) {
					if(currDispatchSequence.containsKey(dispatchRouteEntry.getKey())) {
						route.setDispatchSequence(currDispatchSequence.get(dispatchRouteEntry.getKey()) + 1);
						currDispatchSequence.put(dispatchRouteEntry.getKey(), currDispatchSequence.get(dispatchRouteEntry.getKey()) + 1);
					} else {
						route.setDispatchSequence(1);
						currDispatchSequence.put(dispatchRouteEntry.getKey(), 1);
					}
				}
			}
		}
	}
	
	// Same Exception check but with a force option will performed during Commit Operation
	private void checkStopExceptions() throws RoutingServiceException {
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		
		List<IHandOffBatchStop> handOffStops = proxy.getOrderByCutoff(this.getBatch().getDeliveryDate()
																, this.getBatch().getCutOffDateTime());
		Map<String, String> orderNoToErpNo = new HashMap<String, String>();
		
		Map<String, EnumSaleStatus> exceptions = new HashMap<String, EnumSaleStatus>();
		if(handOffStops != null) {
			for(IHandOffBatchStop stop : handOffStops) {
				if(stop.getErpOrderNumber() == null || stop.getErpOrderNumber().trim().length() == 0) {
					exceptions.put(stop.getOrderNumber(), stop.getStatus());
				}
				orderNoToErpNo.put(stop.getOrderNumber(), stop.getErpOrderNumber());
			}
		}
		
		List<String> exceptionOrderIds = new ArrayList<String>();
		if(exceptions.size() > 0) {
			for(Map.Entry<String, EnumSaleStatus> exp : exceptions.entrySet()) {
				exceptionOrderIds.add(exp.getKey());
			}
		}
		List routes = proxy.getHandOffBatchRoutes(this.getBatch().getBatchId());
		List stops = proxy.getHandOffBatchStops(this.getBatch().getBatchId(), false);
		
		int noOfRoutes = routes != null ? routes.size() : 0;
		int noOfStops = stops != null ? stops.size() : 0;
		if(noOfRoutes == 0 || noOfStops == 0) {
			throw new RoutingServiceException("Error in route mapping check cutoff report"
													+ " ,"+noOfRoutes + "Routes /"+noOfStops+" Stops" 
													, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
		Map<String, IHandOffBatchRoute> routeMapping = new HashMap<String, IHandOffBatchRoute>();
		Iterator<IHandOffBatchRoute> itr = routes.iterator();
		while(itr.hasNext()) {
			IHandOffBatchRoute route = itr.next();
			routeMapping.put(route.getRouteId(), route);
		}
		List<IHandOffBatchStop> needsErpNoUpdate = new ArrayList<IHandOffBatchStop>();
		
		Iterator<IHandOffBatchStop> itrStop = stops.iterator();
		while( itrStop.hasNext()) {
			IHandOffBatchStop stop = itrStop.next();
			
			if((stop.getErpOrderNumber() == null || stop.getErpOrderNumber().trim().length() == 0)) {
				String strNewErpNo = orderNoToErpNo.get(stop.getOrderNumber());
				if((strNewErpNo != null && strNewErpNo.trim().length() > 0)) {
					
					stop.setErpOrderNumber(strNewErpNo);
					stop.setBatchId(this.getBatch().getBatchId());
					needsErpNoUpdate.add(stop);					
					exceptionOrderIds.remove(stop.getOrderNumber());
				}
			}
			if(stop.getRouteId() == null || stop.getRouteId().trim().length() == 0
					|| !routeMapping.containsKey(stop.getRouteId())) {
				throw new RoutingServiceException("Error in route generation check cutoff report Stop No:"+stop.getOrderNumber()
														+ " ,"+noOfRoutes + "Routes /"+noOfStops+" Stops"
															, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);				
			}
		}
		
		if(needsErpNoUpdate.size() > 0) {
			proxy.updateHandOffBatchStopErpNo(needsErpNoUpdate);
		}
		proxy.updateHandOffStopException(this.getBatch().getBatchId(), exceptionOrderIds);
	}
		
	protected class RouteComparator implements Comparator<IRouteModel> {		
		
		public int compare(IRouteModel obj1, IRouteModel obj2){

			int routeId1 = getRouteIndex(( (IRouteModel) obj1).getRouteId());
			int routeId2 = getRouteIndex(( (IRouteModel) obj2).getRouteId());			
			return routeId1 - routeId2;
		}		
	}
	
	@Override
	public EnumHandOffBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumHandOffBatchStatus.ROUTEGENFAILED;
	}
	
	protected Map<String, Set<IRouteModel>> convertDptToRegOrders(Map<String, Set<IRouteModel>> dptRoutes) throws RoutingServiceException {
				
		int allowedDepartTimeDiff = RoutingServicesProperties.getDepotDepartTimeDiff();
		
		if(dptRoutes != null && this.getBatch().getDepotSchedule() != null) {
			Map<String, List<CustomTruckScheduleInfo>> groupSchedule = new TreeMap<String, List<CustomTruckScheduleInfo>>();
			for(IHandOffBatchDepotSchedule _schInfo : this.getBatch().getDepotSchedule()) {
				if(groupSchedule.containsKey(_schInfo.getArea())) {
					((List<CustomTruckScheduleInfo>)groupSchedule.get(_schInfo.getArea())).add
												(new CustomTruckScheduleInfo(new ArrayList(),_schInfo));
				} else {
					List<CustomTruckScheduleInfo> _tmpValues = new ArrayList<CustomTruckScheduleInfo>();
					_tmpValues.add(new CustomTruckScheduleInfo(new ArrayList(),_schInfo));
					groupSchedule.put(_schInfo.getArea(), _tmpValues);
				}
			}
					
			sortGroup(groupSchedule);	
			for(Map.Entry<String, Set<IRouteModel>> areaEntry : dptRoutes.entrySet()) {
				Date currDepotDeparture = null;
				String currRouteId = null;
				for(IRouteModel route : areaEntry.getValue()) {
					Iterator itr = route.getStops().iterator();
					IRoutingStopModel _order = null;
					String routeID = route.getRouteId();
					CustomTruckScheduleInfo _matchSchedule = null;
					
					while(itr.hasNext()) {
						_order = (IRoutingStopModel)itr.next();
						
						if(currRouteId == null) {
							currRouteId = routeID;
						}
						
						if(_order.getOrderNumber() == null || _order.getOrderNumber().trim().length() == 0
								|| IRoutingStopModel.DEPOT_STOPNO.equalsIgnoreCase(_order.getOrderNumber())) {
							currDepotDeparture = _order.getStopArrivalTime();
							continue;
						} else if(currRouteId != null && !currRouteId.equalsIgnoreCase(routeID)) {
							currDepotDeparture = null;
							currRouteId = routeID;
						}
						
						if(currDepotDeparture == null) {
							if(allowedDepartTimeDiff != 0 
										&& RoutingDateUtil.getDiffInHours(_order.getDeliveryInfo().getDeliveryStartTime()
																				, route.getStartTime())
										> allowedDepartTimeDiff) {
								_order.setStopDepartureTime(_order.getDeliveryInfo().getDeliveryStartTime());
							} else {
								_order.setStopDepartureTime(route.getStartTime());
							}
						} else {
							_order.setStopDepartureTime(currDepotDeparture);
						}
						_matchSchedule = matchSchedule(_order
														, (List)groupSchedule.get(splitStringForCode(routeID))
														, route.getOriginId());
						if(_matchSchedule != null) {
							_order.setRoutingRouteId(routeID); //_order.setRouteStartTime(_matchSchedule.getTruckDepartureTime());
							_matchSchedule.addOrder(_order);
						} else {
							throw new RoutingServiceException("Invalid Depot Truck Schedule File : SAP Order No:" + _order.getOrderNumber()
									, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
							
						}
					}
				}
			}	
			
			return updateDptRouteStop(groupSchedule);
		}
		return null;
	}
	
	private Map<String, Set<IRouteModel>> updateDptRouteStop(Map<String, List<CustomTruckScheduleInfo>> groupSchedule) {
		
		Map<String, Set<IRouteModel>> result = new HashMap<String, Set<IRouteModel>>();			
								
		for(Map.Entry<String, List<CustomTruckScheduleInfo>> areaEntry : groupSchedule.entrySet()) {
			result.put(areaEntry.getKey(), new TreeSet<IRouteModel>(new RouteComparator()));
			int intRountCount = 0;
			for(CustomTruckScheduleInfo route : areaEntry.getValue()) {
				
				intRountCount++;
				IRouteModel newRoute = new RouteModel(); 
				
				newRoute.setRouteId(areaEntry.getKey()+"-"+intRountCount);
				newRoute.setStartTime(route.getTruckDepartureTime());
				newRoute.setCompletionTime(route.getDepotArrivalTime());
				
				newRoute.setStops(new TreeSet());
				
				result.get(areaEntry.getKey()).add(newRoute);
				
				Iterator itr = route.getOrders().iterator();
				IRoutingStopModel _order = null;
				
				int intStopCount = 0;
				
				while(itr.hasNext()) {
					_order = (IRoutingStopModel)itr.next();
					
					intStopCount++;
					_order.setStopNo(intStopCount);
					
					newRoute.appendRoutingRoute(_order.getRoutingRouteId());
					newRoute.getStops().add(_order);
					
				}
			}
		}
		return result;
	}
	
	private CustomTruckScheduleInfo matchSchedule(IRoutingStopModel order, List scheduleLst, String routeOriginId) {
		
		CustomTruckScheduleInfo _schInfo = null;
		CustomTruckScheduleInfo _preSchInfo = null;
		
		if(scheduleLst != null) {
			
			Iterator _itrSchedule = scheduleLst.iterator();
									
			while(_itrSchedule.hasNext()) {
				_schInfo = (CustomTruckScheduleInfo)_itrSchedule.next();
				
				if(_schInfo.getOriginId() != null &&
						routeOriginId != null && routeOriginId.equalsIgnoreCase(_schInfo.getOriginId())) {
					if(_schInfo.getDepotArrivalTime().after(order.getStopDepartureTime())) {
						break;
					} else {
						_preSchInfo = _schInfo;
					}
				}
			}
		}
		return _preSchInfo;
	}
	
	private class CustomTruckScheduleInfo extends HandOffBatchDepotSchedule {
		
		List orders = null;
				
		public CustomTruckScheduleInfo(List orders, IHandOffBatchDepotSchedule info) {
			super();
			this.orders = orders;
			this.setArea(info.getArea());
			this.setDepotArrivalTime(info.getDepotArrivalTime());
			this.setTruckDepartureTime(info.getTruckDepartureTime());
			this.setOriginId(info.getOriginId());
		}
				
		public List getOrders() {
			return orders;
		}
		
		public void setOrders(List orders) {
			this.orders = orders;
		}
		
		public void addOrder(Object order) {
			this.orders.add(order);
		}
		
		public String toString() {
			//return orders.toString()+","+super.toString()+"\n";
			return super.toString()+"\n";
		}
	}
		
	private class TruckScheduleComparator implements Comparator {		
		
		public int compare(Object obj1, Object obj2){

			Date arrivalData1 = ( (IHandOffBatchDepotSchedule) obj1).getDepotArrivalTime();
			Date arrivalData2 = ( (IHandOffBatchDepotSchedule) obj2).getDepotArrivalTime();			
			return arrivalData1.compareTo(arrivalData2);
		}		
	}
	
	private class DispatchSequenceComparator implements Comparator<IHandOffBatchRoute> {		
		Map<String, IZoneModel> zoneLookup;
		
		public DispatchSequenceComparator(Map<String, IZoneModel> zoneLookup) {
			super();
			this.zoneLookup = zoneLookup;
		}

		public int compare(IHandOffBatchRoute routeData1, IHandOffBatchRoute routeData2) {
						
			if(zoneLookup != null && routeData1 != null && routeData2 != null) {
				IZoneModel zoneDate1 = zoneLookup.get(routeData1.getArea());
				IZoneModel zoneDate2 = zoneLookup.get(routeData2.getArea());
				if(zoneDate1 != null && zoneDate2 != null) {
					if(zoneDate1.getLoadingPriority() == zoneDate2.getLoadingPriority()) {
						return 0;
					} else if (zoneDate1.getLoadingPriority() > zoneDate2.getLoadingPriority()) {
						return 1;
					}
				}
			}						
			return -1;
		}		
	}
	
	private void sortGroup(Map groupSchedule) {
		Iterator _itrGrpSchedule = groupSchedule.keySet().iterator();
		String grpKey = null;
		TruckScheduleComparator _schComparator = new TruckScheduleComparator();
		while(_itrGrpSchedule.hasNext()) {
			grpKey = (String)_itrGrpSchedule.next();
			Collections.sort(((List)groupSchedule.get(grpKey)), _schComparator);
		}
	}
	
}
