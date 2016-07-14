package com.freshdirect.cmsadmin.config.security.dto;

import java.util.Map;

/**
 * POJO for authentication failure data.
 */
public class AuthenticationFailureData {

    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

}
