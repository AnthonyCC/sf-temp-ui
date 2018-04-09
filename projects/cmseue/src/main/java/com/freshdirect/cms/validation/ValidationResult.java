package com.freshdirect.cms.validation;

import com.freshdirect.cms.contentvalidation.validator.Validator;
import com.freshdirect.cms.core.domain.ContentKey;

public class ValidationResult {

    private ContentKey validatedObject;
    private String message;
    private ValidationResultLevel validationResultLevel;
    private Class<? extends Validator> validatorClass;

    public ValidationResult(ContentKey validatedObject, String message, ValidationResultLevel resultLevel, Class<? extends Validator> validatorClass) {
        this.validatedObject = validatedObject;
        this.message = message;
        this.validationResultLevel = resultLevel;
        this.validatorClass = validatorClass;
    }

    public ContentKey getValidatedObject() {
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
