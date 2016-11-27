package com.freshdirect.fdstore.coremetrics.service;

import java.io.Serializable;

public class CdfProcessResult implements Serializable {
	private static final long serialVersionUID = 3593795057368278270L;
	private boolean success;
	private String error;
	
	public CdfProcessResult(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getError() {
		return error;
	}
}
