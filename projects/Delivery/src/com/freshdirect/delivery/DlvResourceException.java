/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.delivery;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception signalling some kind of problem accessing a resource
 * (eg. remote objects) or unexpected problems occuring in these
 * remote resources (eg. database access problems, etc).
 *
 * @version $Revision$
 * @author $Author$
 */ 
public class DlvResourceException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public DlvResourceException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public DlvResourceException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public DlvResourceException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public DlvResourceException(Exception ex, String message) {
        super(ex, message);
    }

}
