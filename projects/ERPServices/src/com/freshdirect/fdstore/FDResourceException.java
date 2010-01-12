/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

/**
 * Exception signalling some kind of problem accessing a resource
 * (eg. remote objects) or unexpected problems occuring in these
 * remote resources (eg. database access problems, etc).
 *
 * @version $Revision$
 * @author $Author$
 */ 
public class FDResourceException extends FDException {
    
    /**
     * Default constructor.
     */    
    public FDResourceException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public FDResourceException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public FDResourceException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public FDResourceException(Exception ex, String message) {
        super(ex, message);
    }

    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public FDResourceException(String message, Exception ex) {
        super(ex, message);
    }
}
