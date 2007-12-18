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

/**
 * A proxy content service, that makes sure each node returned by the
 * content service is a copy of the original, thus mimics the un-mutable
 * nature of content node.
 */
public class CloneProxyContentService extends ProxyContentService implements ContentServiceI {

	public CloneProxyContentService(ContentServiceI service) {
		super(service);
	}

	public ContentNodeI getContentNode(ContentKey key) {
		ContentNodeI node = super.getContentNode(key);
		
		return node == null ? null : node.copy();
	}

	public Map getContentNodes(Set keys) {
		Map nodes       = super.getContentNodes(keys);
		Map copiedNodes = new HashMap();
		
		for (Iterator it = nodes.entrySet().iterator(); it.hasNext();) {
			Map.Entry    entry = (Map.Entry) it.next();
			ContentNodeI node  = (ContentNodeI) entry.getValue();
			
			copiedNodes.put(entry.getKey(), node.copy());
		}

		return copiedNodes;
	}

}