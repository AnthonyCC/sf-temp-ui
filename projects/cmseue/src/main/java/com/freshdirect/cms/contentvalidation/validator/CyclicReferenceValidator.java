package com.freshdirect.cms.contentvalidation.validator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.google.common.collect.ImmutableList;

@Component
public class CyclicReferenceValidator implements Validator {

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        if (hasParentCycle(ImmutableList.of(contentKey), contentKey, contentSource)) {
            validationResults.addValidationResult(contentKey, "Cannot have cyclic references!", ValidationResultLevel.ERROR, CyclicReferenceValidator.class);
        }
        return validationResults;
    }

    private boolean hasParentCycle(List<ContentKey> originalPath, ContentKey actualNode, ContextualContentProvider contentSource) {
        Set<ContentKey> parents = contentSource.getParentKeys(actualNode);
        for (ContentKey key : originalPath) {
            if (parents.contains(key)) {
                return true;
            }
        }
        for (ContentKey parent : parents) {
            List<ContentKey> path = new ImmutableList.Builder<ContentKey>()
                    .addAll(originalPath)
                    .add(parent)
                    .build();
            if (hasParentCycle(path, parent, contentSource)) {
                return true;
            }
        }
        return false;
    }
}
