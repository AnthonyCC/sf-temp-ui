package com.freshdirect.webapp.ajax.backoffice.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class ActionDataRequest implements Serializable{


	private Map<String, Object> requestData =new HashMap<String, Object>();

	private String action; 


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, Object> getRequestData() {
		return requestData;
	}

	public void setRequestData(Map<String, Object> requestData) {
		this.requestData = requestData;
	}

}
