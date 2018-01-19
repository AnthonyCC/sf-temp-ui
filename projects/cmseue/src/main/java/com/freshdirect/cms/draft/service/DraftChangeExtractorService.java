package com.freshdirect.cms.draft.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentNodeComparatorUtil;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.draft.converter.AttributeValueToStringConverter;
import com.freshdirect.cms.draft.domain.Draft;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.google.common.base.Joiner;

@Service
public class DraftChangeExtractorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DraftChangeExtractorService.class);

    private static final Joiner JOINER = Joiner.on(DraftChangeToContentNodeApplicator.SEPARATOR).skipNulls();
    
    @Autowired
    private AttributeValueToStringConverter attributeValueToStringConverter;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    /**
     * Find changed nodes in the input collection and extract values of changed attributes into a set of draft change objects
     *
     * @param nodes
     * @return
     */
    public List<DraftChange> extractChanges(Map<ContentKey, Map<Attribute, Object>> nodes, Map<ContentKey, Map<Attribute, Object>> originalNodes,
            final String userName, final Draft draft) {
        if (nodes == null || nodes.isEmpty()) {
            return Collections.emptyList();
        }

        List<DraftChange> changes = new ArrayList<DraftChange>();
        for (ContentKey nodeKey : nodes.keySet()) {
            if (ContentNodeComparatorUtil.isNodeChanged(nodes.get(nodeKey), originalNodes.get(nodeKey))) {
                List<DraftChange> list = processNodeChanges(nodeKey, nodes.get(nodeKey), originalNodes.get(nodeKey), userName, draft);
                if (list != null && !list.isEmpty()) {
                    changes.addAll(list);
                }
            }
        }
        return changes;
    }

    /**
     * This method picks changed nodes from a CMS change request and transforms them to draft change objects
     *
     * @param changedNodes
     * @return list of {@link DraftChange} items
     */
    public List<DraftChange> extractChangesFromRequest(Map<ContentKey, Map<Attribute, Object>> changedNodes, Map<ContentKey, Map<Attribute, Object>> originalNodes,
            DraftContext draftContext, String userName) {
        final Draft draft = convertDraftContextToDraft(draftContext);
        return extractChanges(changedNodes, originalNodes, userName, draft);
    }

    /**
     * Transform DraftContext into Draft
     *
     * @param draftContext
     *            Draft Context instance
     * @return converted Draft
     */
    private Draft convertDraftContextToDraft(DraftContext draftContext) {
        Draft draft = null;
        if (draftContext != null) {
            draft = new Draft();
            draft.setId(draftContext.getDraftId());
            draft.setName(draftContext.getDraftName());
        }
        return draft;
    }

    private List<DraftChange> processNodeChanges(ContentKey contentKey, Map<Attribute, Object> changedNode, Map<Attribute, Object> originalNode, final String userName,
            final Draft draft) {
        List<DraftChange> changes = new ArrayList<DraftChange>();

        final long timestamp = System.currentTimeMillis();

        for (Map.Entry<Attribute, Object> changeEntry : changedNode.entrySet()) {

            final String attributeName = changeEntry.getKey().getName();
            final Object attributeValue = changeEntry.getValue();

            final Attribute attributeDefinition = contentTypeInfoService.findAttributeByName(contentKey.type, attributeName).orNull();

            // pre-caution step
            if (attributeDefinition == null) {
                LOGGER.debug("[" + contentKey + "/" + attributeName + "]: SKIP no attribute definition found");
                continue;
            }

            if (ContentNodeComparatorUtil.isValueChanged(originalNode.get(attributeDefinition), changedNode.get(attributeDefinition))) {
                // prepare draft change
                final DraftChange dc = new DraftChange();

                // fill in basic attributes
                dc.setUserName(userName);
                dc.setDraft(draft);
                dc.setCreatedAt(timestamp);

                dc.setContentKey(contentKey.toString());
                dc.setAttributeName(attributeName);
                dc.setValue(serializeAttributeValue(attributeValue, attributeDefinition));

                LOGGER.debug("[" + contentKey + "/" + attributeName + "]: ~> " + dc.getValue());

                changes.add(dc);
            }
        }

        return changes;
    }

    /**
     * Serialize attribute value into string
     *
     * @param value
     *            attribute value (can be null)
     * @param definition
     *            attribute definition
     *
     * @return value serialized into string
     */
    protected String serializeAttributeValue(final Object value, final Attribute definition) {
        final String serializedValue;

        // serialize value
        if (value == null) {
            serializedValue = null;
        } else if (definition instanceof Relationship) {
            // serialize relationship value
            serializedValue = serializeRelationshipValue(value, (Relationship) definition);
        } else {
            // serialize scalar value
            serializedValue = attributeValueToStringConverter.convert(definition, value);
        }
        return serializedValue;
    }

    /**
     * Serialize relationship value into draft change
     *
     * @param value
     *            either a content key or list of content keys based on relationship type
     * @param definition
     *
     * @return serialized value of relationship attribute
     */
    @SuppressWarnings("unchecked")
    protected String serializeRelationshipValue(Object value, Relationship definition) {
        final String serializedValue;

        if (definition.getCardinality() == RelationshipCardinality.ONE) {
            serializedValue = ((ContentKey) value).toString();
        } else {
            List<ContentKey> keys = (List<ContentKey>) value;
            if (keys == null || keys.isEmpty()) {
                serializedValue = null;
            } else {
                serializedValue = JOINER.join(keys);
            }
        }
        return serializedValue;
    }
}
