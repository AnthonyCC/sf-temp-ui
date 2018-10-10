package com.freshdirect.cms.draft.validation.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.draft.domain.Draft;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.NullValueBehavior;
import com.freshdirect.cms.draft.service.DraftApplicatorService;
import com.freshdirect.cms.draft.service.DraftContentProviderService;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.draft.service.DraftService;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;
import com.google.common.base.Optional;

@Profile("database")
@Service
public class DraftValidatorService {

    @Autowired
    private DraftContextHolder draftContextHolder;

    @Autowired
    private DraftService draftService;

    @Autowired
    private DraftContentProviderService draftContentProviderService;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    public Map<DraftChange, ValidationResults> validate() {
        List<DraftChange> draftChanges = draftService.getDraftChanges(draftContextHolder.getDraftContext().getDraftId());
        Map<ContentKey, Map<Attribute, Object>> payloadToValidate = new HashMap<ContentKey, Map<Attribute, Object>>();

        for (DraftChange draftChange : draftChanges) {
            ContentKey key = ContentKeyFactory.get(draftChange.getContentKey());
            payloadToValidate.put(key, draftContentProviderService.getAllAttributesForContentKey(key, NullValueBehavior.INCLUDE_NULLS));
        }

        ValidationResults validationResults = new ValidationResults();
        try {
            draftContentProviderService.validateContent(payloadToValidate);
        } catch (ValidationFailedException validationFailedException) {
            validationResults.addAll(validationFailedException.getValidationResults());
        }

        return collectValidationResultsForDraftChanges(validationResults, draftChanges);
    }

    public Map<DraftChange, ValidationResults> collectValidationResultsForDraftChanges(ValidationResults validationResults, List<DraftChange> draftChanges) {

        Map<DraftChange, ValidationResults> validationResultsByDraftChange = new HashMap<DraftChange, ValidationResults>();

        for (ValidationResult validationResult : validationResults.getValidationResults()) {
            boolean draftChangeFound = false;
            for (DraftChange draftChange : draftChanges) {
                validationResultsByDraftChange.put(draftChange, new ValidationResults());
                if (ContentKeyFactory.get(draftChange.getContentKey()).equals(validationResult.getValidatedObject())) {
                    validationResultsByDraftChange.get(draftChange).addValidationResult(validationResult);
                    draftChangeFound = true;
                }
            }
            if (!draftChangeFound) {
                DraftChange draftChangeThatCausedTheIssue = findDraftChangeThatCausedTheIssue(draftChanges, validationResult.getValidatedObject());

                DraftChange virtualDraftChange = createVirtualDraftChange(draftChanges.get(0).getDraft(), validationResult.getValidatedObject(),
                        draftChangeThatCausedTheIssue);

                ValidationResults validationResultsForVirtualDraft = new ValidationResults();
                validationResultsForVirtualDraft.addValidationResult(validationResult);

                validationResultsByDraftChange.put(virtualDraftChange, validationResultsForVirtualDraft);

                if (draftChangeThatCausedTheIssue != null) {
                    ValidationResults resultsForIssuer = new ValidationResults();
                    resultsForIssuer.addValidationResult(new ValidationResult(ContentKeyFactory.get(draftChangeThatCausedTheIssue.getContentKey()),
                            "This change broke " + validationResult.getValidatedObject().toString(), ValidationResultLevel.ERROR, null));
                    validationResultsByDraftChange.put(draftChangeThatCausedTheIssue, resultsForIssuer);
                }
            }
        }

        // add remaining draft-changes that did not have any validation error
        for (DraftChange draftChange : draftChanges) {
            if (!validationResultsByDraftChange.containsKey(draftChange)) {
                validationResultsByDraftChange.put(draftChange, new ValidationResults());
            }
        }

        return validationResultsByDraftChange;
    }

    private DraftChange findDraftChangeThatCausedTheIssue(Collection<DraftChange> allDraftChanges, ContentKey keyInError) {
        DraftChange draftChangeThatCausedTheIssue = null;
        for (DraftChange draftChange : allDraftChanges) {
            ContentKey key = ContentKeyFactory.get(draftChange.getContentKey());
            Optional<Attribute> changedAttribute = contentTypeInfoService.findAttributeByName(key.type, draftChange.getAttributeName());
            if (changedAttribute.isPresent() && changedAttribute.get() instanceof Relationship
                    && draftChange.getValue() != null
                    && DraftApplicatorService.getContentKeysFromRelationshipValue(draftChange.getValue()).contains(keyInError)) {
                draftChangeThatCausedTheIssue = new DraftChange();
                draftChangeThatCausedTheIssue.setAttributeName(draftChange.getAttributeName());
                draftChangeThatCausedTheIssue.setContentKey(draftChange.getContentKey());
                draftChangeThatCausedTheIssue.setCreatedAt(draftChange.getCreatedAt());
                draftChangeThatCausedTheIssue.setDraft(draftChange.getDraft());
                draftChangeThatCausedTheIssue.setUserName(draftChange.getUserName());
                draftChangeThatCausedTheIssue.setValue(draftChange.getValue());
                break;
            }
        }

        return draftChangeThatCausedTheIssue;
    }

    private DraftChange createVirtualDraftChange(Draft draft, ContentKey contentKey, DraftChange draftChangeThatCausedTheIssue) {
        DraftChange virtualDraftChange = new DraftChange();
        virtualDraftChange.setAttributeName("(parent keys)");
        virtualDraftChange.setContentKey(contentKey.toString());
        if (draftChangeThatCausedTheIssue != null) {
            virtualDraftChange.setCreatedAt(draftChangeThatCausedTheIssue.getCreatedAt());
        } else {
            virtualDraftChange.setCreatedAt(System.currentTimeMillis());
        }
        virtualDraftChange.setDraft(draft);
        virtualDraftChange.setUserName("system");
        virtualDraftChange.setValue("-");
        return virtualDraftChange;
    }

}
