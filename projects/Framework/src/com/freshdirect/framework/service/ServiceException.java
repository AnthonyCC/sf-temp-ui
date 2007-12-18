/*
 * $Workfile: ServiceException.java$
 *
 * $Date: 8/27/2001 9:37:56 AM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.service;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception for service related errors.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */ 
public class ServiceException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public ServiceException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public ServiceException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public ServiceException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public ServiceException(Exception ex, String message) {
        super(ex, message);
    }

}
