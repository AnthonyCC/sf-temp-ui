package com.freshdirect.webapp.ajax.expresscheckout.validation.data;

import com.freshdirect.framework.webapp.ActionError;

public class ValidationError {

	private String name;
	private String error;

	public ValidationError() {
	}

	public ValidationError(String name, String error) {
		this.name = name;
		this.error = error;
	}
	
	public ValidationError(ActionError error) {
		if (error != null) {
			name = error.getType();
			this.error = error.getDescription();
		}
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
