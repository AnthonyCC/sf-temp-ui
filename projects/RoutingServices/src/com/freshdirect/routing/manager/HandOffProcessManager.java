package com.freshdirect.routing.manager;

import static com.freshdirect.routing.manager.IProcessMessage.LOADBALANCE_SUCCESS;
import static com.freshdirect.routing.manager.IProcessMessage.ROUTING_SUCCESS;
import static com.freshdirect.routing.manager.IProcessMessage.SENDROUTES_SUCCESS;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.DepotLocationModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.UnassignedDlvReservationModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.routing.constants.RoutingActivityType;
import com.freshdirect.routing.model.HandOffBatchSession;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchSession;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRegionModel;
import com.freshdirect.routing.model.IRoutingDepotId;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.RoutingDepotId;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.TrnFacilityType;
import com.freshdirect.routing.model.WaveInstance;
import com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder;
import com.freshdirect.routing.service.IGeographyService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.Issue;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.CapacityEngineServiceProxy;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.IRoutingParamConstants;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;
import com.freshdirect.routing.util.RoutingUtil;

public class HandOffProcessManager {
	
	private final String SEPARATOR = "; ";
	

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
		
		RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();
		String scenarioCode = (String)((Map)request.getProcessParam()).get(IRoutingParamConstants.SERVICETIME_SCENARIO);
							
		try {
			IServiceTimeScenarioModel scenarioModel = routingInfoProxy.getRoutingScenarioByCode(scenarioCode);// loadServiceTimeScenario((Map)request.getProcessParam());
			if(scenarioModel == null) {
				throw new RoutingProcessException(null, null, IIssue.PROCESS_SCENARIO_NOTFOUND);
			}
			request.setProcessScenario(scenarioModel);
			request.setServiceTimeTypeCache(routingInfoProxy.getRoutingServiceTimeTypes());
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RoutingProcessException(null,e,IIssue.PROCESS_ZONEINFO_NOTFOUND);
		}
		
		return null;
    }

    public Map<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> endProcess(ProcessContext request) throws RoutingProcessException {
        	
    	IGeographyService service = RoutingServiceLocator.getInstance().getGeographyService();
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
				
				service.insertLocations(locLst);
			} catch (RoutingServiceException e) {
				e.printStackTrace();
				throw new RoutingProcessException(null,e,IIssue.PROCESS_LOCATION_SAVEERROR);
			}
		} 
		
		if(RoutingServicesProperties.getRoutingBatchSyncEnabled()) {
			RoutingInfoServiceProxy proxy = new RoutingInfoServiceProxy();
			if(!proxy.isPlanPublished(request.getHandOffBatch().getDeliveryDate())) {
				throw new RoutingProcessException(null, null,IIssue.PROCESS_WAVEINSTANCE_NOTPUBLISHED);
			}
		}
		
		return doBatchRouting(request);
		
    }

    private Map<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> doBatchRouting(ProcessContext request) 
    																								throws  RoutingProcessException {
 	    	    	
    	Map<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> sessions;
    	List<Exception> exceptions = new ArrayList<Exception>();
		try {
			sessions = groupOrdersForBatchRouting(request);
			SessionExecutionCommand _task = null;
			if(RoutingServicesProperties.isHandOffRouteInMultiThread()) {
				ExecutorService sessionExecutor = Executors.newFixedThreadPool(sessions.keySet().size());
				List<Future<SessionExecutionCommand>> tasks = new ArrayList<Future<SessionExecutionCommand>>();
			
				for(Map.Entry<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> sesEntry : sessions.entrySet()) {
					
					_task = new SessionExecutionCommand(request, sesEntry);
					Future<SessionExecutionCommand> future = sessionExecutor.submit(_task, _task);
				    tasks.add(future);		       						    
				}
				
				try {
					for (Future<SessionExecutionCommand> checkFuture : tasks) {
						_task = checkFuture.get();
						System.out.println(" [Session Executor Thread Completed ]: " 
												+ (_task.getException() == null ? "NO ERRORS" : _task.getException()));
						if(_task.getException() != null) {
							sessionExecutor.shutdown();
							exceptions.add(_task.getException());
							/*throw new RoutingProcessException(_task.getException().getMessage(), _task.getException()
																					, IIssue.PROCESS_HANDOFFBATCH_ERROR);*/
						}
					}
					sessionExecutor.shutdown();
				} catch (ExecutionException e) {
					e.printStackTrace();
					throw new RoutingProcessException(null, e, IIssue.PROCESS_HANDOFFBATCH_ERROR);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
					throw new RoutingProcessException(null, ie, IIssue.PROCESS_HANDOFFBATCH_ERROR);
				}
			} else {
				for(Map.Entry<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> sesEntry : sessions.entrySet()) {
					_task = new SessionExecutionCommand(request, sesEntry);
					_task.execute();
					if(_task.getException() != null) {
						
						throw new RoutingProcessException(_task.getException().getMessage()
															, _task.getException(), IIssue.PROCESS_HANDOFFBATCH_ERROR);
					}
				}
			}
		} catch (ParseException e) {
			throw new RoutingProcessException(null,e,IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
    	if(exceptions.size() > 0) {
    		throw new RoutingProcessException(exceptions.get(0).getMessage(), exceptions.get(0), IIssue.PROCESS_HANDOFFBATCH_ERROR);
    	}
 	
    	return sessions;
    }
    
    private class SessionExecutionCommand implements Serializable, Runnable {
    	
    	private ProcessContext context;
    	
    	private Map.Entry<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> sessionInfo = null;
    	
    	private Exception exception;

    	private boolean hadLoadBalance = false;
    	
    	public SessionExecutionCommand(ProcessContext context, 
    							Map.Entry<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> sessionInfo) {
    		super();
    		this.context = context;
    		this.sessionInfo = sessionInfo;
    	}
    	
    	public void execute()  {
    		System.out.println("\n################### ROUTING START "+ sessionInfo.getKey().getSessionName() +"##################");
    		
    		List<IOrderModel> dynamicOrders = new ArrayList<IOrderModel>();
    		List<IOrderModel> staticOrders = new ArrayList<IOrderModel>();
    		Map<IRoutingSchedulerIdentity, List<IOrderModel>> dynamicSchMap = new HashMap<IRoutingSchedulerIdentity, List<IOrderModel>>();
    		Map<IRoutingSchedulerIdentity, List<IOrderModel>> staticSchMap =  new HashMap<IRoutingSchedulerIdentity, List<IOrderModel>>();
    		Map<IRoutingSchedulerIdentity, List> rsvSchMap =  new HashMap<IRoutingSchedulerIdentity, List>();
    		
    		for(Map.Entry<IRoutingSchedulerIdentity, List<IOrderModel>> schEntry : sessionInfo.getValue().entrySet()) {
    			if(schEntry.getKey().isDynamic())
    			{
    				dynamicOrders.addAll(schEntry.getValue());
    				dynamicSchMap.put(schEntry.getKey(), schEntry.getValue());
    			}
    			else
    			{
    				staticOrders.addAll(schEntry.getValue());
    				staticSchMap.put(schEntry.getKey(), schEntry.getValue());
    			}
    			
    		}
    		try {
    			if(staticOrders!=null && staticOrders.size()>0)
    			{
		    		//Save Location
		    		saveLocations(staticSchMap, sessionInfo.getKey().getRegion());
		    		
		    		//Purge Scheduler Instances
		    		purgeOrders(staticSchMap.keySet());
		    		
		    		if(RoutingServicesProperties.getRoutingBatchSyncEnabled()) {
		    			//Setup WaveInstances for DeliveryDate and CutOff and empty out resources for other cutoff
		    			setupWaveInstances(context.getHandOffBatch(), staticSchMap.keySet());
		    		}
		    		//Bulk Reserve and Other Child Processes
		    		Map<IRoutingSchedulerIdentity, List> unassignedOrders = schedulerBulkReserveOrders(staticSchMap.keySet()
		    																		,staticSchMap
		    																		, (IServiceTimeScenarioModel)context.getProcessScenario()
		    																		, sessionInfo.getKey());
		    		
		    		
		    		// Save Unassigned to RoadNet
		    		saveUnassignedToRoadNet(sessionInfo.getKey(), unassignedOrders);
		    		    		 
		    	}
    			if(dynamicOrders!=null && dynamicOrders.size()>0)
    			{
    				Calendar cal = Calendar.getInstance();
    				TimeOfDay cutoffTime = new TimeOfDay(context.getHandOffBatch().getCutOffDateTime());
    				Calendar cutoffCal = cutoffTime.getAsCalendar(context.getHandOffBatch().getDeliveryDate());
    				cutoffCal.add(Calendar.DATE, -1);
    				
    				if(cal.getTime().after(cutoffCal.getTime()) || RoutingServicesProperties.isProcessUnassignedBeforeCutoff())
    				{
    					/* cancel expired reservations before HandOff route-in. 
    					 * This should cover any expired reservation in UPS from the point the stored procedure EXPIRE_RESERVATION last executed and running the route-in */
    					
    					flagExpiredReservations();
    					rsvSchMap = handleUnassignedReservations(sessionInfo.getKey().getRegion(), context.getHandOffBatch().getDeliveryDate(), context.getHandOffBatch().getCutOffDateTime(), dynamicOrders);
    					
    				}
    				
    				RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
        	    	Iterator tmpIterator = dynamicSchMap.keySet().iterator();
        			
        	    	while(tmpIterator.hasNext()) 
        			{
        				IRoutingSchedulerIdentity schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
        				sendRouteToRoadNet(proxy, schedulerId,  sessionInfo.getKey(), RoutingDateUtil.getWaveCode(context.getHandOffBatch().getCutOffDateTime()));
        			}
        	    	if(rsvSchMap!=null && rsvSchMap.keySet().size()>0)
    				saveUnassignedToRoadNet(sessionInfo.getKey(), rsvSchMap);
    				
    				
    			}
    			if((dynamicOrders!=null && dynamicOrders.size()>0) ||
    			(staticOrders!=null && staticOrders.size()>0))
    				new HandOffServiceProxy().addNewHandOffBatchSession(sessionInfo.getKey().getBatchId()
		    															  , sessionInfo.getKey().getSessionName()
    																	  , sessionInfo.getKey().getRegion());
    			
		    		
    		} catch (RoutingProcessException e) {
    			e.printStackTrace();
    			exception = e;
    		} catch (RoutingServiceException ex) {
				ex.printStackTrace();
    			exception = ex;
			}
    		finally
    		{
    			
    		}
    		System.out.println("\n################### ROUTING END "+ sessionInfo.getKey().getSessionName() +"##################");
    	}
    	
    	private void flagExpiredReservations() {
    		DeliveryServiceProxy proxy = new DeliveryServiceProxy();
    		proxy.flagExpiredReservations();
    	}

		private Map<IRoutingSchedulerIdentity, List> handleUnassignedReservations(String region, Date deliveryDate, Date cutOff, List<IOrderModel> dynamicOrders)
    	{
    		long startTime = System.currentTimeMillis();
    		DeliveryServiceProxy proxy = new DeliveryServiceProxy();
    		List<UnassignedDlvReservationModel> unassignedReservations=new ArrayList<UnassignedDlvReservationModel>();
			
				RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();
				List<String> zones = routingInfoProxy.getStaticZonesByDate(context.getHandOffBatch().getDeliveryDate());
		    	
	    		unassignedReservations = proxy.getUnassignedReservations(deliveryDate,cutOff);
	    		
				if(unassignedReservations != null && unassignedReservations.size() > 0) {			
					int unassignedProcessedCnt = 0;
					int unassignedBatchCnt = 0;
					
					Map<String, DepotLocationModel> depots = proxy.getDepotLocations();
					
					for (UnassignedDlvReservationModel reservation : unassignedReservations) {
						unassignedProcessedCnt++;
						unassignedBatchCnt++;
						if(unassignedBatchCnt > 25) {
							unassignedBatchCnt = 0;
							long batchTime = System.currentTimeMillis();
							if((batchTime - startTime) > (FDStoreProperties.getUnassignedProcessingLimit() * 60 * 1000)) {
								System.out.println("Unassigned Processing Batch Completed : "+unassignedProcessedCnt);
								break;
							}														
						}						
					
						
						processReservation(reservation,depots);
					}
				}
				
			unassignedReservations = proxy.getUnassignedReservationsEx(deliveryDate,cutOff);
				
    		return getFinalUnassignedReservations(region, dynamicOrders, unassignedReservations, zones);
    	}
    	
    	private Map<IRoutingSchedulerIdentity, List> getFinalUnassignedReservations(String region, List<IOrderModel> dynamicOrders,
				List<UnassignedDlvReservationModel> unassignedReservations,List<String> zones)
    	{
    		RoutingEngineServiceProxy routingProxy = new RoutingEngineServiceProxy();
    		Map<IRoutingSchedulerIdentity, List> rsvSchMap =  new HashMap<IRoutingSchedulerIdentity, List>();
			
    		Map<String, IOrderModel> orderMap = new HashMap<String, IOrderModel>();
			for(IOrderModel order : dynamicOrders)
			{
				orderMap.put(order.getOrderNumber(), order);
			}
			
			boolean updateFailed = false;
			List<UnassignedDlvReservationModel> filteredList = new ArrayList<UnassignedDlvReservationModel>();
			for (UnassignedDlvReservationModel reservation : unassignedReservations) {
				if(RoutingActivityType.CANCEL_TIMESLOT.equals(reservation.getUnassignedActivityType()) || RoutingActivityType.CONFIRM_TIMESLOT.equals(reservation.getUnassignedActivityType()))
				{
					updateFailed = true;
				}
				else
				{
					filteredList.add(reservation);
				}
				
			}
			HandOffServiceProxy handOffProxy = new HandOffServiceProxy();
			if(updateFailed)
			{
				handOffProxy.updateHandOffBatchMessage(context.getHandOffBatch().getBatchId(), IProcessMessage.INFO_MESSAGE_ROUTINGINPROGRESS_UPDATE_FAILED);
			}
			
			for (UnassignedDlvReservationModel reservation : filteredList) {
				
				IOrderModel orderModel = orderMap.get(reservation.getOrderId());
				if(orderModel!=null)
					{
	    			IRoutingSchedulerIdentity schedulerId = getSchedulerId(null,orderModel,zones);
	    			DeliveryAreaOrder dlvOrder = routingProxy.getDeliveryAreaModel(schedulerId,orderModel,schedulerId.getRegionId(),
							 RoutingServicesProperties.getDefaultLocationType()				
							, RoutingServicesProperties.getDefaultOrderType());
	    			if(region.equals(schedulerId.getRegionId()) && schedulerId.isDynamic())
		    		{
		    			if(!rsvSchMap.containsKey(schedulerId)) {
		    				rsvSchMap.put(schedulerId, new ArrayList<IOrderModel>());
						}
		    			rsvSchMap.get(schedulerId).add(dlvOrder);
		    		}
	    		}
    		}
			return rsvSchMap;
    	}
    	public Context getInitialContext() throws NamingException {
    		Hashtable<String, String> h = new Hashtable<String, String>();
    		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
    		h.put(Context.PROVIDER_URL, FDStoreProperties.getRoutingProviderURL());
    		return new InitialContext(h);
    	}
    	
    	 private void processReservation(DlvReservationModel reservation, Map<String, DepotLocationModel> depots) {
    	    	try {
    	    		
    	    		TimeslotEventModel event = null;
    	    		ContactAddressModel address =((UnassignedDlvReservationModel)reservation).getAddress(); 
    	    		DeliveryServiceProxy proxy = new DeliveryServiceProxy();
    				
    	    		
    	    		if(address == null) {//Depot

    	    			String locationId = reservation.getAddressId();
    	    			DepotLocationModel depot = depots.get(locationId);
    	    			if(depot != null) {
    		    			address=new ErpDepotAddressModel(depot.getAddress());  
    	    			}
    	    		}

    	    		if(address != null) {
    		    		RoutingActivityType unassignedAction = reservation.getUnassignedActivityType();
    		    		FDTimeslot _timeslot = new FDTimeslot(proxy.getTimeslotById(reservation.getTimeslotId()));
    		    		if(unassignedAction != null) {
    			    		if(RoutingActivityType.RESERVE_TIMESLOT.equals(unassignedAction)) {
    			    			if(_timeslot != null) {
    			    				proxy.reserveTimeslotEx(reservation, address, _timeslot, event);
    			    			}
    			    		} else {
    			
    			    			proxy.commitReservationEx(reservation, address, event);  
    			    			if(reservation.getUpdateStatus() != null) {
    			    				proxy.updateReservationEx(reservation, address, _timeslot);
    			    			}
    						}
    		    		} else if(reservation.getUpdateStatus() != null) {
    		    			proxy.updateReservationEx(reservation, address, _timeslot);
    		    		}
    	    		} else {
    	    			System.out.println("handleUnassignedOrders failed to fetch address reservation for id "+reservation.getId().toString());
    	    		}
    	    		
    				 
    				 
    			} catch (Exception e) {
    				StringWriter sw = new StringWriter();
    				e.printStackTrace(new PrintWriter(sw));
    			}
    	    }
    	    
    	 
    	 
    	private void saveLocations( Map<IRoutingSchedulerIdentity, List<IOrderModel>> orderMappedLst, String region) throws  RoutingProcessException {
    		IRoutingSchedulerIdentity schedulerId = null;
    		try {
    	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
    	    	
    	    	if(orderMappedLst != null && orderMappedLst.keySet()!=null)
    	    	{
	    	    	Iterator tmpIterator = orderMappedLst.keySet().iterator();
	    			while(tmpIterator.hasNext()) {
	    				schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
	    				proxy.saveLocationsEx(orderMappedLst.get(schedulerId), schedulerId, region, RoutingServicesProperties.getDefaultLocationType());
	    			}
    	    	}
    			
        	} catch (RoutingServiceException e) {
        		e.printStackTrace();
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_LOCATION_SAVEERROR), (schedulerId!=null)?schedulerId.getArea().getAreaCode():""));
        		throw new RoutingProcessException(strBuf.toString(),e,IIssue.PROCESS_LOCATION_SAVEERROR);
    		}
        }

        private void purgeOrders(Set schedulerIdLst) throws  RoutingProcessException {
        	IRoutingSchedulerIdentity schedulerId = null;
        	try {
    	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
    	    	Iterator tmpIterator = schedulerIdLst.iterator();
    	    	
    			while(tmpIterator.hasNext()) {
    				schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
    				proxy.purgeBatchOrders(schedulerId, true); //Changed to reload xml option
    			}
        	} catch (RoutingServiceException e) {
        		e.printStackTrace();
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL), (schedulerId!=null)?schedulerId.getArea().getAreaCode():""));
        		
    			throw new RoutingProcessException(strBuf.toString(),e,IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
    		}
        }
        
        private void setupWaveInstances(IHandOffBatch handOffBatch, Set schedulerIdLst) throws  RoutingProcessException {
        	IRoutingSchedulerIdentity schedulerId = null;
        	try {
    	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
    	    	CapacityEngineServiceProxy capacityProxy = new CapacityEngineServiceProxy();
    			RoutingInfoServiceProxy routeInfoProxy = new RoutingInfoServiceProxy();
    			
    			Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> waveInstanceTree = routeInfoProxy
    																													.getWaveInstanceTree(handOffBatch.getDeliveryDate(), null);
    			RoutingTimeOfDay cutOff = new RoutingTimeOfDay(handOffBatch.getCutOffDateTime());
    			Map<String, TrnFacilityType> facilityLookUp = routeInfoProxy.retrieveTrnFacilitys();

    			if(waveInstanceTree != null) {																																
	    	    	Iterator tmpIterator = schedulerIdLst.iterator();
	    	    	schedulerId = null;
	    			while(tmpIterator.hasNext()) {
	    				schedulerId = (IRoutingSchedulerIdentity)tmpIterator.next();
	    				//Dispatch-> CutOff ->WaveInstance
	    				Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> srcInstance = waveInstanceTree.get(schedulerId.getDeliveryDate())
	    																								.get(schedulerId.getArea().getAreaCode());
	    				if(srcInstance != null) {
	    					Collection<Map<RoutingTimeOfDay, List<IWaveInstance>>> _tmpCutOffMpp = srcInstance.values();
	    					//CutOff to Wave Instance Listing
	    					List<IWaveInstance> toSyncWaves = new ArrayList<IWaveInstance>();
	    						    									
	    					for(Map<RoutingTimeOfDay, List<IWaveInstance>> _tmpMpp : _tmpCutOffMpp) {
	    						for(Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> _tmpInnerMpp : _tmpMpp.entrySet()) {
	    							if(_tmpInnerMpp.getKey().equals(cutOff)) {		    							
		    							for(IWaveInstance _srcWaveInst : _tmpInnerMpp.getValue()) {
		    								
		    								if(_srcWaveInst.isNeedsConsolidation()) {
		    									if(toSyncWaves.size() == 0) {
		    										toSyncWaves.add(_srcWaveInst);		    										
		    									} else {
		    										IWaveInstance _rootWaveInstance = toSyncWaves.get(0);
		    										WaveInstance.consolidateWaveInstance(_rootWaveInstance, _srcWaveInst);																    										
		    									}
		    								} else {
		    									toSyncWaves.add(_srcWaveInst);
		    								}
		    							}
	    							}
	    						}										
	    					}
	    					
	    					List<IWaveInstance> destinationInstances = capacityProxy.retrieveWaveInstancesBatch(schedulerId);
	    					
	    					for(IWaveInstance _destInst : destinationInstances) {
	    						if(_destInst.getCutOffTime() != null) {
	    							
	    							if(_destInst.getCutOffTime().equals(cutOff) && toSyncWaves.size() > 0) {
	    								IWaveInstance _tmpMatch = toSyncWaves.remove(0);
	    								_destInst.setDispatchTime(_tmpMatch.getDispatchTime());
	    								_destInst.setMaxRunTime(_tmpMatch.getMaxRunTime());
	    								_destInst.setNoOfResources(_tmpMatch.getNoOfResources());
	    								_destInst.setPreferredRunTime(_tmpMatch.getPreferredRunTime());
	    								_destInst.setWaveStartTime(_tmpMatch.getWaveStartTime());	    								

	    								if(!schedulerId.isDepot()){
		    								IRoutingDepotId routingDepotId = new RoutingDepotId();
		    								routingDepotId.setLocationId(_tmpMatch.getOriginFacility());
		    								routingDepotId.setRegionID(schedulerId.getRegionId());

		    								/*if(_tmpMatch.getOriginFacility() != null)
		    									routingDepotId.setLocationType(facilityLookUp.get(_tmpMatch.getOriginFacility()).getName());
											*/
		    								routingDepotId.setLocationType(RoutingServicesProperties.getDefaultUpsLocationType());

		    								_tmpMatch.setDepotId(routingDepotId);
		    								_destInst.setDepotId(_tmpMatch.getDepotId());
	    								}
	    							} else {
	    								_destInst.setNoOfResources(0);	    								
	    							}
	    							capacityProxy.saveWaveInstancesBatch(schedulerId, _destInst, true);
	    						}
	    					}
	    				}
	    			}
    			}
        	} catch (RoutingServiceException e) {
        		e.printStackTrace();
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_SAVEWAVEINSTANCE_UNSUCCESSFUL), (schedulerId!=null)?schedulerId.getArea().getAreaCode():""));
        		
    			throw new RoutingProcessException(strBuf.toString(),e,IIssue.PROCESS_SAVEWAVEINSTANCE_UNSUCCESSFUL);
    		}
        }

        private Map<IRoutingSchedulerIdentity, List> schedulerBulkReserveOrders(Set schedulerIdLst, Map orderMappedLst
        										, IServiceTimeScenarioModel scenario, IHandOffBatchSession session) throws  RoutingProcessException {
        	        	
        	Map<IRoutingSchedulerIdentity, List> unassignedOrders = new HashMap<IRoutingSchedulerIdentity, List>();
        	
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
    				
    				sendRouteToRoadNet(proxy, schedulerId, session, null);
    				
    				schedulerUnload(proxy, schedulerId);    				
    			}
        	} catch (RoutingServiceException e) {
        		e.printStackTrace();
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL), (schedulerId!=null)?schedulerId.getArea().getAreaCode():""));
        		
    			throw new RoutingProcessException(strBuf.toString(),e,IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
    		}
        	return unassignedOrders;
        }
        
        private void schedulerUnload(RoutingEngineServiceProxy proxy, IRoutingSchedulerIdentity schedulerId) throws  RoutingProcessException {
        	    	
        	try {
        		if(RoutingServicesProperties.isRemoveSchedulerEnabled()) {
        			proxy.schedulerUnload(schedulerId);
        		}
        	} catch (RoutingServiceException e) {
        		e.printStackTrace();
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_REMOVEFROMSERVER_UNSUCCESSFUL), (schedulerId!=null)?schedulerId.getArea().getAreaCode():""));
        		
    			throw new RoutingProcessException(strBuf.toString()
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
        		strBuf.append(ROUTING_SUCCESS).append(SEPARATOR);
        		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_BALANCEROUTES_UNSUCCESSFUL), schedulerId.getArea().getAreaCode()));
        		throw new RoutingProcessException(strBuf.toString(),e,IIssue.EMPTY);
    		}
        }
        
        private String sendRouteToRoadNet(RoutingEngineServiceProxy proxy, IRoutingSchedulerIdentity schedulerId, 
        											IHandOffBatchSession session, String waveCode) throws  RoutingProcessException {
        	String sessionDescription = null;    	
        	try {    		
    			proxy.sendRoutesToRoadNet(schedulerId, session.getSessionName(), waveCode);
        	} catch (RoutingServiceException e) {
        		e.printStackTrace();
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(ROUTING_SUCCESS).append(SEPARATOR);
        		if(hadLoadBalance) {
        			strBuf.append(LOADBALANCE_SUCCESS).append(SEPARATOR);
        		}
        		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_SENDROUTES_UNSUCCESSFUL), schedulerId.getArea().getAreaCode()));
        		throw new RoutingProcessException(strBuf.toString(),e,IIssue.EMPTY);
    		} 
        	return sessionDescription;
        }
        
        private void saveUnassignedToRoadNet(IHandOffBatchSession session
        										, Map<IRoutingSchedulerIdentity, List> unassignedOrders) throws  RoutingProcessException {
        	
        	IRoutingSchedulerIdentity schedulerId = null;
        	try {
    	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
    	    	
    	    	List tmpUnassignedLst = null;
    			for(Map.Entry<IRoutingSchedulerIdentity, List> schUnEntry : unassignedOrders.entrySet()) {
    				schedulerId = schUnEntry.getKey();    				
    				tmpUnassignedLst = schUnEntry.getValue();
    				
    				if(tmpUnassignedLst != null && tmpUnassignedLst.size() > 0) {
    					    					
    					List dataList = proxy.saveUnassignedToRoadNet(schedulerId
    																	, proxy.retrieveRoutingSession(schedulerId, session.getSessionName())
    																	, tmpUnassignedLst);
    					if(dataList != null && dataList.size() > 0) {
    						throw new RoutingServiceException(null, IIssue.PROCESS_SENDUNASSIGNED_UNSUCCESSFUL);
    					}
    				}
    			}
        	} catch (RoutingServiceException e) {
        		e.printStackTrace();
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(ROUTING_SUCCESS).append(SEPARATOR);
        		if(hadLoadBalance) {
        			strBuf.append(LOADBALANCE_SUCCESS).append(SEPARATOR);
        		}
        		strBuf.append(SENDROUTES_SUCCESS).append(SEPARATOR);
        		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_SENDUNASSIGNED_UNSUCCESSFUL), schedulerId.getArea().getAreaCode()));
    			throw new RoutingProcessException(strBuf.toString(), e, IIssue.EMPTY);
    		}
        }
        
        private String getErrorMessage(String message, String areaCode) {
        	return message+" (Area: "+areaCode+")";
        }
    	
		@Override
		public void run() {
			execute();			
		}
		public Map.Entry<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> getSessionInfo() {
			return sessionInfo;
		}
		
		public Exception getException() {
			return exception;
		}
		
		public void setException(Exception exception) {
			this.exception = exception;
		}
		
    }
    
    private Map<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> 
    													groupOrdersForBatchRouting(ProcessContext context) 
    																throws ParseException {
    	
    	Map<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>> result =
			new HashMap<IHandOffBatchSession, Map<IRoutingSchedulerIdentity, List<IOrderModel>>>();
    	
    	String currentTime = getCurrentTime();
    	
    	Map<String, List<Integer>> countMapping = new HashMap<String, List<Integer>>();
    	
    	RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();
    	List<IRegionModel> regions = routingInfoProxy.getRegions();
    	for(IRegionModel region:regions)
    	{
    		List<Integer> cnt = new ArrayList<Integer>();
    		cnt.add(0);
    		countMapping.put(region.getRegionCode(),cnt);
    	}
    	
    	List<String> zones = routingInfoProxy.getStaticZonesByDate(context.getHandOffBatch().getDeliveryDate());
    	
    	String sessionDescription = null;
    	    	
    	Map<IRoutingSchedulerIdentity, List<IOrderModel>> schMapping = new HashMap<IRoutingSchedulerIdentity, List<IOrderModel>>();
    	if((List)context.getOrderList() != null) {
    		Iterator tmpIterator = ((List)context.getOrderList()).iterator();
			IOrderModel orderModel = null;
			IRoutingSchedulerIdentity schedulerId = null;
			
			while(tmpIterator.hasNext()) {
				orderModel = (IOrderModel)tmpIterator.next();
				schedulerId = getSchedulerId(null, orderModel, zones);
				
				if(!schMapping.containsKey(schedulerId)) {
					schMapping.put(schedulerId, new ArrayList<IOrderModel>());
				}
				schMapping.get(schedulerId).add(orderModel);
			}
    	}
    	
    	List unSortedMapping = new LinkedList(schMapping.entrySet());
    	
    	Collections.sort(unSortedMapping, new Comparator() {
    		public int compare(Object o1, Object o2) {
    			int sizeA = ((List)((Map.Entry) (o1)).getValue()).size();
    			int sizeB = ((List)((Map.Entry) (o2)).getValue()).size();
    			if( sizeA > sizeB) {
    				return 1;
    			} else if(sizeA == sizeB) {
    				return 0;
    			} else {
    				return -1;
    			}
    		}
    	});
		
		Map<IRoutingSchedulerIdentity, List<IOrderModel>> sortedSchMapping = new LinkedHashMap<IRoutingSchedulerIdentity, List<IOrderModel>>();
		
		for (Iterator it = unSortedMapping.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			sortedSchMapping.put((IRoutingSchedulerIdentity)entry.getKey(), (List<IOrderModel>)entry.getValue());
		}
		
		IHandOffBatchSession session = null;
		
		for(Map.Entry<IRoutingSchedulerIdentity, List<IOrderModel>> schEntry : sortedSchMapping.entrySet()) {
			List<Integer> currSize = countMapping.get(schEntry.getKey().getRegionId());
			if( currSize.get(currSize.size()-1) + schEntry.getValue().size() 
										> RoutingServicesProperties.getRoadnetSessionSizeThreshold()
										&& currSize.get(currSize.size()-1) > 0) {
				currSize.add(0);
			}
			
			sessionDescription = context.getUserId()+"_"+schEntry.getKey().getRegionId()+"_"+getSessionType(schEntry.getKey())+"_"
									+ RoutingDateUtil.formatPlain(schEntry.getKey().getDeliveryDate())+"_"+currentTime
																									 +"_"+currSize.size();
			session = new HandOffBatchSession(context.getHandOffBatch().getBatchId(), 
														sessionDescription, schEntry.getKey().getRegionId());
			if(!result.containsKey(session)) {
				result.put(session, new HashMap<IRoutingSchedulerIdentity, List<IOrderModel>>());
			}
			result.get(session).put(schEntry.getKey(), schEntry.getValue());
			int _tmpSize = currSize.remove(currSize.size()-1);
			currSize.add(_tmpSize + schEntry.getValue().size());			
		}
		
    	return result;
    	
    }


    private IRoutingSchedulerIdentity getSchedulerId(IRoutingSchedulerIdentity schedulerId, IOrderModel orderModel, List<String> zones) {

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
    	if(zones.contains(schedulerId.getArea().getAreaCode()))
		{
			schedulerId.setDynamic(false);
		}
		else
		{
			schedulerId.setDynamic(true);
		}
			
    	return schedulerId;
    }

    
    private String getSessionType(IRoutingSchedulerIdentity schedulerId) {
    	return (schedulerId != null && schedulerId.isDepot() ? "Depot":"Trucks");
    }
        

    private String getCurrentTime() {
    	try {
    		return RoutingDateUtil.getCurrentTime();
    	} catch (Exception e) {
    		return "000000";
    	}
    }
   
}
