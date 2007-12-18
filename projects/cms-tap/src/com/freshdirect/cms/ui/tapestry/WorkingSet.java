package com.freshdirect.cms.ui.tapestry;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.CmsUser;

public class WorkingSet implements Serializable {

	/** Map of ContentKey -> ContentNodeI */
	private final Map nodes = new HashMap();

	public synchronized ContentNodeI getContentNode(ContentKey key) {
		ContentNodeI node = (ContentNodeI) nodes.get(key);
		if (node == null) {
			ContentNodeI orig = CmsManager.getInstance().getContentNode(key);
			if (orig == null) {
				return null;
			}
			node = orig.copy();
			nodes.put(key, node);
		}
		return node;
	}

	public synchronized ContentNodeI createContentNode(ContentKey key) {
		ContentNodeI node = CmsManager.getInstance().createPrototypeContentNode(key);
		nodes.put(key, node);
		return node;
	}
	
	public synchronized ContentNodeI removeContentNode(ContentKey key) {
		return (ContentNodeI) nodes.remove(key);
	}
	
	public synchronized Collection getContentNodes() {
		return nodes.values();
	}

	public synchronized void clear() {
		nodes.clear();
	}
	
	public void merge(WorkingSet other) {
		nodes.putAll(other.nodes);
		other.clear();
	}

	public synchronized CmsResponseI flush(CmsUser user) {
		CmsRequestI request = new CmsRequest(user);
		for (Iterator i = nodes.values().iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			request.addNode(node);
		}
		CmsResponseI response = CmsManager.getInstance().handle(request);
		clear();
		return response;
	}

	public synchronized int size() {
		return nodes.size();
	}

}