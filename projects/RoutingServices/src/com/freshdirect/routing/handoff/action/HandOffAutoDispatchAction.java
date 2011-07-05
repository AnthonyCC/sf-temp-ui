package com.freshdirect.routing.handoff.action;

import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_AUTODISPATCHCOMPLETED;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_AUTODISPATCHPROGRESS;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import lpsolve.LpSolveException;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumTruckPreference;
import com.freshdirect.routing.model.HandOffDispatch;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchDispatchResource;
import com.freshdirect.routing.model.IHandOffBatchPlan;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffDispatch;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.TruckPreferenceStat;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.truckassignment.Dispatch;
import com.freshdirect.routing.truckassignment.Employee;
import com.freshdirect.routing.truckassignment.Route;
import com.freshdirect.routing.truckassignment.TAPStatistics;
import com.freshdirect.routing.truckassignment.Truck;
import com.freshdirect.routing.truckassignment.TruckAssignmentSolver;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.sap.bapi.BapiInfo;
import com.freshdirect.sap.bapi.BapiSendPhysicalTruckInfo.HandOffRouteTruckIn;
import com.freshdirect.sap.command.SapSendPhysicalTruckInfo;
import com.freshdirect.sap.ejb.SapException;

public class HandOffAutoDispatchAction extends AbstractHandOffAction {
	private static final Logger LOGGER = LoggerFactory.getInstance(HandOffAutoDispatchAction.class);
	
	private List<IHandOffBatchPlan> batchPlanList;
	private List<Truck> trucks;
	private Map<String, Truck> truckMap;
	private List<Employee> employees;
	private Map<String, Employee> employeeMap;
	private List<Dispatch> dispatches;
	private Set<String> dispatchIds;
	private Map<String, Dispatch> dispatchMap;
	private TAPStatistics statistics;
	private Map<String, Collection<TruckPreferenceStat>> truckPreferenceStats;
	private Map<String, IHandOffDispatch> routeDispatchMapping;
		
	public HandOffAutoDispatchAction(IHandOffBatch batch
										,String userId
										,List<IHandOffBatchPlan> batchPlanList) {
		super(batch, userId);
		this.batchPlanList = batchPlanList;		
		trucks = new ArrayList<Truck>();
		truckMap = new LinkedHashMap<String, Truck>();
		employees = new ArrayList<Employee>();
		employeeMap = new HashMap<String, Employee>();
		dispatches = new ArrayList<Dispatch>();
		dispatchIds = new HashSet<String>();
		dispatchMap = new HashMap<String, Dispatch>();
		statistics = new TAPStatistics();
		truckPreferenceStats = new HashMap<String, Collection<TruckPreferenceStat>>();
		routeDispatchMapping = new HashMap<String, IHandOffDispatch>();		
	}
	
	public Object doExecute() throws Exception {
		
		boolean isSuccess = true;
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		
		Map<String, IHandOffDispatch> dispatchMapping = new HashMap<String, IHandOffDispatch>();
		
		LOGGER.info("######################### Auto-DispatchTask START #########################");
		
		proxy.addNewHandOffBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
												, EnumHandOffBatchActionType.AUTODISPATCH, this.getUserId());
		proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_AUTODISPATCHPROGRESS);
		StringBuffer actionResponse = new StringBuffer();
		
		if(this.getBatch() != null && this.getBatch().getBatchId() != null) {
			
			if(this.getBatch() != null) {
				
				if(this.batchPlanList.size() == 0){
					actionResponse.append("No Plans matching Dispatchs COMPLETED /" +this.batchPlanList.size()+" Plans");
					proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), actionResponse.toString());					
				}else{
								
					Map<String, IHandOffBatchRoute> routeMapping = new HashMap<String, IHandOffBatchRoute>();
					
					List<IHandOffBatchRoute> routes = proxy.getHandOffBatchDispatchRoutes(this.getBatch().getBatchId(), this.getBatch().getDeliveryDate());
									
					int noOfRoutes = routes != null ? routes.size() : 0;
					
					/*if(noOfRoutes == 0) {
						actionResponse.append("Routes matching Dispatchs COMPLETED /" +noOfRoutes+" Routes");
						proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), actionResponse.toString());						
					}else{*/
					
						try{
							if(noOfRoutes > 0){
								Iterator<IHandOffBatchRoute> itr = routes.iterator();
								while(itr.hasNext()) {
									IHandOffBatchRoute route = itr.next();								
									routeMapping.put(route.getRouteId(), route);
								}						
							}
							//clear if any dispatched exists
							proxy.clearHandOffBatchAutoDispatches(this.getBatch().getBatchId(), this.getBatch().getDeliveryDate());
							
							this.loadTrucks(proxy);
							this.loadDispatches(dispatchMapping, routeMapping);
							this.constructDispatchesToAlgorithm(dispatchMapping);
							this.assignByPreference(proxy, actionResponse);
							this.eval();
				            this.assignAutomatically();
				            this.eval();
				            this.dumpDispatches(dispatchMapping);
				            // add new dispatches
				            proxy.addNewHandOffBatchAutoDispatches(dispatchMapping.values());				            
				            
							Set<IHandOffDispatch> batchDispatches = proxy.getHandOffDispatch(this.getBatch().getBatchId(), this.getBatch().getDeliveryDate());
							for(IHandOffDispatch dispatch : batchDispatches){
								IHandOffDispatch d = dispatchMapping.get(dispatch.getPlanId());
								if(d != null){
									dispatch.setBatchDispatchResources(d.getBatchDispatchResources());
								}
							}
							// add dispatch resources
							proxy.addNewHandOffBatchAutoDispatchResources(batchDispatches);
							
							
								StringBuffer sapResponse = new StringBuffer();
								List<HandOffRouteTruckIn> routesToTrucksCommit = new ArrayList<HandOffRouteTruckIn>();
								List<IHandOffBatchRoute> rootRoutesIn = (List<IHandOffBatchRoute>)routes; 
								 
								for(IHandOffBatchRoute route : rootRoutesIn) {									
									IHandOffDispatch d = routeDispatchMapping.get(route.getRouteId());
									if(d != null)
										route.setTruckNumber(d.getTruck());
									routesToTrucksCommit.add(route);									
								}
								
																
								SapSendPhysicalTruckInfo sapHandOffEngine 
															= new SapSendPhysicalTruckInfo(routesToTrucksCommit
																							, RoutingServicesProperties.getDefaultPlantCode()
																							, this.getBatch().getDeliveryDate());
								try {
									sapHandOffEngine.execute();
								} catch (SapException e) {
									isSuccess = false;
									e.printStackTrace();//Handled in the below section
									sapResponse.append(e.getMessage()).append("\n");
								}
								
								BapiInfo[] bapiInfos = sapHandOffEngine.getBapiInfos();
								
								if(bapiInfos != null) {
									for (int i = 0; i < bapiInfos.length; i++) {
										
										BapiInfo bi = bapiInfos[i];
										sapResponse.append(bi.getMessage()).append("\n");
										if (BapiInfo.LEVEL_ERROR == bi.getLevel()) {
											isSuccess = false;
										}
										
										if(BapiInfo.LEVEL_INFO == bi.getLevel()) {
											if("ZWAVE/000".equals(bi.getCode()) && bi.getMessage().indexOf("PLEASE RETRY") >= 0){
												isSuccess = false;
											}
										}
									}
								}
								actionResponse.append(sapResponse);						
							
						}catch(ParseException pe){
							pe.printStackTrace();
							isSuccess = false;
							throw new RoutingServiceException("Invalid Routing Date"
									, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
						}catch(RoutingServiceException re){						
							re.printStackTrace();
							isSuccess = false;
							throw new RoutingServiceException("Batch completed, but Auto-Dispatch failed"
									, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
						}catch(Exception e){
							e.printStackTrace();
							isSuccess = false;
							LOGGER.error("######################### Auto-DispatchTask FAILED ######################### ", e);
							throw new RoutingServiceException(" Auto-DispatchTask FAILED ", e, IIssue.PROCESS_HANDOFFBATCH_ERROR);						
						}
						
					//}
				}
			}
		}
		
		if(!isSuccess){
			proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.AUTODISPATCHFAILED);
			proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), actionResponse.toString());
		}else{
			try{
				proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.AUTODISPATCHCOMPLETED);
				proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_AUTODISPATCHCOMPLETED);
			} catch (RoutingServiceException e) {				
				e.printStackTrace();
				throw new RoutingServiceException("Updating Batch status failed"
						, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
			}
		}
		LOGGER.info("######################### Auto-DispatchTask STOP #########################");
		
		return null;		
	}
	
	private void loadTrucks(HandOffServiceProxy proxy){
		Date deliveryDate = null;
		if(this.getBatch() != null){
			deliveryDate = this.getBatch().getDeliveryDate();
		}
		
		trucks = proxy.getAvailableTrucksInService("TRUCK", deliveryDate, "ACT");
		Iterator<Truck> truckItr = trucks.iterator();
		while(truckItr.hasNext()){
			Truck truck = truckItr.next();
			truckMap.put(truck.getId(), truck);			
		}
	}
	
	@SuppressWarnings({ "unchecked"})
	private void loadDispatches(Map<String, IHandOffDispatch> dispatchMapping, Map<String, IHandOffBatchRoute> routeMapping) throws ParseException{
		
		Map<String, List<IHandOffBatchPlan>> batchPlanMap = new HashMap<String, List<IHandOffBatchPlan>>();
		Map<String, List<IHandOffBatchRoute>> batchRouteMap = new HashMap<String, List<IHandOffBatchRoute>>();
				
		List<IHandOffBatchPlan> bullpens = new ArrayList<IHandOffBatchPlan>();
		
		Iterator<IHandOffBatchPlan> planItr = this.batchPlanList.iterator();
		while(planItr.hasNext()){
			IHandOffBatchPlan _plan = planItr.next();
			if(_plan.getZoneCode() == null){
				bullpens.add(_plan);
				continue;
			}
			if(!batchPlanMap.containsKey(_plan.getZoneCode())){
				batchPlanMap.put(_plan.getZoneCode(), new ArrayList<IHandOffBatchPlan>());
			}
			batchPlanMap.get(_plan.getZoneCode()).add(_plan);				
		}
		
		for(Map.Entry<String, IHandOffBatchRoute> routeEntry : routeMapping.entrySet()){
			IHandOffBatchRoute _route = routeEntry.getValue();
			
			if(!batchRouteMap.containsKey(_route.getArea())){
				batchRouteMap.put(_route.getArea(), new ArrayList<IHandOffBatchRoute>());
			}
			batchRouteMap.get(_route.getArea()).add(_route);	
			
		}		
		
		Set finalKeySet = batchPlanMap.keySet();
		Iterator<String> finalBatchPlanItr = finalKeySet.iterator();
		while(finalBatchPlanItr.hasNext()){
			String zone = finalBatchPlanItr.next();
			List<IHandOffBatchRoute> zoneRouteList = batchRouteMap.get(zone);
			if(zoneRouteList == null) 
				zoneRouteList = Collections.EMPTY_LIST;
			constructDispatchModelList(dispatchMapping, (List<IHandOffBatchPlan>)batchPlanMap.get(zone), zoneRouteList);
		}
		//No zone Plan List
		constructDispatchModelList(dispatchMapping, bullpens, Collections.EMPTY_LIST);
		
	}
	
	private static void constructDispatchModelList(Map<String, IHandOffDispatch> dispatchMapping
															, List<IHandOffBatchPlan> zonePlanList, List<IHandOffBatchRoute> zoneRouteList) throws ParseException{
		
		Iterator<IHandOffBatchPlan> planItr = zonePlanList.iterator();
		while(planItr.hasNext()){
			IHandOffBatchPlan _plan = planItr.next();
			
			IHandOffDispatch _dispatch = new HandOffDispatch();
			_dispatch.setDispatchId(_plan.getPlanId());
			_dispatch.setDispatchDate(_plan.getPlanDate());
			_dispatch.setPlanId(_plan.getPlanId());
			_dispatch.setStartTime(_plan.getStartTime());
			_dispatch.setFirstDeliveryTime(RoutingDateUtil.getServerTime(_plan.getFirstDeliveryTime()) != null ?
						RoutingDateUtil.getServerTime(RoutingDateUtil.getServerTime(_plan.getFirstDeliveryTime())): null);
			_dispatch.setSupervisorId(_plan.getSupervisorId());
			_dispatch.setIsBullpen(_plan.getIsBullpen());
			_dispatch.setMaxTime(_plan.getMaxTime());
			_dispatch.setRegion(_plan.getRegion());
			_dispatch.setZone(_plan.getZoneCode());
			_dispatch.setBatchDispatchResources(_plan.getBatchPlanResources());
			
			IHandOffBatchRoute route = matchRoute(_plan, zoneRouteList);
							
			if(route != null) {					
				_dispatch.setRoute(route.getRouteId());
				if(route.getFirstDeliveryTime() != null){
					_dispatch.setFirstDeliveryTime(RoutingDateUtil.getServerTime(route.getFirstDeliveryTime()) != null ?
						RoutingDateUtil.getServerTime(RoutingDateUtil.getServerTime(route.getFirstDeliveryTime())): null);
				}
				
				_dispatch.setCheckInTime(route.getCheckInTime());				
				_dispatch.setZone(route.getArea());
				route = null;				
			}
			
			if(!dispatchMapping.containsKey(_dispatch.getDispatchId()))
				dispatchMapping.put(_dispatch.getDispatchId(), _dispatch);
		}
	}
	
	private static IHandOffBatchRoute matchRoute(IHandOffBatchPlan p, List<IHandOffBatchRoute> routeList) throws ParseException {
		IHandOffBatchRoute result = null;
		if(routeList != null && p != null) {
			Iterator<IHandOffBatchRoute> _routeItr = routeList.iterator();
			while(_routeItr.hasNext()) {
				IHandOffBatchRoute route = _routeItr.next();
						
				if(route.getRouteDispatchTime()!= null && p.getStartTime()!= null){
					String firstDlvTimeFromRoute = RoutingDateUtil.getServerTime(route.getRouteDispatchTime()) != null ? RoutingDateUtil.getServerTime(route.getRouteDispatchTime()) : "";
					String firstDlvTime = RoutingDateUtil.getServerTime(p.getStartTime());					
					if(firstDlvTime != null && firstDlvTime.length() > 0 && firstDlvTime.equals(firstDlvTimeFromRoute)) {
						result = route;						
						_routeItr.remove();
						break;						
					}					
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private void constructDispatchesToAlgorithm(Map<String, IHandOffDispatch> dispatchMapping){
		
		for(Map.Entry<String, IHandOffDispatch> dispatchEntry : dispatchMapping.entrySet()){
			
			IHandOffDispatch _dispatch = dispatchEntry.getValue();
			
			if(_dispatch.getZone() != null && _dispatch.getRoute() != null){
				String id = _dispatch.getDispatchId();
				Date date = _dispatch.getDispatchDate();
				String zone = _dispatch.getZone();
				String area;
				if (zone != null && zone.length() > 0)
					area = zone.substring(0, 1);
				else
					area = "_";
				String route = _dispatch.getRoute();
				int start = toMinutes(_dispatch.getStartTime());
				int end = toMinutes(_dispatch.getCheckInTime());
				if (end < start)
					end += 24 * 60;
				String employeeId = null;
				if(_dispatch.getBatchDispatchResources() != null){
					Iterator<IHandOffBatchDispatchResource> itr = _dispatch.getBatchDispatchResources().iterator();
					while(itr.hasNext()){
						IHandOffBatchDispatchResource dispatchResource = itr.next();
						if("001".equalsIgnoreCase(dispatchResource.getEmployeeRoleType()) 
								|| "004".equalsIgnoreCase(dispatchResource.getEmployeeRoleType()))
							employeeId = dispatchResource.getResourceId();
					}
				}
				int plannedEnd = toMinutes(_dispatch.getMaxTime());
				if (plannedEnd == 0)
					plannedEnd = 24 * 60;
				else
					plannedEnd += start + 59;
				String baseId = id;
				int idSequence = 2;
				while (dispatchIds.contains(id)) {
					id = baseId + "-" + idSequence++;
				}
				Dispatch d = new Dispatch(id);
				d.setDate(date);
				d.setRoute(route);
				d.setZone(zone);
				d.setArea(area);
				d.setLeaves(start);
				d.setNextAvailable(end);
				d.setPlannedEnd(plannedEnd);
				dispatches.add(d);
				dispatchIds.add(id);
				if(!dispatchMap.containsKey(d.getId())){
					dispatchMap.put(id, d);
				}
				Employee employee;
				if (!employeeMap.containsKey(employeeId) && employeeId != null) {
					employee = new Employee(employeeId);
					employeeMap.put(employeeId, employee);
					employees.add(employee);
				} else {
					employee = employeeMap.get(employeeId);
				}
				d.setEmployee(employee);
			}
		}
		
		Collections.sort(dispatches);
	}
	
	private void assignByPreference(HandOffServiceProxy proxy, StringBuffer actionResponse) throws LpSolveException {
		
			// load employee preferences based on Pref tab
			List<TruckPreferenceStat> empTruckPrefs = proxy.getEmployeeTruckPreferences();
			if(empTruckPrefs !=null && empTruckPrefs.size() > 0){
				Iterator<TruckPreferenceStat> empTruckPrefItr = empTruckPrefs.iterator();
				while(empTruckPrefItr.hasNext()){
					TruckPreferenceStat stat = empTruckPrefItr.next();
					String employeeId = stat.getEmployeeId();
					if (!truckPreferenceStats.containsKey(employeeId))
						truckPreferenceStats.put(employeeId, new ArrayList<TruckPreferenceStat>());
					Collection<TruckPreferenceStat> stats = truckPreferenceStats.get(employeeId);
					stats.add(stat);
				}
			}
			// determine current preferences based on Pref tab
			LOGGER.info("Determining truck preferences based on Pref Tab...");
			for (Employee employee : employees) {
				Collection<TruckPreferenceStat> stats = truckPreferenceStats.get(employee.getId());
				List<Truck> prefs = new ArrayList<Truck>();
				HashMap<Truck, Integer> coll = new HashMap<Truck, Integer>();
				if (stats != null) {
					for (TruckPreferenceStat stat : stats) {
						Truck truck = truckMap.get(stat.getTruckId());
						if (truck != null) {
							if (!coll.containsKey(truck))
								coll.put(truck, 0);
							if(EnumTruckPreference.TRUCK_PREF_01.getName().equals(stat.getPrefKey()))
								coll.put(truck, coll.get(truck) + 5);
							else if(EnumTruckPreference.TRUCK_PREF_01.getName().equals(stat.getPrefKey()))
								coll.put(truck, coll.get(truck) + 4);
							else if(EnumTruckPreference.TRUCK_PREF_01.getName().equals(stat.getPrefKey()))
								coll.put(truck, coll.get(truck) + 3);
							else if(EnumTruckPreference.TRUCK_PREF_01.getName().equals(stat.getPrefKey()))
								coll.put(truck, coll.get(truck) + 2);
							else if(EnumTruckPreference.TRUCK_PREF_01.getName().equals(stat.getPrefKey()))
								coll.put(truck, coll.get(truck) + 1);						
						}
					}
					prefs = sortByValue(coll);
					Collections.reverse(prefs);
					if (prefs.size() > RoutingServicesProperties.getHandOffPrefListSize())
						prefs = prefs.subList(0, RoutingServicesProperties.getHandOffPrefListSize());
				}
				LinkedHashMap<Truck, Integer> sorted = new LinkedHashMap<Truck, Integer>();
				for (Truck truck : prefs)
					sorted.put(truck, coll.get(truck));
				employee.setPreferences(sorted);
			}
			
			// set up index <--> id transformation
			LOGGER.info("Preparing truck ID index...");
			Map<String, Integer> toIndex = new HashMap<String, Integer>();
			Map<Integer, String> toId = new HashMap<Integer, String>();
			int index = 1;
			for (Truck truck : trucks) {
				toIndex.put(truck.getId(), index);
				toId.put(index, truck.getId());
				index++;
			}		
	
			// use the simplex algorithm to find assignments based on preferences
			LOGGER.info("Setting up solver...");
			TruckAssignmentSolver solver = new TruckAssignmentSolver(RoutingServicesProperties.getHandOffPrefListSize()
																			, RoutingServicesProperties.getTruckAssignmentSolverTimeOut());
			for (Dispatch dispatch : dispatches) {
				int start = dispatch.getLeaves();
				int end = dispatch.getNextAvailable();
				LinkedHashMap<Truck, Integer> preferences = dispatch.getEmployee() != null ? dispatch.getEmployee().getPreferences(): new LinkedHashMap<Truck, Integer>();
				LinkedHashMap<Integer, Integer> indexed = new LinkedHashMap<Integer, Integer>();
				for (Entry<Truck, Integer> entry : preferences.entrySet())
					indexed.put(toIndex.get(entry.getKey().getId()), entry.getValue());
				solver.addRoute(new Route(dispatch, start, end, indexed, RoutingServicesProperties.getHandOffPrefListSize()));
			}
			LOGGER.info("Starting solver...");
			long start = System.currentTimeMillis();
			solver.solve();
			LOGGER.info("Solution completed (" + (System.currentTimeMillis() - start) + "ms).");
	
			LOGGER.info("Writing back solution into the model...");
			for (Route route : solver.getRoutes()) {
				if (route.getSolution() != Route.NOT_FOUND) {
					int truckIndex = route.getPreferredTruck(route.getSolution());
					String truckId = toId.get(truckIndex);
					if (truckId != null)
						route.getDispatch().setTruck(truckMap.get(truckId));
				}
			}
			LOGGER.info("Model has been updated.");
			solver = null;	
		
	}
	
	private void assignAutomatically() {
		LOGGER.info("Auto-assignment started...");
		long start = System.currentTimeMillis();

		Set<Dispatch> engaged = new HashSet<Dispatch>();
		Set<Dispatch> free = new HashSet<Dispatch>();
		Set<Truck> unused = new HashSet<Truck>(trucks);
		for (Dispatch dispatch : dispatches)
			if (dispatch.getTruck() != null) {
				engaged.add(dispatch);
				unused.remove(dispatch.getTruck());
			} else
				free.add(dispatch);

		List<DispatchTruckFrequency> tuples = new ArrayList<DispatchTruckFrequency>();
		while (!free.isEmpty()) {
			tuples.clear();
			for (Dispatch dispatch : free) {
				LinkedHashMap<Truck, Integer> preferences = dispatch.getEmployee() != null ? dispatch.getEmployee().getPreferences(): new LinkedHashMap<Truck, Integer>();
				TRUCK: for (Truck truck : preferences.keySet()) {
					for (Dispatch engDispatch : engaged)
						if (engDispatch.getTruck().equals(truck) && engDispatch.collide(dispatch))
							continue TRUCK;

					tuples.add(new DispatchTruckFrequency(dispatch, truck, preferences.get(truck)));
					break TRUCK;
				}
			}
			// reverse sort
			Collections.sort(tuples, new Comparator<DispatchTruckFrequency>() {
				@Override
				public int compare(DispatchTruckFrequency o1, DispatchTruckFrequency o2) {
					// !!! reverse sort !!!
					int i = o2.frequency - o1.frequency;
					if (i != 0)
						return i;
					i = o2.truck.compareTo(o1.truck);
					if (i != 0)
						return i;
					if(o2.dispatch.getEmployee()!=null && o1.dispatch.getEmployee()!=null)
						return i;
					return o2.dispatch.getEmployee().getId().compareTo(o1.dispatch.getEmployee().getId());
				}
			});
			Dispatch dispatch;
			Truck truck;
			SELECT: {
				if (!tuples.isEmpty()) {
					DispatchTruckFrequency dtf = tuples.get(0);
					dispatch = dtf.dispatch;
					truck = dtf.truck;
				} else {
					List<Dispatch> freeDispatches = new ArrayList<Dispatch>(free);
					Collections.sort(freeDispatches, new Comparator<Dispatch>() {
						@Override
						public int compare(Dispatch o1, Dispatch o2) {
							if(o1.getEmployee() != null && o2.getEmployee() != null)
								return o1.getEmployee().getId().compareTo(o2.getEmployee().getId());
							return 0;
						}
					});
					// find the first not colliding
					for (Dispatch d : freeDispatches)
						COLLISION: for (Dispatch e : engaged)
							if (!d.collide(e)) {
								for (Dispatch f : engaged)
									if (!f.getId().equals(e.getId()) && e.getTruck().equals(f.getTruck()) &&
											d.collide(f))
										continue COLLISION;
								dispatch = d;
								truck = e.getTruck();
								break SELECT;
							}

					dispatch = freeDispatches.get(0);
					if (!unused.isEmpty()) {
						truck = unused.iterator().next(); // practically random
						unused.remove(truck);
					} else {
						truck = Truck.newVirtualTruck();
						trucks.add(truck);
						truckMap.put(truck.getId(), truck);
					}
				}
			}
			dispatch.setTruck(truck);
			LOGGER.info("Free Dispatch >>> Leaves>> "+dispatch.getLeaves()+" nextAvailable>> "+dispatch.getNextAvailable()+" Route>>> "+dispatch.getRoute()+" Truck>>> "+dispatch.getTruck().getId());
			engaged.add(dispatch);
			free.remove(dispatch);
		}

		LOGGER.info("Auto-assignment took " + (System.currentTimeMillis() - start) + "ms");
	}
	
	public static <K extends Comparable<K>, V extends Comparable<V>> List<K> sortByValue(final Map<K, V> m) {
		List<K> keys = new ArrayList<K>();
		keys.addAll(m.keySet());
		Collections.sort(keys, new Comparator<K>() {
			public int compare(K o1, K o2) {
				V v1 = m.get(o1);
				V v2 = m.get(o2);
				if (v1 == null) {
					return (v2 == null) ? 0 : 1;
				} else if (v2 == null) {
					return -1;
				} else {
					int i = v1.compareTo(v2);
					if (i == 0)
						return o1.compareTo(o2);
					else
						return i;
				}
			}
		});
		return keys;
	}
	
	private static int toMinutes(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int minutes = 0;
		minutes += cal.get(Calendar.MINUTE);
		minutes += 60 * cal.get(Calendar.HOUR_OF_DAY);
		return minutes;
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
		return EnumHandOffBatchStatus.AUTODISPATCHFAILED;
	}
	
	private void eval() {
		statistics = new TAPStatistics();
		for (Dispatch dispatch : dispatches) {
			statistics.collect(dispatch);
		}
		statistics.dump();
	}
	
	private static class DispatchTruckFrequency {
		Dispatch dispatch;
		Truck truck;
		int frequency;

		public DispatchTruckFrequency(Dispatch dispatch, Truck truck, int frequency) {
			super();
			this.dispatch = dispatch;
			this.truck = truck;
			this.frequency = frequency;
		}
	}
	
	private void dumpDispatches(Map<String, IHandOffDispatch> dispatchMapping) {
		
		if(dispatchMap !=null){				
			for (Map.Entry<String, IHandOffDispatch> dispatchEntry : dispatchMapping.entrySet()) {				
				IHandOffDispatch dispatch = dispatchEntry.getValue();
				Dispatch d = dispatchMap.get(dispatch.getDispatchId());
				if(d != null)
					dispatch.setTruck(d.getTruck() != null ? d.getTruck().getId(): null);				
				if(dispatch.getRoute()!= null){
					routeDispatchMapping.put(dispatch.getRoute(), dispatch);
				}
					
			}
		}
	}
	
}
