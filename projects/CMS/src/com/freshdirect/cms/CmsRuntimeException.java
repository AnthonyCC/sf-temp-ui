/*
 * Created on Nov 16, 2004
 */
package com.freshdirect.cms;

/**
 * Generic CMS Runtime Exception base class.
 */
public class CmsRuntimeException extends RuntimeException {

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