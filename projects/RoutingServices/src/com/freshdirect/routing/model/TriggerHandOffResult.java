package com.freshdirect.routing.model;

import java.io.Serializable;

public class TriggerHandOffResult implements Serializable {
	
	private String handOffBatchId;
	private String message;
	
	public TriggerHandOffResult() {
		super();
	}

	public TriggerHandOffResult(String handOffBatchId, String message) {
		super();
		this.handOffBatchId = handOffBatchId;
		this.message = message;
	}
	
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getHandOffBatchId() {
		return handOffBatchId;
	}

	public void setHandOffBatchId(String handOffBatchId) {
		this.handOffBatchId = handOffBatchId;
	}
	
	
}
