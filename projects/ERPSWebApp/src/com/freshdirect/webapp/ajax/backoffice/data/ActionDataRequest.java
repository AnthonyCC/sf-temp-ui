package com.freshdirect.webapp.ajax.backoffice.data;

import java.io.Serializable;
import java.util.Map;


public class ActionDataRequest implements Serializable{


	private Map<String, Object> requestData;

	private String action;


	public Map<String, Object> getRequestData() {
		return requestData;
	}

	public void setRequestData(Map<String, Object> requestData) {
		this.requestData = requestData;
	}

}
