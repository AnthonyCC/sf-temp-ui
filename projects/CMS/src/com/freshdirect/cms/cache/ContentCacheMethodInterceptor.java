package com.freshdirect.cms.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.framework.cache.CacheI;
import com.freshdirect.framework.cache.ManagedCache;
import com.freshdirect.framework.cache.SimpleCache;

/**
 * AOP Alliance-style method interceptor for caching calls on a
 * {@link com.freshdirect.cms.application.ContentServiceI}.
 * Calls are intercepted and the appropriate methods of a
 * {@link com.freshdirect.framework.cache.CacheI} instance are invoked.
 * <p>
 * Also records non-existent nodes with a null-value in the cache.
 */
public class ContentCacheMethodInterceptor implements MethodInterceptor {

	/** Null-object to represent nodes that were not found. */
	final static Object NULL = new Serializable() {};

	private final CacheI cache;

	/** CacheI of ContentKey (parent) -> Set of ContentKey(children) */
	private final CacheI children = new SimpleCache();

	public ContentCacheMethodInterceptor(CacheI cache) {
		this.cache = new ManagedCache("CMS", cache, ContentCacheMethodInterceptor.NULL);
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		String method = invocation.getMethod().getName();
		if ("getContentNode".equals(method)) {
			return getContentNode(invocation);
		}
		if ("getContentNodes".equals(method)) {
			return getContentNodes(invocation);
		}
		if ("getParentKeys".equals(method)) {
			return getParentKeys(invocation);
		}
		if ("handle".equals(method)) {
			return handle(invocation);
		}
		return invocation.proceed();
	}

	private Object getParentKeys(MethodInvocation invocation) throws Throwable {
		ContentKey key = (ContentKey) invocation.getArguments()[0];

		Object obj = children.get(key);
		if (obj == null) {
			obj = invocation.proceed();
			children.put(key, obj);
		}

		return obj;
	}

	private ContentNodeI getContentNode(MethodInvocation invocation) throws Throwable {
		ContentKey key = (ContentKey) invocation.getArguments()[0];
		Object obj=null;        
        obj = getCache().get(key);
		if (obj == null) {
				obj = invocation.proceed();
				getCache().put(key, obj == null ? NULL : obj);
		}		    			        		        		
		return obj == NULL ? null : (ContentNodeI) obj;
	}

	private Map getContentNodes(MethodInvocation invocation) throws Throwable {
		Set keys = (Set) invocation.getArguments()[0];

		Map nodes = new HashMap(keys.size());
		Set missingKeys = new HashSet(keys.size());
		for (Iterator i = keys.iterator(); i.hasNext();) {
			ContentKey key = (ContentKey) i.next();
			Object obj = null;						 
		    obj = getCache().get(key);					    			        						     						
			if (obj == null) {
				missingKeys.add(key);
			} else if (obj != NULL) {
				nodes.put(key, obj);
			}
		}
		if (!missingKeys.isEmpty()) {
			
			invocation.getArguments()[0] = missingKeys;
			Map missingNodes = (Map) invocation.proceed();
			for (Iterator i = missingKeys.iterator(); i.hasNext();) {
			  ContentKey key=null;
  			  key = (ContentKey) i.next();
			  Object obj = missingNodes.get(key);				
			  getCache().put(key, obj == null ? NULL : obj);
			}
			nodes.putAll(missingNodes);						
		}
		return nodes;
	}

	private Object handle(MethodInvocation invocation) throws Throwable {
		CmsRequestI request = (CmsRequestI) invocation.getArguments()[0];
		Object response = invocation.proceed();
		for (Iterator i = request.getNodes().iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			this.getCache().remove(node.getKey());
			// invalidate dependents also
			// FIXME this is suboptimal, but fixes implicit node-creation issues
			for (Iterator j = ContentNodeUtil.getAllRelatedContentKeys(node).iterator(); j.hasNext();) {
				ContentKey k = (ContentKey) j.next();
				this.getCache().remove(k);
			}
		}
		// TODO optimize dependency cache invalidation
		children.clear();
		return response;
	}

	private CacheI getCache() {
		return cache;
	}

}
