package com.freshdirect.temails;

import com.freshdirect.framework.core.RuntimeExceptionSupport;

/**
 * @author knadeem Date Apr 11, 2005
 */
public class TEmailRuntimeException extends RuntimeExceptionSupport {

	public TEmailRuntimeException() {
		super();
	}

	public TEmailRuntimeException(String message) {
		super(message);
	}

	public TEmailRuntimeException(Exception ex) {
		super(ex);
	}

	public TEmailRuntimeException(Exception ex, String message) {
		super(ex, message);
	}

}
