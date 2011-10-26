package com.freshdirect.transadmin.model;

public class CrisisManagerBatchDeliverySlot extends TimeslotModel implements ICrisisManagerBatchDeliverySlot{
	
	private String batchId;
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public CrisisManagerBatchDeliverySlot() {
		super();
	}	
}
