package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.ERROR_MESSAGE_TIMESLOTEXCEPTION;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_CREATERESERVATIONCOMPLETED;
import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_CREATERESERVATIONPROGRESS;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.constants.EnumReservationType;
import com.freshdirect.routing.model.CustomerModel;
import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.routing.model.ICustomerModel;
import com.freshdirect.routing.model.IReservationModel;
import com.freshdirect.routing.model.ReservationModel;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.CrisisManagerUtil;

public class CrisisManagerCreateReservationAction extends
		AbstractCrisisManagerAction {
		
	public CrisisManagerCreateReservationAction(ICrisisManagerBatch batch,
			String userId, ICrisisManagerService crisisMngService) {
		super(batch, userId, crisisMngService);
	}

	@Override
	public Object doExecute() throws Exception {
			
		Map<String, Integer> foundExceptions = new HashMap<String, Integer>();
		
		System.out.println("######################### Create Reservation START #########################");
		try {
			this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
			this.getCrisisMngService().addNewCrisisMngBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
					, EnumCrisisMngBatchActionType.CREATERESERVATION, this.getUserId());
	    	
			this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_CREATERESERVATIONPROGRESS);
	   
			
			Map<String, List<ICrisisManagerBatchDeliverySlot>> exceptionSlots 
															= this.getCrisisMngService().getCrisisMngBatchTimeslot(this.getBatch().getBatchId(), false);
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
			
			if(foundExceptions != null && foundExceptions.size() > 0) {
				this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
				this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(),  ERROR_MESSAGE_TIMESLOTEXCEPTION);
				return foundExceptions;
			}
			
			this.unBlockDeliveryCapacity(true);
			processCreateReservation();
			this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.COMPLETED);
		    this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(),  INFO_MESSAGE_CREATERESERVATIONCOMPLETED);
			
		}catch(Exception e){
	    	e.printStackTrace();
	    	try {
	    		this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
	    		this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(),  decodeErrorMessage(e));
			} catch (TransAdminServiceException exp) {				
				exp.printStackTrace();
			}
	    }	
	    		
	    System.out.println("######################### Create Reservation STOP #########################");
		return null;
	}
	
	private void processCreateReservation() throws Exception{
		
		Set<IReservationModel> rsvModels = new HashSet<IReservationModel>();
		
		List<ICrisisMngBatchOrder> cancelledOrders = this.getCrisisMngService().getCrisisMngBatchRegularOrder(this.getBatch().getBatchId(), true, false);

		/*Map<String, ICrisisManagerBatchReservation> canceledRsvMapping 
										= this.getCrisisMngService().getCrisisMngBatchReservation(this.getBatch().getBatchId(), false);*/
		Map<String, List<ICrisisManagerBatchDeliverySlot>> batchTimeSlots 
												= this.getCrisisMngService().getCrisisMngBatchTimeslot(this.getBatch().getBatchId(), true);
		
		if(cancelledOrders != null && cancelledOrders.size() > 0){
			Iterator<ICrisisMngBatchOrder> orderItr = cancelledOrders.iterator();
			List<ICrisisManagerBatchDeliverySlot> areaSlots = null;
			IReservationModel rsvModel = null;
			while(orderItr.hasNext()){
				ICrisisMngBatchOrder _order = orderItr.next();				
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
						customerModel.setErpCustomerPK(_order.getCustomerModel().getErpCustomerPK());									
						customerModel.setFirstName(_order.getCustomerModel().getFirstName());
						customerModel.setLastName(_order.getCustomerModel().getLastName());
						
						rsvModel.setCustomerModel(customerModel);
						rsvModel.setArea(_order.getArea());
						rsvModel.setDeliveryDate(this.getBatch().getDestinationDate());
						rsvModel.setType(EnumReservationType.ONETIME_RESERVATION);
						rsvModel.setAddressId(_order.getAddressId());
						rsvModel.setOrderId(_order.getOrderNumber());
					}
				}
			}
		
			/*for(Map.Entry<String, ICrisisManagerBatchReservation> rsvEntry : canceledRsvMapping.entrySet()){
				ICrisisManagerBatchReservation _tmpRsvModel = rsvEntry.getValue();
				rsvModel = new ReservationModel();
				areaSlots = batchTimeSlots.get(_tmpRsvModel.getArea());
				Iterator<ICrisisManagerBatchDeliverySlot> itr = areaSlots.iterator();
				while (itr.hasNext()) {
					ICrisisManagerBatchDeliverySlot _tempSlot = itr.next();

					if (_tmpRsvModel.getStartTime().equals(_tempSlot.getStartTime())
							&& _tmpRsvModel.getEndTime().equals(_tempSlot.getEndTime()) 
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
					customerModel.setErpCustomerPK(_tmpRsvModel.getCustomerModel().getErpCustomerPK());									
					customerModel.setFirstName(_tmpRsvModel.getCustomerModel().getFirstName());
					customerModel.setLastName(_tmpRsvModel.getCustomerModel().getLastName());
					
					rsvModel.setCustomerModel(customerModel);
					rsvModel.setArea(_tmpRsvModel.getArea());
					rsvModel.setDeliveryDate(this.getBatch().getDestinationDate());
					rsvModel.setType(EnumReservationType.ONETIME_RESERVATION);
					rsvModel.setAddressId(_tmpRsvModel.getAddressId());
					rsvModel.setOrderId(_tmpRsvModel.getId());
				}
			}*/

	    	Map<String, String> reservations = CrisisManagerUtil.doCreateReservations(rsvModels, this.getUserId());
	    	if(reservations != null && reservations.size() > 0){
	    		this.getCrisisMngService().updateCrisisMngBatchOrderReservation(this.getBatch().getBatchId(), reservations);
	    	}
		}
	}

	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.CREATERESERVATIONFAILED;
	}

}
