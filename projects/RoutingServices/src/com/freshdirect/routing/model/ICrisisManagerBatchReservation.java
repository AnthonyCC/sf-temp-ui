package com.freshdirect.routing.model;

public interface ICrisisManagerBatchReservation extends IReservationModel {
	
	public String getBatchId();
	public void setBatchId(String batchId);
	
	public boolean isException();
	public void setException(boolean isException);
	
}
