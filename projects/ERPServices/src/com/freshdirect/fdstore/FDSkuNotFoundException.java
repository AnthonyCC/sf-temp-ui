package com.freshdirect.fdstore;

/**
 * Exception when a SKU is not found.
 */ 
public class FDSkuNotFoundException extends FDException {
    
    private static final long serialVersionUID = 3615117801812236583L;

    /**
     * Default constructor.
     */    
    public FDSkuNotFoundException() {
        super();
    }
    
    /** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public FDSkuNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public FDSkuNotFoundException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public FDSkuNotFoundException(Exception ex, String message) {
        super(ex, message);
    }

}
