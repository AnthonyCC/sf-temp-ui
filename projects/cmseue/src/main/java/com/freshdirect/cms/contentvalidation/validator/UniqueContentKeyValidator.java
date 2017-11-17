package com.freshdirect.cms.contentvalidation.validator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.validator.Validator;

/**
 * Ensures that ContentKey IDs for certain types are unique with respect to nodes of all such content types.
 *
 */
@Component
public class UniqueContentKeyValidator implements Validator {

    private static final Set<ContentType> TYPES_TO_CHECK_UNICITY_IN = new HashSet<ContentType>();
    static {
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.Department);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.Category);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.Product);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.ComponentGroup);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.ConfiguredProduct);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.ConfiguredProductGroup);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.Sku);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.Recipe);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.RecipeDepartment);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.RecipeCategory);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.RecipeSubcategory);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.RecipeSource);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.RecipeVariant);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.RecipeSection);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.RecipeAuthor);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.FDFolder);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.BookRetailer);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.RecipeSearchPage);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.RecipeSearchCriteria);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.YmalSet);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.StarterList);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.SuperDepartment);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.CategorySection);
        TYPES_TO_CHECK_UNICITY_IN.add(ContentType.GlobalNavigation);

    }

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        ContentType type = contentKey.type;
        if (TYPES_TO_CHECK_UNICITY_IN.contains(type)) {
            Set<ContentKey> keys = new HashSet<ContentKey>(TYPES_TO_CHECK_UNICITY_IN.size() - 1);
            for (ContentType contentType : TYPES_TO_CHECK_UNICITY_IN) {
                if (!contentType.equals(type)) {
                    keys.add(ContentKeyFactory.get(contentType, contentKey.id));
                }
            }

            for (ContentKey key : keys) {
                if (contentSource.containsContentKey(key)) {
                    validationResults.addValidationResult(contentKey, "Content already exists with id: " + key, ValidationResultLevel.ERROR, UniqueContentKeyValidator.class);
                }
            }
        }
        return validationResults;
    }

}
