package com.freshdirect.cmsadmin.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.freshdirect.cmsadmin.exception.ValidationException;

/**
 *
 * The helper class that provide support methods for validation handling.
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ValidationService {

    private static final String GENERAL_ERROR_CODE = "general";

    /**
     * Generates a map of all the validation errors. Errors populated via ValidationException.
     *
     * @param errors
     *            The errors that happen during validation
     */
    public void createErrorResults(Errors errors) {
        Map<String, Object> resultErrors = new HashMap<String, Object>();

        for (FieldError error : errors.getFieldErrors()) {
            resultErrors.put(error.getField(), error.getDefaultMessage());
        }
        List<String> globalErrorMessages = new ArrayList<String>();
        for (ObjectError error : errors.getGlobalErrors()) {
            globalErrorMessages.add(error.getDefaultMessage());
        }
        resultErrors.put(GENERAL_ERROR_CODE, globalErrorMessages);
        throw new ValidationException("Incorrect fields", resultErrors);
    }

}
