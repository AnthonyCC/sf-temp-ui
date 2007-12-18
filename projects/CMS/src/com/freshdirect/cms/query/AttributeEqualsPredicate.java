package com.freshdirect.cms.query;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;

/**
 * {@link org.apache.commons.collections.Predicate} to check for equality of an
 * attribute of a {@link ContentNodeI}.
 * This predicate can also be used to check for relationships of cardinality one.
 */
public class AttributeEqualsPredicate extends AbstractAttributePredicate {

	private final Object value;

	/**
	 *  Constructor.
	 *  
	 *  @param attributeDef an attribute definition, or a relationship definition
	 *         of cardinality one, to check for.
	 *  @param value the value to check the attribute or relationship against.
	 */
	public AttributeEqualsPredicate(AttributeDefI attributeDef, Object value) {
		super(attributeDef);
		this.value = value;
	}

	public boolean evaluate(AttributeI attribute) {
		Object v = attribute.getValue();
		if (v == null && value == null) {
			return true;
		}
		if (value != null) {
			return value.equals(v);
		}
		return false;
	}

}
