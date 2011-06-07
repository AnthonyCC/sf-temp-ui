package com.freshdirect.transadmin.web.json;

import java.util.List;
import java.util.Map;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.transadmin.web.model.HandOffBatchInfo;


public interface IHandOffProvider {
		
	List<HandOffBatchInfo> getHandOffBatch(String deliveryDate);
	
	HandOffBatchInfo getHandOffBatchById(String batchId);
	
	boolean doRoutingOut(String handOffBatchId, String[][] schedule);
	
	boolean doRoutingIn(String handOffBatchId);
	
	boolean doHandOffCancel(String handOffBatchId);
	
	boolean doHandOffStop(String handOffBatchId);
	
	String doHandOffCommit(String handOffBatchId, boolean force, boolean isCommitCheck);
	
	String getServiceTimeScenario(String deliveryDate);
	
	boolean doHandOffAutoDispatch(String handOffBatchId, boolean isBullpen);
}
