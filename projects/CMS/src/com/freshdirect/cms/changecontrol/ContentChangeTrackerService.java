package com.freshdirect.cms.changecontrol;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.BidirectionalRelationshipDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsRequestI.Source;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.ProxyContentService;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;

/**
 * Proxy content service that automatically logs changes (via a specified {@link com.freshdirect.cms.changecontrol.ChangeLogServiceI}) when the {@link #handle(CmsRequestI)} method
 * is invoked.
 */
public class ContentChangeTrackerService extends ProxyContentService {

    private final ChangeLogServiceI changeLogservice;

    /**
     * @param service
     *            the proxied content service
     * @param changeLogService
     *            {@link ChangeLogServiceI} to update
     */
    public ContentChangeTrackerService(ContentServiceI service, ChangeLogServiceI changeLogService) {
        super(service);
        this.changeLogservice = changeLogService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.freshdirect.cms.application.ContentServiceI#handle(com.freshdirect.cms.application.CmsRequestI)
     */
    @Override
    public CmsResponseI handle(CmsRequestI request) {

        if (DraftContext.MAIN != request.getDraftContext()) {
            // do not track changes in draft mode
            return getProxiedService().handle(request);
        }

        Collection<ContentNodeI> reqNodes = request.getNodes();

        Set<ContentKey> keys = new HashSet<ContentKey>();
        List<ContentNodeChange> extraChanges = new ArrayList<ContentNodeChange>();
        for (ContentNodeI node : reqNodes) {
            keys.add(node.getKey());
            final ContentTypeDefI definition = node.getDefinition();
            // handle bi-directional references
            for (String name : definition.getAttributeNames()) {
                AttributeDefI attributeDef = definition.getAttributeDef(name);
                if (attributeDef instanceof BidirectionalRelationshipDefI) {
                    // get the new referant
                    ContentKey refs = (ContentKey) node.getAttributeValue(name);
                    // get the old refering node

                    BidirectionalReferenceHandler handler = getProxiedService().getTypeService().getReferenceHandler(definition.getType(), name);
                    ContentKey key = handler.getInverseReference(refs);
                    if (key != null && !node.getKey().equals(key)) {
                        ContentNodeChange nodeChange = new ContentNodeChange();
                        nodeChange.setChangeType(EnumContentNodeChangeType.MODIFY);
                        nodeChange.setContentKey(key);
                        nodeChange.addDetail(new ChangeDetail(name, refs.toString(), null));

                        extraChanges.add(nodeChange);
                    }
                }
            }
        }

        Map<ContentKey, ContentNodeI> oldNodes = getProxiedService().getContentNodes(keys, request.getDraftContext());

        ChangeSet changeSet = createContentNodeChangeSet(request, oldNodes);
        for (ContentNodeChange extraChange : extraChanges) {
            changeSet.addChange(extraChange);
        }

        CmsResponseI response = getProxiedService().handle(request);

        if (changeSet.getNodeChanges().size() > 0) {
            PrimaryKey changeSetId = changeLogservice.storeChangeSet(changeSet);
            response.setChangeSetId(changeSetId);
        }

        return response;
    }

    /**
     * Creates a change set based on a {@link CmsRequestI}.
     * 
     * @param request
     *            update request
     * @param oldNodes
     *            Map of ContentKey -> ContentNodeI
     * @return ChangeSet (never null)
     */
    private ChangeSet createContentNodeChangeSet(CmsRequestI request, Map<ContentKey, ContentNodeI> oldNodes) {
        ChangeSet changeSet = new ChangeSet();
        changeSet.setUserId(request.getUser().getName());
        changeSet.setModifiedDate(new Date());
        if (Source.MERGE == request.getSource()) {
            changeSet.setNote(MessageFormat.format("Merged from {0} draft branch.", ((CmsUser)request.getUser()).getDraftContext().getDraftName()));
        } else {
            // TODO: how we gonna get the note here, pass it in through the request?
            changeSet.setNote(null);
        }

        for (ContentNodeI node : request.getNodes()) {
            ContentNodeI oldNode = oldNodes.get(node.getKey());
            ContentNodeChange change = compareNodes(node, oldNode);
            if (change != null) {
                changeSet.addChange(change);
            }
        }
        return changeSet;
    }

    /**
     * Create a {@link ContentNodeChange} by comparing two nodes.
     * 
     * @param modifiedNode
     *            new version of node (never null)
     * @param oldNode
     *            original version of node (may be null)
     * 
     * @return details of the changes, null if nodes are identical
     */
    private ContentNodeChange compareNodes(ContentNodeI modifiedNode, ContentNodeI oldNode) {

        if (oldNode == null) {
            ContentNodeChange nodeChange = new ContentNodeChange();
            nodeChange.setChangeType(EnumContentNodeChangeType.ADD);
            nodeChange.setContentKey(modifiedNode.getKey());
            return nodeChange;
        }

        Map<String, AttributeI> oldAttrs = new HashMap<String, AttributeI>(oldNode.getAttributes());
        Map<String, AttributeI> newAttrs = modifiedNode.getAttributes();

        List<ChangeDetail> changeDetails = new ArrayList<ChangeDetail>();
        for (Entry<String, AttributeI> entry : newAttrs.entrySet()) {
            String key = entry.getKey();
            AttributeI newAttr = entry.getValue();
            AttributeI oldAttr = oldAttrs.get(key);

            Object newValue = newAttr.getValue();
            Object oldValue = oldAttr == null ? null : oldAttr.getValue();

            if (EnumCardinality.MANY.equals(newAttr.getDefinition().getCardinality())) {
                List oldList = (List) NVL.apply(oldValue, Collections.EMPTY_LIST);
                List newList = (List) NVL.apply(newValue, Collections.EMPTY_LIST);

                if (!oldList.equals(newList)) {
                    Set removed = new HashSet(oldList);
                    removed.removeAll(newList);
                    Set added = new HashSet(newList);
                    added.removeAll(oldList);

                    changeDetails.add(new ChangeDetail(newAttr.getName(), removed.isEmpty() ? null : "Removed: " + keysToString(removed),
                            added.isEmpty() ? null : "Added: " + keysToString(added)));

                }

            } else {

                if ((newValue == null && oldValue == null) || (newValue != null && newValue.equals(oldValue)) || (oldValue != null && oldValue.equals(newValue))) {
                    continue;
                }

                changeDetails.add(new ChangeDetail(newAttr.getName(), oldValue == null ? null : oldValue.toString(), newValue == null ? null : newValue.toString()));

            }

        }

        ContentNodeChange nodeChange = null;
        if (changeDetails.size() > 0) {
            nodeChange = new ContentNodeChange();
            nodeChange.setChangeType(EnumContentNodeChangeType.MODIFY);
            nodeChange.setChangeDetails(changeDetails);
            nodeChange.setContentKey(modifiedNode.getKey());
        }

        return nodeChange;
    }

    /**
     * Get content keys encoded as a comma separated list.
     * 
     * @param keys
     *            Collection of {@link ContentKey}
     * @return String (never null)
     */
    private String keysToString(Collection<ContentKey> keys) {
        StringBuilder contentKeys = new StringBuilder();
        for (Iterator<ContentKey> i = keys.iterator(); i.hasNext();) {
            ContentKey k = i.next();
            contentKeys.append(k.getEncoded());
            if (i.hasNext()) {
                contentKeys.append(", ");
            }
        }
        return contentKeys.toString();
    }

}
