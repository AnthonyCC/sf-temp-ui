package com.freshdirect.cms.contentvalidation.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.contentvalidation.validator.Validator;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResults;

@Service
public class ContentValidatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentValidatorService.class);

    @Autowired
    private ContentValidatorConfigurationService contentValidatorConfigurationService;

    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        LOGGER.debug("Validating " + contentKey);
        ValidationResults validationResults = new ValidationResults();

        List<Validator> validators = contentValidatorConfigurationService.getValidators();
        for (Validator validator : validators) {
            validationResults.addAll(validator.validate(contentKey, attributesWithValues, contentSource));
        }

        logValidationErrors(validationResults, contentKey);

        return validationResults;
    }

    private void logValidationErrors(ValidationResults validationResults, ContentKey contentKey) {
        if (validationResults.hasError()) {
            for (ValidationResult validationResult : validationResults.getValidationResults()) {
                LOGGER.error("Validation error for " + validationResult.getValidatedObject() + ": " + validationResult.toString());
            }
        }
    }
}
