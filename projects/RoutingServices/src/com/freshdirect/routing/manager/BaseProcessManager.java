package com.freshdirect.routing.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.constants.EnumProcessInfoType;
import com.freshdirect.routing.constants.EnumProcessType;
import com.freshdirect.routing.constants.EnumRoutingFlowType;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.Issue;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.IRoutingParamConstants;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingUtil;

public abstract class BaseProcessManager implements  IProcessManager {

	protected IProcessManager successor;

	private final String ROUTING_SUCCESS = "Routing Success";
	
	private final String LOADBALANCE_SUCCESS = "Load Balance Success";

	private final String SENDROUTES_SUCCESS = "Send Routes to RoadNet Success";

	private final String SEPARATOR = "; ";
	
	private boolean hadLoadBalance = false;

    public void setSuccessor(IProcessManager successor){
        this.successor = successor;
    }

    public Object process(ProcessContext request) throws RoutingProcessException {
    	processRequest(request);
    	if(successor != null) {
    		successor.process(request);
    	}
    	return null;
    }

    public Object startProcess(ProcessContext request) throws RoutingProcessException {
    	DeliveryServiceProxy proxy = new DeliveryServiceProxy();
    	try {
			// load the zone details and fill the cache			
			Map zoneDetails = proxy.getDeliveryZoneDetails();
			request.setDeliveryTypeCache(zoneDetails);
		} catch (RoutingServiceException e) {
			e.printStackTrace();
			throw new RoutingProcessException(null,e,IIssue.PROCESS_ZONEINFO_NOTFOUND);
		}
		
		try {
			if(!StringUtil.isEmpty(RoutingServicesProperties.getRoutingLateDeliveryQuery())) {
				System.out.println("################## START LATE DELIVERY #########################");
				List lateDeliveryOrders = proxy.getLateDeliveryOrders(RoutingServicesProperties.getRoutingLateDeliveryQuery());
				request.setLateDeliveryOrderList(lateDeliveryOrders);
				System.out.println("################## END LATE DELIVERY #########################"+lateDeliveryOrders.size());
			} else {
				System.out.println("################## START LATE DELIVERY EMPTY QUERY#########################");
			}
		} catch (Exception e) {
			// It is not critical run without late delivery.
			e.printStackTrace();
		}

		IServiceTimeScenarioModel scenarioModel = loadServiceTimeScenario((Map)request.getProcessParam());
		if(scenarioModel == null) {
			throw new RoutingProcessException(null, null, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
		request.setProcessScenario(scenarioModel);
		return null;
    }

    public Object endProcess(ProcessContext request) throws RoutingProcessException {

    	/*IGeographyService service = RoutingServiceLocator.getInstance().getGeographyService();
		List locLst = new ArrayList(((Map)request.getLocationList()).values());
		List buildingLst = new ArrayList(((Map)request.getBuildingList()).values());
		//Save Building
		if(buildingLst != null && buildingLst.size() > 0) {
			try {

				service.insertBuildings(buildingLst);

			} catch (RoutingServiceException e) {
				e.printStackTrace();
				throw new RoutingProcessException(null,e,IIssue.PROCESS_BUILDING_SAVEERROR);
			}
		}
		//Save Locations
		if(locLst != null && locLst.size() > 0) {
			try {
				System.out.println("locLst >>"+locLst);
				service.insertLocations(locLst);

			} catch (RoutingServiceException e) {
				e.printStackTrace();
				throw new RoutingProcessException(null,e,IIssue.PROCESS_LOCATION_SAVEERROR);
			}
		} */

		return doBatchRouting(request);
    }

    private Set doBatchRouting(ProcessContext request) throws  RoutingProcessException {

    	Set result = new HashSet();
	 	    		    	    	
    	System.out.println("################### ROUTING START DEPOT ##################");
    	List orderDepotGroupInfo = groupOrdersForBatchRouting((List)request.getOrderList(), true);
    	Set resultDepot = doRoute(request, orderDepotGroupInfo);
    	System.out.println("################### ROUTING END DEPOT ##################");
    	if(resultDepot != null) {
    		result.addAll(resultDepot);
    	}
    	
    	System.out.println("\n################### ROUTING START REGULAR ##################");
    	List orderRegularGroupInfo = groupOrdersForBatchRouting((List)request.getOrderList(), false);
    	Set resultNormal = doRoute(request, orderRegularGroupInfo);
    	System.out.println("################### ROUTING END REGULAR ##################\n");
    	
    	if(resultNormal != null) {
    		result.addAll(resultNormal);
    	}
    	
    	
    	return result;
    }
    
    private Set doRoute(ProcessContext request, List orderGroupInfo) throws  RoutingProcessException {
    	
    	Map tmpParams = (Map)request.getProcessParam();
    	String userId = (String)tmpParams.get(IRoutingParamConstants.ROUTING_USER);
    	
    	Set schedulerIds = (Set)orderGroupInfo.get(0);
    	Map orderMappedLst = (Map)orderGroupInfo.get(1);
    	Map orderLocationLst = (Map)orderGroupInfo.get(2);

    	if(schedulerIds.size() > 0) {    		
    		
        	//Save Locations
    		Iterator tmpIterator = orderLocationLst.keySet().iterator();
    		    		
    		String _region = null;
    		List _locations = null;
    		
			while(tmpIterator.hasNext()) {
				_region = (String)tmpIterator.next();
				_locations = (List)orderLocationLst.get(_region);
				if(_locations != null && _locations.size() > 0) {
					System.out.println("################### START SAVE LOCATION ##################"+_region);
		    		saveLocations(_locations, _region);
		    		System.out.println("################### END SAVE LOCATION ##################");
				}	    		
			}
			
    		
    		purgeOrders(schedulerIds);
	
	    	hadLoadBalance = false;
	    	boolean sendRoutes = false;
	    	
	    	String currentTime = getCurrentTime();
	    	
	    	if(EnumRoutingFlowType.LINEARFLOW.equals(EnumRoutingFlowType.getEnum(RoutingServicesProperties.getRoutingFlowType()))) {
	    		sendRoutes = true;
	    	}
	    	System.out.println("################### BULK RESERVE START ################## "+sendRoutes);
	    	BulkReserveResult result = schedulerBulkReserveOrders(schedulerIds, orderMappedLst
	    															, (IServiceTimeScenarioModel)request.getProcessScenario()
	    															, userId, currentTime, sendRoutes);
	    	
	    	Map unassignedOrders = result.getUnassignedOrders();
	    	System.out.println("################### BULK RESERVE END ##################");
	    	
	    	Map sessionDescriptionMap = null;
	    	if(sendRoutes) {
	    		sessionDescriptionMap = result.getSessionDescriptionMap();
	    	} else {
	    		System.out.println("################### SEND ROUTES TO ROADNET START ##################");
	        	sessionDescriptionMap = sendRoutesToRoadNet(schedulerIds, userId, currentTime);
	        	System.out.println("################### SEND ROUTES TO ROADNET END ##################");
	    	}    	    	
	
	    	System.out.println("################### SAVE UNASSIGNED START ################## ");
	    	Map unassignedSaveFailed = saveUnassignedToRoadNet(sessionDescriptionMap, unassignedOrders);
	    	System.out.println("################### SAVE UNASSIGNED END ##################");
	    	
	    	System.out.println("sessionDescriptionMap >>"+sessionDescriptionMap);
	    	return saveProcessStatus(request, sessionDescriptionMap, unassignedOrders);
    	} else {
    		return null;
    	}
    }


    private void saveLocations(List lstOrders, String region) throws  RoutingProcessException {
    	try {
	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
	    	proxy.saveLocations(lstOrders, region, RoutingServicesProperties.getDefaultLocationType());
    	} catch (RoutingServiceException e) {
    		e.printStackTrace();
			throw new RoutingProcessException(null,e,IIssue.PROCESS_LOCATION_SAVEERROR);
		}
    }

    private void purgeOrders(Set schedulerIdLst) throws  RoutingProcessException {

    	try {
	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
	    	Iterator tmpIterator = schedulerIdLst.iterator();
	    	IRoutingSchedulerIdentity schedulerId = null;
			while(tmpIterator.hasNext()) {
				schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
				proxy.purgeBatchOrders(schedulerId, false);
			}
    	} catch (RoutingServiceException e) {
    		e.printStackTrace();
			throw new RoutingProcessException(null,e,IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
		}
    }

    private BulkReserveResult schedulerBulkReserveOrders(Set schedulerIdLst, Map orderMappedLst
    										, IServiceTimeScenarioModel scenario,
    										String userId, String currentTime, boolean sendRoutes) throws  RoutingProcessException {
    	
    	BulkReserveResult result = new BulkReserveResult();
    	Map unassignedOrders = new HashMap();
    	Map sessionDescriptionMap = new HashMap();
    	IRoutingSchedulerIdentity schedulerId = null;
    	try {
	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
	    	Iterator tmpIterator = schedulerIdLst.iterator();
			while(tmpIterator.hasNext()) {
				schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
				unassignedOrders.put(schedulerId, proxy.schedulerBulkReserveOrder(schedulerId
														, (List)orderMappedLst.get(schedulerId)
														, schedulerId.getRegionId()
														, RoutingServicesProperties.getDefaultLocationType()				
														, RoutingServicesProperties.getDefaultOrderType()));
				
				schedulerBalanceRoutes(proxy, schedulerId, scenario);
				
				if(sendRoutes) {
					sessionDescriptionMap.put(schedulerId, sendRouteToRoadNet(proxy, schedulerId, userId, currentTime));
				}
				
				schedulerRemoveFromServer(proxy, schedulerId);
			}
    	} catch (RoutingServiceException e) {
    		e.printStackTrace();
			throw new RoutingProcessException(getErrorMessage("", schedulerId.getArea().getAreaCode())
													,e,IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
		}
    	result.setUnassignedOrders(unassignedOrders);
    	result.setSessionDescriptionMap(sessionDescriptionMap);
    	return result;
    }
    
    private void schedulerRemoveFromServer(RoutingEngineServiceProxy proxy, IRoutingSchedulerIdentity schedulerId) throws  RoutingProcessException {
    	    	
    	try {
    		if(RoutingServicesProperties.isRemoveSchedulerEnabled()) {
    			proxy.schedulerRemoveFromServer(schedulerId);
    		}
    	} catch (RoutingServiceException e) {
    		e.printStackTrace();
			throw new RoutingProcessException(getErrorMessage("", schedulerId.getArea().getAreaCode())
													,e,IIssue.PROCESS_REMOVEFROMSERVER_UNSUCCESSFUL);
		}
    }

    private void schedulerBalanceRoutes(RoutingEngineServiceProxy proxy, IRoutingSchedulerIdentity schedulerId
    									, IServiceTimeScenarioModel scenario) throws  RoutingProcessException {
    	
    	try {	    	
	    	String balanceBy = null;
	    	double balanceFactor = 0.0;
	    	if(RoutingServicesProperties.isLoadBalanceEnabled()) {
		    	if(scenario.getNeedsLoadBalance() || schedulerId.getArea().getNeedsLoadBalance()) {
					if(schedulerId.getArea().getNeedsLoadBalance()) {
						balanceBy = schedulerId.getArea().getBalanceBy();
				    	balanceFactor = schedulerId.getArea().getLoadBalanceFactor();
					} else {
						balanceBy = scenario.getBalanceBy();
				    	balanceFactor = scenario.getLoadBalanceFactor();
					}
					proxy.schedulerBalanceRoutes(schedulerId, balanceBy, balanceFactor);
					hadLoadBalance = true;
				}
	    	}
    	} catch (RoutingServiceException e) {
    		e.printStackTrace();
    		StringBuffer strBuf = new StringBuffer();
    		strBuf.append(this.ROUTING_SUCCESS).append(SEPARATOR);
    		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_BALANCEROUTES_UNSUCCESSFUL), schedulerId.getArea().getAreaCode()));
    		throw new RoutingProcessException(strBuf.toString(),e,IIssue.EMPTY);
		}
    }
    
    private String sendRouteToRoadNet(RoutingEngineServiceProxy proxy, IRoutingSchedulerIdentity schedulerId, 
    									String userId, String currentTime) throws  RoutingProcessException {
    	String sessionDescription = null;    	
    	try {
    		sessionDescription = userId+"_"+getSessionType(schedulerId)
    								+"_"+RoutingDateUtil.formatPlain(schedulerId.getDeliveryDate())
    									+"_"+currentTime;
			proxy.sendRoutesToRoadNet(schedulerId, sessionDescription);
    	} catch (RoutingServiceException e) {
    		e.printStackTrace();
    		StringBuffer strBuf = new StringBuffer();
    		strBuf.append(this.ROUTING_SUCCESS).append(SEPARATOR);
    		if(hadLoadBalance) {
    			strBuf.append(this.LOADBALANCE_SUCCESS).append(SEPARATOR);
    		}
    		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_SENDROUTES_UNSUCCESSFUL), schedulerId.getArea().getAreaCode()));
    		throw new RoutingProcessException(strBuf.toString(),e,IIssue.EMPTY);
		} catch (ParseException parseExp) {
			parseExp.printStackTrace();
			throw new RoutingProcessException(Issue.getMessage(IIssue.DATEPARSE_ERROR),parseExp,IIssue.PROCESS_SENDROUTES_UNSUCCESSFUL);
		}
    	return sessionDescription;
    }
    
    private Map sendRoutesToRoadNet(Set schedulerIdLst, String userId, String currentTime) throws  RoutingProcessException {
    	Map sessionDescriptionMap = new HashMap();
    	IRoutingSchedulerIdentity schedulerId = null;
    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
    	Iterator tmpIterator = schedulerIdLst.iterator();    	
		while(tmpIterator.hasNext()) {
			schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
			//sessionDescription = userId+"_"+RoutingDateUtil.formatPlain(schedulerId.getDeliveryDate())+"_"+currentTime;
			//proxy.sendRoutesToRoadNet(schedulerId, sessionDescription);
			sessionDescriptionMap.put(schedulerId, sendRouteToRoadNet(proxy, schedulerId, userId, currentTime));
		}
    	return sessionDescriptionMap;
    }

    private Map saveUnassignedToRoadNet(Map sessionDescriptionMap, Map unassignedOrders) throws  RoutingProcessException {
    	Map saveUnassingedFailed = new HashMap();
    	IRoutingSchedulerIdentity schedulerId = null;
    	try {
	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
	    	Iterator tmpIterator = sessionDescriptionMap.keySet().iterator();
	    	String sessionDescription = null;
	    	List tmpUnassignedLst = null;
			while(tmpIterator.hasNext()) {
				schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
				sessionDescription = (String)sessionDescriptionMap.get(schedulerId);
				tmpUnassignedLst = (List)unassignedOrders.get(schedulerId);
				if(tmpUnassignedLst != null && tmpUnassignedLst.size() > 0) {
					List dataList = proxy.saveUnassignedToRoadNet(schedulerId
																	, proxy.retrieveRoutingSession(schedulerId, sessionDescription),
																	tmpUnassignedLst);
					saveUnassingedFailed.put(schedulerId, dataList);
				}
			}
    	} catch (RoutingServiceException e) {
    		e.printStackTrace();
    		StringBuffer strBuf = new StringBuffer();
    		strBuf.append(this.ROUTING_SUCCESS).append(SEPARATOR);
    		if(hadLoadBalance) {
    			strBuf.append(this.LOADBALANCE_SUCCESS).append(SEPARATOR);
    		}
    		strBuf.append(this.SENDROUTES_SUCCESS).append(SEPARATOR);
    		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_SENDUNASSIGNED_UNSUCCESSFUL), schedulerId.getArea().getAreaCode()));
			throw new RoutingProcessException(strBuf.toString(),e,IIssue.EMPTY);
		}
    	return saveUnassingedFailed;
    }

    private Set saveProcessStatus(ProcessContext request, Map sessionDescriptionMap, Map unassignedOrders) throws  RoutingProcessException {

    	Iterator tmpIterator = sessionDescriptionMap.keySet().iterator();
    	IRoutingSchedulerIdentity schedulerId = null;
    	String sessionDescription = null;
    	List tmpUnassignedLst = null;
    	Set sessionIds = new HashSet();
    	String sessionType = null;
		while(tmpIterator.hasNext()) {
			schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
			sessionDescription = (String)sessionDescriptionMap.get(schedulerId);
			tmpUnassignedLst = (List)unassignedOrders.get(schedulerId);
			
			sessionType = getSessionType(schedulerId);

			ProcessInfo  processInfoSession = new ProcessInfo();
			processInfoSession.setProcessType(EnumProcessType.CREATE_ROUTINGSESSION);
			processInfoSession.setProcessInfoType(EnumProcessInfoType.INFO);
			processInfoSession.setAdditionalInfo(schedulerId.toString()+":"+sessionType+" -> "+sessionDescription);
			request.addProcessInfo(processInfoSession);

			ProcessInfo  processInfoUnassinged = new ProcessInfo();
			processInfoUnassinged.setProcessType(EnumProcessType.UNASSINGED_ROUTINGBULK);
			processInfoUnassinged.setProcessInfoType(EnumProcessInfoType.WARNING);
			processInfoUnassinged.setAdditionalInfo(schedulerId.toString()+" -> "
													+(tmpUnassignedLst != null ? tmpUnassignedLst.size() : 0)+" Unassigned Orders");
			request.addProcessInfo(processInfoUnassinged);
			sessionIds.add(sessionType+" -> "+sessionDescription);
		}
		return sessionIds;
    }
    
    private String getSessionType(IRoutingSchedulerIdentity schedulerId) {
    	return (schedulerId != null && schedulerId.isDepot()?"Depot":"Trucks");
    }
    
    private List groupOrdersForBatchRouting(List lstOrders, boolean depot) {

    	Map resultOrderIdMap = new HashMap();
    	Map resultLocationMap = new HashMap();
    	Set resultIds = new HashSet();

    	List result = new ArrayList();
    	result.add(resultIds);
    	result.add(resultOrderIdMap);
    	result.add(resultLocationMap);
    	   	

    	if(lstOrders != null) {

    		Iterator tmpIterator = lstOrders.iterator();
			IOrderModel orderModel = null;
			IRoutingSchedulerIdentity schedulerId = null;
			List orderListForId = null;
			List orderLocations = null;

			while(tmpIterator.hasNext()) {
				orderModel = (IOrderModel)tmpIterator.next();
				schedulerId = getSchedulerId(schedulerId, orderModel);
				if((depot && !schedulerId.isDepot())
						|| !depot && schedulerId.isDepot()) {
					continue;
				}
				orderListForId = (List)resultOrderIdMap.get(schedulerId);
				orderLocations = (List)resultLocationMap.get(schedulerId.getRegionId());
				
				if(orderLocations == null) {
					orderLocations = new ArrayList();
					resultLocationMap.put(schedulerId.getRegionId(), orderLocations);					
				}
				
				if(orderListForId == null) {
					orderListForId = new ArrayList();
					resultOrderIdMap.put(schedulerId, orderListForId);
					resultIds.add(schedulerId);
					schedulerId = null;
				}
				
				orderListForId.add(orderModel);
				orderLocations.add(orderModel);
			}
    	}
    	
    	return result;
    }


    private IRoutingSchedulerIdentity getSchedulerId(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel) {

    	if(schedulerId == null) {
    		schedulerId = new RoutingSchedulerIdentity();
    	}
    	schedulerId.setRegionId(RoutingUtil.getRegion(orderModel.getDeliveryInfo().getDeliveryZone().getArea()));
    	schedulerId.setArea(orderModel.getDeliveryInfo().getDeliveryZone().getArea());
    	schedulerId.setDeliveryDate(orderModel.getDeliveryInfo().getDeliveryDate());
    	if(orderModel.getDeliveryInfo() != null 
    				&& orderModel.getDeliveryInfo().getDeliveryZone() != null
    					&& orderModel.getDeliveryInfo().getDeliveryZone().getArea() != null) {
    		schedulerId.setDepot(orderModel.getDeliveryInfo().getDeliveryZone().getArea().isDepot());
    	}
    	
    	return schedulerId;
    }

    abstract public void processRequest(ProcessContext request) throws RoutingProcessException ;

    private IServiceTimeScenarioModel loadServiceTimeScenario(Map processParam) throws RoutingProcessException {

    	RoutingInfoServiceProxy proxy = new RoutingInfoServiceProxy();
    	IServiceTimeScenarioModel model = null;
    	IServiceTimeScenarioModel defaultModel = null;
    	try {
	    	String scenarioCode = (String)processParam.get(IRoutingParamConstants.SERVICETIME_SCENARIO);
	    	Collection scenarios = proxy.getRoutingScenarios();
	    	if(scenarioCode != null) {
		    	Iterator iterator = scenarios.iterator();
		    	IServiceTimeScenarioModel tmpInputModel = null;
				while(iterator.hasNext()) {

					tmpInputModel = (IServiceTimeScenarioModel)iterator.next();
					if(scenarioCode.equals(tmpInputModel.getCode())) {
						model = tmpInputModel;
					}

					if("X".equals(tmpInputModel.getCode())) {
						defaultModel = tmpInputModel;
					}

				}
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
			throw new RoutingProcessException(null,e,IIssue.PROCESS_SCENARIO_NOTFOUND);
		}

    	if(model == null && defaultModel != null) {
    		model = defaultModel;
    	}

    	return model;
    }

    private String getErrorMessage(String message, String areaCode) {
    	return message+" (Area: "+areaCode+")";
    }

    private String getCurrentTime() {
    	try {
    		return RoutingDateUtil.getCurrentTime();
    	} catch (Exception e) {
    		return "000000";
    	}
    }
    
    class BulkReserveResult {
    	
    	private Map unassignedOrders;
    	private Map sessionDescriptionMap;
    	    	
		public Map getSessionDescriptionMap() {
			return sessionDescriptionMap;
		}
		public void setSessionDescriptionMap(Map sessionDescriptionMap) {
			this.sessionDescriptionMap = sessionDescriptionMap;
		}
		public Map getUnassignedOrders() {
			return unassignedOrders;
		}
		public void setUnassignedOrders(Map unassignedOrders) {
			this.unassignedOrders = unassignedOrders;
		}		
    }
}
