/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception for complaint-related errors: business rule violations.
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpInvalidPasswordException extends ExceptionSupport {

    /**
     * Default constructor.
     */
    public ErpInvalidPasswordException() {
        super();
    }

    /**
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */
    public ErpInvalidPasswordException(String message) {
        super(message);
    }

    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */
    public ErpInvalidPasswordException(Exception ex) {
        super(ex);
    }

    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */
    public ErpInvalidPasswordException(Exception ex, String message) {
        super(ex, message);
    }

}
