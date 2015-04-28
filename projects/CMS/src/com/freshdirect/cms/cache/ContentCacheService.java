package com.freshdirect.cms.cache;

import java.util.Collection;
import java.util.Iterator;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.framework.conf.FDRegistry;

public class ContentCacheService {

	private static final ContentCacheService INSTANCE = new ContentCacheService();
	private final ContentCache contentCacheInterceptor;
	
	private ContentCacheService() {
		contentCacheInterceptor = (ContentCache) FDRegistry.getInstance().getService("com.freshdirect.cms.StoreCacheInterceptor", ContentCache.class);
	}
	
	public static ContentCacheService defaultService() {
		return INSTANCE;
	}
	
	public void invalidateContentNode(Collection<ContentNodeI> nodes) {
		for (Iterator<ContentNodeI> contentNodeIterator = nodes.iterator(); contentNodeIterator.hasNext();) {
			ContentNodeI node = contentNodeIterator.next();
			contentCacheInterceptor.getCache().remove(node.getKey());
			// invalidate dependents also
			// FIXME this is suboptimal, but fixes implicit node-creation issues
			for (Iterator<ContentKey> contentKeyIterator = ContentNodeUtil.getAllRelatedContentKeys(node).iterator(); contentKeyIterator.hasNext();) {
				ContentKey k = contentKeyIterator.next();
				contentCacheInterceptor.getCache().remove(k);
			}
		}
		contentCacheInterceptor.clearChildrenCache();
	}
}
