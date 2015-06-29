package com.freshdirect.webapp.ajax.expresscheckout.validation.data;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

	private String fdform;
	private List<ValidationError> errors = new ArrayList<ValidationError>();

	public String getFdform() {
		return fdform;
	}

	public void setFdform(String fdform) {
		this.fdform = fdform;
	}

	public List<ValidationError> getErrors() {
		return errors;
	}

	public void setErrors(List<ValidationError> errors) {
		this.errors = errors;
	}

}
