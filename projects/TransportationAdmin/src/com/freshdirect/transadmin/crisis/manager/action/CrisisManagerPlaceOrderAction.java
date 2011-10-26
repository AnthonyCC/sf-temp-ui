package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERPROGRESS;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED;

import java.util.List;

import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.IStandingOrderModel;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.exception.IIssue;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.CrisisManagerUtil;

public class CrisisManagerPlaceOrderAction extends AbstractCrisisManagerAction {
	
	private List<IStandingOrderModel> standingOrders;
	
	public CrisisManagerPlaceOrderAction(ICrisisManagerBatch batch,
			String userId, List<IStandingOrderModel> standingOrders, ICrisisManagerService  crisisMngService) {
		super(batch, userId, crisisMngService);	
		this.standingOrders = standingOrders;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object doExecute() throws Exception {
				
		System.out.println("######################### Place Standing Order START #########################");
		
		this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
		this.getCrisisMngService().addNewCrisisMngBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
					, EnumCrisisMngBatchActionType.PLACEORDER, this.getUserId());
	    	
		this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_PLACESTANDINGORDERPROGRESS);
	   
	    CrisisManagerUtil orderMngAgent = new CrisisManagerUtil();
	   	orderMngAgent.setAgent(this.getUserId());
	    orderMngAgent.setOrders(this.standingOrders);
	    List<IStandingOrderModel> standingOrderResult = orderMngAgent.placeStandingOrders();
	    	
	    if(standingOrderResult != null && standingOrderResult.size() > 0){
	    	this.getCrisisMngService().updateCrisisMngBatchStandingOrder(this.getBatch().getBatchId(), standingOrderResult);	    		
	    	StringBuffer errorBuf = new StringBuffer();	    		
	    	for(IStandingOrderModel model : standingOrderResult){	    			
		   		if(errorBuf.length() > 0) { errorBuf.append(", "); }		    		
	    		if(model.getErrorHeader() != null && !"".equalsIgnoreCase(model.getErrorHeader()))
	    			errorBuf.append(model.getId()).append(" - ").append(model.getErrorHeader());
	    	} 		
	    	    		
	    	if(errorBuf.toString().length() > 0){
	    		throw new TransAdminServiceException("Standing Order failures: "+ errorBuf.toString()    		
					, null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
	    	}
	    }		
		
	    this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.COMPLETED);
	    this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED);
	
		System.out.println("######################### Place Standing Order STOP #########################");
		return null;
	}

	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.PLACESOFAILED;
	}

}
