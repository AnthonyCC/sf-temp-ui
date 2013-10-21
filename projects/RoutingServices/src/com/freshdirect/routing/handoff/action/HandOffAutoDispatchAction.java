package com.freshdirect.routing.handoff.action;

import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_AUTODISPATCHCOMPLETED;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_AUTODISPATCHPROGRESS;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.routing.model.HandOffDispatch;
import com.freshdirect.routing.model.HandOffDispatchResource;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchPlan;
import com.freshdirect.routing.model.IHandOffBatchPlanResource;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchTrailer;
import com.freshdirect.routing.model.IHandOffDispatch;
import com.freshdirect.routing.model.IHandOffDispatchResource;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.TrnFacility;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;

public class HandOffAutoDispatchAction extends AbstractHandOffAction {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(HandOffAutoDispatchAction.class);
	
	private List<IHandOffBatchPlan> batchPlanList;
	
	public HandOffAutoDispatchAction(IHandOffBatch batch
										,String userId
										,List<IHandOffBatchPlan> batchPlanList) {
		super(batch, userId);
		this.batchPlanList = batchPlanList;
	}
	
	public Object doExecute() throws Exception {
		
		boolean isSuccess = true;
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		
		Map<String, IHandOffDispatch> dispatchMapping = new HashMap<String, IHandOffDispatch>();
		
		LOGGER.info("######################### Auto-DispatchTask START #########################");
		
		proxy.addNewHandOffBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
												, EnumHandOffBatchActionType.AUTODISPATCH, this.getUserId());
		proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_AUTODISPATCHPROGRESS);
				
		if(this.getBatch() != null && this.getBatch().getBatchId() != null) {
			
			if(this.getBatch() != null) {
				
				if(this.batchPlanList.size() == 0){					
					throw new RoutingServiceException("No plans entry(s) to dispatch /" +this.batchPlanList.size()+" Plans"
							, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);									
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
							//clear if any dispatches exists
							proxy.clearHandOffBatchAutoDispatches(this.getBatch().getBatchId()
																		, this.getBatch().getDeliveryDate()
																		, this.getBatch().getCutOffDateTime());

							this.loadDispatches(dispatchMapping, routeMapping, trailers);
							  
				            // add new dispatches
				            proxy.addNewHandOffBatchAutoDispatches(dispatchMapping.values());
				            
				            proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.AUTODISPATCHCOMPLETED);
							proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_AUTODISPATCHCOMPLETED);
				            
						}  catch (Exception e){
							e.printStackTrace();
							LOGGER.error("######################### Auto-DispatchTask FAILED ######################### ", e);
							throw new RoutingServiceException(e, IIssue.PROCESS_HANDOFFBATCH_ERROR);						
						}
				}
			}
		}
		LOGGER.info("######################### Auto-DispatchTask STOP #########################");
		
		return null;		
	}
	
	@SuppressWarnings({ "unchecked"})
	private void loadDispatches(Map<String, IHandOffDispatch> dispatchMapping, 
			Map<String, IHandOffBatchRoute> routeMapping, List<IHandOffBatchTrailer> trailers) throws ParseException{
		
		Map<String, List<IHandOffBatchPlan>> batchPlanMap = new HashMap<String, List<IHandOffBatchPlan>>();
		Map<String, List<IHandOffBatchRoute>> batchRouteMap = new HashMap<String, List<IHandOffBatchRoute>>();
		Map<String, List<IHandOffBatchPlan>> batchTrailerPlanMap = new HashMap<String, List<IHandOffBatchPlan>>();
		Map<String, List<IHandOffBatchTrailer>> batchTrailerMap = new HashMap<String, List<IHandOffBatchTrailer>>();
		
		List<IHandOffBatchPlan> bullpens = new ArrayList<IHandOffBatchPlan>();	
		RoutingInfoServiceProxy routeInfoProxy = new RoutingInfoServiceProxy();
		Map<String, TrnFacility> facilityLookUp = routeInfoProxy.retrieveTrnFacilityLocations();
		
		String zoneFacility = null;
		for(Entry<String, TrnFacility> facility : facilityLookUp.entrySet())
		{
			if(facility.getValue()!=null && EnumTransportationFacilitySrc.DELIVERYZONE.getName().equals(facility.getValue().getTrnFacilityType().getName()))
				zoneFacility = facility.getValue().getFacilityId();
		}
		
		Iterator<IHandOffBatchPlan> planItr = this.batchPlanList.iterator();
		while(planItr.hasNext()){
			IHandOffBatchPlan _plan = planItr.next();
			if(_plan.getZoneCode() == null && "Y".equalsIgnoreCase(_plan.getIsBullpen())){
				bullpens.add(_plan);
				continue;
			}
			if(_plan.getZoneCode() == null && _plan.getDestinationFacility() != null 
					&& EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(facilityLookUp.get(_plan.getDestinationFacility()).getTrnFacilityType().getName())){
				if(!batchTrailerPlanMap.containsKey(facilityLookUp.get(_plan.getDestinationFacility()).getRoutingCode())){
					batchTrailerPlanMap.put(facilityLookUp.get(_plan.getDestinationFacility()).getRoutingCode(), new ArrayList<IHandOffBatchPlan>());
				}
				batchTrailerPlanMap.get(facilityLookUp.get(_plan.getDestinationFacility()).getRoutingCode()).add(_plan);
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
			if(zoneRouteList == null) {
				zoneRouteList = Collections.EMPTY_LIST;
			}
			Collections.sort(zoneRouteList, new Comparator<IHandOffBatchRoute>(){
				
				public int compare(IHandOffBatchRoute obj1, IHandOffBatchRoute obj2) {
					int routeId1 = getRouteIndex1(obj1.getRouteId());
					int routeId2 = getRouteIndex1(obj2.getRouteId());			
					return routeId1 - routeId2;
				}
			});
			constructDispatchModelList(dispatchMapping, (List<IHandOffBatchPlan>)batchPlanMap.get(zone), zoneRouteList, Collections.EMPTY_LIST, false, facilityLookUp, zoneFacility);
		}
		//Bull-pen Plan List
		constructDispatchModelList(dispatchMapping, bullpens, Collections.EMPTY_LIST, Collections.EMPTY_LIST, false, facilityLookUp,zoneFacility);
		
		//Trailer Plan List
		Iterator<IHandOffBatchTrailer> trailerItr = trailers.iterator();
		while(trailerItr.hasNext()){
			IHandOffBatchTrailer _trailer = trailerItr.next();
			if(_trailer.getTrailerId() != null){
				if(!batchTrailerMap.containsKey(_trailer.getTrailerId().substring(1, 4))){
					batchTrailerMap.put(_trailer.getTrailerId().substring(1, 4), new ArrayList<IHandOffBatchTrailer>());
				}
				batchTrailerMap.get(_trailer.getTrailerId().substring(1, 4)).add(_trailer);
			}
		}	
		
		Set finalTrailerKeySet = batchTrailerPlanMap.keySet();
		Iterator<String> finalBatchTrailerPlanItr = finalTrailerKeySet.iterator();
		while(finalBatchTrailerPlanItr.hasNext()){
			String locRoutingCode = finalBatchTrailerPlanItr.next();
			List<IHandOffBatchTrailer> locTrailerRouteList = batchTrailerMap.get(locRoutingCode);
			if(locTrailerRouteList == null) 
				locTrailerRouteList = Collections.EMPTY_LIST;
			constructDispatchModelList(dispatchMapping, (List<IHandOffBatchPlan>)batchTrailerPlanMap.get(locRoutingCode), Collections.EMPTY_LIST, 
					locTrailerRouteList, true, facilityLookUp, zoneFacility);
		}	
	}
	
	@SuppressWarnings("unchecked")
	public static List<IHandOffBatchPlan> getRunnerPlans(List<IHandOffBatchPlan> zoneRouteList, Map<String, TrnFacility> facilityLookUp){
		List<IHandOffBatchPlan> runnerPlans = new ArrayList<IHandOffBatchPlan>();
		if(zoneRouteList!=null && zoneRouteList.size() > 0) {
			for (Iterator<IHandOffBatchPlan> k = zoneRouteList.iterator(); k.hasNext();) {
				IHandOffBatchPlan p = k.next();
				if(!"Y".equalsIgnoreCase(p.getIsBullpen()) && p.getOriginFacility()!=null &&
						facilityLookUp.get(p.getOriginFacility())!=null &&
								facilityLookUp.get(p.getOriginFacility()).getTrnFacilityType().getName().equals(EnumTransportationFacilitySrc.DEPOTDELIVERY.getName())){
					runnerPlans.add(p);
					k.remove();
				}
			}
		}
		return runnerPlans;
	}
	
	@SuppressWarnings("unchecked")
	private static void constructDispatchModelList(Map<String, IHandOffDispatch> dispatchMapping
															, List<IHandOffBatchPlan> zonePlanList
															, List<IHandOffBatchRoute> zoneRouteList
															, List<IHandOffBatchTrailer> batchTrailers
															, boolean isTrailer, Map<String, TrnFacility> facilityLookUp, String zoneFacility) throws ParseException{
		GeographyServiceProxy geoProxy = new GeographyServiceProxy();
		Map<String, IZoneModel> zoneLookup = geoProxy.getZoneLookup();
		
		List<IHandOffBatchPlan> runnerPlans = getRunnerPlans(zonePlanList, facilityLookUp);
		Set<IHandOffBatchPlan> assignedRunners = new HashSet<IHandOffBatchPlan>();

		Iterator<IHandOffBatchPlan> planItr = zonePlanList.iterator();
		while(planItr.hasNext()){
			IHandOffBatchPlan _plan = planItr.next();
			
			IHandOffDispatch _dispatch = new HandOffDispatch();
			_dispatch.setDispatchId(_plan.getPlanId());
			_dispatch.setDispatchDate(_plan.getPlanDate());
			_dispatch.setPlanId(_plan.getPlanId());
			_dispatch.setOriginFacility(_plan.getOriginFacility());
			_dispatch.setDestinationFacility(_plan.getDestinationFacility());
			_dispatch.setDispatchGroup(_plan.getDispatchGroup());
			_dispatch.setDispatchTime(_plan.getDispatchTime());
			_dispatch.setSupervisorId(_plan.getSupervisorId());
			_dispatch.setIsBullpen(_plan.getIsBullpen() == null ? "N" : _plan.getIsBullpen());
			_dispatch.setMaxTime(_plan.getMaxTime());
			_dispatch.setRegion(_plan.getRegion());
			_dispatch.setZone(_plan.getZoneCode());
			_dispatch.setCutoffTime(_plan.getCutOffTime());
			_dispatch.setDispatchResources(convertPlnToDispatchResource(_plan.getBatchPlanResources()));
			_dispatch.setDispatchType("RGD");
			
			if(isTrailer){
				_dispatch.setTrailer(true);
				IHandOffBatchTrailer trailer = matchTrailer(_plan, batchTrailers);
				if(trailer != null) {
					_dispatch.setRoute(trailer.getTrailerId());
					_dispatch.setCheckInTime(trailer.getCompletionTime());		
					trailer = null;
				}
			} 
			else 
			{
				IHandOffBatchRoute route = null;
				if(_plan.getZoneCode()!=null && zoneLookup.get(_plan.getZoneCode())!=null && zoneLookup.get(_plan.getZoneCode()).getArea()!=null
						&& zoneLookup.get(_plan.getZoneCode()).getArea().isDepot() && _plan.getDestinationFacility()!=null &&
								facilityLookUp.get(_plan.getDestinationFacility())!=null &&
								facilityLookUp.get(_plan.getDestinationFacility()).getTrnFacilityType().getName().equals(EnumTransportationFacilitySrc.DEPOTDELIVERY.getName()))
				{
					DateRange planRange = new DateRange(_plan.getDispatchGroup(), _plan.getDispatchGroup());
					
					int runnerCount = 0;	
						for (Iterator<IHandOffBatchPlan> k = runnerPlans.iterator(); k.hasNext() && runnerCount< _plan.getRunnerMax();) 
						{
							IHandOffBatchPlan runnerPlan = k.next();
							DateRange runnerRange = new DateRange(runnerPlan.getDispatchGroup(), runnerPlan.getEndTime());
							
							if(runnerPlan.getOriginFacility().equals(_dispatch.getDestinationFacility()) && 
									runnerRange.overlaps(planRange) && 
									runnerPlan.getBatchPlanResources()!=null && runnerPlan.getBatchPlanResources().size()>0)
							{
								if(_dispatch.getDispatchResources()==null)
								{
									_dispatch.setDispatchResources(convertPlnToDispatchResource(runnerPlan.getBatchPlanResources(), _dispatch));
								}
								else
								{
									_dispatch.getDispatchResources().addAll(convertPlnToDispatchResource(runnerPlan.getBatchPlanResources(), _dispatch));
								}
								assignedRunners.add(runnerPlan);
								runnerCount++;
							}
						}
					if (runnerCount > 0)
						_dispatch.setDestinationFacility(zoneFacility);
				}
				route = matchRoute(_plan, zoneRouteList);	
				
				if(route != null) {
					_dispatch.setRoute(route.getRouteId());
					/*if(route.getFirstDeliveryTime() != null){
						_dispatch.setFirstDlvTime(RoutingDateUtil.getServerTime(route.getFirstDeliveryTime()) != null ?
							RoutingDateUtil.getServerTime(RoutingDateUtil.getServerTime(route.getFirstDeliveryTime())): null);
					}*/
					_dispatch.setCheckInTime(route.getCheckInTime());				
					_dispatch.setZone(route.getArea());
					route = null;
				}
				
			}
			if(!dispatchMapping.containsKey(_dispatch.getDispatchId()))
				dispatchMapping.put(_dispatch.getDispatchId(), _dispatch);
		}
		Set unassignedRunners = new HashSet();
		for(Iterator<IHandOffBatchPlan> runnerIter = runnerPlans.iterator(); runnerIter.hasNext();)
		{
			IHandOffBatchPlan runnerPlan = runnerIter.next();
			if(!assignedRunners.contains(runnerPlan))
				unassignedRunners.add(runnerPlan);
		}
		planItr = unassignedRunners.iterator();
		while (planItr.hasNext()) {
			IHandOffBatchPlan _plan = planItr.next();

			IHandOffDispatch _dispatch = new HandOffDispatch();
			_dispatch.setDispatchId(_plan.getPlanId());
			_dispatch.setDispatchDate(_plan.getPlanDate());
			_dispatch.setPlanId(_plan.getPlanId());
			_dispatch.setOriginFacility(_plan.getOriginFacility());
			_dispatch.setDestinationFacility(_plan.getDestinationFacility());
			_dispatch.setDispatchGroup(_plan.getDispatchGroup());
			_dispatch.setDispatchTime(_plan.getDispatchTime());
			_dispatch.setEndTime(_plan.getEndTime());
			_dispatch.setSupervisorId(_plan.getSupervisorId());
			_dispatch.setIsBullpen(_plan.getIsBullpen() == null ? "N" : _plan.getIsBullpen());
			_dispatch.setMaxTime(_plan.getMaxTime());
			_dispatch.setRegion(_plan.getRegion());
			_dispatch.setZone(_plan.getZoneCode());
			_dispatch.setCutoffTime(_plan.getCutOffTime());
			_dispatch.setDispatchResources(convertPlnToDispatchResource(_plan.getBatchPlanResources()));
			_dispatch.setDispatchType("RGD");
			
			if(!dispatchMapping.containsKey(_dispatch.getDispatchId()))
				dispatchMapping.put(_dispatch.getDispatchId(), _dispatch);
		}
		
	}
	
	
	public static Set convertPlnToDispatchResource(Set planResourceList,IHandOffDispatch dispatch){
		Set dispatchResourceList = new HashSet();
		if (planResourceList != null && planResourceList.size() > 0) {

			Iterator iterator = planResourceList.iterator();
			IHandOffDispatchResource _dispatchResource = null;
			while(iterator.hasNext()){
				IHandOffBatchPlanResource resource=(IHandOffBatchPlanResource)iterator.next();
				resource.setPlanId(dispatch.getPlanId());
				
				_dispatchResource = new HandOffDispatchResource();
				_dispatchResource.setDispatchId(resource.getPlanId());
				_dispatchResource.setResourceId(resource.getResourceId());
				_dispatchResource.setEmployeeRoleType(resource.getEmployeeRoleType());
				_dispatchResource.setAdjustmentTime(resource.getAdjustmentTime());
				
				dispatchResourceList.add(_dispatchResource);
			}

		}
		return dispatchResourceList;
	}
	
	public static Set convertPlnToDispatchResource(Set planResourceList){
		Set dispatchResourceList = new HashSet();
		if (planResourceList != null && planResourceList.size() > 0) {

			Iterator iterator = planResourceList.iterator();
			IHandOffDispatchResource _dispatchResource = null;
			while(iterator.hasNext()){
				IHandOffBatchPlanResource resource = (IHandOffBatchPlanResource)iterator.next();
								
				_dispatchResource = new HandOffDispatchResource();
				_dispatchResource.setDispatchId(resource.getPlanId());
				_dispatchResource.setResourceId(resource.getResourceId());
				_dispatchResource.setEmployeeRoleType(resource.getEmployeeRoleType());
				_dispatchResource.setAdjustmentTime(resource.getAdjustmentTime());
				
				dispatchResourceList.add(_dispatchResource);
			}

		}
		return dispatchResourceList;
	}


	
	private static IHandOffBatchRoute matchRoute(IHandOffBatchPlan p, List<IHandOffBatchRoute> routeList) throws ParseException {
		IHandOffBatchRoute result = null;
		if(routeList != null && p != null) {
		
			Iterator<IHandOffBatchRoute> _routeItr = routeList.iterator();
			while(_routeItr.hasNext()) {
				IHandOffBatchRoute route = _routeItr.next();				
				if(route.getDispatchTime() != null && p.getDispatchTime() != null){
					if(route.getDispatchTime().getAsDate().equals(p.getDispatchTime())) {
						result = route;						
						_routeItr.remove();
						break;						
					}					
				}
			}
		}
		return result;
	}
	

	private static IHandOffBatchTrailer matchTrailer(IHandOffBatchPlan p, List<IHandOffBatchTrailer> trailerList) throws ParseException {
		IHandOffBatchTrailer result = null;
		if(trailerList != null && p != null) {
			Iterator<IHandOffBatchTrailer> _trailerItr = trailerList.iterator();
			while(_trailerItr.hasNext()) {
				IHandOffBatchTrailer trailer = _trailerItr.next();
				if(trailer.getTrailerDispatchTime()!= null && p.getDispatchTime() != null){
					if(trailer.getTrailerDispatchTime().equals(p.getDispatchTime())) {
						result = trailer;
						_trailerItr.remove();
						break;
					}
				}
			}
		}
		return result;
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
	
}
