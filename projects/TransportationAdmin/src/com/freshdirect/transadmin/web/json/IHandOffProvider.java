package com.freshdirect.transadmin.web.json;

import java.util.List;

import com.freshdirect.transadmin.web.model.HandOffBatchInfo;


public interface IHandOffProvider {
		
	List<HandOffBatchInfo> getHandOffBatch(String deliveryDate);
	
	HandOffBatchInfo getHandOffBatchById(String batchId);
	
	boolean doRoutingOut(String handOffBatchId, String[][] schedule);
	
	boolean doRoutingIn(String handOffBatchId);
	
	boolean doHandOffCancel(String handOffBatchId);
	
	boolean doHandOffStop(String handOffBatchId);
	
	boolean doHandOffCommit(String handOffBatchId);
	
	String getServiceTimeScenario(String deliveryDate);
}
