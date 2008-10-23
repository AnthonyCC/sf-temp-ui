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

import com.freshdirect.routing.constants.EnumProcessInfoType;
import com.freshdirect.routing.constants.EnumProcessType;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.service.IGeographyService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.IRoutingParamConstants;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;

public abstract class BaseProcessManager implements  IProcessManager {
	
	protected IProcessManager successor;

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
    	
    	try {			
			// load the zone details and fill the cache
			DeliveryServiceProxy proxy = new DeliveryServiceProxy();
			Map zoneDetails = proxy.getDeliveryZoneDetails();
			request.setDeliveryTypeCache(zoneDetails);
		} catch (RoutingServiceException e) {			
			throw new RoutingProcessException(null,e,IIssue.PROCESS_ZONEINFO_NOTFOUND);
		}
		
		IServiceTimeScenarioModel scenarioModel = loadServiceTimeScenario((Map)request.getProcessParam());
		if(scenarioModel == null) {
			throw new RoutingProcessException(null, null, IIssue.PROCESS_SCENARIO_NOTFOUND);
		}
		request.setProcessScenario(scenarioModel);
		return null;
    }
    
    public Object endProcess(ProcessContext request) throws RoutingProcessException {
    	    	
    	IGeographyService service = RoutingServiceLocator.getInstance().getGeographyService();
		List locLst = new ArrayList(((Map)request.getLocationList()).values());
		List buildingLst = new ArrayList(((Map)request.getBuildingList()).values());
		//Save Building
		if(buildingLst != null && buildingLst.size() > 0) {
			try {
				
				service.insertBuildings(buildingLst);	
				
			} catch (RoutingServiceException e) {			
				throw new RoutingProcessException(null,e,IIssue.PROCESS_BUILDING_SAVEERROR);
			}	
		}
		//Save Locations
		if(locLst != null && locLst.size() > 0) {
			try {
				
				service.insertLocations(locLst);	
				
			} catch (RoutingServiceException e) {			
				throw new RoutingProcessException(null,e,IIssue.PROCESS_LOCATION_SAVEERROR);
			}	
		}
		
		return doBatchRouting(request);
    }
    
    private Set doBatchRouting(ProcessContext request) throws  RoutingProcessException {
    	
    	Map tmpParams = (Map)request.getProcessParam();
    	String userId = (String)tmpParams.get(IRoutingParamConstants.ROUTING_USER);
    	
    	System.out.println("################### START SAVE LOCATION ##################");
    	//Save Locations
		saveLocations((List)request.getOrderList());
		System.out.println("################### END SAVE LOCATION ##################");
    	
		
    	//Start Order Purge
    	List orderGroupInfo = groupOrdersForBatchRouting((List)request.getOrderList());
    	Set schedulerIds = (Set)orderGroupInfo.get(0);    	
    	Map orderMappedLst = (Map)orderGroupInfo.get(1);
    	
    	
    	
    	purgeOrders(schedulerIds); 
    	    	    	
    	
    	System.out.println("################### BULK RESERVE START ##################");    	
    	Map unassignedOrders = schedulerBulkReserveOrders(schedulerIds, orderMappedLst);    	 
    	System.out.println("################### BULK RESERVE END ##################");
    	
    	System.out.println("################### SEND ROUTES TO ROADNET START ##################");
    	Map sessionDescriptionMap = sendRoutesToRoadNet(schedulerIds, userId, getCurrentTime());    	  
    	System.out.println("################### SEND ROUTES TO ROADNET END ##################");
    	
    	System.out.println("################### SAVE UNASSIGNED START ##################");
    	Map unassignedSaveFailed = saveUnassignedToRoadNet(sessionDescriptionMap, unassignedOrders);
    	System.out.println("################### SAVE UNASSIGNED END ##################");
	    
    	return saveProcessStatus(request, sessionDescriptionMap, unassignedOrders);
    }
       
        
    private void saveLocations(List lstOrders) throws  RoutingProcessException {
    	try {
	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
	    	proxy.saveLocations(lstOrders, RoutingServicesProperties.getDefaultRegion(), RoutingServicesProperties.getDefaultLocationType());
    	} catch (RoutingServiceException e) {			
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
				proxy.purgeOrders(schedulerId);
			}
    	} catch (RoutingServiceException e) {			
			throw new RoutingProcessException(null,e,IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
		} 
    }
    
    private Map schedulerBulkReserveOrders(Set schedulerIdLst, Map orderMappedLst) throws  RoutingProcessException {
    	
    	Map unassignedOrders = new HashMap();
    	try {
	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
	    	Iterator tmpIterator = schedulerIdLst.iterator();
	    	IRoutingSchedulerIdentity schedulerId = null;
			while(tmpIterator.hasNext()) {
				schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
				unassignedOrders.put(schedulerId, proxy.schedulerBulkReserveOrder(schedulerId
														, (List)orderMappedLst.get(schedulerId)
														, RoutingServicesProperties.getDefaultRegion()
														, RoutingServicesProperties.getDefaultLocationType()
														, RoutingServicesProperties.getDefaultOrderType()));
			}
    	} catch (RoutingServiceException e) {			
			throw new RoutingProcessException(null,e,IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
		} 
    	return unassignedOrders;
    }
    
    private Map sendRoutesToRoadNet(Set schedulerIdLst, String userId, String currentTime) throws  RoutingProcessException {    	
    	Map sessionDescriptionMap = new HashMap();
    	try {
	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
	    	Iterator tmpIterator = schedulerIdLst.iterator();
	    	IRoutingSchedulerIdentity schedulerId = null;
	    	String sessionDescription = null;
			while(tmpIterator.hasNext()) {
				schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
				sessionDescription = userId+"_"+RoutingDateUtil.formatPlain(schedulerId.getDeliveryDate())+"_"+currentTime;
				System.out.println(schedulerId+"=="+sessionDescription);
				proxy.sendRoutesToRoadNet(schedulerId, sessionDescription);
				sessionDescriptionMap.put(schedulerId, sessionDescription);				
			}
    	} catch (RoutingServiceException e) {	
    		e.printStackTrace();
			throw new RoutingProcessException(null,e,IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
		} catch (ParseException parseExp) {
			parseExp.printStackTrace();
			throw new RoutingProcessException(null,parseExp,IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
		}    	
    	return sessionDescriptionMap;
    }
    
    private Map saveUnassignedToRoadNet(Map sessionDescriptionMap, Map unassignedOrders) throws  RoutingProcessException {    	
    	Map saveUnassingedFailed = new HashMap();
    	try {
	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
	    	Iterator tmpIterator = sessionDescriptionMap.keySet().iterator();
	    	IRoutingSchedulerIdentity schedulerId = null;
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
			throw new RoutingProcessException(null,e,IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
		}    	
    	return saveUnassingedFailed;
    }
    
    private Set saveProcessStatus(ProcessContext request, Map sessionDescriptionMap, Map unassignedOrders) throws  RoutingProcessException {   	    	
    	
    	Iterator tmpIterator = sessionDescriptionMap.keySet().iterator();
    	IRoutingSchedulerIdentity schedulerId = null;
    	String sessionDescription = null;
    	List tmpUnassignedLst = null;
    	Set sessionIds = new HashSet();
		while(tmpIterator.hasNext()) {
			schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
			sessionDescription = (String)sessionDescriptionMap.get(schedulerId);
			tmpUnassignedLst = (List)unassignedOrders.get(schedulerId);
			
			ProcessInfo  processInfoSession = new ProcessInfo();
			processInfoSession.setProcessType(EnumProcessType.CREATE_ROUTINGSESSION);
			processInfoSession.setProcessInfoType(EnumProcessInfoType.INFO);
			processInfoSession.setAdditionalInfo(schedulerId.toString()+" -> "+sessionDescription);
			request.addProcessInfo(processInfoSession);
						
			ProcessInfo  processInfoUnassinged = new ProcessInfo();
			processInfoUnassinged.setProcessType(EnumProcessType.UNASSINGED_ROUTINGBULK);
			processInfoUnassinged.setProcessInfoType(EnumProcessInfoType.WARNING);
			processInfoUnassinged.setAdditionalInfo(schedulerId.toString()+" -> "
													+(tmpUnassignedLst != null ? tmpUnassignedLst.size() : 0)+" Unassigned Orders");			
			request.addProcessInfo(processInfoUnassinged);
			sessionIds.add(sessionDescription);
		}
		return sessionIds;
    }
    private List groupOrdersForBatchRouting(List lstOrders) {
    	
    	Map resultOrderIdMap = new HashMap();
    	Set resultIds = new HashSet();
    	
    	List result = new ArrayList();
    	result.add(resultIds);
    	result.add(resultOrderIdMap);
    	
    	if(lstOrders != null) {
    		
    		Iterator tmpIterator = lstOrders.iterator();
			IOrderModel orderModel = null;	
			IRoutingSchedulerIdentity schedulerId = null;
			List orderListForId = null;
			
			while(tmpIterator.hasNext()) {				
				orderModel = (IOrderModel)tmpIterator.next();
				schedulerId = getSchedulerId(schedulerId, orderModel);
				orderListForId = (List)resultOrderIdMap.get(schedulerId);
				
				if(orderListForId == null) {
					orderListForId = new ArrayList();
					resultOrderIdMap.put(schedulerId, orderListForId);					
					resultIds.add(schedulerId);
					schedulerId = null;
				} 
				orderListForId.add(orderModel);
			}
    	}
    	return result;
    }
     
    
    private IRoutingSchedulerIdentity getSchedulerId(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel) {
    	
    	if(schedulerId == null) {
    		schedulerId = new RoutingSchedulerIdentity();
    	}
    	schedulerId.setRegionId("FD");
    	schedulerId.setArea(orderModel.getDeliveryInfo().getDeliveryZone().getArea());
    	schedulerId.setDeliveryDate(orderModel.getDeliveryInfo().getDeliveryDate());
    	return schedulerId;
    }

    abstract public void processRequest(ProcessContext request);
    
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
    
    private String getCurrentTime() {
    	try {
    		return RoutingDateUtil.getCurrentTime();
    	} catch (Exception e) {
    		return "000000";
    	}
    }
}
