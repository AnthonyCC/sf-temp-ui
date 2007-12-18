/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception for fdstore related errors.
 *
 * @version $Revision$
 * @author $Author$
 */ 
public class FDException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public FDException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public FDException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public FDException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public FDException(Exception ex, String message) {
        super(ex, message);
    }

}
