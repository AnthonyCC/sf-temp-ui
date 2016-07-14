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

import com.freshdirect.cmsadmin.domain.Permission;

/**
 *
 * The validator that handles error regarding the Permission entity.
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PermissionValidator implements Validator {

    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private ValidationService validationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Permission.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        switch (RequestMethod.valueOf(request.getMethod())) {
            case GET:
                break;
            case PUT:
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "BAD_PERMISSION_ID", "Permission Id must not be null or whitespace");
                break;
            case DELETE:
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "BAD_PERMISSION_ID", "Permission Id must not be null or whitespace");
                break;
            case POST:
                break;
            default:
                break;
        }

        if (errors.hasErrors()) {
            validationService.createErrorResults(errors);
        }

    }

}
