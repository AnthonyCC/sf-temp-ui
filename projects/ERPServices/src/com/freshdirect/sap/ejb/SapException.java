/*
 * $Workfile: SapException.java$
 *
 * $Date: 9/25/2001 6:34:10 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.ejb;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 *
 *
 * @version $Revision: 2$
 * @author $Author: Viktor Szathmary$
 */
public class SapException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public SapException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public SapException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public SapException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public SapException(Exception ex, String message) {
        super(ex, message);
    }

}
