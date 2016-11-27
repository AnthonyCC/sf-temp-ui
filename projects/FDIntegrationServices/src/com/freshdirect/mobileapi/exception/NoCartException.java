package com.freshdirect.mobileapi.exception;

/**
 * Recoverable validation error that should be actively checked
 * and process as part of the normal process.
 * 
 * @author Rob
 *
 */
public class NoCartException extends Exception {

    private static final long serialVersionUID = -2525636465388260366L;

    public NoCartException() {
        super();
    }

    public NoCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCartException(String message) {
        super(message);
    }

    public NoCartException(Throwable cause) {
        super(cause);
    }

}
