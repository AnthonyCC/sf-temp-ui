package com.freshdirect.storeapi.query;

import java.util.List;

import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;

/**
 *  A predicate that performs a decision on all elements of a relationship.
 *  Thus it basically delegates predicate resolution to all members of
 *  the relationship.
 *  If any of the items in the relationship evaluates to true, the result
 *  of this predicate will be true as well.
 */
@CmsLegacy
public class RelationshipAnyPredicate extends AbstractAttributePredicate {

    private static final long serialVersionUID = -3235850319662364026L;

    /**
	 *  The predicate to evaluate on all of the relationship members.
	 */
	private Predicate predicate;

	/**
	 *  Constructor.
	 *
	 *  @param relationshipDef the relationship to traverse, and evaluate the
	 *         supplied predicate for.
	 *  @param predicate the predicate to evaluate on each item in the
	 *         relationship.
	 */
	public RelationshipAnyPredicate(Relationship relationshipDef,
			                        Predicate        predicate) {
		super(relationshipDef);

		if (RelationshipCardinality.MANY != relationshipDef.getCardinality()) {
			throw new IllegalArgumentException(
					"Only cardinality many is supported");
		}

		this.predicate = predicate;
	}

	@Override
	protected boolean evaluateValue(Object v) {
	    final CmsManager contentService = CmsManager.getInstance();

		List<ContentKey>          keys         = (List<ContentKey>) v;
		if (keys == null || keys.isEmpty()) {
			return false;
		}

		for (ContentKey key : keys) {
            ContentNodeI node = contentService.getContentNode(key);

			if (predicate.evaluate(node)) {
				return true;
			}
		}

		return false;
	}

}
