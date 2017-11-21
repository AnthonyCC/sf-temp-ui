package com.freshdirect.storeapi.query;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.ContentNodeI;

/**
 * {@link org.apache.commons.collections.Predicate} to check for equality of an
 * attribute of a {@link ContentNodeI}.
 * This predicate can also be used to check for relationships of cardinality one.
 */
@CmsLegacy
public class AttributeEqualsPredicate extends AbstractAttributePredicate {

    private static final long serialVersionUID = 1351672369706351102L;

    private final Object value;

	/**
	 *  Constructor.
	 *
	 *  @param attributeDef an attribute definition, or a relationship definition
	 *         of cardinality one, to check for.
	 *  @param value the value to check the attribute or relationship against.
	 */
	public AttributeEqualsPredicate(Attribute attributeDef, Object value) {
		super(attributeDef);
		this.value = value;
	}

	@Override
	public boolean evaluateValue(Object v) {
		if (v == null && value == null) {
			return true;
		}
		if (value != null) {
			return value.equals(v);
		}
		return false;
	}

}
