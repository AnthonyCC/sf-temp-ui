package com.freshdirect.transadmin.model;

import java.util.Date;

import com.freshdirect.transadmin.constants.EnumCrisisMngBatchActionType;
import com.freshdirect.routing.model.BaseModel;

@SuppressWarnings("serial")
public class CrisisManagerBatchAction extends BaseModel implements ICrisisManagerBatchAction, Comparable<CrisisManagerBatchAction>  {
	
	private String batchId;
	private Date actionDateTime;
	private EnumCrisisMngBatchActionType actionType;
	private String actionBy;
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public Date getActionDateTime() {
		return actionDateTime;
	}
	public void setActionDateTime(Date actionDateTime) {
		this.actionDateTime = actionDateTime;
	}
	public EnumCrisisMngBatchActionType getActionType() {
		return actionType;
	}
	public void setActionType(EnumCrisisMngBatchActionType actionType) {
		this.actionType = actionType;
	}
	public String getActionBy() {
		return actionBy;
	}
	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((actionDateTime == null) ? 0 : actionDateTime.hashCode());
		result = prime * result
				+ ((actionType == null) ? 0 : actionType.hashCode());
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		CrisisManagerBatchAction other = (CrisisManagerBatchAction) obj;
		if (actionDateTime == null) {
			if (other.actionDateTime != null)
				return false;
		} else if (!actionDateTime.equals(other.actionDateTime))
			return false;
		if (actionType == null) {
			if (other.actionType != null)
				return false;
		} else if (!actionType.equals(other.actionType))
			return false;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(CrisisManagerBatchAction o) {
		// TODO Auto-generated method stub
		if(this.equals(o)) {
			return 0;
		} else {
			return this.toString().compareTo(o.toString());
		}
	}
	@Override
	public String toString() {
		return "OrderScenarioBatchAction [actionBy=" + actionBy + ", actionDateTime="
				+ actionDateTime + ", actionType=" + actionType + ", batchId="
				+ batchId + "]";
	}
	
	
}
