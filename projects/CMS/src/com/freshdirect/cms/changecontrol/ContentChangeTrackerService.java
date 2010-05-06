package com.freshdirect.cms.changecontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.BidirectionalRelationshipDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.ProxyContentService;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;

/**
 * Proxy content service that automatically logs changes 
 * (via a specified {@link com.freshdirect.cms.changecontrol.ChangeLogServiceI})
 * when the {@link #handle(CmsRequestI)} method is invoked.
 */
public class ContentChangeTrackerService extends ProxyContentService {

	private final ChangeLogServiceI changeLogservice;

	/**
	 * @param service the proxied content service
	 * @param changeLogService {@link ChangeLogServiceI} to update
	 */
	public ContentChangeTrackerService(ContentServiceI service, ChangeLogServiceI changeLogService) {
		super(service);
		this.changeLogservice = changeLogService;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.ContentServiceI#handle(com.freshdirect.cms.application.CmsRequestI)
	 */
	public CmsResponseI handle(CmsRequestI request) {

		Collection<ContentNodeI> reqNodes = request.getNodes();

		Set<ContentKey> keys = new HashSet<ContentKey>();
		List<ContentNodeChange> extraChanges = new ArrayList<ContentNodeChange>();
		for (Iterator<ContentNodeI> i = reqNodes.iterator(); i.hasNext();) {
			ContentNodeI node = i.next();
			keys.add(node.getKey());
			final ContentTypeDefI definition = node.getDefinition();
			// handle bi-directional references
                        for (String name : definition.getAttributeNames()) {
                            AttributeDefI attributeDef = definition.getAttributeDef(name);
                            if (attributeDef instanceof BidirectionalRelationshipDefI) {
                                BidirectionalRelationshipDefI b = (BidirectionalRelationshipDefI) attributeDef;
                                if (b.isWritableSide()) {
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
		}

		Map oldNodes = getProxiedService().getContentNodes(keys);

		ChangeSet changeSet = createChangeSet(request, oldNodes);
		for (ContentNodeChange c : extraChanges) {
		    changeSet.addChange(c);
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
	 * @param request update request
	 * @param oldNodes Map of ContentKey -> ContentNodeI
	 * @return ChangeSet (never null)
	 */
	private ChangeSet createChangeSet(CmsRequestI request, Map oldNodes) {
		ChangeSet cs = new ChangeSet();
		cs.setUserId(request.getUser().getName());
		cs.setModifiedDate(new Date());
		//TODO: how we gonna get the note here, pass it in through the request?
		cs.setNote(null);

		for (Iterator i = request.getNodes().iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			ContentNodeI oldNode = (ContentNodeI) oldNodes.get(node.getKey());
			ContentNodeChange change = compareNodes(node, oldNode);
			if (change != null) {
				cs.addChange(change);
			}

		}
		return cs;
	}

	/**
	 * Create a {@link ContentNodeChange} by comparing two nodes.
	 * 
	 * @param modifiedNode new version of node (never null)
	 * @param oldNode original version of node (may be null)
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

		Map oldAttrs = new HashMap(oldNode.getAttributes());
		Map newAttrs = modifiedNode.getAttributes();
		//String contentId = modifiedNode.getKey().getId();

		List changeDetails = new ArrayList();
		for (Iterator i = newAttrs.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			Object key = e.getKey();
			AttributeI newAttr = (AttributeI) e.getValue();
			AttributeI oldAttr = (AttributeI) oldAttrs.get(key);

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

					changeDetails.add(new ChangeDetail(newAttr.getName(), removed.isEmpty()
						? null
						: "Removed: " + keysToString(removed), added.isEmpty() ? null : "Added: " + keysToString(added)));

				}

			} else {

				if ((newValue == null && oldValue == null)
					|| (newValue != null && newValue.equals(oldValue))
					|| (oldValue != null && oldValue.equals(newValue))) {
					continue;
				}

				changeDetails.add(new ChangeDetail(newAttr.getName(), oldValue == null
					? null
					: oldValue.toString(), newValue == null ? null : newValue.toString()));

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
	 * @param keys Collection of {@link ContentKey}
	 * @return String (never null)
	 */
	private String keysToString(Collection keys) {
		StringBuffer sb = new StringBuffer();
		for (Iterator i = keys.iterator(); i.hasNext();) {
			ContentKey k = (ContentKey) i.next();
			sb.append(k.getEncoded());
			if (i.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
}