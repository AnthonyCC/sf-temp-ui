package com.freshdirect.routing.model;

import java.io.Serializable;
import java.util.List;

public class TriggerHandOffResult implements Serializable {
	
	private String handOffBatchId;
	private List<String> messages;
	
	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public String getHandOffBatchId() {
		return handOffBatchId;
	}

	public void setHandOffBatchId(String handOffBatchId) {
		this.handOffBatchId = handOffBatchId;
	}
	
	
}
