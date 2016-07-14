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

import com.freshdirect.cmsadmin.domain.Persona;

/**
 *
 * The validator that handles error regarding the Persona entity.
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PersonaValidator implements Validator {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ValidationService validationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Persona.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Persona persona = (Persona) target;

        switch (RequestMethod.valueOf(request.getMethod())) {
            case GET:
                if (persona == null) {
                    errors.reject(null, "Persona cannot be found by given user id.");
                } else {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "BAD_PERSONA_ID", "Id must not be null or whitespace");
                }
                break;
            case PUT:
                break;
            case DELETE:
                if (persona == null) {
                    errors.reject(null, "Persona cannot be found by given user id.");
                } else {
                    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "BAD_PERSONA_ID", "Id must not be null or whitespace");
                }
                break;
            case POST:
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "BAD_PERSONA_NAME", "Persona name must not be empty or whitespace");
                break;
            default:
                break;
        }

        if (errors.hasErrors()) {
            validationService.createErrorResults(errors);
        }

    }

}
