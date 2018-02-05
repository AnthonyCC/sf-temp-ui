package com.freshdirect.cms.ui.editor.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extjs.gxt.ui.client.widget.form.Time;
import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.draft.service.DraftService;
import com.freshdirect.cms.ui.editor.UnmodifiableContent;
import com.freshdirect.cms.ui.editor.permission.ContentChangeSource;
import com.freshdirect.cms.ui.editor.permission.service.PermissionService;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.publish.GwtValidationError;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.exception.ValidationFailedException;
import com.google.common.base.Optional;

@Service
public class ContentUpdateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentUpdateService.class);

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private ContextualContentProvider contentProviderService;

    @Autowired
    private IndexingService indexingService;

    @Autowired
    private ContentChangesService contentChangesService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DraftContextHolder draftContextHolder;

    @Autowired
    private DraftService draftService;

    public GwtSaveResponse updateContent(GwtUser author, Collection<GwtContentNode> nodes, String note, ContentChangeSource changeOrigin) {

        GwtSaveResponse response = null;

        if (author == null) {
            LOGGER.error("No author");
            response = new GwtSaveResponse("No author is provided");
        } else if (nodes == null || nodes.isEmpty()) {
            LOGGER.error("No or empty content to save");
            response = new GwtSaveResponse("No or empty content to save");
        } else {
            LinkedHashMap<ContentKey, Map<Attribute, Object>> payload = collectChanges(nodes);

            response = updateContent(author, payload, note, changeOrigin);
        }

        return response;
    }

    public GwtSaveResponse updateContent(GwtUser author, LinkedHashMap<ContentKey, Map<Attribute, Object>> payload, String note, ContentChangeSource changeOrigin) {
        if (author == null) {
            LOGGER.error("No author");
            return new GwtSaveResponse("No author is provided");
        } else if (payload == null || payload.isEmpty()) {
            LOGGER.warn("No or empty content to save");
            return new GwtSaveResponse("No or empty content to save");
        }

        ContentUpdateContext context = new ContentUpdateContext(author.getName(), new Date(), note, draftContextHolder.getDraftContext(), payload.keySet());

        GwtSaveResponse response = null;
        try {

            if (permissionService.isSaveAllowed(author, payload, changeOrigin)) {

                Optional<ContentChangeSetEntity> updateResult = contentProviderService.updateContent(payload, context);

                if (draftContextHolder.getDraftContext().isMainDraft()) {
                    indexingService.indexChanged(payload);
                    response = createSuccessfulUpdateResponse(updateResult);
                } else {
                    response = createSuccessfulDraftUpdateResponse(context);
                }

            } else {
                LOGGER.error("Permission error while saving for " + author.getName() + "/" + author.getPersonaName() + " on " + author.getDraftName());
                response = new GwtSaveResponse("You have no efficient rights to save");
            }

        } catch (ValidationFailedException validationFailedException) {
            LOGGER.error("Content Update Failed", validationFailedException);

            response = createFailedUpdateResponse(validationFailedException);
        } catch (Exception exc) {
            LOGGER.error("Content Update Failed", exc);
            response = new GwtSaveResponse(exc.getMessage());
        }

        return response;
    }

    private GwtSaveResponse createSuccessfulUpdateResponse(Optional<ContentChangeSetEntity> optionalUpdateResult) {
        GwtSaveResponse response = null;
        if (optionalUpdateResult.isPresent()) {

            GwtChangeSet singleChangeSet = contentChangesService.toGwtChangeSet(optionalUpdateResult.get(), ContentChangesService.EMPTY_CHANGESET_QUERY);

            response = new GwtSaveResponse(singleChangeSet.getChangeSetId(), singleChangeSet);
        } else {
            response = new GwtSaveResponse();
        }

        return response;
    }

    private GwtSaveResponse createSuccessfulDraftUpdateResponse(ContentUpdateContext updateContext) {
        String id = "draft: " + updateContext.getDraftContext().getDraftId();

        List<DraftChange> latestChanges = draftService.getFilteredDraftChanges(updateContext.getDraftContext().getDraftId(), updateContext.getUpdatedAt(), updateContext.getAuthor(), updateContext.getChangedKeys());

        GwtChangeSet singleChangeset = contentChangesService.toGwtChangeSet(id, latestChanges, updateContext.getDraftContext());

        return new GwtSaveResponse(id, singleChangeset);
    }

    private GwtSaveResponse createFailedUpdateResponse(ValidationFailedException validationFailedException) {
        List<GwtValidationError> gwtValidationErrors = new ArrayList<GwtValidationError>();
        List<ValidationResult> validationResults = validationFailedException.getValidationResults();
        for (ValidationResult validationResult : validationResults) {
            gwtValidationErrors
                    .add(new GwtValidationError(validationResult.getValidatedObject().toString(), validationResult.getValidatedObject().toString(), validationResult.getMessage()));
        }
        return new GwtSaveResponse(gwtValidationErrors);
    }

    private LinkedHashMap<ContentKey, Map<Attribute, Object>> collectChanges(Collection<GwtContentNode> nodes) {
        LinkedHashMap<ContentKey, Map<Attribute, Object>> changes = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();

        for (GwtContentNode changedNode : nodes) {
            String key = changedNode.getKey();
            ContentKey contentKey = ContentKeyFactory.get(key);

            // make sure virtual or ERPS data cannot be saved
            if (UnmodifiableContent.isModifiable(contentKey)) {
                Map<Attribute, Object> changedContent = extractChangedValuesFromEditedNode(changedNode);
                changes.put(contentKey, changedContent);
            } else {
                LOGGER.debug("Excluding unmodifiable content with key " + contentKey + " from payload");
            }

        }

        return changes;
    }

    private Map<Attribute, Object> extractChangedValuesFromEditedNode(GwtContentNode changedNode) {
        ContentKey contentKey = ContentKeyFactory.get(changedNode.getKey());

        Map<Attribute, Object> changedContent = new HashMap<Attribute, Object>();
        for (String attributeName : changedNode.getChangedValueKeys()) {
            Optional<Attribute> optionalAttribute = lookupAttribute(contentKey.type, attributeName);
            if (optionalAttribute.isPresent()) {
                Attribute attribute = optionalAttribute.get();

                if (!attribute.isReadOnly()) {
                    Object original = getServerValue(changedNode.getOriginalAttributeValue(attributeName), attribute.getFlags().isInheritable());
                    Object changed = getServerValue(changedNode.getAttributeValue(attributeName), attribute.getFlags().isInheritable());

                    if (!nullSafeEquals(original, changed)) {
                        changedContent.put(attribute, changed);
                    }
                } else {
                    LOGGER.debug("Skip updating read-only attribute " + attribute.getName() + " of " + contentKey);
                }
            } else {
                LOGGER.error("Attribute definition '" + attributeName + "' for type " + contentKey.type + " not found");
                LOGGER.error("Content " + contentKey + " will miss a change!");
            }
        }
        return changedContent;
    }

    /**
     * Look up attribute by name related to given type including inherited ones.
     *
     * @param type
     * @param attributeName
     * @return
     */
    private Optional<Attribute> lookupAttribute(ContentType type, String attributeName) {
        Collection<Attribute> attributes = contentTypeInfoService.selectAttributes(type);

        Attribute matchedAttribute = null;

        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(attributeName)) {
                matchedAttribute = attribute;
                break;
            }
        }

        return Optional.fromNullable(matchedAttribute);
    }

    private static Object getServerValue(Serializable value, boolean inherited) {
        if (value instanceof ContentNodeModel) {
            return ContentKeyFactory.get(((ContentNodeModel) value).getKey());
        }
        if (value instanceof EnumModel) {
            return ((EnumModel) value).getKey();
        }
        if (value instanceof Time) {
            return ((Time) value).getDate();
        }
        if (value instanceof List) {
            List<Object> relationshipValue = (List<Object>) value;

            if (inherited) {
                if (relationshipValue.isEmpty()) {
                    return null;
                } else if (relationshipValue.size() == 1
                        && (relationshipValue.get(0) instanceof ContentNodeModel)
                        && GwtContentNode.NULL_TYPE.equals(((ContentNodeModel)relationshipValue.get(0)).getType()) ) {
                    return Collections.<ContentKey>emptyList();
                }
            }

            List<Object> result = new ArrayList<Object>();
            for (Object item : relationshipValue) {
                result.add(getServerValue((Serializable) item, false));
            }
            return result;
        }
        return value;
    }

    public static boolean nullSafeEquals(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

}
