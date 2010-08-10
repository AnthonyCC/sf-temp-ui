package com.freshdirect.routing.handoff.action;

import static com.freshdirect.routing.manager.IProcessMessage.INFO_MESSAGE_BATCHCANCELCOMPLETED;

import java.text.ParseException;

import com.freshdirect.routing.constants.EnumHandOffBatchActionType;
import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;

public class HandOffCancelAction extends AbstractHandOffAction {
		
	public HandOffCancelAction(IHandOffBatch batch, String userId) {
		super(batch, userId);
	}

	public void doExecute() throws Exception {
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
				
		proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), EnumHandOffBatchStatus.CANCELLED);
		proxy.addNewHandOffBatchAction(this.getBatch().getBatchId(), RoutingDateUtil.getCurrentDateTime()
				, EnumHandOffBatchActionType.CANCEL, this.getUserId());
		proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), INFO_MESSAGE_BATCHCANCELCOMPLETED);
		
	}

	@Override
	public EnumHandOffBatchStatus getFailureStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
