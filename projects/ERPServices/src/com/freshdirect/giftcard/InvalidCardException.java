/*
 * ErpSaleNotFoundException.java
 *
 * Create Date: 5/23/02 3:49:05 PM
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.giftcard;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 *
 * @author skrishnasamy
 * @version
 */ 
public class InvalidCardException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public InvalidCardException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public InvalidCardException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public InvalidCardException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public InvalidCardException(Exception ex, String message) {
        super(ex, message);
    }

}
