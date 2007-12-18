package com.freshdirect.cms.ui.tapestry;

import java.io.Serializable;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.framework.util.LruCache;

public class CmsVisit implements Serializable {

	private CmsUser user;
	private final LruCache workingSets = new LruCache(3);

	public WorkingSet getWorkingSet(ContentKey key) {
		WorkingSet workingSet = (WorkingSet) workingSets.get(key);
		if (workingSet == null) {
			workingSet = new WorkingSet();

			ContentNodeUtil.eagerFetch(key, false);

			workingSets.put(key, workingSet);
		}
		return workingSet;
	}

	public void setUser(CmsUser user) {
		this.user = user;
	}

	public CmsUser getUser() {
		return user;
	}

}