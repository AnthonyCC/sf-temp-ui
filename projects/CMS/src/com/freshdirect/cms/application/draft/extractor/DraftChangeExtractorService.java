package com.freshdirect.cms.application.draft.extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.draftchange.converter.DraftChangeToContentNodeApplicator;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.node.ChangedContentNode;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public final class DraftChangeExtractorService {

    private static Logger LOGGER = LoggerFactory.getInstance(DraftChangeExtractorService.class);

    private static DraftChangeExtractorService instance;

    private DraftChangeExtractorService() {
    }

    public static DraftChangeExtractorService defaultService() {
        if (instance == null) {
            synchronized (DraftChangeExtractorService.class) {
                if (instance == null) {
                    instance = new DraftChangeExtractorService();
                }
            }
        }
        return instance;
    }

    /**
     * This method picks changed nodes from a CMS change request and transforms them to draft change objects
     * 
     * @param request
     * @return list of {@link DraftChange} items
     */
    public List<DraftChange> extractChangesFromRequest(CmsRequestI request) {
        if (request == null || request.getUser() == null) {
            LOGGER.error("Invalid parameters");
            return Collections.emptyList();
        }

        final String userName = request.getUser().getName();
        final Draft draft = convertDraftContextToDraft(request.getDraftContext());

        return extractChanges(request.getNodes(), userName, draft);
    }

    /**
     * Transform DraftContext into Draft
     * 
     * @param dctx Draft Context instance
     * @return converted Draft
     */
    private Draft convertDraftContextToDraft(DraftContext dctx) {
        Draft draft = null;
        if (dctx != null) {
            draft = new Draft();
            draft.setId(dctx.getDraftId());
            // although this is not necessary, do it
            draft.setName(dctx.getDraftName());
        }
        return draft;
    }

    /**
     * Find changed nodes in the input collection and extract values of changed attributes into a set of draft change objects
     * 
     * @param nodes
     * @return
     */
    public List<DraftChange> extractChanges(Collection<ContentNodeI> nodes, final String userName, final Draft draft) {
        if (nodes == null || nodes.isEmpty()) {
            return Collections.emptyList();
        }

        List<DraftChange> changes = new ArrayList<DraftChange>();

        for (ContentNodeI node : nodes) {
            if (node instanceof ChangedContentNode) {
                final ChangedContentNode changedNode = (ChangedContentNode) node;

                if (changedNode.isChanged()) {

                    List<DraftChange> list = processNodeChanges(changedNode, userName, draft);
                    if (list != null && !list.isEmpty()) {
                        changes.addAll(list);
                    }
                }

            }
        }

        return changes;
    }

    private List<DraftChange> processNodeChanges(final ChangedContentNode changedNode, final String userName, final Draft draft) {
        List<DraftChange> changes = new ArrayList<DraftChange>();

        final ContentTypeDefI typeDef = changedNode.getDefinition();
        final String contentKey = changedNode.getKey().getEncoded();
        final long timestamp = System.currentTimeMillis();

        for (Map.Entry<String, Object> changeEntry : changedNode.getChanges().entrySet()) {

            final String attrName = changeEntry.getKey();
            final Object attrValue = changeEntry.getValue();

            final AttributeDefI attrDef = typeDef.getAttributeDef(attrName);

            // pre-caution step
            if (attrDef == null) {
                LOGGER.debug("[" + contentKey + "/" + attrName + "]: SKIP no attribute definition found");
                continue;
            } else if (attrDef instanceof RelationshipDefI && ((RelationshipDefI) attrDef).isCalculated()) {
                LOGGER.debug("[" + contentKey + "/" + attrName + "]: SKIP computed relationship");
                continue;
            }

            // prepare draft change
            final DraftChange dc = new DraftChange();

            // fill in basic attributes
            dc.setUserName(userName);
            dc.setDraft(draft);
            dc.setCreatedAt(timestamp);

            dc.setContentKey(contentKey);
            dc.setAttributeName(attrName);
            dc.setValue(serializeAttributeValue(attrValue, attrDef));

            LOGGER.debug("[" + contentKey + "/" + attrName + "]: ~> " + dc.getValue());

            changes.add(dc);
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
    protected String serializeAttributeValue(final Object value, final AttributeDefI definition) {
        final String serializedValue;

        // serialize value
        if (value == null) {

            serializedValue = null;

        } else if (definition instanceof RelationshipDefI) {
            // serialize relationship value

            serializedValue = serializeRelationshipValue(value, (RelationshipDefI) definition);

        } else {
            // serialize scalar value

            serializedValue = ContentTypeUtil.attributeToString(definition, value);
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
    protected String serializeRelationshipValue(Object value, RelationshipDefI definition) {
        final String serializedValue;

        if (definition.isCardinalityOne()) {

            serializedValue = ((ContentKey) value).getEncoded();

        } else {
            List<ContentKey> keys = (List<ContentKey>) value;
            if (keys == null || keys.isEmpty()) {

                serializedValue = null;

            } else {
                Collection<String> serializedKeys = new ArrayList<String>(keys.size());
                for (ContentKey k : keys) {
                    serializedKeys.add(k.getEncoded());
                }

                serializedValue = StringUtil.join(serializedKeys, DraftChangeToContentNodeApplicator.SEPARATOR);
            }
        }

        return serializedValue;
    }
}
