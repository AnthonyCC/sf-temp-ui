package com.freshdirect.webapp.ajax.expresscheckout.data;

import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;

public class FormDataResponse {

    private SubmitForm submitForm;
    private ValidationResult validationResult;
    private boolean showSOProduct;

    public SubmitForm getSubmitForm() {
        return submitForm;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void setFormSubmit(SubmitForm submitForm) {
        this.submitForm = submitForm;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

	public boolean isShowSOProduct() {
		return showSOProduct;
	}

	public void setShowSOProduct(boolean showSOProduct) {
		this.showSOProduct = showSOProduct;
	}

}
