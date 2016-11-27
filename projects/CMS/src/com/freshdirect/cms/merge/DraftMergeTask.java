package com.freshdirect.cms.merge;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsRequestI.RunMode;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draft.service.DraftService;
import com.freshdirect.cms.validation.ContentValidationException;
import com.freshdirect.cms.validation.ContentValidationMessage;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DraftMergeTask implements MergeTask {

    private static final Logger LOGGER = LoggerFactory.getInstance(DraftMergeTask.class);

    @Override
    public void execute(MergeResult merge) {
        final CmsRequestI request = merge.getCmsRequest();
        final ContentServiceI cmsManager = CmsManager.getInstance();
        final DraftContext draftContext = request.getDraftContext();

        // fetch draft changes
        List<DraftChange> draftChanges = DraftService.defaultService().getDraftChanges(draftContext.getDraftId());

        // setup validation scope
        final DraftValidationScope validationScope = (new DraftValidationScope.Builder())
                .setContentService(cmsManager).setDraftContext(draftContext)
                .addDraftChanges(draftChanges)
                .build();

        // extra task - validate nodes affected by draft changes but they are not modified
        if (!validationScope.getExpandedKeys().isEmpty()) {
            // prepare a dedicated request to validate affected nodes
            CmsRequestI validateExtraKeysRequest = new CmsRequest(request.getUser(), request.getSource(), request.getDraftContext(), RunMode.DRY);
            populateRequestWithNodes(validateExtraKeysRequest, validationScope.getExpandedKeys(), cmsManager, request.getDraftContext());

            try {
                // validate 'expanded' nodes
                cmsManager.handle(validateExtraKeysRequest);
            } catch (ContentValidationException e) {
                LOGGER.debug("One of the affected nodes is invalid!");
                processValidationErrors(e.getDelegate().getValidationMessages(), draftChanges, validationScope, merge);
            }

        }

        // prepare a request for the merge/validation process with the nodes came with the original request
        CmsRequestI mergeRequest = prepareRequestForMergeOrValidation(request);
        populateRequestWithNodes(mergeRequest, validationScope.getContentKeys(), cmsManager, draftContext);

        // finally, do merge/validation or die
        try {
            cmsManager.handle(mergeRequest);
        } catch (ContentValidationException e) {
            processValidationErrors(e.getDelegate().getValidationMessages(), draftChanges, validationScope, merge);
        }
    }

    /**
     * Populate freshly created CMS request with content nodes referenced by input keys
     * 
     * @param request
     *            CMS request object
     * @param keys
     *            Content Keys
     * @param svc
     *            Content Service
     * @param draftContext
     *            Draft Context
     * 
     * @return the same inbound request populated with nodes
     */
    private CmsRequestI populateRequestWithNodes(CmsRequestI request, Set<ContentKey> keys, ContentServiceI svc, DraftContext draftContext) {
        Map<ContentKey, ContentNodeI> nodes = svc.getContentNodes(keys, draftContext);
        for (ContentNodeI node : nodes.values()) {
            request.addNode(node);
        }
        return request;
    }

    private CmsRequestI prepareRequestForMergeOrValidation(final CmsRequestI request) {
        final CmsRequestI clonedRequest;
        if (!request.isDryMode()) {
            // merge mode - set draft context to mainline
            clonedRequest = new CmsRequest(request.getUser(), request.getSource(), DraftContext.MAIN, request.getRunMode());
        } else {
            // validation mode - validate on specific draft
            clonedRequest = new CmsRequest(request.getUser(), request.getSource(), request.getDraftContext(), request.getRunMode());
        }
        return clonedRequest;
    }

    private void processValidationErrors(final Collection<ContentValidationMessage> validationMessages, final Collection<DraftChange> draftChanges, DraftValidationScope scope,
            MergeResult result) {
        final Set<ContentKey> draftContentKeys = scope.getContentKeys();
        final Set<ContentKey> relatedContentKeys = scope.getExpandedKeys();

        // build draft change collection to key->attribute->change structure
        final DraftChangeLookupMap draftLookupMap = new DraftChangeLookupMap(draftChanges);
        
        // setup a draft pojo for virtual changes
        final Draft draft = new Draft();
        draft.setId(result.getCmsRequest().getDraftContext().getDraftId());
        draft.setName(result.getCmsRequest().getDraftContext().getDraftName());

        // match validation errors to draft changes
        for (final ContentValidationMessage message : validationMessages) {
            final ContentKey messageContentKey = message.getContentKey();

            if (draftContentKeys.contains(messageContentKey)) {
                final DraftChange matchedChange = draftLookupMap.lookupDraftChange(messageContentKey, message.getAttribute());

                if (matchedChange == null) {
                    LOGGER.error("No corresponding draft change found for invalid node (2)" + messageContentKey);
                } else {
                    result.addResult(matchedChange, message.getMessage());
                }
            } else if (relatedContentKeys.contains(messageContentKey)) {
                // this is a related key, no draft change

                DraftChange virtualChange = createVirtualChange(draft, messageContentKey, "(parent keys)", "-");
                virtualChange.setUserName(result.getCmsRequest().getUser().getName());
                result.addResult(virtualChange, message.getMessage());

                // pick draft changes caused this
                for (Long changeId : scope.lookupDraftChangeIds(messageContentKey)) {
                    for (DraftChange change : draftChanges) {
                        if (changeId.equals(change.getId())) {
                            result.addResult(change, "This change broke '" + messageContentKey.getEncoded() + "'");
                        }
                    }
                }
            }

        }
    }

    protected DraftChange createVirtualChange(Draft draft, ContentKey key, String attributeName, String value) {
        DraftChange virtualChange = new DraftChange();
        virtualChange.setDraft(draft);
        virtualChange.setId(0L);
        virtualChange.setCreatedAt(System.currentTimeMillis());
        virtualChange.setContentKey(key.getEncoded());
        virtualChange.setAttributeName(attributeName);
        virtualChange.setValue(value);
        
        return virtualChange;
    }
}
