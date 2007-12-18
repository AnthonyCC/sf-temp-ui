/*
 * ZoneNotFoundException.java
 *
 * Created on September 27, 2001, 3:09 PM
 */

package com.freshdirect.fdstore;

/**
 *
 * @author  knadeem
 * @version 
 */

public class FDZoneNotFoundException extends FDException{

    /**
     * Creates new <code>ZoneNotFoundException</code> without detail message.
     */
    public FDZoneNotFoundException() {
	super();
    }


    /**
     * Constructs an <code>ZoneNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FDZoneNotFoundException(String msg) {
        super(msg);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public FDZoneNotFoundException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public FDZoneNotFoundException(Exception ex, String message) {
        super(ex, message);
    }
}


