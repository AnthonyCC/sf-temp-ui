/*
 * Created on Feb 4, 2005
 */
package com.freshdirect.cms.application.service;

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;

/**
 * Proxy pattern base class for all content services that proxy to another.
 * By default all methods are proxied to the underlying service (which can
 * be obtained via {@link #getProxiedService()}), subclasses should override as
 * needed.
 */
public class ProxyContentService implements ContentServiceI {

	private final ContentServiceI service;

	public ProxyContentService(ContentServiceI service) {
		if (service == null) {
			throw new IllegalArgumentException("Proxied service cannot be null");
		}
		this.service = service;
	}

	public Set getContentKeys() {
		return getProxiedService().getContentKeys();
	}

	public Set getContentKeysByType(ContentType type) {
		return getProxiedService().getContentKeysByType(type);
	}

	public Set getParentKeys(ContentKey key) {
		return getProxiedService().getParentKeys(key);
	}

	public ContentNodeI getContentNode(ContentKey key) {
		return getProxiedService().getContentNode(key);
	}

	public Map getContentNodes(Set keys) {
		return getProxiedService().getContentNodes(keys);
	}
	
	public Map queryContentNodes(ContentType type, Predicate criteria) {
		return getProxiedService().queryContentNodes(type, criteria);
	}

	public ContentNodeI createPrototypeContentNode(ContentKey key) {
		return getProxiedService().createPrototypeContentNode(key);
	}

	public CmsResponseI handle(CmsRequestI request) {
		return getProxiedService().handle(request);
	}

	public ContentTypeServiceI getTypeService() {
		return getProxiedService().getTypeService();
	}

	public String getName() {
		return getProxiedService().getName();
	}

	/**
	 * Get the underlying content service being proxied.
	 * 
	 * @return the underlying service, never null
	 */
	protected ContentServiceI getProxiedService() {
		return service;
	}
	
	public ContentServiceI getSelf() {
		return this;
	}

}