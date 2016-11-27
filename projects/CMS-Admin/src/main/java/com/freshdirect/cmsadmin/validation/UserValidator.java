package com.freshdirect.cmsadmin.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.cmsadmin.domain.User;

/**
 * The validator that handles error regarding the User entity.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserValidator implements Validator {

    @Autowired
    private ValidationService validationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (user == null) {
            errors.reject(null, "User cannot be found.");
        } else {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountName", "BAD_ACCOUNT_NAME", "AccountName must not be null or whitespace");
        }

        if (errors.hasErrors()) {
            validationService.createErrorResults(errors);
        }
    }
}
