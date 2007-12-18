package com.freshdirect.cms.application;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentNodeI;

/**
 * Simple implementation of {@link com.freshdirect.cms.application.CmsRequestI}.
 * 
 * @TODO we don't really need interface/implementation separation here
 */
public class CmsRequest implements CmsRequestI {

	private UserI user;
	private List nodes = new ArrayList();

	public CmsRequest(UserI user) {
		this.user = user;
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
		nodes.add(node);
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.application.CmsRequestI#getNodes()
	 */
	public List getNodes() {
		return nodes;
	}

	public String toString() {
		return "CmsRequest[" + user + ", " + nodes + "]";
	}

}