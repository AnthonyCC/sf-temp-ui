package com.freshdirect.routing.handoff.action;

import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_TRUCKASSIGNMENTCOMPLETED;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_TRUCKASSIGNMENTPROGRESS;

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

import lpsolve.LpSolveException;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumTruckPreference;
import com.freshdirect.routing.model.HandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchTrailer;
import com.freshdirect.routing.model.IHandOffDispatch;
import com.freshdirect.routing.model.IHandOffDispatchResource;
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

public class HandOffTruckAssignmentAction extends AbstractHandOffAction {

	private static final Logger LOGGER = LoggerFactory.getInstance(HandOffAutoDispatchAction.class);
	
	private Map<String, IHandOffDispatch> batchDispatchList;
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
		
	public HandOffTruckAssignmentAction(IHandOffBatch batch
										, String userId
										, Map<String, IHandOffDispatch> batchDispatchList) {
		super(batch, userId);
		this.batchDispatchList = batchDispatchList;		
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
				
		LOGGER.info("######################### Truck-AssignmentTask START #########################");
		
		proxy.addNewHandOffBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
												, EnumHandOffBatchActionType.TRUCKASSIGNMENT, this.getUserId());
		proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_TRUCKASSIGNMENTPROGRESS);
		StringBuffer actionResponse = new StringBuffer();
		
		if(this.getBatch() != null && this.getBatch().getBatchId() != null) {
			
			if(this.getBatch() != null) {
				
				if(this.batchDispatchList.size() == 0){
					actionResponse.append("No Dispatch(s) to assign trucks /" +this.batchDispatchList.size()+" dispatchs");
					proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), actionResponse.toString());					
				} else {
								
					Map<String, IHandOffBatchRoute> routeMapping = new HashMap<String, IHandOffBatchRoute>();
					Map<String, IHandOffBatchTrailer> trailerMapping = new HashMap<String, IHandOffBatchTrailer>();
					
					List<IHandOffBatchRoute> routes = proxy.getHandOffBatchDispatchRoutes(this.getBatch().getBatchId()
																							, this.getBatch().getDeliveryDate()
																							, this.getBatch().getCutOffDateTime());
					List<IHandOffBatchTrailer> trailers = proxy.getHandOffBatchTrailers(this.getBatch().getBatchId());
									
						try {
							if(routes != null && routes.size() > 0){
								Iterator<IHandOffBatchRoute> itr = routes.iterator();
								while(itr.hasNext()) {
									IHandOffBatchRoute route = itr.next();								
									routeMapping.put(route.getRouteId(), route);
								}						
							}

							if(trailers != null && trailers.size() > 0){
								Iterator<IHandOffBatchTrailer> itr = trailers.iterator();
								while(itr.hasNext()) {
									IHandOffBatchTrailer trailer = itr.next();
									trailerMapping.put(trailer.getTrailerId(), trailer);
								}
							}
							//load all active trucks & trailers from Transp Asset tab
							this.loadTrucks(proxy);							
							this.constructDispatchesToAlgorithm(this.batchDispatchList, routeMapping, trailerMapping);
							//Now assign trucks to route(s) based on route driver employee truck preference(s)
							this.assignByPreference(proxy, actionResponse);
							this.eval();
							//Now assign trucks to routes randomly for routes with no preference  
					        this.assignAutomatically();
					        this.eval();
					        this.dumpDispatches(this.batchDispatchList);
					        
					        List<IHandOffDispatch> dispatchs = new ArrayList<IHandOffDispatch>(); 
					        dispatchs.addAll(this.batchDispatchList.values());
					        //update the planned truck to dispatch entry(s)
					        proxy.updateHandOffDispatchTruckInfo(dispatchs);
							
							StringBuffer sapResponse = new StringBuffer();
							List<HandOffRouteTruckIn> routesToTrucksCommit = new ArrayList<HandOffRouteTruckIn>();
							List<IHandOffBatchRoute> rootRoutesIn = (List<IHandOffBatchRoute>) routes; 
							
										
							for(IHandOffBatchRoute route : rootRoutesIn) {									
								IHandOffDispatch d = routeDispatchMapping.get(route.getRouteId());
								if(d != null)
									route.setTruckNumber(d.getTruck());
									routesToTrucksCommit.add(route);									
							}
									
							for(Map.Entry<String, IHandOffBatchTrailer> trailerEntry : trailerMapping.entrySet()) {									
								IHandOffDispatch d = routeDispatchMapping.get(trailerEntry.getValue().getTrailerId());
								if(d != null){
									IHandOffBatchRoute _route = new HandOffBatchRoute();
									_route.setRouteId(trailerEntry.getValue().getTrailerId());
									_route.setTruckNumber(d.getTruck());
									routesToTrucksCommit.add(_route);
								}
							}
							
							if(routesToTrucksCommit.size() > 0) {
																
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
							}
						} catch (Exception e){
							e.printStackTrace();
							isSuccess = false;
							actionResponse.append(e.getMessage());
							LOGGER.error("######################### Truck-AssignmentTask FAILED ######################### ", e);
						}
				}
			}
		}
		
		if(!isSuccess) {			
			proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), actionResponse.toString());
		} else {
			try {				
				proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_TRUCKASSIGNMENTCOMPLETED);
			} catch (RoutingServiceException e) {				
				e.printStackTrace();
				throw new RoutingServiceException("Updating Batch message failed"
						, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
			}
		}
		LOGGER.info("######################### Truck-AssignmentTask STOP #########################");
		
		return null;		
	}
	
	private void loadTrucks(HandOffServiceProxy proxy){
		Date deliveryDate = null;
		if(this.getBatch() != null){
			deliveryDate = this.getBatch().getDeliveryDate();
		}
		
		trucks = proxy.getAvailableTrucksInService(deliveryDate);
		Iterator<Truck> truckItr = trucks.iterator();
		while(truckItr.hasNext()){
			Truck truck = truckItr.next();
			truckMap.put(truck.getId(), truck);			
		}
	}
	
	@SuppressWarnings("unchecked")
	private void constructDispatchesToAlgorithm(Map<String, IHandOffDispatch> dispatchMapping, Map<String, IHandOffBatchRoute> routeMapping, Map<String, IHandOffBatchTrailer> trailerMapping){
		LOGGER.error("######################### Construct Dispatches To Algorithm ######################### ");
		
		for(Map.Entry<String, IHandOffDispatch> dispatchEntry : dispatchMapping.entrySet()){
			
			IHandOffDispatch _dispatch = dispatchEntry.getValue();
			
			if(_dispatch.getRoute() != null){
				String id = _dispatch.getDispatchId();
				Date date = _dispatch.getDispatchDate();
				String zone = _dispatch.getZone();
				String area;
				if (zone != null && zone.length() > 0)
					area = zone.substring(0, 1);
				else
					area = "_";
				String route = _dispatch.getRoute();
				int start = toMinutes(_dispatch.getDispatchTime());
				
				if(_dispatch.getRoute() != null && !_dispatch.isTrailer()) {
					IHandOffBatchRoute routeEx = routeMapping.get(route);
					_dispatch.setCheckInTime(routeEx.getCheckInTime());
				}
				
				if(_dispatch.getRoute() != null && _dispatch.isTrailer()) {
					IHandOffBatchTrailer trailer = trailerMapping.get(route);
					_dispatch.setCheckInTime(trailer.getCompletionTime());
				}
				
				int end = toMinutes(_dispatch.getCheckInTime());
				if (end < start)
					end += 24 * 60;
				String employeeId = null;
				if(_dispatch.getDispatchResources() != null){
					Iterator<IHandOffDispatchResource> itr = _dispatch.getDispatchResources().iterator();
					while(itr.hasNext()){
						IHandOffDispatchResource dispatchResource = itr.next();
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
				d.setTrailer(_dispatch.isTrailer());
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
							else if(EnumTruckPreference.TRUCK_PREF_02.getName().equals(stat.getPrefKey()))
								coll.put(truck, coll.get(truck) + 4);
							else if(EnumTruckPreference.TRUCK_PREF_03.getName().equals(stat.getPrefKey()))
								coll.put(truck, coll.get(truck) + 3);
							else if(EnumTruckPreference.TRUCK_PREF_04.getName().equals(stat.getPrefKey()))
								coll.put(truck, coll.get(truck) + 2);
							else if(EnumTruckPreference.TRUCK_PREF_05.getName().equals(stat.getPrefKey()))
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
		long autoAssignStart = System.currentTimeMillis();
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
			Truck truck = null;
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
							if (!d.collide(e) && !d.isTrailer() && !e.getTruck().isTrailer()) {
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
						Iterator itr = unused.iterator();
						while(itr.hasNext()){
							Truck tempTruck = (Truck)itr.next();
							if(dispatch.isTrailer() && tempTruck.isTrailer()) {
								truck = tempTruck;  // random
								unused.remove(truck);
								break;
							} else if(!dispatch.isTrailer() && !tempTruck.isTrailer()) {
								truck = tempTruck;  // random
								unused.remove(truck);
								break;
							}
						}
					}						
					if(truck == null) {
						truck = Truck.newVirtualTruck();
						if(dispatch.isTrailer()) 
							truck.setTrailer(true);
						else
							truck.setTrailer(false);
						trucks.add(truck);
						truckMap.put(truck.getId(), truck);
					}
				}
			}
			if(dispatch != null && truck != null) {
				if(dispatch.isTrailer() && truck.isTrailer()){
					dispatch.setTruck(truck);
					LOGGER.info("Free Trailer Dispatch >>> Leaves>> "+dispatch.getLeaves()+" nextAvailable>> "+dispatch.getNextAvailable()+" Trailer Route>>> "+dispatch.getRoute()+" Trailer Truck>>> "+dispatch.getTruck().getId());
					engaged.add(dispatch);
					free.remove(dispatch);
				}
				if(!dispatch.isTrailer() && !truck.isTrailer()){
					dispatch.setTruck(truck);
					LOGGER.info("Free Dispatch >>> Leaves>> "+dispatch.getLeaves()+" nextAvailable>> "+dispatch.getNextAvailable()+" Route>>> "+dispatch.getRoute()+" Truck>>> "+dispatch.getTruck().getId());
					engaged.add(dispatch);
					free.remove(dispatch);
				}	
			}
			long autoAssignEnd = System.currentTimeMillis();
			if((autoAssignEnd - autoAssignStart)/1000 > 60) {
				throw new RoutingServiceException("Auto-assignment taking longer than usual. Please check enough truck/trialer assets available In-service", null, IIssue.PROCESS_AUTODISPATCH_ERROR);
			}
		}

		LOGGER.info("Auto-assignment took " + (System.currentTimeMillis() - start) + "ms");
	}
	
	private void assignAutomatically1() {
		
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
		long autoAssignStart = System.currentTimeMillis();
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
			Truck truck = null;
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
							if (!d.collide(e) && !d.isTrailer() && !e.getTruck().isTrailer()) {
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
						Iterator itr = unused.iterator();
						while(itr.hasNext()){
							Truck tempTruck = (Truck)itr.next();
							if(dispatch.isTrailer() && tempTruck.isTrailer()) {
								truck = tempTruck;  // practically random
								unused.remove(truck);
								break;
							} else if(!dispatch.isTrailer() && !tempTruck.isTrailer()) {
								truck = tempTruck;  // practically random
								unused.remove(truck);
								break;
							}
						}
					}						
					if(truck == null) {
						truck = Truck.newVirtualTruck();
						if(dispatch.isTrailer()) 
							truck.setTrailer(true);
						else
							truck.setTrailer(false);
						trucks.add(truck);
						truckMap.put(truck.getId(), truck);
					}
				}
			}
			if(dispatch != null && truck != null) {
				if(dispatch.isTrailer() && truck.isTrailer()){
					dispatch.setTruck(truck);
					LOGGER.info("Free Trailer Dispatch >>> Leaves>> "+dispatch.getLeaves()+" nextAvailable>> "+dispatch.getNextAvailable()+" Trailer Route>>> "+dispatch.getRoute()+" Trailer Truck>>> "+dispatch.getTruck().getId());
					engaged.add(dispatch);
					free.remove(dispatch);
				}
				if(!dispatch.isTrailer() && !truck.isTrailer()){
					dispatch.setTruck(truck);
					LOGGER.info("Free Dispatch >>> Leaves>> "+dispatch.getLeaves()+" nextAvailable>> "+dispatch.getNextAvailable()+" Route>>> "+dispatch.getRoute()+" Truck>>> "+dispatch.getTruck().getId());
					engaged.add(dispatch);
					free.remove(dispatch);
				}	
			}
			long autoAssignEnd = System.currentTimeMillis();
			if((autoAssignEnd - autoAssignStart)/1000 > 60) {
				throw new RoutingServiceException("Auto-assignment taking longer than usual. Please check enough truck/trialer assets available In-service", null, IIssue.PROCESS_AUTODISPATCH_ERROR);
			}
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
		return EnumHandOffBatchStatus.AUTODISPATCHCOMPLETED;
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
