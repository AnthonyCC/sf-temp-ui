package com.freshdirect.cms.query;

import java.util.Set;

import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.ContentNodeI;

/**
 * {@link org.apache.commons.collections.Predicate} that ensures the given
 * {@link com.freshdirect.cms.ContentNodeI} is contained in a fixed set of
 * {@link com.freshdirect.cms.ContentKey}s.
 */
public class ContentKeysPredicate implements Predicate {

	private final Set keys;

	/**
	 * @param keys Set of {@link com.freshdirect.cms.ContentKey} (never null)
	 */
	public ContentKeysPredicate(Set keys) {
		this.keys = keys;
	}

	public Set getKeys() {
		return keys;
	}
	
	public boolean evaluate(Object obj) {
		ContentNodeI node = (ContentNodeI) obj;
		if (node == null) {
			return false;
		}
		return keys.contains(node.getKey());
	}

}
