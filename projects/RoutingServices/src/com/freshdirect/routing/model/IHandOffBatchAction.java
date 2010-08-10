package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.constants.EnumHandOffBatchActionType;

public interface IHandOffBatchAction {
	
	String getBatchId();
	void setBatchId(String batchId);
	
	Date getActionDateTime();
	void setActionDateTime(Date actionDateTime);
	
	EnumHandOffBatchActionType getActionType();
	void setActionType(EnumHandOffBatchActionType actionType);
	
	String getActionBy();
	void setActionBy(String actionBy);
	
}
