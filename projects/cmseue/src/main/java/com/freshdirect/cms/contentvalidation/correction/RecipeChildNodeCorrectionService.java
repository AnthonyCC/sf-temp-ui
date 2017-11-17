package com.freshdirect.cms.contentvalidation.correction;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.contentvalidation.validator.RecipeChildNodeValidator;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.validation.validator.Validator;
import com.google.common.base.Optional;

@Profile({ "database", "test" })
@Component
public class RecipeChildNodeCorrectionService implements CorrectionService {

    @Autowired
    private DraftContextHolder draftContextHolder;

    @Override
    public Class<? extends Validator> getSupportedValidator() {
        return RecipeChildNodeValidator.class;
    }

    @Override
    public void correct(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        switch (contentKey.type) {
            case Recipe:
                createDefaultVariantForRecipe(contentKey, contentSource);
                break;

            case RecipeVariant:
                createMainSection(contentKey, contentSource);
                break;
            default:
                // DO NOTHING
                break;
        }
    }

    private void createDefaultVariantForRecipe(ContentKey recipeKey, ContextualContentProvider contentSource) {
        ContentKey defaultVariant = ContentKeyFactory.get(ContentType.RecipeVariant, recipeKey.id + "_default");

        Map<Attribute, Object> attributesForVariant = new HashMap<Attribute, Object>();
        attributesForVariant.put(ContentTypes.RecipeVariant.name, "default");

        ContentKey mainSectionKey = createMainSection(defaultVariant, contentSource);
        attributesForVariant.put(ContentTypes.RecipeVariant.sections, Arrays.asList(mainSectionKey));

        LinkedHashMap<ContentKey, Map<Attribute, Object>> nodeToSave = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();
        nodeToSave.put(defaultVariant, attributesForVariant);

        contentSource.updateContent(nodeToSave, new ContentUpdateContext("repairman", new Date(), "automatically creating default variant for recipe " + recipeKey,
                draftContextHolder.getDraftContext(), nodeToSave.keySet()));
    }

    private ContentKey createMainSection(ContentKey variantKey, ContextualContentProvider contentSource) {
        ContentKey mainSectionKey = ContentKeyFactory.get(ContentType.RecipeSection, variantKey + "_main");

        Optional<Object> mainSectionName = contentSource.getAttributeValue(mainSectionKey, ContentTypes.RecipeSection.name);
        if (!mainSectionName.isPresent() || !mainSectionName.get().toString().equals("main")) {
            saveMainSection(mainSectionKey, variantKey, contentSource);
        }

        return mainSectionKey;
    }

    private void saveMainSection(ContentKey mainSectionKey, ContentKey variantKey, ContextualContentProvider contentSource) {
        LinkedHashMap<Attribute, Object> attributesForVariant = new LinkedHashMap<Attribute, Object>();
        attributesForVariant.put(ContentTypes.RecipeSection.name, "main");
        LinkedHashMap<ContentKey, Map<Attribute, Object>> nodeToSave = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();
        nodeToSave.put(mainSectionKey, attributesForVariant);

        contentSource.updateContent(nodeToSave, new ContentUpdateContext("repairman", new Date(), "automatically creating main section for recipe variant " + variantKey,
                draftContextHolder.getDraftContext(), nodeToSave.keySet()));
    }

}
