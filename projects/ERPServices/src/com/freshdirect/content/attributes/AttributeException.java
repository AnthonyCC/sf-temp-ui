/*
 * $Workfile: AttributeException.java$
 *
 * $Date: 8/20/2001 8:30:00 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception for attribute related errors.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */ 
public class AttributeException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public AttributeException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public AttributeException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public AttributeException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public AttributeException(Exception ex, String message) {
        super(ex, message);
    }

}
