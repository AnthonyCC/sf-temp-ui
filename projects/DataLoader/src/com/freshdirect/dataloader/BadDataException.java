/*
 * $Workfile: BadDataException.java$
 *
 * $Date: 8/14/2001 11:46:31 AM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader;

import com.freshdirect.framework.core.ExceptionSupport;

/** 
 * an exception that indicates something was wrong with the
 * raw data a loader component interprets
 *
 * @version $Revision: 2$
 * @author $Author: Mike Rose$
 */
public class BadDataException extends ExceptionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** default constructor
     */
    public BadDataException() {
        super();
    }
    
    /** create a BadDataException with a custom message
     * @param msg the custom message
     */    
    public BadDataException(String msg) {
        super(msg);
    }
    
    /** creates a BadDataException that wraps another exception
     * @param e the exception to wrap
     */    
    public BadDataException(Exception e) {
        super(e);
    }
    
    /** creates a BadDataException with an exception to wrap and a custom message
     * @param e an exception to wrap
     * @param msg a custom message
     */    
    public BadDataException(Exception e, String msg) {
        super(e, msg);
    }

}
