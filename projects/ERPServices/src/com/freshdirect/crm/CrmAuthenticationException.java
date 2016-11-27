package com.freshdirect.crm;

public class CrmAuthenticationException extends Exception {

	private final String reason;

	public CrmAuthenticationException(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return this.reason;
	}

}
