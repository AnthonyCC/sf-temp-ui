package com.freshdirect.storeapi.query;

import java.util.Set;

import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.ContentNodeI;

/**
 * {@link org.apache.commons.collections.Predicate} that ensures the given
 * {@link com.freshdirect.storeapi.ContentNodeI} is contained in a fixed set of
 * {@link com.freshdirect.cms.ContentKey}s.
 */
@CmsLegacy
public class ContentKeysPredicate implements Predicate {

	private final Set<ContentKey> keys;

	/**
	 * @param keys Set of {@link com.freshdirect.cms.ContentKey} (never null)
	 */
	public ContentKeysPredicate(Set<ContentKey> keys) {
		this.keys = keys;
	}

	public Set<ContentKey> getKeys() {
		return keys;
	}

	@Override
    public boolean evaluate(Object obj) {
		ContentNodeI node = (ContentNodeI) obj;
		if (node == null) {
			return false;
		}
		return keys.contains(node.getKey());
	}

}
