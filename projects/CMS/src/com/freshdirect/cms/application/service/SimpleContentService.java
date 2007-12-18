package com.freshdirect.cms.application.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	private Map nodesByKey = new HashMap();

	/** Map of ContentKey (child) -> Set of ContentKey (parents) */
	private Map nodeParentsByKey = new HashMap();

	public SimpleContentService(ContentTypeServiceI typeService) {
		this.typeService = typeService;
	}

	public ContentTypeServiceI getTypeService() {
		return typeService;
	}

	public ContentNodeI getContentNode(ContentKey key) {
		return (ContentNodeI) nodesByKey.get(key);
	}

	public Map getContentNodes(Set keys) {
		Map m = new HashMap(keys.size());
		for (Iterator i = keys.iterator(); i.hasNext();) {
			ContentKey key = (ContentKey) i.next();
			ContentNodeI node = getContentNode(key);
			if (node != null) {
				m.put(key, node);
			}
		}
		return m;
	}

	public Set getContentKeysByType(ContentType type) {
		Set keys = new HashSet();
		for (Iterator i = nodesByKey.keySet().iterator(); i.hasNext();) {
			ContentKey key = (ContentKey) i.next();
			if (key.getType().equals(type)) {
				keys.add(key);
			}
		}
		return keys;
	}

	public Set getContentKeys() {
		return Collections.unmodifiableSet(nodesByKey.keySet());
	}

	public Set getParentKeys(ContentKey key) {
		Set s = (Set) nodeParentsByKey.get(key);
		return s == null ? Collections.EMPTY_SET : s;
	}

	public ContentNodeI createPrototypeContentNode(ContentKey key) {
		if (!getTypeService().getContentTypes().contains(key.getType())) {
			return null;
		}
		return new ContentNode(this, key);
	}

	public CmsResponseI handle(CmsRequestI request) {
		for (Iterator i = request.getNodes().iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			nodesByKey.put(node.getKey(), node);
		}
		buildNodeIndex();
		return new CmsResponse();
	}

	protected void putNodes(Map nodes) {
		nodesByKey.putAll(nodes);
		buildNodeIndex();
	}

	private synchronized void buildNodeIndex() {
		this.nodeParentsByKey = ContentNodeUtil.getParentIndex(nodesByKey.values());
	}

}
