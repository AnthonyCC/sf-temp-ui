package com.freshdirect.cms.contentvalidation.validator;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

@Component
public class ModuleValidator implements Validator {

    private static final int IMAGE_GRID_SIZE = 6;
    
    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
       
        if (ContentType.Module.equals(contentKey.getType())) {
            String productSourceType = (String) attributesWithValues.get(ContentTypes.Module.productSourceType);
            String displayType = (String) attributesWithValues.get(ContentTypes.Module.displayType);
            if (productSourceType.equals("GENERIC")) {
                if (displayType.equals("ICON_CAROUSEL_MODULE")) {
                    validateIconCarouselModule(contentKey, attributesWithValues, contentSource, validationResults);
                } else if (displayType.equals("IMAGEGRID_MODULE")) {
                    validateImageGridModule(contentKey, attributesWithValues, contentSource, validationResults);
                } else if (displayType.equals("OPENHTML_MODULE")) {
                    validateOpenHtmlModule(contentKey, attributesWithValues, contentSource, validationResults);
                } else {
                    validationResults.addValidationResult(contentKey, "Invalid source type: use a non generic source type", ValidationResultLevel.ERROR, ModuleValidator.class);
                }
            } else {
                if (displayType.equals("ICON_CAROUSEL_MODULE") || displayType.equals("IMAGEGRID_MODULE") || displayType.equals("OPENHTML_MODULE")) {
                    validationResults.addValidationResult(contentKey, "Invalid source type: use GENERIC.", ValidationResultLevel.ERROR, ModuleValidator.class);
                } else {
                    ContentKey moduleSourceNode = (ContentKey) attributesWithValues.get(ContentTypes.Module.sourceNode);
                    validateSourceTypeSourceNodeMatch(contentKey, productSourceType, moduleSourceNode, contentSource, validationResults);
                    if (displayType.equals("EDITORIAL_MODULE")) {
                        validateEditoralModuleSpecificAttributes(contentKey, attributesWithValues, contentSource, validationResults);
                    }
                }
            }

        }
        return validationResults;
    }

    private void validateIconCarouselModule(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource,
            ValidationResults validationResults) {
        if (((List) attributesWithValues.get(ContentTypes.Module.iconList)).isEmpty()) {
            validationResults.addValidationResult(contentKey, "Icon list is missing.", ValidationResultLevel.ERROR, ModuleValidator.class);
        }
    }

    private void validateImageGridModule(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource,
            ValidationResults validationResults) {
        List<Object> imageGrids = (List) attributesWithValues.get(ContentTypes.Module.imageGrid);
        if (imageGrids == null) {
            validationResults.addValidationResult(contentKey, "Image Grid list is missing.", ValidationResultLevel.ERROR, ModuleValidator.class);
        } else if (imageGrids.size() != IMAGE_GRID_SIZE) {
            validationResults.addValidationResult(contentKey, "Please add 6 Image Grids.", ValidationResultLevel.ERROR,
                    ModuleValidator.class);
        }
    }

    private void validateOpenHtmlModule(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource,
            ValidationResults validationResults) {
        if (null == attributesWithValues.get(ContentTypes.Module.openHTML)) {
            validationResults.addValidationResult(contentKey, "Open HTML Media is missing.", ValidationResultLevel.ERROR, ModuleValidator.class);
        }
    }

    private void validateSourceTypeSourceNodeMatch(ContentKey contentKey, String productSourceType, ContentKey moduleSourceNode, ContextualContentProvider contentSource,
            ValidationResults validationResults) {
        if (productSourceType.equals("BROWSE")) {
            if (moduleSourceNode == null || !moduleSourceNode.getType().equals(ContentType.Category)) {
                validationResults.addValidationResult(contentKey, "Invalid source node. Please set a category.", ValidationResultLevel.ERROR, ModuleValidator.class);
            }
        } else if (productSourceType.equals("FEATURED_RECOMMENDER")) {
            if (moduleSourceNode == null || !moduleSourceNode.getType().equals(ContentType.Department)) {
                validationResults.addValidationResult(contentKey, "Invalid source node. Please set a department.", ValidationResultLevel.ERROR, ModuleValidator.class);
            }
        } else if (productSourceType.equals("BRAND_FEATURED_PRODUCTS")) {
            if (moduleSourceNode == null || !moduleSourceNode.getType().equals(ContentType.Brand)) {
                validationResults.addValidationResult(contentKey, "Invalid source node. Please set a brand.", ValidationResultLevel.ERROR, ModuleValidator.class);
            }
        }
    }

    private void validateEditoralModuleSpecificAttributes(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource,
            ValidationResults validationResults) {
        if (null == attributesWithValues.get(ContentTypes.Module.heroGraphic)) {
            validationResults.addValidationResult(contentKey, "Hero Graphic is missing", ValidationResultLevel.ERROR, ModuleValidator.class);
        }
        if (null == attributesWithValues.get(ContentTypes.Module.headerGraphic)) {
            validationResults.addValidationResult(contentKey, "Hader Graphic is missing", ValidationResultLevel.ERROR, ModuleValidator.class);
        }
        if (null == attributesWithValues.get(ContentTypes.Module.editorialContent)) {
            validationResults.addValidationResult(contentKey, "Editorial Content is missing", ValidationResultLevel.ERROR, ModuleValidator.class);
        }
        if (null == attributesWithValues.get(ContentTypes.Module.heroTitle)) {
            validationResults.addValidationResult(contentKey, "Hero Title is missing", ValidationResultLevel.ERROR, ModuleValidator.class);
        }
        if (null == attributesWithValues.get(ContentTypes.Module.heroSubtitle)) {
            validationResults.addValidationResult(contentKey, "Hero Subtitle is missing", ValidationResultLevel.ERROR, ModuleValidator.class);
        }
        if (null == attributesWithValues.get(ContentTypes.Module.headerTitle)) {
            validationResults.addValidationResult(contentKey, "Header Title is missing", ValidationResultLevel.ERROR, ModuleValidator.class);
        }
        if (null == attributesWithValues.get(ContentTypes.Module.headerSubtitle)) {
            validationResults.addValidationResult(contentKey, "Header Subtitle is missing", ValidationResultLevel.ERROR, ModuleValidator.class);
        }
    }

}
