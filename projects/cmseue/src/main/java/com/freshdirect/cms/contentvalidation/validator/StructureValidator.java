package com.freshdirect.cms.contentvalidation.validator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

/**
 * Ensures nodes of the following type have a one and only one parent:
 * <ul>
 * <li>Department</li>
 * <li>Category</li>
 * <li>Sku</li>
 * <li>ComponentGroup</li>
 * <li>Domain</li>
 * <li>DomainValue</li>
 * <li>Recipe</li>
 * <li>RecipeSection</li>
 * <li>RecipeVariant</li>
 * <li>StarterList</li>
 * </ul>
 *
 * Except for parents that are of the following types:
 * <ul>
 * <li>ImageBanner</li>
 * <li>Anchor</li>
 * <li>Section</li>
 * <li>WebPage</li>
 * <li>PickList</li>
 * <li>DarkStore</li>
 * </ul>
 * These parents do not count as parents.
 */
@Component
public class StructureValidator implements Validator {

    private static final Set<ContentType> TYPES = new HashSet<ContentType>();
    static {
        TYPES.add(ContentType.Department);
        TYPES.add(ContentType.Category);
        TYPES.add(ContentType.Sku);
        TYPES.add(ContentType.ComponentGroup);
        TYPES.add(ContentType.Domain);
        TYPES.add(ContentType.DomainValue);
        TYPES.add(ContentType.Recipe);
        TYPES.add(ContentType.RecipeSection);
        TYPES.add(ContentType.RecipeVariant);
        TYPES.add(ContentType.StarterList);
    }

    private static final Set<ContentType> IGNORABLE_PARENT_TYPES = new HashSet<ContentType>();
    static {
        IGNORABLE_PARENT_TYPES.add(ContentType.ImageBanner);
        IGNORABLE_PARENT_TYPES.add(ContentType.Anchor);
        IGNORABLE_PARENT_TYPES.add(ContentType.Section);
        IGNORABLE_PARENT_TYPES.add(ContentType.WebPage);
        IGNORABLE_PARENT_TYPES.add(ContentType.PickList);
        IGNORABLE_PARENT_TYPES.add(ContentType.DarkStore);
    }

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        if (TYPES.contains(contentKey.type)) {
            Set<ContentKey> parentKeys = contentSource.getParentKeys(contentKey);
            Set<ContentKey> nonIgnorableKeys = new HashSet<ContentKey>();
            for (ContentKey parentKey : parentKeys) {
                if (!IGNORABLE_PARENT_TYPES.contains(parentKey.type)) {
                    nonIgnorableKeys.add(parentKey);
                }
            }
            if (nonIgnorableKeys.size() > 1) {
                validationResults.addValidationResult(contentKey,
                        "Cannot have multiple parents! " + nonIgnorableKeys.toString(), ValidationResultLevel.ERROR, StructureValidator.class);
            }
        }
        return validationResults;
    }
}
