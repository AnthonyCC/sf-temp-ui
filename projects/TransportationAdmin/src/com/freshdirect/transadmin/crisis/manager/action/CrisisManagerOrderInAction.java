package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.ERROR_MESSAGE_NOORDER;

import java.util.List;

import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.model.ICrisisManagerBatchOrder;
import com.freshdirect.transadmin.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.model.IStandingOrderModel;
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
		    	
		    	getService().clearCrisisMngBatch(getProcess().getBatchId());		    	
		    	
		    	getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
		    	getService().addNewCrisisMngBatchAction(getProcess().getBatchId(), RoutingDateUtil.getCurrentDateTime()
						, EnumCrisisMngBatchActionType.ORDERDATAIN, CrisisManagerOrderInAction.this.getUserId());
		    	
				List<ICrisisManagerBatchOrder> inputDataList = getProcess().getOrder();
				getService().updateCrisisMngBatchMessage(getProcess().getBatchId(), ICrisisManagerProcessMessage.INFO_MESSAGE_ORDERDATACOLLECTIONPROGRESS);
		    	
		    	List<ICrisisManagerBatchReservation> reservationList = getService().getReservationByCriteria(getProcess().getDeliveryDate()
																, getProcess().getCutOffDateTime()
																, getProcess().getArea()
																, RoutingDateUtil.getServerTime(getProcess().getStartTime())
																, RoutingDateUtil.getServerTime(getProcess().getEndTime())
																, getProcess().getProfile());
		    	
		    	if(reservationList != null && reservationList.size() > 0){
		    		for(ICrisisManagerBatchReservation rsvModel : reservationList) {
		    			rsvModel.setBatchId(CrisisManagerOrderInAction.this.getBatch().getBatchId());
					}
		    		getService().addNewCrisisMngBatchReservation(reservationList);
		    	}
		    	
		    	List<IStandingOrderModel> standingOrderLst = getService().getStandingOrderByCriteria(getProcess().getDeliveryDate()
																, getProcess().getCutOffDateTime()
																, getProcess().getArea()
																, RoutingDateUtil.getServerTime(getProcess().getStartTime())
																, RoutingDateUtil.getServerTime(getProcess().getEndTime())
																, getProcess().getDeliveryType()
																, getProcess().getProfile()
																, getProcess().isStandingOrderIncluded()
															);
		    	
		    	if(standingOrderLst != null && standingOrderLst.size() > 0){
		    		for(IStandingOrderModel soModel : standingOrderLst) {
		    			soModel.setBatchId(getProcess().getBatchId());
					}
		    		getService().addNewCrisisMngBatchStandingOrder(standingOrderLst);
		    	}
		    	
		    	if(inputDataList.size() == 0) {
		    		inputDataList = getService().getOrderByCriteria(getProcess().getDeliveryDate()
		    													, getProcess().getCutOffDateTime()
		    													, getProcess().getArea()
		    													, RoutingDateUtil.getServerTime(getProcess().getStartTime())
																, RoutingDateUtil.getServerTime(getProcess().getEndTime())
		    													, getProcess().getDeliveryType()
		    													, getProcess().getProfile()
		    													, getProcess().isStandingOrderIncluded()
		    												);
		    	}
		    	
		    	System.out.println("######################### OrderScenario NO OF ORDERS ######################### " + inputDataList.size());
				if(inputDataList != null && inputDataList.size() > 0) {
					for(ICrisisManagerBatchOrder orderModel : inputDataList) {
						orderModel.setBatchId(getProcess().getBatchId());
					}
					
					// Logging order data collection complete
					getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.ORDERCOLECTIONCOMPETE);
					getService().updateCrisisMngBatchMessage(getProcess().getBatchId(), ICrisisManagerProcessMessage.INFO_MESSAGE_ORDERDATACOLLECTIONCOMPLETED);
					getService().addNewCrisisMngBatchOrder(inputDataList);			
				} else {
					getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.ORDERCOLECTIONFAILED);
					getService().updateCrisisMngBatchMessage(getProcess().getBatchId(), ERROR_MESSAGE_NOORDER);
				}
		    } catch (Exception exp) {
		    	exp.printStackTrace();
		    	try {
		    		getService().updateCrisisMngBatchStatus(getProcess().getBatchId(), EnumCrisisMngBatchStatus.ORDERCOLECTIONFAILED);
		    		getService().updateCrisisMngBatchMessage(getProcess().getBatchId(),  decodeErrorMessage(exp));
				} catch (TransAdminServiceException e) {
					// TODO Auto-generated catch block
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
