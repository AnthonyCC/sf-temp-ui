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
public class CardOnHoldException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public CardOnHoldException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public CardOnHoldException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public CardOnHoldException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public CardOnHoldException(Exception ex, String message) {
        super(ex, message);
    }

}
