package com.freshdirect.fdstore.bazaarvoice.service;

import java.io.Serializable;

public class UploadFeedProcessResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5315673239463792603L;
	private boolean success;
	private String error;
	
	public UploadFeedProcessResult(boolean success, String error) {
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
