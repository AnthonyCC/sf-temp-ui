package com.freshdirect.fdstore;


/**
 * Exception for not allowing users to access on the site
 */ 
public class FDActionNotAllowedException extends FDException {
    
	private static final long serialVersionUID = 1L;


	/**
     * Default constructor.
     */    
    public FDActionNotAllowedException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public FDActionNotAllowedException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public FDActionNotAllowedException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public FDActionNotAllowedException(Exception ex, String message) {
        super(ex, message);
    }

    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public FDActionNotAllowedException(String message, Exception ex) {
        super(ex, message);
    }
}
