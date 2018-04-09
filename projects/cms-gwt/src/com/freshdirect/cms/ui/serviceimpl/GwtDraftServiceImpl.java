package com.freshdirect.cms.ui.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.draft.domain.Draft;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.merge.service.DraftMergeService;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.draft.service.DraftService;
import com.freshdirect.cms.draft.validation.service.DraftValidatorService;
import com.freshdirect.cms.ui.editor.converter.DraftChangeToGwtDraftChangeConverter;
import com.freshdirect.cms.ui.editor.permission.service.PersonaService;
import com.freshdirect.cms.ui.editor.service.IndexingService;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.draft.GwtDraftChange;
import com.freshdirect.cms.ui.service.GwtDraftService;
import com.freshdirect.cms.ui.service.GwtSecurityException;
import com.freshdirect.cms.ui.service.ServerException;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

public class GwtDraftServiceImpl extends GwtServiceBase implements GwtDraftService {

    private static final long serialVersionUID = -2141795710378150875L;

    private static final Logger LOGGER = LoggerFactory.getLogger(GwtDraftServiceImpl.class);

    private DraftService draftService = CmsServiceLocator.draftService();
    private DraftChangeToGwtDraftChangeConverter draftChangeToGwtDraftChangeConverter = DraftChangeToGwtDraftChangeConverter.getInstance();
    private DraftContextHolder draftContextHolder = CmsServiceLocator.getDraftContextHolder();
    private DraftValidatorService draftValidatorService = CmsServiceLocator.getDraftValidatorService();
    private DraftMergeService draftMergeService = CmsServiceLocator.getDraftMergeService();
    private PersonaService personaService = EditorServiceLocator.personaService();
    private IndexingService indexingService = EditorServiceLocator.indexingService();

    public GwtDraftServiceImpl() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public List<GwtDraftChange> loadDraftChanges() throws ServerException {
        List<DraftChange> draftChanges = draftService.getDraftChanges(getDraftContext().getDraftId());
        List<GwtDraftChange> gwtDraftChanges = draftChangeToGwtDraftChangeConverter.convert(draftChanges);
        return gwtDraftChanges;
    }

    @Override
    public List<GwtDraftChange> validateDraft() throws ServerException {
        final GwtUser user = getUser();

        if (!user.isHasAccessToDraftBranches()) {
            throw new GwtSecurityException("User " + user.getName() + " is not allowed to validate branch!");
        }

        validateUserActionForDraft(user);

        draftContextHolder.setDraftContext(getDraftContext());
        Map<DraftChange, ValidationResults> validationResultsByDraftChange = draftValidatorService.validate();

        List<GwtDraftChange> gwtDraftChanges = processValidationResults(validationResultsByDraftChange);

        return gwtDraftChanges;
    }

    @Override
    public List<GwtDraftChange> mergeDraft() throws ServerException {
        GwtUser user = getUser();

        if (!user.isHasAccessToDraftBranches()) {
            throw new GwtSecurityException("User " + user.getName() + " is not allowed to validate branch!");
        }

        validateUserActionForDraft(user);

        draftContextHolder.setDraftContext(getDraftContext());
        Map<DraftChange, ValidationResults> validationResultsByDraftChange = draftMergeService.mergeDraftToMain(getUser().getName());

        boolean success = isValidationSuccess(validationResultsByDraftChange);
        List<GwtDraftChange> gwtDraftChanges = processValidationResults(validationResultsByDraftChange);

        if (success) {
            // start a search re-index asynchronously
            new Thread(new AsyncSearchIndex()).start();
            personaService.invalidatePersona(getThreadLocalRequest().getUserPrincipal().getName());
        }

        return gwtDraftChanges;
    }
    
    private class AsyncSearchIndex implements Runnable {
        @Override
        public void run() {
            indexingService.indexAll();
        }
    }

    private void validateUserActionForDraft(GwtUser user) {
        List<Draft> notDroppedOrMergedDrafts = draftService.getDrafts();
        DraftContext draftContext = getDraftContext();
        boolean isDraftNotDroppedOrMerged = false;
        for (Draft draft : notDroppedOrMergedDrafts) {
            if (draft.getId() == draftContext.getDraftId()) {
                isDraftNotDroppedOrMerged = true;
                break;
            }
        }
        if (!isDraftNotDroppedOrMerged) {
            throw new GwtSecurityException("Draft " + draftContext.getDraftName() + " is already merged or deleted!");
        }
    }

    private List<GwtDraftChange> processValidationResults(Map<DraftChange, ValidationResults> validationResultsByDraftChange) {
        ValidationResults validationResults = null;
        List<GwtDraftChange> gwtDraftChanges = new ArrayList<GwtDraftChange>();
        for (DraftChange draftChange : validationResultsByDraftChange.keySet()) {
            validationResults = validationResultsByDraftChange.get(draftChange);
            GwtDraftChange gwtDraftChange = draftChangeToGwtDraftChangeConverter.convert(draftChange);
            if (validationResults.hasError()) {
                StringBuilder errorBuilder = new StringBuilder();
                List<ValidationResult> errorResults = validationResults.getValidationResultsForLevel(ValidationResultLevel.ERROR);
                for (ValidationResult error : errorResults) {
                    errorBuilder.append(error.getMessage());
                    if (!errorResults.get(errorResults.size() - 1).equals(error)) {
                        errorBuilder.append("| ");
                    }
                }
                gwtDraftChange.setValidationError(errorBuilder.toString());
            }
            gwtDraftChanges.add(gwtDraftChange);
        }

        Collections.sort(gwtDraftChanges, new Comparator<GwtDraftChange>() {

            @Override
            public int compare(GwtDraftChange o1, GwtDraftChange o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });

        return gwtDraftChanges;
    }

    private boolean isValidationSuccess(Map<DraftChange, ValidationResults> validationResultsByDraftChange) {
        boolean success = true;
        for (ValidationResults validationResults : validationResultsByDraftChange.values()) {
            if (validationResults.hasError()) {
                success = false;
                break;
            }
        }
        return success;
    }
}
