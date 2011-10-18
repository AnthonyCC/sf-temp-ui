package com.freshdirect.transadmin.crisis.manager.action;

import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_BATCHCANCELCOMPLETED;

import com.freshdirect.routing.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.routing.constants.EnumCrisisMngBatchStatus;

import com.freshdirect.routing.model.ICrisisManagerBatch;
import com.freshdirect.routing.service.proxy.CrisisManagerServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;

public class CrisisManagerCancelAction extends AbstractCrisisManagerAction {
	
	public CrisisManagerCancelAction(ICrisisManagerBatch batch, String userId) {
		super(batch, userId);
	}

	public Object doExecute() throws Exception {
		
		CrisisManagerServiceProxy proxy = new CrisisManagerServiceProxy();
				
		proxy.updateCrisisMngBatchStatus(this.getBatch().getBatchId(), EnumCrisisMngBatchStatus.CANCELLED);
		proxy.addNewCrisisMngBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
				, EnumCrisisMngBatchActionType.CANCEL, this.getUserId());
		proxy.updateCrisisMngBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_BATCHCANCELCOMPLETED);
		return null;
		
	}

	@Override
	public EnumCrisisMngBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return null;
	}
}
