package com.freshdirect.cmsadmin.exception;

import java.util.Map;

/**
 * The exception that is thrown when a validation error happens.
 */
public class ValidationException extends BusinessException {

    private static final long serialVersionUID = -2752574169491126921L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Exception e) {
        super(e);
    }

    public ValidationException(Exception e, Map<String, Object> errors) {
        super(e, errors);
    }

    public ValidationException(String message, Map<String, Object> errors) {
        super(message, errors);
    }
}
