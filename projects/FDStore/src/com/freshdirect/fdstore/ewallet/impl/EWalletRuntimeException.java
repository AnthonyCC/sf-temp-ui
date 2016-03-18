package com.freshdirect.fdstore.ewallet.impl;

import com.freshdirect.framework.core.RuntimeExceptionSupport;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EWalletRuntimeException extends RuntimeExceptionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2292281336190939663L;

	public EWalletRuntimeException() {
		super();
	}

	public EWalletRuntimeException(String message) {
		super(message);
	}

	public EWalletRuntimeException(Exception ex) {
		super(ex);
	}

	public EWalletRuntimeException(Exception ex, String message) {
		super(ex, message);
	}
}
