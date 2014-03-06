package com.freshdirect.dashboard.exception;

public interface ErrorCodeEnum {

	int getErrorCode();

	String getName();

	int getServiceId();

	String getMessageKey();

	String getDefaultMessage();

}
