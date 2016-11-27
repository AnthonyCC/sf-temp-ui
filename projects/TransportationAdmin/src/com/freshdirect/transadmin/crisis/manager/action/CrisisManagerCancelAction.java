package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.transadmin.manager.ICrisisManagerProcessMessage.INFO_MESSAGE_BATCHCANCELCOMPLETED;

import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;

import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.service.ICrisisManagerService;

public class CrisisManagerCancelAction extends AbstractCrisisManagerAction {
	
	public CrisisManagerCancelAction(ICrisisManagerBatch batch, String userId, ICrisisManagerService  crisisMngService) {
		super(batch, userId, crisisMngService);
	}

	public Object doExecute() throws Exception {
		
		this.getCrisisMngService().updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.CANCELLED);
		this.getCrisisMngService().addNewCrisisMngBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
				, EnumCrisisMngBatchActionType.CANCEL, this.getUserId());
		this.getCrisisMngService().updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_BATCHCANCELCOMPLETED);
		
		unBlockDeliveryCapacity(false);
    	
		return null;
		
	}

	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return null;
	}
}
