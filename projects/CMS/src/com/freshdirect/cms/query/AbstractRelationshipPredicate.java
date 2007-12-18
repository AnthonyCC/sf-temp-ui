package com.freshdirect.cms.query;

import java.util.Set;

import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;

/**
 * Abstract {@link org.apache.commons.collections.Predicate} to operate on a
 * relationship of {@link com.freshdirect.cms.ContentNodeI}s.
 * Only works on relationships of cardinality many. For cardinality one
 * relationships, use the {@link com.freshdirect.cms.AttributeEqualsPredicate}.
 */
public abstract class AbstractRelationshipPredicate extends
		AbstractAttributePredicate {

	private final Set contentKeys;

	public AbstractRelationshipPredicate(RelationshipDefI relationshipDef,
			Set contentKeys) {
		super(relationshipDef);
		if (!EnumCardinality.MANY.equals(relationshipDef.getCardinality())) {
			throw new IllegalArgumentException(
					"Only cardinality many is supported");
		}
		if (contentKeys == null) {
			throw new IllegalArgumentException("Content keys cannot be null");
		}
		if (contentKeys.isEmpty()) {
			throw new IllegalArgumentException("Content keys cannot be empty");
		}
		this.contentKeys = contentKeys;
	}

	public Set getContentKeys() {
		return contentKeys;
	}

}
