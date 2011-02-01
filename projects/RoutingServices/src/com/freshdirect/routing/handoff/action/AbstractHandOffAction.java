package com.freshdirect.routing.handoff.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumHandOffDispatchStatus;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public abstract class AbstractHandOffAction {
	
	private IHandOffBatch batch;
	
	private String userId;
	
	private NumberFormat formatter = new DecimalFormat("00");
	
	public AbstractHandOffAction(IHandOffBatch batch, String userId) {
		super();
		this.batch = batch;
		this.userId = userId;
	}
	
	public IHandOffBatch getBatch() {
		return batch;
	}

	public void setBatch(IHandOffBatch batch) {
		this.batch = batch;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static int getRouteIndex(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if(dataLst != null && dataLst.length >1) {
				return Integer.parseInt(dataLst[1]);
			} 
		} catch(Exception e) {
			//do nothing
		}
		return 0;
	}
	
	public static String splitStringForCode(String search) {
		String[] dataLst = StringUtils.split(search, "-");
		if(dataLst != null && dataLst.length >0) {
			return dataLst[0];
		} else {
			return "000";
		}
	}
	
	public static int splitStringForValue(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if(dataLst != null && dataLst.length >1) {
				return Integer.parseInt(dataLst[1]);
			} 
		} catch(Exception e) {
			//do nothing
		}
		return 0;
	}
	
	public static String getRouteArea(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if(dataLst != null && dataLst.length >1) {
				return dataLst[0];
			} 
		} catch(Exception e) {
			//do nothing
		}
		return null;
	}
	
	public String formatRouteNumber(int input) {
		return formatter.format(input);
	}
	
	protected DispatchCorrelationResult correlateDispatch(List<IHandOffBatchRoute> routes
															, Map<String, IAreaModel> areaLookup) throws RoutingServiceException {
		
		RoutingTimeOfDay rCutOff = new RoutingTimeOfDay(this.getBatch().getCutOffDateTime());
		DispatchCorrelationResult result = new DispatchCorrelationResult();
		RoutingInfoServiceProxy routingInfoProxy = new RoutingInfoServiceProxy();
		
		//Map<ZoneCode, Map<DispatchTime, Map<CutOffTime, IWaveInstance>>>
		Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> plannedDispatchTree = null;
		
		if(RoutingServicesProperties.getRoutingBatchSyncEnabled()) {
			Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> waveInstanceTree = routingInfoProxy
																										.getWaveInstanceTree(this.getBatch().getDeliveryDate(), null);

			if(waveInstanceTree != null && waveInstanceTree.keySet().size() > 0) {
				plannedDispatchTree = waveInstanceTree.get(waveInstanceTree.keySet().toArray()[0]);
			}
		} else {
			plannedDispatchTree = routingInfoProxy.getPlannedDispatchTree(this.getBatch().getDeliveryDate());
		}
		
		List<IHandOffBatchRoute> mismatchRoutes = new ArrayList<IHandOffBatchRoute>();
				
		if(routes != null && areaLookup != null && plannedDispatchTree != null) {
			for(IHandOffBatchRoute routeModel : routes) {
				boolean foundWave = false;
				IAreaModel areaModel = areaLookup.get(routeModel.getArea());
				
				if(areaModel != null ) {					
					Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> dispatchMapping = plannedDispatchTree.get(routeModel.getArea());
					
					if(dispatchMapping != null) {
						for(Map.Entry<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> dispEntry : dispatchMapping.entrySet()) {
							List<IWaveInstance> waveInstances = dispEntry.getValue().get(rCutOff);
					
							if(waveInstances != null) {
								for(IWaveInstance waveInstance : waveInstances) {
									
									if(areaModel.isDepot()) {
										
										if(waveInstance.getDispatchTime() != null 
												&& waveInstance.getDispatchTime()
															.equals(new RoutingTimeOfDay(routeModel.getStartTime()))) {
											foundWave = true;
											routeModel.copyWaveProperties(waveInstance);
										}
									} else {
										if(waveInstance.getWaveStartTime() != null 
												&& waveInstance.getWaveStartTime().equals(new RoutingTimeOfDay(routeModel.getStartTime()))
												&& waveInstance.getPreferredRunTime() == routeModel.getPreferredRunTime()
												&& waveInstance.getMaxRunTime() == routeModel.getMaxRunTime()) {
											foundWave = true;
											routeModel.copyWaveProperties(waveInstance);
										}
									}
								}
							}
						}
					}					
				}
				if(!foundWave) {
					mismatchRoutes.add(routeModel);
				}
			}
		}
		
		Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus = new TreeMap<RoutingTimeOfDay, EnumHandOffDispatchStatus>();
		if(plannedDispatchTree != null) {
			for(Map.Entry<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>> areaEntry : plannedDispatchTree.entrySet()) {
				for(Map.Entry<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>> dispEntry : areaEntry.getValue().entrySet()) {
					for(Map.Entry<RoutingTimeOfDay, List<IWaveInstance>> cutOffEntry : dispEntry.getValue().entrySet()) {
						if(!dispatchStatus.containsKey(dispEntry.getKey())) {
							dispatchStatus.put(dispEntry.getKey(), EnumHandOffDispatchStatus.COMPLETE);
						}
						if(cutOffEntry.getKey().after(rCutOff)) {
							dispatchStatus.put(dispEntry.getKey(), EnumHandOffDispatchStatus.PENDING);
						}									
					}
				}
			}
		}
		result.setDispatchStatus(dispatchStatus);
		result.setMismatchRoutes(mismatchRoutes);
		return result;
	}
	
	protected DispatchCorrelationResult checkRouteMismatch(List<IHandOffBatchRoute> routes, Map<String, IAreaModel> areaLookup) throws RoutingServiceException {
		
		DispatchCorrelationResult correlationResult = this.correlateDispatch(routes, areaLookup);
		if(correlationResult != null && correlationResult.getMismatchRoutes() != null 
											&& correlationResult.getMismatchRoutes().size() > 0) {
			StringBuffer errorBuf = new StringBuffer(); 
			for(IHandOffBatchRoute mismatchRoute : correlationResult.getMismatchRoutes()) {
				if(errorBuf.length() > 0) {
					errorBuf.append(",");
				}
				errorBuf.append(mismatchRoute.getRouteId()).append(mismatchRoute.getRoutingRouteId());
			}
			throw new RoutingServiceException("Plan Mismatch "+ errorBuf.toString() 
					, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
		return correlationResult;
	}
	
	public Object execute() {
		long startTime = System.currentTimeMillis();
		try {
			return doExecute();
		} catch (Exception exp) {
			HandOffServiceProxy proxy = new HandOffServiceProxy();
			exp.printStackTrace();
			
			try {
				if(getFailureStatus() != null) {
					proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
					proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), 
														(exp.getMessage() != null ? exp.getMessage() : exp.toString()));
				}
			} catch (RoutingServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		long endTime = System.currentTimeMillis();
		System.out.println("HandOffAction "+this.getClass().getName()+" completed in"+ ((endTime - startTime)/60) +" secs");
		return null;
	}
	
	class DispatchCorrelationResult {
		
		List<IHandOffBatchRoute> mismatchRoutes;
		// DispatchTime vs DispatchStatus
		Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus = null;
		
		public List<IHandOffBatchRoute> getMismatchRoutes() {
			return mismatchRoutes;
		}
		public Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> getDispatchStatus() {
			return dispatchStatus;
		}
		public void setMismatchRoutes(List<IHandOffBatchRoute> mismatchRoutes) {
			this.mismatchRoutes = mismatchRoutes;
		}
		public void setDispatchStatus(Map<RoutingTimeOfDay, EnumHandOffDispatchStatus> dispatchStatus) {
			this.dispatchStatus = dispatchStatus;
		}
	}
	
	public abstract Object doExecute() throws Exception;
	
	public abstract EnumHandOffBatchStatus getFailureStatus();
	
	
}
