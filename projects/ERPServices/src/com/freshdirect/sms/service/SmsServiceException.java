package com.freshdirect.sms.service;


public class SmsServiceException extends Exception {
	
	private static final long serialVersionUID = -9063750142096839385L;
	
	private String details;
	
	public SmsServiceException() {
		super();
	}
	
	public SmsServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public SmsServiceException(String message) {
		super(message);
	}

	public SmsServiceException(Throwable cause) {
		super(cause);
	}

	public SmsServiceException(String message, String details) {
		super(message);
		this.details = details;
	}
	
	public String getDetails() {
		return details;
	}
	
	@Override
	public String toString() {
		return "SmsServiceException [message=" + this.getMessage()
				+ ". details=" + details + "]";
	}
	
}
