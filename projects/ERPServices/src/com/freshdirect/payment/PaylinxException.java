/*
 * PaylinxException.java
 *
 * Created on January 11, 2002, 2:49 PM
 */

package com.freshdirect.payment;

/**
 *
 * @author  knadeem
 * @version 
 */
import com.freshdirect.framework.core.ExceptionSupport;

public class PaylinxException extends ExceptionSupport {

	/** Creates new ErpDuplicateUserIdException */
    public PaylinxException() {
		super();
    }
	
	/** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public PaylinxException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public PaylinxException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public PaylinxException(Exception ex, String message) {
        super(ex, message);
    }

}
