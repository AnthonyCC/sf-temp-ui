package com.freshdirect.mobileapi.exception;

/**
 * Recoverable validation error that should be actively checked
 * and process as part of the normal process.
 * 
 * @author Rob
 *
 */
public class NoSessionException extends Exception {

    private static final long serialVersionUID = -4051795732683484979L;

    public NoSessionException() {
        super();
    }

    public NoSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSessionException(String message) {
        super(message);
    }

    public NoSessionException(Throwable cause) {
        super(cause);
    }

}
