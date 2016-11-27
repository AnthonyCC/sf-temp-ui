/*
 * $Workfile: BapiAbapException.java$
 *
 * $Date: 1/31/2002 7:38:33 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

/**
 * Exception for SAP application-level errors.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */ 
public class BapiAbapException extends BapiException {
    
    /**
     * Default constructor.
     */    
    public BapiAbapException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public BapiAbapException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public BapiAbapException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public BapiAbapException(Exception ex, String message) {
        super(ex, message);
    }

}
