/*
 * ErpDuplicateAddressException.java
 *
 * Created on January 5, 2002, 5:33 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version
 */
import com.freshdirect.framework.core.ExceptionSupport;

public class ErpDuplicateAddressException extends ExceptionSupport {

	/** Creates new ErpDuplicateUserIdException */
    public ErpDuplicateAddressException() {
		super();
    }

	/**
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */
    public ErpDuplicateAddressException(String message) {
        super(message);
    }

    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */
    public ErpDuplicateAddressException(Exception ex) {
        super(ex);
    }

    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */
    public ErpDuplicateAddressException(Exception ex, String message) {
        super(ex, message);
    }

}
