package com.freshdirect.webapp.ajax.expresscheckout.data;

import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;

public class FormDataResponse {

	private SubmitForm submitForm;
	private ValidationResult validationResult;

	public SubmitForm getSubmitForm() {
		return submitForm;
	}

	public void setFormSubmit(SubmitForm submitForm) {
		this.submitForm = submitForm;
	}

	public ValidationResult getValidationResult() {
		return validationResult;
	}

	public void setValidationResult(ValidationResult validationResult) {
		this.validationResult = validationResult;
	}

}
