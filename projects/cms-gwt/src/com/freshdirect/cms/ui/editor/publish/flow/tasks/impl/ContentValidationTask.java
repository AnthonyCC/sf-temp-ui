package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.contentvalidation.service.ValidatorService;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextService;
import com.freshdirect.cms.core.service.NodeCollectionContentProvider;
import com.freshdirect.cms.ui.editor.UnmodifiableContent;
import com.freshdirect.cms.ui.editor.publish.flow.domain.Input;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.TransformerTask;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContentValidationTask extends TransformerTask<Input, ValidationResults> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentValidationTask.class);

    private static final ValidationResultLevel SEVERITY_IN_PUBLISH = ValidationResultLevel.WARNING;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private ContextService contextService;

    @Override
    public ValidationResults call() throws Exception {
        ValidationResults totalValidationResult = new ValidationResults();

        NodeCollectionContentProvider nodeCollectionContentProviderService =
                new NodeCollectionContentProvider(contentTypeInfoService, contextService, input.getContentNodes());

        for (final Map.Entry<ContentKey, Map<Attribute, Object>> entry : input.getContentNodes().entrySet()) {
            final ContentKey contentKey = entry.getKey();
            if (!UnmodifiableContent.isModifiable(contentKey)) {
                LOGGER.debug("Skip validating unmodifiable content " + contentKey);
                continue;
            }

            try {
                totalValidationResult.addAll(validatorService.validate(contentKey, entry.getValue(), nodeCollectionContentProviderService));
            } catch (ValidationFailedException e) {
                if (e.getValidationResults() == null || e.getValidationResults().isEmpty()) {
                    totalValidationResult.addValidationResult(new ValidationResult(contentKey, e.getMessage(), SEVERITY_IN_PUBLISH, null));
                } else {
                    // extract validation result from the exception
                    for (ValidationResult validationDetail : e.getValidationResults()) {
                        totalValidationResult.addValidationResult(new ValidationResult(validationDetail.getValidatedObject(),
                                validationDetail.getMessage(), SEVERITY_IN_PUBLISH, validationDetail.getValidatorClass()));
                    }
                }
            }
        }

        if (!totalValidationResult.hasError()) {
            LOGGER.error("Validation failed");
        } else {
            LOGGER.debug("Validation completed");
        }
        putFixedContentIntoInput(nodeCollectionContentProviderService);
        return totalValidationResult;
    }

    @Override
    public String getName() {
        return "Validate content nodes";
    }

    private void putFixedContentIntoInput(NodeCollectionContentProvider nodeCollectionContentProviderService) {
        Map<ContentKey, Map<Attribute, Object>> allNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (ContentKey key : nodeCollectionContentProviderService.getContentKeys()) {
            allNodes.put(key, nodeCollectionContentProviderService.getAllAttributesForContentKey(key));
        }
        input.setContentNodes(allNodes);
    }
}
