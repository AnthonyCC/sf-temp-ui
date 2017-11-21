package com.freshdirect.cms.validation.validator;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

@Component
public class KeyValidator implements Validator {

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        if (contentKey.type == null) {
            validationResults.addValidationResult(contentKey, "Invalid contentKey: no type", ValidationResultLevel.ERROR, KeyValidator.class);
        }
        if (contentKey.id == null || contentKey.id.isEmpty()) {
            validationResults.addValidationResult(contentKey, "Invalid contentKey: no id", ValidationResultLevel.ERROR, KeyValidator.class);
        }
        return validationResults;
    }

}
