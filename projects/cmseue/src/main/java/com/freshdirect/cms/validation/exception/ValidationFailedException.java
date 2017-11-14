package com.freshdirect.cms.validation.exception;

import java.util.List;

import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResults;

public class ValidationFailedException extends RuntimeException {

    private static final long serialVersionUID = 7879346569294443663L;

    private final ValidationResults validationResults;

    public ValidationFailedException(ValidationResults validationResults) {
        this.validationResults = validationResults;
    }

    public ValidationFailedException(String message, ValidationResults validationResults) {
        super(message);
        this.validationResults = validationResults;
    }

    public List<ValidationResult> getValidationResults() {
        return validationResults.getValidationResults();
    }

}
