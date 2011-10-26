package com.freshdirect.transadmin.model;

import java.util.Date;

import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;

public interface ICrisisManagerBatchAction {
	
	String getBatchId();
	void setBatchId(String batchId);
	
	Date getActionDateTime();
	void setActionDateTime(Date actionDateTime);
	
	EnumCrisisMngBatchActionType getActionType();
	void setActionType(EnumCrisisMngBatchActionType actionType);
	
	String getActionBy();
	void setActionBy(String actionBy);
	
}
