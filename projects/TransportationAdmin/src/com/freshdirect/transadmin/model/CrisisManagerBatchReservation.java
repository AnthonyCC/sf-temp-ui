package com.freshdirect.transadmin.model;

import com.freshdirect.routing.model.ReservationModel;

public class CrisisManagerBatchReservation extends ReservationModel implements ICrisisManagerBatchReservation {
	
	private String batchId;
	private boolean isException;
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public boolean isException() {
		return isException;
	}
	public void setException(boolean isException) {
		this.isException = isException;
	}
	
	public CrisisManagerBatchReservation() {
		super();		
	}
}
