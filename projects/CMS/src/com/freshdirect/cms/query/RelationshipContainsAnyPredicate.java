package com.freshdirect.cms.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.RelationshipDefI;

/**
 * {@link org.apache.commons.collections.Predicate} to check if a relationship
 * of a {@link com.freshdirect.cms.ContentNodeI} contains any of the
 * {@link com.freshdirect.cms.ContentKey}s in a set.
 */
public class RelationshipContainsAnyPredicate extends
		AbstractRelationshipPredicate {

	public RelationshipContainsAnyPredicate(RelationshipDefI relationshipDef,
			Set contentKeys) {
		super(relationshipDef, contentKeys);
	}

	public boolean evaluateValue(Object v) {
		List keys = (List) v;
		if (keys == null || keys.isEmpty()) {
			return false;
		}
		Collection searchKeys = getContentKeys();
		for (Iterator i = keys.iterator(); i.hasNext();) {
			ContentKey k = (ContentKey) i.next();
			if (searchKeys.contains(k)) {
				return true;
			}
		}
		return false;
	}

}
