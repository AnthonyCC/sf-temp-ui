package com.freshdirect.fdstore;

/**
 * Exception signalling some kind of problem accessing a resource
 * (eg. remote objects) or unexpected problems occuring in these
 * remote resources (eg. database access problems, etc).
 */ 
public class FDResourceException extends FDException {
    
	private static final long	serialVersionUID	= -3926618449641623809L;
    private boolean warningOnly = false;

	/**
     * Default constructor.
     */    
    public FDResourceException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public FDResourceException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public FDResourceException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public FDResourceException(Exception ex, String message) {
        super(ex, message);
    }

    
    /**
     * Creates an exception with a custom message and a wrapped exception with a warningOnly flag
     *
     * @param ex
     *            the wrapped exception
     * @param message
     *            a custom message
     * @param warningOnly
     *            decides whether exception is handled as a warning during checkout
     */
    public FDResourceException(Exception ex, String message, boolean warningOnly) {
        super(ex, message);
        this.warningOnly = warningOnly;
    }

    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex
     *            the wrapped exception
     * @param message
     *            a custom message
     */    
    public FDResourceException(String message, Exception ex) {
        super(ex, message);
    }

    /**
     * Gets whether the warningOnlyFlag is set.
     */
   public boolean isWarningOnly() {
        return warningOnly;
    }


}
