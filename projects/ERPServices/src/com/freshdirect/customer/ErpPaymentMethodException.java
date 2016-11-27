/*
 * Created on May 13, 2005
 */
package com.freshdirect.customer;

import com.freshdirect.framework.core.ExceptionSupport;

/**
 * @author jng
 */
public class ErpPaymentMethodException extends ExceptionSupport {
    public ErpPaymentMethodException() {
		super();
    }

	/**
     * Creates an exception with a custom message.
     *
     * @param message a custom message
     */
    public ErpPaymentMethodException(String message) {
        super(message);
    }

    /**
     * Creates an exception that wraps another exception.
     *
     * @param ex the wrapped exception
     */
    public ErpPaymentMethodException(Exception ex) {
        super(ex);
    }

    /**
     * Creates an exception with a custom message and a wrapped exception.
     *
     * @param ex the wrapped exception
     * @param message a custom message
     */
    public ErpPaymentMethodException(Exception ex, String message) {
        super(ex, message);
    }

}
