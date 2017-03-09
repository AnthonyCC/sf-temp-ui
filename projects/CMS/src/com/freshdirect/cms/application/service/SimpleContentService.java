package com.freshdirect.cms.application.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponse;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.node.ContentNode;
import com.freshdirect.cms.node.ContentNodeUtil;

/**
 * Simple in-memory {@link com.freshdirect.cms.application.ContentServiceI}
 * implementation. Maintains a Map of nodes and an index of navigable
 * child-parent relationships for efficient lookups.
 * 
 * @FIXME not quite thread safe
 */
public class SimpleContentService extends AbstractContentService implements ContentServiceI {

	private final ContentTypeServiceI typeService;

	/** Map of ContentKey -> ContentNodeI */
	private Map<ContentKey,ContentNodeI> nodesByKey = new HashMap<ContentKey,ContentNodeI>();

	/** Map of ContentKey (child) -> Set of ContentKey (parents) */
	private Map<ContentKey,Set<ContentKey>> nodeParentsByKey = new HashMap<ContentKey,Set<ContentKey>>();

	public SimpleContentService(ContentTypeServiceI typeService) {
		this.typeService = typeService;
	}

    public SimpleContentService(ContentTypeServiceI typeService, Collection<ContentNodeI> nodes) {
        this.typeService = typeService;
        
        for (ContentNodeI node : nodes) {
            nodesByKey.put(node.getKey(), node);
        }
        buildNodeIndex();
    }

    @Override
	public ContentTypeServiceI getTypeService() {
		return typeService;
	}

	@Override
	public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext) {
		return nodesByKey.get(key);
	}

	@Override
	public Map<ContentKey,ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
		Map<ContentKey,ContentNodeI> m = new HashMap<ContentKey,ContentNodeI>(keys.size());
		for (ContentKey key : keys) {
			ContentNodeI node = getContentNode(key, draftContext);
			if (node != null) {
				m.put(key, node);
			}
		}
		return m;
	}

	@Override
	public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext) {
		Set<ContentKey> keys = new HashSet<ContentKey>();
		for (ContentKey key : nodesByKey.keySet()) {
			if (key.getType().equals(type)) {
				keys.add(key);
			}
		}
		return keys;
	}

	@Override
	public Set<ContentKey> getContentKeys(DraftContext draftContext) {
		return Collections.unmodifiableSet(nodesByKey.keySet());
	}

	@Override
	public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext) {
		Set<ContentKey> s = nodeParentsByKey.get(key);
		return s == null ? Collections.EMPTY_SET : s;
	}

	@Override
	public ContentNodeI createPrototypeContentNode(ContentKey key, DraftContext draftContext) {
		if (!getTypeService().getContentTypes().contains(key.getType())) {
			return null;
		}
		return new ContentNode(this, draftContext, key);
	}

	@Override
	public CmsResponseI handle(CmsRequestI request) {
		for (ContentNodeI node : request.getNodes()) {
			nodesByKey.put(node.getKey(), node);
		}
		buildNodeIndex();
		return new CmsResponse();
	}

	protected void putNodes(Map<ContentKey,ContentNodeI> nodes) {
		nodesByKey.putAll(nodes);
		buildNodeIndex();
	}

	private synchronized void buildNodeIndex() {
		this.nodeParentsByKey = ContentNodeUtil.getParentIndex(nodesByKey.values());
	}

	@Override
	public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext) {
		return getContentNode(key, draftContext);
	}

}
