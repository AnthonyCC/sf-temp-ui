/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader;

import com.freshdirect.framework.core.ExceptionSupport;

/** an exception that indicates a loader component ran into a problem
 * creating or updating entities in the system
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class LoaderException extends ExceptionSupport {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BadDataException[] badDataExceptions;

    /** Creates new BadDataException */
    public LoaderException() {
        super();
    }
    
    /** creates a new LoaderException
     * @param msg a custom message
     */    
    public LoaderException(String msg) {
        super(msg);
    }
    
    /** creates a LoaderException that wraps another exception
     * @param e an exception to wrap
     */    
    public LoaderException(Exception e) {
        super(e);
    }
    
    /** creates a LoaderException that wraps another exception and
     * provides a custom message
     * @param e the exception to wrap
     * @param msg a custom message
     */    
    public LoaderException(Exception e, String msg) {
        super(e, msg);
    }

    public LoaderException(BadDataException[] badDataExceptions, String msg) {
        super(msg);
        this.badDataExceptions = badDataExceptions;
    }
    
    public BadDataException[] getBadDataExceptions() {
    	return this.badDataExceptions;
    }


}
