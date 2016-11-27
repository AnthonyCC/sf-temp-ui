/*
 * Created on Nov 16, 2004
 */
package com.freshdirect.cms;

/**
 * Generic CMS Exception base class.
 */
public class CmsException extends Exception {

	public CmsException() {
		super();
	}

	public CmsException(String message) {
		super(message);
	}

	public CmsException(Throwable throwable) {
		super(throwable);
	}

	public CmsException(String message, Throwable throwable) {
		super(message, throwable);
	}

}