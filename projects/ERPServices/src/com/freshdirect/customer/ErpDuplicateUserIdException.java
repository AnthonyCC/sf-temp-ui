/*
 * ErpDuplicateUserIdException.java
 *
 * Created on January 5, 2002, 5:33 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version 
 */
import com.freshdirect.framework.core.ExceptionSupport;

public class ErpDuplicateUserIdException extends ExceptionSupport {

	/** Creates new ErpDuplicateUserIdException */
    public ErpDuplicateUserIdException() {
		super();
    }
	
	/** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public ErpDuplicateUserIdException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public ErpDuplicateUserIdException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public ErpDuplicateUserIdException(Exception ex, String message) {
        super(ex, message);
    }

}
