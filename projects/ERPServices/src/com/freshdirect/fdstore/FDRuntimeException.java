package com.freshdirect.fdstore;

import com.freshdirect.framework.core.RuntimeExceptionSupport;

public class FDRuntimeException extends RuntimeExceptionSupport {
	private static final long serialVersionUID = 7231842869252460970L;

	public FDRuntimeException() {
		super();
	}

	public FDRuntimeException(String message) {
		super(message);
	}

	public FDRuntimeException(Exception ex) {
		super(ex);
	}

	public FDRuntimeException(Exception ex, String message) {
		super(ex, message);
	}

}
