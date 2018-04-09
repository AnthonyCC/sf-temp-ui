package com.freshdirect.cms.contentvalidation.validator;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

@Component
public class CyclicReferenceValidator implements Validator {

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        if (hasParentCycle(contentKey, contentKey, contentSource)) {
            validationResults.addValidationResult(contentKey, "Cannot have cyclic references!", ValidationResultLevel.ERROR, CyclicReferenceValidator.class);
        }
        return validationResults;
    }

    private boolean hasParentCycle(ContentKey originalNode, ContentKey actualNode, ContextualContentProvider contentSource) {
        Set<ContentKey> parents = contentSource.getParentKeys(actualNode);
        if (parents.contains(originalNode)) {
            return true;
        }
        for (ContentKey parent : parents) {
            if (hasParentCycle(originalNode, parent, contentSource)) {
                return true;
            }
        }
        return false;
    }
}
