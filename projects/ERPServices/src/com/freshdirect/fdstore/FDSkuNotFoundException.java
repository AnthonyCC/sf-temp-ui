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
 * Exception when a SKU is not found.
 *
 * @version $Revision$
 * @author $Author$
 */ 
public class FDSkuNotFoundException extends FDException {
    
    /**
     * Default constructor.
     */    
    public FDSkuNotFoundException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public FDSkuNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public FDSkuNotFoundException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public FDSkuNotFoundException(Exception ex, String message) {
        super(ex, message);
    }

}
