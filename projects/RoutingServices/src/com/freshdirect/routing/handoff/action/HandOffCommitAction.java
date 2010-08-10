package com.freshdirect.routing.handoff.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
import com.freshdirect.sap.ejb.SapException;

import static com.freshdirect.routing.manager.IProcessMessage.*;

public class HandOffCommitAction extends AbstractHandOffAction {
		
	public HandOffCommitAction(IHandOffBatch batch, String userId) {
		super(batch, userId);
	}

	public void doExecute() throws Exception {
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		List routes = proxy.getHandOffBatchRoutes(this.getBatch().getBatchId());
		List stops = proxy.getHandOffBatchStops(this.getBatch().getBatchId());
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
				route.getStops().add(stop);
			}
		}
		
		SapSendHandOff sapHandOffEngine = new SapSendHandOff((List<HandOffRouteIn>)routes
																, (List<HandOffStopIn>)stops
																, RoutingServicesProperties.getDefaultPlantCode()
																, this.getBatch().getDeliveryDate()
																, this.getBatch().getBatchId(), true);
		if(this.getBatch().isEligibleForCommit()) {
			sapHandOffEngine.execute();
			
			BapiInfo[] bapiInfos = null;//sapHandOffEngine;

			boolean success = true;
			boolean sapShutingdown = false;
			for (int i = 0; i < bapiInfos.length; i++) {
				BapiInfo bi = bapiInfos[i];
				if (BapiInfo.LEVEL_ERROR == bi.getLevel()) {
					success = false;
				}
				
				if(BapiInfo.LEVEL_INFO == bi.getLevel()) {
					if("ZWAVE/000".equals(bi.getCode()) && bi.getMessage().indexOf("PLEASE RETRY") >= 0){
						sapShutingdown = true;
					}
				}
			}
			
			if(sapShutingdown) {
				throw new RuntimeException("SAP is Shutting down");
			}
			

			if (!success) {

				StringBuffer buf = new StringBuffer("BAPI task failed in SAP. Return structure:");
				for (int i = 0; i < bapiInfos.length; i++) {
					buf.append('\n').append(bapiInfos[i].toString());
				}

				throw new SapException(buf.toString());
			}
		}
		
		proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.COMPLETED);
		proxy.addNewHandOffBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
				, EnumHandOffBatchActionType.COMMIT, this.getUserId());
		proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_BATCHCOMMITCOMPLETED);		
		
	}

	@Override
	public EnumHandOffBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumHandOffBatchStatus.COMMMITFAILED;
	}
	
}
