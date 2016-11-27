package com.freshdirect.cmsadmin.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * The default error that gets returned after an AJAX validation fails.
 *
 */
public class ErrorView {

    @JsonProperty("errors")
    private final Map<String, Object> errorMessages;

    /**
     * Constructor that sets the error message.
     *
     * @param message
     *            The map that contains the errors
     */
    public ErrorView(Map<String, Object> message) {
        this.errorMessages = message;
    }

    public Map<String, Object> getErrorMessages() {
        return errorMessages;
    }

}
