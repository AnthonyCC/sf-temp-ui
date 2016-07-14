package com.freshdirect.cmsadmin.validation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMethod;

import com.freshdirect.cmsadmin.domain.UserPersona;

/**
 *
 * The validator that handles error regarding the UserPersona entity.
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserPersonaValidator implements Validator {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ValidationService validationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserPersona.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserPersona userPersona = (UserPersona) target;
        switch (RequestMethod.valueOf(request.getMethod())) {
            case GET:
                if (userPersona == null) {
                    errors.reject(null, "User persona association cannot be found by given user id.");
                } else {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "BAD_USER_ID", "UserId must not be null or whitespace");
                }
                break;
            case PUT:
                break;
            case DELETE:
                if (userPersona == null) {
                    errors.reject(null, "User persona association cannot be found by given user id.");
                } else {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "BAD_USER_ID", "UserId must not be null or whitespace");
                }
                break;
            case POST:
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "BAD_USER_ID", "UserId must not be null or whitespace");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "persona.id", "BAD_PERSONA_ID", "Persona id must not be null or whitespace");
                break;
            default:
                break;
        }

        if (errors.hasErrors()) {
            validationService.createErrorResults(errors);
        }
    }

}
