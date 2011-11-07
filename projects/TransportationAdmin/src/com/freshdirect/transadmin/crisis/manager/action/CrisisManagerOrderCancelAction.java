package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_ORDERCANCELPROGRESS;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_ORDERCANCELCOMPLETED;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_REGULARORDERCANCELCOMPLETED;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchType;
import com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.CrisisManagerUtil;

public class CrisisManagerOrderCancelAction extends AbstractCrisisManagerAction {
	
	private final static Logger LOGGER = LoggerFactory.getInstance(CrisisManagerOrderCancelAction.class);
	private List<String> cancelOrders;
	
	public CrisisManagerOrderCancelAction(ICrisisManagerBatch batch, String userId, List<String> cancelOrders, ICrisisManagerService  crisisMngService) {
		super(batch, userId, crisisMngService);
		this.cancelOrders = cancelOrders;
	}

	private ICrisisManagerService getService(){ 
		return CrisisManagerOrderCancelAction.this.getCrisisMngService();
	}
	
	private ICrisisManagerBatch getProcess(){ 
		return CrisisManagerOrderCancelAction.this.getBatch();
	}
	
	@Override
	public Object doExecute() throws Exception {
		OrderCancelTask task = new OrderCancelTask();
		Thread orderCancelThread = new Thread(task, "OrderCancelTask");
		orderCancelThread.start();		
		return null;
	}
	
	private class OrderCancelTask implements Runnable {
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
					
			List<String> exceptionOrderIds = null;
			List<String> messages = new ArrayList<String>();			
			List<String> exceptions = new ArrayList<String>();
			
			System.out.println("########### CancelOrderTask START ############");
		    try {
		    	CrisisManagerUtil orderMngAgent = new CrisisManagerUtil();
		    	orderMngAgent.setAgent(CrisisManagerOrderCancelAction.this.getUserId());		    		
		    	orderMngAgent.setOrders(CrisisManagerOrderCancelAction.this.cancelOrders);
		    	
		    	getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
		    	getService().addNewCrisisMngBatchAction(getProcess().getBatchId(), RoutingDateUtil.getCurrentDateTime()
						, EnumCrisisMngBatchActionType.ORDERCANCEL, CrisisManagerOrderCancelAction.this.getUserId());
		    	
		    	getService().updateCrisisMngBatchMessage(getProcess().getBatchId(), INFO_MESSAGE_ORDERCANCELPROGRESS);
		    	
		    	//check order exceptions
		    	checkOrderExceptions(messages, exceptions);  	
		    			    								
				Map<String, ICrisisManagerBatchReservation> rsvMapping 
		    						= getService().getCrisisMngBatchReservation(getProcess().getBatchId(), false);
		    			    	
		    	System.out.println("############ Cancel Reservation Count ########### " + rsvMapping.keySet().size());
		    	Set keySet = new HashSet(rsvMapping.keySet());
		    	orderMngAgent.setReservations(keySet);
		    	orderMngAgent.doCancelReservations();		    		
		    	
		    	System.out.println("############ Cancel Order Count ########### " + orderMngAgent.getOrders().size());
	    		
		    	if(orderMngAgent.getOrders().size() > 0)
		    		exceptionOrderIds = orderMngAgent.doCancelOrders();		    		
		    			    	
		    	if(exceptionOrderIds != null && exceptionOrderIds.size() > 0){
		    		messages.addAll(exceptionOrderIds);
		    		getService().updateCrisisMngOrderException(getProcess().getBatchId(), getProcess().getBatchType().name(),exceptionOrderIds);
		    		getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), getFailureStatus());
		    		getService().updateCrisisMngBatchMessage(getProcess().getBatchId(),  formatMessages(messages));					
		    	}
		    	
		    	/*update the order status*/
		    	getService().updateCrisisMngReservationStatus(getProcess().getBatchId(), null);
		    	getService().updateCrisisMngOrderStatus(getProcess().getBatchId(),getProcess().getBatchType().name(), exceptionOrderIds);
		    	if(EnumCrisisMngBatchType.STANDINGORDER.equals(getProcess().getBatchType())){
		    		getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.ORDERCANCELCOMPLETE);
		    		getService().updateCrisisMngBatchMessage(getProcess().getBatchId(),  INFO_MESSAGE_ORDERCANCELCOMPLETED);
				}else{
					getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.AUTOCOMPLETED);
					getService().updateCrisisMngBatchMessage(getProcess().getBatchId(),  INFO_MESSAGE_REGULARORDERCANCELCOMPLETED);
				}
		    	
		    } catch(Exception e){
		    	LOGGER.error("CancelOrderTask failed with exception ", e);
		    	try {
		    		getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), getFailureStatus());
		    		getService().updateCrisisMngBatchMessage(getProcess().getBatchId(),  decodeErrorMessage(e));
				} catch (TransAdminServiceException exp) {						
					LOGGER.error("Failure to update batch status",exp);
				}
		    }	
		    System.out.println("############ CancelOrderTask STOP #############");
		}
		
	}
	
	private void checkOrderExceptions(List<String> messages,List<String> exceptions) {
		
		Map<EnumSaleStatus, Integer> orderStats = getService()
				.getOrderStatsByDate(getProcess().getDeliveryDate(),
						getProcess().getBatchId());

		for (Map.Entry<EnumSaleStatus, Integer> statusEntry : orderStats.entrySet()) {

			if (statusEntry.getKey().equals(EnumSaleStatus.SUBMITTED)
					|| statusEntry.getKey().equals(EnumSaleStatus.AVS_EXCEPTION)
					|| statusEntry.getKey().equals(EnumSaleStatus.AUTHORIZATION_FAILED)
					|| statusEntry.getKey().equals(EnumSaleStatus.AUTHORIZED)
					|| statusEntry.getKey().equals(EnumSaleStatus.CANCELED)) {
			} else {
				exceptions.add(statusEntry.getKey() + "="
						+ statusEntry.getValue());
			}
		}
		if (exceptions.size() > 0) {
			messages.add(ICrisisManagerProcessMessage.ERROR_MESSAGE_ORDEREXCEPTION);
			messages.addAll(exceptions);

			getService().updateCrisisMngBatchStatus(getProcess().getBatchId()
														,getFailureStatus());
			getService().updateCrisisMngBatchMessage(getProcess().getBatchId()
														,formatMessages(messages));
			return;
		}
	}
	
	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.ORDERCANCELFAILED;
	}

}
