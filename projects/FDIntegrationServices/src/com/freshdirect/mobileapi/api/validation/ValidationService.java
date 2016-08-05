package com.freshdirect.mobileapi.api.validation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.freshdirect.mobileapi.api.exception.ValidationException;

/**
 *
 * The helper class that provide support methods for validation handling.
 *
 */
@Component
public class ValidationService {

    /**
     * Generates a map of all the validation errors. Errors populated via ValidationException.
     *
     * @param errors
     *            The errors that happen during validation
     */
    public void createErrorResults(Errors errors) {
        Map<String, String> resultErrors = new HashMap<String, String>();
        
        for (ObjectError error : errors.getAllErrors()) {
            resultErrors.put(error.getCode(), error.getDefaultMessage());
        }
        throw new ValidationException("validation error", resultErrors);
    }

}
