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
import com.freshdirect.cms.application.DraftContext;

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

	@Override
	public Set<ContentKey> getContentKeys(DraftContext draftContext) {
		return getProxiedService().getContentKeys(draftContext);
	}

	@Override
	public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext) {
		return getProxiedService().getContentKeysByType(type, draftContext);
	}

	@Override
	public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext) {
		return getProxiedService().getParentKeys(key, draftContext);
	}

	@Override
	public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext) {
		return getProxiedService().getContentNode(key, draftContext);
	}

	@Override
	public Map<ContentKey,ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
		return getProxiedService().getContentNodes(keys, draftContext);
	}
	
	@Override
	public Map<ContentKey, ContentNodeI> queryContentNodes(ContentType type, Predicate criteria, DraftContext draftContext) {
		return getProxiedService().queryContentNodes(type, criteria, draftContext);
	}

	@Override
	public ContentNodeI createPrototypeContentNode(ContentKey key, DraftContext draftContext) {
		return getProxiedService().createPrototypeContentNode(key, draftContext);
	}

	@Override
	public CmsResponseI handle(CmsRequestI request) {
		return getProxiedService().handle(request);
	}

	@Override
	public ContentTypeServiceI getTypeService() {
		return getProxiedService().getTypeService();
	}

	@Override
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

	@Override
	public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext) {
		return getProxiedService().getRealContentNode(key, draftContext);
	}
	
}