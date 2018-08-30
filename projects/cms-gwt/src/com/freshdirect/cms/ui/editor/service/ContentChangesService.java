package com.freshdirect.cms.ui.editor.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.freshdirect.cms.changecontrol.entity.ContentChangeDetailEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.changecontrol.service.ContentChangeControlService;
import com.freshdirect.cms.core.converter.ScalarValueConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.ui.editor.domain.ContentAttributeKey;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublish;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.feed.converter.FeedPublishMessageToGwtPublishMessageConverter;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublish;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessage;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessageLevel;
import com.freshdirect.cms.ui.editor.publish.feed.service.FeedPublishService;
import com.freshdirect.cms.ui.editor.publish.service.StorePublishService;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQueryResponse;
import com.freshdirect.cms.ui.model.changeset.Comparators;
import com.freshdirect.cms.ui.model.changeset.GwtChangeDetail;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.changeset.GwtNodeChange;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

@Service
public class ContentChangesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentChangesService.class);

    private static final String DRAFT_NULL_VALUE_DESCRIPTION = "(removed)";

    public static final ChangeSetQuery EMPTY_CHANGESET_QUERY = new ChangeSetQuery();

    @Autowired
    private ContentChangeControlService contentChangeControlService;

    @Autowired
    private LabelProviderService labelProviderService;

    @Autowired
    private PreviewLinkProvider previewLinkService;

    @Autowired
    private StorePublishService storePublishService;

    @Autowired
    private FeedPublishService feedPublishService;

    @Autowired
    private FeedPublishMessageToGwtPublishMessageConverter feedPublishMessageToGwtConverter;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    /**
     * Record a {@link GwtChangeSet}.
     *
     * @param changeSet
     *            change set to store (never null)
     *
     * @return primary key of stored change set (never null)
     */
    public String storeChangeSet(GwtChangeSet changeSet) {
        throw new UnsupportedOperationException("Feature not implemented yet");
    }

    /**
     * Get change sets affecting a given content object. Note, that the retrieved change sets will only have the {@link ContentNodeChange} records affecting the given content node.
     *
     * @param key
     *            content key (never null)
     *
     * @return List of {@link GwtChangeSet} (never null)
     */
    public List<GwtChangeSet> getChangeHistory(ContentKey key) {
        return toGwtChangeSetList(contentChangeControlService.getHistory(key), EMPTY_CHANGESET_QUERY);
    }

    /**
     * Get a change set by primary key.
     *
     * @param pk
     *            primary key of the change set
     *
     * @return the {@link GwtChangeSet}, or null if not found
     */
    public Optional<GwtChangeSet> getChangeSet(String pk) {
        Assert.notNull(pk, "ChangeSet ID is mandatory");

        final Optional<ContentChangeSetEntity> changeSet = contentChangeControlService.fetchChangeSet(Integer.parseInt(pk, 10));

        GwtChangeSet result = changeSet.isPresent() ? toGwtChangeSet(changeSet.get(), EMPTY_CHANGESET_QUERY) : null;

        return Optional.fromNullable(result);
    }

    /**
     * Get changes by date range.
     *
     * @param startDate
     *            start date (never null)
     * @param endDate
     *            end date (never null)
     *
     * @return List of {@link GwtChangeSet} (never null)
     */
    public List<GwtChangeSet> getChangesBetween(Date startDate, Date endDate) {
        return toGwtChangeSetList(contentChangeControlService.queryChangeSetEntities(null, null, startDate, endDate), EMPTY_CHANGESET_QUERY);
    }

    /**
     * Get changes by a specific user.
     *
     * @param userId
     *            user ID (never null)
     *
     * @return List of {@link GwtChangeSet} (never null)
     */
    public List<GwtChangeSet> getChangesByUser(String userId) {
        return toGwtChangeSetList(contentChangeControlService.queryChangeSetEntities(null, userId, null, null), EMPTY_CHANGESET_QUERY);
    }

    /**
     * Get change sets by combined query parameters. All of the above queries combined. Any parameter can be null.
     *
     * @param key
     *            content key
     * @param startDate
     *            start date
     * @param endDate
     *            end date
     * @param userId
     *            user ID
     *
     * @return List of {@link GwtChangeSet} (never null)
     */
    public List<GwtChangeSet> getChangeSets(ContentKey key, String userId, Date startDate, Date endDate) {
        return toGwtChangeSetList(contentChangeControlService.queryChangeSetEntities(key, userId, startDate, endDate), EMPTY_CHANGESET_QUERY);
    }

    public ChangeSetQueryResponse getChangeSetsByPublishId(ChangeSetQuery query) {
        Assert.notNull(query.getPublishId(), "Publish ID must not be null");
        Assert.isNull(query.getPublishType(), "Feed Publish is not yet supported!");

        ChangeSetQueryResponse response = null;

        final String queryPublishId = query.getPublishId();
        Long publishId = parsePublishId(query);

        Optional<Date[]> optionalTimeFrame = storePublishService.timeInfo(publishId);
        Optional<StorePublish> optionalPublish = storePublishService.findPublish(publishId);

        StorePublish publish = optionalPublish.orNull();

        if (!optionalTimeFrame.isPresent()) {
            // CRASH
            LOGGER.error("Failed to determine time frame for collecting change sets");
            return null;
        } else if (publish == null) {
            LOGGER.error("Failed to find publish with ID " + queryPublishId);
            return null;

        }

        Date timeFrame[] = optionalTimeFrame.get();

        if (query.isPublishInfoQuery()) {
            if (optionalPublish.isPresent()) {
                List<GwtPublishMessage> filteredMessages = toGwtPublishMessages(publish, query);
                response = new ChangeSetQueryResponse(publish.getStatus().name(), publish.getTimestamp(), System.currentTimeMillis() - publish.getTimestamp().getTime(),
                        filteredMessages, publish.lastInfoMessage());

            }
        } else {
            Date timestamp = timeFrame[0];
            Date prevTimestamp = timeFrame[1];

            LOGGER.info("collecting changes from " + prevTimestamp + " to " + timestamp + ", query:" + query);

            Set<ContentChangeSetEntity> changeSetEntities = prevTimestamp != null ? contentChangeControlService.queryChangeSetEntities(null, null, prevTimestamp, timestamp)
                    : Collections.<ContentChangeSetEntity> emptySet();
            List<GwtChangeSet> result = toGwtChangeSetList(changeSetEntities, query);

            LOGGER.info("returning " + result.size() + " changeset" + ", query:" + query);

            // hack, to not fail with thousands of changesets ...
            List<GwtPublishMessage> publishMessages = toGwtPublishMessages(publish, query);
            response = createResponse(result, query, publishMessages, sizeOfFileredMessages(publish, query));
        }

        return response;
    }

    public ChangeSetQueryResponse getChangeSetsByFeedPublishId(ChangeSetQuery query) {
        Assert.notNull(query.getPublishId(), "Publish ID must not be null");
        LOGGER.info("Loading changeSets for feedPublish id: " + query.getPublishId());
        ChangeSetQueryResponse response = null;

        final String publishId = query.getPublishId().equals("latest") ? null : query.getPublishId();

        Optional<Date[]> optionalTimeFrame = feedPublishService.timeInfo(publishId);
        Optional<FeedPublish> optionalPublish = feedPublishService.findFeedPublish(query.getPublishId());

        FeedPublish publish = optionalPublish.orNull();

        if (!optionalTimeFrame.isPresent()) {
            // CRASH
            LOGGER.error("Failed to determine time frame for collecting change sets");
            return null;
        } else if (publish == null) {
            LOGGER.error("Failed to find publish with ID " + publishId);
            return null;

        }

        Date timeFrame[] = optionalTimeFrame.get();

        if (query.isPublishInfoQuery()) {
            if (optionalPublish.isPresent()) {
                List<GwtPublishMessage> filteredMessages = toGwtPublishMessages(publish, query);
                response = new ChangeSetQueryResponse(publish.getStatus().name(), publish.getCreationTimestamp(),
                        System.currentTimeMillis() - publish.getCreationTimestamp().getTime(), filteredMessages, publish.lastInfoMessage());

            }
        } else {
            Date timestamp = timeFrame[0];
            Date prevTimestamp = timeFrame[1];

            LOGGER.info("collecting changes from " + prevTimestamp + " to " + timestamp + ", query:" + query);

            Set<ContentChangeSetEntity> changeSetEntities = prevTimestamp != null ? contentChangeControlService.queryChangeSetEntities(null, null, prevTimestamp, timestamp)
                    : Collections.<ContentChangeSetEntity> emptySet();

            filterForFeedContentChanges(changeSetEntities);

            List<GwtChangeSet> result = toGwtChangeSetList(changeSetEntities, query);

            LOGGER.info("returning " + result.size() + " changeset" + ", query:" + query);

            // hack, to not fail with thousands of changesets ...
            List<GwtPublishMessage> publishMessages = toGwtPublishMessages(publish, query);
            response = createResponse(result, query, publishMessages, sizeOfFileredMessages(publish, query));
        }

        return response;
    }

    public ChangeSetQueryResponse queryChanges(ChangeSetQuery query) {
        LOGGER.info("ChangeSets by query: " + query);
        ChangeSetQueryResponse response = null;

        if (query.getPublishId() != null) {
            if (query.getPublishType() != null) {
                response = getChangeSetsByFeedPublishId(query);
            } else {
                response = getChangeSetsByPublishId(query);
            }
        } else {
            ContentKey queriedContentKey = null;

            if (Strings.isNullOrEmpty(query.getContentKey())) {
                try {
                    queriedContentKey = ContentKeyFactory.get(query.getContentKey());
                } catch (Exception exc) {
                    LOGGER.error("Failed to parse content key " + query.getContentKey());
                }
            }

            Set<ContentChangeSetEntity> result = contentChangeControlService.queryChangeSetEntities(queriedContentKey, query.getUser(), query.getStartDate(), query.getEndDate());

            List<GwtChangeSet> gwtChangeSets = new ArrayList<GwtChangeSet>();

            int changeCount = 0;
            for (ContentChangeSetEntity changeSetEntity : result) {
                GwtChangeSet gwtChangeSet = toGwtChangeSet(changeSetEntity, query);

                gwtChangeSets.add(gwtChangeSet);

                changeCount += gwtChangeSet.length();
            }

            response = new ChangeSetQueryResponse(gwtChangeSets, gwtChangeSets.size(), changeCount, query);

            response.setLabel(MessageFormat.format("Query result: {0} changes in {1} changesets.", changeCount, gwtChangeSets.size()));
        }
        return response;
    }

    public List<GwtChangeSet> toGwtChangeSetList(Collection<ContentChangeSetEntity> entityList, ChangeSetQuery query) {
        Assert.notNull(entityList);

        List<GwtChangeSet> result = new ArrayList<GwtChangeSet>(entityList.size());

        for (ContentChangeSetEntity changeSetEntity : entityList) {

            /*
             * Contributor filter
             */
            if (query.getContributor() != null && !changeSetEntity.getUserId().equals(query.getContributor())) {
                continue;
            }

            result.add(toGwtChangeSet(changeSetEntity, query));
        }

        return result;
    }

    public GwtChangeSet toGwtChangeSet(ContentChangeSetEntity changeSetEntity, ChangeSetQuery query) {
        final GwtChangeSet gwtChangeSet = new GwtChangeSet(String.valueOf(changeSetEntity.getId()), changeSetEntity.getUserId(), changeSetEntity.getTimestamp(),
                changeSetEntity.getNote());
        for (ContentChangeEntity changeEntity : changeSetEntity.getChanges()) {

            /*
             * Content type filter
             */
            if (query.getContentType() != null && !changeEntity.getContentType().name().equals(query.getContentType())) {
                continue;
            }

            Optional<GwtNodeChange> change = toGwtNodeChange(changeEntity);
            if (change.isPresent()) {
                gwtChangeSet.addChange(change.get());
            }
        }
        return gwtChangeSet;
    }

    public GwtChangeSet toGwtChangeSet(String id, Collection<DraftChange> draftChanges, Map<ContentAttributeKey, Object> shadowedFields, Map<ContentKey, Map<Attribute, Object>> payloadBeforeUpdate, DraftContext draftContext) {
        String username = null;
        long modifiedDate = 0l;

        List<GwtNodeChange> nodeChanges = new ArrayList<GwtNodeChange>();
        for (final DraftChange draftChange : draftChanges) {
            username = draftChange.getUserName();
            modifiedDate = draftChange.getCreatedAt();

            final ContentKey key = ContentKeyFactory.get(draftChange.getContentKey());
            final String attributeName = draftChange.getAttributeName();
            final ContentAttributeKey attributeKey = new ContentAttributeKey(key, attributeName);
            final Attribute attributeDef = contentTypeInfoService.findAttributeByName(key.type, attributeName).get();

            final boolean isFieldShadowed = shadowedFields.keySet().contains(attributeKey);
            final Object oldValue = payloadBeforeUpdate.get(key).get(attributeDef);
            final String draftValue = draftChange.getValue();

            nodeChanges.add(toGwtNodeChange(key, attributeDef, isFieldShadowed, oldValue, draftValue));
        }

        GwtChangeSet gwtChangeSet = new GwtChangeSet(id, username, new Date(modifiedDate), null);
        gwtChangeSet.addChanges(nodeChanges);
        return gwtChangeSet;
    }



    private Optional<GwtNodeChange> toGwtNodeChange(ContentChangeEntity changeEntity) {
        GwtNodeChange gwtNodeChange = null;

        Optional<ContentKey> optionalKey = changeEntity.getContentKey();
        if (optionalKey.isPresent()) {
            ContentKey contentKey = optionalKey.get();

            String label = labelProviderService.labelOfContentKey(contentKey);
            String previewLink = previewLinkService.getLink(contentKey);

            final String changeType = changeEntity.getChangeType().description;
            gwtNodeChange = new GwtNodeChange(contentKey.type.name(), label, contentKey.toString(), changeType, previewLink);

            for (ContentChangeDetailEntity detail : changeEntity.getDetails()) {
                GwtChangeDetail gwtChangeDetail = toGwtChangeDetail(changeType, contentKey.getType(), detail);
                gwtNodeChange.addDetail(gwtChangeDetail);
            }
        }

        return Optional.fromNullable(gwtNodeChange);
    }

    private GwtNodeChange toGwtNodeChange(ContentKey key, Attribute attributeDef, boolean isFieldShadowed, Object valueBefore, String valueOnDraft) {
        final String[] describedValues = describeValueChange(key, attributeDef, valueBefore, valueOnDraft);

        GwtNodeChange gwtNodeChange = createGwtNodeChangePrototype(key, isFieldShadowed);
        gwtNodeChange.addDetail(new GwtChangeDetail(gwtNodeChange.getChangeType(), attributeDef.getName(), describedValues[0], describedValues[1], valueOnDraft));

        return gwtNodeChange;
    }

    private GwtNodeChange createGwtNodeChangePrototype(ContentKey key, boolean isFieldShadowed) {
        String label = labelProviderService.labelOfContentKey(key);
        final String draftChangeKind = isFieldShadowed ? "Override" : "Create";
        return new GwtNodeChange(key.type.name(), label, key.getEncoded(), draftChangeKind, null);
    }

    private String[] describeValueChange(ContentKey key, Attribute attributeDef, Object valueBefore, String valueOnDraft) {
        String[] describedValues = null;

        if (valueBefore != null) {

            if (attributeDef instanceof Scalar) {
                describedValues = new String[] { ScalarValueConverter.serializeToString((Scalar) attributeDef, valueBefore),
                        valueOnDraft != null ? valueOnDraft : DRAFT_NULL_VALUE_DESCRIPTION };
            } else if (attributeDef instanceof Relationship) {
                Relationship relationshipDef = (Relationship) attributeDef;

                if (relationshipDef.getCardinality() == RelationshipCardinality.ONE) {
                    describedValues = new String[] { ((ContentKey) valueBefore).getEncoded(), valueOnDraft != null ? valueOnDraft : DRAFT_NULL_VALUE_DESCRIPTION };
                } else {
                    if (valueOnDraft != null) {
                        describedValues = describeContentKeyListChange(valueBefore, valueOnDraft);
                    } else {
                        List<ContentKey> oldKeys = (List<ContentKey>) valueBefore;

                        StringBuilder oldValueBuilder = new StringBuilder();
                        oldValueBuilder
                            .append("Deleted: ")
                            .append(StringUtils.join(", ", oldKeys));

                        describedValues = new String[] { oldValueBuilder.toString(), DRAFT_NULL_VALUE_DESCRIPTION };
                    }
                }
            }
        } else {
            if (attributeDef instanceof Relationship && RelationshipCardinality.MANY == ((Relationship) attributeDef).getCardinality()) {
                List<String> newKeys = decodedListOfContentKeysFromDraftValue(valueOnDraft);

                StringBuilder newValueBuilder = new StringBuilder();
                newValueBuilder
                    .append("Added: ")
                    .append(StringUtils.join(", ", newKeys));

                return new String[] { null, newValueBuilder.toString() };

            } else {
                describedValues = new String[] {null, valueOnDraft};
            }
        }

        return describedValues;
    }

    private String[] describeContentKeyListChange(Object valueBefore, String draftValue) {
        String[] describedValues = null;

        // do the magic !
        List<String> oldKeys = new ArrayList<String>();
        for (ContentKey contentKey : (List<ContentKey>) valueBefore) {
            oldKeys.add(contentKey.getEncoded());
        }
        List<String> newKeys = decodedListOfContentKeysFromDraftValue(draftValue);

        Map<String, int[]> changeVector = computeItemPositions(oldKeys, newKeys);
        if (changeVector != null && !changeVector.isEmpty()) {

            Set<String> keysAdded = new HashSet<String>();
            Set<String> keysRemoved = new HashSet<String>();
            Map<String, String> keysMoved = new HashMap<String, String>();
            Map<String, String> keysPermuted = new HashMap<String, String>();

            for (Map.Entry<String, int[]> entry : changeVector.entrySet()) {
                final String aKey = entry.getKey();
                int[] positions = entry.getValue();
                if (positions[1] == -1) {
                    keysRemoved.add(aKey);
                } else if (positions[0] == -1) {
                    keysAdded.add(aKey);
                }
            }

            // check for permutations
            if (keysRemoved.size() < oldKeys.size() || keysAdded.size() < newKeys.size()) {
                List<String> filteredOldKeys = new ArrayList<String>();
                List<String> filteredNewKeys = new ArrayList<String>();

                for (String aKey : oldKeys) {
                    if (!keysRemoved.contains(aKey)) {
                        filteredOldKeys.add(aKey);
                    }
                }
                for (String aKey : newKeys) {
                    if (!keysAdded.contains(aKey)) {
                        filteredNewKeys.add(aKey);
                    }
                }

                // At this point we have two equally sized lists having the same elements
                // element order may or may not be the same
                Map<String, int[]> changeVector2 = computeItemPositions(filteredOldKeys, filteredNewKeys);
                if (changeVector2 != null) {
                    for (Map.Entry<String, int[]> entry : changeVector2.entrySet()) {
                        String movedKey = entry.getKey();
                        int[] positions = entry.getValue();
                        if (positions[0] != positions[1]) {
                            // detect two items are permuted
                            if (filteredNewKeys.get(positions[0]).equals(filteredOldKeys.get(positions[1]))) {
                                String thisKey = movedKey;
                                String otherKey = filteredNewKeys.get(positions[0]);

                                // don't put {A,B} pair if {B,A} is already in
                                if (!keysPermuted.containsKey(otherKey)) {
                                    keysPermuted.put(thisKey, otherKey);
                                }
                            } else {
                                keysMoved.put(movedKey, String.format("%d => %d", oldKeys.indexOf(movedKey), newKeys.indexOf(movedKey)));
                            }
                        }
                    }
                }
            }

            describedValues = describeKeyChanges(keysAdded, keysRemoved, keysPermuted, keysMoved);
        } else {
            describedValues = new String[] {"#error", null};
        }

        return describedValues;
    }

    private List<String> decodedListOfContentKeysFromDraftValue(String draftValue) {
        if ("[]".equals(draftValue)) {
            return Collections.emptyList();
        }
        return Arrays.asList(draftValue.split("\\|"));
    }

    private Map<String, int[]> computeItemPositions(List<String> left, List<String> right) {
        Set<String> keys = new HashSet<String>();
        if (left != null) {
            keys.addAll(left);
        }
        if (right != null) {
            keys.addAll(right);
        }

        Map<String, int[]> changeVector = new HashMap<String, int[]>(keys.size());
        for (String key: keys) {
            int[] positions = new int[2];
            positions[0] = left != null ? left.indexOf(key) : -1;
            positions[1] = right != null ? right.indexOf(key) : -1;

            changeVector.put(key, positions);
        }

        return changeVector;
    }

    private String[] describeKeyChanges(Collection<String> keysAdded, Collection<String> keysRemoved, Map<String, String> keysPermuted, Map<String, String> keysMoved) {
        StringBuilder oldValueBuilder = new StringBuilder();
        StringBuilder newValueBuilder = new StringBuilder();

        if (!keysRemoved.isEmpty()) {
            oldValueBuilder
                .append("Deleted: ")
                .append(StringUtils.join(keysRemoved, ", "));
        }
        if (!keysAdded.isEmpty()) {
            newValueBuilder
                .append("Added: ")
                .append(StringUtils.join(keysAdded, ", "));
        }
        if (!keysPermuted.isEmpty()) {
            if (newValueBuilder.length() > 0) {
                newValueBuilder.append("\n");
            }

            newValueBuilder.append("Exchanged: ");
            for (Map.Entry<String, String> entry : keysPermuted.entrySet()) {
                newValueBuilder
                    .append(entry.getKey()).append("<->")
                    .append(entry.getValue()).append("; ");
            }
        }
        if (!keysMoved.isEmpty()) {
            if (newValueBuilder.length() > 0) {
                newValueBuilder.append("\n");
            }

            newValueBuilder.append("Moved: ");
            for (Map.Entry<String, String> entry : keysMoved.entrySet()) {
                newValueBuilder
                    .append(entry.getKey()).append(" ")
                    .append(entry.getValue()).append("; ");
            }
        }

        String[] describedValues = new String[2];

        if (oldValueBuilder.length() > 0) {
            describedValues[0] = oldValueBuilder.toString();
        }
        if (newValueBuilder.length() > 0) {
            describedValues[1] = newValueBuilder.toString();
        }

        return describedValues;
    }

    private GwtChangeDetail toGwtChangeDetail(String nodeChangeType, ContentType type, ContentChangeDetailEntity detail) {
        String changeType = null;

        // if the whole node was added, attributes also must
        if ("ADD".equalsIgnoreCase(nodeChangeType)) {
            changeType = "ADD";
        } else {
            changeType = detail.getChangeType() != null ? detail.getChangeType().name() : nodeChangeType;
        }

        String oldValue = detail.getOldValue();
        String newValue = detail.getNewValue();
        Attribute attr = contentTypeInfoService.findAttributeByName(type, detail.getAttributeName()).orNull();
        if (attr != null && attr instanceof Scalar && ((Scalar) attr).isEnumerated()) {
            Map<String, String> labels = labelProviderService.getEnumLabels(type, attr);
            if (labels != null) {
                oldValue = enumToString(oldValue, labels);
                newValue = enumToString(newValue, labels);
            }
        }
        return new GwtChangeDetail(changeType, detail.getAttributeName(), oldValue, newValue);
    }

    private static final String enumToString(String enumValue, Map<String, String> enumLabels) {
        String label = enumLabels.get(enumValue);
        return label != null ? label + " [" + enumValue + "]" : enumValue;
    }

    public ChangeSetQueryResponse createResponse(List<GwtChangeSet> changeHistory, ChangeSetQuery query, List<GwtPublishMessage> publishMessages, int allPublishMessageCount) {
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

        return new ChangeSetQueryResponse(clientChanges, changeHistory.size(), changeCount, query, publishMessages, allPublishMessageCount);
    }

    private List<GwtPublishMessage> toGwtPublishMessages(StorePublish publish, ChangeSetQuery query) {
        Assert.notNull(publish);
        Assert.notNull(query);

        StorePublishMessageSeverity severity = StorePublishMessageSeverity.valueOf(query.getMessageSeverity());
        List<StorePublishMessage> filteredMessages = storePublishService.filterMessagesBySeverity(publish.getMessages(), severity);

        List<GwtPublishMessage> publishMessages = storePublishService.toPublishMessages(filteredMessages, query.getPublishMessageStart(), query.getPublishMessageEnd());
        return publishMessages;
    }

    private int sizeOfFileredMessages(StorePublish publish, ChangeSetQuery query) {
        Assert.notNull(publish);
        Assert.notNull(query);

        StorePublishMessageSeverity severity = StorePublishMessageSeverity.valueOf(query.getMessageSeverity());
        List<StorePublishMessage> filteredMessages = storePublishService.filterMessagesBySeverity(publish.getMessages(), severity);

        return filteredMessages.size();
    }

    private List<GwtPublishMessage> toGwtPublishMessages(FeedPublish publish, ChangeSetQuery query) {
        Assert.notNull(publish);
        Assert.notNull(query);

        FeedPublishMessageLevel severity = null;
        if (query.getMessageSeverity() != -1) {
            severity = FeedPublishMessageLevel.valueOf(query.getMessageSeverity());
        }
        List<FeedPublishMessage> filteredMessages = feedPublishService.filterMessagesByLevel(publish.getMessages(), severity);

        List<GwtPublishMessage> publishMessages = feedPublishMessageToGwtConverter.toPublishMessages(filteredMessages, query.getPublishMessageStart(),
                query.getPublishMessageEnd());
        return publishMessages;
    }

    private int sizeOfFileredMessages(FeedPublish publish, ChangeSetQuery query) {
        Assert.notNull(publish);
        Assert.notNull(query);

        FeedPublishMessageLevel severity = null;
        if (query.getMessageSeverity() != -1) {
            severity = FeedPublishMessageLevel.valueOf(query.getMessageSeverity());
        }
        List<FeedPublishMessage> filteredMessages = feedPublishService.filterMessagesByLevel(publish.getMessages(), severity);

        return filteredMessages.size();
    }

    private void filterForFeedContentChanges(Set<ContentChangeSetEntity> changeSetEntities) {
        Iterator<ContentChangeSetEntity> contentChangeSetIter = changeSetEntities.iterator();
        while (contentChangeSetIter.hasNext()) {

            ContentChangeSetEntity changeSetEntity = contentChangeSetIter.next();
            Iterator<ContentChangeEntity> changeSetIter = changeSetEntity.getChanges().iterator();

            while (changeSetIter.hasNext()) {
                ContentChangeEntity contentChangeEntity = changeSetIter.next();
                if (!feedPublishService.isFeedRelatedChange(contentChangeEntity.getContentType().name())) {
                    changeSetIter.remove();
                }
            }

            if (changeSetEntity.getChanges().isEmpty()) {
                contentChangeSetIter.remove();
            }

        }
    }

    /**
     * @param query
     * @return publish Id or null meaning latest publish
     */
    public Long parsePublishId(ChangeSetQuery query) {
        Long parshedPublishId = "latest".equals(query.getPublishId()) ? null : Long.parseLong(query.getPublishId(), 10);
        return parshedPublishId;
    }
}
