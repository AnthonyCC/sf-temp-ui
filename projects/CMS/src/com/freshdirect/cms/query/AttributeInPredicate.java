package com.freshdirect.cms.query;

import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;

/**
 * {@link org.apache.commons.collections.Predicate} to check for an
 * attribute to be a member of a specified set of {@link ContentNodeI} object..
 * This predicate can also be used to check for relationships of cardinality one.
 */
public class AttributeInPredicate extends AbstractAttributePredicate {

	private final Set values;

	/**
	 *  Constructor.
	 *  
	 *  @param attributeDef an attribute definition, or a relationship definition
	 *         of cardinality one, to check for.
	 *  @param values the set of values to check each attribute againts.
	 *         the predicate will be true for attributes contained in this set.
	 */
	public AttributeInPredicate(AttributeDefI attributeDef, Set values) {
		super(attributeDef);
		this.values = values;
	}

	public boolean evaluate(AttributeI attribute) {
		Object v = attribute.getValue();
		if (v == null && values == null) {
			return true;
		}
		if (values != null) {
			return values.contains(v);
		}
		return false;
	}

}
