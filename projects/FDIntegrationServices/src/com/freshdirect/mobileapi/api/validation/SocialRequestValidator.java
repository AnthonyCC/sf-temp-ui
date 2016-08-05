package com.freshdirect.mobileapi.api.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.mobileapi.api.data.request.SocialLoginMessageRequest;

@Component
public class SocialRequestValidator implements Validator {

    @Autowired
    private ValidationService validationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SocialLoginMessageRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SocialLoginMessageRequest socialRequest = (SocialLoginMessageRequest) target;

        if (!errors.hasErrors() && socialRequest == null) {
            errors.reject("OBJECT_IS_NULL", "Given request is invalid");
        }

        if (!errors.hasErrors()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "connection_token", "BAD_CONNECTION_TOKEN", "Connection token must not be null or whitespace");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "redirect_url", "BAD_REDIRECT_URL", "Redirect URL must not be null or whitespace");
        }

        if (errors.hasErrors()) {
            validationService.createErrorResults(errors);
        }

    }

}
