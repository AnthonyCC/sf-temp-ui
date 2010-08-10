package com.freshdirect.routing.handoff.action;

import static com.freshdirect.routing.manager.IProcessMessage.ERROR_MESSAGE_NOORDER;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_ROUTINGINCOMPLETED;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_ROUTINGINPROGRESS;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_ROUTINGINDATACOLLECTIONPROGRESS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.manager.DeliveryManager;
import com.freshdirect.routing.manager.EstimationManager;
import com.freshdirect.routing.manager.GeographyManager;
import com.freshdirect.routing.manager.HandOffProcessManager;
import com.freshdirect.routing.manager.IProcessManager;
import com.freshdirect.routing.manager.PlantPackagingManager;
import com.freshdirect.routing.manager.ProcessContext;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchStop;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.util.IRoutingParamConstants;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class HandOffRoutingInAction extends AbstractHandOffAction {
		
	public HandOffRoutingInAction(IHandOffBatch batch, String userId) {
		super(batch, userId);
		// TODO Auto-generated constructor stub
	}

	public void doExecute() throws Exception {
		
		RoutingInTask task = new RoutingInTask();
		Thread routingInThread = new Thread(task, "RoutingInTask");
		routingInThread.start();
	}
	
	private class RoutingInTask  implements Runnable {

		@Override
		public void run() {
			
			HandOffServiceProxy proxy = new HandOffServiceProxy();
			Map paramMap = new HashMap();
		    paramMap.put(IRoutingParamConstants.SERVICETIME_SCENARIO, HandOffRoutingInAction.this.getBatch().getServiceTimeScenario());
		    paramMap.put(IRoutingParamConstants.ROUTING_USER, HandOffRoutingInAction.this.getUserId());
		    
		    System.out.println("######################### RoutingInTask START #########################");
		    try {
		    	
		    	proxy.clearHandOffBatch(HandOffRoutingInAction.this.getBatch().getBatchId());
		    	proxy.updateHandOffBatchCommitEligibility(HandOffRoutingInAction.this.getBatch().getBatchId()
		    												, !RoutingServicesProperties.getRoutingCutOffStandByEnabled());
		    	proxy.updateHandOffBatchStatus(HandOffRoutingInAction.this.getBatch().getBatchId(), EnumHandOffBatchStatus.PROCESSING);
				proxy.addNewHandOffBatchAction(HandOffRoutingInAction.this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
						, EnumHandOffBatchActionType.ROUTEIN, HandOffRoutingInAction.this.getUserId());
		    	List<IHandOffBatchStop> inputDataList = HandOffRoutingInAction.this.getBatch().getStops();
		    	proxy.updateHandOffBatchMessage(HandOffRoutingInAction.this.getBatch().getBatchId(), INFO_MESSAGE_ROUTINGINDATACOLLECTIONPROGRESS);
		    	
		    	if(inputDataList == null) {
		    		inputDataList = proxy.getOrderByCutoff(HandOffRoutingInAction.this.getBatch().getDeliveryDate()
		    													, HandOffRoutingInAction.this.getBatch().getCutOffDateTime());
		    	}
			    System.out.println("######################### NO OF ORDERS ######################### "+inputDataList.size());
				if(inputDataList != null && inputDataList.size() > 0) {
															
					IProcessManager rootProcessMgr = getRoutingInProcessChain();
					ProcessContext context = new ProcessContext();
					context.setHandOffBatchId(HandOffRoutingInAction.this.getBatch().getBatchId());
					context.setUserId(HandOffRoutingInAction.this.getUserId());
					
					List outputDataList = new ArrayList();
					
					fillBatchInfo(context, inputDataList);
					context.setDeliveryTypeCache(new HashMap());
					context.addProcessParam(paramMap);
					
					HandOffProcessManager handOffProcessMgr  = new HandOffProcessManager();
					//Start the process which will initialize the context which cache information
					handOffProcessMgr.startProcess(context);
					
					for(IHandOffBatchStop orderModel : inputDataList) {
						
						orderModel.setBatchId(HandOffRoutingInAction.this.getBatch().getBatchId());
						context.setDataModel(orderModel);
						rootProcessMgr.process(context);
						context.addOrder(context.getDataModel());
						outputDataList.add(context.getDataModel());
					}
					
					proxy.updateHandOffBatchMessage(HandOffRoutingInAction.this.getBatch().getBatchId(), INFO_MESSAGE_ROUTINGINPROGRESS);
					//End the process which will complete the routing sequence
					handOffProcessMgr.endProcess(context);
					
					// Logging routing in complete
					proxy.updateHandOffBatchStatus(HandOffRoutingInAction.this.getBatch().getBatchId(), EnumHandOffBatchStatus.ROUTINGCOMPETE);
					proxy.updateHandOffBatchMessage(HandOffRoutingInAction.this.getBatch().getBatchId(), INFO_MESSAGE_ROUTINGINCOMPLETED);
					proxy.addNewHandOffBatchStops((List)context.getOrderList());					
				} else {
					proxy.updateHandOffBatchStatus(HandOffRoutingInAction.this.getBatch().getBatchId(), EnumHandOffBatchStatus.ROUTINGFAILED);
					proxy.updateHandOffBatchMessage(HandOffRoutingInAction.this.getBatch().getBatchId(), ERROR_MESSAGE_NOORDER);
				}
		    } catch (Exception exp) {
		    	exp.printStackTrace();
		    	try {
					proxy.updateHandOffBatchStatus(HandOffRoutingInAction.this.getBatch().getBatchId(), EnumHandOffBatchStatus.ROUTINGFAILED);
					proxy.updateHandOffBatchMessage(HandOffRoutingInAction.this.getBatch().getBatchId(), exp.getMessage());
				} catch (RoutingServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    }
		    System.out.println("######################### RoutingInTask END #########################");
		}
		
		private void fillBatchInfo(ProcessContext context, List<IHandOffBatchStop>  dataList) {
									
			List outputOrderList = new ArrayList();
			List batchOrderList = new ArrayList();
				
			int intCount = 0;
			int batchCount = 500;
			for(IHandOffBatchStop tmpInputModel : dataList) {
				batchOrderList.add(tmpInputModel.getOrderNumber());
				intCount++;
				if(intCount == batchCount) {				
					outputOrderList.add(batchOrderList);
					batchOrderList = new ArrayList();
					intCount = 0;
				}
			}
						
			if(batchOrderList.size() > 0) {
				outputOrderList.add(batchOrderList);
			}	
			context.setOrderIdLst(outputOrderList);
		}		
	}
	
	private IProcessManager getRoutingInProcessChain() {
		
		IProcessManager geoManager = new GeographyManager();
		IProcessManager estimationManager = new EstimationManager();
		IProcessManager packagingManager = new PlantPackagingManager();
		IProcessManager deliveryManager = new DeliveryManager();
	   
		packagingManager.setSuccessor(geoManager);
		geoManager.setSuccessor(deliveryManager);
		deliveryManager.setSuccessor(estimationManager);
		
		return packagingManager;
	}

	@Override
	public EnumHandOffBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumHandOffBatchStatus.ROUTINGFAILED;
	}

}
