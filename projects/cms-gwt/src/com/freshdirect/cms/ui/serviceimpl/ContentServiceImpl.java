package com.freshdirect.cms.ui.serviceimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.ContentNodeComparator;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsPermissionI;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsRequestI.Source;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draft.service.DraftService;
import com.freshdirect.cms.changecontrol.ChangeLogServiceI;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.fdstore.FDRootKey;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.cms.merge.MergeException;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.node.ChangedContentNode;
import com.freshdirect.cms.node.ContentNode;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.PublishServiceI;
import com.freshdirect.cms.publish.PublishX;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.NavigableRelationInfo;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute.ProductConfigParams;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.changeset.Comparators;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.changeset.GwtNodeChange;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;
import com.freshdirect.cms.ui.model.publish.GwtValidationError;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.GwtSecurityException;
import com.freshdirect.cms.ui.service.ServerException;
import com.freshdirect.cms.ui.translator.TranslatorFromGwt;
import com.freshdirect.cms.ui.translator.TranslatorToGwt;
import com.freshdirect.cms.util.CmsPermissionManager;
import com.freshdirect.cms.util.CmsPermissionManager.Permit;
import com.freshdirect.cms.util.CmsPermissionUtility;
import com.freshdirect.cms.util.PrimaryHomeUtil;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidationException;
import com.freshdirect.cms.validation.ContentValidationMessage;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.cms.validation.UniqueContentKeyValidator;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("/contentService")
public class ContentServiceImpl extends GwtServiceBase implements ContentService {

    private static final long serialVersionUID = 1893834571301941106L;
    private static final Logger LOG = LoggerFactory.getInstance(ContentServiceImpl.class);
    private static final int MAX_HITS = 120;
    /**
     * If previous publish date is not available (publish not or once done) then let the lower bound of changesets for a publish is the earliest one.
     */
    private static final Date TIME_ZERO = new Date(0);

    public ContentServiceImpl() {
        super();
    }

    @Override
    public String getGoogleMapsApiKey() {
        return FDStoreProperties.getGoogleMapsAPIKey();
    }

    @Override
    public String getPreviewUrl(String nodeKey, String storeId) {
        ContentKey key = ContentKey.getContentKey(nodeKey);

        ContentKey storeKey = null;
        if (storeId != null) {
            try {
                storeKey = ContentKey.create(FDContentTypes.STORE, storeId);
            } catch (InvalidContentKeyException e) {
                LOG.error("Invalid store key " + e);
                return null;
            }
        } else {
            // Fall back to FreshDirect
            try {
                storeKey = ContentKey.create(FDContentTypes.STORE, "FreshDirect");
            } catch (InvalidContentKeyException e) {
                LOG.error("Invalid store key " + e);
                return null;
            }
        }

        return PreviewLinkProvider.getLink(key, storeKey, contentService, getDraftContext());
    }

    // ====================== User ======================

    @Override
    public GwtUser getUser() {
        HttpServletRequest request = getThreadLocalRequest();
        return getUserFromRequest(request);
    }

    /**
     * Return a GWT User instance from a request
     *
     * @param request
     * @return
     */
    protected static GwtUser getUserFromRequest(HttpServletRequest request) {
        final CmsUser cmsUser = getCmsUserFromRequest(request);
        final GwtUser gwtUser = TranslatorToGwt.getGwtUser(cmsUser);
        return gwtUser;
    }

    // ====================== Content tree ======================
    @Override
    public List<TreeContentNodeModel> search(String searchTerm) {
        // FIXME expose search-like methods via service
        Collection<SearchHit> hits = ((CmsManager) contentService).search(searchTerm, false, MAX_HITS);
        List<ContentNodeI> resultNodes = new ArrayList<ContentNodeI>(hits.size());
        List<TreeContentNodeModel> result = new ArrayList<TreeContentNodeModel>(hits.size());

        final DraftContext draftContext = getDraftContext();

        for (SearchHit hit : hits) {
            resultNodes.add(contentService.getContentNode(hit.getContentKey(), draftContext));
        }
        Collections.sort(resultNodes, ContentNodeComparator.DEFAULT);

        for (ContentNodeI node : resultNodes) {
            if (node == null)
                continue;
            result.add(TranslatorToGwt.toTreeContentNodeModel(node, contentService, draftContext));
        }

        return result;
    }

    @Override
    public List<TreeContentNodeModel> getChildren(TreeContentNodeModel parentNode) throws ServerException {
        try {
            ArrayList<TreeContentNodeModel> children = new ArrayList<TreeContentNodeModel>();

            final DraftContext draftContext = getDraftContext();

            if (parentNode == null) {
                final List<ContentKey> rootKeys = FDRootKey.getAllRootKeysIncluding();
                for (ContentKey key : rootKeys) {
                    ContentNodeI cn = contentService.getContentNode(key, draftContext);
                    if (cn != null) {
                        children.add(TranslatorToGwt.toTreeContentNodeModel(cn, contentService, draftContext));
                    }
                }
                return children;
            }

            ContentNodeI root = contentService.getContentNode(ContentKey.getContentKey(parentNode.getKey()), draftContext);
            if (root != null) {
                List<ContentKey> childKeys = new ArrayList<ContentKey>(root.getChildKeys());
                TreeSet<ContentNodeI> nodes = getOrderedNodes(childKeys, draftContext);
                for (ContentNodeI childNode : nodes) {
                    children.add(TranslatorToGwt.toTreeContentNodeModel(childNode, parentNode, contentService, draftContext));
                }
            }
            return children;

        } catch (Throwable e) {
            LOG.error("runtime exception for " + parentNode, e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    private TreeSet<ContentNodeI> getOrderedNodes(Collection<ContentKey> childKeys, DraftContext draftContext) {
        TreeSet<ContentNodeI> nodes = new TreeSet<ContentNodeI>(ContentNodeComparator.DEFAULT);
        for (ContentKey key : childKeys) {
            ContentNodeI node = contentService.getContentNode(key, draftContext);
            if (node != null)
                nodes.add(node);
        }
        return nodes;
    }

    // ====================== Node data ======================
    @Override
    public GwtNodeData loadNodeData(String nodeKey) throws ServerException {
        try {
            final ContentKey ncKey = ContentKey.getContentKey(nodeKey);

            final CmsUser permission = getCmsUser();
            DraftContext draftContext = getDraftContext();
            final ContentNodeI node = contentService.getContentNode(ncKey, draftContext);

            // apply permissions to the current node

            GwtNodePermission gwtPermission = setupClientPermissions(node, permission, draftContext);
            /* END OF BIG PERMISSION EVALUATOR */

            GwtNodeData gwtNode = TranslatorToGwt.gwtNodeData(node, gwtPermission, false, contentService, draftContext);
            gwtNode.setParentMap(getParentMapping(ncKey, draftContext));

            return gwtNode;

        } catch (IllegalArgumentException e) {
            LOG.error("IllegalArgumentException for " + nodeKey, e);
            throw new ServerException("Invalid content key : " + e.getMessage());
        } catch (Throwable e) {
            LOG.error("Runtime Exception : " + e.getMessage(), e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    protected GwtNodePermission setupClientPermissions(final ContentNodeI node, final CmsPermissionI permissionHolder, DraftContext draftContext) {
        GwtNodePermission nodePermission = new GwtNodePermission();
        final ContentKey ncKey = node.getKey();

        /*
         * Evaluate node permissions Input: scope permission, type permissions Output: node editable (or read-only) create and delete-type restrictions
         */
        final Permit scopePermit = CmsPermissionManager.requestEditPermission(node, permissionHolder, contentService, draftContext);
        final boolean hasAnyTypeBasedPermission = permissionHolder.isAnyContentTypeBasedPermission();
        boolean doCalculateCreateDeleteSets = false;
        if (scopePermit == Permit.REJECT) {
            /// ** No scope based permission **

            if (!hasAnyTypeBasedPermission) {
                /// Neither allowed types => node will be READ-ONLY
                nodePermission.setEditable(false);
            } else {
                /// Node has type based permissions
                if (permissionHolder.isContentTypeBasedPermissionEnabled(ncKey.getType())) {
                    /// Node type is allowed
                    nodePermission.setEditable(true);

                    doCalculateCreateDeleteSets = true;
                } else {
                    /// Node is not allowed (not in allowed types)
                    nodePermission.setEditable(false);
                }
            }
        } else {
            /// *** Scope based permission is active ***
            nodePermission.setEditable(true);
        }

        // calculate additional restrictions
        if (doCalculateCreateDeleteSets) {
            final Set<ContentType> typesNoCreate = CmsPermissionManager.getTypesDisallowedToCreate(node, permissionHolder, contentService);
            final Set<ContentType> typesNoDelete = CmsPermissionManager.getTypesDisallowedToDelete(node, permissionHolder, contentService);
            nodePermission.setDisallowedForCreateTypes(TranslatorToGwt.mapCollectionToString(typesNoCreate));
            nodePermission.setDisallowedForDeleteTypes(TranslatorToGwt.mapCollectionToString(typesNoDelete));
        }

        // multi-homed products
        final ContentType type = node.getKey().getType();
        if (type.equals(FDContentTypes.PRODUCT) || type.equals(FDContentTypes.CONFIGURED_PRODUCT) || type.equals(FDContentTypes.CONFIGURED_PRODUCT_GROUP)) {
            final Set<ContentKey> allowedStoreKeys = CmsPermissionUtility.getAllowedStoreKeys(permissionHolder);
            nodePermission.setAllowedStores(TranslatorToGwt.mapCollectionToString(allowedStoreKeys));
        }

        return nodePermission;
    }

    /**
     * Method called from client-side requesting a new node
     *
     * @param type
     *            Content type
     * @param id
     *            ID to allocate
     * @return properly prepared content node prototype
     *
     * @throws ServerException
     */
    @Override
    public GwtNodeData createNodeData(String type, String id) throws ServerException {
        // candidate node
        final DraftContext draftContext = getDraftContext();
        final ContentNodeI node = createPrototypeNode(type, id, draftContext);

        final GwtNodeData gwtNode;
        if (node != null) {
            GwtNodePermission gwtPermission = GwtNodePermission.FULL_PERMISSION;

            gwtNode = TranslatorToGwt.gwtNodeData(node, gwtPermission, true, contentService, draftContext);
            gwtNode.setParentMap(new HashMap<String, Set<String>>());
        } else {
            gwtNode = null;
        }

        return gwtNode;
    }

    private ContentNodeI createPrototypeNode(final String type, final String id, DraftContext draftContext) throws ServerException {
        ContentNodeI node;
        try {
            ContentKey key = ContentKey.create(ContentType.get(type), id);

            // Additional check: check whether key is unique
            if (!checkUniqueContentKey(key, contentService, draftContext)) {
                throw new ServerException("Content ID '" + id + "' is not unique!");
            }

            ContentNodeI oldNode = contentService.getRealContentNode(key, draftContext);
            if (oldNode != null) {
                // there is already a node, return null.
                return null;
            }

            node = contentService.createPrototypeContentNode(key, draftContext);
        } catch (InvalidContentKeyException e) {
            e.printStackTrace();
            throw new ServerException("Invalid content key : " + type + ":" + id);
        }

        return node;
    }

    private boolean checkUniqueContentKey(ContentKey cKey, ContentServiceI contentService, DraftContext draftContext) {
        final ContentValidatorI v = new UniqueContentKeyValidator();
        final ContentValidationDelegate delegate = new ContentValidationDelegate();

        // fake nodeSimpleContentService
        ContentNodeI node = new ContentNode(contentService, cKey);

        v.validate(delegate, contentService, draftContext, node, null, null);

        return delegate.isEmpty();
    }

    // ====================== Save ======================
    @Override
    public GwtSaveResponse save(Collection<GwtContentNode> nodes) throws ServerException {
        HttpServletRequest request = getThreadLocalRequest();

        final DraftContext draftContext = getDraftContext();
        if (isNodeModificationEnabled(draftContext)) {
            // assemble set of changed content nodes
            List<ContentNodeI> changedNodes = new ArrayList<ContentNodeI>(nodes.size());
            for (GwtContentNode gwtNode : nodes) {
                // fetch original content node
                ContentNodeI node = TranslatorFromGwt.getContentNode(gwtNode, draftContext);

                ChangedContentNode cNode = new ChangedContentNode(node.copy());

                // apply changes made in CMS Editor
                for (String attrName : gwtNode.getChangedValueKeys()) {
                    final Object originalValue = TranslatorFromGwt.getServerValue(gwtNode.getOriginalAttributeValue(attrName));
                    final Object changedValue = TranslatorFromGwt.getServerValue(gwtNode.getAttributeValue(attrName));
                    if (!NVL.nullEquals(originalValue, changedValue)) {
                        LOG.debug("Record attribute change of " + attrName);
                        cNode.setAttributeValue(attrName, changedValue);
                    } else {
                        LOG.debug("Skip fake attribute change of " + attrName);
                    }
                }

                changedNodes.add(cNode);
            }

            // update CMS with changes
            return saveNodes(request, changedNodes, Source.NODE_EDITOR);
        } else {
            throw new ServerException("Can't save nodes as a publish is in progress");
        }
    }

    public static GwtSaveResponse saveNodes(HttpServletRequest request, Collection<ContentNodeI> changedNodes, CmsRequestI.Source source) throws ServerException {
        CmsUser user = getCmsUserFromRequest(request);
        CmsManager cmsManager = CmsManager.getInstance();
        DraftContext draftContext = user.getDraftContext();
        if (isNodeModificationEnabled(draftContext)) {

            try {
                // setup change request
                CmsRequest cmsRequest = new CmsRequest(user, source, draftContext);
                for (ContentNodeI node : changedNodes) {
                    cmsRequest.addNode(node);
                }

                long now = System.currentTimeMillis();

                // make changes
                CmsResponseI responseI = cmsManager.handle(cmsRequest);

                String id = null;
                GwtChangeSet gwtChangeSet = null;
                if (responseI != null && responseI.getChangeSetId() != null) {
                    id = responseI.getChangeSetId().getId();
                    ChangeSet changeSet = getChangeLogService().getChangeSet(responseI.getChangeSetId());
                    gwtChangeSet = TranslatorToGwt.getGwtChangeSet(changeSet, new ChangeSetQuery(), cmsManager, draftContext);
                } else if (!draftContext.isMainDraft()) {
                    id = "draft: " + draftContext.getDraftId();
                    List<DraftChange> latestChanges = DraftService.defaultService().getFilteredDraftChanges(draftContext.getDraftId(), now, cmsRequest.getUser().getName());
                    gwtChangeSet = TranslatorToGwt.getGwtChangeSet(id, latestChanges, cmsManager, draftContext);
                }
                return new GwtSaveResponse(id, gwtChangeSet);
            } catch (MergeException e) {
                String errorMessage = e.getMessage();
                LOG.error(errorMessage);
                return new GwtSaveResponse(errorMessage);
            } catch (ContentValidationException v) {
                List<ContentValidationMessage> messages = v.getDelegate().getValidationMessages();
                List<GwtValidationError> errors = new ArrayList<GwtValidationError>();
                for (ContentValidationMessage msg : messages) {
                    errors.add(new GwtValidationError(msg.getContentKey().getEncoded(), msg.getAttribute(), msg.getMessage()));
                }
                return new GwtSaveResponse(errors);
            } catch (SecurityException exc) {
                LOG.error("Action denied " + changedNodes, exc);
                throw TranslatorToGwt.wrap(exc);
            } catch (Throwable e) {
                LOG.error("RuntimeException saving  " + changedNodes, e);
                throw TranslatorToGwt.wrap(e);
            }
        } else {
            throw new ServerException("Can't save nodes as a publish is in progress");
        }
    }

    // ====================== Changesets & Publish ======================
    private PublishServiceI getPublishService() {
        return (PublishServiceI) FDRegistry.getInstance().getService("com.freshdirect.cms.publish.PublishService", PublishServiceI.class);
    }

    private PublishServiceI getFeedPublishService() {
        return (PublishServiceI) FDRegistry.getInstance().getService("com.freshdirect.cms.publish.FeedPublishService", PublishServiceI.class);
    }

    private static ChangeLogServiceI getChangeLogService() {
        return (ChangeLogServiceI) FDRegistry.getInstance().getService(ChangeLogServiceI.class);
    }

    @Override
    public ChangeSetQueryResponse getChangeSets(ChangeSetQuery query) throws ServerException {
        try {
            ChangeLogServiceI chgService = getChangeLogService();
            ChangeSetQueryResponse response = null;
            DraftContext draftContext = getCmsUser().getDraftContext();

            if (query.isChangeSetQuery()) {
                ContentKey key;
                try {
                    key = ContentKey.getContentKey(query.getContentKey());
                } catch (IllegalArgumentException ex) {
                    key = null;
                }
                List<ChangeSet> list = chgService.getChangeSets(key, query.getUser(), query.getStartDate(), query.getEndDate());
                List<GwtChangeSet> changeHistory = TranslatorToGwt.getGwtChangeSets(list, query, contentService, draftContext);
                response = createResponse(changeHistory, query, null);
                response.setLabel("Query result : " + response.getChangeCount() + " changes in " + response.getChangeSetCount() + " changesets.");

            } else if (query.getPublishId() != null) {
                response = getChangeSetsByPublishId(query, chgService, draftContext);
                response.setLabel("Changes in publish #" + query.getPublishId());

            } else if (query.getContentKey() != null) {
                ContentKey key = ContentKey.getContentKey(query.getContentKey());
                List<GwtChangeSet> changeHistory = TranslatorToGwt.getGwtChangeSets(chgService.getChangeHistory(key), query, contentService, draftContext);
                response = createResponse(changeHistory, query, null);
                response.setLabel("History of " + contentService.getContentNode(key, draftContext).getLabel());

            }

            return response;
        } catch (Throwable e) {
            LOG.error("RuntimeException saving  " + query, e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    protected ChangeSetQueryResponse getChangeSetsByPublishId(ChangeSetQuery query, ChangeLogServiceI chgService, DraftContext draftContext) {
        if (query.getPublishId() == null || chgService == null) {
            return null;
        }

        // Query by publish ID
        PublishServiceI service = getPublishService();
        Date prevTimestamp;
        Date timestamp;
        Publish publish;
        if ("latest".equalsIgnoreCase(query.getPublishId())) {
            timestamp = new Date();
            if (query.getPublishType() == null)
                publish = service.getMostRecentPublish();
            else {
                publish = service.getMostRecentPublishX();
            }
            prevTimestamp = publish != null ? publish.getTimestamp() : TIME_ZERO;
        } else {
            if (query.getPublishType() == null) {
                publish = service.getPublish(query.getPublishId(), Publish.class);
                Publish prevPublish = service.getPreviousPublish(publish);   
                prevTimestamp = prevPublish != null ? prevPublish.getTimestamp() : TIME_ZERO;
            } else {
                publish = service.getPublish(query.getPublishId(), PublishX.class);
                Publish prevPublish = service.getPreviousFeedPublish(publish);  
                prevTimestamp = prevPublish != null ? prevPublish.getTimestamp() : TIME_ZERO;
            }
            timestamp = publish.getTimestamp();
        }
        if (query.isPublishInfoQuery()) {
            LOG.info("publish info:" + query.getPublishId() + " -> " + publish.getId() + ':' + publish.getStatus().getName() + " (" + publish.getDescription() + ')');
            List<PublishMessage> filteredMessages = TranslatorToGwt.getSeverityFilteredPublishMessages(publish.getMessages(), query.getMessageSeverity());
            return new ChangeSetQueryResponse(publish.getStatus().getName(), publish.getTimestamp(), System.currentTimeMillis() - publish.getTimestamp().getTime(),
                    TranslatorToGwt.getPublishMessages(filteredMessages, query.getPublishMessageStart(), query.getPublishMessageEnd()), getLastInfo(publish));
        } else {

            LOG.info("collecting changes from " + prevTimestamp + " to " + timestamp + ", query:" + query);
            List<ChangeSet> result = prevTimestamp != null ? (List<ChangeSet>) chgService.getChangesBetween(prevTimestamp, timestamp) : new ArrayList<ChangeSet>();
            LOG.info("returning " + result.size() + " changeset" + ", query:" + query);

            // hack, to not fail with thousands of changesets ...
            return createResponse(TranslatorToGwt.getGwtChangeSets(result, query, contentService, draftContext), query, publish);
        }
    }

    private ChangeSetQueryResponse createResponse(List<GwtChangeSet> changeHistory, ChangeSetQuery query, Publish publish) {
        int changeCount = 0;
        for (GwtChangeSet gcs : changeHistory) {
            changeCount += gcs.length();
        }

        if (query.getDirection() != null && query.getDirection() != SortDir.NONE) {
            if ("user".equals(query.getSortType())) {
                if (query.getDirection() == SortDir.ASC) {
                    Collections.sort(changeHistory, Comparators.USER_COMPARATOR);
                } else {
                    Collections.sort(changeHistory, Comparators.USER_COMPARATOR_INV);
                }
            }
            if ("date".equals(query.getSortType())) {
                if (query.getDirection() == SortDir.ASC) {
                    Collections.sort(changeHistory, Comparators.DATE_COMPARATOR);
                } else {
                    Collections.sort(changeHistory, Comparators.DATE_COMPARATOR_INV);
                }
            }
        } else {
            Collections.sort(changeHistory, Comparators.DATE_COMPARATOR);
        }

        int limit = query.getLimit() <= 0 ? 1000 : Math.min(query.getLimit(), 1000);
        int end = Math.min(query.getStart() + limit, changeHistory.size());

        List<GwtChangeSet> clientChanges = null;

        if (end == changeHistory.size() && query.getStart() == 0) {
            clientChanges = changeHistory;
        } else {
            clientChanges = new ArrayList<GwtChangeSet>(changeHistory.subList(query.getStart(), end));
        }
        List<GwtPublishMessage> publishMessages = null;
        int publishMessageCount = 0;

        if (publish != null) {
            List<PublishMessage> filteredMessages = TranslatorToGwt.getSeverityFilteredPublishMessages(publish.getMessages(), query.getMessageSeverity());
            publishMessages = TranslatorToGwt.getPublishMessages(filteredMessages, query.getPublishMessageStart(), query.getPublishMessageEnd());
            publishMessageCount = filteredMessages.size();
        }

        return new ChangeSetQueryResponse(clientChanges, changeHistory.size(), changeCount, query, publishMessages, publishMessageCount);
    }

    private String getLastInfo(Publish publish) {
        String lastMessage = "running.";
        Date lastDate = null;
        for (PublishMessage m : publish.getMessages()) {
            if (m.getSeverity() == PublishMessage.INFO) {
                if (lastDate == null || lastDate.before(m.getTimestamp())) {
                    if (m.getMessage() != null && m.getMessage().trim().length() > 0) {
                        lastMessage = m.getMessage();
                        lastDate = m.getTimestamp();
                    }
                }
            }
        }
        return lastMessage;
    }

    @Override
    public String startPublish(String comment) throws ServerException {
        try {
            GwtUser user = getUser();
            if (!user.isHasAccessToPublishTab()) {
                throw new GwtSecurityException("User " + user.getName() + " is not allowed to publish!");
            }
            LOG.info("start publish called by " + user.getName() + " : '" + comment + "'");
            Publish recentPublish = getPublishService().getMostRecentNotCompletedPublish();
            LOG.info("most recent publish : " + recentPublish + ", status : " + (recentPublish != null ? recentPublish.getStatus() : null) + ", id : "
                    + (recentPublish != null ? recentPublish.getId() : null));

            if (recentPublish != null && EnumPublishStatus.PROGRESS.equals(recentPublish.getStatus())) {
                LOG.info("publish in progress ...: " + recentPublish.getId());
                return recentPublish.getId();
            }

            Date date = new Date();

            Publish publish = new Publish();
            publish.setTimestamp(date);
            publish.setUserId(user.getName());
            publish.setStatus(EnumPublishStatus.PROGRESS);
            publish.setDescription(comment);
            publish.setLastModified(date);

            LOG.info("starting new publish by " + user.getName() + ", with comment : '" + comment + "'");
            return getPublishService().doPublish(publish);
        } catch (Throwable e) {
            LOG.error("RuntimeException for startPublish ", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @Override
    public String startPublishX(String comment) throws ServerException {
        try {
            GwtUser user = getUser();
            if (!user.isHasAccessToFeedPublishTab()) {
                throw new GwtSecurityException("User " + user.getName() + " is not allowed to publish!");
            }
            LOG.info("start publish called by " + user.getName() + " : '" + comment + "'");
            PublishX recentPublish = getFeedPublishService().getMostRecentNotCompletedPublishX();
            LOG.info("most recent publish : " + recentPublish + ", status : " + (recentPublish != null ? recentPublish.getStatus() : null) + ", id : "
                    + (recentPublish != null ? recentPublish.getId() : null));

            if (recentPublish != null && EnumPublishStatus.PROGRESS.equals(recentPublish.getStatus())) {
                LOG.info("publish in progress ...: " + recentPublish.getId());
                return recentPublish.getId();
            }

            Date date = new Date();

            PublishX publish = new PublishX();
            publish.setTimestamp(date);
            publish.setUserId(user.getName());
            publish.setStatus(EnumPublishStatus.PROGRESS);
            publish.setDescription(comment);
            publish.setLastModified(date);
            LOG.info("starting new feed publish by " + user.getName() + ", with comment : '" + comment + "'");
            return getFeedPublishService().doFeedPublish(publish);
        } catch (Throwable e) {
            LOG.error("RuntimeException for startPublish ", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @Override
    public GwtPublishData getPublishData(ChangeSetQuery query) throws ServerException {
        try {
            GwtPublishData publishData;
            ChangeSetQueryResponse r = getChangeSets(query);
            ArrayList<GwtChangeSet> changes = new ArrayList<GwtChangeSet>(r.getChanges());

            Publish publish;
            PublishServiceI service = getPublishService();

            if ("latest".equalsIgnoreCase(query.getPublishId())) {
                publish = service.getMostRecentPublish();
            } else {
                publish = service.getPublish(query.getPublishId(), Publish.class);
            }

            if (publish == null) {
                publishData = new GwtPublishData();
            } else {
                publishData = TranslatorToGwt.getPublishData(publish);
            }
            publishData.setId(query.getPublishId());
            publishData.setFullyLoaded(true);
            int changeCount = 0;
            for (GwtChangeSet change : changes) {

                for (GwtNodeChange nodechange : change.getNodeChanges()) {
                    int length = nodechange.length();
                    publishData.addContributor(change.getUserId(), length);
                    publishData.addType(nodechange.getType(), length);
                    changeCount += length;
                }
            }

            publishData.setChangeCount(changeCount);

            if (publish != null) {
                for (PublishMessage message : publish.getMessages()) {
                    switch (message.getSeverity()) {
                        // Failure
                        case 0:
                            // Error
                        case 1:
                            // Warning
                        case 2:
                            publishData.addMessage(String.valueOf(message.getSeverity()));
                    }
                }

                Collections.sort(publishData.getContributors());
                Collections.sort(publishData.getTypes());
                Collections.sort(publishData.getMessages());
            }
            return publishData;

        } catch (Throwable e) {
            LOG.error("RuntimeException in getPublishHistory", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @Override
    public GwtPublishData getPublishDataX(ChangeSetQuery query) throws ServerException {
        try {
            GwtPublishData publishData;
            Publish publish;
            PublishServiceI service = getFeedPublishService();

            if ("latest".equalsIgnoreCase(query.getPublishId())) {
                publish = service.getMostRecentPublishX();
            } else {
                publish = service.getPublish(query.getPublishId(), PublishX.class);
            }

            if (publish == null) {
                publishData = new GwtPublishData();
            } else {
                publishData = TranslatorToGwt.getPublishData(publish);
            }
            publishData.setId(query.getPublishId());
            publishData.setFullyLoaded(true);
            int changeCount = 0;

            publishData.setChangeCount(changeCount);

            if (publish != null) {
                for (PublishMessage message : publish.getMessages()) {
                    switch (message.getSeverity()) {
                        // Failure
                        case 0:
                            // Error
                        case 1:
                            // Warning
                        case 2:
                            publishData.addMessage(String.valueOf(message.getSeverity()));
                    }
                }

                Collections.sort(publishData.getContributors());
                Collections.sort(publishData.getTypes());
                Collections.sort(publishData.getMessages());
            }
            return publishData;

        } catch (Throwable e) {
            LOG.error("RuntimeException in getPublishHistory", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @Override
    public List<GwtPublishData> getPublishHistory(PagingLoadConfig config) throws ServerException {
        try {
            PublishServiceI ps = getPublishService();
            List<Publish> listPublishes = ps.getPublishHistory();

            if (listPublishes.size() == 0)
                return new ArrayList<GwtPublishData>();

            List<GwtPublishData> result = new ArrayList<GwtPublishData>(listPublishes.size());
            for (Publish p : listPublishes) {
                result.add(TranslatorToGwt.getPublishData(p));
            }
            int start = config.getOffset();
            int limit = listPublishes.size();
            if (config.getLimit() > 0) {
                limit = Math.min(start + config.getLimit(), limit);
            }
            return new ArrayList<GwtPublishData>(result.subList(start, limit));
        } catch (Throwable e) {
            LOG.error("RuntimeException in getPublishHistory", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @Override
    public List<GwtPublishData> getPublishHistoryByType(PagingLoadConfig config, String type) throws ServerException {
        try {
            PublishServiceI ps = getPublishService();
            List<Publish> listPublishes = ps.getPublishHistoryByType(type);
            if (listPublishes.size() == 0)
                return new ArrayList<GwtPublishData>();
            List<GwtPublishData> result = new ArrayList<GwtPublishData>(listPublishes.size());
            for (Publish p : listPublishes) {
                result.add(TranslatorToGwt.getPublishData(p));
            }
            int start = config.getOffset();
            int limit = listPublishes.size();
            if (config.getLimit() > 0) {
                limit = Math.min(start + config.getLimit(), limit);
            }
            return new ArrayList<GwtPublishData>(result.subList(start, limit));
        } catch (Throwable e) {
            LOG.error("RuntimeException in getPublishHistory", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @Override
    public String getLastPublishStatus() throws ServerException {
        return getLastPublishStatusName();
    }

    // ====================== Other stuff ======================
    @Override
    public String generateUniqueId(String type) {
        return contentService.getTypeService().generateUniqueId(ContentType.get(type));
    }

    @Override
    public Map<String, List<ContentNodeModel>> getDomainValues(List<ContentNodeModel> domains) throws ServerException {
        try {
            return TranslatorToGwt.getDomainValues(domains, contentService, getDraftContext());
        } catch (Throwable e) {
            LOG.error("RuntimeException for getDomainValues " + domains, e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @Override
    public ProductConfigParams getProductConfigParams(String skuKey) throws ServerException {
        try {
            return TranslatorToGwt.getProductConfigParams(ContentKey.getContentKey(skuKey), contentService, getDraftContext());
        } catch (Throwable e) {
            LOG.error("RuntimeException in getProductConfigParams", e);
            throw TranslatorToGwt.wrap(e);
        }
    }

    @Override
    public NavigableRelationInfo getNavigableRelations(String contentType) {
        ContentType type = ContentType.get(contentType);

        NavigableRelationInfo nr = new NavigableRelationInfo();
        nr.setContentType(contentType);
        Set<? extends ContentTypeDefI> typeDefinitions = contentService.getTypeService().getContentTypeDefinitions();
        for (ContentTypeDefI def : typeDefinitions) {

            String attrName = getNavigableAttributeName(type, def);
            if (attrName != null) {
                nr.addNavigableAttribute(def.getType().getName(), attrName);
            }
        }
        return nr;
    }

    private String getNavigableAttributeName(ContentType type, ContentTypeDefI def) {
        Collection<RelationshipDefI> navRels = ContentTypeUtil.getNavigableRelationshipDefs(def);
        for (RelationshipDefI relDef : navRels) {
            if (relDef.getContentTypes().contains(type)) {
                if (EnumCardinality.MANY.equals(relDef.getCardinality())) {
                    return relDef.getName();
                }
            }
        }
        return null;
    }

    /**
     * This utility method collects parent keys per store IDs in a {@see Map} where keys are store IDs and values are a {@see Set} of {@see ContentKey} entries All serialized to
     * Strings
     *
     * FIXME move this method to TranslatorToGwt
     *
     * @param key
     *            Product Content Key
     * @return
     */
    private Map<String, Set<String>> getParentMapping(ContentKey key, DraftContext draftContext) {
        Map<ContentKey, Set<ContentKey>> aMap = PrimaryHomeUtil.collectParentsMap(key, contentService, draftContext);

        // serialize keys to strings
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        for (Map.Entry<ContentKey, Set<ContentKey>> e : aMap.entrySet()) {
            // serialize
            Set<String> keys = new HashSet<String>();
            for (ContentKey k : e.getValue()) {
                keys.add(k.getEncoded());
            }

            result.put(e.getKey().getEncoded(), keys);
        }

        return result;
    }
}
