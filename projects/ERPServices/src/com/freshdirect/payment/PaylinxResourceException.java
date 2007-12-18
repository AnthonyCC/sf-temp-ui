/*
 * PaylinxResourceException.java
 *
 * Created on May 11, 2002, 3:49 PM
 */

package com.freshdirect.payment;

/**
 *
 * @author  knadeem
 * @version 
 */

import com.freshdirect.framework.core.ExceptionSupport;

public class PaylinxResourceException extends ExceptionSupport {
	
	/** Creates new PaylinxResponseException */
    public PaylinxResourceException() {
		super();
    }
	
	/** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public PaylinxResourceException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public PaylinxResourceException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public PaylinxResourceException(Exception ex, String message) {
        super(ex, message);
    }

}
