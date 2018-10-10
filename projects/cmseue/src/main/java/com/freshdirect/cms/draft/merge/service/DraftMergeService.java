package com.freshdirect.cms.draft.merge.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.domain.DraftStatus;
import com.freshdirect.cms.draft.domain.NullValueBehavior;
import com.freshdirect.cms.draft.service.DraftContentProviderService;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.draft.service.DraftService;
import com.freshdirect.cms.draft.validation.service.DraftValidatorService;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;

@Profile("database")
@Service
public class DraftMergeService {

    @Autowired
    private DraftContextHolder draftContextHolder;

    @Autowired
    private DraftValidatorService draftValidatorService;

    @Autowired
    private DraftContentProviderService draftContentProviderService;

    @Autowired
    private DraftService draftService;

    public Map<DraftChange, ValidationResults> mergeDraftToMain(String userName) {
        if (DraftContext.MAIN.equals(draftContextHolder.getDraftContext())) {
            throw new RuntimeException("Can't merge the main draft!");
        }
        Map<DraftChange, ValidationResults> validationResultsByDraftChange = draftValidatorService.validate();
        ValidationResults allResults = new ValidationResults();
        for (ValidationResults validationResults : validationResultsByDraftChange.values()) {
            allResults.addAll(validationResults);
        }

        if (!allResults.hasError()) {
            DraftContext draftContext = draftContextHolder.getDraftContext();
            LinkedHashMap<ContentKey, Map<Attribute, Object>> allNodesToSave = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();
            for (DraftChange draftChange : draftService.getDraftChanges(draftContext.getDraftId())) {
                ContentKey keyOfNode = ContentKeyFactory.get(draftChange.getContentKey());
                allNodesToSave.put(keyOfNode, draftContentProviderService.getAllAttributesForContentKey(keyOfNode, NullValueBehavior.INCLUDE_NULLS));
            }
            Date updatedAt = new Date();
            ContentUpdateContext contentUpdateContext = new ContentUpdateContext(userName, updatedAt,
                    "Merging draft '" + draftContext.getDraftName() + "' to mainline", DraftContext.MAIN, allNodesToSave.keySet());
            // set draftContext to main to merge the draft!
            draftContextHolder.setDraftContext(DraftContext.MAIN);
            try {
                draftContentProviderService.updateContent(allNodesToSave, contentUpdateContext);
                draftService.updateDraftStatusForDraft(draftContext.getDraftId(), DraftStatus.MERGED);
            } catch (ValidationFailedException validationFailedException) {
                draftContextHolder.setDraftContext(draftContext); // restoring the original draftContext
                draftService.updateDraftStatusForDraft(draftContext.getDraftId(), DraftStatus.FAILED);
                validationResultsByDraftChange = fetchValidationResultsFromException(validationFailedException, validationResultsByDraftChange.keySet());
            }
        }

        return validationResultsByDraftChange;
    }

    private Map<DraftChange, ValidationResults> fetchValidationResultsFromException(ValidationFailedException validationFailedException, Set<DraftChange> draftChanges) {
        ValidationResults validationResults = new ValidationResults();
        validationResults.addAll(validationFailedException.getValidationResults());
        List<DraftChange> draftChangesList = new ArrayList<DraftChange>();
        draftChangesList.addAll(draftChanges);
        return draftValidatorService.collectValidationResultsForDraftChanges(validationResults, draftChangesList);
    }
}
