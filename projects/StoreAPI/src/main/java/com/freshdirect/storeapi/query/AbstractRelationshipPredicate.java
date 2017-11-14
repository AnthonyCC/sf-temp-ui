package com.freshdirect.storeapi.query;

import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.storeapi.CmsLegacy;

/**
 * Abstract {@link org.apache.commons.collections.Predicate} to operate on a
 * relationship of {@link com.freshdirect.storeapi.ContentNodeI}s.
 * Only works on relationships of cardinality many. For cardinality one
 * relationships, use the {@link com.freshdirect.cms.AttributeEqualsPredicate}.
 */
@CmsLegacy
public abstract class AbstractRelationshipPredicate extends
		AbstractAttributePredicate {

    private static final long serialVersionUID = 3760094656175713889L;

    private final Set<ContentKey> contentKeys;

	public AbstractRelationshipPredicate(Relationship relationshipDef,
			Set<ContentKey> contentKeys) {
		super(relationshipDef);
		if (RelationshipCardinality.MANY != relationshipDef.getCardinality()) {
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

	public Set<ContentKey> getContentKeys() {
		return contentKeys;
	}

}
