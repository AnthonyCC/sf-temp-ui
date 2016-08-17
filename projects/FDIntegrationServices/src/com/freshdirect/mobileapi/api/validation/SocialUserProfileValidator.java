package com.freshdirect.mobileapi.api.validation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SocialUserProfileValidator implements Validator {

    private static final String[] PROPERTIES = { "identityToken", "userToken", "userid", "email", "firstName", "lastName", "displayName", "preferredUsername", "domain", "provider",
            "emailVerified" };

    @Autowired
    private ValidationDtoConverter validationDtoConverter;

    @Override
    public boolean supports(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        @SuppressWarnings("unchecked")
        Map<String, String> socialUserProfile = (Map<String, String>) target;

        if (!errors.hasErrors() && socialUserProfile == null) {
            errors.reject("OBJECT_IS_NULL", "Given token is invalid");
        }

        if (!errors.hasErrors()) {
            for (String property : PROPERTIES) {
                String value = socialUserProfile.get(property);
                if (value == null) {
                    errors.reject(property + "_IS_NULL", property + " is null");
                }
            }
        }

        if (errors.hasErrors()) {
            validationDtoConverter.createErrorResults(errors);
        }

    }

}
