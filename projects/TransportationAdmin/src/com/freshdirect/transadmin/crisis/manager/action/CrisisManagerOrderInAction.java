package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.routing.manager.IProcessMessage.ERROR_MESSAGE_NOORDER;

import java.util.List;

import com.freshdirect.routing.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.routing.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.routing.manager.IProcessMessage;
import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.model.ICrisisManagerBatchOrder;
import com.freshdirect.routing.model.ICrisisManagerBatchReservation;
import com.freshdirect.routing.model.IStandingOrderModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.CrisisManagerServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;

public class CrisisManagerOrderInAction extends AbstractCrisisManagerAction {
		
	public CrisisManagerOrderInAction(ICrisisManagerBatch batch, String userId) {
		super(batch, userId);	
	}

	public Object doExecute() throws Exception {
		
		OrderDataInTask task = new OrderDataInTask();
		Thread dataCollectionThread = new Thread(task, "OrderDataInTask");
		dataCollectionThread.start();
		return null;
	}
	 
	private class OrderDataInTask  implements Runnable {

		@Override
		public void run() {
			
			CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
			
		    System.out.println("######################### OrderDataInTask START #########################");
		    try {
		    	
		    	proxy.clearCrisisMngBatch(CrisisManagerOrderInAction.this.getBatch().getBatchId());		    	
		    	
		    	proxy.updateCrisisMngBatchStatus(CrisisManagerOrderInAction.this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.PROCESSING);
				proxy.addNewCrisisMngBatchAction(CrisisManagerOrderInAction.this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
						, EnumCrisisMngBatchActionType.ORDERDATAIN, CrisisManagerOrderInAction.this.getUserId());
		    	
				List<ICrisisManagerBatchOrder> inputDataList = CrisisManagerOrderInAction.this.getBatch().getOrder();
		    	proxy.updateCrisisMngBatchMessage(CrisisManagerOrderInAction.this.getBatch().getBatchId(), IProcessMessage.INFO_MESSAGE_ORDERDATACOLLECTIONPROGRESS);
		    	
		    	List<ICrisisManagerBatchReservation> reservationList = proxy.getReservationByCriteria(CrisisManagerOrderInAction.this.getBatch().getDeliveryDate()
																, CrisisManagerOrderInAction.this.getBatch().getCutOffDateTime()
																, CrisisManagerOrderInAction.this.getBatch().getArea()
																, RoutingDateUtil.getServerTime(CrisisManagerOrderInAction.this.getBatch().getStartTime())
																, RoutingDateUtil.getServerTime(CrisisManagerOrderInAction.this.getBatch().getEndTime()));
		    	
		    	if(reservationList != null && reservationList.size() > 0){
		    		for(ICrisisManagerBatchReservation rsvModel : reservationList) {
		    			rsvModel.setBatchId(CrisisManagerOrderInAction.this.getBatch().getBatchId());
					}
		    		proxy.addNewCrisisMngBatchReservation(reservationList);
		    	}
		    	
		    	List<IStandingOrderModel> standingOrderLst = proxy.getStandingOrderByCriteria(CrisisManagerOrderInAction.this.getBatch().getDeliveryDate()
																, CrisisManagerOrderInAction.this.getBatch().getCutOffDateTime()
																, CrisisManagerOrderInAction.this.getBatch().getArea()
																, RoutingDateUtil.getServerTime(CrisisManagerOrderInAction.this.getBatch().getStartTime())
																, RoutingDateUtil.getServerTime(CrisisManagerOrderInAction.this.getBatch().getEndTime())
																, CrisisManagerOrderInAction.this.getBatch().getDeliveryType()
																, CrisisManagerOrderInAction.this.getBatch().isStandingOrderIncluded()
															);
		    	
		    	if(standingOrderLst != null && standingOrderLst.size() > 0){
		    		for(IStandingOrderModel soModel : standingOrderLst) {
		    			soModel.setBatchId(CrisisManagerOrderInAction.this.getBatch().getBatchId());
					}
		    		proxy.addNewCrisisMngBatchStandingOrder(standingOrderLst);
		    	}
		    	
		    	if(inputDataList.size() == 0) {
		    		inputDataList = proxy.getOrderByCriteria(CrisisManagerOrderInAction.this.getBatch().getDeliveryDate()
		    													, CrisisManagerOrderInAction.this.getBatch().getCutOffDateTime()
		    													, CrisisManagerOrderInAction.this.getBatch().getArea()
		    													, RoutingDateUtil.getServerTime(CrisisManagerOrderInAction.this.getBatch().getStartTime())
																, RoutingDateUtil.getServerTime(CrisisManagerOrderInAction.this.getBatch().getEndTime())
		    													, CrisisManagerOrderInAction.this.getBatch().getDeliveryType()
		    													, CrisisManagerOrderInAction.this.getBatch().isStandingOrderIncluded()
		    												);
		    	}
		    	
		    	System.out.println("######################### OrderScenario NO OF ORDERS ######################### " + inputDataList.size());
				if(inputDataList != null && inputDataList.size() > 0) {
					for(ICrisisManagerBatchOrder orderModel : inputDataList) {
						orderModel.setBatchId(CrisisManagerOrderInAction.this.getBatch().getBatchId());
					}
					
					// Logging order data collection complete
					proxy.updateCrisisMngBatchStatus(CrisisManagerOrderInAction.this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.ORDERCOLECTIONCOMPETE);
					proxy.updateCrisisMngBatchMessage(CrisisManagerOrderInAction.this.getBatch().getBatchId(), IProcessMessage.INFO_MESSAGE_ORDERDATACOLLECTIONCOMPLETED);
					proxy.addNewCrisisMngBatchOrder(inputDataList);			
				} else {
					proxy.updateCrisisMngBatchStatus(CrisisManagerOrderInAction.this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.ORDERCOLECTIONFAILED);
					proxy.updateCrisisMngBatchMessage(CrisisManagerOrderInAction.this.getBatch().getBatchId(), ERROR_MESSAGE_NOORDER);
				}
		    } catch (Exception exp) {
		    	exp.printStackTrace();
		    	try {
					proxy.updateCrisisMngBatchStatus(CrisisManagerOrderInAction.this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.ORDERCOLECTIONFAILED);
					proxy.updateCrisisMngBatchMessage(CrisisManagerOrderInAction.this.getBatch().getBatchId(),  decodeErrorMessage(exp));
				} catch (RoutingServiceException e) {
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
