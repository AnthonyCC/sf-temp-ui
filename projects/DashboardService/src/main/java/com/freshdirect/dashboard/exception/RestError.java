package com.freshdirect.dashboard.exception;

import java.util.HashMap;
import java.util.Map;

public class RestError {
		
	private int httpStatus;
	private int serviceId;
	private int errorCode;
	private String debugMessage;
	private Map<String, String> messageArgs = new HashMap<String, String>();
	
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getDebugMessage() {
		return debugMessage;
	}
	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}
	
	public Map<String, String> getMessageArgs() {
		return messageArgs;
	}
	public void setMessageArgs(Map<String, String> messageArgs) {
		this.messageArgs = messageArgs;
	}
	
	public BaseException transformRestError() {
		return ServiceEnum.createServiceException(this);
	}

}
