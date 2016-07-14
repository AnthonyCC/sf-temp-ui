package com.freshdirect.cmsadmin.exception;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * The base exception for CMS-Admin.
 *
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -2752574119991126929L;

    private final Map<String, Object> errors;

    public BusinessException() {
        super();
        errors = new HashMap<String, Object>();
    }

    public BusinessException(String message) {
        super(message);
        errors = new HashMap<String, Object>();
    }

    public BusinessException(Exception e) {
        super(e);
        errors = new HashMap<String, Object>();
    }

    public BusinessException(Exception e, Map<String, Object> errors) {
        super(e);
        this.errors = errors;
    }

    public BusinessException(String message, Map<String, Object> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }
}
