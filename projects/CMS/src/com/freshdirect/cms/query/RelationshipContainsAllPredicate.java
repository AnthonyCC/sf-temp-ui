package com.freshdirect.cms.query;

import java.util.List;
import java.util.Set;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.RelationshipDefI;

/**
 * {@link org.apache.commons.collections.Predicate} to check if a relationship
 * of a {@link com.freshdirect.cms.ContentNodeI} contains all
 * {@link com.freshdirect.cms.ContentKey}s in a set.
 */
public class RelationshipContainsAllPredicate extends
		AbstractRelationshipPredicate {

	/**
	 *  Constructor.
	 *  
	 *  @param relationshipDef The relationship to check for.
	 *  @param contentKeys a Set containing ContentKey objects to check for.
	 */
	public RelationshipContainsAllPredicate(RelationshipDefI relationshipDef,
			Set contentKeys) {
		super(relationshipDef, contentKeys);
	}

	public boolean evaluate(AttributeI attribute) {
		List keys = (List) attribute.getValue();
		if (keys == null || keys.isEmpty()) {
			return false;
		}
		return keys.containsAll(getContentKeys());
	}

}
