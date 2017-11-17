package com.freshdirect.cms.validation;

import com.freshdirect.cms.validation.validator.Validator;

public class ValidationResult {

    private Object validatedObject;
    private String message;
    private ValidationResultLevel validationResultLevel;
    private Class<? extends Validator> validatorClass;

    public ValidationResult(Object validatedObject, String message, ValidationResultLevel resultLevel, Class<? extends Validator> validatorClass) {
        this.validatedObject = validatedObject;
        this.message = message;
        this.validationResultLevel = resultLevel;
        this.validatorClass = validatorClass;
    }

    public Object getValidatedObject() {
        return validatedObject;
    }

    public String getMessage() {
        return message;
    }

    public ValidationResultLevel getFailureLevel() {
        return validationResultLevel;
    }

    public Class<? extends Validator> getValidatorClass() {
        return validatorClass;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{").append(validationResultLevel).append("}[ ").append(validatedObject).append(" - ").append(message).append("] by ")
                .append(validatorClass.getCanonicalName());
        return stringBuilder.toString();
    }

}
