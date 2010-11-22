package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.constants.EnumHandOffDispatchStatus;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffDispatchIn;

public class HandOffBatchDispatch implements HandOffDispatchIn  {
	
	private Date dispatchTime;
	private EnumHandOffDispatchStatus status;
		
	public HandOffBatchDispatch() {
		super();
	}

	public Date getDispatchTime() {
		return dispatchTime;
	}

	public boolean isComplete() {
		return status != null && status.equals(EnumHandOffDispatchStatus.COMPLETE);
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public EnumHandOffDispatchStatus getStatus() {
		return status;
	}

	public void setStatus(EnumHandOffDispatchStatus status) {
		this.status = status;
	}
	
	
}
