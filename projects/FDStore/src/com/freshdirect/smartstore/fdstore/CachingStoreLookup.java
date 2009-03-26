/**
 * 
 */
package com.freshdirect.smartstore.fdstore;

import com.freshdirect.fdstore.content.ContentNodeModel;

public abstract class CachingStoreLookup implements StoreLookup {
	OnlineScoreCache cache;
	
	public CachingStoreLookup(OnlineScoreCache cache) {
		this.cache = cache;
	}

	public double getVariable(ContentNodeModel contentNode) {
		return cache.getVariable(contentNode);
	}

	public void reloadCache() {
		cache.reload();
	}
}