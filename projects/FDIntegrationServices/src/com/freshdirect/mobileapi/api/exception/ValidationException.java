package com.freshdirect.mobileapi.api.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * The exception that is thrown when a validation error happens.
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -2429139292652944846L;

    private final Map<String, String> errors;

    public ValidationException() {
        super();
        errors = new HashMap<String, String>();
    }

    public ValidationException(String message) {
        super(message);
        errors = new HashMap<String, String>();
    }

    public ValidationException(Exception e) {
        super(e);
        errors = new HashMap<String, String>();
    }

    public ValidationException(Exception e, Map<String, String> errors) {
        super(e);
        this.errors = errors;
    }

    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
