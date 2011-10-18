package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_ORDERCANCELPROGRESS;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_ORDERCANCELCOMPLETED;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.routing.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.routing.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.routing.manager.IProcessMessage;
import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.routing.model.ICrisisManagerBatchOrder;
import com.freshdirect.routing.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.CrisisManagerServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.util.CrisisManagerUtil;

public class CrisisManagerOrderCancelAction extends AbstractCrisisManagerAction {

	private List<ICrisisManagerBatchOrder> cancelOrders;
	
	public CrisisManagerOrderCancelAction(ICrisisManagerBatch batch, String userId, 
			List<ICrisisManagerBatchOrder> cancelOrders) {
		super(batch, userId);
		this.cancelOrders = cancelOrders;
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
			CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();			
			List<String> exceptionOrderIds = null;
			List<String> messages = new ArrayList<String>();			
			List<String> exceptions = new ArrayList<String>();
			
			System.out.println("######################### OrderCancelTask START #########################");
		    try {
		    	proxy.updateCrisisMngBatchStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
		    	proxy.addNewCrisisMngBatchAction(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
						, EnumCrisisMngBatchActionType.ORDERCANCEL, CrisisManagerOrderCancelAction.this.getUserId());
		    	
		    	proxy.updateCrisisMngBatchMessage(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), INFO_MESSAGE_ORDERCANCELPROGRESS);
		    	
		    	
		    	//check order exceptions
		    	Map<EnumSaleStatus, Integer> orderStats = proxy.getOrderStatsByDate(CrisisManagerOrderCancelAction.this.getBatch().getDeliveryDate()
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
					messages.add(IProcessMessage.ERROR_MESSAGE_ORDEREXCEPTION);
					messages.addAll(exceptions);
					
					proxy.updateCrisisMngBatchStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), getFailureStatus());
					proxy.updateCrisisMngBatchMessage(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(),  formatMessages(messages));
					return;
				}  	
		    	
		    	
		    	Map<String, ICrisisManagerBatchReservation> reservationMapping 
		    						= proxy.getCrisisMngBatchReservation(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), false);
		    	
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
		    		proxy.clearCrisisMngBatchDeliverySlot(CrisisManagerOrderCancelAction.this.getBatch().getBatchId());
		    	}
		    	
		    	if(exceptionOrderIds != null && exceptionOrderIds.size() > 0){
		    		proxy.updateCrisisMngOrderException(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), exceptionOrderIds);
		    	}
		    	
		    	checkDeliverySlotExceptions();
		    	
		    	proxy.updateCrisisMngReservationStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), null);
		    	proxy.updateCrisisMngOrderStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), exceptionOrderIds);
		    			    	
		    }catch(Exception e){
		    	e.printStackTrace();
		    	try {
					proxy.updateCrisisMngBatchStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), getFailureStatus());
					proxy.updateCrisisMngBatchMessage(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(),  decodeErrorMessage(e));
				} catch (RoutingServiceException exp) {
					// TODO Auto-generated catch block
					exp.printStackTrace();
				}
		    }
		    		    
	    	
	    	proxy.updateCrisisMngBatchStatus(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.ORDERCANCELCOMPLETE);
			proxy.updateCrisisMngBatchMessage(CrisisManagerOrderCancelAction.this.getBatch().getBatchId(),  INFO_MESSAGE_ORDERCANCELCOMPLETED);
			
		    System.out.println("######################### OrderCancelTask STOP #########################");
		}
	}
	
	private void checkDeliverySlotExceptions() throws RoutingServiceException {
		
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();			
		
		Map<String, List<ICrisisManagerBatchDeliverySlot>> batchGroupedSlots 
											= proxy.getCrisisMngBatchTimeslotByZone(CrisisManagerOrderCancelAction.this.getBatch().getBatchId());
		
		Map<String, List<ICrisisManagerBatchDeliverySlot>> groupedSlots 
											= proxy.getTimeslotByDate(CrisisManagerOrderCancelAction.this.getBatch().getDestinationDate());
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
			proxy.addCrisisMngBatchDeliveryslot(batchGroupedSlots);
		}
		
	}	
	
	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.ORDERCANCELFAILED;
	}

}
