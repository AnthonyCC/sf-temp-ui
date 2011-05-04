package com.freshdirect.cms.application;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;

/**
 * Simple implementation of {@link com.freshdirect.cms.application.CmsRequestI}.
 * 
 * @TODO we don't really need interface/implementation separation here
 */
public class CmsRequest implements CmsRequestI {

	private UserI user;
	private Map<ContentKey, ContentNodeI> nodes = new HashMap<ContentKey, ContentNodeI>();
	private boolean bulk = false;

	public CmsRequest(UserI user) {
		this.user = user;
	}

        public CmsRequest(UserI user, boolean bulk) {
            this.user = user;
            this.bulk = bulk;
        }

	public void setNodes(Map<ContentKey, ContentNodeI> nodes) {
            this.nodes = nodes;
        }

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.CmsRequestI#getUser()
	 */
	public UserI getUser() {
		return user;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.CmsRequestI#addNode(com.freshdirect.cms.ContentNodeI)
	 */
	public void addNode(ContentNodeI node) {
		nodes.put(node.getKey(), node);
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.CmsRequestI#getNodes()
	 */
	public Collection<ContentNodeI> getNodes() {
		return nodes.values();
	}
	
	@Override
	public Map<ContentKey, ContentNodeI> getNodeMap() {
	return nodes;
	}

	public String toString() {
		return "CmsRequest[" + user + ", " + nodes + "]";
	}
	
	@Override
	public boolean isBulk() {
	    return bulk;
	}

}