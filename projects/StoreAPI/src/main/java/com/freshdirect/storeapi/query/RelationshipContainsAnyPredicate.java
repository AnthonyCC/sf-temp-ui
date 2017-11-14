package com.freshdirect.storeapi.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.storeapi.CmsLegacy;

/**
 * {@link org.apache.commons.collections.Predicate} to check if a relationship
 * of a {@link com.freshdirect.storeapi.ContentNodeI} contains any of the
 * {@link com.freshdirect.cms.ContentKey}s in a set.
 */
@CmsLegacy
public class RelationshipContainsAnyPredicate extends
		AbstractRelationshipPredicate {

    private static final long serialVersionUID = 2043039615042293593L;

    public RelationshipContainsAnyPredicate(Relationship relationshipDef,
			Set<ContentKey> contentKeys) {
		super(relationshipDef, contentKeys);
	}

	@Override
    public boolean evaluateValue(Object v) {
		List<ContentKey> keys = (List<ContentKey>) v;
		if (keys == null || keys.isEmpty()) {
			return false;
		}
		Collection<ContentKey> searchKeys = getContentKeys();
		for (Iterator<ContentKey> i = keys.iterator(); i.hasNext();) {
			ContentKey k = i.next();
			if (searchKeys.contains(k)) {
				return true;
			}
		}
		return false;
	}

}
