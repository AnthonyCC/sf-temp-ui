package com.freshdirect.transadmin.model;

import com.freshdirect.routing.model.IReservationModel;

public interface ICrisisManagerBatchReservation extends IReservationModel {
	
	public String getBatchId();
	public void setBatchId(String batchId);
	
	public boolean isException();
	public void setException(boolean isException);
	
}
