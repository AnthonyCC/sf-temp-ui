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
import com.freshdirect.cms.application.DraftContext;
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
	
	@Override
	public ContentTypeServiceI getTypeService() {
		return mask.getTypeService();
	}

	@Override
	public Set<ContentKey> getContentKeys(DraftContext draftContext) {
		Set<ContentKey> s = new HashSet<ContentKey>();
		s.addAll(base.getContentKeys( draftContext ));
		s.addAll(mask.getContentKeys( draftContext ));
		return s;
	}

	@Override
	public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext) {
		Set<ContentKey> s = new HashSet<ContentKey>();
		s.addAll(base.getContentKeysByType(type, draftContext));
		s.addAll(mask.getContentKeysByType(type, draftContext));
		return s;
	}

	@Override
	public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext) {
		// get all potential parents from both and recalculate based on masked node data

		Set<ContentKey> s = new HashSet<ContentKey>();
		s.addAll(base.getParentKeys(key, draftContext));
		s.addAll(mask.getParentKeys(key, draftContext));

		Map<ContentKey, Set<ContentKey>> parentIndex = ContentNodeUtil.getParentIndex(getContentNodes(s, draftContext).values());

		Set<ContentKey> parentKeys = parentIndex.get(key);
		return parentKeys == null ? Collections.EMPTY_SET : parentKeys;
	}

	@Override
	public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext) {
		ContentNodeI n = mask.getContentNode(key, draftContext);
		if (n == null) {
			n = base.getContentNode(key, draftContext);
		}
		return n;
	}

	@Override
	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
		Map<ContentKey, ContentNodeI> m = new HashMap<ContentKey, ContentNodeI>(keys.size());
		m.putAll(base.getContentNodes(keys, draftContext));
		m.putAll(mask.getContentNodes(keys, draftContext));
		return m;
	}

	@Override
	public ContentNodeI createPrototypeContentNode(ContentKey key, DraftContext draftContext) {
		return base.createPrototypeContentNode(key, draftContext);
	}

	@Override
	public CmsResponseI handle(CmsRequestI request) {
		return mask.handle(request);
	}

	@Override
	public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext) {
		ContentNodeI n = mask.getRealContentNode(key, draftContext);
		if (n == null) {
			n = base.getRealContentNode(key, draftContext);
		}
		return n;
	}

}
