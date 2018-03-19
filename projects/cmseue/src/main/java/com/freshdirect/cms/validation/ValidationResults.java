package com.freshdirect.cms.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.contentvalidation.validator.Validator;
import com.freshdirect.cms.core.domain.ContentKey;

public class ValidationResults {

    private List<ValidationResult> validationResults = new ArrayList<ValidationResult>();

    public void addValidationResult(ValidationResult validationResult) {
        validationResults.add(validationResult);
    }

    public void addValidationResult(ContentKey validatedObject, String message, ValidationResultLevel validationResultLevel, Class<? extends Validator> validatorClass) {
        validationResults.add(new ValidationResult(validatedObject, message, validationResultLevel, validatorClass));
    }

    public void addAll(ValidationResults validationResults) {
        this.validationResults.addAll(validationResults.getValidationResults());
    }

    public void addAll(List<ValidationResult> validationResults) {
        this.validationResults.addAll(validationResults);
    }

    public boolean hasError() {
        return hasResultForFailureLevel(ValidationResultLevel.ERROR);
    }

    public boolean hasWarning() {
        return hasResultForFailureLevel(ValidationResultLevel.WARNING);
    }

    public boolean hasInfo() {
        return hasResultForFailureLevel(ValidationResultLevel.INFO);
    }

    public List<ValidationResult> getValidationResults() {
        return Collections.unmodifiableList(validationResults);
    }

    public List<ValidationResult> getValidationResultsForLevel(ValidationResultLevel validationResultLevel) {
        List<ValidationResult> resultsForLevel = new ArrayList<ValidationResult>();
        for (ValidationResult validationResult : validationResults) {
            if (validationResult.getFailureLevel() == validationResultLevel) {
                resultsForLevel.add(validationResult);
            }
        }
        return resultsForLevel;
    }

    private boolean hasResultForFailureLevel(ValidationResultLevel level) {
        boolean hasLevel = false;
        for (ValidationResult validationResult : validationResults) {
            if (validationResult.getFailureLevel() == level) {
                hasLevel = true;
                break;
            }
        }
        return hasLevel;
    }

    @Override
    public String toString() {
        return validationResults.toString();
    }
}
