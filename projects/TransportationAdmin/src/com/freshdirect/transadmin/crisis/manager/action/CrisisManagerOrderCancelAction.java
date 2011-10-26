package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_ORDERCANCELPROGRESS;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_ORDERCANCELCOMPLETED;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.CrisisManagerUtil;

public class CrisisManagerOrderCancelAction extends AbstractCrisisManagerAction {

	private List<String> cancelOrders;
	
	public CrisisManagerOrderCancelAction(ICrisisManagerBatch batch, String userId, List<String> cancelOrders, ICrisisManagerService  crisisMngService) {
		super(batch, userId, crisisMngService);
		this.cancelOrders = cancelOrders;
	}

	private ICrisisManagerService getService(){ 
		return CrisisManagerOrderCancelAction.this.getCrisisMngService();
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
			
			System.out.println("######################### OrderCancelTask START #########################");
		    try {
		    	getService().updateCrisisMngBatchStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
		    	getService().addNewCrisisMngBatchAction(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
						, EnumCrisisMngBatchActionType.ORDERCANCEL, CrisisManagerOrderCancelAction.this.getUserId());
		    	
		    	getService().updateCrisisMngBatchMessage(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), INFO_MESSAGE_ORDERCANCELPROGRESS);
		    	
		    	
		    	//check order exceptions
		    	Map<EnumSaleStatus, Integer> orderStats = getService().getOrderStatsByDate(CrisisManagerOrderCancelAction.this.getBatch().getDeliveryDate()
		    						, CrisisManagerOrderCancelAction.this.getBatch().getBatchId());
				
				
				for(Map.Entry<EnumSaleStatus, Integer> statusEntry : orderStats.entrySet()) {
					
					if(statusEntry.getKey().equals(EnumSaleStatus.SUBMITTED)
							|| statusEntry.getKey().equals(EnumSaleStatus.AVS_EXCEPTION)
							|| statusEntry.getKey().equals(EnumSaleStatus.AUTHORIZATION_FAILED)
								|| statusEntry.getKey().equals(EnumSaleStatus.AUTHORIZED)) {					
						
					} else {
						exceptions.add(statusEntry.getKey() +"="+ statusEntry.getValue());
					}
				}
				if(exceptions.size() > 0) {
					messages.add(ICrisisManagerProcessMessage.ERROR_MESSAGE_ORDEREXCEPTION);
					messages.addAll(exceptions);
					
					getService().updateCrisisMngBatchStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), getFailureStatus());
					getService().updateCrisisMngBatchMessage(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(),  formatMessages(messages));
					return;
				}  	
		    	
		    	
		    	Map<String, ICrisisManagerBatchReservation> reservationMapping 
		    						= getService().getCrisisMngBatchReservation(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), false);
		    	
		    	CrisisManagerUtil orderMngAgent = new CrisisManagerUtil();
		    	orderMngAgent.setAgent(CrisisManagerOrderCancelAction.this.getUserId());		    		
		    	orderMngAgent.setOrders(CrisisManagerOrderCancelAction.this.cancelOrders);
		    	if(reservationMapping.keySet() != null && reservationMapping.keySet().size() > 0){
		    		System.out.println("######################### Reservation Cancel size ######################### " + reservationMapping.keySet().size());
		    		Set keySet = new HashSet(reservationMapping.keySet());
		    		orderMngAgent.setReservations(keySet);
		    		orderMngAgent.doCancelReservations();		    		
		    	}
		    	System.out.println("######################### Order Cancel size ######################### " + CrisisManagerOrderCancelAction.this.cancelOrders.size());
	    		
		    	if(CrisisManagerOrderCancelAction.this.cancelOrders.size() > 0){
		    		exceptionOrderIds = orderMngAgent.doCancelOrders();
		    		getService().clearCrisisMngBatchDeliverySlot(CrisisManagerOrderCancelAction.this.getBatch().getBatchId());
		    	}
		    	
		    	if(exceptionOrderIds != null && exceptionOrderIds.size() > 0){
		    		getService().updateCrisisMngOrderException(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), exceptionOrderIds);
		    	}
		    	
		    	checkDeliverySlotExceptions();
		    	
		    	getService().updateCrisisMngReservationStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), null);
		    	getService().updateCrisisMngOrderStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), exceptionOrderIds);
		    			    	
		    }catch(Exception e){
		    	e.printStackTrace();
		    	try {
		    		getService().updateCrisisMngBatchStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), getFailureStatus());
		    		getService().updateCrisisMngBatchMessage(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(),  decodeErrorMessage(e));
				} catch (TransAdminServiceException exp) {
					// TODO Auto-generated catch block
					exp.printStackTrace();
				}
		    }
		    		    
	    	
		    getService().updateCrisisMngBatchStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.ORDERCANCELCOMPLETE);
		    getService().updateCrisisMngBatchMessage(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(),  INFO_MESSAGE_ORDERCANCELCOMPLETED);
			
		    System.out.println("######################### OrderCancelTask STOP #########################");
		}
	}
	
	private void checkDeliverySlotExceptions() throws TransAdminServiceException {
			
		Map<String, List<ICrisisManagerBatchDeliverySlot>> batchGroupedSlots 
											= getService().getCrisisMngBatchTimeslotByZone(CrisisManagerOrderCancelAction.this.getBatch().getBatchId());
		
		Map<String, List<ICrisisManagerBatchDeliverySlot>> groupedSlots 
											= getService().getTimeslotByDate(CrisisManagerOrderCancelAction.this.getBatch().getDestinationDate());
		if(batchGroupedSlots != null && groupedSlots != null){
			List<ICrisisManagerBatchDeliverySlot> destAreaSlots = null;
			for(Map.Entry<String, List<ICrisisManagerBatchDeliverySlot>> slotEntry : batchGroupedSlots.entrySet()){
				destAreaSlots = groupedSlots.get(slotEntry.getKey());
				for(ICrisisManagerBatchDeliverySlot _slot : slotEntry.getValue()){
					if(destAreaSlots != null)
						matchTimeslot(_slot, destAreaSlots);
					else
						break;
				}
			}
			getService().addCrisisMngBatchDeliveryslot(batchGroupedSlots);
		}
		
	}	
	
	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.ORDERCANCELFAILED;
	}

}
