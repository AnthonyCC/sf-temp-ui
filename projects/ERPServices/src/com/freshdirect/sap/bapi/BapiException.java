/*
 * $Workfile: BapiException.java$
 *
 * $Date: 8/27/2001 7:19:01 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception for BAPI related errors.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */ 
public class BapiException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public BapiException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public BapiException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public BapiException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public BapiException(Exception ex, String message) {
        super(ex, message);
    }

}
