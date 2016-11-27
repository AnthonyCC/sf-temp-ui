/*
 * $Workfile: ErpTransactionException.java$
 *
 * $Date: 11/30/2001 12:56:56 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception for transaction-related errors: business rule violations.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */ 
public class ErpTransactionException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public ErpTransactionException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public ErpTransactionException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public ErpTransactionException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public ErpTransactionException(Exception ex, String message) {
        super(ex, message);
    }

}
