/*
 * $Workfile: PricingException.java$
 *
 * $Date: 8/8/2001 1:27:31 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.pricing;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * Exception for pricing related errors.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */ 
public class PricingException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public PricingException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public PricingException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public PricingException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public PricingException(Exception ex, String message) {
        super(ex, message);
    }

}
