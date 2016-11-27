package com.freshdirect.cms.query;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentNodeI;

/**
 * {@link org.apache.commons.collections.Predicate} to check for an
 * attribute of a {@link ContentNodeI} if substring contents, in a non
 * case-sensitive manner.
 */
public class AttributeContainsNocasePredicate extends AbstractAttributePredicate {

	private final Object value;
	private final String valueString;

	public AttributeContainsNocasePredicate(AttributeDefI attributeDef, Object value) {
		super(attributeDef);
		this.value       = value;
		this.valueString = value == null
		                 ? ""
		                 : value.toString().toLowerCase();
	}

	@Override
	public boolean evaluateValue(Object v) {
		if (v == null && value == null) {
			return true;
		}
		if (v != null) {
			return v.toString().toLowerCase().indexOf(valueString) != -1;
		}
		return false;
	}

}
