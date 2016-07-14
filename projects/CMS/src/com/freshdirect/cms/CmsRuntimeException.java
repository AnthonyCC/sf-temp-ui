/*
 * Created on Nov 16, 2004
 */
package com.freshdirect.cms;

/**
 * Generic CMS Runtime Exception base class.
 */
public class CmsRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -5966114054463425031L;

    public CmsRuntimeException() {
		super();
	}

	public CmsRuntimeException(String message) {
		super(message);
	}

	public CmsRuntimeException(Throwable throwable) {
		super(throwable);
	}

	public CmsRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}