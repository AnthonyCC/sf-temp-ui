package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.ERROR_MESSAGE_TIMESLOTEXCEPTION;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERPROGRESS;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERFAILED;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.exception.IIssue;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.CrisisManagerUtil;

public class CrisisManagerPlaceOrderAction extends AbstractCrisisManagerAction {
	
	private List<ICrisisMngBatchOrder> standingOrders;
	private boolean isExceptionCheck;
	
	public CrisisManagerPlaceOrderAction(ICrisisManagerBatch batch,
			String userId, List<ICrisisMngBatchOrder> standingOrders, boolean isExceptionCheck, ICrisisManagerService  crisisMngService) {
		super(batch, userId, crisisMngService);	
		this.standingOrders = standingOrders;
		this.isExceptionCheck = isExceptionCheck;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object doExecute() throws Exception {
				
		Map<String, Integer> foundExceptions = new HashMap<String, Integer>();
		
		System.out.println("######################### Place Standing Order START #########################");
		
		this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
		this.getCrisisMngService().addNewCrisisMngBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
					, EnumCrisisMngBatchActionType.PLACEORDER, this.getUserId());
	    	
		this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_PLACESTANDINGORDERPROGRESS);
	   
		Map<String, List<ICrisisManagerBatchDeliverySlot>> exceptionSlots 
																= this.getCrisisMngService().getCrisisMngBatchTimeslot(this.getBatch().getBatchId(), false);
		if (exceptionSlots.size() > 0) {
			for (Map.Entry<String, List<ICrisisManagerBatchDeliverySlot>> exceptionEntry : exceptionSlots.entrySet()) {

				for (ICrisisManagerBatchDeliverySlot _slot : exceptionEntry.getValue()) {
					if (!foundExceptions.containsKey(_slot.getArea())) {
						foundExceptions.put(_slot.getArea(), 0);
					}
					foundExceptions.put(_slot.getArea(), foundExceptions.get(
							_slot.getArea()).intValue() + 1);
				}
			}
		}

		if (!isExceptionCheck && foundExceptions.size() > 0) {
			this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
			this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId()
																				,ERROR_MESSAGE_TIMESLOTEXCEPTION);
			return foundExceptions;
		}

		if (foundExceptions.size() == 0 && isExceptionCheck) {
		
		    CrisisManagerUtil orderMngAgent = new CrisisManagerUtil();
		   	orderMngAgent.setAgent(this.getUserId());
		    orderMngAgent.setOrders(this.standingOrders);
		    List<ICrisisMngBatchOrder> standingOrderResult = orderMngAgent.placeStandingOrders();
		    	
		    if(standingOrderResult != null && standingOrderResult.size() > 0){
		    	this.getCrisisMngService().updateCrisisMngBatchStandingOrder(this.getBatch().getBatchId(), standingOrderResult);	    		
		    	StringBuffer errorBuf = new StringBuffer();	    		
		    	for(ICrisisMngBatchOrder model : standingOrderResult){	    			
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
		} else{
			this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
			this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_PLACESTANDINGORDERFAILED);			
		}
	    
		System.out.println("######################### Place Standing Order STOP #########################");
		return null;
	}

	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.PLACESOFAILED;
	}

}
