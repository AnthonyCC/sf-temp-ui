/*
 * ErpSaleNotFoundException.java
 *
 * Create Date: 5/23/02 3:49:05 PM
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 *
 * @author knadeem
 * @version
 */ 
public class ErpSaleNotFoundException extends ExceptionSupport {
    
    /**
     * Default constructor.
     */    
    public ErpSaleNotFoundException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public ErpSaleNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public ErpSaleNotFoundException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public ErpSaleNotFoundException(Exception ex, String message) {
        super(ex, message);
    }

}
