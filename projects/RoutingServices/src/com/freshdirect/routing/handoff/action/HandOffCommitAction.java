package com.freshdirect.routing.handoff.action;

import static com.freshdirect.routing.manager.IProcessMessage.ERROR_MESSAGE_INELIGIBLECOMMIT;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_BATCHCOMMITCOMPLETED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchRoute;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.sap.bapi.BapiInfo;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffRouteIn;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffStopIn;
import com.freshdirect.sap.command.SapSendHandOff;

public class HandOffCommitAction extends AbstractHandOffAction {
	
	private String processResponse = null;
	
	private boolean force;
	
	public HandOffCommitAction(IHandOffBatch batch, String userId, boolean force) {
		super(batch, userId);
		this.force = force;
	}

	public Object doExecute() throws Exception {
		processResponse = null;
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		
		List<IHandOffBatchStop> handOffStops = proxy.getOrderByCutoff(this.getBatch().getDeliveryDate()
																, this.getBatch().getCutOffDateTime());
		Map<String, EnumSaleStatus> exceptions = new HashMap<String, EnumSaleStatus>();
		if(handOffStops != null) {
			for(IHandOffBatchStop stop : handOffStops) {
				if(stop.getErpOrderNumber() == null || stop.getErpOrderNumber().trim().length() == 0) {
					exceptions.put(stop.getOrderNumber(), stop.getStatus());
				}
			}
		}
		List<String> exceptionOrderIds = new ArrayList<String>();
		if(force && exceptions.size() > 0) {
			for(Map.Entry<String, EnumSaleStatus> exp : exceptions.entrySet()) {
				exceptionOrderIds.add(exp.getKey());
			}
		}
		List routes = proxy.getHandOffBatchRoutes(this.getBatch().getBatchId());
		List stops = proxy.getHandOffBatchStops(this.getBatch().getBatchId(), false);
		int noOfRoutes = routes != null ? routes.size() : 0;
		int noOfStops = stops != null ? stops.size() : 0;
		if(noOfRoutes == 0 || noOfStops == 0) {
			throw new RoutingServiceException("Error in route mappinge check cutoff report"
													+ " ,"+noOfRoutes + "Routes /"+noOfStops+" Stops" 
													, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);
		}
		Map<String, IHandOffBatchRoute> routeMapping = new HashMap<String, IHandOffBatchRoute>();
		Iterator<IHandOffBatchRoute> itr = routes.iterator();
		while(itr.hasNext()) {
			IHandOffBatchRoute route = itr.next();
			routeMapping.put(route.getRouteId(), route);
		}
		
		Iterator<IHandOffBatchStop> itrStop = stops.iterator();
		while( itrStop.hasNext()) {
			IHandOffBatchStop stop = itrStop.next();
			if(exceptions.containsKey(stop.getOrderNumber()) && !force) {
				return exceptions;
			}
			if(stop.getRouteId() == null || stop.getRouteId().trim().length() == 0
					|| !routeMapping.containsKey(stop.getRouteId())) {
				throw new RoutingServiceException("Error in route generation check cutoff report Stop No:"+stop.getOrderNumber()
														+ " ,"+noOfRoutes + "Routes /"+noOfStops+" Stops"
															, null, IIssue.PROCESS_HANDOFFBATCH_ERROR);				
			} else {
				IHandOffBatchRoute route = routeMapping.get(stop.getRouteId());
				if(route.getStops() == null) {
					route.setStops(new TreeSet());
				}
				if(!exceptions.containsKey(stop.getOrderNumber())) {
					route.getStops().add(stop);
				}
			}
		}
		if(exceptionOrderIds.size() > 0) {
			proxy.updateHandOffStopException(this.getBatch().getBatchId(), exceptionOrderIds);
		}
		boolean success = true;
		StringBuffer sapResponse = new StringBuffer();
		if(this.getBatch().isEligibleForCommit()) {
			
    		SapSendHandOff sapHandOffEngine = new SapSendHandOff((List<HandOffRouteIn>)routes
																	, (List<HandOffStopIn>)stops
																	, RoutingServicesProperties.getDefaultPlantCode()
																	, this.getBatch().getDeliveryDate()
																	, this.getBatch().getBatchId(), true);
			
			sapHandOffEngine.execute();
			
			BapiInfo[] bapiInfos = sapHandOffEngine.getBapiInfos();
			
			if(bapiInfos != null) {
				for (int i = 0; i < bapiInfos.length; i++) {
					
					BapiInfo bi = bapiInfos[i];
					sapResponse.append(bi.getMessage()).append("\n");
					if (BapiInfo.LEVEL_ERROR == bi.getLevel()) {
						success = false;
					}
					
					if(BapiInfo.LEVEL_INFO == bi.getLevel()) {
						if("ZWAVE/000".equals(bi.getCode()) && bi.getMessage().indexOf("PLEASE RETRY") >= 0){
							success = false;
						}
					}
				}
			} else {
				sapResponse.append("SAP response is empty");
			}
		} else {
			success = false;
			sapResponse.append(ERROR_MESSAGE_INELIGIBLECOMMIT);
		}
		processResponse = sapResponse.toString();
		if(success) {
			proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.COMPLETED);			
			proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_BATCHCOMMITCOMPLETED);		
		} else {
			proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.COMMMITFAILED);
			proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), sapResponse.toString());
		}
		proxy.addNewHandOffBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
											, EnumHandOffBatchActionType.COMMIT, this.getUserId());
		return null;
	}

	@Override
	public EnumHandOffBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumHandOffBatchStatus.COMMMITFAILED;
	}

	public String getProcessResponse() {
		return processResponse;
	}

	public void setProcessResponse(String processResponse) {
		this.processResponse = processResponse;
	}
	
}
