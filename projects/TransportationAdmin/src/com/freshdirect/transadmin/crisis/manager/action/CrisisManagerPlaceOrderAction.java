package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERPROGRESS;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERMSG;

import java.util.Iterator;
import java.util.List;

import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.routing.model.StandingOrderModel;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.exception.IIssue;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.CrisisManagerUtil;

public class CrisisManagerPlaceOrderAction extends AbstractCrisisManagerAction {
	
	private List<StandingOrderModel> standingOrders;

	public CrisisManagerPlaceOrderAction(ICrisisManagerBatch batch,
			String userId, List<StandingOrderModel> standingOrders, ICrisisManagerService  crisisMngService) {
		super(batch, userId, crisisMngService);	
		this.standingOrders = standingOrders;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object doExecute() throws Exception {
				
			System.out.println("######################### Place Standing Order START #########################");
			StringBuffer errorBuf = new StringBuffer();
			this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
			this.getCrisisMngService().addNewCrisisMngBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
						, EnumCrisisMngBatchActionType.PLACEORDER, this.getUserId());
		    	
			this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_PLACESTANDINGORDERPROGRESS);
		   
			this.unBlockDeliveryCapacity(true);			
			
			List<StandingOrderModel> standingOrderResult = CrisisManagerUtil.placeStandingOrders(this.standingOrders, this.getUserId());
				    	
			if(standingOrderResult != null && standingOrderResult.size() > 0){
			 	this.getCrisisMngService().updateCrisisMngBatchStandingOrder(this.getBatch().getBatchId(), standingOrderResult);	    		
			       		
			   	for(StandingOrderModel model : standingOrderResult){	    			
				if(errorBuf.length() > 0) { errorBuf.append(", "); }		    		
				if(model.getErrorHeader() != null && !"".equalsIgnoreCase(model.getErrorHeader()))
			    	errorBuf.append(model.getId()).append(" - ").append(model.getErrorHeader());
			    }
			}
			this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PLACESOCOMPLETE);
			this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId()
			    		, errorBuf.toString().length() > 0 ? INFO_MESSAGE_PLACESTANDINGORDERMSG : INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED);
			
		System.out.println("######################### Place Standing Order STOP #########################");
		return null;
	}

	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.PLACESOFAILED;
	}

}
