package com.freshdirect.routing.manager;

import static com.freshdirect.routing.manager.IProcessMessage.LOADBALANCE_SUCCESS;
import static com.freshdirect.routing.manager.IProcessMessage.ROUTING_SUCCESS;
import static com.freshdirect.routing.manager.IProcessMessage.SENDROUTES_SUCCESS;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.beanutils.BeanUtils;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.model.HandOffBatchSession;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchSession;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
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
    		
    		List<IOrderModel> orders = new ArrayList<IOrderModel>();
    		for(Map.Entry<IRoutingSchedulerIdentity, List<IOrderModel>> schEntry : sessionInfo.getValue().entrySet()) {
    			orders.addAll(schEntry.getValue());
    		}
    		try {
	    		//Save Location
	    		saveLocations(orders, sessionInfo.getKey().getRegion());
	    		
	    		//Purge Scheduler Instances
	    		purgeOrders(sessionInfo.getValue().keySet());
	    		
	    		if(RoutingServicesProperties.getRoutingBatchSyncEnabled()) {
	    			//Setup WaveInstances for DeliveryDate and CutOff and empty out resources for other cutoff
	    			setupWaveInstances(context.getHandOffBatch(), sessionInfo.getValue().keySet());
	    		}
	    		//Bulk Reserve and Other Child Processes
	    		Map<IRoutingSchedulerIdentity, List> unassignedOrders = schedulerBulkReserveOrders(sessionInfo.getValue().keySet()
	    																		, sessionInfo.getValue()
	    																		, (IServiceTimeScenarioModel)context.getProcessScenario()
	    																		, sessionInfo.getKey());
	    		
	    		// Save Unassigned to RoadNet
	    		saveUnassignedToRoadNet(sessionInfo.getKey(), unassignedOrders);
	    		    		 
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
    		System.out.println("\n################### ROUTING END "+ sessionInfo.getKey().getSessionName() +"##################");
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
    				proxy.purgeBatchOrders(schedulerId, true); //Changed to reload xml option
    			}
        	} catch (RoutingServiceException e) {
        		e.printStackTrace();
    			throw new RoutingProcessException(null,e,IIssue.PROCESS_PURGEORDERS_UNSUCCESSFUL);
    		}
        }
        
        private void setupWaveInstances(IHandOffBatch handOffBatch, Set schedulerIdLst) throws  RoutingProcessException {

        	try {
    	    	RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
    	    	CapacityEngineServiceProxy capacityProxy = new CapacityEngineServiceProxy();
    			RoutingInfoServiceProxy routeInfoProxy = new RoutingInfoServiceProxy();
    			
    			Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> waveInstanceTree = routeInfoProxy
    																													.getWaveInstanceTree(handOffBatch.getDeliveryDate(), null);
    			RoutingTimeOfDay cutOff = new RoutingTimeOfDay(handOffBatch.getCutOffDateTime());
    			if(waveInstanceTree != null) {																																
	    	    	Iterator tmpIterator = schedulerIdLst.iterator();
	    	    	IRoutingSchedulerIdentity schedulerId = null;
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
    			throw new RoutingProcessException(null,e,IIssue.PROCESS_SAVEWAVEINSTANCE_UNSUCCESSFUL);
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
    				
    				sendRouteToRoadNet(proxy, schedulerId, session);
    				
    				schedulerRemoveFromServer(proxy, schedulerId);    				
    			}
        	} catch (RoutingServiceException e) {
        		e.printStackTrace();
    			throw new RoutingProcessException(getErrorMessage("", schedulerId.getArea().getAreaCode())
    													,e,IIssue.PROCESS_BULKRESERVE_UNSUCCESSFUL);
    		}
        	return unassignedOrders;
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
        		strBuf.append(ROUTING_SUCCESS).append(SEPARATOR);
        		strBuf.append(this.getErrorMessage(Issue.getMessage(IIssue.PROCESS_BALANCEROUTES_UNSUCCESSFUL), schedulerId.getArea().getAreaCode()));
        		throw new RoutingProcessException(strBuf.toString(),e,IIssue.EMPTY);
    		}
        }
        
        private String sendRouteToRoadNet(RoutingEngineServiceProxy proxy, IRoutingSchedulerIdentity schedulerId, 
        											IHandOffBatchSession session) throws  RoutingProcessException {
        	String sessionDescription = null;    	
        	try {    		
    			proxy.sendRoutesToRoadNet(schedulerId, session.getSessionName());
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
    	List<Integer> regularCnt = new ArrayList<Integer>();
    	regularCnt.add(0);
    	
    	List<Integer> depotCnt = new ArrayList<Integer>();
    	depotCnt.add(0);
    	
    	countMapping.put("FD", regularCnt);
    	countMapping.put("MDP", depotCnt);
    	
    	String sessionDescription = null;
    	    	
    	Map<IRoutingSchedulerIdentity, List<IOrderModel>> schMapping = new HashMap<IRoutingSchedulerIdentity, List<IOrderModel>>();
    	if((List)context.getOrderList() != null) {
    		Iterator tmpIterator = ((List)context.getOrderList()).iterator();
			IOrderModel orderModel = null;
			IRoutingSchedulerIdentity schedulerId = null;
			
			while(tmpIterator.hasNext()) {
				orderModel = (IOrderModel)tmpIterator.next();
				schedulerId = getSchedulerId(null, orderModel);
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
			
			sessionDescription = context.getUserId()+"_"+getSessionType(schEntry.getKey())+"_"
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
