package com.freshdirect.rules;

import com.freshdirect.framework.core.RuntimeExceptionSupport;

/**
 * @author knadeem Date Apr 11, 2005
 */
public class RulesRuntimeException extends RuntimeExceptionSupport {

	public RulesRuntimeException() {
		super();
	}

	public RulesRuntimeException(String message) {
		super(message);
	}

	public RulesRuntimeException(Exception ex) {
		super(ex);
	}

	public RulesRuntimeException(Exception ex, String message) {
		super(ex, message);
	}

}
