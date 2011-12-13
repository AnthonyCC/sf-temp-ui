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
import com.freshdirect.routing.model.HandOffBatchDepotSchedule;
import com.freshdirect.routing.model.HandOffBatchRoute;
import com.freshdirect.routing.model.HandOffBatchStop;
import com.freshdirect.routing.model.HandOffBatchTrailer;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IFacilityModel;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IHandOffBatchDepotScheduleEx;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchSession;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.model.IHandOffBatchTrailer;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.ITrailerModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.RouteModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.TrailerModel;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
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
		RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();
		DeliveryServiceProxy dlvProxy = new DeliveryServiceProxy();
		
		Map<IHandOffBatchSession, Map<String, Set<IRouteModel>>> sessionMapping = new HashMap<IHandOffBatchSession
																						, Map<String, Set<IRouteModel>>>();
		
		List<IHandOffBatchStop> s_stops = new ArrayList<IHandOffBatchStop>();
		List<IHandOffBatchRoute> s_routes = new ArrayList<IHandOffBatchRoute>();
		List<IHandOffBatchTrailer> s_trailers = new ArrayList<IHandOffBatchTrailer>();
		IHandOffBatchStop s_stop = null;
		IHandOffBatchRoute s_route = null;
		IHandOffBatchTrailer s_trailer = null;
		
		proxy.addNewHandOffBatchDepotSchedules(this.getBatch().getBatchId(), this.getBatch().getDepotSchedule());
		proxy.addNewHandOffBatchDepotSchedulesEx(dayOfWeek,this.getBatch().getCutOffDateTime(), masterDepotSchedule);
		
		proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.PROCESSING);
		proxy.addNewHandOffBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
												, EnumHandOffBatchActionType.ROUTEOUT, this.getUserId());
		proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_ROUTINGOUTPROGRESS);

		Map<String, IAreaModel> areaLookup = geoProxy.getAreaLookup();
		Map<String, IZoneModel> zoneLookup = geoProxy.getZoneLookup();
		Map<String, IFacilityModel> facilityLookup = geoProxy.getFacilityLookup();
		Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> currDispStatus = null;
		String lastCommittedBatchId = null;
		List<DispatchTrailerCorrelationResult> trailerResult = new ArrayList<DispatchTrailerCorrelationResult>();

		IServiceTimeScenarioModel scenarioModel = routingInfoProxy.getRoutingScenarioByCode(HandOffRoutingOutAction.this.getBatch().getServiceTimeScenario());
		if(scenarioModel == null) {
			throw new RoutingProcessException(null, null, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}

		//Description -> Map<DestinatinFacility, Map<DispatchTIme, Map<CutOffTime, IWaveInstance>>>
		Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> plannedTrailerDispatchTree 
									= routingInfoProxy.getPlannedTrailerDispatchTree(this.getBatch().getDeliveryDate(), this.getBatch().getCutOffDateTime());

		if(this.getBatch() != null && this.getBatch().getBatchId() != null) {
			
			if(this.getBatch() != null) {
				lastCommittedBatchId = proxy.getLastCommittedHandOffBatch(this.getBatch().getDeliveryDate());
				if(lastCommittedBatchId != null) {
					currDispStatus = proxy.getHandOffBatchDispatchStatus(lastCommittedBatchId);
				}
				proxy.clearHandOffBatchStopRoute(this.getBatch().getDeliveryDate(), this.getBatch().getBatchId());
				Map<String, Integer> routeCnts = proxy.getHandOffBatchRouteCnt(this.getBatch().getDeliveryDate());
				Map<String, Integer> trailerCnts = proxy.getHandOffBatchTrailerCnt(this.getBatch().getDeliveryDate());
								
				Set<IHandOffBatchSession> sessions = this.getBatch().getSession();
				if(sessions != null) {
					for(IHandOffBatchSession session : sessions) {
						IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
						schedulerId.setRegionId(session.getRegion());
						schedulerId.setDeliveryDate(this.getBatch().getDeliveryDate());
						schedulerId.setDepot(session.isDepot());
						List<IRouteModel> routes = dlvProxy.retrieveRoutingSession(schedulerId, session.getSessionName(), session.isDepot());
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

				/*OriginLocation->RouteModel*/
				Map<String, List<IHandOffBatchRoute>> routeLocationMapping = new HashMap<String, List<IHandOffBatchRoute>>();	
							String originLocationId = null;
				for(IHandOffBatchRoute _routeModel : s_routes) {
					originLocationId = _routeModel.getOriginId();
					_routeModel.setDispatchTime(_routeModel.getStartTime() != null 
							? new RoutingTimeOfDay(RoutingDateUtil.addMinutes(_routeModel.getStartTime(), -25)) : null);

								IFacilityModel model = facilityLookup.get(originLocationId);
								if(model != null && IFacilityModel.CROSS_DOCK.equalsIgnoreCase(model.getFacilityTypeModel())){
									if(!routeLocationMapping.containsKey(originLocationId)){
							routeLocationMapping.put(originLocationId, new ArrayList<IHandOffBatchRoute>());
			}
						routeLocationMapping.get(originLocationId).add(_routeModel);
		}
								}

				/*OriginLocation->DispatchTrailerCorrelationResult*/
				Map<String, DispatchTrailerCorrelationResult> trailerRouteMapping = new HashMap<String, DispatchTrailerCorrelationResult>();
		
				for(Map.Entry<String, List<IHandOffBatchRoute>> locEntry : routeLocationMapping.entrySet()) {
		
					DispatchTrailerCorrelationResult result = this.assignRoutesToTrailers(locEntry.getKey(), locEntry.getValue()
																							, plannedTrailerDispatchTree.get(locEntry.getKey())
																							, scenarioModel);
					trailerRouteMapping.put(locEntry.getKey(), result);
				}

				for (Map.Entry<String, DispatchTrailerCorrelationResult> locEntry : trailerRouteMapping.entrySet()) {
					if (locEntry.getValue() != null) {

							DispatchTrailerCorrelationResult result = locEntry.getValue();
							trailerResult.add(result);
							if(result.getTrailers() != null){
								for(ITrailerModel trailer : result.getTrailers()){							
									if(!trailerCnts.containsKey(locEntry.getKey())) {
										trailerCnts.put(locEntry.getKey(), 0);
									}
									trailerCnts.put(locEntry.getKey(), trailerCnts.get(locEntry.getKey()).intValue()+1);
									trailer.appendRoutingTrailer(trailer.getTrailerId());
									String formattedTrailerSeq = RoutingServicesProperties.isTrailerNoFormatEnabled() 
												?  formatTrailerNumber(trailerCnts.get(locEntry.getKey())): formatRouteNumber(trailerCnts.get(locEntry.getKey()));
									trailer.setTrailerId(facilityLookup.get(locEntry.getKey()).getPrefix()
											+ splitStringForCode(trailer.getTrailerId())+ formattedTrailerSeq);
									Iterator<IRouteModel> itr = trailer.getRoutes().iterator();
									IRouteModel _route = null;								
									IHandOffBatchRoute t_route = null; 
									while(itr.hasNext()){
										_route = itr.next();
										t_route = new HandOffBatchRoute(_route); 
										if(t_route.getTrailerId() == null &&
												trailer.getTrailerId() != null && trailer.getTrailerId().length() > 0){
											t_route.setTrailerId(trailer.getTrailerId());
										}
									}
									s_trailer = new HandOffBatchTrailer(trailer);
									s_trailer.setBatchId(this.getBatch().getBatchId());
									s_trailer.setOriginId(locEntry.getKey());								
									s_trailers.add(s_trailer);
								}
							}
					}
				}
			}
		}
		
		
		//processResult(this.getBatch().getBatchId(), sessionMapping);
		DispatchCorrelationResult correlationResult = null;
		if(RoutingServicesProperties.getHandOffDispatchCorrelationEnabled()) {			
			correlationResult = checkRouteMismatch(s_routes, areaLookup, currDispStatus);	
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
		proxy.updateHandOffBatchDetails(this.getBatch().getBatchId(), s_trailers, s_routes, s_stops, correlationResult.getDispatchStatus());
		
		checkStopExceptions();
		checkTrailerRouteExceptions(trailerResult);
				
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
	
	protected class StopComparator implements Comparator<IRoutingStopModel> {		
		
		public int compare(IRoutingStopModel obj1, IRoutingStopModel obj2){
			if(obj1!=null && obj2!=null 
					&& obj1.getDeliveryInfo()!=null && obj2.getDeliveryInfo()!=null
					&& obj1.getDeliveryInfo().getDeliveryStartTime()!=null && obj2.getDeliveryInfo().getDeliveryStartTime()!=null  )
			{
				return obj1.getDeliveryInfo().getDeliveryStartTime().compareTo(obj2.getDeliveryInfo().getDeliveryStartTime());
			}
			return 0;
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
								//|| IRoutingStopModel.DEPOT_STOPNO.equalsIgnoreCase(_order.getOrderNumber())) {
								|| _order.getOrderNumber().startsWith(IRoutingStopModel.DEPOT_STOPNO)) {
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
				
				List orders = route.getOrders();
				
				//sort by delivery window only if the property is set
				if(RoutingServicesProperties.sortStopbyWindow())
					Collections.sort(orders, new StopComparator());
				
				Iterator itr = orders.iterator();
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
	
	private void checkTrailerRouteExceptions(List<DispatchTrailerCorrelationResult> result) throws RoutingServiceException {
		Iterator<DispatchTrailerCorrelationResult> itr = result.iterator();
		StringBuffer errorBuf = new StringBuffer();
		while(itr.hasNext()){
			DispatchTrailerCorrelationResult _r = itr.next();
			if (_r != null && _r.getUnassignedRoutes() != null
												&& _r.getUnassignedRoutes().size() > 0) {
				for (IRouteModel unassignedRoute : _r.getUnassignedRoutes()) {
					if (errorBuf.length() > 0) {
						errorBuf.append(",");
					}
					errorBuf.append(unassignedRoute.getRouteId()).append(
							unassignedRoute.getRoutingRouteId());
				}
				throw new RoutingServiceException("Routes unassigned to Trailers: " + errorBuf.toString(),
																				null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private DispatchTrailerCorrelationResult  assignRoutesToTrailers(String crossDockLoc, List<IHandOffBatchRoute> crossDockRoutes
										, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> plannedTrailerDispatchTree
										, IServiceTimeScenarioModel scenarioModel) {
		
		DispatchTrailerCorrelationResult result = new DispatchTrailerCorrelationResult();
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		
		List<IHandOffBatchStop> handOffStops = proxy.getOrderByCutoff(this.getBatch().getDeliveryDate()
																, this.getBatch().getCutOffDateTime());
		Map<String, Integer> orderNoToCartonNo = new HashMap<String, Integer>();		
		
		if(handOffStops != null) {
			for(IHandOffBatchStop stop : handOffStops) {
				if(stop.getDeliveryInfo() != null && stop.getDeliveryInfo().getPackagingDetail() != null)
					orderNoToCartonNo.put(stop.getOrderNumber(), (int)stop.getDeliveryInfo().getPackagingDetail().getNoOfCartons());
			}
		}

		int maxCartonsPerCont = 0;
		int maxContPerTrailer = 0;
		if(scenarioModel != null && scenarioModel.getDefaultContainerCartonCount() !=0 &&
				scenarioModel.getDefaultTrailerContainerCount() != 0){
			 maxCartonsPerCont = scenarioModel.getDefaultContainerCartonCount();
			 maxContPerTrailer = scenarioModel.getDefaultTrailerContainerCount();
		}else{
			maxCartonsPerCont = RoutingServicesProperties.getMaxTrailerCartonSize();
			maxContPerTrailer = RoutingServicesProperties.getMaxTrailerContainerSize();
		}
		
		Set<ITrailerModel> trailers = new TreeSet<ITrailerModel>(new TrailerComparator1());
		StringBuffer errorBuf = new StringBuffer();
			
		if(plannedTrailerDispatchTree != null && plannedTrailerDispatchTree.size() > 0){
			for(Map.Entry<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> dispatchEntry : plannedTrailerDispatchTree.entrySet()){
				for(Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> waveInstanceEntry : dispatchEntry.getValue().entrySet()){
					List<IWaveInstance> waveInstances = waveInstanceEntry.getValue();
					if(waveInstances != null){
						Iterator<IWaveInstance> itr = waveInstances.iterator();
						int trailerCount = 0;
						while(itr.hasNext()){
							IWaveInstance _waveInstance = itr.next();
						
							ITrailerModel model = new TrailerModel();
							trailers.add(model);
							
							model.setTrailerId(_waveInstance.getRoutingCode()+"-"+(++trailerCount));
							model.setDispatchTime(_waveInstance.getDispatchTime());
							model.setPreferredRunTime(_waveInstance.getPreferredRunTime());
							model.setMaxRunTime(_waveInstance.getMaxRunTime());
							model.setStartTime(_waveInstance.getWaveStartTime().getAsDate());											
							model.setCompletionTime(RoutingDateUtil.addSeconds(model.getStartTime(), model.getMaxRunTime()));
							
							model.setRoutes(new TreeSet<IHandOffBatchRoute>(
									new RouteComparator1()));
						}
					}
				}
			}
		} else {
			for (IHandOffBatchRoute unassignedRoute : crossDockRoutes) {
				if (errorBuf.length() > 0) {
					errorBuf.append(",");
				}
				errorBuf.append(unassignedRoute.getRouteId()).append(
						unassignedRoute.getRoutingRouteId());
			}
			throw new RoutingServiceException("CrossDock Loc: " + crossDockLoc + ", No planned trailers to assign routes: " + errorBuf.toString(),
					null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}

		for(ITrailerModel trailer : trailers){
			double trailerContainerCnt = 0;						
			Collections.sort(crossDockRoutes, new HandOffBatchRouteComparator());
			Iterator<IHandOffBatchRoute> routeItr = crossDockRoutes.iterator();
			while(routeItr.hasNext()){
				IHandOffBatchRoute route = routeItr.next();
				if(route.getDispatchTime() != null && route.getDispatchTime().after(new RoutingTimeOfDay(trailer.getCompletionTime()))){
									double routeCartonCnt = 0;
									double routeContCnt = 0;
									Iterator<IRoutingStopModel> _stopItr = route.getStops().iterator();							
									IRoutingStopModel _order = null;
									while(_stopItr.hasNext()){
										_order = _stopItr.next();
										routeCartonCnt += orderNoToCartonNo.get(_order.getOrderNumber())!= null 
																			? orderNoToCartonNo.get(_order.getOrderNumber()) : 0;								
									}
									
									routeContCnt = routeCartonCnt/maxCartonsPerCont;
									
									if ((routeContCnt + trailerContainerCnt) < maxContPerTrailer) {
										trailerContainerCnt += routeContCnt;
										trailer.getRoutes().add(route);
										routeItr.remove();
									} else {
										break;
									}
				}
			}
			if(trailer.getRoutes().size() > 0){
				if(result.getTrailers() == null) { 
					result.setTrailers(new TreeSet<ITrailerModel>(new TrailerComparator()));
				}
				result.getTrailers().add(trailer);
				}
			}
		result.setUnassignedRoutes(crossDockRoutes);
		return result;
	}

	class DispatchTrailerCorrelationResult {

		List<IHandOffBatchRoute> unassignedRoutes;
		Set<ITrailerModel> trailers = new TreeSet<ITrailerModel>(new TrailerComparator1());

		public List<IHandOffBatchRoute> getUnassignedRoutes() {
			return unassignedRoutes;
		}
		public void setUnassignedRoutes(List<IHandOffBatchRoute> unassignedRoutes) {
			this.unassignedRoutes = unassignedRoutes;
		}
		public Set<ITrailerModel> getTrailers() {
			return trailers;
		}
		public void setTrailers(Set<ITrailerModel> trailers) {
			this.trailers = trailers;
		}
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
	
	private class HandOffBatchRouteComparator implements Comparator<IHandOffBatchRoute> {

		public int compare(IHandOffBatchRoute route1, IHandOffBatchRoute route2) {
			if(route1.getDispatchTime() != null &&  route2.getDispatchTime() != null) {
				return route1.getDispatchTime().getAsDate().compareTo(route2.getDispatchTime().getAsDate());
}
			return 0;
		}

	}
}
