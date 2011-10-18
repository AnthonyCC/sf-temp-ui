package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERPROGRESS;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED;

import java.util.List;

import com.freshdirect.routing.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.routing.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.IStandingOrderModel;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.CrisisManagerServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.util.CrisisManagerUtil;

public class CrisisManagerPlaceOrderAction extends AbstractCrisisManagerAction {
	
	private List<IStandingOrderModel> standingOrders;
	
	public CrisisManagerPlaceOrderAction(ICrisisManagerBatch batch,
			String userId, List<IStandingOrderModel> standingOrders) {
		super(batch, userId);
		this.standingOrders = standingOrders;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object doExecute() throws Exception {
		
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
		System.out.println("######################### Place Standing Order START #########################");
		
		proxy.updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
	    proxy.addNewCrisisMngBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
					, EnumCrisisMngBatchActionType.PLACEORDER, this.getUserId());
	    	
	    proxy.updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_PLACESTANDINGORDERPROGRESS);
	   
	    CrisisManagerUtil orderMngAgent = new CrisisManagerUtil();
	   	orderMngAgent.setAgent(this.getUserId());
	    orderMngAgent.setOrders(this.standingOrders);
	    List<IStandingOrderModel> standingOrderResult = orderMngAgent.placeStandingOrders();
	    	
	    if(standingOrderResult != null && standingOrderResult.size() > 0){
	    	proxy.updateCrisisMngBatchStandingOrder(this.getBatch().getBatchId(), standingOrderResult);	    		
	    	StringBuffer errorBuf = new StringBuffer();	    		
	    	for(IStandingOrderModel model : standingOrderResult){	    			
		   		if(errorBuf.length() > 0) { errorBuf.append(", "); }		    		
	    		if(model.getErrorHeader() != null && !"".equalsIgnoreCase(model.getErrorHeader()))
	    			errorBuf.append(model.getId()).append(" - ").append(model.getErrorHeader());
	    	} 		
	    	    		
	    	if(errorBuf.toString().length() > 0){
	    		throw new RoutingServiceException("Standing Order failures: "+ errorBuf.toString()    		
					, null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
	    	}
	    }		
		
		proxy.updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.COMPLETED);
		proxy.updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED);
	
		System.out.println("######################### Place Standing Order STOP #########################");
		return null;
	}

	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.PLACESOFAILED;
	}

}
