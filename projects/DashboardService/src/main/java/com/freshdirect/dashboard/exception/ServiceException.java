package com.freshdirect.dashboard.exception;

import java.util.Map;

public class ServiceException extends BaseException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7741576224884028867L;

	public ServiceException(ErrorCodeEnum errorCode) {
		super();
		this.setErrorCode(errorCode);
	}
	
	public ServiceException(ErrorCodeEnum errorCode, String debugMessage) {
		super();
		this.setErrorCode(errorCode);
		this.setDebugMessage(debugMessage);
	}
	
	public ServiceException(ErrorCodeEnum errorCode, String debugMessage, 
			Map<String, String> messageArgs) {
		super();
		this.setErrorCode(errorCode);
		this.setDebugMessage(debugMessage);
		this.messageArgs = messageArgs;
	}

}
