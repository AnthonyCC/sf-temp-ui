package com.freshdirect.cms.ui.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsRequestI.RunMode;
import com.freshdirect.cms.application.CmsRequestI.Source;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draft.service.DraftService;
import com.freshdirect.cms.application.permission.service.PersonaService;
import com.freshdirect.cms.merge.MergeResult;
import com.freshdirect.cms.merge.ValidationResult;
import com.freshdirect.cms.search.IBackgroundProcessor;
import com.freshdirect.cms.ui.model.draft.GwtDraftChange;
import com.freshdirect.cms.ui.service.GwtDraftService;
import com.freshdirect.cms.ui.service.GwtSecurityException;
import com.freshdirect.cms.ui.service.ServerException;
import com.freshdirect.cms.ui.translator.TranslatorToGwt;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;

public class GwtDraftServiceImpl extends GwtServiceBase implements GwtDraftService {

    private static final long serialVersionUID = -2141795710378150875L;

    private static final Logger LOGGER = LoggerFactory.getInstance(GwtDraftServiceImpl.class);

    private IBackgroundProcessor backgroundProcessor;
    private DraftService draftService;

    public GwtDraftServiceImpl() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();

        this.draftService = DraftService.defaultService();
        this.backgroundProcessor = (IBackgroundProcessor) FDRegistry.getInstance().getService("com.freshdirect.cms.backgroundProcessor", IBackgroundProcessor.class);
    }

    @Override
    public List<GwtDraftChange> loadDraftChanges() throws ServerException {
        return fetchAllDraftChanges(getDraftContext());
    }

    @Override
    public List<GwtDraftChange> validateDraft() throws ServerException {
        final CmsUser user = getCmsUser();

        if (!user.isHasAccessToDraftBranches()) {
            throw new GwtSecurityException("User " + user.getName() + " is not allowed to validate branch!");
        }

        validateUserActionForDraft(user);

        List<GwtDraftChange> draftChanges = loadDraftChanges();

        // setup validation task
        CmsRequestI cmsRequest = new CmsRequest(user, Source.MERGE, user.getDraftContext(), RunMode.DRY);
        ValidationResult context = new ValidationResult(cmsRequest);
        Future<ValidationResult> promise = backgroundProcessor.validateDraft(context);

        while (!promise.isDone()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new ServerException(e);
            }
        }

        // process validation result
        try {
            // update context
            context = promise.get();
        } catch (InterruptedException e) {
            throw new ServerException(e);
        } catch (ExecutionException e) {
            throw new ServerException(e);
        }

        final List<GwtDraftChange> payload;
        if (!context.isSuccess()) {
            payload = processMergeResult(draftChanges, context);
        } else {
            payload = draftChanges;
        }

        return payload;
    }

    @Override
    public List<GwtDraftChange> mergeDraft() throws ServerException {
        final HttpServletRequest request = getThreadLocalRequest();
        final CmsUser user = getCmsUserFromRequest(request);

        if (!user.isHasAccessToDraftBranches()) {
            throw new GwtSecurityException("User " + user.getName() + " is not allowed to validate branch!");
        }

        validateUserActionForDraft(user);

        List<GwtDraftChange> draftChanges = loadDraftChanges();

        // setup validation task
        CmsRequestI cmsRequest = new CmsRequest(user, Source.MERGE, user.getDraftContext());
        MergeResult context = new MergeResult(cmsRequest);
        Future<MergeResult> promise = backgroundProcessor.mergeDraft(context);

        while (!promise.isDone()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new ServerException(e);
            }
        }

        try {
            // update context
            context = promise.get();
        } catch (InterruptedException e) {
            throw new ServerException(e);
        } catch (ExecutionException e) {
            throw new ServerException(e);
        }

        // process merge result
        final List<GwtDraftChange> payload;
        if (!context.isSuccess()) {
            payload = processMergeResult(draftChanges, context);
        } else {
            payload = draftChanges;

            // success - drop session before returning ...
            PersonaService.defaultService().invalidatePersona(request.getUserPrincipal().getName());
        }

        return payload;
    }

    private void validateUserActionForDraft(CmsUser user) {
        List<Draft> notDroppedOrMergedDrafts = draftService.getDrafts();
        boolean isDraftNotDroppedOrMerged = false;
        for (Draft draft : notDroppedOrMergedDrafts) {
            if (draft.getId() == user.getDraftContext().getDraftId()) {
                isDraftNotDroppedOrMerged = true;
                break;
            }
        }
        if (!isDraftNotDroppedOrMerged) {
            throw new GwtSecurityException("Draft " + user.getDraftContext().getDraftName() + " is already merged or deleted!");
        }
    }

    protected List<GwtDraftChange> processMergeResult(List<GwtDraftChange> draftChanges, MergeResult mergeResult) {
        List<GwtDraftChange> result = new ArrayList<GwtDraftChange>(draftChanges);
        for (final DraftChange dc : mergeResult.getInvalidChanges()) {
            final Long id = dc.getId();
            boolean processed = false;
            // try to match validation error to one of existing draft changes
            for (GwtDraftChange gdc : draftChanges) {
                if (gdc.getDraftId() != null && gdc.getDraftId().equals(id)) {
                    gdc.setValidationError(mergeResult.getErrorMessage(dc));
                    processed = true;
                }
            }
            // special case - validator task generated an additional (virtual) draft change
            if (!processed) {
                GwtDraftChange virtualChange = TranslatorToGwt.convertDraftChangeToGwtDraftChange(dc);
                virtualChange.setValidationError(mergeResult.getErrorMessage(dc));
                result.add(virtualChange);
            }
        }
        return result;
    }

    private List<GwtDraftChange> fetchAllDraftChanges(final DraftContext draftContext) throws ServerException {
        List<GwtDraftChange> draftChanges;
        if (draftContext != DraftContext.MAIN) {
            draftChanges = TranslatorToGwt.convertDraftChangesToGwtDraftChanges(draftService.getDraftChanges(draftContext.getDraftId()));
        } else {
            LOGGER.error("Attempted to fetch draft changes in MAIN draft context");
            throw TranslatorToGwt.wrap(new ServerException("Draft Changes cannot be obtained in MAIN draft context"));
        }
        return draftChanges;
    }
}
