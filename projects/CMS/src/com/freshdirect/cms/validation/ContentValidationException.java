/*
 * Created on May 4, 2005
 */
package com.freshdirect.cms.validation;

import com.freshdirect.cms.CmsRuntimeException;

/**
 * Runtime exception to represent a validation error with the collected messages.
 */
public class ContentValidationException extends CmsRuntimeException {

    private static final long serialVersionUID = -8870891797271792358L;
    private final ContentValidationDelegate delegate;

	public ContentValidationException(ContentValidationDelegate delegate) {
		super();
		this.delegate = delegate;
	}

	public ContentValidationDelegate getDelegate() {
		return delegate;
	}

	public String toString() {
		return delegate.toString();
	}
}
