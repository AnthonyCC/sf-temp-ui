package com.freshdirect.cms.contentvalidation.validator;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.validator.Validator;
import com.google.common.base.Optional;

/**
 * Makes sure that a recipe node has a default recipe variant, and that recipe variant has a main recipe section when created.
 */
@Component
public class RecipeChildNodeValidator implements Validator {

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        boolean exists = false;
        ContentType type = contentKey.type;
        if (ContentType.Recipe.equals(type)) {
            exists = checkDefaultRecipeVariantExists(contentKey, contentSource);
            if (!exists) {
                validationResults.addValidationResult(contentKey, "There is no default recipe variant for recipe!", ValidationResultLevel.ERROR, RecipeChildNodeValidator.class);
            }
        } else if (ContentType.RecipeVariant.equals(type)) {
            exists = checkMainRecipeSectionExists(contentKey, contentSource);
            if (!exists) {
                validationResults.addValidationResult(contentKey, "There is no main recipe section for recipe variant!", ValidationResultLevel.ERROR,
                        RecipeChildNodeValidator.class);
            }
        }
        return validationResults;
    }

    @SuppressWarnings("unchecked")
    private boolean checkDefaultRecipeVariantExists(ContentKey contentKey, ContextualContentProvider contentSource) {
        ContentKey defaultRecipeVariantKey = ContentKeyFactory.get(ContentType.RecipeVariant, contentKey.id + "_default");
        Optional<Object> recipeVariant = contentSource.getAttributeValue(contentKey, ContentTypes.Recipe.variants);
        return recipeVariant.isPresent() && ((List<ContentKey>) recipeVariant.get()).contains(defaultRecipeVariantKey);
    }

    @SuppressWarnings("unchecked")
    private boolean checkMainRecipeSectionExists(ContentKey contentKey, ContextualContentProvider contentSource) {
        ContentKey mainRecipeSectionKey = ContentKeyFactory.get(ContentType.RecipeSection, contentKey.id + "_main");
        Optional<Object> recipeSection = contentSource.getAttributeValue(contentKey, ContentTypes.RecipeVariant.sections);
        return recipeSection.isPresent() && ((List<ContentKey>) recipeSection.get()).contains(mainRecipeSectionKey);
    }
}
