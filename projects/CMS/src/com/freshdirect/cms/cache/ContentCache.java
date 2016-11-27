package com.freshdirect.cms.cache;

import org.aopalliance.intercept.MethodInterceptor;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.framework.cache.CacheI;

public interface ContentCache extends MethodInterceptor {

	CacheI<ContentKey, Object> getCache();

	void clearChildrenCache();
}
