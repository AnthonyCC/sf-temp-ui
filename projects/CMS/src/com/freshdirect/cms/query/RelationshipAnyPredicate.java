package com.freshdirect.cms.query;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;

/**
 *  A predicate that performs a decision on all elements of a relationship.
 *  Thus it basically delegates predicate resulution to all members of
 *  the relationship.
 *  If any of the items in the relationship evaluates to true, the result
 *  of this predicate will be true as well.
 */
public class RelationshipAnyPredicate extends AbstractAttributePredicate {

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
	public RelationshipAnyPredicate(RelationshipDefI relationshipDef,
			                        Predicate        predicate) {
		super(relationshipDef);
		
		if (!EnumCardinality.MANY.equals(relationshipDef.getCardinality())) {
			throw new IllegalArgumentException(
					"Only cardinality many is supported");
		}
		
		this.predicate = predicate;
	}
	
	@Override
	protected boolean evaluateValue(Object v) {
		List          keys         = (List) v;
		if (keys == null) {
			return false;
		}

		for (Iterator it = keys.iterator(); it.hasNext(); ) {
			ContentKey 		 key  = (ContentKey) it.next();
			ContentNodeI     node = key.lookupContentNode();
			
			if (predicate.evaluate(node)) {
				return true;
			}
		}
		
		return false;
	}

}
