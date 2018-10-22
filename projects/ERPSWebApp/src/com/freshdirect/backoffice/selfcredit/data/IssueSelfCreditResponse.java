package com.freshdirect.backoffice.selfcredit.data;

import java.io.Serializable;

public class IssueSelfCreditResponse implements Serializable {

	private static final long serialVersionUID = -621761913141517633L;
	
	private String message;
	private boolean success;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
