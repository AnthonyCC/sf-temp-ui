package com.freshdirect.webapp.ajax.expresscheckout.validation.data;

import com.freshdirect.framework.webapp.ActionError;

public class ValidationError {

    private static final String TECHNICAL_DIFFICULTY_ERROR_NAME = "technical_difficulty";

	private String name;
	private String error;

	public ValidationError() {
	}

    public ValidationError(String error) {
        this(TECHNICAL_DIFFICULTY_ERROR_NAME, error);
    }

    public ValidationError(ActionError error) {
        this(error.getType(), error.getDescription());
    }

	public ValidationError(String name, String error) {
		this.name = name;
		this.error = error;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
