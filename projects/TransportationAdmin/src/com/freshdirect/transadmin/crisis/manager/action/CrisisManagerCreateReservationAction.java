package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_CREATERESERVATIONPROGRESS;
import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_CREATERESERVATIONCOMPLETED;
import static com.freshdirect.routing.manager.IProcessMessage.ERROR_MESSAGE_TIMESLOTEXCEPTION;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.routing.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.routing.constants.EnumReservationType;
import com.freshdirect.routing.model.CustomerModel;
import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.routing.model.ICrisisManagerBatchOrder;
import com.freshdirect.routing.model.ICustomerModel;
import com.freshdirect.routing.model.IReservationModel;
import com.freshdirect.routing.model.ReservationModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.CrisisManagerServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.util.CrisisManagerUtil;

public class CrisisManagerCreateReservationAction extends
		AbstractCrisisManagerAction {
	
	private boolean isExceptionCheck;
	
	public CrisisManagerCreateReservationAction(ICrisisManagerBatch batch,
			String userId, boolean isExceptionCheck) {
		super(batch, userId);
		this.isExceptionCheck = isExceptionCheck;
	}

	@Override
	public Object doExecute() throws Exception {

		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();			
		Map<String, Integer> foundExceptions = new HashMap<String, Integer>();
		
		System.out.println("######################### Create Reservation START #########################");
		try {
			proxy.updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
	    	proxy.addNewCrisisMngBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
					, EnumCrisisMngBatchActionType.CREATERESERVATION, this.getUserId());
	    	
	    	proxy.updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_CREATERESERVATIONPROGRESS);
	   
			
			Map<String, List<ICrisisManagerBatchDeliverySlot>> exceptionSlots 
															= proxy.getCrisisMngBatchTimeslot(this.getBatch().getBatchId(), false);
			if(exceptionSlots.size() > 0){
				for(Map.Entry<String, List<ICrisisManagerBatchDeliverySlot>> exceptionEntry : exceptionSlots.entrySet()){
					
					for(ICrisisManagerBatchDeliverySlot _slot : exceptionEntry.getValue()){
						if(!foundExceptions.containsKey(_slot.getArea())){
							foundExceptions.put(_slot.getArea(), 0);
						}
						foundExceptions.put(_slot.getArea(), foundExceptions.get(_slot.getArea()).intValue()+1);						
					}
				}
			}
			
			if(!isExceptionCheck && foundExceptions.size() > 0) {
				proxy.updateCrisisMngBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
				proxy.updateCrisisMngBatchMessage(this.getBatch().getBatchId(),  ERROR_MESSAGE_TIMESLOTEXCEPTION);
				return foundExceptions;
			}
			
			if(foundExceptions.size() == 0 && isExceptionCheck){
				processCreateReservation(proxy);
			}
	    			    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	try {
				proxy.updateCrisisMngBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
				proxy.updateCrisisMngBatchMessage(this.getBatch().getBatchId(),  decodeErrorMessage(e));
			} catch (RoutingServiceException exp) {				
				exp.printStackTrace();
			}
	    }	
	    proxy.updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.CREATERESERVATIONCOMPLETE);
		proxy.updateCrisisMngBatchMessage(this.getBatch().getBatchId(),  INFO_MESSAGE_CREATERESERVATIONCOMPLETED);
		System.out.println("######################### Create Reservation STOP #########################");
		return null;
	}
	
	private void processCreateReservation(CrisisManagerServiceProxy proxy){
		
		Set<IReservationModel> rsvModels = new HashSet<IReservationModel>();
		
		List<ICrisisManagerBatchOrder> cancelledOrders = proxy.getCrisisMngBatchOrders(this.getBatch().getBatchId(), true, false);
		Map<String, List<ICrisisManagerBatchDeliverySlot>> batchTimeSlots 
												= proxy.getCrisisMngBatchTimeslot(this.getBatch().getBatchId(), true);
		
		if(cancelledOrders != null && cancelledOrders.size() > 0){
			Iterator<ICrisisManagerBatchOrder> orderItr = cancelledOrders.iterator();
			List<ICrisisManagerBatchDeliverySlot> areaSlots = null;
			IReservationModel rsvModel = null;
			while(orderItr.hasNext()){
				ICrisisManagerBatchOrder _order = orderItr.next();				
				if(!_order.isException()){
					rsvModel = new ReservationModel();
					areaSlots = batchTimeSlots.get(_order.getArea());
					Iterator<ICrisisManagerBatchDeliverySlot> itr = areaSlots.iterator();
					while (itr.hasNext()) {
						ICrisisManagerBatchDeliverySlot _tempSlot = itr.next();
	
						if (_order.getStartTime().equals(_tempSlot.getStartTime())
								&& _order.getEndTime().equals(_tempSlot.getEndTime()) 
								&& _tempSlot.getDestStartTime() != null && _tempSlot.getDestEndTime() != null) {
							rsvModel.setStartTime(_tempSlot.getDestStartTime());
							rsvModel.setEndTime(_tempSlot.getDestEndTime());
							rsvModel.setTimeSlotId(_tempSlot.getTimeSlotId());
							break;
						}
					}	
					if(rsvModel.getTimeSlotId() != null){
						rsvModels.add(rsvModel);
						
						ICustomerModel customerModel = new CustomerModel();				
						customerModel.setErpCustomerPK(_order.getErpCustomerPK());
						customerModel.setFdCustomerPK(_order.getFdCustomerPK());				
						customerModel.setFirstName(_order.getFirstName());
						customerModel.setLastName(_order.getLastName());
						
						rsvModel.setCustomerModel(customerModel);
						rsvModel.setArea(_order.getArea());
						rsvModel.setDeliveryDate(this.getBatch().getDestinationDate());
						rsvModel.setType(EnumReservationType.ONETIME_RESERVATION);
						rsvModel.setAddressId(_order.getAddressId());
						rsvModel.setOrderId(_order.getOrderNumber());
					}
				}
			}
			
			CrisisManagerUtil orderMngAgent = new CrisisManagerUtil();
	    	orderMngAgent.setAgent(this.getUserId());		    		
	    	orderMngAgent.setReservations(rsvModels);
	    	Map<String, String> reservations = orderMngAgent.doCreateReservations();
	    	if(reservations != null && reservations.size() > 0){
	    		proxy.updateCrisisMngBatchOrderReservation(this.getBatch().getBatchId(), reservations);
	    	}
		}
	}

	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.CREATERESERVATIONFAILED;
	}

}
