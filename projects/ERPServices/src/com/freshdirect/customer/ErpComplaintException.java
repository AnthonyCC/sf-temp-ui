/*
 * $Workfile: ErpComplaintException.java$
 *
 * $Date: 1/7/2002 7:28:29 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception for complaint-related errors: business rule violations.
 *
 * @version $Revision: 1$
 * @author $Author: Jonathan McCarter$
 */
public class ErpComplaintException extends ExceptionSupport {

    /**
     * Default constructor.
     */
    public ErpComplaintException() {
        super();
    }

    /**
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */
    public ErpComplaintException(String message) {
        super(message);
    }

    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */
    public ErpComplaintException(Exception ex) {
        super(ex);
    }

    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */
    public ErpComplaintException(Exception ex, String message) {
        super(ex, message);
    }

}
