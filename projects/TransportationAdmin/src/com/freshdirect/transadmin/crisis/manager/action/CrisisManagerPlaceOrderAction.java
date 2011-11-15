package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.ERROR_MESSAGE_TIMESLOTEXCEPTION;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_PLACESTANDINGORDERPROGRESS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.routing.model.StandingOrderModel;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
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

		if (foundExceptions != null && foundExceptions.size() > 0) {
			this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
			this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId()
																				,ERROR_MESSAGE_TIMESLOTEXCEPTION);
			return foundExceptions;
		}

		
		this.unBlockDeliveryCapacity(true);			
		try{
			List<StandingOrderModel> standingOrderResult = CrisisManagerUtil.placeStandingOrders(this.standingOrders, this.getUserId());
		    	
		    if(standingOrderResult != null && standingOrderResult.size() > 0){
		    	this.getCrisisMngService().updateCrisisMngBatchStandingOrder(this.getBatch().getBatchId(), standingOrderResult);	    		
		    	StringBuffer errorBuf = new StringBuffer();	    		
		    	for(StandingOrderModel model : standingOrderResult){	    			
			   		if(errorBuf.length() > 0) { errorBuf.append(", "); }		    		
		    		if(model.getErrorHeader() != null && !"".equalsIgnoreCase(model.getErrorHeader()))
		    			errorBuf.append(model.getId()).append(" - ").append(model.getErrorHeader());
		    	} 		
		    	    		
		    	if(errorBuf.toString().length() > 0){
		    		throw new TransAdminServiceException("Standing Order failures: "+ errorBuf.toString()    		
						, null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		    	}
		    }
		} catch(Exception ex){
			throw new TransAdminServiceException("Standing Order failure: "+ ex.toString()    		
					, null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
		List<ICrisisMngBatchOrder> batchOrders = this.getCrisisMngService().getCrisisMngBatchStandingOrder(this.getBatch().getBatchId(), false, false);
		Iterator<ICrisisMngBatchOrder> _orderItr = batchOrders.iterator();
		int orderCount = 0;
		while(_orderItr.hasNext()){
			ICrisisMngBatchOrder _order = _orderItr.next();
			if(_order.getStatus() == null || "FAILURE".equals(_order.getStatus())){
				orderCount++;
			}
	    }
		if(orderCount > 0){
			this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PLACESOCOMPETE);
		    this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED);
		} else {
			this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.COMPLETED);
		    this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_PLACESTANDINGORDERCOMPLETED);
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
