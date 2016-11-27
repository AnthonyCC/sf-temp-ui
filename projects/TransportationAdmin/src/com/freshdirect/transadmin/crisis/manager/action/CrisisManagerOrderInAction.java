package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.ERROR_MESSAGE_NOORDER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchType;
import com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.model.ICrisisMngBatchOrder;
import com.freshdirect.routing.model.ICustomerModel;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;


public class CrisisManagerOrderInAction extends AbstractCrisisManagerAction {
		
	public CrisisManagerOrderInAction(ICrisisManagerBatch batch, String userId, ICrisisManagerService  crisisMngService) {
		super(batch, userId, crisisMngService);	
	}

	public Object doExecute() throws Exception {
		
		OrderDataInTask task = new OrderDataInTask();
		Thread dataCollectionThread = new Thread(task, "OrderDataInTask");
		dataCollectionThread.start();
		return null;
	}
	
	private ICrisisManagerService getService(){ 
		return CrisisManagerOrderInAction.this.getCrisisMngService();
	}
	
	private ICrisisManagerBatch getProcess(){ 
		return CrisisManagerOrderInAction.this.getBatch();
	}
	
	private class OrderDataInTask  implements Runnable {

		@Override
		public void run() {
			
		    System.out.println("######################### OrderDataInTask START #########################");
		    try {
		    	
		    	getService().clearCrisisMngBatch(getProcess().getBatchId(), getProcess().getBatchType());		    	
		    	
		    	getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
		    	getService().addNewCrisisMngBatchAction(getProcess().getBatchId(), RoutingDateUtil.getCurrentDateTime()
						, EnumCrisisMngBatchActionType.ORDERDATAIN, CrisisManagerOrderInAction.this.getUserId());
		    	getService().updateCrisisMngBatchMessage(getProcess().getBatchId(), ICrisisManagerProcessMessage.INFO_MESSAGE_ORDERDATACOLLECTIONPROGRESS);
				/*Block Capacity for source & destination dates*/
		    	blockDeliveryCapacity();
		    	
				List<ICrisisMngBatchOrder> inputDataList = getProcess().getOrder();
				Set<ICustomerModel> custModels = new HashSet<ICustomerModel>();
				List<ICrisisManagerBatchReservation> reservationList = new ArrayList<ICrisisManagerBatchReservation>();
				ICustomerModel tmpModel = null;
				/*check batch type STANDINGORDER or REGULARORDER*/
		    	if(EnumCrisisMngBatchType.STANDINGORDER.equals(getProcess().getBatchType())){
		    		inputDataList = getService().getStandingOrderByCriteria(getProcess().getDeliveryDate()
																				, getProcess().getCutOffDateTime()
																				, getProcess().getArea()
																				, RoutingDateUtil.getServerTime(getProcess().getStartTime())
																				, RoutingDateUtil.getServerTime(getProcess().getEndTime())
																				, getProcess().getDeliveryType()
																				, getProcess().getProfile()
																			);
		    		System.out.println("########### CrisisMngBatch -> STANDING ORDER COUNT ########### " + inputDataList.size());
		    	} else {
		    		reservationList = getService().getReservationByCriteria(getProcess().getDeliveryDate()
																				, getProcess().getCutOffDateTime()
																				, getProcess().getArea()
																				, RoutingDateUtil.getServerTime(getProcess().getStartTime())
																				, RoutingDateUtil.getServerTime(getProcess().getEndTime())
																				, getProcess().getProfile());
		    		for(ICrisisManagerBatchReservation rsvModel : reservationList){				
		    			tmpModel = rsvModel.getCustomerModel();
		    			tmpModel.setBatchId(getProcess().getBatchId());
		    			custModels.add(rsvModel.getCustomerModel());
					}
		    		System.out.println("########### CrisisMngBatch -> RESERVATION COUNT ############ " + reservationList.size());
		    		inputDataList = getService().getOrderByCriteria(getProcess().getDeliveryDate()
		    																	, getProcess().getCutOffDateTime()
		    																	, getProcess().getArea()
		    																	, RoutingDateUtil.getServerTime(getProcess().getStartTime())
		    																	, RoutingDateUtil.getServerTime(getProcess().getEndTime())
		    																	, getProcess().getDeliveryType()
		    																	, getProcess().getProfile()			    													
			    															);
			    	
			    	System.out.println("########### CrisisMngBatch -> REGULAR ORDER COUNT ############ " + inputDataList.size());						    		
		    	}
		    	if((inputDataList != null && inputDataList.size() > 0)
		    			|| (reservationList != null && reservationList.size() > 0)) {												
		    		
		    		for(ICrisisMngBatchOrder _order : inputDataList){				
		    			tmpModel = _order.getCustomerModel();
		    			tmpModel.setBatchId(getProcess().getBatchId());
		    			custModels.add(tmpModel);		    			
					}		    		
		    			getService().addNewCrisisMngBatchCustomer(custModels, getProcess().getBatchId());
		    		if(EnumCrisisMngBatchType.REGULARORDER.equals( getProcess().getBatchType())){
		    			getService().addNewCrisisMngBatchReservation(reservationList, getProcess().getBatchId());
		    			getService().addNewCrisisMngBatchRegularOrder(inputDataList, getProcess().getBatchId());
		    		} else {
						getService().addNewCrisisMngBatchStandingOrder(inputDataList, getProcess().getBatchId());
		    		}
		    		
		    		getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.ORDERCOLECTIONCOMPLETE);
					getService().updateCrisisMngBatchMessage(getProcess().getBatchId(), ICrisisManagerProcessMessage.INFO_MESSAGE_ORDERDATACOLLECTIONCOMPLETED);
					
				} else {
					getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.ORDERCOLECTIONFAILED);
					getService().updateCrisisMngBatchMessage(getProcess().getBatchId(), ERROR_MESSAGE_NOORDER);
				}		    	
		    	/*map timeslots for selected & destination dates*/
		    	checkDeliverySlotExceptions();	    	
				
		    } catch (Exception exp) {
		    	exp.printStackTrace();
		    	try {
		    		getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.ORDERCOLECTIONFAILED);
		    		getService().updateCrisisMngBatchMessage(getProcess().getBatchId(),  decodeErrorMessage(exp));
				} catch (TransAdminServiceException e) {
					e.printStackTrace();
				}				
		    }
		    System.out.println("######################### OrderDataInTask END #########################");
		}			
	}
	
	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return EnumCrisisMngBatchStatus.ORDERCOLECTIONFAILED;
	}
}
