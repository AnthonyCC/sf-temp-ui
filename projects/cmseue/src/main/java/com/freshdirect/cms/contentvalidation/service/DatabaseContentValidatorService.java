package com.freshdirect.cms.contentvalidation.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.contentvalidation.correction.CorrectionManager;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;

@Profile({"database", "test"})
@Service
public class DatabaseContentValidatorService implements ValidatorService {

    @Autowired
    private ContentValidatorService contentValidatorService;

    @Autowired
    private CorrectionManager correctionManager;

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = contentValidatorService.validate(contentKey, attributesWithValues, contentSource);
        if (validationResults.hasError()) {
            correctionManager.runCorrectionServicesOn(contentKey, attributesWithValues, validationResults, contentSource);
            validationResults = contentValidatorService.validate(contentKey, attributesWithValues, contentSource);
        }

        if (validationResults.hasError()) {
            throw new ValidationFailedException(validationResults);
        }
        return validationResults;
    }
}
