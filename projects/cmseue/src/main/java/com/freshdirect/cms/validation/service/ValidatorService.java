package com.freshdirect.cms.validation.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;
import com.freshdirect.cms.validation.validator.KeyValidator;
import com.freshdirect.cms.validation.validator.TypeValidator;

@Service
public class ValidatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorService.class);

    @Autowired
    private TypeValidator typeValidator;

    @Autowired
    private KeyValidator keyValidator;

    /**
     * Validates the attributes with values for a contentKey based on the type system's rules.
     *
     * @param contentKey
     * @param attributesWithValues
     * @return the ValidationResults
     * @throws ValidationFailedException
     *             (extends RuntimeException) if the validationResults contains errors
     */
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues) {
        ValidationResults validationResults = typeValidator.validate(contentKey, attributesWithValues, null);
        validationResults.addAll(keyValidator.validate(contentKey, attributesWithValues, null));

        if (validationResults.hasError()) {
            for (ValidationResult validationResult : validationResults.getValidationResults()) {
                LOGGER.error("Validation error for " + contentKey + ": " + validationResult.toString());
            }
            throw new ValidationFailedException("Validation failed for " + contentKey, validationResults);
        }
        return validationResults;
    }
}
