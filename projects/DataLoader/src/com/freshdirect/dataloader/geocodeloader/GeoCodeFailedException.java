package com.freshdirect.dataloader.geocodeloader;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.framework.core.ExceptionSupport;

public class GeoCodeFailedException extends ExceptionSupport{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BadDataException[] badDataExceptions;
	
	  public GeoCodeFailedException() {
	        super();
	    }
	    
	    /** creates a new LoaderException
	     * @param msg a custom message
	     */    
	    public GeoCodeFailedException(String msg) {
	        super(msg);
	    }
	    
	    /** creates a LoaderException that wraps another exception
	     * @param e an exception to wrap
	     */    
	    public GeoCodeFailedException(Exception e) {
	        super(e);
	    }
	    
	    /** creates a LoaderException that wraps another exception and
	     * provides a custom message
	     * @param e the exception to wrap
	     * @param msg a custom message
	     */    
	    public GeoCodeFailedException(Exception e, String msg) {
	        super(e, msg);
	    }

	    public GeoCodeFailedException(BadDataException[] badDataExceptions, String msg) {
	        super(msg);
	        this.badDataExceptions = badDataExceptions;
	    }
	    
	    public BadDataException[] getBadDataExceptions() {
	    	return this.badDataExceptions;
	    }
	
}
