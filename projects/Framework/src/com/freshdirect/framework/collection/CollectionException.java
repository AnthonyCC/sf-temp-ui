package com.freshdirect.framework.collection;

import com.freshdirect.framework.core.RuntimeExceptionSupport;

/**
 * Exception thrown by framework collection classes to indicate
 * a problem either retrieving an item or modifying the collection.
 *
 * @version $Revision: 3$
 * @author $Author: Viktor Szathmary$
 */ 
public class CollectionException extends RuntimeExceptionSupport {
    
	private static final long	serialVersionUID	= -8133474831507572816L;

	/**
     * Default constructor.
     */    
    public CollectionException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public CollectionException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public CollectionException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public CollectionException(Exception ex, String message) {
        super(ex, message);
    }

}


