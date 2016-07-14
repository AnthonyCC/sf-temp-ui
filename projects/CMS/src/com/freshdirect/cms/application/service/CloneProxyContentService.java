/*
 * Created on Feb 4, 2005
 */
package com.freshdirect.cms.application.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;

/**
 * A proxy content service, that makes sure each node returned by the
 * content service is a copy of the original, thus mimics the un-mutable
 * nature of content node.
 */
public class CloneProxyContentService extends ProxyContentService implements ContentServiceI {

	public CloneProxyContentService(ContentServiceI service) {
		super(service);
	}

	@Override
	public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext) {
		ContentNodeI node = super.getContentNode(key, draftContext);
		
		return node == null ? null : node.copy();
	}

	@Override
	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
		Map<ContentKey, ContentNodeI> nodes       = super.getContentNodes(keys, draftContext);
		Map<ContentKey, ContentNodeI> copiedNodes = new HashMap<ContentKey, ContentNodeI>();
		
		for (Iterator<Map.Entry<ContentKey, ContentNodeI>> it = nodes.entrySet().iterator(); it.hasNext();) {
			Map.Entry<ContentKey, ContentNodeI>    entry = (Map.Entry<ContentKey, ContentNodeI>) it.next();
			ContentNodeI node  = (ContentNodeI) entry.getValue();
			
			copiedNodes.put(entry.getKey(), node.copy());
		}

		return copiedNodes;
	}

}