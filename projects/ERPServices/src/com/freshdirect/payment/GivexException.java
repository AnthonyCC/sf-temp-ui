/*
 * PaylinxException.java
 *
 * Created on January 11, 2002, 2:49 PM
 */

package com.freshdirect.payment;

/**
 *
 * @author  skrishnasamy
 * @version 
 */
import com.freshdirect.framework.core.ExceptionSupport;

public class GivexException extends ExceptionSupport {
	
	public static final int ERROR_TIME_OUT = -1;
	public static final int ERROR_FAIL_OVER = -2;
	public static final int ERROR_GENERIC = -3;
	public static final int ERROR_CERT_NOT_EXIST = 2;
	public static final int ERROR_BALANCE = 9;
	public static final int ERROR_CERT_ON_HOLD = 27;
	public static final int ERROR_CERT_CANCELLED = 28;

	int errorCode;
	
	/** Creates new ErpDuplicateUserIdException */
    public GivexException() {
		super();
    }
	
	/** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public GivexException(String message) {
        super(message);
    }
    
	/** 
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */    
    public GivexException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */    
    public GivexException(Exception ex) {
        super(ex);
    }
    
    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public GivexException(Exception ex, String message) {
        super(ex, message);
    }

    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */    
    public GivexException(Exception ex, String message, int errorCode) {
        super(ex, message);
        this.errorCode = errorCode;
    }
    
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
