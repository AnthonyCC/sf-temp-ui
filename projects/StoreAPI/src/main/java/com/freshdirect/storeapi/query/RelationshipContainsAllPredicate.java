package com.freshdirect.storeapi.query;

import java.util.List;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.storeapi.CmsLegacy;

/**
 * {@link org.apache.commons.collections.Predicate} to check if a relationship
 * of a {@link com.freshdirect.storeapi.ContentNodeI} contains all
 * {@link com.freshdirect.cms.ContentKey}s in a set.
 */
@CmsLegacy
public class RelationshipContainsAllPredicate extends
		AbstractRelationshipPredicate {

    private static final long serialVersionUID = 3929772082358897183L;

    /**
	 *  Constructor.
	 *
	 *  @param relationshipDef The relationship to check for.
	 *  @param contentKeys a Set containing ContentKey objects to check for.
	 */
	public RelationshipContainsAllPredicate(Relationship relationshipDef,
			Set<ContentKey> contentKeys) {
		super(relationshipDef, contentKeys);
	}

	@Override
	public boolean evaluateValue(Object v) {
		List<ContentKey> keys = (List<ContentKey>) v;
		if (keys == null || keys.isEmpty()) {
			return false;
		}
		return keys.containsAll(getContentKeys());
	}

}
