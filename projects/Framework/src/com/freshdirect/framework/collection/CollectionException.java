/*
 * $Workfile: CollectionException.java$
 *
 * $Date: 8/8/2001 10:19:15 AM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.collection;

import com.freshdirect.framework.core.RuntimeExceptionSupport;

/**
 * Exception thrown by framework collection classes to indicate
 * a problem either retreiving an item or modifying the collection.
 *
 * @version $Revision: 3$
 * @author $Author: Viktor Szathmary$
 */ 
public class CollectionException extends RuntimeExceptionSupport {
    
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


