package com.freshdirect.cms.application.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.node.ContentNodeUtil;

/**
 * Shadows a base content service with another. Contents of the two
 * repositories are merged: the contents of the <code>mask</code>
 * service take precedence over whatever is in the <code>base</code>
 * service. Calls to {@link #handle(CmsRequestI)} are always delegated
 * to the <code>mask</code> service.
 * <p>
 * Useful for simulating changes to the base service without actually
 * applying them.
 * 
 * @see com.freshdirect.cms.validation.ValidatingContentService
 */
public class MaskContentService extends AbstractContentService implements ContentServiceI {

	private final ContentServiceI base;
	private final ContentServiceI mask;

	public MaskContentService(ContentServiceI base, ContentServiceI mask) {
		this.base = base;
		this.mask = mask;
	}

	public ContentTypeServiceI getTypeService() {
		return mask.getTypeService();
	}

	public Set<ContentKey> getContentKeys() {
		Set<ContentKey> s = new HashSet<ContentKey>();
		s.addAll(base.getContentKeys());
		s.addAll(mask.getContentKeys());
		return s;
	}

	public Set<ContentKey> getContentKeysByType(ContentType type) {
		Set<ContentKey> s = new HashSet<ContentKey>();
		s.addAll(base.getContentKeysByType(type));
		s.addAll(mask.getContentKeysByType(type));
		return s;
	}

	public Set<ContentKey> getParentKeys(ContentKey key) {
		// get all potential parents from both and recalculate based on masked node data

		Set<ContentKey> s = new HashSet<ContentKey>();
		s.addAll(base.getParentKeys(key));
		s.addAll(mask.getParentKeys(key));

		Map parentIndex = ContentNodeUtil.getParentIndex(getContentNodes(s).values());

		Set parentKeys = (Set) parentIndex.get(key);
		return parentKeys == null ? Collections.EMPTY_SET : parentKeys;
	}

	public ContentNodeI getContentNode(ContentKey key) {
		ContentNodeI n = mask.getContentNode(key);
		if (n == null) {
			n = base.getContentNode(key);
		}
		return n;
	}

	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys) {
		Map<ContentKey, ContentNodeI> m = new HashMap<ContentKey, ContentNodeI>(keys.size());
		m.putAll(base.getContentNodes(keys));
		m.putAll(mask.getContentNodes(keys));
		return m;
	}

	public ContentNodeI createPrototypeContentNode(ContentKey key) {
		return base.createPrototypeContentNode(key);
	}

	public CmsResponseI handle(CmsRequestI request) {
		return mask.handle(request);
	}

}
