package com.freshdirect.cms.contentvalidation.validator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

@Component
public class WhitespaceValidator implements Validator {

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        if (StringUtils.containsWhitespace(contentKey.id)) {
            validationResults.addValidationResult(contentKey, "Content id can't contain whitespace: " + contentKey, ValidationResultLevel.ERROR, WhitespaceValidator.class);
        }
        return validationResults;
    }

}
