package com.freshdirect.cms.ui.serviceimpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.UniqueContentKeyGeneratorService;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.properties.service.PropertyResolverService;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.editor.permission.ContentChangeSource;
import com.freshdirect.cms.ui.editor.permission.service.PermissionService;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishStatus;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublish;
import com.freshdirect.cms.ui.editor.publish.feed.converter.FeedPublishToGwtPublishDataConverter;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublish;
import com.freshdirect.cms.ui.editor.publish.feed.entity.PublishStatus;
import com.freshdirect.cms.ui.editor.publish.feed.service.FeedPublishService;
import com.freshdirect.cms.ui.editor.publish.service.StorePublishService;
import com.freshdirect.cms.ui.editor.service.ContentChangesService;
import com.freshdirect.cms.ui.editor.service.ContentLoaderService;
import com.freshdirect.cms.ui.editor.service.ContentUpdateService;
import com.freshdirect.cms.ui.editor.service.LabelProviderService;
import com.freshdirect.cms.ui.editor.service.PreviewLinkProvider;
import com.freshdirect.cms.ui.editor.service.PublishService;
import com.freshdirect.cms.ui.editor.service.SearchService;
import com.freshdirect.cms.ui.editor.service.TreeNodeService;
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
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.changeset.GwtNodeChange;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.model.publish.PublishType;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.GwtSecurityException;
import com.freshdirect.cms.ui.service.ServerException;
import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("/contentService")
public class ContentServiceImpl extends GwtServiceBase implements ContentService {

    private static final long serialVersionUID = 1893834571301941106L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);

    private ContentLoaderService contentLoaderService = EditorServiceLocator.contentLoaderService();

    private ContentUpdateService contentUpdateService = EditorServiceLocator.contentUpdateService();

    private PermissionService permissionService = EditorServiceLocator.permissionService();

    private PublishService publishService = EditorServiceLocator.publishService();

    private FeedPublishService feedPublishService = EditorServiceLocator.feedPublishService();

    private ContentChangesService contentChangesService = EditorServiceLocator.contentChangesService();

    private StorePublishService storePublishService = EditorServiceLocator.storePublishService();

    private TreeNodeService treeNodeService = EditorServiceLocator.treeNodeService();

    private SearchService searchService = EditorServiceLocator.searchService();

    private LabelProviderService labelProviderService = EditorServiceLocator.labelProviderService();

    private FeedPublishToGwtPublishDataConverter feedPublishToGwtPublishDataConverter = EditorServiceLocator.feedPublishToGwtPublishDataConverter();

    private UniqueContentKeyGeneratorService uniqueContentKeyGeneratorService = CmsServiceLocator.uniqueContentKeyGeneratorService();

    private PreviewLinkProvider previewLinkProvider = EditorServiceLocator.previewLinkProvider();

    private DraftContextHolder draftContextHolder = CmsServiceLocator.draftContextHolder();

    private PropertyResolverService propertyResolverService = EditorServiceLocator.propertyResolverService();

    @Override
    public String getGoogleMapsApiKey() {
        return propertyResolverService.getGoogleMapsApiKey();
    }

    @Override
    public String getPreviewUrl(String nodeKey, String storeId) {
        draftContextHolder.setDraftContext(getDraftContext());
        ContentKey contentKey = ContentKeyFactory.get(nodeKey);
        ContentKey storeKey = null;
        if (storeId != null) {
            storeKey = ContentKeyFactory.get(ContentType.Store, storeId);
        } else {
            storeKey = RootContentKey.STORE_FRESHDIRECT.contentKey;
        }

        return previewLinkProvider.getLink(contentKey, storeKey);
    }

    // ====================== User ======================

    @Override
    public GwtUser getUser() {
        HttpServletRequest request = getThreadLocalRequest();
        return getGwtUserFromRequest(request);
    }

    // ====================== Content tree ======================
    @Override
    public List<TreeContentNodeModel> search(String searchTerm) {
        List<TreeContentNodeModel> searchResults = searchService.search(searchTerm);
        return searchResults;
    }

    @Override
    public List<TreeContentNodeModel> getChildren(TreeContentNodeModel parentNode) throws ServerException {
        draftContextHolder.setDraftContext(getDraftContext());
        return treeNodeService.createTreeChildren(parentNode);
    }

    // ====================== Node data ======================
    @Override
    public GwtNodeData loadNodeData(String nodeKey) throws ServerException {
        draftContextHolder.setDraftContext(getDraftContext());
        if (nodeKey == null) {
            throw new ServerException("Null content key");
        }

        ContentKey key = ContentKeyFactory.get(nodeKey);
        GwtNodePermission permission = permissionService.setupClientPermissions(key, getUser());

        return contentLoaderService.getGwtNodeData(key, permission);
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
        return contentLoaderService.getPrototypeGwtNodeData(ContentKeyFactory.get(type, id), GwtNodePermission.FULL_PERMISSION);
    }

    // ====================== Save ======================
    @Override
    public GwtSaveResponse save(Collection<GwtContentNode> nodes) throws ServerException {
        final GwtUser author = getUser();
        draftContextHolder.setDraftContext(getDraftContext());
        if (permissionService.isNodeModificationEnabled()) {

            String authorNote = MessageFormat.format("Updated content by {0} on draft {1}", author.getName(), author.getDraftName());

            return contentUpdateService.updateContent(author, nodes, authorNote, ContentChangeSource.NODE_EDITOR);

        } else {
            throw new ServerException("Can't save nodes as a publish is in progress");
        }

    }

    // ====================== Changesets & Publish ======================

    @Override
    public ChangeSetQueryResponse getChangeSets(ChangeSetQuery query) throws ServerException {
        ChangeSetQueryResponse response = null;

        if (query.isChangeSetQuery()) {
            ContentKey key;
            try {
                key = ContentKeyFactory.get(query.getContentKey());
            } catch (IllegalArgumentException ex) {
                key = null;
            }
            List<GwtChangeSet> changeHistory = contentChangesService.getChangeSets(key, query.getUser(), query.getStartDate(), query.getEndDate());
            response = contentChangesService.createResponse(changeHistory, query, null, 0);
            response.setLabel("Query result : " + response.getChangeCount() + " changes in " + response.getChangeSetCount() + " changesets.");

        } else if (query.getPublishId() != null) {
            LOGGER.info("Query: " + query);
            response = contentChangesService.queryChanges(query);
            response.setLabel("Changes in publish #" + query.getPublishId());

        } else if (query.getContentKey() != null) {
            ContentKey key = ContentKeyFactory.get(query.getContentKey());
            List<GwtChangeSet> changeHistory = contentChangesService.getChangeHistory(key);
            response = contentChangesService.createResponse(changeHistory, query, null, 0);
            String label = labelProviderService.labelOfContentKey(key);
            response.setLabel("History of " + label);

        }

        return response;
    }

    @Override
    public String startPublish(String comment) throws ServerException {
        GwtUser author = getUser();
        if (!author.isHasAccessToPublishTab()) {
            throw new GwtSecurityException("User " + author.getName() + " is not allowed to publish!");
        }

        // check whether publish is already running
        StorePublish publish = storePublishService.getLastStorePublish();
        if (publish != null && StorePublishStatus.PROGRESS == publish.getStatus()) {
            LOGGER.info("Publish in progress ...: " + publish.getId());
            return publish.getId().toString();
        }

        LOGGER.info("start publish called by " + author.getName() + " : '" + comment + "'");

        // kick off new publish
        try {
            publish = storePublishService.startPublish(author, comment);
        } catch (Exception exc) {
            LOGGER.error("Publish failed!", exc);

            throw new ServerException(exc);
        }

        return publish.getId().toString();
    }

    @Override
    public String startPublishX(String comment) throws ServerException {
        GwtUser author = getUser();
        if (!author.isHasAccessToFeedPublishTab()) {
            throw new GwtSecurityException("User " + author.getName() + " is not allowed to do feed publish!");
        }

        return startFeedPublish(author, comment);
    }

    @Override
    public GwtPublishData getPublishData(ChangeSetQuery query) throws ServerException {
        try {
            ChangeSetQueryResponse r = getChangeSets(query);
            ArrayList<GwtChangeSet> changes = new ArrayList<GwtChangeSet>(r.getChanges());

            Long pId = contentChangesService.parsePublishId(query);
            Optional<StorePublish> possibleData = storePublishService.findPublish(pId);

            GwtPublishData publishData = null;
            if (possibleData.isPresent()) {
                publishData = storePublishService.toPublishData(possibleData.get(), true);
            } else {
                publishData = new GwtPublishData();
            }

            publishData.setId(query.getPublishId());

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

            Collections.sort(publishData.getContributors());
            Collections.sort(publishData.getTypes());
            Collections.sort(publishData.getMessages());

            return publishData;
        } catch (Exception exc) {
            LOGGER.error("Failed to get store data for " + query.getPublishId(), exc);

            throw new ServerException("Failed to get store data for " + query.getPublishId(), exc);
        }
    }

    @Override
    public GwtPublishData getPublishDataX(ChangeSetQuery query) throws ServerException {

        try {
            ChangeSetQueryResponse r = getChangeSets(query);
            ArrayList<GwtChangeSet> changes = new ArrayList<GwtChangeSet>(r.getChanges());

            Optional<FeedPublish> possibleData = feedPublishService.findFeedPublish(query.getPublishId());

            GwtPublishData publishData = null;
            if (possibleData.isPresent()) {
                publishData = feedPublishToGwtPublishDataConverter.convert(possibleData.get(), true);
            } else {
                publishData = new GwtPublishData();
            }

            publishData.setId(query.getPublishId());

            int changeCount = 0;
            for (GwtChangeSet change : changes) {

                for (GwtNodeChange nodechange : change.getNodeChanges()) {
                    if (feedPublishService.isFeedRelatedChange(nodechange.getType())) {
                        int length = nodechange.length();
                        publishData.addContributor(change.getUserId(), length);
                        publishData.addType(nodechange.getType(), length);
                        changeCount += length;
                    }
                }
            }

            publishData.setChangeCount(changeCount);

            Collections.sort(publishData.getContributors());
            Collections.sort(publishData.getTypes());
            return publishData;
        } catch (Exception exc) {
            LOGGER.error("Failed to get store data for " + query.getPublishId(), exc);

            throw new ServerException("Failed to get store data for " + query.getPublishId(), exc);
        }
    }

    @Override
    public List<GwtPublishData> getPublishHistory(PagingLoadConfig config) throws ServerException {
        List<StorePublish> fullHistory = storePublishService.publishHistory();

        List<GwtPublishData> history = new ArrayList<GwtPublishData>();

        for (StorePublish publish : fullHistory) {
            history.add(storePublishService.toPublishData(publish, false));
        }

        int start = config.getOffset();
        int limit = fullHistory.size();
        if (config.getLimit() > 0) {
            limit = Math.min(start + config.getLimit(), limit);
        }
        return new ArrayList<GwtPublishData>(history.subList(start, limit));

    }

    @Override
    public List<GwtPublishData> getPublishHistoryByType(PagingLoadConfig config, String type) throws ServerException {
        PublishType publishType = PublishType.STORE_PUBLISH;
        if (type.equalsIgnoreCase("FEED")) {
            publishType = PublishType.PUBLISH_X;
            return publishService.getPublishHistory(publishType);
        }

        return getPublishHistory(config);
    }

    @Override
    public String getLastPublishStatus() throws ServerException {
        return storePublishService.getLastStorePublishStatus();
    }

    // ====================== Other stuff ======================
    @Override
    public String generateUniqueId(String type) {
        ContentType contentType = ContentType.valueOf(type);

        Optional<String> generatedId = uniqueContentKeyGeneratorService.generateContentId(contentType);

        return generatedId.orNull();
    }

    @Override
    public Map<String, List<ContentNodeModel>> getDomainValues(List<ContentNodeModel> domains) throws ServerException {
        // NOTE: Variation Matrix is no longer populated
        return Collections.<String, List<ContentNodeModel>>emptyMap();
    }

    @Override
    public ProductConfigParams getProductConfigParams(String skuKey) throws ServerException {
        final ContentKey contentKey = ContentKeyFactory.get(skuKey);
        draftContextHolder.setDraftContext(getDraftContext());
        return contentLoaderService.getProductConfigParams(contentKey);
    }

    @Override
    public NavigableRelationInfo getNavigableRelations(String contentType) {
        // May throw IllegalArgumentException if type not found
        ContentType selectedType = ContentType.valueOf(contentType);

        NavigableRelationInfo info = new NavigableRelationInfo();
        info.setContentType(contentType);

        Map<ContentType, List<Relationship>> navigableRelationships = CmsServiceLocator.contentTypeInfoService().selectAllNavigableRelationships();

        for (ContentType type : ContentType.values()) {
            final List<Relationship> navigableRelationshipsOfType = navigableRelationships.get(type);
            if (navigableRelationshipsOfType == null || navigableRelationshipsOfType.isEmpty()) {
                continue;
            }

            Relationship foundRelationship = findRelationshipWithType(selectedType, navigableRelationshipsOfType);
            if (foundRelationship != null) {
                info.addNavigableAttribute(type.name(), foundRelationship.getName());
            }
        }

        return info;
    }

    private Relationship findRelationshipWithType(ContentType selectedType, Collection<Relationship> relationships) {
        for (Relationship navigableRelationship : relationships) {
            for (ContentType destinationType : navigableRelationship.getDestinationTypes()) {
                if (selectedType == destinationType) {
                    return navigableRelationship;
                }
            }
        }

        return null;
    }

    private synchronized String startFeedPublish(GwtUser author, String comment) throws ServerException {
        // check whether publish is already running
        FeedPublish publish = null;

        Optional<FeedPublish> optionalPublish = feedPublishService.findFeedPublish("latest");
        publish = optionalPublish.orNull();

        if (publish != null && PublishStatus.PROGRESS == publish.getStatus()) {
            LOGGER.info("Feed Publish in progress ...: " + publish.getPublishId());
            return publish.getPublishId();
        }

        LOGGER.info("feed publish kicked off by " + author.getName() + " : '" + comment + "'");

        return publishService.startPublish(PublishType.PUBLISH_X, comment, author.getName());
    }



}
