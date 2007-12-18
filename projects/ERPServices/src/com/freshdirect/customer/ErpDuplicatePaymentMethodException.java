/*
 * ErpDuplicatePaymentMethodException.java
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

public class ErpDuplicatePaymentMethodException extends ExceptionSupport {

	/** Creates new ErpDuplicateUserIdException */
    public ErpDuplicatePaymentMethodException() {
		super();
    }

	/**
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */
    public ErpDuplicatePaymentMethodException(String message) {
        super(message);
    }

    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */
    public ErpDuplicatePaymentMethodException(Exception ex) {
        super(ex);
    }

    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */
    public ErpDuplicatePaymentMethodException(Exception ex, String message) {
        super(ex, message);
    }

}
