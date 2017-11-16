package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContentKeyParentsCollectorService;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextService;
import com.freshdirect.cms.core.service.NodeCollectionContentProviderService;
import com.freshdirect.cms.ui.editor.UnmodifiableContent;
import com.freshdirect.cms.ui.editor.publish.flow.domain.Input;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.TransformerTask;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;
import com.freshdirect.cms.validation.service.ValidatorService;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContentValidationTask extends TransformerTask<Input, ValidationResults> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentValidationTask.class);

    private static final ValidationResultLevel SEVERITY_IN_PUBLISH = ValidationResultLevel.WARNING;

    @Autowired
    private ValidatorService typeSystemValidator;

    @Autowired
    private com.freshdirect.cms.contentvalidation.service.ValidatorService businessValidator;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private ContentKeyParentsCollectorService contentKeyParentsCollectorService;

    @Autowired
    private ContextService contextService;

    @Override
    public ValidationResults call() throws Exception {

        ValidationResults totalValidationResult = new ValidationResults();

        NodeCollectionContentProviderService nodeCollectionContentProviderService = new NodeCollectionContentProviderService(contentTypeInfoService,
                contentKeyParentsCollectorService, contextService, input.getContentNodes());

        for (final Map.Entry<ContentKey, Map<Attribute, Object>> entry : input.getContentNodes().entrySet()) {

            final ContentKey contentKey = entry.getKey();
            if (!UnmodifiableContent.isModifiable(contentKey)) {
                LOGGER.debug("Skip validating unmodifiable content " + contentKey);
                continue;
            }

            try {
                ValidationResults result = typeSystemValidator.validate(contentKey, entry.getValue());
                totalValidationResult.addAll(result);
                if (contentKey.type == ContentType.Product) {
                    LOGGER.debug(contentKey + " ~ PH (before validation) ~> " + nodeCollectionContentProviderService.findPrimaryHomes(contentKey));
                }
                result = businessValidator.validate(contentKey, entry.getValue(), nodeCollectionContentProviderService);
                totalValidationResult.addAll(result);
                if (contentKey.type == ContentType.Product) {
                    LOGGER.debug(contentKey + " ~ PH (after validation) ~> " + nodeCollectionContentProviderService.findPrimaryHomes(contentKey) + "\n");
                }
            } catch (ValidationFailedException e) {
                if (e.getValidationResults() == null || e.getValidationResults().isEmpty()) {
                    totalValidationResult.addValidationResult(new ValidationResult(contentKey, e.getMessage(), SEVERITY_IN_PUBLISH, null));
                } else {
                    // extract validation result from the exception
                    for (ValidationResult validationDetail : e.getValidationResults()) {
                        Object detailKey = validationDetail.getValidatedObject();

                        String detailMessage = validationDetail.getMessage();

                        // prepend message with the name of validated object if other than content key
                        if (detailKey != null && !(detailKey instanceof ContentKey)) {
                            if (detailKey instanceof Attribute) {
                                detailMessage = ((Attribute)detailKey).getName() + ": " + detailMessage;
                            } else {
                                detailMessage = detailKey.toString() + ": " + detailMessage;
                            }
                        }

                        ValidationResult extractedResult = new ValidationResult(contentKey, detailMessage,
                                SEVERITY_IN_PUBLISH, validationDetail.getValidatorClass());

                        totalValidationResult.addValidationResult(extractedResult);
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

    private void putFixedContentIntoInput(NodeCollectionContentProviderService nodeCollectionContentProviderService) {
        Map<ContentKey, Map<Attribute, Object>> allNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (ContentKey key : nodeCollectionContentProviderService.getContentKeys()) {
            allNodes.put(key, nodeCollectionContentProviderService.getAllAttributesForContentKey(key));
        }
        input.setContentNodes(allNodes);
    }

}
