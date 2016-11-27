package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.List;

public class TriggerCrisisManagerResult implements Serializable {
	
	private String crisisMngBatchId;
	private List<String> messages;
	
	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public String getCrisisMngBatchId() {
		return crisisMngBatchId;
	}

	public void setCrisisMngBatchId(String crisisMngBatchId) {
		this.crisisMngBatchId = crisisMngBatchId;
	}

		
	
}
